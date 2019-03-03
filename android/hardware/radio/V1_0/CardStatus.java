package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CardStatus
{
  public final ArrayList<AppStatus> applications = new ArrayList();
  public int cardState;
  public int cdmaSubscriptionAppIndex;
  public int gsmUmtsSubscriptionAppIndex;
  public int imsSubscriptionAppIndex;
  public int universalPinState;
  
  public CardStatus() {}
  
  public static final ArrayList<CardStatus> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CardStatus localCardStatus = new CardStatus();
      localCardStatus.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localCardStatus);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CardStatus> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((CardStatus)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != CardStatus.class) {
      return false;
    }
    paramObject = (CardStatus)paramObject;
    if (cardState != cardState) {
      return false;
    }
    if (universalPinState != universalPinState) {
      return false;
    }
    if (gsmUmtsSubscriptionAppIndex != gsmUmtsSubscriptionAppIndex) {
      return false;
    }
    if (cdmaSubscriptionAppIndex != cdmaSubscriptionAppIndex) {
      return false;
    }
    if (imsSubscriptionAppIndex != imsSubscriptionAppIndex) {
      return false;
    }
    return HidlSupport.deepEquals(applications, applications);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cardState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(universalPinState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(gsmUmtsSubscriptionAppIndex))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cdmaSubscriptionAppIndex))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(imsSubscriptionAppIndex))), Integer.valueOf(HidlSupport.deepHashCode(applications)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cardState = paramHwBlob.getInt32(paramLong + 0L);
    universalPinState = paramHwBlob.getInt32(paramLong + 4L);
    gsmUmtsSubscriptionAppIndex = paramHwBlob.getInt32(paramLong + 8L);
    cdmaSubscriptionAppIndex = paramHwBlob.getInt32(paramLong + 12L);
    imsSubscriptionAppIndex = paramHwBlob.getInt32(paramLong + 16L);
    int i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    paramHwBlob = paramHwParcel.readEmbeddedBuffer(i * 64, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    applications.clear();
    for (int j = 0; j < i; j++)
    {
      AppStatus localAppStatus = new AppStatus();
      localAppStatus.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, j * 64);
      applications.add(localAppStatus);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cardState = ");
    localStringBuilder.append(CardState.toString(cardState));
    localStringBuilder.append(", .universalPinState = ");
    localStringBuilder.append(PinState.toString(universalPinState));
    localStringBuilder.append(", .gsmUmtsSubscriptionAppIndex = ");
    localStringBuilder.append(gsmUmtsSubscriptionAppIndex);
    localStringBuilder.append(", .cdmaSubscriptionAppIndex = ");
    localStringBuilder.append(cdmaSubscriptionAppIndex);
    localStringBuilder.append(", .imsSubscriptionAppIndex = ");
    localStringBuilder.append(imsSubscriptionAppIndex);
    localStringBuilder.append(", .applications = ");
    localStringBuilder.append(applications);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, cardState);
    paramHwBlob.putInt32(4L + paramLong, universalPinState);
    paramHwBlob.putInt32(paramLong + 8L, gsmUmtsSubscriptionAppIndex);
    paramHwBlob.putInt32(paramLong + 12L, cdmaSubscriptionAppIndex);
    paramHwBlob.putInt32(16L + paramLong, imsSubscriptionAppIndex);
    int i = applications.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 64);
    while (j < i)
    {
      ((AppStatus)applications.get(j)).writeEmbeddedToBlob(localHwBlob, j * 64);
      j++;
    }
    paramHwBlob.putBlob(24L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
