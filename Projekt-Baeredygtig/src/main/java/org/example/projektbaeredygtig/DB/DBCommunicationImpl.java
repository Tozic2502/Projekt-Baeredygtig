package org.example.projektbaeredygtig.DB;

import org.example.projektbaeredygtig.Measurement;

public class DBCommunicationImpl implements DBCommunication
{
    @Override
    public void createMeasurement()
    {
        DBCreate.createMeasurement(new Measurement());
    }

    @Override
    public Measurement getMeasurement(int measurementID)
    {
        return null;
    }
}
