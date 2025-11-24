# Configuration Guide

## Config File Location
`plugins/CommandSanitizer/config.yml`

## Security Settings

### `escape-separators`
- **Type**: boolean
- **Default**: `true`
- **Description**: Escapes shell separators (`;`, `|`, `&`, `` ` ``, `$`, `()`) to spaces instead of blocking
- **Recommended**: `true` for less strict, `false` for maximum security

### `allow-urls`
- **Type**: boolean
- **Default**: `false`
- **Description**: Allow URLs in commands
- **Recommended**: `false` for security

### `max-command-length`
- **Type**: integer
- **Default**: `512`
- **Description**: Maximum command length in characters (DoS protection)

## Cross-Platform

### `strip-bedrock-formatting`
- **Type**: boolean
- **Default**: `false`
- **Description**: Remove Bedrock formatting codes (§)
- **Recommended**: `false` (preserve for Bedrock color support)

## Messages

### `blocked-message`
- **Type**: string
- **Default**: Multi-line error message
- **Description**: Message shown when command is blocked
- **Format**: Supports § color codes and `\n` for newlines
