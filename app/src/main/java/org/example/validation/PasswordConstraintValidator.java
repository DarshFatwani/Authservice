package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) return false;
        String pwd = value.trim();
        if (pwd.length() < 8 || pwd.length() > 64) return false;

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false, hasSpace = false;
        for (char c : pwd.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isWhitespace(c)) hasSpace = true;
            else hasSpecial = true;
        }
        return !hasSpace && hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
