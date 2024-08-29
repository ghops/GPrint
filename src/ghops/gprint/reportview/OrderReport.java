package ghops.gprint.reportview;

import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Order;
import ghops.gprint.tools.PDFCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    @Override
    public void writePDF(String fileName, TableView table) {

        TableView<Order> ot = (TableView<Order>) table;
        ObservableList<Order> items = ot.getSelectionModel().isEmpty() ? ot.getItems() : ot.getSelectionModel().getSelectedItems();

        //List<String> headers = new ArrayList<>();
        TreeMap<String, String> headers = new TreeMap<>();
        TreeMap<String, Float> sizeList = new TreeMap<>();

        List<TreeMap<String, String>> data = new ArrayList<>();

        headers.put("title1", "TARİH");
        headers.put("title2", "SİPARİŞ NO");
        headers.put("title3", "MÜŞTERİ");
        headers.put("title4", "DESEN");
        headers.put("title5", "KALİTE");
        headers.put("title6", "METRE");
        headers.put("title7", "BASILAN");

        sizeList.put("column1", 80f);
        sizeList.put("column2", 90f);
        sizeList.put("column3", 200f);
        sizeList.put("column4", 150f);
        sizeList.put("column5", 150f);
        sizeList.put("column6", 100f);
        sizeList.put("column7", 100f);

        for (int i = 0; i < items.size(); i++) {
            TreeMap<String, String> tm = new TreeMap<>();
            tm.put("value1", items.get(i).getDate().toString());
            tm.put("value2", items.get(i).getOrderNo());

            String customer = (items.get(i).getCustomer().getName().length() > 40) ? items.get(i).getCustomer().getName().substring(0, 40) : items.get(i).getCustomer().getName();
            tm.put("value3", customer);
            tm.put("value4", items.get(i).getProducts().get(0).getDesign());
            tm.put("value5", items.get(i).getProducts().get(0).getFabric().getName());
            tm.put("value6", String.valueOf(items.get(i).getProducts().get(0).getMeters()));
            tm.put("value7", String.valueOf(items.get(i).getProducts().get(0).getPrintedMeters()));
            data.add(tm);

        }

        PDFCreator creator = new PDFCreator(headers, sizeList, data);
        creator.setFileName(fileName);
        creator.write(true, true);
    }

 

    @Override
    public ObservableList getData(LocalDate first, LocalDate last) {
        return this.getData();
    }

}
