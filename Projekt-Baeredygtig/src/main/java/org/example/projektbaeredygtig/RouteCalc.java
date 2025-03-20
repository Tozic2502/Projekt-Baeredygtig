package org.example.projektbaeredygtig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * RouteCalc calculates the daily and aggregated results for the route system.
 *
 * For every day the trashman drives:
 *  - A constant 30 km is driven to start and 30 km at the end (60 km total).
 *  - Then one alternative detour (with its own distance/time) is driven.
 *
 * The full route (if not taking the alternative) is 80 km and 90 minutes.
 * This class calculates:
 *  - The overall driven distance and time for each day.
 *  - The savings (difference compared to 80 km/90 min) for that day.
 *  - Aggregates the totals for any given date range.
 */
public class RouteCalc {
    // Full route constants (if the alternative was not taken)
    private static final double FULL_ROUTE_DISTANCE = 80.0; // km
    private static final int FULL_ROUTE_TIME = 90; // minutes

    // Constant segments: always drive 30 km to start and 30 km at the end (60 km total).
    private static final double CONSTANT_START = 30.0; // km
    private static final double CONSTANT_END = 30.0;   // km
    private static final double CONSTANT_DISTANCE = CONSTANT_START + CONSTANT_END; // 60 km

    // Compute the average speed (km per minute) from the full route
    private static final double AVERAGE_SPEED = FULL_ROUTE_DISTANCE / FULL_ROUTE_TIME;
    // Compute the time for the constant segments (driving 60 km)
    private static final double CONSTANT_TIME = CONSTANT_DISTANCE / AVERAGE_SPEED;

    /**
     * Container for alternative detour data.
     * Values here represent only the detour part.
     */
    public static class RouteSegment {
        private String name;
        private double detourDistance; // km for the detour part only
        private int detourTime;        // minutes for the detour part only

        public RouteSegment(String name, double detourDistance, int detourTime) {
            this.name = name;
            this.detourDistance = detourDistance;
            this.detourTime = detourTime;
        }

        public String getName() {
            return name;
        }

        public double getDetourDistance() {
            return detourDistance;
        }

        public int getDetourTime() {
            return detourTime;
        }
    }

    /**
     * Available alternative routes (detours).
     * The following data is constant:
     *  - Frem og tilbage Broager: 60 km, 60 min
     *  - Drejby til Sønderby: 9.9 km, 11 min
     *  - Drejby til Østerby og Østerby til Sønderby: 7.6+1.9 km, 10+5 min
     *  - Drejby til Sønderkobbel Strand Camping: 3.8 km, 5 min
     */
    public static final RouteSegment[] ALTERNATIVES = new RouteSegment[] {
            new RouteSegment("Frem og tilbage Broager", 60.0, 60),
            new RouteSegment("Drejby til Sønderby", 9.9, 11),
            new RouteSegment("Drejby til Østerby og Østerby til Sønderby", 7.6 + 1.9, 10 + 5),
            new RouteSegment("Drejby til Sønderkobbel Strand Camping", 3.8, 5)
    };

    /**
     * Represents the calculated result for one day.
     * overallDistance/time: the total driven route (constant segments + detour)
     * distanceSaved/timeSaved: savings compared to the full route (80 km/90 min)
     */
    public static class CalculationResult {
        private double overallDistance;
        private double overallTime;
        private double distanceSaved;
        private double timeSaved;

        public CalculationResult(double overallDistance, double overallTime,
                                 double distanceSaved, double timeSaved) {
            this.overallDistance = overallDistance;
            this.overallTime = overallTime;
            this.distanceSaved = distanceSaved;
            this.timeSaved = timeSaved;
        }

        public double getOverallDistance() {
            return overallDistance;
        }

        public double getOverallTime() {
            return overallTime;
        }

        public double getDistanceSaved() {
            return distanceSaved;
        }

        public double getTimeSaved() {
            return timeSaved;
        }
    }

    /**
     * Represents one day's route record.
     * It holds the date, the chosen alternative route, and the calculation result.
     */
    public static class RouteRecord {
        private LocalDate date;
        private RouteSegment segment;
        private CalculationResult result;

        public RouteRecord(LocalDate date, RouteSegment segment, CalculationResult result) {
            this.date = date;
            this.segment = segment;
            this.result = result;
        }

        public LocalDate getDate() {
            return date;
        }

        public RouteSegment getSegment() {
            return segment;
        }

