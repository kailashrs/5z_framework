package android.telephony.ims.feature;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsMmTelFeature.Stub;
import android.telephony.ims.aidl.IImsMmTelListener;
import android.telephony.ims.aidl.IImsMmTelListener.Stub;
import android.telephony.ims.aidl.IImsSmsListener;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.telephony.ims.stub.ImsSmsImplBase;
import android.telephony.ims.stub.ImsUtImplBase;
import android.util.Log;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsUt;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public class MmTelFeature
  extends ImsFeature
{
  private static final String LOG_TAG = "MmTelFeature";
  public static final int PROCESS_CALL_CSFB = 1;
  public static final int PROCESS_CALL_IMS = 0;
  private final IImsMmTelFeature mImsMMTelBinder = new IImsMmTelFeature.Stub()
  {
    public void acknowledgeSms(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      synchronized (mLock)
      {
        MmTelFeature.this.acknowledgeSms(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        return;
      }
    }
    
    public void acknowledgeSmsReport(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      synchronized (mLock)
      {
        MmTelFeature.this.acknowledgeSmsReport(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        return;
      }
    }
    
    public void addCapabilityCallback(IImsCapabilityCallback paramAnonymousIImsCapabilityCallback)
    {
      MmTelFeature.this.addCapabilityCallback(paramAnonymousIImsCapabilityCallback);
    }
    
    public void changeCapabilitiesConfiguration(CapabilityChangeRequest paramAnonymousCapabilityChangeRequest, IImsCapabilityCallback paramAnonymousIImsCapabilityCallback)
    {
      synchronized (mLock)
      {
        requestChangeEnabledCapabilities(paramAnonymousCapabilityChangeRequest, paramAnonymousIImsCapabilityCallback);
        return;
      }
    }
    
    /* Error */
    public ImsCallProfile createCallProfile(int paramAnonymousInt1, int paramAnonymousInt2)
      throws RemoteException
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 12	android/telephony/ims/feature/MmTelFeature$1:this$0	Landroid/telephony/ims/feature/MmTelFeature;
      //   4: getfield 22	android/telephony/ims/feature/MmTelFeature:mLock	Ljava/lang/Object;
      //   7: astore_3
      //   8: aload_3
      //   9: monitorenter
      //   10: aload_0
      //   11: getfield 12	android/telephony/ims/feature/MmTelFeature$1:this$0	Landroid/telephony/ims/feature/MmTelFeature;
      //   14: iload_1
      //   15: iload_2
      //   16: invokevirtual 47	android/telephony/ims/feature/MmTelFeature:createCallProfile	(II)Landroid/telephony/ims/ImsCallProfile;
      //   19: astore 4
      //   21: aload_3
      //   22: monitorexit
      //   23: aload 4
      //   25: areturn
      //   26: astore 4
      //   28: goto +23 -> 51
      //   31: astore 4
      //   33: new 43	android/os/RemoteException
      //   36: astore 5
      //   38: aload 5
      //   40: aload 4
      //   42: invokevirtual 51	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   45: invokespecial 54	android/os/RemoteException:<init>	(Ljava/lang/String;)V
      //   48: aload 5
      //   50: athrow
      //   51: aload_3
      //   52: monitorexit
      //   53: aload 4
      //   55: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	56	0	this	1
      //   0	56	1	paramAnonymousInt1	int
      //   0	56	2	paramAnonymousInt2	int
      //   7	45	3	localObject1	Object
      //   19	5	4	localImsCallProfile	ImsCallProfile
      //   26	1	4	localObject2	Object
      //   31	23	4	localException	Exception
      //   36	13	5	localRemoteException	RemoteException
      // Exception table:
      //   from	to	target	type
      //   10	21	26	finally
      //   21	23	26	finally
      //   33	51	26	finally
      //   51	53	26	finally
      //   10	21	31	java/lang/Exception
    }
    
    public IImsCallSession createCallSession(ImsCallProfile paramAnonymousImsCallProfile)
      throws RemoteException
    {
      synchronized (mLock)
      {
        paramAnonymousImsCallProfile = createCallSessionInterface(paramAnonymousImsCallProfile);
        return paramAnonymousImsCallProfile;
      }
    }
    
    public IImsEcbm getEcbmInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        IImsEcbm localIImsEcbm = MmTelFeature.this.getEcbmInterface();
        return localIImsEcbm;
      }
    }
    
    public int getFeatureState()
      throws RemoteException
    {
      try
      {
        int i = MmTelFeature.this.getFeatureState();
        return i;
      }
      catch (Exception localException)
      {
        throw new RemoteException(localException.getMessage());
      }
    }
    
    public IImsMultiEndpoint getMultiEndpointInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        IImsMultiEndpoint localIImsMultiEndpoint = MmTelFeature.this.getMultiEndpointInterface();
        return localIImsMultiEndpoint;
      }
    }
    
    public String getSmsFormat()
    {
      synchronized (mLock)
      {
        String str = MmTelFeature.this.getSmsFormat();
        return str;
      }
    }
    
    public IImsUt getUtInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        IImsUt localIImsUt = MmTelFeature.this.getUtInterface();
        return localIImsUt;
      }
    }
    
    public void onSmsReady()
    {
      synchronized (mLock)
      {
        MmTelFeature.this.onSmsReady();
        return;
      }
    }
    
    public void queryCapabilityConfiguration(int paramAnonymousInt1, int paramAnonymousInt2, IImsCapabilityCallback paramAnonymousIImsCapabilityCallback)
    {
      synchronized (mLock)
      {
        MmTelFeature.this.queryCapabilityConfigurationInternal(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousIImsCapabilityCallback);
        return;
      }
    }
    
    public int queryCapabilityStatus()
    {
      return queryCapabilityStatus().mCapabilities;
    }
    
    public void removeCapabilityCallback(IImsCapabilityCallback paramAnonymousIImsCapabilityCallback)
    {
      MmTelFeature.this.removeCapabilityCallback(paramAnonymousIImsCapabilityCallback);
    }
    
    public void sendSms(int paramAnonymousInt1, int paramAnonymousInt2, String paramAnonymousString1, String paramAnonymousString2, boolean paramAnonymousBoolean, byte[] paramAnonymousArrayOfByte)
    {
      synchronized (mLock)
      {
        MmTelFeature.this.sendSms(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousString1, paramAnonymousString2, paramAnonymousBoolean, paramAnonymousArrayOfByte);
        return;
      }
    }
    
    public void setListener(IImsMmTelListener paramAnonymousIImsMmTelListener)
    {
      MmTelFeature.this.setListener(paramAnonymousIImsMmTelListener);
    }
    
    public void setSmsListener(IImsSmsListener paramAnonymousIImsSmsListener)
    {
      MmTelFeature.this.setSmsListener(paramAnonymousIImsSmsListener);
    }
    
    /* Error */
    public void setUiTtyMode(int paramAnonymousInt, Message paramAnonymousMessage)
      throws RemoteException
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 12	android/telephony/ims/feature/MmTelFeature$1:this$0	Landroid/telephony/ims/feature/MmTelFeature;
      //   4: getfield 22	android/telephony/ims/feature/MmTelFeature:mLock	Ljava/lang/Object;
      //   7: astore_3
      //   8: aload_3
      //   9: monitorenter
      //   10: aload_0
      //   11: getfield 12	android/telephony/ims/feature/MmTelFeature$1:this$0	Landroid/telephony/ims/feature/MmTelFeature;
      //   14: iload_1
      //   15: aload_2
      //   16: invokevirtual 126	android/telephony/ims/feature/MmTelFeature:setUiTtyMode	(ILandroid/os/Message;)V
      //   19: aload_3
      //   20: monitorexit
      //   21: return
      //   22: astore_2
      //   23: goto +20 -> 43
      //   26: astore 4
      //   28: new 43	android/os/RemoteException
      //   31: astore_2
      //   32: aload_2
      //   33: aload 4
      //   35: invokevirtual 51	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   38: invokespecial 54	android/os/RemoteException:<init>	(Ljava/lang/String;)V
      //   41: aload_2
      //   42: athrow
      //   43: aload_3
      //   44: monitorexit
      //   45: aload_2
      //   46: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	47	0	this	1
      //   0	47	1	paramAnonymousInt	int
      //   0	47	2	paramAnonymousMessage	Message
      //   7	37	3	localObject	Object
      //   26	8	4	localException	Exception
      // Exception table:
      //   from	to	target	type
      //   10	19	22	finally
      //   19	21	22	finally
      //   28	43	22	finally
      //   43	45	22	finally
      //   10	19	26	java/lang/Exception
    }
    
    public int shouldProcessCall(String[] paramAnonymousArrayOfString)
    {
      synchronized (mLock)
      {
        int i = MmTelFeature.this.shouldProcessCall(paramAnonymousArrayOfString);
        return i;
      }
    }
  };
  private IImsMmTelListener mListener;
  
  public MmTelFeature() {}
  
  private void acknowledgeSms(int paramInt1, int paramInt2, int paramInt3)
  {
    getSmsImplementation().acknowledgeSms(paramInt1, paramInt2, paramInt3);
  }
  
  private void acknowledgeSmsReport(int paramInt1, int paramInt2, int paramInt3)
  {
    getSmsImplementation().acknowledgeSmsReport(paramInt1, paramInt2, paramInt3);
  }
  
  private String getSmsFormat()
  {
    return getSmsImplementation().getSmsFormat();
  }
  
  private void onSmsReady()
  {
    getSmsImplementation().onReady();
  }
  
  private void queryCapabilityConfigurationInternal(int paramInt1, int paramInt2, IImsCapabilityCallback paramIImsCapabilityCallback)
  {
    boolean bool = queryCapabilityConfiguration(paramInt1, paramInt2);
    if (paramIImsCapabilityCallback != null) {
      try
      {
        paramIImsCapabilityCallback.onQueryCapabilityConfiguration(paramInt1, paramInt2, bool);
      }
      catch (RemoteException paramIImsCapabilityCallback)
      {
        Log.e("MmTelFeature", "queryCapabilityConfigurationInternal called on dead binder!");
      }
    }
  }
  
  private void sendSms(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    getSmsImplementation().sendSms(paramInt1, paramInt2, paramString1, paramString2, paramBoolean, paramArrayOfByte);
  }
  
  private void setListener(IImsMmTelListener paramIImsMmTelListener)
  {
    synchronized (mLock)
    {
      mListener = paramIImsMmTelListener;
      if (mListener != null) {
        onFeatureReady();
      }
      return;
    }
  }
  
  private void setSmsListener(IImsSmsListener paramIImsSmsListener)
  {
    getSmsImplementation().registerSmsListener(paramIImsSmsListener);
  }
  
  public void changeEnabledCapabilities(CapabilityChangeRequest paramCapabilityChangeRequest, ImsFeature.CapabilityCallbackProxy paramCapabilityCallbackProxy) {}
  
  public ImsCallProfile createCallProfile(int paramInt1, int paramInt2)
  {
    return null;
  }
  
  public ImsCallSessionImplBase createCallSession(ImsCallProfile paramImsCallProfile)
  {
    return null;
  }
  
  public IImsCallSession createCallSessionInterface(ImsCallProfile paramImsCallProfile)
    throws RemoteException
  {
    paramImsCallProfile = createCallSession(paramImsCallProfile);
    if (paramImsCallProfile != null) {
      paramImsCallProfile = paramImsCallProfile.getServiceImpl();
    } else {
      paramImsCallProfile = null;
    }
    return paramImsCallProfile;
  }
  
  public final IImsMmTelFeature getBinder()
  {
    return mImsMMTelBinder;
  }
  
  public ImsEcbmImplBase getEcbm()
  {
    return new ImsEcbmImplBase();
  }
  
  protected IImsEcbm getEcbmInterface()
    throws RemoteException
  {
    Object localObject = getEcbm();
    if (localObject != null) {
      localObject = ((ImsEcbmImplBase)localObject).getImsEcbm();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public ImsMultiEndpointImplBase getMultiEndpoint()
  {
    return new ImsMultiEndpointImplBase();
  }
  
  public IImsMultiEndpoint getMultiEndpointInterface()
    throws RemoteException
  {
    Object localObject = getMultiEndpoint();
    if (localObject != null) {
      localObject = ((ImsMultiEndpointImplBase)localObject).getIImsMultiEndpoint();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public ImsSmsImplBase getSmsImplementation()
  {
    return new ImsSmsImplBase();
  }
  
  public ImsUtImplBase getUt()
  {
    return new ImsUtImplBase();
  }
  
  protected IImsUt getUtInterface()
    throws RemoteException
  {
    Object localObject = getUt();
    if (localObject != null) {
      localObject = ((ImsUtImplBase)localObject).getInterface();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public final void notifyCapabilitiesStatusChanged(MmTelCapabilities paramMmTelCapabilities)
  {
    super.notifyCapabilitiesStatusChanged(paramMmTelCapabilities);
  }
  
  public final void notifyIncomingCall(ImsCallSessionImplBase paramImsCallSessionImplBase, Bundle paramBundle)
  {
    synchronized (mLock)
    {
      IImsMmTelListener localIImsMmTelListener = mListener;
      if (localIImsMmTelListener != null) {
        try
        {
          mListener.onIncomingCall(paramImsCallSessionImplBase.getServiceImpl(), paramBundle);
          return;
        }
        catch (RemoteException paramBundle)
        {
          paramImsCallSessionImplBase = new java/lang/RuntimeException;
          paramImsCallSessionImplBase.<init>(paramBundle);
          throw paramImsCallSessionImplBase;
        }
      }
      paramImsCallSessionImplBase = new java/lang/IllegalStateException;
      paramImsCallSessionImplBase.<init>("Session is not available.");
      throw paramImsCallSessionImplBase;
    }
  }
  
  public final void notifyIncomingCallSession(IImsCallSession paramIImsCallSession, Bundle paramBundle)
  {
    synchronized (mLock)
    {
      IImsMmTelListener localIImsMmTelListener = mListener;
      if (localIImsMmTelListener != null) {
        try
        {
          mListener.onIncomingCall(paramIImsCallSession, paramBundle);
          return;
        }
        catch (RemoteException paramIImsCallSession)
        {
          paramBundle = new java/lang/RuntimeException;
          paramBundle.<init>(paramIImsCallSession);
          throw paramBundle;
        }
      }
      paramIImsCallSession = new java/lang/IllegalStateException;
      paramIImsCallSession.<init>("Session is not available.");
      throw paramIImsCallSession;
    }
  }
  
  public final void notifyRejectedCall(ImsCallProfile paramImsCallProfile, ImsReasonInfo paramImsReasonInfo)
  {
    synchronized (mLock)
    {
      IImsMmTelListener localIImsMmTelListener = mListener;
      if (localIImsMmTelListener != null) {
        try
        {
          mListener.onRejectedCall(paramImsCallProfile, paramImsReasonInfo);
          return;
        }
        catch (RemoteException paramImsReasonInfo)
        {
          paramImsCallProfile = new java/lang/RuntimeException;
          paramImsCallProfile.<init>(paramImsReasonInfo);
          throw paramImsCallProfile;
        }
      }
      paramImsCallProfile = new java/lang/IllegalStateException;
      paramImsCallProfile.<init>("Session is not available.");
      throw paramImsCallProfile;
    }
  }
  
  public final void notifyVoiceMessageCountUpdate(int paramInt)
  {
    synchronized (mLock)
    {
      Object localObject2 = mListener;
      if (localObject2 != null) {
        try
        {
          mListener.onVoiceMessageCountUpdate(paramInt);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          localObject2 = new java/lang/RuntimeException;
          ((RuntimeException)localObject2).<init>(localRemoteException);
          throw ((Throwable)localObject2);
        }
      }
      localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("Session is not available.");
      throw ((Throwable)localObject2);
    }
  }
  
  public void onFeatureReady() {}
  
  public void onFeatureRemoved() {}
  
  public boolean queryCapabilityConfiguration(int paramInt1, int paramInt2)
  {
    return false;
  }
  
  public final MmTelCapabilities queryCapabilityStatus()
  {
    return new MmTelCapabilities(super.queryCapabilityStatus());
  }
  
  public void setUiTtyMode(int paramInt, Message paramMessage) {}
  
  public int shouldProcessCall(String[] paramArrayOfString)
  {
    return 0;
  }
  
  public static class Listener
    extends IImsMmTelListener.Stub
  {
    public Listener() {}
    
    public void onIncomingCall(IImsCallSession paramIImsCallSession, Bundle paramBundle) {}
    
    public void onRejectedCall(ImsCallProfile paramImsCallProfile, ImsReasonInfo paramImsReasonInfo) {}
    
    public void onVoiceMessageCountUpdate(int paramInt) {}
  }
  
  public static class MmTelCapabilities
    extends ImsFeature.Capabilities
  {
    public static final int CAPABILITY_TYPE_SMS = 8;
    public static final int CAPABILITY_TYPE_UT = 4;
    public static final int CAPABILITY_TYPE_VIDEO = 2;
    public static final int CAPABILITY_TYPE_VOICE = 1;
    
    @VisibleForTesting
    public MmTelCapabilities() {}
    
    public MmTelCapabilities(int paramInt)
    {
      mCapabilities = paramInt;
    }
    
    public MmTelCapabilities(ImsFeature.Capabilities paramCapabilities)
    {
      mCapabilities = mCapabilities;
    }
    
    public final void addCapabilities(int paramInt)
    {
      super.addCapabilities(paramInt);
    }
    
    public final boolean isCapable(int paramInt)
    {
      return super.isCapable(paramInt);
    }
    
    public final void removeCapabilities(int paramInt)
    {
      super.removeCapabilities(paramInt);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("MmTel Capabilities - [");
      localStringBuilder.append("Voice: ");
      localStringBuilder.append(isCapable(1));
      localStringBuilder.append(" Video: ");
      localStringBuilder.append(isCapable(2));
      localStringBuilder.append(" UT: ");
      localStringBuilder.append(isCapable(4));
      localStringBuilder.append(" SMS: ");
      localStringBuilder.append(isCapable(8));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface MmTelCapability {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProcessCallResult {}
}
