package pl.kowalczyk.nowwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{

    private JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }


    @Override
    public void sendTokenMessage(String to, String token,String mail) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        String link = "http://localhost:8080/active?token="+token;
        String txt = "<h1>Aktywuj swoje konto używając tego linku</h1><br><a href=\""+link+"\">Activate</a>";
        helper.setTo(mail);
        helper.setSubject("Witaj użytkowniku");
       helper.setText(txt,true);
        mailSender.send(msg);

    }
}
