package com.example.soulspire.Crafting;
import com.example.soulspire.Item.Equipment;
import com.example.soulspire.Item.EquipmentSlot;
import com.example.soulspire.Item.Inventory;
import com.example.soulspire.Item.MaterialType;
import com.example.soulspire.Util.GameLogger;

import java.util.*;


/**
 * Manages all crafting recipes and executes crafting operations.
 * Used by the Blacksmith NPC in safe zones.
 */
public class CraftingSystem {

    private static final GameLogger logger = GameLogger.getLogger(CraftingSystem.class);

    private List<CraftingRecipe> recipes;

    public CraftingSystem() {
        this.recipes = new ArrayList<>();
        loadRecipes();
    }

    /**
     * Initializes the available crafting recipes.
     */
    private void loadRecipes() {
        Map<MaterialType, Integer> cost;

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 30);
        recipes.add(new CraftingRecipe(
                new Equipment("Iron Sigil", "+5 atk", 5, 0, EquipmentSlot.WEAPON), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 35); cost.put(MaterialType.ETHEREAL_DUST, 5);
        recipes.add(new CraftingRecipe(
                new Equipment("Sharp Sigil", "+8 atk", 8, 0, EquipmentSlot.WEAPON), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 40); cost.put(MaterialType.ETHEREAL_DUST, 10);
        recipes.add(new CraftingRecipe(
                new Equipment("Soulforged Sigil", "+15 atk", 15, 0, EquipmentSlot.WEAPON), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 60); cost.put(MaterialType.ETHEREAL_DUST, 30);
        recipes.add(new CraftingRecipe(
                new Equipment("Eternal Sigil", "+25 atk", 25, 0, EquipmentSlot.WEAPON), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 30);
        recipes.add(new CraftingRecipe(
                new Equipment("Iron Ward", "+3 def", 0, 3, EquipmentSlot.ARMOR), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 40); cost.put(MaterialType.ETHEREAL_DUST, 5);
        recipes.add(new CraftingRecipe(
                new Equipment("Reinforced Ward", "+5 def", 0, 5, EquipmentSlot.ARMOR), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 70); cost.put(MaterialType.ETHEREAL_DUST,20);
        recipes.add(new CraftingRecipe(
                new Equipment("Soulforged Ward", "+10 def", 0, 10, EquipmentSlot.ARMOR), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 90); cost.put(MaterialType.ETHEREAL_DUST, 30);
        recipes.add(new CraftingRecipe(
                new Equipment("Eternal Ward", "+18 def", 0, 18, EquipmentSlot.ARMOR), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 80); cost.put(MaterialType.ETHEREAL_DUST, 30);
        recipes.add(new CraftingRecipe(
                new Equipment("Berserker Sigil", "+20 atk +30 spd", 20, 0, 30, EquipmentSlot.WEAPON), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 10); cost.put(MaterialType.ETHEREAL_DUST, 30);
        recipes.add(new CraftingRecipe(
                new Equipment("Swift Boots", "+2 def +50 spd", 0, 2, 50, EquipmentSlot.ARMOR), cost));

        cost = new HashMap<>(); cost.put(MaterialType.IRON_ORE, 50); cost.put(MaterialType.ETHEREAL_DUST, 20);
        recipes.add(new CraftingRecipe(
                new Equipment("Phantom Cloak", "+4 def +30 spd", 0, 4, 30, EquipmentSlot.ARMOR), cost));

    }

    /**
     * Attempts to craft a recipe, consuming materials from the inventory.
     *
     * @param recipe    the recipe to craft
     * @param inventory the player's inventory
     * @return true if crafting was successful
     */


    public boolean craft(CraftingRecipe recipe, Inventory inventory) {
        if (!recipe.canCraft(inventory)) {
            return false;
        }

        for (Map.Entry<MaterialType, Integer> e : recipe.getRequiredMaterials().entrySet()) {
            inventory.removeMaterial(e.getKey(), e.getValue());
        }
        Equipment crafted = (Equipment) recipe.getResult().copy();
        inventory.equip(crafted);
        logger.info("Crafted: " + crafted.getName());
        return true;

    }


    /**
     * Returns only the recipes the player currently has materials for.
     */

    /*
    public List<CraftingRecipe> getAvailableRecipes(Inventory inventory) {

    }

     */
    public List<CraftingRecipe> getAllRecipes() { return recipes; }
}