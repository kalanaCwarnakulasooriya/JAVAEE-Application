package lk.ijse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/event");
        dataSource.setUsername("root");
        dataSource.setPassword("Ijse@1234");
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(50);

        ServletContext servletContext = sce.getServletContext();
        sce.getServletContext().setAttribute("dataSource", dataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ServletContext servletContext = sce.getServletContext();
            BasicDataSource dataSource = (BasicDataSource) servletContext.getAttribute("dataSource");
            dataSource.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
