package com.botos.webfluxplayground.sec02.repositories;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@Builder
@Getter
@Setter
@Table("customer")
public class CustomerEntity {

	@Id
	private Integer id;

	@Column("name")
	private String name;
	private String email;
}
