package ru.yellowblacksnek;

import java.util.HashMap;

public class AreaUtils {

    public static final int R = 70;
    static HashMap<String, Integer> X_COORDS;
    static {
        X_COORDS = new HashMap<>();
        X_COORDS.put("-R", 30);
        X_COORDS.put("-R/2", 65);
        X_COORDS.put("0", 100);
        X_COORDS.put("R/2", 135);
        X_COORDS.put("R", 170);
    }
    static HashMap<String, Integer> Y_COORDS;
    static {
        Y_COORDS = new HashMap<>();
        Y_COORDS.put("-R", 170);
        Y_COORDS.put("-R/2", 135);
        Y_COORDS.put("0", 100);
        Y_COORDS.put("R/2", 65);
        Y_COORDS.put("R", 30);
    }

    public enum ArcSectors {
        I, II, III, IV
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(String x, String y) {
            this.x = X_COORDS.get(x);
            this.y = Y_COORDS.get(y);
        }
    }

    public static class Rect {
        public final int x;
        public final int y;
        public final int width;
        public final int height;

        public Rect(String x, String y, int width, int height) {
            this.x = X_COORDS.get(x);
            this.y = Y_COORDS.get(y);
            this.width = width;
            this.height = height;
        }

        public boolean contains(double x, double y) {
            if(this.x > x || this.y > y) return false;
            if(x > this.x + width || y > this.y + height) return false;
            return true;
        }

    }

    public static class Triangle {
        public final Point first;
        public final Point second;
        public final Point third = new Point(100, 100);

        public Triangle(Point first, Point second) {
            this.first = first;
            this.second = second;
        }

        public boolean contains(double x, double y) {
            Triangle1 tr = new Triangle1(first.x, first.y, second.x, second.y, third.x, third.y);
            return tr.contains(x, y);
        }
    }

    public static class Triangle1 {

        private final double x3, y3;
        private final double y23, x32, y31, x13;
        private final double det, minD, maxD;

        Triangle1(double x1, double y1, double x2, double y2, double x3,
                  double y3) {
            this.x3 = x3;
            this.y3 = y3;
            y23 = y2 - y3;
            x32 = x3 - x2;
            y31 = y3 - y1;
            x13 = x1 - x3;
            det = y23 * x13 - x32 * y31;
            minD = Math.min(det, 0);
            maxD = Math.max(det, 0);
        }

        boolean contains(double x, double y) {
            double dx = x - x3;
            double dy = y - y3;
            double a = y23 * dx + x32 * dy;
            if (a < minD || a > maxD)
                return false;
            double b = y31 * dx + x13 * dy;
            if (b < minD || b > maxD)
                return false;
            double c = det - a - b;
            if (c < minD || c > maxD)
                return false;
            return true;
        }
    }

    public static class Arc {
        public final int radius;
        public final int offset;
        public final int sweepFlag;
        public final int x = 100;
        public final int y;
        public final ArcSectors sector;

        public Arc(int radius, ArcSectors sector) {
            this.radius = radius;
            this.sector = sector;
            switch (sector) {
                case I :
                    offset = 1 * radius;
                    y = 100 - radius;
                    sweepFlag = 0;
                    break;
                case II:
                    offset = 1 * radius;
                    y = 100 + radius;
                    sweepFlag = 1;
                    break;
                case III:
                    offset = -1 * radius;
                    y = 100 + radius;
                    sweepFlag = 0;
                    break;
                case IV:
                    offset = -1 * radius;
                    y = 100 - radius;
                    sweepFlag = 1;
                    break;
                default:
                    offset = 0;
                    y = 0;
                    sweepFlag = 0;
            }
        }

        public boolean contains(double x, double y) {
            boolean circleContains = (Math.pow(x*70, 2) + Math.pow(y*70, 2) < Math.pow(radius, 2));
            if(!circleContains) return false;
            switch (sector) {
                case I:
                    if(x >= 0 && y >= 0) return true;
                    break;
                case II:
                    if(x >= 0 && y <= 0) return true;
                    break;
                case III:
                    if(x <= 0 && y <= 0) return true;
                    break;
                case IV:
                    if(x <= 0 && y >= 0) return true;
                    break;
                default:
            }
            return false;
        }

    }

    public static String printRect(Rect rect) {
        return "<rect x=\"" + rect.x
                + "\" y=\"" + rect.y
                + "\" width=\"" + rect.width
                + "\" height=\"" + rect.height
                + "\" fill=\"#3e97ff\"/>";
    }
}
