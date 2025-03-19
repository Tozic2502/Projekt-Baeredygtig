package org.example.projektbaeredygtig;

import java.sql.Date;

public class Measurement {

    private int MeasureID;
    private int BinID;
    private Date MeasuredDate;
    private Date EmptiedDate;
    private BinColor Color;
    private Boolean HazardWaste;

    public Measurement() {}

    public Measurement(int MeasureID, int BinID, Date MeasuredDate, Date EmptiedDate, BinColor Color, Boolean HazardWaste) {
        this.MeasureID = MeasureID;
        this.BinID = BinID;
        this.MeasuredDate = MeasuredDate;
        this.EmptiedDate = EmptiedDate;
        this.Color = Color;
        this.HazardWaste = HazardWaste;
    }

    public int getMeasureID() {
        return MeasureID;
    }

    public void setMeasureID(int measureID) {
        MeasureID = measureID;
    }

    public int getBinID() {
        return BinID;
    }

    public void setBinID(int binID) {
        BinID = binID;
    }

    public Date getMeasuredDate() {
        return MeasuredDate;
    }

    public void setMeasuredDate(Date measuredDate) {
        MeasuredDate = measuredDate;
    }

    public Date getEmptiedDate() {
        return EmptiedDate;
    }

    public void setEmptiedDate(Date emptiedDate) {
        EmptiedDate = emptiedDate;
    }

    public BinColor getColor() {
        return Color;
    }

    public void setColor(BinColor color) {
        Color = color;
    }

    public Boolean getHazardWaste() {
        return HazardWaste;
    }

    public void setHazardWaste(Boolean hazardWaste) {
        HazardWaste = hazardWaste;
    }
}
