package android.view.inputmethod;

import android.content.ComponentName;

public abstract interface InputMethodManagerInternal
{
  public abstract void hideCurrentInputMethod();
  
  public abstract void setInteractive(boolean paramBoolean);
  
  public abstract void startVrInputMethodNoCheck(ComponentName paramComponentName);
  
  public abstract void switchInputMethod(boolean paramBoolean);
}
