package android.view;

import android.content.Context;
import android.content.res.CompatibilityInfo.Translator;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SurfaceView
  extends View
  implements ViewRootImpl.WindowStoppedCallback
{
  private static final boolean DEBUG = false;
  private static final String TAG = "SurfaceView";
  private boolean mAttachedToWindow;
  final ArrayList<SurfaceHolder.Callback> mCallbacks = new ArrayList();
  final Configuration mConfiguration = new Configuration();
  SurfaceControl mDeferredDestroySurfaceControl;
  boolean mDrawFinished = false;
  private final ViewTreeObserver.OnPreDrawListener mDrawListener = new ViewTreeObserver.OnPreDrawListener()
  {
    public boolean onPreDraw()
    {
      SurfaceView localSurfaceView = SurfaceView.this;
      boolean bool;
      if ((getWidth() > 0) && (getHeight() > 0)) {
        bool = true;
      } else {
        bool = false;
      }
      mHaveFrame = bool;
      updateSurface();
      return true;
    }
  };
  boolean mDrawingStopped = true;
  int mFormat = -1;
  private boolean mGlobalListenersAdded;
  boolean mHaveFrame = false;
  boolean mIsCreating = false;
  long mLastLockTime = 0L;
  int mLastSurfaceHeight = -1;
  int mLastSurfaceWidth = -1;
  boolean mLastWindowVisibility = false;
  final int[] mLocation = new int[2];
  private int mPendingReportDraws;
  private Rect mRTLastReportedPosition = new Rect();
  int mRequestedFormat = 4;
  int mRequestedHeight = -1;
  boolean mRequestedVisible = false;
  int mRequestedWidth = -1;
  private volatile boolean mRtHandlingPositionUpdates = false;
  private SurfaceControl.Transaction mRtTransaction = new SurfaceControl.Transaction();
  final Rect mScreenRect = new Rect();
  private final ViewTreeObserver.OnScrollChangedListener mScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener()
  {
    public void onScrollChanged()
    {
      updateSurface();
    }
  };
  int mSubLayer = -2;
  final Surface mSurface = new Surface();
  SurfaceControlWithBackground mSurfaceControl;
  boolean mSurfaceCreated = false;
  private int mSurfaceFlags = 4;
  final Rect mSurfaceFrame = new Rect();
  int mSurfaceHeight = -1;
  private final SurfaceHolder mSurfaceHolder = new SurfaceHolder()
  {
    private static final String LOG_TAG = "SurfaceHolder";
    
    private Canvas internalLockCanvas(Rect paramAnonymousRect, boolean paramAnonymousBoolean)
    {
      mSurfaceLock.lock();
      Object localObject1 = null;
      Object localObject2 = localObject1;
      if (!mDrawingStopped)
      {
        localObject2 = localObject1;
        if (mSurfaceControl != null)
        {
          if (paramAnonymousBoolean) {
            try
            {
              paramAnonymousRect = mSurface.lockHardwareCanvas();
            }
            catch (Exception paramAnonymousRect)
            {
              break label78;
            }
          } else {
            paramAnonymousRect = mSurface.lockCanvas(paramAnonymousRect);
          }
          localObject2 = paramAnonymousRect;
          break label90;
          label78:
          Log.e("SurfaceHolder", "Exception locking surface", paramAnonymousRect);
          localObject2 = localObject1;
        }
      }
      label90:
      if (localObject2 != null)
      {
        mLastLockTime = SystemClock.uptimeMillis();
        return localObject2;
      }
      long l1 = SystemClock.uptimeMillis();
      long l2 = mLastLockTime + 100L;
      long l3 = l1;
      if (l2 > l1)
      {
        try
        {
          Thread.sleep(l2 - l1);
        }
        catch (InterruptedException paramAnonymousRect) {}
        l3 = SystemClock.uptimeMillis();
      }
      mLastLockTime = l3;
      mSurfaceLock.unlock();
      return null;
    }
    
    public void addCallback(SurfaceHolder.Callback paramAnonymousCallback)
    {
      synchronized (mCallbacks)
      {
        if (!mCallbacks.contains(paramAnonymousCallback)) {
          mCallbacks.add(paramAnonymousCallback);
        }
        return;
      }
    }
    
    public Surface getSurface()
    {
      return mSurface;
    }
    
    public Rect getSurfaceFrame()
    {
      return mSurfaceFrame;
    }
    
    public boolean isCreating()
    {
      return mIsCreating;
    }
    
    public Canvas lockCanvas()
    {
      return internalLockCanvas(null, false);
    }
    
    public Canvas lockCanvas(Rect paramAnonymousRect)
    {
      return internalLockCanvas(paramAnonymousRect, false);
    }
    
    public Canvas lockHardwareCanvas()
    {
      return internalLockCanvas(null, true);
    }
    
    public void removeCallback(SurfaceHolder.Callback paramAnonymousCallback)
    {
      synchronized (mCallbacks)
      {
        mCallbacks.remove(paramAnonymousCallback);
        return;
      }
    }
    
    public void setFixedSize(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if ((mRequestedWidth != paramAnonymousInt1) || (mRequestedHeight != paramAnonymousInt2))
      {
        mRequestedWidth = paramAnonymousInt1;
        mRequestedHeight = paramAnonymousInt2;
        requestLayout();
      }
    }
    
    public void setFormat(int paramAnonymousInt)
    {
      int i = paramAnonymousInt;
      if (paramAnonymousInt == -1) {
        i = 4;
      }
      mRequestedFormat = i;
      if (mSurfaceControl != null) {
        updateSurface();
      }
    }
    
    public void setKeepScreenOn(boolean paramAnonymousBoolean)
    {
      SurfaceView.this.runOnUiThread(new _..Lambda.SurfaceView.3.XvaZSTTyv1kHN4GtX5NDdmQTRp8(this, paramAnonymousBoolean));
    }
    
    public void setSizeFromLayout()
    {
      if ((mRequestedWidth != -1) || (mRequestedHeight != -1))
      {
        SurfaceView localSurfaceView = SurfaceView.this;
        mRequestedHeight = -1;
        mRequestedWidth = -1;
        requestLayout();
      }
    }
    
    @Deprecated
    public void setType(int paramAnonymousInt) {}
    
    public void unlockCanvasAndPost(Canvas paramAnonymousCanvas)
    {
      mSurface.unlockCanvasAndPost(paramAnonymousCanvas);
      mSurfaceLock.unlock();
    }
  };
  final ReentrantLock mSurfaceLock = new ReentrantLock();
  SurfaceSession mSurfaceSession;
  int mSurfaceWidth = -1;
  final Rect mTmpRect = new Rect();
  private CompatibilityInfo.Translator mTranslator;
  boolean mViewVisibility = false;
  boolean mVisible = false;
  int mWindowSpaceLeft = -1;
  int mWindowSpaceTop = -1;
  boolean mWindowStopped = false;
  boolean mWindowVisibility = false;
  
  public SurfaceView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SurfaceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SurfaceView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SurfaceView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mRenderNode.requestPositionUpdates(this);
    setWillNotDraw(true);
  }
  
  private void applySurfaceTransforms(SurfaceControl paramSurfaceControl, Rect paramRect, long paramLong)
  {
    if (paramLong > 0L)
    {
      ViewRootImpl localViewRootImpl = getViewRootImpl();
      mRtTransaction.deferTransactionUntilSurface(paramSurfaceControl, mSurface, paramLong);
    }
    mRtTransaction.setPosition(paramSurfaceControl, left, top);
    mRtTransaction.setMatrix(paramSurfaceControl, paramRect.width() / mSurfaceWidth, 0.0F, 0.0F, paramRect.height() / mSurfaceHeight);
  }
  
  private Rect getParentSurfaceInsets()
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl == null) {
      return null;
    }
    return mWindowAttributes.surfaceInsets;
  }
  
  private SurfaceHolder.Callback[] getSurfaceCallbacks()
  {
    synchronized (mCallbacks)
    {
      SurfaceHolder.Callback[] arrayOfCallback = new SurfaceHolder.Callback[mCallbacks.size()];
      mCallbacks.toArray(arrayOfCallback);
      return arrayOfCallback;
    }
  }
  
  private boolean isAboveParent()
  {
    boolean bool;
    if (mSubLayer >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void onDrawFinished()
  {
    if (mDeferredDestroySurfaceControl != null)
    {
      mDeferredDestroySurfaceControl.destroy();
      mDeferredDestroySurfaceControl = null;
    }
    runOnUiThread(new _..Lambda.SurfaceView.Cs7TGTdA1lXf9qW8VOJAfEsMjdk(this));
  }
  
  private void performDrawFinished()
  {
    if (mPendingReportDraws > 0)
    {
      mDrawFinished = true;
      if (mAttachedToWindow)
      {
        notifyDrawFinished();
        invalidate();
      }
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(System.identityHashCode(this));
      localStringBuilder.append("finished drawing but no pending report draw (extra call to draw completion runnable?)");
      Log.e("SurfaceView", localStringBuilder.toString());
    }
  }
  
  private void runOnUiThread(Runnable paramRunnable)
  {
    Handler localHandler = getHandler();
    if ((localHandler != null) && (localHandler.getLooper() != Looper.myLooper())) {
      localHandler.post(paramRunnable);
    } else {
      paramRunnable.run();
    }
  }
  
  private void setParentSpaceRectangle(Rect paramRect, long paramLong)
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    applySurfaceTransforms(mSurfaceControl, paramRect, paramLong);
    applySurfaceTransforms(mSurfaceControl.mBackgroundControl, paramRect, paramLong);
    applyChildSurfaceTransaction_renderWorker(mRtTransaction, mSurface, paramLong);
    mRtTransaction.apply();
  }
  
  private void updateOpaqueFlag()
  {
    if (!PixelFormat.formatHasAlpha(mRequestedFormat)) {
      mSurfaceFlags |= 0x400;
    } else {
      mSurfaceFlags &= 0xFBFF;
    }
  }
  
  private void updateRequestedVisibility()
  {
    boolean bool;
    if ((mViewVisibility) && (mWindowVisibility) && (!mWindowStopped)) {
      bool = true;
    } else {
      bool = false;
    }
    mRequestedVisible = bool;
  }
  
  protected void applyChildSurfaceTransaction_renderWorker(SurfaceControl.Transaction paramTransaction, Surface paramSurface, long paramLong) {}
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    if ((mDrawFinished) && (!isAboveParent()) && ((mPrivateFlags & 0x80) == 128)) {
      paramCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }
    super.dispatchDraw(paramCanvas);
  }
  
  public void draw(Canvas paramCanvas)
  {
    if ((mDrawFinished) && (!isAboveParent()) && ((mPrivateFlags & 0x80) == 0)) {
      paramCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }
    super.draw(paramCanvas);
  }
  
  public boolean gatherTransparentRegion(Region paramRegion)
  {
    if ((!isAboveParent()) && (mDrawFinished))
    {
      boolean bool1 = true;
      boolean bool2;
      if ((mPrivateFlags & 0x80) == 0)
      {
        bool2 = super.gatherTransparentRegion(paramRegion);
      }
      else
      {
        bool2 = bool1;
        if (paramRegion != null)
        {
          int i = getWidth();
          int j = getHeight();
          bool2 = bool1;
          if (i > 0)
          {
            bool2 = bool1;
            if (j > 0)
            {
              getLocationInWindow(mLocation);
              int k = mLocation[0];
              int m = mLocation[1];
              paramRegion.op(k, m, k + i, m + j, Region.Op.UNION);
              bool2 = bool1;
            }
          }
        }
      }
      if (PixelFormat.formatHasAlpha(mRequestedFormat)) {
        bool2 = false;
      }
      return bool2;
    }
    return super.gatherTransparentRegion(paramRegion);
  }
  
  public SurfaceHolder getHolder()
  {
    return mSurfaceHolder;
  }
  
  public boolean isFixedSize()
  {
    boolean bool;
    if ((mRequestedWidth == -1) && (mRequestedHeight == -1)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  void notifyDrawFinished()
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl != null) {
      localViewRootImpl.pendingDrawFinished();
    }
    mPendingReportDraws -= 1;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    getViewRootImpl().addWindowStoppedCallback(this);
    boolean bool = false;
    mWindowStopped = false;
    if (getVisibility() == 0) {
      bool = true;
    }
    mViewVisibility = bool;
    updateRequestedVisibility();
    mAttachedToWindow = true;
    mParent.requestTransparentRegion(this);
    if (!mGlobalListenersAdded)
    {
      ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
      localViewTreeObserver.addOnScrollChangedListener(mScrollChangedListener);
      localViewTreeObserver.addOnPreDrawListener(mDrawListener);
      mGlobalListenersAdded = true;
    }
  }
  
  protected void onDetachedFromWindow()
  {
    Object localObject = getViewRootImpl();
    if (localObject != null) {
      ((ViewRootImpl)localObject).removeWindowStoppedCallback(this);
    }
    mAttachedToWindow = false;
    if (mGlobalListenersAdded)
    {
      localObject = getViewTreeObserver();
      ((ViewTreeObserver)localObject).removeOnScrollChangedListener(mScrollChangedListener);
      ((ViewTreeObserver)localObject).removeOnPreDrawListener(mDrawListener);
      mGlobalListenersAdded = false;
    }
    while (mPendingReportDraws > 0) {
      notifyDrawFinished();
    }
    mRequestedVisible = false;
    updateSurface();
    if (mSurfaceControl != null) {
      mSurfaceControl.destroy();
    }
    mSurfaceControl = null;
    mHaveFrame = false;
    super.onDetachedFromWindow();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mRequestedWidth >= 0) {
      paramInt1 = resolveSizeAndState(mRequestedWidth, paramInt1, 0);
    } else {
      paramInt1 = getDefaultSize(0, paramInt1);
    }
    if (mRequestedHeight >= 0) {
      paramInt2 = resolveSizeAndState(mRequestedHeight, paramInt2, 0);
    } else {
      paramInt2 = getDefaultSize(0, paramInt2);
    }
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mWindowVisibility = bool;
    updateRequestedVisibility();
    updateSurface();
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    updateSurface();
    return bool;
  }
  
  public void setResizeBackgroundColor(int paramInt)
  {
    mSurfaceControl.setBackgroundColor(paramInt);
  }
  
  public void setSecure(boolean paramBoolean)
  {
    if (paramBoolean) {
      mSurfaceFlags |= 0x80;
    } else {
      mSurfaceFlags &= 0xFF7F;
    }
  }
  
  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    boolean bool1 = false;
    if (paramInt == 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mViewVisibility = bool2;
    boolean bool2 = bool1;
    if (mWindowVisibility)
    {
      bool2 = bool1;
      if (mViewVisibility)
      {
        bool2 = bool1;
        if (!mWindowStopped) {
          bool2 = true;
        }
      }
    }
    if (bool2 != mRequestedVisible) {
      requestLayout();
    }
    mRequestedVisible = bool2;
    updateSurface();
  }
  
  public void setZOrderMediaOverlay(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = -1;
    } else {
      i = -2;
    }
    mSubLayer = i;
  }
  
  public void setZOrderOnTop(boolean paramBoolean)
  {
    if (paramBoolean) {
      mSubLayer = 1;
    } else {
      mSubLayer = -2;
    }
  }
  
  public final void surfacePositionLost_uiRtSync(long paramLong)
  {
    mRTLastReportedPosition.setEmpty();
    if (mSurfaceControl == null) {
      return;
    }
    if (mRtHandlingPositionUpdates)
    {
      mRtHandlingPositionUpdates = false;
      if ((!mScreenRect.isEmpty()) && (!mScreenRect.equals(mRTLastReportedPosition))) {
        try
        {
          setParentSpaceRectangle(mScreenRect, paramLong);
        }
        catch (Exception localException)
        {
          Log.e("SurfaceView", "Exception configuring surface", localException);
        }
      }
    }
  }
  
  /* Error */
  protected void updateSurface()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 164	android/view/SurfaceView:mHaveFrame	Z
    //   4: ifne +4 -> 8
    //   7: return
    //   8: aload_0
    //   9: invokevirtual 223	android/view/SurfaceView:getViewRootImpl	()Landroid/view/ViewRootImpl;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull +1867 -> 1881
    //   17: aload_1
    //   18: getfield 226	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   21: ifnull +1860 -> 1881
    //   24: aload_1
    //   25: getfield 226	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   28: invokevirtual 548	android/view/Surface:isValid	()Z
    //   31: ifne +6 -> 37
    //   34: goto +1847 -> 1881
    //   37: aload_0
    //   38: aload_1
    //   39: getfield 550	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   42: putfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   45: aload_0
    //   46: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   49: ifnull +14 -> 63
    //   52: aload_0
    //   53: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   56: aload_0
    //   57: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   60: invokevirtual 555	android/view/Surface:setCompatibilityTranslator	(Landroid/content/res/CompatibilityInfo$Translator;)V
    //   63: aload_0
    //   64: getfield 158	android/view/SurfaceView:mRequestedWidth	I
    //   67: istore_2
    //   68: iload_2
    //   69: istore_3
    //   70: iload_2
    //   71: ifgt +8 -> 79
    //   74: aload_0
    //   75: invokevirtual 410	android/view/SurfaceView:getWidth	()I
    //   78: istore_3
    //   79: aload_0
    //   80: getfield 160	android/view/SurfaceView:mRequestedHeight	I
    //   83: istore_2
    //   84: iload_2
    //   85: istore 4
    //   87: iload_2
    //   88: ifgt +9 -> 97
    //   91: aload_0
    //   92: invokevirtual 413	android/view/SurfaceView:getHeight	()I
    //   95: istore 4
    //   97: aload_0
    //   98: getfield 180	android/view/SurfaceView:mFormat	I
    //   101: aload_0
    //   102: getfield 162	android/view/SurfaceView:mRequestedFormat	I
    //   105: if_icmpeq +8 -> 113
    //   108: iconst_1
    //   109: istore_2
    //   110: goto +5 -> 115
    //   113: iconst_0
    //   114: istore_2
    //   115: aload_0
    //   116: getfield 170	android/view/SurfaceView:mVisible	Z
    //   119: aload_0
    //   120: getfield 148	android/view/SurfaceView:mRequestedVisible	Z
    //   123: if_icmpeq +9 -> 132
    //   126: iconst_1
    //   127: istore 5
    //   129: goto +6 -> 135
    //   132: iconst_0
    //   133: istore 5
    //   135: aload_0
    //   136: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   139: ifnull +12 -> 151
    //   142: iload_2
    //   143: ifne +8 -> 151
    //   146: iload 5
    //   148: ifeq +16 -> 164
    //   151: aload_0
    //   152: getfield 148	android/view/SurfaceView:mRequestedVisible	Z
    //   155: ifeq +9 -> 164
    //   158: iconst_1
    //   159: istore 6
    //   161: goto +6 -> 167
    //   164: iconst_0
    //   165: istore 6
    //   167: aload_0
    //   168: getfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   171: iload_3
    //   172: if_icmpne +21 -> 193
    //   175: aload_0
    //   176: getfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   179: iload 4
    //   181: if_icmpeq +6 -> 187
    //   184: goto +9 -> 193
    //   187: iconst_0
    //   188: istore 7
    //   190: goto +6 -> 196
    //   193: iconst_1
    //   194: istore 7
    //   196: aload_0
    //   197: getfield 150	android/view/SurfaceView:mWindowVisibility	Z
    //   200: aload_0
    //   201: getfield 152	android/view/SurfaceView:mLastWindowVisibility	Z
    //   204: if_icmpeq +9 -> 213
    //   207: iconst_1
    //   208: istore 8
    //   210: goto +6 -> 216
    //   213: iconst_0
    //   214: istore 8
    //   216: iload 6
    //   218: ifne +268 -> 486
    //   221: iload_2
    //   222: ifne +264 -> 486
    //   225: iload 7
    //   227: ifne +259 -> 486
    //   230: iload 5
    //   232: ifne +254 -> 486
    //   235: iload 8
    //   237: ifeq +6 -> 243
    //   240: goto +246 -> 486
    //   243: aload_0
    //   244: aload_0
    //   245: getfield 106	android/view/SurfaceView:mLocation	[I
    //   248: invokevirtual 558	android/view/SurfaceView:getLocationInSurface	([I)V
    //   251: aload_0
    //   252: getfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   255: aload_0
    //   256: getfield 106	android/view/SurfaceView:mLocation	[I
    //   259: iconst_0
    //   260: iaload
    //   261: if_icmpne +24 -> 285
    //   264: aload_0
    //   265: getfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   268: aload_0
    //   269: getfield 106	android/view/SurfaceView:mLocation	[I
    //   272: iconst_1
    //   273: iaload
    //   274: if_icmpeq +6 -> 280
    //   277: goto +8 -> 285
    //   280: iconst_0
    //   281: istore_2
    //   282: goto +5 -> 287
    //   285: iconst_1
    //   286: istore_2
    //   287: aload_0
    //   288: invokevirtual 410	android/view/SurfaceView:getWidth	()I
    //   291: aload_0
    //   292: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   295: invokevirtual 244	android/graphics/Rect:width	()I
    //   298: if_icmpne +25 -> 323
    //   301: aload_0
    //   302: invokevirtual 413	android/view/SurfaceView:getHeight	()I
    //   305: aload_0
    //   306: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   309: invokevirtual 247	android/graphics/Rect:height	()I
    //   312: if_icmpeq +6 -> 318
    //   315: goto +8 -> 323
    //   318: iconst_0
    //   319: istore_3
    //   320: goto +5 -> 325
    //   323: iconst_1
    //   324: istore_3
    //   325: iload_2
    //   326: ifne +13 -> 339
    //   329: iload_3
    //   330: ifeq +6 -> 336
    //   333: goto +6 -> 339
    //   336: goto +147 -> 483
    //   339: aload_0
    //   340: aload_0
    //   341: getfield 106	android/view/SurfaceView:mLocation	[I
    //   344: iconst_0
    //   345: iaload
    //   346: putfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   349: aload_0
    //   350: aload_0
    //   351: getfield 106	android/view/SurfaceView:mLocation	[I
    //   354: iconst_1
    //   355: iaload
    //   356: putfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   359: aload_0
    //   360: getfield 106	android/view/SurfaceView:mLocation	[I
    //   363: iconst_0
    //   364: aload_0
    //   365: invokevirtual 410	android/view/SurfaceView:getWidth	()I
    //   368: iastore
    //   369: aload_0
    //   370: getfield 106	android/view/SurfaceView:mLocation	[I
    //   373: iconst_1
    //   374: aload_0
    //   375: invokevirtual 413	android/view/SurfaceView:getHeight	()I
    //   378: iastore
    //   379: aload_0
    //   380: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   383: aload_0
    //   384: getfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   387: aload_0
    //   388: getfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   391: aload_0
    //   392: getfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   395: aload_0
    //   396: getfield 106	android/view/SurfaceView:mLocation	[I
    //   399: iconst_0
    //   400: iaload
    //   401: iadd
    //   402: aload_0
    //   403: getfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   406: aload_0
    //   407: getfield 106	android/view/SurfaceView:mLocation	[I
    //   410: iconst_1
    //   411: iaload
    //   412: iadd
    //   413: invokevirtual 562	android/graphics/Rect:set	(IIII)V
    //   416: aload_0
    //   417: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   420: ifnull +14 -> 434
    //   423: aload_0
    //   424: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   427: aload_0
    //   428: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   431: invokevirtual 568	android/content/res/CompatibilityInfo$Translator:translateRectInAppWindowToScreen	(Landroid/graphics/Rect;)V
    //   434: aload_0
    //   435: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   438: ifnonnull +4 -> 442
    //   441: return
    //   442: aload_0
    //   443: invokevirtual 571	android/view/SurfaceView:isHardwareAccelerated	()Z
    //   446: ifeq +10 -> 456
    //   449: aload_0
    //   450: getfield 138	android/view/SurfaceView:mRtHandlingPositionUpdates	Z
    //   453: ifne +30 -> 483
    //   456: aload_0
    //   457: aload_0
    //   458: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   461: ldc2_w 572
    //   464: invokespecial 540	android/view/SurfaceView:setParentSpaceRectangle	(Landroid/graphics/Rect;J)V
    //   467: goto +16 -> 483
    //   470: astore 9
    //   472: ldc 22
    //   474: ldc_w 542
    //   477: aload 9
    //   479: invokestatic 545	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   482: pop
    //   483: goto +1397 -> 1880
    //   486: aload_0
    //   487: aload_0
    //   488: getfield 106	android/view/SurfaceView:mLocation	[I
    //   491: invokevirtual 417	android/view/SurfaceView:getLocationInWindow	([I)V
    //   494: aload_0
    //   495: getfield 148	android/view/SurfaceView:mRequestedVisible	Z
    //   498: istore 10
    //   500: aload_0
    //   501: iload 10
    //   503: putfield 170	android/view/SurfaceView:mVisible	Z
    //   506: aload_0
    //   507: aload_0
    //   508: getfield 106	android/view/SurfaceView:mLocation	[I
    //   511: iconst_0
    //   512: iaload
    //   513: putfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   516: aload_0
    //   517: aload_0
    //   518: getfield 106	android/view/SurfaceView:mLocation	[I
    //   521: iconst_1
    //   522: iaload
    //   523: putfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   526: aload_0
    //   527: iload_3
    //   528: putfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   531: aload_0
    //   532: iload 4
    //   534: putfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   537: aload_0
    //   538: aload_0
    //   539: getfield 162	android/view/SurfaceView:mRequestedFormat	I
    //   542: putfield 180	android/view/SurfaceView:mFormat	I
    //   545: aload_0
    //   546: aload_0
    //   547: getfield 150	android/view/SurfaceView:mWindowVisibility	Z
    //   550: putfield 152	android/view/SurfaceView:mLastWindowVisibility	Z
    //   553: aload_0
    //   554: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   557: aload_0
    //   558: getfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   561: putfield 233	android/graphics/Rect:left	I
    //   564: aload_0
    //   565: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   568: aload_0
    //   569: getfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   572: putfield 236	android/graphics/Rect:top	I
    //   575: aload_0
    //   576: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   579: aload_0
    //   580: getfield 172	android/view/SurfaceView:mWindowSpaceLeft	I
    //   583: aload_0
    //   584: invokevirtual 410	android/view/SurfaceView:getWidth	()I
    //   587: iadd
    //   588: putfield 576	android/graphics/Rect:right	I
    //   591: aload_0
    //   592: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   595: aload_0
    //   596: getfield 174	android/view/SurfaceView:mWindowSpaceTop	I
    //   599: aload_0
    //   600: invokevirtual 413	android/view/SurfaceView:getHeight	()I
    //   603: iadd
    //   604: putfield 579	android/graphics/Rect:bottom	I
    //   607: aload_0
    //   608: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   611: astore 9
    //   613: aload 9
    //   615: ifnull +22 -> 637
    //   618: aload_0
    //   619: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   622: aload_0
    //   623: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   626: invokevirtual 568	android/content/res/CompatibilityInfo$Translator:translateRectInAppWindowToScreen	(Landroid/graphics/Rect;)V
    //   629: goto +8 -> 637
    //   632: astore 9
    //   634: goto +1235 -> 1869
    //   637: aload_0
    //   638: invokespecial 581	android/view/SurfaceView:getParentSurfaceInsets	()Landroid/graphics/Rect;
    //   641: astore 9
    //   643: aload_0
    //   644: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   647: aload 9
    //   649: getfield 233	android/graphics/Rect:left	I
    //   652: aload 9
    //   654: getfield 236	android/graphics/Rect:top	I
    //   657: invokevirtual 584	android/graphics/Rect:offset	(II)V
    //   660: iload 6
    //   662: ifeq +161 -> 823
    //   665: new 586	android/view/SurfaceSession
    //   668: astore 11
    //   670: aload 11
    //   672: aload_1
    //   673: getfield 226	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   676: invokespecial 589	android/view/SurfaceSession:<init>	(Landroid/view/Surface;)V
    //   679: aload_0
    //   680: aload 11
    //   682: putfield 591	android/view/SurfaceView:mSurfaceSession	Landroid/view/SurfaceSession;
    //   685: aload_0
    //   686: aload_0
    //   687: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   690: putfield 285	android/view/SurfaceView:mDeferredDestroySurfaceControl	Landroid/view/SurfaceControl;
    //   693: aload_0
    //   694: invokespecial 593	android/view/SurfaceView:updateOpaqueFlag	()V
    //   697: new 305	java/lang/StringBuilder
    //   700: astore 11
    //   702: aload 11
    //   704: invokespecial 306	java/lang/StringBuilder:<init>	()V
    //   707: aload 11
    //   709: ldc_w 595
    //   712: invokevirtual 321	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   715: pop
    //   716: aload 11
    //   718: aload_1
    //   719: invokevirtual 599	android/view/ViewRootImpl:getTitle	()Ljava/lang/CharSequence;
    //   722: invokeinterface 602 1 0
    //   727: invokevirtual 321	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   730: pop
    //   731: aload 11
    //   733: invokevirtual 325	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   736: astore 11
    //   738: new 14	android/view/SurfaceView$SurfaceControlWithBackground
    //   741: astore 12
    //   743: aload_0
    //   744: getfield 188	android/view/SurfaceView:mSurfaceFlags	I
    //   747: sipush 1024
    //   750: iand
    //   751: ifeq +9 -> 760
    //   754: iconst_1
    //   755: istore 13
    //   757: goto +6 -> 763
    //   760: iconst_0
    //   761: istore 13
    //   763: new 604	android/view/SurfaceControl$Builder
    //   766: astore 14
    //   768: aload 14
    //   770: aload_0
    //   771: getfield 591	android/view/SurfaceView:mSurfaceSession	Landroid/view/SurfaceSession;
    //   774: invokespecial 607	android/view/SurfaceControl$Builder:<init>	(Landroid/view/SurfaceSession;)V
    //   777: aload 12
    //   779: aload_0
    //   780: aload 11
    //   782: iload 13
    //   784: aload 14
    //   786: aload_0
    //   787: getfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   790: aload_0
    //   791: getfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   794: invokevirtual 611	android/view/SurfaceControl$Builder:setSize	(II)Landroid/view/SurfaceControl$Builder;
    //   797: aload_0
    //   798: getfield 180	android/view/SurfaceView:mFormat	I
    //   801: invokevirtual 615	android/view/SurfaceControl$Builder:setFormat	(I)Landroid/view/SurfaceControl$Builder;
    //   804: aload_0
    //   805: getfield 188	android/view/SurfaceView:mSurfaceFlags	I
    //   808: invokevirtual 618	android/view/SurfaceControl$Builder:setFlags	(I)Landroid/view/SurfaceControl$Builder;
    //   811: invokespecial 621	android/view/SurfaceView$SurfaceControlWithBackground:<init>	(Landroid/view/SurfaceView;Ljava/lang/String;ZLandroid/view/SurfaceControl$Builder;)V
    //   814: aload_0
    //   815: aload 12
    //   817: putfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   820: goto +11 -> 831
    //   823: aload_0
    //   824: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   827: ifnonnull +4 -> 831
    //   830: return
    //   831: aload_0
    //   832: getfield 111	android/view/SurfaceView:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   835: invokevirtual 624	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   838: iload 10
    //   840: ifne +9 -> 849
    //   843: iconst_1
    //   844: istore 13
    //   846: goto +6 -> 852
    //   849: iconst_0
    //   850: istore 13
    //   852: aload_0
    //   853: iload 13
    //   855: putfield 118	android/view/SurfaceView:mDrawingStopped	Z
    //   858: invokestatic 627	android/view/SurfaceControl:openTransaction	()V
    //   861: aload_0
    //   862: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   865: aload_0
    //   866: getfield 134	android/view/SurfaceView:mSubLayer	I
    //   869: invokevirtual 630	android/view/SurfaceView$SurfaceControlWithBackground:setLayer	(I)V
    //   872: aload_0
    //   873: getfield 154	android/view/SurfaceView:mViewVisibility	Z
    //   876: istore 13
    //   878: iload 13
    //   880: ifeq +18 -> 898
    //   883: aload_0
    //   884: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   887: invokevirtual 633	android/view/SurfaceView$SurfaceControlWithBackground:show	()V
    //   890: goto +15 -> 905
    //   893: astore 9
    //   895: goto +944 -> 1839
    //   898: aload_0
    //   899: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   902: invokevirtual 636	android/view/SurfaceView$SurfaceControlWithBackground:hide	()V
    //   905: iload 7
    //   907: ifne +19 -> 926
    //   910: iload 6
    //   912: ifne +14 -> 926
    //   915: aload_0
    //   916: getfield 138	android/view/SurfaceView:mRtHandlingPositionUpdates	Z
    //   919: istore 13
    //   921: iload 13
    //   923: ifne +63 -> 986
    //   926: aload_0
    //   927: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   930: aload_0
    //   931: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   934: getfield 233	android/graphics/Rect:left	I
    //   937: i2f
    //   938: aload_0
    //   939: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   942: getfield 236	android/graphics/Rect:top	I
    //   945: i2f
    //   946: invokevirtual 639	android/view/SurfaceView$SurfaceControlWithBackground:setPosition	(FF)V
    //   949: aload_0
    //   950: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   953: aload_0
    //   954: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   957: invokevirtual 244	android/graphics/Rect:width	()I
    //   960: i2f
    //   961: aload_0
    //   962: getfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   965: i2f
    //   966: fdiv
    //   967: fconst_0
    //   968: fconst_0
    //   969: aload_0
    //   970: getfield 125	android/view/SurfaceView:mScreenRect	Landroid/graphics/Rect;
    //   973: invokevirtual 247	android/graphics/Rect:height	()I
    //   976: i2f
    //   977: aload_0
    //   978: getfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   981: i2f
    //   982: fdiv
    //   983: invokevirtual 642	android/view/SurfaceView$SurfaceControlWithBackground:setMatrix	(FFFF)V
    //   986: iload 7
    //   988: ifeq +18 -> 1006
    //   991: aload_0
    //   992: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   995: aload_0
    //   996: getfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   999: aload_0
    //   1000: getfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   1003: invokevirtual 644	android/view/SurfaceView$SurfaceControlWithBackground:setSize	(II)V
    //   1006: invokestatic 647	android/view/SurfaceControl:closeTransaction	()V
    //   1009: iload 7
    //   1011: ifne +17 -> 1028
    //   1014: iload 6
    //   1016: ifeq +6 -> 1022
    //   1019: goto +9 -> 1028
    //   1022: iconst_0
    //   1023: istore 8
    //   1025: goto +6 -> 1031
    //   1028: iconst_1
    //   1029: istore 8
    //   1031: aload_0
    //   1032: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1035: iconst_0
    //   1036: putfield 233	android/graphics/Rect:left	I
    //   1039: aload_0
    //   1040: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1043: iconst_0
    //   1044: putfield 236	android/graphics/Rect:top	I
    //   1047: aload_0
    //   1048: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   1051: astore 11
    //   1053: aload 11
    //   1055: ifnonnull +33 -> 1088
    //   1058: aload_0
    //   1059: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1062: aload_0
    //   1063: getfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   1066: putfield 576	android/graphics/Rect:right	I
    //   1069: aload_0
    //   1070: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1073: aload_0
    //   1074: getfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   1077: putfield 579	android/graphics/Rect:bottom	I
    //   1080: goto +57 -> 1137
    //   1083: astore 9
    //   1085: goto +767 -> 1852
    //   1088: aload_0
    //   1089: getfield 551	android/view/SurfaceView:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   1092: getfield 651	android/content/res/CompatibilityInfo$Translator:applicationInvertedScale	F
    //   1095: fstore 15
    //   1097: aload_0
    //   1098: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1101: aload_0
    //   1102: getfield 176	android/view/SurfaceView:mSurfaceWidth	I
    //   1105: i2f
    //   1106: fload 15
    //   1108: fmul
    //   1109: ldc_w 652
    //   1112: fadd
    //   1113: f2i
    //   1114: putfield 576	android/graphics/Rect:right	I
    //   1117: aload_0
    //   1118: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1121: aload_0
    //   1122: getfield 178	android/view/SurfaceView:mSurfaceHeight	I
    //   1125: i2f
    //   1126: fload 15
    //   1128: fmul
    //   1129: ldc_w 652
    //   1132: fadd
    //   1133: f2i
    //   1134: putfield 579	android/graphics/Rect:bottom	I
    //   1137: aload_0
    //   1138: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1141: getfield 576	android/graphics/Rect:right	I
    //   1144: istore 16
    //   1146: aload_0
    //   1147: getfield 182	android/view/SurfaceView:mSurfaceFrame	Landroid/graphics/Rect;
    //   1150: getfield 579	android/graphics/Rect:bottom	I
    //   1153: istore 17
    //   1155: aload_0
    //   1156: getfield 184	android/view/SurfaceView:mLastSurfaceWidth	I
    //   1159: istore 18
    //   1161: iload 18
    //   1163: iload 16
    //   1165: if_icmpne +25 -> 1190
    //   1168: aload_0
    //   1169: getfield 186	android/view/SurfaceView:mLastSurfaceHeight	I
    //   1172: istore 18
    //   1174: iload 18
    //   1176: iload 17
    //   1178: if_icmpeq +6 -> 1184
    //   1181: goto +9 -> 1190
    //   1184: iconst_0
    //   1185: istore 18
    //   1187: goto +6 -> 1193
    //   1190: iconst_1
    //   1191: istore 18
    //   1193: aload_0
    //   1194: iload 16
    //   1196: putfield 184	android/view/SurfaceView:mLastSurfaceWidth	I
    //   1199: aload_0
    //   1200: iload 17
    //   1202: putfield 186	android/view/SurfaceView:mLastSurfaceHeight	I
    //   1205: aload_0
    //   1206: getfield 111	android/view/SurfaceView:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   1209: invokevirtual 655	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   1212: iload 10
    //   1214: ifeq +25 -> 1239
    //   1217: aload_0
    //   1218: getfield 120	android/view/SurfaceView:mDrawFinished	Z
    //   1221: istore 13
    //   1223: iload 13
    //   1225: ifne +14 -> 1239
    //   1228: iconst_1
    //   1229: istore 16
    //   1231: goto +11 -> 1242
    //   1234: astore 9
    //   1236: goto +550 -> 1786
    //   1239: iconst_0
    //   1240: istore 16
    //   1242: aconst_null
    //   1243: astore 11
    //   1245: aload_0
    //   1246: getfield 166	android/view/SurfaceView:mSurfaceCreated	Z
    //   1249: istore 13
    //   1251: iload 13
    //   1253: ifeq +133 -> 1386
    //   1256: iload 6
    //   1258: ifne +19 -> 1277
    //   1261: iload 10
    //   1263: ifne +11 -> 1274
    //   1266: iload 5
    //   1268: ifeq +6 -> 1274
    //   1271: goto +6 -> 1277
    //   1274: goto +112 -> 1386
    //   1277: aload 9
    //   1279: astore 14
    //   1281: aload_0
    //   1282: iconst_0
    //   1283: putfield 166	android/view/SurfaceView:mSurfaceCreated	Z
    //   1286: aload 9
    //   1288: astore 14
    //   1290: aload_0
    //   1291: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1294: invokevirtual 548	android/view/Surface:isValid	()Z
    //   1297: ifeq +89 -> 1386
    //   1300: aload 9
    //   1302: astore 14
    //   1304: aload_0
    //   1305: invokespecial 657	android/view/SurfaceView:getSurfaceCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   1308: astore 11
    //   1310: aload 9
    //   1312: astore 14
    //   1314: aload 11
    //   1316: arraylength
    //   1317: istore 19
    //   1319: iconst_0
    //   1320: istore 17
    //   1322: iload 17
    //   1324: iload 19
    //   1326: if_icmpge +35 -> 1361
    //   1329: aload 11
    //   1331: iload 17
    //   1333: aaload
    //   1334: astore 12
    //   1336: aload 9
    //   1338: astore 14
    //   1340: aload_0
    //   1341: getfield 198	android/view/SurfaceView:mSurfaceHolder	Landroid/view/SurfaceHolder;
    //   1344: astore 14
    //   1346: aload 12
    //   1348: aload 14
    //   1350: invokeinterface 661 2 0
    //   1355: iinc 17 1
    //   1358: goto -36 -> 1322
    //   1361: aload_0
    //   1362: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1365: invokevirtual 548	android/view/Surface:isValid	()Z
    //   1368: ifeq +10 -> 1378
    //   1371: aload_0
    //   1372: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1375: invokevirtual 664	android/view/Surface:forceScopedDisconnect	()V
    //   1378: goto +8 -> 1386
    //   1381: astore 9
    //   1383: goto +403 -> 1786
    //   1386: iload 6
    //   1388: ifeq +22 -> 1410
    //   1391: aload_0
    //   1392: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1395: aload_0
    //   1396: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1399: invokevirtual 668	android/view/Surface:copyFrom	(Landroid/view/SurfaceControl;)V
    //   1402: goto +8 -> 1410
    //   1405: astore 9
    //   1407: goto +379 -> 1786
    //   1410: iload 7
    //   1412: ifeq +29 -> 1441
    //   1415: aload_0
    //   1416: invokevirtual 672	android/view/SurfaceView:getContext	()Landroid/content/Context;
    //   1419: invokevirtual 678	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   1422: getfield 683	android/content/pm/ApplicationInfo:targetSdkVersion	I
    //   1425: bipush 26
    //   1427: if_icmpge +14 -> 1441
    //   1430: aload_0
    //   1431: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1434: aload_0
    //   1435: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1438: invokevirtual 686	android/view/Surface:createFrom	(Landroid/view/SurfaceControl;)V
    //   1441: iload 10
    //   1443: ifeq +300 -> 1743
    //   1446: iload_2
    //   1447: istore 17
    //   1449: aload_0
    //   1450: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1453: invokevirtual 548	android/view/Surface:isValid	()Z
    //   1456: ifeq +287 -> 1743
    //   1459: iload_2
    //   1460: istore 17
    //   1462: aload_0
    //   1463: getfield 166	android/view/SurfaceView:mSurfaceCreated	Z
    //   1466: istore 13
    //   1468: aload 11
    //   1470: astore 9
    //   1472: iload 13
    //   1474: ifne +77 -> 1551
    //   1477: iload 6
    //   1479: ifne +12 -> 1491
    //   1482: aload 11
    //   1484: astore 9
    //   1486: iload 5
    //   1488: ifeq +63 -> 1551
    //   1491: aload_0
    //   1492: iconst_1
    //   1493: putfield 166	android/view/SurfaceView:mSurfaceCreated	Z
    //   1496: aload_0
    //   1497: iconst_1
    //   1498: putfield 136	android/view/SurfaceView:mIsCreating	Z
    //   1501: aload 11
    //   1503: astore 9
    //   1505: aload 11
    //   1507: ifnonnull +9 -> 1516
    //   1510: aload_0
    //   1511: invokespecial 657	android/view/SurfaceView:getSurfaceCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   1514: astore 9
    //   1516: aload 9
    //   1518: arraylength
    //   1519: istore 19
    //   1521: iconst_0
    //   1522: istore 17
    //   1524: iload 17
    //   1526: iload 19
    //   1528: if_icmpge +23 -> 1551
    //   1531: aload 9
    //   1533: iload 17
    //   1535: aaload
    //   1536: aload_0
    //   1537: getfield 198	android/view/SurfaceView:mSurfaceHolder	Landroid/view/SurfaceHolder;
    //   1540: invokeinterface 689 2 0
    //   1545: iinc 17 1
    //   1548: goto -24 -> 1524
    //   1551: iload 6
    //   1553: ifne +28 -> 1581
    //   1556: iload_2
    //   1557: ifne +24 -> 1581
    //   1560: iload 7
    //   1562: ifne +19 -> 1581
    //   1565: iload 5
    //   1567: ifne +14 -> 1581
    //   1570: iload 18
    //   1572: ifeq +6 -> 1578
    //   1575: goto +6 -> 1581
    //   1578: goto +78 -> 1656
    //   1581: aload 9
    //   1583: astore 11
    //   1585: aload 9
    //   1587: ifnonnull +9 -> 1596
    //   1590: aload_0
    //   1591: invokespecial 657	android/view/SurfaceView:getSurfaceCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   1594: astore 11
    //   1596: iload_2
    //   1597: istore 17
    //   1599: aload 11
    //   1601: arraylength
    //   1602: istore 6
    //   1604: iconst_0
    //   1605: istore 5
    //   1607: aload 11
    //   1609: astore 9
    //   1611: iload 5
    //   1613: iload 6
    //   1615: if_icmpge +41 -> 1656
    //   1618: aload 9
    //   1620: iload 5
    //   1622: aaload
    //   1623: astore 11
    //   1625: iload_2
    //   1626: istore 17
    //   1628: aload_0
    //   1629: getfield 198	android/view/SurfaceView:mSurfaceHolder	Landroid/view/SurfaceHolder;
    //   1632: astore 14
    //   1634: aload 11
    //   1636: aload 14
    //   1638: aload_0
    //   1639: getfield 180	android/view/SurfaceView:mFormat	I
    //   1642: iload_3
    //   1643: iload 4
    //   1645: invokeinterface 693 5 0
    //   1650: iinc 5 1
    //   1653: goto -42 -> 1611
    //   1656: iload 8
    //   1658: iload 16
    //   1660: ior
    //   1661: ifeq +82 -> 1743
    //   1664: aload 9
    //   1666: astore 11
    //   1668: aload 9
    //   1670: ifnonnull +17 -> 1687
    //   1673: aload_0
    //   1674: invokespecial 657	android/view/SurfaceView:getSurfaceCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   1677: astore 11
    //   1679: goto +8 -> 1687
    //   1682: astore 9
    //   1684: goto +102 -> 1786
    //   1687: aload_0
    //   1688: aload_0
    //   1689: getfield 295	android/view/SurfaceView:mPendingReportDraws	I
    //   1692: iconst_1
    //   1693: iadd
    //   1694: putfield 295	android/view/SurfaceView:mPendingReportDraws	I
    //   1697: aload_1
    //   1698: invokevirtual 696	android/view/ViewRootImpl:drawPending	()V
    //   1701: new 698	com/android/internal/view/SurfaceCallbackHelper
    //   1704: astore 14
    //   1706: new 700	android/view/_$$Lambda$SurfaceView$SyyzxOgxKwZMRgiiTGcRYbOU5JY
    //   1709: astore 9
    //   1711: aload 9
    //   1713: aload_0
    //   1714: invokespecial 701	android/view/_$$Lambda$SurfaceView$SyyzxOgxKwZMRgiiTGcRYbOU5JY:<init>	(Landroid/view/SurfaceView;)V
    //   1717: aload 14
    //   1719: aload 9
    //   1721: invokespecial 703	com/android/internal/view/SurfaceCallbackHelper:<init>	(Ljava/lang/Runnable;)V
    //   1724: aload 14
    //   1726: aload_0
    //   1727: getfield 198	android/view/SurfaceView:mSurfaceHolder	Landroid/view/SurfaceHolder;
    //   1730: aload 11
    //   1732: invokevirtual 707	com/android/internal/view/SurfaceCallbackHelper:dispatchSurfaceRedrawNeededAsync	(Landroid/view/SurfaceHolder;[Landroid/view/SurfaceHolder$Callback;)V
    //   1735: goto +8 -> 1743
    //   1738: astore 9
    //   1740: goto +46 -> 1786
    //   1743: aload_0
    //   1744: iconst_0
    //   1745: putfield 136	android/view/SurfaceView:mIsCreating	Z
    //   1748: aload_0
    //   1749: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1752: ifnull +128 -> 1880
    //   1755: aload_0
    //   1756: getfield 166	android/view/SurfaceView:mSurfaceCreated	Z
    //   1759: ifne +121 -> 1880
    //   1762: aload_0
    //   1763: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1766: invokevirtual 710	android/view/Surface:release	()V
    //   1769: aload_0
    //   1770: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1773: invokevirtual 487	android/view/SurfaceView$SurfaceControlWithBackground:destroy	()V
    //   1776: aload_0
    //   1777: aconst_null
    //   1778: putfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1781: goto +99 -> 1880
    //   1784: astore 9
    //   1786: aload_0
    //   1787: iconst_0
    //   1788: putfield 136	android/view/SurfaceView:mIsCreating	Z
    //   1791: aload_0
    //   1792: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1795: ifnull +29 -> 1824
    //   1798: aload_0
    //   1799: getfield 166	android/view/SurfaceView:mSurfaceCreated	Z
    //   1802: ifne +22 -> 1824
    //   1805: aload_0
    //   1806: getfield 116	android/view/SurfaceView:mSurface	Landroid/view/Surface;
    //   1809: invokevirtual 710	android/view/Surface:release	()V
    //   1812: aload_0
    //   1813: getfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1816: invokevirtual 487	android/view/SurfaceView$SurfaceControlWithBackground:destroy	()V
    //   1819: aload_0
    //   1820: aconst_null
    //   1821: putfield 359	android/view/SurfaceView:mSurfaceControl	Landroid/view/SurfaceView$SurfaceControlWithBackground;
    //   1824: aload 9
    //   1826: athrow
    //   1827: astore 9
    //   1829: goto +40 -> 1869
    //   1832: astore 9
    //   1834: goto +18 -> 1852
    //   1837: astore 9
    //   1839: invokestatic 647	android/view/SurfaceControl:closeTransaction	()V
    //   1842: aload 9
    //   1844: athrow
    //   1845: astore 9
    //   1847: goto +5 -> 1852
    //   1850: astore 9
    //   1852: aload_0
    //   1853: getfield 111	android/view/SurfaceView:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   1856: invokevirtual 655	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   1859: aload 9
    //   1861: athrow
    //   1862: astore 9
    //   1864: goto +5 -> 1869
    //   1867: astore 9
    //   1869: ldc 22
    //   1871: ldc_w 542
    //   1874: aload 9
    //   1876: invokestatic 545	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1879: pop
    //   1880: return
    //   1881: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1882	0	this	SurfaceView
    //   12	1686	1	localViewRootImpl	ViewRootImpl
    //   67	1559	2	i	int
    //   69	1574	3	j	int
    //   85	1559	4	k	int
    //   127	1524	5	m	int
    //   159	1457	6	n	int
    //   188	1373	7	i1	int
    //   208	1453	8	i2	int
    //   470	8	9	localException1	Exception
    //   611	3	9	localTranslator	CompatibilityInfo.Translator
    //   632	1	9	localException2	Exception
    //   641	12	9	localRect	Rect
    //   893	1	9	localObject1	Object
    //   1083	1	9	localObject2	Object
    //   1234	103	9	localObject3	Object
    //   1381	1	9	localObject4	Object
    //   1405	1	9	localObject5	Object
    //   1470	199	9	localObject6	Object
    //   1682	1	9	localObject7	Object
    //   1709	11	9	localSyyzxOgxKwZMRgiiTGcRYbOU5JY	_..Lambda.SurfaceView.SyyzxOgxKwZMRgiiTGcRYbOU5JY
    //   1738	1	9	localObject8	Object
    //   1784	41	9	localObject9	Object
    //   1827	1	9	localException3	Exception
    //   1832	1	9	localObject10	Object
    //   1837	6	9	localObject11	Object
    //   1845	1	9	localObject12	Object
    //   1850	10	9	localObject13	Object
    //   1862	1	9	localException4	Exception
    //   1867	8	9	localException5	Exception
    //   498	944	10	bool1	boolean
    //   668	1063	11	localObject14	Object
    //   741	606	12	localSurfaceControlWithBackground	SurfaceControlWithBackground
    //   755	718	13	bool2	boolean
    //   766	959	14	localObject15	Object
    //   1095	32	15	f	float
    //   1144	517	16	i3	int
    //   1153	474	17	i4	int
    //   1159	412	18	i5	int
    //   1317	212	19	i6	int
    // Exception table:
    //   from	to	target	type
    //   456	467	470	java/lang/Exception
    //   618	629	632	java/lang/Exception
    //   665	754	632	java/lang/Exception
    //   763	820	632	java/lang/Exception
    //   883	890	893	finally
    //   915	921	893	finally
    //   991	1006	893	finally
    //   1058	1080	1083	finally
    //   1168	1174	1083	finally
    //   1217	1223	1234	finally
    //   1281	1286	1381	finally
    //   1290	1300	1381	finally
    //   1304	1310	1381	finally
    //   1314	1319	1381	finally
    //   1340	1346	1381	finally
    //   1346	1355	1405	finally
    //   1361	1378	1405	finally
    //   1391	1402	1405	finally
    //   1415	1441	1405	finally
    //   1491	1501	1405	finally
    //   1510	1516	1405	finally
    //   1516	1521	1405	finally
    //   1531	1545	1405	finally
    //   1590	1596	1405	finally
    //   1634	1650	1682	finally
    //   1673	1679	1682	finally
    //   1687	1735	1682	finally
    //   1449	1459	1738	finally
    //   1462	1468	1738	finally
    //   1599	1604	1738	finally
    //   1628	1634	1738	finally
    //   1245	1251	1784	finally
    //   1205	1212	1827	java/lang/Exception
    //   1031	1053	1832	finally
    //   1088	1137	1832	finally
    //   1137	1161	1832	finally
    //   1193	1205	1832	finally
    //   861	878	1837	finally
    //   898	905	1837	finally
    //   926	986	1837	finally
    //   1839	1845	1845	finally
    //   852	861	1850	finally
    //   1006	1009	1850	finally
    //   1743	1781	1862	java/lang/Exception
    //   1786	1824	1862	java/lang/Exception
    //   1824	1827	1862	java/lang/Exception
    //   1852	1862	1862	java/lang/Exception
    //   494	613	1867	java/lang/Exception
    //   637	660	1867	java/lang/Exception
    //   823	830	1867	java/lang/Exception
    //   831	838	1867	java/lang/Exception
  }
  
  public final void updateSurfacePosition_renderWorker(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mSurfaceControl == null) {
      return;
    }
    mRtHandlingPositionUpdates = true;
    if ((mRTLastReportedPosition.left == paramInt1) && (mRTLastReportedPosition.top == paramInt2) && (mRTLastReportedPosition.right == paramInt3) && (mRTLastReportedPosition.bottom == paramInt4)) {
      return;
    }
    try
    {
      mRTLastReportedPosition.set(paramInt1, paramInt2, paramInt3, paramInt4);
      setParentSpaceRectangle(mRTLastReportedPosition, paramLong);
    }
    catch (Exception localException)
    {
      Log.e("SurfaceView", "Exception from repositionChild", localException);
    }
  }
  
  public void windowStopped(boolean paramBoolean)
  {
    mWindowStopped = paramBoolean;
    updateRequestedVisibility();
    updateSurface();
  }
  
  class SurfaceControlWithBackground
    extends SurfaceControl
  {
    SurfaceControl mBackgroundControl;
    private boolean mOpaque = true;
    public boolean mVisible = false;
    
    public SurfaceControlWithBackground(String paramString, boolean paramBoolean, SurfaceControl.Builder paramBuilder)
      throws Exception
    {
      super();
      this$1 = new StringBuilder();
      append("Background for -");
      append(paramString);
      mBackgroundControl = paramBuilder.setName(toString()).setFormat(1024).setColorLayer(true).build();
      mOpaque = paramBoolean;
    }
    
    private void setBackgroundColor(int paramInt)
    {
      float f1 = Color.red(paramInt) / 255.0F;
      float f2 = Color.green(paramInt) / 255.0F;
      float f3 = Color.blue(paramInt) / 255.0F;
      SurfaceControl.openTransaction();
      try
      {
        mBackgroundControl.setColor(new float[] { f1, f2, f3 });
        return;
      }
      finally
      {
        SurfaceControl.closeTransaction();
      }
    }
    
    public void deferTransactionUntil(IBinder paramIBinder, long paramLong)
    {
      super.deferTransactionUntil(paramIBinder, paramLong);
      mBackgroundControl.deferTransactionUntil(paramIBinder, paramLong);
    }
    
    public void deferTransactionUntil(Surface paramSurface, long paramLong)
    {
      super.deferTransactionUntil(paramSurface, paramLong);
      mBackgroundControl.deferTransactionUntil(paramSurface, paramLong);
    }
    
    public void destroy()
    {
      super.destroy();
      mBackgroundControl.destroy();
    }
    
    public void hide()
    {
      super.hide();
      mVisible = false;
      updateBackgroundVisibility();
    }
    
    public void release()
    {
      super.release();
      mBackgroundControl.release();
    }
    
    public void setAlpha(float paramFloat)
    {
      super.setAlpha(paramFloat);
      mBackgroundControl.setAlpha(paramFloat);
    }
    
    public void setFinalCrop(Rect paramRect)
    {
      super.setFinalCrop(paramRect);
      mBackgroundControl.setFinalCrop(paramRect);
    }
    
    public void setLayer(int paramInt)
    {
      super.setLayer(paramInt);
      mBackgroundControl.setLayer(-3);
    }
    
    public void setLayerStack(int paramInt)
    {
      super.setLayerStack(paramInt);
      mBackgroundControl.setLayerStack(paramInt);
    }
    
    public void setMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      super.setMatrix(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      mBackgroundControl.setMatrix(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    
    public void setOpaque(boolean paramBoolean)
    {
      super.setOpaque(paramBoolean);
      mOpaque = paramBoolean;
      updateBackgroundVisibility();
    }
    
    public void setPosition(float paramFloat1, float paramFloat2)
    {
      super.setPosition(paramFloat1, paramFloat2);
      mBackgroundControl.setPosition(paramFloat1, paramFloat2);
    }
    
    public void setSecure(boolean paramBoolean)
    {
      super.setSecure(paramBoolean);
    }
    
    public void setSize(int paramInt1, int paramInt2)
    {
      super.setSize(paramInt1, paramInt2);
      mBackgroundControl.setSize(paramInt1, paramInt2);
    }
    
    public void setTransparentRegionHint(Region paramRegion)
    {
      super.setTransparentRegionHint(paramRegion);
      mBackgroundControl.setTransparentRegionHint(paramRegion);
    }
    
    public void setWindowCrop(Rect paramRect)
    {
      super.setWindowCrop(paramRect);
      mBackgroundControl.setWindowCrop(paramRect);
    }
    
    public void show()
    {
      super.show();
      mVisible = true;
      updateBackgroundVisibility();
    }
    
    void updateBackgroundVisibility()
    {
      if ((mOpaque) && (mVisible)) {
        mBackgroundControl.show();
      } else {
        mBackgroundControl.hide();
      }
    }
  }
}
