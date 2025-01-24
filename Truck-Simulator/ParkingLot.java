/**
 * ParkingLot class represents a parking lot with specific capacity constraints and truck limits.
 * It maintains queues for trucks waiting to be ready and trucks that are ready for loading.
 */
public class ParkingLot {
    private long capacityConstraint;        // Capacity constraint of the parking lot
    private long truckLimit;                // Maximum number of trucks allowed in the parking lot
    private long truckNumberInWaiting;      // Current number of trucks in the waiting section
    private long truckNumberInReady;        // Current number of trucks in the ready section
    private MyQueue<Truck> waitingSection;  // Queue to hold trucks waiting to be ready
    private MyQueue<Truck> readySection;    // Queue to hold trucks that are ready for loading

    /**
     * Default constructor initializes an empty ParkingLot object.
     */
    ParkingLot() {
    }
    /**
     * Parameterized constructor initializes a ParkingLot object with specified capacity constraint and truck limit.
     * It also initializes the queues for waiting and ready sections.
     *
     * @param capacityConstraint Maximum load capacity for the parking lot
     * @param truckLimit         Maximum number of trucks allowed in the parking lot
     */
    ParkingLot(long capacityConstraint, long truckLimit) {
        this.waitingSection = new MyQueue<>();
        this.readySection = new MyQueue<>();
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
    }

    /**
     * Adds a truck to the waiting section if the truck limit is not exceeded.
     *
     * @param truck The truck to be added to the waiting section
     */
    void addTruck(Truck truck) {
        if (truckNumberInWaiting + truckNumberInReady < truckLimit) {
            waitingSection.add(truck); // Add truck to waiting section
            truckNumberInWaiting++; // Increment count of trucks in waiting
        }
    }

    /**
     * Removes and returns the first truck from the ready section if available.
     *
     * @return The truck removed from the ready section, or null if the section is empty
     */
    Truck removeFromReady() {
        if (truckNumberInReady > 0) {
            truckNumberInReady--; // Decrement count of trucks in ready section
            return readySection.remove(); // Remove and return truck from ready section
        }
        return null; // Return null if no trucks are in ready section
    }

    /**
     * Moves the first truck from the waiting section to the ready section.
     *
     * @return The truck moved to the ready section, or null if the waiting section is empty
     */
    Truck moveToReady() {
        if (!waitingSection.isEmpty()) {
            Truck firstWaitingTruck = waitingSection.remove();
            truckNumberInWaiting--;
            readySection.add(firstWaitingTruck);
            truckNumberInReady++;
            return firstWaitingTruck; // Return the truck moved to ready section
        }
        return null; // Return null if no trucks are in waiting section
    }

    /**
     * @return The capacity constraint of the parking lot
     */
    long getCapacityConstraint() {
        return capacityConstraint;
    }

    /**
     * @return The number of trucks in the waiting section
     */
    long getTruckNumberInWaiting() {
        return truckNumberInWaiting;
    }

    /**
     * @return The number of trucks in the ready section
     */
    long getTruckNumberInReady() {
        return truckNumberInReady;
    }

    /**
     * Checks if there is availability for more trucks in the parking lot.
     *
     * @return True if more trucks can be added, false otherwise
     */
    boolean getAvailability() {
        return truckLimit > getTruckNumberInWaiting() + getTruckNumberInReady();
    }
}
