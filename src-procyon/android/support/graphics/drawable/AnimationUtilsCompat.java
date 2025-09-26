// 
// Decompiled by Procyon v0.6.0
// 

package android.support.graphics.drawable;

import android.content.res.XmlResourceParser;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.content.res.Resources$NotFoundException;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.animation.AnimationUtils;
import android.os.Build$VERSION;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.util.Xml;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AnimationUtilsCompat
{
    private AnimationUtilsCompat() {
    }
    
    private static Interpolator createInterpolatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        Object o = null;
        while (true) {
            final int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return (Interpolator)o;
            }
            if (next != 2) {
                continue;
            }
            final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            final String name = xmlPullParser.getName();
            if (name.equals("linearInterpolator")) {
                o = new LinearInterpolator();
            }
            else if (name.equals("accelerateInterpolator")) {
                o = new AccelerateInterpolator(context, attributeSet);
            }
            else if (name.equals("decelerateInterpolator")) {
                o = new DecelerateInterpolator(context, attributeSet);
            }
            else if (name.equals("accelerateDecelerateInterpolator")) {
                o = new AccelerateDecelerateInterpolator();
            }
            else if (name.equals("cycleInterpolator")) {
                o = new CycleInterpolator(context, attributeSet);
            }
            else if (name.equals("anticipateInterpolator")) {
                o = new AnticipateInterpolator(context, attributeSet);
            }
            else if (name.equals("overshootInterpolator")) {
                o = new OvershootInterpolator(context, attributeSet);
            }
            else if (name.equals("anticipateOvershootInterpolator")) {
                o = new AnticipateOvershootInterpolator(context, attributeSet);
            }
            else if (name.equals("bounceInterpolator")) {
                o = new BounceInterpolator();
            }
            else {
                if (!name.equals("pathInterpolator")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown interpolator name: ");
                    sb.append(xmlPullParser.getName());
                    throw new RuntimeException(sb.toString());
                }
                o = new PathInterpolatorCompat(context, attributeSet, xmlPullParser);
            }
        }
    }
    
    public static Interpolator loadInterpolator(final Context context, final int n) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return AnimationUtils.loadInterpolator(context, n);
        }
        XmlResourceParser xmlResourceParser = null;
        Label_0277: {
            final XmlResourceParser animation;
            Label_0139: {
                Label_0054: {
                    if (n == 17563663) {
                        Label_0208: {
                            try {
                                try {
                                    return (Interpolator)new FastOutLinearInInterpolator();
                                }
                                finally {}
                            }
                            catch (final IOException animation) {
                                break Label_0139;
                            }
                            catch (final XmlPullParserException animation) {
                                break Label_0208;
                            }
                            break Label_0054;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Can't load animation resource ID #0x");
                        sb.append(Integer.toHexString(n));
                        final Resources$NotFoundException ex = new Resources$NotFoundException(sb.toString());
                        ex.initCause((Throwable)animation);
                        throw ex;
                    }
                }
                if (n == 17563661) {
                    return (Interpolator)new FastOutSlowInInterpolator();
                }
                if (n == 17563662) {
                    return (Interpolator)new LinearOutSlowInInterpolator();
                }
                animation = context.getResources().getAnimation(n);
                try {
                    final Interpolator interpolatorFromXml = createInterpolatorFromXml(context, context.getResources(), context.getTheme(), (XmlPullParser)animation);
                    if (animation != null) {
                        animation.close();
                    }
                    return interpolatorFromXml;
                }
                catch (final IOException ex2) {}
                catch (final XmlPullParserException ex3) {}
                finally {
                    xmlResourceParser = animation;
                    break Label_0277;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't load animation resource ID #0x");
            sb2.append(Integer.toHexString(n));
            final Resources$NotFoundException ex4 = new Resources$NotFoundException(sb2.toString());
            ex4.initCause((Throwable)animation);
            throw ex4;
        }
        if (xmlResourceParser != null) {
            xmlResourceParser.close();
        }
    }
}
