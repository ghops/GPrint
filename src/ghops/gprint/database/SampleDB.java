package ghops.gprint.database;

import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Machine;
import ghops.gprint.models.PrintType;
import ghops.gprint.models.Sample;
import ghops.gprint.models.SampleType;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import org.mariadb.jdbc.Statement;

public class SampleDB extends DBConnect {

    public static final String GROUP_SAMPLE = "samples.id";
    public static final String GROUP_MACHINE = "samples.machineId";
    public static final String GROUP_CUSTOMER = "samples.customerId";
    public static final String GROUP_FABRIC = "samples.fabricId";
    public static final String GROUP_MONTH = "MONTH(samples.date)";
    public static final String GROUP_YEAR = "YEAR(samples.date)";
    public static final String GROUP_PRINT_TYPE = "samples.printTypeId";
    public static final String GROUP_SAMPLE_TYPE = "samples.sampleTypeId";

    public static final String SAMPLE_REPORT = """
        SELECT            
            IFNULL(customers.name, '') AS customer,
            DATE_FORMAT(samples.date, '%d.%m.%Y') AS dateFormat,
            UPPER( DATE_FORMAT(samples.date, '%M', 'tr_TR')) AS month_name,
            samples.date,
            fabrics.name AS fabric,
            printtype.name AS printType,
            sampletype.name AS sampleType,
            COUNT(samples.id) AS quantity,
            SUM(samples.meters) AS meters,
            machines.name AS machine

            FROM samples
                    LEFT JOIN customers ON customers.id=samples.customerId
                    LEFT JOIN fabrics ON fabrics.id=samples.fabricId
                    LEFT JOIN printtype ON printtype.id=samples.printTypeId
                    LEFT JOIN sampletype ON sampletype.id=samples.sampleTypeId
                    LEFT JOIN machines ON machines.id=samples.machineId
            WHERE samples.date>=? AND samples.date<=?
 

        """;

