package com.framework.core.interfaces.impl.internal;

import static com.framework.core.interfaces.impl.internal.ImplementedByProcessor.getWrapperClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.framework.core.interfaces.Element;
import com.framework.exception.AutomationException;
import com.framework.exception.automation.WebException;
import com.framework.utils.AllegisDriver;

public class ElementListHandler implements InvocationHandler {
	
    private final ElementLocator locator;
    private final Class<?> wrappingType;
    private AllegisDriver driver;

    public <T> ElementListHandler(Class<T> interfaceType, ElementLocator locator) {
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new AutomationException("interface not assignable to Element.");
        }
        
        this.locator = locator;
        this.wrappingType = getWrapperClass(interfaceType);
    }

    public <T> ElementListHandler(Class<T> interfaceType, ElementLocator locator, AllegisDriver driver) {
        this(interfaceType, locator);

        this.driver = driver;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        By by = null;

        try {
            Field elementField = locator.getClass().getDeclaredField("by");
            elementField.setAccessible(true);
            by = (By) elementField.get(locator);
        } catch (Exception e) {
            throw new WebException("Failed to obtain element locator", driver);
        }

        if ("getWrappedElement".equals(method.getName())) {
            return locator.findElement();
        }

        if ("getWrappedDriver".equals(method.getName())) {
            return driver;
        }

        Constructor cons = wrappingType.getConstructor(AllegisDriver.class, By.class, WebElement.class);
        List<Object> wrappedList = new ArrayList<>();

        List<WebElement> elements = locator.findElements();
        
        for (WebElement element : elements) {
            Object thing = cons.newInstance(driver, by, element);
            wrappedList.add(wrappingType.cast(thing));
        }

        try {
            return method.invoke(wrappedList, objects);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (WebDriverException e) {
            return false;
        }
    }
}