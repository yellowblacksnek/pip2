package ru.yellowblacksnek.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doTheJob(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doTheJob(req, resp);
    }

    private boolean correct(HttpServletRequest req) {
        String x = req.getParameter("x");
        String y = req.getParameter("y");
        String r = req.getParameter("r");
        if(x == null || y == null || r == null
        || isNotNumeric(x) || isNotNumeric(y) || isNotNumeric(r)) return false;
        return true;
    }

    private void doTheJob(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        boolean isCorrect = correct(req);
        String path = "/index.jsp";
        if(isCorrect) {
            path = "/check";
        }
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }

    private static boolean isNotNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum.replace(',', '.'));
        } catch (NumberFormatException | NullPointerException nfe) {
            return true;
        }
        return false;
    }
}
