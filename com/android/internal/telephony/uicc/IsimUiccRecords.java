package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.os.RegistrantList;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.gsm.SimTlv;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class IsimUiccRecords
  extends IccRecords
  implements IsimRecords
{
  private static final boolean DBG = true;
  private static final boolean DUMP_RECORDS = false;
  private static final int EVENT_APP_READY = 1;
  private static final int EVENT_ISIM_AUTHENTICATE_DONE = 91;
  public static final String INTENT_ISIM_REFRESH = "com.android.intent.isim_refresh";
  protected static final String LOG_TAG = "IsimUiccRecords";
  private static final int TAG_ISIM_VALUE = 128;
  private static final boolean VDBG = false;
  private String auth_rsp;
  private String mIsimDomain;
  private String mIsimImpi;
  private String[] mIsimImpu;
  private String mIsimIst;
  private String[] mIsimPcscf;
  private final Object mLock = new Object();
  
  public IsimUiccRecords(UiccCardApplication paramUiccCardApplication, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramContext, paramCommandsInterface);
    mRecordsRequested = false;
    mLockedRecordsReqReason = 0;
    mRecordsToLoad = 0;
    resetRecords();
    mParentApp.registerForReady(this, 1, null);
    paramUiccCardApplication = new StringBuilder();
    paramUiccCardApplication.append("IsimUiccRecords X ctor this=");
    paramUiccCardApplication.append(this);
    log(paramUiccCardApplication.toString());
  }
  
  private void broadcastRefresh()
  {
    Intent localIntent = new Intent("com.android.intent.isim_refresh");
    log("send ISim REFRESH: com.android.intent.isim_refresh");
    mContext.sendBroadcast(localIntent);
  }
  
  private static String isimTlvToString(byte[] paramArrayOfByte)
  {
    paramArrayOfByte = new SimTlv(paramArrayOfByte, 0, paramArrayOfByte.length);
    do
    {
      if (paramArrayOfByte.getTag() == 128) {
        return new String(paramArrayOfByte.getData(), Charset.forName("UTF-8"));
      }
    } while (paramArrayOfByte.nextObject());
    return null;
  }
  
  private void onLockedAllRecordsLoaded()
  {
    log("SIM locked; record load complete");
    if (mLockedRecordsReqReason == 1)
    {
      mLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    else if (mLockedRecordsReqReason == 2)
    {
      mNetworkLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onLockedAllRecordsLoaded: unexpected mLockedRecordsReqReason ");
      localStringBuilder.append(mLockedRecordsReqReason);
      loge(localStringBuilder.toString());
    }
  }
  
  public void dispose()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Disposing ");
    localStringBuilder.append(this);
    log(localStringBuilder.toString());
    mCi.unregisterForIccRefresh(this);
    mParentApp.unregisterForReady(this);
    resetRecords();
    super.dispose();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IsimRecords: ");
    localStringBuilder.append(this);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.println(" extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.flush();
  }
  
  protected void fetchIsimRecords()
  {
    mRecordsRequested = true;
    mFh.loadEFTransparent(28418, obtainMessage(100, new EfIsimImpiLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixedAll(28420, obtainMessage(100, new EfIsimImpuLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28419, obtainMessage(100, new EfIsimDomainLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28423, obtainMessage(100, new EfIsimIstLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixedAll(28425, obtainMessage(100, new EfIsimPcscfLoaded(null)));
    mRecordsToLoad += 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("fetchIsimRecords ");
    localStringBuilder.append(mRecordsToLoad);
    localStringBuilder.append(" requested: ");
    localStringBuilder.append(mRecordsRequested);
    log(localStringBuilder.toString());
  }
  
  public int getDisplayRule(ServiceState paramServiceState)
  {
    return 0;
  }
  
  public String getIsimDomain()
  {
    return mIsimDomain;
  }
  
  public String getIsimImpi()
  {
    return mIsimImpi;
  }
  
  public String[] getIsimImpu()
  {
    String[] arrayOfString;
    if (mIsimImpu != null) {
      arrayOfString = (String[])mIsimImpu.clone();
    } else {
      arrayOfString = null;
    }
    return arrayOfString;
  }
  
  public String getIsimIst()
  {
    return mIsimIst;
  }
  
  public String[] getIsimPcscf()
  {
    String[] arrayOfString;
    if (mIsimPcscf != null) {
      arrayOfString = (String[])mIsimPcscf.clone();
    } else {
      arrayOfString = null;
    }
    return arrayOfString;
  }
  
  public int getVoiceMessageCount()
  {
    return 0;
  }
  
  protected void handleFileUpdate(int paramInt)
  {
    if (paramInt != 28423)
    {
      if (paramInt != 28425)
      {
        switch (paramInt)
        {
        default: 
          break;
        case 28420: 
          mFh.loadEFLinearFixedAll(28420, obtainMessage(100, new EfIsimImpuLoaded(null)));
          mRecordsToLoad += 1;
          break;
        case 28419: 
          mFh.loadEFTransparent(28419, obtainMessage(100, new EfIsimDomainLoaded(null)));
          mRecordsToLoad += 1;
          break;
        case 28418: 
          mFh.loadEFTransparent(28418, obtainMessage(100, new EfIsimImpiLoaded(null)));
          mRecordsToLoad += 1;
          break;
        }
      }
      else
      {
        mFh.loadEFLinearFixedAll(28425, obtainMessage(100, new EfIsimPcscfLoaded(null)));
        mRecordsToLoad += 1;
      }
      fetchIsimRecords();
    }
    else
    {
      mFh.loadEFTransparent(28423, obtainMessage(100, new EfIsimIstLoaded(null)));
      mRecordsToLoad += 1;
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    if (mDestroyed.get())
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Received message ");
      ((StringBuilder)???).append(paramMessage);
      ((StringBuilder)???).append("[");
      ((StringBuilder)???).append(what);
      ((StringBuilder)???).append("] while being destroyed. Ignoring.");
      Rlog.e("IsimUiccRecords", ((StringBuilder)???).toString());
      return;
    }
    ??? = new StringBuilder();
    ((StringBuilder)???).append("IsimUiccRecords: handleMessage ");
    ((StringBuilder)???).append(paramMessage);
    ((StringBuilder)???).append("[");
    ((StringBuilder)???).append(what);
    ((StringBuilder)???).append("] ");
    loge(((StringBuilder)???).toString());
    try
    {
      int i = what;
      if (i != 1)
      {
        if (i != 31)
        {
          if (i != 91)
          {
            super.handleMessage(paramMessage);
          }
          else
          {
            paramMessage = (AsyncResult)obj;
            log("EVENT_ISIM_AUTHENTICATE_DONE");
            if (exception != null)
            {
              ??? = new java/lang/StringBuilder;
              ((StringBuilder)???).<init>();
              ((StringBuilder)???).append("Exception ISIM AKA: ");
              ((StringBuilder)???).append(exception);
              log(((StringBuilder)???).toString());
            }
            else
            {
              try
              {
                auth_rsp = ((String)result);
                paramMessage = new java/lang/StringBuilder;
                paramMessage.<init>();
                paramMessage.append("ISIM AKA: auth_rsp = ");
                paramMessage.append(auth_rsp);
                log(paramMessage.toString());
              }
              catch (Exception paramMessage)
              {
                ??? = new java/lang/StringBuilder;
                ((StringBuilder)???).<init>();
                ((StringBuilder)???).append("Failed to parse ISIM AKA contents: ");
                ((StringBuilder)???).append(paramMessage);
                log(((StringBuilder)???).toString());
              }
            }
            synchronized (mLock)
            {
              mLock.notifyAll();
            }
          }
        }
        else
        {
          broadcastRefresh();
          super.handleMessage(paramMessage);
        }
      }
      else {
        onReady();
      }
    }
    catch (RuntimeException paramMessage)
    {
      Rlog.w("IsimUiccRecords", "Exception parsing SIM record", paramMessage);
    }
  }
  
  protected void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ISIM] ");
    localStringBuilder.append(paramString);
    Rlog.d("IsimUiccRecords", localStringBuilder.toString());
  }
  
  protected void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ISIM] ");
    localStringBuilder.append(paramString);
    Rlog.e("IsimUiccRecords", localStringBuilder.toString());
  }
  
  protected void onAllRecordsLoaded()
  {
    log("record load complete");
    mLoaded.set(true);
    mRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
  }
  
  public void onReady()
  {
    fetchIsimRecords();
  }
  
  protected void onRecordLoaded()
  {
    mRecordsToLoad -= 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onRecordLoaded ");
    localStringBuilder.append(mRecordsToLoad);
    localStringBuilder.append(" requested: ");
    localStringBuilder.append(mRecordsRequested);
    log(localStringBuilder.toString());
    if (getRecordsLoaded()) {
      onAllRecordsLoaded();
    } else if ((!getLockedRecordsLoaded()) && (!getNetworkLockedRecordsLoaded()))
    {
      if (mRecordsToLoad < 0)
      {
        loge("recordsToLoad <0, programmer error suspected");
        mRecordsToLoad = 0;
      }
    }
    else {
      onLockedAllRecordsLoaded();
    }
  }
  
  public void onRefresh(boolean paramBoolean, int[] paramArrayOfInt)
  {
    if (paramBoolean) {
      fetchIsimRecords();
    }
  }
  
  protected void resetRecords()
  {
    mIsimImpi = null;
    mIsimDomain = null;
    mIsimImpu = null;
    mIsimIst = null;
    mIsimPcscf = null;
    auth_rsp = null;
    mRecordsRequested = false;
    mLockedRecordsReqReason = 0;
    mLoaded.set(false);
  }
  
  public void setVoiceMailNumber(String paramString1, String paramString2, Message paramMessage) {}
  
  public void setVoiceMessageWaiting(int paramInt1, int paramInt2) {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IsimUiccRecords: ");
    localStringBuilder.append(super.toString());
    localStringBuilder.append("");
    return localStringBuilder.toString();
  }
  
  private class EfIsimDomainLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfIsimDomainLoaded() {}
    
    public String getEfName()
    {
      return "EF_ISIM_DOMAIN";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      paramAsyncResult = (byte[])result;
      IsimUiccRecords.access$802(IsimUiccRecords.this, IsimUiccRecords.isimTlvToString(paramAsyncResult));
    }
  }
  
  private class EfIsimImpiLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfIsimImpiLoaded() {}
    
    public String getEfName()
    {
      return "EF_ISIM_IMPI";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      paramAsyncResult = (byte[])result;
      IsimUiccRecords.access$502(IsimUiccRecords.this, IsimUiccRecords.isimTlvToString(paramAsyncResult));
    }
  }
  
  private class EfIsimImpuLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfIsimImpuLoaded() {}
    
    public String getEfName()
    {
      return "EF_ISIM_IMPU";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      ArrayList localArrayList = (ArrayList)result;
      paramAsyncResult = IsimUiccRecords.this;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("EF_IMPU record count: ");
      ((StringBuilder)localObject).append(localArrayList.size());
      paramAsyncResult.log(((StringBuilder)localObject).toString());
      IsimUiccRecords.access$702(IsimUiccRecords.this, new String[localArrayList.size()]);
      int i = 0;
      localObject = localArrayList.iterator();
      while (((Iterator)localObject).hasNext())
      {
        paramAsyncResult = IsimUiccRecords.isimTlvToString((byte[])((Iterator)localObject).next());
        mIsimImpu[i] = paramAsyncResult;
        i++;
      }
    }
  }
  
  private class EfIsimIstLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfIsimIstLoaded() {}
    
    public String getEfName()
    {
      return "EF_ISIM_IST";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      paramAsyncResult = (byte[])result;
      IsimUiccRecords.access$902(IsimUiccRecords.this, IccUtils.bytesToHexString(paramAsyncResult));
    }
  }
  
  private class EfIsimPcscfLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfIsimPcscfLoaded() {}
    
    public String getEfName()
    {
      return "EF_ISIM_PCSCF";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      Object localObject = (ArrayList)result;
      paramAsyncResult = IsimUiccRecords.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("EF_PCSCF record count: ");
      localStringBuilder.append(((ArrayList)localObject).size());
      paramAsyncResult.log(localStringBuilder.toString());
      IsimUiccRecords.access$1002(IsimUiccRecords.this, new String[((ArrayList)localObject).size()]);
      int i = 0;
      paramAsyncResult = ((ArrayList)localObject).iterator();
      while (paramAsyncResult.hasNext())
      {
        localObject = IsimUiccRecords.isimTlvToString((byte[])paramAsyncResult.next());
        mIsimPcscf[i] = localObject;
        i++;
      }
    }
  }
}
