package com.meta.winterfull.annotation;

import java.lang.annotation.*;

/**
 * @author : ytxu5
 * @date: 2023/3/27
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MetaLogging {
}
