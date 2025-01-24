import java.io.FileWriter;
import java.io.IOException;
public class Methods {

    /**
     * Creates a new ParkingLot with the specified capacity constraint and truck limit.
     *
     * @param capacityConstraint The maximum load capacity of the parking lot
     * @param truckLimit The maximum number of trucks allowed in the parking lot
     * @return A new ParkingLot instance with the specified capacity constraint and truck limit
     */
    static ParkingLot createParkingLot(long capacityConstraint, long truckLimit) {
        return new ParkingLot(capacityConstraint, truckLimit);
    }

    /**
     * Finds the nearest available parking lot in the AVL tree with a capacity constraint
     * less than or equal to the specified capacity constraint.
     *
     * @param AVLtree The AVL tree containing parking lots
     * @param capacityConstraint The capacity constraint to compare against
     * @return A parking lot with the largest capacity constraint below the specified constraint
     *         and is available, or null if no such lot is found
     */
    static ParkingLot findLowerAvailableLot(MyAvlTree AVLtree, long capacityConstraint) {

        ParkingLot parkingLot = AVLtree.searchElement(capacityConstraint);
        // if there exist a lot with given capacity constraint, then return it
        if (parkingLot != null && parkingLot.getAvailability()) {
            return parkingLot;
        }

        // Use the helper function to get the highest smaller element directly
        parkingLot = findHighestSmallerElement(AVLtree, capacityConstraint);
        if(parkingLot != null && parkingLot.getAvailability()){
            return parkingLot;
        }
        else return null;
    }

    /**
     * Finds the node in the AVL tree with the largest capacity constraint that is less than
     * the specified capacity constraint. This node is considered the "highest smaller" element.
     *
     * @param AVLtree The AVL tree containing parking lots
     * @param capacityConstraint The capacity constraint to compare against
     * @return The ParkingLot with the largest capacity constraint less than the specified constraint,
     *         or null if no such parking lot exists
     */
    static ParkingLot findHighestSmallerElement(MyAvlTree AVLtree, long capacityConstraint) {
        Node current = AVLtree.getRootNode(); // Start search from the root node
        Node predecessor = null; // Initialize predecessor node to track the highest smaller element

        while (current != null) {
            // If current node's capacity is smaller, update predecessor and move right
            if (capacityConstraint > current.element.getCapacityConstraint()) {
                predecessor = current;
                current = current.rightChild;
            }
            // Otherwise, move to the left child
            else {
                current = current.leftChild;
            }
        }
        if (predecessor != null){
            return predecessor.element;
        }
        else{
            return null;
        }
    }


    /**
     * Finds the nearest available parking lot in the AVL tree with a capacity constraint
     * greater than or equal to the specified capacity constraint.
     *
     * @param AVLtree The AVL tree containing parking lots
     * @param capacityConstraint The capacity constraint to compare against
     * @return A parking lot with the smallest capacity constraint above the specified constraint
     *         and has trucks waiting, or null if no such lot is found
     */
    static ParkingLot findUpperAvailableLot(MyAvlTree AVLtree, long capacityConstraint) {

        ParkingLot parkingLot = AVLtree.searchElement(capacityConstraint);

        if (parkingLot != null && parkingLot.getTruckNumberInWaiting() > 0) {
            return parkingLot;
        }
        // Use the helper function to get the least big element directly
        parkingLot = findLeastBiggerElement(AVLtree, capacityConstraint);
        if(parkingLot != null && parkingLot.getTruckNumberInWaiting()>0){
            return parkingLot;
        }
        else return null;
    }

    /**
     * Finds the nearest available parking lot in the AVL tree with a capacity constraint
     * greater than or equal to the specified capacity constraint for loading purposes.
     *
     * @param AVLtree The AVL tree containing parking lots
     * @param capacityConstraint The capacity constraint to compare against
     * @return A parking lot with the smallest capacity constraint greater than or equal to the specified constraint,
     *         or null if no such lot is found
     */
    static ParkingLot findUpperAvailableLotForLoad(MyAvlTree AVLtree, long capacityConstraint) {
        ParkingLot parkingLot = AVLtree.searchElement(capacityConstraint);

        if (parkingLot != null) {
            return parkingLot;
        }

        // Use the helper function to get the least big element directly
        parkingLot = findLeastBiggerElement(AVLtree, capacityConstraint);
        return parkingLot;
    }

