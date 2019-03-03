package android.hardware.radio.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class RadioAccessSpecifier
{
  public final ArrayList<Integer> channels = new ArrayList();
  public final ArrayList<Integer> eutranBands = new ArrayList();
  public final ArrayList<Integer> geranBands = new ArrayList();
  public int radioAccessNetwork;
  public final ArrayList<Integer> utranBands = new ArrayList();
  
  public RadioAccessSpecifier() {}
  
  public static final ArrayList<RadioAccessSpecifier> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 72, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      RadioAccessSpecifier localRadioAccessSpecifier = new RadioAccessSpecifier();
      localRadioAccessSpecifier.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 72);
      localArrayList.add(localRadioAccessSpecifier);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<RadioAccessSpecifier> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 72);
    while (j < i)
    {
      ((RadioAccessSpecifier)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 72);
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
    if (paramObject.getClass() != RadioAccessSpecifier.class) {
      return false;
    }
    paramObject = (RadioAccessSpecifier)paramObject;
    if (radioAccessNetwork != radioAccessNetwork) {
      return false;
    }
    if (!HidlSupport.deepEquals(geranBands, geranBands)) {
      return false;
    }
    if (!HidlSupport.deepEquals(utranBands, utranBands)) {
      return false;
    }
    if (!HidlSupport.deepEquals(eutranBands, eutranBands)) {
      return false;
    }
    return HidlSupport.deepEquals(channels, channels);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(radioAccessNetwork))), Integer.valueOf(HidlSupport.deepHashCode(geranBands)), Integer.valueOf(HidlSupport.deepHashCode(utranBands)), Integer.valueOf(HidlSupport.deepHashCode(eutranBands)), Integer.valueOf(HidlSupport.deepHashCode(channels)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    radioAccessNetwork = paramHwBlob.getInt32(paramLong + 0L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    geranBands.clear();
    int j = 0;
    int m;
    for (int k = 0; k < i; k++)
    {
      m = localHwBlob.getInt32(k * 4);
      geranBands.add(Integer.valueOf(m));
    }
    i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    utranBands.clear();
    for (k = 0; k < i; k++)
    {
      m = localHwBlob.getInt32(k * 4);
      utranBands.add(Integer.valueOf(m));
    }
    i = paramHwBlob.getInt32(paramLong + 40L + 8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 40L + 0L, true);
    eutranBands.clear();
    for (k = 0; k < i; k++)
    {
      m = localHwBlob.getInt32(k * 4);
      eutranBands.add(Integer.valueOf(m));
    }
    i = paramHwBlob.getInt32(paramLong + 56L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 56L + 0L, true);
    channels.clear();
    for (k = j; k < i; k++)
    {
      j = paramHwParcel.getInt32(k * 4);
      channels.add(Integer.valueOf(j));
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(72L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".radioAccessNetwork = ");
    localStringBuilder.append(RadioAccessNetworks.toString(radioAccessNetwork));
    localStringBuilder.append(", .geranBands = ");
    localStringBuilder.append(geranBands);
    localStringBuilder.append(", .utranBands = ");
    localStringBuilder.append(utranBands);
    localStringBuilder.append(", .eutranBands = ");
    localStringBuilder.append(eutranBands);
    localStringBuilder.append(", .channels = ");
    localStringBuilder.append(channels);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, radioAccessNetwork);
    int i = geranBands.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 4);
    for (int j = 0; j < i; j++) {
      localHwBlob.putInt32(j * 4, ((Integer)geranBands.get(j)).intValue());
    }
    paramHwBlob.putBlob(paramLong + 8L + 0L, localHwBlob);
    i = utranBands.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    localHwBlob = new HwBlob(i * 4);
    for (j = 0; j < i; j++) {
      localHwBlob.putInt32(j * 4, ((Integer)utranBands.get(j)).intValue());
    }
    paramHwBlob.putBlob(paramLong + 24L + 0L, localHwBlob);
    i = eutranBands.size();
    paramHwBlob.putInt32(paramLong + 40L + 8L, i);
    paramHwBlob.putBool(paramLong + 40L + 12L, false);
    localHwBlob = new HwBlob(i * 4);
    for (j = 0; j < i; j++) {
      localHwBlob.putInt32(j * 4, ((Integer)eutranBands.get(j)).intValue());
    }
    paramHwBlob.putBlob(paramLong + 40L + 0L, localHwBlob);
    i = channels.size();
    paramHwBlob.putInt32(paramLong + 56L + 8L, i);
    j = 0;
    paramHwBlob.putBool(paramLong + 56L + 12L, false);
    localHwBlob = new HwBlob(i * 4);
    while (j < i)
    {
      localHwBlob.putInt32(j * 4, ((Integer)channels.get(j)).intValue());
      j++;
    }
    paramHwBlob.putBlob(paramLong + 56L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(72);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
