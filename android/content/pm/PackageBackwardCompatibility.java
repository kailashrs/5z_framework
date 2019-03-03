package android.content.pm;

import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@VisibleForTesting
public class PackageBackwardCompatibility
  extends PackageSharedLibraryUpdater
{
  private static final PackageBackwardCompatibility INSTANCE;
  private static final String TAG = PackageBackwardCompatibility.class.getSimpleName();
  private final boolean mBootClassPathContainsATB;
  private final boolean mBootClassPathContainsOAHL;
  private final PackageSharedLibraryUpdater[] mPackageUpdaters;
  
  static
  {
    ArrayList localArrayList = new ArrayList();
    boolean bool = addOptionalUpdater(localArrayList, "android.content.pm.OrgApacheHttpLegacyUpdater", _..Lambda.FMztmpMwSp3D3ge8Zxr31di8ZBg.INSTANCE);
    localArrayList.add(new AndroidTestRunnerSplitUpdater());
    INSTANCE = new PackageBackwardCompatibility(bool ^ true, addOptionalUpdater(localArrayList, "android.content.pm.AndroidTestBaseUpdater", _..Lambda.jpya2qgMDDEok2GAoKRDqPM5lIE.INSTANCE) ^ true, (PackageSharedLibraryUpdater[])localArrayList.toArray(new PackageSharedLibraryUpdater[0]));
  }
  
  public PackageBackwardCompatibility(boolean paramBoolean1, boolean paramBoolean2, PackageSharedLibraryUpdater[] paramArrayOfPackageSharedLibraryUpdater)
  {
    mBootClassPathContainsOAHL = paramBoolean1;
    mBootClassPathContainsATB = paramBoolean2;
    mPackageUpdaters = paramArrayOfPackageSharedLibraryUpdater;
  }
  
  private static boolean addOptionalUpdater(List<PackageSharedLibraryUpdater> paramList, String paramString, Supplier<PackageSharedLibraryUpdater> paramSupplier)
  {
    String str;
    try
    {
      Class localClass = PackageBackwardCompatibility.class.getClassLoader().loadClass(paramString).asSubclass(PackageSharedLibraryUpdater.class);
      localObject = TAG;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Loaded ");
      localStringBuilder.append(paramString);
      Log.i((String)localObject, localStringBuilder.toString());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      str = TAG;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Could not find ");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(", ignoring");
      Log.i(str, ((StringBuilder)localObject).toString());
      str = null;
    }
    boolean bool = false;
    if (str == null) {
      paramString = (PackageSharedLibraryUpdater)paramSupplier.get();
    }
    try
    {
      paramSupplier = (PackageSharedLibraryUpdater)str.getConstructor(new Class[0]).newInstance(new Object[0]);
      bool = true;
      paramString = paramSupplier;
      paramList.add(paramString);
      return bool;
    }
    catch (ReflectiveOperationException paramList)
    {
      paramSupplier = new StringBuilder();
      paramSupplier.append("Could not create instance of ");
      paramSupplier.append(paramString);
      throw new IllegalStateException(paramSupplier.toString(), paramList);
    }
  }
  
  @VisibleForTesting
  public static boolean bootClassPathContainsATB()
  {
    return INSTANCEmBootClassPathContainsATB;
  }
  
  @VisibleForTesting
  public static boolean bootClassPathContainsOAHL()
  {
    return INSTANCEmBootClassPathContainsOAHL;
  }
  
  @VisibleForTesting
  public static PackageSharedLibraryUpdater getInstance()
  {
    return INSTANCE;
  }
  
  @VisibleForTesting
  public static void modifySharedLibraries(PackageParser.Package paramPackage)
  {
    INSTANCE.updatePackage(paramPackage);
  }
  
  public void updatePackage(PackageParser.Package paramPackage)
  {
    PackageSharedLibraryUpdater[] arrayOfPackageSharedLibraryUpdater = mPackageUpdaters;
    int i = arrayOfPackageSharedLibraryUpdater.length;
    for (int j = 0; j < i; j++) {
      arrayOfPackageSharedLibraryUpdater[j].updatePackage(paramPackage);
    }
  }
  
  @VisibleForTesting
  public static class AndroidTestRunnerSplitUpdater
    extends PackageSharedLibraryUpdater
  {
    public AndroidTestRunnerSplitUpdater() {}
    
    public void updatePackage(PackageParser.Package paramPackage)
    {
      prefixImplicitDependency(paramPackage, "android.test.runner", "android.test.mock");
    }
  }
  
  @VisibleForTesting
  public static class RemoveUnnecessaryAndroidTestBaseLibrary
    extends PackageSharedLibraryUpdater
  {
    public RemoveUnnecessaryAndroidTestBaseLibrary() {}
    
    public void updatePackage(PackageParser.Package paramPackage)
    {
      removeLibrary(paramPackage, "android.test.base");
    }
  }
  
  @VisibleForTesting
  public static class RemoveUnnecessaryOrgApacheHttpLegacyLibrary
    extends PackageSharedLibraryUpdater
  {
    public RemoveUnnecessaryOrgApacheHttpLegacyLibrary() {}
    
    public void updatePackage(PackageParser.Package paramPackage)
    {
      removeLibrary(paramPackage, "org.apache.http.legacy");
    }
  }
}
