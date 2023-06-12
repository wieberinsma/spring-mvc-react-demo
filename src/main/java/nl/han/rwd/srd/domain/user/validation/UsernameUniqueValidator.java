package nl.han.rwd.srd.domain.user.validation;

import nl.han.rwd.srd.api.model.RegistrationRequest;
import nl.han.rwd.srd.database.model.UserEntity;
import nl.han.rwd.srd.domain.user.spec.repository.UserRepository;
import org.springframework.beans.BeanWrapperImpl;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom Hibernate validator that requires access to the entire object to be able to retrieve values. Therefore, the
 * custom validation annotation {@link FieldUnique} is used at class level.
 *
 * For issues with retrieving data from the database during custom validation, it may be required to inject Hibernate's
 * {@link javax.persistence.EntityManager} to configure entityManager.setFlushMode(FlushModeType.COMMIT) and revert to
 * the original flush mode afterwards.
 */
public class UsernameUniqueValidator implements ConstraintValidator<FieldUnique, RegistrationRequest>
{
    @Inject
    private UserRepository userRepository;

    private String unique;

    private String message;

    @Override
    public void initialize(FieldUnique constraint)
    {
        this.unique = constraint.unique();
        this.message = constraint.message();
    }

    @Override
    public boolean isValid(final RegistrationRequest object, ConstraintValidatorContext context)
    {
        final BeanWrapperImpl wrapper = new BeanWrapperImpl(object);
        String username = (String) wrapper.getPropertyValue(unique);

        UserEntity existingUser = userRepository.findByUsername(username);

        boolean isValid = existingUser == null;
        if (!isValid)
        {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(unique).addConstraintViolation();
        }

        return isValid;
    }

}

