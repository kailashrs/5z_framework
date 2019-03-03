package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.text.TextUtils;
import java.util.Objects;

@Deprecated
public class PackageStats
  implements Parcelable
{
  public static final Parcelable.Creator<PackageStats> CREATOR = new Parcelable.Creator()
  {
    public PackageStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PackageStats(paramAnonymousParcel);
    }
    
    public PackageStats[] newArray(int paramAnonymousInt)
    {
      return new PackageStats[paramAnonymousInt];
    }
  };
  public long cacheSize;
  public long codeSize;
  public long dataSize;
  public long externalCacheSize;
  public long externalCodeSize;
  public long externalDataSize;
  public long externalMediaSize;
  public long externalObbSize;
  public String packageName;
  public int userHandle;
  
  public PackageStats(PackageStats paramPackageStats)
  {
    packageName = packageName;
    userHandle = userHandle;
    codeSize = codeSize;
    dataSize = dataSize;
    cacheSize = cacheSize;
    externalCodeSize = externalCodeSize;
    externalDataSize = externalDataSize;
    externalCacheSize = externalCacheSize;
    externalMediaSize = externalMediaSize;
    externalObbSize = externalObbSize;
  }
  
  public PackageStats(Parcel paramParcel)
  {
    packageName = paramParcel.readString();
    userHandle = paramParcel.readInt();
    codeSize = paramParcel.readLong();
    dataSize = paramParcel.readLong();
    cacheSize = paramParcel.readLong();
    externalCodeSize = paramParcel.readLong();
    externalDataSize = paramParcel.readLong();
    externalCacheSize = paramParcel.readLong();
    externalMediaSize = paramParcel.readLong();
    externalObbSize = paramParcel.readLong();
  }
  
  public PackageStats(String paramString)
  {
    packageName = paramString;
    userHandle = UserHandle.myUserId();
  }
  
  public PackageStats(String paramString, int paramInt)
  {
    packageName = paramString;
    userHandle = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof PackageStats;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (PackageStats)paramObject;
    bool1 = bool2;
    if (TextUtils.equals(packageName, packageName))
    {
      bool1 = bool2;
      if (userHandle == userHandle)
      {
        bool1 = bool2;
        if (codeSize == codeSize)
        {
          bool1 = bool2;
          if (dataSize == dataSize)
          {
            bool1 = bool2;
            if (cacheSize == cacheSize)
            {
              bool1 = bool2;
              if (externalCodeSize == externalCodeSize)
              {
                bool1 = bool2;
                if (externalDataSize == externalDataSize)
                {
                  bool1 = bool2;
                  if (externalCacheSize == externalCacheSize)
                  {
                    bool1 = bool2;
                    if (externalMediaSize == externalMediaSize)
                    {
                      bool1 = bool2;
                      if (externalObbSize == externalObbSize) {
                        bool1 = true;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { packageName, Integer.valueOf(userHandle), Long.valueOf(codeSize), Long.valueOf(dataSize), Long.valueOf(cacheSize), Long.valueOf(externalCodeSize), Long.valueOf(externalDataSize), Long.valueOf(externalCacheSize), Long.valueOf(externalMediaSize), Long.valueOf(externalObbSize) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("PackageStats{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(packageName);
    if (codeSize != 0L)
    {
      localStringBuilder.append(" code=");
      localStringBuilder.append(codeSize);
    }
    if (dataSize != 0L)
    {
      localStringBuilder.append(" data=");
      localStringBuilder.append(dataSize);
    }
    if (cacheSize != 0L)
    {
      localStringBuilder.append(" cache=");
      localStringBuilder.append(cacheSize);
    }
    if (externalCodeSize != 0L)
    {
      localStringBuilder.append(" extCode=");
      localStringBuilder.append(externalCodeSize);
    }
    if (externalDataSize != 0L)
    {
      localStringBuilder.append(" extData=");
      localStringBuilder.append(externalDataSize);
    }
    if (externalCacheSize != 0L)
    {
      localStringBuilder.append(" extCache=");
      localStringBuilder.append(externalCacheSize);
    }
    if (externalMediaSize != 0L)
    {
      localStringBuilder.append(" media=");
      localStringBuilder.append(externalMediaSize);
    }
    if (externalObbSize != 0L)
    {
      localStringBuilder.append(" obb=");
      localStringBuilder.append(externalObbSize);
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(packageName);
    paramParcel.writeInt(userHandle);
    paramParcel.writeLong(codeSize);
    paramParcel.writeLong(dataSize);
    paramParcel.writeLong(cacheSize);
    paramParcel.writeLong(externalCodeSize);
    paramParcel.writeLong(externalDataSize);
    paramParcel.writeLong(externalCacheSize);
    paramParcel.writeLong(externalMediaSize);
    paramParcel.writeLong(externalObbSize);
  }
}
