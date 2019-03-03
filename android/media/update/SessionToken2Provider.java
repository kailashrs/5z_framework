package android.media.update;

import android.os.Bundle;

public abstract interface SessionToken2Provider
{
  public abstract boolean equals_impl(Object paramObject);
  
  public abstract String getId_imp();
  
  public abstract String getPackageName_impl();
  
  public abstract int getType_impl();
  
  public abstract int getUid_impl();
  
  public abstract int hashCode_impl();
  
  public abstract Bundle toBundle_impl();
  
  public abstract String toString_impl();
}
