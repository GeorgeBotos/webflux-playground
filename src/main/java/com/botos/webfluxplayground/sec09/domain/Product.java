package com.botos.webfluxplayground.sec09.domain;

import lombok.Builder;

@Builder
public record Product(Integer id,
                      String description,
                      Integer price) {

	public static Product fromEntity(ProductEntity productEntity) {
		return Product.builder()
		              .id(productEntity.getId())
		              .description(productEntity.getDescription())
		              .price(productEntity.getPrice())
		              .build();
	}
}
