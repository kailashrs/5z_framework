package android.location;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

public class Address
  implements Parcelable
{
  public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator()
  {
    public Address createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject1 = paramAnonymousParcel.readString();
      Object localObject2 = paramAnonymousParcel.readString();
      if (((String)localObject2).length() > 0) {
        localObject1 = new Locale((String)localObject1, (String)localObject2);
      } else {
        localObject1 = new Locale((String)localObject1);
      }
      localObject2 = new Address((Locale)localObject1);
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      if (i > 0)
      {
        Address.access$002((Address)localObject2, new HashMap(i));
        for (int j = 0; j < i; j++)
        {
          int k = paramAnonymousParcel.readInt();
          localObject1 = paramAnonymousParcel.readString();
          mAddressLines.put(Integer.valueOf(k), localObject1);
          Address.access$102((Address)localObject2, Math.max(mMaxAddressLineIndex, k));
        }
      }
      Address.access$002((Address)localObject2, null);
      Address.access$102((Address)localObject2, -1);
      Address.access$202((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$302((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$402((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$502((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$602((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$702((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$802((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$902((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$1002((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$1102((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$1202((Address)localObject2, paramAnonymousParcel.readString());
      boolean bool2;
      if (paramAnonymousParcel.readInt() == 0) {
        bool2 = false;
      } else {
        bool2 = true;
      }
      Address.access$1302((Address)localObject2, bool2);
      if (mHasLatitude) {
        Address.access$1402((Address)localObject2, paramAnonymousParcel.readDouble());
      }
      if (paramAnonymousParcel.readInt() == 0) {
        bool2 = bool1;
      } else {
        bool2 = true;
      }
      Address.access$1502((Address)localObject2, bool2);
      if (mHasLongitude) {
        Address.access$1602((Address)localObject2, paramAnonymousParcel.readDouble());
      }
      Address.access$1702((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$1802((Address)localObject2, paramAnonymousParcel.readString());
      Address.access$1902((Address)localObject2, paramAnonymousParcel.readBundle());
      return localObject2;
    }
    
    public Address[] newArray(int paramAnonymousInt)
    {
      return new Address[paramAnonymousInt];
    }
  };
  private HashMap<Integer, String> mAddressLines;
  private String mAdminArea;
  private String mCountryCode;
  private String mCountryName;
  private Bundle mExtras = null;
  private String mFeatureName;
  private boolean mHasLatitude = false;
  private boolean mHasLongitude = false;
  private double mLatitude;
  private Locale mLocale;
  private String mLocality;
  private double mLongitude;
  private int mMaxAddressLineIndex = -1;
  private String mPhone;
  private String mPostalCode;
  private String mPremises;
  private String mSubAdminArea;
  private String mSubLocality;
  private String mSubThoroughfare;
  private String mThoroughfare;
  private String mUrl;
  
  public Address(Locale paramLocale)
  {
    mLocale = paramLocale;
  }
  
  public void clearLatitude()
  {
    mHasLatitude = false;
  }
  
  public void clearLongitude()
  {
    mHasLongitude = false;
  }
  
  public int describeContents()
  {
    int i;
    if (mExtras != null) {
      i = mExtras.describeContents();
    } else {
      i = 0;
    }
    return i;
  }
  
  public String getAddressLine(int paramInt)
  {
    if (paramInt >= 0)
    {
      if (mAddressLines == null) {
        localObject = null;
      } else {
        localObject = (String)mAddressLines.get(Integer.valueOf(paramInt));
      }
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("index = ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" < 0");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public String getAdminArea()
  {
    return mAdminArea;
  }
  
  public String getCountryCode()
  {
    return mCountryCode;
  }
  
  public String getCountryName()
  {
    return mCountryName;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public String getFeatureName()
  {
    return mFeatureName;
  }
  
  public double getLatitude()
  {
    if (mHasLatitude) {
      return mLatitude;
    }
    throw new IllegalStateException();
  }
  
  public Locale getLocale()
  {
    return mLocale;
  }
  
  public String getLocality()
  {
    return mLocality;
  }
  
  public double getLongitude()
  {
    if (mHasLongitude) {
      return mLongitude;
    }
    throw new IllegalStateException();
  }
  
  public int getMaxAddressLineIndex()
  {
    return mMaxAddressLineIndex;
  }
  
  public String getPhone()
  {
    return mPhone;
  }
  
  public String getPostalCode()
  {
    return mPostalCode;
  }
  
  public String getPremises()
  {
    return mPremises;
  }
  
  public String getSubAdminArea()
  {
    return mSubAdminArea;
  }
  
  public String getSubLocality()
  {
    return mSubLocality;
  }
  
  public String getSubThoroughfare()
  {
    return mSubThoroughfare;
  }
  
  public String getThoroughfare()
  {
    return mThoroughfare;
  }
  
  public String getUrl()
  {
    return mUrl;
  }
  
  public boolean hasLatitude()
  {
    return mHasLatitude;
  }
  
  public boolean hasLongitude()
  {
    return mHasLongitude;
  }
  
  public void setAddressLine(int paramInt, String paramString)
  {
    if (paramInt >= 0)
    {
      if (mAddressLines == null) {
        mAddressLines = new HashMap();
      }
      mAddressLines.put(Integer.valueOf(paramInt), paramString);
      if (paramString == null)
      {
        mMaxAddressLineIndex = -1;
        paramString = mAddressLines.keySet().iterator();
        while (paramString.hasNext())
        {
          Integer localInteger = (Integer)paramString.next();
          mMaxAddressLineIndex = Math.max(mMaxAddressLineIndex, localInteger.intValue());
        }
      }
      mMaxAddressLineIndex = Math.max(mMaxAddressLineIndex, paramInt);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("index = ");
    paramString.append(paramInt);
    paramString.append(" < 0");
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public void setAdminArea(String paramString)
  {
    mAdminArea = paramString;
  }
  
  public void setCountryCode(String paramString)
  {
    mCountryCode = paramString;
  }
  
  public void setCountryName(String paramString)
  {
    mCountryName = paramString;
  }
  
  public void setExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      paramBundle = null;
    } else {
      paramBundle = new Bundle(paramBundle);
    }
    mExtras = paramBundle;
  }
  
  public void setFeatureName(String paramString)
  {
    mFeatureName = paramString;
  }
  
  public void setLatitude(double paramDouble)
  {
    mLatitude = paramDouble;
    mHasLatitude = true;
  }
  
  public void setLocality(String paramString)
  {
    mLocality = paramString;
  }
  
  public void setLongitude(double paramDouble)
  {
    mLongitude = paramDouble;
    mHasLongitude = true;
  }
  
  public void setPhone(String paramString)
  {
    mPhone = paramString;
  }
  
  public void setPostalCode(String paramString)
  {
    mPostalCode = paramString;
  }
  
  public void setPremises(String paramString)
  {
    mPremises = paramString;
  }
  
  public void setSubAdminArea(String paramString)
  {
    mSubAdminArea = paramString;
  }
  
  public void setSubLocality(String paramString)
  {
    mSubLocality = paramString;
  }
  
  public void setSubThoroughfare(String paramString)
  {
    mSubThoroughfare = paramString;
  }
  
  public void setThoroughfare(String paramString)
  {
    mThoroughfare = paramString;
  }
  
  public void setUrl(String paramString)
  {
    mUrl = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Address[addressLines=[");
    for (int i = 0; i <= mMaxAddressLineIndex; i++)
    {
      if (i > 0) {
        localStringBuilder.append(',');
      }
      localStringBuilder.append(i);
      localStringBuilder.append(':');
      String str = (String)mAddressLines.get(Integer.valueOf(i));
      if (str == null)
      {
        localStringBuilder.append("null");
      }
      else
      {
        localStringBuilder.append('"');
        localStringBuilder.append(str);
        localStringBuilder.append('"');
      }
    }
    localStringBuilder.append(']');
    localStringBuilder.append(",feature=");
    localStringBuilder.append(mFeatureName);
    localStringBuilder.append(",admin=");
    localStringBuilder.append(mAdminArea);
    localStringBuilder.append(",sub-admin=");
    localStringBuilder.append(mSubAdminArea);
    localStringBuilder.append(",locality=");
    localStringBuilder.append(mLocality);
    localStringBuilder.append(",thoroughfare=");
    localStringBuilder.append(mThoroughfare);
    localStringBuilder.append(",postalCode=");
    localStringBuilder.append(mPostalCode);
    localStringBuilder.append(",countryCode=");
    localStringBuilder.append(mCountryCode);
    localStringBuilder.append(",countryName=");
    localStringBuilder.append(mCountryName);
    localStringBuilder.append(",hasLatitude=");
    localStringBuilder.append(mHasLatitude);
    localStringBuilder.append(",latitude=");
    localStringBuilder.append(mLatitude);
    localStringBuilder.append(",hasLongitude=");
    localStringBuilder.append(mHasLongitude);
    localStringBuilder.append(",longitude=");
    localStringBuilder.append(mLongitude);
    localStringBuilder.append(",phone=");
    localStringBuilder.append(mPhone);
    localStringBuilder.append(",url=");
    localStringBuilder.append(mUrl);
    localStringBuilder.append(",extras=");
    localStringBuilder.append(mExtras);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mLocale.getLanguage());
    paramParcel.writeString(mLocale.getCountry());
    if (mAddressLines == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      Object localObject = mAddressLines.entrySet();
      paramParcel.writeInt(((Set)localObject).size());
      Iterator localIterator = ((Set)localObject).iterator();
      while (localIterator.hasNext())
      {
        localObject = (Map.Entry)localIterator.next();
        paramParcel.writeInt(((Integer)((Map.Entry)localObject).getKey()).intValue());
        paramParcel.writeString((String)((Map.Entry)localObject).getValue());
      }
    }
    paramParcel.writeString(mFeatureName);
    paramParcel.writeString(mAdminArea);
    paramParcel.writeString(mSubAdminArea);
    paramParcel.writeString(mLocality);
    paramParcel.writeString(mSubLocality);
    paramParcel.writeString(mThoroughfare);
    paramParcel.writeString(mSubThoroughfare);
    paramParcel.writeString(mPremises);
    paramParcel.writeString(mPostalCode);
    paramParcel.writeString(mCountryCode);
    paramParcel.writeString(mCountryName);
    paramParcel.writeInt(mHasLatitude);
    if (mHasLatitude) {
      paramParcel.writeDouble(mLatitude);
    }
    paramParcel.writeInt(mHasLongitude);
    if (mHasLongitude) {
      paramParcel.writeDouble(mLongitude);
    }
    paramParcel.writeString(mPhone);
    paramParcel.writeString(mUrl);
    paramParcel.writeBundle(mExtras);
  }
}
