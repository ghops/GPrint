package ghops.gprint.database;

import ghops.gprint.models.Customer;
import ghops.gprint.tools.TextEdit;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.mariadb.jdbc.Statement;

public class CustomerDB extends DBConnect {

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList();
        String query = "SELECT * FROM customers ORDER BY name DESC";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Customer customer = new Customer();
                customer.setId(result.getInt("id"));
                customer.setName(result.getString("name"));
                customer.setAddress(result.getString("address"));
                customer.setCity(result.getString("city"));
                customer.setEmail(result.getString("email"));
                customer.setFax(result.getString("fax"));
                customer.setTelephone(result.getString("telephone"));
                customer.setWeb(result.getString("web"));
                customer.setStatus(result.getInt("status"));
                list.add(customer);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

    public List<Customer> getAll(boolean w) {
        List<Customer> list = new ArrayList();
        String query = "SELECT * FROM customers ORDER BY name ASC";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Customer customer = new Customer();
                customer.setId(result.getInt("id"));
                customer.setName(result.getString("name"));
                list.add(customer);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

    public Customer add(Customer c) {

        try (PreparedStatement statement = connect().prepareStatement("INSERT INTO customers SET name=?, email=?, telephone=?, web=?, city=?, address=?, fax=?, status=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, TextEdit.toUpper(c.getName()));
            statement.setString(2, c.getEmail());
            statement.setString(3, c.getTelephone());
            statement.setString(4, c.getWeb());
            statement.setString(5, c.getCity());
            statement.setString(6, c.getAddress());
            statement.setString(7, c.getFax());
            statement.setInt(8, c.getStatusValue());

            if (statement.executeUpdate() < 1) {
                return null;
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.setId(generatedKeys.getInt(1));
                    c.setName(TextEdit.toUpper(c.getName()));
                    System.err.println(generatedKeys.getInt(1));
                } else {
                    return null;
                }

            } catch (SQLException eq) {

                System.err.println(eq);
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return c;
    }

    public Customer update(Customer c) {
        System.out.println("update");
        try (PreparedStatement statement = connect().prepareStatement("UPDATE customers SET name=?, email=?, telephone=?, status=?, city=?, fax=?, address=?, web=? WHERE id=?")) {
            statement.setString(1, c.getName());
            statement.setString(2, c.getEmail());
            statement.setString(3, c.getTelephone());
            statement.setInt(4, c.getStatusValue());
            statement.setString(5, c.getCity());
            statement.setString(6, c.getFax());
            statement.setString(7, c.getAddress());
            statement.setString(8, c.getWeb());
            statement.setInt(9, c.getId());

            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return c;
    }

    public Customer save(Customer c) {

        if (c.getId() > 0) {
            return this.update(c);
        } else {
            return this.add(c);
        }
    }

    private boolean check(Customer c) {
        c.setName(TextEdit.toUpperEN(c.getName()));

        for (Customer tmp : new CustomerDB().getAll()) {
            if (tmp.getName() != null && tmp.getName().equals(c.getName())) {
                return false;
            }
        }
        return true;
    }

    public boolean delete(int id) {
        try (PreparedStatement statement = connect().prepareStatement("DELETE FROM customers WHERE id=?")) {
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

}
