package android.net.lowpan;

import android.content.Context;
import android.net.IpPrefix;
import android.net.LinkAddress;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import java.util.HashMap;

public class LowpanInterface
{
  public static final String EMPTY_PARTITION_ID = "";
  public static final String NETWORK_TYPE_THREAD_V1 = "org.threadgroup.thread.v1";
  public static final String ROLE_COORDINATOR = "coordinator";
  public static final String ROLE_DETACHED = "detached";
  public static final String ROLE_END_DEVICE = "end-device";
  public static final String ROLE_LEADER = "leader";
  public static final String ROLE_ROUTER = "router";
  public static final String ROLE_SLEEPY_END_DEVICE = "sleepy-end-device";
  public static final String ROLE_SLEEPY_ROUTER = "sleepy-router";
  public static final String STATE_ATTACHED = "attached";
  public static final String STATE_ATTACHING = "attaching";
  public static final String STATE_COMMISSIONING = "commissioning";
  public static final String STATE_FAULT = "fault";
  public static final String STATE_OFFLINE = "offline";
  private static final String TAG = LowpanInterface.class.getSimpleName();
  private final ILowpanInterface mBinder;
  private final HashMap<Integer, ILowpanInterfaceListener> mListenerMap = new HashMap();
  private final Looper mLooper;
  
  public LowpanInterface(Context paramContext, ILowpanInterface paramILowpanInterface, Looper paramLooper)
  {
    mBinder = paramILowpanInterface;
    mLooper = paramLooper;
  }
  
  public void addExternalRoute(IpPrefix paramIpPrefix, int paramInt)
    throws LowpanException
  {
    try
    {
      mBinder.addExternalRoute(paramIpPrefix, paramInt);
      return;
    }
    catch (ServiceSpecificException paramIpPrefix)
    {
      throw LowpanException.rethrowFromServiceSpecificException(paramIpPrefix);
    }
    catch (RemoteException paramIpPrefix)
    {
      throw paramIpPrefix.rethrowAsRuntimeException();
    }
  }
  
  public void addOnMeshPrefix(IpPrefix paramIpPrefix, int paramInt)
    throws LowpanException
  {
    try
    {
      mBinder.addOnMeshPrefix(paramIpPrefix, paramInt);
      return;
    }
    catch (ServiceSpecificException paramIpPrefix)
    {
      throw LowpanException.rethrowFromServiceSpecificException(paramIpPrefix);
    }
    catch (RemoteException paramIpPrefix)
    {
      throw paramIpPrefix.rethrowAsRuntimeException();
    }
  }
  
  public void attach(LowpanProvision paramLowpanProvision)
    throws LowpanException
  {
    try
    {
      mBinder.attach(paramLowpanProvision);
      return;
    }
    catch (ServiceSpecificException paramLowpanProvision)
    {
      throw LowpanException.rethrowFromServiceSpecificException(paramLowpanProvision);
    }
    catch (RemoteException paramLowpanProvision)
    {
      throw paramLowpanProvision.rethrowAsRuntimeException();
    }
  }
  
  public LowpanScanner createScanner()
  {
    return new LowpanScanner(mBinder);
  }
  
  public void form(LowpanProvision paramLowpanProvision)
    throws LowpanException
  {
    try
    {
      mBinder.form(paramLowpanProvision);
      return;
    }
    catch (ServiceSpecificException paramLowpanProvision)
    {
      throw LowpanException.rethrowFromServiceSpecificException(paramLowpanProvision);
    }
    catch (RemoteException paramLowpanProvision)
    {
      throw paramLowpanProvision.rethrowAsRuntimeException();
    }
  }
  
