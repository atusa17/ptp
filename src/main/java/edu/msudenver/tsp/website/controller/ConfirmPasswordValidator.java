package edu.msudenver.tsp.website.controller;

import edu.msudenver.tsp.website.forms.UserCreationForm;

import javax.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator
    implements ConstraintValidator<PasswordMatchChecker, Object> {

        @Override
        public void initialize(final PasswordMatchChecker constraintAnnotation) {
        }
        @Override
        public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
            final UserCreationForm user = (UserCreationForm) obj;
            return user.getPassword().equals(user.getConfirmPassword());
        }
}
