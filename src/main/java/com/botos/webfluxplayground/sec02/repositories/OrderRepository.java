package com.botos.webfluxplayground.sec02.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<OrderEntity, UUID> {

	@Query("""
	       select P.*
	       from CUSTOMER C
	       inner join CUSTOMER_ORDER CO on C.ID = CO.CUSTOMER_ID
	       inner join PRODUCT P on CO.PRODUCT_ID = P.ID
	       where C.NAME = :NAME""")
	Flux<ProductEntity> getProductsOrderedByCustomer(String name);

	@Query("""
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
	       order by CO.AMOUNT desc
	       """)
	Flux<OrderLine> getOrderDetailsByProduct(String description);
}
