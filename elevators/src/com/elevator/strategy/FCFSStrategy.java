package com.elevator.strategy;

import java.util.List;

import com.elevator.car.ElevatorCar;
import com.elevator.enums.ElevatorStatus;
import com.elevator.exception.NoCarAvailableException;
import com.elevator.model.Request;

public class FCFSStrategy implements ElevatorStrategy {

    @Override
    public ElevatorCar dispatch(Request request, List<ElevatorCar> cars) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getStatus() == ElevatorStatus.STATIONARY) {
                return cars.get(i);
            }
        }
        throw new NoCarAvailableException("No elevator car available to handle the request.");
    }

    @Override
    public boolean shouldStopAt(int floor, ElevatorCar car) {
        return false;
    }
}
