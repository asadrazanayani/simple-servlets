package dao;

import entity.Ticket;
import org.junit.Before;
import org.junit.Test;
import entity.Employee;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeDaoTest {

    EmployeeDao employeeDao = DaoFactory.getEmployeDao();

    @Before
    public void setup() {
        employeeDao.initTable();
        Employee employee = new Employee("test", "test@domain.com", "test123");
        employeeDao.insert(employee);
    }

    @Test
    public void checkInsertUser() {
        Employee employee = new Employee("test", "test@domain.com", "test123");
        assertTrue(employeeDao.insert(employee));
    }

    @Test
    public void neg_checkInsertUser() {
        Employee em = new Employee();
        assertFalse(employeeDao.insert(em));
    }

    @Test
    public void checkUpdateUser() {
        Employee employee = new Employee(1, "test", "test@domain.com", "test1234");
        assertTrue(employeeDao.update(employee));
    }

    @Test
    public void neg_checkUpdateUser() {
        Employee employee = new Employee();
        assertFalse(employeeDao.update(employee));
    }

    @Test
    public void checkgetAllEmployee() {
        UDArray<Employee> employeeUDArray;
        employeeUDArray = employeeDao.getAllEmployees();
        assertTrue(employeeUDArray.getSize()>0);
    }

    @Test
    public void checkisLoggedIn() {
        assertTrue(!employeeDao.isLoggedIn(1));
    }

    @Test
    public void neg_checkisLoggedIn() {
        assertTrue(!employeeDao.isLoggedIn(-1));
    }

    @Test
    public void checkloginSuccess() {
        employeeDao.insert(new Employee(1, "test", "test@domain.com", "test1234"));
        assertTrue(employeeDao.loginSuccess("test@domain.com", "test1234"));
    }
}
