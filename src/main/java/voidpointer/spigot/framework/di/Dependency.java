package voidpointer.spigot.framework.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
    /**
     * An identifier if you have multiple objects with the same name.
     *  If not set {@link Class#getName()} will be used to identify.
     */
    String id() default "";
}
