package android.content.pm;

import com.android.server.LocalServices;
import java.util.List;

public class PackageList
  implements PackageManagerInternal.PackageListObserver, AutoCloseable
{
  private final List<String> mPackageNames;
  private final PackageManagerInternal.PackageListObserver mWrappedObserver;
  
  public PackageList(List<String> paramList, PackageManagerInternal.PackageListObserver paramPackageListObserver)
  {
    mPackageNames = paramList;
    mWrappedObserver = paramPackageListObserver;
  }
  
  public void close()
    throws Exception
  {
    ((PackageManagerInternal)LocalServices.getService(PackageManagerInternal.class)).removePackageListObserver(this);
  }
  
  public List<String> getPackageNames()
  {
    return mPackageNames;
  }
  
  public void onPackageAdded(String paramString)
  {
    if (mWrappedObserver != null) {
      mWrappedObserver.onPackageAdded(paramString);
    }
  }
  
  public void onPackageRemoved(String paramString)
  {
    if (mWrappedObserver != null) {
      mWrappedObserver.onPackageRemoved(paramString);
    }
  }
}
