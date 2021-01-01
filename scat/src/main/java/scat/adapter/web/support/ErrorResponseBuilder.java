package scat.adapter.web.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author levry
 */
public class ErrorResponseBuilder {

    private final MessageSourceAccessor messageSourceAccessor;

    private ErrorResponseBuilder(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    ErrorResponseBuilder(ApplicationContext applicationContext) {
        this(new MessageSourceAccessor(applicationContext));
    }

    public List<Map<String, Object>> collectErrors(BindingResult result) {
        return result.getAllErrors().stream()
                .map(toResponse())
                .collect(toList());
    }

    private Function<ObjectError, Map<String, Object>> toResponse() {
        return error -> {
            Map<String, Object> map = new LinkedHashMap<>(3);
            if(error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                map.put("field", fieldError.getField());
            }
            map.put("code", error.getCode());
            map.put("message", messageSourceAccessor.getMessage(error));
            return map;
        };
    }

}
