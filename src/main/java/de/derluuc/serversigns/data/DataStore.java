package de.derluuc.serversigns.data;

import java.util.ArrayList;
import java.util.List;

import de.derluuc.serversigns.ServerSigns;
import de.derluuc.serversigns.signs.ServerSign;
import de.derluuc.serversigns.signs.SignUtils;

public class DataStore {
	
	private ServerSigns pl;
	private static DataStore instance;

	public static String NODE_SIGNLIST = "serversigns.signsdata";

	public static String NODE_LAYOUTS = "serversigns.layouts.";
	public static String NODE_LAYOUT(String layout) { return NODE_LAYOUTS + layout; }
	public static String NODE_LAYOUT_DATA(String layout, LayoutData dat) { return NODE_LAYOUT(layout) + "." + dat.node(); }

	public static String NODE_CYCLE_UPDATE = "serversigns.cycleupdate";
	public static String NODE_CYCLE_OFFLINECHECK = "serversigns.cycleofflinecheck";
	
	public DataStore() {
		this.pl = ServerSigns.getInstance();
		ArrayList<ServerSign> signdata = new ArrayList<ServerSign>();
		signdata.add(new ServerSign("examplesign", "examplelayout", "exampleworld", 0, 0, 0));
		this.pl.getConfig().addDefault(NODE_SIGNLIST, SignUtils.serialize(signdata));
		this.pl.getConfig().addDefault(NODE_LAYOUT_DATA("examplelayout", LayoutData.LINE1), "line1");
		this.pl.getConfig().addDefault(NODE_LAYOUT_DATA("examplelayout", LayoutData.LINE2), "line2");
		this.pl.getConfig().addDefault(NODE_LAYOUT_DATA("examplelayout", LayoutData.LINE3), "line3");
		this.pl.getConfig().addDefault(NODE_LAYOUT_DATA("examplelayout", LayoutData.LINE4), "line4");
		this.pl.getConfig().addDefault(NODE_CYCLE_UPDATE, "line4");
		this.pl.getConfig().addDefault(NODE_LAYOUT_DATA("examplelayout", LayoutData.LINE4), "line4");
		this.pl.getConfig().options().copyDefaults(true);
		this.pl.saveConfig();
	}
	
	public void put(String node, Object o) {
		this.pl.getConfig().set(node, o);
	}
	
	public static DataStore getInstance() {
		if(instance == null) {
			instance = new DataStore();
		}
		return instance;
	}

	public List<ServerSign> getSigns() {
		return SignUtils.deserialize(this.pl.getConfig().getStringList(NODE_SIGNLIST));
	}
	
	public void setSigns(List<ServerSign> signs) {
		this.pl.getConfig().set(NODE_SIGNLIST, SignUtils.serialize(signs));
		this.pl.saveConfig();
		this.pl.reloadConfig();
		ServerSigns.getInstance().reloadSigns();
	}
	
	public String[] getLayout(String layout) {
		return new String[] { this.pl.getConfig().getString(NODE_LAYOUT_DATA(layout, LayoutData.LINE1)), this.pl.getConfig().getString(NODE_LAYOUT_DATA(layout, LayoutData.LINE2)), this.pl.getConfig().getString(NODE_LAYOUT_DATA(layout, LayoutData.LINE3)), this.pl.getConfig().getString(NODE_LAYOUT_DATA(layout, LayoutData.LINE4)) };
	}
	
	public boolean existsLayout(String layout) {
		return this.pl.getConfig().contains(NODE_LAYOUT(layout));
	}
	
}