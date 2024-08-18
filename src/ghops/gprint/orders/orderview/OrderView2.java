package ghops.gprint.orders.orderview;

import ghops.gprint.database.FabricDB;
import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Order;
import ghops.gprint.models.Product;
import ghops.gprint.orders.orderitem.productItem.ProductItem;
import java.io.IOException;
import java.time.LocalDate;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;

public class OrderView2 extends AnchorPane {

    @FXML
    private TableView<Order> table;

    private ObservableList<Order> orders;

    private TableColumn<Order, ObservableList<Product>> colProduct;
    private TableColumn<Order, Customer> colCustomer;
    private TableColumn<Order, String> colOrderNo;
    private TableColumn<Order, LocalDate> date;
    private TableColumn<Order, LocalDate> delivery;
    private TableColumn productColumns = new TableColumn<>("prds");
    private TableColumn<Product, String> colDesign = new TableColumn<>("DESEN");

    public OrderView2() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderView2.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.init();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init() {
        this.colProduct = new TableColumn<>("ÜRETİMLER");
        this.colProduct.setCellValueFactory(new PropertyValueFactory<>("products"));
        this.colProduct.setCellFactory((tableColumn) -> {
            TableCell<Order, ObservableList<Product>> cell = new TableCell<>() {
                @Override
                protected void updateItem(ObservableList<Product> list, boolean bln) {
                    if (list == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        VBox box = new VBox();
                        box.setSpacing(10);
                        for (Product p : list) {
                            box.getChildren().add(new ProductItem(p));
                        }
                        setGraphic(box);
                    }
                }

            };
            return cell;

        });

        this.colCustomer = new TableColumn<>("MÜŞTERİ");
        this.colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));

        this.colOrderNo = new TableColumn<>("SİPARİŞ");
        this.colOrderNo.setCellValueFactory(new PropertyValueFactory("orderNo"));

        this.colOrderNo.setCellFactory((tableColumn) -> {
            TableCell<Order, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String str, boolean bln) {
                    if (str == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setAlignment(Pos.TOP_LEFT);
                        setText(str);
                        setGraphic(new Button("looper"));
                    }
                }

            };
            return cell;

        });

        this.date = new TableColumn<>("TARİH");
        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));

        this.delivery = new TableColumn<>("TERMİN");
        this.delivery.setCellValueFactory(new PropertyValueFactory<>("delivery"));

        this.orders = FXCollections.observableList(new OrderDB().getOrders());
        this.table.setItems(this.orders);

        this.productColumns.getColumns().add(this.colDesign);

        this.table.getColumns().addAll(colOrderNo, colCustomer, colProduct, productColumns, date, delivery);
        this.table.refresh();

    }
}
