package nl.han.rwd.srd.domain.user.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UsernameUniqueValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldUnique
{
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String unique() default "";

    String message() default "Field must be unique.";
}
