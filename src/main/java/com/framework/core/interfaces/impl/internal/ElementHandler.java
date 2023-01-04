package com.framework.core.interfaces.impl.internal;

import static com.framework.core.interfaces.impl.internal.ImplementedByProcessor.getWrapperClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.framework.core.interfaces.Element;
import com.framework.exception.AutomationException;
import com.framework.exception.automation.WebException;
import com.framework.utils.AllegisDriver;

public class ElementHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<?> wrappingType;
    private AllegisDriver driver;

    public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator) {
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new AutomationException("interface not assignable to Element.");
        }

        this.locator = locator;
        this.wrappingType = getWrapperClass(interfaceType);
    }

    public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator, AllegisDriver driver) {
        this(interfaceType, locator);
        
        this.driver = driver;
    }

    
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        By by = null;

        try {
            Field field = locator.getClass().getDeclaredField("by");
            field.setAccessible(true);
            by = (By) field.get(locator);
        } catch (Exception e) {
            throw new WebException("Failed to obtain element locator", driver);
        }

        if ("getWrappedElement".equals(method.getName())) {
            return locator.findElement();
        }

        if ("getWrappedDriver".equals(method.getName())) {
            return driver;
        }

        try {
        	Constructor cons = wrappingType.getConstructor(AllegisDriver.class, By.class);
            Object thing = cons.newInstance(driver, by);
        	return method.invoke(wrappingType.cast(thing), objects);
        } catch (Exception e) {
            throw new AutomationException(e.getCause().toString());
        } 
    }
}