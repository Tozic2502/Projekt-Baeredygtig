package org.example.projektbaeredygtig.DB;

import org.example.projektbaeredygtig.Measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBCreate
{

    public void createMeasurement(Measurement measurement) throws Exception
    {
        String sql = "INSERT INTO table (field1, field2, field3, field4, field5)VALUES (?,?,?,?,?)";
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,measurement.getBinID());
        ps.setDate(2, measurement.getMeasuredDate());
        ps.setDate(3, measurement.getEmptiedDate());
        ps.setInt(4,measurement.getColor());
    }


}
