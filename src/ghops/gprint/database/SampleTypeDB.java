package ghops.gprint.database;

import ghops.gprint.models.SampleType;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SampleTypeDB extends DBConnect {

    public List<SampleType> getAll() {
        List<SampleType> list = new ArrayList();
        String query = "SELECT sampletype.* FROM sampletype";
        try {
            PreparedStatement statement = connect().prepareStatement(query);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                SampleType sampleType = new SampleType();
                sampleType.setId(result.getInt("id"));
                sampleType.setName(result.getString("name"));
                list.add(sampleType);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

}
