/**
 * This program manages a fleet of trucks with varying capacities, ensuring that they are assigned to jobs efficiently and effectively.
 * @author Kerem Oğuz, Student ID: 2022400270
 * @since Date: 07.11.2024
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main class to manage parking lot operations using AVL trees.
 * This program reads instructions from an input file to perform
 * various operations on parking lots and trucks, such as creation,
 * deletion, addition of trucks, and load management.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // Constructing 4 AVL trees to handle parking lot operations based on their statuses
        MyAvlTree parkingLots = new MyAvlTree();              // All parking lots
        MyAvlTree availableParkingLots = new MyAvlTree();     // Available parking lots (lots that can take new truck )
        MyAvlTree readyParkingLots = new MyAvlTree();         // Ready parking lots (lots that have truck in waiting)
        MyAvlTree readyToLoadParkingLots = new MyAvlTree();   // Parking lots ready to load (lots that have truck in ready section)

        String actions = args[0];
        File actionsFile = new File(actions);
        FileWriter outputFile = new FileWriter(args[1]);

        if (!actionsFile.exists()) {
            System.out.printf("%s cannot be found.", actionsFile);
            System.exit(1);
        }
        Scanner actionsInputFile = new Scanner(actionsFile);

        // Reading and processing each line from the input file
        while (actionsInputFile.hasNextLine()) {
            String line = actionsInputFile.nextLine(); // Current line is kept
            String[] info = line.split(" "); // in given format words are spaced so, we split by blank char

            // process create_parking_lot <capacity constraint> <truck limit> (given format)
            if (info[0].equals("create_parking_lot")) {
                long capacityConstraint = Long.parseLong(info[1]);
                long truckLimit = Long.parseLong(info[2]);
                ParkingLot parkingLot = Methods.createParkingLot(capacityConstraint, truckLimit);
                // after creating the lot it is initially added to parking lots
                // and availableParkingLots (lots that have place, initially all lots have place)
                parkingLots.insertElement(parkingLot);
                availableParkingLots.insertElement(parkingLot);
            }

            // process delete_parking_lot <capacity constraint>
            if (info[0].equals("delete_parking_lot")) {
                long capacityConstraint = Long.parseLong(info[1]);
                // parking lot should be deleted from all AVL trees since no operation can be done on it now
                parkingLots.deleteElement(capacityConstraint);
                availableParkingLots.deleteElement(capacityConstraint);
                readyParkingLots.deleteElement(capacityConstraint);
                readyToLoadParkingLots.deleteElement(capacityConstraint);
            }

            // process delete_parking_lot <capacity constraint>
            if (info[0].equals("add_truck")) {
                long truckId = Long.parseLong(info[1]);
                long capacity = Long.parseLong(info[2]);
                Methods.addToParkingLot(availableParkingLots, readyParkingLots, new Truck(truckId, capacity), outputFile);
            }

            // process ready <capacity constraint>
            if (info[0].equals("ready")) {
                long capacityConstraint = Long.parseLong(info[1]);
                Methods.transferToReady(readyParkingLots, readyToLoadParkingLots, capacityConstraint, outputFile);
            }

            // process load <capacity constraint> <load amount>
            if (info[0].equals("load")) {
                long capacityConstraint = Long.parseLong(info[1]);
                long loadAmount = Long.parseLong(info[2]);
                Methods.loadToTrucks(availableParkingLots, readyParkingLots, readyToLoadParkingLots, capacityConstraint, loadAmount, outputFile);
            }

            // process count <capacity constraint>
            if (info[0].equals("count")) {
                long capacityConstraint = Long.parseLong(info[1]);
                long totalTruckNumber = Methods.countTrucksInLargerLots(parkingLots, capacityConstraint);
                // in the count unlike the others, returned value is written to output file here
                outputFile.write(totalTruckNumber + "\n");
            }
        }

        actionsInputFile.close(); // Closing input file for safety
        outputFile.close(); // Closing output file for safety
    }
}

