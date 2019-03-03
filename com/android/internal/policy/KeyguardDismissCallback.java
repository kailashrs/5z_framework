package com.android.internal.policy;

import android.os.RemoteException;

public class KeyguardDismissCallback
  extends IKeyguardDismissCallback.Stub
{
  public KeyguardDismissCallback() {}
  
  public void onDismissCancelled()
    throws RemoteException
  {}
  
  public void onDismissError()
    throws RemoteException
  {}
  
  public void onDismissSucceeded()
    throws RemoteException
  {}
}
