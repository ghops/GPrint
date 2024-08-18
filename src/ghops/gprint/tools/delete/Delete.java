package ghops.gprint.tools.delete;

import java.io.IOException;
import java.util.Collection;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Delete extends AnchorPane {

    @FXML
    private VBox confirmation;
    @FXML
    private VBox rows;

    @FXML
    private Button cancelButton;

    @FXML
    private Label code;

    @FXML
    private Label message;

    @FXML
    private Button okButton;

    @FXML
    private TextField confirmCode;

    @FXML
    private Label title;

    private Stage window;

    private String titleText = "";
    private String messageText = "";
    private boolean validation = true;

    public Delete(String title, String message) {

        this.titleText = title;
        this.messageText = message;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Delete.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {

            fxmlLoader.load();
            this.window = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            this.window.setScene(scene);
            this.window.initModality(Modality.APPLICATION_MODAL);

            this.window.initStyle(StageStyle.UNDECORATED);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    boolean result = false;

    public boolean open(boolean validation) {
        this.validation = validation;
        return this.open();
    }

    public boolean open() {

        this.init();

        this.window.showAndWait();
        return result;
    }

    private void initConfirmation() {
        this.code.setText(this.getAlphaNumericString(5));
        this.confirmCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.code.getText().equals(newValue)) {
                this.okButton.setDisable(false);
            } else {
                this.okButton.setDisable(true);
            }
        });
        if (this.validation) {
            this.confirmCode.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;
            }));

            this.confirmCode.setOnKeyPressed((key) -> {
                if (key.getCode() == KeyCode.ESCAPE) {
                    this.window.close();
                }
            });

            this.confirmCode.requestFocus();

        } else {
            this.confirmCode.setText(this.code.getText());
            this.okButton.requestFocus();
        }

    }

    private void init() {
        this.getScene().setOnKeyPressed((key) -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                this.window.close();
            }
        });

        this.title.setText(this.titleText);
        this.message.setText(this.messageText);

        this.okButton.setOnAction((action) -> {
            this.result = true;
            this.window.close();
        });

        this.cancelButton.setOnAction((action) -> {
            this.result = false;
            this.window.close();
        });

        this.initConfirmation();
    }

    private String getAlphaNumericString(int n) {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
