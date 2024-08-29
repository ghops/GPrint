package ghops.gprint.reportview;

import ghops.gprint.models.Order;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public interface IReport<T> {

    public ObservableList<TableColumn> getColumns();

    public ObservableList<T> getData();

    public ObservableList<T> getData(LocalDate first, LocalDate last);

    public void setText(String text);

    public void writePDF(String fileName, TableView<T> table);

}
