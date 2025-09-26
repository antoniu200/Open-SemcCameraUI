// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import android.util.Range;

public class IntegerRangeCapabilityItem extends CapabilityItem<Range<Integer>>
{
    IntegerRangeCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    IntegerRangeCapabilityItem(final String s, final Range<Integer> range) {
        super(s, range);
    }
    
    @Override
    Range<Integer> getDefaultValue() {
        return (Range<Integer>)new Range((Comparable)0, (Comparable)0);
    }
    
    public Range<Integer> read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return SharedPrefsTranslator.getIntegerRange(sharedPreferences.getString(s, ""));
        }
        return (Range<Integer>)new Range((Comparable)0, (Comparable)0);
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final Range range = ((CapabilityItem<Range>)this).get();
        if (range != null) {
            sharedPreferences$Editor.putString(this.getName(), SharedPrefsTranslator.fromIntegerRange((Range<Integer>)range));
        }
    }
}
