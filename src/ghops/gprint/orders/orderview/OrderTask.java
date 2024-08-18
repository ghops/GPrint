package ghops.gprint.orders.orderview;

import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Order;
import ghops.gprint.models.PrintType;
import ghops.gprint.models.Product;
import ghops.gprint.orders.orderitem.OrderItem;
import ghops.gprint.orders.orderitem.productItem.ProductItem;
import ghops.gprint.tools.delete.Delete;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;

public class OrderTask extends Task<ObservableList<Node>> {

    private ObservableList<Node> items;
    private List<Order> list;

    public OrderTask(ObservableList<Node> items, List<Order> list) {
        System.out.println(list.size());
        this.items = items;
        this.list = list;
    }

    @Override
    protected ObservableList<Node> call() {
        for (Order order : list) {
            if (isCancelled()) {
                break;
            }
            OrderItem oi = new OrderItem(order);
            oi.getDeleteButton().setOnAction((action) -> {
                boolean del = new Delete("Sipariş siliniyor...", order.getOrderNo() + " nolu sipariş tüm alt kayıtlarıyla birlik te silinecek!").open();
                if (del) {
                    boolean res = new OrderDB().delete(order.getId());
                    items.remove(oi);
                }
            });

            Platform.runLater(() -> {

                items.add(oi);
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        return items;
    }

}
