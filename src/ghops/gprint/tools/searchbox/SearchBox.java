package ghops.gprint.tools.searchbox;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SearchBox<T> extends AnchorPane {

    @FXML
    private ListView<T> list;

    @FXML
    private TextField search;

    @FXML
    private Button selectButton;
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label title;

    private T t;

    private ObservableList<T> dataList;
    private ObservableList<T> tmp;

    private Stage window;

    public SearchBox(List<T> list) {
        this.dataList = FXCollections.observableArrayList(list);
        this.tmp = FXCollections.observableArrayList(list);

        Platform.runLater(() -> {
            this.init();
        });

    }

    public T open(String title, Stage win) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SearchBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.window = new Stage();
        this.window.initOwner(win);
        this.window.initModality(Modality.APPLICATION_MODAL);
        this.window.setTitle(title);
        this.title.setText(title);
        this.window.setWidth(400);
        this.window.setHeight(375);
        this.window.initStyle(StageStyle.UNDECORATED);
        

        this.list.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                if (me.getClickCount() > 1) {
                    this.t = list.getSelectionModel().getSelectedItem();
                    this.window.close();
                }
            }
        });

        selectButton.setOnAction((action) -> {
            System.out.println("dgdsgdfgdfgdfgdfs");
        });

        try {
            Scene scene = new Scene(fxmlLoader.load());

            this.window.setScene(scene);
            this.window.showAndWait();

        } catch (IOException ex) {
            System.out.println(ex);
        }

        return t;

    }

    private void init() {
        this.selectButton.setOnAction((action) -> {
            this.window.close();
        });

        this.cancelButton.setOnAction((action) -> {
            this.t = null;
            this.window.close();
        });

        this.list.setItems(this.tmp);

        this.list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.t = newValue;
            
        });

        this.list.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                if (me.getClickCount() == 2) {
                    this.window.close();
                }
            }
        });

        this.search.textProperty().addListener((observable, oldValue, newValue) -> {
            this.tmp.clear();
            for (T item : this.dataList) {
                if (item.toString().toUpperCase().contains(newValue.toUpperCase())) {
                    this.tmp.add(item);
                }
            }
            
            if(!this.tmp.isEmpty()) this.list.getSelectionModel().select(0);

        });

        this.search.setOnKeyPressed((key) -> {
            if(key.getCode()==KeyCode.ENTER){
                if(!this.list.getSelectionModel().isEmpty()) this.window.close();
            }
        });
        this.search.requestFocus();

    }

}
