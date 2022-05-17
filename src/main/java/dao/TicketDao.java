package dao;

import entity.Ticket;

import java.sql.*;

public class TicketDao {

    Connection conn = ConnectionFactory.getConnection();
    EmployeeDao eDoa = DaoFactory.getEmployeDao();

    private Ticket getTicketFromRS(ResultSet rs) {
        Ticket ticket = null;
        try {
            int ticket_id = rs.getInt("ticket_id");
            double ticket_amount = rs.getDouble("ticket_amount");
            String ticket_description = rs.getString("ticket_description");
            Timestamp ticket_timestamp = rs.getTimestamp("ticket_timestamp");
            String ticket_status = rs.getString("ticket_status");
            String ticket_category = rs.getString("ticket_category");
            int employee_id = rs.getInt("employee_id");
            ticket = new Ticket(ticket_id, ticket_amount, ticket_description, ticket_timestamp, ticket_status,
                    ticket_category, employee_id);
        } catch (SQLException e) {e.printStackTrace();}
        return ticket;
    }

    public boolean insert(Ticket ticket) {
        boolean isSuccess = false;
        String query = "insert into tickets (ticket_amount, ticket_description, ticket_category, employee_id) VALUES" +
                "(?, ?, ?, ?);";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, ticket.getAmount());
            ps.setString(2, ticket.getDescription());
            ps.setString(3, ticket.getExpenseCategory());
            ps.setInt(4, ticket.getEmployee_id());
            int count = ps.executeUpdate();
            if (count > 0) isSuccess = true;
        } catch (Exception ex) {}
        return isSuccess;
    }

    public UDArray<Ticket> getTicketsPending(int employee_id, String order) {
        UDArray<Ticket> tickets = new UDArray<>();
        String DESCOrASC = order.equals("DESC") ? "DESC" : "ASC";
        String query = String.format("SELECT * from tickets where employee_id = %s ORDER BY ticket_timestamp %s;",
                employee_id, DESCOrASC);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Ticket ticket = getTicketFromRS(rs);
                tickets.add(ticket);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return tickets;
    }

    public UDArray<Ticket> getPendingTickets(int employee_id) {
        UDArray<Ticket> tickets = new UDArray<>();
        String query = String.format("SELECT * from tickets where employee_id = %s and ticket_status = 'pending'",
                employee_id);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Ticket ticket = getTicketFromRS(rs);
                tickets.add(ticket);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return tickets;
    }

    public UDArray<Ticket> getTicketsPending() {
        UDArray<Ticket> tickets = new UDArray<>();
       String query = ("SELECT * from tickets where ticket_status = 'pending';");
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Ticket ticket = getTicketFromRS(rs);
                tickets.add(ticket);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return tickets;
    }

    public boolean updateTicketStatus(int ticket_id, String new_status) {
        boolean isSuccessful = false;
        String query = "Update tickets SET ticket_status = ? where ticket_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, new_status);
            ps.setInt(2, ticket_id);
            isSuccessful = ps.executeUpdate() > 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return isSuccessful;
    }

    public boolean initTable() {
        String query = "DROP TABLE IF EXISTS TICKETS CASCADE;" +
                "CREATE TABLE tickets (" +
                "ticket_id SERIAL PRIMARY KEY," +
                "ticket_amount DECIMAL," +
                "ticket_description VARCHAR(250)," +
                "ticket_timestamp TIMESTAMP default CURRENT_TIMESTAMP," +
                "ticket_status VARCHAR(50) default 'pending'," +
                "ticket_category VARCHAR(50), " +
                "employee_id INTEGER REFERENCES employees(employee_id)" +
                ")";
        try {
            Statement statement = conn.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
