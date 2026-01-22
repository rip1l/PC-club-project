package Database;

import POJO.Tariff;
import java.sql.SQLException;
import java.util.List;

public interface IntTariffDB {
    List<Tariff> getAllTariffs() throws SQLException;
    Tariff getTariffById(int id) throws SQLException;
    boolean updateTariff(Tariff tariff) throws SQLException;
}
