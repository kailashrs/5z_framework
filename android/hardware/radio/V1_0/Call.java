package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class Call
{
  public byte als;
  public int index;
  public boolean isMT;
  public boolean isMpty;
  public boolean isVoice;
  public boolean isVoicePrivacy;
  public String name = new String();
  public int namePresentation;
  public String number = new String();
  public int numberPresentation;
  public int state;
  public int toa;
  public final ArrayList<UusInfo> uusInfo = new ArrayList();
  
  public Call() {}
  
  public static final ArrayList<Call> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new Call();
      ((Call)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 88);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<Call> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 88);
    while (j < i)
    {
      ((Call)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 88);
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
    if (paramObject.getClass() != Call.class) {
      return false;
    }
    paramObject = (Call)paramObject;
    if (state != state) {
      return false;
    }
    if (index != index) {
      return false;
    }
    if (toa != toa) {
      return false;
    }
    if (isMpty != isMpty) {
      return false;
    }
    if (isMT != isMT) {
      return false;
    }
    if (als != als) {
      return false;
    }
    if (isVoice != isVoice) {
      return false;
    }
    if (isVoicePrivacy != isVoicePrivacy) {
      return false;
    }
    if (!HidlSupport.deepEquals(number, number)) {
      return false;
    }
    if (numberPresentation != numberPresentation) {
      return false;
    }
    if (!HidlSupport.deepEquals(name, name)) {
      return false;
    }
    if (namePresentation != namePresentation) {
      return false;
    }
    return HidlSupport.deepEquals(uusInfo, uusInfo);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(state))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(index))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(toa))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isMpty))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isMT))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(als))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isVoice))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isVoicePrivacy))), Integer.valueOf(HidlSupport.deepHashCode(number)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberPresentation))), Integer.valueOf(HidlSupport.deepHashCode(name)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(namePresentation))), Integer.valueOf(HidlSupport.deepHashCode(uusInfo)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    state = paramHwBlob.getInt32(paramLong + 0L);
    index = paramHwBlob.getInt32(paramLong + 4L);
    toa = paramHwBlob.getInt32(paramLong + 8L);
    isMpty = paramHwBlob.getBool(paramLong + 12L);
    isMT = paramHwBlob.getBool(paramLong + 13L);
    als = paramHwBlob.getInt8(paramLong + 14L);
    isVoice = paramHwBlob.getBool(paramLong + 15L);
    isVoicePrivacy = paramHwBlob.getBool(paramLong + 16L);
    number = paramHwBlob.getString(paramLong + 24L);
    paramHwParcel.readEmbeddedBuffer(number.getBytes().length + 1, paramHwBlob.handle(), paramLong + 24L + 0L, false);
    numberPresentation = paramHwBlob.getInt32(paramLong + 40L);
    name = paramHwBlob.getString(paramLong + 48L);
    paramHwParcel.readEmbeddedBuffer(name.getBytes().length + 1, paramHwBlob.handle(), paramLong + 48L + 0L, false);
    namePresentation = paramHwBlob.getInt32(paramLong + 64L);
    int i = paramHwBlob.getInt32(paramLong + 72L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, paramHwBlob.handle(), paramLong + 72L + 0L, true);
    uusInfo.clear();
    for (int j = 0; j < i; j++)
    {
      paramHwBlob = new UusInfo();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      uusInfo.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(88L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".state = ");
    localStringBuilder.append(CallState.toString(state));
    localStringBuilder.append(", .index = ");
    localStringBuilder.append(index);
    localStringBuilder.append(", .toa = ");
    localStringBuilder.append(toa);
    localStringBuilder.append(", .isMpty = ");
    localStringBuilder.append(isMpty);
    localStringBuilder.append(", .isMT = ");
    localStringBuilder.append(isMT);
    localStringBuilder.append(", .als = ");
    localStringBuilder.append(als);
    localStringBuilder.append(", .isVoice = ");
    localStringBuilder.append(isVoice);
    localStringBuilder.append(", .isVoicePrivacy = ");
    localStringBuilder.append(isVoicePrivacy);
    localStringBuilder.append(", .number = ");
    localStringBuilder.append(number);
    localStringBuilder.append(", .numberPresentation = ");
    localStringBuilder.append(CallPresentation.toString(numberPresentation));
    localStringBuilder.append(", .name = ");
    localStringBuilder.append(name);
    localStringBuilder.append(", .namePresentation = ");
    localStringBuilder.append(CallPresentation.toString(namePresentation));
    localStringBuilder.append(", .uusInfo = ");
    localStringBuilder.append(uusInfo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, state);
    paramHwBlob.putInt32(4L + paramLong, index);
    paramHwBlob.putInt32(paramLong + 8L, toa);
    paramHwBlob.putBool(paramLong + 12L, isMpty);
    paramHwBlob.putBool(13L + paramLong, isMT);
    paramHwBlob.putInt8(14L + paramLong, als);
    paramHwBlob.putBool(15L + paramLong, isVoice);
    paramHwBlob.putBool(16L + paramLong, isVoicePrivacy);
    paramHwBlob.putString(24L + paramLong, number);
    paramHwBlob.putInt32(40L + paramLong, numberPresentation);
    paramHwBlob.putString(48L + paramLong, name);
    paramHwBlob.putInt32(64L + paramLong, namePresentation);
    int i = uusInfo.size();
    paramHwBlob.putInt32(paramLong + 72L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 72L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 24);
    while (j < i)
    {
      ((UusInfo)uusInfo.get(j)).writeEmbeddedToBlob(localHwBlob, j * 24);
      j++;
    }
    paramHwBlob.putBlob(72L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(88);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
