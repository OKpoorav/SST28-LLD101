package com.elevator.strategy;

import java.util.List;

import com.elevator.car.ElevatorCar;
import com.elevator.model.Request;

public interface ElevatorStrategy {

    ElevatorCar dispatch(Request request, List<ElevatorCar> cars);

    boolean shouldStopAt(int floor, ElevatorCar car);
}
