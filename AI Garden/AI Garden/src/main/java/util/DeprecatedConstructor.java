package main.java.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.CONSTRUCTOR)
public @interface DeprecatedConstructor {
    String reason() default "";
}
