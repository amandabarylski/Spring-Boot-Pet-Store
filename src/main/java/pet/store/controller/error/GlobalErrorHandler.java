package pet.store.controller.error;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {

//I added the ExceptionHandler and ResponseStatus annotations to my error handler.
//I didn't know how to return a new map with content already in it, so I created a HashMap and put the required elements into it.
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> handleNoSuchElementException(NoSuchElementException e) {
		log.error("Error: " + e.toString());
		Map<String, String> exception = new HashMap<>();
		exception.put("message", e.toString());
		return exception;
	}
	
}
