package ru.yellowblacksnek;

import static ru.yellowblacksnek.AreaUtils.*;

public class ServerConfig {
    public final static String method = "get";
    public final static AreaUtils.Rect rect = new AreaUtils.Rect("-R/2", "R", R/2, R);
    public final static AreaUtils.Triangle poly = new AreaUtils.Triangle(new AreaUtils.Point("0", "R/2"), new AreaUtils.Point("R", "0"));
    public final static AreaUtils.Arc arc = new AreaUtils.Arc(R/2, AreaUtils.ArcSectors.III);
    public final static FieldConfig x = new FieldConfig("x", FieldConfig.FieldTypes.Button, FieldConfig.DataTypes.Int_And_Halves, -2, 2);
    public final static FieldConfig y = new FieldConfig("y", FieldConfig.FieldTypes.Text, FieldConfig.DataTypes.Text, -3, 5);
    public final static FieldConfig r = new FieldConfig("r", FieldConfig.FieldTypes.CheckBox, FieldConfig.DataTypes.Int, 1, 5);
    public final static boolean useContext = true;
    public final static String variant = "201015";
}
