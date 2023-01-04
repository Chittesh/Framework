package com.framework.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
public @interface Preamble {
    String author() default "N/A";

    String date() default "N/A";

    String[] reviewers() default "N/A";

    String[] steps() default "N/A";

    String description() default "N/A";

    String[] versionOneTestID() default "N/A";

    String versionOneTestSuiteName() default "N/A";

    String precondition() default "N/A";

    String dataFile() default "N/A";

    String[] params() default "N/A";

    String returns() default "N/A";

    String[] canThrow() default "N/A";

    int currentRevision() default 1;

    String lastModified() default "N/A";

    String lastModifiedBy() default "N/A";

    String changeComment() default "";

    String testId() default "N/A";
}