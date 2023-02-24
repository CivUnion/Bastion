package isaac.bastion.listeners;

import isaac.bastion.BastionBlock;
import isaac.bastion.storage.BastionBlockStorage;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockPlaceInOceanListener implements Listener {
	final List<Biome> biomes = Arrays.asList(Biome.OCEAN, Biome.DEEP_OCEAN, Biome.RIVER);
	private BastionBlockStorage storage;
	public blockPlaceInOceanListener(BastionBlockStorage storage) {
		this.storage = storage;
	}
	@EventHandler()
	public void onBlockPlaced(BlockPlaceEvent e) {
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

