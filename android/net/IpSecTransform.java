package android.net;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;

public final class IpSecTransform
  implements AutoCloseable
{
  public static final int ENCAP_ESPINUDP = 2;
  public static final int ENCAP_ESPINUDP_NON_IKE = 1;
  public static final int ENCAP_NONE = 0;
  public static final int MODE_TRANSPORT = 0;
  public static final int MODE_TUNNEL = 1;
  private static final String TAG = "IpSecTransform";
  private Handler mCallbackHandler;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final IpSecConfig mConfig;
  private final Context mContext;
  private ConnectivityManager.PacketKeepalive mKeepalive;
  private final ConnectivityManager.PacketKeepaliveCallback mKeepaliveCallback = new ConnectivityManager.PacketKeepaliveCallback()
  {
    public void onError(int paramAnonymousInt)
    {
      try
      {
        IpSecTransform.access$102(IpSecTransform.this, null);
        Handler localHandler = mCallbackHandler;
        _..Lambda.IpSecTransform.1._ae2VrMToKvertNlEIezU0bdvXE local_ae2VrMToKvertNlEIezU0bdvXE = new android/net/_$$Lambda$IpSecTransform$1$_ae2VrMToKvertNlEIezU0bdvXE;
        local_ae2VrMToKvertNlEIezU0bdvXE.<init>(this, paramAnonymousInt);
        localHandler.post(local_ae2VrMToKvertNlEIezU0bdvXE);
        return;
      }
      finally {}
    }
    
    public void onStarted()
    {
      try
      {
        Handler localHandler = mCallbackHandler;
        _..Lambda.IpSecTransform.1.zl9bpxiE2uj_QuCOkuJ091wPuwo localZl9bpxiE2uj_QuCOkuJ091wPuwo = new android/net/_$$Lambda$IpSecTransform$1$zl9bpxiE2uj_QuCOkuJ091wPuwo;
        localZl9bpxiE2uj_QuCOkuJ091wPuwo.<init>(this);
        localHandler.post(localZl9bpxiE2uj_QuCOkuJ091wPuwo);
        return;
      }
      finally {}
    }
    
    public void onStopped()
    {
      try
      {
        IpSecTransform.access$102(IpSecTransform.this, null);
        Handler localHandler = mCallbackHandler;
        _..Lambda.IpSecTransform.1.Rc3lbWP51o1kJRHwkpVUEV1G_d8 localRc3lbWP51o1kJRHwkpVUEV1G_d8 = new android/net/_$$Lambda$IpSecTransform$1$Rc3lbWP51o1kJRHwkpVUEV1G_d8;
        localRc3lbWP51o1kJRHwkpVUEV1G_d8.<init>(this);
        localHandler.post(localRc3lbWP51o1kJRHwkpVUEV1G_d8);
        return;
      }
      finally {}
    }
  };
  private int mResourceId;
  private NattKeepaliveCallback mUserKeepaliveCallback;
  
  @VisibleForTesting
  public IpSecTransform(Context paramContext, IpSecConfig paramIpSecConfig)
  {
    mContext = paramContext;
    mConfig = new IpSecConfig(paramIpSecConfig);
    mResourceId = -1;
  }
  
  /* Error */
  private IpSecTransform activate()
    throws IOException, IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 110	android/net/IpSecTransform:getIpSecService	()Landroid/net/IIpSecService;
    //   6: astore_1
    //   7: aload_0
    //   8: getfield 73	android/net/IpSecTransform:mConfig	Landroid/net/IpSecConfig;
    //   11: astore_2
    //   12: new 112	android/os/Binder
    //   15: astore_3
    //   16: aload_3
    //   17: invokespecial 113	android/os/Binder:<init>	()V
    //   20: aload_1
    //   21: aload_2
    //   22: aload_3
    //   23: aload_0
    //   24: getfield 66	android/net/IpSecTransform:mContext	Landroid/content/Context;
    //   27: invokevirtual 119	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   30: invokeinterface 125 4 0
    //   35: astore_2
    //   36: aload_0
    //   37: aload_2
    //   38: getfield 130	android/net/IpSecTransformResponse:status	I
    //   41: invokespecial 134	android/net/IpSecTransform:checkResultStatus	(I)V
    //   44: aload_0
    //   45: aload_2
    //   46: getfield 137	android/net/IpSecTransformResponse:resourceId	I
    //   49: putfield 75	android/net/IpSecTransform:mResourceId	I
    //   52: new 139	java/lang/StringBuilder
    //   55: astore_2
    //   56: aload_2
    //   57: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   60: aload_2
    //   61: ldc -114
    //   63: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_2
    //   68: aload_0
    //   69: getfield 75	android/net/IpSecTransform:mResourceId	I
    //   72: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   75: pop
    //   76: ldc 30
    //   78: aload_2
    //   79: invokevirtual 152	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   82: invokestatic 158	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   85: pop
    //   86: aload_0
    //   87: getfield 59	android/net/IpSecTransform:mCloseGuard	Ldalvik/system/CloseGuard;
    //   90: ldc -96
    //   92: invokevirtual 164	dalvik/system/CloseGuard:open	(Ljava/lang/String;)V
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_0
    //   98: areturn
    //   99: astore_2
    //   100: goto +15 -> 115
    //   103: astore_2
    //   104: aload_2
    //   105: invokevirtual 168	android/os/RemoteException:rethrowAsRuntimeException	()Ljava/lang/RuntimeException;
    //   108: athrow
    //   109: astore_2
    //   110: aload_2
    //   111: invokestatic 174	android/net/IpSecManager:rethrowUncheckedExceptionFromServiceSpecificException	(Landroid/os/ServiceSpecificException;)Ljava/lang/RuntimeException;
    //   114: athrow
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_2
    //   118: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	119	0	this	IpSecTransform
    //   6	15	1	localIIpSecService	IIpSecService
    //   11	68	2	localObject1	Object
    //   99	1	2	localObject2	Object
    //   103	2	2	localRemoteException	RemoteException
    //   109	9	2	localServiceSpecificException	android.os.ServiceSpecificException
    //   15	8	3	localBinder	android.os.Binder
    // Exception table:
    //   from	to	target	type
    //   2	95	99	finally
    //   95	97	99	finally
    //   104	109	99	finally
    //   110	115	99	finally
    //   115	117	99	finally
    //   2	95	103	android/os/RemoteException
    //   2	95	109	android/os/ServiceSpecificException
  }
  
  private void checkResultStatus(int paramInt)
    throws IOException, IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException
  {
    switch (paramInt)
    {
    default: 
      break;
    case 2: 
      Log.wtf("IpSecTransform", "Attempting to use an SPI that was somehow not reserved");
      break;
    case 1: 
      throw new IpSecManager.ResourceUnavailableException("Failed to allocate a new IpSecTransform");
    case 0: 
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Failed to Create a Transform with status code ");
    localStringBuilder.append(paramInt);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  @VisibleForTesting
  public static boolean equals(IpSecTransform paramIpSecTransform1, IpSecTransform paramIpSecTransform2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if ((paramIpSecTransform1 != null) && (paramIpSecTransform2 != null))
    {
      bool1 = bool2;
      if (IpSecConfig.equals(paramIpSecTransform1.getConfig(), paramIpSecTransform2.getConfig()))
      {
        bool1 = bool2;
        if (mResourceId == mResourceId) {
          bool1 = true;
        }
      }
      return bool1;
    }
    if (paramIpSecTransform1 == paramIpSecTransform2) {
      bool1 = true;
    }
    return bool1;
  }
  
  private IIpSecService getIpSecService()
  {
    IBinder localIBinder = ServiceManager.getService("ipsec");
    if (localIBinder != null) {
      return IIpSecService.Stub.asInterface(localIBinder);
    }
    throw new RemoteException("Failed to connect to IpSecService").rethrowAsRuntimeException();
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: new 139	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   7: astore_1
    //   8: aload_1
    //   9: ldc -37
    //   11: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   14: pop
    //   15: aload_1
    //   16: aload_0
    //   17: getfield 75	android/net/IpSecTransform:mResourceId	I
    //   20: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   23: pop
    //   24: ldc 30
    //   26: aload_1
    //   27: invokevirtual 152	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   30: invokestatic 158	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   33: pop
    //   34: aload_0
    //   35: getfield 75	android/net/IpSecTransform:mResourceId	I
    //   38: iconst_m1
    //   39: if_icmpne +11 -> 50
    //   42: aload_0
    //   43: getfield 59	android/net/IpSecTransform:mCloseGuard	Ldalvik/system/CloseGuard;
    //   46: invokevirtual 221	dalvik/system/CloseGuard:close	()V
    //   49: return
    //   50: aload_0
    //   51: invokespecial 110	android/net/IpSecTransform:getIpSecService	()Landroid/net/IIpSecService;
    //   54: aload_0
    //   55: getfield 75	android/net/IpSecTransform:mResourceId	I
    //   58: invokeinterface 224 2 0
    //   63: aload_0
    //   64: invokevirtual 227	android/net/IpSecTransform:stopNattKeepalive	()V
    //   67: aload_0
    //   68: iconst_m1
    //   69: putfield 75	android/net/IpSecTransform:mResourceId	I
    //   72: aload_0
    //   73: getfield 59	android/net/IpSecTransform:mCloseGuard	Ldalvik/system/CloseGuard;
    //   76: invokevirtual 221	dalvik/system/CloseGuard:close	()V
    //   79: goto +55 -> 134
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 139	java/lang/StringBuilder
    //   90: astore_2
    //   91: aload_2
    //   92: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   95: aload_2
    //   96: ldc -27
    //   98: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: aload_2
    //   103: aload_0
    //   104: invokevirtual 232	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: aload_2
    //   109: ldc -22
    //   111: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: pop
    //   115: aload_2
    //   116: aload_1
    //   117: invokevirtual 232	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: ldc 30
    //   123: aload_2
    //   124: invokevirtual 152	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   127: invokestatic 237	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   130: pop
    //   131: goto -64 -> 67
    //   134: return
    //   135: astore_1
    //   136: aload_1
    //   137: invokevirtual 168	android/os/RemoteException:rethrowAsRuntimeException	()Ljava/lang/RuntimeException;
    //   140: athrow
    //   141: aload_0
    //   142: iconst_m1
    //   143: putfield 75	android/net/IpSecTransform:mResourceId	I
    //   146: aload_0
    //   147: getfield 59	android/net/IpSecTransform:mCloseGuard	Ldalvik/system/CloseGuard;
    //   150: invokevirtual 221	dalvik/system/CloseGuard:close	()V
    //   153: aload_1
    //   154: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	155	0	this	IpSecTransform
    //   7	20	1	localStringBuilder1	StringBuilder
    //   82	1	1	localObject	Object
    //   86	31	1	localException	Exception
    //   135	19	1	localRemoteException	RemoteException
    //   90	34	2	localStringBuilder2	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   50	67	82	finally
    //   87	131	82	finally
    //   136	141	82	finally
    //   50	67	86	java/lang/Exception
    //   50	67	135	android/os/RemoteException
  }
  
  protected void finalize()
    throws Throwable
  {
    if (mCloseGuard != null) {
      mCloseGuard.warnIfOpen();
    }
    close();
  }
  
  IpSecConfig getConfig()
  {
    return mConfig;
  }
  
  @VisibleForTesting
  public int getResourceId()
  {
    return mResourceId;
  }
  
  public void startNattKeepalive(NattKeepaliveCallback paramNattKeepaliveCallback, int paramInt, Handler paramHandler)
    throws IOException
  {
    Preconditions.checkNotNull(paramNattKeepaliveCallback);
    if ((paramInt >= 20) && (paramInt <= 3600))
    {
      Preconditions.checkNotNull(paramHandler);
      if (mResourceId != -1) {
        synchronized (mKeepaliveCallback)
        {
          if (mKeepaliveCallback == null)
          {
            mUserKeepaliveCallback = paramNattKeepaliveCallback;
            mKeepalive = ((ConnectivityManager)mContext.getSystemService("connectivity")).startNattKeepalive(mConfig.getNetwork(), paramInt, mKeepaliveCallback, NetworkUtils.numericToInetAddress(mConfig.getSourceAddress()), 4500, NetworkUtils.numericToInetAddress(mConfig.getDestinationAddress()));
            mCallbackHandler = paramHandler;
            return;
          }
          paramNattKeepaliveCallback = new java/lang/IllegalStateException;
          paramNattKeepaliveCallback.<init>("Keepalive already active");
          throw paramNattKeepaliveCallback;
        }
      }
      throw new IllegalStateException("Packet keepalive cannot be started for an inactive transform");
    }
    throw new IllegalArgumentException("Invalid NAT-T keepalive interval");
  }
  
  public void stopNattKeepalive()
  {
    synchronized (mKeepaliveCallback)
    {
      if (mKeepalive == null)
      {
        Log.e("IpSecTransform", "No active keepalive to stop");
        return;
      }
      mKeepalive.stop();
      return;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IpSecTransform{resourceId=");
    localStringBuilder.append(mResourceId);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public static class Builder
  {
    private IpSecConfig mConfig;
    private Context mContext;
    
    public Builder(Context paramContext)
    {
      Preconditions.checkNotNull(paramContext);
      mContext = paramContext;
      mConfig = new IpSecConfig();
    }
    
    public IpSecTransform buildTransportModeTransform(InetAddress paramInetAddress, IpSecManager.SecurityParameterIndex paramSecurityParameterIndex)
      throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException, IOException
    {
      Preconditions.checkNotNull(paramInetAddress);
      Preconditions.checkNotNull(paramSecurityParameterIndex);
      if (paramSecurityParameterIndex.getResourceId() != -1)
      {
        mConfig.setMode(0);
        mConfig.setSourceAddress(paramInetAddress.getHostAddress());
        mConfig.setSpiResourceId(paramSecurityParameterIndex.getResourceId());
        return new IpSecTransform(mContext, mConfig).activate();
      }
      throw new IllegalArgumentException("Invalid SecurityParameterIndex");
    }
    
    public IpSecTransform buildTunnelModeTransform(InetAddress paramInetAddress, IpSecManager.SecurityParameterIndex paramSecurityParameterIndex)
      throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException, IOException
    {
      Preconditions.checkNotNull(paramInetAddress);
      Preconditions.checkNotNull(paramSecurityParameterIndex);
      if (paramSecurityParameterIndex.getResourceId() != -1)
      {
        mConfig.setMode(1);
        mConfig.setSourceAddress(paramInetAddress.getHostAddress());
        mConfig.setSpiResourceId(paramSecurityParameterIndex.getResourceId());
        return new IpSecTransform(mContext, mConfig).activate();
      }
      throw new IllegalArgumentException("Invalid SecurityParameterIndex");
    }
    
    public Builder setAuthenticatedEncryption(IpSecAlgorithm paramIpSecAlgorithm)
    {
      Preconditions.checkNotNull(paramIpSecAlgorithm);
      mConfig.setAuthenticatedEncryption(paramIpSecAlgorithm);
      return this;
    }
    
    public Builder setAuthentication(IpSecAlgorithm paramIpSecAlgorithm)
    {
      Preconditions.checkNotNull(paramIpSecAlgorithm);
      mConfig.setAuthentication(paramIpSecAlgorithm);
      return this;
    }
    
    public Builder setEncryption(IpSecAlgorithm paramIpSecAlgorithm)
    {
      Preconditions.checkNotNull(paramIpSecAlgorithm);
      mConfig.setEncryption(paramIpSecAlgorithm);
      return this;
    }
    
    public Builder setIpv4Encapsulation(IpSecManager.UdpEncapsulationSocket paramUdpEncapsulationSocket, int paramInt)
    {
      Preconditions.checkNotNull(paramUdpEncapsulationSocket);
      mConfig.setEncapType(2);
      if (paramUdpEncapsulationSocket.getResourceId() != -1)
      {
        mConfig.setEncapSocketResourceId(paramUdpEncapsulationSocket.getResourceId());
        mConfig.setEncapRemotePort(paramInt);
        return this;
      }
      throw new IllegalArgumentException("Invalid UdpEncapsulationSocket");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EncapType {}
  
  public static class NattKeepaliveCallback
  {
    public static final int ERROR_HARDWARE_ERROR = 3;
    public static final int ERROR_HARDWARE_UNSUPPORTED = 2;
    public static final int ERROR_INVALID_NETWORK = 1;
    
    public NattKeepaliveCallback() {}
    
    public void onError(int paramInt) {}
    
    public void onStarted() {}
    
    public void onStopped() {}
  }
}
