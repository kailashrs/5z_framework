package android.media.tv;

import android.annotation.SystemApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class TvView
  extends ViewGroup
{
  private static final boolean DEBUG = false;
  private static final WeakReference<TvView> NULL_TV_VIEW = new WeakReference(null);
  private static final String TAG = "TvView";
  private static final int ZORDER_MEDIA = 0;
  private static final int ZORDER_MEDIA_OVERLAY = 1;
  private static final int ZORDER_ON_TOP = 2;
  private static WeakReference<TvView> sMainTvView = NULL_TV_VIEW;
  private static final Object sMainTvViewLock = new Object();
  private final AttributeSet mAttrs;
  private TvInputCallback mCallback;
  private Boolean mCaptionEnabled;
  private final int mDefStyleAttr;
  private final TvInputManager.Session.FinishedInputEventCallback mFinishedInputEventCallback = new TvInputManager.Session.FinishedInputEventCallback()
  {
    public void onFinishedInputEvent(Object paramAnonymousObject, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        return;
      }
      paramAnonymousObject = (InputEvent)paramAnonymousObject;
      if (dispatchUnhandledInputEvent(paramAnonymousObject)) {
        return;
      }
      ViewRootImpl localViewRootImpl = getViewRootImpl();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchUnhandledInputEvent(paramAnonymousObject);
      }
    }
  };
  private final Handler mHandler = new Handler();
  private OnUnhandledInputEventListener mOnUnhandledInputEventListener;
  private boolean mOverlayViewCreated;
  private Rect mOverlayViewFrame;
  private final Queue<Pair<String, Bundle>> mPendingAppPrivateCommands = new ArrayDeque();
  private TvInputManager.Session mSession;
  private MySessionCallback mSessionCallback;
  private Float mStreamVolume;
  private Surface mSurface;
  private boolean mSurfaceChanged;
  private int mSurfaceFormat;
  private int mSurfaceHeight;
  private final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback()
  {
    public void surfaceChanged(SurfaceHolder paramAnonymousSurfaceHolder, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      TvView.access$002(TvView.this, paramAnonymousInt1);
      TvView.access$102(TvView.this, paramAnonymousInt2);
      TvView.access$202(TvView.this, paramAnonymousInt3);
      TvView.access$302(TvView.this, true);
      TvView.this.dispatchSurfaceChanged(mSurfaceFormat, mSurfaceWidth, mSurfaceHeight);
    }
    
    public void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder)
    {
      TvView.access$502(TvView.this, paramAnonymousSurfaceHolder.getSurface());
      TvView.this.setSessionSurface(mSurface);
    }
    
    public void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder)
    {
      TvView.access$502(TvView.this, null);
      TvView.access$302(TvView.this, false);
      TvView.this.setSessionSurface(null);
    }
  };
  private SurfaceView mSurfaceView;
  private int mSurfaceViewBottom;
  private int mSurfaceViewLeft;
  private int mSurfaceViewRight;
  private int mSurfaceViewTop;
  private int mSurfaceWidth;
  private TimeShiftPositionCallback mTimeShiftPositionCallback;
  private final TvInputManager mTvInputManager;
  private boolean mUseRequestedSurfaceLayout;
  private int mWindowZOrder;
  
  public TvView(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public TvView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TvView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    mAttrs = paramAttributeSet;
    mDefStyleAttr = paramInt;
    resetSurfaceView();
    mTvInputManager = ((TvInputManager)getContext().getSystemService("tv_input"));
  }
  
  private boolean checkChangeHdmiCecActiveSourcePermission()
  {
    boolean bool;
    if (getContext().checkSelfPermission("android.permission.CHANGE_HDMI_CEC_ACTIVE_SOURCE") == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void createSessionOverlayView()
  {
    if ((mSession != null) && (isAttachedToWindow()) && (!mOverlayViewCreated) && (mWindowZOrder == 0))
    {
      mOverlayViewFrame = getViewFrameOnScreen();
      mSession.createOverlayView(this, mOverlayViewFrame);
      mOverlayViewCreated = true;
      return;
    }
  }
  
  private void dispatchSurfaceChanged(int paramInt1, int paramInt2, int paramInt3)
  {
    if (mSession == null) {
      return;
    }
    mSession.dispatchSurfaceChanged(paramInt1, paramInt2, paramInt3);
  }
  
  private void ensurePositionTracking()
  {
    if (mSession == null) {
      return;
    }
    TvInputManager.Session localSession = mSession;
    boolean bool;
    if (mTimeShiftPositionCallback != null) {
      bool = true;
    } else {
      bool = false;
    }
    localSession.timeShiftEnablePositionTracking(bool);
  }
  
  private Rect getViewFrameOnScreen()
  {
    Rect localRect = new Rect();
    getGlobalVisibleRect(localRect);
    RectF localRectF = new RectF(localRect);
    getMatrix().mapRect(localRectF);
    localRectF.round(localRect);
    return localRect;
  }
  
  private void relayoutSessionOverlayView()
  {
    if ((mSession != null) && (isAttachedToWindow()) && (mOverlayViewCreated) && (mWindowZOrder == 0))
    {
      Rect localRect = getViewFrameOnScreen();
      if (localRect.equals(mOverlayViewFrame)) {
        return;
      }
      mSession.relayoutOverlayView(localRect);
      mOverlayViewFrame = localRect;
      return;
    }
  }
  
  private void removeSessionOverlayView()
  {
    if ((mSession != null) && (mOverlayViewCreated))
    {
      mSession.removeOverlayView();
      mOverlayViewCreated = false;
      mOverlayViewFrame = null;
      return;
    }
  }
  
  private void resetInternal()
  {
    mSessionCallback = null;
    mPendingAppPrivateCommands.clear();
    if (mSession != null)
    {
      setSessionSurface(null);
      removeSessionOverlayView();
      mUseRequestedSurfaceLayout = false;
      mSession.release();
      mSession = null;
      resetSurfaceView();
    }
  }
  
  private void resetSurfaceView()
  {
    if (mSurfaceView != null)
    {
      mSurfaceView.getHolder().removeCallback(mSurfaceHolderCallback);
      removeView(mSurfaceView);
    }
    mSurface = null;
    mSurfaceView = new SurfaceView(getContext(), mAttrs, mDefStyleAttr)
    {
      protected void updateSurface()
      {
        super.updateSurface();
        TvView.this.relayoutSessionOverlayView();
      }
    };
    mSurfaceView.setSecure(true);
    mSurfaceView.getHolder().addCallback(mSurfaceHolderCallback);
    if (mWindowZOrder == 1) {
      mSurfaceView.setZOrderMediaOverlay(true);
    } else if (mWindowZOrder == 2) {
      mSurfaceView.setZOrderOnTop(true);
    }
    addView(mSurfaceView);
  }
  
  private void setSessionSurface(Surface paramSurface)
  {
    if (mSession == null) {
      return;
    }
    mSession.setSurface(paramSurface);
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    if (mWindowZOrder != 2) {
      paramCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }
    super.dispatchDraw(paramCanvas);
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = super.dispatchGenericMotionEvent(paramMotionEvent);
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (mSession == null) {
      return false;
    }
    paramMotionEvent = paramMotionEvent.copy();
    if (mSession.dispatchInputEvent(paramMotionEvent, paramMotionEvent, mFinishedInputEventCallback, mHandler) == 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool1 = super.dispatchKeyEvent(paramKeyEvent);
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (mSession == null) {
      return false;
    }
    paramKeyEvent = paramKeyEvent.copy();
    if (mSession.dispatchInputEvent(paramKeyEvent, paramKeyEvent, mFinishedInputEventCallback, mHandler) == 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = super.dispatchTouchEvent(paramMotionEvent);
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (mSession == null) {
      return false;
    }
    paramMotionEvent = paramMotionEvent.copy();
    if (mSession.dispatchInputEvent(paramMotionEvent, paramMotionEvent, mFinishedInputEventCallback, mHandler) == 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = super.dispatchTrackballEvent(paramMotionEvent);
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (mSession == null) {
      return false;
    }
    paramMotionEvent = paramMotionEvent.copy();
    if (mSession.dispatchInputEvent(paramMotionEvent, paramMotionEvent, mFinishedInputEventCallback, mHandler) == 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean dispatchUnhandledInputEvent(InputEvent paramInputEvent)
  {
    if ((mOnUnhandledInputEventListener != null) && (mOnUnhandledInputEventListener.onUnhandledInputEvent(paramInputEvent))) {
      return true;
    }
    return onUnhandledInputEvent(paramInputEvent);
  }
  
  public void dispatchWindowFocusChanged(boolean paramBoolean)
  {
    super.dispatchWindowFocusChanged(paramBoolean);
    Object localObject1 = sMainTvViewLock;
    if (paramBoolean) {
      try
      {
        if ((this == sMainTvView.get()) && (mSession != null) && (checkChangeHdmiCecActiveSourcePermission())) {
          mSession.setMain();
        }
      }
      finally
      {
        break label56;
      }
    }
    return;
    label56:
    throw localObject2;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mWindowZOrder != 2) {
      paramCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }
    super.draw(paramCanvas);
  }
  
  public boolean gatherTransparentRegion(Region paramRegion)
  {
    if ((mWindowZOrder != 2) && (paramRegion != null))
    {
      int i = getWidth();
      int j = getHeight();
      if ((i > 0) && (j > 0))
      {
        int[] arrayOfInt = new int[2];
        getLocationInWindow(arrayOfInt);
        int k = arrayOfInt[0];
        int m = arrayOfInt[1];
        paramRegion.op(k, m, k + i, m + j, Region.Op.UNION);
      }
    }
    return super.gatherTransparentRegion(paramRegion);
  }
  
  public String getSelectedTrack(int paramInt)
  {
    if (mSession == null) {
      return null;
    }
    return mSession.getSelectedTrack(paramInt);
  }
  
  public List<TvTrackInfo> getTracks(int paramInt)
  {
    if (mSession == null) {
      return null;
    }
    return mSession.getTracks(paramInt);
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    createSessionOverlayView();
  }
  
  protected void onDetachedFromWindow()
  {
    removeSessionOverlayView();
    super.onDetachedFromWindow();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mUseRequestedSurfaceLayout) {
      mSurfaceView.layout(mSurfaceViewLeft, mSurfaceViewTop, mSurfaceViewRight, mSurfaceViewBottom);
    } else {
      mSurfaceView.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    mSurfaceView.measure(paramInt1, paramInt2);
    int i = mSurfaceView.getMeasuredWidth();
    int j = mSurfaceView.getMeasuredHeight();
    int k = mSurfaceView.getMeasuredState();
    setMeasuredDimension(resolveSizeAndState(i, paramInt1, k), resolveSizeAndState(j, paramInt2, k << 16));
  }
  
  public boolean onUnhandledInputEvent(InputEvent paramInputEvent)
  {
    return false;
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    mSurfaceView.setVisibility(paramInt);
    if (paramInt == 0) {
      createSessionOverlayView();
    } else {
      removeSessionOverlayView();
    }
  }
  
  public void requestUnblockContent(TvContentRating paramTvContentRating)
  {
    unblockContent(paramTvContentRating);
  }
  
  public void reset()
  {
    synchronized (sMainTvViewLock)
    {
      if (this == sMainTvView.get()) {
        sMainTvView = NULL_TV_VIEW;
      }
      resetInternal();
      return;
    }
  }
  
  public void selectTrack(int paramInt, String paramString)
  {
    if (mSession != null) {
      mSession.selectTrack(paramInt, paramString);
    }
  }
  
  public void sendAppPrivateCommand(String paramString, Bundle paramBundle)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (mSession != null)
      {
        mSession.sendAppPrivateCommand(paramString, paramBundle);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("sendAppPrivateCommand - session not yet created (action \"");
        localStringBuilder.append(paramString);
        localStringBuilder.append("\" pending)");
        Log.w("TvView", localStringBuilder.toString());
        mPendingAppPrivateCommands.add(Pair.create(paramString, paramBundle));
      }
      return;
    }
    throw new IllegalArgumentException("action cannot be null or an empty string");
  }
  
  public void setCallback(TvInputCallback paramTvInputCallback)
  {
    mCallback = paramTvInputCallback;
  }
  
  public void setCaptionEnabled(boolean paramBoolean)
  {
    mCaptionEnabled = Boolean.valueOf(paramBoolean);
    if (mSession != null) {
      mSession.setCaptionEnabled(paramBoolean);
    }
  }
  
  @SystemApi
  public void setMain()
  {
    synchronized (sMainTvViewLock)
    {
      WeakReference localWeakReference = new java/lang/ref/WeakReference;
      localWeakReference.<init>(this);
      sMainTvView = localWeakReference;
      if ((hasWindowFocus()) && (mSession != null)) {
        mSession.setMain();
      }
      return;
    }
  }
  
  public void setOnUnhandledInputEventListener(OnUnhandledInputEventListener paramOnUnhandledInputEventListener)
  {
    mOnUnhandledInputEventListener = paramOnUnhandledInputEventListener;
  }
  
  public void setStreamVolume(float paramFloat)
  {
    mStreamVolume = Float.valueOf(paramFloat);
    if (mSession == null) {
      return;
    }
    mSession.setStreamVolume(paramFloat);
  }
  
  public void setTimeShiftPositionCallback(TimeShiftPositionCallback paramTimeShiftPositionCallback)
  {
    mTimeShiftPositionCallback = paramTimeShiftPositionCallback;
    ensurePositionTracking();
  }
  
  public void setZOrderMediaOverlay(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      mWindowZOrder = 1;
      removeSessionOverlayView();
    }
    else
    {
      mWindowZOrder = 0;
      createSessionOverlayView();
    }
    if (mSurfaceView != null)
    {
      mSurfaceView.setZOrderOnTop(false);
      mSurfaceView.setZOrderMediaOverlay(paramBoolean);
    }
  }
  
  public void setZOrderOnTop(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      mWindowZOrder = 2;
      removeSessionOverlayView();
    }
    else
    {
      mWindowZOrder = 0;
      createSessionOverlayView();
    }
    if (mSurfaceView != null)
    {
      mSurfaceView.setZOrderMediaOverlay(false);
      mSurfaceView.setZOrderOnTop(paramBoolean);
    }
  }
  
  public void timeShiftPause()
  {
    if (mSession != null) {
      mSession.timeShiftPause();
    }
  }
  
  public void timeShiftPlay(String paramString, Uri paramUri)
  {
    if (!TextUtils.isEmpty(paramString)) {
      synchronized (sMainTvViewLock)
      {
        if (sMainTvView.get() == null)
        {
          WeakReference localWeakReference = new java/lang/ref/WeakReference;
          localWeakReference.<init>(this);
          sMainTvView = localWeakReference;
        }
        if ((mSessionCallback != null) && (TextUtils.equals(mSessionCallback.mInputId, paramString)))
        {
          if (mSession != null) {
            mSession.timeShiftPlay(paramUri);
          } else {
            mSessionCallback.mRecordedProgramUri = paramUri;
          }
        }
        else
        {
          resetInternal();
          mSessionCallback = new MySessionCallback(paramString, paramUri);
          if (mTvInputManager != null) {
            mTvInputManager.createSession(paramString, mSessionCallback, mHandler);
          }
        }
        return;
      }
    }
    throw new IllegalArgumentException("inputId cannot be null or an empty string");
  }
  
  public void timeShiftResume()
  {
    if (mSession != null) {
      mSession.timeShiftResume();
    }
  }
  
  public void timeShiftSeekTo(long paramLong)
  {
    if (mSession != null) {
      mSession.timeShiftSeekTo(paramLong);
    }
  }
  
  public void timeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams)
  {
    if (mSession != null) {
      mSession.timeShiftSetPlaybackParams(paramPlaybackParams);
    }
  }
  
  public void tune(String paramString, Uri paramUri)
  {
    tune(paramString, paramUri, null);
  }
  
  public void tune(String paramString, Uri paramUri, Bundle paramBundle)
  {
    if (!TextUtils.isEmpty(paramString)) {
      synchronized (sMainTvViewLock)
      {
        if (sMainTvView.get() == null)
        {
          WeakReference localWeakReference = new java/lang/ref/WeakReference;
          localWeakReference.<init>(this);
          sMainTvView = localWeakReference;
        }
        if ((mSessionCallback != null) && (TextUtils.equals(mSessionCallback.mInputId, paramString)))
        {
          if (mSession != null)
          {
            mSession.tune(paramUri, paramBundle);
          }
          else
          {
            mSessionCallback.mChannelUri = paramUri;
            mSessionCallback.mTuneParams = paramBundle;
          }
        }
        else
        {
          resetInternal();
          mSessionCallback = new MySessionCallback(paramString, paramUri, paramBundle);
          if (mTvInputManager != null) {
            mTvInputManager.createSession(paramString, mSessionCallback, mHandler);
          }
        }
        return;
      }
    }
    throw new IllegalArgumentException("inputId cannot be null or an empty string");
  }
  
  @SystemApi
  public void unblockContent(TvContentRating paramTvContentRating)
  {
    if (mSession != null) {
      mSession.unblockContent(paramTvContentRating);
    }
  }
  
  private class MySessionCallback
    extends TvInputManager.SessionCallback
  {
    Uri mChannelUri;
    final String mInputId;
    Uri mRecordedProgramUri;
    Bundle mTuneParams;
    
    MySessionCallback(String paramString, Uri paramUri)
    {
      mInputId = paramString;
      mRecordedProgramUri = paramUri;
    }
    
    MySessionCallback(String paramString, Uri paramUri, Bundle paramBundle)
    {
      mInputId = paramString;
      mChannelUri = paramUri;
      mTuneParams = paramBundle;
    }
    
    public void onChannelRetuned(TvInputManager.Session paramSession, Uri paramUri)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onChannelRetuned - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onChannelRetuned(mInputId, paramUri);
      }
    }
    
    public void onContentAllowed(TvInputManager.Session paramSession)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onContentAllowed - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onContentAllowed(mInputId);
      }
    }
    
    public void onContentBlocked(TvInputManager.Session paramSession, TvContentRating paramTvContentRating)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onContentBlocked - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onContentBlocked(mInputId, paramTvContentRating);
      }
    }
    
    public void onLayoutSurface(TvInputManager.Session paramSession, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onLayoutSurface - session not created");
        return;
      }
      TvView.access$2102(TvView.this, paramInt1);
      TvView.access$2202(TvView.this, paramInt2);
      TvView.access$2302(TvView.this, paramInt3);
      TvView.access$2402(TvView.this, paramInt4);
      TvView.access$2502(TvView.this, true);
      requestLayout();
    }
    
    public void onSessionCreated(TvInputManager.Session paramSession)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onSessionCreated - session already created");
        if (paramSession != null) {
          paramSession.release();
        }
        return;
      }
      TvView.access$902(TvView.this, paramSession);
      if (paramSession != null)
      {
        ??? = mPendingAppPrivateCommands.iterator();
        while (((Iterator)???).hasNext())
        {
          paramSession = (Pair)((Iterator)???).next();
          mSession.sendAppPrivateCommand((String)first, (Bundle)second);
        }
        mPendingAppPrivateCommands.clear();
        synchronized (TvView.sMainTvViewLock)
        {
          if ((hasWindowFocus()) && (TvView.this == TvView.sMainTvView.get()) && (TvView.this.checkChangeHdmiCecActiveSourcePermission())) {
            mSession.setMain();
          }
          if (mSurface != null)
          {
            TvView.this.setSessionSurface(mSurface);
            if (mSurfaceChanged) {
              TvView.this.dispatchSurfaceChanged(mSurfaceFormat, mSurfaceWidth, mSurfaceHeight);
            }
          }
          TvView.this.createSessionOverlayView();
          if (mStreamVolume != null) {
            mSession.setStreamVolume(mStreamVolume.floatValue());
          }
          if (mCaptionEnabled != null) {
            mSession.setCaptionEnabled(mCaptionEnabled.booleanValue());
          }
          if (mChannelUri != null) {
            mSession.tune(mChannelUri, mTuneParams);
          } else {
            mSession.timeShiftPlay(mRecordedProgramUri);
          }
          TvView.this.ensurePositionTracking();
        }
      }
      TvView.access$802(TvView.this, null);
      if (mCallback != null) {
        mCallback.onConnectionFailed(mInputId);
      }
    }
    
    public void onSessionEvent(TvInputManager.Session paramSession, String paramString, Bundle paramBundle)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onSessionEvent - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onEvent(mInputId, paramString, paramBundle);
      }
    }
    
    public void onSessionReleased(TvInputManager.Session paramSession)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onSessionReleased - session not created");
        return;
      }
      TvView.access$1902(TvView.this, false);
      TvView.access$2002(TvView.this, null);
      TvView.access$802(TvView.this, null);
      TvView.access$902(TvView.this, null);
      if (mCallback != null) {
        mCallback.onDisconnected(mInputId);
      }
    }
    
    public void onTimeShiftCurrentPositionChanged(TvInputManager.Session paramSession, long paramLong)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onTimeShiftCurrentPositionChanged - session not created");
        return;
      }
      if (mTimeShiftPositionCallback != null) {
        mTimeShiftPositionCallback.onTimeShiftCurrentPositionChanged(mInputId, paramLong);
      }
    }
    
    public void onTimeShiftStartPositionChanged(TvInputManager.Session paramSession, long paramLong)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onTimeShiftStartPositionChanged - session not created");
        return;
      }
      if (mTimeShiftPositionCallback != null) {
        mTimeShiftPositionCallback.onTimeShiftStartPositionChanged(mInputId, paramLong);
      }
    }
    
    public void onTimeShiftStatusChanged(TvInputManager.Session paramSession, int paramInt)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onTimeShiftStatusChanged - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onTimeShiftStatusChanged(mInputId, paramInt);
      }
    }
    
    public void onTrackSelected(TvInputManager.Session paramSession, int paramInt, String paramString)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onTrackSelected - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onTrackSelected(mInputId, paramInt, paramString);
      }
    }
    
    public void onTracksChanged(TvInputManager.Session paramSession, List<TvTrackInfo> paramList)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onTracksChanged - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onTracksChanged(mInputId, paramList);
      }
    }
    
    public void onVideoAvailable(TvInputManager.Session paramSession)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onVideoAvailable - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onVideoAvailable(mInputId);
      }
    }
    
    public void onVideoSizeChanged(TvInputManager.Session paramSession, int paramInt1, int paramInt2)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onVideoSizeChanged - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onVideoSizeChanged(mInputId, paramInt1, paramInt2);
      }
    }
    
    public void onVideoUnavailable(TvInputManager.Session paramSession, int paramInt)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvView", "onVideoUnavailable - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onVideoUnavailable(mInputId, paramInt);
      }
    }
  }
  
  public static abstract interface OnUnhandledInputEventListener
  {
    public abstract boolean onUnhandledInputEvent(InputEvent paramInputEvent);
  }
  
  public static abstract class TimeShiftPositionCallback
  {
    public TimeShiftPositionCallback() {}
    
    public void onTimeShiftCurrentPositionChanged(String paramString, long paramLong) {}
    
    public void onTimeShiftStartPositionChanged(String paramString, long paramLong) {}
  }
  
  public static abstract class TvInputCallback
  {
    public TvInputCallback() {}
    
    public void onChannelRetuned(String paramString, Uri paramUri) {}
    
    public void onConnectionFailed(String paramString) {}
    
    public void onContentAllowed(String paramString) {}
    
    public void onContentBlocked(String paramString, TvContentRating paramTvContentRating) {}
    
    public void onDisconnected(String paramString) {}
    
    @SystemApi
    public void onEvent(String paramString1, String paramString2, Bundle paramBundle) {}
    
    public void onTimeShiftStatusChanged(String paramString, int paramInt) {}
    
    public void onTrackSelected(String paramString1, int paramInt, String paramString2) {}
    
    public void onTracksChanged(String paramString, List<TvTrackInfo> paramList) {}
    
    public void onVideoAvailable(String paramString) {}
    
    public void onVideoSizeChanged(String paramString, int paramInt1, int paramInt2) {}
    
    public void onVideoUnavailable(String paramString, int paramInt) {}
  }
}
