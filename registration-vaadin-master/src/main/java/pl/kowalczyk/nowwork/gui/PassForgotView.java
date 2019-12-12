import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("forgot")
public class PassForgotView extends VerticalLayout {

    @Autowired
    public PassForgotView(){
        Label label = new Label("Password reset");
        EmailField emailField = new EmailField("Email");
        Button button = new Button("Verify");
        add(label, emailField, button);
    }

}