    public List<Sample> getAll(LocalDate first, LocalDate last) {
        List<Sample> list = new ArrayList();
        String query = "SELECT customers.name AS customer, fabrics.name AS fabric, machines.name AS machine, printtype.name AS printtype, sampletype.name AS sampleType, samples.* FROM samples LEFT JOIN customers ON customers.id=samples.customerId LEFT JOIN fabrics ON fabrics.id=samples.fabricId LEFT JOIN machines ON machines.id=samples.machineId LEFT JOIN printtype ON printtype.id=samples.printTypeId LEFT JOIN sampletype ON sampletype.id=samples.sampleTypeId WHERE samples.date>=? AND DATE<=?";
        try {

            PreparedStatement statement = connect().prepareStatement(query);
            statement.setString(1, first.toString());
            statement.setString(2, last.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Sample sample = new Sample();
                sample.setId(result.getInt("id"));
                sample.setDesign(result.getString("design"));
                sample.setCustomer(new Customer(result.getInt("customerId"), result.getString("customer")));
                sample.setFabric(new Fabric(result.getInt("fabricId"), result.getString("fabric")));
                sample.setMachine(new Machine(result.getInt("machineId"), result.getString("machine")));
                sample.setPrintType(new PrintType(result.getInt("printTypeId"), result.getString("printType")));
                sample.setSampleType(new SampleType(result.getInt("sampleTypeId"), result.getString("sampleType")));
                sample.setMeters(result.getInt("meters"));
                sample.setDate(result.getDate("date").toLocalDate());
                sample.setStatus(true);
                list.add(sample);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

    public Sample add(Sample s) {
        try (PreparedStatement statement = connect().prepareStatement("INSERT INTO samples SET design=?, customerId=?, fabricId=?, machineId=?, printTypeId=?, sampleTypeId=?, meters=?, date=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, s.getDesign());
            statement.setInt(2, s.getCustomer().getId());
            statement.setInt(3, s.getFabric().getId());
            statement.setInt(4, s.getMachine().getId());
            statement.setInt(5, s.getPrintType().getId());
            statement.setInt(6, s.getSampleType().getId());
            statement.setInt(7, s.getMeters());
            statement.setString(8, s.getDate().toString());
            

            if (statement.executeUpdate() < 1) {
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    s.setId(generatedKeys.getInt(1));
                    System.err.println(generatedKeys.getInt(1));
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return s;
    }

    public Sample update(Sample s) {
        try (PreparedStatement statement = connect().prepareStatement("UPDATE samples SET design=?, customerId=?, fabricId=?, machineId=?, printTypeId=?, sampleTypeId=?, meters=?, date=? WHERE id=?")) {
            statement.setString(1, s.getDesign());
            statement.setInt(2, s.getCustomer().getId());
            statement.setInt(3, s.getFabric().getId());
            statement.setInt(4, s.getMachine().getId());
            statement.setInt(5, s.getPrintType().getId());
            statement.setInt(6, s.getSampleType().getId());
            statement.setInt(7, s.getMeters());
            statement.setString(8, s.getDate().toString());
            statement.setInt(9, s.getId());
            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return s;
    }

    public Sample delete(Sample s) {
        try (PreparedStatement statement = connect().prepareStatement("DELETE FROM samples WHERE id=?")) {
            statement.setInt(1, s.getId());
            if (statement.executeUpdate() < 1) {
                return null;
            }

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
        return s;
    }

    public Sample save(Sample s) {
        Sample tmp;
        if (s.getId() > 0) {
            tmp = this.update(s);
        } else {
            tmp = this.add(s);
        }

        return tmp;
    }

    public ObservableList<Sample> save(ObservableList<Sample> list) {
        for (Sample s : list) {
            if (s.getId() > 0) {
                this.update(s);
            } else {
                this.add(s);
            }
        }
        return list;
    }

    public ObservableList<Sample> delete(ObservableList<Sample> list) {
        for (Sample s : list) {
            this.delete(s);
        }
        return list;
    }

    public List<Sample> getReport(LocalDate first, LocalDate last, String group) {
        List<Sample> list = new ArrayList();
        String query = SAMPLE_REPORT;

        query += " GROUP BY " + group;
        query += " ORDER BY samples.date DESC ";

        try {

            PreparedStatement statement = connect().prepareStatement(query);
            statement.setString(1, first.toString());
            statement.setString(2, last.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                Sample sample = new Sample();

                if (group == GROUP_CUSTOMER || group == GROUP_SAMPLE) {
                    sample.setCustomer(new Customer(0, result.getString("customer")));
                } else {
                    sample.setCustomer(new Customer(0, ""));
                }

                if (group == GROUP_FABRIC || group == GROUP_SAMPLE) {
                    sample.setFabric(new Fabric(0, result.getString("fabric")));
                } else {
                    sample.setFabric(new Fabric(0, ""));
                }

                if (group == GROUP_MACHINE || group == GROUP_SAMPLE) {
                    sample.setMachine(new Machine(0, result.getString("machine")));
                } else {
                    sample.setMachine(new Machine(0, ""));
                }

                if (group == GROUP_PRINT_TYPE || group == GROUP_SAMPLE) {
                    sample.setPrintType(new PrintType(0, result.getString("printType")));
                } else {
                    sample.setPrintType(new PrintType(0, ""));
                }

                if (group == GROUP_SAMPLE || group == GROUP_SAMPLE) {
                    sample.setSampleType(new SampleType(0, result.getString("sampleType")));
                } else {
                    sample.setSampleType(new SampleType(0, ""));
                }

                sample.setMeters(result.getInt("meters"));
                sample.setDate(result.getDate("date").toLocalDate());

                if (group == GROUP_SAMPLE) {
                    sample.setQuantity(1);
                } else {
                    sample.setQuantity(result.getInt("quantity"));
                }

                list.add(sample);

            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return list;
    }

}
