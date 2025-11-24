# Geyser / Bedrock Players Support

## Overview
CommandSanitizer automatically detects Bedrock Edition players using Floodgate API (bundled with Geyser).

## Detection
- Plugin checks for `org.geysermc.floodgate.api.FloodgateApi`
- Logs include platform type: `Java` or `Bedrock`
- No configuration needed - automatic detection

## Bedrock-Specific Considerations

### Formatting Codes
Bedrock clients may include § formatting codes. Use `strip-bedrock-formatting` config option:
- `false` (default): Preserves Bedrock color codes
- `true`: Strips § codes from commands

### URL Pasting
Bedrock players (especially mobile) may paste URLs more frequently. Consider:
- `allow-urls: false` - blocks all URLs (recommended)
- `allow-urls: true` - allows URLs (less secure)

### Command Length
Mobile paste operations might exceed default limits. Adjust `max-command-length` if needed.

## Requirements
- **Geyser** and **Floodgate** installed for platform detection
- Works without them, but logs will show all players as `Java`
