package tim03we.advancedjoin;

/*
 * Copyright (c) 2019 tim03we  < https://github.com/tim03we >
 * Discord: tim03we | TP#9129
 *
 * This software is distributed under "GNU General Public License v3.0".
 * This license allows you to use it and/or modify it but you are not at
 * all allowed to sell this plugin at any cost. If found doing so the
 * necessary action required would be taken.
 *
 * AdvancedJoin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3.0 for more details.
 *
 * You should have received a copy of the GNU General Public License v3.0
 * along with this program. If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveResource("config.yml");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (getConfig().getString("Spawn-Point").equals("")) {
            getLogger().debug("Since no spawn point was set in the config, the player is not teleported.");
        } else {
            getServer().loadLevel(getConfig().getString("Spawn-Point"));
            getServer().getScheduler().scheduleDelayedTask(new SpawnTask(this, event.getPlayer()), 1);
        }
        for (String command : getConfig().getStringList("Commands")) {
            getServer().dispatchCommand(new ConsoleCommandSender(), command.replace("&", "§").replace("{player}", event.getPlayer().getName()));
        }
        if(getConfig().getBoolean("Inventory-Clear")) {
            event.getPlayer().getInventory().clearAll();
        }
        if(getConfig().getBoolean("Health")) {
            event.getPlayer().setHealth(20);
        }
        if(getConfig().getBoolean("Feed")) {
            event.getPlayer().getFoodData().setLevel(20);
        }
        if(getConfig().getString("Welcome-Message").equals("")) {
            getLogger().debug("The player will not receive a welcome message because none has been set in the config.");
        } else {
            event.getPlayer().sendMessage(getConfig().getString("Welcome-Message").replace("{player}", event.getPlayer().getName()));
        }
        if(getConfig().getBoolean("Enable-JoinMessage")) {
            if (event.getPlayer().isOp()) {
                event.setJoinMessage(getConfig().getString("JoinMessage-OP").replace("&", "§").replace("{player}", event.getPlayer().getName()));
            } else if(event.getPlayer().hasPermission("staff.join")) {
                event.setJoinMessage(getConfig().getString("JoinMessage-Staff").replace("&", "§").replace("{player}", event.getPlayer().getName()));
            } else {
                event.setJoinMessage(getConfig().getString("JoinMessage").replace("&", "§").replace("{player}", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        if(getConfig().getBoolean("Enable-QuitMessage")) {
            if (event.getPlayer().isOp()) {
                event.setQuitMessage(getConfig().getString("QuitMessage-OP").replace("&", "§").replace("{player}", event.getPlayer().getName()));
            } else if(event.getPlayer().hasPermission("staff.quit")) {
                event.setQuitMessage(getConfig().getString("QuitMessage-Staff").replace("&", "§").replace("{player}", event.getPlayer().getName()));
            } else {
                event.setQuitMessage(getConfig().getString("QuitMessage").replace("&", "§").replace("{player}", event.getPlayer().getName()));
            }
        }
    }
}
