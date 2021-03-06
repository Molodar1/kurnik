package pl.kowalczyk.nowwork.gui;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.context.annotation.Bean;
import pl.kowalczyk.nowwork.MessageList;
import pl.kowalczyk.nowwork.model.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@StyleSheet("frontend://styles/styles.css")
@Route("chat")
@Push
public class ChatGui extends VerticalLayout {


    private  UnicastProcessor<ChatMessage> publisher;
    private  Flux<ChatMessage> messages;

    private String username;

    public ChatGui(UnicastProcessor<ChatMessage> publisher,Flux<ChatMessage> messages ) {
        this.publisher = publisher;
        this.messages = messages;
        addClassName("chat-view");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 header = new H1("Kurnik chat");
        header.getElement().getThemeList().add("dark");

        add(header);

        askUsername();
    }

    private void askUsername() {
        HorizontalLayout layout = new HorizontalLayout();
        TextField usernameField = new TextField();
        Button startButton = new Button("Start chat");

        layout.add(usernameField, startButton);

        startButton.addClickListener(click -> {
            username = usernameField.getValue();
            remove(layout);
            showChat();
        });

        add(layout);
    }

    private void showChat() {


        add(messageList, createInputLayout());
        expand(messageList);


        messages.subscribe(this::addMessage);
    }

    private void addMessage(ChatMessage message) {

        if (message.getMessage().length() > 100) {
            Notification notification = new Notification("Message is too long", 3000);
            notification.open();
            return;
        }
        Label username = new Label();
        username.setText(message.getFrom() + " : ");
        if (message.getFrom().equals(this.username)) {
            username.getStyle().set("color", "red");
        }
        Label messageText = new Label();
        messageText.setText(message.getMessage());
        getUI().ifPresent(ui ->
                ui.access(() -> messageList.add(new HorizontalLayout(username, messageText))));


    }
    private Component createInputLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");

        TextField messageField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layout.add(messageField, sendButton);
        layout.expand(messageField);

        sendButton.addClickListener(click -> {
            publisher.onNext(new ChatMessage(username, messageField.getValue()));
            messageField.clear();
            messageField.focus();
        });
        messageField.focus();

        return layout;
    }

}

