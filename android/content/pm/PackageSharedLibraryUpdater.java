package android.content.pm;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;

@VisibleForTesting
public abstract class PackageSharedLibraryUpdater
{
  public PackageSharedLibraryUpdater() {}
  
  static boolean apkTargetsApiLevelLessThanOrEqualToOMR1(PackageParser.Package paramPackage)
  {
    boolean bool;
    if (applicationInfo.targetSdkVersion < 28) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isLibraryPresent(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2, String paramString)
  {
    boolean bool;
    if ((!ArrayUtils.contains(paramArrayList1, paramString)) && (!ArrayUtils.contains(paramArrayList2, paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static <T> ArrayList<T> prefix(ArrayList<T> paramArrayList, T paramT)
  {
    Object localObject = paramArrayList;
    if (paramArrayList == null) {
      localObject = new ArrayList();
    }
    ((ArrayList)localObject).add(0, paramT);
    return localObject;
  }
  
  static void removeLibrary(PackageParser.Package paramPackage, String paramString)
  {
    usesLibraries = ArrayUtils.remove(usesLibraries, paramString);
    usesOptionalLibraries = ArrayUtils.remove(usesOptionalLibraries, paramString);
  }
  
  void prefixImplicitDependency(PackageParser.Package paramPackage, String paramString1, String paramString2)
  {
    ArrayList localArrayList1 = usesLibraries;
    ArrayList localArrayList2 = usesOptionalLibraries;
    if (!isLibraryPresent(localArrayList1, localArrayList2, paramString2))
    {
      if (ArrayUtils.contains(localArrayList1, paramString1)) {
        prefix(localArrayList1, paramString2);
      } else if (ArrayUtils.contains(localArrayList2, paramString1)) {
        prefix(localArrayList2, paramString2);
      }
      usesLibraries = localArrayList1;
      usesOptionalLibraries = localArrayList2;
    }
  }
  
  void prefixRequiredLibrary(PackageParser.Package paramPackage, String paramString)
  {
    ArrayList localArrayList = usesLibraries;
    if (!isLibraryPresent(localArrayList, usesOptionalLibraries, paramString)) {
      usesLibraries = prefix(localArrayList, paramString);
    }
  }
  
  public abstract void updatePackage(PackageParser.Package paramPackage);
}
