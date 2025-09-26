// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content.res;

import android.util.Log;
import java.io.IOException;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.graphics.Shader;
import android.content.res.ColorStateList;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class ComplexColorCompat
{
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;
    
    private ComplexColorCompat(final Shader mShader, final ColorStateList mColorStateList, @ColorInt final int mColor) {
        this.mShader = mShader;
        this.mColorStateList = mColorStateList;
        this.mColor = mColor;
    }
    
    @NonNull
    private static ComplexColorCompat createFromXml(@NonNull final Resources resources, @ColorRes int n, @Nullable final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        final XmlResourceParser xml = resources.getXml(n);
        final AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
        int next;
        do {
            next = ((XmlPullParser)xml).next();
            n = 1;
        } while (next != 2 && next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        final String name = ((XmlPullParser)xml).getName();
        final int hashCode = name.hashCode();
        Label_0112: {
            if (hashCode != 89650992) {
                if (hashCode == 1191572447) {
                    if (name.equals("selector")) {
                        n = 0;
                        break Label_0112;
                    }
                }
            }
            else if (name.equals("gradient")) {
                break Label_0112;
            }
            n = -1;
        }
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append(((XmlPullParser)xml).getPositionDescription());
                sb.append(": unsupported complex color tag ");
                sb.append(name);
                throw new XmlPullParserException(sb.toString());
            }
            case 1: {
                return from(GradientColorInflaterCompat.createFromXmlInner(resources, (XmlPullParser)xml, attributeSet, resources$Theme));
            }
            case 0: {
                return from(ColorStateListInflaterCompat.createFromXmlInner(resources, (XmlPullParser)xml, attributeSet, resources$Theme));
            }
        }
    }
    
    static ComplexColorCompat from(@ColorInt final int n) {
        return new ComplexColorCompat(null, null, n);
    }
    
    static ComplexColorCompat from(@NonNull final ColorStateList list) {
        return new ComplexColorCompat(null, list, list.getDefaultColor());
    }
    
    static ComplexColorCompat from(@NonNull final Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }
    
    @Nullable
    public static ComplexColorCompat inflate(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) {
        try {
            return createFromXml(resources, n, resources$Theme);
        }
        catch (final Exception ex) {
            Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", (Throwable)ex);
            return null;
        }
    }
    
    @ColorInt
    public int getColor() {
        return this.mColor;
    }
    
    @Nullable
    public Shader getShader() {
        return this.mShader;
    }
    
    public boolean isGradient() {
        return this.mShader != null;
    }
    
    public boolean isStateful() {
        return this.mShader == null && this.mColorStateList != null && this.mColorStateList.isStateful();
    }
    
    public boolean onStateChanged(final int[] array) {
        if (this.isStateful()) {
            final int colorForState = this.mColorStateList.getColorForState(array, this.mColorStateList.getDefaultColor());
            if (colorForState != this.mColor) {
                final boolean b = true;
                this.mColor = colorForState;
                return b;
            }
        }
        return false;
    }
    
    public void setColor(@ColorInt final int mColor) {
        this.mColor = mColor;
    }
    
    public boolean willDraw() {
        return this.isGradient() || this.mColor != 0;
    }
}
