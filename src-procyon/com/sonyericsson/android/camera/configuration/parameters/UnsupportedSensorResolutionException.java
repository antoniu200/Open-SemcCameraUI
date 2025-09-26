// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

public class UnsupportedSensorResolutionException extends RuntimeException
{
    private static final long serialVersionUID = -9181369815613920691L;
    
    public UnsupportedSensorResolutionException(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Sensor which max picture width = ");
        sb.append(i);
        sb.append(" is not supported");
        super(sb.toString());
    }
}
