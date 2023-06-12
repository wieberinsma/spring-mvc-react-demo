package nl.han.rwd.srd.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SRDExceptionMapper
{
    @ExceptionHandler(SRDEXception.class)
    public ResponseEntity<String> handleSRDException(SRDEXception ex)
    {
        return ResponseEntity.internalServerError().body(ex.getMessage() + ": " + ex.getCause());
    }
}
