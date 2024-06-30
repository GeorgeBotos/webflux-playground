package com.botos.webfluxplayground.sec06.domain;

import lombok.Builder;

@Builder
public record Customer(
		Integer id,

		String name,

		String email
) {
	public static Customer fromEntity(CustomerEntity customerEntity) {
		return Customer.builder()
		               .id(customerEntity.id())
		               .name(customerEntity.name())
		               .email(customerEntity.email())
		               .build();
	}
}
