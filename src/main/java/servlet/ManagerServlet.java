package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ManagerServlet extends HttpServlet {
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
            Employee employee = objectMapper.readValue(input.toString(), Employee.class);
            System.out.println(employee.toString());
            boolean isUpdated = employeeDao.update(employee);
            if (isUpdated) {
                out.println("Employee " + employee.toString() + "Updated");
                resp.setStatus(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
