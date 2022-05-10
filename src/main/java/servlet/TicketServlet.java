package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.EmployeeDao;
import dao.TicketDao;
import dao.UDArray;
import entity.Employee;
import entity.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TicketServlet extends HttpServlet {
    EmployeeDao employeeDao = DaoFactory.getEmployeDao();
    TicketDao ticketDao = DaoFactory.getTicketDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("At do Get Ticket");
        PrintWriter out = resp.getWriter();
        String path = req.getPathInfo();
        System.out.println(path.contains("pending"));
        String[] paths = path.split("/");
        System.out.println(paths[paths.length - 1].equals("pending"));
        ObjectMapper objectMapper = new ObjectMapper();
        if (paths[paths.length - 1].equals("pending")) {
            int employee_id = Integer.parseInt(paths[1]);
            System.out.println(employee_id);
            boolean loggedIn = employeeDao.isLoggedIn(employee_id);
            System.out.println(loggedIn);
            if (loggedIn) {
                UDArray<Ticket> tickets = ticketDao.getPendingTickets(employee_id);
                System.out.println(tickets.getSize());
                resp.setStatus(200);
                for (int i = 0; i < tickets.getSize(); i++) {
                    out.println(objectMapper.writeValueAsString(tickets.get(i)));
                }
            } else {
                resp.setStatus(401);
                out.println(objectMapper.writeValueAsString("Not Logged In"));
            }
        }
        String orderby = req.getParameter("orderby").trim();
        if (orderby != null) {
            System.out.println(orderby);
            int employee_id = Integer.parseInt(paths[1]);
            System.out.println(employee_id);
            UDArray<Ticket> ticketUDArray = ticketDao.getTickets(employee_id, orderby);
            for (int i  = 0; i < ticketUDArray.getSize(); i++) {
                System.out.println(ticketUDArray.get(i).toString());
                out.println(objectMapper.writeValueAsString(ticketUDArray.get(i)));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            Ticket ticket = objectMapper.readValue(input.toString(), Ticket.class);
            System.out.println(ticket.toString());
            boolean isInserted = ticketDao.insert(ticket);
            if (isInserted) {
                out.println("Ticket " + ticket.toString() + "inserted");
                resp.setStatus(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
