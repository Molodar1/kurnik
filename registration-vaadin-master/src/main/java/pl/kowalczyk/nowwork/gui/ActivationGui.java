package pl.kowalczyk.nowwork.gui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kowalczyk.nowwork.model.Token;
import pl.kowalczyk.nowwork.model.User;
import pl.kowalczyk.nowwork.repository.TokenRepo;
import pl.kowalczyk.nowwork.repository.UserRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route("active")
public class ActivationGui extends VerticalLayout implements HasUrlParameter<String> {

    private UserRepo userRepo;
    private TokenRepo tokenRepo;
    @Autowired
    public ActivationGui(UserRepo userRepo,TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> map = queryParameters.getParameters();
        String token = map.get("token").get(0);
        Optional<Token> userToken  = tokenRepo.findTokenByToken(token);
        Label labelUser = new Label();
        if(userToken.isPresent()){
            if(userToken.get().getExpTime().isAfter(LocalDateTime.now())){
            User user = userToken.get().getUser();
            labelUser.setText("Hello," + user.getUsername());
                user.setEnabled(true);
            userRepo.save(user);}
            else {
                labelUser.setText("Your token expired");
            }
        }
        else{
            labelUser.setText("We couldnt find user");
        }
        add(labelUser);

    }
}
