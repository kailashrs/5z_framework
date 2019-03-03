package com.android.internal.util;

import android.os.Message;

public class State
  implements IState
{
  protected State() {}
  
  public void enter() {}
  
  public void exit() {}
  
  public String getName()
  {
    String str = getClass().getName();
    return str.substring(str.lastIndexOf('$') + 1);
  }
  
  public boolean processMessage(Message paramMessage)
  {
    return false;
  }
}
