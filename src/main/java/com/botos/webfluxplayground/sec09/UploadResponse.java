package com.botos.webfluxplayground.sec09;

import java.util.UUID;

public record UploadResponse (UUID confirmationId,
                              Long productsCount) {

	public UploadResponse(Long productsCount) {
		this(UUID.randomUUID(), productsCount);
	}
}
