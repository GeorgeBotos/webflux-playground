package com.botos.webfluxplayground.sec07;

import lombok.Builder;

@Builder
public record Product(Integer id,
                      String description,
                      Integer price) {}
