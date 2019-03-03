package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import java.util.ArrayList;

public class AdnRecordLoader
  extends Handler
{
  static final int EVENT_ADN_LOAD_ALL_DONE = 3;
  static final int EVENT_ADN_LOAD_DONE = 1;
  static final int EVENT_EF_LINEAR_RECORD_SIZE_DONE = 4;
  static final int EVENT_EXT_RECORD_LOAD_DONE = 2;
  static final int EVENT_UPDATE_RECORD_DONE = 5;
  static final String LOG_TAG = "AdnRecordLoader";
  static final boolean VDBG = false;
  ArrayList<AdnRecord> mAdns;
  int mEf;
  int mExtensionEF;
  private IccFileHandler mFh;
  int mPendingExtLoads;
  String mPin2;
  int mRecordNumber;
  Object mResult;
  Message mUserResponse;
  
  AdnRecordLoader(IccFileHandler paramIccFileHandler)
  {
    super(Looper.getMainLooper());
    mFh = paramIccFileHandler;
  }
  
  private String getEFPath(int paramInt)
  {
    if (paramInt == 28474) {
      return "3F007F10";
    }
    return null;
  }
  
  public void handleMessage(Message paramMessage)
  {
    try
    {
      Object localObject1;
      Object localObject2;
      switch (what)
      {
      default: 
        break;
      case 5: 
        localObject1 = (AsyncResult)obj;
        if (exception == null)
        {
          mPendingExtLoads = 0;
          mResult = null;
        }
        else
        {
          paramMessage = new java/lang/RuntimeException;
          paramMessage.<init>("update EF adn record failed", exception);
          throw paramMessage;
        }
        break;
      case 4: 
        paramMessage = (AsyncResult)obj;
        localObject2 = (AdnRecord)userObj;
        if (exception == null)
        {
          localObject1 = (int[])result;
          if ((localObject1.length == 3) && (mRecordNumber <= localObject1[2]))
          {
            localObject1 = ((AdnRecord)localObject2).buildAdnString(localObject1[0]);
            if (localObject1 != null)
            {
              mFh.updateEFLinearFixed(mEf, getEFPath(mEf), mRecordNumber, (byte[])localObject1, mPin2, obtainMessage(5));
              mPendingExtLoads = 1;
            }
            else
            {
              localObject1 = new java/lang/RuntimeException;
              ((RuntimeException)localObject1).<init>("wrong ADN format", exception);
              throw ((Throwable)localObject1);
            }
          }
          else
          {
            localObject1 = new java/lang/RuntimeException;
            ((RuntimeException)localObject1).<init>("get wrong EF record size format", exception);
            throw ((Throwable)localObject1);
          }
        }
        else
        {
          localObject1 = new java/lang/RuntimeException;
          ((RuntimeException)localObject1).<init>("get EF record size failed", exception);
          throw ((Throwable)localObject1);
        }
        break;
      case 3: 
        localObject1 = (AsyncResult)obj;
        paramMessage = (ArrayList)result;
        if (exception == null)
        {
          localObject1 = new java/util/ArrayList;
          ((ArrayList)localObject1).<init>(paramMessage.size());
          mAdns = ((ArrayList)localObject1);
          mResult = mAdns;
          mPendingExtLoads = 0;
          int i = 0;
          int j = paramMessage.size();
          while (i < j)
          {
            localObject1 = new com/android/internal/telephony/uicc/AdnRecord;
            ((AdnRecord)localObject1).<init>(mEf, 1 + i, (byte[])paramMessage.get(i));
            mAdns.add(localObject1);
            if (((AdnRecord)localObject1).hasExtendedRecord())
            {
              mPendingExtLoads += 1;
              mFh.loadEFLinearFixed(mExtensionEF, mExtRecord, obtainMessage(2, localObject1));
            }
            i++;
          }
        }
        paramMessage = new java/lang/RuntimeException;
        paramMessage.<init>("load failed", exception);
        throw paramMessage;
      case 2: 
        localObject2 = (AsyncResult)obj;
        localObject1 = (byte[])result;
        paramMessage = (AdnRecord)userObj;
        if (exception == null)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("ADN extension EF: 0x");
          ((StringBuilder)localObject2).append(Integer.toHexString(mExtensionEF));
          ((StringBuilder)localObject2).append(":");
          ((StringBuilder)localObject2).append(mExtRecord);
          ((StringBuilder)localObject2).append("\n");
          ((StringBuilder)localObject2).append(IccUtils.bytesToHexString((byte[])localObject1));
          Rlog.d("AdnRecordLoader", ((StringBuilder)localObject2).toString());
          paramMessage.appendExtRecord((byte[])localObject1);
        }
        else
        {
          Rlog.e("AdnRecordLoader", "Failed to read ext record. Clear the number now.");
          paramMessage.setNumber("");
        }
        mPendingExtLoads -= 1;
        break;
      case 1: 
        paramMessage = (AsyncResult)obj;
        localObject1 = (byte[])result;
        if (exception == null)
        {
          paramMessage = new com/android/internal/telephony/uicc/AdnRecord;
          paramMessage.<init>(mEf, mRecordNumber, (byte[])localObject1);
          mResult = paramMessage;
          if (paramMessage.hasExtendedRecord())
          {
            mPendingExtLoads = 1;
            mFh.loadEFLinearFixed(mExtensionEF, mExtRecord, obtainMessage(2, paramMessage));
          }
        }
        else
        {
          localObject1 = new java/lang/RuntimeException;
          ((RuntimeException)localObject1).<init>("load failed", exception);
          throw ((Throwable)localObject1);
        }
        break;
      }
      if ((mUserResponse != null) && (mPendingExtLoads == 0))
      {
        forMessagemUserResponse).result = mResult;
        mUserResponse.sendToTarget();
        mUserResponse = null;
      }
      return;
    }
    catch (RuntimeException paramMessage)
    {
      if (mUserResponse != null)
      {
        forMessagemUserResponse).exception = paramMessage;
        mUserResponse.sendToTarget();
        mUserResponse = null;
      }
    }
  }
  
  public void loadAllFromEF(int paramInt1, int paramInt2, Message paramMessage)
  {
    mEf = paramInt1;
    mExtensionEF = paramInt2;
    mUserResponse = paramMessage;
    mFh.loadEFLinearFixedAll(paramInt1, getEFPath(paramInt1), obtainMessage(3));
  }
  
  public void loadFromEF(int paramInt1, int paramInt2, int paramInt3, Message paramMessage)
  {
    mEf = paramInt1;
    mExtensionEF = paramInt2;
    mRecordNumber = paramInt3;
    mUserResponse = paramMessage;
    mFh.loadEFLinearFixed(paramInt1, getEFPath(paramInt1), paramInt3, obtainMessage(1));
  }
  
  public void updateEF(AdnRecord paramAdnRecord, int paramInt1, int paramInt2, int paramInt3, String paramString, Message paramMessage)
  {
    mEf = paramInt1;
    mExtensionEF = paramInt2;
    mRecordNumber = paramInt3;
    mUserResponse = paramMessage;
    mPin2 = paramString;
    mFh.getEFLinearRecordSize(paramInt1, getEFPath(paramInt1), obtainMessage(4, paramAdnRecord));
  }
}
