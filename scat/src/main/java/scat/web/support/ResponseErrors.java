package scat.web.support;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author levry
 */
public class ResponseErrors {

    private final String message;
    private final List<Error> errors = new ArrayList<>();

    public ResponseErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<Error> getErrors() {
        return errors;
    }

    private void add(String field, String code, String message) {
        errors.add(new Error(field, code, message));
    }

    public static ResponseEntity<ResponseErrors> validation(Consumer<ErrorsBuilder> errors) {
        return errors("Validation failed", HttpStatus.UNPROCESSABLE_ENTITY, errors);
    }

    public static ResponseEntity<?> badRequest(Consumer<ErrorsBuilder> errors) {
        return errors("Bad request", HttpStatus.BAD_REQUEST, errors);
    }

    private static ResponseEntity<ResponseErrors> errors(String message, HttpStatus httpStatus, Consumer<ErrorsBuilder> errors) {
        ErrorsBuilder builder = new ErrorsBuilder(message);
        errors.accept(builder);
        return ResponseEntity
                .status(httpStatus)
                .body(builder.build());
    }

    public static class Error {
        private final String field;
        private final String code;
        private final String message;

        Error(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ErrorsBuilder {

        private final ResponseErrors errors;

        ErrorsBuilder(String message) {
            errors = new ResponseErrors(message);
        }

        ResponseErrors build() {
            return errors;
        }

        public void notFound(String field) {
            errors.add(field, "not_found", "Not found");
        }
    }

}
