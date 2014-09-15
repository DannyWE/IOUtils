package unused;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class MainClass {


    public static void main(String[] args) {


        ValueObject valueObject = new ValueObject("123", "123", null, "123", "123", "123");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ValueObject>> violations = validator.validate(valueObject);


        System.out.println(violations.iterator().next());
    }
}
