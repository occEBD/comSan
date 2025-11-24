# ViaConnect / Cross-Version Support

## Overview
CommandSanitizer is compatible with ViaVersion, ViaBackwards, and ViaRewind (ViaConnect suite).

## How It Works
- Sanitization happens at the command event level
- Works independently of protocol translation layers
- No special configuration needed

## Compatibility
- **ViaVersion**: Full support for newer clients on older servers
- **ViaBackwards**: Full support for older clients on newer servers
- **ViaRewind**: Full support for legacy protocol versions

## Security Across Versions
Command injection attempts are blocked regardless of client version:
- Shell separators are escaped or blocked
- URLs are filtered (if `allow-urls: false`)
- Command length limits apply to all versions

## No Additional Setup
Just install ViaConnect plugins alongside CommandSanitizer. Both work transparently together.
