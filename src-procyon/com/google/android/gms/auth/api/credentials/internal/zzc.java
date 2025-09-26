// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials.internal;

import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.common.api.Result;
import android.os.RemoteException;
import com.google.android.gms.internal.zzlb;
import android.content.Context;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.credentials.CredentialsApi;

public final class zzc implements CredentialsApi
{
    @Override
    public PendingResult<Status> delete(final GoogleApiClient googleApiClient, final Credential credential) {
        return googleApiClient.zzb((PendingResult<Status>)new zzd<Status>(this, googleApiClient, credential) {
            final zzc zzSF;
            final Credential zzSH;
            
            @Override
            protected void zza(final Context context, final zzh zzh) throws RemoteException {
                zzh.zza(new zza((zzlb.zzb<Status>)this), new DeleteRequest(this.zzSH));
            }
            
            protected Status zzd(final Status status) {
                return status;
            }
        });
    }
    
    @Override
    public PendingResult<Status> disableAutoSignIn(final GoogleApiClient googleApiClient) {
        return googleApiClient.zzb((PendingResult<Status>)new zzd<Status>(this, googleApiClient) {
            final zzc zzSF;
            
            @Override
            protected void zza(final Context context, final zzh zzh) throws RemoteException {
                zzh.zza(new zza((zzlb.zzb<Status>)this));
            }
            
            protected Status zzd(final Status status) {
                return status;
            }
        });
    }
    
    @Override
    public PendingResult<CredentialRequestResult> request(final GoogleApiClient googleApiClient, final CredentialRequest credentialRequest) {
        return googleApiClient.zza((PendingResult<CredentialRequestResult>)new zzd<CredentialRequestResult>(this, googleApiClient, credentialRequest) {
            final CredentialRequest zzSE;
            final zzc zzSF;
            
            @Override
            protected void zza(final Context context, final zzh zzh) throws RemoteException {
                zzh.zza(new zza(this) {
                    final zzc$1 zzSG;
                    
                    @Override
                    public void zza(final Status status, final Credential credential) {
                        this.zzSG.zzb((R)new zzb(status, credential));
                    }
                    
                    @Override
                    public void zzg(final Status status) {
                        this.zzSG.zzb((R)zzb.zzh(status));
                    }
                }, this.zzSE);
            }
            
            protected CredentialRequestResult zzi(final Status status) {
                return zzb.zzh(status);
            }
        });
    }
    
    @Override
    public PendingResult<Status> save(final GoogleApiClient googleApiClient, final Credential credential) {
        return googleApiClient.zzb((PendingResult<Status>)new zzd<Status>(this, googleApiClient, credential) {
            final zzc zzSF;
            final Credential zzSH;
            
            @Override
            protected void zza(final Context context, final zzh zzh) throws RemoteException {
                zzh.zza(new zza((zzlb.zzb<Status>)this), new SaveRequest(this.zzSH));
            }
            
            protected Status zzd(final Status status) {
                return status;
            }
        });
    }
    
    private static class zza extends com.google.android.gms.auth.api.credentials.internal.zza
    {
        private zzlb.zzb<Status> zzSI;
        
        zza(final zzlb.zzb<Status> zzSI) {
            this.zzSI = zzSI;
        }
        
        @Override
        public void zzg(final Status status) {
            this.zzSI.zzp(status);
        }
    }
}
