import dao.DaoFactory;
import dao.EmployeeDao;
import dao.ManagerDao;
import dao.UDArray;
import entity.Employee;
import entity.Manager;

public class Main {
    public static void main(String[] args) {
        ManagerDao managerDao = DaoFactory.getManagerDao();
        Manager manager = new Manager("Boss Man", "bossman@domain.com", "bossman01", true);
        System.out.println(managerDao.update(manager));

    }
}
