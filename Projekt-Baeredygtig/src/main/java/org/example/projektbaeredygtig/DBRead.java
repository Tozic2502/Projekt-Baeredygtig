package org.example.projektbaeredygtig;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBRead {

    /**
     * Attempts to get an existing measurement from the Database.
     * @param id ID of the measurement
     * @return Measurement object with the variables from the Database.
     */
    public static Measurement getMeasurement(int id)
    {
         Connection conn = DB.getConnection();
         String sql = "SELECT * FROM Measurements WHERE id = " + id;
         Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
     * Attempts to get an existing measurement from the Database based on the date it was measured.
     * @param date of last being emptied
     * @return Measurement object with the variables from the Database.
     */
    public static Measurement getMeasurement(Date date)
    {
        Connection conn = DB.getConnection();
        String sql = "SELECT MeasureID FROM Measurements WHERE MeasureDate = " + date;
        Measurement measurement = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            return getMeasurement(rs.getInt(0));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getCityOfBin(int BinID)
    {
        Connection conn = DB.getConnection();
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
