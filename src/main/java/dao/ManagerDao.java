package dao;

import entity.Manager;

import java.sql.*;

public class ManagerDao {
    Connection conn = ConnectionFactory.getConnection();

    public boolean update(Manager manager) {
        boolean isUpdated = false;
        String query = "UPDATE managers SET manager_name=?, manager_email=?, manager_password=?, is_logged_in=? WHERE" +
                " manager_id=?;";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, manager.getManager_name());
            ps.setString(2, manager.getManager_email());
            ps.setString(3, manager.getManager_password());
            ps.setBoolean(4, manager.isIs_logged_in());
            ps.setInt(5, manager.getManager_id());
            isUpdated = ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isUpdated;

    }

    public boolean managementLogin(String manager_email, String manager_password) {
        boolean isLoginSuccess = false;
        String query = "SELECT * from managers where manager_email = ? and manager_password = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, manager_email);
            ps.setString(2, manager_password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Manager manager = getManagerFromRS(rs);
            if (manager.getManager_email().equals(manager_email) && manager.getManager_password().equals(manager_password)) {
                isLoginSuccess = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return isLoginSuccess;
    }

    private Manager getManagerFromRS(ResultSet rs) {
        Manager manager = null;
        //int manager_id, String manager_name, String manager_email, String manager_password
        try {
            manager = new Manager(rs.getInt("manager_id"),
                    rs.getString("manager_name"),
                    rs.getString("manager_email"),
                    rs.getString("manager_password"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return manager;
    }
}