    /**
     * Finds the node in the AVL tree with the smallest capacity constraint greater than
     * the specified capacity constraint. This node is considered the "least bigger" element.
     *
     * @param AVLtree The AVL tree containing parking lots
     * @param capacityConstraint The capacity constraint to compare against
     * @return The ParkingLot with the smallest capacity constraint greater than the specified constraint,
     *         or null if no such parking lot exists
     */
    static ParkingLot findLeastBiggerElement(MyAvlTree AVLtree, long capacityConstraint) {
        Node current = AVLtree.getRootNode();
        Node successor = null;
        // Traverse the AVL tree to find the least big element
        while (current != null) {
            // If current node's capacity is greater than the specified constraint, update successor and move left
            if (capacityConstraint < current.element.getCapacityConstraint()) {
                successor = current;
                current = current.leftChild;
            }
            // Otherwise, move to the right child
            else {
                current = current.rightChild;
            }
        }
        // Return the successor's element if found, otherwise return null
        if(successor != null){
            return successor.element;
        }
        else {
            return null;
        }
    }

    /**
     * Attempts to add a truck to an available parking lot within the specified capacity constraint.
     * If no suitable parking lot is found, writes "-1" to the output file. Otherwise, updates the
     * appropriate AVL trees and writes the capacity constraint of the selected parking lot to the output file.
     *
     * @param availableAVLtree The AVL tree of available parking lots
     * @param readyParkingLots The AVL tree of parking lots that have truck in waiting
     * @param truck The truck to be added to the parking lot
     * @param outputFile The FileWriter object to write output results
     * @throws IOException If an I/O error occurs while writing to the file
     */
    static void addToParkingLot(MyAvlTree availableAVLtree, MyAvlTree readyParkingLots, Truck truck, FileWriter outputFile) throws IOException {
        long capacityConstraint = truck.getCapacityConstraint();

        // finds the lot with at most given capacity constraint, if there is no null is returned
        ParkingLot availableLot = findLowerAvailableLot(availableAVLtree, capacityConstraint);

        if (availableLot == null) {
            outputFile.write("-1" + "\n");
            return;
        }
        boolean isWaitingEmpty = (availableLot.getTruckNumberInWaiting() == 0);

        availableLot.addTruck(truck);
        // if lot was empty before adding the truck, then we add it to readyParkingLots
        if (isWaitingEmpty) {
            readyParkingLots.insertElement(availableLot);
        }
        // if lot becomes full after addition, we delete the lot from availableAVLtree
        if (!availableLot.getAvailability()) {
            availableAVLtree.deleteElement(availableLot.getCapacityConstraint());
        }
        // <capacity constraint> format
        outputFile.write(availableLot.getCapacityConstraint() + "\n");
    }


    /**
     * Attempts to add a truck to an available parking lot based on the truck's remaining capacity constraint.
     * If no suitable parking lot is found, writes id -1,
     * if successful, updates the appropriate AVL trees and writes the truck ID along with the capacity constraint
     *
     * @param availableAVLtree The AVL tree of available parking lots
     * @param readyParkingLots The AVL tree of parking lots that have truck in waiting section
     * @param truck The truck to be added to the parking lot
     * @param outputFile The FileWriter object to write output results
     * @throws IOException If an I/O error occurs while writing to the file
     */
    static void addToParkingLotFromLoad(MyAvlTree availableAVLtree, MyAvlTree readyParkingLots, Truck truck, FileWriter outputFile) throws IOException {
        long capacityConstraint = truck.getCapacityConstraint();

        // finds the lot with at most given capacity constraint
        // if there is no lot found, then null is returned
        ParkingLot availableLot = findLowerAvailableLot(availableAVLtree, capacityConstraint);

        if (availableLot == null) {
            outputFile.write(truck.getId() + " -1");
            return;
        }
        boolean isWaitingEmpty = availableLot.getTruckNumberInWaiting() == 0;

        availableLot.addTruck(truck);
        // if lot was empty before adding the truck, then we add it to readyParkingLots
        if (isWaitingEmpty) {
            readyParkingLots.insertElement(availableLot);
        }
        // if lot becomes full after addition, we delete the lot from availableAVLtree
        if (!availableLot.getAvailability()) {
            availableAVLtree.deleteElement(availableLot.getCapacityConstraint());
        }

        // <id> <capacity constraint> format
        outputFile.write(truck.getId() + " " + availableLot.getCapacityConstraint());
    }

