package com.sonyericsson.cameracommon.mediasaving.updator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CrQueryParameter {
    public String[] projection = null;
    public String where = null;
    public String[] selectionArgs = null;
    public String sortOrder = null;
    public int limit = 0;
    public int offset = 0;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.projection != null) {
            sb.append("project:[");
            for (String str : this.projection) {
                sb.append(str + ",");
            }
            sb.append("] ");
        }
        if (this.where != null) {
            sb.append("where:" + this.where + " ");
        }
        if (this.selectionArgs != null) {
            sb.append("selectionarg:[");
            for (String str2 : this.selectionArgs) {
                sb.append(str2 + ",");
            }
            sb.append("] ");
        }
        if (this.sortOrder != null) {
            sb.append("sort:" + this.sortOrder + " ");
        }
        sb.append("limit:" + this.limit + " ");
        sb.append("offset:" + this.offset + " ");
        return sb.toString();
    }
}
