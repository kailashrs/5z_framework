package android.content;

import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;

public class ClipboardManager
  extends android.text.ClipboardManager
{
  private final Context mContext;
  private final Handler mHandler;
  private final ArrayList<OnPrimaryClipChangedListener> mPrimaryClipChangedListeners = new ArrayList();
  private final IOnPrimaryClipChangedListener.Stub mPrimaryClipChangedServiceListener = new IOnPrimaryClipChangedListener.Stub()
  {
    public void dispatchPrimaryClipChanged()
    {
      mHandler.post(new _..Lambda.ClipboardManager.1.hQk8olbGAgUi4WWNG4ZuDZsM39s(this));
    }
  };
  private final IClipboard mService;
  
  public ClipboardManager(Context paramContext, Handler paramHandler)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mHandler = paramHandler;
    mService = IClipboard.Stub.asInterface(ServiceManager.getServiceOrThrow("clipboard"));
  }
  
  public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener paramOnPrimaryClipChangedListener)
  {
    synchronized (mPrimaryClipChangedListeners)
    {
      boolean bool = mPrimaryClipChangedListeners.isEmpty();
      if (bool) {
        try
        {
          mService.addPrimaryClipChangedListener(mPrimaryClipChangedServiceListener, mContext.getOpPackageName());
        }
        catch (RemoteException paramOnPrimaryClipChangedListener)
        {
          throw paramOnPrimaryClipChangedListener.rethrowFromSystemServer();
        }
      }
      mPrimaryClipChangedListeners.add(paramOnPrimaryClipChangedListener);
      return;
    }
  }
  
  public void clearPrimaryClip()
  {
    try
    {
      mService.clearPrimaryClip(mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ClipData getPrimaryClip()
  {
    try
    {
      ClipData localClipData = mService.getPrimaryClip(mContext.getOpPackageName());
      return localClipData;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ClipDescription getPrimaryClipDescription()
  {
    try
    {
      ClipDescription localClipDescription = mService.getPrimaryClipDescription(mContext.getOpPackageName());
      return localClipDescription;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public CharSequence getText()
  {
    ClipData localClipData = getPrimaryClip();
    if ((localClipData != null) && (localClipData.getItemCount() > 0)) {
      return localClipData.getItemAt(0).coerceToText(mContext);
    }
    return null;
  }
  
  public boolean hasPrimaryClip()
  {
    try
    {
      boolean bool = mService.hasPrimaryClip(mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean hasText()
  {
    try
    {
      boolean bool = mService.hasClipboardText(mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener paramOnPrimaryClipChangedListener)
  {
    synchronized (mPrimaryClipChangedListeners)
    {
      mPrimaryClipChangedListeners.remove(paramOnPrimaryClipChangedListener);
      boolean bool = mPrimaryClipChangedListeners.isEmpty();
      if (bool) {
        try
        {
          mService.removePrimaryClipChangedListener(mPrimaryClipChangedServiceListener);
        }
        catch (RemoteException paramOnPrimaryClipChangedListener)
        {
          throw paramOnPrimaryClipChangedListener.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  void reportPrimaryClipChanged()
  {
    synchronized (mPrimaryClipChangedListeners)
    {
      if (mPrimaryClipChangedListeners.size() <= 0) {
        return;
      }
      Object[] arrayOfObject = mPrimaryClipChangedListeners.toArray();
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((OnPrimaryClipChangedListener)arrayOfObject[i]).onPrimaryClipChanged();
      }
      return;
    }
  }
  
  public void setPrimaryClip(ClipData paramClipData)
  {
    try
    {
      Preconditions.checkNotNull(paramClipData);
      paramClipData.prepareToLeaveProcess(true);
      mService.setPrimaryClip(paramClipData, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramClipData)
    {
      throw paramClipData.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setText(CharSequence paramCharSequence)
  {
    setPrimaryClip(ClipData.newPlainText(null, paramCharSequence));
  }
  
  public static abstract interface OnPrimaryClipChangedListener
  {
    public abstract void onPrimaryClipChanged();
  }
}
