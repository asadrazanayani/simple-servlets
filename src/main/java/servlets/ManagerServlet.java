package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.ManagerDao;
import dao.TicketDao;
import dao.UDArray;
import entity.Manager;
import entity.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ManagerServlet extends HttpServlet {
    ManagerDao managerDao = DaoFactory.getManagerDao();
    TicketDao ticketDao = DaoFactory.getTicketDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.contains("viewpending")) {
            UDArray<Ticket> tickets = ticketDao.getTicketsPending();
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter out = resp.getWriter();
            for (int i = 0; i < tickets.getSize(); i++) {
                if (i == 0) out.println("[");
                out.println("\t" + objectMapper.writeValueAsString(tickets.get(i)) + ",");
            }
            out.println("]");
            resp.setStatus(200);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.contains("login")) {
            PrintWriter out = resp.getWriter();
            StringBuffer input = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null) {
                    input.append(line);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                Manager manager = objectMapper.readValue(input.toString(), Manager.class);
                boolean isUpdated = managerDao.update(manager);
                if (isUpdated) {
                    out.println("Manager " + manager.toString() + "Updated");
                    resp.setStatus(201);
                } else {
                    out.println("Cannot login");
                    resp.setStatus(401);
                }
            } catch (Exception e) {
                resp.setStatus(400);
                e.printStackTrace();
            }
        }
        String[] paths = pathInfo.split("/");
        if (pathInfo.contains("tickets")) {
            // approve or reject
            StringBuffer payload = new StringBuffer();
            try {
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    payload.append(line);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                Ticket ticket = objectMapper.readValue(payload.toString(), Ticket.class);
                boolean isUpdated = ticketDao.updateTicketStatus(ticket.getId(), ticket.getStatus());
                if (isUpdated) {
                    PrintWriter out = resp.getWriter();
                    out.println("Ticket Status Changed");
                    resp.setStatus(201);
                }
            } catch (Exception ex) {
                resp.setStatus(400);
                ex.printStackTrace();
            }
        }
    }
}
