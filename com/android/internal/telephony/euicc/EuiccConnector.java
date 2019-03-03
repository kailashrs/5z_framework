package com.android.internal.telephony.euicc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.service.euicc.GetDefaultDownloadableSubscriptionListResult;
import android.service.euicc.GetDownloadableSubscriptionMetadataResult;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.service.euicc.IDeleteSubscriptionCallback;
import android.service.euicc.IDeleteSubscriptionCallback.Stub;
import android.service.euicc.IDownloadSubscriptionCallback;
import android.service.euicc.IDownloadSubscriptionCallback.Stub;
import android.service.euicc.IEraseSubscriptionsCallback;
import android.service.euicc.IEraseSubscriptionsCallback.Stub;
import android.service.euicc.IEuiccService;
import android.service.euicc.IEuiccService.Stub;
import android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback;
import android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback.Stub;
import android.service.euicc.IGetDownloadableSubscriptionMetadataCallback;
import android.service.euicc.IGetDownloadableSubscriptionMetadataCallback.Stub;
import android.service.euicc.IGetEidCallback;
import android.service.euicc.IGetEidCallback.Stub;
import android.service.euicc.IGetEuiccInfoCallback;
import android.service.euicc.IGetEuiccInfoCallback.Stub;
import android.service.euicc.IGetEuiccProfileInfoListCallback;
import android.service.euicc.IGetEuiccProfileInfoListCallback.Stub;
import android.service.euicc.IGetOtaStatusCallback;
import android.service.euicc.IGetOtaStatusCallback.Stub;
import android.service.euicc.IOtaStatusChangedCallback.Stub;
import android.service.euicc.IRetainSubscriptionsForFactoryResetCallback.Stub;
import android.service.euicc.ISwitchToSubscriptionCallback;
import android.service.euicc.ISwitchToSubscriptionCallback.Stub;
import android.service.euicc.IUpdateSubscriptionNicknameCallback.Stub;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.content.PackageMonitor;
import com.android.internal.util.IState;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EuiccConnector
  extends StateMachine
  implements ServiceConnection
{
  private static final int BIND_TIMEOUT_MILLIS = 30000;
  private static final int CMD_COMMAND_COMPLETE = 6;
  private static final int CMD_CONNECT_TIMEOUT = 2;
  private static final int CMD_DELETE_SUBSCRIPTION = 106;
  private static final int CMD_DOWNLOAD_SUBSCRIPTION = 102;
  private static final int CMD_ERASE_SUBSCRIPTIONS = 109;
  private static final int CMD_GET_DEFAULT_DOWNLOADABLE_SUBSCRIPTION_LIST = 104;
  private static final int CMD_GET_DOWNLOADABLE_SUBSCRIPTION_METADATA = 101;
  private static final int CMD_GET_EID = 100;
  private static final int CMD_GET_EUICC_INFO = 105;
  private static final int CMD_GET_EUICC_PROFILE_INFO_LIST = 103;
  private static final int CMD_GET_OTA_STATUS = 111;
  private static final int CMD_LINGER_TIMEOUT = 3;
  private static final int CMD_PACKAGE_CHANGE = 1;
  private static final int CMD_RETAIN_SUBSCRIPTIONS = 110;
  private static final int CMD_SERVICE_CONNECTED = 4;
  private static final int CMD_SERVICE_DISCONNECTED = 5;
  private static final int CMD_START_OTA_IF_NECESSARY = 112;
  private static final int CMD_SWITCH_TO_SUBSCRIPTION = 107;
  private static final int CMD_UPDATE_SUBSCRIPTION_NICKNAME = 108;
  private static final int EUICC_QUERY_FLAGS = 269484096;
  @VisibleForTesting
  static final int LINGER_TIMEOUT_MILLIS = 60000;
  private static final String TAG = "EuiccConnector";
  private Set<BaseEuiccCommandCallback> mActiveCommandCallbacks = new ArraySet();
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public AvailableState mAvailableState;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public BindingState mBindingState;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public ConnectedState mConnectedState;
  private Context mContext;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public DisconnectedState mDisconnectedState;
  private IEuiccService mEuiccService;
  private final PackageMonitor mPackageMonitor = new EuiccPackageMonitor(null);
  private PackageManager mPm;
  private ServiceInfo mSelectedComponent;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public UnavailableState mUnavailableState;
  private final BroadcastReceiver mUserUnlockedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("android.intent.action.USER_UNLOCKED".equals(paramAnonymousIntent.getAction())) {
        sendMessage(1);
      }
    }
  };
  
  EuiccConnector(Context paramContext)
  {
    super("EuiccConnector");
    init(paramContext);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public EuiccConnector(Context paramContext, Looper paramLooper)
  {
    super("EuiccConnector", paramLooper);
    init(paramContext);
  }
  
  private boolean createBinding()
  {
    if (mSelectedComponent == null)
    {
      Log.wtf("EuiccConnector", "Attempting to create binding but no component is selected");
      return false;
    }
    Intent localIntent = new Intent("android.service.euicc.EuiccService");
    localIntent.setComponent(mSelectedComponent.getComponentName());
    return mContext.bindService(localIntent, this, 67108865);
  }
  
  public static ActivityInfo findBestActivity(PackageManager paramPackageManager, Intent paramIntent)
  {
    Object localObject = paramPackageManager.queryIntentActivities(paramIntent, 269484096);
    localObject = (ActivityInfo)findBestComponent(paramPackageManager, (List)localObject);
    if (localObject == null)
    {
      paramPackageManager = new StringBuilder();
      paramPackageManager.append("No valid component found for intent: ");
      paramPackageManager.append(paramIntent);
      Log.w("EuiccConnector", paramPackageManager.toString());
    }
    return localObject;
  }
  
  public static ComponentInfo findBestComponent(PackageManager paramPackageManager)
  {
    Intent localIntent = new Intent("android.service.euicc.EuiccService");
    paramPackageManager = findBestComponent(paramPackageManager, paramPackageManager.queryIntentServices(localIntent, 269484096));
    if (paramPackageManager == null) {
      Log.w("EuiccConnector", "No valid EuiccService implementation found");
    }
    return paramPackageManager;
  }
  
  private static ComponentInfo findBestComponent(PackageManager paramPackageManager, List<ResolveInfo> paramList)
  {
    int i = Integer.MIN_VALUE;
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramList != null)
    {
      Iterator localIterator = paramList.iterator();
      paramList = localObject2;
      for (;;)
      {
        localObject1 = paramList;
        if (!localIterator.hasNext()) {
          break;
        }
        localObject1 = (ResolveInfo)localIterator.next();
        if (isValidEuiccComponent(paramPackageManager, (ResolveInfo)localObject1))
        {
          int j = i;
          if (filter.getPriority() > i)
          {
            j = filter.getPriority();
            paramList = ((ResolveInfo)localObject1).getComponentInfo();
          }
          i = j;
        }
      }
    }
    return localObject1;
  }
  
  private ServiceInfo findBestComponent()
  {
    return (ServiceInfo)findBestComponent(mPm);
  }
  
  private static BaseEuiccCommandCallback getCallback(Message paramMessage)
  {
    switch (what)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported message: ");
      localStringBuilder.append(what);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 108: 
      return obj).mCallback;
    case 107: 
      return obj).mCallback;
    case 106: 
      return obj).mCallback;
    case 104: 
      return obj).mCallback;
    case 102: 
      return obj).mCallback;
    case 101: 
      return obj).mCallback;
    }
    return (BaseEuiccCommandCallback)obj;
  }
  
  private void init(Context paramContext)
  {
    mContext = paramContext;
    mPm = paramContext.getPackageManager();
    mUnavailableState = new UnavailableState(null);
    addState(mUnavailableState);
    mAvailableState = new AvailableState(null);
    addState(mAvailableState, mUnavailableState);
    mBindingState = new BindingState(null);
    addState(mBindingState);
    mDisconnectedState = new DisconnectedState(null);
    addState(mDisconnectedState);
    mConnectedState = new ConnectedState(null);
    addState(mConnectedState, mDisconnectedState);
    mSelectedComponent = findBestComponent();
    if (mSelectedComponent != null) {
      paramContext = mAvailableState;
    } else {
      paramContext = mUnavailableState;
    }
    setInitialState(paramContext);
    mPackageMonitor.register(mContext, null, false);
    mContext.registerReceiver(mUserUnlockedReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
    start();
  }
  
  private static boolean isEuiccCommand(int paramInt)
  {
    boolean bool;
    if (paramInt >= 100) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidEuiccComponent(PackageManager paramPackageManager, ResolveInfo paramResolveInfo)
  {
    ComponentInfo localComponentInfo = paramResolveInfo.getComponentInfo();
    String str = localComponentInfo.getComponentName().getPackageName();
    if (paramPackageManager.checkPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS", str) != 0)
    {
      paramPackageManager = new StringBuilder();
      paramPackageManager.append("Package ");
      paramPackageManager.append(str);
      paramPackageManager.append(" does not declare WRITE_EMBEDDED_SUBSCRIPTIONS");
      Log.wtf("EuiccConnector", paramPackageManager.toString());
      return false;
    }
    if ((localComponentInfo instanceof ServiceInfo)) {}
    for (paramPackageManager = permission;; paramPackageManager = permission)
    {
      break;
      if (!(localComponentInfo instanceof ActivityInfo)) {
        break label218;
      }
    }
    if (!TextUtils.equals(paramPackageManager, "android.permission.BIND_EUICC_SERVICE"))
    {
      paramPackageManager = new StringBuilder();
      paramPackageManager.append("Package ");
      paramPackageManager.append(str);
      paramPackageManager.append(" does not require the BIND_EUICC_SERVICE permission");
      Log.wtf("EuiccConnector", paramPackageManager.toString());
      return false;
    }
    if ((filter != null) && (filter.getPriority() != 0)) {
      return true;
    }
    paramPackageManager = new StringBuilder();
    paramPackageManager.append("Package ");
    paramPackageManager.append(str);
    paramPackageManager.append(" does not specify a priority");
    Log.wtf("EuiccConnector", paramPackageManager.toString());
    return false;
    label218:
    throw new IllegalArgumentException("Can only verify services/activities");
  }
  
  private void onCommandEnd(BaseEuiccCommandCallback paramBaseEuiccCommandCallback)
  {
    if (!mActiveCommandCallbacks.remove(paramBaseEuiccCommandCallback)) {
      Log.wtf("EuiccConnector", "Callback already removed from mActiveCommandCallbacks");
    }
    if (mActiveCommandCallbacks.isEmpty()) {
      sendMessageDelayed(3, 60000L);
    }
  }
  
  private void onCommandStart(BaseEuiccCommandCallback paramBaseEuiccCommandCallback)
  {
    mActiveCommandCallbacks.add(paramBaseEuiccCommandCallback);
    removeMessages(3);
  }
  
  private void unbind()
  {
    mEuiccService = null;
    mContext.unbindService(this);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void deleteSubscription(String paramString, DeleteCommandCallback paramDeleteCommandCallback)
  {
    DeleteRequest localDeleteRequest = new DeleteRequest();
    mIccid = paramString;
    mCallback = paramDeleteCommandCallback;
    sendMessage(106, localDeleteRequest);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void downloadSubscription(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean1, boolean paramBoolean2, DownloadCommandCallback paramDownloadCommandCallback)
  {
    DownloadRequest localDownloadRequest = new DownloadRequest();
    mSubscription = paramDownloadableSubscription;
    mSwitchAfterDownload = paramBoolean1;
    mForceDeactivateSim = paramBoolean2;
    mCallback = paramDownloadCommandCallback;
    sendMessage(102, localDownloadRequest);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mSelectedComponent=");
    paramFileDescriptor.append(mSelectedComponent);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mEuiccService=");
    paramFileDescriptor.append(mEuiccService);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mActiveCommandCount=");
    paramFileDescriptor.append(mActiveCommandCallbacks.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void eraseSubscriptions(EraseCommandCallback paramEraseCommandCallback)
  {
    sendMessage(109, paramEraseCommandCallback);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void getDefaultDownloadableSubscriptionList(boolean paramBoolean, GetDefaultListCommandCallback paramGetDefaultListCommandCallback)
  {
    GetDefaultListRequest localGetDefaultListRequest = new GetDefaultListRequest();
    mForceDeactivateSim = paramBoolean;
    mCallback = paramGetDefaultListCommandCallback;
    sendMessage(104, localGetDefaultListRequest);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void getDownloadableSubscriptionMetadata(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, GetMetadataCommandCallback paramGetMetadataCommandCallback)
  {
    GetMetadataRequest localGetMetadataRequest = new GetMetadataRequest();
    mSubscription = paramDownloadableSubscription;
    mForceDeactivateSim = paramBoolean;
    mCallback = paramGetMetadataCommandCallback;
    sendMessage(101, localGetMetadataRequest);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void getEid(GetEidCommandCallback paramGetEidCommandCallback)
  {
    sendMessage(100, paramGetEidCommandCallback);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void getEuiccInfo(GetEuiccInfoCommandCallback paramGetEuiccInfoCommandCallback)
  {
    sendMessage(105, paramGetEuiccInfoCommandCallback);
  }
  
  void getEuiccProfileInfoList(GetEuiccProfileInfoListCommandCallback paramGetEuiccProfileInfoListCommandCallback)
  {
    sendMessage(103, paramGetEuiccProfileInfoListCommandCallback);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void getOtaStatus(GetOtaStatusCommandCallback paramGetOtaStatusCommandCallback)
  {
    sendMessage(111, paramGetOtaStatusCommandCallback);
  }
  
  public void onHalting()
  {
    mPackageMonitor.unregister();
    mContext.unregisterReceiver(mUserUnlockedReceiver);
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    sendMessage(4, IEuiccService.Stub.asInterface(paramIBinder));
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    sendMessage(5);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void retainSubscriptions(RetainSubscriptionsCommandCallback paramRetainSubscriptionsCommandCallback)
  {
    sendMessage(110, paramRetainSubscriptionsCommandCallback);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void startOtaIfNecessary(OtaStatusChangedCallback paramOtaStatusChangedCallback)
  {
    sendMessage(112, paramOtaStatusChangedCallback);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void switchToSubscription(String paramString, boolean paramBoolean, SwitchCommandCallback paramSwitchCommandCallback)
  {
    SwitchRequest localSwitchRequest = new SwitchRequest();
    mIccid = paramString;
    mForceDeactivateSim = paramBoolean;
    mCallback = paramSwitchCommandCallback;
    sendMessage(107, localSwitchRequest);
  }
  
  protected void unhandledMessage(Message paramMessage)
  {
    IState localIState = getCurrentState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unhandled message ");
    localStringBuilder.append(what);
    localStringBuilder.append(" in state ");
    if (localIState == null) {
      paramMessage = "null";
    } else {
      paramMessage = localIState.getName();
    }
    localStringBuilder.append(paramMessage);
    Log.wtf("EuiccConnector", localStringBuilder.toString());
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void updateSubscriptionNickname(String paramString1, String paramString2, UpdateNicknameCommandCallback paramUpdateNicknameCommandCallback)
  {
    UpdateNicknameRequest localUpdateNicknameRequest = new UpdateNicknameRequest();
    mIccid = paramString1;
    mNickname = paramString2;
    mCallback = paramUpdateNicknameCommandCallback;
    sendMessage(108, localUpdateNicknameRequest);
  }
  
  private class AvailableState
    extends State
  {
    private AvailableState() {}
    
    public boolean processMessage(Message paramMessage)
    {
      if (EuiccConnector.isEuiccCommand(what))
      {
        deferMessage(paramMessage);
        transitionTo(mBindingState);
        return true;
      }
      return false;
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface BaseEuiccCommandCallback
  {
    public abstract void onEuiccServiceUnavailable();
  }
  
  private class BindingState
    extends State
  {
    private BindingState() {}
    
    public void enter()
    {
      if (EuiccConnector.this.createBinding()) {
        transitionTo(mDisconnectedState);
      } else {
        transitionTo(mAvailableState);
      }
    }
    
    public boolean processMessage(Message paramMessage)
    {
      deferMessage(paramMessage);
      return true;
    }
  }
  
  private class ConnectedState
    extends State
  {
    private ConnectedState() {}
    
    public void enter()
    {
      removeMessages(2);
      sendMessageDelayed(3, 60000L);
    }
    
    public void exit()
    {
      removeMessages(3);
      Iterator localIterator = mActiveCommandCallbacks.iterator();
      while (localIterator.hasNext()) {
        ((EuiccConnector.BaseEuiccCommandCallback)localIterator.next()).onEuiccServiceUnavailable();
      }
      mActiveCommandCallbacks.clear();
    }
    
    public boolean processMessage(Message paramMessage)
    {
      if (what == 5)
      {
        EuiccConnector.access$1102(EuiccConnector.this, null);
        transitionTo(mDisconnectedState);
        return true;
      }
      if (what == 3)
      {
        EuiccConnector.this.unbind();
        transitionTo(mAvailableState);
        return true;
      }
      if (what == 6)
      {
        ((Runnable)obj).run();
        return true;
      }
      if (EuiccConnector.isEuiccCommand(what))
      {
        EuiccConnector.BaseEuiccCommandCallback localBaseEuiccCommandCallback = EuiccConnector.getCallback(paramMessage);
        EuiccConnector.this.onCommandStart(localBaseEuiccCommandCallback);
        try
        {
          Object localObject2;
          boolean bool1;
          switch (what)
          {
          default: 
            break;
          case 112: 
            localObject1 = mEuiccService;
            paramMessage = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$13;
            paramMessage.<init>(this, localBaseEuiccCommandCallback);
            ((IEuiccService)localObject1).startOtaIfNecessary(-1, paramMessage);
            break;
          case 111: 
            paramMessage = mEuiccService;
            localObject1 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$12;
            ((12)localObject1).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.getOtaStatus(-1, (IGetOtaStatusCallback)localObject1);
            break;
          case 110: 
            localObject1 = mEuiccService;
            paramMessage = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$11;
            paramMessage.<init>(this, localBaseEuiccCommandCallback);
            ((IEuiccService)localObject1).retainSubscriptionsForFactoryReset(-1, paramMessage);
            break;
          case 109: 
            paramMessage = mEuiccService;
            localObject1 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$10;
            ((10)localObject1).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.eraseSubscriptions(-1, (IEraseSubscriptionsCallback)localObject1);
            break;
          case 108: 
            localObject2 = (EuiccConnector.UpdateNicknameRequest)obj;
            paramMessage = mEuiccService;
            localObject1 = mIccid;
            localObject2 = mNickname;
            IUpdateSubscriptionNicknameCallback.Stub local9 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$9;
            local9.<init>(this, localBaseEuiccCommandCallback);
            paramMessage.updateSubscriptionNickname(-1, (String)localObject1, (String)localObject2, local9);
            break;
          case 107: 
            localObject2 = (EuiccConnector.SwitchRequest)obj;
            localObject1 = mEuiccService;
            paramMessage = mIccid;
            bool1 = mForceDeactivateSim;
            localObject2 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$8;
            ((8)localObject2).<init>(this, localBaseEuiccCommandCallback);
            ((IEuiccService)localObject1).switchToSubscription(-1, paramMessage, bool1, (ISwitchToSubscriptionCallback)localObject2);
            break;
          case 106: 
            localObject1 = (EuiccConnector.DeleteRequest)obj;
            paramMessage = mEuiccService;
            localObject1 = mIccid;
            localObject2 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$7;
            ((7)localObject2).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.deleteSubscription(-1, (String)localObject1, (IDeleteSubscriptionCallback)localObject2);
            break;
          case 105: 
            paramMessage = mEuiccService;
            localObject1 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$6;
            ((6)localObject1).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.getEuiccInfo(-1, (IGetEuiccInfoCallback)localObject1);
            break;
          case 104: 
            localObject1 = (EuiccConnector.GetDefaultListRequest)obj;
            paramMessage = mEuiccService;
            bool1 = mForceDeactivateSim;
            localObject1 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$5;
            ((5)localObject1).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.getDefaultDownloadableSubscriptionList(-1, bool1, (IGetDefaultDownloadableSubscriptionListCallback)localObject1);
            break;
          case 103: 
            paramMessage = mEuiccService;
            localObject1 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$4;
            ((4)localObject1).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.getEuiccProfileInfoList(-1, (IGetEuiccProfileInfoListCallback)localObject1);
            break;
          case 102: 
            localObject2 = (EuiccConnector.DownloadRequest)obj;
            paramMessage = mEuiccService;
            localObject1 = mSubscription;
            bool1 = mSwitchAfterDownload;
            boolean bool2 = mForceDeactivateSim;
            localObject2 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$3;
            ((3)localObject2).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.downloadSubscription(-1, (DownloadableSubscription)localObject1, bool1, bool2, (IDownloadSubscriptionCallback)localObject2);
            break;
          case 101: 
            localObject2 = (EuiccConnector.GetMetadataRequest)obj;
            localObject1 = mEuiccService;
            paramMessage = mSubscription;
            bool1 = mForceDeactivateSim;
            localObject2 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$2;
            ((2)localObject2).<init>(this, localBaseEuiccCommandCallback);
            ((IEuiccService)localObject1).getDownloadableSubscriptionMetadata(-1, paramMessage, bool1, (IGetDownloadableSubscriptionMetadataCallback)localObject2);
            break;
          case 100: 
            paramMessage = mEuiccService;
            localObject1 = new com/android/internal/telephony/euicc/EuiccConnector$ConnectedState$1;
            ((1)localObject1).<init>(this, localBaseEuiccCommandCallback);
            paramMessage.getEid(-1, (IGetEidCallback)localObject1);
          }
          break label791;
          Object localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Unimplemented eUICC command: ");
          ((StringBuilder)localObject1).append(what);
          Log.wtf("EuiccConnector", ((StringBuilder)localObject1).toString());
          localBaseEuiccCommandCallback.onEuiccServiceUnavailable();
          EuiccConnector.this.onCommandEnd(localBaseEuiccCommandCallback);
          return true;
        }
        catch (Exception paramMessage)
        {
          Log.w("EuiccConnector", "Exception making binder call to EuiccService", paramMessage);
          localBaseEuiccCommandCallback.onEuiccServiceUnavailable();
          EuiccConnector.this.onCommandEnd(localBaseEuiccCommandCallback);
        }
        label791:
        return true;
      }
      return false;
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface DeleteCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onDeleteComplete(int paramInt);
  }
  
  static class DeleteRequest
  {
    EuiccConnector.DeleteCommandCallback mCallback;
    String mIccid;
    
    DeleteRequest() {}
  }
  
  private class DisconnectedState
    extends State
  {
    private DisconnectedState() {}
    
    public void enter()
    {
      sendMessageDelayed(2, 30000L);
    }
    
    public boolean processMessage(Message paramMessage)
    {
      if (what == 4)
      {
        EuiccConnector.access$1102(EuiccConnector.this, (IEuiccService)obj);
        transitionTo(mConnectedState);
        return true;
      }
      int i = what;
      int j = 0;
      if (i == 1)
      {
        ServiceInfo localServiceInfo = EuiccConnector.this.findBestComponent();
        paramMessage = (String)obj;
        if (localServiceInfo == null)
        {
          if (mSelectedComponent != null) {
            i = 1;
          } else {
            i = 0;
          }
        }
        else if ((mSelectedComponent != null) && (!Objects.equals(localServiceInfo.getComponentName(), mSelectedComponent.getComponentName()))) {
          i = 0;
        } else {
          i = 1;
        }
        int k = j;
        if (localServiceInfo != null)
        {
          k = j;
          if (Objects.equals(packageName, paramMessage)) {
            k = 1;
          }
        }
        if ((i == 0) || (k != 0))
        {
          EuiccConnector.this.unbind();
          EuiccConnector.access$602(EuiccConnector.this, localServiceInfo);
          if (mSelectedComponent == null) {
            transitionTo(mUnavailableState);
          } else {
            transitionTo(mBindingState);
          }
        }
        return true;
      }
      if (what == 2)
      {
        transitionTo(mAvailableState);
        return true;
      }
      if (EuiccConnector.isEuiccCommand(what))
      {
        deferMessage(paramMessage);
        return true;
      }
      return false;
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface DownloadCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onDownloadComplete(int paramInt);
  }
  
  static class DownloadRequest
  {
    EuiccConnector.DownloadCommandCallback mCallback;
    boolean mForceDeactivateSim;
    DownloadableSubscription mSubscription;
    boolean mSwitchAfterDownload;
    
    DownloadRequest() {}
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface EraseCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onEraseComplete(int paramInt);
  }
  
  private class EuiccPackageMonitor
    extends PackageMonitor
  {
    private EuiccPackageMonitor() {}
    
    private void sendPackageChange(String paramString, boolean paramBoolean)
    {
      EuiccConnector localEuiccConnector = EuiccConnector.this;
      if (!paramBoolean) {
        paramString = null;
      }
      localEuiccConnector.sendMessage(1, paramString);
    }
    
    public boolean onHandleForceStop(Intent paramIntent, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        int i = paramArrayOfString.length;
        for (int j = 0; j < i; j++) {
          sendPackageChange(paramArrayOfString[j], true);
        }
      }
      return super.onHandleForceStop(paramIntent, paramArrayOfString, paramInt, paramBoolean);
    }
    
    public void onPackageAdded(String paramString, int paramInt)
    {
      sendPackageChange(paramString, true);
    }
    
    public void onPackageModified(String paramString)
    {
      sendPackageChange(paramString, false);
    }
    
    public void onPackageRemoved(String paramString, int paramInt)
    {
      sendPackageChange(paramString, true);
    }
    
    public void onPackageUpdateFinished(String paramString, int paramInt)
    {
      sendPackageChange(paramString, true);
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface GetDefaultListCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onGetDefaultListComplete(GetDefaultDownloadableSubscriptionListResult paramGetDefaultDownloadableSubscriptionListResult);
  }
  
  static class GetDefaultListRequest
  {
    EuiccConnector.GetDefaultListCommandCallback mCallback;
    boolean mForceDeactivateSim;
    
    GetDefaultListRequest() {}
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface GetEidCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onGetEidComplete(String paramString);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface GetEuiccInfoCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onGetEuiccInfoComplete(EuiccInfo paramEuiccInfo);
  }
  
  static abstract interface GetEuiccProfileInfoListCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onListComplete(GetEuiccProfileInfoListResult paramGetEuiccProfileInfoListResult);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface GetMetadataCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onGetMetadataComplete(GetDownloadableSubscriptionMetadataResult paramGetDownloadableSubscriptionMetadataResult);
  }
  
  static class GetMetadataRequest
  {
    EuiccConnector.GetMetadataCommandCallback mCallback;
    boolean mForceDeactivateSim;
    DownloadableSubscription mSubscription;
    
    GetMetadataRequest() {}
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface GetOtaStatusCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onGetOtaStatusComplete(int paramInt);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface OtaStatusChangedCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onOtaStatusChanged(int paramInt);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface RetainSubscriptionsCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onRetainSubscriptionsComplete(int paramInt);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface SwitchCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onSwitchComplete(int paramInt);
  }
  
  static class SwitchRequest
  {
    EuiccConnector.SwitchCommandCallback mCallback;
    boolean mForceDeactivateSim;
    String mIccid;
    
    SwitchRequest() {}
  }
  
  private class UnavailableState
    extends State
  {
    private UnavailableState() {}
    
    public boolean processMessage(Message paramMessage)
    {
      if (what == 1)
      {
        EuiccConnector.access$602(EuiccConnector.this, EuiccConnector.this.findBestComponent());
        if (mSelectedComponent != null) {
          transitionTo(mAvailableState);
        } else if (getCurrentState() != mUnavailableState) {
          transitionTo(mUnavailableState);
        }
        return true;
      }
      if (EuiccConnector.isEuiccCommand(what))
      {
        EuiccConnector.getCallback(paramMessage).onEuiccServiceUnavailable();
        return true;
      }
      return false;
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static abstract interface UpdateNicknameCommandCallback
    extends EuiccConnector.BaseEuiccCommandCallback
  {
    public abstract void onUpdateNicknameComplete(int paramInt);
  }
  
  static class UpdateNicknameRequest
  {
    EuiccConnector.UpdateNicknameCommandCallback mCallback;
    String mIccid;
    String mNickname;
    
    UpdateNicknameRequest() {}
  }
}
