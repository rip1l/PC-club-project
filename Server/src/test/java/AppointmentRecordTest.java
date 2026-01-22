import org.junit.jupiter.api.Test;

public class AppointmentRecordTest {

    @Test
    public void testConstructorAndGetters() {
        AppointmentRecord record = new AppointmentRecord(
                1, 2, "Иванов", "Иван", "1234567890",
                3, "Петров", "Петр", 4,
                "Стрижка", 1500.0, "2024-12-10", "15:00"
        );

        assertEquals(1, record.getAppointmentId());
        assertEquals(2, record.getClientId());
        assertEquals("Иванов", record.getClientLastName());
        assertEquals("Иван", record.getClientFirstName());
        assertEquals("1234567890", record.getClientPhoneNumber());
        assertEquals(3, record.getEmployeeId());
        assertEquals("Петров", record.getEmployeeLastName());
        assertEquals("Петр", record.getEmployeeFirstName());
        assertEquals(4, record.getServiseId());
        assertEquals("Стрижка", record.getServiceName());
        assertEquals(1500.0, record.getServicePrice());
        assertEquals("2024-12-10", record.getAppointmentDate());
        assertEquals("15:00", record.getAppointmentTime());
    }

    @Test
    public void testSetters() {
        AppointmentRecord record = new AppointmentRecord();
        record.setAppointmentId(10);
        record.setServicePrice(2000.0);

        assertEquals(10, record.getAppointmentId());
        assertEquals(2000.0, record.getServicePrice());
    }

    @Test
    public void testToString() {
        AppointmentRecord record = new AppointmentRecord(
                1, 2, "Иванов", "Иван", "1234567890",
                3, "Петров", "Петр", 4,
                "Стрижка", 1500.0, "2024-12-10", "15:00"
        );

        String expected = "Запись на услугу: Клиент: Иван Иванов (1234567890), " +
                "Мастер: Петр Петров, Услуга: Стрижка, " +
                "Стоимость: 1500.0 руб., Дата записи: 2024-12-10, Время записи: 15:00";

        assertEquals(expected, record.toString());
    }
}
