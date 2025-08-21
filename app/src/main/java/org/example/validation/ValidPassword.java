// org.example.validation.ValidPassword.java
package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default
            "Password must be 8–64 chars, include upper, lower, digit, special, and contain no spaces.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
