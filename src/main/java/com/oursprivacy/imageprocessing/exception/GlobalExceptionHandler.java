package com.oursprivacy.imageprocessing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.oursprivacy.imageprocessing.model.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ImageDownloadException.class)
        public ResponseEntity<ApiError> handleImageDownloadException(
                        ImageDownloadException exception) {

                ApiError error = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage());

                return ResponseEntity
                                .status(error.getStatus())
                                .body(error);
        }

        @ExceptionHandler(ImageProcessingException.class)
        public ResponseEntity<ApiError> handleImageProcessingException(
                        ImageProcessingException exception) {

                ApiError error = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage());
                return ResponseEntity
                                .status(error.getStatus())
                                .body(error);
        }

        @ExceptionHandler(InvalidProcessingOptionsException.class)
        public ResponseEntity<ApiError> handleInvalidProcessingOptions(
                        InvalidProcessingOptionsException exception) {
                ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());

                return ResponseEntity
                                .status(error.getStatus())
                                .body(error);
        }
}