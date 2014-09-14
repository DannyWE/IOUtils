package base

import javax.validation.Validation

object javaxValidator extends StreamValidator(Validation.buildDefaultValidatorFactory.getValidator)
