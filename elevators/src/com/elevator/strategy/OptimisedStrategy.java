package com.elevator.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elevator.car.ElevatorCar;
import com.elevator.enums.ElevatorStatus;
import com.elevator.exception.NoCarAvailableException;
import com.elevator.model.Request;

public class OptimisedStrategy implements ElevatorStrategy {

    private Map<ElevatorCar, Integer> destinationMap;

    public OptimisedStrategy() {
        this.destinationMap = new HashMap<>();
    }

    @Override
    public ElevatorCar dispatch(Request request, List<ElevatorCar> cars) {
        ElevatorCar closestCar = null;
        int smallestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < cars.size(); i++) {
            ElevatorCar car = cars.get(i);
            if (car.getStatus() == ElevatorStatus.STATIONARY) {
                int distance = Math.abs(car.getCurrentFloor() - request.getOriginFloor());
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestCar = car;
                }
            }
        }

        if (closestCar != null) {
            destinationMap.put(closestCar, request.getOriginFloor());
            return closestCar;
        }

        for (int i = 0; i < cars.size(); i++) {
            ElevatorCar car = cars.get(i);
            if (car.getStatus() != ElevatorStatus.UNDER_MAINTENANCE) {
                destinationMap.put(car, request.getOriginFloor());
                return car;
            }
        }

        throw new NoCarAvailableException("No elevator car available to handle the request.");
    }

    @Override
    public boolean shouldStopAt(int floor, ElevatorCar car) {
        if (car.getStatus() != ElevatorStatus.MOVING_UP && car.getStatus() != ElevatorStatus.MOVING_DOWN) {
            return false;
        }

        if (!destinationMap.containsKey(car)) {
            return false;
        }

        int destination = destinationMap.get(car);
        int currentFloor = car.getCurrentFloor();

        if (currentFloor < destination) {
            return floor > currentFloor && floor < destination;
        } else {
            return floor < currentFloor && floor > destination;
        }
    }
}
