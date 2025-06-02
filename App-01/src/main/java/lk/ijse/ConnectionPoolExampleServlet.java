package lk.ijse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/connectionpool")
public class ConnectionPoolExampleServlet extends HttpServlet {
//    private BasicDataSource dataSource;
    @Override
    public void init() throws ServletException {
        //50
//        dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/event");
//        dataSource.setUsername("root");
//        dataSource.setPassword("Ijse@1234");
//        dataSource.setInitialSize(5);
//        dataSource.setMaxTotal(50);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource dataSource = (BasicDataSource) servletContext.getAttribute("dataSource");
        try {
            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM events");
            List<Map<String,String>> elist = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> event = new HashMap<>();
                event.put("eid", resultSet.getString("eid"));
                event.put("ename", resultSet.getString("ename"));
                event.put("edescription", resultSet.getString("edescription"));
                event.put("edate", resultSet.getString("edate"));
                event.put("eplace", resultSet.getString("eplace"));
                elist.add(event);
            }
            resp.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), elist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource dataSource = (BasicDataSource) servletContext.getAttribute("dataSource");
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
}
