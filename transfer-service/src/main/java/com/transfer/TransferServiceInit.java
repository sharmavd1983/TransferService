package com.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@PropertySources({
		@PropertySource("file:${baseDir}/config/properties/db.properties"),
		@PropertySource("file:${baseDir}/config/properties/app.properties") })
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.transfer" })
@EnableJpaRepositories(basePackages = {"com.transfer.repository"})
@EntityScan({ "com.transfer.domain" })
public class TransferServiceInit {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TransferServiceInit.class, args);
	}
}