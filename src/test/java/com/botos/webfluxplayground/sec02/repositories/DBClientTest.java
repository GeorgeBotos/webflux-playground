package com.botos.webfluxplayground.sec02.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
		"sec=sec02",
		"logging.level.org.springframework.r2dbc=DEBUG"
})
public class DBClientTest {

	private static final Logger log = LoggerFactory.getLogger(DBClientTest.class);

	@Autowired
	private DatabaseClient databaseClient;

	@Test
	@DisplayName("Test if query returns the expected OrderLine objects")
	void orderDetailsByproduct() {
		var query = """
		            select 
		                CO.ORDER_ID, 
		                C.NAME as CUSTOMER_NAME,
		                P.DESCRIPTION as PRODUCT_NAME,
		                CO.AMOUNT,
		                CO.ORDER_DATE
		            from
		                CUSTOMER C
		            inner join CUSTOMER_ORDER CO on C.ID = CO.CUSTOMER_ID
		            inner join PRODUCT P on P.ID = CO.PRODUCT_ID
		            where
		                P.DESCRIPTION = :description
		            order by CO.AMOUNT desc""";
		databaseClient.sql(query)
		              .bind("description", "iphone 20")
		              .mapProperties(OrderLine.class)
		              .all()
		              .as(StepVerifier::create)
		              .assertNext(orderLine -> assertEquals(975, orderLine.amount()))
		              .assertNext(orderLine -> assertEquals(950, orderLine.amount()))
		              .verifyComplete();


	}
}
