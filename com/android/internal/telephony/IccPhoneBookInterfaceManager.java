package com.android.internal.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.uicc.IccCardStatus.CardState;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class IccPhoneBookInterfaceManager
{
  protected static final boolean ALLOW_SIM_OP_IN_UI_THREAD = false;
  protected static final boolean DBG = true;
  protected static final int EVENT_GET_SIMPHONEBOOK_PROP_DONE = 4;
  protected static final int EVENT_GET_SIZE_DONE = 1;
  protected static final int EVENT_LOAD_DONE = 2;
  protected static final int EVENT_UPDATE_DONE = 3;
  static final String LOG_TAG = "IccPhoneBookIM";
  private static final HandlerThread mHandlerThread = new HandlerThread("IccPbHandlerLoader");
  protected AdnRecordCache mAdnCache;
  protected final IccPbHandler mBaseHandler;
  private UiccCardApplication mCurrentApp = null;
  private boolean mIs3gCard = false;
  protected final Object mLock = new Object();
  protected Phone mPhone;
  protected int[] mRecordSize;
  protected List<AdnRecord> mRecords;
  protected boolean mSuccess;
  
  static
  {
    mHandlerThread.start();
  }
  
  public IccPhoneBookInterfaceManager(Phone paramPhone)
  {
    mPhone = paramPhone;
    paramPhone = paramPhone.getIccRecords();
    if (paramPhone != null) {
      mAdnCache = paramPhone.getAdnCache();
    }
    mBaseHandler = new IccPbHandler(mHandlerThread.getLooper());
  }
  
  private boolean CheckSIMState()
  {
    Object localObject = IccCardStatus.CardState.CARDSTATE_ABSENT;
    localObject = mPhone.getUiccCard();
    boolean bool = false;
    if (localObject != null)
    {
      IccCardStatus.CardState localCardState = ((UiccCard)localObject).getCardState();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("UiccCardState = ");
      ((StringBuilder)localObject).append(localCardState);
      loge(((StringBuilder)localObject).toString());
      if (localCardState == IccCardStatus.CardState.CARDSTATE_PRESENT) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  protected void checkThread()
  {
    if (!Looper.getMainLooper().equals(Looper.myLooper())) {
      return;
    }
    loge("query() called on the main UI thread!");
    throw new IllegalStateException("You cannot call query on this provder from the main UI thread.");
  }
  
  public void dispose() {}
  
  public int[] getAdnRecordsCapacity()
  {
    logd("getAdnRecordsCapacity");
    return new int[10];
  }
  
  public List<AdnRecord> getAdnRecordsInEf(int paramInt)
  {
    if (mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") == 0)
    {
      paramInt = updateEfForIccType(paramInt);
      ??? = new StringBuilder();
      ((StringBuilder)???).append("getAdnRecordsInEF: efid=0x");
      ((StringBuilder)???).append(Integer.toHexString(paramInt).toUpperCase());
      logd(((StringBuilder)???).toString());
      synchronized (mLock)
      {
        checkThread();
        AtomicBoolean localAtomicBoolean = new java/util/concurrent/atomic/AtomicBoolean;
        localAtomicBoolean.<init>(false);
        Message localMessage = mBaseHandler.obtainMessage(2, localAtomicBoolean);
        if (mAdnCache != null)
        {
          mAdnCache.requestLoadAllAdnLike(paramInt, mAdnCache.extensionEfForEf(paramInt), localMessage);
          waitForResult(localAtomicBoolean);
        }
        else
        {
          loge("Failure while trying to load from SIM due to uninitialised adncache");
        }
        return mRecords;
      }
    }
    throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
  }
  
  public int[] getAdnRecordsSize(int paramInt)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("getAdnRecordsSize: efid=");
    ((StringBuilder)???).append(paramInt);
    logd(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      checkThread();
      mRecordSize = new int[3];
      AtomicBoolean localAtomicBoolean = new java/util/concurrent/atomic/AtomicBoolean;
      localAtomicBoolean.<init>(false);
      Message localMessage = mBaseHandler.obtainMessage(1, localAtomicBoolean);
      IccFileHandler localIccFileHandler = mPhone.getIccFileHandler();
      if (localIccFileHandler != null)
      {
        localIccFileHandler.getEFLinearRecordSize(paramInt, localMessage);
        waitForResult(localAtomicBoolean);
      }
      return mRecordSize;
    }
  }
  
  protected String[] getAnrStringArray(String paramString)
  {
    if (paramString != null) {
      return paramString.split(":");
    }
    return null;
  }
  
  public int getRowIdForNewAddedRecord()
  {
    int i = -1;
    if (mAdnCache != null) {
      i = mAdnCache.getRowIdForNewAddedRecord();
    } else {
      loge("Failure while trying to update by search due to uninitialised adncache");
    }
    return i;
  }
  
  public int getSIMADNReadyState()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[getSIMADNReadyState] for Phone: ");
    localStringBuilder.append(mPhone);
    localStringBuilder.append(", adnReady: ");
    localStringBuilder.append(0);
    logd(localStringBuilder.toString());
    return 0;
  }
  
  public int[] getSIMPhoneBookProperty(int paramInt)
  {
    paramInt = updateEfForIccType(paramInt);
    mRecordSize = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    synchronized (mLock)
    {
      checkThread();
      if (CheckSIMState()) {
        if (paramInt == 20272)
        {
          Message localMessage1 = mBaseHandler.obtainMessage(4);
          if (mAdnCache != null)
          {
            mAdnCache.getUSIMPhoneBookProperty(paramInt, localMessage1);
            try
            {
              mLock.wait();
            }
            catch (InterruptedException localInterruptedException1)
            {
              for (;;)
              {
                logd("interrupted while trying to load from the SIM");
              }
            }
          }
          else
          {
            loge("Failure while trying to load from SIM due to uninitialised adncache");
          }
          mRecordSize[0] = 1;
        }
        else
        {
          Message localMessage2 = mBaseHandler.obtainMessage(4);
          IccFileHandler localIccFileHandler = mPhone.getIccFileHandler();
          if (localIccFileHandler != null)
          {
            localIccFileHandler.getEFLinearRecordSize(paramInt, localMessage2);
            try
            {
              mLock.wait();
            }
            catch (InterruptedException localInterruptedException2)
            {
              loge("interrupted while trying to load from the SIM");
            }
          }
          mRecordSize[1] = mRecordSize[2];
          mRecordSize[2] = mRecordSize[0];
          mRecordSize[0] = 0;
        }
      }
      return mRecordSize;
    }
  }
  
  protected String[] getStringArray(String paramString)
  {
    if (paramString != null) {
      return paramString.split(",");
    }
    return null;
  }
  
  protected void logd(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[IccPbInterfaceManager] ");
    localStringBuilder.append(paramString);
    Rlog.d("IccPhoneBookIM", localStringBuilder.toString());
  }
  
  protected void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[IccPbInterfaceManager] ");
    localStringBuilder.append(paramString);
    Rlog.e("IccPhoneBookIM", localStringBuilder.toString());
  }
  
  public boolean updateAdnRecordsInEfByIndex(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt2, String paramString5)
  {
    if (mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") == 0)
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("updateAdnRecordsInEfByIndex: efid=0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(paramInt1).toUpperCase());
      ((StringBuilder)localObject1).append(" Index=");
      ((StringBuilder)localObject1).append(paramInt2);
      ((StringBuilder)localObject1).append(" ==> (");
      ((StringBuilder)localObject1).append(Rlog.pii("IccPhoneBookIM", paramString1));
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(Rlog.pii("IccPhoneBookIM", paramString2));
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(Rlog.pii("IccPhoneBookIM", paramString3));
      ((StringBuilder)localObject1).append(") pin2=");
      ((StringBuilder)localObject1).append(Rlog.pii("IccPhoneBookIM", paramString5));
      logd(((StringBuilder)localObject1).toString());
      synchronized (mLock)
      {
        checkThread();
        mSuccess = false;
        AtomicBoolean localAtomicBoolean = new java/util/concurrent/atomic/AtomicBoolean;
        localAtomicBoolean.<init>(false);
        Message localMessage = mBaseHandler.obtainMessage(3, localAtomicBoolean);
        if ((paramString3 == null) && (paramString4 == null)) {}
        for (paramString1 = new AdnRecord(paramString1, paramString2);; paramString1 = (String)localObject1)
        {
          break;
          localObject1 = new com/android/internal/telephony/uicc/AdnRecord;
          ((AdnRecord)localObject1).<init>(paramString1, paramString2, new String[] { paramString3 }, new String[] { paramString4 });
        }
        if (mAdnCache != null)
        {
          mAdnCache.updateAdnByIndex(paramInt1, paramString1, paramInt2, paramString5, localMessage);
          waitForResult(localAtomicBoolean);
        }
        else
        {
          loge("Failure while trying to update by index due to uninitialised adncache");
        }
        return mSuccess;
      }
    }
    throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
  }
  
  public boolean updateAdnRecordsInEfBySearch(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    if (mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") == 0)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("updateAdnRecordsInEfBySearch: efid=0x");
      ((StringBuilder)???).append(Integer.toHexString(paramInt).toUpperCase());
      ((StringBuilder)???).append(" (");
      ((StringBuilder)???).append(Rlog.pii("IccPhoneBookIM", paramString1));
      ((StringBuilder)???).append(",");
      ((StringBuilder)???).append(Rlog.pii("IccPhoneBookIM", paramString2));
      ((StringBuilder)???).append(")==> (");
      ((StringBuilder)???).append(Rlog.pii("IccPhoneBookIM", paramString3));
      ((StringBuilder)???).append(",");
      ((StringBuilder)???).append(Rlog.pii("IccPhoneBookIM", paramString4));
      ((StringBuilder)???).append(") pin2=");
      ((StringBuilder)???).append(Rlog.pii("IccPhoneBookIM", paramString5));
      logd(((StringBuilder)???).toString());
      paramInt = updateEfForIccType(paramInt);
      synchronized (mLock)
      {
        checkThread();
        mSuccess = false;
        AtomicBoolean localAtomicBoolean = new java/util/concurrent/atomic/AtomicBoolean;
        localAtomicBoolean.<init>(false);
        Message localMessage = mBaseHandler.obtainMessage(3, localAtomicBoolean);
        AdnRecord localAdnRecord = new com/android/internal/telephony/uicc/AdnRecord;
        localAdnRecord.<init>(paramString1, paramString2);
        paramString1 = new com/android/internal/telephony/uicc/AdnRecord;
        paramString1.<init>(paramString3, paramString4);
        if (mAdnCache != null)
        {
          mAdnCache.updateAdnBySearch(paramInt, localAdnRecord, paramString1, paramString5, localMessage);
          waitForResult(localAtomicBoolean);
        }
        else
        {
          loge("Failure while trying to update by search due to uninitialised adncache");
        }
        return mSuccess;
      }
    }
    throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
  }
  
  public boolean updateAdnRecordsWithContentValuesInEfBySearch(int paramInt, ContentValues paramContentValues, String paramString)
  {
    if (mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") == 0)
    {
      String str1 = paramContentValues.getAsString("tag");
      String str2 = paramContentValues.getAsString("newTag");
      String str3 = paramContentValues.getAsString("number");
      String str4 = paramContentValues.getAsString("newNumber");
      Object localObject1 = paramContentValues.getAsString("emails");
      Object localObject2 = paramContentValues.getAsString("newEmails");
      Object localObject3 = paramContentValues.getAsString("additionalnumber");
      ??? = paramContentValues.getAsString("newAnrs");
      boolean bool = TextUtils.isEmpty((CharSequence)localObject1);
      String[] arrayOfString = null;
      if (bool) {
        localObject1 = null;
      } else {
        localObject1 = getStringArray((String)localObject1);
      }
      if (TextUtils.isEmpty((CharSequence)localObject2)) {
        localObject2 = null;
      } else {
        localObject2 = getStringArray((String)localObject2);
      }
      if (TextUtils.isEmpty((CharSequence)localObject3)) {
        localObject3 = null;
      } else {
        localObject3 = getAnrStringArray((String)localObject3);
      }
      if (!TextUtils.isEmpty((CharSequence)???)) {
        arrayOfString = getAnrStringArray((String)???);
      }
      paramInt = updateEfForIccType(paramInt);
      ??? = new StringBuilder();
      ((StringBuilder)???).append("updateAdnRecordsWithContentValuesInEfBySearch: efid=");
      ((StringBuilder)???).append(paramInt);
      ((StringBuilder)???).append(", values = ");
      ((StringBuilder)???).append(paramContentValues);
      ((StringBuilder)???).append(", pin2=");
      ((StringBuilder)???).append(paramString);
      logd(((StringBuilder)???).toString());
      Object localObject5;
      Message localMessage;
      synchronized (mLock)
      {
        checkThread();
        mSuccess = false;
        paramContentValues = new java/util/concurrent/atomic/AtomicBoolean;
      }
    }
    throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
  }
  
  protected int updateEfForIccType(int paramInt)
  {
    if ((paramInt == 28474) && (mPhone.getCurrentUiccAppType() == IccCardApplicationStatus.AppType.APPTYPE_USIM)) {
      return 20272;
    }
    return paramInt;
  }
  
  public void updateIccRecords(IccRecords paramIccRecords)
  {
    if (paramIccRecords != null) {
      mAdnCache = paramIccRecords.getAdnCache();
    } else {
      mAdnCache = null;
    }
  }
  
  protected void waitForResult(AtomicBoolean paramAtomicBoolean)
  {
    while (!paramAtomicBoolean.get()) {
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        logd("interrupted while trying to update by search");
      }
    }
  }
  
  protected class IccPbHandler
    extends Handler
  {
    public IccPbHandler(Looper paramLooper)
    {
      super();
    }
    
    private void notifyPending(AsyncResult paramAsyncResult)
    {
      if (userObj != null) {
        ((AtomicBoolean)userObj).set(true);
      }
      mLock.notifyAll();
    }
    
    public void handleMessage(Message arg1)
    {
      int i = what;
      boolean bool = false;
      Object localObject5;
      switch (i)
      {
      default: 
        break;
      case 4: 
        AsyncResult localAsyncResult1 = (AsyncResult)obj;
        synchronized (mLock)
        {
          if ((exception == null) && (result != null)) {
            mRecordSize = ((int[])result);
          }
          mLock.notifyAll();
        }
      case 3: 
        ??? = (AsyncResult)obj;
        if (exception != null)
        {
          localObject5 = IccPhoneBookInterfaceManager.this;
          ??? = new StringBuilder();
          ((StringBuilder)???).append("exception of EVENT_UPDATE_DONE is");
          ((StringBuilder)???).append(exception);
          ((IccPhoneBookInterfaceManager)localObject5).logd(((StringBuilder)???).toString());
        }
        synchronized (mLock)
        {
          localObject5 = IccPhoneBookInterfaceManager.this;
          if (exception == null) {
            bool = true;
          }
          mSuccess = bool;
          notifyPending(???);
        }
      case 2: 
        ??? = (AsyncResult)obj;
        synchronized (mLock)
        {
          if (exception == null)
          {
            logd("Load ADN records done");
            mRecords = ((List)result);
          }
          else
          {
            logd("Cannot load ADN records");
            mRecords = null;
          }
          notifyPending((AsyncResult)???);
        }
      case 1: 
        AsyncResult localAsyncResult2 = (AsyncResult)obj;
        synchronized (mLock)
        {
          if (exception == null)
          {
            mRecordSize = ((int[])result);
            IccPhoneBookInterfaceManager localIccPhoneBookInterfaceManager = IccPhoneBookInterfaceManager.this;
            localObject5 = new java/lang/StringBuilder;
            ((StringBuilder)localObject5).<init>();
            ((StringBuilder)localObject5).append("GET_RECORD_SIZE Size ");
            ((StringBuilder)localObject5).append(mRecordSize[0]);
            ((StringBuilder)localObject5).append(" total ");
            ((StringBuilder)localObject5).append(mRecordSize[1]);
            ((StringBuilder)localObject5).append(" #record ");
            ((StringBuilder)localObject5).append(mRecordSize[2]);
            localIccPhoneBookInterfaceManager.logd(((StringBuilder)localObject5).toString());
          }
          notifyPending(localAsyncResult2);
        }
      }
    }
  }
}
