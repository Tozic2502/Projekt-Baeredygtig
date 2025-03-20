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
         String sql = "SELECT * FROM Measurements WHERE MeasureID = " + id;
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
                    rs.getBoolean(5),
                    rs.getBoolean(6),
                    rs.getFloat(7)
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
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Measurements WHERE BinID = ? AND MeasuredDate = ?";
        Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, binID);
            pstmt.setDate(2, new java.sql.Date(measurementDate.getTime()));
            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                System.out.println("Measurement found " + rs.getInt(1));
                // Creates and returns the measurement from the DB
                return new Measurement(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        ColorConverter.convert(rs.getInt(5)),
                        rs.getBoolean(6),
                        rs.getFloat(7)
                );

            }
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
        String sql = "SELECT MeasureID FROM Measurements WHERE MeasuredDate = " + date;
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
                    rs.getBoolean(5),
                    rs.getBoolean(6),
                    rs.getFloat(7)
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
        String sql = "SELECT City FROM Bins WHERE BinID = " + BinID;
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
