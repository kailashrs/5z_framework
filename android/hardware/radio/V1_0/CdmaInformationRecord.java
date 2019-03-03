package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaInformationRecord
{
  public final ArrayList<CdmaT53AudioControlInfoRecord> audioCtrl = new ArrayList();
  public final ArrayList<CdmaT53ClirInfoRecord> clir = new ArrayList();
  public final ArrayList<CdmaDisplayInfoRecord> display = new ArrayList();
  public final ArrayList<CdmaLineControlInfoRecord> lineCtrl = new ArrayList();
  public int name;
  public final ArrayList<CdmaNumberInfoRecord> number = new ArrayList();
  public final ArrayList<CdmaRedirectingNumberInfoRecord> redir = new ArrayList();
  public final ArrayList<CdmaSignalInfoRecord> signal = new ArrayList();
  
  public CdmaInformationRecord() {}
  
  public static final ArrayList<CdmaInformationRecord> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 120, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaInformationRecord localCdmaInformationRecord = new CdmaInformationRecord();
      localCdmaInformationRecord.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 120);
      localArrayList.add(localCdmaInformationRecord);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaInformationRecord> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 120);
    while (j < i)
    {
      ((CdmaInformationRecord)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 120);
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
    if (paramObject.getClass() != CdmaInformationRecord.class) {
      return false;
    }
    paramObject = (CdmaInformationRecord)paramObject;
    if (name != name) {
      return false;
    }
    if (!HidlSupport.deepEquals(display, display)) {
      return false;
    }
    if (!HidlSupport.deepEquals(number, number)) {
      return false;
    }
    if (!HidlSupport.deepEquals(signal, signal)) {
      return false;
    }
    if (!HidlSupport.deepEquals(redir, redir)) {
      return false;
    }
    if (!HidlSupport.deepEquals(lineCtrl, lineCtrl)) {
      return false;
    }
    if (!HidlSupport.deepEquals(clir, clir)) {
      return false;
    }
    return HidlSupport.deepEquals(audioCtrl, audioCtrl);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(name))), Integer.valueOf(HidlSupport.deepHashCode(display)), Integer.valueOf(HidlSupport.deepHashCode(number)), Integer.valueOf(HidlSupport.deepHashCode(signal)), Integer.valueOf(HidlSupport.deepHashCode(redir)), Integer.valueOf(HidlSupport.deepHashCode(lineCtrl)), Integer.valueOf(HidlSupport.deepHashCode(clir)), Integer.valueOf(HidlSupport.deepHashCode(audioCtrl)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    name = paramHwBlob.getInt32(paramLong + 0L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    Object localObject1 = paramHwParcel.readEmbeddedBuffer(i * 16, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    display.clear();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      localObject2 = new CdmaDisplayInfoRecord();
      ((CdmaDisplayInfoRecord)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 16);
      display.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    localObject1 = paramHwParcel.readEmbeddedBuffer(i * 24, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    number.clear();
    for (k = 0; k < i; k++)
    {
      localObject2 = new CdmaNumberInfoRecord();
      ((CdmaNumberInfoRecord)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 24);
      number.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 40L + 8L);
    localObject1 = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 40L + 0L, true);
    signal.clear();
    for (k = 0; k < i; k++)
    {
      localObject2 = new CdmaSignalInfoRecord();
      ((CdmaSignalInfoRecord)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 4);
      signal.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 56L + 8L);
    Object localObject2 = paramHwParcel.readEmbeddedBuffer(i * 32, paramHwBlob.handle(), paramLong + 56L + 0L, true);
    redir.clear();
    for (k = 0; k < i; k++)
    {
      localObject1 = new CdmaRedirectingNumberInfoRecord();
      ((CdmaRedirectingNumberInfoRecord)localObject1).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 32);
      redir.add(localObject1);
    }
    i = paramHwBlob.getInt32(paramLong + 72L + 8L);
    localObject2 = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 72L + 0L, true);
    lineCtrl.clear();
    for (k = 0; k < i; k++)
    {
      localObject1 = new CdmaLineControlInfoRecord();
      ((CdmaLineControlInfoRecord)localObject1).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 4);
      lineCtrl.add(localObject1);
    }
    i = paramHwBlob.getInt32(paramLong + 88L + 8L);
    localObject1 = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 88L + 0L, true);
    clir.clear();
    for (k = 0; k < i; k++)
    {
      localObject2 = new CdmaT53ClirInfoRecord();
      ((CdmaT53ClirInfoRecord)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 1);
      clir.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 104L + 8L);
    localObject2 = paramHwParcel.readEmbeddedBuffer(i * 2, paramHwBlob.handle(), 0L + (paramLong + 104L), true);
    audioCtrl.clear();
    for (k = j; k < i; k++)
    {
      paramHwBlob = new CdmaT53AudioControlInfoRecord();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 2);
      audioCtrl.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(120L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".name = ");
    localStringBuilder.append(CdmaInfoRecName.toString(name));
    localStringBuilder.append(", .display = ");
    localStringBuilder.append(display);
    localStringBuilder.append(", .number = ");
    localStringBuilder.append(number);
    localStringBuilder.append(", .signal = ");
    localStringBuilder.append(signal);
    localStringBuilder.append(", .redir = ");
    localStringBuilder.append(redir);
    localStringBuilder.append(", .lineCtrl = ");
    localStringBuilder.append(lineCtrl);
    localStringBuilder.append(", .clir = ");
    localStringBuilder.append(clir);
    localStringBuilder.append(", .audioCtrl = ");
    localStringBuilder.append(audioCtrl);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, name);
    int i = display.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 16);
    for (int k = 0; k < i; k++) {
      ((CdmaDisplayInfoRecord)display.get(k)).writeEmbeddedToBlob(localHwBlob, k * 16);
    }
    paramHwBlob.putBlob(paramLong + 8L + 0L, localHwBlob);
    i = number.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    localHwBlob = new HwBlob(i * 24);
    for (k = 0; k < i; k++) {
      ((CdmaNumberInfoRecord)number.get(k)).writeEmbeddedToBlob(localHwBlob, k * 24);
    }
    paramHwBlob.putBlob(paramLong + 24L + 0L, localHwBlob);
    i = signal.size();
    paramHwBlob.putInt32(paramLong + 40L + 8L, i);
    paramHwBlob.putBool(paramLong + 40L + 12L, false);
    localHwBlob = new HwBlob(i * 4);
    for (k = 0; k < i; k++) {
      ((CdmaSignalInfoRecord)signal.get(k)).writeEmbeddedToBlob(localHwBlob, k * 4);
    }
    paramHwBlob.putBlob(paramLong + 40L + 0L, localHwBlob);
    i = redir.size();
    paramHwBlob.putInt32(paramLong + 56L + 8L, i);
    paramHwBlob.putBool(paramLong + 56L + 12L, false);
    localHwBlob = new HwBlob(i * 32);
    for (k = 0; k < i; k++) {
      ((CdmaRedirectingNumberInfoRecord)redir.get(k)).writeEmbeddedToBlob(localHwBlob, k * 32);
    }
    paramHwBlob.putBlob(paramLong + 56L + 0L, localHwBlob);
    i = lineCtrl.size();
    paramHwBlob.putInt32(paramLong + 72L + 8L, i);
    paramHwBlob.putBool(paramLong + 72L + 12L, false);
    localHwBlob = new HwBlob(i * 4);
    for (k = 0; k < i; k++) {
      ((CdmaLineControlInfoRecord)lineCtrl.get(k)).writeEmbeddedToBlob(localHwBlob, k * 4);
    }
    paramHwBlob.putBlob(paramLong + 72L + 0L, localHwBlob);
    i = clir.size();
    paramHwBlob.putInt32(paramLong + 88L + 8L, i);
    paramHwBlob.putBool(paramLong + 88L + 12L, false);
    localHwBlob = new HwBlob(i * 1);
    for (k = 0; k < i; k++) {
      ((CdmaT53ClirInfoRecord)clir.get(k)).writeEmbeddedToBlob(localHwBlob, k * 1);
    }
    paramHwBlob.putBlob(paramLong + 88L + 0L, localHwBlob);
    i = audioCtrl.size();
    paramHwBlob.putInt32(paramLong + 104L + 8L, i);
    paramHwBlob.putBool(paramLong + 104L + 12L, false);
    localHwBlob = new HwBlob(i * 2);
    for (k = j; k < i; k++) {
      ((CdmaT53AudioControlInfoRecord)audioCtrl.get(k)).writeEmbeddedToBlob(localHwBlob, k * 2);
    }
    paramHwBlob.putBlob(paramLong + 104L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(120);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
