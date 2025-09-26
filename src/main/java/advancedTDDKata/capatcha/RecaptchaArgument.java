package advancedTDDKata.capatcha;

import lombok.Data;

@Data
public class RecaptchaArgument {
    private String ip;
    private String recaptchaResponse;
}
