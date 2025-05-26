package lk.ijse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/event")
public class EventServlet extends HttpServlet {
    private BasicDataSource dataSource;
    @Override
    public void init() throws ServletException {
        //50
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/event");
        dataSource.setUsername("root");
        dataSource.setPassword("Ijse@1234");
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(50);
    }
//    private Connection getConnection() throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        return DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/event",
//                "root",
//                "Ijse@1234"
//        );
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM events");
            ResultSet resultSet = pstm.executeQuery();

            List<Map<String, String>> elist = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> event = new HashMap<>();
                event.put("eid", resultSet.getString("eid"));
                event.put("ename", resultSet.getString("ename"));
                event.put("edescription", resultSet.getString("edescription"));
                event.put("edate", resultSet.getString("edate"));
                event.put("eplace", resultSet.getString("eplace"));
                elist.add(event);
            }

//            setCORS(resp);
            resp.setContentType("application/json");
            new ObjectMapper().writeValue(resp.getWriter(), elist);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(req.getReader(), Map.class);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(
                    "INSERT INTO events (eid, ename, edescription, edate, eplace) VALUES (?, ?, ?, ?, ?)"
            );
            pstm.setString(1, data.get("eid"));
            pstm.setString(2, data.get("ename"));
            pstm.setString(3, data.get("edescription"));
            pstm.setString(4, data.get("edate"));
            pstm.setString(5, data.get("eplace"));
            pstm.executeUpdate();

//            setCORS(resp);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(req.getReader(), Map.class);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(
                    "UPDATE events SET ename=?, edescription=?, edate=?, eplace=? WHERE eid=?"
            );
            pstm.setString(1, data.get("ename"));
            pstm.setString(2, data.get("edescription"));
            pstm.setString(3, data.get("edate"));
            pstm.setString(4, data.get("eplace"));
            pstm.setString(5, data.get("eid"));
            pstm.executeUpdate();

//            setCORS(resp);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String eid = req.getParameter("eid");

        if (eid == null || eid.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing event ID for deletion.");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM events WHERE eid=?");
            pstm.setString(1, eid);
            int affected = pstm.executeUpdate();

//            setCORS(resp);
            if (affected > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found.");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

//    private void setCORS(HttpServletResponse resp) {
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
//    }

//    @Override
//    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        setCORS(resp);
//        resp.setStatus(HttpServletResponse.SC_OK);
//    }
}