package scat.adapter.web.support;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author levry
 */
class ErrorResponseBuilderTests {

    @Test
    void exception_with_object_error() {
        BindingResult bindingResult = bindingResult(objectError("test", "This is error message"));

        ErrorResponseBuilder builder = new ErrorResponseBuilder(stubContext());
        List<Map<String, Object>> errors = builder.collectErrors(bindingResult);


        assertThat(errors).hasSize(1);

        Map<String, Object> error = errors.get(0);
        assertThat(error)
                .contains(entry("message", "This is error message"))
                .contains(entry("code", "test"));
    }

    @Test
    void exception_with_field_error() {

        ObjectError fieldError = new FieldError("test", "testField", "Test message");
        BindingResult bindingResult = bindingResult(fieldError);

        ErrorResponseBuilder builder = new ErrorResponseBuilder(stubContext());

        List<Map<String, Object>> errors = builder.collectErrors(bindingResult);

        assertThat(errors).hasSize(1);

        Map<String, Object> error = errors.get(0);
        assertThat(error)
                .contains(entry("message", "Test message"))
                .contains(entry("field", "testField"));
    }

    private BindingResult bindingResult(ObjectError... errors) {
        BindingResult bindingResult = new MapBindingResult(emptyMap(), "test");
        if(null != errors && errors.length > 0) {
            Arrays.stream(errors).forEach(bindingResult::addError);
        }
        return bindingResult;
    }

    private ObjectError objectError(String code, String message) {
        String[] codes = {code};
        return new ObjectError("test", codes, null, message);
    }

    private ApplicationContext stubContext() {
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getMessage(any(), any())).thenAnswer(invocation -> {
            MessageSourceResolvable messageSource = invocation.getArgument(0);
            return messageSource.getDefaultMessage();
        });
        return context;
    }

}