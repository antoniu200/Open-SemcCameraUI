package org.apache.commons.imaging.formats.png;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class PngText {
    public final String keyword;
    public final String text;

    public PngText(String str, String str2) {
        this.keyword = str;
        this.text = str2;
    }

    public static class Text extends PngText {
        public Text(String str, String str2) {
            super(str, str2);
        }
    }

    public static class Ztxt extends PngText {
        public Ztxt(String str, String str2) {
            super(str, str2);
        }
    }

    public static class Itxt extends PngText {
        public final String languageTag;
        public final String translatedKeyword;

        public Itxt(String str, String str2, String str3, String str4) {
            super(str, str2);
            this.languageTag = str3;
            this.translatedKeyword = str4;
        }
    }
}
