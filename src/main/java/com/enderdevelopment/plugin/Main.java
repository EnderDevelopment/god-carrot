package com.enderdevelopment.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {
    private static final UUID HEALTH_MODIFIER_ID = UUID.fromString("1f8a9ce1-7e7e-4b59-9f90-2cc1c50e2f6c");
    private NamespacedKey godCarrotKey;
    private NamespacedKey healthModifierKey;

    @Override
    public void onEnable() {
        godCarrotKey = new NamespacedKey(this, "god_carrot");
        healthModifierKey = new NamespacedKey(this, "god_carrot_health");
        registerRecipe();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, this::updatePlayers, 0L, 20L);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeHealthBonus(player);
        }
    }

    private void registerRecipe() {
        ItemStack result = createGodCarrot();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "god_carrot_recipe"), result);
        recipe.shape("WGW", "ACA", "WBW");
        recipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        recipe.setIngredient('G', Material.HEAVY_CORE);
        recipe.setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE);
        recipe.setIngredient('C', Material.CARROT);
        recipe.setIngredient('B', Material.BREEZE_ROD);
        Bukkit.addRecipe(recipe);
    }

    private ItemStack createGodCarrot() {
        ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("God Carrot").color(NamedTextColor.GOLD));
        meta.getPersistentDataContainer().set(godCarrotKey, PersistentDataType.BYTE, (byte) 1);
        addEnchantment(meta, "breach", 1);
        addEnchantment(meta, "wind_burst", 3);
        addEnchantment(meta, "sharpness", 5);
        addEnchantment(meta, "lunge", 3);
        addEnchantment(meta, "efficiency", 5);
        item.setItemMeta(meta);
        return item;
    }

    private void addEnchantment(ItemMeta meta, String key, int level) {
        Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
        if (enchantment != null) {
            meta.addEnchant(enchantment, level, true);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() instanceof ShapedRecipe shaped
                && shaped.getKey().equals(new NamespacedKey(this, "god_carrot_recipe"))) {
            event.getInventory().setResult(createGodCarrot());
        }
    }

    private void updatePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasGodCarrot(player)) {
                applyEffects(player);
                applyHealthBonus(player);
            } else {
                removeHealthBonus(player);
            }
        }
    }

    private boolean hasGodCarrot(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isGodCarrot(item)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGodCarrot(ItemStack item) {
        if (item == null || item.getType() != Material.GOLDEN_CARROT) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(godCarrotKey, PersistentDataType.BYTE);
    }

    private void applyEffects(Player player) {
        int duration = 40;
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, 0, true, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 0, true, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, duration, 0, true, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 0, true, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, duration, 0, true, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, duration, 0, true, false, true));
    }

    private void applyHealthBonus(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute == null || attribute.getModifier(HEALTH_MODIFIER_ID) != null) {
            return;
        }
        AttributeModifier modifier = new AttributeModifier(HEALTH_MODIFIER_ID, healthModifierKey.getKey(), 10.0,
                AttributeModifier.Operation.ADD_NUMBER);
        attribute.addModifier(modifier);
        if (player.getHealth() > attribute.getValue()) {
            player.setHealth(attribute.getValue());
        }
    }

    private void removeHealthBonus(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute == null) {
            return;
        }
        AttributeModifier modifier = attribute.getModifier(HEALTH_MODIFIER_ID);
        if (modifier != null) {
            attribute.removeModifier(modifier);
            if (player.getHealth() > attribute.getValue()) {
                player.setHealth(attribute.getValue());
            }
        }
    }
}
