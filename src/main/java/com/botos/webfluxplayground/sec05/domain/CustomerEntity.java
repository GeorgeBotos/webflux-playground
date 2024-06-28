package com.botos.webfluxplayground.sec05.domain;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("customer")
public record CustomerEntity(@Id
                             Integer id,

                             @Column("name")
                             String name,

                             String email
) {
	public static CustomerEntity fromCustomer(Customer customer) {
		return CustomerEntity.builder()
		                     .id(customer.id())
		                     .name(customer.name())
		                     .email(customer.email())
		                     .build();
	}
}
