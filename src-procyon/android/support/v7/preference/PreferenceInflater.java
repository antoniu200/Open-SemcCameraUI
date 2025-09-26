// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.preference;

import android.util.Xml;
import android.content.res.XmlResourceParser;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Intent;
import org.xmlpull.v1.XmlPullParser;
import android.support.v14.preference.SwitchPreference;
import android.view.InflateException;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.content.Context;
import java.lang.reflect.Constructor;
import java.util.HashMap;

class PreferenceInflater
{
    private static final HashMap<String, Constructor> CONSTRUCTOR_MAP;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE;
    private static final String EXTRA_TAG_NAME = "extra";
    private static final String INTENT_TAG_NAME = "intent";
    private static final String TAG = "PreferenceInflater";
    private final Object[] mConstructorArgs;
    private final Context mContext;
    private String[] mDefaultPackages;
    private PreferenceManager mPreferenceManager;
    
    static {
        CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class };
        CONSTRUCTOR_MAP = new HashMap<String, Constructor>();
    }
    
    public PreferenceInflater(final Context mContext, final PreferenceManager preferenceManager) {
        this.mConstructorArgs = new Object[2];
        this.mContext = mContext;
        this.init(preferenceManager);
    }
    
    private Preference createItem(@NonNull final String s, @Nullable final String[] array, final AttributeSet set) throws ClassNotFoundException, InflateException {
        Constructor<?> constructor = null;
        Label_0228: {
            if ((constructor = PreferenceInflater.CONSTRUCTOR_MAP.get(s)) == null) {
                Label_0249: {
                    try {
                        final ClassLoader classLoader = this.mContext.getClassLoader();
                        Class<?> loadClass2;
                        if (array != null && array.length != 0) {
                            final int length = array.length;
                            int n = 0;
                            final Class clazz = null;
                            final ClassNotFoundException ex = null;
                            Class<?> loadClass;
                            while (true) {
                                loadClass = clazz;
                                if (n < length) {
                                    final String str = array[n];
                                    try {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append(str);
                                        sb.append(s);
                                        loadClass = classLoader.loadClass(sb.toString());
                                    }
                                    catch (final ClassNotFoundException ex) {
                                        ++n;
                                        continue;
                                    }
                                    break;
                                }
                                break;
                            }
                            if ((loadClass2 = loadClass) == null) {
                                if (ex == null) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append(set.getPositionDescription());
                                    sb2.append(": Error inflating class ");
                                    sb2.append(s);
                                    throw new InflateException(sb2.toString());
                                }
                                throw ex;
                            }
                        }
                        else {
                            loadClass2 = classLoader.loadClass(s);
                        }
                        constructor = loadClass2.getConstructor(PreferenceInflater.CONSTRUCTOR_SIGNATURE);
                        constructor.setAccessible(true);
                        PreferenceInflater.CONSTRUCTOR_MAP.put(s, constructor);
                    }
                    catch (final Exception ex2) {
                        break Label_0249;
                    }
                    catch (final ClassNotFoundException ex3) {
                        throw ex3;
                    }
                    break Label_0228;
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(set.getPositionDescription());
                sb3.append(": Error inflating class ");
                sb3.append(s);
                final InflateException ex4 = new InflateException(sb3.toString());
                final Exception ex2;
                ex4.initCause((Throwable)ex2);
                throw ex4;
            }
        }
        final Object[] mConstructorArgs = this.mConstructorArgs;
        mConstructorArgs[1] = set;
        return (Preference)constructor.newInstance(mConstructorArgs);
    }
    
    private Preference createItemFromTag(String o, final AttributeSet set) {
        try {
            if (-1 == ((String)o).indexOf(46)) {
                o = this.onCreateItem((String)o, set);
            }
            else {
                o = this.createItem((String)o, null, set);
            }
            return (Preference)o;
        }
        catch (final Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(set.getPositionDescription());
            sb.append(": Error inflating class ");
            sb.append((String)o);
            final InflateException ex2 = new InflateException(sb.toString());
            ex2.initCause((Throwable)ex);
            throw ex2;
        }
        catch (final ClassNotFoundException ex3) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(set.getPositionDescription());
            sb2.append(": Error inflating class (not found)");
            sb2.append((String)o);
            final InflateException ex4 = new InflateException(sb2.toString());
            ex4.initCause((Throwable)ex3);
            throw ex4;
        }
        catch (final InflateException ex5) {
            throw ex5;
        }
    }
    
    private void init(final PreferenceManager mPreferenceManager) {
        this.mPreferenceManager = mPreferenceManager;
        final StringBuilder sb = new StringBuilder();
        sb.append(Preference.class.getPackage().getName());
        sb.append(".");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(SwitchPreference.class.getPackage().getName());
        sb2.append(".");
        this.setDefaultPackages(new String[] { string, sb2.toString() });
    }
    
    @NonNull
    private PreferenceGroup onMergeRoots(final PreferenceGroup preferenceGroup, @NonNull final PreferenceGroup preferenceGroup2) {
        if (preferenceGroup == null) {
            preferenceGroup2.onAttachedToHierarchy(this.mPreferenceManager);
            return preferenceGroup2;
        }
        return preferenceGroup;
    }
    
    private void rInflate(final XmlPullParser xmlPullParser, final Preference preference, final AttributeSet set) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        while (true) {
            final int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                break;
            }
            if (next != 2) {
                continue;
            }
            final String name = xmlPullParser.getName();
            if ("intent".equals(name)) {
                try {
                    preference.setIntent(Intent.parseIntent(this.getContext().getResources(), xmlPullParser, set));
                    continue;
                }
                catch (final IOException ex) {
                    final XmlPullParserException ex2 = new XmlPullParserException("Error parsing preference");
                    ex2.initCause((Throwable)ex);
                    throw ex2;
                }
            }
            if ("extra".equals(name)) {
                this.getContext().getResources().parseBundleExtra("extra", set, preference.getExtras());
                try {
                    skipCurrentTag(xmlPullParser);
                    continue;
                }
                catch (final IOException ex3) {
                    final XmlPullParserException ex4 = new XmlPullParserException("Error parsing preference");
                    ex4.initCause((Throwable)ex3);
                    throw ex4;
                }
            }
            final Preference itemFromTag = this.createItemFromTag(name, set);
            ((PreferenceGroup)preference).addItemFromInflater(itemFromTag);
            this.rInflate(xmlPullParser, itemFromTag, set);
        }
    }
    
    private static void skipCurrentTag(final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        int next;
        do {
            next = xmlPullParser.next();
        } while (next != 1 && (next != 3 || xmlPullParser.getDepth() > depth));
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public String[] getDefaultPackages() {
        return this.mDefaultPackages;
    }
    
    public Preference inflate(final int n, @Nullable final PreferenceGroup preferenceGroup) {
        final XmlResourceParser xml = this.getContext().getResources().getXml(n);
        try {
            return this.inflate((XmlPullParser)xml, preferenceGroup);
        }
        finally {
            xml.close();
        }
    }
    
    public Preference inflate(final XmlPullParser xmlPullParser, @Nullable PreferenceGroup onMergeRoots) {
        synchronized (this.mConstructorArgs) {
            final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            this.mConstructorArgs[0] = this.mContext;
            try {
                int next;
                do {
                    next = xmlPullParser.next();
                } while (next != 2 && next != 1);
                if (next != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(xmlPullParser.getPositionDescription());
                    sb.append(": No start tag found!");
                    throw new InflateException(sb.toString());
                }
                onMergeRoots = this.onMergeRoots(onMergeRoots, (PreferenceGroup)this.createItemFromTag(xmlPullParser.getName(), attributeSet));
                this.rInflate(xmlPullParser, onMergeRoots, attributeSet);
                return onMergeRoots;
            }
            catch (final IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(xmlPullParser.getPositionDescription());
                sb2.append(": ");
                sb2.append(ex.getMessage());
                final InflateException ex2 = new InflateException(sb2.toString());
                ex2.initCause((Throwable)ex);
                throw ex2;
            }
            catch (final XmlPullParserException ex3) {
                final InflateException ex4 = new InflateException(ex3.getMessage());
                ex4.initCause((Throwable)ex3);
                throw ex4;
            }
            catch (final InflateException ex5) {
                throw ex5;
            }
        }
    }
    
    protected Preference onCreateItem(final String s, final AttributeSet set) throws ClassNotFoundException {
        return this.createItem(s, this.mDefaultPackages, set);
    }
    
    public void setDefaultPackages(final String[] mDefaultPackages) {
        this.mDefaultPackages = mDefaultPackages;
    }
}
