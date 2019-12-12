package pl.kowalczyk.nowwork.gui;


import com.google.common.hash.Hashing;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kowalczyk.nowwork.model.Token;
import pl.kowalczyk.nowwork.model.User;
import pl.kowalczyk.nowwork.repository.TokenRepo;
import pl.kowalczyk.nowwork.repository.UserRepo;
import pl.kowalczyk.nowwork.service.MailService;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;


@Route("registration")
public class RegistrationGui extends VerticalLayout {

    private UserRepo userRepo;
    private MailService mailService;
    private TokenRepo tokenRepo;

    @Autowired
    public RegistrationGui(UserRepo userRepo, MailService mailService,TokenRepo tokenRepo) {
        this.userRepo = userRepo;
        this.mailService = mailService;
        this.tokenRepo = tokenRepo;
        Label label = new Label("There was an error");
        label.setVisible(false);
        TextArea textAreaUsername = new TextArea("Username");
        textAreaUsername.setPlaceholder("Username");
        textAreaUsername.setRequired(true);
        EmailField emailField = new EmailField("Email");
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("Enter password");
        passwordField.setRequired(true);
        PasswordField repeatPasswordField = new PasswordField();
        repeatPasswordField.setLabel("Password");
        repeatPasswordField.setPlaceholder("Reapeat password");
        repeatPasswordField.setLabel("Repeat password");
        repeatPasswordField.setRequired(true);
        Button buttonSubmit = new Button("Submit");
        buttonSubmit.addClickListener(event->
        {
            if(isValid(textAreaUsername.getValue(),emailField.getValue(),passwordField.getValue(),repeatPasswordField.getValue())){
                try {
                    saveUser(textAreaUsername.getValue(),emailField.getValue(),passwordField.getValue());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
           else {
                label.setVisible(true);
            }


        });
        add(textAreaUsername,emailField,passwordField,repeatPasswordField,buttonSubmit,label);

    }



    private Boolean checkTextLength(AbstractField field,int min,int max){
        String text = (String) field.getValue();

        if(text.trim().length()>=min && text.trim().length()<=max){
            return true;
        }
        return false;
    }


    private void saveUser(String username,String email, String password) throws MessagingException {
        String hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

        User user = new User(username, email, hashedPassword);
        user.setActive(false);
        Token userToken = new Token(user, LocalDateTime.now(),LocalDateTime.now().plusHours(24));
        userToken.generateToken();
        mailService.sendTokenMessage("fasdf",userToken.getToken(),user.getEmail());
        userRepo.save(user);
        tokenRepo.save(userToken);


    }

    public Boolean isValid(String username, String email, String password, String rePassword){
        if(!password.equals(rePassword)){
            return false;

        }

        if(username.trim().length()<3){
            return false;
        }

        if (password.trim().length()<8){
            return false;
        }

        if (!email.contains("@")){
            return false;
        }
        if(!email.split("@")[1].contains(".")){
            return false;
        }
        Optional<User> userByUsername = userRepo.findUserByUsername(username);
        if(userByUsername.isPresent()){
            return false;
        }
        Optional<User> userByEmail = userRepo.findUserByEmail(email);
        if(userByEmail.isPresent()){
            return false;
        }

        return true;
    }





}