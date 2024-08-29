package ghops.gprint.reportview;

import ghops.gprint.database.SampleDB;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Sample;
import ghops.gprint.tools.PDFCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SampleReport extends AReport implements IReport {

    @Override
    public ObservableList getColumns() {
        ObservableList<TableColumn> list = FXCollections.observableArrayList();

        TableColumn<Sample, LocalDate> date = new TableColumn<>("TARİH");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Sample, Customer> customer = new TableColumn<>("MÜŞTERİ");
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Sample, String> design = new TableColumn<>("DESEN");
        design.setCellValueFactory(new PropertyValueFactory<>("design"));

        TableColumn<Sample, Fabric> fabric = new TableColumn<>("KALİTE");
        fabric.setCellValueFactory(new PropertyValueFactory<>("fabric"));

        TableColumn<Sample, Float> meters = new TableColumn<>("METRE");
        meters.setCellValueFactory(new PropertyValueFactory<>("meters"));

        list.addAll(date, customer, design);

        return list;

    }

    @Override
    public ObservableList getData() {
        return null;
    }

    @Override
    public void setText(String text) {
        super.text = text;
    }

    @Override
    public void writePDF(String fileName, TableView table) {

        TableView<Sample> ot = (TableView<Sample>) table;
        ObservableList<Sample> items = ot.getSelectionModel().isEmpty() ? ot.getItems() : ot.getSelectionModel().getSelectedItems();

        //List<String> headers = new ArrayList<>();
        TreeMap<String, String> headers = new TreeMap<>();
        TreeMap<String, Float> sizeList = new TreeMap<>();

        List<TreeMap<String, String>> data = new ArrayList<>();

        headers.put("title1", "TARİH");
        headers.put("title2", "MÜŞTERİ");
        headers.put("title3", "DESEN");
        headers.put("title4", "KALİTE");
        headers.put("title5", "METRE");

        sizeList.put("column1", 80f);
        sizeList.put("column2", 90f);
        sizeList.put("column3", 200f);
        sizeList.put("column4", 150f);
        sizeList.put("column5", 150f);

        for (int i = 0; i < items.size(); i++) {
            TreeMap<String, String> tm = new TreeMap<>();
            tm.put("value1", items.get(i).getDate().toString());
            String customer = (items.get(i).getCustomer().getName().length() > 40) ? items.get(i).getCustomer().getName().substring(0, 40) : items.get(i).getCustomer().getName();
            tm.put("value2", customer);
            tm.put("value3", items.get(i).getDesign());
            tm.put("value4", items.get(i).getFabric().getName());
            tm.put("value5", String.valueOf(items.get(i).getMeters()));
            data.add(tm);

        }

        PDFCreator creator = new PDFCreator(headers, sizeList, data);
        creator.setFileName(fileName);
        creator.write(true, true);
    }

    @Override
    public ObservableList getData(LocalDate first, LocalDate last) {
        System.out.println(first + " - " + last);
        return FXCollections.observableList(new SampleDB().getAll(first, last));
    }

}
