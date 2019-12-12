package pl.kowalczyk.nowwork.service;

import javax.mail.MessagingException;

public interface MailService {

    public void sendTokenMessage(String to, String token,String mail) throws MessagingException;

}
