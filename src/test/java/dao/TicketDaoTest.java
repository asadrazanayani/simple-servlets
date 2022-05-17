package dao;

import entity.Employee;
import entity.Ticket;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TicketDaoTest {

    TicketDao ticketDao = DaoFactory.getTicketDao();
    EmployeeDao employeeDao = DaoFactory.getEmployeDao();

    @Before
    public void setup() {
        employeeDao.initTable();
        ticketDao.initTable();
    }

    @Before
    public void insertingData() {
        Employee employee = new Employee("test", "test@domain.com", "test123");
        employeeDao.insert(employee);
        Ticket ticket = new Ticket(500, "Uber ride", "pending", 1);
        ticketDao.insert(ticket);
    }

    @Test
    public void checkInsertGetTickets() {
        UDArray<Ticket> tickets = ticketDao.getTicketsPending();
        assertTrue(tickets.getSize() > 0);
    }

    @Test
    public void checkUpdate() {
        assertTrue(ticketDao.updateTicketStatus(1,"approved"));
    }

    @Test
    public void checkGetTicketsPendingEmployee() {
        UDArray<Ticket> tickets = ticketDao.getPendingTickets(1);
        assertTrue(tickets.getSize() > 0);
    }

    @Test
    public void checkGt() {
        UDArray<Ticket> tickets = ticketDao.getTicketsPending(1, "ASC");
        assertTrue(tickets.getSize() > 0);
    }



}
