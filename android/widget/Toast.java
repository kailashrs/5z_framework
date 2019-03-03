package android.widget;

import android.app.INotificationManager;
import android.app.INotificationManager.Stub;
import android.app.ITransientNotification.Stub;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Toast
{
  public static final int LENGTH_LONG = 1;
  public static final int LENGTH_SHORT = 0;
  static final String TAG = "Toast";
  static final boolean localLOGV = false;
  private static INotificationManager sService;
  final Context mContext;
  int mDuration;
  View mNextView;
  final TN mTN;
  
  public Toast(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Toast(Context paramContext, Looper paramLooper)
  {
    mContext = paramContext;
    mTN = new TN(paramContext.getPackageName(), paramLooper);
    mTN.mY = paramContext.getResources().getDimensionPixelSize(17105479);
    mTN.mGravity = paramContext.getResources().getInteger(17694881);
  }
  
  private static INotificationManager getService()
  {
    if (sService != null) {
      return sService;
    }
    sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    return sService;
  }
  
  public static Toast makeText(Context paramContext, int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return makeText(paramContext, paramContext.getResources().getText(paramInt1), paramInt2);
  }
  
  public static Toast makeText(Context paramContext, Looper paramLooper, CharSequence paramCharSequence, int paramInt)
  {
    paramLooper = new Toast(paramContext, paramLooper);
    paramContext = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(17367348, null);
    ((TextView)paramContext.findViewById(16908299)).setText(paramCharSequence);
    mNextView = paramContext;
    mDuration = paramInt;
    return paramLooper;
  }
  
  public static Toast makeText(Context paramContext, CharSequence paramCharSequence, int paramInt)
  {
    return makeText(paramContext, null, paramCharSequence, paramInt);
  }
  
  public void cancel()
  {
    mTN.cancel();
  }
  
  public int getDuration()
  {
    return mDuration;
  }
  
  public int getGravity()
  {
    return mTN.mGravity;
  }
  
  public float getHorizontalMargin()
  {
    return mTN.mHorizontalMargin;
  }
  
  public float getVerticalMargin()
  {
    return mTN.mVerticalMargin;
  }
  
  public View getView()
  {
    return mNextView;
  }
  
  public WindowManager.LayoutParams getWindowParams()
  {
    return mTN.mParams;
  }
  
  public int getXOffset()
  {
    return mTN.mX;
  }
  
  public int getYOffset()
  {
    return mTN.mY;
  }
  
  public void setDuration(int paramInt)
  {
    mDuration = paramInt;
    mTN.mDuration = paramInt;
  }
  
  public void setGravity(int paramInt1, int paramInt2, int paramInt3)
  {
    mTN.mGravity = paramInt1;
    mTN.mX = paramInt2;
    mTN.mY = paramInt3;
  }
  
  public void setMargin(float paramFloat1, float paramFloat2)
  {
    mTN.mHorizontalMargin = paramFloat1;
    mTN.mVerticalMargin = paramFloat2;
  }
  
  public void setText(int paramInt)
  {
    setText(mContext.getText(paramInt));
  }
  
  public void setText(CharSequence paramCharSequence)
  {
    if (mNextView != null)
    {
      TextView localTextView = (TextView)mNextView.findViewById(16908299);
      if (localTextView != null)
      {
        localTextView.setText(paramCharSequence);
        return;
      }
      throw new RuntimeException("This Toast was not created with Toast.makeText()");
    }
    throw new RuntimeException("This Toast was not created with Toast.makeText()");
  }
  
  public void setView(View paramView)
  {
    mNextView = paramView;
  }
  
  public void show()
  {
    if (mNextView != null)
    {
      INotificationManager localINotificationManager = getService();
      String str = mContext.getOpPackageName();
      TN localTN = mTN;
      mNextView = mNextView;
      try
      {
        localINotificationManager.enqueueToast(str, localTN, mDuration);
      }
      catch (RemoteException localRemoteException) {}
      return;
    }
    throw new RuntimeException("setView must have been called");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Duration {}
  
  private static class TN
    extends ITransientNotification.Stub
  {
    private static final int CANCEL = 2;
    private static final int HIDE = 1;
    static final long LONG_DURATION_TIMEOUT = 7000L;
    static final long SHORT_DURATION_TIMEOUT = 4000L;
    private static final int SHOW = 0;
    int mDuration;
    int mGravity;
    final Handler mHandler;
    float mHorizontalMargin;
    View mNextView;
    String mPackageName;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    float mVerticalMargin;
    View mView;
    WindowManager mWM;
    int mX;
    int mY;
    
    TN(String paramString, Looper paramLooper)
    {
      WindowManager.LayoutParams localLayoutParams = mParams;
      height = -2;
      width = -2;
      format = -3;
      windowAnimations = 16973828;
      type = 2005;
      localLayoutParams.setTitle("Toast");
      flags = 152;
      mPackageName = paramString;
      paramString = paramLooper;
      if (paramLooper == null)
      {
        paramString = Looper.myLooper();
        if (paramString == null) {
          throw new RuntimeException("Can't toast on a thread that has not called Looper.prepare()");
        }
      }
      mHandler = new Handler(paramString, null)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          switch (what)
          {
          default: 
            break;
          case 2: 
            handleHide();
            mNextView = null;
            try
            {
              Toast.access$100().cancelToast(mPackageName, Toast.TN.this);
            }
            catch (RemoteException paramAnonymousMessage) {}
          case 1: 
            handleHide();
            mNextView = null;
            break;
          case 0: 
            paramAnonymousMessage = (IBinder)obj;
            handleShow(paramAnonymousMessage);
          }
        }
      };
    }
    
    private void trySendAccessibilityEvent()
    {
      AccessibilityManager localAccessibilityManager = AccessibilityManager.getInstance(mView.getContext());
      if (!localAccessibilityManager.isEnabled()) {
        return;
      }
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(64);
      localAccessibilityEvent.setClassName(getClass().getName());
      localAccessibilityEvent.setPackageName(mView.getContext().getPackageName());
      mView.dispatchPopulateAccessibilityEvent(localAccessibilityEvent);
      localAccessibilityManager.sendAccessibilityEvent(localAccessibilityEvent);
    }
    
    public void cancel()
    {
      mHandler.obtainMessage(2).sendToTarget();
    }
    
    public void handleHide()
    {
      if (mView != null)
      {
        if (mView.getParent() != null) {
          mWM.removeViewImmediate(mView);
        }
        try
        {
          Toast.access$100().finishToken(mPackageName, this);
        }
        catch (RemoteException localRemoteException) {}
        mView = null;
      }
    }
    
    public void handleShow(IBinder paramIBinder)
    {
      if ((!mHandler.hasMessages(2)) && (!mHandler.hasMessages(1)))
      {
        if (mView != mNextView)
        {
          handleHide();
          mView = mNextView;
          Context localContext = mView.getContext().getApplicationContext();
          String str = mView.getContext().getOpPackageName();
          Object localObject = localContext;
          if (localContext == null) {
            localObject = mView.getContext();
          }
          mWM = ((WindowManager)((Context)localObject).getSystemService("window"));
          localObject = mView.getContext().getResources().getConfiguration();
          int i = Gravity.getAbsoluteGravity(mGravity, ((Configuration)localObject).getLayoutDirection());
          mParams.gravity = i;
          if ((i & 0x7) == 7) {
            mParams.horizontalWeight = 1.0F;
          }
          if ((i & 0x70) == 112) {
            mParams.verticalWeight = 1.0F;
          }
          mParams.x = mX;
          mParams.y = mY;
          mParams.verticalMargin = mVerticalMargin;
          mParams.horizontalMargin = mHorizontalMargin;
          mParams.packageName = str;
          localObject = mParams;
          long l;
          if (mDuration == 1) {
            l = 7000L;
          } else {
            l = 4000L;
          }
          hideTimeoutMilliseconds = l;
          mParams.token = paramIBinder;
          if (mView.getParent() != null) {
            mWM.removeView(mView);
          }
          if (SystemProperties.getInt("ro.debuggable", 0) == 1)
          {
            paramIBinder = new StringBuilder();
            paramIBinder.append("HANDLE SHOW: packageName=");
            paramIBinder.append(str);
            Log.v("Toast", paramIBinder.toString());
          }
          try
          {
            mWM.addView(mView, mParams);
            trySendAccessibilityEvent();
          }
          catch (WindowManager.BadTokenException paramIBinder) {}
        }
        return;
      }
    }
    
    public void hide()
    {
      mHandler.obtainMessage(1).sendToTarget();
    }
    
    public void show(IBinder paramIBinder)
    {
      mHandler.obtainMessage(0, paramIBinder).sendToTarget();
    }
  }
}
