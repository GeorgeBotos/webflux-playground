package com.botos.webfluxplayground.sec02.repositories;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Builder
@Table("product")
public record ProductEntity(@Id
                            Integer id,

                            String description,

                            Integer price
) {
    @Override
    public String toString() {
        return "product: {description: %s, price: %d}".formatted(description, price);
    }
}
