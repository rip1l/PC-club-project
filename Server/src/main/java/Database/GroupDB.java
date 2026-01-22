package Database;

import POJO.ClientGroup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDB implements IntGroupDB {
    private static GroupDB instance;
    private DatabaseConnection dbConnection;

    private GroupDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static GroupDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new GroupDB();
        }
        return instance;
    }

    @Override
    public boolean addClientToGroup(int clientId, int groupId) throws SQLException {
        String sql = "INSERT INTO client_group_membership (client_id, group_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, groupId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Ошибка дублирования
                throw new SQLException("Клиент уже состоит в этой группе", e);
            }
            throw e;
        }
    }

    @Override
    public boolean removeClientFromGroup(int clientId, int groupId) throws SQLException {
        String sql = "DELETE FROM client_group_membership WHERE client_id = ? AND group_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, groupId);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<ClientGroup> getAllGroups() throws SQLException {
        String sql = "SELECT group_id, name, discount_percent FROM client_groups";
        List<ClientGroup> groups = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                groups.add(new ClientGroup(
                        rs.getInt("group_id"),
                        rs.getString("name"),
                        rs.getDouble("discount_percent")
                ));
            }
        }
        return groups;
    }

    @Override
    public double getClientDiscount(int clientId) throws SQLException {
        String sql = "SELECT MAX(cg.discount_percent) " +
                "FROM client_group_membership cgm " +
                "JOIN client_groups cg ON cgm.group_id = cg.group_id " +
                "WHERE cgm.client_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }
}
