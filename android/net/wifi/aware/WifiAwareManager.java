package android.net.wifi.aware;

import android.content.Context;
import android.net.NetworkSpecifier;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.List;
import libcore.util.HexEncoding;

public class WifiAwareManager
{
  public static final String ACTION_WIFI_AWARE_STATE_CHANGED = "android.net.wifi.aware.action.WIFI_AWARE_STATE_CHANGED";
  private static final boolean DBG = false;
  private static final String TAG = "WifiAwareManager";
  private static final boolean VDBG = false;
  public static final int WIFI_AWARE_DATA_PATH_ROLE_INITIATOR = 0;
  public static final int WIFI_AWARE_DATA_PATH_ROLE_RESPONDER = 1;
  private final Context mContext;
  private final Object mLock = new Object();
  private final IWifiAwareManager mService;
  
  public WifiAwareManager(Context paramContext, IWifiAwareManager paramIWifiAwareManager)
  {
    mContext = paramContext;
    mService = paramIWifiAwareManager;
  }
  
  public void attach(AttachCallback paramAttachCallback, IdentityChangedListener paramIdentityChangedListener, Handler paramHandler)
  {
    attach(paramHandler, null, paramAttachCallback, paramIdentityChangedListener);
  }
  
  public void attach(AttachCallback paramAttachCallback, Handler paramHandler)
  {
    attach(paramHandler, null, paramAttachCallback, null);
  }
  
  public void attach(Handler paramHandler, ConfigRequest paramConfigRequest, AttachCallback paramAttachCallback, IdentityChangedListener paramIdentityChangedListener)
  {
    if (paramAttachCallback != null)
    {
      Object localObject = mLock;
      if (paramHandler == null) {
        try
        {
          paramHandler = Looper.getMainLooper();
        }
        finally
        {
          break label118;
        }
      } else {
        paramHandler = paramHandler.getLooper();
      }
      try
      {
        Binder localBinder = new android/os/Binder;
        localBinder.<init>();
        IWifiAwareManager localIWifiAwareManager = mService;
        String str = mContext.getOpPackageName();
        WifiAwareEventCallbackProxy localWifiAwareEventCallbackProxy = new android/net/wifi/aware/WifiAwareManager$WifiAwareEventCallbackProxy;
        localWifiAwareEventCallbackProxy.<init>(this, paramHandler, localBinder, paramAttachCallback, paramIdentityChangedListener);
        if (paramIdentityChangedListener != null) {}
        for (boolean bool = true;; bool = false) {
          break;
        }
        localIWifiAwareManager.connect(localBinder, str, localWifiAwareEventCallbackProxy, paramConfigRequest, bool);
        return;
      }
      catch (RemoteException paramHandler)
      {
        throw paramHandler.rethrowFromSystemServer();
      }
      label118:
      throw paramHandler;
    }
    throw new IllegalArgumentException("Null callback provided");
  }
  
  public NetworkSpecifier createNetworkSpecifier(int paramInt1, int paramInt2, int paramInt3, PeerHandle paramPeerHandle, byte[] paramArrayOfByte, String paramString)
  {
    if ((paramInt2 != 0) && (paramInt2 != 1)) {
      throw new IllegalArgumentException("createNetworkSpecifier: Invalid 'role' argument when creating a network specifier");
    }
    if (paramInt2 != 0) {
      if (WifiAwareUtils.isLegacyVersion(mContext, 28)) {
        break label46;
      }
    }
    if (paramPeerHandle != null)
    {
      label46:
      int i;
      if (paramPeerHandle == null) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if (paramPeerHandle != null) {
        j = peerId;
      } else {
        j = 0;
      }
      return new WifiAwareNetworkSpecifier(i, paramInt2, paramInt1, paramInt3, j, null, paramArrayOfByte, paramString, Process.myUid());
    }
    throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer handle - cannot be null");
  }
  
  public NetworkSpecifier createNetworkSpecifier(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString)
  {
    if ((paramInt2 != 0) && (paramInt2 != 1)) {
      throw new IllegalArgumentException("createNetworkSpecifier: Invalid 'role' argument when creating a network specifier");
    }
    if (paramInt2 != 0) {
      if (WifiAwareUtils.isLegacyVersion(mContext, 28)) {
        break label45;
      }
    }
    if (paramArrayOfByte1 != null)
    {
      label45:
      if ((paramArrayOfByte1 != null) && (paramArrayOfByte1.length != 6)) {
        throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer MAC address");
      }
      if (paramArrayOfByte1 == null) {}
      for (int i = 3;; i = 2) {
        break;
      }
      return new WifiAwareNetworkSpecifier(i, paramInt2, paramInt1, 0, 0, paramArrayOfByte1, paramArrayOfByte2, paramString, Process.myUid());
    }
    throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer MAC - cannot be null");
  }
  
