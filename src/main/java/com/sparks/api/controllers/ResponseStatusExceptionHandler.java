package com.sparks.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.responses.ResponseStatusExceptionResponse;

@ControllerAdvice
public class ResponseStatusExceptionHandler extends ResponseEntityExceptionHandler {
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NotFoundException.class })
	public ResponseStatusExceptionResponse handleResourceNotFoundException(NotFoundException ex) {
		ResponseStatusExceptionResponse response = new ResponseStatusExceptionResponse(ex.getMessage());

		return response;
	}

	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadRequestException.class })
	public ResponseStatusExceptionResponse handleBadRequestException(BadRequestException ex) {
		ResponseStatusExceptionResponse response = new ResponseStatusExceptionResponse(ex.getMessage());

		return response;
	}

	@ResponseBody
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Exception.class })
	public ResponseStatusExceptionResponse handleException(Exception ex) {
		ResponseStatusExceptionResponse response = new ResponseStatusExceptionResponse("Aconteceu um problema interno");

		return response;
	}
}
