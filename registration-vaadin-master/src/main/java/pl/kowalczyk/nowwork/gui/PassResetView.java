import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("reset")
public class PassResetView extends VerticalLayout {

    @Autowired
    public PassResetView(){
        Label label = new Label("Password reset");
        PasswordField passwordField1 = new PasswordField("New password");
        PasswordField passwordField2 = new PasswordField("Repeat new password");
        Button button = new Button("Submit");
        add(label, passwordField1, passwordField2, button);
    }

}