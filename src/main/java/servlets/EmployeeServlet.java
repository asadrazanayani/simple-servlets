package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.EmployeeDao;
import dao.UDArray;
import entity.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EmployeeServlet extends HttpServlet {
    EmployeeDao employeeDao = DaoFactory.getEmployeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-type", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        try {
            String pathInfo = req.getPathInfo();
            System.out.println(pathInfo);
            if (pathInfo == null) {
                UDArray<Employee> employees = employeeDao.getAllEmployees();
                for (int i = 0; i < employees.getSize(); i++) {
                    if (i == 0) out.println("[");
                    out.println("\t" + objectMapper.writeValueAsString(employees.get(i)) + ",");
                }
                out.println("]");
            }
            String[] paths = pathInfo.substring(1).split("/");
            // pathinfo gets the info for the parameters for query and resource
            if (pathInfo != null) {
                if (paths[paths.length - 1].trim().equals("login")) {
                    String[] loginData = paths[0].split("&");
                    String employee_email = loginData[0].split("=")[1];
                    String employee_password = loginData[1].split("=")[1];
                    System.out.println(employee_email);
                    System.out.println(employee_password);
                    boolean isLoggedIn = employeeDao.loginSuccess(employee_email, employee_password);
                    if (isLoggedIn) {
                        resp.setStatus(200);
                        out.println(objectMapper.writeValueAsString("Logged In"));
                    } else {
                        resp.setStatus(401);
                        objectMapper.writeValueAsString("Unable to Log in");
                    }
                }
                if (pathInfo.contains("tickets")) {
                    req.getRequestDispatcher("/tickets").include(req, resp);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /*
    Steps for doGet:
    1. resp json, so use fasterxml
    2. parse through the request format and get the parameters for resources
     */

    /*
    Steps for doPost:
    1. data will be coming from body
    2. StringBuffer will store each lines read by BufferedReader
    3. Finally, objectMapper will convert the json to object we need.
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        PrintWriter out = resp.getWriter();
        StringBuffer input = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ( (line=reader.readLine()) != null) {
                input.append(line);
                System.out.println(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Employee employee = objectMapper.readValue(input.toString(), Employee.class);
            System.out.println(employee.toString());
            boolean isInserted = employeeDao.insert(employee);
            if (isInserted) {
                out.println("Employee " + employee.toString() + "inserted");
                resp.setStatus(201);
            }
            if (path.contains("tickets")) {
                req.getRequestDispatcher("/tickets").include(req, resp);
            }
        } catch (Exception e) {
            resp.setStatus(400);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
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
            boolean isUpdated = employeeDao.update(employee);
            if (isUpdated) {
                out.println("Employee " + employee.toString() + "Updated");
                resp.setStatus(201);
            }
        } catch (Exception e) {
            resp.setStatus(400);
            e.printStackTrace();
        }
    }
}
