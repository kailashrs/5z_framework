package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class DataProfileInfo
{
  public String apn = new String();
  public int authType;
  public int bearerBitmap;
  public boolean enabled;
  public int maxConns;
  public int maxConnsTime;
  public int mtu;
  public String mvnoMatchData = new String();
  public int mvnoType;
  public String password = new String();
  public int profileId;
  public String protocol = new String();
  public String roamingProtocol = new String();
  public int supportedApnTypesBitmap;
  public int type;
  public String user = new String();
  public int waitTime;
  
  public DataProfileInfo() {}
  
  public static final ArrayList<DataProfileInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 152, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new DataProfileInfo();
      ((DataProfileInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 152);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<DataProfileInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 152);
    while (j < i)
    {
      ((DataProfileInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 152);
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
    if (paramObject.getClass() != DataProfileInfo.class) {
      return false;
    }
    paramObject = (DataProfileInfo)paramObject;
    if (profileId != profileId) {
      return false;
    }
    if (!HidlSupport.deepEquals(apn, apn)) {
      return false;
    }
    if (!HidlSupport.deepEquals(protocol, protocol)) {
      return false;
    }
    if (!HidlSupport.deepEquals(roamingProtocol, roamingProtocol)) {
      return false;
    }
    if (authType != authType) {
      return false;
    }
    if (!HidlSupport.deepEquals(user, user)) {
      return false;
    }
    if (!HidlSupport.deepEquals(password, password)) {
      return false;
    }
    if (type != type) {
      return false;
    }
    if (maxConnsTime != maxConnsTime) {
      return false;
    }
    if (maxConns != maxConns) {
      return false;
    }
    if (waitTime != waitTime) {
      return false;
    }
    if (enabled != enabled) {
      return false;
    }
    if (!HidlSupport.deepEquals(Integer.valueOf(supportedApnTypesBitmap), Integer.valueOf(supportedApnTypesBitmap))) {
      return false;
    }
    if (!HidlSupport.deepEquals(Integer.valueOf(bearerBitmap), Integer.valueOf(bearerBitmap))) {
      return false;
    }
    if (mtu != mtu) {
      return false;
    }
    if (mvnoType != mvnoType) {
      return false;
    }
    return HidlSupport.deepEquals(mvnoMatchData, mvnoMatchData);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(profileId))), Integer.valueOf(HidlSupport.deepHashCode(apn)), Integer.valueOf(HidlSupport.deepHashCode(protocol)), Integer.valueOf(HidlSupport.deepHashCode(roamingProtocol)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(authType))), Integer.valueOf(HidlSupport.deepHashCode(user)), Integer.valueOf(HidlSupport.deepHashCode(password)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxConnsTime))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxConns))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(waitTime))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(enabled))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(supportedApnTypesBitmap))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(bearerBitmap))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(mtu))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(mvnoType))), Integer.valueOf(HidlSupport.deepHashCode(mvnoMatchData)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    profileId = paramHwBlob.getInt32(paramLong + 0L);
    apn = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(apn.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    protocol = paramHwBlob.getString(paramLong + 24L);
    paramHwParcel.readEmbeddedBuffer(protocol.getBytes().length + 1, paramHwBlob.handle(), paramLong + 24L + 0L, false);
    roamingProtocol = paramHwBlob.getString(paramLong + 40L);
    paramHwParcel.readEmbeddedBuffer(roamingProtocol.getBytes().length + 1, paramHwBlob.handle(), paramLong + 40L + 0L, false);
    authType = paramHwBlob.getInt32(paramLong + 56L);
    user = paramHwBlob.getString(paramLong + 64L);
    paramHwParcel.readEmbeddedBuffer(user.getBytes().length + 1, paramHwBlob.handle(), paramLong + 64L + 0L, false);
    password = paramHwBlob.getString(paramLong + 80L);
    paramHwParcel.readEmbeddedBuffer(password.getBytes().length + 1, paramHwBlob.handle(), paramLong + 80L + 0L, false);
    type = paramHwBlob.getInt32(paramLong + 96L);
    maxConnsTime = paramHwBlob.getInt32(paramLong + 100L);
    maxConns = paramHwBlob.getInt32(paramLong + 104L);
    waitTime = paramHwBlob.getInt32(paramLong + 108L);
    enabled = paramHwBlob.getBool(paramLong + 112L);
    supportedApnTypesBitmap = paramHwBlob.getInt32(paramLong + 116L);
    bearerBitmap = paramHwBlob.getInt32(paramLong + 120L);
    mtu = paramHwBlob.getInt32(paramLong + 124L);
    mvnoType = paramHwBlob.getInt32(paramLong + 128L);
    mvnoMatchData = paramHwBlob.getString(paramLong + 136L);
    paramHwParcel.readEmbeddedBuffer(mvnoMatchData.getBytes().length + 1, paramHwBlob.handle(), paramLong + 136L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(152L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".profileId = ");
    localStringBuilder.append(DataProfileId.toString(profileId));
    localStringBuilder.append(", .apn = ");
    localStringBuilder.append(apn);
    localStringBuilder.append(", .protocol = ");
    localStringBuilder.append(protocol);
    localStringBuilder.append(", .roamingProtocol = ");
    localStringBuilder.append(roamingProtocol);
    localStringBuilder.append(", .authType = ");
    localStringBuilder.append(ApnAuthType.toString(authType));
    localStringBuilder.append(", .user = ");
    localStringBuilder.append(user);
    localStringBuilder.append(", .password = ");
    localStringBuilder.append(password);
    localStringBuilder.append(", .type = ");
    localStringBuilder.append(DataProfileInfoType.toString(type));
    localStringBuilder.append(", .maxConnsTime = ");
    localStringBuilder.append(maxConnsTime);
    localStringBuilder.append(", .maxConns = ");
    localStringBuilder.append(maxConns);
    localStringBuilder.append(", .waitTime = ");
    localStringBuilder.append(waitTime);
    localStringBuilder.append(", .enabled = ");
    localStringBuilder.append(enabled);
    localStringBuilder.append(", .supportedApnTypesBitmap = ");
    localStringBuilder.append(ApnTypes.dumpBitfield(supportedApnTypesBitmap));
    localStringBuilder.append(", .bearerBitmap = ");
    localStringBuilder.append(RadioAccessFamily.dumpBitfield(bearerBitmap));
    localStringBuilder.append(", .mtu = ");
    localStringBuilder.append(mtu);
    localStringBuilder.append(", .mvnoType = ");
    localStringBuilder.append(MvnoType.toString(mvnoType));
    localStringBuilder.append(", .mvnoMatchData = ");
    localStringBuilder.append(mvnoMatchData);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, profileId);
    paramHwBlob.putString(8L + paramLong, apn);
    paramHwBlob.putString(24L + paramLong, protocol);
    paramHwBlob.putString(40L + paramLong, roamingProtocol);
    paramHwBlob.putInt32(56L + paramLong, authType);
    paramHwBlob.putString(64L + paramLong, user);
    paramHwBlob.putString(80L + paramLong, password);
    paramHwBlob.putInt32(96L + paramLong, type);
    paramHwBlob.putInt32(100L + paramLong, maxConnsTime);
    paramHwBlob.putInt32(104L + paramLong, maxConns);
    paramHwBlob.putInt32(108L + paramLong, waitTime);
    paramHwBlob.putBool(112L + paramLong, enabled);
    paramHwBlob.putInt32(116L + paramLong, supportedApnTypesBitmap);
    paramHwBlob.putInt32(120L + paramLong, bearerBitmap);
    paramHwBlob.putInt32(124L + paramLong, mtu);
    paramHwBlob.putInt32(128L + paramLong, mvnoType);
    paramHwBlob.putString(136L + paramLong, mvnoMatchData);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(152);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
