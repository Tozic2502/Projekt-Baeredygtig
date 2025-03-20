package org.example.projektbaeredygtig.DBPackage;

import org.example.projektbaeredygtig.ColorConverter;
import org.example.projektbaeredygtig.Measurement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRead {

    /**
     * Attempts to get an existing measurement from the Database.
     * @param BinID MeasureID
     * @return Measurement object with the variables from the Database.
     */
    public static Measurement getMeasurement(int BinID)
    {
         Connection conn = DBConnection.getConnection();
         String sql = "SELECT * FROM Measurements WHERE BinID = " + BinID;
         Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, BinID);
            ResultSet rs = pstmt.executeQuery();

            // Creates and returns the measurement from the DB
            return new Measurement(
                    rs.getInt(0), //MeasureID
                    rs.getInt(1), //BinID
                    rs.getDate(2), //MeasureDate
                    rs.getDate(3), //EmptiedDate
                    ColorConverter.convert(rs.getInt(4)), //Colour
                    rs.getBoolean(5), //HazardWaste
                    rs.getBoolean(6), //FoodWaste
                    rs.getFloat(7) // BinLevel
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
        String sql = "SELECT  FROM Measurements WHERE BinID = ? AND MeasureDate = ?";
        Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, binID);
            pstmt.setDate(2, measurementDate);
            ResultSet rs = pstmt.executeQuery();

            //System.out.println(rs.getInt(0));
            /*
            // Creates and returns the measurement from the DB
            Measurement ms = new Measurement(
                    rs.getInt(0),
                    rs.getInt(1),
                    rs.getDate(2),
                    rs.getDate(3),
                    ColorConverter.convert(rs.getInt(4)),
                    rs.getBoolean(5),
                    rs.getBoolean(6),
                    rs.getFloat(7)
            );
            rs.close();
            return ms;

             */
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
        String sql = "SELECT MeasureID FROM Measurements WHERE MeasureDate = '2026-01-01'";
        List<Measurement> measurements = new ArrayList<Measurement>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //pstmt.setDate(1, date);
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

    public static boolean doesItExist(int BinID, Date measurementDate)
    {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Measurements WHERE MeasureDate = ? AND BinID = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDate(1, measurementDate);
            preparedStatement.setInt(2, BinID);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                if (rs.getInt("MeasureID") > 0)
                {
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
