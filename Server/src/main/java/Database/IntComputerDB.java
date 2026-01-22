package Database;

import POJO.Computer;
import java.sql.SQLException;
import java.util.List;

public interface IntComputerDB {
    List<Computer> getAllComputers() throws SQLException;
    Computer getComputerById(int id) throws SQLException;
    boolean updateComputerStatus(int computerId, boolean isActive) throws SQLException;
}
