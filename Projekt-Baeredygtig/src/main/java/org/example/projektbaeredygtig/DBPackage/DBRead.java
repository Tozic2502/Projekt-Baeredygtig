package org.example.projektbaeredygtig.DBPackage;

import org.example.projektbaeredygtig.ColorConverter;
import org.example.projektbaeredygtig.Measurement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBRead {

    /**
     * Attempts to get an existing measurement from the Database.
     * @param id MeasureID
     * @return Measurement object with the variables from the Database.
     */
    public static Measurement getMeasurement(int id)
    {
         Connection conn = DBConnection.getConnection();
         String sql = "SELECT * FROM Measurements WHERE id = " + id;
         Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            // Creates and returns the measurement from the DB
            return new Measurement(
                    rs.getInt(0),
                    rs.getInt(1),
                    rs.getDate(2),
                    rs.getDate(3),
                    ColorConverter.convert(rs.getInt(4)),
                    rs.getBoolean(5)
            );
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to get an existing measurement from the Database.
     * @param binID BinID.
     * @param measurementDate Date of measurement.
     * @return Measurement object with the variables from the Database.
     */
    public static Measurement getMeasurement(int binID, Date measurementDate)
    {
        Connection conn = DB.getConnection();
        String sql = "SELECT * FROM Measurements WHERE BinID = ? AND MeasureDate = ?";
        Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, binID);
            pstmt.setDate(2, measurementDate);
            ResultSet rs = pstmt.executeQuery();

            // Creates and returns the measurement from the DB
            return new Measurement(
                    rs.getInt(0),
                    rs.getInt(1),
                    rs.getDate(2),
                    rs.getDate(3),
                    ColorConverter.convert(rs.getInt(4)),
                    rs.getBoolean(5)
            );
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to get all existing measurement from the Database with a date matching the input.
     * @param date of measurement
     * @return Measurement object with the variables from the Database.
     */
    public static List<Measurement> getMeasurements(Date date)
    {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT MeasureID FROM Measurements WHERE MeasureDate = " + date;
        List<Measurement> measurements = new ArrayList<Measurement>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, date);
            ResultSet rs = pstmt.executeQuery();

            // Creates and returns the measurement from the DB
            measurements.add(new Measurement(
                    rs.getInt(0),
                    rs.getInt(1),
                    rs.getDate(2),
                    rs.getDate(3),
                    ColorConverter.convert(rs.getInt(4)),
                    rs.getBoolean(5)
            ));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return measurements;
    }

    public static String getCityOfBin(int BinID)
    {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT City FROM Bins WHERE id = " + BinID;
        Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Returns city of the bin.
            return rs.getString(0);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
