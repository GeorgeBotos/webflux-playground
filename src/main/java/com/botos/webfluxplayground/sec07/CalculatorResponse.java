package com.botos.webfluxplayground.sec07;

public record CalculatorResponse(Integer first,
                                 Integer second,
                                 String operation,
                                 Double result) {}
