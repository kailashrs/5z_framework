package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SetupDataCallResult
{
  public int active;
  public String addresses = new String();
  public int cid;
  public String dnses = new String();
  public String gateways = new String();
  public String ifname = new String();
  public int mtu;
  public String pcscf = new String();
  public int status;
  public int suggestedRetryTime;
  public String type = new String();
  
  public SetupDataCallResult() {}
  
  public static final ArrayList<SetupDataCallResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 120, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new SetupDataCallResult();
      ((SetupDataCallResult)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 120);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SetupDataCallResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 120);
    while (j < i)
    {
      ((SetupDataCallResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 120);
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
    if (paramObject.getClass() != SetupDataCallResult.class) {
      return false;
    }
    paramObject = (SetupDataCallResult)paramObject;
    if (status != status) {
      return false;
    }
    if (suggestedRetryTime != suggestedRetryTime) {
      return false;
    }
    if (cid != cid) {
      return false;
    }
    if (active != active) {
      return false;
    }
    if (!HidlSupport.deepEquals(type, type)) {
      return false;
    }
    if (!HidlSupport.deepEquals(ifname, ifname)) {
      return false;
    }
    if (!HidlSupport.deepEquals(addresses, addresses)) {
      return false;
    }
    if (!HidlSupport.deepEquals(dnses, dnses)) {
      return false;
    }
    if (!HidlSupport.deepEquals(gateways, gateways)) {
      return false;
    }
    if (!HidlSupport.deepEquals(pcscf, pcscf)) {
      return false;
    }
    return mtu == mtu;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(suggestedRetryTime))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cid))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(active))), Integer.valueOf(HidlSupport.deepHashCode(type)), Integer.valueOf(HidlSupport.deepHashCode(ifname)), Integer.valueOf(HidlSupport.deepHashCode(addresses)), Integer.valueOf(HidlSupport.deepHashCode(dnses)), Integer.valueOf(HidlSupport.deepHashCode(gateways)), Integer.valueOf(HidlSupport.deepHashCode(pcscf)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(mtu))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    status = paramHwBlob.getInt32(paramLong + 0L);
    suggestedRetryTime = paramHwBlob.getInt32(paramLong + 4L);
    cid = paramHwBlob.getInt32(paramLong + 8L);
    active = paramHwBlob.getInt32(paramLong + 12L);
    type = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(type.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    ifname = paramHwBlob.getString(paramLong + 32L);
    paramHwParcel.readEmbeddedBuffer(ifname.getBytes().length + 1, paramHwBlob.handle(), paramLong + 32L + 0L, false);
    addresses = paramHwBlob.getString(paramLong + 48L);
    paramHwParcel.readEmbeddedBuffer(addresses.getBytes().length + 1, paramHwBlob.handle(), paramLong + 48L + 0L, false);
    dnses = paramHwBlob.getString(paramLong + 64L);
    paramHwParcel.readEmbeddedBuffer(dnses.getBytes().length + 1, paramHwBlob.handle(), paramLong + 64L + 0L, false);
    gateways = paramHwBlob.getString(paramLong + 80L);
    paramHwParcel.readEmbeddedBuffer(gateways.getBytes().length + 1, paramHwBlob.handle(), paramLong + 80L + 0L, false);
    pcscf = paramHwBlob.getString(paramLong + 96L);
    paramHwParcel.readEmbeddedBuffer(pcscf.getBytes().length + 1, paramHwBlob.handle(), paramLong + 96L + 0L, false);
    mtu = paramHwBlob.getInt32(paramLong + 112L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(120L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".status = ");
    localStringBuilder.append(DataCallFailCause.toString(status));
    localStringBuilder.append(", .suggestedRetryTime = ");
    localStringBuilder.append(suggestedRetryTime);
    localStringBuilder.append(", .cid = ");
    localStringBuilder.append(cid);
    localStringBuilder.append(", .active = ");
    localStringBuilder.append(active);
    localStringBuilder.append(", .type = ");
    localStringBuilder.append(type);
    localStringBuilder.append(", .ifname = ");
    localStringBuilder.append(ifname);
    localStringBuilder.append(", .addresses = ");
    localStringBuilder.append(addresses);
    localStringBuilder.append(", .dnses = ");
    localStringBuilder.append(dnses);
    localStringBuilder.append(", .gateways = ");
    localStringBuilder.append(gateways);
    localStringBuilder.append(", .pcscf = ");
    localStringBuilder.append(pcscf);
    localStringBuilder.append(", .mtu = ");
    localStringBuilder.append(mtu);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, status);
    paramHwBlob.putInt32(4L + paramLong, suggestedRetryTime);
    paramHwBlob.putInt32(8L + paramLong, cid);
    paramHwBlob.putInt32(12L + paramLong, active);
    paramHwBlob.putString(16L + paramLong, type);
    paramHwBlob.putString(32L + paramLong, ifname);
    paramHwBlob.putString(48L + paramLong, addresses);
    paramHwBlob.putString(64L + paramLong, dnses);
    paramHwBlob.putString(80L + paramLong, gateways);
    paramHwBlob.putString(96L + paramLong, pcscf);
    paramHwBlob.putInt32(112L + paramLong, mtu);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(120);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
