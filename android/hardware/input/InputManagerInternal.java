package android.hardware.input;

import android.hardware.display.DisplayViewport;
import android.view.InputEvent;
import java.util.List;

public abstract class InputManagerInternal
{
  public InputManagerInternal() {}
  
  public abstract boolean injectInputEvent(InputEvent paramInputEvent, int paramInt1, int paramInt2);
  
  public abstract void setDisplayViewports(DisplayViewport paramDisplayViewport1, DisplayViewport paramDisplayViewport2, List<DisplayViewport> paramList);
  
  public abstract void setInteractive(boolean paramBoolean);
  
  public abstract void setPulseGestureEnabled(boolean paramBoolean);
  
  public abstract void toggleCapsLock(int paramInt);
}
