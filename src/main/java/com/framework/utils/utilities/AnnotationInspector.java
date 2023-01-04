package com.framework.utils.utilities;

import java.lang.annotation.Annotation;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.testng.IInvokedMethod;
import org.testng.IMethodInstance;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;

import com.framework.utils.Preamble;
import com.framework.utils.annotationsAllegis.Retry;

@Component
public class AnnotationInspector {

	public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation, IMethodInstance method) {
		return getAnnotation(annotation, method.getMethod());
	}
	
    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation, ITestResult result) {
        return getAnnotation(annotation, result.getMethod());
    }
	
    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation, ITestNGMethod method) {
        if (!method.isTest()) {
        	return Optional.empty();
        }
        return getAnnotation(annotation, method.getConstructorOrMethod());
    }
    
    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation, IInvokedMethod method) {
        if (!method.isTestMethod()) {
            return Optional.empty();
        }
        return getAnnotation(annotation, method.getTestMethod());
    }    

	public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation, ConstructorOrMethod com) {
		if (com.getMethod() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(com.getMethod().getAnnotation(annotation));
	}
	
	public Optional<Preamble> preambleFrom(ITestResult result) {
		return getAnnotation(Preamble.class, result);
	}
	
	public Optional<Preamble> preambleFrom(ITestNGMethod result) {
		return getAnnotation(Preamble.class, result);
	}
	
	public Optional<Test> testFrom(ITestNGMethod method) {
		return getAnnotation(Test.class, method);
	}

	public Optional<Test> testFrom(IInvokedMethod method) {
		return getAnnotation(Test.class, method);
	}

	public Optional<Preamble> preambleFrom(IInvokedMethod method) {
		return getAnnotation(Preamble.class, method);
	}
	
	public Optional<Retry> retryFrom(IInvokedMethod method) {
		return getAnnotation(Retry.class, method);
	}
	
	public Optional<Retry> retryFrom(ITestResult result) {
		return getAnnotation(Retry.class, result);
	}
}
