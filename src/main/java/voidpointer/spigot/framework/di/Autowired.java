package voidpointer.spigot.framework.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    /**
     * An identifier to map {@link Autowired} object to {@link Dependency}.
     *  If not set {@link Class#getName()} will be used to identify.
     */
    String mapId() default "";
}
