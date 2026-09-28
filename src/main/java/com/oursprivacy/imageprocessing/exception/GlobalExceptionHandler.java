package com.oursprivacy.imageprocessing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ImageDownloadException.class)
        public ResponseEntity<String> handleImageDownloadException(
                        ImageDownloadException exception) {

                return ResponseEntity
                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                .body(exception.getMessage());
        }

        @ExceptionHandler(ImageProcessingException.class)
        public ResponseEntity<String> handleImageProcessingException(
                        ImageProcessingException exception) {

                return ResponseEntity
                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                .body(exception.getMessage());
        }
}