package com.example.soulspire.Item;

import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Core.Saveable;

import java.util.*;

/**
 * Manages the player's carried items and equipped gear.
 * Has a maximum capacity defined by {@link GameConfig#INVENTORY_MAX_SIZE}.
 *
 * <p>Equipment can be placed into one of the {@link EquipmentSlot} slots.
 * Materials of the same {@link MaterialType} stack automatically.</p>
 *
 * <p>Implements {@link Saveable} so the full inventory state can be
 * persisted as part of the game save.</p>
 */
public class Inventory implements Saveable {

    /** All items currently in the inventory (including materials and soul echoes). */
    private final List<Item> items;

    /** Currently equipped items, one per slot. */
    private final Map<EquipmentSlot, Equipment> equipped;

    /** Maximum number of items the inventory can hold. */
    private final int maxSize;

    /**
     * Creates an empty inventory with the default maximum size.
     */
    public Inventory() {
        this.items = new ArrayList<>();
        this.equipped = new EnumMap<>(EquipmentSlot.class);
        this.maxSize = GameConfig.INVENTORY_MAX_SIZE;
    }

    /**
     * Attempts to add an item to the inventory.
     * Materials are stacked if a matching type already exists.
     *
     * @param item the item to add
     * @return true if the item was added, false if the inventory is full
     */
    public boolean addItem(Item item) {
        for (Item existing : items) {
            if (existing.canStackWith(item)) {
                if (existing instanceof Material mat && item instanceof Material newMat) {
                    mat.addQuantity(newMat.getQuantity());
                    return true;
                }
            }
        }
        // Otherwise add as new item
        if (items.size() >= maxSize) {
            return false;
        }
        items.add(item);
        return true;
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item the item to remove
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Equips a piece of equipment into its designated slot.
     * If the slot is already occupied, the old equipment is unequipped first.
     *
     * @param equipment the equipment to put on
     */
    public void equip(Equipment equipment) {
        EquipmentSlot slot = equipment.getSlot();
        if (equipped.containsKey(slot)) {
            items.add(equipped.get(slot));
        }
        equipped.put(slot, equipment);
        items.remove(equipment);
    }

    /**
     * Unequips the item in the given slot and returns it to the inventory.
     *
     * @param slot the slot to unequip
     */
    public void unequip(EquipmentSlot slot) {
        Equipment removed = equipped.remove(slot);
        if (removed != null) {
            items.add(removed);
        }
    }

    /**
     * Returns the equipment currently in the given slot, or null if empty.
     *
     * @param slot the equipment slot to check
     * @return the equipped item, or null
     */
    public Equipment getEquipped(EquipmentSlot slot) {
        return equipped.get(slot);
    }

    /**
     * Counts how many units of a given material type the player has.
     *
     * @param type the material type to count
     * @return total quantity across all stacks of this type
     */
    public int getMaterialCount(MaterialType type) {
        return items.stream()
                .filter(item -> item instanceof Material mat && mat.getMaterialType() == type)
                .mapToInt(item -> ((Material) item).getQuantity())
                .sum();
    }

    /**
     * Removes a specified amount of a material type from the inventory.
     * Used by the crafting system when a recipe is executed.
     *
     * @param type   the material type to consume
     * @param amount the quantity to remove
     * @return true if enough material was available and removed
     */
    public boolean removeMaterial(MaterialType type, int amount) {
        if (getMaterialCount(type) < amount) return false;

        int remaining = amount;
        var iterator = items.iterator();
        while (iterator.hasNext() && remaining > 0) {
            Item item = iterator.next();
            if (item instanceof Material mat && mat.getMaterialType() == type) {
                if (mat.getQuantity() <= remaining) {
                    remaining -= mat.getQuantity();
                    iterator.remove();
                } else {
                    mat.removeQuantity(remaining);
                    remaining = 0;
                }
            }
        }
        return true;
    }

    /**
     * Returns all active SoulEcho modifiers in the inventory.
     *
     * @return list of SoulEcho items
     */
    public List<SoulEcho> getActiveSoulEchoes() {
        return items.stream()
                .filter(item -> item instanceof SoulEcho)
                .map(item -> (SoulEcho) item)
                .toList();
    }

    public double getTotalMoveSpeedBonus() {
        double total = 0;
        for (Equipment e : equipped.values()) {
            total += e.getMoveSpeedBonus();
        }
        return total;
    }

    /**
     * @return true if the inventory has reached maximum capacity
     */
    public boolean isFull() {
        return items.size() >= maxSize;
    }

    /**
     * @return unmodifiable view of all items in the inventory
     */
    public List<Item> getItems() {
        return List.copyOf(items);
    }

    /**
     * @return total attack bonus from all equipped items
     */
    public int getTotalAttackBonus() {
        return equipped.values().stream().mapToInt(Equipment::getAttackBonus).sum();
    }

    /**
     * @return total defense bonus from all equipped items
     */
    public int getTotalDefenseBonus() {
        return equipped.values().stream().mapToInt(Equipment::getDefenseBonus).sum();
    }

    @Override
    public Map<String, Object> toSaveData() {
        List<Map<String, Object>> mats = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Material mat) {
                Map<String, Object> m = new HashMap<>();
                m.put("type", mat.getMaterialType().name());
                m.put("quantity", mat.getQuantity());
                mats.add(m);
            }
        }

        List<Map<String, Object>> equippedList = new ArrayList<>();
        for (Map.Entry<EquipmentSlot, Equipment> entry : equipped.entrySet()) {
            Equipment e = entry.getValue();
            Map<String, Object> eq = new HashMap<>();
            eq.put("slot", entry.getKey().name());
            eq.put("name", e.getName());
            eq.put("description", e.getDescription());
            eq.put("atk", e.getAttackBonus());
            eq.put("def", e.getDefenseBonus());
            eq.put("speed", e.getMoveSpeedBonus());
            equippedList.add(eq);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("materials", mats);
        result.put("equipped", equippedList);
        return result;
    }

    @Override
    public void loadSaveData(Map<String, Object> data) {
        items.clear();
        equipped.clear();

        Object matsObj = data.get("materials");
        if (matsObj instanceof List<?> matsList) {
            for (Object o : matsList) {
                if (o instanceof Map<?,?> m) {
                    String typeName = (String) m.get("type");
                    int qty = ((Number) m.get("quantity")).intValue();
                    addItem(new Material(MaterialType.valueOf(typeName), qty));
                }
            }
        }

        Object eqObj = data.get("equipped");
        if (eqObj instanceof List<?> eqList) {
            for (Object o : eqList) {
                if (o instanceof Map<?, ?> m) {
                    EquipmentSlot slot = EquipmentSlot.valueOf((String) m.get("slot"));
                    String name = (String) m.get("name");
                    String desc = (String) m.get("description");
                    int atk = ((Number) m.get("atk")).intValue();
                    int def = ((Number) m.get("def")).intValue();
                    double speed = ((Number) m.get("speed")).doubleValue();
                    equipped.put(slot, new Equipment(name, desc, atk, def, speed, slot));
                }
            }
        }
    }
}