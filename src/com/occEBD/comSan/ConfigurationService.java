package com.occEBD.comSan;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;


public class ConfigurationService {
private final JavaPlugin plugin;
private FileConfiguration cfg;


public ConfigurationService(JavaPlugin plugin) {
this.plugin = plugin;
load();
}


public void load() {
File f = new File(plugin.getDataFolder(), "config.yml");
if (!f.exists()) {
plugin.getDataFolder().mkdirs();
plugin.saveResource("config.yml", false);
}
cfg = YamlConfiguration.loadConfiguration(f);
}


public boolean isEscapeSeparators() {
return cfg.getBoolean("escape-separators", true);
}


public boolean isAllowUrls() {
return cfg.getBoolean("allow-urls", false);
}


public String getBlockedMessage() {
return cfg.getString("blocked-message", "§cCommand blocked by sanitizer: suspicious content detected.");
}


    public boolean isStripBedrockFormatting() {
        return cfg.getBoolean("strip-bedrock-formatting", false);
    }


    public int getMaxCommandLength() {
        return cfg.getInt("max-command-length", 512);
    }


    public String getStatus() {
        return "escape-separators=" + isEscapeSeparators() + 
               ", allow-urls=" + isAllowUrls() +
               ", strip-bedrock-formatting=" + isStripBedrockFormatting() +
               ", max-command-length=" + getMaxCommandLength();
    }
}