// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.decoder;

final class Dct
{
    private static final float A1;
    private static final float A2;
    private static final float A3;
    private static final float A4;
    private static final float A5;
    private static final float C2;
    private static final float C4;
    private static final float C6;
    private static final float[] DCT_SCALING_FACTORS;
    private static final float[] IDCT_SCALING_FACTORS;
    private static final float Q;
    private static final float R;
    
    static {
        DCT_SCALING_FACTORS = new float[] { (float)(0.5 / Math.sqrt(2.0)), (float)(0.25 / Math.cos(0.19634954084936207)), (float)(0.25 / Math.cos(0.39269908169872414)), (float)(0.25 / Math.cos(0.5890486225480862)), (float)(0.25 / Math.cos(0.7853981633974483)), (float)(0.25 / Math.cos(0.9817477042468103)), (float)(0.25 / Math.cos(1.1780972450961724)), (float)(0.25 / Math.cos(1.3744467859455345)) };
        IDCT_SCALING_FACTORS = new float[] { (float)(8.0 / Math.sqrt(2.0) * 0.0625), (float)(Math.cos(0.19634954084936207) * 4.0 * 0.125), (float)(Math.cos(0.39269908169872414) * 4.0 * 0.125), (float)(Math.cos(0.5890486225480862) * 4.0 * 0.125), (float)(Math.cos(0.7853981633974483) * 4.0 * 0.125), (float)(Math.cos(0.9817477042468103) * 4.0 * 0.125), (float)(Math.cos(1.1780972450961724) * 4.0 * 0.125), (float)(4.0 * Math.cos(1.3744467859455345) * 0.125) };
        A1 = (float)Math.cos(0.7853981633974483);
        A2 = (float)(Math.cos(0.39269908169872414) - Math.cos(1.1780972450961724));
        A3 = Dct.A1;
        A4 = (float)(Math.cos(0.39269908169872414) + Math.cos(1.1780972450961724));
        A5 = (float)Math.cos(1.1780972450961724);
        C2 = (float)(Math.cos(0.39269908169872414) * 2.0);
        C4 = (float)(Math.cos(0.7853981633974483) * 2.0);
        C6 = (float)(2.0 * Math.cos(1.1780972450961724));
        Q = Dct.C2 - Dct.C6;
        R = Dct.C2 + Dct.C6;
    }
    
    private Dct() {
    }
    
    public static void forwardDCT8(final float[] array) {
        final float n = array[0] + array[7];
        final float n2 = array[1] + array[6];
        final float n3 = array[2] + array[5];
        final float n4 = array[3] + array[4];
        final float n5 = array[3];
        final float n6 = array[4];
        final float n7 = array[2] - array[5];
        final float n8 = array[1] - array[6];
        final float n9 = array[0] - array[7];
        final float n10 = n + n4;
        final float n11 = n2 + n3;
        final float n12 = n - n4;
        final float n13 = n5 - n6 + n7;
        final float n14 = n8 + n9;
        final float n15 = (n2 - n3 + n12) * Dct.A1;
        final float n16 = (n14 - n13) * Dct.A5;
        final float n17 = n13 * Dct.A2 - n16;
        final float n18 = (n7 + n8) * Dct.A3;
        final float n19 = n14 * Dct.A4 - n16;
        final float n20 = n9 + n18;
        final float n21 = n9 - n18;
        array[0] = n10 + n11;
        array[4] = n10 - n11;
        array[2] = n12 + n15;
        array[6] = n12 - n15;
        array[5] = n21 + n17;
        array[1] = n20 + n19;
        array[7] = n20 - n19;
        array[3] = n21 - n17;
    }
    
