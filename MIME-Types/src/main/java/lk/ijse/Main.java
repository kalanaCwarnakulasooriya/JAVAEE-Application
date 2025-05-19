package lk.ijse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/mime")
//@MultipartConfig
public class Main extends HttpServlet {
    //read text/plain data form httpRequest body
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String body = new BufferedReader(new InputStreamReader
//                (req.getInputStream()))
//                .lines()
//                .collect(Collectors.joining("\n"));
//
//        resp.setContentType("text/plain");
//        resp.getWriter().write(body);
//    }


    //read x-www-form-urlencoded data form httpRequest body
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        String address = req.getParameter("address");
//
//        resp.setContentType("text/plain");
//        resp.getWriter().println("Name: " + name);
//        resp.getWriter().println("Address: " + address);
//    }


    //read multipart/form-data data form httpRequest body

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        Part part = req.getPart("file");
//        String fileName = part.getSubmittedFileName();
//
//        resp.setContentType("text/plain");
//        resp.getWriter().println("Name: " + name);
//        resp.getWriter().println("File Name: " + fileName);
//    }

    //read JSON data form httpRequest body

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(req.getReader());
//
//        String name = jsonNode.get("name").asText();
//        String address = jsonNode.get("address").asText();
//
//        resp.setContentType("text/plain");
//        resp.getWriter().println("Name: " + name);
//        resp.getWriter().println("Address: " + address);
//    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<JsonNode> users =
                mapper.readValue(req.getReader(),
                        new TypeReference<List<JsonNode>>() {});

        for (JsonNode user : users) {
            String name = user.get("name").asText();
            String address = user.get("address").asText();
            System.out.println(name + " " + address);
        }

        resp.setContentType("text/plain");
        resp.getWriter().println("Success");
    }
}