package fi.aalto.ekanban.validators;

import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckWipLimitsValidator implements ConstraintValidator<CheckWipLimits, Map<String, Integer>> {

    @Override
    public void initialize(CheckWipLimits constraintAnnotation) { }

    @Override
    public boolean isValid(Map<String, Integer> value, ConstraintValidatorContext context) {
        for (Map.Entry<String, Integer> entry : value.entrySet()) {
            if (entry.getValue() < 0) {
                context.buildConstraintViolationWithTemplate("Negative wip limit value");
                return false;
            }
        }
        return true;
    }
}
