package com.occEBD.comSan;


public class SanitizationResult {
public final boolean allowed;
public final boolean modified;
public final String preview;
public final String reason;


public SanitizationResult(boolean allowed, boolean modified, String preview, String reason) {
this.allowed = allowed;
this.modified = modified;
this.preview = preview;
this.reason = reason;
}


public static SanitizationResult block(String reason) {
return new SanitizationResult(false, false, "", reason);
}


public static SanitizationResult blockWithPreview(String reason, String preview) {
return new SanitizationResult(false, false, preview == null ? "" : preview, reason);
}
}

