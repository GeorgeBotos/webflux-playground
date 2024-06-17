package com.botos.webfluxplayground.sec02.repositories;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Builder
@Table("customer_order")
public record OrderEntity(@Id UUID id,
                          Integer customerId,
                          Integer productId,
                          Integer amount,
                          Instant orderDate) {
}
