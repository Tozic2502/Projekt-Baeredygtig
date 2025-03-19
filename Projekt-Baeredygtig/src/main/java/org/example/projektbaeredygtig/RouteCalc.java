package org.example.projektbaeredygtig;

public class RouteCalc {
    private static final double FULL_ROUTE_DISTANCE = 80.0; // km
    private static final int FULL_ROUTE_TIME = 90; // minutes

    // The common segments at the start and end (each 30 km)
    private static final double COMMON_START = 30.0; // km
    private static final double COMMON_END = 30.0;   // km
    private static final double COMMON_DISTANCE = COMMON_START + COMMON_END; // 60 km total

    // Calculate the full route's average speed (km per minute)
    private static final double AVERAGE_SPEED = FULL_ROUTE_DISTANCE / FULL_ROUTE_TIME;
    // Compute the time required for the common segments
    private static final double COMMON_TIME = COMMON_DISTANCE / AVERAGE_SPEED;

    // Define alternative detour segments as a data structure
    private static class RouteSegment {
        String name;
        double detourDistance; // km for the alternative detour part only
        int detourTime;        // minutes for the alternative detour part only

        public RouteSegment(String name, double detourDistance, int detourTime) {
            this.name = name;
            this.detourDistance = detourDistance;
            this.detourTime = detourTime;
        }
    }

    // List your alternative detours here.
    // Note: For the Østerby detour we combine two segments: "DrejBy til Østerby" and "Østerby til Sønderby".
    private static final RouteSegment[] alternatives = new RouteSegment[] {
            new RouteSegment("Drejby til Sønderby", 9.9, 11),
            new RouteSegment("Drejby til Østerby & Østerby til Sønderby", 7.6 + 1.9, 10 + 5),
            new RouteSegment("Drejby til Sønderkobbel Strand Camping", 3.8, 5)
            // If needed, you can add others such as:
            // new RouteSegment("Frem og tilbage Broager", 58.8, /*detour time if available*/)
    };

    public static void main(String[] args) {
        System.out.printf("Full route: %.1f km, %d min%n", FULL_ROUTE_DISTANCE, FULL_ROUTE_TIME);
        System.out.printf("Common segments: %.1f km, %.1f min%n", COMMON_DISTANCE, COMMON_TIME);
        System.out.println();

        for (RouteSegment segment : alternatives) {
            // Calculate the alternative route total:
            // Total distance = common start + alternative detour + common end
            double alternativeTotalDistance = COMMON_START + segment.detourDistance + COMMON_END;
            // Total time = time for common segments + detour time.
            double alternativeTotalTime = COMMON_TIME + segment.detourTime;

            // Calculate saved distance and time relative to the full route
            double distanceSaved = FULL_ROUTE_DISTANCE - alternativeTotalDistance;
            double timeSaved = FULL_ROUTE_TIME - alternativeTotalTime;

            System.out.println("Alternative: " + segment.name);
            System.out.printf("  Detour only: %.1f km, %d min%n", segment.detourDistance, segment.detourTime);
            System.out.printf("  Total route: %.1f km, %.1f min%n", alternativeTotalDistance, alternativeTotalTime);
            System.out.printf("  Saved: %.1f km, %.1f min%n", distanceSaved, timeSaved);
            System.out.println();
        }
    }
}

