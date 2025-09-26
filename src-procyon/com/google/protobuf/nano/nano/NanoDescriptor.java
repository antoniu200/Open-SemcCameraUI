// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano.nano;

import com.google.protobuf.nano.NanoEnumValue;
import com.google.protobuf.nano.DescriptorProtos;
import com.google.protobuf.nano.Extension;

public abstract class NanoDescriptor
{
    public static final Extension<DescriptorProtos.EnumValueOptions, Boolean> emeritus;
    public static final Extension<DescriptorProtos.FileOptions, byte[]> encodedMungee;
    public static final Extension<DescriptorProtos.EnumOptions, Boolean> enumAsLite;
    public static final Extension<DescriptorProtos.EnumOptions, String> enumUnmungedFileDescriptorName;
    public static final Extension<DescriptorProtos.FileOptions, Boolean> fileAsLite;
    public static final Extension<DescriptorProtos.FieldOptions, Integer> generateAs;
    public static final Extension<DescriptorProtos.EnumOptions, Boolean> legacyEnum;
    public static final Extension<DescriptorProtos.MessageOptions, Boolean> legacyOneof;
    public static final Extension<DescriptorProtos.MessageOptions, Boolean> messageAsLite;
    public static final Extension<DescriptorProtos.MessageOptions, String> messageUnmungedFileDescriptorName;
    public static final Extension<DescriptorProtos.FileOptions, Integer> munger;
    public static final Extension<DescriptorProtos.EnumValueOptions, Boolean> unmungedDepsCompliant;
    public static final Extension<DescriptorProtos.EnumValueOptions, Boolean> watermarkCompliant;
    public static final Extension<DescriptorProtos.EnumValueOptions, String[]> whitelisted;
    
    static {
        legacyOneof = Extension.createPrimitiveTyped(8, Boolean.class, 1180950304L);
        messageAsLite = Extension.createPrimitiveTyped(8, Boolean.class, 1195348696L);
        messageUnmungedFileDescriptorName = Extension.createPrimitiveTyped(9, String.class, 1522304402L);
        enumAsLite = Extension.createPrimitiveTyped(8, Boolean.class, 1195355736L);
        legacyEnum = Extension.createPrimitiveTyped(8, Boolean.class, 1308211224L);
        enumUnmungedFileDescriptorName = Extension.createPrimitiveTyped(9, String.class, 1522362506L);
        generateAs = Extension.createPrimitiveTyped(14, Integer.class, 1437615632L);
        watermarkCompliant = Extension.createPrimitiveTyped(8, Boolean.class, 1301621224L);
        emeritus = Extension.createPrimitiveTyped(8, Boolean.class, 1307892264L);
        unmungedDepsCompliant = Extension.createPrimitiveTyped(8, Boolean.class, 1362093848L);
        whitelisted = Extension.createRepeatedPrimitiveTyped(9, String[].class, 1432768322L, 1432768322L, 0L);
        munger = Extension.createPrimitiveTyped(14, Integer.class, 1243722024L);
        encodedMungee = Extension.createPrimitiveTyped(12, byte[].class, 1257962002L);
        fileAsLite = Extension.createPrimitiveTyped(8, Boolean.class, 1445185760L);
    }
    
    private NanoDescriptor() {
    }
    
    @NanoEnumValue(legacy = false, value = GenerateAs.class)
    public static int checkGenerateAsOrThrow(final int i) {
        if (i >= 0 && i <= 3) {
            return i;
        }
        final StringBuilder sb = new StringBuilder(42);
        sb.append(i);
        sb.append(" is not a valid enum GenerateAs");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @NanoEnumValue(legacy = false, value = GenerateAs.class)
    public static int[] checkGenerateAsOrThrow(int[] array) {
        array = array.clone();
        for (int length = array.length, i = 0; i < length; ++i) {
            checkGenerateAsOrThrow(array[i]);
        }
        return array;
    }
    
    @NanoEnumValue(legacy = false, value = Munger.class)
    public static int checkMungerOrThrow(final int i) {
        if (i >= 0 && i <= 18) {
            return i;
        }
        if (i >= 100 && i <= 101) {
            return i;
        }
        final StringBuilder sb = new StringBuilder(38);
        sb.append(i);
        sb.append(" is not a valid enum Munger");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @NanoEnumValue(legacy = false, value = Munger.class)
    public static int[] checkMungerOrThrow(int[] array) {
        array = array.clone();
        for (int length = array.length, i = 0; i < length; ++i) {
            checkMungerOrThrow(array[i]);
        }
        return array;
    }
    
    public interface GenerateAs
    {
        @NanoEnumValue(legacy = false, value = GenerateAs.class)
        public static final int INVALID = 0;
        @NanoEnumValue(legacy = false, value = GenerateAs.class)
        public static final int LITE = 2;
        @NanoEnumValue(legacy = false, value = GenerateAs.class)
        public static final int NANO = 1;
        @NanoEnumValue(legacy = false, value = GenerateAs.class)
        public static final int OMIT = 3;
    }
    
    public interface Munger
    {
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int ADS_EXPRESS_MOBILEAPP = 10;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int AGSA_PROTO = 2;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int ANDROID_UTIL_CONVERT_TO_NANO_PROTOS = 7;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int CLOCKWORK_PROTO = 15;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int COMMERCE_PAYMENTS_INSTORE_TOOLS_CONVERT_TO_NANO_PROTOS = 6;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int DOTS = 18;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int FIXED_CORRECT_PROTO_PLAY_COMMON = 14;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int FIXED_CORRECT_PROTO_PLAY_ENTERTAINMENT = 3;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int FIXED_CORRECT_PROTO_PLAY_STORE = 4;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int GOGGLES_PROTO = 16;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int HANGOUTS = 12;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int INNERTUBE = 11;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int KIDS_MANAGEMENT = 9;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int NONE = 0;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int REDUCED_NANO_PROTO = 1;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int S3_SPEECH_PROTO = 17;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int SIMUX = 8;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int TESTING_NOT_WATERMARK_COMPLIANT = 100;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int TESTING_WATERMARK_COMPLIANT = 101;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int TVSEARCH = 13;
        @NanoEnumValue(legacy = false, value = Munger.class)
        public static final int WASP = 5;
    }
}
