package com.framework.core.interfaces.impl.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import com.framework.core.interfaces.Element;
import com.framework.utils.AllegisDriver;

public class ElementDecorator implements FieldDecorator {

	protected CustomElementLocatorFactory factory;
    protected AllegisDriver driver;

    public ElementDecorator(CustomElementLocatorFactory factory) {
        this.factory = factory;
    }

    public ElementDecorator(CustomElementLocatorFactory factory, AllegisDriver driver) {
        this.factory = factory;
        this.driver = driver;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }

        Class<?> fieldType = field.getType();
        if (WebElement.class.equals(fieldType)) {
            fieldType = Element.class;
        }

        if (WebElement.class.isAssignableFrom(fieldType)) {
            return proxyForLocator(loader, fieldType, locator, driver);
        } 
        
        if (List.class.isAssignableFrom(fieldType)) {
            Class<?> erasureClass = getErasureClass(field);
            return proxyForListLocator(loader, erasureClass, locator, driver);
        } else {
            return null;
        }
        
    }

    @SuppressWarnings("rawtypes")
    private Class getErasureClass(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) 
            return null;

        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    private boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) 
            return false;

        @SuppressWarnings("rawtypes")
        Class erasureClass = getErasureClass(field);
        
        if (erasureClass == null || !WebElement.class.isAssignableFrom(erasureClass)) 
            return false;

        if (field.getAnnotation(FindBy.class) == null && 
            field.getAnnotation(FindBys.class) == null) 
        	return false;

        return true;
    }

    protected <T> T proxyForLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator, AllegisDriver driver) {
        InvocationHandler handler = new ElementHandler(interfaceType, locator, driver);

        return interfaceType.cast(Proxy.newProxyInstance(loader, 
        												 new Class[] { 
        														 	   interfaceType, 
        														 	   WebElement.class, 
        														 	   WrapsElement.class 
        														 	   //Locatable.class 
        														 	 }, 
        												 handler));
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> proxyForListLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator, AllegisDriver driver) {
        InvocationHandler handler = Element.class.isAssignableFrom(interfaceType)
        								? new ElementListHandler(interfaceType, locator, driver)
        								: new LocatingElementListHandler(locator);
        
        return  (List<T>) Proxy.newProxyInstance(loader, 
        		 							     new Class[] { List.class }, 
        		 							     handler);
    }
    
}