    /**
     * Transfers a truck from the waiting section to the ready section of a parking lot with a capacity
     * constraint greater than or equal to the specified value. If no suitable parking lot is found,
     * writes "-1". Otherwise, updates the AVL trees and writes the truck ID and
     * parking lot capacity constraint to the output file.
     *
     * @param readyParkingLots The AVL tree of parking lots that have truck in waiting section
     * @param readyToLoadParkingLots The AVL tree of parking lots with trucks ready to be loaded
     * @param capacityConstraint The minimum capacity constraint for the parking lot
     * @param outputFile The FileWriter object to write output results
     * @throws IOException If an I/O error occurs while writing to the file
     */
    public static void transferToReady(MyAvlTree readyParkingLots,
                                       MyAvlTree readyToLoadParkingLots,
                                       long capacityConstraint,
                                       FileWriter outputFile) throws IOException {
        // finds lot that has at least given capacity constraint, if there is no then null
        ParkingLot availableLot = findUpperAvailableLot(readyParkingLots, capacityConstraint);
        if (availableLot == null) {
            outputFile.write("-1" + "\n");
            return;
        }
        boolean doesAlreadyHaveReady = availableLot.getTruckNumberInReady() > 0;
        Truck transferredTruck = availableLot.moveToReady();
        // if there were no truck in ready, then we insert the lot to readyToLoadParkingLots
        if (!doesAlreadyHaveReady) {
            readyToLoadParkingLots.insertElement(availableLot);
        }
        // since we transferred the truck from waiting we check if waiting section has become empty
        // if so, then remove it from readyParkingLots
        if (availableLot.getTruckNumberInWaiting() == 0) {
            readyParkingLots.deleteElement(availableLot.getCapacityConstraint());
        }
        // writes <id> <capacity constraint>
        outputFile.write(transferredTruck.getId() + " " + availableLot.getCapacityConstraint() + "\n");
    }

