package com.elevator;

import java.util.ArrayList;
import java.util.List;

import com.elevator.car.ElevatorCar;
import com.elevator.car.StandardElevatorCar;
import com.elevator.emergency.BasicEmergencyHandler;
import com.elevator.emergency.EmergencyEvent;
import com.elevator.enums.Direction;
import com.elevator.exception.InvalidFloorException;
import com.elevator.model.Request;
import com.elevator.panel.OutsidePanel;
import com.elevator.strategy.ElevatorStrategy;

public class ElevatorSystem {

    private static final double DEFAULT_WEIGHT_LIMIT = 500.0;

    private static ElevatorSystem instance;

    private List<ElevatorCar> cars;
    private List<OutsidePanel> outsidePanels;
    private ElevatorStrategy strategy;
    private int totalFloors;

    private ElevatorSystem(int totalFloors, int numberOfCars, ElevatorStrategy strategy) {
        this.totalFloors = totalFloors;
        this.strategy = strategy;
        this.cars = new ArrayList<>();
        this.outsidePanels = new ArrayList<>();

        BasicEmergencyHandler emergencyHandler = new BasicEmergencyHandler();

        for (int i = 0; i < numberOfCars; i++) {
            StandardElevatorCar car = new StandardElevatorCar(totalFloors, DEFAULT_WEIGHT_LIMIT);
            car.registerHandler(emergencyHandler);
            cars.add(car);
        }

        for (int floor = 1; floor <= totalFloors; floor++) {
            outsidePanels.add(new OutsidePanel(floor));
        }
    }

    public static ElevatorSystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ElevatorSystem has not been initialised yet.");
        }
        return instance;
    }

    public static ElevatorSystem initialise(int totalFloors, int numberOfCars, ElevatorStrategy strategy) {
        if (instance != null) {
            return instance;
        }
        instance = new ElevatorSystem(totalFloors, numberOfCars, strategy);
        return instance;
    }

    public void handleOutsideRequest(int floor, Direction direction) {
        OutsidePanel panel = getOutsidePanelForFloor(floor);

        Request request;
        if (direction == Direction.UP) {
            request = panel.pressUp();
        } else {
            request = panel.pressDown();
        }

        ElevatorCar assignedCar = strategy.dispatch(request, cars);
        System.out.println("Car assigned to floor " + floor + " request.");
        assignedCar.moveToFloor(floor);
    }

    public void handleInsideRequest(ElevatorCar car, int targetFloor) {
        if (targetFloor < 1 || targetFloor > totalFloors) {
            throw new InvalidFloorException("Floor " + targetFloor + " does not exist in this building.");
        }

        car.getInsidePanel().pressFloor(targetFloor);
        System.out.println("Inside request: car moving to floor " + targetFloor + ".");
        car.moveToFloor(targetFloor);
    }

    public void triggerEmergency(EmergencyEvent event) {
        System.out.println("SYSTEM: Emergency triggered — " + event.getDescription());
        for (int i = 0; i < cars.size(); i++) {
            cars.get(i).onEmergency(event);
        }
    }

    public ElevatorCar getCarAtIndex(int index) {
        return cars.get(index);
    }

    public OutsidePanel getOutsidePanelForFloor(int floor) {
        for (int i = 0; i < outsidePanels.size(); i++) {
            if (outsidePanels.get(i).getFloor() == floor) {
                return outsidePanels.get(i);
            }
        }
        return null;
    }

    public void setStrategy(ElevatorStrategy strategy) {
        this.strategy = strategy;
    }
}
