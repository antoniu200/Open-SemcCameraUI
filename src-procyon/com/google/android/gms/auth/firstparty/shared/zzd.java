// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.firstparty.shared;

public enum zzd
{
    @Deprecated
    zzTN("ClientLoginDisabled"), 
    @Deprecated
    zzTO("DeviceManagementRequiredOrSyncDisabled"), 
    @Deprecated
    zzTP("SocketTimeout"), 
    zzTQ("Ok"), 
    zzTR("UNKNOWN_ERR"), 
    zzTS("NetworkError"), 
    zzTT("ServiceUnavailable"), 
    zzTU("InternalError"), 
    zzTV("BadAuthentication"), 
    zzTW("EmptyConsumerPackageOrSig"), 
    zzTX("InvalidSecondFactor"), 
    zzTY("PostSignInFlowRequired"), 
    zzTZ("NeedsBrowser"), 
    zzUA("NotLoggedIn"), 
    zzUB("NoGmail"), 
    zzUC("RequestDenied"), 
    zzUD("ServerError"), 
    zzUE("UsernameUnavailable"), 
    zzUF("GPlusOther"), 
    zzUG("GPlusNickname"), 
    zzUH("GPlusInvalidChar"), 
    zzUI("GPlusInterstitial"), 
    zzUJ("ProfileUpgradeError");
    
    private static final zzd[] zzUL;
    
    zzUa("Unknown"), 
    zzUb("NotVerified"), 
    zzUc("TermsNotAgreed"), 
    zzUd("AccountDisabled"), 
    zzUe("CaptchaRequired"), 
    zzUf("AccountDeleted"), 
    zzUg("ServiceDisabled"), 
    zzUh("NeedPermission"), 
    zzUi("INVALID_SCOPE"), 
    zzUj("UserCancel"), 
    zzUk("PermissionDenied"), 
    zzUl("ThirdPartyDeviceManagementRequired"), 
    zzUm("DeviceManagementInternalError"), 
    zzUn("DeviceManagementSyncDisabled"), 
    zzUo("DeviceManagementAdminBlocked"), 
    zzUp("DeviceManagementAdminPendingApproval"), 
    zzUq("DeviceManagementStaleSyncRequired"), 
    zzUr("DeviceManagementDeactivated"), 
    zzUs("DeviceManagementRequired"), 
    zzUt("ALREADY_HAS_GMAIL"), 
    zzUu("WeakPassword"), 
    zzUv("BadRequest"), 
    zzUw("BadUsername"), 
    zzUx("DeletedGmail"), 
    zzUy("ExistingUsername"), 
    zzUz("LoginFail");
    
    private final String zzUK;
    
    static {
        zzUL = new zzd[] { zzd.zzTN, zzd.zzTO, zzd.zzTP, zzd.zzTQ, zzd.zzTR, zzd.zzTS, zzd.zzTT, zzd.zzTU, zzd.zzTV, zzd.zzTW, zzd.zzTX, zzd.zzTY, zzd.zzTZ, zzd.zzUa, zzd.zzUb, zzd.zzUc, zzd.zzUd, zzd.zzUe, zzd.zzUf, zzd.zzUg, zzd.zzUh, zzd.zzUi, zzd.zzUj, zzd.zzUk, zzd.zzUl, zzd.zzUm, zzd.zzUn, zzd.zzUo, zzd.zzUp, zzd.zzUq, zzd.zzUr, zzd.zzUs, zzd.zzUt, zzd.zzUu, zzd.zzUv, zzd.zzUw, zzd.zzUx, zzd.zzUy, zzd.zzUz, zzd.zzUA, zzd.zzUB, zzd.zzUC, zzd.zzUD, zzd.zzUE, zzd.zzUF, zzd.zzUG, zzd.zzUH, zzd.zzUI, zzd.zzUJ };
    }
    
    private zzd(final String zzUK) {
        this.zzUK = zzUK;
    }
    
    public static boolean zza(final zzd zzd) {
        return zzd.zzTV.equals(zzd) || zzd.zzUe.equals(zzd) || zzd.zzUh.equals(zzd) || zzd.zzTZ.equals(zzd) || zzd.zzUj.equals(zzd) || zzd.zzUl.equals(zzd) || zzb(zzd);
    }
    
    public static boolean zzb(final zzd zzd) {
        return zzd.zzTO.equals(zzd) || zzd.zzUm.equals(zzd) || zzd.zzUn.equals(zzd) || zzd.zzUo.equals(zzd) || zzd.zzUp.equals(zzd) || zzd.zzUq.equals(zzd) || zzd.zzUr.equals(zzd) || zzd.zzUs.equals(zzd);
    }
    
    public static final zzd zzbE(final String anObject) {
        final zzd[] values = values();
        final int length = values.length;
        zzd zzd = null;
        for (final zzd zzd2 : values) {
            if (zzd2.zzUK.equals(anObject)) {
                zzd = zzd2;
            }
        }
        return zzd;
    }
    
    public static boolean zzc(final zzd zzd) {
        return zzd.zzTS.equals(zzd) || zzd.zzTT.equals(zzd);
    }
}
