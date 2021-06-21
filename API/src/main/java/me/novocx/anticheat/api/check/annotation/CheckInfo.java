package me.novocx.anticheat.api.check.annotation;

import me.novocx.anticheat.api.check.CheckType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckInfo {
    String name() default "Check (A)";
    String description() default "";
    int maxVl() default 20;
    int vlToAlert() default 0;
    boolean dev() default true;
    CheckType type() default CheckType.ALL;
}
