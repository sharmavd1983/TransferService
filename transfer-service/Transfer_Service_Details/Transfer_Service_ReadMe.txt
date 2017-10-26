--------------------------------------------
1. Technology Choices
--------------------------------------------
- Spring Boot, Spring MVC, Spring Core
- Hibernate
- Postgres Database


--------------------------------------------
2. Deployment Instructions
--------------------------------------------
1. Create batabase with schema name as "transfer" and "username=postgres", "password=password"

2. Project ext-resouces folder contains all properties file i.e. db.properties, app.properties and log4j.properties

3. ext-resouces folder also contains Create_insert_database.sql file for creating tables and sequences 

4. It also contains startup.bat and startup.sh file to start spring boot application on port 8100

5. Build project with following command

   mvn clean install -P deployment

- It will create one folder in target as "transfer-service-deployable" and copy all required file i.e. jar and external resources  

- Simply go there and run startup.bat or startup.sh file from command prompt

- It will start start spring boot application on port 8100

- After that, user can setup client accounts using postman REST API and do transfer money 

- Postman REST requests details can be found in Transfer_Service_Details.zip file > Postman_Rest_Requests folder with name TransferService.postman_collection.json


--------------------------------------------
3. Database Details
--------------------------------------------
SEQUENCE id_seq
- Used for ID generation

TABLE ACCOUNT
- Used for storing all client accounts

TABLE ACCOUNT_ENTRY
- Used to capture any financial entries on client accounts

TABLE BUSINESS_TRANSACTION
- Used to capture each business transaction like money transfer

TABLE BUSINESS_TRANSACTION_DATA
- Used to capture business transaction data i.e. payer, payee, amount etc.

TABLE ACCOUNT_TRANSACTION
- Used to capture account related details of business transaction 

TABLE PROPOSED_TRANSACTION
- Used to capture proposal of any transaction before reserving funds and to capture credit, debit and amount details.

TABLE RESERVATION
- Used to capture reserved amount before actually moving money.

TABLE STATEMENT_LINE_DETAIL
-Used to capture all line items of an account for a business transaction.

