package com.android.internal.telephony.cdma;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.MmiCode.State;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.uicc.UiccCardApplication;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CdmaMmiCode
  extends Handler
  implements MmiCode
{
  static final String ACTION_REGISTER = "**";
  static final int EVENT_SET_COMPLETE = 1;
  static final String LOG_TAG = "CdmaMmiCode";
  static final int MATCH_GROUP_ACTION = 2;
  static final int MATCH_GROUP_DIALING_NUMBER = 12;
  static final int MATCH_GROUP_POUND_STRING = 1;
  static final int MATCH_GROUP_PWD_CONFIRM = 11;
  static final int MATCH_GROUP_SERVICE_CODE = 3;
  static final int MATCH_GROUP_SIA = 5;
  static final int MATCH_GROUP_SIB = 7;
  static final int MATCH_GROUP_SIC = 9;
  static final String SC_PIN = "04";
  static final String SC_PIN2 = "042";
  static final String SC_PUK = "05";
  static final String SC_PUK2 = "052";
  static Pattern sPatternSuppService = Pattern.compile("((\\*|#|\\*#|\\*\\*|##)(\\d{2,3})(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*))?)?)?)?#)(.*)");
  String mAction;
  Context mContext;
  String mDialingNumber;
  CharSequence mMessage;
  GsmCdmaPhone mPhone;
  String mPoundString;
  String mPwd;
  String mSc;
  String mSia;
  String mSib;
  String mSic;
  MmiCode.State mState = MmiCode.State.PENDING;
  UiccCardApplication mUiccApplication;
  
  CdmaMmiCode(GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication)
  {
    super(paramGsmCdmaPhone.getHandler().getLooper());
    mPhone = paramGsmCdmaPhone;
    mContext = paramGsmCdmaPhone.getContext();
    mUiccApplication = paramUiccCardApplication;
  }
  
  private CharSequence getScString()
  {
    if ((mSc != null) && (isPinPukCommand())) {
      return mContext.getText(17039409);
    }
    return "";
  }
  
  private void handlePasswordError(int paramInt)
  {
    mState = MmiCode.State.FAILED;
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    localStringBuilder.append(mContext.getText(paramInt));
    mMessage = localStringBuilder;
    mPhone.onMMIDone(this);
  }
  
  private static String makeEmptyNull(String paramString)
  {
    if ((paramString != null) && (paramString.length() == 0)) {
      return null;
    }
    return paramString;
  }
  
  public static CdmaMmiCode newFromDialString(String paramString, GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication)
  {
    Object localObject = null;
    Matcher localMatcher = sPatternSuppService.matcher(paramString);
    paramString = localObject;
    if (localMatcher.matches())
    {
      paramString = new CdmaMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
      mPoundString = makeEmptyNull(localMatcher.group(1));
      mAction = makeEmptyNull(localMatcher.group(2));
      mSc = makeEmptyNull(localMatcher.group(3));
      mSia = makeEmptyNull(localMatcher.group(5));
      mSib = makeEmptyNull(localMatcher.group(7));
      mSic = makeEmptyNull(localMatcher.group(9));
      mPwd = makeEmptyNull(localMatcher.group(11));
      mDialingNumber = makeEmptyNull(localMatcher.group(12));
    }
    return paramString;
  }
  
  private void onSetComplete(Message paramMessage, AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      if ((exception instanceof CommandException))
      {
        paramAsyncResult = ((CommandException)exception).getCommandError();
        if (paramAsyncResult == CommandException.Error.PASSWORD_INCORRECT)
        {
          if (isPinPukCommand())
          {
            if ((!mSc.equals("05")) && (!mSc.equals("052"))) {
              localStringBuilder.append(mContext.getText(17039609));
            } else {
              localStringBuilder.append(mContext.getText(17039610));
            }
            int i = arg1;
            if (i <= 0)
            {
              Rlog.d("CdmaMmiCode", "onSetComplete: PUK locked, cancel as lock screen will handle this");
              mState = MmiCode.State.CANCELLED;
            }
            else if (i > 0)
            {
              paramMessage = new StringBuilder();
              paramMessage.append("onSetComplete: attemptsRemaining=");
              paramMessage.append(i);
              Rlog.d("CdmaMmiCode", paramMessage.toString());
              localStringBuilder.append(mContext.getResources().getQuantityString(18153493, i, new Object[] { Integer.valueOf(i) }));
            }
          }
          else
          {
            localStringBuilder.append(mContext.getText(17040539));
          }
        }
        else if (paramAsyncResult == CommandException.Error.SIM_PUK2)
        {
          localStringBuilder.append(mContext.getText(17039609));
          localStringBuilder.append("\n");
          localStringBuilder.append(mContext.getText(17040458));
        }
        else if (paramAsyncResult == CommandException.Error.REQUEST_NOT_SUPPORTED)
        {
          if (mSc.equals("04")) {
            localStringBuilder.append(mContext.getText(17039919));
          }
        }
        else
        {
          localStringBuilder.append(mContext.getText(17040449));
        }
      }
      else
      {
        localStringBuilder.append(mContext.getText(17040449));
      }
    }
    else if (isRegister())
    {
      mState = MmiCode.State.COMPLETE;
      localStringBuilder.append(mContext.getText(17040983));
    }
    else
    {
      mState = MmiCode.State.FAILED;
      localStringBuilder.append(mContext.getText(17040449));
    }
    mMessage = localStringBuilder;
    mPhone.onMMIDone(this);
  }
  
  public void cancel()
  {
    if ((mState != MmiCode.State.COMPLETE) && (mState != MmiCode.State.FAILED))
    {
      mState = MmiCode.State.CANCELLED;
      mPhone.onMMIDone(this);
      return;
    }
  }
  
  public String getDialString()
  {
    return null;
  }
  
  public CharSequence getMessage()
  {
    return mMessage;
  }
  
  public Phone getPhone()
  {
    return mPhone;
  }
  
  public String getServiceCode()
  {
    return mSc;
  }
  
  public MmiCode.State getState()
  {
    return mState;
  }
  
  public ResultReceiver getUssdCallbackReceiver()
  {
    return null;
  }
  
  public void handleMessage(Message paramMessage)
  {
    if (what == 1) {
      onSetComplete(paramMessage, (AsyncResult)obj);
    } else {
      Rlog.e("CdmaMmiCode", "Unexpected reply");
    }
  }
  
  public boolean isCancelable()
  {
    return false;
  }
  
  public boolean isPinPukCommand()
  {
    boolean bool;
    if ((mSc != null) && ((mSc.equals("04")) || (mSc.equals("042")) || (mSc.equals("05")) || (mSc.equals("052")))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isRegister()
  {
    boolean bool;
    if ((mAction != null) && (mAction.equals("**"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUssdRequest()
  {
    Rlog.w("CdmaMmiCode", "isUssdRequest is not implemented in CdmaMmiCode");
    return false;
  }
  
  public void processCode()
  {
    try
    {
      if (isPinPukCommand())
      {
        Object localObject1 = mSia;
        Object localObject2 = mSib;
        int i = ((String)localObject2).length();
        if (isRegister())
        {
          if (!((String)localObject2).equals(mSic)) {
            handlePasswordError(17040439);
          } else if ((i >= 4) && (i <= 8))
          {
            if ((mSc.equals("04")) && (mUiccApplication != null) && (mUiccApplication.getState() == IccCardApplicationStatus.AppState.APPSTATE_PUK))
            {
              handlePasswordError(17040457);
            }
            else if (mUiccApplication != null)
            {
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("process mmi service code using UiccApp sc=");
              localStringBuilder.append(mSc);
              Rlog.d("CdmaMmiCode", localStringBuilder.toString());
              if (mSc.equals("04"))
              {
                mUiccApplication.changeIccLockPassword((String)localObject1, (String)localObject2, obtainMessage(1, this));
              }
              else if (mSc.equals("042"))
              {
                mUiccApplication.changeIccFdnPassword((String)localObject1, (String)localObject2, obtainMessage(1, this));
              }
              else if (mSc.equals("05"))
              {
                mUiccApplication.supplyPuk((String)localObject1, (String)localObject2, obtainMessage(1, this));
              }
              else if (mSc.equals("052"))
              {
                mUiccApplication.supplyPuk2((String)localObject1, (String)localObject2, obtainMessage(1, this));
              }
              else
              {
                localObject2 = new java/lang/RuntimeException;
                localObject1 = new java/lang/StringBuilder;
                ((StringBuilder)localObject1).<init>();
                ((StringBuilder)localObject1).append("Unsupported service code=");
                ((StringBuilder)localObject1).append(mSc);
                ((RuntimeException)localObject2).<init>(((StringBuilder)localObject1).toString());
                throw ((Throwable)localObject2);
              }
            }
            else
            {
              localObject1 = new java/lang/RuntimeException;
              ((RuntimeException)localObject1).<init>("No application mUiccApplicaiton is null");
              throw ((Throwable)localObject1);
            }
          }
          else {
            handlePasswordError(17040141);
          }
        }
        else
        {
          localObject2 = new java/lang/RuntimeException;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Ivalid register/action=");
          ((StringBuilder)localObject1).append(mAction);
          ((RuntimeException)localObject2).<init>(((StringBuilder)localObject1).toString());
          throw ((Throwable)localObject2);
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      mState = MmiCode.State.FAILED;
      mMessage = mContext.getText(17040449);
      mPhone.onMMIDone(this);
    }
  }
}
