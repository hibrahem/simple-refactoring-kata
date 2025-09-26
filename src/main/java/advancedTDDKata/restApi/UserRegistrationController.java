package advancedTDDKata.restApi;

import advancedTDDKata.DTO.UserDTO;
import advancedTDDKata.capatcha.CaptchaService;
import advancedTDDKata.capatcha.FakeCaptchaService;
import advancedTDDKata.capatcha.RecaptchaArgument;
import advancedTDDKata.entities.UserEntity;
import advancedTDDKata.services.EmailVerificationService;
import advancedTDDKata.services.UserRegistrationService;
import advancedTDDKata.services.UserRegistrationServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

@RestController
public class UserRegistrationController implements UserRegistrationApi {


    @Autowired
    private ModelMapper dataMapper;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private CaptchaService captchaService;


    public UserRegistrationController(ModelMapper dataMapper, UserRegistrationService userRegistrationService) {
        this.dataMapper = dataMapper;
        this.userRegistrationService = userRegistrationService;
    }

    public UserRegistrationController() {
    }

    public UserRegistrationController(ModelMapper dataMapper, UserRegistrationServiceImpl userRegistrationService, FakeCaptchaService captchaService, EmailVerificationService emailVerificationService) {
        this.dataMapper = dataMapper;
        this.userRegistrationService = userRegistrationService;
        this.captchaService = captchaService;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public ResponseEntity registerUser(UserDTO userDto, HttpServletRequest request, String recaptchaResponse)
            throws Exception {
        //capatcha
        String ip = request.getRemoteAddr();
        RecaptchaArgument recaptchaArguments = new RecaptchaArgument();
        recaptchaArguments.setIp(ip);
        recaptchaArguments.setRecaptchaResponse(recaptchaResponse);
//        String VerifyMessage =
//                (String)captchaService.verifyCaptcha(recaptchaResponse);
        String captchaVerifyMessage =
                (String) captchaService.verifyCaptcha(recaptchaArguments);

        if (StringUtils.isNotEmpty(captchaVerifyMessage)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", captchaVerifyMessage);
            return ResponseEntity.badRequest().body(response);
        }

        UserEntity data = dataMapper.map(userDto, UserEntity.class);
        UserEntity newUser = userRegistrationService.saveNewUserData(data, getSiteURL(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
        public String verifyUser(String code) {
        String verificationString;
        if (emailVerificationService.verify(code)) {

            verificationString = "<h3>Your E-mail has been verified.\n</h3>";
            return verificationString;

        } else {
            verificationString = " <h3>Something went wrong while verifying your E-mail, please contact support!\n</h3>";
            return verificationString;
        }
    }

    @Override
    public List<UserDTO> findByAll() {
        List<UserEntity> contatList = userRegistrationService.getAll();
        List<UserDTO> mapList = contatList.stream().map(person -> dataMapper.map(person, UserDTO.class))
                .collect(Collectors.toList());
        return mapList;

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


}
