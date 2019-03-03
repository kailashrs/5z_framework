package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public final class HomeSp
  implements Parcelable
{
  public static final Parcelable.Creator<HomeSp> CREATOR = new Parcelable.Creator()
  {
    private Map<String, Long> readHomeNetworkIds(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == -1) {
        return null;
      }
      HashMap localHashMap = new HashMap(i);
      for (int j = 0; j < i; j++)
      {
        String str = paramAnonymousParcel.readString();
        Long localLong = null;
        long l = paramAnonymousParcel.readLong();
        if (l != -1L) {
          localLong = Long.valueOf(l);
        }
        localHashMap.put(str, localLong);
      }
      return localHashMap;
    }
    
    public HomeSp createFromParcel(Parcel paramAnonymousParcel)
    {
      HomeSp localHomeSp = new HomeSp();
      localHomeSp.setFqdn(paramAnonymousParcel.readString());
      localHomeSp.setFriendlyName(paramAnonymousParcel.readString());
      localHomeSp.setIconUrl(paramAnonymousParcel.readString());
      localHomeSp.setHomeNetworkIds(readHomeNetworkIds(paramAnonymousParcel));
      localHomeSp.setMatchAllOis(paramAnonymousParcel.createLongArray());
      localHomeSp.setMatchAnyOis(paramAnonymousParcel.createLongArray());
      localHomeSp.setOtherHomePartners(paramAnonymousParcel.createStringArray());
      localHomeSp.setRoamingConsortiumOis(paramAnonymousParcel.createLongArray());
      return localHomeSp;
    }
    
    public HomeSp[] newArray(int paramAnonymousInt)
    {
      return new HomeSp[paramAnonymousInt];
    }
  };
  private static final int MAX_SSID_BYTES = 32;
  private static final int NULL_VALUE = -1;
  private static final String TAG = "HomeSp";
  private String mFqdn = null;
  private String mFriendlyName = null;
  private Map<String, Long> mHomeNetworkIds = null;
  private String mIconUrl = null;
  private long[] mMatchAllOis = null;
  private long[] mMatchAnyOis = null;
  private String[] mOtherHomePartners = null;
  private long[] mRoamingConsortiumOis = null;
  
  public HomeSp() {}
  
  public HomeSp(HomeSp paramHomeSp)
  {
    if (paramHomeSp == null) {
      return;
    }
    mFqdn = mFqdn;
    mFriendlyName = mFriendlyName;
    mIconUrl = mIconUrl;
    if (mHomeNetworkIds != null) {
      mHomeNetworkIds = Collections.unmodifiableMap(mHomeNetworkIds);
    }
    if (mMatchAllOis != null) {
      mMatchAllOis = Arrays.copyOf(mMatchAllOis, mMatchAllOis.length);
    }
    if (mMatchAnyOis != null) {
      mMatchAnyOis = Arrays.copyOf(mMatchAnyOis, mMatchAnyOis.length);
    }
    if (mOtherHomePartners != null) {
      mOtherHomePartners = ((String[])Arrays.copyOf(mOtherHomePartners, mOtherHomePartners.length));
    }
    if (mRoamingConsortiumOis != null) {
      mRoamingConsortiumOis = Arrays.copyOf(mRoamingConsortiumOis, mRoamingConsortiumOis.length);
    }
  }
  
  private static void writeHomeNetworkIds(Parcel paramParcel, Map<String, Long> paramMap)
  {
    if (paramMap == null)
    {
      paramParcel.writeInt(-1);
      return;
    }
    paramParcel.writeInt(paramMap.size());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramMap = (Map.Entry)localIterator.next();
      paramParcel.writeString((String)paramMap.getKey());
      if (paramMap.getValue() == null) {
        paramParcel.writeLong(-1L);
      } else {
        paramParcel.writeLong(((Long)paramMap.getValue()).longValue());
      }
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
    if (!(paramObject instanceof HomeSp)) {
      return false;
    }
    paramObject = (HomeSp)paramObject;
    if ((!TextUtils.equals(mFqdn, mFqdn)) || (!TextUtils.equals(mFriendlyName, mFriendlyName)) || (!TextUtils.equals(mIconUrl, mIconUrl)) || (mHomeNetworkIds == null ? mHomeNetworkIds != null : !mHomeNetworkIds.equals(mHomeNetworkIds)) || (!Arrays.equals(mMatchAllOis, mMatchAllOis)) || (!Arrays.equals(mMatchAnyOis, mMatchAnyOis)) || (!Arrays.equals(mOtherHomePartners, mOtherHomePartners)) || (!Arrays.equals(mRoamingConsortiumOis, mRoamingConsortiumOis))) {
      bool = false;
    }
    return bool;
  }
  
  public String getFqdn()
  {
    return mFqdn;
  }
  
  public String getFriendlyName()
  {
    return mFriendlyName;
  }
  
  public Map<String, Long> getHomeNetworkIds()
  {
    return mHomeNetworkIds;
  }
  
  public String getIconUrl()
  {
    return mIconUrl;
  }
  
  public long[] getMatchAllOis()
  {
    return mMatchAllOis;
  }
  
  public long[] getMatchAnyOis()
  {
    return mMatchAnyOis;
  }
  
  public String[] getOtherHomePartners()
  {
    return mOtherHomePartners;
  }
  
  public long[] getRoamingConsortiumOis()
  {
    return mRoamingConsortiumOis;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mFqdn, mFriendlyName, mIconUrl, mHomeNetworkIds, mMatchAllOis, mMatchAnyOis, mOtherHomePartners, mRoamingConsortiumOis });
  }
  
  public void setFqdn(String paramString)
  {
    mFqdn = paramString;
  }
  
  public void setFriendlyName(String paramString)
  {
    mFriendlyName = paramString;
  }
  
  public void setHomeNetworkIds(Map<String, Long> paramMap)
  {
    mHomeNetworkIds = paramMap;
  }
  
  public void setIconUrl(String paramString)
  {
    mIconUrl = paramString;
  }
  
  public void setMatchAllOis(long[] paramArrayOfLong)
  {
    mMatchAllOis = paramArrayOfLong;
  }
  
  public void setMatchAnyOis(long[] paramArrayOfLong)
  {
    mMatchAnyOis = paramArrayOfLong;
  }
  
  public void setOtherHomePartners(String[] paramArrayOfString)
  {
    mOtherHomePartners = paramArrayOfString;
  }
  
  public void setRoamingConsortiumOis(long[] paramArrayOfLong)
  {
    mRoamingConsortiumOis = paramArrayOfLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FQDN: ");
    localStringBuilder.append(mFqdn);
    localStringBuilder.append("\n");
    localStringBuilder.append("FriendlyName: ");
    localStringBuilder.append(mFriendlyName);
    localStringBuilder.append("\n");
    localStringBuilder.append("IconURL: ");
    localStringBuilder.append(mIconUrl);
    localStringBuilder.append("\n");
    localStringBuilder.append("HomeNetworkIDs: ");
    localStringBuilder.append(mHomeNetworkIds);
    localStringBuilder.append("\n");
    localStringBuilder.append("MatchAllOIs: ");
    localStringBuilder.append(mMatchAllOis);
    localStringBuilder.append("\n");
    localStringBuilder.append("MatchAnyOIs: ");
    localStringBuilder.append(mMatchAnyOis);
    localStringBuilder.append("\n");
    localStringBuilder.append("OtherHomePartners: ");
    localStringBuilder.append(mOtherHomePartners);
    localStringBuilder.append("\n");
    localStringBuilder.append("RoamingConsortiumOIs: ");
    localStringBuilder.append(mRoamingConsortiumOis);
    localStringBuilder.append("\n");
    return localStringBuilder.toString();
  }
  
  public boolean validate()
  {
    if (TextUtils.isEmpty(mFqdn))
    {
      Log.d("HomeSp", "Missing FQDN");
      return false;
    }
    if (TextUtils.isEmpty(mFriendlyName))
    {
      Log.d("HomeSp", "Missing friendly name");
      return false;
    }
    if (mHomeNetworkIds != null)
    {
      Iterator localIterator = mHomeNetworkIds.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if ((localEntry.getKey() != null) && (((String)localEntry.getKey()).getBytes(StandardCharsets.UTF_8).length <= 32)) {}
        Log.d("HomeSp", "Invalid SSID in HomeNetworkIDs");
        return false;
      }
    }
    return true;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mFqdn);
    paramParcel.writeString(mFriendlyName);
    paramParcel.writeString(mIconUrl);
    writeHomeNetworkIds(paramParcel, mHomeNetworkIds);
    paramParcel.writeLongArray(mMatchAllOis);
    paramParcel.writeLongArray(mMatchAnyOis);
    paramParcel.writeStringArray(mOtherHomePartners);
    paramParcel.writeLongArray(mRoamingConsortiumOis);
  }
}
