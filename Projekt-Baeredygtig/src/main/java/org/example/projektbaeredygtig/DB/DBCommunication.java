package org.example.projektbaeredygtig.DB;

import org.example.projektbaeredygtig.Measurement;

public interface DBCommunication
{
    public void createMeasurement();

    public Measurement getMeasurement(int measurementID);
}
