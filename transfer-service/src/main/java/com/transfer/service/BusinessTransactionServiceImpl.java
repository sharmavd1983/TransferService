package com.transfer.service;

import static com.transfer.constants.TransferConstants.PAYEE_ACCOUNT;
import static com.transfer.constants.TransferConstants.PAYER_ACCOUNT;
import static com.transfer.constants.TransferConstants.TRANSFER_AMOUNT;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.transfer.domain.Account;
import com.transfer.domain.AccountEntry;
import com.transfer.domain.AccountTransaction;
import com.transfer.domain.AccountTransaction.TransactionStatus;
import com.transfer.domain.BusinessTransaction;
import com.transfer.domain.BusinessTransaction.BusinessTransactionStatus;
import com.transfer.domain.ClientAccount;
import com.transfer.domain.ProposedTransaction;
import com.transfer.domain.Reservation;
import com.transfer.domain.StatementLineDetail;
import com.transfer.domain.StatementLineDetail.StatementStatus;
import com.transfer.repository.BusinessTransactionRepository;
import com.transfer.repository.StatementLineDetailRepository;

@Component
public class BusinessTransactionServiceImpl implements BusinessTransactionService {

	Logger LOGGER = LoggerFactory.getLogger(BusinessTransactionService.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	private BusinessTransactionRepository businessTransactionRepository;

	@Autowired
	private StatementLineDetailRepository statementLineDetailRepository;

	public BusinessTransaction createBusinessTransaction(final String transactionType, final String payerClientCode,
			final String payeeClientCode, final BigDecimal amount) {
		
		LOGGER.info("Create business transaction for any financtial money movement.");
		
		final BusinessTransaction bussTrans = new BusinessTransaction();
		bussTrans.setBusinessTransactionCode(transactionType);
		bussTrans.setStatus(BusinessTransactionStatus.TRANSACTION_PENDING);
		bussTrans.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		bussTrans.setAccountUserClientCode(payerClientCode);

		bussTrans.addBusinessTransactionData(PAYER_ACCOUNT, payerClientCode);
		bussTrans.addBusinessTransactionData(PAYEE_ACCOUNT, payeeClientCode);
		bussTrans.addBusinessTransactionData(TRANSFER_AMOUNT, String.valueOf(amount.longValue()));

		return bussTrans;
	}

	public void proposeTransaction(final long payerAccountId, final long payeeAccountId, final BigDecimal amount,
			final String transactionType, final BusinessTransaction businessTransaction) {

		
		final ProposedTransaction proposedTransaction = new ProposedTransaction();
		proposedTransaction.setAccountTransactionType(transactionType);
		proposedTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		proposedTransaction.setAccountToBeDebited(payerAccountId);
		proposedTransaction.setAccountToBeCredited(payeeAccountId);
		proposedTransaction.setAmount(amount);
		proposedTransaction.setStatus(BusinessTransactionStatus.TRANSACTION_PENDING);
		businessTransaction.addProposedTransaction(proposedTransaction);
	}

	public void reserveFunds(final Account debitAccount, final Account creditAccount,
			final BusinessTransaction businessTransaction) {

		// For debitting of amount, they should go as -ve in reservation table.
		// The only credit amounts of any client account should go positive in
		// reservation table.
		for (final ProposedTransaction proposedTransaction : businessTransaction.getProposedTransactionCollection()) {
			if (debitAccount instanceof ClientAccount) {
				final Reservation reserveForDebit = this.reserve(debitAccount,
						proposedTransaction.getAmount().negate());
				businessTransaction.addReservation(reserveForDebit);
			}

			if (creditAccount instanceof ClientAccount) {
				final Reservation reserveForCredit = this.reserve(creditAccount, proposedTransaction.getAmount());
				businessTransaction.addReservation(reserveForCredit);
			}

		}
	}

	public Reservation reserve(final Account account, final BigDecimal reserveAmount) {

		final Reservation reservedAccount = new Reservation();
		final ClientAccount clientAccount = (ClientAccount) account;
		reservedAccount.setAccount(clientAccount);

		reservedAccount.addToReservedAmount(reserveAmount);
		reservedAccount.addToInitialReservationAmount(reserveAmount);

		if (BigDecimal.ZERO.compareTo(reserveAmount) > 0) {
			// Client Account always have positive reserved amounts.
			clientAccount.addToReservedAmount(reserveAmount.negate());

		}
		return reservedAccount;
	}

	@Transactional
	public BusinessTransaction saveBusinessTransaction(BusinessTransaction businessTransaction) {
		businessTransaction = businessTransactionRepository.save(businessTransaction);
		return businessTransaction;
	}

	@Transactional
	public void createStatementEntry(final Account account, final BusinessTransaction businessTransaction,
			final String transactionType, final BigDecimal amount) {

		final StatementLineDetail statementLineDetail = new StatementLineDetail();

		statementLineDetail.setAccountId(account.getId());
		statementLineDetail.setBusinessTransactionId(businessTransaction.getId());
		statementLineDetail.setTransactionType(transactionType);
		statementLineDetail.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		statementLineDetail.setStatementStatus(StatementStatus.COMPLETED);
		statementLineDetail.addToAvailableBalance(this.accountService.getAvailableBalance((ClientAccount) account));
		statementLineDetail.addToCurrentBalance(((ClientAccount) account).getCurrentBalance());
		statementLineDetail.addToTransactionAmount(getTransactionAmount(account.getId(), businessTransaction));
		statementLineDetail.setDescriptionCode(transactionType);

		statementLineDetailRepository.save(statementLineDetail);
	}

	private BigDecimal getTransactionAmount(final long account, final BusinessTransaction businessTransaction) {
		
		final Collection<AccountTransaction> accountTransactionCollection = businessTransaction
				.getAccountTransactionCollection();
		BigDecimal transactionAmount = new BigDecimal(0);

		if ((null != accountTransactionCollection) && (accountTransactionCollection.size() > 0)) {
			for (final AccountTransaction accountTransaction : accountTransactionCollection) {
				for (final AccountEntry accountEntry : accountTransaction.getAccountEntryCollection()) {
					if ((accountEntry.getAccountId() == account)) {
						transactionAmount = transactionAmount.add(accountEntry.getAmount());
					}
				}
			}
		}
		return transactionAmount;
	}

	@Transactional
	public void effectBusinessTransaction(final Account debitAccount, final Account creditAccount,
			final BusinessTransaction businessTransaction) {

		final Collection<ProposedTransaction> proposedTransactionCollection = businessTransaction
				.getProposedTransactionCollection();
		for (final ProposedTransaction proposedTransaction : proposedTransactionCollection) {
			this.setTransactionEntries(debitAccount, creditAccount, businessTransaction, proposedTransaction);
		}
		for (final Reservation reservation : businessTransaction.getReservationCollection()) {
			final ClientAccount account = (ClientAccount) reservation.getAccount();
			if (BigDecimal.ZERO.compareTo(reservation.getReservedAmount()) > 0) {
				account.addToReservedAmount(reservation.getReservedAmount());
			}
			reservation.addToReservedAmount(reservation.getReservedAmount().negate());
		}
		businessTransaction.setStatus(BusinessTransactionStatus.TRANSACTION_COMPLETED);
	}

	private void setTransactionEntries(final Account debitAccount, final Account creditAccount,
			final BusinessTransaction businessTransaction, final ProposedTransaction proposedTransaction) {
		AccountTransaction accountTransaction = new AccountTransaction();
		accountTransaction.addToTransactionAmount(proposedTransaction.getAmount());
		accountTransaction.setTransactionType(proposedTransaction.getAccountTransactionType());

		accountTransaction = this.createAccountEntries(debitAccount, creditAccount, proposedTransaction,
				accountTransaction, proposedTransaction.getAmount());
		accountTransaction.setStatus(TransactionStatus.TRANSACTION_APPLIED);
		accountTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		proposedTransaction.setStatus(BusinessTransactionStatus.TRANSACTION_COMPLETED);
		accountTransaction.setBusinessTransaction(businessTransaction);
		businessTransaction.addAccountTransaction(accountTransaction);
	}

	private AccountTransaction createAccountEntries(final Account debitAccount, final Account creditAccount,
			final ProposedTransaction proposedTransaction, final AccountTransaction accountTransaction,
			final BigDecimal amount) {

		if (debitAccount instanceof ClientAccount) {
			((ClientAccount) debitAccount).subtractToCurrentBalance(amount);
		}
		accountTransaction.createDebitAccountEntries(debitAccount);

		if (creditAccount instanceof ClientAccount) {
			((ClientAccount) creditAccount).addToCurrentBalance(amount);
		}
		accountTransaction.createCreditAccountEntries(creditAccount);

		return accountTransaction;
	}
}
