package ghops.gprint.reportview;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public interface IReport<T> {

    public ObservableList<TableColumn> getColumns();

    public ObservableList<T> getData();

    public void setText(String text);

 
}
