/**
 * Truck class represents a truck with an ID, capacity, load, and remaining capacity.
 * It provides methods to manage the truck's load and capacity.
 */
public class Truck {
    private long id;                // Unique identifier for the truck
    private long capacity;          // Maximum capacity of the truck
    private long load;              // Current load in the truck
    private long remainingCapacity;  // Remaining capacity of the truck

    /**
     * Default constructor initializes a Truck object with default values.
     */
    Truck() {
        this.id = 0;
        this.capacity = 0;
        this.load = 0;
        this.remainingCapacity = 0;
    }

    /**
     * Parameterized constructor initializes a Truck object with specified ID and capacity.
     *
     * @param id       Unique identifier for the truck
     * @param capacity Maximum capacity of the truck
     */
    Truck(long id, long capacity) {
        this.id = id;
        this.capacity = capacity;
        this.load = 0;
        this.remainingCapacity = capacity;
    }

    /**
     * Returns the ID of the truck.
     *
     * @return the ID of the truck
     */
    long getId() {
        return this.id;
    }

    /**
     * Returns the remaining capacity of the truck.
     *
     * @return the remaining capacity of the truck
     */
    long getCapacityConstraint() {
        return this.remainingCapacity;
    }

    /**
     * Adds the specified load to the truck and updates the remaining capacity.
     *
     * @param load Amount of load to be received by the truck
     * @return the amount of load received
     */
    void receiveLoad(long load) {
        this.load += load;
        this.remainingCapacity -= load;
    }

    /**
     * Empties the truck by setting the load to 0 and resetting the remaining capacity to its maximum.
     */
    void unloadTruck() {
        this.load = 0;
        this.remainingCapacity = capacity;
    }
}
