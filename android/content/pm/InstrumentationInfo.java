package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;

public class InstrumentationInfo
  extends PackageItemInfo
  implements Parcelable
{
  public static final Parcelable.Creator<InstrumentationInfo> CREATOR = new Parcelable.Creator()
  {
    public InstrumentationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InstrumentationInfo(paramAnonymousParcel, null);
    }
    
    public InstrumentationInfo[] newArray(int paramAnonymousInt)
    {
      return new InstrumentationInfo[paramAnonymousInt];
    }
  };
  public String credentialProtectedDataDir;
  public String dataDir;
  public String deviceProtectedDataDir;
  public boolean functionalTest;
  public boolean handleProfiling;
  public String nativeLibraryDir;
  public String primaryCpuAbi;
  public String publicSourceDir;
  public String secondaryCpuAbi;
  public String secondaryNativeLibraryDir;
  public String sourceDir;
  public SparseArray<int[]> splitDependencies;
  public String[] splitNames;
  public String[] splitPublicSourceDirs;
  public String[] splitSourceDirs;
  public String targetPackage;
  public String targetProcesses;
  
  public InstrumentationInfo() {}
  
  public InstrumentationInfo(InstrumentationInfo paramInstrumentationInfo)
  {
    super(paramInstrumentationInfo);
    targetPackage = targetPackage;
    targetProcesses = targetProcesses;
    sourceDir = sourceDir;
    publicSourceDir = publicSourceDir;
    splitNames = splitNames;
    splitSourceDirs = splitSourceDirs;
    splitPublicSourceDirs = splitPublicSourceDirs;
    splitDependencies = splitDependencies;
    dataDir = dataDir;
    deviceProtectedDataDir = deviceProtectedDataDir;
    credentialProtectedDataDir = credentialProtectedDataDir;
    primaryCpuAbi = primaryCpuAbi;
    secondaryCpuAbi = secondaryCpuAbi;
    nativeLibraryDir = nativeLibraryDir;
    secondaryNativeLibraryDir = secondaryNativeLibraryDir;
    handleProfiling = handleProfiling;
    functionalTest = functionalTest;
  }
  
  private InstrumentationInfo(Parcel paramParcel)
  {
    super(paramParcel);
    targetPackage = paramParcel.readString();
    targetProcesses = paramParcel.readString();
    sourceDir = paramParcel.readString();
    publicSourceDir = paramParcel.readString();
    splitNames = paramParcel.readStringArray();
    splitSourceDirs = paramParcel.readStringArray();
    splitPublicSourceDirs = paramParcel.readStringArray();
    splitDependencies = paramParcel.readSparseArray(null);
    dataDir = paramParcel.readString();
    deviceProtectedDataDir = paramParcel.readString();
    credentialProtectedDataDir = paramParcel.readString();
    primaryCpuAbi = paramParcel.readString();
    secondaryCpuAbi = paramParcel.readString();
    nativeLibraryDir = paramParcel.readString();
    secondaryNativeLibraryDir = paramParcel.readString();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    handleProfiling = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    functionalTest = bool2;
  }
  
  public void copyTo(ApplicationInfo paramApplicationInfo)
  {
    packageName = packageName;
    sourceDir = sourceDir;
    publicSourceDir = publicSourceDir;
    splitNames = splitNames;
    splitSourceDirs = splitSourceDirs;
    splitPublicSourceDirs = splitPublicSourceDirs;
    splitDependencies = splitDependencies;
    dataDir = dataDir;
    deviceProtectedDataDir = deviceProtectedDataDir;
    credentialProtectedDataDir = credentialProtectedDataDir;
    primaryCpuAbi = primaryCpuAbi;
    secondaryCpuAbi = secondaryCpuAbi;
    nativeLibraryDir = nativeLibraryDir;
    secondaryNativeLibraryDir = secondaryNativeLibraryDir;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("InstrumentationInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(packageName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(targetPackage);
    paramParcel.writeString(targetProcesses);
    paramParcel.writeString(sourceDir);
    paramParcel.writeString(publicSourceDir);
    paramParcel.writeStringArray(splitNames);
    paramParcel.writeStringArray(splitSourceDirs);
    paramParcel.writeStringArray(splitPublicSourceDirs);
    paramParcel.writeSparseArray(splitDependencies);
    paramParcel.writeString(dataDir);
    paramParcel.writeString(deviceProtectedDataDir);
    paramParcel.writeString(credentialProtectedDataDir);
    paramParcel.writeString(primaryCpuAbi);
    paramParcel.writeString(secondaryCpuAbi);
    paramParcel.writeString(nativeLibraryDir);
    paramParcel.writeString(secondaryNativeLibraryDir);
    paramParcel.writeInt(handleProfiling);
    paramParcel.writeInt(functionalTest);
  }
}
