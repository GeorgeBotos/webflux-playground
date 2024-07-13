package com.botos.webfluxplayground.sec10;

import lombok.Builder;

@Builder
public record Product(Integer id,
                      String description,
                      Integer price) {}
