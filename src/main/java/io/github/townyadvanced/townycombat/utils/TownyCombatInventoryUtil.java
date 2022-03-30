package io.github.townyadvanced.townycombat.utils;

import com.palmergames.bukkit.towny.object.Translatable;
import io.github.townyadvanced.townycombat.settings.TownyCombatSettings;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 
 * @author Goosius
 *
 */
public class TownyCombatInventoryUtil {

	public static void degradeInventory(PlayerDeathEvent playerDeathEvent) {
		Damageable damageable;
		double maxDurability;
		int currentDurability, damageToInflict, newDurability, durabilityWarning;
		boolean closeToBreaking = false;
        for (ItemStack itemStack : playerDeathEvent.getEntity().getInventory().getContents()) {
            if (itemStack != null && itemStack.getType().getMaxDurability() != 0 && !itemStack.getItemMeta().isUnbreakable()) {
                damageable = ((Damageable) itemStack.getItemMeta());
                maxDurability = itemStack.getType().getMaxDurability();
                currentDurability = damageable.getDamage();
                damageToInflict = (int)(maxDurability / 100 * TownyCombatSettings.getKeepInventoryOnDeathToolsDegradePercentage());
                newDurability = currentDurability + damageToInflict;
                if (newDurability >= maxDurability) {
                    damageable.setDamage(Math.max((int)maxDurability-25, currentDurability));
                    closeToBreaking = true;
                }
                else {
                    damageable.setDamage(newDurability);
                    durabilityWarning = damageToInflict * 2 + currentDurability;
                    if (durabilityWarning >= maxDurability)
                        closeToBreaking = true;
                }
                itemStack.setItemMeta((ItemMeta)damageable);
            }
        }
        if (closeToBreaking) //One or more items are close to breaking, send warning.
            Messaging.sendMsg(playerDeathEvent.getEntity(), Translatable.of("msg_inventory_degrade_warning"));
	}

	public static void keepInventory(PlayerDeathEvent playerDeathEvent) {
        playerDeathEvent.setKeepInventory(true);
        playerDeathEvent.getDrops().clear();
	}

}
