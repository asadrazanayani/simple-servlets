package entity;

public class Employee {
    private int employee_id;
    private String employee_name;
    private String employee_email;
    private String employee_password;
    private boolean is_Logged_In;

    public Employee(int employee_id, String employee_name, String employee_email, String employee_password, boolean is_Logged_In) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.employee_email = employee_email;
        this.employee_password = employee_password;
        this.is_Logged_In = is_Logged_In;
    }

    public boolean isLoggedIn() {
        return is_Logged_In;
    }

    public void setLoggedIn(boolean loggedIn) {
        is_Logged_In = loggedIn;
    }

    public Employee(int employee_id, String employee_name, String employee_email, String employee_password) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.employee_email = employee_email;
        this.employee_password = employee_password;
    }

    public Employee(String employee_name, String employee_email, String employee_password) {
        this.employee_name = employee_name;
        this.employee_email = employee_email;
        this.employee_password = employee_password;
    }

    public Employee() {
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_email() {
        return employee_email;
    }

    public void setEmployee_email(String employee_email) {
        this.employee_email = employee_email;
    }

    public String getEmployee_password() {
        return employee_password;
    }

    public void setEmployee_password(String employee_password) {
        this.employee_password = employee_password;
    }

    @Override
    public String toString() {
        return "Employee{" + "employee_id=" + employee_id + ", employee_name='" + employee_name + '\'' + ", employee_email='" + employee_email + '\'' + ", employee_password='" + employee_password + '\'' + ", is_Logged_In=" + is_Logged_In + '}';
    }
}
