package com.android.internal.util;

import android.os.Message;

public abstract interface IState
{
  public static final boolean HANDLED = true;
  public static final boolean NOT_HANDLED = false;
  
  public abstract void enter();
  
  public abstract void exit();
  
  public abstract String getName();
  
  public abstract boolean processMessage(Message paramMessage);
}
