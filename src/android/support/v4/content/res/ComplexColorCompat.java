package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ComplexColorCompat {
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;

    private ComplexColorCompat(Shader shader, ColorStateList colorStateList, @ColorInt int i) {
        this.mShader = shader;
        this.mColorStateList = colorStateList;
        this.mColor = i;
    }

    static ComplexColorCompat from(@NonNull Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }

    static ComplexColorCompat from(@NonNull ColorStateList colorStateList) {
        return new ComplexColorCompat(null, colorStateList, colorStateList.getDefaultColor());
    }

    static ComplexColorCompat from(@ColorInt int i) {
        return new ComplexColorCompat(null, null, i);
    }

    @Nullable
    public Shader getShader() {
        return this.mShader;
    }

    @ColorInt
    public int getColor() {
        return this.mColor;
    }

    public void setColor(@ColorInt int i) {
        this.mColor = i;
    }

    public boolean isGradient() {
        return this.mShader != null;
    }

    public boolean isStateful() {
        return this.mShader == null && this.mColorStateList != null && this.mColorStateList.isStateful();
    }

    public boolean onStateChanged(int[] iArr) {
        int colorForState;
        if (!isStateful() || (colorForState = this.mColorStateList.getColorForState(iArr, this.mColorStateList.getDefaultColor())) == this.mColor) {
            return false;
        }
        this.mColor = colorForState;
        return true;
    }

    public boolean willDraw() {
        return isGradient() || this.mColor != 0;
    }

    @Nullable
    public static ComplexColorCompat inflate(@NonNull Resources resources, @ColorRes int i, @Nullable Resources.Theme theme) {
        try {
            return createFromXml(resources, i, theme);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to inflate ComplexColor.", e);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0044  */
    @android.support.annotation.NonNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.support.v4.content.res.ComplexColorCompat createFromXml(@android.support.annotation.NonNull android.content.res.Resources r6, @android.support.annotation.ColorRes int r7, @android.support.annotation.Nullable android.content.res.Resources.Theme r8) throws org.xmlpull.v1.XmlPullParserException, android.content.res.Resources.NotFoundException, java.io.IOException {
        /*
            android.content.res.XmlResourceParser r7 = r6.getXml(r7)
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r7)
        L8:
            int r1 = r7.next()
            r2 = 1
            r3 = 2
            if (r1 == r3) goto L13
            if (r1 == r2) goto L13
            goto L8
        L13:
            if (r1 == r3) goto L1d
            org.xmlpull.v1.XmlPullParserException r6 = new org.xmlpull.v1.XmlPullParserException
            java.lang.String r7 = "No start tag found"
            r6.<init>(r7)
            throw r6
        L1d:
            java.lang.String r1 = r7.getName()
            r3 = -1
            int r4 = r1.hashCode()
            r5 = 89650992(0x557f730, float:1.01546526E-35)
            if (r4 == r5) goto L3b
            r2 = 1191572447(0x4705f3df, float:34291.87)
            if (r4 == r2) goto L31
            goto L44
        L31:
            java.lang.String r2 = "selector"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L44
            r2 = 0
            goto L45
        L3b:
            java.lang.String r4 = "gradient"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L44
            goto L45
        L44:
            r2 = r3
        L45:
            switch(r2) {
                case 0: goto L6f;
                case 1: goto L66;
                default: goto L48;
            }
        L48:
            org.xmlpull.v1.XmlPullParserException r6 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r7 = r7.getPositionDescription()
            r8.append(r7)
            java.lang.String r7 = ": unsupported complex color tag "
            r8.append(r7)
            r8.append(r1)
            java.lang.String r7 = r8.toString()
            r6.<init>(r7)
            throw r6
        L66:
            android.graphics.Shader r6 = android.support.v4.content.res.GradientColorInflaterCompat.createFromXmlInner(r6, r7, r0, r8)
            android.support.v4.content.res.ComplexColorCompat r6 = from(r6)
            return r6
        L6f:
            android.content.res.ColorStateList r6 = android.support.v4.content.res.ColorStateListInflaterCompat.createFromXmlInner(r6, r7, r0, r8)
            android.support.v4.content.res.ComplexColorCompat r6 = from(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.res.ComplexColorCompat.createFromXml(android.content.res.Resources, int, android.content.res.Resources$Theme):android.support.v4.content.res.ComplexColorCompat");
    }
}
