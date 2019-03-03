package android.os.storage;

public abstract class StorageManagerInternal
{
  public StorageManagerInternal() {}
  
  public abstract void addExternalStoragePolicy(ExternalStorageMountPolicy paramExternalStorageMountPolicy);
  
  public abstract int getExternalStorageMountMode(int paramInt, String paramString);
  
  public abstract void onExternalStoragePolicyChanged(int paramInt, String paramString);
  
  public static abstract interface ExternalStorageMountPolicy
  {
    public abstract int getMountMode(int paramInt, String paramString);
    
    public abstract boolean hasExternalStorage(int paramInt, String paramString);
  }
}
