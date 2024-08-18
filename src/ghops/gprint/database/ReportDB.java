package ghops.gprint.database;

import static ghops.gprint.database.DBConnect.connect;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Order;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDB extends DBConnect {

    public List<Order> getOrdersReport() {
        List<Order> orders = new ArrayList();
        String query = "SELECT IFNULL(prints.date, orders.date) AS date, customers.name AS customer, products.design, fabrics.name AS fabric, printtype.name AS printtype, products.meters, IFNULL(SUM(prints.meters), '0') AS totolPrint, orders.orderNo FROM orders\n"
                + "\n"
                + "LEFT JOIN customers ON orders.customerId=customers.id\n"
                + "LEFT JOIN products ON orders.id=products.orderId\n"
                + "LEFT JOIN fabrics ON products.fabricId=fabrics.id\n"
                + "LEFT JOIN printtype ON products.printTypeId=printtype.id\n"
                + "LEFT JOIN prints ON products.id=prints.productId\n"
                + "\n"
                + "WHERE orders.date>='2023-01-01' AND orders.date<'2024-03-01'\n"
                + "\n"
                + "GROUP BY orders.id, products.id\n"
                + "ORDER BY orders.date DESC ";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Order o = new Order();
                o.setOrderNo(result.getString("orderNo"));
                o.setCustomer(new Customer(0, result.getString("customer")));

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return orders;
    }
}
