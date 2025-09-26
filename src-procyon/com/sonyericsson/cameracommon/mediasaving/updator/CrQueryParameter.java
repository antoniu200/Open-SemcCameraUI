// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.updator;

public final class CrQueryParameter
{
    public int limit;
    public int offset;
    public String[] projection;
    public String[] selectionArgs;
    public String sortOrder;
    public String where;
    
    public CrQueryParameter() {
        this.projection = null;
        this.where = null;
        this.selectionArgs = null;
        this.sortOrder = null;
        this.limit = 0;
        this.offset = 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String[] projection = this.projection;
        final int n = 0;
        if (projection != null) {
            sb.append("project:[");
            for (final String str : this.projection) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(",");
                sb.append(sb2.toString());
            }
            sb.append("] ");
        }
        if (this.where != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("where:");
            sb3.append(this.where);
            sb3.append(" ");
            sb.append(sb3.toString());
        }
        if (this.selectionArgs != null) {
            sb.append("selectionarg:[");
            final String[] selectionArgs = this.selectionArgs;
            for (int length2 = selectionArgs.length, j = n; j < length2; ++j) {
                final String str2 = selectionArgs[j];
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(str2);
                sb4.append(",");
                sb.append(sb4.toString());
            }
            sb.append("] ");
        }
        if (this.sortOrder != null) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("sort:");
            sb5.append(this.sortOrder);
            sb5.append(" ");
            sb.append(sb5.toString());
        }
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("limit:");
        sb6.append(this.limit);
        sb6.append(" ");
        sb.append(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("offset:");
        sb7.append(this.offset);
        sb7.append(" ");
        sb.append(sb7.toString());
        return sb.toString();
    }
}
