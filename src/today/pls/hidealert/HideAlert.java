package today.pls.hidealert;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class HideAlert extends JavaPlugin implements Listener {
    FileConfiguration config;

    HashMap<UUID,String> advancementWasChanged = new HashMap<>();
    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        config = this.getConfig();

        config.options().copyDefaults(true);
        this.saveConfig();

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent pje){
        int blockreq = config.getInt("JoinMutePopReq",20);
        if(blockreq >= 0 && blockreq <= getServerPop())
            pje.setJoinMessage("");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent pqe){
        int blockreq = config.getInt("LeaveMutePopReq",20);
        if(blockreq >= 0 && blockreq <= getServerPop())
            pqe.setQuitMessage("");
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent pade){
        int blockreq = config.getInt("AdvancementMutePopReq",20);
        if(blockreq >= 0 && blockreq < getServerPop()) {
            advancementWasChanged.put(pade.getPlayer().getWorld().getUID(),pade.getPlayer().getWorld().getGameRuleValue("announceAdvancements"));
            pade.getPlayer().getWorld().setGameRuleValue("announceAdvancements", "false");
        }else if(advancementWasChanged.containsKey(pade.getPlayer().getWorld().getUID()))
            pade.getPlayer().getWorld().setGameRuleValue("announceAdvancements",advancementWasChanged.get(pade.getPlayer().getWorld().getUID()));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent pde){
        int blockreq = config.getInt("DeathMutePopReq",20);
        if(blockreq >= 0 && blockreq < getServerPop())
            pde.setDeathMessage("");
    }

    public int getServerPop(){
        return getServer().getOnlinePlayers().size();
    }
}
