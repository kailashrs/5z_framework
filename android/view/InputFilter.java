package android.view;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

public abstract class InputFilter
  extends IInputFilter.Stub
{
  private static final int MSG_INPUT_EVENT = 3;
  private static final int MSG_INSTALL = 1;
  private static final int MSG_UNINSTALL = 2;
  private final H mH;
  private IInputFilterHost mHost;
  private final InputEventConsistencyVerifier mInboundInputEventConsistencyVerifier;
  private final InputEventConsistencyVerifier mOutboundInputEventConsistencyVerifier;
  
  public InputFilter(Looper paramLooper)
  {
    boolean bool = InputEventConsistencyVerifier.isInstrumentationEnabled();
    Object localObject1 = null;
    if (bool) {
      localObject2 = new InputEventConsistencyVerifier(this, 1, "InputFilter#InboundInputEventConsistencyVerifier");
    } else {
      localObject2 = null;
    }
    mInboundInputEventConsistencyVerifier = ((InputEventConsistencyVerifier)localObject2);
    Object localObject2 = localObject1;
    if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
      localObject2 = new InputEventConsistencyVerifier(this, 1, "InputFilter#OutboundInputEventConsistencyVerifier");
    }
    mOutboundInputEventConsistencyVerifier = ((InputEventConsistencyVerifier)localObject2);
    mH = new H(paramLooper);
  }
  
  public final void filterInputEvent(InputEvent paramInputEvent, int paramInt)
  {
    mH.obtainMessage(3, paramInt, 0, paramInputEvent).sendToTarget();
  }
  
  public final void install(IInputFilterHost paramIInputFilterHost)
  {
    mH.obtainMessage(1, paramIInputFilterHost).sendToTarget();
  }
  
  public void onInputEvent(InputEvent paramInputEvent, int paramInt)
  {
    sendInputEvent(paramInputEvent, paramInt);
  }
  
  public void onInstalled() {}
  
  public void onUninstalled() {}
  
  public void sendInputEvent(InputEvent paramInputEvent, int paramInt)
  {
    if (paramInputEvent != null)
    {
      if (mHost != null)
      {
        if (mOutboundInputEventConsistencyVerifier != null) {
          mOutboundInputEventConsistencyVerifier.onInputEvent(paramInputEvent, 0);
        }
        try
        {
          mHost.sendInputEvent(paramInputEvent, paramInt);
        }
        catch (RemoteException paramInputEvent) {}
        return;
      }
      throw new IllegalStateException("Cannot send input event because the input filter is not installed.");
    }
    throw new IllegalArgumentException("event must not be null");
  }
  
  public final void uninstall()
  {
    mH.obtainMessage(2).sendToTarget();
  }
  
  private final class H
    extends Handler
  {
    public H(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 3: 
        InputEvent localInputEvent = (InputEvent)obj;
        try
        {
          if (mInboundInputEventConsistencyVerifier != null) {
            mInboundInputEventConsistencyVerifier.onInputEvent(localInputEvent, 0);
          }
          onInputEvent(localInputEvent, arg1);
        }
        finally
        {
          localInputEvent.recycle();
        }
      case 2: 
      case 1: 
        try
        {
          onUninstalled();
          InputFilter.access$002(InputFilter.this, null);
        }
        finally
        {
          InputFilter.access$002(InputFilter.this, null);
        }
        if (mInboundInputEventConsistencyVerifier != null) {
          mInboundInputEventConsistencyVerifier.reset();
        }
        if (mOutboundInputEventConsistencyVerifier != null) {
          mOutboundInputEventConsistencyVerifier.reset();
        }
        onInstalled();
      }
    }
  }
}
