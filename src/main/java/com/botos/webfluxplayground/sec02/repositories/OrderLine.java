package com.botos.webfluxplayground.sec02.repositories;

import java.time.Instant;
import java.util.UUID;

public record OrderLine(UUID orderId,

                        String customerName,

                        String productName,

                        Integer amount,

                        Instant orderDate
) {}
