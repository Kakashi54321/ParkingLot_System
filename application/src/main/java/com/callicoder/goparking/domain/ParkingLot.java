package com.callicoder.goparking.domain;

import com.callicoder.goparking.exceptions.ParkingLotFullException;
import com.callicoder.goparking.exceptions.SlotNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class ParkingLot {

    private final int numSlots;
    private final int numFloors;
    private SortedSet<ParkingSlot> availableSlots = new TreeSet<>();
    private Set<ParkingSlot> occupiedSlots = new HashSet<>();

    public ParkingLot(int numSlots) {
        if (numSlots <= 0) {
            throw new IllegalArgumentException(
                "Number of slots in the Parking Lot must be greater than zero."
            );
        }

        // Assuming Single floor since only numSlots are specified in the input.
        this.numSlots = numSlots;
        this.numFloors = 1;

        for (int i = 0; i < numSlots; i++) {
            ParkingSlot parkingSlot = new ParkingSlot(i + 1, 1);
            this.availableSlots.add(parkingSlot);
        }
    }

    public synchronized Ticket reserveSlot(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car must not be null");
        }

        if (this.isFull()) {
            throw new ParkingLotFullException();
        }

        ParkingSlot nearestSlot = this.availableSlots.first();

        nearestSlot.reserve(car);
        this.availableSlots.remove(nearestSlot);
        this.occupiedSlots.add(nearestSlot);

        return new Ticket(
            nearestSlot.getSlotNumber(),
            car.getRegistrationNumber(),
            car.getColor()
        );
    }

    public ParkingSlot leaveSlot(int slotNumber) {
        //TODO: implement leave
    	ParkingSlot resultantSlot = null;
    	for(ParkingSlot slot : occupiedSlots) {
    		if(slot.getSlotNumber() == slotNumber) {
    			resultantSlot = slot;
    			break;
    		}
    	}
    	
    	if(resultantSlot == null) {
    		throw new SlotNotFoundException(slotNumber);
    	}else {
    		occupiedSlots.remove(resultantSlot);
    		availableSlots.add(resultantSlot);
    		resultantSlot.clear();
    	}
        return resultantSlot;
    }

    public boolean isFull() {
        return this.availableSlots.isEmpty();
    }

    public List<String> getRegistrationNumbersByColor(String color) {
        //TODO: implement getRegistrationNumbersByColor
    	List<String> regNumbers = occupiedSlots.stream()
    			.filter(slot -> slot.getCar() != null && slot.getCar().getColor().equalsIgnoreCase(color))
    			.map(slot -> slot.getCar().getRegistrationNumber())
    			.collect(Collectors.toList());
        return regNumbers;
    }

    public List<Integer> getSlotNumbersByColor(String color) {
        //TODO: implement getSlotNumbersByColor
    	List<Integer> slotNumbers = occupiedSlots.stream()
    			.filter(slot -> slot.getCar() != null && slot.getCar().getColor().equalsIgnoreCase(color))
    			.map(slot -> slot.getSlotNumber())
    			.collect(Collectors.toList());
        return slotNumbers;
    }

    public Optional<Integer> getSlotNumberByRegistrationNumber(
        String registrationNumber
    ) {
        //TODO: implement getSlotNumberByRegistrationNumber
    	Optional<Integer> slotNumbers = occupiedSlots.stream()
    			.filter(slot -> slot.getCar() != null && slot.getCar().getRegistrationNumber().equalsIgnoreCase(registrationNumber))
    			.map(slot -> slot.getSlotNumber())
    			.findFirst();
        return slotNumbers;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public int getNumFloors() {
        return numFloors;
    }

    public SortedSet<ParkingSlot> getAvailableSlots() {
        return availableSlots;
    }

    public Set<ParkingSlot> getOccupiedSlots() {
        return occupiedSlots;
    }
}
