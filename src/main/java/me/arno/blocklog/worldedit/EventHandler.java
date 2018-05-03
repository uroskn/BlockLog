package me.arno.blocklog.worldedit;

import com.sk89q.worldedit.EditSession.Stage;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.eventbus.Subscribe;

import me.arno.blocklog.BlockLog;

public class EventHandler {
	
	private BlockLog plugin;
	
	public EventHandler(BlockLog lb) {
		this.plugin = lb;
	}
	
    @Subscribe
    public void wrapForLogging(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (actor != null && actor.isPlayer() && event.getStage() == Stage.BEFORE_CHANGE) {
            event.setExtent(new WorldeditLogger(actor, event.getExtent(), this.plugin));
        }
    }
}

