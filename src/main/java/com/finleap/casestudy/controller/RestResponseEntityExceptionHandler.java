package com.finleap.casestudy.controller;




import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String COLON = "  :  ";


	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<MessageResponse> validationException(ValidationException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoIncidentFoundException.class)
	public ResponseEntity<MessageResponse> noIncidentFoundException(final NoIncidentFoundException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef), HttpStatus.NOT_FOUND);
	}


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		MessageResponse error = new MessageResponse("Validation Failed: " + details);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<MessageResponse> userNotFoundException(final UserNotFoundException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyAssignedException.class)
	public ResponseEntity<MessageResponse> userAlreadyAssignedException(final UserAlreadyAssignedException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserUnauthorisedException.class)
	public ResponseEntity<MessageResponse> userUnauthorisedException(final UserUnauthorisedException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef), HttpStatus.FORBIDDEN);
	}



	private String logError(Exception ex) {
		String errorRef = "Error Reference: " + UUID.randomUUID();
		logger.error(errorRef, ex);
		return errorRef;
	}

}