package com.elevator.model;

import com.elevator.enums.Direction;
import com.elevator.enums.RequestType;

public class Request {

    private int originFloor;
    private Direction direction;
    private RequestType type;

    public Request(int originFloor, Direction direction, RequestType type) {
        this.originFloor = originFloor;
        this.direction = direction;
        this.type = type;
    }

    public int getOriginFloor() {
        return originFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public RequestType getType() {
        return type;
    }
}
