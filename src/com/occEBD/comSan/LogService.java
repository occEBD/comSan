package com.occEBD.comSan;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Logging service with different log levels for command sanitization events.
 * Helps administrators monitor security events and debug issues.
 */
public class LogService {
    
    private final Logger logger;
    private boolean debugMode;
    
    public LogService(Logger logger) {
        this.logger = logger;
        this.debugMode = false; // Can be made configurable
    }
    
    /**
     * Log an informational message
     */
    public void info(String message) {
        logger.log(Level.INFO, "[CommandSanitizer] " + message);
    }
    
    /**
     * Log a warning message (e.g., blocked commands)
     */
    public void warn(String message) {
        logger.log(Level.WARNING, "[CommandSanitizer] " + message);
    }
    
    /**
     * Log a debug message (only if debug mode is enabled)
     */
    public void debug(String message) {
        if (debugMode) {
            logger.log(Level.INFO, "[CommandSanitizer DEBUG] " + message);
        }
    }
    
    /**
     * Log a severe error message
     */
    public void error(String message) {
        logger.log(Level.SEVERE, "[CommandSanitizer ERROR] " + message);
    }
    
    /**
     * Enable or disable debug logging
     */
    public void setDebugMode(boolean enabled) {
        this.debugMode = enabled;
    }
    
    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return debugMode;
    }
}
