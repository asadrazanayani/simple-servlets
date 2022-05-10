import dao.DaoFactory;
import dao.EmployeeDao;
import dao.UDArray;
import entity.Employee;

public class Main {
    public static void main(String[] args) {

        UDArray<Employee> em = DaoFactory.getEmployeDao().getAllEmployees();
        for (int i = 0; i < em.getSize(); i++) {
            System.out.println(em.get(i));

        }
    }
}
