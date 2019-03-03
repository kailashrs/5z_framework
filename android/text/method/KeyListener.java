package android.text.method;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;

public abstract interface KeyListener
{
  public abstract void clearMetaKeyState(View paramView, Editable paramEditable, int paramInt);
  
  public abstract int getInputType();
  
  public abstract boolean onKeyDown(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent);
  
  public abstract boolean onKeyOther(View paramView, Editable paramEditable, KeyEvent paramKeyEvent);
  
  public abstract boolean onKeyUp(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent);
}
