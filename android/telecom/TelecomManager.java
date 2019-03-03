package android.telecom;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telecom.ITelecomService;
import com.android.internal.telecom.ITelecomService.Stub;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelecomManager
{
  public static final String ACTION_CALL_TYPE = "codeaurora.telecom.action.CALL_TYPE";
  public static final String ACTION_CHANGE_DEFAULT_DIALER = "android.telecom.action.CHANGE_DEFAULT_DIALER";
  public static final String ACTION_CHANGE_PHONE_ACCOUNTS = "android.telecom.action.CHANGE_PHONE_ACCOUNTS";
  public static final String ACTION_CONFIGURE_PHONE_ACCOUNT = "android.telecom.action.CONFIGURE_PHONE_ACCOUNT";
  public static final String ACTION_CURRENT_TTY_MODE_CHANGED = "android.telecom.action.CURRENT_TTY_MODE_CHANGED";
  public static final String ACTION_DEFAULT_DIALER_CHANGED = "android.telecom.action.DEFAULT_DIALER_CHANGED";
  public static final String ACTION_INCOMING_CALL = "android.telecom.action.INCOMING_CALL";
  public static final String ACTION_NEW_UNKNOWN_CALL = "android.telecom.action.NEW_UNKNOWN_CALL";
  public static final String ACTION_PHONE_ACCOUNT_REGISTERED = "android.telecom.action.PHONE_ACCOUNT_REGISTERED";
  public static final String ACTION_PHONE_ACCOUNT_UNREGISTERED = "android.telecom.action.PHONE_ACCOUNT_UNREGISTERED";
  public static final String ACTION_SHOW_CALL_ACCESSIBILITY_SETTINGS = "android.telecom.action.SHOW_CALL_ACCESSIBILITY_SETTINGS";
  public static final String ACTION_SHOW_CALL_SETTINGS = "android.telecom.action.SHOW_CALL_SETTINGS";
  public static final String ACTION_SHOW_MISSED_CALLS_NOTIFICATION = "android.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION";
  public static final String ACTION_SHOW_RESPOND_VIA_SMS_SETTINGS = "android.telecom.action.SHOW_RESPOND_VIA_SMS_SETTINGS";
  public static final String ACTION_TTY_PREFERRED_MODE_CHANGED = "android.telecom.action.TTY_PREFERRED_MODE_CHANGED";
  public static final char DTMF_CHARACTER_PAUSE = ',';
  public static final char DTMF_CHARACTER_WAIT = ';';
  public static final ComponentName EMERGENCY_DIALER_COMPONENT = ComponentName.createRelative("com.android.phone", ".EmergencyDialer");
  public static final String EXTRA_CALL_AUDIO_STATE = "android.telecom.extra.CALL_AUDIO_STATE";
  @SystemApi
  public static final String EXTRA_CALL_BACK_INTENT = "android.telecom.extra.CALL_BACK_INTENT";
  public static final String EXTRA_CALL_BACK_NUMBER = "android.telecom.extra.CALL_BACK_NUMBER";
  public static final String EXTRA_CALL_CREATED_TIME_MILLIS = "android.telecom.extra.CALL_CREATED_TIME_MILLIS";
  public static final String EXTRA_CALL_DISCONNECT_CAUSE = "android.telecom.extra.CALL_DISCONNECT_CAUSE";
  public static final String EXTRA_CALL_DISCONNECT_MESSAGE = "android.telecom.extra.CALL_DISCONNECT_MESSAGE";
  public static final String EXTRA_CALL_EXTERNAL_RINGER = "android.telecom.extra.CALL_EXTERNAL_RINGER";
  public static final String EXTRA_CALL_SUBJECT = "android.telecom.extra.CALL_SUBJECT";
  public static final String EXTRA_CALL_TECHNOLOGY_TYPE = "android.telecom.extra.CALL_TECHNOLOGY_TYPE";
  public static final String EXTRA_CALL_TELECOM_ROUTING_END_TIME_MILLIS = "android.telecom.extra.CALL_TELECOM_ROUTING_END_TIME_MILLIS";
  public static final String EXTRA_CALL_TELECOM_ROUTING_START_TIME_MILLIS = "android.telecom.extra.CALL_TELECOM_ROUTING_START_TIME_MILLIS";
  public static final String EXTRA_CALL_TYPE_CS = "codeaurora.telecom.extra.CALL_TYPE_CS";
  public static final String EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME = "android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME";
  @SystemApi
  public static final String EXTRA_CLEAR_MISSED_CALLS_INTENT = "android.telecom.extra.CLEAR_MISSED_CALLS_INTENT";
  @SystemApi
  public static final String EXTRA_CONNECTION_SERVICE = "android.telecom.extra.CONNECTION_SERVICE";
  public static final String EXTRA_CURRENT_TTY_MODE = "android.telecom.intent.extra.CURRENT_TTY_MODE";
  public static final String EXTRA_HANDOVER_FROM_PHONE_ACCOUNT = "android.telecom.extra.HANDOVER_FROM_PHONE_ACCOUNT";
  public static final String EXTRA_INCOMING_CALL_ADDRESS = "android.telecom.extra.INCOMING_CALL_ADDRESS";
  public static final String EXTRA_INCOMING_CALL_EXTRAS = "android.telecom.extra.INCOMING_CALL_EXTRAS";
  public static final String EXTRA_INCOMING_VIDEO_STATE = "android.telecom.extra.INCOMING_VIDEO_STATE";
  public static final String EXTRA_IS_HANDOVER = "android.telecom.extra.IS_HANDOVER";
  public static final String EXTRA_IS_HANDOVER_CONNECTION = "android.telecom.extra.IS_HANDOVER_CONNECTION";
  public static final String EXTRA_NEW_OUTGOING_CALL_CANCEL_TIMEOUT = "android.telecom.extra.NEW_OUTGOING_CALL_CANCEL_TIMEOUT";
  public static final String EXTRA_NOTIFICATION_COUNT = "android.telecom.extra.NOTIFICATION_COUNT";
  public static final String EXTRA_NOTIFICATION_PHONE_NUMBER = "android.telecom.extra.NOTIFICATION_PHONE_NUMBER";
  public static final String EXTRA_OUTGOING_CALL_EXTRAS = "android.telecom.extra.OUTGOING_CALL_EXTRAS";
  public static final String EXTRA_PHONE_ACCOUNT_HANDLE = "android.telecom.extra.PHONE_ACCOUNT_HANDLE";
  public static final String EXTRA_START_CALL_WITH_RTT = "android.telecom.extra.START_CALL_WITH_RTT";
  public static final String EXTRA_START_CALL_WITH_SPEAKERPHONE = "android.telecom.extra.START_CALL_WITH_SPEAKERPHONE";
  public static final String EXTRA_START_CALL_WITH_VIDEO_STATE = "android.telecom.extra.START_CALL_WITH_VIDEO_STATE";
  public static final String EXTRA_TTY_PREFERRED_MODE = "android.telecom.intent.extra.TTY_PREFERRED";
  public static final String EXTRA_UNKNOWN_CALL_HANDLE = "android.telecom.extra.UNKNOWN_CALL_HANDLE";
  public static final String EXTRA_USE_ASSISTED_DIALING = "android.telecom.extra.USE_ASSISTED_DIALING";
  public static final String GATEWAY_ORIGINAL_ADDRESS = "android.telecom.extra.GATEWAY_ORIGINAL_ADDRESS";
  public static final String GATEWAY_PROVIDER_PACKAGE = "android.telecom.extra.GATEWAY_PROVIDER_PACKAGE";
  public static final String METADATA_INCLUDE_EXTERNAL_CALLS = "android.telecom.INCLUDE_EXTERNAL_CALLS";
  public static final String METADATA_INCLUDE_SELF_MANAGED_CALLS = "android.telecom.INCLUDE_SELF_MANAGED_CALLS";
  public static final String METADATA_IN_CALL_SERVICE_CAR_MODE_UI = "android.telecom.IN_CALL_SERVICE_CAR_MODE_UI";
  public static final String METADATA_IN_CALL_SERVICE_RINGING = "android.telecom.IN_CALL_SERVICE_RINGING";
  public static final String METADATA_IN_CALL_SERVICE_UI = "android.telecom.IN_CALL_SERVICE_UI";
  public static final int PRESENTATION_ALLOWED = 1;
  public static final int PRESENTATION_PAYPHONE = 4;
  public static final int PRESENTATION_RESTRICTED = 2;
  public static final int PRESENTATION_UNKNOWN = 3;
  private static final String TAG = "TelecomManager";
  public static final int TTY_MODE_FULL = 1;
  public static final int TTY_MODE_HCO = 2;
  public static final int TTY_MODE_OFF = 0;
  public static final int TTY_MODE_VCO = 3;
  private final Context mContext;
  private final ITelecomService mTelecomServiceOverride;
  
  public TelecomManager(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TelecomManager(Context paramContext, ITelecomService paramITelecomService)
  {
    Context localContext = paramContext.getApplicationContext();
    if (localContext != null) {
      mContext = localContext;
    } else {
      mContext = paramContext;
    }
    mTelecomServiceOverride = paramITelecomService;
  }
  
  public static TelecomManager from(Context paramContext)
  {
    return (TelecomManager)paramContext.getSystemService("telecom");
  }
  
  private ITelecomService getTelecomService()
  {
    if (mTelecomServiceOverride != null) {
      return mTelecomServiceOverride;
    }
    return ITelecomService.Stub.asInterface(ServiceManager.getService("telecom"));
  }
  
  private boolean isServiceConnected()
  {
    boolean bool;
    if (getTelecomService() != null) {
      bool = true;
    } else {
      bool = false;
    }
    if (!bool) {
      Log.w("TelecomManager", "Telecom Service not found.");
    }
    return bool;
  }
  
  public void acceptHandover(Uri paramUri, int paramInt, PhoneAccountHandle paramPhoneAccountHandle)
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().acceptHandover(paramUri, paramInt, paramPhoneAccountHandle);
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      paramUri = new StringBuilder();
      paramUri.append("RemoteException acceptHandover: ");
      paramUri.append(paramPhoneAccountHandle);
      Log.e("TelecomManager", paramUri.toString());
    }
  }
  
  public void acceptRingingCall()
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().acceptRingingCall(mContext.getPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#acceptRingingCall", localRemoteException);
    }
  }
  
  public void acceptRingingCall(int paramInt)
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().acceptRingingCallWithVideoState(mContext.getPackageName(), paramInt);
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#acceptRingingCallWithVideoState", localRemoteException);
    }
  }
  
  public void addNewIncomingCall(PhoneAccountHandle paramPhoneAccountHandle, Bundle paramBundle)
  {
    try
    {
      if (isServiceConnected())
      {
        if ((paramBundle != null) && (paramBundle.getBoolean("android.telecom.extra.IS_HANDOVER")) && (mContext.getApplicationContext().getApplicationInfo().targetSdkVersion > 27)) {
          Log.e("TAG", "addNewIncomingCall failed. Use public api acceptHandover for API > O-MR1");
        }
        ITelecomService localITelecomService = getTelecomService();
        if (paramBundle == null)
        {
          paramBundle = new android/os/Bundle;
          paramBundle.<init>();
        }
        localITelecomService.addNewIncomingCall(paramPhoneAccountHandle, paramBundle);
      }
    }
    catch (RemoteException localRemoteException)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("RemoteException adding a new incoming call: ");
      paramBundle.append(paramPhoneAccountHandle);
      Log.e("TelecomManager", paramBundle.toString(), localRemoteException);
    }
  }
  
  @SystemApi
  public void addNewUnknownCall(PhoneAccountHandle paramPhoneAccountHandle, Bundle paramBundle)
  {
    try
    {
      if (isServiceConnected())
      {
        localObject = getTelecomService();
        if (paramBundle == null)
        {
          paramBundle = new android/os/Bundle;
          paramBundle.<init>();
        }
        ((ITelecomService)localObject).addNewUnknownCall(paramPhoneAccountHandle, paramBundle);
      }
    }
    catch (RemoteException paramBundle)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("RemoteException adding a new unknown call: ");
      ((StringBuilder)localObject).append(paramPhoneAccountHandle);
      Log.e("TelecomManager", ((StringBuilder)localObject).toString(), paramBundle);
    }
  }
  
  public void cancelMissedCallsNotification()
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        localITelecomService.cancelMissedCallsNotification(mContext.getOpPackageName());
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("TelecomManager", "Error calling ITelecomService#cancelMissedCallsNotification", localRemoteException);
      }
    }
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public void clearAccounts()
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().clearAccounts(mContext.getPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#clearAccounts", localRemoteException);
    }
  }
  
  public void clearAccountsForPackage(String paramString)
  {
    try
    {
      if ((isServiceConnected()) && (!TextUtils.isEmpty(paramString))) {
        getTelecomService().clearAccounts(paramString);
      }
    }
    catch (RemoteException paramString)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#clearAccountsForPackage", paramString);
    }
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public void clearPhoneAccounts()
  {
    clearAccounts();
  }
  
  public Intent createManageBlockedNumbersIntent()
  {
    ITelecomService localITelecomService = getTelecomService();
    Object localObject1 = null;
    Object localObject2 = localObject1;
    Object localObject3;
    if (localITelecomService != null) {
      try
      {
        localObject2 = localITelecomService.createManageBlockedNumbersIntent();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("TelecomManager", "Error calling ITelecomService#createManageBlockedNumbersIntent", localRemoteException);
        localObject3 = localObject1;
      }
    }
    return localObject3;
  }
  
  @SystemApi
  public TelecomAnalytics dumpAnalytics()
  {
    ITelecomService localITelecomService = getTelecomService();
    Object localObject1 = null;
    Object localObject2 = localObject1;
    Object localObject3;
    if (localITelecomService != null) {
      try
      {
        localObject2 = localITelecomService.dumpCallAnalytics();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("TelecomManager", "Error dumping call analytics", localRemoteException);
        localObject3 = localObject1;
      }
    }
    return localObject3;
  }
  
  @SystemApi
  public void enablePhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        localITelecomService.enablePhoneAccount(paramPhoneAccountHandle, paramBoolean);
      }
      catch (RemoteException paramPhoneAccountHandle)
      {
        Log.e("TelecomManager", "Error enablePhoneAbbount", paramPhoneAccountHandle);
      }
    }
  }
  
  @SystemApi
  public boolean endCall()
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().endCall(mContext.getPackageName());
        return bool;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#endCall", localRemoteException);
    }
    return false;
  }
  
  public Uri getAdnUriForPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
  {
    ITelecomService localITelecomService = getTelecomService();
    if ((localITelecomService != null) && (paramPhoneAccountHandle != null)) {
      try
      {
        paramPhoneAccountHandle = localITelecomService.getAdnUriForPhoneAccount(paramPhoneAccountHandle, mContext.getOpPackageName());
        return paramPhoneAccountHandle;
      }
      catch (RemoteException paramPhoneAccountHandle)
      {
        Log.e("TelecomManager", "Error calling ITelecomService#getAdnUriForPhoneAccount", paramPhoneAccountHandle);
      }
    }
    return Uri.parse("content://icc/adn");
  }
  
  @SystemApi
  public List<PhoneAccountHandle> getAllPhoneAccountHandles()
  {
    try
    {
      if (isServiceConnected())
      {
        List localList = getTelecomService().getAllPhoneAccountHandles();
        return localList;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getAllPhoneAccountHandles", localRemoteException);
    }
    return Collections.EMPTY_LIST;
  }
  
  @SystemApi
  public List<PhoneAccount> getAllPhoneAccounts()
  {
    try
    {
      if (isServiceConnected())
      {
        List localList = getTelecomService().getAllPhoneAccounts();
        return localList;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getAllPhoneAccounts", localRemoteException);
    }
    return Collections.EMPTY_LIST;
  }
  
  @SystemApi
  public int getAllPhoneAccountsCount()
  {
    try
    {
      if (isServiceConnected())
      {
        int i = getTelecomService().getAllPhoneAccountsCount();
        return i;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getAllPhoneAccountsCount", localRemoteException);
    }
    return 0;
  }
  
  public List<PhoneAccountHandle> getCallCapablePhoneAccounts()
  {
    return getCallCapablePhoneAccounts(false);
  }
  
  public List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean paramBoolean)
  {
    try
    {
      if (isServiceConnected())
      {
        localObject = getTelecomService().getCallCapablePhoneAccounts(paramBoolean, mContext.getOpPackageName());
        return localObject;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Error calling ITelecomService#getCallCapablePhoneAccounts(");
      ((StringBuilder)localObject).append(paramBoolean);
      ((StringBuilder)localObject).append(")");
      Log.e("TelecomManager", ((StringBuilder)localObject).toString(), localRemoteException);
    }
    return new ArrayList();
  }
  
  @SystemApi
  public int getCallState()
  {
    try
    {
      if (isServiceConnected())
      {
        int i = getTelecomService().getCallState();
        return i;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.d("TelecomManager", "RemoteException calling getCallState().", localRemoteException);
    }
    return 0;
  }
  
  @SystemApi
  public PhoneAccountHandle getConnectionManager()
  {
    return getSimCallManager();
  }
  
  public int getCurrentTtyMode()
  {
    try
    {
      if (isServiceConnected())
      {
        int i = getTelecomService().getCurrentTtyMode(mContext.getOpPackageName());
        return i;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException attempting to get the current TTY mode.", localRemoteException);
    }
    return 0;
  }
  
  public String getDefaultDialerPackage()
  {
    try
    {
      if (isServiceConnected())
      {
        String str = getTelecomService().getDefaultDialerPackage();
        return str;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException attempting to get the default dialer package name.", localRemoteException);
    }
    return null;
  }
  
  public PhoneAccountHandle getDefaultOutgoingPhoneAccount(String paramString)
  {
    try
    {
      if (isServiceConnected())
      {
        paramString = getTelecomService().getDefaultOutgoingPhoneAccount(paramString, mContext.getOpPackageName());
        return paramString;
      }
    }
    catch (RemoteException paramString)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getDefaultOutgoingPhoneAccount", paramString);
    }
    return null;
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public ComponentName getDefaultPhoneApp()
  {
    try
    {
      if (isServiceConnected())
      {
        ComponentName localComponentName = getTelecomService().getDefaultPhoneApp();
        return localComponentName;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException attempting to get the default phone app.", localRemoteException);
    }
    return null;
  }
  
  public String getLine1Number(PhoneAccountHandle paramPhoneAccountHandle)
  {
    try
    {
      if (isServiceConnected())
      {
        paramPhoneAccountHandle = getTelecomService().getLine1Number(paramPhoneAccountHandle, mContext.getOpPackageName());
        return paramPhoneAccountHandle;
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      Log.e("TelecomManager", "RemoteException calling ITelecomService#getLine1Number.", paramPhoneAccountHandle);
    }
    return null;
  }
  
  public PhoneAccount getPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
  {
    try
    {
      if (isServiceConnected())
      {
        paramPhoneAccountHandle = getTelecomService().getPhoneAccount(paramPhoneAccountHandle);
        return paramPhoneAccountHandle;
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getPhoneAccount", paramPhoneAccountHandle);
    }
    return null;
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public List<PhoneAccountHandle> getPhoneAccountsForPackage()
  {
    try
    {
      if (isServiceConnected())
      {
        List localList = getTelecomService().getPhoneAccountsForPackage(mContext.getPackageName());
        return localList;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getPhoneAccountsForPackage", localRemoteException);
    }
    return null;
  }
  
  @SystemApi
  public List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String paramString)
  {
    try
    {
      if (isServiceConnected())
      {
        paramString = getTelecomService().getPhoneAccountsSupportingScheme(paramString, mContext.getOpPackageName());
        return paramString;
      }
    }
    catch (RemoteException paramString)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getPhoneAccountsSupportingScheme", paramString);
    }
    return new ArrayList();
  }
  
  public List<PhoneAccountHandle> getSelfManagedPhoneAccounts()
  {
    try
    {
      if (isServiceConnected())
      {
        List localList = getTelecomService().getSelfManagedPhoneAccounts(mContext.getOpPackageName());
        return localList;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getSelfManagedPhoneAccounts()", localRemoteException);
    }
    return new ArrayList();
  }
  
  public PhoneAccountHandle getSimCallManager()
  {
    try
    {
      if (isServiceConnected())
      {
        PhoneAccountHandle localPhoneAccountHandle = getTelecomService().getSimCallManager();
        return localPhoneAccountHandle;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getSimCallManager");
    }
    return null;
  }
  
  public PhoneAccountHandle getSimCallManager(int paramInt)
  {
    try
    {
      if (isServiceConnected())
      {
        PhoneAccountHandle localPhoneAccountHandle = getTelecomService().getSimCallManagerForUser(paramInt);
        return localPhoneAccountHandle;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getSimCallManagerForUser");
    }
    return null;
  }
  
  public String getSystemDialerPackage()
  {
    try
    {
      if (isServiceConnected())
      {
        String str = getTelecomService().getSystemDialerPackage();
        return str;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException attempting to get the system dialer package name.", localRemoteException);
    }
    return null;
  }
  
  public PhoneAccountHandle getUserSelectedOutgoingPhoneAccount()
  {
    try
    {
      if (isServiceConnected())
      {
        PhoneAccountHandle localPhoneAccountHandle = getTelecomService().getUserSelectedOutgoingPhoneAccount();
        return localPhoneAccountHandle;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#getUserSelectedOutgoingPhoneAccount", localRemoteException);
    }
    return null;
  }
  
  public String getVoiceMailNumber(PhoneAccountHandle paramPhoneAccountHandle)
  {
    try
    {
      if (isServiceConnected())
      {
        paramPhoneAccountHandle = getTelecomService().getVoiceMailNumber(paramPhoneAccountHandle, mContext.getOpPackageName());
        return paramPhoneAccountHandle;
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      Log.e("TelecomManager", "RemoteException calling ITelecomService#hasVoiceMailNumber.", paramPhoneAccountHandle);
    }
    return null;
  }
  
  public boolean handleMmi(String paramString)
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        boolean bool = localITelecomService.handlePinMmi(paramString, mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException paramString)
      {
        Log.e("TelecomManager", "Error calling ITelecomService#handlePinMmi", paramString);
      }
    }
    return false;
  }
  
  public boolean handleMmi(String paramString, PhoneAccountHandle paramPhoneAccountHandle)
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        boolean bool = localITelecomService.handlePinMmiForPhoneAccount(paramPhoneAccountHandle, paramString, mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException paramString)
      {
        Log.e("TelecomManager", "Error calling ITelecomService#handlePinMmi", paramString);
      }
    }
    return false;
  }
  
  public boolean isInCall()
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().isInCall(mContext.getOpPackageName());
        return bool;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException calling isInCall().", localRemoteException);
    }
    return false;
  }
  
  public boolean isInManagedCall()
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().isInManagedCall(mContext.getOpPackageName());
        return bool;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException calling isInManagedCall().", localRemoteException);
    }
    return false;
  }
  
  public boolean isIncomingCallPermitted(PhoneAccountHandle paramPhoneAccountHandle)
  {
    if (paramPhoneAccountHandle == null) {
      return false;
    }
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        boolean bool = localITelecomService.isIncomingCallPermitted(paramPhoneAccountHandle);
        return bool;
      }
      catch (RemoteException paramPhoneAccountHandle)
      {
        Log.e("TelecomManager", "Error isIncomingCallPermitted", paramPhoneAccountHandle);
      }
    }
    return false;
  }
  
  public boolean isOutgoingCallPermitted(PhoneAccountHandle paramPhoneAccountHandle)
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        boolean bool = localITelecomService.isOutgoingCallPermitted(paramPhoneAccountHandle);
        return bool;
      }
      catch (RemoteException paramPhoneAccountHandle)
      {
        Log.e("TelecomManager", "Error isOutgoingCallPermitted", paramPhoneAccountHandle);
      }
    }
    return false;
  }
  
  @SystemApi
  public boolean isRinging()
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().isRinging(mContext.getOpPackageName());
        return bool;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException attempting to get ringing state of phone app.", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean isTtySupported()
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().isTtySupported(mContext.getOpPackageName());
        return bool;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "RemoteException attempting to get TTY supported state.", localRemoteException);
    }
    return false;
  }
  
  public boolean isVoiceMailNumber(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().isVoiceMailNumber(paramPhoneAccountHandle, paramString, mContext.getOpPackageName());
        return bool;
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      Log.e("TelecomManager", "RemoteException calling ITelecomService#isVoiceMailNumber.", paramPhoneAccountHandle);
    }
    return false;
  }
  
  public void placeCall(Uri paramUri, Bundle paramBundle)
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null)
    {
      if (paramUri == null) {
        Log.w("TelecomManager", "Cannot place call to empty address.");
      }
      if (paramBundle == null) {
        try
        {
          paramBundle = new android/os/Bundle;
          paramBundle.<init>();
        }
        catch (RemoteException paramUri)
        {
          break label59;
        }
      }
      localITelecomService.placeCall(paramUri, paramBundle, mContext.getOpPackageName());
      return;
      label59:
      Log.e("TelecomManager", "Error calling ITelecomService#placeCall", paramUri);
    }
  }
  
  public void registerPhoneAccount(PhoneAccount paramPhoneAccount)
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().registerPhoneAccount(paramPhoneAccount);
      }
    }
    catch (RemoteException paramPhoneAccount)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#registerPhoneAccount", paramPhoneAccount);
    }
  }
  
  public boolean setDefaultDialer(String paramString)
  {
    try
    {
      if (isServiceConnected())
      {
        boolean bool = getTelecomService().setDefaultDialer(paramString);
        return bool;
      }
    }
    catch (RemoteException paramString)
    {
      Log.e("TelecomManager", "RemoteException attempting to set the default dialer.", paramString);
    }
    return false;
  }
  
  public void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().setUserSelectedOutgoingPhoneAccount(paramPhoneAccountHandle);
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#setUserSelectedOutgoingPhoneAccount");
    }
  }
  
  public void showInCallScreen(boolean paramBoolean)
  {
    ITelecomService localITelecomService = getTelecomService();
    if (localITelecomService != null) {
      try
      {
        localITelecomService.showInCallScreen(paramBoolean, mContext.getOpPackageName());
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("TelecomManager", "Error calling ITelecomService#showCallScreen", localRemoteException);
      }
    }
  }
  
  public void silenceRinger()
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().silenceRinger(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#silenceRinger", localRemoteException);
    }
  }
  
  public void unregisterPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
  {
    try
    {
      if (isServiceConnected()) {
        getTelecomService().unregisterPhoneAccount(paramPhoneAccountHandle);
      }
    }
    catch (RemoteException paramPhoneAccountHandle)
    {
      Log.e("TelecomManager", "Error calling ITelecomService#unregisterPhoneAccount", paramPhoneAccountHandle);
    }
  }
}
