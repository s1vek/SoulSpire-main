package com.example.soulspire.World;

import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Core.Saveable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The tower containing all floors. Manages progression through floors
 * and difficulty scaling.
 */
public class Tower implements Saveable {

    private List<Floor> floors;
    private int currentFloorIndex;

    public Tower() {
        this.floors = new ArrayList<>();
        this.currentFloorIndex = 0;
    }

    /**
     * Generates all floors for the tower.
     */
    public void generateFloors() {
        floors.clear();
        for (int i = 0; i < GameConfig.TOTAL_FLOORS; i++) {
            boolean safe = (i > 0 && i % GameConfig.SAFE_ZONE_INTERVAL == 0);
            Floor floor = new Floor(i, GameConfig.FLOOR_WIDTH_TILES, GameConfig.FLOOR_HEIGHT_TILES, safe);
            floors.add(floor);
        }
    }

    /**
     * Advances to the next floor.
     * @return true if there is a next floor, false if this was the last one
     */
    public boolean advanceFloor() {
        if (currentFloorIndex < floors.size() - 1) {
            currentFloorIndex++;
            return true;
        }
        return false;
    }

    public Floor getCurrentFloor() { return floors.get(currentFloorIndex); }
    public int getCurrentFloorNumber() { return currentFloorIndex; }
    public boolean isFinalFloor() { return currentFloorIndex == floors.size() - 1; }

    /**
     * Returns the difficulty multiplier for the current floor.
     */
    public double getDifficultyMultiplier() {
        return 1.0 + currentFloorIndex * GameConfig.DIFFICULTY_SCALE;
    }

    @Override
    public Map<String, Object> toSaveData() {
        return Map.of("currentFloor", currentFloorIndex);
    }

    @Override
    public void loadSaveData(Map<String, Object> data) {
        this.currentFloorIndex = (int) data.getOrDefault("currentFloor", 0);
    }
}