        public CalculationResult getResult() {
            return result;
        }
    }

    // A list to hold all daily route records.
    private final List<RouteRecord> routeRecords = new ArrayList<>();

    /**
     * Calculates the overall route values for a given alternative route.
     * Overall values = constant segments (60 km and its time) + alternative detour.
     *
     * @param segment the chosen alternative route (detour part only)
     * @return CalculationResult containing overall distance, time and savings.
     */
    public CalculationResult calculateDailyResult(RouteSegment segment) {
        double overallDistance = CONSTANT_DISTANCE + segment.getDetourDistance();
        double overallTime = CONSTANT_TIME + segment.getDetourTime();
        double distanceSaved = FULL_ROUTE_DISTANCE - overallDistance;
        double timeSaved = FULL_ROUTE_TIME - overallTime;
        return new CalculationResult(overallDistance, overallTime, distanceSaved, timeSaved);
    }

    /**
     * Records a daily route calculation.
     * This method can be called for each record you load from your CSV file.
     *
     * @param date       the date the route was driven.
     * @param routeIndex the index of the chosen alternative in the ALTERNATIVES array.
     */
    public void addRouteRecord(LocalDate date, int routeIndex) {
        if (routeIndex < 0 || routeIndex >= ALTERNATIVES.length) {
            throw new IllegalArgumentException("Invalid route index");
        }
        RouteSegment segment = ALTERNATIVES[routeIndex];
        CalculationResult result = calculateDailyResult(segment);
        routeRecords.add(new RouteRecord(date, segment, result));
    }

    /**
     * Aggregates the totals over a given period.
     * For example, to compute totals for a week, month, quarter, or year.
     *
     * @param start the start date (inclusive)
     * @param end   the end date (inclusive)
     * @return an AggregatedResult with total overall distance/time and savings.
     */
    public AggregatedResult aggregatePeriod(LocalDate start, LocalDate end) {
        double totalDrivenDistance = 0;
        double totalDrivenTime = 0;
        double totalDistanceSaved = 0;
        double totalTimeSaved = 0;
        int count = 0;
        for (RouteRecord record : routeRecords) {
            if (!record.getDate().isBefore(start) && !record.getDate().isAfter(end)) {
                totalDrivenDistance += record.getResult().getOverallDistance();
                totalDrivenTime += record.getResult().getOverallTime();
                totalDistanceSaved += record.getResult().getDistanceSaved();
                totalTimeSaved += record.getResult().getTimeSaved();
                count++;
            }
        }
        return new AggregatedResult(totalDrivenDistance, totalDrivenTime, totalDistanceSaved, totalTimeSaved, count);
    }

    /**
     * Container for aggregated results.
     * Contains the totals over the period and the number of days the route was driven.
     */
    public static class AggregatedResult {
        private double totalDrivenDistance;
        private double totalDrivenTime;
        private double totalDistanceSaved;
        private double totalTimeSaved;
        private int daysDriven;

        public AggregatedResult(double totalDrivenDistance, double totalDrivenTime,
                                double totalDistanceSaved, double totalTimeSaved, int daysDriven) {
            this.totalDrivenDistance = totalDrivenDistance;
            this.totalDrivenTime = totalDrivenTime;
            this.totalDistanceSaved = totalDistanceSaved;
            this.totalTimeSaved = totalTimeSaved;
            this.daysDriven = daysDriven;
        }

        public double getTotalDrivenDistance() {
            return totalDrivenDistance;
        }

        public double getTotalDrivenTime() {
            return totalDrivenTime;
        }

        public double getTotalDistanceSaved() {
            return totalDistanceSaved;
        }

        public double getTotalTimeSaved() {
            return totalTimeSaved;
        }

        public int getDaysDriven() {
            return daysDriven;
        }
    }

    /**
     * For debugging or UI purposes: prints all recorded route data.
     */
    public void printAllRecords() {
        for (RouteRecord record : routeRecords) {
            System.out.println("Date: " + record.getDate() + " | Route: " + record.getSegment().getName());
            System.out.printf("  Overall route: %.1f km, %.1f min%n",
                    record.getResult().getOverallDistance(),
                    record.getResult().getOverallTime());
            System.out.printf("  Savings: %.1f km, %.1f min%n",
                    record.getResult().getDistanceSaved(),
                    record.getResult().getTimeSaved());
        }
    }
}
