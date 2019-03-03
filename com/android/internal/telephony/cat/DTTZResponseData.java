package com.android.internal.telephony.cat;

import android.os.SystemProperties;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.TimeZone;

class DTTZResponseData
  extends ResponseData
{
  private Calendar mCalendar;
  
  public DTTZResponseData(Calendar paramCalendar)
  {
    mCalendar = paramCalendar;
  }
  
  private byte byteToBCD(int paramInt)
  {
    if ((paramInt < 0) && (paramInt > 99))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Err: byteToBCD conversion Value is ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" Value has to be between 0 and 99");
      CatLog.d(this, localStringBuilder.toString());
      return 0;
    }
    return (byte)(paramInt / 10 | paramInt % 10 << 4);
  }
  
  private byte getTZOffSetByte(long paramLong)
  {
    byte b1 = 1;
    byte b2;
    if (paramLong < 0L) {
      b2 = 1;
    } else {
      b2 = 0;
    }
    paramLong /= 900000L;
    if (b2 != 0) {
      b1 = -1;
    }
    b1 = byteToBCD((int)(b1 * paramLong));
    byte b3;
    if (b2 != 0)
    {
      b2 = (byte)(b1 | 0x8);
      b3 = b2;
    }
    else
    {
      b3 = b1;
    }
    return b3;
  }
  
  public void format(ByteArrayOutputStream paramByteArrayOutputStream)
  {
    if (paramByteArrayOutputStream == null) {
      return;
    }
    paramByteArrayOutputStream.write(0x80 | AppInterface.CommandType.PROVIDE_LOCAL_INFORMATION.value());
    byte[] arrayOfByte = new byte[8];
    int i = 0;
    arrayOfByte[0] = ((byte)7);
    if (mCalendar == null) {
      mCalendar = Calendar.getInstance();
    }
    arrayOfByte[1] = byteToBCD(mCalendar.get(1) % 100);
    arrayOfByte[2] = byteToBCD(mCalendar.get(2) + 1);
    arrayOfByte[3] = byteToBCD(mCalendar.get(5));
    arrayOfByte[4] = byteToBCD(mCalendar.get(11));
    arrayOfByte[5] = byteToBCD(mCalendar.get(12));
    arrayOfByte[6] = byteToBCD(mCalendar.get(13));
    Object localObject = SystemProperties.get("persist.sys.timezone", "");
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      arrayOfByte[7] = ((byte)-1);
    }
    else
    {
      localObject = TimeZone.getTimeZone((String)localObject);
      arrayOfByte[7] = getTZOffSetByte(((TimeZone)localObject).getRawOffset() + ((TimeZone)localObject).getDSTSavings());
    }
    int j = arrayOfByte.length;
    while (i < j)
    {
      paramByteArrayOutputStream.write(arrayOfByte[i]);
      i++;
    }
  }
}
