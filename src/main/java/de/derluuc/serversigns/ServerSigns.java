package de.derluuc.serversigns;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.derluuc.serversigns.data.DataStore;
import de.derluuc.serversigns.listeners.PlayerListener;
import de.derluuc.serversigns.listeners.SignListener;
import de.derluuc.serversigns.signs.ServerSign;

public class ServerSigns extends JavaPlugin {

	private static ServerSigns instance;
	private List<ServerSign> signs;
	
	private int signupdateid;
	
	@Override
	public void onEnable() {
		instance = this;
		DataStore.getInstance();
		signs = DataStore.getInstance().getSigns();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		scheduleSignUpdating();
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(signupdateid);
	}
	
	public static ServerSigns getInstance() {
		return instance;
	}
	
	public void reloadSigns() {
		signs.clear();
		signs = DataStore.getInstance().getSigns();
	}
	
	public List<ServerSign> getSigns() {
		return signs;
	}
	
	public void scheduleSignUpdating() {
		signupdateid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				for(ServerSign rs : signs) {
					rs.update();
				}
			}
			
		}, 0L, 20L);
	}
	
}