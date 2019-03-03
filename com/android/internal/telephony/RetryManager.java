package com.android.internal.telephony;

import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.telephony.dataconnection.ApnSetting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RetryManager
{
  public static final boolean DBG = true;
  private static final long DEFAULT_APN_RETRY_AFTER_DISCONNECT_DELAY = 10000L;
  private static final String DEFAULT_DATA_RETRY_CONFIG = "max_retries=3, 5000, 5000, 5000";
  private static final long DEFAULT_INTER_APN_DELAY = 20000L;
  private static final long DEFAULT_INTER_APN_DELAY_FOR_PROVISIONING = 3000L;
  public static final String LOG_TAG = "RetryManager";
  private static final int MAX_SAME_APN_RETRY = 3;
  public static final long NO_RETRY = -1L;
  public static final long NO_SUGGESTED_RETRY_DELAY = -2L;
  private static final String OTHERS_APN_TYPE = "others";
  public static final boolean VDBG = false;
  private long mApnRetryAfterDisconnectDelay;
  private String mApnType;
  private String mConfig;
  private int mCurrentApnIndex = -1;
  private long mFailFastInterApnDelay;
  private long mInterApnDelay;
  private int mMaxRetryCount;
  private long mModemSuggestedDelay = -2L;
  private Phone mPhone;
  private ArrayList<RetryRec> mRetryArray = new ArrayList();
  private int mRetryCount = 0;
  private boolean mRetryForever = false;
  private Random mRng = new Random();
  private int mSameApnRetryCount = 0;
  private ArrayList<ApnSetting> mWaitingApns = null;
  
  public RetryManager(Phone paramPhone, String paramString)
  {
    mPhone = paramPhone;
    mApnType = paramString;
  }
  
  private boolean configure(String paramString)
  {
    Object localObject = paramString;
    if (paramString.startsWith("\""))
    {
      localObject = paramString;
      if (paramString.endsWith("\"")) {
        localObject = paramString.substring(1, paramString.length() - 1);
      }
    }
    reset();
    paramString = new StringBuilder();
    paramString.append("configure: '");
    paramString.append((String)localObject);
    paramString.append("'");
    log(paramString.toString());
    mConfig = ((String)localObject);
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      paramString = ((String)localObject).split(",");
      int i = 0;
      for (int j = 0; j < paramString.length; j++)
      {
        localObject = paramString[j].split("=", 2);
        localObject[0] = localObject[0].trim();
        if (localObject.length > 1)
        {
          localObject[1] = localObject[1].trim();
          if (TextUtils.equals(localObject[0], "default_randomization"))
          {
            localObject = parseNonNegativeInt(localObject[0], localObject[1]);
            if (!((Boolean)first).booleanValue()) {
              return false;
            }
            i = ((Integer)second).intValue();
          }
          else if (TextUtils.equals(localObject[0], "max_retries"))
          {
            if (TextUtils.equals("infinite", localObject[1]))
            {
              mRetryForever = true;
            }
            else
            {
              localObject = parseNonNegativeInt(localObject[0], localObject[1]);
              if (!((Boolean)first).booleanValue()) {
                return false;
              }
              mMaxRetryCount = ((Integer)second).intValue();
            }
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Unrecognized configuration name value pair: ");
            ((StringBuilder)localObject).append(paramString[j]);
            Rlog.e("RetryManager", ((StringBuilder)localObject).toString());
            return false;
          }
        }
        else
        {
          String[] arrayOfString = paramString[j].split(":", 2);
          arrayOfString[0] = arrayOfString[0].trim();
          localObject = new RetryRec(0, 0);
          Pair localPair = parseNonNegativeInt("delayTime", arrayOfString[0]);
          if (!((Boolean)first).booleanValue()) {
            return false;
          }
          mDelayTime = ((Integer)second).intValue();
          if (arrayOfString.length > 1)
          {
            arrayOfString[1] = arrayOfString[1].trim();
            localPair = parseNonNegativeInt("randomizationTime", arrayOfString[1]);
            if (!((Boolean)first).booleanValue()) {
              return false;
            }
            mRandomizationTime = ((Integer)second).intValue();
          }
          else
          {
            mRandomizationTime = i;
          }
          mRetryArray.add(localObject);
        }
      }
      if (mRetryArray.size() > mMaxRetryCount) {
        mMaxRetryCount = mRetryArray.size();
      }
    }
    else
    {
      log("configure: cleared");
    }
    return true;
  }
  
  private void configureRetry()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    label235:
    label269:
    String str;
    try
    {
      if (Build.IS_DEBUGGABLE)
      {
        localObject5 = SystemProperties.get("test.data_retry_config");
        if (!TextUtils.isEmpty((CharSequence)localObject5))
        {
          configure((String)localObject5);
          return;
        }
      }
      Object localObject5 = ((CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(mPhone.getSubId());
      mInterApnDelay = ((PersistableBundle)localObject5).getLong("carrier_data_call_apn_delay_default_long", 20000L);
      mFailFastInterApnDelay = ((PersistableBundle)localObject5).getLong("carrier_data_call_apn_delay_faster_long", 3000L);
      mApnRetryAfterDisconnectDelay = ((PersistableBundle)localObject5).getLong("carrier_data_call_apn_retry_after_disconnect_long", 10000L);
      String[] arrayOfString1 = ((PersistableBundle)localObject5).getStringArray("carrier_data_call_retry_config_strings");
      localObject5 = localObject3;
      if (arrayOfString1 != null)
      {
        int i = arrayOfString1.length;
        int j = 0;
        for (;;)
        {
          localObject1 = localObject2;
          if (j < i)
          {
            localObject1 = arrayOfString1[j];
            localObject5 = localObject3;
            try
            {
              if (!TextUtils.isEmpty((CharSequence)localObject1))
              {
                String[] arrayOfString2 = ((String)localObject1).split(":", 2);
                localObject5 = localObject3;
                if (arrayOfString2.length == 2)
                {
                  localObject1 = arrayOfString2[0].trim();
                  if (((String)localObject1).equals(mApnType))
                  {
                    localObject1 = arrayOfString2[1];
                    break label235;
                  }
                  localObject5 = localObject3;
                  if (((String)localObject1).equals("others")) {
                    localObject5 = arrayOfString2[1];
                  }
                }
              }
              j++;
              localObject3 = localObject5;
            }
            catch (NullPointerException localNullPointerException1)
            {
              break label269;
            }
          }
        }
        localObject5 = localNullPointerException1;
      }
      Object localObject4 = localObject1;
      if (localObject1 == null) {
        if (localObject5 != null)
        {
          localObject4 = localObject5;
        }
        else
        {
          log("Invalid APN retry configuration!. Use the default one now.");
          localObject4 = "max_retries=3, 5000, 5000, 5000";
        }
      }
    }
    catch (NullPointerException localNullPointerException2)
    {
      log("Failed to read configuration! Use the hardcoded default value.");
      mInterApnDelay = 20000L;
      mFailFastInterApnDelay = 3000L;
      str = "max_retries=3, 5000, 5000, 5000";
    }
    configure(str);
  }
  
  private int getRetryTimer()
  {
    int i;
    if (mRetryCount < mRetryArray.size()) {
      i = mRetryCount;
    } else {
      i = mRetryArray.size() - 1;
    }
    if ((i >= 0) && (i < mRetryArray.size())) {
      i = mRetryArray.get(i)).mDelayTime + nextRandomizationTime(i);
    } else {
      i = 0;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getRetryTimer: ");
    localStringBuilder.append(i);
    log(localStringBuilder.toString());
    return i;
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mApnType);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.d("RetryManager", localStringBuilder.toString());
  }
  
  private int nextRandomizationTime(int paramInt)
  {
    paramInt = mRetryArray.get(paramInt)).mRandomizationTime;
    if (paramInt == 0) {
      return 0;
    }
    return mRng.nextInt(paramInt);
  }
  
  private Pair<Boolean, Integer> parseNonNegativeInt(String paramString1, String paramString2)
  {
    try
    {
      int i = Integer.parseInt(paramString2);
      localObject = new android/util/Pair;
      ((Pair)localObject).<init>(Boolean.valueOf(validateNonNegativeInt(paramString1, i)), Integer.valueOf(i));
      paramString1 = (String)localObject;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString1);
      ((StringBuilder)localObject).append(" bad value: ");
      ((StringBuilder)localObject).append(paramString2);
      Rlog.e("RetryManager", ((StringBuilder)localObject).toString(), localNumberFormatException);
      paramString1 = new Pair(Boolean.valueOf(false), Integer.valueOf(0));
    }
    return paramString1;
  }
  
  private void reset()
  {
    mMaxRetryCount = 0;
    mRetryCount = 0;
    mCurrentApnIndex = -1;
    mSameApnRetryCount = 0;
    mModemSuggestedDelay = -2L;
    mRetryArray.clear();
  }
  
  private boolean validateNonNegativeInt(String paramString, int paramInt)
  {
    boolean bool;
    if (paramInt < 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" bad value: is < 0");
      Rlog.e("RetryManager", localStringBuilder.toString());
      bool = false;
    }
    else
    {
      bool = true;
    }
    return bool;
  }
  
  public long getDelayForNextApn(boolean paramBoolean)
  {
    if ((mWaitingApns != null) && (mWaitingApns.size() != 0))
    {
      if (mModemSuggestedDelay == -1L)
      {
        log("Modem suggested not retrying.");
        return -1L;
      }
      StringBuilder localStringBuilder;
      if ((mModemSuggestedDelay != -2L) && (mSameApnRetryCount < 3))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Modem suggested retry in ");
        localStringBuilder.append(mModemSuggestedDelay);
        localStringBuilder.append(" ms.");
        log(localStringBuilder.toString());
        return mModemSuggestedDelay;
      }
      int i = mCurrentApnIndex;
      int j;
      do
      {
        i++;
        j = i;
        if (i >= mWaitingApns.size()) {
          j = 0;
        }
        if (!mWaitingApns.get(j)).permanentFailed)
        {
          long l1;
          if (j <= mCurrentApnIndex)
          {
            if ((!mRetryForever) && (mRetryCount + 1 > mMaxRetryCount))
            {
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("Reached maximum retry count ");
              localStringBuilder.append(mMaxRetryCount);
              localStringBuilder.append(".");
              log(localStringBuilder.toString());
              return -1L;
            }
            l1 = getRetryTimer();
            mRetryCount += 1;
          }
          else
          {
            l1 = mInterApnDelay;
          }
          long l2 = l1;
          if (paramBoolean)
          {
            l2 = l1;
            if (l1 > mFailFastInterApnDelay) {
              l2 = mFailFastInterApnDelay;
            }
          }
          return l2;
        }
        i = j;
      } while (j != mCurrentApnIndex);
      log("All APNs have permanently failed.");
      return -1L;
    }
    log("Waiting APN list is null or empty.");
    return -1L;
  }
  
  public ApnSetting getNextApnSetting()
  {
    if ((mWaitingApns != null) && (mWaitingApns.size() != 0))
    {
      if ((mModemSuggestedDelay != -2L) && (mSameApnRetryCount < 3))
      {
        mSameApnRetryCount += 1;
        return (ApnSetting)mWaitingApns.get(mCurrentApnIndex);
      }
      mSameApnRetryCount = 0;
      int i = mCurrentApnIndex;
      int j;
      do
      {
        i++;
        j = i;
        if (i == mWaitingApns.size()) {
          j = 0;
        }
        if (!mWaitingApns.get(j)).permanentFailed)
        {
          mCurrentApnIndex = j;
          return (ApnSetting)mWaitingApns.get(mCurrentApnIndex);
        }
        i = j;
      } while (j != mCurrentApnIndex);
      return null;
    }
    log("Waiting APN list is null or empty.");
    return null;
  }
  
  public long getRetryAfterDisconnectDelay()
  {
    return mApnRetryAfterDisconnectDelay;
  }
  
  public ArrayList<ApnSetting> getWaitingApns()
  {
    return mWaitingApns;
  }
  
  public void markApnPermanentFailed(ApnSetting paramApnSetting)
  {
    if (paramApnSetting != null) {
      permanentFailed = true;
    }
  }
  
  public void setModemSuggestedDelay(long paramLong)
  {
    mModemSuggestedDelay = paramLong;
  }
  
  public void setWaitingApns(ArrayList<ApnSetting> paramArrayList)
  {
    if (paramArrayList == null)
    {
      log("No waiting APNs provided");
      return;
    }
    mWaitingApns = paramArrayList;
    configureRetry();
    paramArrayList = mWaitingApns.iterator();
    while (paramArrayList.hasNext()) {
      nextpermanentFailed = false;
    }
    paramArrayList = new StringBuilder();
    paramArrayList.append("Setting ");
    paramArrayList.append(mWaitingApns.size());
    paramArrayList.append(" waiting APNs.");
    log(paramArrayList.toString());
  }
  
  public String toString()
  {
    if (mConfig == null) {
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RetryManager: mApnType=");
    localStringBuilder.append(mApnType);
    localStringBuilder.append(" mRetryCount=");
    localStringBuilder.append(mRetryCount);
    localStringBuilder.append(" mMaxRetryCount=");
    localStringBuilder.append(mMaxRetryCount);
    localStringBuilder.append(" mCurrentApnIndex=");
    localStringBuilder.append(mCurrentApnIndex);
    localStringBuilder.append(" mSameApnRtryCount=");
    localStringBuilder.append(mSameApnRetryCount);
    localStringBuilder.append(" mModemSuggestedDelay=");
    localStringBuilder.append(mModemSuggestedDelay);
    localStringBuilder.append(" mRetryForever=");
    localStringBuilder.append(mRetryForever);
    localStringBuilder.append(" mInterApnDelay=");
    localStringBuilder.append(mInterApnDelay);
    localStringBuilder.append(" mApnRetryAfterDisconnectDelay=");
    localStringBuilder.append(mApnRetryAfterDisconnectDelay);
    localStringBuilder.append(" mConfig={");
    localStringBuilder.append(mConfig);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  private static class RetryRec
  {
    int mDelayTime;
    int mRandomizationTime;
    
    RetryRec(int paramInt1, int paramInt2)
    {
      mDelayTime = paramInt1;
      mRandomizationTime = paramInt2;
    }
  }
}
