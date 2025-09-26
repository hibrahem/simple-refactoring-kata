package advancedTDDKata.restApi;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import advancedTDDKata.DTO.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/user")

public interface UserRegistrationApi {

//    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
   ResponseEntity registerUser(@Valid @RequestBody UserDTO userDto , HttpServletRequest request, @RequestParam String recaptchaResponse) throws Exception;


    @GetMapping("/verify")
    String verifyUser(@RequestParam ("code") String code) ;


// test db in deployement
    @GetMapping
    List<UserDTO> findByAll();

}
