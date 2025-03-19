package org.example.projektbaeredygtig;

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

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            Measurement measurement = new Measurement();
            measurement.setMeasureID(Integer.parseInt(parts[0]));   // MeasureID
            measurement.setBinID(Integer.parseInt(parts[1]));       // BinID
            measurement.setMeasuredDate(Date.valueOf(parts[2]));    // MeasuredDate
            measurement.setEmptiedDate(Date.valueOf(parts[3]));     // EmptiedDate
            measurement.setColor(ColorConverter.convert(parts[4])); // BinColor
            measurement.setHazardWaste(Boolean.parseBoolean(parts[5])); // HazardWaste
        }
    }
}
