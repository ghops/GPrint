package ghops.gprint.database;

import ghops.gprint.models.Fabric;
import ghops.gprint.models.Machine;
import ghops.gprint.models.Print;
import ghops.gprint.models.PrintType;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrintTypeDB extends DBConnect {

    public List<PrintType> getAll() {
        List<PrintType> list = new ArrayList();
        String query = "SELECT printtype.* FROM printtype WHERE status=1";
        try {
            PreparedStatement statement = connect().prepareStatement(query);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                PrintType printType = new PrintType();
                printType.setId(result.getInt("id"));
                printType.setName(result.getString("name"));
                list.add(printType);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

}
