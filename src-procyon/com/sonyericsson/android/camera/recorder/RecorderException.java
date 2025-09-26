// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder;

public class RecorderException extends Exception
{
    private static final long serialVersionUID = 1747395873533117021L;
    
    public RecorderException() {
    }
    
    public RecorderException(final String message) {
        super(message);
    }
    
    public RecorderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public RecorderException(final Throwable cause) {
        super(cause);
    }
}
