package library.management.controllers;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import library.management.utils.ProblemDetailJson;
import library.management.utils.ResourceAlreadyExistException;
import library.management.utils.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	ProblemDetailJson sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
		return ProblemDetailJson.forStatus(HttpStatus.CONFLICT)
				.setTitle("SQL Constraint Violation")
				.setDetail(exception.getMessage())
				.set("sql_state", exception.getSQLState())
				.set("error_code", exception.getErrorCode());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ProblemDetailJson handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		ProblemDetailJson body = ProblemDetailJson.forStatus(HttpStatus.BAD_REQUEST);
		body.setDetail("validation failed on some fields");
		body.setTitle("Validation Failed");

		List<Object> jsonFieldErrors = new ArrayList<>();
		body.put("field_errors", jsonFieldErrors);

		for (FieldError fieldError : exception.getFieldErrors()) {
			Map<String, Object> jsonFieldError = new HashMap<>();
			jsonFieldErrors.add(jsonFieldError);
			jsonFieldError.put("field", fieldError.getField());
			jsonFieldError.put("message", fieldError.getDefaultMessage());
			jsonFieldError.put("rejected_value", fieldError.getRejectedValue());
			jsonFieldError.put("type_mismatch", fieldError.isBindingFailure());
		}
		return body;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ProblemDetailJson httpMessageNotReadableException(HttpMessageNotReadableException exception) {
		return ProblemDetailJson.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	ProblemDetailJson bindException(BindException exception) {
		Map<String, Object> errors = new HashMap<>();
		for (FieldError fieldError : exception.getFieldErrors()) {
			final String key = snakeCase(fieldError.getField()), value = fieldError.getDefaultMessage();
			errors.put(key, value);
		}
		return ProblemDetailJson.forStatus(HttpStatus.BAD_REQUEST)
				.setDetail("constraint violation occurred on some fields")
				.setTitle("Constraint Violation")
				.set("field_errors", errors);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ResourceAlreadyExistException.class)
	ProblemDetailJson resourceAlreadyExistException(ResourceAlreadyExistException exception) {
		return ProblemDetailJson.create()
				.setTitle("Resource Already Exist")
				.setDetail(exception.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	ProblemDetailJson resourceNotFoundException(ResourceNotFoundException exception) {
		return ProblemDetailJson.create()
				.setTitle("Resource Not Found")
				.setDetail(exception.getMessage());
	}

	private String snakeCase(final String input) {
		return PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE.translate(input);
	}
}

