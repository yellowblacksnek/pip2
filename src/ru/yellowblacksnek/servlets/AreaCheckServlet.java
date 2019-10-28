package ru.yellowblacksnek.servlets;

import ru.yellowblacksnek.Combination;
import ru.yellowblacksnek.History;
import ru.yellowblacksnek.ServerConfig;

import static ru.yellowblacksnek.AreaUtils.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

public class AreaCheckServlet extends HttpServlet {
    final Rect rect = ServerConfig.rect;
    final Triangle poly = ServerConfig.poly;
    final Arc arc = ServerConfig.arc;

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

        //ArrayList<Combination> history = new ArrayList<>();
        History history;

        HttpSession session = req.getSession();

        boolean useContext = ServerConfig.useContext;

        if(useContext) {
            ServletContext context = session.getServletContext();

            HashMap<String, History> historyMap;

            if (context.getAttribute("historyMap") != null) {
                historyMap = (HashMap<String, History>) context.getAttribute("historyMap");
            } else {
                historyMap = new HashMap<>();
            }

            String sessionId = session.getId();
            if (historyMap.containsKey(sessionId)) {
                history = historyMap.get(sessionId);
            } else {
                history = new History();
                historyMap.put(sessionId, history);
            }
            boolean unique = isUnique(history, combination);
            if(unique) history.getHistory().add(combination);
            context.setAttribute("historyMap", historyMap);
        } else {
            if (session.getAttribute("history") != null) {
                history = (History) session.getAttribute("history");
            } else {
                history = new History();
            }
            boolean unique = isUnique(history, combination);
            if(unique) history.getHistory().add(combination);
            session.setAttribute("history", history);
        }

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

    private boolean isUnique(History history, Combination combination){
        for(Combination each : history.getHistory()) {
            if(each.getX().equals(combination.getX())
                    && each.getY().equals(combination.getY())
                    && each.getR().equals(combination.getR())
                    && each.isMatched() == combination.isMatched()) {
                return false;
            }
        }
        return true;
    }
}
