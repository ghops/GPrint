package ghops.gprint.database;

import ghops.gprint.models.Fabric;
import ghops.gprint.models.PrintType;
import ghops.gprint.tools.TextEdit;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.mariadb.jdbc.Statement;

public class FabricDB extends DBConnect {

    public List<Fabric> getAll() {
        List<Fabric> list = new ArrayList();
        String query = "SELECT fabrics.*, printtype.name AS printTypeName FROM fabrics LEFT JOIN printtype ON printtype.id=fabrics.type ORDER BY id DESC";
        try {
            PreparedStatement statement = connect().prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Fabric fabric = new Fabric();
                fabric.setId(result.getInt("id"));
                fabric.setName(result.getString("name"));
                fabric.setName2(result.getString("name2"));
                fabric.setName3(result.getString("name3"));
                fabric.setDescription(result.getString("description"));
                fabric.setWeight(result.getInt("weight"));
                fabric.setWidth(result.getInt("width"));
                fabric.setStatus(result.getInt("status"));
                fabric.setPrintType(new PrintType(result.getInt("type"), result.getString("printTypeName")));
                list.add(fabric);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

    public Fabric add(Fabric f) {
        try (PreparedStatement statement = connect().prepareStatement("INSERT INTO fabrics SET name=?, name2=?, name3=?, width=?, weight=?, description=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, f.getName());
            statement.setString(2, f.getName2());
            statement.setString(3, f.getName3());
            statement.setInt(4, f.getWidth());
            statement.setInt(5, f.getWeight());
            statement.setString(6, f.getDescription());

            if (statement.executeUpdate() < 1) {
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    f.setId(generatedKeys.getInt(1));
                    System.err.println(generatedKeys.getInt(1));
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return f;
    }

    public Fabric update(Fabric f) {

        try (PreparedStatement statement = connect().prepareStatement("UPDATE fabrics SET name=?, name2=?, name3=?, width=?, weight=?, type=?, status=? WHERE id=?")) {
            statement.setString(1, f.getName());
            statement.setString(2, f.getName2());
            statement.setString(3, f.getName3());
            statement.setInt(4, f.getWidth());
            statement.setInt(5, f.getWeight());
            statement.setInt(6, f.getPrintType().getId());
            statement.setInt(7, f.getStatusValue());
            statement.setInt(8, f.getId());

            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return f;
    }

    public Fabric save(Fabric f) {
        f.setName(TextEdit.toUpperEN(f.getName()));
        f.setName2(TextEdit.toUpperEN(f.getName2()));
        f.setName3(TextEdit.toUpperEN(f.getName3()));

        if (f.getId() > 0) {
            return this.update(f);
        } else {
            return this.add(f);
        }
    }

    public boolean delete(int id) {
        try (PreparedStatement statement = connect().prepareStatement("DELETE FROM fabrics WHERE id=?")) {
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

    public static boolean check(Fabric f) {
        f.setName(TextEdit.toUpperEN(f.getName()));
        f.setName2(TextEdit.toUpperEN(f.getName2()));
        f.setName3(TextEdit.toUpperEN(f.getName3()));

        for (Fabric tmp : new FabricDB().getAll()) {
            if (tmp.getName() != null && tmp.getName().equals(f.getName())) {
                return false;
            } else if (tmp.getName() != null && tmp.getName().equals(f.getName2())) {
                return false;
            } else if (tmp.getName() != null && tmp.getName().equals(f.getName3())) {
                return false;
            } else if (tmp.getName2() != null && tmp.getName().equals(f.getName())) {
                return false;
            } else if (tmp.getName2() != null && tmp.getName().equals(f.getName2())) {
                return false;
            } else if (tmp.getName2() != null && tmp.getName().equals(f.getName3())) {
                return false;
            } else if (tmp.getName3() != null && tmp.getName().equals(f.getName())) {
                return false;
            } else if (tmp.getName3() != null && tmp.getName().equals(f.getName2())) {
                return false;
            } else if (tmp.getName3() != null && tmp.getName().equals(f.getName3())) {
                return false;
            }
        }
        return true;
    }

}
