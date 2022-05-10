package dao;

import entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    Connection connection = ConnectionFactory.getConnection();

    // int employee_id, String employee_name, String employee_email, String employee_password

    private Employee getEmployeeFromRS(ResultSet rs) {
        Employee em = null;
        try {
            em = new Employee(rs.getInt("employee_id"),
                    rs.getString("employee_name"),
                    rs.getString("employee_email"),
                    rs.getString("employee_password"),
                    rs.getBoolean("is_logged_in"));
            System.out.println(em.toString());
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return em;
    }

    public boolean insert(Employee employee) {
        boolean isInserted = false;
        String query = "INSERT INTO employees (employee_name,  employee_email, employee_password) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getEmployee_name());
            ps.setString(2, employee.getEmployee_email());
            ps.setString(3, employee.getEmployee_password());
            isInserted = ps.executeUpdate() >= 1;
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return isInserted;
    }

    public boolean update(Employee employee) {
        boolean isUpdated = false;
        String query = "UPDATE employees SET employee_name=?, employee_email=?, employee_password=?,is_logged_in=? " +
                "WHERE " +
                "employee_id=?;";
        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getEmployee_name());
            ps.setString(2, employee.getEmployee_email());
            ps.setString(3, employee.getEmployee_password());
            ps.setBoolean(4, employee.isLoggedIn());
            ps.setInt(5, employee.getEmployee_id());
            isUpdated = ps.executeUpdate() >= 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return isUpdated;
    }

    public UDArray<Employee> getAllEmployees() {
        UDArray<Employee> employeeList = new UDArray<>();
        String query = "SELECT * FROM employees";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee em = getEmployeeFromRS(rs);
                employeeList.add(em);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public boolean loginSuccess(String email, String password) {
        boolean isLoginSuccess = false;
        String query = "SELECT * FROM employees where employee_email = ? and employee_password = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Employee em = getEmployeeFromRS(rs);
            if (em.getEmployee_email().equals(email) && em.getEmployee_password().equals(password)) {
                isLoginSuccess = true;
                em.setLoggedIn(true);
                System.out.println(em.toString());
                boolean isUpdated = update(em);
                if (isUpdated) {
                    System.out.println("Employee Logged In");
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isLoginSuccess;
    }

    public boolean isLoggedIn(int id) {
        String query = "Select * from employees where employee_id = "+id+";";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            Employee em = getEmployeeFromRS(rs);
            return em.isLoggedIn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Employee getEmployee(int employee_id) {
        Employee em = null;
        String query = "SELECT * FROM employees where employee_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, employee_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            em = getEmployeeFromRS(rs);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return em;
    }



}