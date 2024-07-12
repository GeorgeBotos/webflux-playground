package com.botos.webfluxplayground.sec08.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("PRODUCT")
public class ProductEntity {

	@Id
	private Integer id;
	private String description;
	private Integer price;

	public static ProductEntity fromProduct(Product product) {
		return ProductEntity.builder()
		                    .id(product.id())
		                    .description(product.description())
		                    .price(product.price())
		                    .build();
	}
}
