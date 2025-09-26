// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.search.shared.service.proto;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.Internal$EnumLiteMap;
import com.google.protobuf.MessageLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.WireFormat$FieldType;
import com.google.protobuf.GeneratedMessageLite$GeneratedExtension;

public final class LensServiceEvent
{
    public static final int LENS_SERVICE_EVENT_DATA_FIELD_NUMBER = 152666888;
    public static final GeneratedMessageLite$GeneratedExtension<ServiceEventProto, LensServiceEventData> lensServiceEventData;
    
    static {
        lensServiceEventData = GeneratedMessageLite.newSingularGeneratedExtension((MessageLite)ServiceEventProto.getDefaultInstance(), (Object)LensServiceEventData.getDefaultInstance(), (MessageLite)LensServiceEventData.getDefaultInstance(), (Internal$EnumLiteMap)null, 152666888, WireFormat$FieldType.MESSAGE, (Class)LensServiceEventData.class);
    }
    
    private LensServiceEvent() {
    }
    
    public static void registerAllExtensions(final ExtensionRegistryLite extensionRegistryLite) {
        extensionRegistryLite.add((GeneratedMessageLite$GeneratedExtension)LensServiceEvent.lensServiceEventData);
    }
}
