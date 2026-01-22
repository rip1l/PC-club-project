package Database;

import POJO.Computer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputerDB implements IntComputerDB {
    private static ComputerDB instance;
    private DatabaseConnection dbConnection;

    private ComputerDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static ComputerDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new ComputerDB();
        }
        return instance;
    }

    @Override
    public List<Computer> getAllComputers() throws SQLException {
        String sql = "SELECT computer_id, specs, is_active FROM computers";
        List<Computer> computers = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                computers.add(new Computer(
                        rs.getInt("computer_id"),
                        rs.getString("specs"),
                        rs.getBoolean("is_active")
                ));
            }
        }
        return computers;
    }

    @Override
    public Computer getComputerById(int id) throws SQLException {
        String sql = "SELECT computer_id, specs, is_active FROM computers WHERE computer_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Computer(
                            rs.getInt("computer_id"),
                            rs.getString("specs"),
                            rs.getBoolean("is_active")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateComputerStatus(int computerId, boolean isActive) throws SQLException {
        String sql = "UPDATE computers SET is_active = ? WHERE computer_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, computerId);

            return pstmt.executeUpdate() > 0;
        }
    }
}