    public static void forwardDCT8x8(final float[] array) {
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= 8) {
                break;
            }
            final int n3 = 8 * n2;
            final float n4 = array[n3];
            final int n5 = n3 + 7;
            final float n6 = n4 + array[n5];
            final int n7 = n3 + 1;
            final float n8 = array[n7];
            final int n9 = n3 + 6;
            final float n10 = n8 + array[n9];
            final int n11 = n3 + 2;
            final float n12 = array[n11];
            final int n13 = n3 + 5;
            final float n14 = n12 + array[n13];
            final int n15 = n3 + 3;
            final float n16 = array[n15];
            final int n17 = n3 + 4;
            final float n18 = n16 + array[n17];
            final float n19 = array[n15];
            final float n20 = array[n17];
            final float n21 = array[n11] - array[n13];
            final float n22 = array[n7] - array[n9];
            final float n23 = array[n3] - array[n5];
            final float n24 = n6 + n18;
            final float n25 = n10 + n14;
            final float n26 = n6 - n18;
            final float n27 = n19 - n20 + n21;
            final float n28 = n22 + n23;
            final float n29 = (n10 - n14 + n26) * Dct.A1;
            final float n30 = (n28 - n27) * Dct.A5;
            final float n31 = n27 * Dct.A2 - n30;
            final float n32 = (n21 + n22) * Dct.A3;
            final float n33 = n28 * Dct.A4 - n30;
            final float n34 = n23 + n32;
            final float n35 = n23 - n32;
            array[n3] = n24 + n25;
            array[n17] = n24 - n25;
            array[n11] = n26 + n29;
            array[n9] = n26 - n29;
            array[n13] = n35 + n31;
            array[n7] = n34 + n33;
            array[n5] = n34 - n33;
            array[n15] = n35 - n31;
            ++n2;
        }
        while (i < 8) {
            final float n36 = array[i];
            final int n37 = 56 + i;
            final float n38 = n36 + array[n37];
            final int n39 = 8 + i;
            final float n40 = array[n39];
            final int n41 = 48 + i;
            final float n42 = n40 + array[n41];
            final int n43 = 16 + i;
            final float n44 = array[n43];
            final int n45 = 40 + i;
            final float n46 = n44 + array[n45];
            final int n47 = 24 + i;
            final float n48 = array[n47];
            final int n49 = 32 + i;
            final float n50 = n48 + array[n49];
            final float n51 = array[n47];
            final float n52 = array[n49];
            final float n53 = array[n43] - array[n45];
            final float n54 = array[n39] - array[n41];
            final float n55 = array[i] - array[n37];
            final float n56 = n38 + n50;
            final float n57 = n42 + n46;
            final float n58 = n38 - n50;
            final float n59 = n51 - n52 + n53;
            final float n60 = n54 + n55;
            final float n61 = (n42 - n46 + n58) * Dct.A1;
            final float n62 = (n60 - n59) * Dct.A5;
            final float n63 = n59 * Dct.A2 - n62;
            final float n64 = (n53 + n54) * Dct.A3;
            final float n65 = n60 * Dct.A4 - n62;
            final float n66 = n55 + n64;
            final float n67 = n55 - n64;
            array[i] = n56 + n57;
            array[n49] = n56 - n57;
            array[n43] = n58 + n61;
            array[n41] = n58 - n61;
            array[n45] = n67 + n63;
            array[n39] = n66 + n65;
            array[n37] = n66 - n65;
            array[n47] = n67 - n63;
            ++i;
        }
    }
    
    public static void inverseDCT8(final float[] array) {
        final float n = array[2];
        final float n2 = array[6];
        final float n3 = array[2] + array[6];
        final float n4 = array[5] - array[3];
        final float n5 = array[1] + array[7];
        final float n6 = array[3] + array[5];
        final float n7 = array[1] - array[7];
        final float n8 = n5 + n6;
        final float n9 = Dct.C6 * (n4 + n7);
        final float q = Dct.Q;
        final float r = Dct.R;
        final float c4 = Dct.C4;
        final float c5 = Dct.C4;
        final float n10 = r * n7 - n9 - n8;
        final float n11 = n10 - (n5 - n6) * c5;
        final float n12 = array[0] - array[4];
        final float n13 = (n - n2) * c4 - n3;
        final float n14 = array[0] + array[4];
        final float n15 = n12 + n13;
        final float n16 = n14 + n3;
        final float n17 = n12 - n13;
        final float n18 = n14 - n3;
        final float n19 = q * n4 + n9 + n11;
        array[0] = n16 + n8;
        array[1] = n15 + n10;
        array[2] = n17 - n11;
        array[3] = n18 + n19;
        array[4] = n18 - n19;
        array[5] = n17 + n11;
        array[6] = n15 - n10;
        array[7] = n16 - n8;
    }
    
    public static void inverseDCT8x8(final float[] array) {
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= 8) {
                break;
            }
            final int n3 = 8 * n2;
            final int n4 = n3 + 2;
            final float n5 = array[n4];
            final int n6 = n3 + 6;
            final float n7 = array[n6];
            final float n8 = array[n4] + array[n6];
            final int n9 = n3 + 5;
            final float n10 = array[n9];
            final int n11 = n3 + 3;
            final float n12 = n10 - array[n11];
            final int n13 = n3 + 1;
            final float n14 = array[n13];
            final int n15 = n3 + 7;
            final float n16 = n14 + array[n15];
            final float n17 = array[n11] + array[n9];
            final float n18 = array[n13] - array[n15];
            final float n19 = n16 + n17;
            final float n20 = Dct.C6 * (n12 + n18);
            final float q = Dct.Q;
            final float r = Dct.R;
            final float c4 = Dct.C4;
            final float c5 = Dct.C4;
            final float n21 = r * n18 - n20 - n19;
            final float n22 = n21 - (n16 - n17) * c5;
            final float n23 = array[n3];
            final int n24 = n3 + 4;
            final float n25 = n23 - array[n24];
            final float n26 = (n5 - n7) * c4 - n8;
            final float n27 = array[n3] + array[n24];
            final float n28 = n25 + n26;
            final float n29 = n27 + n8;
            final float n30 = n25 - n26;
            final float n31 = n27 - n8;
            final float n32 = q * n12 + n20 + n22;
            array[n3] = n29 + n19;
            array[n13] = n28 + n21;
            array[n4] = n30 - n22;
            array[n11] = n31 + n32;
            array[n24] = n31 - n32;
            array[n9] = n30 + n22;
            array[n6] = n28 - n21;
            array[n15] = n29 - n19;
            ++n2;
        }
        while (i < 8) {
            final int n33 = 16 + i;
            final float n34 = array[n33];
            final int n35 = 48 + i;
            final float n36 = array[n35];
            final float n37 = array[n33] + array[n35];
            final int n38 = 40 + i;
            final float n39 = array[n38];
            final int n40 = 24 + i;
            final float n41 = n39 - array[n40];
            final int n42 = 8 + i;
            final float n43 = array[n42];
            final int n44 = 56 + i;
            final float n45 = n43 + array[n44];
            final float n46 = array[n40] + array[n38];
            final float n47 = array[n42] - array[n44];
            final float n48 = n45 + n46;
            final float n49 = Dct.C6 * (n41 + n47);
            final float q2 = Dct.Q;
            final float r2 = Dct.R;
            final float c6 = Dct.C4;
            final float c7 = Dct.C4;
            final float n50 = r2 * n47 - n49 - n48;
            final float n51 = n50 - (n45 - n46) * c7;
            final float n52 = array[i];
            final int n53 = 32 + i;
            final float n54 = n52 - array[n53];
            final float n55 = (n34 - n36) * c6 - n37;
            final float n56 = array[i] + array[n53];
            final float n57 = n54 + n55;
            final float n58 = n56 + n37;
            final float n59 = n54 - n55;
            final float n60 = n56 - n37;
            final float n61 = q2 * n41 + n49 + n51;
            array[i] = n58 + n48;
            array[n42] = n57 + n50;
            array[n33] = n59 - n51;
            array[n40] = n60 + n61;
            array[n53] = n60 - n61;
            array[n38] = n59 + n51;
            array[n35] = n57 - n50;
            array[n44] = n58 - n48;
            ++i;
        }
    }
    
    public static void scaleDequantizationMatrix(final float[] array) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                final int n = 8 * i + j;
                array[n] *= Dct.IDCT_SCALING_FACTORS[i] * Dct.IDCT_SCALING_FACTORS[j];
            }
        }
    }
    
    public static void scaleDequantizationVector(final float[] array) {
        for (int i = 0; i < 8; ++i) {
            array[i] *= Dct.IDCT_SCALING_FACTORS[i];
        }
    }
    
    public static void scaleQuantizationMatrix(final float[] array) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                final int n = 8 * i + j;
                array[n] *= Dct.DCT_SCALING_FACTORS[i] * Dct.DCT_SCALING_FACTORS[j];
            }
        }
    }
    
    public static void scaleQuantizationVector(final float[] array) {
        for (int i = 0; i < 8; ++i) {
            array[i] *= Dct.DCT_SCALING_FACTORS[i];
        }
    }
}
