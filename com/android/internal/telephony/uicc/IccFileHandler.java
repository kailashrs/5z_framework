package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.CommandsInterface;
import java.util.ArrayList;

public abstract class IccFileHandler
  extends Handler
  implements IccConstants
{
  protected static final int COMMAND_GET_RESPONSE = 192;
  protected static final int COMMAND_READ_BINARY = 176;
  protected static final int COMMAND_READ_RECORD = 178;
  protected static final int COMMAND_SEEK = 162;
  protected static final int COMMAND_UPDATE_BINARY = 214;
  protected static final int COMMAND_UPDATE_RECORD = 220;
  protected static final int EF_TYPE_CYCLIC = 3;
  protected static final int EF_TYPE_LINEAR_FIXED = 1;
  protected static final int EF_TYPE_TRANSPARENT = 0;
  protected static final int EVENT_GET_BINARY_SIZE_DONE = 4;
  protected static final int EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE = 8;
  protected static final int EVENT_GET_RECORD_SIZE_DONE = 6;
  protected static final int EVENT_GET_RECORD_SIZE_IMG_DONE = 11;
  protected static final int EVENT_READ_BINARY_DONE = 5;
  protected static final int EVENT_READ_ICON_DONE = 10;
  protected static final int EVENT_READ_IMG_DONE = 9;
  protected static final int EVENT_READ_RECORD_DONE = 7;
  protected static final int GET_RESPONSE_EF_IMG_SIZE_BYTES = 10;
  protected static final int GET_RESPONSE_EF_SIZE_BYTES = 15;
  protected static final int READ_RECORD_MODE_ABSOLUTE = 4;
  protected static final int RESPONSE_DATA_ACCESS_CONDITION_1 = 8;
  protected static final int RESPONSE_DATA_ACCESS_CONDITION_2 = 9;
  protected static final int RESPONSE_DATA_ACCESS_CONDITION_3 = 10;
  protected static final int RESPONSE_DATA_FILE_ID_1 = 4;
  protected static final int RESPONSE_DATA_FILE_ID_2 = 5;
  protected static final int RESPONSE_DATA_FILE_SIZE_1 = 2;
  protected static final int RESPONSE_DATA_FILE_SIZE_2 = 3;
  protected static final int RESPONSE_DATA_FILE_STATUS = 11;
  protected static final int RESPONSE_DATA_FILE_TYPE = 6;
  protected static final int RESPONSE_DATA_LENGTH = 12;
  protected static final int RESPONSE_DATA_RECORD_LENGTH = 14;
  protected static final int RESPONSE_DATA_RFU_1 = 0;
  protected static final int RESPONSE_DATA_RFU_2 = 1;
  protected static final int RESPONSE_DATA_RFU_3 = 7;
  protected static final int RESPONSE_DATA_STRUCTURE = 13;
  protected static final int TYPE_DF = 2;
  protected static final int TYPE_EF = 4;
  protected static final int TYPE_MF = 1;
  protected static final int TYPE_RFU = 0;
  private static final boolean VDBG = false;
  protected final String mAid;
  protected final CommandsInterface mCi;
  protected final UiccCardApplication mParentApp;
  
  protected IccFileHandler(UiccCardApplication paramUiccCardApplication, String paramString, CommandsInterface paramCommandsInterface)
  {
    mParentApp = paramUiccCardApplication;
    mAid = paramString;
    mCi = paramCommandsInterface;
  }
  
  private boolean processException(Message paramMessage, AsyncResult paramAsyncResult)
  {
    boolean bool = false;
    IccIoResult localIccIoResult = (IccIoResult)result;
    if (exception != null)
    {
      sendResult(paramMessage, null, exception);
      bool = true;
    }
    else
    {
      paramAsyncResult = localIccIoResult.getException();
      if (paramAsyncResult != null)
      {
        sendResult(paramMessage, null, paramAsyncResult);
        bool = true;
      }
    }
    return bool;
  }
  
  private void sendResult(Message paramMessage, Object paramObject, Throwable paramThrowable)
  {
    if (paramMessage == null) {
      return;
    }
    AsyncResult.forMessage(paramMessage, paramObject, paramThrowable);
    paramMessage.sendToTarget();
  }
  
  public void dispose() {}
  
  protected String getCommonIccEFPath(int paramInt)
  {
    if ((paramInt != 12037) && (paramInt != 12258))
    {
      if (paramInt != 20256)
      {
        if (paramInt != 20272)
        {
          if ((paramInt != 28480) && (paramInt != 28645)) {
            switch (paramInt)
            {
            default: 
              switch (paramInt)
              {
              default: 
                return null;
              }
              break;
            }
          }
          return "3F007F10";
        }
        return "3F007F105F3A";
      }
      return "3F007F105F50";
    }
    return "3F00";
  }
  
  public void getEFLinearRecordSize(int paramInt, Message paramMessage)
  {
    getEFLinearRecordSize(paramInt, getEFPath(paramInt), paramMessage);
  }
  
  public void getEFLinearRecordSize(int paramInt, String paramString, Message paramMessage)
  {
    if (paramString == null) {
      paramString = getEFPath(paramInt);
    }
    paramMessage = obtainMessage(8, new LoadLinearFixedContext(paramInt, paramString, paramMessage));
    mCi.iccIOForApp(192, paramInt, paramString, 0, 0, 15, null, null, mAid, paramMessage);
  }
  
  protected abstract String getEFPath(int paramInt);
  
  public void handleMessage(Message paramMessage)
  {
    Message localMessage = null;
    Object localObject1 = localMessage;
    try
    {
      Object localObject2;
      Object localObject3;
      switch (what)
      {
      default: 
        break;
      case 8: 
        localObject1 = localMessage;
        localObject2 = (AsyncResult)obj;
        localObject1 = localMessage;
        localObject3 = (LoadLinearFixedContext)userObj;
        localObject1 = localMessage;
        localObject2 = (IccIoResult)result;
        localObject1 = localMessage;
        localMessage = mOnLoaded;
        localObject1 = localMessage;
        if (!processException(localMessage, (AsyncResult)obj))
        {
          localObject1 = localMessage;
          localObject3 = payload;
          if ((4 == localObject3[6]) && (1 == localObject3[13]))
          {
            localObject1 = localMessage;
            paramMessage = new int[3];
            paramMessage[0] = (localObject3[14] & 0xFF);
            paramMessage[1] = (((localObject3[2] & 0xFF) << 8) + (localObject3[3] & 0xFF));
            localObject1 = localMessage;
            paramMessage[2] = (paramMessage[1] / paramMessage[0]);
            localObject1 = localMessage;
            sendResult(localMessage, paramMessage, null);
          }
          else
          {
            localObject1 = localMessage;
            paramMessage = new com/android/internal/telephony/uicc/IccFileTypeMismatch;
            localObject1 = localMessage;
            paramMessage.<init>();
            localObject1 = localMessage;
            throw paramMessage;
          }
        }
        break;
      case 7: 
      case 9: 
        localObject1 = localMessage;
        localObject3 = (AsyncResult)obj;
        localObject1 = localMessage;
        localObject2 = (LoadLinearFixedContext)userObj;
        localObject1 = localMessage;
        IccIoResult localIccIoResult = (IccIoResult)result;
        localObject1 = localMessage;
        localMessage = mOnLoaded;
        localObject1 = localMessage;
        localObject3 = mPath;
        localObject1 = localMessage;
        if (!processException(localMessage, (AsyncResult)obj))
        {
          localObject1 = localMessage;
          if (!mLoadAll)
          {
            localObject1 = localMessage;
            sendResult(localMessage, payload, null);
          }
          else
          {
            localObject1 = localMessage;
            results.add(payload);
            localObject1 = localMessage;
            mRecordNum += 1;
            localObject1 = localMessage;
            if (mRecordNum > mCountRecords)
            {
              localObject1 = localMessage;
              sendResult(localMessage, results, null);
            }
            else
            {
              paramMessage = (Message)localObject3;
              if (localObject3 == null)
              {
                localObject1 = localMessage;
                paramMessage = getEFPath(mEfid);
              }
              localObject1 = localMessage;
              mCi.iccIOForApp(178, mEfid, paramMessage, mRecordNum, 4, mRecordSize, null, null, mAid, obtainMessage(7, localObject2));
            }
          }
        }
        break;
      case 6: 
      case 11: 
        localObject1 = localMessage;
        localObject3 = (AsyncResult)obj;
        localObject1 = localMessage;
        localObject2 = (LoadLinearFixedContext)userObj;
        localObject1 = localMessage;
        localObject3 = (IccIoResult)result;
        localObject1 = localMessage;
        localMessage = mOnLoaded;
        localObject1 = localMessage;
        if (processException(localMessage, (AsyncResult)obj))
        {
          localObject1 = localMessage;
          loge("exception caught from EVENT_GET_RECORD_SIZE");
        }
        else
        {
          localObject1 = localMessage;
          paramMessage = payload;
          localObject1 = localMessage;
          localObject3 = mPath;
          if (4 == paramMessage[6])
          {
            if (1 == paramMessage[13])
            {
              localObject1 = localMessage;
              mRecordSize = (paramMessage[14] & 0xFF);
              localObject1 = localMessage;
              mCountRecords = ((((paramMessage[2] & 0xFF) << 8) + (paramMessage[3] & 0xFF)) / mRecordSize);
              localObject1 = localMessage;
              if (mLoadAll)
              {
                localObject1 = localMessage;
                paramMessage = new java/util/ArrayList;
                localObject1 = localMessage;
                paramMessage.<init>(mCountRecords);
                localObject1 = localMessage;
                results = paramMessage;
              }
              paramMessage = (Message)localObject3;
              if (localObject3 == null)
              {
                localObject1 = localMessage;
                paramMessage = getEFPath(mEfid);
              }
              localObject1 = localMessage;
              mCi.iccIOForApp(178, mEfid, paramMessage, mRecordNum, 4, mRecordSize, null, null, mAid, obtainMessage(7, localObject2));
            }
            else
            {
              localObject1 = localMessage;
              paramMessage = new com/android/internal/telephony/uicc/IccFileTypeMismatch;
              localObject1 = localMessage;
              paramMessage.<init>();
              localObject1 = localMessage;
              throw paramMessage;
            }
          }
          else
          {
            localObject1 = localMessage;
            paramMessage = new com/android/internal/telephony/uicc/IccFileTypeMismatch;
            localObject1 = localMessage;
            paramMessage.<init>();
            localObject1 = localMessage;
            throw paramMessage;
          }
        }
        break;
      case 5: 
      case 10: 
        localObject1 = localMessage;
        localObject3 = (AsyncResult)obj;
        localObject1 = localMessage;
        localMessage = (Message)userObj;
        localObject1 = localMessage;
        localObject3 = (IccIoResult)result;
        localObject1 = localMessage;
        if (!processException(localMessage, (AsyncResult)obj))
        {
          localObject1 = localMessage;
          sendResult(localMessage, payload, null);
        }
        break;
      case 4: 
        localObject1 = localMessage;
        localObject3 = (AsyncResult)obj;
        localObject1 = localMessage;
        localMessage = (Message)userObj;
        localObject1 = localMessage;
        localObject3 = (IccIoResult)result;
        localObject1 = localMessage;
        if (!processException(localMessage, (AsyncResult)obj))
        {
          localObject1 = localMessage;
          localObject3 = payload;
          localObject1 = localMessage;
          int i = arg1;
          if (4 == localObject3[6])
          {
            if (localObject3[13] == 0)
            {
              int j = localObject3[2];
              int k = localObject3[3];
              localObject1 = localMessage;
              mCi.iccIOForApp(176, i, getEFPath(i), 0, 0, ((j & 0xFF) << 8) + (k & 0xFF), null, null, mAid, obtainMessage(5, i, 0, localMessage));
            }
            else
            {
              localObject1 = localMessage;
              paramMessage = new com/android/internal/telephony/uicc/IccFileTypeMismatch;
              localObject1 = localMessage;
              paramMessage.<init>();
              localObject1 = localMessage;
              throw paramMessage;
            }
          }
          else
          {
            localObject1 = localMessage;
            paramMessage = new com/android/internal/telephony/uicc/IccFileTypeMismatch;
            localObject1 = localMessage;
            paramMessage.<init>();
            localObject1 = localMessage;
            throw paramMessage;
          }
        }
        break;
      }
    }
    catch (Exception paramMessage)
    {
      if (localObject1 != null)
      {
        sendResult((Message)localObject1, null, paramMessage);
      }
      else
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("uncaught exception");
        ((StringBuilder)localObject1).append(paramMessage);
        loge(((StringBuilder)localObject1).toString());
      }
    }
  }
  
  public void loadEFImgLinearFixed(int paramInt, Message paramMessage)
  {
    paramMessage = obtainMessage(11, new LoadLinearFixedContext(20256, paramInt, paramMessage));
    mCi.iccIOForApp(192, 20256, getEFPath(20256), paramInt, 4, 10, null, null, mAid, paramMessage);
  }
  
  public void loadEFImgTransparent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Message paramMessage)
  {
    paramMessage = obtainMessage(10, paramInt1, 0, paramMessage);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IccFileHandler: loadEFImgTransparent fileid = ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" filePath = ");
    localStringBuilder.append(getEFPath(20256));
    localStringBuilder.append(" highOffset = ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(" lowOffset = ");
    localStringBuilder.append(paramInt3);
    localStringBuilder.append(" length = ");
    localStringBuilder.append(paramInt4);
    logd(localStringBuilder.toString());
    mCi.iccIOForApp(176, paramInt1, getEFPath(20256), paramInt2, paramInt3, paramInt4, null, null, mAid, paramMessage);
  }
  
  public void loadEFLinearFixed(int paramInt1, int paramInt2, Message paramMessage)
  {
    loadEFLinearFixed(paramInt1, getEFPath(paramInt1), paramInt2, paramMessage);
  }
  
  public void loadEFLinearFixed(int paramInt1, String paramString, int paramInt2, Message paramMessage)
  {
    if (paramString == null) {
      paramString = getEFPath(paramInt1);
    }
    paramMessage = obtainMessage(6, new LoadLinearFixedContext(paramInt1, paramInt2, paramString, paramMessage));
    mCi.iccIOForApp(192, paramInt1, paramString, 0, 0, 15, null, null, mAid, paramMessage);
  }
  
  public void loadEFLinearFixedAll(int paramInt, Message paramMessage)
  {
    loadEFLinearFixedAll(paramInt, getEFPath(paramInt), paramMessage);
  }
  
  public void loadEFLinearFixedAll(int paramInt, String paramString, Message paramMessage)
  {
    if (paramString == null) {
      paramString = getEFPath(paramInt);
    }
    paramMessage = obtainMessage(6, new LoadLinearFixedContext(paramInt, paramString, paramMessage));
    mCi.iccIOForApp(192, paramInt, paramString, 0, 0, 15, null, null, mAid, paramMessage);
  }
  
  public void loadEFTransparent(int paramInt1, int paramInt2, Message paramMessage)
  {
    paramMessage = obtainMessage(5, paramInt1, 0, paramMessage);
    mCi.iccIOForApp(176, paramInt1, getEFPath(paramInt1), 0, 0, paramInt2, null, null, mAid, paramMessage);
  }
  
  public void loadEFTransparent(int paramInt, Message paramMessage)
  {
    paramMessage = obtainMessage(4, paramInt, 0, paramMessage);
    mCi.iccIOForApp(192, paramInt, getEFPath(paramInt), 0, 0, 15, null, null, mAid, paramMessage);
  }
  
  protected abstract void logd(String paramString);
  
  protected abstract void loge(String paramString);
  
  public void updateEFLinearFixed(int paramInt1, int paramInt2, byte[] paramArrayOfByte, String paramString, Message paramMessage)
  {
    mCi.iccIOForApp(220, paramInt1, getEFPath(paramInt1), paramInt2, 4, paramArrayOfByte.length, IccUtils.bytesToHexString(paramArrayOfByte), paramString, mAid, paramMessage);
  }
  
  public void updateEFLinearFixed(int paramInt1, String paramString1, int paramInt2, byte[] paramArrayOfByte, String paramString2, Message paramMessage)
  {
    if (paramString1 == null) {
      paramString1 = getEFPath(paramInt1);
    }
    mCi.iccIOForApp(220, paramInt1, paramString1, paramInt2, 4, paramArrayOfByte.length, IccUtils.bytesToHexString(paramArrayOfByte), paramString2, mAid, paramMessage);
  }
  
  public void updateEFTransparent(int paramInt, byte[] paramArrayOfByte, Message paramMessage)
  {
    mCi.iccIOForApp(214, paramInt, getEFPath(paramInt), 0, 0, paramArrayOfByte.length, IccUtils.bytesToHexString(paramArrayOfByte), null, mAid, paramMessage);
  }
  
  static class LoadLinearFixedContext
  {
    int mCountRecords;
    int mEfid;
    boolean mLoadAll;
    Message mOnLoaded;
    String mPath;
    int mRecordNum;
    int mRecordSize;
    ArrayList<byte[]> results;
    
    LoadLinearFixedContext(int paramInt1, int paramInt2, Message paramMessage)
    {
      mEfid = paramInt1;
      mRecordNum = paramInt2;
      mOnLoaded = paramMessage;
      mLoadAll = false;
      mPath = null;
    }
    
    LoadLinearFixedContext(int paramInt1, int paramInt2, String paramString, Message paramMessage)
    {
      mEfid = paramInt1;
      mRecordNum = paramInt2;
      mOnLoaded = paramMessage;
      mLoadAll = false;
      mPath = paramString;
    }
    
    LoadLinearFixedContext(int paramInt, Message paramMessage)
    {
      mEfid = paramInt;
      mRecordNum = 1;
      mLoadAll = true;
      mOnLoaded = paramMessage;
      mPath = null;
    }
    
    LoadLinearFixedContext(int paramInt, String paramString, Message paramMessage)
    {
      mEfid = paramInt;
      mRecordNum = 1;
      mLoadAll = true;
      mOnLoaded = paramMessage;
      mPath = paramString;
    }
  }
}
