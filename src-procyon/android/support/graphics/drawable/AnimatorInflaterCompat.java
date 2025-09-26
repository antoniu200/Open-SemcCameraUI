// 
// Decompiled by Procyon v0.6.0
// 

package android.support.graphics.drawable;

import android.graphics.PathMeasure;
import android.graphics.Path;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.res.XmlResourceParser;
import android.content.res.Resources$NotFoundException;
import android.animation.AnimatorInflater;
import android.os.Build$VERSION;
import android.support.annotation.AnimatorRes;
import android.util.TypedValue;
import android.animation.TypeEvaluator;
import android.view.InflateException;
import android.support.v4.graphics.PathParser;
import android.util.Log;
import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.content.res.TypedArray;
import java.util.Iterator;
import java.util.ArrayList;
import android.support.v4.content.res.TypedArrayUtils;
import android.animation.ValueAnimator;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.animation.AnimatorSet;
import android.util.Xml;
import android.animation.Animator;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AnimatorInflaterCompat
{
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;
    
    private AnimatorInflaterCompat() {
    }
    
    private static Animator createAnimatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final float n) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, resources, resources$Theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0, n);
    }
    
    private static Animator createAnimatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final AttributeSet set, final AnimatorSet set2, final int n, final float n2) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        Object o = null;
        ArrayList list = null;
        while (true) {
            final int next = xmlPullParser.next();
            boolean b = false;
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                if (set2 != null && list != null) {
                    final Animator[] array = new Animator[list.size()];
                    final Iterator iterator = list.iterator();
                    int n3 = 0;
                    while (iterator.hasNext()) {
                        array[n3] = (Animator)iterator.next();
                        ++n3;
                    }
                    if (n == 0) {
                        set2.playTogether(array);
                    }
                    else {
                        set2.playSequentially(array);
                    }
                }
                return (Animator)o;
            }
            if (next != 2) {
                continue;
            }
            final String name = xmlPullParser.getName();
            Object e;
            if (name.equals("objectAnimator")) {
                e = loadObjectAnimator(context, resources, resources$Theme, set, n2, xmlPullParser);
            }
            else if (name.equals("animator")) {
                e = loadAnimator(context, resources, resources$Theme, set, null, n2, xmlPullParser);
            }
            else if (name.equals("set")) {
                e = new AnimatorSet();
                final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATOR_SET);
                createAnimatorFromXml(context, resources, resources$Theme, xmlPullParser, set, (AnimatorSet)e, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "ordering", 0, 0), n2);
                obtainAttributes.recycle();
                b = false;
            }
            else {
                if (!name.equals("propertyValuesHolder")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown animator name: ");
                    sb.append(xmlPullParser.getName());
                    throw new RuntimeException(sb.toString());
                }
                final PropertyValuesHolder[] loadValues = loadValues(context, resources, resources$Theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser));
                if (loadValues != null && o != null && o instanceof ValueAnimator) {
                    ((ValueAnimator)o).setValues(loadValues);
                }
                b = true;
                e = o;
            }
            o = e;
            if (set2 == null) {
                continue;
            }
            o = e;
            if (b) {
                continue;
            }
            ArrayList list2;
            if ((list2 = list) == null) {
                list2 = new ArrayList();
            }
            list2.add(e);
            o = e;
            list = list2;
        }
    }
    
    private static Keyframe createNewKeyframe(Keyframe keyframe, final float n) {
        if (keyframe.getType() == Float.TYPE) {
            keyframe = Keyframe.ofFloat(n);
        }
        else if (keyframe.getType() == Integer.TYPE) {
            keyframe = Keyframe.ofInt(n);
        }
        else {
            keyframe = Keyframe.ofObject(n);
        }
        return keyframe;
    }
    
    private static void distributeKeyframes(final Keyframe[] array, float n, int i, final int n2) {
        n /= n2 - i + 2;
        while (i <= n2) {
            array[i].setFraction(array[i - 1].getFraction() + n);
            ++i;
        }
    }
    
    private static void dumpKeyframes(final Object[] array, String s) {
        if (array != null && array.length != 0) {
            Log.d("AnimatorInflater", s);
            for (int length = array.length, i = 0; i < length; ++i) {
                final Keyframe keyframe = (Keyframe)array[i];
                final StringBuilder sb = new StringBuilder();
                sb.append("Keyframe ");
                sb.append(i);
                sb.append(": fraction ");
                if (keyframe.getFraction() < 0.0f) {
                    s = "null";
                }
                else {
                    s = (String)Float.valueOf(keyframe.getFraction());
                }
                sb.append((Object)s);
                sb.append(", ");
                sb.append(", value : ");
                if (keyframe.hasValue()) {
                    s = (String)keyframe.getValue();
                }
                else {
                    s = "null";
                }
                sb.append((Object)s);
                Log.d("AnimatorInflater", sb.toString());
            }
        }
    }
    
    private static PropertyValuesHolder getPVH(final TypedArray typedArray, int n, int n2, final int n3, final String s) {
        final TypedValue peekValue = typedArray.peekValue(n2);
        final boolean b = peekValue != null;
        int type;
        if (b) {
            type = peekValue.type;
        }
        else {
            type = 0;
        }
        final TypedValue peekValue2 = typedArray.peekValue(n3);
        final boolean b2 = peekValue2 != null;
        int type2;
        if (b2) {
            type2 = peekValue2.type;
        }
        else {
            type2 = 0;
        }
        int n4 = n;
        if (n == 4) {
            if ((b && isColorType(type)) || (b2 && isColorType(type2))) {
                n4 = 3;
            }
            else {
                n4 = 0;
            }
        }
        if (n4 == 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        final PropertyValuesHolder propertyValuesHolder = null;
        final PropertyValuesHolder propertyValuesHolder2 = null;
        PropertyValuesHolder propertyValuesHolder3;
        if (n4 == 2) {
            final String string = typedArray.getString(n2);
            final String string2 = typedArray.getString(n3);
            final PathParser.PathDataNode[] nodesFromPathData = PathParser.createNodesFromPathData(string);
            final PathParser.PathDataNode[] nodesFromPathData2 = PathParser.createNodesFromPathData(string2);
            if (nodesFromPathData == null) {
                propertyValuesHolder3 = propertyValuesHolder;
                if (nodesFromPathData2 == null) {
                    return propertyValuesHolder3;
                }
            }
            if (nodesFromPathData != null) {
                final PathDataEvaluator pathDataEvaluator = new PathDataEvaluator();
                if (nodesFromPathData2 != null) {
                    if (!PathParser.canMorph(nodesFromPathData, nodesFromPathData2)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(" Can't morph from ");
                        sb.append(string);
                        sb.append(" to ");
                        sb.append(string2);
                        throw new InflateException(sb.toString());
                    }
                    propertyValuesHolder3 = PropertyValuesHolder.ofObject(s, (TypeEvaluator)pathDataEvaluator, new Object[] { nodesFromPathData, nodesFromPathData2 });
                }
                else {
                    propertyValuesHolder3 = PropertyValuesHolder.ofObject(s, (TypeEvaluator)pathDataEvaluator, new Object[] { nodesFromPathData });
                }
            }
            else {
                propertyValuesHolder3 = propertyValuesHolder;
                if (nodesFromPathData2 != null) {
                    propertyValuesHolder3 = PropertyValuesHolder.ofObject(s, (TypeEvaluator)new PathDataEvaluator(), new Object[] { nodesFromPathData2 });
                }
            }
        }
        else {
            Object instance;
            if (n4 == 3) {
                instance = ArgbEvaluator.getInstance();
            }
            else {
                instance = null;
            }
            PropertyValuesHolder propertyValuesHolder5;
            if (n != 0) {
                PropertyValuesHolder propertyValuesHolder4;
                if (b) {
                    float n5;
                    if (type == 5) {
                        n5 = typedArray.getDimension(n2, 0.0f);
                    }
                    else {
                        n5 = typedArray.getFloat(n2, 0.0f);
                    }
                    if (b2) {
                        float n6;
                        if (type2 == 5) {
                            n6 = typedArray.getDimension(n3, 0.0f);
                        }
                        else {
                            n6 = typedArray.getFloat(n3, 0.0f);
                        }
                        propertyValuesHolder4 = PropertyValuesHolder.ofFloat(s, new float[] { n5, n6 });
                    }
                    else {
                        propertyValuesHolder4 = PropertyValuesHolder.ofFloat(s, new float[] { n5 });
                    }
                }
                else {
                    float n7;
                    if (type2 == 5) {
                        n7 = typedArray.getDimension(n3, 0.0f);
                    }
                    else {
                        n7 = typedArray.getFloat(n3, 0.0f);
                    }
                    propertyValuesHolder4 = PropertyValuesHolder.ofFloat(s, new float[] { n7 });
                }
                propertyValuesHolder5 = propertyValuesHolder4;
            }
            else if (b) {
                if (type == 5) {
                    n = (int)typedArray.getDimension(n2, 0.0f);
                }
                else if (isColorType(type)) {
                    n = typedArray.getColor(n2, 0);
                }
                else {
                    n = typedArray.getInt(n2, 0);
                }
                if (b2) {
                    if (type2 == 5) {
                        n2 = (int)typedArray.getDimension(n3, 0.0f);
                    }
                    else if (isColorType(type2)) {
                        n2 = typedArray.getColor(n3, 0);
                    }
                    else {
                        n2 = typedArray.getInt(n3, 0);
                    }
                    propertyValuesHolder5 = PropertyValuesHolder.ofInt(s, new int[] { n, n2 });
                }
                else {
                    propertyValuesHolder5 = PropertyValuesHolder.ofInt(s, new int[] { n });
                }
            }
            else {
                propertyValuesHolder5 = propertyValuesHolder2;
                if (b2) {
                    if (type2 == 5) {
                        n = (int)typedArray.getDimension(n3, 0.0f);
                    }
                    else if (isColorType(type2)) {
                        n = typedArray.getColor(n3, 0);
                    }
                    else {
                        n = typedArray.getInt(n3, 0);
                    }
                    propertyValuesHolder5 = PropertyValuesHolder.ofInt(s, new int[] { n });
                }
            }
            if ((propertyValuesHolder3 = propertyValuesHolder5) != null) {
                propertyValuesHolder3 = propertyValuesHolder5;
                if (instance != null) {
                    propertyValuesHolder5.setEvaluator((TypeEvaluator)instance);
                    propertyValuesHolder3 = propertyValuesHolder5;
                }
            }
        }
        return propertyValuesHolder3;
    }
    
    private static int inferValueTypeFromValues(final TypedArray typedArray, int n, int n2) {
        final TypedValue peekValue = typedArray.peekValue(n);
        final int n3 = 1;
        final int n4 = 0;
        if (peekValue != null) {
            n = 1;
        }
        else {
            n = 0;
        }
        int type;
        if (n != 0) {
            type = peekValue.type;
        }
        else {
            type = 0;
        }
        final TypedValue peekValue2 = typedArray.peekValue(n2);
        if (peekValue2 != null) {
            n2 = n3;
        }
        else {
            n2 = 0;
        }
        int type2;
        if (n2 != 0) {
            type2 = peekValue2.type;
        }
        else {
            type2 = 0;
        }
        if (n == 0 || !isColorType(type)) {
            n = n4;
            if (n2 == 0) {
                return n;
            }
            n = n4;
            if (!isColorType(type2)) {
                return n;
            }
        }
        n = 3;
        return n;
    }
    
    private static int inferValueTypeOfKeyframe(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final XmlPullParser xmlPullParser) {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_KEYFRAME);
        final boolean b = false;
        final TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        final boolean b2 = peekNamedValue != null;
        int n = b ? 1 : 0;
        if (b2) {
            n = (b ? 1 : 0);
            if (isColorType(peekNamedValue.type)) {
                n = 3;
            }
        }
        obtainAttributes.recycle();
        return n;
    }
    
    private static boolean isColorType(final int n) {
        return n >= 28 && n <= 31;
    }
    
    public static Animator loadAnimator(final Context context, @AnimatorRes final int n) throws Resources$NotFoundException {
        Animator animator;
        if (Build$VERSION.SDK_INT >= 24) {
            animator = AnimatorInflater.loadAnimator(context, n);
        }
        else {
            animator = loadAnimator(context, context.getResources(), context.getTheme(), n);
        }
        return animator;
    }
    
    public static Animator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, @AnimatorRes final int n) throws Resources$NotFoundException {
        return loadAnimator(context, resources, resources$Theme, n, 1.0f);
    }
    
    public static Animator loadAnimator(final Context context, final Resources ex, final Resources$Theme resources$Theme, @AnimatorRes final int i, final float n) throws Resources$NotFoundException {
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                final XmlResourceParser animation = ((Resources)ex).getAnimation(i);
                try {
                    final Animator animatorFromXml = createAnimatorFromXml(context, (Resources)ex, resources$Theme, (XmlPullParser)animation, n);
                    if (animation != null) {
                        animation.close();
                    }
                    return animatorFromXml;
                }
                catch (final IOException ex) {}
                catch (final XmlPullParserException ex) {}
                finally {
                    xmlResourceParser = animation;
                }
            }
            finally {}
        }
        catch (final IOException ex) {}
        catch (final XmlPullParserException ex) {}
        final StringBuilder sb = new StringBuilder();
        sb.append("Can't load animation resource ID #0x");
        sb.append(Integer.toHexString(i));
        final Resources$NotFoundException ex2 = new Resources$NotFoundException(sb.toString());
        ex2.initCause((Throwable)ex);
        throw ex2;
        if (xmlResourceParser != null) {
            xmlResourceParser.close();
        }
    }
    
    private static ValueAnimator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final ValueAnimator valueAnimator, final float n, final XmlPullParser xmlPullParser) throws Resources$NotFoundException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATOR);
        final TypedArray obtainAttributes2 = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        ValueAnimator valueAnimator2 = valueAnimator;
        if (valueAnimator == null) {
            valueAnimator2 = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(valueAnimator2, obtainAttributes, obtainAttributes2, n, xmlPullParser);
        final int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            valueAnimator2.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        if (obtainAttributes2 != null) {
            obtainAttributes2.recycle();
        }
        return valueAnimator2;
    }
    
    private static Keyframe loadKeyframe(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, int namedResourceId, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_KEYFRAME);
        final float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "fraction", 3, -1.0f);
        final TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        final boolean b = peekNamedValue != null;
        int n = namedResourceId;
        if (namedResourceId == 4) {
            if (b && isColorType(peekNamedValue.type)) {
                n = 3;
            }
            else {
                n = 0;
            }
        }
        Keyframe keyframe = null;
        Label_0185: {
            if (b) {
                if (n != 3) {
                    switch (n) {
                        default: {
                            keyframe = null;
                            break Label_0185;
                        }
                        case 0: {
                            keyframe = Keyframe.ofFloat(namedFloat, TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "value", 0, 0.0f));
                            break Label_0185;
                        }
                        case 1: {
                            break;
                        }
                    }
                }
                keyframe = Keyframe.ofInt(namedFloat, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "value", 0, 0));
            }
            else if (n == 0) {
                keyframe = Keyframe.ofFloat(namedFloat);
            }
            else {
                keyframe = Keyframe.ofInt(namedFloat);
            }
        }
        namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 1, 0);
        if (namedResourceId > 0) {
            keyframe.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        return keyframe;
    }
    
    private static ObjectAnimator loadObjectAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final float n, final XmlPullParser xmlPullParser) throws Resources$NotFoundException {
        final ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, resources, resources$Theme, set, (ValueAnimator)objectAnimator, n, xmlPullParser);
        return objectAnimator;
    }
    
    private static PropertyValuesHolder loadPvh(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final String s, int i) throws XmlPullParserException, IOException {
        final PropertyValuesHolder propertyValuesHolder = null;
        ArrayList list = null;
        int n = i;
        while (true) {
            i = xmlPullParser.next();
            if (i == 3 || i == 1) {
                break;
            }
            if (!xmlPullParser.getName().equals("keyframe")) {
                continue;
            }
            if ((i = n) == 4) {
                i = inferValueTypeOfKeyframe(resources, resources$Theme, Xml.asAttributeSet(xmlPullParser), xmlPullParser);
            }
            final Keyframe loadKeyframe = loadKeyframe(context, resources, resources$Theme, Xml.asAttributeSet(xmlPullParser), i, xmlPullParser);
            ArrayList list2 = list;
            if (loadKeyframe != null) {
                if ((list2 = list) == null) {
                    list2 = new ArrayList();
                }
                list2.add(loadKeyframe);
            }
            xmlPullParser.next();
            n = i;
            list = list2;
        }
        PropertyValuesHolder ofKeyframe = propertyValuesHolder;
        if (list != null) {
            final int size = list.size();
            ofKeyframe = propertyValuesHolder;
            if (size > 0) {
                final int n2 = 0;
                final Keyframe keyframe = (Keyframe)list.get(0);
                final Keyframe keyframe2 = (Keyframe)list.get(size - 1);
                final float fraction = keyframe2.getFraction();
                i = size;
                if (fraction < 1.0f) {
                    if (fraction < 0.0f) {
                        keyframe2.setFraction(1.0f);
                        i = size;
                    }
                    else {
                        list.add(list.size(), createNewKeyframe(keyframe2, 1.0f));
                        i = size + 1;
                    }
                }
                final float fraction2 = keyframe.getFraction();
                int n3 = i;
                if (fraction2 != 0.0f) {
                    if (fraction2 < 0.0f) {
                        keyframe.setFraction(0.0f);
                        n3 = i;
                    }
                    else {
                        list.add(0, createNewKeyframe(keyframe, 0.0f));
                        n3 = i + 1;
                    }
                }
                final Keyframe[] a = new Keyframe[n3];
                list.toArray(a);
                Keyframe keyframe3;
                int n4;
                int n5;
                int n6;
                for (i = n2; i < n3; ++i) {
                    keyframe3 = a[i];
                    if (keyframe3.getFraction() < 0.0f) {
                        if (i == 0) {
                            keyframe3.setFraction(0.0f);
                        }
                        else {
                            n4 = n3 - 1;
                            if (i == n4) {
                                keyframe3.setFraction(1.0f);
                            }
                            else {
                                n5 = i + 1;
                                n6 = i;
                                while (n5 < n4 && a[n5].getFraction() < 0.0f) {
                                    n6 = n5;
                                    ++n5;
                                }
                                distributeKeyframes(a, a[n6 + 1].getFraction() - a[i - 1].getFraction(), i, n6);
                            }
                        }
                    }
                }
                final PropertyValuesHolder propertyValuesHolder2 = ofKeyframe = PropertyValuesHolder.ofKeyframe(s, a);
                if (n == 3) {
                    propertyValuesHolder2.setEvaluator((TypeEvaluator)ArgbEvaluator.getInstance());
                    ofKeyframe = propertyValuesHolder2;
                }
            }
        }
        return ofKeyframe;
    }
    
    private static PropertyValuesHolder[] loadValues(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final AttributeSet set) throws XmlPullParserException, IOException {
        final PropertyValuesHolder[] array = null;
        ArrayList list = null;
        int index;
        while (true) {
            final int eventType = xmlPullParser.getEventType();
            index = 0;
            if (eventType == 3 || eventType == 1) {
                break;
            }
            if (eventType != 2) {
                xmlPullParser.next();
            }
            else {
                if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                    final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    final String namedString = TypedArrayUtils.getNamedString(obtainAttributes, xmlPullParser, "propertyName", 3);
                    final int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "valueType", 2, 4);
                    PropertyValuesHolder e;
                    if ((e = loadPvh(context, resources, resources$Theme, xmlPullParser, namedString, namedInt)) == null) {
                        e = getPVH(obtainAttributes, namedInt, 0, 1, namedString);
                    }
                    ArrayList list2 = list;
                    if (e != null) {
                        if ((list2 = list) == null) {
                            list2 = new ArrayList();
                        }
                        list2.add(e);
                    }
                    obtainAttributes.recycle();
                    list = list2;
                }
                xmlPullParser.next();
            }
        }
        PropertyValuesHolder[] array2 = array;
        if (list != null) {
            final int size = list.size();
            final PropertyValuesHolder[] array3 = new PropertyValuesHolder[size];
            while (true) {
                array2 = array3;
                if (index >= size) {
                    break;
                }
                array3[index] = (PropertyValuesHolder)list.get(index);
                ++index;
            }
        }
        return array2;
    }
    
    private static void parseAnimatorFromTypeArray(final ValueAnimator valueAnimator, final TypedArray typedArray, final TypedArray typedArray2, final float n, final XmlPullParser xmlPullParser) {
        final long duration = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        final long startDelay = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int namedInt;
        final int n2 = namedInt = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom")) {
            namedInt = n2;
            if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
                int inferValueTypeFromValues;
                if ((inferValueTypeFromValues = n2) == 4) {
                    inferValueTypeFromValues = inferValueTypeFromValues(typedArray, 5, 6);
                }
                final PropertyValuesHolder pvh = getPVH(typedArray, inferValueTypeFromValues, 5, 6, "");
                namedInt = inferValueTypeFromValues;
                if (pvh != null) {
                    valueAnimator.setValues(new PropertyValuesHolder[] { pvh });
                    namedInt = inferValueTypeFromValues;
                }
            }
        }
        valueAnimator.setDuration(duration);
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            setupObjectAnimator(valueAnimator, typedArray2, namedInt, n, xmlPullParser);
        }
    }
    
    private static void setupObjectAnimator(final ValueAnimator valueAnimator, final TypedArray typedArray, final int n, final float n2, final XmlPullParser xmlPullParser) {
        final ObjectAnimator objectAnimator = (ObjectAnimator)valueAnimator;
        final String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 1);
        if (namedString != null) {
            final String namedString2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyXName", 2);
            final String namedString3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyYName", 3);
            if (n != 2) {}
            if (namedString2 == null && namedString3 == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(typedArray.getPositionDescription());
                sb.append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(sb.toString());
            }
            setupPathMotion(PathParser.createPathFromPathData(namedString), objectAnimator, 0.5f * n2, namedString2, namedString3);
        }
        else {
            objectAnimator.setPropertyName(TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 0));
        }
    }
    
    private static void setupPathMotion(final Path path, final ObjectAnimator objectAnimator, float n, final String s, final String s2) {
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        final ArrayList list = new ArrayList();
        list.add(0.0f);
        float n2 = 0.0f;
        float f;
        do {
            f = n2 + pathMeasure.getLength();
            list.add(f);
            n2 = f;
        } while (pathMeasure.nextContour());
        final PathMeasure pathMeasure2 = new PathMeasure(path, false);
        final int min = Math.min(100, (int)(f / n) + 1);
        final float[] array = new float[min];
        final float[] array2 = new float[min];
        final float[] array3 = new float[2];
        final float n3 = f / (min - 1);
        int index = 0;
        n = 0.0f;
        int n4 = 0;
        PropertyValuesHolder propertyValuesHolder;
        while (true) {
            propertyValuesHolder = null;
            if (n4 >= min) {
                break;
            }
            pathMeasure2.getPosTan(n - (float)list.get(index), array3, (float[])null);
            array[n4] = array3[0];
            array2[n4] = array3[1];
            n += n3;
            final int index2 = index + 1;
            int n5 = index;
            if (index2 < list.size()) {
                n5 = index;
                if (n > (float)list.get(index2)) {
                    pathMeasure2.nextContour();
                    n5 = index2;
                }
            }
            ++n4;
            index = n5;
        }
        PropertyValuesHolder ofFloat;
        if (s != null) {
            ofFloat = PropertyValuesHolder.ofFloat(s, array);
        }
        else {
            ofFloat = null;
        }
        PropertyValuesHolder ofFloat2 = propertyValuesHolder;
        if (s2 != null) {
            ofFloat2 = PropertyValuesHolder.ofFloat(s2, array2);
        }
        if (ofFloat == null) {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat2 });
        }
        else if (ofFloat2 == null) {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat });
        }
        else {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat, ofFloat2 });
        }
    }
    
    private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]>
    {
        private PathParser.PathDataNode[] mNodeArray;
        
        PathDataEvaluator() {
        }
        
        PathDataEvaluator(final PathParser.PathDataNode[] mNodeArray) {
            this.mNodeArray = mNodeArray;
        }
        
        public PathParser.PathDataNode[] evaluate(final float n, final PathParser.PathDataNode[] array, final PathParser.PathDataNode[] array2) {
            if (!PathParser.canMorph(array, array2)) {
                throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            }
            if (this.mNodeArray == null || !PathParser.canMorph(this.mNodeArray, array)) {
                this.mNodeArray = PathParser.deepCopyNodes(array);
            }
            for (int i = 0; i < array.length; ++i) {
                this.mNodeArray[i].interpolatePathDataNode(array[i], array2[i], n);
            }
            return this.mNodeArray;
        }
    }
}
