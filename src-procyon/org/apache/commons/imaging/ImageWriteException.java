// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

public class ImageWriteException extends ImagingException
{
    private static final long serialVersionUID = -1L;
    
    public ImageWriteException(final String s) {
        super(s);
    }
    
    public ImageWriteException(final String str, final Object obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append(obj);
        sb.append(" (");
        sb.append(getType(obj));
        sb.append(")");
        super(sb.toString());
    }
    
    public ImageWriteException(final String s, final Throwable t) {
        super(s, t);
    }
    
    private static String getType(final Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof Object[]) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[Object[]: ");
            sb.append(((Object[])o).length);
            sb.append("]");
            return sb.toString();
        }
        if (o instanceof char[]) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("[char[]: ");
            sb2.append(((char[])o).length);
            sb2.append("]");
            return sb2.toString();
        }
        if (o instanceof byte[]) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("[byte[]: ");
            sb3.append(((byte[])o).length);
            sb3.append("]");
            return sb3.toString();
        }
        if (o instanceof short[]) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("[short[]: ");
            sb4.append(((short[])o).length);
            sb4.append("]");
            return sb4.toString();
        }
        if (o instanceof int[]) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("[int[]: ");
            sb5.append(((int[])o).length);
            sb5.append("]");
            return sb5.toString();
        }
        if (o instanceof long[]) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("[long[]: ");
            sb6.append(((long[])o).length);
            sb6.append("]");
            return sb6.toString();
        }
        if (o instanceof float[]) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("[float[]: ");
            sb7.append(((float[])o).length);
            sb7.append("]");
            return sb7.toString();
        }
        if (o instanceof double[]) {
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("[double[]: ");
            sb8.append(((double[])o).length);
            sb8.append("]");
            return sb8.toString();
        }
        if (o instanceof boolean[]) {
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("[boolean[]: ");
            sb9.append(((boolean[])o).length);
            sb9.append("]");
            return sb9.toString();
        }
        return o.getClass().getName();
    }
}
