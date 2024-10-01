package org.gameserver.errors;

import org.gameserver.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> catchNotFoundException(NotFoundException e) {
        ErrorDto errorDto = new ErrorDto("NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadDataException.class)
    public ResponseEntity<ErrorDto> catchBadDataException(BadDataException e) {
        ErrorDto errorDto = new ErrorDto("BAD_DATA", e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
