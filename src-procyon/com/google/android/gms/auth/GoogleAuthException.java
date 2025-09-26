// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

public class GoogleAuthException extends Exception
{
    public GoogleAuthException() {
    }
    
    public GoogleAuthException(final String message) {
        super(message);
    }
    
    public GoogleAuthException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public GoogleAuthException(final Throwable cause) {
        super(cause);
    }
}
