package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.ManagerDao;
import entity.Employee;
import entity.Manager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ManagerServlet extends HttpServlet {
    ManagerDao managerDao = DaoFactory.getManagerDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        StringBuffer input = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line=reader.readLine()) != null) {
                input.append(line);
                System.out.println(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Manager manager = objectMapper.readValue(input.toString(), Manager.class);
            boolean isUpdated = managerDao.update(manager);
            if (isUpdated) {
                out.println("Manager " + manager.toString() + "Updated");
                resp.setStatus(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
