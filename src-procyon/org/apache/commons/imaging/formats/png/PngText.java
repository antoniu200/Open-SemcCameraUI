// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

public abstract class PngText
{
    public final String keyword;
    public final String text;
    
    public PngText(final String keyword, final String text) {
        this.keyword = keyword;
        this.text = text;
    }
    
    public static class Itxt extends PngText
    {
        public final String languageTag;
        public final String translatedKeyword;
        
        public Itxt(final String s, final String s2, final String languageTag, final String translatedKeyword) {
            super(s, s2);
            this.languageTag = languageTag;
            this.translatedKeyword = translatedKeyword;
        }
    }
    
    public static class Text extends PngText
    {
        public Text(final String s, final String s2) {
            super(s, s2);
        }
    }
    
    public static class Ztxt extends PngText
    {
        public Ztxt(final String s, final String s2) {
            super(s, s2);
        }
    }
}
