package android.app;

import android.content.ComponentName;
import android.os.RemoteException;

public abstract class TaskStackListener
  extends ITaskStackListener.Stub
{
  public TaskStackListener() {}
  
  public void onActivityDismissingDockedStack()
    throws RemoteException
  {}
  
  public void onActivityForcedResizable(String paramString, int paramInt1, int paramInt2)
    throws RemoteException
  {}
  
  public void onActivityLaunchOnSecondaryDisplayFailed()
    throws RemoteException
  {}
  
  public void onActivityPinned(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {}
  
  public void onActivityRequestedOrientationChanged(int paramInt1, int paramInt2)
    throws RemoteException
  {}
  
  public void onActivityUnpinned()
    throws RemoteException
  {}
  
  public void onPinnedActivityRestartAttempt(boolean paramBoolean)
    throws RemoteException
  {}
  
  public void onPinnedStackAnimationEnded()
    throws RemoteException
  {}
  
  public void onPinnedStackAnimationStarted()
    throws RemoteException
  {}
  
  public void onTaskCreated(int paramInt, ComponentName paramComponentName)
    throws RemoteException
  {}
  
  public void onTaskDescriptionChanged(int paramInt, ActivityManager.TaskDescription paramTaskDescription)
    throws RemoteException
  {}
  
  public void onTaskMovedToFront(int paramInt)
    throws RemoteException
  {}
  
  public void onTaskProfileLocked(int paramInt1, int paramInt2)
    throws RemoteException
  {}
  
  public void onTaskRemovalStarted(int paramInt)
    throws RemoteException
  {}
  
  public void onTaskRemoved(int paramInt)
    throws RemoteException
  {}
  
  public void onTaskSnapshotChanged(int paramInt, ActivityManager.TaskSnapshot paramTaskSnapshot)
    throws RemoteException
  {}
  
  public void onTaskStackChanged()
    throws RemoteException
  {}
}
