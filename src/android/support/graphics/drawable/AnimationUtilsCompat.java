package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AnimationUtilsCompat {
    public static Interpolator loadInterpolator(Context context, int i) throws Throwable {
        if (Build.VERSION.SDK_INT >= 21) {
            return AnimationUtils.loadInterpolator(context, i);
        }
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                if (i == 17563663) {
                    return new FastOutLinearInInterpolator();
                }
                if (i == 17563661) {
                    return new FastOutSlowInInterpolator();
                }
                if (i == 17563662) {
                    return new LinearOutSlowInInterpolator();
                }
                XmlResourceParser animation = context.getResources().getAnimation(i);
                try {
                    Interpolator interpolatorCreateInterpolatorFromXml = createInterpolatorFromXml(context, context.getResources(), context.getTheme(), animation);
                    if (animation != null) {
                        animation.close();
                    }
                    return interpolatorCreateInterpolatorFromXml;
                } catch (IOException e) {
                    e = e;
                    Resources.NotFoundException notFoundException = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(i));
                    notFoundException.initCause(e);
                    throw notFoundException;
                } catch (XmlPullParserException e2) {
                    e = e2;
                    Resources.NotFoundException notFoundException2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(i));
                    notFoundException2.initCause(e);
                    throw notFoundException2;
                } catch (Throwable th) {
                    th = th;
                    xmlResourceParser = animation;
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
            } catch (XmlPullParserException e4) {
                e = e4;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static Interpolator createInterpolatorFromXml(Context context, Resources res,
            Theme theme,
            XmlPullParser parser)
            throws XmlPullParserException, IOException {
        Interpolator interpolator = null;
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            AttributeSet attrs = Xml.asAttributeSet(parser);
            String name = parser.getName();
            if (name.equals("linearInterpolator")) {
                interpolator = new LinearInterpolator();
            } else if (name.equals("accelerateInterpolator")) {
                interpolator = new AccelerateInterpolator(context, attrs);
            } else if (name.equals("decelerateInterpolator")) {
                interpolator = new DecelerateInterpolator(context, attrs);
            } else if (name.equals("accelerateDecelerateInterpolator")) {
                interpolator = new AccelerateDecelerateInterpolator();
            } else if (name.equals("cycleInterpolator")) {
                interpolator = new CycleInterpolator(context, attrs);
            } else if (name.equals("anticipateInterpolator")) {
                interpolator = new AnticipateInterpolator(context, attrs);
            } else if (name.equals("overshootInterpolator")) {
                interpolator = new OvershootInterpolator(context, attrs);
            } else if (name.equals("anticipateOvershootInterpolator")) {
                interpolator = new AnticipateOvershootInterpolator(context, attrs);
            } else if (name.equals("bounceInterpolator")) {
                interpolator = new BounceInterpolator();
            } else if (name.equals("pathInterpolator")) {
                interpolator = new PathInterpolatorCompat(context, attrs, parser);
            } else {
                throw new RuntimeException("Unknown interpolator name: " + parser.getName());
            }
        }
        return interpolator;
    }

    private AnimationUtilsCompat() {
    }
}