  public void disconnect(int paramInt, Binder paramBinder)
  {
    try
    {
      mService.disconnect(paramInt, paramBinder);
      return;
    }
    catch (RemoteException paramBinder)
    {
      throw paramBinder.rethrowFromSystemServer();
    }
  }
  
  public Characteristics getCharacteristics()
  {
    try
    {
      Characteristics localCharacteristics = mService.getCharacteristics();
      return localCharacteristics;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isAvailable()
  {
    try
    {
      boolean bool = mService.isUsageEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void publish(int paramInt, Looper paramLooper, PublishConfig paramPublishConfig, DiscoverySessionCallback paramDiscoverySessionCallback)
  {
    if (paramDiscoverySessionCallback != null) {
      try
      {
        IWifiAwareManager localIWifiAwareManager = mService;
        String str = mContext.getOpPackageName();
        WifiAwareDiscoverySessionCallbackProxy localWifiAwareDiscoverySessionCallbackProxy = new android/net/wifi/aware/WifiAwareManager$WifiAwareDiscoverySessionCallbackProxy;
        localWifiAwareDiscoverySessionCallbackProxy.<init>(this, paramLooper, true, paramDiscoverySessionCallback, paramInt);
        localIWifiAwareManager.publish(str, paramInt, paramPublishConfig, localWifiAwareDiscoverySessionCallbackProxy);
        return;
      }
      catch (RemoteException paramLooper)
      {
        throw paramLooper.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("Null callback provided");
  }
  
  public void sendMessage(int paramInt1, int paramInt2, PeerHandle paramPeerHandle, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
  {
    if (paramPeerHandle != null) {
      try
      {
        mService.sendMessage(paramInt1, paramInt2, peerId, paramArrayOfByte, paramInt3, paramInt4);
        return;
      }
      catch (RemoteException paramPeerHandle)
      {
        throw paramPeerHandle.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("sendMessage: invalid peerHandle - must be non-null");
  }
  
  public void subscribe(int paramInt, Looper paramLooper, SubscribeConfig paramSubscribeConfig, DiscoverySessionCallback paramDiscoverySessionCallback)
  {
    if (paramDiscoverySessionCallback != null) {
      try
      {
        IWifiAwareManager localIWifiAwareManager = mService;
        String str = mContext.getOpPackageName();
        WifiAwareDiscoverySessionCallbackProxy localWifiAwareDiscoverySessionCallbackProxy = new android/net/wifi/aware/WifiAwareManager$WifiAwareDiscoverySessionCallbackProxy;
        localWifiAwareDiscoverySessionCallbackProxy.<init>(this, paramLooper, false, paramDiscoverySessionCallback, paramInt);
        localIWifiAwareManager.subscribe(str, paramInt, paramSubscribeConfig, localWifiAwareDiscoverySessionCallbackProxy);
        return;
      }
      catch (RemoteException paramLooper)
      {
        throw paramLooper.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("Null callback provided");
  }
  
  public void terminateSession(int paramInt1, int paramInt2)
  {
    try
    {
      mService.terminateSession(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void updatePublish(int paramInt1, int paramInt2, PublishConfig paramPublishConfig)
  {
    try
    {
      mService.updatePublish(paramInt1, paramInt2, paramPublishConfig);
      return;
    }
    catch (RemoteException paramPublishConfig)
    {
      throw paramPublishConfig.rethrowFromSystemServer();
    }
  }
  
  public void updateSubscribe(int paramInt1, int paramInt2, SubscribeConfig paramSubscribeConfig)
  {
    try
    {
      mService.updateSubscribe(paramInt1, paramInt2, paramSubscribeConfig);
      return;
    }
    catch (RemoteException paramSubscribeConfig)
    {
      throw paramSubscribeConfig.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DataPathRole {}
  
  private static class WifiAwareDiscoverySessionCallbackProxy
    extends IWifiAwareDiscoverySessionCallback.Stub
  {
    private static final int CALLBACK_MATCH = 4;
    private static final int CALLBACK_MATCH_WITH_DISTANCE = 8;
    private static final int CALLBACK_MESSAGE_RECEIVED = 7;
    private static final int CALLBACK_MESSAGE_SEND_FAIL = 6;
    private static final int CALLBACK_MESSAGE_SEND_SUCCESS = 5;
    private static final int CALLBACK_SESSION_CONFIG_FAIL = 2;
    private static final int CALLBACK_SESSION_CONFIG_SUCCESS = 1;
    private static final int CALLBACK_SESSION_STARTED = 0;
    private static final int CALLBACK_SESSION_TERMINATED = 3;
    private static final String MESSAGE_BUNDLE_KEY_MESSAGE = "message";
    private static final String MESSAGE_BUNDLE_KEY_MESSAGE2 = "message2";
    private final WeakReference<WifiAwareManager> mAwareManager;
    private final int mClientId;
    private final Handler mHandler;
    private final boolean mIsPublish;
    private final DiscoverySessionCallback mOriginalCallback;
    private DiscoverySession mSession;
    
    WifiAwareDiscoverySessionCallbackProxy(WifiAwareManager paramWifiAwareManager, Looper paramLooper, boolean paramBoolean, DiscoverySessionCallback paramDiscoverySessionCallback, int paramInt)
    {
      mAwareManager = new WeakReference(paramWifiAwareManager);
      mIsPublish = paramBoolean;
      mOriginalCallback = paramDiscoverySessionCallback;
      mClientId = paramInt;
      mHandler = new Handler(paramLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          if (mAwareManager.get() == null)
          {
            Log.w("WifiAwareManager", "WifiAwareDiscoverySessionCallbackProxy: handleMessage post GC");
            return;
          }
          switch (what)
          {
          default: 
            break;
          case 7: 
            mOriginalCallback.onMessageReceived(new PeerHandle(arg1), (byte[])obj);
            break;
          case 6: 
            mOriginalCallback.onMessageSendFailed(arg1);
            break;
          case 5: 
            mOriginalCallback.onMessageSendSucceeded(arg1);
            break;
          case 4: 
          case 8: 
            byte[] arrayOfByte = paramAnonymousMessage.getData().getByteArray("message2");
            Object localObject;
            try
            {
              localObject = new android/net/wifi/aware/TlvBufferUtils$TlvIterable;
              ((TlvBufferUtils.TlvIterable)localObject).<init>(0, 1, arrayOfByte);
              localObject = ((TlvBufferUtils.TlvIterable)localObject).toList();
            }
            catch (BufferOverflowException localBufferOverflowException)
            {
              localObject = null;
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("onServiceDiscovered: invalid match filter byte array '");
              localStringBuilder.append(new String(HexEncoding.encode(arrayOfByte)));
              localStringBuilder.append("' - cannot be parsed: e=");
              localStringBuilder.append(localBufferOverflowException);
              Log.e("WifiAwareManager", localStringBuilder.toString());
            }
            if (what == 4) {
              mOriginalCallback.onServiceDiscovered(new PeerHandle(arg1), paramAnonymousMessage.getData().getByteArray("message"), (List)localObject);
            } else {
              mOriginalCallback.onServiceDiscoveredWithinRange(new PeerHandle(arg1), paramAnonymousMessage.getData().getByteArray("message"), (List)localObject, arg2);
            }
            break;
          case 3: 
            onProxySessionTerminated(arg1);
            break;
          case 2: 
            mOriginalCallback.onSessionConfigFailed();
            if (mSession == null) {
              mAwareManager.clear();
            }
            break;
          case 1: 
            mOriginalCallback.onSessionConfigUpdated();
            break;
          case 0: 
            onProxySessionStarted(arg1);
          }
        }
      };
    }
    
    private void onMatchCommon(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt3)
    {
      Bundle localBundle = new Bundle();
      localBundle.putByteArray("message", paramArrayOfByte1);
      localBundle.putByteArray("message2", paramArrayOfByte2);
      paramArrayOfByte1 = mHandler.obtainMessage(paramInt1);
      arg1 = paramInt2;
      arg2 = paramInt3;
      paramArrayOfByte1.setData(localBundle);
      mHandler.sendMessage(paramArrayOfByte1);
    }
    
    public void onMatch(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      onMatchCommon(4, paramInt, paramArrayOfByte1, paramArrayOfByte2, 0);
    }
    
    public void onMatchWithDistance(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2)
    {
      onMatchCommon(8, paramInt1, paramArrayOfByte1, paramArrayOfByte2, paramInt2);
    }
    
    public void onMessageReceived(int paramInt, byte[] paramArrayOfByte)
    {
      Message localMessage = mHandler.obtainMessage(7);
      arg1 = paramInt;
      obj = paramArrayOfByte;
      mHandler.sendMessage(localMessage);
    }
    
    public void onMessageSendFail(int paramInt1, int paramInt2)
    {
      Message localMessage = mHandler.obtainMessage(6);
      arg1 = paramInt1;
      arg2 = paramInt2;
      mHandler.sendMessage(localMessage);
    }
    
    public void onMessageSendSuccess(int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(5);
      arg1 = paramInt;
      mHandler.sendMessage(localMessage);
    }
    
    public void onProxySessionStarted(int paramInt)
    {
      if (mSession == null)
      {
        localObject = (WifiAwareManager)mAwareManager.get();
        if (localObject == null)
        {
          Log.w("WifiAwareManager", "onProxySessionStarted: mgr GC'd");
          return;
        }
        if (mIsPublish)
        {
          localObject = new PublishDiscoverySession((WifiAwareManager)localObject, mClientId, paramInt);
          mSession = ((DiscoverySession)localObject);
          mOriginalCallback.onPublishStarted((PublishDiscoverySession)localObject);
        }
        else
        {
          localObject = new SubscribeDiscoverySession((WifiAwareManager)localObject, mClientId, paramInt);
          mSession = ((DiscoverySession)localObject);
          mOriginalCallback.onSubscribeStarted((SubscribeDiscoverySession)localObject);
        }
        return;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onSessionStarted: sessionId=");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(": session already created!?");
      Log.e("WifiAwareManager", ((StringBuilder)localObject).toString());
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onSessionStarted: sessionId=");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(": session already created!?");
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    
    public void onProxySessionTerminated(int paramInt)
    {
      if (mSession != null)
      {
        mSession.setTerminated();
        mSession = null;
      }
      else
      {
        Log.w("WifiAwareManager", "Proxy: onSessionTerminated called but mSession is null!?");
      }
      mAwareManager.clear();
      mOriginalCallback.onSessionTerminated();
    }
    
    public void onSessionConfigFail(int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(2);
      arg1 = paramInt;
      mHandler.sendMessage(localMessage);
    }
    
    public void onSessionConfigSuccess()
    {
      Message localMessage = mHandler.obtainMessage(1);
      mHandler.sendMessage(localMessage);
    }
    
    public void onSessionStarted(int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(0);
      arg1 = paramInt;
      mHandler.sendMessage(localMessage);
    }
    
    public void onSessionTerminated(int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(3);
      arg1 = paramInt;
      mHandler.sendMessage(localMessage);
    }
  }
  
  private static class WifiAwareEventCallbackProxy
    extends IWifiAwareEventCallback.Stub
  {
    private static final int CALLBACK_CONNECT_FAIL = 1;
    private static final int CALLBACK_CONNECT_SUCCESS = 0;
    private static final int CALLBACK_IDENTITY_CHANGED = 2;
    private final WeakReference<WifiAwareManager> mAwareManager;
    private final Binder mBinder;
    private final Handler mHandler;
    private final Looper mLooper;
    
    WifiAwareEventCallbackProxy(WifiAwareManager paramWifiAwareManager, Looper paramLooper, Binder paramBinder, final AttachCallback paramAttachCallback, final IdentityChangedListener paramIdentityChangedListener)
    {
      mAwareManager = new WeakReference(paramWifiAwareManager);
      mLooper = paramLooper;
      mBinder = paramBinder;
      mHandler = new Handler(paramLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          WifiAwareManager localWifiAwareManager = (WifiAwareManager)mAwareManager.get();
          if (localWifiAwareManager == null)
          {
            Log.w("WifiAwareManager", "WifiAwareEventCallbackProxy: handleMessage post GC");
            return;
          }
          switch (what)
          {
          default: 
            break;
          case 2: 
            if (paramIdentityChangedListener == null) {
              Log.e("WifiAwareManager", "CALLBACK_IDENTITY_CHANGED: null listener.");
            } else {
              paramIdentityChangedListener.onIdentityChanged((byte[])obj);
            }
            break;
          case 1: 
            mAwareManager.clear();
            paramAttachCallback.onAttachFailed();
            break;
          case 0: 
            paramAttachCallback.onAttached(new WifiAwareSession(localWifiAwareManager, mBinder, arg1));
          }
        }
      };
    }
    
    public void onConnectFail(int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(1);
      arg1 = paramInt;
      mHandler.sendMessage(localMessage);
    }
    
    public void onConnectSuccess(int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(0);
      arg1 = paramInt;
      mHandler.sendMessage(localMessage);
    }
    
    public void onIdentityChanged(byte[] paramArrayOfByte)
    {
      Message localMessage = mHandler.obtainMessage(2);
      obj = paramArrayOfByte;
      mHandler.sendMessage(localMessage);
    }
  }
}
