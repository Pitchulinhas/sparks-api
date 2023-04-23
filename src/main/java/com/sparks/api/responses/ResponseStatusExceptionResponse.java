package com.sparks.api.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseStatusExceptionResponse {
	private String error;
	
	public ResponseStatusExceptionResponse(String error) {
		this.error = error;
	}
}
