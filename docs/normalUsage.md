# Normal Usage Guide

## How It Works

CommandSanitizer runs automatically in the background. Players don't need to do anything special - the plugin processes all commands before execution.

## Player Experience

### Allowed Commands
Normal commands work exactly as expected:
```
/tp PlayerName
/gamemode creative
/give @s diamond 64
```

### Sanitized Commands
If a command contains suspicious characters, they're automatically escaped:
- Shell separators (`;`, `|`, `&`) → converted to spaces
- The sanitized command executes automatically
- Players see: `Sanitized and re-dispatched for PlayerName: /command`

### Blocked Commands
Commands are blocked when:
- URLs detected (if `allow-urls: false`)
- Command exceeds `max-command-length`
- Unable to sanitize safely

Players see the configured `blocked-message`.

## Admin Commands

### `/sanitizestatus`
Shows current plugin configuration:
- `escape-separators` setting
- `allow-urls` setting
- `max-command-length` value
- `strip-bedrock-formatting` status

**Permission**: Default (all players can check status)

## Common Scenarios

### Accidental Paste
Player accidentally pastes text with special characters:
- Plugin escapes separators
- Command executes safely
- Logged for admin review

### Malicious Input
Player attempts command injection:
- Command blocked immediately
- Player notified
- Admin alerted in logs with platform (Java/Bedrock)

### Chat Plugin Compatibility
Works with chat/command plugins:
- Processes commands at event level
- Compatible with essentials, permissions, etc.
- No conflicts with most plugins
