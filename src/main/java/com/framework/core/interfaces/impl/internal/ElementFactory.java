package com.framework.core.interfaces.impl.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementFactory {	
	
	private ElementFactory() {
		
	}
	
	public static <T> T initElements(AllegisDriver driver, Class<T> pageClassToProxy) {
		T page = instantiatePage(driver, pageClassToProxy);

		PageFactory.initElements(new ElementDecorator(new CustomElementLocatorFactory(driver)), page);

		return page;
	}

	public static void initElements(AllegisDriver driver, Object page) {
		PageFactory.initElements(new ElementDecorator(new CustomElementLocatorFactory(driver), driver), page);
	}

	public static void initElements(CustomElementLocatorFactory factory, Object page) {
		PageFactory.initElements(new ElementDecorator(factory), page);
	}

	public static void initElements(FieldDecorator decorator, Object page) {
		PageFactory.initElements(decorator, page);
	}

	private static <T> T instantiatePage(WebDriver driver, Class<T> pageClass) {
		try {
			Constructor<T> constructor = pageClass.getConstructor(WebDriver.class);
			return constructor.newInstance(driver);
		}
		catch (NoSuchMethodException e) {
			return getPageInstance(pageClass);
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new AutomationException("Failed to create instance of: " + pageClass.getName() + 
										 "\n" + e.getMessage());
		}
	}		
	
	private static <T> T getPageInstance(Class<T> pageClass) {
		try {
			return pageClass.newInstance();
		} catch (IllegalAccessException | InstantiationException ie) {
			throw new AutomationException("Failed to create instance of: " + pageClass.getName());
		}

	}
}