    /**
     * Loads a specified amount of load into trucks across available parking lots that meet a certain capacity constraint.
     * This method iteratively finds available parking lots and trucks that can take the load, distributing the load among them.
     * If none of the load is distributed, writes "-1" to the output file.
     *
     * @param availabelAVLtree AVL tree containing available parking lots, i.e. lots that can take truck
     * @param readyParkingLots AVL tree of parking lots that have truck in waiting
     * @param readyToLoadParkingLots AVL tree of parking lots ready to load trucks
     * @param capacityConstraint The capacity constraint of parking lot to be used
     * @param loadAmount The total load to be distributed across trucks
     * @param outputFile FileWriter object to write output results
     * @throws IOException If an I/O error occurs while writing to the file
     */
    public static void loadToTrucks(MyAvlTree availabelAVLtree,
                                    MyAvlTree readyParkingLots,
                                    MyAvlTree readyToLoadParkingLots,
                                    long capacityConstraint,
                                    long loadAmount, FileWriter outputFile) throws IOException {
        // assigned to remaining load to check if at least some of them is distributed at the end
        long remainingLoad = loadAmount;
        while (true) {
            if (remainingLoad == 0) break; // if there is no load left, we can terminate load operation

            // finds the available lot (with given capacity constraint(or bigger)) that has ready trucks
            ParkingLot availableLot = findUpperAvailableLotForLoad(readyToLoadParkingLots, capacityConstraint);

            // if there is no available lot for load, then terminate
            if (availableLot == null) {
                break;
            }

            // continues to perform load until there is no load or truck in ready left
            while (availableLot.getTruckNumberInReady() != 0) {
                boolean isAlreadyAvailable = availableLot.getAvailability();
                Truck currentReadyTruck = availableLot.removeFromReady();

                // if it did not have place for truck, but now it has then, insert it to availableAVLtree
                if (!isAlreadyAvailable) {
                    availabelAVLtree.insertElement(availableLot);
                }
                // after removing the truck from ready section, we check if it becomes empty, if so then,
                // delete from readyToLoadParkingLots
                if (availableLot.getTruckNumberInReady() == 0) {
                    readyToLoadParkingLots.deleteElement(availableLot.getCapacityConstraint());
                }
                // to write in <> - <> format
                if (remainingLoad != loadAmount) {
                    outputFile.write(" - ");
                }

                // Check if the remaining load exceeds the capacity of the available lot
                if (remainingLoad > availableLot.getCapacityConstraint()) {
                    remainingLoad -= availableLot.getCapacityConstraint();
                    currentReadyTruck.receiveLoad(availableLot.getCapacityConstraint());

                    // If the truck's capacity is fully utilized, unload it,
                    // getCapacityConstraint method returns the remaining capacity
                    if (currentReadyTruck.getCapacityConstraint() == 0) {
                        currentReadyTruck.unloadTruck();
                    }
                    // Add the truck back to a suitable parking lot after loading
                    addToParkingLotFromLoad(availabelAVLtree, readyParkingLots, currentReadyTruck, outputFile);
                } else {
                    currentReadyTruck.receiveLoad(remainingLoad);
                    // If the truck's capacity is fully utilized, unload it
                    if (currentReadyTruck.getCapacityConstraint() == 0) {
                        currentReadyTruck.unloadTruck();
                    }
                    // Set remaining load to zero, as it's now fully distributed
                    remainingLoad = 0;
                    // Add the truck back to a suitable parking lot after loading
                    addToParkingLotFromLoad(availabelAVLtree, readyParkingLots, currentReadyTruck, outputFile);
                    // Exit the loop since the load has been fully distributed
                    break;
                }
            }

        }
        // to write in the given format
        if (loadAmount == remainingLoad) {
            outputFile.write("-1\n");
        } else {
            outputFile.write("\n");
        }
    }

    /**
     * Counts the total number of trucks in parking lots with a capacity greater than the specified constraint.
     *
     * @param parkingLots The AVL tree of parking lots
     * @param capacityConstraint The capacity constraint to compare against
     * @return The total number of trucks in parking lots with larger capacities
     */
    public static long countTrucksInLargerLots(MyAvlTree parkingLots, long capacityConstraint) {
        return countTrucksInLargerLots(parkingLots.getRootNode(), capacityConstraint);
    }

    /**
     * Helper method to recursively count trucks in parking lots that have a capacity greater than the specified constraint.
     *
     * @param node The current node in the AVL tree
     * @param capacityConstraint The capacity constraint to compare against
     * @return The total number of trucks in parking lots with capacities larger than the constraint
     */
    private static long countTrucksInLargerLots(Node node, long capacityConstraint) {
        // if empty then truck number is zero
        if (node == null) {
            return 0;
        }
        long totalTruckNumber = 0;
        if (node.element.getCapacityConstraint() > capacityConstraint) {
            // this statement ensures that we take trucks from lots that have higher capacity constraint from the given
            totalTruckNumber += node.element.getTruckNumberInReady();
            totalTruckNumber += node.element.getTruckNumberInWaiting();
        }

        // Traverse both left and right subtrees
        totalTruckNumber += countTrucksInLargerLots(node.leftChild, capacityConstraint); // left subtree truck number
        totalTruckNumber += countTrucksInLargerLots(node.rightChild, capacityConstraint); // right subtree truck number

        return totalTruckNumber;
    }
}
