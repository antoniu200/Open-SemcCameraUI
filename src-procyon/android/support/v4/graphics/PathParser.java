// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics;

import android.util.Log;
import android.graphics.Path;
import java.util.ArrayList;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class PathParser
{
    private static final String LOGTAG = "PathParser";
    
    private PathParser() {
    }
    
    private static void addNode(final ArrayList<PathDataNode> list, final char c, final float[] array) {
        list.add(new PathDataNode(c, array));
    }
    
    public static boolean canMorph(final PathDataNode[] array, final PathDataNode[] array2) {
        if (array == null || array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i].mType != array2[i].mType || array[i].mParams.length != array2[i].mParams.length) {
                return false;
            }
        }
        return true;
    }
    
    static float[] copyOfRange(final float[] array, final int n, int a) {
        if (n > a) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n >= 0 && n <= length) {
            a -= n;
            final int min = Math.min(a, length - n);
            final float[] array2 = new float[a];
            System.arraycopy(array, n, array2, 0, min);
            return array2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public static PathDataNode[] createNodesFromPathData(final String s) {
        if (s == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        int i = 1;
        int n = 0;
        while (i < s.length()) {
            i = nextStart(s, i);
            final String trim = s.substring(n, i).trim();
            if (trim.length() > 0) {
                addNode(list, trim.charAt(0), getFloats(trim));
            }
            n = i;
            ++i;
        }
        if (i - n == 1 && n < s.length()) {
            addNode(list, s.charAt(n), new float[0]);
        }
        return list.toArray(new PathDataNode[list.size()]);
    }
    
    public static Path createPathFromPathData(final String str) {
        final Path path = new Path();
        final PathDataNode[] nodesFromPathData = createNodesFromPathData(str);
        if (nodesFromPathData != null) {
            try {
                PathDataNode.nodesToPath(nodesFromPathData, path);
                return path;
            }
            catch (final RuntimeException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error in parsing ");
                sb.append(str);
                throw new RuntimeException(sb.toString(), cause);
            }
        }
        return null;
    }
    
    public static PathDataNode[] deepCopyNodes(final PathDataNode[] array) {
        if (array == null) {
            return null;
        }
        final PathDataNode[] array2 = new PathDataNode[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new PathDataNode(array[i]);
        }
        return array2;
    }
    
    private static void extract(final String s, final int n, final ExtractFloatResult extractFloatResult) {
        extractFloatResult.mEndWithNegOrDot = false;
        int i = n;
        int n2 = 0;
        boolean b;
        int n3 = (b = false) ? 1 : 0;
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            Label_0141: {
                Label_0136: {
                    if (char1 != ' ') {
                        if (char1 != 'E' && char1 != 'e') {
                            switch (char1) {
                                case 46: {
                                    if (n3 == 0) {
                                        n2 = 0;
                                        n3 = 1;
                                        break Label_0141;
                                    }
                                    extractFloatResult.mEndWithNegOrDot = true;
                                    break Label_0136;
                                }
                                case 45: {
                                    if (i != n && n2 == 0) {
                                        extractFloatResult.mEndWithNegOrDot = true;
                                        break Label_0136;
                                    }
                                    break;
                                }
                                case 44: {
                                    break Label_0136;
                                }
                            }
                            n2 = 0;
                            break Label_0141;
                        }
                        n2 = 1;
                        break Label_0141;
                    }
                }
                n2 = 0;
                b = true;
            }
            if (b) {
                break;
            }
            ++i;
        }
        extractFloatResult.mEndPosition = i;
    }
    
    private static float[] getFloats(final String str) {
        if (str.charAt(0) != 'z') {
            if (str.charAt(0) != 'Z') {
                try {
                    final float[] array = new float[str.length()];
                    final ExtractFloatResult extractFloatResult = new ExtractFloatResult();
                    final int length = str.length();
                    int i = 1;
                    int n = 0;
                    while (i < length) {
                        extract(str, i, extractFloatResult);
                        final int mEndPosition = extractFloatResult.mEndPosition;
                        int n2 = n;
                        if (i < mEndPosition) {
                            array[n] = Float.parseFloat(str.substring(i, mEndPosition));
                            n2 = n + 1;
                        }
                        if (extractFloatResult.mEndWithNegOrDot) {
                            i = mEndPosition;
                            n = n2;
                        }
                        else {
                            i = mEndPosition + 1;
                            n = n2;
                        }
                    }
                    return copyOfRange(array, 0, n);
                }
                catch (final NumberFormatException cause) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("error in parsing \"");
                    sb.append(str);
                    sb.append("\"");
                    throw new RuntimeException(sb.toString(), cause);
                }
            }
        }
        return new float[0];
    }
    
    private static int nextStart(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (((char1 - 'A') * (char1 - 'Z') <= 0 || (char1 - 'a') * (char1 - 'z') <= 0) && char1 != 'e' && char1 != 'E') {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    public static void updateNodes(final PathDataNode[] array, final PathDataNode[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array[i].mType = array2[i].mType;
            for (int j = 0; j < array2[i].mParams.length; ++j) {
                array[i].mParams[j] = array2[i].mParams[j];
            }
        }
    }
    
    private static class ExtractFloatResult
    {
        int mEndPosition;
        boolean mEndWithNegOrDot;
        
        ExtractFloatResult() {
        }
    }
    
    public static class PathDataNode
    {
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public float[] mParams;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public char mType;
        
        PathDataNode(final char mType, final float[] mParams) {
            this.mType = mType;
            this.mParams = mParams;
        }
        
        PathDataNode(final PathDataNode pathDataNode) {
            this.mType = pathDataNode.mType;
            this.mParams = PathParser.copyOfRange(pathDataNode.mParams, 0, pathDataNode.mParams.length);
        }
        
        private static void addCommand(final Path path, final float[] array, final char c, final char c2, final float[] array2) {
            final float n = array[0];
            final float n2 = array[1];
            final float n3 = array[2];
            final float n4 = array[3];
            float n5 = array[4];
            float n6 = array[5];
            float n7 = n;
            float n8 = n2;
            float n9 = n3;
            float n10 = n4;
            int n11 = 0;
            Label_0340: {
                switch (c2) {
                    case 'Z':
                    case 'z': {
                        path.close();
                        path.moveTo(n5, n6);
                        n7 = (n9 = n5);
                        n8 = (n10 = n6);
                    }
                    default: {
                        n10 = n4;
                        n9 = n3;
                        n8 = n2;
                        n7 = n;
                    }
                    case 'L':
                    case 'M':
                    case 'T':
                    case 'l':
                    case 'm':
                    case 't': {
                        n11 = 2;
                        break Label_0340;
                    }
                    case 'Q':
                    case 'S':
                    case 'q':
                    case 's': {
                        n11 = 4;
                        n7 = n;
                        n8 = n2;
                        n9 = n3;
                        n10 = n4;
                        break Label_0340;
                    }
                    case 'H':
                    case 'V':
                    case 'h':
                    case 'v': {
                        n11 = 1;
                        n7 = n;
                        n8 = n2;
                        n9 = n3;
                        n10 = n4;
                        break Label_0340;
                    }
                    case 'C':
                    case 'c': {
                        n11 = 6;
                        break;
                    }
                    case 'A':
                    case 'a': {
                        n11 = 7;
                        break;
                    }
                }
                n10 = n4;
                n9 = n3;
                n8 = n2;
                n7 = n;
            }
            final int n12 = 0;
            char c3 = c;
        Label_2154:
            for (int i = n12; i < array2.length; i += n11, c3 = c2) {
                final float n13 = 0.0f;
                final float n14 = 0.0f;
                Label_2146: {
                    float n80 = 0.0f;
                    float n81 = 0.0f;
                    Label_1758: {
                        while (true) {
                            float n32 = 0.0f;
                            float n33 = 0.0f;
                            Label_1207: {
                                switch (c2) {
                                    case 'v': {
                                        final int n15 = i + 0;
                                        path.rLineTo(0.0f, array2[n15]);
                                        n8 += array2[n15];
                                        break;
                                    }
                                    case 't': {
                                        float n16;
                                        float n17;
                                        if (c3 != 'q' && c3 != 't' && c3 != 'Q' && c3 != 'T') {
                                            n16 = 0.0f;
                                            n17 = n14;
                                        }
                                        else {
                                            n17 = n7 - n9;
                                            n16 = n8 - n10;
                                        }
                                        final int n18 = i + 0;
                                        final float n19 = array2[n18];
                                        final int n20 = i + 1;
                                        path.rQuadTo(n17, n16, n19, array2[n20]);
                                        final float n21 = n7 + array2[n18];
                                        final float n22 = n8 + array2[n20];
                                        n10 = n16 + n8;
                                        n9 = n17 + n7;
                                        n8 = n22;
                                        n7 = n21;
                                        break;
                                    }
                                    case 's': {
                                        float n23;
                                        float n24;
                                        if (c3 != 'c' && c3 != 's' && c3 != 'C' && c3 != 'S') {
                                            n23 = 0.0f;
                                            n24 = n13;
                                        }
                                        else {
                                            n23 = n8 - n10;
                                            n24 = n7 - n9;
                                        }
                                        final int n25 = i + 0;
                                        final float n26 = array2[n25];
                                        final int n27 = i + 1;
                                        final float n28 = array2[n27];
                                        final int n29 = i + 2;
                                        final float n30 = array2[n29];
                                        final int n31 = i + 3;
                                        path.rCubicTo(n24, n23, n26, n28, n30, array2[n31]);
                                        n32 = array2[n25] + n7;
                                        n33 = array2[n27] + n8;
                                        n7 += array2[n29];
                                        n8 += array2[n31];
                                        break Label_1207;
                                    }
                                    case 'q': {
                                        final int n34 = i + 0;
                                        final float n35 = array2[n34];
                                        final int n36 = i + 1;
                                        final float n37 = array2[n36];
                                        final int n38 = i + 2;
                                        final float n39 = array2[n38];
                                        final int n40 = i + 3;
                                        path.rQuadTo(n35, n37, n39, array2[n40]);
                                        n32 = array2[n34] + n7;
                                        n33 = array2[n36] + n8;
                                        n7 += array2[n38];
                                        n8 += array2[n40];
                                        break Label_1207;
                                    }
                                    case 'm': {
                                        final int n41 = i + 0;
                                        n7 += array2[n41];
                                        final int n42 = i + 1;
                                        n8 += array2[n42];
                                        if (i > 0) {
                                            path.rLineTo(array2[n41], array2[n42]);
                                            break;
                                        }
                                        path.rMoveTo(array2[n41], array2[n42]);
                                        n6 = n8;
                                        n5 = n7;
                                        break;
                                    }
                                    case 'l': {
                                        final int n43 = i + 0;
                                        final float n44 = array2[n43];
                                        final int n45 = i + 1;
                                        path.rLineTo(n44, array2[n45]);
                                        n7 += array2[n43];
                                        n8 += array2[n45];
                                        break;
                                    }
                                    case 'h': {
                                        final int n46 = i + 0;
                                        path.rLineTo(array2[n46], 0.0f);
                                        n7 += array2[n46];
                                        break;
                                    }
                                    case 'c': {
                                        final float n47 = array2[i + 0];
                                        final float n48 = array2[i + 1];
                                        final int n49 = i + 2;
                                        final float n50 = array2[n49];
                                        final int n51 = i + 3;
                                        final float n52 = array2[n51];
                                        final int n53 = i + 4;
                                        final float n54 = array2[n53];
                                        final int n55 = i + 5;
                                        path.rCubicTo(n47, n48, n50, n52, n54, array2[n55]);
                                        n32 = array2[n49] + n7;
                                        n33 = array2[n51] + n8;
                                        n7 += array2[n53];
                                        n8 += array2[n55];
                                        break Label_1207;
                                    }
                                    case 'a': {
                                        final int n56 = i + 5;
                                        final float n57 = array2[n56];
                                        final int n58 = i + 6;
                                        drawArc(path, n7, n8, n57 + n7, array2[n58] + n8, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                                        n7 += array2[n56];
                                        n8 += array2[n58];
                                        break Label_2146;
                                    }
                                    case 'V': {
                                        final int n59 = i + 0;
                                        path.lineTo(n7, array2[n59]);
                                        n8 = array2[n59];
                                        break;
                                    }
                                    case 'T': {
                                        final float n60 = n8;
                                        final float n61 = n7;
                                        float n62 = 0.0f;
                                        float n63 = 0.0f;
                                        Label_1450: {
                                            if (c3 != 'q' && c3 != 't' && c3 != 'Q') {
                                                n62 = n61;
                                                n63 = n60;
                                                if (c3 != 'T') {
                                                    break Label_1450;
                                                }
                                            }
                                            n63 = 2.0f * n60 - n10;
                                            n62 = 2.0f * n61 - n9;
                                        }
                                        final int n64 = i + 0;
                                        final float n65 = array2[n64];
                                        final int n66 = i + 1;
                                        path.quadTo(n62, n63, n65, array2[n66]);
                                        final float n67 = array2[n64];
                                        final float n68 = array2[n66];
                                        n9 = n62;
                                        n10 = n63;
                                        n8 = n68;
                                        n7 = n67;
                                        break;
                                    }
                                    case 'S': {
                                        float n70;
                                        float n71;
                                        if (c3 != 'c' && c3 != 's' && c3 != 'C' && c3 != 'S') {
                                            final float n69 = n7;
                                            n70 = n8;
                                            n71 = n69;
                                        }
                                        else {
                                            final float n72 = 2.0f * n7 - n9;
                                            n70 = 2.0f * n8 - n10;
                                            n71 = n72;
                                        }
                                        final int n73 = i + 0;
                                        final float n74 = array2[n73];
                                        final int n75 = i + 1;
                                        final float n76 = array2[n75];
                                        final int n77 = i + 2;
                                        final float n78 = array2[n77];
                                        final int n79 = i + 3;
                                        path.cubicTo(n71, n70, n74, n76, n78, array2[n79]);
                                        n80 = array2[n73];
                                        n81 = array2[n75];
                                        n7 = array2[n77];
                                        n8 = array2[n79];
                                        break Label_1758;
                                    }
                                    case 'Q': {
                                        final int n82 = i + 0;
                                        final float n83 = array2[n82];
                                        final int n84 = i + 1;
                                        final float n85 = array2[n84];
                                        final int n86 = i + 2;
                                        final float n87 = array2[n86];
                                        final int n88 = i + 3;
                                        path.quadTo(n83, n85, n87, array2[n88]);
                                        n80 = array2[n82];
                                        n81 = array2[n84];
                                        n7 = array2[n86];
                                        n8 = array2[n88];
                                        break Label_1758;
                                    }
                                    case 'M': {
                                        final int n89 = i + 0;
                                        n7 = array2[n89];
                                        final int n90 = i + 1;
                                        n8 = array2[n90];
                                        if (i > 0) {
                                            path.lineTo(array2[n89], array2[n90]);
                                            break;
                                        }
                                        path.moveTo(array2[n89], array2[n90]);
                                        n6 = n8;
                                        n5 = n7;
                                        break;
                                    }
                                    case 'L': {
                                        final int n91 = i + 0;
                                        final float n92 = array2[n91];
                                        final int n93 = i + 1;
                                        path.lineTo(n92, array2[n93]);
                                        n7 = array2[n91];
                                        n8 = array2[n93];
                                        break;
                                    }
                                    case 'H': {
                                        final int n94 = i + 0;
                                        path.lineTo(array2[n94], n8);
                                        n7 = array2[n94];
                                        break;
                                    }
                                    case 'C': {
                                        final float n95 = array2[i + 0];
                                        final float n96 = array2[i + 1];
                                        final int n97 = i + 2;
                                        final float n98 = array2[n97];
                                        final int n99 = i + 3;
                                        final float n100 = array2[n99];
                                        final int n101 = i + 4;
                                        final float n102 = array2[n101];
                                        final int n103 = i + 5;
                                        path.cubicTo(n95, n96, n98, n100, n102, array2[n103]);
                                        n7 = array2[n101];
                                        n8 = array2[n103];
                                        n9 = array2[n97];
                                        n10 = array2[n99];
                                        break;
                                    }
                                    case 'A': {
                                        final int n104 = i + 5;
                                        final float n105 = array2[n104];
                                        final int n106 = i + 6;
                                        drawArc(path, n7, n8, n105, array2[n106], array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                                        n7 = array2[n104];
                                        n8 = array2[n106];
                                        break Label_2146;
                                    }
                                }
                                continue Label_2154;
                            }
                            final float n107 = n32;
                            n10 = n33;
                            n9 = n107;
                            continue;
                        }
                    }
                    final float n108 = n81;
                    n9 = n80;
                    n10 = n108;
                    continue;
                }
                n10 = n8;
                n9 = n7;
            }
            array[0] = n7;
            array[1] = n8;
            array[2] = n9;
            array[3] = n10;
            array[4] = n5;
            array[5] = n6;
        }
        
        private static void arcToBezier(final Path path, final double n, final double n2, final double n3, double n4, double n5, double n6, double sin, double n7, double n8) {
            final int n9 = (int)Math.ceil(Math.abs(n8 * 4.0 / 3.141592653589793));
            final double cos = Math.cos(sin);
            final double sin2 = Math.sin(sin);
            final double cos2 = Math.cos(n7);
            sin = Math.sin(n7);
            final double n10 = -n3;
            final double n11 = n10 * cos;
            final double n12 = n4 * sin2;
            final double n13 = n10 * sin2;
            final double n14 = n4 * cos;
            n8 /= n9;
            int i = 0;
            n4 = sin * n13 + cos2 * n14;
            double n15 = n11 * sin - n12 * cos2;
            double n16 = n7;
            n7 = n4;
            double n17 = n6;
            sin = n5;
            n4 = sin2;
            n5 = cos;
            final double n18 = n13;
            n6 = n8;
            n8 = n14;
            while (i < n9) {
                final double n19 = n16 + n6;
                final double sin3 = Math.sin(n19);
                final double cos3 = Math.cos(n19);
                final double n20 = n + n3 * n5 * cos3 - n12 * sin3;
                final double n21 = n2 + n3 * n4 * cos3 + n8 * sin3;
                final double n22 = n11 * sin3 - n12 * cos3;
                final double n23 = sin3 * n18 + cos3 * n8;
                final double a = n19 - n16;
                final double tan = Math.tan(a / 2.0);
                final double n24 = Math.sin(a) * (Math.sqrt(4.0 + 3.0 * tan * tan) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(sin + n15 * n24), (float)(n17 + n7 * n24), (float)(n20 - n24 * n22), (float)(n21 - n24 * n23), (float)n20, (float)n21);
                ++i;
                n17 = n21;
                sin = n20;
                n7 = n23;
                n15 = n22;
                n16 = n19;
            }
        }
        
        private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
            final double radians = Math.toRadians(n7);
            final double cos = Math.cos(radians);
            final double sin = Math.sin(radians);
            final double n8 = n;
            final double n9 = n2;
            final double n10 = n5;
            final double n11 = (n8 * cos + n9 * sin) / n10;
            final double n12 = -n;
            final double n13 = n6;
            final double n14 = (n12 * sin + n9 * cos) / n13;
            final double n15 = n3;
            final double n16 = n4;
            final double n17 = (n15 * cos + n16 * sin) / n10;
            final double n18 = (-n3 * sin + n16 * cos) / n13;
            final double n19 = n11 - n17;
            final double n20 = n14 - n18;
            final double n21 = (n11 + n17) / 2.0;
            final double n22 = (n14 + n18) / 2.0;
            final double n23 = n19 * n19 + n20 * n20;
            if (n23 == 0.0) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            final double a = 1.0 / n23 - 0.25;
            if (a < 0.0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Points are too far apart ");
                sb.append(n23);
                Log.w("PathParser", sb.toString());
                final float n24 = (float)(Math.sqrt(n23) / 1.99999);
                drawArc(path, n, n2, n3, n4, n5 * n24, n6 * n24, n7, b, b2);
                return;
            }
            final double sqrt = Math.sqrt(a);
            final double n25 = n19 * sqrt;
            final double n26 = sqrt * n20;
            double n27;
            double n28;
            if (b == b2) {
                n27 = n21 - n26;
                n28 = n22 + n25;
            }
            else {
                n27 = n21 + n26;
                n28 = n22 - n25;
            }
            final double atan2 = Math.atan2(n14 - n28, n11 - n27);
            final double n29 = Math.atan2(n18 - n28, n17 - n27) - atan2;
            final double n30 = dcmpl(n29, 0.0);
            final boolean b3 = n30 >= 0;
            double n31 = n29;
            if (b2 != b3) {
                if (n30 > 0) {
                    n31 = n29 - 6.283185307179586;
                }
                else {
                    n31 = n29 + 6.283185307179586;
                }
            }
            final double n32 = n27 * n10;
            final double n33 = n28 * n13;
            arcToBezier(path, n32 * cos - n33 * sin, n32 * sin + n33 * cos, n10, n13, n8, n9, radians, atan2, n31);
        }
        
        public static void nodesToPath(final PathDataNode[] array, final Path path) {
            final float[] array2 = new float[6];
            char mType = 'm';
            for (int i = 0; i < array.length; ++i) {
                addCommand(path, array2, mType, array[i].mType, array[i].mParams);
                mType = array[i].mType;
            }
        }
        
        public void interpolatePathDataNode(final PathDataNode pathDataNode, final PathDataNode pathDataNode2, final float n) {
            for (int i = 0; i < pathDataNode.mParams.length; ++i) {
                this.mParams[i] = pathDataNode.mParams[i] * (1.0f - n) + pathDataNode2.mParams[i] * n;
            }
        }
    }
}
