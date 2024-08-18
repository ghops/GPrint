package ghops.gprint.database;

import ghops.gprint.models.Machine;
import ghops.gprint.models.Print;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.mariadb.jdbc.Statement;

public class PrintDB extends DBConnect {

    public List<Print> getAll(int productId) {
        List<Print> list = new ArrayList();
        String query = "SELECT prints.*, machines.name AS machineName FROM prints LEFT JOIN machines ON machines.id=prints.machineId WHERE prints.productId=? ORDER BY DATE DESC, id DESC";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Print print = new Print(result.getInt("productId"));
                print.setId(result.getInt("id"));
                print.setMeters(result.getInt("meters"));
                print.setDate(result.getDate("date").toLocalDate());
                print.setMachine(new Machine(result.getInt("machineId"), result.getString("machineName")));
                list.add(print);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

    public Print add(Print p) {
        System.out.println("ADD: " + p);
        try (PreparedStatement statement = connect().prepareStatement("INSERT INTO prints SET productId=?, date=?, meters=?, machineId=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, p.getProductId());
            statement.setString(2, p.getDate().toString());
            statement.setDouble(3, p.getMeters());
            statement.setInt(4, p.getMachine().getId());

            if (statement.executeUpdate() < 1) {
                return null;
            }

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

    public Print update(Print p) {
        System.out.println("UPDATE: " + p);

        try (PreparedStatement statement = connect().prepareStatement("UPDATE prints SET date=?, meters=?, machineId=? WHERE id=?")) {

            statement.setString(1, p.getDate().toString());
            statement.setDouble(2, p.getMeters());
            statement.setInt(3, p.getMachine().getId());
            statement.setInt(4, p.getId());

            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return p;
    }
    
    public Print save(Print p){
        if(p.getId()>0) return this.update(p);
        else return this.add(p);
                
    }

    public boolean delete(int id) {
        System.out.println("db delete");
        try (PreparedStatement statement = connect().prepareStatement("DELETE FROM prints WHERE id=?")) {
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
