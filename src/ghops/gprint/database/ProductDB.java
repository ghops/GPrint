package ghops.gprint.database;

import ghops.gprint.models.Product;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.mariadb.jdbc.Statement;

public class ProductDB extends DBConnect {

    public Product save(Product p) {
        if (p.getId() > 0) {
            return this.update(p);
        } else {
            return this.add(p);
        }
    }

    public Product add(Product p) {

        try (PreparedStatement statement = connect().prepareStatement("INSERT INTO products SET orderId=?, fabricId=?, design=?, description=?, meters=?, weight=?, width=?, STATUS=?, printTypeId=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, p.getOrderId());
            statement.setInt(2, p.getFabric().getId());
            statement.setString(3, p.getDesign());
            statement.setString(4, p.getDescription());
            statement.setDouble(5, p.getMeters());
            statement.setDouble(6, p.getWeight());
            statement.setDouble(7, p.getWidth());
            statement.setInt(8, p.getStatusValue());
            statement.setInt(9, p.getPrintType().getId());

            if (statement.executeUpdate() < 1) {
                return null;
            }

            System.out.println("ghops.gprint.database.ProductDB.add()");
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                    System.err.println(generatedKeys.getInt(1));
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return p;
    }

    public Product update(Product p) {
        System.out.println("ghops.gprint.database.ProductDB.update()");

        try (PreparedStatement statement = connect().prepareStatement("UPDATE products SET orderId=?, fabricId=?, design=?, description=?, meters=?, weight=?, width=?, status=?, printTypeId=? WHERE id=?")) {
            statement.setInt(1, p.getOrderId());
            statement.setInt(2, p.getFabric().getId());
            statement.setString(3, p.getDesign());
            statement.setString(4, p.getDescription());
            statement.setDouble(5, p.getMeters());
            statement.setDouble(6, p.getWeight());
            statement.setDouble(7, p.getWidth());
            statement.setInt(8, p.getStatusValue());
            statement.setInt(9, p.getPrintType().getId());
            statement.setInt(10, p.getId());
            //Date date = Date.from(o.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return p;

        /*
        try (PreparedStatement statement = connect().prepareStatement("UPDATE products SET orderId=?, fabricId=?, design=?, description=?, meters=?, weight=?, width=?, status=?, printTypeId=? WHERE id=?")) {
            statement.setInt(1, p.getOrderId());
            statement.setInt(2, p.getFabric().getId());
            statement.setString(3, p.getDesign());
            statement.setString(4, p.getDescrption());
            statement.setInt(5, p.getMeters());
            statement.setInt(6, p.getWeight());
            statement.setInt(7, p.getWidth());
            statement.setInt(8, p.getStatus().getValue());
            statement.setInt(9, p.getPrintType().getId());
            statement.setInt(10, p.getId());
            //Date date = Date.from(o.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }*/
    }

    public boolean delete(int id) {
        try (PreparedStatement statement = connect().prepareStatement("DELETE FROM products WHERE id=?")) {
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
        /*
        String query = "SELECT products.*, fabrics.name AS fabricName FROM products LEFT JOIN fabrics ON fabrics.id=products.fabricId  WHERE orderId=?";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Product p = new Product(orderId);
                p.setId(result.getInt("id"));
                p.setDesign(result.getString("design"));
                p.setOrderId(orderId);
                p.setMeters(result.getInt("meters"));
                p.setFabric(new Fabric(result.getInt("fabricId"), result.getString("fabricName")));
                products.add(p);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }
         */
        return products;
    }
}
