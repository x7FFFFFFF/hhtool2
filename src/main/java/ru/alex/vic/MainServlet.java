package ru.alex.vic;

import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
        /*String name = "John";
        req.setAttribute("name", name);

        Integer numberOfItems = 1000;
        req.setAttribute("itemCount", numberOfItems);*/

        requestDispatcher.forward(req, resp);
    }
}
