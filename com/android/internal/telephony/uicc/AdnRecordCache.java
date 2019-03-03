package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.util.SparseArray;
import com.android.internal.telephony.gsm.UsimPhoneBookManager;
import java.util.ArrayList;
import java.util.Iterator;

public class AdnRecordCache
  extends Handler
  implements IccConstants
{
  static final int EVENT_LOAD_ALL_ADN_LIKE_DONE = 1;
  static final int EVENT_UPDATE_ADN_DONE = 2;
  SparseArray<ArrayList<AdnRecord>> mAdnLikeFiles = new SparseArray();
  SparseArray<ArrayList<Message>> mAdnLikeWaiters = new SparseArray();
  private IccFileHandler mFh;
  SparseArray<Message> mUserWriteResponse = new SparseArray();
  private UsimPhoneBookManager mUsimPhoneBookManager;
  int rowId = -1;
  
  AdnRecordCache(IccFileHandler paramIccFileHandler)
  {
    mFh = paramIccFileHandler;
    mUsimPhoneBookManager = new UsimPhoneBookManager(mFh, this);
  }
  
  private void clearUserWriters()
  {
    int i = mUserWriteResponse.size();
    for (int j = 0; j < i; j++) {
      sendErrorResponse((Message)mUserWriteResponse.valueAt(j), "AdnCace reset");
    }
    mUserWriteResponse.clear();
  }
  
  private void clearWaiters()
  {
    int i = mAdnLikeWaiters.size();
    for (int j = 0; j < i; j++) {
      notifyWaiters((ArrayList)mAdnLikeWaiters.valueAt(j), new AsyncResult(null, null, new RuntimeException("AdnCache reset")));
    }
    mAdnLikeWaiters.clear();
  }
  
  private void notifyWaiters(ArrayList<Message> paramArrayList, AsyncResult paramAsyncResult)
  {
    if (paramArrayList == null) {
      return;
    }
    int i = 0;
    int j = paramArrayList.size();
    while (i < j)
    {
      Message localMessage = (Message)paramArrayList.get(i);
      AsyncResult.forMessage(localMessage, result, exception);
      localMessage.sendToTarget();
      i++;
    }
  }
  
  private void sendErrorResponse(Message paramMessage, String paramString)
  {
    if (paramMessage != null)
    {
      paramString = new RuntimeException(paramString);
      forMessageexception = paramString;
      paramMessage.sendToTarget();
    }
  }
  
  public int extensionEfForEf(int paramInt)
  {
    if (paramInt != 20272)
    {
      if (paramInt != 28480)
      {
        if (paramInt != 28489)
        {
          if (paramInt != 28615)
          {
            switch (paramInt)
            {
            default: 
              return -1;
            case 28475: 
              return 28491;
            }
            return 28490;
          }
          return 28616;
        }
        return 28492;
      }
      return 28490;
    }
    return 0;
  }
  
  public ArrayList<AdnRecord> getRecordsIfLoaded(int paramInt)
  {
    return (ArrayList)mAdnLikeFiles.get(paramInt);
  }
  
  public int getRowIdForNewAddedRecord()
  {
    return rowId;
  }
  
  public void getUSIMPhoneBookProperty(int paramInt, Message paramMessage)
  {
    int[] arrayOfInt = mUsimPhoneBookManager.getUSIMPhoneBookProperty();
    if (paramMessage != null)
    {
      forMessageresult = arrayOfInt;
      paramMessage.sendToTarget();
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    AsyncResult localAsyncResult;
    int i;
    switch (what)
    {
    default: 
      break;
    case 2: 
      localAsyncResult = (AsyncResult)obj;
      i = arg1;
      int j = arg2;
      paramMessage = (AdnRecord)userObj;
      if (exception == null)
      {
        ((ArrayList)mAdnLikeFiles.get(i)).set(j - 1, paramMessage);
        mUsimPhoneBookManager.invalidateCache();
      }
      paramMessage = (Message)mUserWriteResponse.get(i);
      mUserWriteResponse.delete(i);
      if (paramMessage != null)
      {
        AsyncResult.forMessage(paramMessage, null, exception);
        paramMessage.sendToTarget();
      }
      break;
    case 1: 
      localAsyncResult = (AsyncResult)obj;
      i = arg1;
      paramMessage = (ArrayList)mAdnLikeWaiters.get(i);
      mAdnLikeWaiters.delete(i);
      if (exception == null) {
        mAdnLikeFiles.put(i, (ArrayList)result);
      }
      notifyWaiters(paramMessage, localAsyncResult);
    }
  }
  
  public void requestLoadAllAdnLike(int paramInt1, int paramInt2, Message paramMessage)
  {
    if (paramInt1 == 20272) {
      localObject = mUsimPhoneBookManager.loadEfFilesFromUsim();
    } else {
      localObject = getRecordsIfLoaded(paramInt1);
    }
    if (localObject != null)
    {
      if (paramMessage != null)
      {
        forMessageresult = localObject;
        paramMessage.sendToTarget();
      }
      return;
    }
    Object localObject = (ArrayList)mAdnLikeWaiters.get(paramInt1);
    if (localObject != null)
    {
      ((ArrayList)localObject).add(paramMessage);
      return;
    }
    localObject = new ArrayList();
    ((ArrayList)localObject).add(paramMessage);
    mAdnLikeWaiters.put(paramInt1, localObject);
    if (paramInt2 < 0)
    {
      if (paramMessage != null)
      {
        AsyncResult localAsyncResult = AsyncResult.forMessage(paramMessage);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("EF is not known ADN-like EF:0x");
        ((StringBuilder)localObject).append(Integer.toHexString(paramInt1).toUpperCase());
        exception = new RuntimeException(((StringBuilder)localObject).toString());
        paramMessage.sendToTarget();
      }
      return;
    }
    new AdnRecordLoader(mFh).loadAllFromEF(paramInt1, paramInt2, obtainMessage(1, paramInt1, 0));
  }
  
  public void reset()
  {
    mAdnLikeFiles.clear();
    mUsimPhoneBookManager.reset();
    clearWaiters();
    clearUserWriters();
  }
  
  public void updateAdnByIndex(int paramInt1, AdnRecord paramAdnRecord, int paramInt2, String paramString, Message paramMessage)
  {
    int i = extensionEfForEf(paramInt1);
    if (i < 0)
    {
      paramAdnRecord = new StringBuilder();
      paramAdnRecord.append("EF is not known ADN-like EF:0x");
      paramAdnRecord.append(Integer.toHexString(paramInt1).toUpperCase());
      sendErrorResponse(paramMessage, paramAdnRecord.toString());
      return;
    }
    if ((Message)mUserWriteResponse.get(paramInt1) != null)
    {
      paramAdnRecord = new StringBuilder();
      paramAdnRecord.append("Have pending update for EF:0x");
      paramAdnRecord.append(Integer.toHexString(paramInt1).toUpperCase());
      sendErrorResponse(paramMessage, paramAdnRecord.toString());
      return;
    }
    mUserWriteResponse.put(paramInt1, paramMessage);
    new AdnRecordLoader(mFh).updateEF(paramAdnRecord, paramInt1, i, paramInt2, paramString, obtainMessage(2, paramInt1, paramInt2, paramAdnRecord));
  }
  
  public void updateAdnBySearch(int paramInt, AdnRecord paramAdnRecord1, AdnRecord paramAdnRecord2, String paramString, Message paramMessage)
  {
    int i = paramInt;
    int j = extensionEfForEf(paramInt);
    if (j < 0)
    {
      paramAdnRecord1 = new StringBuilder();
      paramAdnRecord1.append("EF is not known ADN-like EF:0x");
      paramAdnRecord1.append(Integer.toHexString(paramInt).toUpperCase());
      sendErrorResponse(paramMessage, paramAdnRecord1.toString());
      return;
    }
    ArrayList localArrayList;
    if (i == 20272) {
      localArrayList = mUsimPhoneBookManager.loadEfFilesFromUsim();
    } else {
      localArrayList = getRecordsIfLoaded(paramInt);
    }
    if (localArrayList == null)
    {
      paramAdnRecord1 = new StringBuilder();
      paramAdnRecord1.append("Adn list not exist for EF:0x");
      paramAdnRecord1.append(Integer.toHexString(paramInt).toUpperCase());
      sendErrorResponse(paramMessage, paramAdnRecord1.toString());
      return;
    }
    int k = -1;
    Object localObject = localArrayList.iterator();
    for (int m = 1;; m++)
    {
      paramInt = k;
      if (!((Iterator)localObject).hasNext()) {
        break;
      }
      if (paramAdnRecord1.isEqual((AdnRecord)((Iterator)localObject).next()))
      {
        paramInt = m;
        break;
      }
    }
    if (paramInt == -1)
    {
      paramAdnRecord2 = new StringBuilder();
      paramAdnRecord2.append("Adn record don't exist for ");
      paramAdnRecord2.append(paramAdnRecord1);
      sendErrorResponse(paramMessage, paramAdnRecord2.toString());
      return;
    }
    rowId = paramInt;
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("update oldADN:");
    ((StringBuilder)localObject).append(paramAdnRecord1.toString());
    ((StringBuilder)localObject).append(", newAdn:");
    ((StringBuilder)localObject).append(paramAdnRecord2.toString());
    ((StringBuilder)localObject).append(",index :");
    ((StringBuilder)localObject).append(paramInt);
    Rlog.d("AdnRecordCache", ((StringBuilder)localObject).toString());
    k = i;
    m = paramInt;
    if (i == 20272)
    {
      paramAdnRecord1 = (AdnRecord)localArrayList.get(paramInt - 1);
      k = mEfid;
      j = mExtRecord;
      m = mRecordNumber;
      mEfid = k;
      mExtRecord = j;
      mRecordNumber = m;
    }
    if ((Message)mUserWriteResponse.get(k) != null)
    {
      paramAdnRecord1 = new StringBuilder();
      paramAdnRecord1.append("Have pending update for EF:0x");
      paramAdnRecord1.append(Integer.toHexString(k).toUpperCase());
      sendErrorResponse(paramMessage, paramAdnRecord1.toString());
      return;
    }
    mUserWriteResponse.put(k, paramMessage);
    new AdnRecordLoader(mFh).updateEF(paramAdnRecord2, k, j, m, paramString, obtainMessage(2, k, m, paramAdnRecord2));
  }
}
