package android.net.wifi.p2p;

import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceResponse;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import dalvik.system.CloseGuard;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiP2pManager
{
  public static final int ADD_LOCAL_SERVICE = 139292;
  public static final int ADD_LOCAL_SERVICE_FAILED = 139293;
  public static final int ADD_LOCAL_SERVICE_SUCCEEDED = 139294;
  public static final int ADD_SERVICE_REQUEST = 139301;
  public static final int ADD_SERVICE_REQUEST_FAILED = 139302;
  public static final int ADD_SERVICE_REQUEST_SUCCEEDED = 139303;
  private static final int BASE = 139264;
  public static final int BUSY = 2;
  public static final String CALLING_PACKAGE = "android.net.wifi.p2p.CALLING_PACKAGE";
  public static final int CANCEL_CONNECT = 139274;
  public static final int CANCEL_CONNECT_FAILED = 139275;
  public static final int CANCEL_CONNECT_SUCCEEDED = 139276;
  public static final int CLEAR_LOCAL_SERVICES = 139298;
  public static final int CLEAR_LOCAL_SERVICES_FAILED = 139299;
  public static final int CLEAR_LOCAL_SERVICES_SUCCEEDED = 139300;
  public static final int CLEAR_SERVICE_REQUESTS = 139307;
  public static final int CLEAR_SERVICE_REQUESTS_FAILED = 139308;
  public static final int CLEAR_SERVICE_REQUESTS_SUCCEEDED = 139309;
  public static final int CONNECT = 139271;
  public static final int CONNECT_FAILED = 139272;
  public static final int CONNECT_SUCCEEDED = 139273;
  public static final int CREATE_GROUP = 139277;
  public static final int CREATE_GROUP_FAILED = 139278;
  public static final int CREATE_GROUP_SUCCEEDED = 139279;
  public static final int DELETE_PERSISTENT_GROUP = 139318;
  public static final int DELETE_PERSISTENT_GROUP_FAILED = 139319;
  public static final int DELETE_PERSISTENT_GROUP_SUCCEEDED = 139320;
  public static final int DISCOVER_PEERS = 139265;
  public static final int DISCOVER_PEERS_FAILED = 139266;
  public static final int DISCOVER_PEERS_SUCCEEDED = 139267;
  public static final int DISCOVER_SERVICES = 139310;
  public static final int DISCOVER_SERVICES_FAILED = 139311;
  public static final int DISCOVER_SERVICES_SUCCEEDED = 139312;
  public static final int ERROR = 0;
  public static final String EXTRA_DISCOVERY_STATE = "discoveryState";
  public static final String EXTRA_HANDOVER_MESSAGE = "android.net.wifi.p2p.EXTRA_HANDOVER_MESSAGE";
  public static final String EXTRA_NETWORK_INFO = "networkInfo";
  public static final String EXTRA_P2P_DEVICE_LIST = "wifiP2pDeviceList";
  public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";
  public static final String EXTRA_WIFI_P2P_GROUP = "p2pGroupInfo";
  public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";
  public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";
  public static final int GET_HANDOVER_REQUEST = 139339;
  public static final int GET_HANDOVER_SELECT = 139340;
  public static final int INITIATOR_REPORT_NFC_HANDOVER = 139342;
  public static final int MIRACAST_DISABLED = 0;
  public static final int MIRACAST_SINK = 2;
  public static final int MIRACAST_SOURCE = 1;
  public static final int NO_SERVICE_REQUESTS = 3;
  public static final int P2P_UNSUPPORTED = 1;
  public static final int PING = 139313;
  public static final int REMOVE_GROUP = 139280;
  public static final int REMOVE_GROUP_FAILED = 139281;
  public static final int REMOVE_GROUP_SUCCEEDED = 139282;
  public static final int REMOVE_LOCAL_SERVICE = 139295;
  public static final int REMOVE_LOCAL_SERVICE_FAILED = 139296;
  public static final int REMOVE_LOCAL_SERVICE_SUCCEEDED = 139297;
  public static final int REMOVE_SERVICE_REQUEST = 139304;
  public static final int REMOVE_SERVICE_REQUEST_FAILED = 139305;
  public static final int REMOVE_SERVICE_REQUEST_SUCCEEDED = 139306;
  public static final int REPORT_NFC_HANDOVER_FAILED = 139345;
  public static final int REPORT_NFC_HANDOVER_SUCCEEDED = 139344;
  public static final int REQUEST_CONNECTION_INFO = 139285;
  public static final int REQUEST_GROUP_INFO = 139287;
  public static final int REQUEST_PEERS = 139283;
  public static final int REQUEST_PERSISTENT_GROUP_INFO = 139321;
  public static final int RESPONDER_REPORT_NFC_HANDOVER = 139343;
  public static final int RESPONSE_CONNECTION_INFO = 139286;
  public static final int RESPONSE_GET_HANDOVER_MESSAGE = 139341;
  public static final int RESPONSE_GROUP_INFO = 139288;
  public static final int RESPONSE_PEERS = 139284;
  public static final int RESPONSE_PERSISTENT_GROUP_INFO = 139322;
  public static final int RESPONSE_SERVICE = 139314;
  public static final int SET_CHANNEL = 139335;
  public static final int SET_CHANNEL_FAILED = 139336;
  public static final int SET_CHANNEL_SUCCEEDED = 139337;
  public static final int SET_DEVICE_NAME = 139315;
  public static final int SET_DEVICE_NAME_FAILED = 139316;
  public static final int SET_DEVICE_NAME_SUCCEEDED = 139317;
  public static final int SET_WFDR2_INFO = 139346;
  public static final int SET_WFDR2_INFO_FAILED = 139347;
  public static final int SET_WFDR2_INFO_SUCCEEDED = 139348;
  public static final int SET_WFD_INFO = 139323;
  public static final int SET_WFD_INFO_FAILED = 139324;
  public static final int SET_WFD_INFO_SUCCEEDED = 139325;
  public static final int START_LISTEN = 139329;
  public static final int START_LISTEN_FAILED = 139330;
  public static final int START_LISTEN_SUCCEEDED = 139331;
  public static final int START_WPS = 139326;
  public static final int START_WPS_FAILED = 139327;
  public static final int START_WPS_SUCCEEDED = 139328;
  public static final int STOP_DISCOVERY = 139268;
  public static final int STOP_DISCOVERY_FAILED = 139269;
  public static final int STOP_DISCOVERY_SUCCEEDED = 139270;
  public static final int STOP_LISTEN = 139332;
  public static final int STOP_LISTEN_FAILED = 139333;
  public static final int STOP_LISTEN_SUCCEEDED = 139334;
  private static final String TAG = "WifiP2pManager";
  public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION = "android.net.wifi.p2p.CONNECTION_STATE_CHANGE";
  public static final String WIFI_P2P_DISCOVERY_CHANGED_ACTION = "android.net.wifi.p2p.DISCOVERY_STATE_CHANGE";
  public static final int WIFI_P2P_DISCOVERY_STARTED = 2;
  public static final int WIFI_P2P_DISCOVERY_STOPPED = 1;
  public static final String WIFI_P2P_PEERS_CHANGED_ACTION = "android.net.wifi.p2p.PEERS_CHANGED";
  public static final String WIFI_P2P_PERSISTENT_GROUPS_CHANGED_ACTION = "android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED";
  public static final String WIFI_P2P_STATE_CHANGED_ACTION = "android.net.wifi.p2p.STATE_CHANGED";
  public static final int WIFI_P2P_STATE_DISABLED = 1;
  public static final int WIFI_P2P_STATE_ENABLED = 2;
  public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION = "android.net.wifi.p2p.THIS_DEVICE_CHANGED";
  IWifiP2pManager mService;
  
  public WifiP2pManager(IWifiP2pManager paramIWifiP2pManager)
  {
    mService = paramIWifiP2pManager;
  }
  
  private static void checkChannel(Channel paramChannel)
  {
    if (paramChannel != null) {
      return;
    }
    throw new IllegalArgumentException("Channel needs to be initialized");
  }
  
  private static void checkP2pConfig(WifiP2pConfig paramWifiP2pConfig)
  {
    if (paramWifiP2pConfig != null)
    {
      if (!TextUtils.isEmpty(deviceAddress)) {
        return;
      }
      throw new IllegalArgumentException("deviceAddress cannot be empty");
    }
    throw new IllegalArgumentException("config cannot be null");
  }
  
  private static void checkServiceInfo(WifiP2pServiceInfo paramWifiP2pServiceInfo)
  {
    if (paramWifiP2pServiceInfo != null) {
      return;
    }
    throw new IllegalArgumentException("service info is null");
  }
  
  private static void checkServiceRequest(WifiP2pServiceRequest paramWifiP2pServiceRequest)
  {
    if (paramWifiP2pServiceRequest != null) {
      return;
    }
    throw new IllegalArgumentException("service request is null");
  }
  
  private Channel initalizeChannel(Context paramContext, Looper paramLooper, ChannelListener paramChannelListener, Messenger paramMessenger, Binder paramBinder)
  {
    if (paramMessenger == null) {
      return null;
    }
    paramLooper = new Channel(paramContext, paramLooper, paramChannelListener, paramBinder, this);
    if (mAsyncChannel.connectSync(paramContext, mHandler, paramMessenger) == 0) {
      return paramLooper;
    }
    paramLooper.close();
    return null;
  }
  
  public void addLocalService(Channel paramChannel, WifiP2pServiceInfo paramWifiP2pServiceInfo, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    checkServiceInfo(paramWifiP2pServiceInfo);
    mAsyncChannel.sendMessage(139292, 0, paramChannel.putListener(paramActionListener), paramWifiP2pServiceInfo);
  }
  
  public void addServiceRequest(Channel paramChannel, WifiP2pServiceRequest paramWifiP2pServiceRequest, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    checkServiceRequest(paramWifiP2pServiceRequest);
    mAsyncChannel.sendMessage(139301, 0, paramChannel.putListener(paramActionListener), paramWifiP2pServiceRequest);
  }
  
  public void cancelConnect(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139274, 0, paramChannel.putListener(paramActionListener));
  }
  
  public void clearLocalServices(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139298, 0, paramChannel.putListener(paramActionListener));
  }
  
  public void clearServiceRequests(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139307, 0, paramChannel.putListener(paramActionListener));
  }
  
  public void connect(Channel paramChannel, WifiP2pConfig paramWifiP2pConfig, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    checkP2pConfig(paramWifiP2pConfig);
    mAsyncChannel.sendMessage(139271, 0, paramChannel.putListener(paramActionListener), paramWifiP2pConfig);
  }
  
  public void createGroup(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139277, -2, paramChannel.putListener(paramActionListener));
  }
  
  public void deletePersistentGroup(Channel paramChannel, int paramInt, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139318, paramInt, paramChannel.putListener(paramActionListener));
  }
  
  public void discoverPeers(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139265, 0, paramChannel.putListener(paramActionListener));
  }
  
  public void discoverServices(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139310, 0, paramChannel.putListener(paramActionListener));
  }
  
  public Messenger getMessenger(Binder paramBinder)
  {
    try
    {
      paramBinder = mService.getMessenger(paramBinder);
      return paramBinder;
    }
    catch (RemoteException paramBinder)
    {
      throw paramBinder.rethrowFromSystemServer();
    }
  }
  
  public void getNfcHandoverRequest(Channel paramChannel, HandoverMessageListener paramHandoverMessageListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139339, 0, paramChannel.putListener(paramHandoverMessageListener));
  }
  
  public void getNfcHandoverSelect(Channel paramChannel, HandoverMessageListener paramHandoverMessageListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139340, 0, paramChannel.putListener(paramHandoverMessageListener));
  }
  
  public Messenger getP2pStateMachineMessenger()
  {
    try
    {
      Messenger localMessenger = mService.getP2pStateMachineMessenger();
      return localMessenger;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Channel initialize(Context paramContext, Looper paramLooper, ChannelListener paramChannelListener)
  {
    Binder localBinder = new Binder();
    return initalizeChannel(paramContext, paramLooper, paramChannelListener, getMessenger(localBinder), localBinder);
  }
  
  public Channel initializeInternal(Context paramContext, Looper paramLooper, ChannelListener paramChannelListener)
  {
    return initalizeChannel(paramContext, paramLooper, paramChannelListener, getP2pStateMachineMessenger(), null);
  }
  
  public void initiatorReportNfcHandover(Channel paramChannel, String paramString, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    Bundle localBundle = new Bundle();
    localBundle.putString("android.net.wifi.p2p.EXTRA_HANDOVER_MESSAGE", paramString);
    mAsyncChannel.sendMessage(139342, 0, paramChannel.putListener(paramActionListener), localBundle);
  }
  
  public void listen(Channel paramChannel, boolean paramBoolean, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    AsyncChannel localAsyncChannel = mAsyncChannel;
    int i;
    if (paramBoolean) {
      i = 139329;
    } else {
      i = 139332;
    }
    localAsyncChannel.sendMessage(i, 0, paramChannel.putListener(paramActionListener));
  }
  
  public void removeGroup(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139280, 0, paramChannel.putListener(paramActionListener));
  }
  
  public void removeLocalService(Channel paramChannel, WifiP2pServiceInfo paramWifiP2pServiceInfo, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    checkServiceInfo(paramWifiP2pServiceInfo);
    mAsyncChannel.sendMessage(139295, 0, paramChannel.putListener(paramActionListener), paramWifiP2pServiceInfo);
  }
  
  public void removeServiceRequest(Channel paramChannel, WifiP2pServiceRequest paramWifiP2pServiceRequest, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    checkServiceRequest(paramWifiP2pServiceRequest);
    mAsyncChannel.sendMessage(139304, 0, paramChannel.putListener(paramActionListener), paramWifiP2pServiceRequest);
  }
  
  public void requestConnectionInfo(Channel paramChannel, ConnectionInfoListener paramConnectionInfoListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139285, 0, paramChannel.putListener(paramConnectionInfoListener));
  }
  
  public void requestGroupInfo(Channel paramChannel, GroupInfoListener paramGroupInfoListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139287, 0, paramChannel.putListener(paramGroupInfoListener));
  }
  
  public void requestPeers(Channel paramChannel, PeerListListener paramPeerListListener)
  {
    checkChannel(paramChannel);
    Bundle localBundle = new Bundle();
    localBundle.putString("android.net.wifi.p2p.CALLING_PACKAGE", mContext.getOpPackageName());
    mAsyncChannel.sendMessage(139283, 0, paramChannel.putListener(paramPeerListListener), localBundle);
  }
  
  public void requestPersistentGroupInfo(Channel paramChannel, PersistentGroupInfoListener paramPersistentGroupInfoListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139321, 0, paramChannel.putListener(paramPersistentGroupInfoListener));
  }
  
  public void responderReportNfcHandover(Channel paramChannel, String paramString, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    Bundle localBundle = new Bundle();
    localBundle.putString("android.net.wifi.p2p.EXTRA_HANDOVER_MESSAGE", paramString);
    mAsyncChannel.sendMessage(139343, 0, paramChannel.putListener(paramActionListener), localBundle);
  }
  
  public void setDeviceName(Channel paramChannel, String paramString, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    WifiP2pDevice localWifiP2pDevice = new WifiP2pDevice();
    deviceName = paramString;
    mAsyncChannel.sendMessage(139315, 0, paramChannel.putListener(paramActionListener), localWifiP2pDevice);
  }
  
  public void setDnsSdResponseListeners(Channel paramChannel, DnsSdServiceResponseListener paramDnsSdServiceResponseListener, DnsSdTxtRecordListener paramDnsSdTxtRecordListener)
  {
    checkChannel(paramChannel);
    Channel.access$702(paramChannel, paramDnsSdServiceResponseListener);
    Channel.access$802(paramChannel, paramDnsSdTxtRecordListener);
  }
  
  public void setMiracastMode(int paramInt)
  {
    try
    {
      mService.setMiracastMode(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setServiceResponseListener(Channel paramChannel, ServiceResponseListener paramServiceResponseListener)
  {
    checkChannel(paramChannel);
    Channel.access$602(paramChannel, paramServiceResponseListener);
  }
  
  public void setUpnpServiceResponseListener(Channel paramChannel, UpnpServiceResponseListener paramUpnpServiceResponseListener)
  {
    checkChannel(paramChannel);
    Channel.access$902(paramChannel, paramUpnpServiceResponseListener);
  }
  
  public void setWFDInfo(Channel paramChannel, WifiP2pWfdInfo paramWifiP2pWfdInfo, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    try
    {
      mService.checkConfigureWifiDisplayPermission();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    mAsyncChannel.sendMessage(139323, 0, paramChannel.putListener(paramActionListener), paramWifiP2pWfdInfo);
  }
  
  public void setWFDR2Info(Channel paramChannel, WifiP2pWfdInfo paramWifiP2pWfdInfo, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    try
    {
      mService.checkConfigureWifiDisplayPermission();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    mAsyncChannel.sendMessage(139346, 0, paramChannel.putListener(paramActionListener), paramWifiP2pWfdInfo);
  }
  
  public void setWifiP2pChannels(Channel paramChannel, int paramInt1, int paramInt2, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    Bundle localBundle = new Bundle();
    localBundle.putInt("lc", paramInt1);
    localBundle.putInt("oc", paramInt2);
    mAsyncChannel.sendMessage(139335, 0, paramChannel.putListener(paramActionListener), localBundle);
  }
  
  public void startWps(Channel paramChannel, WpsInfo paramWpsInfo, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139326, 0, paramChannel.putListener(paramActionListener), paramWpsInfo);
  }
  
  public void stopPeerDiscovery(Channel paramChannel, ActionListener paramActionListener)
  {
    checkChannel(paramChannel);
    mAsyncChannel.sendMessage(139268, 0, paramChannel.putListener(paramActionListener));
  }
  
  public static abstract interface ActionListener
  {
    public abstract void onFailure(int paramInt);
    
    public abstract void onSuccess();
  }
  
  public static class Channel
    implements AutoCloseable
  {
    private static final int INVALID_LISTENER_KEY = 0;
    private AsyncChannel mAsyncChannel = new AsyncChannel();
    final Binder mBinder;
    private WifiP2pManager.ChannelListener mChannelListener;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    Context mContext;
    private WifiP2pManager.DnsSdServiceResponseListener mDnsSdServRspListener;
    private WifiP2pManager.DnsSdTxtRecordListener mDnsSdTxtListener;
    private P2pHandler mHandler;
    private int mListenerKey = 0;
    private HashMap<Integer, Object> mListenerMap = new HashMap();
    private final Object mListenerMapLock = new Object();
    private final WifiP2pManager mP2pManager;
    private WifiP2pManager.ServiceResponseListener mServRspListener;
    private WifiP2pManager.UpnpServiceResponseListener mUpnpServRspListener;
    
    public Channel(Context paramContext, Looper paramLooper, WifiP2pManager.ChannelListener paramChannelListener, Binder paramBinder, WifiP2pManager paramWifiP2pManager)
    {
      mHandler = new P2pHandler(paramLooper);
      mChannelListener = paramChannelListener;
      mContext = paramContext;
      mBinder = paramBinder;
      mP2pManager = paramWifiP2pManager;
      mCloseGuard.open("close");
    }
    
    private Object getListener(int paramInt)
    {
      if (paramInt == 0) {
        return null;
      }
      synchronized (mListenerMapLock)
      {
        Object localObject2 = mListenerMap.remove(Integer.valueOf(paramInt));
        return localObject2;
      }
    }
    
    private void handleDnsSdServiceResponse(WifiP2pDnsSdServiceResponse paramWifiP2pDnsSdServiceResponse)
    {
      if (paramWifiP2pDnsSdServiceResponse.getDnsType() == 12)
      {
        if (mDnsSdServRspListener != null) {
          mDnsSdServRspListener.onDnsSdServiceAvailable(paramWifiP2pDnsSdServiceResponse.getInstanceName(), paramWifiP2pDnsSdServiceResponse.getDnsQueryName(), paramWifiP2pDnsSdServiceResponse.getSrcDevice());
        }
      }
      else if (paramWifiP2pDnsSdServiceResponse.getDnsType() == 16)
      {
        if (mDnsSdTxtListener != null) {
          mDnsSdTxtListener.onDnsSdTxtRecordAvailable(paramWifiP2pDnsSdServiceResponse.getDnsQueryName(), paramWifiP2pDnsSdServiceResponse.getTxtRecord(), paramWifiP2pDnsSdServiceResponse.getSrcDevice());
        }
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unhandled resp ");
        localStringBuilder.append(paramWifiP2pDnsSdServiceResponse);
        Log.e("WifiP2pManager", localStringBuilder.toString());
      }
    }
    
    private void handleServiceResponse(WifiP2pServiceResponse paramWifiP2pServiceResponse)
    {
      if ((paramWifiP2pServiceResponse instanceof WifiP2pDnsSdServiceResponse)) {
        handleDnsSdServiceResponse((WifiP2pDnsSdServiceResponse)paramWifiP2pServiceResponse);
      } else if ((paramWifiP2pServiceResponse instanceof WifiP2pUpnpServiceResponse))
      {
        if (mUpnpServRspListener != null) {
          handleUpnpServiceResponse((WifiP2pUpnpServiceResponse)paramWifiP2pServiceResponse);
        }
      }
      else if (mServRspListener != null) {
        mServRspListener.onServiceAvailable(paramWifiP2pServiceResponse.getServiceType(), paramWifiP2pServiceResponse.getRawData(), paramWifiP2pServiceResponse.getSrcDevice());
      }
    }
    
    private void handleUpnpServiceResponse(WifiP2pUpnpServiceResponse paramWifiP2pUpnpServiceResponse)
    {
      mUpnpServRspListener.onUpnpServiceAvailable(paramWifiP2pUpnpServiceResponse.getUniqueServiceNames(), paramWifiP2pUpnpServiceResponse.getSrcDevice());
    }
    
    private int putListener(Object paramObject)
    {
      if (paramObject == null) {
        return 0;
      }
      synchronized (mListenerMapLock)
      {
        int i;
        do
        {
          i = mListenerKey;
          mListenerKey = (i + 1);
        } while (i == 0);
        mListenerMap.put(Integer.valueOf(i), paramObject);
        return i;
      }
    }
    
    public void close()
    {
      if (mP2pManager == null) {
        Log.w("WifiP2pManager", "Channel.close(): Null mP2pManager!?");
      }
      try
      {
        mP2pManager.mService.close(mBinder);
        mAsyncChannel.disconnect();
        mCloseGuard.close();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (mCloseGuard != null) {
          mCloseGuard.warnIfOpen();
        }
        close();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
    
    class P2pHandler
      extends Handler
    {
      P2pHandler(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        Object localObject = WifiP2pManager.Channel.this.getListener(arg2);
        int i = what;
        StringBuilder localStringBuilder = null;
        switch (i)
        {
        default: 
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Ignored ");
          localStringBuilder.append(paramMessage);
          Log.d("WifiP2pManager", localStringBuilder.toString());
          break;
        case 139341: 
          paramMessage = (Bundle)obj;
          if (localObject != null)
          {
            if (paramMessage != null) {
              paramMessage = paramMessage.getString("android.net.wifi.p2p.EXTRA_HANDOVER_MESSAGE");
            } else {
              paramMessage = localStringBuilder;
            }
            ((WifiP2pManager.HandoverMessageListener)localObject).onHandoverMessageAvailable(paramMessage);
          }
          break;
        case 139322: 
          paramMessage = (WifiP2pGroupList)obj;
          if (localObject != null) {
            ((WifiP2pManager.PersistentGroupInfoListener)localObject).onPersistentGroupInfoAvailable(paramMessage);
          }
          break;
        case 139314: 
          paramMessage = (WifiP2pServiceResponse)obj;
          WifiP2pManager.Channel.this.handleServiceResponse(paramMessage);
          break;
        case 139288: 
          paramMessage = (WifiP2pGroup)obj;
          if (localObject != null) {
            ((WifiP2pManager.GroupInfoListener)localObject).onGroupInfoAvailable(paramMessage);
          }
          break;
        case 139286: 
          paramMessage = (WifiP2pInfo)obj;
          if (localObject != null) {
            ((WifiP2pManager.ConnectionInfoListener)localObject).onConnectionInfoAvailable(paramMessage);
          }
          break;
        case 139284: 
          paramMessage = (WifiP2pDeviceList)obj;
          if (localObject != null) {
            ((WifiP2pManager.PeerListListener)localObject).onPeersAvailable(paramMessage);
          }
          break;
        case 139267: 
        case 139270: 
        case 139273: 
        case 139276: 
        case 139279: 
        case 139282: 
        case 139294: 
        case 139297: 
        case 139300: 
        case 139303: 
        case 139306: 
        case 139309: 
        case 139312: 
        case 139317: 
        case 139320: 
        case 139325: 
        case 139328: 
        case 139331: 
        case 139334: 
        case 139337: 
        case 139344: 
        case 139348: 
          if (localObject != null) {
            ((WifiP2pManager.ActionListener)localObject).onSuccess();
          }
          break;
        case 139266: 
        case 139269: 
        case 139272: 
        case 139275: 
        case 139278: 
        case 139281: 
        case 139293: 
        case 139296: 
        case 139299: 
        case 139302: 
        case 139305: 
        case 139308: 
        case 139311: 
        case 139316: 
        case 139319: 
        case 139324: 
        case 139327: 
        case 139330: 
        case 139333: 
        case 139336: 
        case 139345: 
        case 139347: 
          if (localObject != null) {
            ((WifiP2pManager.ActionListener)localObject).onFailure(arg1);
          }
          break;
        case 69636: 
          if (mChannelListener != null)
          {
            mChannelListener.onChannelDisconnected();
            WifiP2pManager.Channel.access$102(WifiP2pManager.Channel.this, null);
          }
          break;
        }
      }
    }
  }
  
  public static abstract interface ChannelListener
  {
    public abstract void onChannelDisconnected();
  }
  
  public static abstract interface ConnectionInfoListener
  {
    public abstract void onConnectionInfoAvailable(WifiP2pInfo paramWifiP2pInfo);
  }
  
  public static abstract interface DnsSdServiceResponseListener
  {
    public abstract void onDnsSdServiceAvailable(String paramString1, String paramString2, WifiP2pDevice paramWifiP2pDevice);
  }
  
  public static abstract interface DnsSdTxtRecordListener
  {
    public abstract void onDnsSdTxtRecordAvailable(String paramString, Map<String, String> paramMap, WifiP2pDevice paramWifiP2pDevice);
  }
  
  public static abstract interface GroupInfoListener
  {
    public abstract void onGroupInfoAvailable(WifiP2pGroup paramWifiP2pGroup);
  }
  
  public static abstract interface HandoverMessageListener
  {
    public abstract void onHandoverMessageAvailable(String paramString);
  }
  
  public static abstract interface PeerListListener
  {
    public abstract void onPeersAvailable(WifiP2pDeviceList paramWifiP2pDeviceList);
  }
  
  public static abstract interface PersistentGroupInfoListener
  {
    public abstract void onPersistentGroupInfoAvailable(WifiP2pGroupList paramWifiP2pGroupList);
  }
  
  public static abstract interface ServiceResponseListener
  {
    public abstract void onServiceAvailable(int paramInt, byte[] paramArrayOfByte, WifiP2pDevice paramWifiP2pDevice);
  }
  
  public static abstract interface UpnpServiceResponseListener
  {
    public abstract void onUpnpServiceAvailable(List<String> paramList, WifiP2pDevice paramWifiP2pDevice);
  }
}
