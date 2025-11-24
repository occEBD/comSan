> [!WARNING]
> I don't know HOW this plugin works completely. This code is quite old, too. 
> It **could** be in a broken state, documentation is written by AI to give **ME** an overview.
> I will rework SOME of this from the start, I don't know if this is vibecoded too.... so.... yeah.

> [!INFO]
> PROJECTID: 0N25-JAVA 
> PROJECTND: occEBD



# CommandSanitizer

A Minecraft Bukkit/Spigot plugin that protects your server from command injection and MITM-style attacks by sanitizing player and console commands.

## Features

- **Command Injection Protection** - Detects and escapes shell separators (`;`, `|`, `&`, `` ` ``, `$`, `()`)
- **URL Filtering** - Block or allow URLs in commands
- **DoS Protection** - Configurable command length limits
- **Bedrock Support** - Automatic platform detection with Geyser/Floodgate
- **Cross-Version Compatible** - Works with ViaVersion, ViaBackwards, ViaRewind
- **Detailed Logging** - Platform-aware logging (Java/Bedrock) for security monitoring

## Installation

1. Download the latest `CommandSanitizer-*.jar`
2. Place in your server's `plugins/` directory
3. Restart the server
4. Configure in `plugins/CommandSanitizer/config.yml`
5. Reload or restart

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sanitizestatus` | Show current plugin configuration | Default |

## Configuration

Quick configuration overview (`plugins/CommandSanitizer/config.yml`):

```yaml
# Escape shell separators instead of blocking?
escape-separators: true

# Allow URLs in commands?
allow-urls: false

# Maximum command length (DoS protection)
max-command-length: 512

# Strip Bedrock formatting codes?
strip-bedrock-formatting: false

# Message shown when command is blocked
blocked-message: "§cBlocked by CommandSanitizer..."
```

See [docs/configUsage.md](docs/configUsage.md) for detailed configuration reference.

## Documentation

- **[Normal Usage](docs/normalUsage.md)** - How the plugin works for players and admins
- **[Configuration Reference](docs/configUsage.md)** - All config options explained
- **[Geyser/Bedrock Support](docs/geyserUsage.md)** - Bedrock Edition player compatibility
- **[ViaConnect Support](docs/viaConnectUsage.md)** - Cross-version compatibility

## Building from Source

```bash
chmod +x build.sh
./build.sh
```

The JAR will be generated in the `out/` directory as `CommandSanitizer-<filecount>-<timestamp>.jar`

## Requirements

- Minecraft server (Bukkit/Spigot/Paper) API 1.20+
- Java 8 or higher
- Optional: Geyser + Floodgate for Bedrock player detection
- Optional: ViaVersion suite for cross-version support

## License

See [LICENSE](LICENSE) file for details.

## Security

This plugin provides protection against:
- Command injection via shell separators
- Malicious URL payloads
- DoS attacks via extremely long commands
- MITM-style command manipulation

For best security, use `escape-separators: false` and `allow-urls: false`.
