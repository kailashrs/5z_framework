package com.android.internal.telephony;

import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TelephonyDevController
  extends Handler
{
  private static final boolean DBG = true;
  private static final int EVENT_HARDWARE_CONFIG_CHANGED = 1;
  private static final String LOG_TAG = "TDC";
  private static final Object mLock = new Object();
  private static ArrayList<HardwareConfig> mModems = new ArrayList();
  private static ArrayList<HardwareConfig> mSims = new ArrayList();
  private static Message sRilHardwareConfig;
  private static TelephonyDevController sTelephonyDevController;
  
  private TelephonyDevController()
  {
    initFromResource();
    mModems.trimToSize();
    mSims.trimToSize();
  }
  
  public static TelephonyDevController create()
  {
    synchronized (mLock)
    {
      if (sTelephonyDevController == null)
      {
        localObject2 = new com/android/internal/telephony/TelephonyDevController;
        ((TelephonyDevController)localObject2).<init>();
        sTelephonyDevController = (TelephonyDevController)localObject2;
        localObject2 = sTelephonyDevController;
        return localObject2;
      }
      Object localObject2 = new java/lang/RuntimeException;
      ((RuntimeException)localObject2).<init>("TelephonyDevController already created!?!");
      throw ((Throwable)localObject2);
    }
  }
  
  public static TelephonyDevController getInstance()
  {
    synchronized (mLock)
    {
      if (sTelephonyDevController != null)
      {
        localObject2 = sTelephonyDevController;
        return localObject2;
      }
      Object localObject2 = new java/lang/RuntimeException;
      ((RuntimeException)localObject2).<init>("TelephonyDevController not yet created!?!");
      throw ((Throwable)localObject2);
    }
  }
  
  public static int getModemCount()
  {
    synchronized (mLock)
    {
      int i = mModems.size();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getModemCount: ");
      localStringBuilder.append(i);
      logd(localStringBuilder.toString());
      return i;
    }
  }
  
  private static void handleGetHardwareConfigChanged(AsyncResult paramAsyncResult)
  {
    if ((exception == null) && (result != null))
    {
      paramAsyncResult = (List)result;
      for (int i = 0; i < paramAsyncResult.size(); i++)
      {
        HardwareConfig localHardwareConfig = (HardwareConfig)paramAsyncResult.get(i);
        if (localHardwareConfig != null) {
          if (type == 0) {
            updateOrInsert(localHardwareConfig, mModems);
          } else if (type == 1) {
            updateOrInsert(localHardwareConfig, mSims);
          }
        }
      }
    }
    else
    {
      loge("handleGetHardwareConfigChanged - returned an error.");
    }
  }
  
  private void initFromResource()
  {
    String[] arrayOfString = Resources.getSystem().getStringArray(17236044);
    if (arrayOfString != null)
    {
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        HardwareConfig localHardwareConfig = new HardwareConfig(arrayOfString[j]);
        if (type == 0) {
          updateOrInsert(localHardwareConfig, mModems);
        } else if (type == 1) {
          updateOrInsert(localHardwareConfig, mSims);
        }
      }
    }
  }
  
  private static void logd(String paramString)
  {
    Rlog.d("TDC", paramString);
  }
  
  private static void loge(String paramString)
  {
    Rlog.e("TDC", paramString);
  }
  
  public static void registerRIL(CommandsInterface paramCommandsInterface)
  {
    paramCommandsInterface.getHardwareConfig(sRilHardwareConfig);
    if (sRilHardwareConfig != null)
    {
      AsyncResult localAsyncResult = (AsyncResult)sRilHardwareConfigobj;
      if (exception == null) {
        handleGetHardwareConfigChanged(localAsyncResult);
      }
    }
    paramCommandsInterface.registerForHardwareConfigChanged(sTelephonyDevController, 1, null);
  }
  
  public static void unregisterRIL(CommandsInterface paramCommandsInterface)
  {
    paramCommandsInterface.unregisterForHardwareConfigChanged(sTelephonyDevController);
  }
  
  private static void updateOrInsert(HardwareConfig paramHardwareConfig, ArrayList<HardwareConfig> paramArrayList)
  {
    synchronized (mLock)
    {
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++)
      {
        localObject2 = (HardwareConfig)paramArrayList.get(j);
        if (uuid.compareTo(uuid) == 0)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("updateOrInsert: removing: ");
          localStringBuilder.append(localObject2);
          logd(localStringBuilder.toString());
          paramArrayList.remove(j);
          break;
        }
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("updateOrInsert: inserting: ");
      ((StringBuilder)localObject2).append(paramHardwareConfig);
      logd(((StringBuilder)localObject2).toString());
      paramArrayList.add(paramHardwareConfig);
      return;
    }
  }
  
  public ArrayList<HardwareConfig> getAllModems()
  {
    synchronized (mLock)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      if (mModems.isEmpty())
      {
        logd("getAllModems: empty list.");
      }
      else
      {
        Iterator localIterator = mModems.iterator();
        while (localIterator.hasNext()) {
          localArrayList.add((HardwareConfig)localIterator.next());
        }
      }
      return localArrayList;
    }
  }
  
  public ArrayList<HardwareConfig> getAllSims()
  {
    synchronized (mLock)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      if (mSims.isEmpty())
      {
        logd("getAllSims: empty list.");
      }
      else
      {
        Iterator localIterator = mSims.iterator();
        while (localIterator.hasNext()) {
          localArrayList.add((HardwareConfig)localIterator.next());
        }
      }
      return localArrayList;
    }
  }
  
  public ArrayList<HardwareConfig> getAllSimsForModem(int paramInt)
  {
    synchronized (mLock)
    {
      if (mSims.isEmpty())
      {
        loge("getAllSimsForModem: no registered sim device?!?");
        return null;
      }
      if (paramInt > getModemCount())
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("getAllSimsForModem: out-of-bounds access for modem device ");
        ((StringBuilder)localObject2).append(paramInt);
        ((StringBuilder)localObject2).append(" max: ");
        ((StringBuilder)localObject2).append(getModemCount());
        loge(((StringBuilder)localObject2).toString());
        return null;
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("getAllSimsForModem ");
      ((StringBuilder)localObject2).append(paramInt);
      logd(((StringBuilder)localObject2).toString());
      localObject2 = new java/util/ArrayList;
      ((ArrayList)localObject2).<init>();
      HardwareConfig localHardwareConfig1 = getModem(paramInt);
      Iterator localIterator = mSims.iterator();
      while (localIterator.hasNext())
      {
        HardwareConfig localHardwareConfig2 = (HardwareConfig)localIterator.next();
        if (modemUuid.equals(uuid)) {
          ((ArrayList)localObject2).add(localHardwareConfig2);
        }
      }
      return localObject2;
    }
  }
  
  public HardwareConfig getModem(int paramInt)
  {
    synchronized (mLock)
    {
      if (mModems.isEmpty())
      {
        loge("getModem: no registered modem device?!?");
        return null;
      }
      if (paramInt > getModemCount())
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("getModem: out-of-bounds access for modem device ");
        ((StringBuilder)localObject2).append(paramInt);
        ((StringBuilder)localObject2).append(" max: ");
        ((StringBuilder)localObject2).append(getModemCount());
        loge(((StringBuilder)localObject2).toString());
        return null;
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("getModem: ");
      ((StringBuilder)localObject2).append(paramInt);
      logd(((StringBuilder)localObject2).toString());
      localObject2 = (HardwareConfig)mModems.get(paramInt);
      return localObject2;
    }
  }
  
  public HardwareConfig getModemForSim(int paramInt)
  {
    synchronized (mLock)
    {
      if ((!mModems.isEmpty()) && (!mSims.isEmpty()))
      {
        if (paramInt > getSimCount())
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("getModemForSim: out-of-bounds access for sim device ");
          ((StringBuilder)localObject2).append(paramInt);
          ((StringBuilder)localObject2).append(" max: ");
          ((StringBuilder)localObject2).append(getSimCount());
          loge(((StringBuilder)localObject2).toString());
          return null;
        }
        Object localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("getModemForSim ");
        ((StringBuilder)localObject2).append(paramInt);
        logd(((StringBuilder)localObject2).toString());
        HardwareConfig localHardwareConfig1 = getSim(paramInt);
        localObject2 = mModems.iterator();
        while (((Iterator)localObject2).hasNext())
        {
          HardwareConfig localHardwareConfig2 = (HardwareConfig)((Iterator)localObject2).next();
          if (uuid.equals(modemUuid)) {
            return localHardwareConfig2;
          }
        }
        return null;
      }
      loge("getModemForSim: no registered modem/sim device?!?");
      return null;
    }
  }
  
  public HardwareConfig getSim(int paramInt)
  {
    synchronized (mLock)
    {
      if (mSims.isEmpty())
      {
        loge("getSim: no registered sim device?!?");
        return null;
      }
      if (paramInt > getSimCount())
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("getSim: out-of-bounds access for sim device ");
        ((StringBuilder)localObject2).append(paramInt);
        ((StringBuilder)localObject2).append(" max: ");
        ((StringBuilder)localObject2).append(getSimCount());
        loge(((StringBuilder)localObject2).toString());
        return null;
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("getSim: ");
      ((StringBuilder)localObject2).append(paramInt);
      logd(((StringBuilder)localObject2).toString());
      localObject2 = (HardwareConfig)mSims.get(paramInt);
      return localObject2;
    }
  }
  
  public int getSimCount()
  {
    synchronized (mLock)
    {
      int i = mSims.size();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getSimCount: ");
      localStringBuilder.append(i);
      logd(localStringBuilder.toString());
      return i;
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    if (what != 1)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleMessage: Unknown Event ");
      localStringBuilder.append(what);
      loge(localStringBuilder.toString());
    }
    else
    {
      logd("handleMessage: received EVENT_HARDWARE_CONFIG_CHANGED");
      handleGetHardwareConfigChanged((AsyncResult)obj);
    }
  }
}
