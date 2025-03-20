package org.example.projektbaeredygtig;

import org.example.projektbaeredygtig.DBPackage.DBCreate;
import org.example.projektbaeredygtig.DBPackage.DBRead;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class CSVReader {

    /**
     * Reads CSV file and adds the date to the database.
     * Returns null if file was not found.
     * @param path of CSV file.
     */
    public static void ReadCSV(String path)
    {
        Scanner scanner = null;
        File file = new File(path);
        try {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            e.getMessage();
            return;
        }
        scanner.nextLine();
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            Measurement measurement = new Measurement();
            // Date time
            String dTime = parts[0].substring(0, parts[0].indexOf(" "));
            dTime = dTime.replaceAll("/", "-");
            measurement.setMeasuredDate(Date.valueOf(dTime));    // MeasuredDate
            measurement.setBinID(Integer.parseInt(parts[1]));       // BinID
            measurement.setColor(ColorConverter.convert(parts[5])); // BinColor
            measurement.setHazardWaste(Boolean.parseBoolean(parts[6])); // HazardWaste
            measurement.setFoodWaste(Boolean.parseBoolean(parts[7]));
            measurement.setBinLevel(Float.parseFloat(parts[8]));

            if (!DBRead.doesItExist(measurement.getBinID(), measurement.getMeasuredDate()))
            {
                DBCreate.createMeasurement(measurement);
            }
        }
    }
}
