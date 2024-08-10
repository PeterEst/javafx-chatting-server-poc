package com.peterestephan.chattingserver;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private VBox vbox_messages;

    private Server server;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            server = new Server(new ServerSocket(1234));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        server.receiveMessageFromClient(vbox_messages);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_message.getText();

                if (messageToSend.isEmpty())
                    return;

                Controller.addLabel(messageToSend, vbox_messages, false);
                tf_message.clear();

                server.sendMessageToClient(messageToSend);
            }
        });
    }

    public static void addLabel(String message, VBox vbox_messages, boolean isClient) {
        HBox hbox = new HBox();
        hbox.setAlignment(isClient ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        String style = isClient ?
                """
                        -fx-color: rgb(239, 242, 255);
                        -fx-background-color: rgb(15, 125, 242);
                        -fx-background-radius: 20px;
                        """ :
                """
                        -fx-color: rgb(15, 125, 242);
                        -fx-background-color: rgb(233, 233, 235);
                        -fx-background-radius: 20px;
                        """;
        textFlow.setStyle(style);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hbox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox_messages.getChildren().add(hbox);
            }
        });
    }
}