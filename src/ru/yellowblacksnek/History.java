package ru.yellowblacksnek;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {
    ArrayList<Combination> history = new ArrayList<>();
    String historyString = new String();

    public ArrayList<Combination> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Combination> history) {
        this.history = history;
    }

    public String getHistoryString() {
        StringBuilder buf = new StringBuilder();
        for(Combination each : history) buf.append(each.toString());
        return buf.toString();
    }

    public void setHistoryString(String historyString) {
        this.historyString = historyString;
    }

    @Override
    public String toString() {
        return getHistoryString();
    }
}
