package lk.ijse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/lifecycles")
public class ServletLifeCycles extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("init method called : Servlet is being initialized");//run to 1
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet method called : Processing GET request");//run to 2
        resp.getWriter().println("Hello World");
    }

    @Override
    public void destroy() {
        System.out.println("destroy method called : Servlet is being destroyed");//project close
    }
}
