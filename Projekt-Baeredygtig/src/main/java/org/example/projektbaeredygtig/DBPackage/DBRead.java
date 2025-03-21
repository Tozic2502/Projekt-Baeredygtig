package org.example.projektbaeredygtig.DBPackage;

import org.example.projektbaeredygtig.BinColor;
import org.example.projektbaeredygtig.ColorConverter;
import org.example.projektbaeredygtig.Measurement;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println(e.getLocalizedMessage());
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
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

    public static Map<String, Map<BinColor, Long>> getBinMeasureDataForPeriod(Date startDate, Date endDate) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT BinID, Colour, COUNT(*) " +
                "FROM Measurements " +
                "WHERE MeasureDate BETWEEN ? AND ? " +
                "GROUP BY BinID, Colour";
        Map<String, Map<BinColor, Long>> binMeasureData = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String binId = rs.getString("BinID");
                BinColor color = ColorConverter.convert(rs.getInt("Colour"));
                long count = rs.getLong(3);

                binMeasureData.computeIfAbsent(binId, k -> new HashMap<>()).put(color, count);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving bin measure data: " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return binMeasureData;
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
            System.out.println(e.getLocalizedMessage());
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

    public static Date MaxDate()
    {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT MAX(MeasureDate) FROM Measurements";
        Date date = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                date = rs.getDate(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static Date MinDate()
    {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT MIN(MeasureDate) FROM Measurements";
        Date date = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                date = rs.getDate(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static Map<BinColor, Long> getColorData(Date startDate, Date endDate) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT Colour, COUNT(*) FROM Measurements WHERE MeasureDate BETWEEN ? AND ? GROUP BY Colour";
        Map<BinColor, Long> colorCountMap = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BinColor color = ColorConverter.convert(rs.getInt("Colour"));
                long count = rs.getLong(2);
                colorCountMap.put(color, count);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving color data: " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return colorCountMap;
    }

    public static Map<String, Map<BinColor, Long>> getMonthlyColorDataForQuarter(int year, String quarter) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT MONTH(MeasureDate) AS Month, Color, COUNT(*) FROM Measurements WHERE YEAR(MeasureDate) = ? AND QUARTER(MeasureDate) = ? GROUP BY MONTH(MeasureDate), Colour";
        Map<String, Map<BinColor, Long>> monthlyColorData = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setString(2, quarter);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String month = YearMonth.of(year, rs.getInt("Month")).getMonth().name();
                BinColor color = ColorConverter.convert(rs.getInt("Colour"));
                long count = rs.getLong(3);

                monthlyColorData.computeIfAbsent(month, k -> new HashMap<>()).put(color, count);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving monthly color data for quarter: " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return monthlyColorData;
    }

    public static Map<String, Map<BinColor, Long>> getWeeklyColorDataForMonth(int year, int month) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT WEEK(MeasureDate) AS Week, Color, COUNT(*) FROM Measurements WHERE YEAR(MeasureDate) = ? AND MONTH(MeasureDate) = ? GROUP BY WEEK(MeasureDate), Colour";
        Map<String, Map<BinColor, Long>> weeklyColorData = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String week = "Week " + rs.getInt("Week");
                BinColor color = ColorConverter.convert(rs.getInt("Colour"));
                long count = rs.getLong(3);

                weeklyColorData.computeIfAbsent(week, k -> new HashMap<>()).put(color, count);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving weekly color data for month: " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return weeklyColorData;
    }

    public static Map<String, Map<BinColor, Long>> getDailyColorDataForWeek(int year, int month, int week) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT DAY(MeasureDate) AS Day, Color, COUNT(*) FROM Measurements WHERE YEAR(MeasureDate) = ? AND MONTH(MeasureDate) = ? AND WEEK(MeasureDate) = ? GROUP BY DAY(MeasureDate), Colour";
        Map<String, Map<BinColor, Long>> dailyColorData = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, week);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String day = "Day " + rs.getInt("Day");
                BinColor color = ColorConverter.convert(rs.getInt("Colour"));
                long count = rs.getLong(3);

                dailyColorData.computeIfAbsent(day, k -> new HashMap<>()).put(color, count);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving daily color data for week: " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return dailyColorData;
    }
}
