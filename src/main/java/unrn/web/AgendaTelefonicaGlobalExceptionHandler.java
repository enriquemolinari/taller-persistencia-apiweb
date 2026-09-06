package unrn.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//El término advise viene de AOP
//Un advice es un bloque de código que se ejecuta antes y/o despues de otro bloque código.
@RestControllerAdvice
public class AgendaTelefonicaGlobalExceptionHandler {

    //las que lanza SpringMVC cuando el Body o cualquier otro parametro requerido no viene
    //las que lanza mi modelo o servicio
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleSpringMVCParams(Exception e) {
        ErrorResponse error = new ErrorResponse(
                "Parámetros inválidos");
        return ResponseEntity.badRequest().body(error);
    }

    //Atrapa cuando falta la cookie de autenticación (token) en el request: @CookieValue(required = true) String token
    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<ErrorResponse> handleSpringMVCCookie(Exception e) {
        ErrorResponse error = new ErrorResponse(
                "Tenes que autenticarse para acceder a este servicio");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    //cualquier otra
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse(
                "Algo salió mal... contacte a bla bla");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
