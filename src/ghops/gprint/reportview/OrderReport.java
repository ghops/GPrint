package ghops.gprint.reportview;

import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Order;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrderReport extends AReport implements IReport {
    
    @Override
    public ObservableList getColumns() {
        ObservableList<TableColumn> list = FXCollections.observableArrayList();
        
        TableColumn<Order, LocalDate> date = new TableColumn<>("TARİH");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
 
        TableColumn<Order, String> orderNo = new TableColumn<>("SİPARİŞ");
        orderNo.setCellValueFactory(new PropertyValueFactory<>("orderNo"));
        
        TableColumn<Order, Customer> customer = new TableColumn<>("MÜŞTERİ");
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        
        TableColumn<Order, Fabric> fabric = new TableColumn<>("KALİTE");
        fabric.setCellFactory(param -> new TableCell<Order, Fabric>() {
            @Override
            protected void updateItem(Fabric item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                    setStyle("");
                } else {
                    
                    Order o = getTableView().getItems().get(getIndex());
                    setText(o.getProducts().get(0).getFabric().getName());
                    
                }
            }
        });
        
        TableColumn<Order, Integer> meters = new TableColumn<>("METRE");
        meters.setCellFactory(param -> new TableCell<Order, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                    setStyle("");
                } else {
                    Order o = getTableView().getItems().get(getIndex());
                    setText(String.valueOf(o.getProducts().get(0).getMeters()));
                    
                }
            }
        });
        
        TableColumn<Order, Integer> printedMeters = new TableColumn<>("BASILAN");
        printedMeters.setCellFactory(param -> new TableCell<Order, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                    
                    setStyle("");
                } else {
                    Order o = getTableView().getItems().get(getIndex());
                    setText(String.valueOf(o.getProducts().get(0).getPrintedMeters()));
                }
            }
        });
        
        list.addAll(date, customer, orderNo, fabric, meters, printedMeters);
        
        return list;
        
    }
    
    @Override
    public ObservableList getData() {
        return FXCollections.observableList(new OrderDB().getByProducts());
    }
    
    @Override
    public void setText(String text) {
        super.text = text;
    }

 
    
 
    
}
