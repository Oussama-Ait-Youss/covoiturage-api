
package com.covoiturage.api.exception;

import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.exception.EmailAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // EMAIL ALREADY EXISTS
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(NumeroPermisAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleNumeroPermisExists(
            NumeroPermisAlreadyExistsException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    // user not found

    @ExceptionHandler(UtilisateurNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUtilisateurNotFound(
            UtilisateurNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(ConducteurNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerConducteurNotFound(
            ConducteurNotFoundException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(CapacityNumberInvalideException.class)
    public ResponseEntity<ProblemDetail> handlerCapacity(
            CapacityNumberInvalideException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<ProblemDetail> handlerUserAcitivy(
            UserNotActiveException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(VehiculeNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerVehiculenotfound(
            VehiculeNotFoundException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(VehiculeNotOwnedException.class)
    public ResponseEntity<ProblemDetail> handlerVehiculeNotOwned(
            VehiculeNotOwnedException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }


    // DTO VALIDATION ERRORS
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return buildError(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
    }

    // INVALID JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Invalid JSON request",
                request
        );
    }

    // DATABASE CONSTRAINT ERRORS
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDatabaseConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.CONFLICT,
                "Database constraint violation",
                request
        );
    }

    // UNEXPECTED ERRORS
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedError(
            Exception ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }

    // COMMON ERROR RESPONSE
    private ResponseEntity<ProblemDetail> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status,
                message
        );

        problem.setTitle(status.getReasonPhrase());
        problem.setProperty("path", request.getRequestURI());

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(TrajetNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTrajetNotFound(
            TrajetNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidTrajetException.class)
    public ResponseEntity<ProblemDetail> handleInvalidTrajet(
            InvalidTrajetException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(PassagerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerPassager(
            PassagerNotFoundException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }
    @ExceptionHandler(TrajetNotOpenException.class)
    public ResponseEntity<ProblemDetail> handleTrajetInscri(
            TrajetNotOpenException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(PointArretNotFound.class)
    public ResponseEntity<ProblemDetail> handlePointArret(
            PointArretNotFound ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }
    @ExceptionHandler(PointArretNotBelongToTrajetException.class)
    public ResponseEntity<ProblemDetail> handlePointArretBelongToTrajet(
            PointArretNotBelongToTrajetException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }
    @ExceptionHandler(PointArretOrderException.class)
    public ResponseEntity<ProblemDetail> handlePointArretOrder(
            PointArretOrderException ex,
            HttpServletRequest request
    ){
        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

}
