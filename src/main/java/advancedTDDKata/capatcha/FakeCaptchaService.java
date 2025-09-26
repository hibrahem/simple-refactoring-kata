package advancedTDDKata.capatcha;


public class FakeCaptchaService implements CaptchaService<RecaptchaArgument,String>{

    @Override
    public String verifyCaptcha(RecaptchaArgument capatchaArgumentObject) {
        if (capatchaArgumentObject.getRecaptchaResponse().equals("valid-recaptcha-response") && capatchaArgumentObject.getIp().equals("0.0.0.0"))
            return null;
        return capatchaArgumentObject.getRecaptchaResponse();
    }
}
