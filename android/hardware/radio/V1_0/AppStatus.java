package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class AppStatus
{
  public String aidPtr = new String();
  public String appLabelPtr = new String();
  public int appState;
  public int appType;
  public int persoSubstate;
  public int pin1;
  public int pin1Replaced;
  public int pin2;
  
  public AppStatus() {}
  
  public static final ArrayList<AppStatus> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 64, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new AppStatus();
      ((AppStatus)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 64);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<AppStatus> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 64);
    while (j < i)
    {
      ((AppStatus)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 64);
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
    if (paramObject.getClass() != AppStatus.class) {
      return false;
    }
    paramObject = (AppStatus)paramObject;
    if (appType != appType) {
      return false;
    }
    if (appState != appState) {
      return false;
    }
    if (persoSubstate != persoSubstate) {
      return false;
    }
    if (!HidlSupport.deepEquals(aidPtr, aidPtr)) {
      return false;
    }
    if (!HidlSupport.deepEquals(appLabelPtr, appLabelPtr)) {
      return false;
    }
    if (pin1Replaced != pin1Replaced) {
      return false;
    }
    if (pin1 != pin1) {
      return false;
    }
    return pin2 == pin2;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(appType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(appState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(persoSubstate))), Integer.valueOf(HidlSupport.deepHashCode(aidPtr)), Integer.valueOf(HidlSupport.deepHashCode(appLabelPtr)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(pin1Replaced))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(pin1))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(pin2))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    appType = paramHwBlob.getInt32(paramLong + 0L);
    appState = paramHwBlob.getInt32(paramLong + 4L);
    persoSubstate = paramHwBlob.getInt32(paramLong + 8L);
    aidPtr = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(aidPtr.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    appLabelPtr = paramHwBlob.getString(paramLong + 32L);
    paramHwParcel.readEmbeddedBuffer(appLabelPtr.getBytes().length + 1, paramHwBlob.handle(), paramLong + 32L + 0L, false);
    pin1Replaced = paramHwBlob.getInt32(paramLong + 48L);
    pin1 = paramHwBlob.getInt32(paramLong + 52L);
    pin2 = paramHwBlob.getInt32(paramLong + 56L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(64L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".appType = ");
    localStringBuilder.append(AppType.toString(appType));
    localStringBuilder.append(", .appState = ");
    localStringBuilder.append(AppState.toString(appState));
    localStringBuilder.append(", .persoSubstate = ");
    localStringBuilder.append(PersoSubstate.toString(persoSubstate));
    localStringBuilder.append(", .aidPtr = ");
    localStringBuilder.append(aidPtr);
    localStringBuilder.append(", .appLabelPtr = ");
    localStringBuilder.append(appLabelPtr);
    localStringBuilder.append(", .pin1Replaced = ");
    localStringBuilder.append(pin1Replaced);
    localStringBuilder.append(", .pin1 = ");
    localStringBuilder.append(PinState.toString(pin1));
    localStringBuilder.append(", .pin2 = ");
    localStringBuilder.append(PinState.toString(pin2));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, appType);
    paramHwBlob.putInt32(4L + paramLong, appState);
    paramHwBlob.putInt32(8L + paramLong, persoSubstate);
    paramHwBlob.putString(16L + paramLong, aidPtr);
    paramHwBlob.putString(32L + paramLong, appLabelPtr);
    paramHwBlob.putInt32(48L + paramLong, pin1Replaced);
    paramHwBlob.putInt32(52L + paramLong, pin1);
    paramHwBlob.putInt32(56L + paramLong, pin2);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(64);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
