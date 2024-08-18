package ghops.gprint.database;

import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Order;
import ghops.gprint.models.PrintType;
import ghops.gprint.models.Product;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mariadb.jdbc.Statement;

public class OrderDB extends DBConnect {

    public static final String GROUP_ORDER = "products.id";
    public static final String GROUP_CUSTOMER = "orders.customerId";
    public static final String GROUP_FABRIC = "products.fabricId";
    public static final String GROUP_DAY = "DAY(prints.date)";
    public static final String GROUP_MONTH = "MONTH(prints.date)";
    public static final String GROUP_YEAR = "YEAR(prints.date)";
    public static final String GROUP_PRINT_TYPE = "products.printTypeId";

    public static final String REPORT = """
                       SELECT IFNULL(prints.date, orders.date) AS date, customers.name AS customer, customers.id AS customerId, products.design, fabrics.name AS fabric, printtype.name AS printtype, products.meters, IFNULL(SUM(prints.meters), 0) AS totalPrint, orders.orderNo FROM orders
                       
                       LEFT JOIN customers ON orders.customerId=customers.id
                       LEFT JOIN products ON orders.id=products.orderId
                       LEFT JOIN fabrics ON products.fabricId=fabrics.id
                       LEFT JOIN printtype ON products.printTypeId=printtype.id
                       LEFT JOIN prints ON products.id=prints.productId
                       
                       WHERE prints.date>=? AND prints.date<?
                       """;

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList();
        String query = "SELECT orders.*, customers.name AS customerName FROM orders LEFT JOIN customers ON customers.id=orders.customerId ORDER BY orders.date DESC, orders.id DESC  ";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Order o = new Order();
                o.setId(result.getInt("id"));
                o.setOrderNo(result.getString("orderNo"));
                o.setDocket(result.getString("docket"));
                o.setDescription(result.getString("description"));
                o.setCustomer(new Customer(result.getInt("customerId"), result.getString("customerName")));
                o.setDate(result.getDate("date").toLocalDate());
                o.setDelivery(result.getDate("delivery").toLocalDate());
                o.setStatus(result.getInt("status"));
                o.setProducts(FXCollections.observableArrayList(this.getProducts(o.getId())));

