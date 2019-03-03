package android.app;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Window;
import android.view.WindowManagerImpl;

public class Presentation
  extends Dialog
{
  private static final int MSG_CANCEL = 1;
  private static final String TAG = "Presentation";
  private final Display mDisplay;
  private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener()
  {
    public void onDisplayAdded(int paramAnonymousInt) {}
    
    public void onDisplayChanged(int paramAnonymousInt)
    {
      if (paramAnonymousInt == mDisplay.getDisplayId()) {
        Presentation.this.handleDisplayChanged();
      }
    }
    
    public void onDisplayRemoved(int paramAnonymousInt)
    {
      if (paramAnonymousInt == mDisplay.getDisplayId()) {
        Presentation.this.handleDisplayRemoved();
      }
    }
  };
  private final DisplayManager mDisplayManager;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 1) {
        cancel();
      }
    }
  };
  private final IBinder mToken = new Binder();
  
  public Presentation(Context paramContext, Display paramDisplay)
  {
    this(paramContext, paramDisplay, 0);
  }
  
  public Presentation(Context paramContext, Display paramDisplay, int paramInt)
  {
    super(createPresentationContext(paramContext, paramDisplay, paramInt), paramInt, false);
    mDisplay = paramDisplay;
    mDisplayManager = ((DisplayManager)getContext().getSystemService("display"));
    paramDisplay = getWindow();
    paramContext = paramDisplay.getAttributes();
    token = mToken;
    paramDisplay.setAttributes(paramContext);
    paramDisplay.setGravity(119);
    paramDisplay.setType(2037);
    setCanceledOnTouchOutside(false);
  }
  
  private static Context createPresentationContext(Context paramContext, Display paramDisplay, int paramInt)
  {
    if (paramContext != null)
    {
      if (paramDisplay != null)
      {
        paramDisplay = paramContext.createDisplayContext(paramDisplay);
        int i = paramInt;
        if (paramInt == 0)
        {
          TypedValue localTypedValue = new TypedValue();
          paramDisplay.getTheme().resolveAttribute(16843712, localTypedValue, true);
          i = resourceId;
        }
        paramContext = (WindowManagerImpl)paramContext.getSystemService("window");
        new ContextThemeWrapper(paramDisplay, i)
        {
          public Object getSystemService(String paramAnonymousString)
          {
            if ("window".equals(paramAnonymousString)) {
              return val$displayWindowManager;
            }
            return super.getSystemService(paramAnonymousString);
          }
        };
      }
      throw new IllegalArgumentException("display must not be null");
    }
    throw new IllegalArgumentException("outerContext must not be null");
  }
  
  private void handleDisplayChanged()
  {
    onDisplayChanged();
    if (!isConfigurationStillValid())
    {
      Log.i("Presentation", "Presentation is being dismissed because the display metrics have changed since it was created.");
      cancel();
    }
  }
  
  private void handleDisplayRemoved()
  {
    onDisplayRemoved();
    cancel();
  }
  
  private boolean isConfigurationStillValid()
  {
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    mDisplay.getMetrics(localDisplayMetrics);
    return localDisplayMetrics.equalsPhysical(getResources().getDisplayMetrics());
  }
  
  public Display getDisplay()
  {
    return mDisplay;
  }
  
  public Resources getResources()
  {
    return getContext().getResources();
  }
  
  public void onDisplayChanged() {}
  
  public void onDisplayRemoved() {}
  
  protected void onStart()
  {
    super.onStart();
    mDisplayManager.registerDisplayListener(mDisplayListener, mHandler);
    if (!isConfigurationStillValid())
    {
      Log.i("Presentation", "Presentation is being dismissed because the display metrics have changed since it was created.");
      mHandler.sendEmptyMessage(1);
    }
  }
  
  protected void onStop()
  {
    mDisplayManager.unregisterDisplayListener(mDisplayListener);
    super.onStop();
  }
  
  public void show()
  {
    super.show();
  }
}
