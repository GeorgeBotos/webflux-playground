package com.botos.webfluxplayground.sec02.repositories;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

public record OrderLine(@Id UUID orderId,

                        String customerName,

                        String productName,

                        Integer amount,

                        Instant orderDate
) {}
