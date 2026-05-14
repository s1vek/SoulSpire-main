package com.example.soulspire.Crafting;

import com.example.soulspire.Item.Equipment;
import com.example.soulspire.Item.Inventory;
import com.example.soulspire.Item.MaterialType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Defines a crafting recipe: the required materials and the resulting equipment.
 */
public class CraftingRecipe {

    private String resultName;
    private Equipment result;
    private Map<MaterialType, Integer> requiredMaterials;

    /**
     * Creates a new crafting recipe.
     *
     * @param result            the equipment produced
     * @param requiredMaterials map of material type to quantity needed
     */
    public CraftingRecipe(Equipment result, Map<MaterialType, Integer> requiredMaterials) {
        this.result = result;
        this.resultName = result.getName();
        this.requiredMaterials = new EnumMap<>(requiredMaterials);
    }

    /**
     * Checks whether the player has enough materials to craft this recipe.
     *
     * @param inventory the player's inventory
     * @return true if all required materials are available
     */


    public boolean canCraft(Inventory inventory) {
        for (Map.Entry<MaterialType, Integer> e : requiredMaterials.entrySet()) {
            if (inventory.getMaterialCount(e.getKey()) < e.getValue()) {
                return false;
            }
        }
        return true;

    }


    /**
     * Getters and setters.
     */
    public String getResultName() { return resultName; }
    public Equipment getResult() { return result; }
    public Map<MaterialType, Integer> getRequiredMaterials() { return requiredMaterials; }
}