# CommandSanitizer Documentation

## Overview
CommandSanitizer protects your Minecraft server from command injection and MITM-style attacks by sanitizing player and console commands.

## Features
- Shell separator detection and escaping (`; | & $ ()` etc.)
- URL filtering
- Command length limits (DoS protection)
- Bedrock Edition support (Geyser/Floodgate)
- Cross-version compatibility (ViaConnect)
- Detailed logging with platform detection

## Command
- `/sanitizestatus` - Show current configuration

## Documentation Files
- [configUsage.md](configUsage.md) - Configuration options reference
- [geyserUsage.md](geyserUsage.md) - Bedrock player support
- [viaConnectUsage.md](viaConnectUsage.md) - Cross-version compatibility

## Quick Start
1. Install plugin in `plugins/` directory
2. Restart server to generate `config.yml`
3. Adjust settings in `plugins/CommandSanitizer/config.yml`
4. Reload/restart server