                orders.add(o);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return orders;
    }

    public List<Order> getByProducts() {
        List<Order> orders = new ArrayList();
        String query = """
                      SELECT orders.date, orders.orderNo, customers.name AS customer, products.design, fabrics.name AS fabric, products.meters, SUM(prints.meters) AS printedMeters FROM orders
                      LEFT JOIN products ON products.orderId=orders.id
                      LEFT JOIN fabrics ON fabrics.id=products.fabricId
                      LEFT JOIN customers ON customers.id=orders.customerId
                      LEFT JOIN prints ON prints.productId=products.id
                      GROUP BY products.id
                      ORDER BY orders.date DESC, orders.id ASC
                      """;

        try {
            PreparedStatement statement = connect().prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) { 

                Order o = new Order(); 
                o.setOrderNo(result.getString("orderNo"));  
                o.setCustomer(new Customer(0, result.getString("customer")));
                o.setDate(result.getDate("date").toLocalDate());  
                Product p = new Product();
                p.setDesign(result.getString("design"));
                p.setMeters(result.getInt("meters"));
                p.setPrintedMeters(result.getInt("printedMeters"));
                p.setFabric(new Fabric(0, result.getString("fabric")));
                o.getProducts().setAll(p);
                orders.add(o);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return orders;
    }

    /*
    
    SELECT orders.date, orders.orderNo, customers.name AS customer, products.design, fabrics.name AS fabric, products.meters, SUM(prints.meters) AS printedMeters FROM orders
    LEFT JOIN products ON products.orderId=orders.id
    LEFT JOIN fabrics ON fabrics.id=products.fabricId
    LEFT JOIN customers ON customers.id=orders.customerId
    LEFT JOIN prints ON prints.productId=products.id
    GROUP BY products.id
    ORDER BY orders.date DESC, orders.id ASC
    
     */
    public Order save(Order o) {
        if (o.getId() > 0) {
            this.update(o);
        } else {
            this.add(o);
        }
        return o;
    }

    public Order add(Order o) {

        try (PreparedStatement statement = connect().prepareStatement("INSERT INTO orders SET orderNo=?, docket=?, description=?, customerId=?, date=?, delivery=?, status=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, o.getOrderNo());
            statement.setString(2, o.getDocket());
            statement.setString(3, o.getDescription());
            statement.setInt(4, o.getCustomer().getId());
            statement.setString(5, o.getDate().toString());
            statement.setString(6, o.getDelivery().toString());
            statement.setInt(7, o.getStatusValue());

            //Date date = Date.from(o.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (statement.executeUpdate() < 1) {
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    o.setId(generatedKeys.getInt(1));
                    System.err.println(generatedKeys.getInt(1));
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return o;
    }

    public Order update(Order o) {
        try (PreparedStatement statement = connect().prepareStatement("UPDATE orders SET orderNo=?, docket=?, description=?, customerId=?, date=?, delivery=?, STATUS=? WHERE id=?")) {
            statement.setString(1, o.getOrderNo());
            statement.setString(2, o.getDocket());
            statement.setString(3, o.getDescription());
            statement.setInt(4, o.getCustomer().getId());
            statement.setString(5, o.getDate().toString());
            statement.setString(6, o.getDelivery().toString());
            statement.setInt(7, o.getStatusValue());
            statement.setInt(8, o.getId());
            //Date date = Date.from(o.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return o;
    }

    public boolean delete(int id) {
        System.out.println("db delete");
        try (PreparedStatement statement = connect().prepareStatement("DELETE FROM orders WHERE id=?")) {
            statement.setInt(1, id);

            //Date date = Date.from(o.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println(e);

        }

        return false;
    }

    private List<Product> getProducts(int orderId) {
        List<Product> products = new ArrayList();
        String query = "SELECT products.*, fabrics.name AS fabricName, fabrics.name2 AS fabricName2, SUM(prints.meters) AS printedMeters, printtype.name AS printTypeName FROM products LEFT JOIN fabrics ON fabrics.id=products.fabricId LEFT JOIN prints ON prints.productId=products.id LEFT JOIN printtype ON printtype.id=products.printTypeId  WHERE orderId=? GROUP BY products.id";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Product p = new Product(orderId);
                p.setId(result.getInt("id"));
                p.setDesign(result.getString("design"));
//                p.setOrderId(orderId);
                p.setWidth(result.getDouble("width"));
                p.setWeight(result.getDouble("weight"));
                p.setPrintedMeters(result.getDouble("printedMeters"));
                p.setDescription(result.getString("description"));
                p.setMeters(result.getDouble("meters"));
                p.setStatus(result.getInt("status"));
                p.setPrintType(new PrintType(result.getInt("printTypeId"), result.getString("printTypeName")));
                Fabric f = new Fabric(result.getInt("fabricId"), result.getString("fabricName"));
                f.setName2(result.getString("fabricName2"));

                p.setFabric(f);

                products.add(p);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return products;
    }

    /*
    public List<Order> getReport(LocalDate first, LocalDate last, Customer customer) {
        List<Order> orders = new ArrayList();
        String query = CUSTOMER_REPORT;
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            statement.setString(1, first.toString());
            statement.setString(2, last.toString());
            statement.setInt(3, customer.getId());
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Order o = new Order();
                o.setOrderNo(result.getString("orderNo"));
                o.setCustomer(new Customer(result.getInt("customerId"), result.getString("customer")));
                Product p = new Product(0);
                p.setDesign(result.getString("design"));
                p.setFabric(new Fabric(0, result.getString("fabric")));
                p.setPrintType(new PrintType(0, result.getString("printType")));
                p.setPrintedMeters(result.getDouble("totalPrint"));
                System.out.println(result.getString("totalPrint"));
                o.setDate(result.getDate("date").toLocalDate());
                o.getProducts().add(0, p);
                orders.add(o);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return orders;
    }
     */
    public List<Order> getReport(LocalDate first, LocalDate last, String group) {
        List<Order> orders = new ArrayList();

        String query = REPORT + "GROUP BY " + group;

        query += " \nORDER BY orders.date DESC";
        System.out.println(query);

        try {
            PreparedStatement statement = connect().prepareStatement(query);
            statement.setString(1, first.toString());
            statement.setString(2, last.toString());
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Order o = new Order();
                if (group == GROUP_ORDER) {
                    o.setOrderNo(result.getString("orderNo"));
                } else {
                    o.setOrderNo("");
                }

                if (group == GROUP_ORDER || group == GROUP_CUSTOMER) {
                    o.setCustomer(new Customer(result.getInt("customerId"), result.getString("customer")));
                } else {
                    o.setCustomer(null);
                }

                Product p = new Product(0);
                p.setDesign(result.getString("design"));
                if (group == GROUP_FABRIC || group == GROUP_ORDER) {
                    p.setFabric(new Fabric(0, result.getString("fabric")));
                } else {
                    p.setFabric(null);
                }

                if (group == GROUP_ORDER || group == GROUP_PRINT_TYPE) {
                    p.setPrintType(new PrintType(0, result.getString("printType")));
                } else {
                    p.setFabric(null);
                }

                p.setPrintedMeters(result.getDouble("totalPrint"));
                p.setMeters(result.getDouble("meters"));
                if (group == GROUP_ORDER || group == GROUP_DAY || group == GROUP_MONTH || group == GROUP_YEAR) {
                    o.setDate(result.getDate("date").toLocalDate());
                } else {
                    o.setDate(null);
                }

                o.getProducts().add(0, p);
                orders.add(o);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return orders;
    }
}
