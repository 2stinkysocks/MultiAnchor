package com.twostinkysocks.multianchor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {
    private HashMap<Location, Integer> anchors; // location, charges

    public void onEnable() {
        anchors = new HashMap<Location, Integer>();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR && e.getItem() != null && e.getItem().getType() == Material.GLOWSTONE) {
            RespawnAnchor r = (RespawnAnchor) e.getClickedBlock().getBlockData();
            anchors.put(e.getClickedBlock().getLocation(), r.getCharges());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(anchors.containsKey(e.getBlock().getLocation())) {
            anchors.remove(e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent e) {
        if(anchors.containsKey(e.getBlock().getLocation())) {
            int charges = anchors.get(e.getBlock().getLocation()) > 3 ? 3 : anchors.get(e.getBlock().getLocation());
            anchors.remove(e.getBlock().getLocation());
            RepeatingTask repeatingTask = new RepeatingTask(this, 3L, 3L) {
                private int i = 0;
                @Override
                public void run() {
                    if(i >= charges) {
                        cancelTask();
                    } else {
                        e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 5.0F, true, true);
                    }
                    i++;
                }
            };
        }
    }
}
