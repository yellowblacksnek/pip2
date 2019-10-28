package ru.yellowblacksnek;

import java.io.Serializable;

public class Combination implements Serializable {
    private String x;
    private String y;
    private String r;
    private boolean matched;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    @Override
    public String toString() {
        return String.format("{ \"x\":\"%s\", \"y\":\"%s\", \"r\":\"%s\", \"result\":\"%b\" }", x, y, r, matched);
    }
}
