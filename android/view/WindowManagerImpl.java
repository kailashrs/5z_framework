package android.view;

import android.content.Context;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SeempLog;
import com.android.internal.os.IResultReceiver.Stub;

public final class WindowManagerImpl
  implements WindowManager
{
  private final Context mContext;
  private IBinder mDefaultToken;
  private final WindowManagerGlobal mGlobal = WindowManagerGlobal.getInstance();
  private final Window mParentWindow;
  
  public WindowManagerImpl(Context paramContext)
  {
    this(paramContext, null);
  }
  
  private WindowManagerImpl(Context paramContext, Window paramWindow)
  {
    mContext = paramContext;
    mParentWindow = paramWindow;
  }
  
  private void applyDefaultToken(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((mDefaultToken != null) && (mParentWindow == null)) {
      if ((paramLayoutParams instanceof WindowManager.LayoutParams))
      {
        paramLayoutParams = (WindowManager.LayoutParams)paramLayoutParams;
        if (token == null) {
          token = mDefaultToken;
        }
      }
      else
      {
        throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
      }
    }
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    SeempLog.record_vg_layout(383, paramLayoutParams);
    applyDefaultToken(paramLayoutParams);
    mGlobal.addView(paramView, paramLayoutParams, mContext.getDisplay(), mParentWindow);
  }
  
  public WindowManagerImpl createLocalWindowManager(Window paramWindow)
  {
    return new WindowManagerImpl(mContext, paramWindow);
  }
  
  public WindowManagerImpl createPresentationWindowManager(Context paramContext)
  {
    return new WindowManagerImpl(paramContext, mParentWindow);
  }
  
  public Region getCurrentImeTouchRegion()
  {
    try
    {
      Region localRegion = WindowManagerGlobal.getWindowManagerService().getCurrentImeTouchRegion();
      return localRegion;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public Display getDefaultDisplay()
  {
    return mContext.getDisplay();
  }
  
  public void removeView(View paramView)
  {
    mGlobal.removeView(paramView, false);
  }
  
  public void removeViewImmediate(View paramView)
  {
    mGlobal.removeView(paramView, true);
  }
  
  public void requestAppKeyboardShortcuts(final WindowManager.KeyboardShortcutsReceiver paramKeyboardShortcutsReceiver, int paramInt)
  {
    paramKeyboardShortcutsReceiver = new IResultReceiver.Stub()
    {
      public void send(int paramAnonymousInt, Bundle paramAnonymousBundle)
        throws RemoteException
      {
        paramAnonymousBundle = paramAnonymousBundle.getParcelableArrayList("shortcuts_array");
        paramKeyboardShortcutsReceiver.onKeyboardShortcutsReceived(paramAnonymousBundle);
      }
    };
    try
    {
      WindowManagerGlobal.getWindowManagerService().requestAppKeyboardShortcuts(paramKeyboardShortcutsReceiver, paramInt);
    }
    catch (RemoteException paramKeyboardShortcutsReceiver) {}
  }
  
  public void setDefaultToken(IBinder paramIBinder)
  {
    mDefaultToken = paramIBinder;
  }
  
  public void updateViewLayout(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    SeempLog.record_vg_layout(384, paramLayoutParams);
    applyDefaultToken(paramLayoutParams);
    mGlobal.updateViewLayout(paramView, paramLayoutParams);
  }
}
