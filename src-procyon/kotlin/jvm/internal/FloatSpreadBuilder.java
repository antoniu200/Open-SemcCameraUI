// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\u0002J\f\u0010\f\u001a\u00020\u0004*\u00020\u0002H\u0014R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r" }, d2 = { "Lkotlin/jvm/internal/FloatSpreadBuilder;", "Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "", "size", "", "(I)V", "values", "add", "", "value", "", "toArray", "getSize", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class FloatSpreadBuilder extends PrimitiveSpreadBuilder<float[]>
{
    private final float[] values;
    
    public FloatSpreadBuilder(final int n) {
        super(n);
        this.values = new float[n];
    }
    
    public final void add(final float n) {
        final float[] values = this.values;
        final int position = this.getPosition();
        this.setPosition(position + 1);
        values[position] = n;
    }
    
    @Override
    protected int getSize(@NotNull final float[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return array.length;
    }
    
    @NotNull
    public final float[] toArray() {
        return this.toArray(this.values, new float[this.size()]);
    }
}
