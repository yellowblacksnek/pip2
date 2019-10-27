package ru.yellowblacksnek;

public class FieldConfig {
    public enum FieldTypes {
        Text,
        Radio,
        Select,
        Button
    }

    public enum DataTypes {
        Int,
        Int_And_Halves,
        Text
    }

    private String name;
    private FieldTypes fieldType;
    private DataTypes dataType;
    private float start;
    private float end;
    private String start_s;
    private String end_s;

    public FieldConfig(String name, FieldTypes f_type, DataTypes d_type, float start, float end) {
        this.name = name.toLowerCase();
        this.fieldType = f_type;
        this.dataType = d_type;
        this.start = start;
        this.end = end;
        start_s = (start == Math.round(start)) ? "" + (int) start : "" + start;
        end_s = (end == Math.round(end)) ? "" + (int) end : "" + end;
    }

    public String getInputOnly() {
        StringBuilder buf = new StringBuilder();
        if (fieldType == FieldTypes.Text) {
            String a =
                    String.format("<input type=\"text\" name=\"%s\" id=\"%s\" placeholder=\"[%s;%s]\" " +
                            "onchange=\"checkData(this)\" onkeyup=\"checkData(this)\" " +
                            "autocomplete=\"off\" maxlength=\"6\" min=\"%s\" max=\"%s\" required>", name, name, start_s, end_s, start_s, end_s);
            buf.append(a);
        }
        if (fieldType == FieldTypes.Radio) {
            if(dataType == DataTypes.Int) {
                for (int i = (int)start; i <= (int) end; i += 1) {
                    buf.append(String.format("<label><input type=\"radio\" name=\"%s\" value=\"%d\" required/>%d</label> ", name, i, i));
                }
            } else {
                for (float i = start; i <= end; i += 0.5f) {
                    buf.append(String.format("<label><input type=\"radio\" name=\"%s\" value=\"%f\" required/>%f</label> ", name, i, i));
                }
            }
            buf.append(String.format("<label><input type=\"radio\" name=\"%s\" id=\"%sCustom\" hidden/></label>", name, name));
        }
        if(fieldType == FieldTypes.Select) {
            buf.append(String.format("<select name=\"%s\" id=\"%s\" required> ",name,name));
            buf.append("<option value=\"\">Выберите число</option> ");
            if(dataType == DataTypes.Int) {
                for (int i = (int)start; i <= (int) end; i += 1) {
                    buf.append(String.format("<option value=\"%d\">%d</option>", i, i));
                }
            } else {
                for (float i = start; i <= end; i += 0.5f) {
                    buf.append(String.format("<option value=\"%1.1f\">%1.1f</option>", i, i));
                }
            }
            buf.append(String.format("<option hidden id=\"%sCustom\"></option>", name));
            buf.append("</select>");
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("<tr>");
        buf.append("<td>" + name.toUpperCase() + ": </td>");

        String colspan = fieldType == FieldTypes.Text ? "" : " colspan=\"2\"";

        buf.append("<td" +  colspan + ">");
        buf.append(getInputOnly());
        buf.append("</td>");
        if(fieldType == FieldTypes.Text) {
            buf.append("<td>");
            buf.append(String.format("<span class=\"hide alert\" id=\"%s_wrongAlert\">От %s до %s!</span>", name, start_s, end_s));
            buf.append("</td>");
        }
        buf.append("</tr>");
        return buf.toString();
    }
}
