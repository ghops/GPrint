package ghops.gprint.database;

import ghops.gprint.models.Fabric;
import ghops.gprint.models.Machine;
import ghops.gprint.models.Print;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MachineDB extends DBConnect {

    public List<Machine> getAll() {
        List<Machine> list = new ArrayList();
        String query = "SELECT machines.* FROM machines WHERE status=1";
        try {
            PreparedStatement statement = connect().prepareStatement(query);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Machine machine= new Machine();
                machine.setId(result.getInt("id"));
                machine.setName(result.getString("name"));
                list.add(machine);
              

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

}
