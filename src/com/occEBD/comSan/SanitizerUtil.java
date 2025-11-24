package com.occEBD.comSan;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Core sanitization utility for command validation.
 * Enhanced with Geyser (Bedrock Edition) and ViaConnect (cross-version) compatibility.
 */
public class SanitizerUtil {

    // ===== PATTERN DEFINITIONS =====
    
    // Control characters (excluding normal whitespace)
    private static final Pattern CONTROL_CHARS = Pattern.compile("[\\p{Cntrl}&&[^\\t\\n\\r]]");
    
    // Newlines and carriage returns (command injection vector)
    private static final Pattern NEWLINES = Pattern.compile("[\\r\\n]");
    
    // Shell command separators (multi-command injection)
    private static final Pattern SHELL_SEPARATORS = Pattern.compile("[;&|`$()]");
    
    // URL detection (potentially suspicious in commands)
    private static final Pattern URLS = Pattern.compile(
        "(?i)(?:https?|ftp)://[a-z0-9.-]+(?:\\.[a-z]{2,})+(?:/[^\\s]*)?"
    );
    
    // Large Base64 detection (potential payload hiding)
    private static final Pattern BASE64_LARGE = Pattern.compile(
        "[A-Za-z0-9+/]{100,}={0,2}"
    );
    
    // Suspicious keywords (common exploit patterns)
    private static final Pattern SUSPICIOUS_KEYWORDS = Pattern.compile(
        "(?i)\\b(?:eval|exec|system|script|execute|payload|inject|exploit|hack|bypass)\\b"
    );
    
    // Bedrock-specific: Detect Bedrock Edition formatting codes (§ followed by character)
    // These are legitimate in Bedrock but need proper handling
    private static final Pattern BEDROCK_FORMAT_CODES = Pattern.compile("§[0-9a-fk-or]");
    
    // Zero-width characters often used in obfuscation (Geyser may pass these through)
    private static final Pattern ZERO_WIDTH_CHARS = Pattern.compile(
        "[\\u200B\\u200C\\u200D\\uFEFF\\u00AD]"
    );
    
    // Mixed script detection (homoglyph attacks across different Unicode blocks)
    // This is particularly important for Geyser as mobile keyboards vary
    private static final Pattern MIXED_SCRIPTS = Pattern.compile(
        "(?:(?:[\\u0400-\\u04FF].*[\\u0020-\\u007F])|(?:[\\u0020-\\u007F].*[\\u0400-\\u04FF]))"
    );

    /**
     * Sanitize a command with enhanced compatibility for Geyser and ViaConnect.
     * 
     * @param rawMessage The raw command message to sanitize
     * @param config Configuration service for sanitization rules
     * @return SanitizationResult indicating whether command is allowed and any modifications
     */
    public static SanitizationResult sanitize(String rawMessage, ConfigurationService config) {
        if (rawMessage == null) {
            return SanitizationResult.block("null_message");
        }
        
        // Handle empty commands
        if (rawMessage.trim().isEmpty()) {
            return SanitizationResult.block("empty_message");
        }
        
        String original = rawMessage;
        
        // Remove leading slash for uniform processing
        String working = original.startsWith("/") ? original.substring(1) : original;
        
        // === STEP 1: Pre-normalization cleaning ===
        
        // Remove zero-width characters (often used in obfuscation, can come from mobile clients)
        String preNormalized = ZERO_WIDTH_CHARS.matcher(working).replaceAll("");
        
        // === STEP 2: Unicode normalization ===
        
        // Normalize to NFKC to reduce homoglyph tricks
        // This is CRITICAL for Geyser compatibility as Bedrock clients may send different encodings
        String normalized;
        try {
            normalized = Normalizer.normalize(preNormalized, Normalizer.Form.NFKC);
        } catch (Exception e) {
            // If normalization fails (malformed input), block it
            return SanitizationResult.block("normalization_failed");
        }
        
        // === STEP 3: Control character checks ===
        
        if (CONTROL_CHARS.matcher(normalized).find()) {
            return SanitizationResult.blockWithPreview("control_chars", normalized);
        }
        
        if (NEWLINES.matcher(normalized).find()) {
            return SanitizationResult.blockWithPreview("newlines", normalized);
        }
        
        // === STEP 4: Shell separator handling ===
        
        if (SHELL_SEPARATORS.matcher(normalized).find()) {
            if (config.isEscapeSeparators()) {
                // Replace with spaces (safer than allowing)
                normalized = SHELL_SEPARATORS.matcher(normalized).replaceAll(" ");
            } else {
                return SanitizationResult.blockWithPreview("shell_separators", normalized);
            }
        }
        
        // === STEP 5: URL detection ===
        
        if (URLS.matcher(normalized).find()) {
            if (!config.isAllowUrls()) {
                return SanitizationResult.blockWithPreview("urls_disallowed", normalized);
            }
            // URLs are allowed but flagged for logging
        }
        
        // === STEP 6: Base64 payload detection ===
        
        if (BASE64_LARGE.matcher(normalized).find()) {
            return SanitizationResult.blockWithPreview("large_base64_detected", normalized);
        }
        
        // === STEP 7: Suspicious keyword detection ===
        
        if (SUSPICIOUS_KEYWORDS.matcher(normalized).find()) {
            return SanitizationResult.blockWithPreview("suspicious_keywords", normalized);
        }
        
        // === STEP 8: Bedrock-specific handling ===
        
        // Preserve legitimate Bedrock formatting codes if configured
        // These are NOT security risks, just platform differences
        boolean hasBedrockFormatting = BEDROCK_FORMAT_CODES.matcher(normalized).find();
        if (hasBedrockFormatting && config.isStripBedrockFormatting()) {
            // Strip Bedrock formatting codes if configured
            normalized = BEDROCK_FORMAT_CODES.matcher(normalized).replaceAll("");
        }
        
        // === STEP 9: Length validation ===
        
        // Protect against extremely long commands (potential DoS)
        int maxCommandLength = config.getMaxCommandLength();
        if (normalized.length() > maxCommandLength) {
            return SanitizationResult.blockWithPreview("command_too_long", 
                normalized.substring(0, Math.min(50, normalized.length())) + "...");
        }
        
        // === STEP 10: Final whitespace normalization ===
        
        // Collapse repeated whitespace and trim
        // This prevents spacing-based obfuscation techniques
        String collapsed = normalized.replaceAll("\\s+", " ").trim();
        
        // === STEP 11: Check for modifications ===
        
        boolean modified = !collapsed.equals(working);
        String preview = collapsed;
        
        // Return success result
        return new SanitizationResult(true, modified, preview, "ok");
    }
    
    /**
     * Quick check if a message contains any suspicious patterns.
     * Useful for logging/monitoring without full sanitization.
     * 
     * @param message The message to check
     * @return true if any suspicious patterns are detected
     */
    public static boolean hasSuspiciousPatterns(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }
        
        return CONTROL_CHARS.matcher(message).find() ||
               NEWLINES.matcher(message).find() ||
               SHELL_SEPARATORS.matcher(message).find() ||
               BASE64_LARGE.matcher(message).find() ||
               SUSPICIOUS_KEYWORDS.matcher(message).find() ||
               ZERO_WIDTH_CHARS.matcher(message).find();
    }
}