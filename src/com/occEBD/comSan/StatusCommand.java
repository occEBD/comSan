package com.occEBD.comSan;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class StatusCommand implements CommandExecutor {


private final ConfigurationService config;


public StatusCommand(ConfigurationService config) {
this.config = config;
}


@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
sender.sendMessage("[Sanitizer] Status: " + config.getStatus());
return true;
}
}