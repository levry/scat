package scat.web.support;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author levry
 */
@Component
public class ModelErrorAttributes extends DefaultErrorAttributes implements ApplicationContextAware {

    private MessageSourceAccessor messageSourceAccessor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.messageSourceAccessor = new MessageSourceAccessor(applicationContext);
    }

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Throwable error = getError(requestAttributes);
        if(null == error) {
            return Collections.emptyMap();
        }

        Map<String, Object> errors = new LinkedHashMap<>();
        BindingResult result = extractBindingResult(error);
        if(null == result) {
            errors.put("message", error.getMessage());
        } else if(result.hasErrors()) {
            errors.put("message", "Validation failed");
            errors.put("errors", extractErrors(result));
        }
        return errors;
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    private List<?> extractErrors(BindingResult result) {
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
