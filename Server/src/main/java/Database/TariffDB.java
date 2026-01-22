package Database;

import POJO.Tariff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class TariffDB implements IntTariffDB{
    private static TariffDB instance;
    private DatabaseConnection dbConnection;

    private TariffDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static TariffDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new TariffDB();
        }
        return instance;
    }

    @Override
    public List<Tariff> getAllTariffs() throws SQLException {
        String sql = "SELECT tariff_id, name, price_per_hour FROM tariffs";
        List<Tariff> tariffs = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                tariffs.add(new Tariff(
                        rs.getInt("tariff_id"),
                        rs.getString("name"),
                        rs.getDouble("price_per_hour")
                ));
            }
        }
        return tariffs;
    }

    @Override
    public Tariff getTariffById(int id) throws SQLException {
        String sql = "SELECT tariff_id, name, price_per_hour FROM tariffs WHERE tariff_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Tariff(
                            rs.getInt("tariff_id"),
                            rs.getString("name"),
                            rs.getDouble("price_per_hour")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateTariff(Tariff tariff) throws SQLException {
        String sql = "UPDATE tariffs SET name = ?, price_per_hour = ? WHERE tariff_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, tariff.getName());
            pstmt.setDouble(2, tariff.getPricePerHour());
            pstmt.setInt(3, tariff.getTariffId());

            return pstmt.executeUpdate() > 0;
        }
    }
}
