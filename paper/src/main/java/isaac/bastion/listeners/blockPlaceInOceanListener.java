package isaac.bastion.listeners;

import isaac.bastion.Bastion;
import isaac.bastion.BastionBlock;
import isaac.bastion.BastionType;
import isaac.bastion.Permissions;
import isaac.bastion.commands.PlayersStates;
import isaac.bastion.commands.PlayersStates.Mode;
import isaac.bastion.manager.BastionBlockManager;
import isaac.bastion.storage.BastionBlockStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import vg.civcraft.mc.citadel.Citadel;
import vg.civcraft.mc.citadel.events.ReinforcementCreationEvent;
import vg.civcraft.mc.citadel.model.Reinforcement;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.permission.PermissionType;

public class blockPlaceInOceanListener implements Listener {

	private BastionBlockStorage storage;
	public blockPlaceInOceanListener(BastionBlockStorage storage) {
		this.storage = storage;
	}
	@EventHandler()
	public void onBlockPlaced(BlockPlaceEvent e) {
		List<Biome> biomes = new ArrayList<Biome>();
		biomes.add(Biome.OCEAN); biomes.add(Biome.DEEP_OCEAN); biomes.add(Biome.RIVER);
		if (!(biomes.contains(e.getBlock().getBiome()))) {
			e.setCancelled(true);
			return;
		}
		Player player = e.getPlayer();
		Set<BastionBlock> Basts = storage.getAllBastions();
		for (BastionBlock b : Basts) {
			if (b.canPlace(player) || player.isOp()) {
				if (b.isMature() && b.inField(player.getLocation())) {
					Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
					if (block.isSolid()) {
						return;
					}
				}
			}
		}
		e.setCancelled(true);
	}
}

