package ghops.gprint.orders.orderview;

import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Order;
import ghops.gprint.orders.orderform.OrderForm;
import ghops.gprint.orders.orderitem.OrderItem;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OrderView extends StackPane {

    @FXML
    private VBox orderList;

    @FXML
    private Button addButton;

    @FXML
    private ScrollPane pane;

    @FXML
    private Button upButton;
    @FXML
    private Button downButton;

    @FXML
    private Button moreButton;
    private int first = 0;
    private int quantity = 25;
    private int last = quantity;

    private double viewStage = 0;
    public static String previewsPath = "file:" + System.getProperty("user.dir") + "/previews/";
    private ObservableList<Order> list = FXCollections.observableList(new OrderDB().getOrders());
    private ObservableList<Node> items = FXCollections.observableArrayList();

    Service<ObservableList<OrderItem>> orderService = new Service() {
        @Override
        protected Task<ObservableList<Node>> createTask() {
            System.out.println("First: " + first);
            return new OrderTask(items, list.subList(first, last));
        }

    };

    public OrderView() {

        //last = quantity;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init() {
        orderList.getChildren().setAll(items);
        Bindings.bindContentBidirectional(items, orderList.getChildren());

        orderService.start();
        moreButton.disableProperty().bind(orderService.runningProperty());

        moreButton.setOnAction((action) -> {
            first += quantity;
            last += quantity;
            if (last > list.size()) {
                last = list.size();
            }

            orderService.reset();
            orderService.start();
        });

        orderList.heightProperty().addListener(observable -> pane.setVvalue(viewStage));
        upButton.setOnAction((action) -> {
            viewStage = 0;
            pane.setVvalue(0);
        });
        downButton.setOnAction((action) -> {
            viewStage = 1;
            pane.setVvalue(1);
        });

        addButton.setOnAction((action) -> {
            Order o = new OrderForm(new Order()).open();
            if(o!=null){
                System.out.println("Ekleniyor : " + o);
            }
        });
    }

}
