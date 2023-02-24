package isaac.bastion.listeners;

import isaac.bastion.BastionBlock;
import isaac.bastion.storage.BastionBlockStorage;

import java.util.ArrayList;
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

