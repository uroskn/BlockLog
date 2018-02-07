package me.arno.blocklog.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bags.BlockBag;
import me.arno.blocklog.BlockLog;

public class BlockLogEditSessionFactory extends EditSessionFactory {

	private BlockLog plugin;

	public BlockLogEditSessionFactory(BlockLog lb) {
		this.plugin = lb;
	}

	@Override
	public EditSession getEditSession(LocalWorld world, int maxBlocks, LocalPlayer player) {
		return new BlockLogEditSession(world, maxBlocks, player, plugin);
	}

	public static void initialize(BlockLog logBlock) {
		try {
			// Check to see if the world edit version is compatible
			Class.forName("com.sk89q.worldedit.EditSessionFactory").getDeclaredMethod("getEditSession", LocalWorld.class, int.class, BlockBag.class, LocalPlayer.class);
			WorldEdit.getInstance().setEditSessionFactory(new BlockLogEditSessionFactory(logBlock));
			logBlock.log.info("Hooked into WorldEdit!");
		} catch (Throwable ignored) {
			logBlock.log.info("Error hooking into WorldEdit:");
			ignored.printStackTrace();
		}
	}
	
}
