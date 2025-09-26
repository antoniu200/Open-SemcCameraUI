// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

public class ImagingException extends Exception
{
    private static final long serialVersionUID = -1L;
    
    public ImagingException(final String message) {
        super(message);
    }
    
    public ImagingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
