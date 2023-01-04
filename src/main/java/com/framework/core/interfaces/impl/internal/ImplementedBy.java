package com.framework.core.interfaces.impl.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.framework.core.interfaces.impl.ElementImpl;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementedBy {
	
	@SuppressWarnings("rawtypes")
	Class value() default ElementImpl.class;

}