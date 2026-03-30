package com.elevator;

import com.elevator.car.ElevatorCar;
import com.elevator.enums.Direction;
import com.elevator.exception.InvalidFloorException;
import com.elevator.strategy.FCFSStrategy;
import com.elevator.strategy.OptimisedStrategy;

public class Main {

    public static void main(String[] args) {
        ElevatorSystem system = ElevatorSystem.initialise(10, 3, new FCFSStrategy());

        System.out.println("--- Step 1: Outside request from floor 3 going UP ---");
        system.handleOutsideRequest(3, Direction.UP);

        System.out.println("\n--- Step 2: Inside request from car at floor 3 to floor 7 ---");
        ElevatorCar carAtFloor3 = system.getCarAtIndex(0);
        system.handleInsideRequest(carAtFloor3, 7);

        System.out.println("\n--- Step 3: Outside request from floor 1 going UP ---");
        system.handleOutsideRequest(1, Direction.UP);

        System.out.println("\n--- Step 4: Trigger alarm from car 2 ---");
        system.getCarAtIndex(1).getInsidePanel().pressAlarm();

        System.out.println("\n--- Step 5: Invalid floor request ---");
        try {
            system.handleInsideRequest(system.getCarAtIndex(0), 99);
        } catch (InvalidFloorException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n--- Step 6: Switch to OptimisedStrategy ---");
        OptimisedStrategy optimisedStrategy = new OptimisedStrategy();
        system.setStrategy(optimisedStrategy);
        System.out.println("Strategy switched to OptimisedStrategy.");
        system.handleOutsideRequest(5, Direction.UP);

        System.out.println("\n--- Step 7: Final status of all cars ---");
        for (int i = 0; i < 3; i++) {
            ElevatorCar car = system.getCarAtIndex(i);
            System.out.println("Car " + (i + 1) + ": Floor " + car.getCurrentFloor() + ", Status: " + car.getStatus());
        }
    }
}
