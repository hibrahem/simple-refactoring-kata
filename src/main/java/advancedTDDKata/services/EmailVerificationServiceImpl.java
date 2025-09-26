package advancedTDDKata.services;

import advancedTDDKata.TestObserver;
import advancedTDDKata.repositories.UserRepository;
import advancedTDDKata.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private UserRepository repo;


    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(UserEntity user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "savemyroaming123@gmail.com";
        String senderName = "VOXERA";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirst_name());
        String verifyURL = siteURL + "/user/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        TestObserver.captureValue("verificationCode",user.getVerificationCode());
        TestObserver.log("Sending email to " + toAddress);
        TestObserver.log("fromAddress: " + fromAddress);
        TestObserver.log("subject: " + subject);
        TestObserver.log("senderName: " + senderName);
        TestObserver.log("Email content: " + content);
        mailSender.send(message);
    }

    @Override
    public boolean verify(String verificationCode) {
        UserEntity user = repo.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);
            return true;
        }
    }
}
