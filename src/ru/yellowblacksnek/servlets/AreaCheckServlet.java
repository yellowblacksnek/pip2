package ru.yellowblacksnek.servlets;

import ru.yellowblacksnek.Combination;
import static ru.yellowblacksnek.AreaUtils.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AreaCheckServlet extends HttpServlet {
    final Rect rect = new Rect("-R", "R/2", R, R/2);
    final Triangle poly = new Triangle(new Point("0", "R/2"), new Point("R/2", "0"));
    final Arc arc = new Arc(R, ArcSectors.III);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doTheJob(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doTheJob(req, resp);
    }

    private void doTheJob(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.nanoTime();

        String x = makePretty(req.getParameter("x"));
        String y = makePretty(req.getParameter("y"));
        String r = makePretty(req.getParameter("r"));

        double realX = 100 + 70 * Double.parseDouble(x)/Double.parseDouble(r);
        double realY = 100 - 70 * Double.parseDouble(y)/Double.parseDouble(r);

        boolean matched = matched(x, y, r);
        Combination combination = new Combination();
        combination.setX(x.replace('.', ','));
        combination.setY(y.replace('.', ','));
        combination.setR(r.replace('.', ','));
        combination.setMatched(matched);

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        ArrayList<Combination> history = new ArrayList<>();

        HttpSession session = req.getSession();
        if(session.getAttribute("history") != null) {
            history = (ArrayList<Combination>) session.getAttribute("history");
        }

        boolean unique = true;

        for(Combination each : history) {
            if(each.getX().equals(combination.getX())
            && each.getY().equals(combination.getY())
            && each.getR().equals(combination.getR())
            && each.isMatched() == combination.isMatched()) {
                unique = false;
                break;
            }
        }

        if(unique) history.add(combination);
        session.setAttribute("history", history);

        req.setAttribute("x", x.replace('.', ','));
        req.setAttribute("y", y.replace('.', ','));
        req.setAttribute("r", r.replace('.', ','));
        req.setAttribute("matched", matched);
        req.setAttribute("executionTime", executionTime);

        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/checkResult.jsp");
        requestDispatcher.forward(req, resp);
    }

    private boolean matched(String x, String y, String r) {
        double realX = 100 + 70 * Double.parseDouble(x)/Double.parseDouble(r);
        double realY = 100 - 70 * Double.parseDouble(y)/Double.parseDouble(r);

        if (rect.contains(realX ,realY)) return true;
        else if(poly.contains(realX,realY)) return true;
        else if(arc.contains(Double.parseDouble(x)/Double.parseDouble(r), Double.parseDouble(y)/Double.parseDouble(r))) return true;
        else return false;
    }

    private String makePretty(String input) {
        double a = Double.parseDouble(input.replaceAll(",", "."));
        if(a == Math.round(a))
            return ("" + (int) a);
        else
            return String.format("%1.2f", a).replaceAll(",", ".");
    }
}
