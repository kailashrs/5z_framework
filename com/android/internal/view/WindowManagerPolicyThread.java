package com.android.internal.view;

import android.os.Looper;

public class WindowManagerPolicyThread
{
  static Looper mLooper;
  static Thread mThread;
  
  public WindowManagerPolicyThread() {}
  
  public static Looper getLooper()
  {
    return mLooper;
  }
  
  public static Thread getThread()
  {
    return mThread;
  }
  
  public static void set(Thread paramThread, Looper paramLooper)
  {
    mThread = paramThread;
    mLooper = paramLooper;
  }
}
