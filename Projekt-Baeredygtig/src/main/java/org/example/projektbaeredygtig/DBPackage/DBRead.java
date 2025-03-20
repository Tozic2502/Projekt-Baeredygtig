package org.example.projektbaeredygtig.DBPackage;

import org.example.projektbaeredygtig.ColorConverter;
import org.example.projektbaeredygtig.Measurement;

import java.sql.*;
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
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getDate(3),
                    rs.getDate(4),
                    ColorConverter.convert(rs.getInt(5)),
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
        String sql = "SELECT  FROM Measurements WHERE BinID = ? AND MeasureDate = ?";
        Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, binID);
            pstmt.setDate(2, measurementDate);
            ResultSet rs = pstmt.executeQuery();

            //System.out.println(rs.getInt(0));

            // Creates and returns the measurement from the DB
            if (rs.next()) {
                measurement = new Measurement(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        ColorConverter.convert(rs.getInt(5)),
                        rs.getBoolean(6),
                        rs.getFloat(7)
                );
            }
            rs.close();
            return measurement;
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
        String sql = "SELECT * FROM Measurements WHERE MeasureDate = ?";
        List<Measurement> measurements = new ArrayList<Measurement>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, date);
            ResultSet rs = pstmt.executeQuery();

            // Creates and returns the measurement from the DB
            while (rs.next()) {
                measurements.add(new Measurement(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        ColorConverter.convert(rs.getInt(5)),
                        rs.getBoolean(6),
                        rs.getFloat(7)
                ));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
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
            if (rs.next())
            {
                return rs.getString(1);
            }
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
