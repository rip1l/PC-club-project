package Database;

import POJO.ClientGroup;
import java.util.List;
import java.sql.SQLException;

public interface IntGroupDB {
    boolean addClientToGroup(int clientId, int groupId) throws SQLException;
    boolean removeClientFromGroup(int clientId, int groupId) throws SQLException;
    List<ClientGroup> getAllGroups() throws SQLException;
    double getClientDiscount(int clientId) throws SQLException;
}
