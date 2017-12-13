package scat.web.support;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.*;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author levry
 */
public class ModelErrorAttributesTest {

    @Test
    public void shouldContainsMessage() {
        RequestAttributes requestAttributes = stubRequestAttributes(new RuntimeException("exception message"));

        ModelErrorAttributes attributes = new ModelErrorAttributes();
        Map<String, Object> model = attributes.getErrorAttributes(requestAttributes, false);

        assertEquals("exception message", model.get("message"));
    }

    @Test
    public void noExceptionInRequest() {
        RequestAttributes requestAttributes = stubRequestAttributes(null);

        ModelErrorAttributes attributes = new ModelErrorAttributes();
        Map<String, Object> model = attributes.getErrorAttributes(requestAttributes, false);

        assertNotNull(model);
    }

    @Test
    public void exceptionWithObjectError() {
        Throwable exception = stubBindException(objectError("test", "This is error message"));
        RequestAttributes requestAttributes = stubRequestAttributes(exception);

        ModelErrorAttributes attributes = new ModelErrorAttributes();
        attributes.setApplicationContext(stubContext());
        Map<String, Object> model = attributes.getErrorAttributes(requestAttributes, false);

        assertNotNull(model.get("errors"));

        List<Map> errors = (List) model.get("errors");
        assertEquals(errors.size(), 1);

        Map error = errors.get(0);
        assertEquals(error.get("message"), "This is error message");
        assertEquals(error.get("code"), "test");
    }

    @Test
    public void exceptionWithFieldError() {

        ObjectError fieldError = new FieldError("test", "testField", "Test message");
        Throwable exception = stubBindException(fieldError);
        RequestAttributes requestAttributes = stubRequestAttributes(exception);

        ModelErrorAttributes attributes = new ModelErrorAttributes();
        attributes.setApplicationContext(stubContext());
        Map<String, Object> model = attributes.getErrorAttributes(requestAttributes, false);

        assertNotNull(model.get("errors"));

        List<Map> errors = (List) model.get("errors");
        assertEquals(errors.size(), 1);

        Map error = errors.get(0);
        assertEquals(error.get("message"), "Test message");
        assertEquals(error.get("field"), "testField");
    }


    private Throwable stubBindException(ObjectError... errors) {
        BindingResult bindingResult = new MapBindingResult(emptyMap(), "test");
        if(null != errors && errors.length > 0) {
            Arrays.stream(errors).forEach(bindingResult::addError);
        }
        return new BindException(bindingResult);
    }

    private ObjectError objectError(String code, String message) {
        String[] codes = {code};
        return new ObjectError("test", codes, null, message);
    }

    private ApplicationContext stubContext() {
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getMessage(any(), any())).thenAnswer(invocation -> {
            MessageSourceResolvable messageSource = invocation.getArgumentAt(0, MessageSourceResolvable.class);
            return messageSource.getDefaultMessage();
        });
        return context;
    }

    private RequestAttributes stubRequestAttributes(Throwable throwable) {
        RequestAttributes requestAttributes = mock(RequestAttributes.class);
        when(requestAttributes.getAttribute(eq("javax.servlet.error.exception"), anyInt())).thenReturn(throwable);
        return requestAttributes;
    }
}