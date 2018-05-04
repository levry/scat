package scat;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

/**
 * @author levry
 */
@TestConfiguration
@TestPropertySource(locations = "classpath:/resources/application-test.properties")
public class TestConfig {
}
