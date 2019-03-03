package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

public final class SharedLibraryInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SharedLibraryInfo> CREATOR = new Parcelable.Creator()
  {
    public SharedLibraryInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SharedLibraryInfo(paramAnonymousParcel, null);
    }
    
    public SharedLibraryInfo[] newArray(int paramAnonymousInt)
    {
      return new SharedLibraryInfo[paramAnonymousInt];
    }
  };
  public static final int TYPE_BUILTIN = 0;
  public static final int TYPE_DYNAMIC = 1;
  public static final int TYPE_STATIC = 2;
  public static final int VERSION_UNDEFINED = -1;
  private final VersionedPackage mDeclaringPackage;
  private final List<VersionedPackage> mDependentPackages;
  private final String mName;
  private final int mType;
  private final long mVersion;
  
  private SharedLibraryInfo(Parcel paramParcel)
  {
    this(paramParcel.readString(), paramParcel.readLong(), paramParcel.readInt(), (VersionedPackage)paramParcel.readParcelable(null), paramParcel.readArrayList(null));
  }
  
  public SharedLibraryInfo(String paramString, long paramLong, int paramInt, VersionedPackage paramVersionedPackage, List<VersionedPackage> paramList)
  {
    mName = paramString;
    mVersion = paramLong;
    mType = paramInt;
    mDeclaringPackage = paramVersionedPackage;
    mDependentPackages = paramList;
  }
  
  private static String typeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 2: 
      return "static";
    case 1: 
      return "dynamic";
    }
    return "builtin";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public VersionedPackage getDeclaringPackage()
  {
    return mDeclaringPackage;
  }
  
  public List<VersionedPackage> getDependentPackages()
  {
    if (mDependentPackages == null) {
      return Collections.emptyList();
    }
    return mDependentPackages;
  }
  
  public long getLongVersion()
  {
    return mVersion;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getType()
  {
    return mType;
  }
  
  @Deprecated
  public int getVersion()
  {
    if (mVersion < 0L) {}
    int i;
    for (long l = mVersion;; l = mVersion & 0x7FFFFFFF)
    {
      i = (int)l;
      break;
    }
    return i;
  }
  
  public boolean isBuiltin()
  {
    boolean bool;
    if (mType == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDynamic()
  {
    int i = mType;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStatic()
  {
    boolean bool;
    if (mType == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SharedLibraryInfo[name:");
    localStringBuilder.append(mName);
    localStringBuilder.append(", type:");
    localStringBuilder.append(typeToString(mType));
    localStringBuilder.append(", version:");
    localStringBuilder.append(mVersion);
    String str;
    if (!getDependentPackages().isEmpty()) {
      str = " has dependents";
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeLong(mVersion);
    paramParcel.writeInt(mType);
    paramParcel.writeParcelable(mDeclaringPackage, paramInt);
    paramParcel.writeList(mDependentPackages);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface Type {}
}
