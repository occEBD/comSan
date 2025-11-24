package com.occEBD.comSan;


import org.bukkit.plugin.java.JavaPlugin;


public class MainPlugin extends JavaPlugin {


private ConfigurationService configurationService;
private LogService logService;
private CommandSanitizerListener sanitizerListener;


@Override
public void onEnable() {
// init services
this.configurationService = new ConfigurationService(this);
this.logService = new LogService(this.getLogger());


// register events and commands
this.sanitizerListener = new CommandSanitizerListener(configurationService, logService);
getServer().getPluginManager().registerEvents(sanitizerListener, this);


this.getCommand("sanitizestatus").setExecutor(new StatusCommand(configurationService));


logService.info("CommandSanitizer enabled — protecting commands from injection / MITM-style payloads");
}


@Override
public void onDisable() {
logService.info("CommandSanitizer disabled");
}
}