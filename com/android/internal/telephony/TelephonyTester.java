package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.RegistrantList;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.ims.ImsExternalCallState;
import android.telephony.ims.ImsReasonInfo;
import com.android.ims.ImsCall;
import com.android.ims.ImsCall.ImsCallSessionListenerProxy;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.imsphone.ImsExternalCallTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.test.TestConferenceEventPackageParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TelephonyTester
{
  private static final String ACTION_RESET = "reset";
  private static final String ACTION_TEST_CONFERENCE_EVENT_PACKAGE = "com.android.internal.telephony.TestConferenceEventPackage";
  private static final String ACTION_TEST_DIALOG_EVENT_PACKAGE = "com.android.internal.telephony.TestDialogEventPackage";
  private static final String ACTION_TEST_HANDOVER_FAIL = "com.android.internal.telephony.TestHandoverFail";
  private static final String ACTION_TEST_SERVICE_STATE = "com.android.internal.telephony.TestServiceState";
  private static final String ACTION_TEST_SUPP_SRVC_FAIL = "com.android.internal.telephony.TestSuppSrvcFail";
  private static final String ACTION_TEST_SUPP_SRVC_NOTIFICATION = "com.android.internal.telephony.TestSuppSrvcNotification";
  private static final boolean DBG = true;
  private static final String EXTRA_ACTION = "action";
  private static final String EXTRA_CANPULL = "canPull";
  private static final String EXTRA_CODE = "code";
  private static final String EXTRA_DATA_RAT = "data_rat";
  private static final String EXTRA_DATA_REG_STATE = "data_reg_state";
  private static final String EXTRA_DATA_ROAMING_TYPE = "data_roaming_type";
  private static final String EXTRA_DIALOGID = "dialogId";
  private static final String EXTRA_FAILURE_CODE = "failureCode";
  private static final String EXTRA_FILENAME = "filename";
  private static final String EXTRA_NUMBER = "number";
  private static final String EXTRA_SENDPACKAGE = "sendPackage";
  private static final String EXTRA_STARTPACKAGE = "startPackage";
  private static final String EXTRA_STATE = "state";
  private static final String EXTRA_TYPE = "type";
  private static final String EXTRA_VOICE_RAT = "voice_rat";
  private static final String EXTRA_VOICE_REG_STATE = "voice_reg_state";
  private static final String EXTRA_VOICE_ROAMING_TYPE = "voice_roaming_type";
  private static final String LOG_TAG = "TelephonyTester";
  private static List<ImsExternalCallState> mImsExternalCallStates = null;
  protected BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      String str = paramAnonymousIntent.getAction();
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("sIntentReceiver.onReceive: action=");
        localStringBuilder.append(str);
        TelephonyTester.log(localStringBuilder.toString());
        if (str.equals(mPhone.getActionDetached()))
        {
          TelephonyTester.log("simulate detaching");
          mPhone.getServiceStateTracker().mDetachedRegistrants.notifyRegistrants();
        }
        else if (str.equals(mPhone.getActionAttached()))
        {
          TelephonyTester.log("simulate attaching");
          mPhone.getServiceStateTracker().mAttachedRegistrants.notifyRegistrants();
        }
        else if (str.equals("com.android.internal.telephony.TestConferenceEventPackage"))
        {
          TelephonyTester.log("inject simulated conference event package");
          TelephonyTester.this.handleTestConferenceEventPackage(paramAnonymousContext, paramAnonymousIntent.getStringExtra("filename"));
        }
        else if (str.equals("com.android.internal.telephony.TestDialogEventPackage"))
        {
          TelephonyTester.log("handle test dialog event package intent");
          TelephonyTester.this.handleTestDialogEventPackageIntent(paramAnonymousIntent);
        }
        else if (str.equals("com.android.internal.telephony.TestSuppSrvcFail"))
        {
          TelephonyTester.log("handle test supp svc failed intent");
          TelephonyTester.this.handleSuppServiceFailedIntent(paramAnonymousIntent);
        }
        else if (str.equals("com.android.internal.telephony.TestHandoverFail"))
        {
          TelephonyTester.log("handle handover fail test intent");
          TelephonyTester.this.handleHandoverFailedIntent();
        }
        else if (str.equals("com.android.internal.telephony.TestSuppSrvcNotification"))
        {
          TelephonyTester.log("handle supp service notification test intent");
          TelephonyTester.this.sendTestSuppServiceNotification(paramAnonymousIntent);
        }
        else if (str.equals("com.android.internal.telephony.TestServiceState"))
        {
          TelephonyTester.log("handle test service state changed intent");
          TelephonyTester.access$702(TelephonyTester.this, paramAnonymousIntent);
          mPhone.getServiceStateTracker().sendEmptyMessage(2);
        }
        else
        {
          paramAnonymousContext = new java/lang/StringBuilder;
          paramAnonymousContext.<init>();
          paramAnonymousContext.append("onReceive: unknown action=");
          paramAnonymousContext.append(str);
          TelephonyTester.log(paramAnonymousContext.toString());
        }
      }
      catch (BadParcelableException paramAnonymousContext)
      {
        Rlog.w("TelephonyTester", paramAnonymousContext);
      }
    }
  };
  private Phone mPhone;
  private Intent mServiceStateTestIntent;
  
  TelephonyTester(Phone paramPhone)
  {
    mPhone = paramPhone;
    if (Build.IS_DEBUGGABLE)
    {
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction(mPhone.getActionDetached());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(mPhone.getActionDetached());
      log(localStringBuilder.toString());
      localIntentFilter.addAction(mPhone.getActionAttached());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(mPhone.getActionAttached());
      log(localStringBuilder.toString());
      if (mPhone.getPhoneType() == 5)
      {
        log("register for intent action=com.android.internal.telephony.TestConferenceEventPackage");
        localIntentFilter.addAction("com.android.internal.telephony.TestConferenceEventPackage");
        localIntentFilter.addAction("com.android.internal.telephony.TestDialogEventPackage");
        localIntentFilter.addAction("com.android.internal.telephony.TestSuppSrvcFail");
        localIntentFilter.addAction("com.android.internal.telephony.TestHandoverFail");
        localIntentFilter.addAction("com.android.internal.telephony.TestSuppSrvcNotification");
        mImsExternalCallStates = new ArrayList();
      }
      else
      {
        localIntentFilter.addAction("com.android.internal.telephony.TestServiceState");
        log("register for intent action=com.android.internal.telephony.TestServiceState");
      }
      paramPhone.getContext().registerReceiver(mIntentReceiver, localIntentFilter, null, mPhone.getHandler());
    }
  }
  
  private void handleHandoverFailedIntent()
  {
    Object localObject = (ImsPhone)mPhone;
    if (localObject == null) {
      return;
    }
    localObject = ((ImsPhone)localObject).getForegroundCall();
    if (localObject == null) {
      return;
    }
    localObject = ((ImsPhoneCall)localObject).getImsCall();
    if (localObject == null) {
      return;
    }
    ((ImsCall)localObject).getImsCallSessionListenerProxy().callSessionHandoverFailed(((ImsCall)localObject).getCallSession(), 14, 18, new ImsReasonInfo());
  }
  
  private void handleSuppServiceFailedIntent(Intent paramIntent)
  {
    ImsPhone localImsPhone = (ImsPhone)mPhone;
    if (localImsPhone == null) {
      return;
    }
    int i = paramIntent.getIntExtra("failureCode", 0);
    localImsPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.values()[i]);
  }
  
  private void handleTestConferenceEventPackage(Context paramContext, String paramString)
  {
    Object localObject = (ImsPhone)mPhone;
    if (localObject == null) {
      return;
    }
    localObject = ((ImsPhone)localObject).getForegroundCall();
    if (localObject == null) {
      return;
    }
    localObject = ((ImsPhoneCall)localObject).getImsCall();
    if (localObject == null) {
      return;
    }
    paramContext = new File(paramContext.getFilesDir(), paramString);
    try
    {
      paramString = new FileInputStream(paramContext);
      paramContext = new TestConferenceEventPackageParser(paramString).parse();
      if (paramContext == null) {
        return;
      }
      ((ImsCall)localObject).conferenceStateUpdated(paramContext);
      return;
    }
    catch (FileNotFoundException paramString)
    {
      paramString = new StringBuilder();
      paramString.append("Test conference event package file not found: ");
      paramString.append(paramContext.getAbsolutePath());
      log(paramString.toString());
    }
  }
  
  private void handleTestDialogEventPackageIntent(Intent paramIntent)
  {
    Object localObject = (ImsPhone)mPhone;
    if (localObject == null) {
      return;
    }
    localObject = ((ImsPhone)localObject).getExternalCallTracker();
    if (localObject == null) {
      return;
    }
    if (paramIntent.hasExtra("startPackage"))
    {
      mImsExternalCallStates.clear();
    }
    else if (paramIntent.hasExtra("sendPackage"))
    {
      ((ImsExternalCallTracker)localObject).refreshExternalCallState(mImsExternalCallStates);
      mImsExternalCallStates.clear();
    }
    else if (paramIntent.hasExtra("dialogId"))
    {
      paramIntent = new ImsExternalCallState(paramIntent.getIntExtra("dialogId", 0), Uri.parse(paramIntent.getStringExtra("number")), paramIntent.getBooleanExtra("canPull", true), paramIntent.getIntExtra("state", 1), 2, false);
      mImsExternalCallStates.add(paramIntent);
    }
  }
  
  private static void log(String paramString)
  {
    Rlog.d("TelephonyTester", paramString);
  }
  
  private void sendTestSuppServiceNotification(Intent paramIntent)
  {
    if ((paramIntent.hasExtra("code")) && (paramIntent.hasExtra("type")))
    {
      int i = paramIntent.getIntExtra("code", -1);
      int j = paramIntent.getIntExtra("type", -1);
      paramIntent = (ImsPhone)mPhone;
      if (paramIntent == null) {
        return;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Test supp service notification:");
      ((StringBuilder)localObject).append(i);
      log(((StringBuilder)localObject).toString());
      localObject = new SuppServiceNotification();
      code = i;
      notificationType = j;
      paramIntent.notifySuppSvcNotification((SuppServiceNotification)localObject);
    }
  }
  
  void dispose()
  {
    if (Build.IS_DEBUGGABLE) {
      mPhone.getContext().unregisterReceiver(mIntentReceiver);
    }
  }
  
  void overrideServiceState(ServiceState paramServiceState)
  {
    if ((mServiceStateTestIntent != null) && (paramServiceState != null))
    {
      if ((mServiceStateTestIntent.hasExtra("action")) && ("reset".equals(mServiceStateTestIntent.getStringExtra("action"))))
      {
        log("Service state override reset");
        return;
      }
      StringBuilder localStringBuilder;
      if (mServiceStateTestIntent.hasExtra("voice_reg_state"))
      {
        paramServiceState.setVoiceRegState(mServiceStateTestIntent.getIntExtra("voice_reg_state", 4));
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Override voice reg state with ");
        localStringBuilder.append(paramServiceState.getVoiceRegState());
        log(localStringBuilder.toString());
      }
      if (mServiceStateTestIntent.hasExtra("data_reg_state"))
      {
        paramServiceState.setDataRegState(mServiceStateTestIntent.getIntExtra("data_reg_state", 4));
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Override data reg state with ");
        localStringBuilder.append(paramServiceState.getDataRegState());
        log(localStringBuilder.toString());
      }
      if (mServiceStateTestIntent.hasExtra("voice_rat"))
      {
        paramServiceState.setRilVoiceRadioTechnology(mServiceStateTestIntent.getIntExtra("voice_rat", 0));
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Override voice rat with ");
        localStringBuilder.append(paramServiceState.getRilVoiceRadioTechnology());
        log(localStringBuilder.toString());
      }
      if (mServiceStateTestIntent.hasExtra("data_rat"))
      {
        paramServiceState.setRilDataRadioTechnology(mServiceStateTestIntent.getIntExtra("data_rat", 0));
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Override data rat with ");
        localStringBuilder.append(paramServiceState.getRilDataRadioTechnology());
        log(localStringBuilder.toString());
      }
      if (mServiceStateTestIntent.hasExtra("voice_roaming_type"))
      {
        paramServiceState.setVoiceRoamingType(mServiceStateTestIntent.getIntExtra("voice_roaming_type", 1));
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Override voice roaming type with ");
        localStringBuilder.append(paramServiceState.getVoiceRoamingType());
        log(localStringBuilder.toString());
      }
      if (mServiceStateTestIntent.hasExtra("data_roaming_type"))
      {
        paramServiceState.setDataRoamingType(mServiceStateTestIntent.getIntExtra("data_roaming_type", 1));
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Override data roaming type with ");
        localStringBuilder.append(paramServiceState.getDataRoamingType());
        log(localStringBuilder.toString());
      }
      return;
    }
  }
}
