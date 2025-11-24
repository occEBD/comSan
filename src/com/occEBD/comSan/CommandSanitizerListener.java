package com.occEBD.comSan;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;


public class CommandSanitizerListener implements Listener {


private ConfigurationService config;
private LogService log;


public CommandSanitizerListener(ConfigurationService config, LogService log) {
this.config = config;
this.log = log;
}


    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String raw = e.getMessage(); // starts with '/'
        Player p = e.getPlayer();
        
        try {
            SanitizationResult res = SanitizerUtil.sanitize(raw, config);
            
            if (!res.allowed) {
                e.setCancelled(true);
                p.sendMessage(config.getBlockedMessage());
                
                // Enhanced logging with platform detection (for Geyser/Floodgate)
                String platform = detectPlayerPlatform(p);
                log.warn("Blocked player command from " + p.getName() + 
                        " (" + platform + "): " + res.reason);
                log.debug("Original: " + raw + " -> Sanitized preview: " + res.preview);
                return;
            }
            
            if (!res.modified) return; // nothing changed
            
            // Replace message by sanitized preview (without leading slash)
            String sanitized = res.preview.startsWith("/") ? res.preview : "/" + res.preview;
            
            // Manipulate to execute sanitized command instead of original
            e.setCancelled(true);
            getFallbackExecutor(p, sanitized);
            log.info("Sanitized and re-dispatched for " + p.getName() + ": " + sanitized);
            
        } catch (Exception ex) {
            // Fail-safe: if sanitization crashes, block the command
            e.setCancelled(true);
            p.sendMessage("§cAn error occurred while processing your command.");
            log.error("Exception during command sanitization for " + p.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Detect player platform (Java/Bedrock) for logging purposes.
     * Works with Geyser/Floodgate if installed.
     */
    private String detectPlayerPlatform(Player player) {
        try {
            // Try to detect Floodgate (comes with Geyser)
            Class<?> floodgateApi = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object api = floodgateApi.getMethod("getInstance").invoke(null);
            boolean isBedrock = (boolean) floodgateApi
                .getMethod("isFloodgatePlayer", java.util.UUID.class)
                .invoke(api, player.getUniqueId());
            
            if (isBedrock) {
                return "Bedrock";
            }
        } catch (Exception ignored) {
            // Floodgate not installed or player is Java
        }
        
        return "Java";
    }



@EventHandler
public void onConsoleCommand(ServerCommandEvent e) {
String raw = e.getCommand();
CommandSender sender = e.getSender();


SanitizationResult res = SanitizerUtil.sanitize(raw, config);
if (!res.allowed) {
e.setCancelled(true);
sender.sendMessage(config.getBlockedMessage());
log.warn("Blocked console command: " + res.reason);
log.debug("Original: " + raw + " -> Sanitized preview: " + res.preview);
return;
}


if (!res.modified) return;


e.setCommand(res.preview);
log.info("Sanitized console command -> " + res.preview);
}


private void getFallbackExecutor(CommandSender sender, String sanitized) {
// dispatch the sanitized command from the same sender context
if (sender instanceof Player) {
getPlayerDispatch((Player) sender, sanitized);
} else {
sender.getServer().dispatchCommand(sender, sanitized.replaceFirst("^/", ""));
}
}


private void getPlayerDispatch(Player player, String sanitized) {
// dispatch as the player without the leading slash
String withoutSlash = sanitized.startsWith("/") ? sanitized.substring(1) : sanitized;
player.getServer().dispatchCommand(player, withoutSlash);
}
}