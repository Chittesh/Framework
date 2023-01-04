package com.framework.core.interfaces.impl.internal;

import java.lang.reflect.Field;

import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import com.framework.utils.AllegisDriver;

public class CustomElementLocatorFactory implements ElementLocatorFactory {
	private final AllegisDriver driver;

	public CustomElementLocatorFactory(final AllegisDriver driver) {
		this.driver = driver;
	}

	@Override
	public ElementLocator createLocator(final Field field) {
		return new DefaultElementLocator(driver.getWebDriver(), field);
	}
}