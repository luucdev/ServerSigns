package de.derluuc.serversigns.signs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import de.derluuc.serversigns.ServerSigns;
import de.derluuc.serversigns.communication.DataHandler;
import de.derluuc.serversigns.data.DataStore;
import de.derluuc.serversigns.data.ServerData;

public class ServerSign {

	private String server;
	private String layout;
	private Location loc;
	
	private ServerData data;
	
	public ServerSign(String server, String layout, Location loc) {
		this.server = server;
		this.layout = layout;
		this.loc = loc;
		this.data = null;
	}

	public ServerSign(String server, String layout, String world, int x, int y, int z) {
		this.server = server;
		this.layout = layout;
		this.loc = new Location(Bukkit.getWorld(world), x, y, z);
		this.data = null;
	}
	
	public String getServer() {
		return server;
	}
	
	public String getLayout() {
		return layout;
	}
	
	public Location getSignLocation() {
		return loc;
	}
	
	public void join(Player p) {
		if(data != null) {
			if(data.isOffline()) {
				p.sendMessage(ChatColor.GREEN + "[ServerSigns] " + ChatColor.GOLD + ServerSigns.getLangManager().getLocaleMessage("lang.sign.joincantreach"));
				return;
			}
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try {
				out.writeUTF("Connect");
				out.writeUTF(data.getServer());
			} catch (IOException e) {
				p.sendMessage(ChatColor.GREEN + "[ServerSigns] " + ChatColor.GOLD + ServerSigns.getLangManager().getLocaleMessage("lang.sign.joinerror"));
			}
			p.sendPluginMessage(ServerSigns.getInstance(), "BungeeCord", b.toByteArray());
		} else {
			p.sendMessage(ChatColor.GREEN + "[ServerSigns] " + ChatColor.GOLD + ServerSigns.getLangManager().getLocaleMessage("lang.sign.joincantreach"));
		}
	}
	
	public void update() {
		data = DataHandler.getInstance().getData(server);
		if(!(loc.getBlock().getState() instanceof Sign))
			return;
		Sign s = (Sign)loc.getBlock().getState();
		String[] l = DataStore.getInstance().getLayout(layout);
		//System.out.println(layout);
		for(int i = 0; i < l.length; i++) {
			//System.out.println(i);
			if(i > 3)
				break;
			String line = l[i];
			//System.out.println(line);
			s.setLine(i, SignUtils.replaceVars(line, data));
		}
		s.update();
	}
	
}
