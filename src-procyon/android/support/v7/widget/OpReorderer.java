// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import java.util.List;

class OpReorderer
{
    final Callback mCallback;
    
    OpReorderer(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    private int getLastMoveOutOfOrder(final List<AdapterHelper.UpdateOp> list) {
        int i = list.size() - 1;
        int n = 0;
        while (i >= 0) {
            int n2;
            if (((AdapterHelper.UpdateOp)list.get(i)).cmd == 8) {
                if ((n2 = n) != 0) {
                    return i;
                }
            }
            else {
                n2 = 1;
            }
            --i;
            n = n2;
        }
        return -1;
    }
    
    private void swapMoveAdd(final List<AdapterHelper.UpdateOp> list, final int n, final AdapterHelper.UpdateOp updateOp, final int n2, final AdapterHelper.UpdateOp updateOp2) {
        int n3;
        if (updateOp.itemCount < updateOp2.positionStart) {
            n3 = -1;
        }
        else {
            n3 = 0;
        }
        int n4 = n3;
        if (updateOp.positionStart < updateOp2.positionStart) {
            n4 = n3 + 1;
        }
        if (updateOp2.positionStart <= updateOp.positionStart) {
            updateOp.positionStart += updateOp2.itemCount;
        }
        if (updateOp2.positionStart <= updateOp.itemCount) {
            updateOp.itemCount += updateOp2.itemCount;
        }
        updateOp2.positionStart += n4;
        list.set(n, updateOp2);
        list.set(n2, updateOp);
    }
    
    private void swapMoveOp(final List<AdapterHelper.UpdateOp> list, final int n, final int n2) {
        final AdapterHelper.UpdateOp updateOp = list.get(n);
        final AdapterHelper.UpdateOp updateOp2 = list.get(n2);
        final int cmd = updateOp2.cmd;
        if (cmd != 4) {
            switch (cmd) {
                case 2: {
                    this.swapMoveRemove(list, n, updateOp, n2, updateOp2);
                    break;
                }
                case 1: {
                    this.swapMoveAdd(list, n, updateOp, n2, updateOp2);
                    break;
                }
            }
        }
        else {
            this.swapMoveUpdate(list, n, updateOp, n2, updateOp2);
        }
    }
    
    void reorderOps(final List<AdapterHelper.UpdateOp> list) {
        while (true) {
            final int lastMoveOutOfOrder = this.getLastMoveOutOfOrder(list);
            if (lastMoveOutOfOrder == -1) {
                break;
            }
            this.swapMoveOp(list, lastMoveOutOfOrder, lastMoveOutOfOrder + 1);
        }
    }
    
    void swapMoveRemove(final List<AdapterHelper.UpdateOp> list, final int n, final AdapterHelper.UpdateOp updateOp, final int n2, final AdapterHelper.UpdateOp updateOp2) {
        final int positionStart = updateOp.positionStart;
        final int itemCount = updateOp.itemCount;
        boolean b = false;
        boolean b2;
        if (positionStart < itemCount) {
            if (updateOp2.positionStart == updateOp.positionStart && updateOp2.itemCount == updateOp.itemCount - updateOp.positionStart) {
                b2 = false;
                b = true;
            }
            else {
                b2 = false;
            }
        }
        else if (updateOp2.positionStart == updateOp.itemCount + 1 && updateOp2.itemCount == updateOp.positionStart - updateOp.itemCount) {
            b2 = true;
            b = true;
        }
        else {
            b2 = true;
        }
        if (updateOp.itemCount < updateOp2.positionStart) {
            --updateOp2.positionStart;
        }
        else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
            --updateOp2.itemCount;
            updateOp.cmd = 2;
            updateOp.itemCount = 1;
            if (updateOp2.itemCount == 0) {
                list.remove(n2);
                this.mCallback.recycleUpdateOp(updateOp2);
            }
            return;
        }
        final int positionStart2 = updateOp.positionStart;
        final int positionStart3 = updateOp2.positionStart;
        AdapterHelper.UpdateOp obtainUpdateOp = null;
        if (positionStart2 <= positionStart3) {
            ++updateOp2.positionStart;
        }
        else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            obtainUpdateOp = this.mCallback.obtainUpdateOp(2, updateOp.positionStart + 1, updateOp2.positionStart + updateOp2.itemCount - updateOp.positionStart, null);
            updateOp2.itemCount = updateOp.positionStart - updateOp2.positionStart;
        }
        if (b) {
            list.set(n, updateOp2);
            list.remove(n2);
            this.mCallback.recycleUpdateOp(updateOp);
            return;
        }
        if (b2) {
            if (obtainUpdateOp != null) {
                if (updateOp.positionStart > obtainUpdateOp.positionStart) {
                    updateOp.positionStart -= obtainUpdateOp.itemCount;
                }
                if (updateOp.itemCount > obtainUpdateOp.positionStart) {
                    updateOp.itemCount -= obtainUpdateOp.itemCount;
                }
            }
            if (updateOp.positionStart > updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount > updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        }
        else {
            if (obtainUpdateOp != null) {
                if (updateOp.positionStart >= obtainUpdateOp.positionStart) {
                    updateOp.positionStart -= obtainUpdateOp.itemCount;
                }
                if (updateOp.itemCount >= obtainUpdateOp.positionStart) {
                    updateOp.itemCount -= obtainUpdateOp.itemCount;
                }
            }
            if (updateOp.positionStart >= updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount >= updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        }
        list.set(n, updateOp2);
        if (updateOp.positionStart != updateOp.itemCount) {
            list.set(n2, updateOp);
        }
        else {
            list.remove(n2);
        }
        if (obtainUpdateOp != null) {
            list.add(n, obtainUpdateOp);
        }
    }
    
    void swapMoveUpdate(final List<AdapterHelper.UpdateOp> list, final int n, final AdapterHelper.UpdateOp updateOp, final int n2, final AdapterHelper.UpdateOp updateOp2) {
        final int itemCount = updateOp.itemCount;
        final int positionStart = updateOp2.positionStart;
        AdapterHelper.UpdateOp obtainUpdateOp = null;
        AdapterHelper.UpdateOp obtainUpdateOp2 = null;
        Label_0096: {
            if (itemCount < positionStart) {
                --updateOp2.positionStart;
            }
            else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
                --updateOp2.itemCount;
                obtainUpdateOp2 = this.mCallback.obtainUpdateOp(4, updateOp.positionStart, 1, updateOp2.payload);
                break Label_0096;
            }
            obtainUpdateOp2 = null;
        }
        if (updateOp.positionStart <= updateOp2.positionStart) {
            ++updateOp2.positionStart;
        }
        else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            final int n3 = updateOp2.positionStart + updateOp2.itemCount - updateOp.positionStart;
            obtainUpdateOp = this.mCallback.obtainUpdateOp(4, updateOp.positionStart + 1, n3, updateOp2.payload);
            updateOp2.itemCount -= n3;
        }
        list.set(n2, updateOp);
        if (updateOp2.itemCount > 0) {
            list.set(n, updateOp2);
        }
        else {
            list.remove(n);
            this.mCallback.recycleUpdateOp(updateOp2);
        }
        if (obtainUpdateOp2 != null) {
            list.add(n, obtainUpdateOp2);
        }
        if (obtainUpdateOp != null) {
            list.add(n, obtainUpdateOp);
        }
    }
    
    interface Callback
    {
        AdapterHelper.UpdateOp obtainUpdateOp(final int p0, final int p1, final int p2, final Object p3);
        
        void recycleUpdateOp(final AdapterHelper.UpdateOp p0);
    }
}
