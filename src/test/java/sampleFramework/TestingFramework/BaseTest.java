package sampleFramework.TestingFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.springframework.beans.factory.annotation.Value;

public class BaseTest {

	private Logger logger = LogManager.getLogger(StringFormatterMessageFactory.INSTANCE);
	private static final String POM_PROPERTIES_PATH = "target/my.properties";

	protected static final TestEnvironmentExecutionContext context = new TestEnvironmentExecutionContext();


	protected String mobileOSVersion = "";
	
    protected String deviceID = "";

    protected String appiumHash;
	
	@Value("${spring.profiles.active:default}")
	private String[] profiles;
}
