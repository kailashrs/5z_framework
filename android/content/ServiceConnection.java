package android.content;

import android.os.IBinder;

public abstract interface ServiceConnection
{
  public void onBindingDied(ComponentName paramComponentName) {}
  
  public void onNullBinding(ComponentName paramComponentName) {}
  
  public abstract void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder);
  
  public abstract void onServiceDisconnected(ComponentName paramComponentName);
}
