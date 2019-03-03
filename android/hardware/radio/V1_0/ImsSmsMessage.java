package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class ImsSmsMessage
{
  public final ArrayList<CdmaSmsMessage> cdmaMessage = new ArrayList();
  public final ArrayList<GsmSmsMessage> gsmMessage = new ArrayList();
  public int messageRef;
  public boolean retry;
  public int tech;
  
  public ImsSmsMessage() {}
  
  public static final ArrayList<ImsSmsMessage> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 48, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new ImsSmsMessage();
      ((ImsSmsMessage)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 48);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<ImsSmsMessage> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 48);
    while (j < i)
    {
      ((ImsSmsMessage)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 48);
      j++;
    }
    localHwBlob1.putBlob(0L, localHwBlob2);
    paramHwParcel.writeBuffer(localHwBlob1);
  }
  
  public final boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (paramObject.getClass() != ImsSmsMessage.class) {
      return false;
    }
    paramObject = (ImsSmsMessage)paramObject;
    if (tech != tech) {
      return false;
    }
    if (retry != retry) {
      return false;
    }
    if (messageRef != messageRef) {
      return false;
    }
    if (!HidlSupport.deepEquals(cdmaMessage, cdmaMessage)) {
      return false;
    }
    return HidlSupport.deepEquals(gsmMessage, gsmMessage);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(tech))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(retry))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(messageRef))), Integer.valueOf(HidlSupport.deepHashCode(cdmaMessage)), Integer.valueOf(HidlSupport.deepHashCode(gsmMessage)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    tech = paramHwBlob.getInt32(paramLong + 0L);
    retry = paramHwBlob.getBool(paramLong + 4L);
    messageRef = paramHwBlob.getInt32(paramLong + 8L);
    int i = paramHwBlob.getInt32(paramLong + 16L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, paramHwBlob.handle(), paramLong + 16L + 0L, true);
    cdmaMessage.clear();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      localObject = new CdmaSmsMessage();
      ((CdmaSmsMessage)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, k * 88);
      cdmaMessage.add(localObject);
    }
    i = paramHwBlob.getInt32(paramLong + 32L + 8L);
    Object localObject = paramHwParcel.readEmbeddedBuffer(i * 32, paramHwBlob.handle(), 0L + (paramLong + 32L), true);
    gsmMessage.clear();
    for (k = j; k < i; k++)
    {
      paramHwBlob = new GsmSmsMessage();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject, k * 32);
      gsmMessage.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(48L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".tech = ");
    localStringBuilder.append(RadioTechnologyFamily.toString(tech));
    localStringBuilder.append(", .retry = ");
    localStringBuilder.append(retry);
    localStringBuilder.append(", .messageRef = ");
    localStringBuilder.append(messageRef);
    localStringBuilder.append(", .cdmaMessage = ");
    localStringBuilder.append(cdmaMessage);
    localStringBuilder.append(", .gsmMessage = ");
    localStringBuilder.append(gsmMessage);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, tech);
    paramHwBlob.putBool(paramLong + 4L, retry);
    paramHwBlob.putInt32(paramLong + 8L, messageRef);
    int i = cdmaMessage.size();
    paramHwBlob.putInt32(paramLong + 16L + 8L, i);
    paramHwBlob.putBool(paramLong + 16L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 88);
    for (int j = 0; j < i; j++) {
      ((CdmaSmsMessage)cdmaMessage.get(j)).writeEmbeddedToBlob(localHwBlob, j * 88);
    }
    paramHwBlob.putBlob(paramLong + 16L + 0L, localHwBlob);
    i = gsmMessage.size();
    paramHwBlob.putInt32(paramLong + 32L + 8L, i);
    j = 0;
    paramHwBlob.putBool(paramLong + 32L + 12L, false);
    localHwBlob = new HwBlob(i * 32);
    while (j < i)
    {
      ((GsmSmsMessage)gsmMessage.get(j)).writeEmbeddedToBlob(localHwBlob, j * 32);
      j++;
    }
    paramHwBlob.putBlob(paramLong + 32L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(48);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
