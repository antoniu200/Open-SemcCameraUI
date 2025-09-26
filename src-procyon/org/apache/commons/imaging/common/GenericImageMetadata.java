// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class GenericImageMetadata implements ImageMetadata
{
    private static final String NEWLINE;
    private final List<ImageMetadataItem> items;
    
    static {
        NEWLINE = System.getProperty("line.separator");
    }
    
    public GenericImageMetadata() {
        this.items = new ArrayList<ImageMetadataItem>();
    }
    
    public void add(final String s, final String s2) {
        this.add(new GenericImageMetadataItem(s, s2));
    }
    
    public void add(final ImageMetadataItem imageMetadataItem) {
        this.items.add(imageMetadataItem);
    }
    
    @Override
    public List<? extends ImageMetadataItem> getItems() {
        return new ArrayList<ImageMetadataItem>(this.items);
    }
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    @Override
    public String toString(final String s) {
        String str = s;
        if (s == null) {
            str = "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.items.size(); ++i) {
            if (i > 0) {
                sb.append(GenericImageMetadata.NEWLINE);
            }
            final ImageMetadataItem imageMetadataItem = this.items.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("\t");
            sb.append(imageMetadataItem.toString(sb2.toString()));
        }
        return sb.toString();
    }
    
    public static class GenericImageMetadataItem implements ImageMetadataItem
    {
        private final String keyword;
        private final String text;
        
        public GenericImageMetadataItem(final String keyword, final String text) {
            this.keyword = keyword;
            this.text = text;
        }
        
        public String getKeyword() {
            return this.keyword;
        }
        
        public String getText() {
            return this.text;
        }
        
        @Override
        public String toString() {
            return this.toString(null);
        }
        
        @Override
        public String toString(final String str) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.keyword);
            sb.append(": ");
            sb.append(this.text);
            final String string = sb.toString();
            if (str != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(string);
                return sb2.toString();
            }
            return string;
        }
    }
}
