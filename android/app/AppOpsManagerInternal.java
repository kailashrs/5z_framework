package android.app;

import android.util.SparseIntArray;

public abstract class AppOpsManagerInternal
{
  public AppOpsManagerInternal() {}
  
  public abstract void setDeviceAndProfileOwners(SparseIntArray paramSparseIntArray);
}
