package org.example.projektbaeredygtig;

public class ColorConverter
{
    public static BinColor convert(int i)
    {
        return switch (i) {
            case 1 -> BinColor.YELLOW;
            case 2 -> BinColor.RED;
            default -> BinColor.GREEN;
        };

    }

    public static BinColor convert(String s)
    {
        return switch (s) {
            case "yellow LED" -> BinColor.YELLOW;
            case "red LED" -> BinColor.RED;
            default -> BinColor.GREEN;
        };
    }
}
