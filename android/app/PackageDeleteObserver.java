package android.app;

import android.content.Intent;
import android.content.pm.IPackageDeleteObserver2;
import android.content.pm.IPackageDeleteObserver2.Stub;

public class PackageDeleteObserver
{
  private final IPackageDeleteObserver2.Stub mBinder = new IPackageDeleteObserver2.Stub()
  {
    public void onPackageDeleted(String paramAnonymousString1, int paramAnonymousInt, String paramAnonymousString2)
    {
      PackageDeleteObserver.this.onPackageDeleted(paramAnonymousString1, paramAnonymousInt, paramAnonymousString2);
    }
    
    public void onUserActionRequired(Intent paramAnonymousIntent)
    {
      PackageDeleteObserver.this.onUserActionRequired(paramAnonymousIntent);
    }
  };
  
  public PackageDeleteObserver() {}
  
  public IPackageDeleteObserver2 getBinder()
  {
    return mBinder;
  }
  
  public void onPackageDeleted(String paramString1, int paramInt, String paramString2) {}
  
  public void onUserActionRequired(Intent paramIntent) {}
}
