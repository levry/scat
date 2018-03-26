package scat.web.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author levry
 */
@ControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler implements ApplicationContextAware {

    private ErrorResponseBuilder errorResponseBuilder;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        errorResponseBuilder = new ErrorResponseBuilder(applicationContext);
    }

    @ExceptionHandler({
            ObjectRetrievalFailureException.class,
            EntityNotFoundException.class
    })
    @ResponseBody
    public ResponseEntity<?> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                      HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = toErrorResponse(ex.getMessage(), ex.getBindingResult());
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = toErrorResponse(ex.getMessage(), ex.getBindingResult());
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    private Map<String, Object> toErrorResponse(String message, BindingResult result) {
        Map<String, Object> errors = new LinkedHashMap<>();
        if(null == result) {
            errors.put("message", message);
        } else if(result.hasErrors()) {
            errors.put("message", "Validation failed");
            errors.put("errors", errorResponseBuilder.collectErrors(result));
        }
        return errors;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<?> handleException(Throwable exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

}
