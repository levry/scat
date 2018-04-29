package scat.web.support;

import org.junit.Test;
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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author levry
 */
public class ErrorResponseBuilderTest {

    @Test
    public void exception_with_object_error() {
        BindingResult bindingResult = bindingResult(objectError("test", "This is error message"));

        ErrorResponseBuilder builder = new ErrorResponseBuilder(stubContext());
        List<Map<String, Object>> errors = builder.collectErrors(bindingResult);

        assertNotNull(errors);
        assertThat(errors.size(), is(1));

        Map error = errors.get(0);
        assertThat(error.get("message"), is("This is error message"));
        assertThat(error.get("code"), is("test"));
    }

    @Test
    public void exception_with_field_error() {

        ObjectError fieldError = new FieldError("test", "testField", "Test message");
        BindingResult bindingResult = bindingResult(fieldError);

        ErrorResponseBuilder builder = new ErrorResponseBuilder(stubContext());

        List<Map<String, Object>> errors = builder.collectErrors(bindingResult);

        assertNotNull(errors);
        assertThat(errors.size(), is(1));

        Map error = errors.get(0);
        assertThat(error.get("message"), is("Test message"));
        assertThat(error.get("field"), is("testField"));
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