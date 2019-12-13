package pl.kowalczyk.nowwork.service;

import javax.mail.MessagingException;

public interface MailService {

    public void sendTokenMessage( String token,String mail) throws MessagingException;

}
