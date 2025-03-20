package org.example.projektbaeredygtig.DBPackage;

import org.example.projektbaeredygtig.Measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBCreate
{
    public static void createMeasurement(Measurement measurement)
    {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO Measurements (BinID, MeasureDate, EmptiedDate, Colour, HazardWaste, BinLevel) VALUES (?,?,?,?,?,?)";
        try
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, measurement.getBinID());
            ps.setDate(2, measurement.getMeasuredDate());
            ps.setDate(3, measurement.getEmptiedDate());
            ps.setInt(4, measurement.getColor().ordinal());
            ps.setBoolean(5, measurement.getHazardWaste());
            ps.setFloat(6, measurement.getBinLevel());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0)
            {
                //System.out.println("Measurement created successfully");
            }
            else
            {
                //System.out.println("Measurement creation failed");
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        //DBConnection.disconnect();
    }
}
