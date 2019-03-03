package android.view;

public class SearchEvent
{
  private InputDevice mInputDevice;
  
  public SearchEvent(InputDevice paramInputDevice)
  {
    mInputDevice = paramInputDevice;
  }
  
  public InputDevice getInputDevice()
  {
    return mInputDevice;
  }
}
