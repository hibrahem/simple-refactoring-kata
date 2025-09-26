package advancedTDDKata.services;

import advancedTDDKata.entities.UserEntity;
import advancedTDDKata.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.management.RuntimeErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
 public class UserRegistrationServiceImplTest {

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;


    @InjectMocks
    private EmailVerificationServiceImpl emailVerificationService;

    @Mock
    private UserRepository userRepo;

    public UserEntity prepareValidUserEntity() {
        UserEntity user = new UserEntity();
       user.setFirst_name("firstName");
       user.setLast_name("lastName");
       user.setEmail("email@orange.com");
        return user;

    }

 @Test(expected = RuntimeErrorException.class)
   public void userDataNullOrEmptyValidationFirstNameTest (){
        UserEntity user = new UserEntity();
        user.setEmail("email@orange.com");
        user.setLast_name("lastName");
        userRegistrationService.userDataNullOrEmptyValidation(user);
 }

    @Test(expected = RuntimeErrorException.class)
    public void userDataNullOrEmptyValidationEmailTest (){
        UserEntity user = new UserEntity();
        user.setFirst_name("firstname");
        user.setLast_name("lastName");
        userRegistrationService.userDataNullOrEmptyValidation(user);

    }

    @Test(expected = RuntimeErrorException.class)
    public void userDataNullOrEmptyValidationObjectlTest (){
        UserEntity user = new UserEntity();
        userRegistrationService.userDataNullOrEmptyValidation(user);

    }

    @Test
    public void testIsValidusername() {
        UserEntity user = new UserEntity();
        user.setEmail("ayhaga@orange.com");
        assertEquals(userRegistrationService.isValidateUsername(user.getEmail()),true);
    }

    @Test
    public void testIsNotValidusername() {
        UserEntity user = new UserEntity();
        user.setEmail("ayhaga@yyy.com");
        assertEquals(userRegistrationService.isValidateUsername(user.getEmail()),false);
    }






}