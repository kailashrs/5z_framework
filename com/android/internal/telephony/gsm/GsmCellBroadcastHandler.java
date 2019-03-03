package com.android.internal.telephony.gsm;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Message;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import com.android.internal.telephony.CellBroadcastHandler;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class GsmCellBroadcastHandler
  extends CellBroadcastHandler
{
  private static final boolean VDBG = false;
  private final HashMap<SmsCbConcatInfo, byte[][]> mSmsCbPageMap = new HashMap(4);
  
  protected GsmCellBroadcastHandler(Context paramContext, Phone paramPhone)
  {
    super("GsmCellBroadcastHandler", paramContext, paramPhone);
    mCi.setOnNewGsmBroadcastSms(getHandler(), 1, null);
  }
  
  private SmsCbMessage handleGsmBroadcastSms(AsyncResult paramAsyncResult)
  {
    try
    {
      paramAsyncResult = (byte[])result;
      SmsCbHeader localSmsCbHeader = new com/android/internal/telephony/gsm/SmsCbHeader;
      localSmsCbHeader.<init>(paramAsyncResult);
      byte[] arrayOfByte = Arrays.copyOf(paramAsyncResult, paramAsyncResult.length - 1);
      String str = TelephonyManager.from(mContext).getNetworkOperatorForPhone(mPhone.getPhoneId());
      int i = -1;
      int j = -1;
      paramAsyncResult = mPhone.getCellLocation();
      if ((paramAsyncResult instanceof GsmCellLocation))
      {
        paramAsyncResult = (GsmCellLocation)paramAsyncResult;
        i = paramAsyncResult.getLac();
        j = paramAsyncResult.getCid();
      }
      int k = localSmsCbHeader.getGeographicalScope();
      if (k != 0) {
        switch (k)
        {
        default: 
          paramAsyncResult = new android/telephony/SmsCbLocation;
          paramAsyncResult.<init>(str);
          break;
        case 2: 
          paramAsyncResult = new SmsCbLocation(str, i, -1);
          break;
        }
      } else {
        paramAsyncResult = new SmsCbLocation(str, i, j);
      }
      int m = localSmsCbHeader.getNumberOfPages();
      k = 0;
      Object localObject2;
      if (m > 1)
      {
        SmsCbConcatInfo localSmsCbConcatInfo = new com/android/internal/telephony/gsm/GsmCellBroadcastHandler$SmsCbConcatInfo;
        localSmsCbConcatInfo.<init>(localSmsCbHeader, paramAsyncResult);
        localObject1 = (byte[][])mSmsCbPageMap.get(localSmsCbConcatInfo);
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new byte[m][];
          mSmsCbPageMap.put(localSmsCbConcatInfo, localObject2);
        }
        localObject2[(localSmsCbHeader.getPageIndex() - 1)] = arrayOfByte;
        m = localObject2.length;
        while (k < m)
        {
          if (localObject2[k] == null)
          {
            log("still missing pdu");
            return null;
          }
          k++;
        }
        mSmsCbPageMap.remove(localSmsCbConcatInfo);
      }
      else
      {
        localObject2 = new byte[1][];
        localObject2[0] = arrayOfByte;
      }
      Object localObject1 = mSmsCbPageMap.keySet().iterator();
      while (((Iterator)localObject1).hasNext()) {
        if (!((SmsCbConcatInfo)((Iterator)localObject1).next()).matchesLocation(str, i, j)) {
          ((Iterator)localObject1).remove();
        }
      }
      paramAsyncResult = GsmSmsCbMessage.createSmsCbMessage(mContext, localSmsCbHeader, paramAsyncResult, (byte[][])localObject2);
      return paramAsyncResult;
    }
    catch (RuntimeException paramAsyncResult)
    {
      loge("Error in decoding SMS CB pdu", paramAsyncResult);
    }
    return null;
  }
  
  public static GsmCellBroadcastHandler makeGsmCellBroadcastHandler(Context paramContext, Phone paramPhone)
  {
    paramContext = new GsmCellBroadcastHandler(paramContext, paramPhone);
    paramContext.start();
    return paramContext;
  }
  
  protected boolean handleSmsMessage(Message paramMessage)
  {
    if ((obj instanceof AsyncResult))
    {
      SmsCbMessage localSmsCbMessage = handleGsmBroadcastSms((AsyncResult)obj);
      if (localSmsCbMessage != null)
      {
        handleBroadcastSms(localSmsCbMessage);
        return true;
      }
    }
    return super.handleSmsMessage(paramMessage);
  }
  
  protected void onQuitting()
  {
    mPhone.mCi.unSetOnNewGsmBroadcastSms(getHandler());
    super.onQuitting();
  }
  
  private static final class SmsCbConcatInfo
  {
    private final SmsCbHeader mHeader;
    private final SmsCbLocation mLocation;
    
    SmsCbConcatInfo(SmsCbHeader paramSmsCbHeader, SmsCbLocation paramSmsCbLocation)
    {
      mHeader = paramSmsCbHeader;
      mLocation = paramSmsCbLocation;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof SmsCbConcatInfo;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (SmsCbConcatInfo)paramObject;
        bool1 = bool2;
        if (mHeader.getSerialNumber() == mHeader.getSerialNumber())
        {
          bool1 = bool2;
          if (mLocation.equals(mLocation)) {
            bool1 = true;
          }
        }
        return bool1;
      }
      return false;
    }
    
    public int hashCode()
    {
      return mHeader.getSerialNumber() * 31 + mLocation.hashCode();
    }
    
    public boolean matchesLocation(String paramString, int paramInt1, int paramInt2)
    {
      return mLocation.isInLocationArea(paramString, paramInt1, paramInt2);
    }
  }
}
