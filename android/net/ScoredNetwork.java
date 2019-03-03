package android.net;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@SystemApi
public class ScoredNetwork
  implements Parcelable
{
  public static final String ATTRIBUTES_KEY_BADGING_CURVE = "android.net.attributes.key.BADGING_CURVE";
  public static final String ATTRIBUTES_KEY_HAS_CAPTIVE_PORTAL = "android.net.attributes.key.HAS_CAPTIVE_PORTAL";
  public static final String ATTRIBUTES_KEY_RANKING_SCORE_OFFSET = "android.net.attributes.key.RANKING_SCORE_OFFSET";
  public static final Parcelable.Creator<ScoredNetwork> CREATOR = new Parcelable.Creator()
  {
    public ScoredNetwork createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ScoredNetwork(paramAnonymousParcel, null);
    }
    
    public ScoredNetwork[] newArray(int paramAnonymousInt)
    {
      return new ScoredNetwork[paramAnonymousInt];
    }
  };
  public final Bundle attributes;
  public final boolean meteredHint;
  public final NetworkKey networkKey;
  public final RssiCurve rssiCurve;
  
  public ScoredNetwork(NetworkKey paramNetworkKey, RssiCurve paramRssiCurve)
  {
    this(paramNetworkKey, paramRssiCurve, false);
  }
  
  public ScoredNetwork(NetworkKey paramNetworkKey, RssiCurve paramRssiCurve, boolean paramBoolean)
  {
    this(paramNetworkKey, paramRssiCurve, paramBoolean, null);
  }
  
  public ScoredNetwork(NetworkKey paramNetworkKey, RssiCurve paramRssiCurve, boolean paramBoolean, Bundle paramBundle)
  {
    networkKey = paramNetworkKey;
    rssiCurve = paramRssiCurve;
    meteredHint = paramBoolean;
    attributes = paramBundle;
  }
  
  private ScoredNetwork(Parcel paramParcel)
  {
    networkKey = ((NetworkKey)NetworkKey.CREATOR.createFromParcel(paramParcel));
    int i = paramParcel.readByte();
    boolean bool = true;
    if (i == 1) {
      rssiCurve = ((RssiCurve)RssiCurve.CREATOR.createFromParcel(paramParcel));
    } else {
      rssiCurve = null;
    }
    if (paramParcel.readByte() != 1) {
      bool = false;
    }
    meteredHint = bool;
    attributes = paramParcel.readBundle();
  }
  
  private boolean bundleEquals(Bundle paramBundle1, Bundle paramBundle2)
  {
    if (paramBundle1 == paramBundle2) {
      return true;
    }
    if ((paramBundle1 != null) && (paramBundle2 != null))
    {
      if (paramBundle1.size() != paramBundle2.size()) {
        return false;
      }
      Iterator localIterator = paramBundle1.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (!Objects.equals(paramBundle1.get(str), paramBundle2.get(str))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  public int calculateBadge(int paramInt)
  {
    if ((attributes != null) && (attributes.containsKey("android.net.attributes.key.BADGING_CURVE"))) {
      return ((RssiCurve)attributes.getParcelable("android.net.attributes.key.BADGING_CURVE")).lookupScore(paramInt);
    }
    return 0;
  }
  
  public int calculateRankingScore(int paramInt)
    throws UnsupportedOperationException
  {
    if (hasRankingScore())
    {
      int i = 0;
      Bundle localBundle = attributes;
      int j = 0;
      if (localBundle != null) {
        i = 0 + attributes.getInt("android.net.attributes.key.RANKING_SCORE_OFFSET", 0);
      }
      if (rssiCurve == null) {
        paramInt = j;
      } else {
        paramInt = rssiCurve.lookupScore(paramInt) << 8;
      }
      try
      {
        i = Math.addExact(paramInt, i);
        return i;
      }
      catch (ArithmeticException localArithmeticException)
      {
        if (paramInt < 0) {
          paramInt = Integer.MIN_VALUE;
        } else {
          paramInt = Integer.MAX_VALUE;
        }
        return paramInt;
      }
    }
    throw new UnsupportedOperationException("Either rssiCurve or rankingScoreOffset is required to calculate the ranking score");
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
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ScoredNetwork)paramObject;
      if ((!Objects.equals(networkKey, networkKey)) || (!Objects.equals(rssiCurve, rssiCurve)) || (!Objects.equals(Boolean.valueOf(meteredHint), Boolean.valueOf(meteredHint))) || (!bundleEquals(attributes, attributes))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public boolean hasRankingScore()
  {
    boolean bool;
    if ((rssiCurve == null) && ((attributes == null) || (!attributes.containsKey("android.net.attributes.key.RANKING_SCORE_OFFSET")))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { networkKey, rssiCurve, Boolean.valueOf(meteredHint), attributes });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("ScoredNetwork{networkKey=");
    localStringBuilder1.append(networkKey);
    localStringBuilder1.append(", rssiCurve=");
    localStringBuilder1.append(rssiCurve);
    localStringBuilder1.append(", meteredHint=");
    localStringBuilder1.append(meteredHint);
    StringBuilder localStringBuilder2 = new StringBuilder(localStringBuilder1.toString());
    if ((attributes != null) && (!attributes.isEmpty()))
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append(", attributes=");
      localStringBuilder1.append(attributes);
      localStringBuilder2.append(localStringBuilder1.toString());
    }
    localStringBuilder2.append('}');
    return localStringBuilder2.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    networkKey.writeToParcel(paramParcel, paramInt);
    if (rssiCurve != null)
    {
      paramParcel.writeByte((byte)1);
      rssiCurve.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeByte((byte)meteredHint);
    paramParcel.writeBundle(attributes);
  }
}
