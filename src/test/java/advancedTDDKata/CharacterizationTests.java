package advancedTDDKata;

import advancedTDDKata.DTO.UserDTO;
import advancedTDDKata.capatcha.FakeCaptchaService;
import advancedTDDKata.restApi.UserRegistrationController;
import advancedTDDKata.services.EmailVerificationServiceImpl;
import advancedTDDKata.services.UserRegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CharacterizationTests {

    @Autowired
    UserRegistrationServiceImpl userRegistrationService;
    @Autowired
    EmailVerificationServiceImpl emailVerificationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockHttpServletRequest request ;
    private UserRegistrationController controller;


    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("DELETE from user where true");
        // Reset the hibernate_sequence
        jdbcTemplate.execute("ALTER TABLE hibernate_sequence AUTO_INCREMENT = 1");
        TestObserver.reset();

        request = new MockHttpServletRequest();
        request.setRemoteAddr("0.0.0.0");
        controller = new UserRegistrationController(new ModelMapper(), userRegistrationService, new FakeCaptchaService(), emailVerificationService);

    }

    @Test
    public void when_invalid_captcha_response_should_return_bad_request() throws Exception {
        //Arrange
        var invalidRecaptchaResponse = "invalid-recaptcha-response";

        //Act
        var response = this.controller.registerUser(new UserDTO(), request, invalidRecaptchaResponse);

        // Assert
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void when_firstName_is_empty_should_throws_exception(String firstName) {
        // Arrange
        var userDto = new UserDTO(firstName, "Ibrahim", "hassan@orange.com");

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () -> this.controller.registerUser(userDto, request, "valid-recaptcha-response"));
        assertEquals("firstname should not be empty", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void when_lastName_is_empty_should_throws_exception(String lastName) {
        // Arrange
        var userDto = new UserDTO("Hassan", lastName, "hassan@orange.com");

        // Act & Assert
        var exception = assertThrows(RuntimeException.class, () -> controller.registerUser(userDto, request, "valid-recaptcha-response"));
        assertEquals("email should not be empty", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void when_email_is_empty_or_null_should_throws_exception(String email) {
        // Arrange
        var userDto = new UserDTO("Hassan", "Ibrahim", email);

        // Act & Assert
        var exception = assertThrows(Exception.class, () -> controller.registerUser(userDto, request, "valid-recaptcha-response"));
        assertEquals("email should not be empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"hassan@gmail.com", "hassan@orange"})
    public void when_email_is_invalid_should_throws_exception(String email) {
        // Arrange
        var userDto = new UserDTO("Hassan", "Ibrahim", email);

        // Act & Assert
        var exception = assertThrows(Exception.class, () -> controller.registerUser(userDto, request, "valid-recaptcha-response"));
        assertEquals("email is not valid", exception.getMessage());
    }

    @Test
    public void when_email_already_exists_should_throws_exception() throws Exception {
        // Arrange
        var userDto = new UserDTO("Hassan", "Ibrahim", "hassan@orange.com");
        this.controller.registerUser(userDto, request, "valid-recaptcha-response");

        // Act & Assert
        var exception = assertThrows(ResponseStatusException.class, () -> controller.registerUser(userDto, request, "valid-recaptcha-response"));
        assertEquals("400 BAD_REQUEST \"this user is already registered\"", exception.getMessage());
    }

    @Test
    public void when_email_valid_request_should_register_the_user() throws Exception {
        // Arrange
        var userDto = new UserDTO("Hassan", "Ibrahim", "hassan@orange.com");
        var validRecaptchaResponse = "valid-recaptcha-response";

        // Act
        var response = controller.registerUser(userDto, request, validRecaptchaResponse);

        // Assert
        assertEquals(ResponseEntity.status(201).build().getStatusCode(), response.getStatusCode());

        // assert the email is sent
        Object code = TestObserver.getCapturedValue("verificationCode");
        String logs = TestObserver.getLogs();
        assertEquals("Sending email to hassan@orange.com\n" +
                "fromAddress: savemyroaming123@gmail.com\n" +
                "subject: Please verify your registration\n" +
                "senderName: VOXERA\n" +
                "Email content: Dear Hassan,<br>Please click the link below to verify your registration:<br><h3><a href=\"http://localhost/user/verify?code="+code+"\" target=\"_self\">VERIFY</a></h3>Thank you,<br>Your company name.", logs);

        // assert the user is registered
        List<UserDTO> users = controller.findByAll();
        assertEquals(1, users.size());
        assertEquals("Hassan", users.get(0).getFirst_name());
        assertEquals("Ibrahim", users.get(0).getLast_name());
        assertEquals("hassan@orange.com", users.get(0).getEmail());
    }

    @Test
    public void when_correct_verification_code_should_verify_the_user() throws Exception {
        // Arrange
        var userDto = new UserDTO("Hassan", "Ibrahim", "hassan@orange.com");
        var validRecaptchaResponse = "valid-recaptcha-response";
        this.controller.registerUser(userDto, request, validRecaptchaResponse);
        Object code = TestObserver.getCapturedValue("verificationCode");

        // Act
        var response = this.controller.verifyUser(code.toString());

        // Assert
        assertEquals("<h3>Your E-mail has been verified.\n</h3>", response);
    }

    @Test
    public void when_incorrect_verification_code_should_return_error_message() throws Exception {
        // Arrange
        var userDto = new UserDTO("Hassan", "Ibrahim", "hassan@orange.com");
        var validRecaptchaResponse = "valid-recaptcha-response";
        this.controller.registerUser(userDto, request, validRecaptchaResponse);

        // Act
        var response = this.controller.verifyUser("invalid-code");

        // Assert
        assertEquals(" <h3>Something went wrong while verifying your E-mail, please contact support!\n</h3>", response);
    }

}
