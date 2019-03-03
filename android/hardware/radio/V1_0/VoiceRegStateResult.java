package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class VoiceRegStateResult
{
  public final CellIdentity cellIdentity = new CellIdentity();
  public boolean cssSupported;
  public int defaultRoamingIndicator;
  public int rat;
  public int reasonForDenial;
  public int regState;
  public int roamingIndicator;
  public int systemIsInPrl;
  
  public VoiceRegStateResult() {}
  
  public static final ArrayList<VoiceRegStateResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 120, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new VoiceRegStateResult();
      ((VoiceRegStateResult)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 120);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<VoiceRegStateResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 120);
    while (j < i)
    {
      ((VoiceRegStateResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 120);
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
    if (paramObject.getClass() != VoiceRegStateResult.class) {
      return false;
    }
    paramObject = (VoiceRegStateResult)paramObject;
    if (regState != regState) {
      return false;
    }
    if (rat != rat) {
      return false;
    }
    if (cssSupported != cssSupported) {
      return false;
    }
    if (roamingIndicator != roamingIndicator) {
      return false;
    }
    if (systemIsInPrl != systemIsInPrl) {
      return false;
    }
    if (defaultRoamingIndicator != defaultRoamingIndicator) {
      return false;
    }
    if (reasonForDenial != reasonForDenial) {
      return false;
    }
    return HidlSupport.deepEquals(cellIdentity, cellIdentity);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(regState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rat))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(cssSupported))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(roamingIndicator))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(systemIsInPrl))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(defaultRoamingIndicator))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(reasonForDenial))), Integer.valueOf(HidlSupport.deepHashCode(cellIdentity)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    regState = paramHwBlob.getInt32(0L + paramLong);
    rat = paramHwBlob.getInt32(4L + paramLong);
    cssSupported = paramHwBlob.getBool(8L + paramLong);
    roamingIndicator = paramHwBlob.getInt32(12L + paramLong);
    systemIsInPrl = paramHwBlob.getInt32(16L + paramLong);
    defaultRoamingIndicator = paramHwBlob.getInt32(20L + paramLong);
    reasonForDenial = paramHwBlob.getInt32(24L + paramLong);
    cellIdentity.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 32L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(120L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".regState = ");
    localStringBuilder.append(RegState.toString(regState));
    localStringBuilder.append(", .rat = ");
    localStringBuilder.append(rat);
    localStringBuilder.append(", .cssSupported = ");
    localStringBuilder.append(cssSupported);
    localStringBuilder.append(", .roamingIndicator = ");
    localStringBuilder.append(roamingIndicator);
    localStringBuilder.append(", .systemIsInPrl = ");
    localStringBuilder.append(systemIsInPrl);
    localStringBuilder.append(", .defaultRoamingIndicator = ");
    localStringBuilder.append(defaultRoamingIndicator);
    localStringBuilder.append(", .reasonForDenial = ");
    localStringBuilder.append(reasonForDenial);
    localStringBuilder.append(", .cellIdentity = ");
    localStringBuilder.append(cellIdentity);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, regState);
    paramHwBlob.putInt32(4L + paramLong, rat);
    paramHwBlob.putBool(8L + paramLong, cssSupported);
    paramHwBlob.putInt32(12L + paramLong, roamingIndicator);
    paramHwBlob.putInt32(16L + paramLong, systemIsInPrl);
    paramHwBlob.putInt32(20L + paramLong, defaultRoamingIndicator);
    paramHwBlob.putInt32(24L + paramLong, reasonForDenial);
    cellIdentity.writeEmbeddedToBlob(paramHwBlob, 32L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(120);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
