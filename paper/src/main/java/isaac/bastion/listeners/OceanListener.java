package isaac.bastion.listeners;

import isaac.bastion.Bastion;
import isaac.bastion.BastionBlock;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import vg.civcraft.mc.civmodcore.utilities.cooldowns.TickCoolDownHandler;
import vg.civcraft.mc.civmodcore.world.WorldUtils;

public class OceanListener implements Listener {
	private final List<Biome> biomes = List.of(Biome.DEEP_OCEAN);

	private final TickCoolDownHandler<UUID> messageCooldown;

	public OceanListener(Bastion plugin){
		messageCooldown = new TickCoolDownHandler<>(plugin, 60L * 20L);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlaced(BlockPlaceEvent e) {
		if ((e.getBlock().getBiome() != Biome.DEEP_OCEAN && e.getPlayer().getLocation().getBlock().getBiome() != Biome.DEEP_OCEAN) || e.getPlayer().isOp()) {
			//Bastion.getPlugin().getLogger().info("Player op or not in ocean biome, skipping.");
			return;
		}
		Player player = e.getPlayer();
		Location blockLoc = e.getBlock().getLocation();
		Set<BastionBlock> basts = Bastion.getBastionManager().getBlockingBastions(blockLoc);

		if(basts.isEmpty()){
			//Bastion.getPlugin().getLogger().info("No bastions present");
			if (!checkCanPlace(player)) {
				//Bastion.getPlugin().getLogger().info("Cannot place, cancelling place.");
				handleBastionCancel(e);
			}
			return;
		}

		for(BastionBlock b : basts){
			if((b.inField(player.getLocation()) || b.inField(blockLoc)) && !b.isMature() && b.canPlace(player)){
				if(checkCanPlace(player)){
					Bastion.getPlugin().getLogger().info("Can place in field, continuing");
					continue;
				}
				Bastion.getPlugin().getLogger().info("Cannot place, cancelling place.");
				handleBastionCancel(e);
				return;
			}
		}
	}

	private void handleBastionCancel(BlockPlaceEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if(!messageCooldown.onCoolDown(uuid)){
			player.sendMessage(ChatColor.DARK_AQUA + "You need to be in a friendly bastion field to build freely in deep ocean biomes!");
			messageCooldown.putOnCoolDown(uuid);
		}
		event.setCancelled(true);
	}

	private boolean checkCanPlace(Player player){
		Block belowBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if(checkDistance(player, belowBlock)){
			return true;
		}

		for(BlockFace face : WorldUtils.PLANAR_SIDES){
			if(checkDistance(player, belowBlock.getRelative(face))){
				return true;
			}
		}

		return false;
	}

	private boolean checkDistance(Player player, Block block){
		if(!block.isSolid()){
			return false;
		}

		Vector locA = block.getLocation().toCenterLocation().toVector(); // player stood on block
		Vector locB = player.getLocation().toVector();
		double dist = locA.distance(locB);
		Bastion.getPlugin().getLogger().info(String.format("distance: %s", dist));
		return dist < 0.56D;
	}
}

