package com.android.internal.telephony.cat;

import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;

class RilMessageDecoder
  extends StateMachine
{
  private static final int CMD_PARAMS_READY = 2;
  private static final int CMD_START = 1;
  private static RilMessageDecoder[] mInstance = null;
  private static int mSimCount = 0;
  private Handler mCaller = null;
  private CommandParamsFactory mCmdParamsFactory = null;
  private RilMessage mCurrentRilMessage = null;
  private StateCmdParamsReady mStateCmdParamsReady = new StateCmdParamsReady(null);
  private StateStart mStateStart = new StateStart(null);
  
  private RilMessageDecoder()
  {
    super("RilMessageDecoder");
  }
  
  private RilMessageDecoder(Handler paramHandler, IccFileHandler paramIccFileHandler)
  {
    super("RilMessageDecoder");
    addState(mStateStart);
    addState(mStateCmdParamsReady);
    setInitialState(mStateStart);
    mCaller = paramHandler;
    mCmdParamsFactory = CommandParamsFactory.getInstance(this, paramIccFileHandler);
  }
  
  private boolean decodeMessageParams(RilMessage paramRilMessage)
  {
    mCurrentRilMessage = paramRilMessage;
    boolean bool;
    switch (mId)
    {
    default: 
      bool = false;
      break;
    case 2: 
    case 3: 
    case 5: 
      try
      {
        paramRilMessage = IccUtils.hexStringToBytes((String)mData);
        try
        {
          mCmdParamsFactory.make(BerTlv.decode(paramRilMessage));
          bool = true;
        }
        catch (ResultException localResultException)
        {
          paramRilMessage = new StringBuilder();
          paramRilMessage.append("decodeMessageParams: caught ResultException e=");
          paramRilMessage.append(localResultException);
          CatLog.d(this, paramRilMessage.toString());
          mCurrentRilMessage.mResCode = localResultException.result();
          sendCmdForExecution(mCurrentRilMessage);
          bool = false;
        }
        mCurrentRilMessage.mResCode = ResultCode.OK;
      }
      catch (Exception paramRilMessage)
      {
        CatLog.d(this, "decodeMessageParams dropping zombie messages");
        bool = false;
      }
    case 1: 
    case 4: 
      sendCmdForExecution(mCurrentRilMessage);
      bool = false;
    }
    return bool;
  }
  
  public static RilMessageDecoder getInstance(Handler paramHandler, IccFileHandler paramIccFileHandler, int paramInt)
  {
    try
    {
      if (mInstance == null)
      {
        mSimCount = TelephonyManager.getDefault().getSimCount();
        mInstance = new RilMessageDecoder[mSimCount];
        for (int i = 0; i < mSimCount; i++) {
          mInstance[i] = null;
        }
      }
      if ((paramInt != -1) && (paramInt < mSimCount))
      {
        if (mInstance[paramInt] == null)
        {
          RilMessageDecoder[] arrayOfRilMessageDecoder = mInstance;
          RilMessageDecoder localRilMessageDecoder = new com/android/internal/telephony/cat/RilMessageDecoder;
          localRilMessageDecoder.<init>(paramHandler, paramIccFileHandler);
          arrayOfRilMessageDecoder[paramInt] = localRilMessageDecoder;
        }
        paramHandler = mInstance[paramInt];
        return paramHandler;
      }
      paramHandler = new java/lang/StringBuilder;
      paramHandler.<init>();
      paramHandler.append("invaild slot id: ");
      paramHandler.append(paramInt);
      CatLog.d("RilMessageDecoder", paramHandler.toString());
      return null;
    }
    finally {}
  }
  
  private void sendCmdForExecution(RilMessage paramRilMessage)
  {
    mCaller.obtainMessage(10, new RilMessage(paramRilMessage)).sendToTarget();
  }
  
  public void dispose()
  {
    quitNow();
    mStateStart = null;
    mStateCmdParamsReady = null;
    mCmdParamsFactory.dispose();
    mCmdParamsFactory = null;
    mCurrentRilMessage = null;
    mCaller = null;
    mInstance = null;
  }
  
  public void sendMsgParamsDecoded(ResultCode paramResultCode, CommandParams paramCommandParams)
  {
    Message localMessage = obtainMessage(2);
    arg1 = paramResultCode.value();
    obj = paramCommandParams;
    sendMessage(localMessage);
  }
  
  public void sendStartDecodingMessageParams(RilMessage paramRilMessage)
  {
    Message localMessage = obtainMessage(1);
    obj = paramRilMessage;
    sendMessage(localMessage);
  }
  
  private class StateCmdParamsReady
    extends State
  {
    private StateCmdParamsReady() {}
    
    public boolean processMessage(Message paramMessage)
    {
      if (what == 2)
      {
        mCurrentRilMessage.mResCode = ResultCode.fromInt(arg1);
        mCurrentRilMessage.mData = obj;
        RilMessageDecoder.this.sendCmdForExecution(mCurrentRilMessage);
        transitionTo(mStateStart);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("StateCmdParamsReady expecting CMD_PARAMS_READY=2 got ");
        localStringBuilder.append(what);
        CatLog.d(this, localStringBuilder.toString());
        deferMessage(paramMessage);
      }
      return true;
    }
  }
  
  private class StateStart
    extends State
  {
    private StateStart() {}
    
    public boolean processMessage(Message paramMessage)
    {
      if (what == 1)
      {
        if (RilMessageDecoder.this.decodeMessageParams((RilMessage)obj)) {
          transitionTo(mStateCmdParamsReady);
        }
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("StateStart unexpected expecting START=1 got ");
        localStringBuilder.append(what);
        CatLog.d(this, localStringBuilder.toString());
      }
      return true;
    }
  }
}