  public LinkAddress[] getLinkAddresses()
    throws LowpanException
  {
    try
    {
      String[] arrayOfString = mBinder.getLinkAddresses();
      LinkAddress[] arrayOfLinkAddress = new LinkAddress[arrayOfString.length];
      int i = 0;
      int j = arrayOfString.length;
      int k = 0;
      while (k < j)
      {
        arrayOfLinkAddress[i] = new LinkAddress(arrayOfString[k]);
        k++;
        i++;
      }
      return arrayOfLinkAddress;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public IpPrefix[] getLinkNetworks()
    throws LowpanException
  {
    try
    {
      IpPrefix[] arrayOfIpPrefix = mBinder.getLinkNetworks();
      return arrayOfIpPrefix;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public LowpanCredential getLowpanCredential()
  {
    try
    {
      LowpanCredential localLowpanCredential = mBinder.getLowpanCredential();
      return localLowpanCredential;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public LowpanIdentity getLowpanIdentity()
  {
    try
    {
      LowpanIdentity localLowpanIdentity = mBinder.getLowpanIdentity();
      return localLowpanIdentity;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return new LowpanIdentity();
  }
  
  public String getName()
  {
    try
    {
      String str = mBinder.getName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return "";
  }
  
  public String getPartitionId()
  {
    try
    {
      String str = mBinder.getPartitionId();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return "";
  }
  
  public String getRole()
  {
    try
    {
      String str = mBinder.getRole();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return "detached";
  }
  
  public ILowpanInterface getService()
  {
    return mBinder;
  }
  
  public String getState()
  {
    try
    {
      String str = mBinder.getState();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return "fault";
  }
  
  public LowpanChannelInfo[] getSupportedChannels()
    throws LowpanException
  {
    try
    {
      LowpanChannelInfo[] arrayOfLowpanChannelInfo = mBinder.getSupportedChannels();
      return arrayOfLowpanChannelInfo;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public String[] getSupportedNetworkTypes()
    throws LowpanException
  {
    try
    {
      String[] arrayOfString = mBinder.getSupportedNetworkTypes();
      return arrayOfString;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public boolean isCommissioned()
  {
    try
    {
      boolean bool = mBinder.isCommissioned();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return false;
  }
  
  public boolean isConnected()
  {
    try
    {
      boolean bool = mBinder.isConnected();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return false;
  }
  
  public boolean isEnabled()
  {
    try
    {
      boolean bool = mBinder.isEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return false;
  }
  
  public boolean isUp()
  {
    try
    {
      boolean bool = mBinder.isUp();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
    catch (DeadObjectException localDeadObjectException) {}
    return false;
  }
  
  public void join(LowpanProvision paramLowpanProvision)
    throws LowpanException
  {
    try
    {
      mBinder.join(paramLowpanProvision);
      return;
    }
    catch (ServiceSpecificException paramLowpanProvision)
    {
      throw LowpanException.rethrowFromServiceSpecificException(paramLowpanProvision);
    }
    catch (RemoteException paramLowpanProvision)
    {
      throw paramLowpanProvision.rethrowAsRuntimeException();
    }
  }
  
  public void leave()
    throws LowpanException
  {
    try
    {
      mBinder.leave();
      return;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public void registerCallback(Callback paramCallback)
  {
    registerCallback(paramCallback, null);
  }
  
  /* Error */
  public void registerCallback(final Callback paramCallback, Handler arg2)
  {
    // Byte code:
    //   0: new 6	android/net/lowpan/LowpanInterface$1
    //   3: dup
    //   4: aload_0
    //   5: aload_2
    //   6: aload_1
    //   7: invokespecial 200	android/net/lowpan/LowpanInterface$1:<init>	(Landroid/net/lowpan/LowpanInterface;Landroid/os/Handler;Landroid/net/lowpan/LowpanInterface$Callback;)V
    //   10: astore_3
    //   11: aload_0
    //   12: getfield 82	android/net/lowpan/LowpanInterface:mBinder	Landroid/net/lowpan/ILowpanInterface;
    //   15: aload_3
    //   16: invokeinterface 204 2 0
    //   21: aload_0
    //   22: getfield 80	android/net/lowpan/LowpanInterface:mListenerMap	Ljava/util/HashMap;
    //   25: astore_2
    //   26: aload_2
    //   27: monitorenter
    //   28: aload_0
    //   29: getfield 80	android/net/lowpan/LowpanInterface:mListenerMap	Ljava/util/HashMap;
    //   32: aload_1
    //   33: invokestatic 210	java/lang/System:identityHashCode	(Ljava/lang/Object;)I
    //   36: invokestatic 216	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   39: aload_3
    //   40: invokevirtual 220	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   43: pop
    //   44: aload_2
    //   45: monitorexit
    //   46: return
    //   47: astore_1
    //   48: aload_2
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    //   52: astore_1
    //   53: aload_1
    //   54: invokevirtual 107	android/os/RemoteException:rethrowAsRuntimeException	()Ljava/lang/RuntimeException;
    //   57: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	58	0	this	LowpanInterface
    //   0	58	1	paramCallback	Callback
    //   10	30	3	local1	1
    // Exception table:
    //   from	to	target	type
    //   28	46	47	finally
    //   48	50	47	finally
    //   11	21	52	android/os/RemoteException
  }
  
  public void removeExternalRoute(IpPrefix paramIpPrefix)
  {
    try
    {
      try
      {
        mBinder.removeExternalRoute(paramIpPrefix);
      }
      catch (ServiceSpecificException paramIpPrefix)
      {
        Log.e(TAG, paramIpPrefix.toString());
      }
      return;
    }
    catch (RemoteException paramIpPrefix)
    {
      throw paramIpPrefix.rethrowAsRuntimeException();
    }
  }
  
  public void removeOnMeshPrefix(IpPrefix paramIpPrefix)
  {
    try
    {
      try
      {
        mBinder.removeOnMeshPrefix(paramIpPrefix);
      }
      catch (ServiceSpecificException paramIpPrefix)
      {
        Log.e(TAG, paramIpPrefix.toString());
      }
      return;
    }
    catch (RemoteException paramIpPrefix)
    {
      throw paramIpPrefix.rethrowAsRuntimeException();
    }
  }
  
  public void reset()
    throws LowpanException
  {
    try
    {
      mBinder.reset();
      return;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public void setEnabled(boolean paramBoolean)
    throws LowpanException
  {
    try
    {
      mBinder.setEnabled(paramBoolean);
      return;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public LowpanCommissioningSession startCommissioningSession(LowpanBeaconInfo paramLowpanBeaconInfo)
    throws LowpanException
  {
    try
    {
      mBinder.startCommissioningSession(paramLowpanBeaconInfo);
      paramLowpanBeaconInfo = new LowpanCommissioningSession(mBinder, paramLowpanBeaconInfo, mLooper);
      return paramLowpanBeaconInfo;
    }
    catch (ServiceSpecificException paramLowpanBeaconInfo)
    {
      throw LowpanException.rethrowFromServiceSpecificException(paramLowpanBeaconInfo);
    }
    catch (RemoteException paramLowpanBeaconInfo)
    {
      throw paramLowpanBeaconInfo.rethrowAsRuntimeException();
    }
  }
  
  public void unregisterCallback(Callback arg1)
  {
    int i = System.identityHashCode(???);
    synchronized (mListenerMap)
    {
      ILowpanInterfaceListener localILowpanInterfaceListener = (ILowpanInterfaceListener)mListenerMap.get(Integer.valueOf(i));
      if (localILowpanInterfaceListener != null)
      {
        mListenerMap.remove(Integer.valueOf(i));
        try
        {
          mBinder.removeListener(localILowpanInterfaceListener);
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowAsRuntimeException();
        }
        catch (DeadObjectException localDeadObjectException) {}
      }
      return;
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onConnectedChanged(boolean paramBoolean) {}
    
    public void onEnabledChanged(boolean paramBoolean) {}
    
    public void onLinkAddressAdded(LinkAddress paramLinkAddress) {}
    
    public void onLinkAddressRemoved(LinkAddress paramLinkAddress) {}
    
    public void onLinkNetworkAdded(IpPrefix paramIpPrefix) {}
    
    public void onLinkNetworkRemoved(IpPrefix paramIpPrefix) {}
    
    public void onLowpanIdentityChanged(LowpanIdentity paramLowpanIdentity) {}
    
    public void onRoleChanged(String paramString) {}
    
    public void onStateChanged(String paramString) {}
    
    public void onUpChanged(boolean paramBoolean) {}
  }
}
