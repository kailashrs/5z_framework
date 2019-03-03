package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public final class Policy
  implements Parcelable
{
  public static final Parcelable.Creator<Policy> CREATOR = new Parcelable.Creator()
  {
    private Map<Integer, String> readProtoPortMap(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == -1) {
        return null;
      }
      HashMap localHashMap = new HashMap(i);
      for (int j = 0; j < i; j++) {
        localHashMap.put(Integer.valueOf(paramAnonymousParcel.readInt()), paramAnonymousParcel.readString());
      }
      return localHashMap;
    }
    
    private List<Policy.RoamingPartner> readRoamingPartnerList(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == -1) {
        return null;
      }
      ArrayList localArrayList = new ArrayList();
      for (int j = 0; j < i; j++) {
        localArrayList.add((Policy.RoamingPartner)paramAnonymousParcel.readParcelable(null));
      }
      return localArrayList;
    }
    
    public Policy createFromParcel(Parcel paramAnonymousParcel)
    {
      Policy localPolicy = new Policy();
      localPolicy.setMinHomeDownlinkBandwidth(paramAnonymousParcel.readLong());
      localPolicy.setMinHomeUplinkBandwidth(paramAnonymousParcel.readLong());
      localPolicy.setMinRoamingDownlinkBandwidth(paramAnonymousParcel.readLong());
      localPolicy.setMinRoamingUplinkBandwidth(paramAnonymousParcel.readLong());
      localPolicy.setExcludedSsidList(paramAnonymousParcel.createStringArray());
      localPolicy.setRequiredProtoPortMap(readProtoPortMap(paramAnonymousParcel));
      localPolicy.setMaximumBssLoadValue(paramAnonymousParcel.readInt());
      localPolicy.setPreferredRoamingPartnerList(readRoamingPartnerList(paramAnonymousParcel));
      localPolicy.setPolicyUpdate((UpdateParameter)paramAnonymousParcel.readParcelable(null));
      return localPolicy;
    }
    
    public Policy[] newArray(int paramAnonymousInt)
    {
      return new Policy[paramAnonymousInt];
    }
  };
  private static final int MAX_EXCLUSION_SSIDS = 128;
  private static final int MAX_PORT_STRING_BYTES = 64;
  private static final int MAX_SSID_BYTES = 32;
  private static final int NULL_VALUE = -1;
  private static final String TAG = "Policy";
  private String[] mExcludedSsidList = null;
  private int mMaximumBssLoadValue = Integer.MIN_VALUE;
  private long mMinHomeDownlinkBandwidth = Long.MIN_VALUE;
  private long mMinHomeUplinkBandwidth = Long.MIN_VALUE;
  private long mMinRoamingDownlinkBandwidth = Long.MIN_VALUE;
  private long mMinRoamingUplinkBandwidth = Long.MIN_VALUE;
  private UpdateParameter mPolicyUpdate = null;
  private List<RoamingPartner> mPreferredRoamingPartnerList = null;
  private Map<Integer, String> mRequiredProtoPortMap = null;
  
  public Policy() {}
  
  public Policy(Policy paramPolicy)
  {
    if (paramPolicy == null) {
      return;
    }
    mMinHomeDownlinkBandwidth = mMinHomeDownlinkBandwidth;
    mMinHomeUplinkBandwidth = mMinHomeUplinkBandwidth;
    mMinRoamingDownlinkBandwidth = mMinRoamingDownlinkBandwidth;
    mMinRoamingUplinkBandwidth = mMinRoamingUplinkBandwidth;
    mMaximumBssLoadValue = mMaximumBssLoadValue;
    if (mExcludedSsidList != null) {
      mExcludedSsidList = ((String[])Arrays.copyOf(mExcludedSsidList, mExcludedSsidList.length));
    }
    if (mRequiredProtoPortMap != null) {
      mRequiredProtoPortMap = Collections.unmodifiableMap(mRequiredProtoPortMap);
    }
    if (mPreferredRoamingPartnerList != null) {
      mPreferredRoamingPartnerList = Collections.unmodifiableList(mPreferredRoamingPartnerList);
    }
    if (mPolicyUpdate != null) {
      mPolicyUpdate = new UpdateParameter(mPolicyUpdate);
    }
  }
  
  private static void writeProtoPortMap(Parcel paramParcel, Map<Integer, String> paramMap)
  {
    if (paramMap == null)
    {
      paramParcel.writeInt(-1);
      return;
    }
    paramParcel.writeInt(paramMap.size());
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      paramParcel.writeInt(((Integer)localEntry.getKey()).intValue());
      paramParcel.writeString((String)localEntry.getValue());
    }
  }
  
  private static void writeRoamingPartnerList(Parcel paramParcel, int paramInt, List<RoamingPartner> paramList)
  {
    if (paramList == null)
    {
      paramParcel.writeInt(-1);
      return;
    }
    paramParcel.writeInt(paramList.size());
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      paramParcel.writeParcelable((RoamingPartner)paramList.next(), paramInt);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof Policy)) {
      return false;
    }
    paramObject = (Policy)paramObject;
    if ((mMinHomeDownlinkBandwidth == mMinHomeDownlinkBandwidth) && (mMinHomeUplinkBandwidth == mMinHomeUplinkBandwidth) && (mMinRoamingDownlinkBandwidth == mMinRoamingDownlinkBandwidth) && (mMinRoamingUplinkBandwidth == mMinRoamingUplinkBandwidth) && (Arrays.equals(mExcludedSsidList, mExcludedSsidList)) && (mRequiredProtoPortMap == null ? mRequiredProtoPortMap == null : mRequiredProtoPortMap.equals(mRequiredProtoPortMap)) && (mMaximumBssLoadValue == mMaximumBssLoadValue) && (mPreferredRoamingPartnerList == null ? mPreferredRoamingPartnerList == null : mPreferredRoamingPartnerList.equals(mPreferredRoamingPartnerList))) {
      if (mPolicyUpdate == null) {
        if (mPolicyUpdate != null) {
          break label196;
        }
      } else if (mPolicyUpdate.equals(mPolicyUpdate)) {
        return bool;
      }
    }
    label196:
    bool = false;
    return bool;
  }
  
  public String[] getExcludedSsidList()
  {
    return mExcludedSsidList;
  }
  
  public int getMaximumBssLoadValue()
  {
    return mMaximumBssLoadValue;
  }
  
  public long getMinHomeDownlinkBandwidth()
  {
    return mMinHomeDownlinkBandwidth;
  }
  
  public long getMinHomeUplinkBandwidth()
  {
    return mMinHomeUplinkBandwidth;
  }
  
  public long getMinRoamingDownlinkBandwidth()
  {
    return mMinRoamingDownlinkBandwidth;
  }
  
  public long getMinRoamingUplinkBandwidth()
  {
    return mMinRoamingUplinkBandwidth;
  }
  
  public UpdateParameter getPolicyUpdate()
  {
    return mPolicyUpdate;
  }
  
  public List<RoamingPartner> getPreferredRoamingPartnerList()
  {
    return mPreferredRoamingPartnerList;
  }
  
  public Map<Integer, String> getRequiredProtoPortMap()
  {
    return mRequiredProtoPortMap;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Long.valueOf(mMinHomeDownlinkBandwidth), Long.valueOf(mMinHomeUplinkBandwidth), Long.valueOf(mMinRoamingDownlinkBandwidth), Long.valueOf(mMinRoamingUplinkBandwidth), mExcludedSsidList, mRequiredProtoPortMap, Integer.valueOf(mMaximumBssLoadValue), mPreferredRoamingPartnerList, mPolicyUpdate });
  }
  
  public void setExcludedSsidList(String[] paramArrayOfString)
  {
    mExcludedSsidList = paramArrayOfString;
  }
  
  public void setMaximumBssLoadValue(int paramInt)
  {
    mMaximumBssLoadValue = paramInt;
  }
  
  public void setMinHomeDownlinkBandwidth(long paramLong)
  {
    mMinHomeDownlinkBandwidth = paramLong;
  }
  
  public void setMinHomeUplinkBandwidth(long paramLong)
  {
    mMinHomeUplinkBandwidth = paramLong;
  }
  
  public void setMinRoamingDownlinkBandwidth(long paramLong)
  {
    mMinRoamingDownlinkBandwidth = paramLong;
  }
  
  public void setMinRoamingUplinkBandwidth(long paramLong)
  {
    mMinRoamingUplinkBandwidth = paramLong;
  }
  
  public void setPolicyUpdate(UpdateParameter paramUpdateParameter)
  {
    mPolicyUpdate = paramUpdateParameter;
  }
  
  public void setPreferredRoamingPartnerList(List<RoamingPartner> paramList)
  {
    mPreferredRoamingPartnerList = paramList;
  }
  
  public void setRequiredProtoPortMap(Map<Integer, String> paramMap)
  {
    mRequiredProtoPortMap = paramMap;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MinHomeDownlinkBandwidth: ");
    localStringBuilder.append(mMinHomeDownlinkBandwidth);
    localStringBuilder.append("\n");
    localStringBuilder.append("MinHomeUplinkBandwidth: ");
    localStringBuilder.append(mMinHomeUplinkBandwidth);
    localStringBuilder.append("\n");
    localStringBuilder.append("MinRoamingDownlinkBandwidth: ");
    localStringBuilder.append(mMinRoamingDownlinkBandwidth);
    localStringBuilder.append("\n");
    localStringBuilder.append("MinRoamingUplinkBandwidth: ");
    localStringBuilder.append(mMinRoamingUplinkBandwidth);
    localStringBuilder.append("\n");
    localStringBuilder.append("ExcludedSSIDList: ");
    localStringBuilder.append(mExcludedSsidList);
    localStringBuilder.append("\n");
    localStringBuilder.append("RequiredProtoPortMap: ");
    localStringBuilder.append(mRequiredProtoPortMap);
    localStringBuilder.append("\n");
    localStringBuilder.append("MaximumBSSLoadValue: ");
    localStringBuilder.append(mMaximumBssLoadValue);
    localStringBuilder.append("\n");
    localStringBuilder.append("PreferredRoamingPartnerList: ");
    localStringBuilder.append(mPreferredRoamingPartnerList);
    localStringBuilder.append("\n");
    if (mPolicyUpdate != null)
    {
      localStringBuilder.append("PolicyUpdate Begin ---\n");
      localStringBuilder.append(mPolicyUpdate);
      localStringBuilder.append("PolicyUpdate End ---\n");
    }
    return localStringBuilder.toString();
  }
  
  public boolean validate()
  {
    if (mPolicyUpdate == null)
    {
      Log.d("Policy", "PolicyUpdate not specified");
      return false;
    }
    if (!mPolicyUpdate.validate()) {
      return false;
    }
    Object localObject1;
    if (mExcludedSsidList != null)
    {
      if (mExcludedSsidList.length > 128)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("SSID exclusion list size exceeded the max: ");
        ((StringBuilder)localObject1).append(mExcludedSsidList.length);
        Log.d("Policy", ((StringBuilder)localObject1).toString());
        return false;
      }
      for (localObject1 : mExcludedSsidList) {
        if (((String)localObject1).getBytes(StandardCharsets.UTF_8).length > 32)
        {
          ??? = new StringBuilder();
          ((StringBuilder)???).append("Invalid SSID: ");
          ((StringBuilder)???).append((String)localObject1);
          Log.d("Policy", ((StringBuilder)???).toString());
          return false;
        }
      }
    }
    if (mRequiredProtoPortMap != null)
    {
      ??? = mRequiredProtoPortMap.entrySet().iterator();
      while (((Iterator)???).hasNext())
      {
        localObject1 = (String)((Map.Entry)((Iterator)???).next()).getValue();
        if (((String)localObject1).getBytes(StandardCharsets.UTF_8).length > 64)
        {
          ??? = new StringBuilder();
          ((StringBuilder)???).append("PortNumber string bytes exceeded the max: ");
          ((StringBuilder)???).append((String)localObject1);
          Log.d("Policy", ((StringBuilder)???).toString());
          return false;
        }
      }
    }
    if (mPreferredRoamingPartnerList != null)
    {
      localObject1 = mPreferredRoamingPartnerList.iterator();
      while (((Iterator)localObject1).hasNext()) {
        if (!((RoamingPartner)((Iterator)localObject1).next()).validate()) {
          return false;
        }
      }
    }
    return true;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mMinHomeDownlinkBandwidth);
    paramParcel.writeLong(mMinHomeUplinkBandwidth);
    paramParcel.writeLong(mMinRoamingDownlinkBandwidth);
    paramParcel.writeLong(mMinRoamingUplinkBandwidth);
    paramParcel.writeStringArray(mExcludedSsidList);
    writeProtoPortMap(paramParcel, mRequiredProtoPortMap);
    paramParcel.writeInt(mMaximumBssLoadValue);
    writeRoamingPartnerList(paramParcel, paramInt, mPreferredRoamingPartnerList);
    paramParcel.writeParcelable(mPolicyUpdate, paramInt);
  }
  
  public static final class RoamingPartner
    implements Parcelable
  {
    public static final Parcelable.Creator<RoamingPartner> CREATOR = new Parcelable.Creator()
    {
      public Policy.RoamingPartner createFromParcel(Parcel paramAnonymousParcel)
      {
        Policy.RoamingPartner localRoamingPartner = new Policy.RoamingPartner();
        localRoamingPartner.setFqdn(paramAnonymousParcel.readString());
        boolean bool;
        if (paramAnonymousParcel.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        localRoamingPartner.setFqdnExactMatch(bool);
        localRoamingPartner.setPriority(paramAnonymousParcel.readInt());
        localRoamingPartner.setCountries(paramAnonymousParcel.readString());
        return localRoamingPartner;
      }
      
      public Policy.RoamingPartner[] newArray(int paramAnonymousInt)
      {
        return new Policy.RoamingPartner[paramAnonymousInt];
      }
    };
    private String mCountries = null;
    private String mFqdn = null;
    private boolean mFqdnExactMatch = false;
    private int mPriority = Integer.MIN_VALUE;
    
    public RoamingPartner() {}
    
    public RoamingPartner(RoamingPartner paramRoamingPartner)
    {
      if (paramRoamingPartner != null)
      {
        mFqdn = mFqdn;
        mFqdnExactMatch = mFqdnExactMatch;
        mPriority = mPriority;
        mCountries = mCountries;
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof RoamingPartner)) {
        return false;
      }
      paramObject = (RoamingPartner)paramObject;
      if ((!TextUtils.equals(mFqdn, mFqdn)) || (mFqdnExactMatch != mFqdnExactMatch) || (mPriority != mPriority) || (!TextUtils.equals(mCountries, mCountries))) {
        bool = false;
      }
      return bool;
    }
    
    public String getCountries()
    {
      return mCountries;
    }
    
    public String getFqdn()
    {
      return mFqdn;
    }
    
    public boolean getFqdnExactMatch()
    {
      return mFqdnExactMatch;
    }
    
    public int getPriority()
    {
      return mPriority;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mFqdn, Boolean.valueOf(mFqdnExactMatch), Integer.valueOf(mPriority), mCountries });
    }
    
    public void setCountries(String paramString)
    {
      mCountries = paramString;
    }
    
    public void setFqdn(String paramString)
    {
      mFqdn = paramString;
    }
    
    public void setFqdnExactMatch(boolean paramBoolean)
    {
      mFqdnExactMatch = paramBoolean;
    }
    
    public void setPriority(int paramInt)
    {
      mPriority = paramInt;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("FQDN: ");
      localStringBuilder.append(mFqdn);
      localStringBuilder.append("\n");
      localStringBuilder.append("ExactMatch: ");
      localStringBuilder.append("mFqdnExactMatch");
      localStringBuilder.append("\n");
      localStringBuilder.append("Priority: ");
      localStringBuilder.append(mPriority);
      localStringBuilder.append("\n");
      localStringBuilder.append("Countries: ");
      localStringBuilder.append(mCountries);
      localStringBuilder.append("\n");
      return localStringBuilder.toString();
    }
    
    public boolean validate()
    {
      if (TextUtils.isEmpty(mFqdn))
      {
        Log.d("Policy", "Missing FQDN");
        return false;
      }
      if (TextUtils.isEmpty(mCountries))
      {
        Log.d("Policy", "Missing countries");
        return false;
      }
      return true;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mFqdn);
      paramParcel.writeInt(mFqdnExactMatch);
      paramParcel.writeInt(mPriority);
      paramParcel.writeString(mCountries);
    }
  }
}
