package POJO;
import java.io.Serializable;
import java.time.LocalTime;

public class Employee implements Serializable {
    private int employeeId;
    private int userId;
    private String firstName;
    private String lastName;
    private int experienceYears;
    private String specialization;
    private LocalTime workStartTime;
    private LocalTime workEndTime;

    // Конструкторы
    public Employee() {}

    public Employee(int employeeId, int userId, String firstName, String lastName, int experienceYears, String specialization, LocalTime workStartTime, LocalTime workEndTime) {
        this.employeeId = employeeId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.experienceYears = experienceYears;
        this.specialization = specialization;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
    }

    // Геттеры и сеттеры
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalTime getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(LocalTime workStartTime) {
        this.workStartTime = workStartTime;
    }

    public LocalTime getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(LocalTime workEndTime) {
        this.workEndTime = workEndTime;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", experienceYears=" + experienceYears +
                ", specialization='" + specialization + '\'' +
                ", workStartTime=" + workStartTime +
                ", workEndTime=" + workEndTime +
                '}';
    }
}
