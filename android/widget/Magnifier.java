package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.DisplayListCanvas;
import android.view.PixelCopy;
import android.view.RenderNode;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceControl.Builder;
import android.view.SurfaceHolder;
import android.view.SurfaceSession;
import android.view.SurfaceView;
import android.view.ThreadedRenderer.FrameDrawingCallback;
import android.view.ThreadedRenderer.SimpleRenderer;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowManager.LayoutParams;
import com.android.internal.util.Preconditions;

public final class Magnifier
{
  private static final int NONEXISTENT_PREVIOUS_CONFIG_VALUE = -1;
  private static final HandlerThread sPixelCopyHandlerThread = new HandlerThread("magnifier pixel copy result handler");
  private final int mBitmapHeight;
  private final int mBitmapWidth;
  private Callback mCallback;
  private final Point mCenterZoomCoords = new Point();
  private final Point mClampedCenterZoomCoords = new Point();
  private SurfaceInfo mContentCopySurface;
  private final Object mLock = new Object();
  private SurfaceInfo mParentSurface;
  private final Rect mPixelCopyRequestRect = new Rect();
  private final PointF mPrevPosInView = new PointF(-1.0F, -1.0F);
  private final Point mPrevStartCoordsInSurface = new Point(-1, -1);
  private final View mView;
  private final int[] mViewCoordinatesInSurface;
  private InternalPopupWindow mWindow;
  private final Point mWindowCoords = new Point();
  private final float mWindowCornerRadius;
  private final float mWindowElevation;
  private final int mWindowHeight;
  private final int mWindowWidth;
  private final float mZoom;
  
  static
  {
    sPixelCopyHandlerThread.start();
  }
  
  public Magnifier(View paramView)
  {
    mView = ((View)Preconditions.checkNotNull(paramView));
    paramView = mView.getContext();
    mWindowWidth = paramView.getResources().getDimensionPixelSize(17105284);
    mWindowHeight = paramView.getResources().getDimensionPixelSize(17105282);
    mWindowElevation = paramView.getResources().getDimension(17105281);
    mWindowCornerRadius = getDeviceDefaultDialogCornerRadius();
    mZoom = paramView.getResources().getFloat(17105285);
    mBitmapWidth = Math.round(mWindowWidth / mZoom);
    mBitmapHeight = Math.round(mWindowHeight / mZoom);
    mViewCoordinatesInSurface = new int[2];
  }
  
  private float getDeviceDefaultDialogCornerRadius()
  {
    TypedArray localTypedArray = new ContextThemeWrapper(mView.getContext(), 16974120).obtainStyledAttributes(new int[] { 16844145 });
    float f = localTypedArray.getDimension(0, 0.0F);
    localTypedArray.recycle();
    return f;
  }
  
  public static PointF getMagnifierDefaultSize()
  {
    Resources localResources = Resources.getSystem();
    float f = getDisplayMetricsdensity;
    PointF localPointF = new PointF();
    x = (localResources.getDimension(17105284) / f);
    y = (localResources.getDimension(17105282) / f);
    return localPointF;
  }
  
  private void obtainContentCoordinates(float paramFloat1, float paramFloat2)
  {
    mView.getLocationInSurface(mViewCoordinatesInSurface);
    if (!(mView instanceof SurfaceView))
    {
      paramFloat1 = mViewCoordinatesInSurface[0] + paramFloat1;
      paramFloat2 = mViewCoordinatesInSurface[1] + paramFloat2;
    }
    mCenterZoomCoords.x = Math.round(paramFloat1);
    mCenterZoomCoords.y = Math.round(paramFloat2);
    Rect localRect1 = new Rect();
    mView.getGlobalVisibleRect(localRect1);
    if (mView.getViewRootImpl() != null)
    {
      Rect localRect2 = mView.getViewRootImpl().mWindowAttributes.surfaceInsets;
      localRect1.offset(left, top);
    }
    if ((mView instanceof SurfaceView)) {
      localRect1.offset(-mViewCoordinatesInSurface[0], -mViewCoordinatesInSurface[1]);
    }
    mClampedCenterZoomCoords.x = Math.max(left + mBitmapWidth / 2, Math.min(mCenterZoomCoords.x, right - mBitmapWidth / 2));
    mClampedCenterZoomCoords.y = mCenterZoomCoords.y;
  }
  
  private void obtainSurfaces()
  {
    Object localObject1 = SurfaceInfo.NULL;
    Object localObject2 = localObject1;
    Object localObject3;
    if (mView.getViewRootImpl() != null)
    {
      localObject3 = mView.getViewRootImpl();
      localObject4 = mSurface;
      localObject2 = localObject1;
      if (localObject4 != null)
      {
        localObject2 = localObject1;
        if (((Surface)localObject4).isValid())
        {
          localObject2 = mWindowAttributes.surfaceInsets;
          int i = ((ViewRootImpl)localObject3).getWidth();
          int j = left;
          int k = right;
          localObject2 = new SurfaceInfo((Surface)localObject4, i + j + k, ((ViewRootImpl)localObject3).getHeight() + top + bottom, true);
        }
      }
    }
    Object localObject4 = SurfaceInfo.NULL;
    localObject1 = localObject4;
    if ((mView instanceof SurfaceView))
    {
      SurfaceHolder localSurfaceHolder = ((SurfaceView)mView).getHolder();
      localObject3 = localSurfaceHolder.getSurface();
      localObject1 = localObject4;
      if (localObject3 != null)
      {
        localObject1 = localObject4;
        if (((Surface)localObject3).isValid())
        {
          localObject1 = localSurfaceHolder.getSurfaceFrame();
          localObject1 = new SurfaceInfo((Surface)localObject3, right, bottom, false);
        }
      }
    }
    if (localObject2 != SurfaceInfo.NULL) {
      localObject4 = localObject2;
    } else {
      localObject4 = localObject1;
    }
    mParentSurface = ((SurfaceInfo)localObject4);
    if ((mView instanceof SurfaceView)) {
      localObject2 = localObject1;
    }
    mContentCopySurface = ((SurfaceInfo)localObject2);
  }
  
  private void obtainWindowCoordinates()
  {
    int i = mView.getContext().getResources().getDimensionPixelSize(17105283);
    mWindowCoords.x = (mCenterZoomCoords.x - mWindowWidth / 2);
    mWindowCoords.y = (mCenterZoomCoords.y - mWindowHeight / 2 - i);
    if (mParentSurface != mContentCopySurface)
    {
      Point localPoint = mWindowCoords;
      x += mViewCoordinatesInSurface[0];
      localPoint = mWindowCoords;
      y += mViewCoordinatesInSurface[1];
    }
  }
  
  private void performPixelCopy(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((mContentCopySurface.mSurface != null) && (mContentCopySurface.mSurface.isValid()))
    {
      int i = Math.max(0, Math.min(paramInt1, mContentCopySurface.mWidth - mBitmapWidth));
      int j = Math.max(0, Math.min(paramInt2, mContentCopySurface.mHeight - mBitmapHeight));
      if (mParentSurface.mIsMainWindowSurface)
      {
        localObject = mView.getRootWindowInsets().getSystemWindowInsets();
        localObject = new Rect(left, top, mParentSurface.mWidth - right, mParentSurface.mHeight - bottom);
      }
      else
      {
        localObject = new Rect(0, 0, mParentSurface.mWidth, mParentSurface.mHeight);
      }
      int k = Math.max(left, Math.min(right - mWindowWidth, mWindowCoords.x));
      int m = Math.max(top, Math.min(bottom - mWindowHeight, mWindowCoords.y));
      mPixelCopyRequestRect.set(i, j, mBitmapWidth + i, mBitmapHeight + j);
      Object localObject = mWindow;
      Bitmap localBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
      PixelCopy.request(mContentCopySurface.mSurface, mPixelCopyRequestRect, localBitmap, new _..Lambda.Magnifier.1ctRJdojBZQzahoS7og5wm1FKM4(this, (InternalPopupWindow)localObject, paramBoolean, k, m, localBitmap), sPixelCopyHandlerThread.getThreadHandler());
      mPrevStartCoordsInSurface.x = paramInt1;
      mPrevStartCoordsInSurface.y = paramInt2;
      return;
    }
  }
  
  public void dismiss()
  {
    if (mWindow != null) {
      synchronized (mLock)
      {
        mWindow.destroy();
        mWindow = null;
        mPrevPosInView.x = -1.0F;
        mPrevPosInView.y = -1.0F;
        mPrevStartCoordsInSurface.x = -1;
        mPrevStartCoordsInSurface.y = -1;
      }
    }
  }
  
  public Bitmap getContent()
  {
    if (mWindow == null) {
      return null;
    }
    synchronized (mWindow.mLock)
    {
      Bitmap localBitmap = Bitmap.createScaledBitmap(mWindow.mBitmap, mWindowWidth, mWindowHeight, true);
      return localBitmap;
    }
  }
  
  public int getHeight()
  {
    return mWindowHeight;
  }
  
  public int getWidth()
  {
    return mWindowWidth;
  }
  
  public Point getWindowCoords()
  {
    if (mWindow == null) {
      return null;
    }
    Rect localRect = mView.getViewRootImpl().mWindowAttributes.surfaceInsets;
    return new Point(mWindow.mLastDrawContentPositionX - left, mWindow.mLastDrawContentPositionY - top);
  }
  
  public Rect getWindowPositionOnScreen()
  {
    int[] arrayOfInt1 = new int[2];
    mView.getLocationOnScreen(arrayOfInt1);
    int[] arrayOfInt2 = new int[2];
    mView.getLocationInSurface(arrayOfInt2);
    int i = mWindowCoords.x + arrayOfInt1[0] - arrayOfInt2[0];
    int j = mWindowCoords.y + arrayOfInt1[1] - arrayOfInt2[1];
    return new Rect(i, j, mWindowWidth + i, mWindowHeight + j);
  }
  
  public float getZoom()
  {
    return mZoom;
  }
  
  public void setOnOperationCompleteCallback(Callback paramCallback)
  {
    mCallback = paramCallback;
    if (mWindow != null) {
      InternalPopupWindow.access$602(mWindow, paramCallback);
    }
  }
  
  public void show(float paramFloat1, float paramFloat2)
  {
    paramFloat1 = Math.max(0.0F, Math.min(paramFloat1, mView.getWidth()));
    float f1 = Math.max(0.0F, Math.min(paramFloat2, mView.getHeight()));
    obtainSurfaces();
    obtainContentCoordinates(paramFloat1, f1);
    obtainWindowCoordinates();
    int i = mClampedCenterZoomCoords.x;
    int j = mBitmapWidth / 2;
    int k = mClampedCenterZoomCoords.y;
    int m = mBitmapHeight / 2;
    if ((paramFloat1 != mPrevPosInView.x) || (f1 != mPrevPosInView.y))
    {
      if (mWindow == null)
      {
        InternalPopupWindow localInternalPopupWindow;
        Context localContext;
        Display localDisplay;
        Surface localSurface;
        int n;
        int i1;
        float f2;
        Handler localHandler;
        Object localObject4;
        synchronized (mLock)
        {
          localInternalPopupWindow = new android/widget/Magnifier$InternalPopupWindow;
          localContext = mView.getContext();
          localDisplay = mView.getDisplay();
          localSurface = mParentSurface.mSurface;
          n = mWindowWidth;
          i1 = mWindowHeight;
          paramFloat2 = mWindowElevation;
          f2 = mWindowCornerRadius;
          localHandler = Handler.getMain();
          localObject4 = mLock;
        }
        throw localObject2;
      }
      performPixelCopy(i - j, k - m, true);
      mPrevPosInView.x = paramFloat1;
      mPrevPosInView.y = f1;
    }
  }
  
  public void update()
  {
    if (mWindow != null)
    {
      obtainSurfaces();
      performPixelCopy(mPrevStartCoordsInSurface.x, mPrevStartCoordsInSurface.y, false);
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void onOperationComplete();
  }
  
  private static class InternalPopupWindow
  {
    private static final int CONTENT_BITMAP_ALPHA = 242;
    private static final int SURFACE_Z = 5;
    private Bitmap mBitmap;
    private final RenderNode mBitmapRenderNode;
    private Magnifier.Callback mCallback;
    private final int mContentHeight;
    private final int mContentWidth;
    private final Object mDestroyLock = new Object();
    private final Display mDisplay;
    private boolean mFirstDraw = true;
    private boolean mFrameDrawScheduled;
    private final Handler mHandler;
    private int mLastDrawContentPositionX;
    private int mLastDrawContentPositionY;
    private final Object mLock;
    private final Runnable mMagnifierUpdater;
    private final int mOffsetX;
    private final int mOffsetY;
    private boolean mPendingWindowPositionUpdate;
    private final ThreadedRenderer.SimpleRenderer mRenderer;
    private final Surface mSurface;
    private final SurfaceControl mSurfaceControl;
    private final int mSurfaceHeight;
    private final SurfaceSession mSurfaceSession;
    private final int mSurfaceWidth;
    private int mWindowPositionX;
    private int mWindowPositionY;
    
    InternalPopupWindow(Context paramContext, Display paramDisplay, Surface paramSurface, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Handler paramHandler, Object paramObject, Magnifier.Callback paramCallback)
    {
      mDisplay = paramDisplay;
      mLock = paramObject;
      mCallback = paramCallback;
      mContentWidth = paramInt1;
      mContentHeight = paramInt2;
      mOffsetX = ((int)(paramInt1 * 0.1F));
      mOffsetY = ((int)(0.1F * paramInt2));
      mSurfaceWidth = (mContentWidth + mOffsetX * 2);
      mSurfaceHeight = (mContentHeight + 2 * mOffsetY);
      mSurfaceSession = new SurfaceSession(paramSurface);
      mSurfaceControl = new SurfaceControl.Builder(mSurfaceSession).setFormat(-3).setSize(mSurfaceWidth, mSurfaceHeight).setName("magnifier surface").setFlags(4).build();
      mSurface = new Surface();
      mSurface.copyFrom(mSurfaceControl);
      mRenderer = new ThreadedRenderer.SimpleRenderer(paramContext, "magnifier renderer", mSurface);
      mBitmapRenderNode = createRenderNodeForBitmap("magnifier content", paramFloat1, paramFloat2);
      paramContext = mRenderer.getRootNode().start(paramInt1, paramInt2);
      try
      {
        paramContext.insertReorderBarrier();
        paramContext.drawRenderNode(mBitmapRenderNode);
        paramContext.insertInorderBarrier();
        mRenderer.getRootNode().end(paramContext);
        mHandler = paramHandler;
        mMagnifierUpdater = new _..Lambda.Magnifier.InternalPopupWindow.t9Cn2sIi2LBUhAVikvRPKKoAwIU(this);
        mFrameDrawScheduled = false;
        return;
      }
      finally
      {
        mRenderer.getRootNode().end(paramContext);
      }
    }
    
    private RenderNode createRenderNodeForBitmap(String paramString, float paramFloat1, float paramFloat2)
    {
      paramString = RenderNode.create(paramString, null);
      paramString.setLeftTopRightBottom(mOffsetX, mOffsetY, mOffsetX + mContentWidth, mOffsetY + mContentHeight);
      paramString.setElevation(paramFloat1);
      Outline localOutline = new Outline();
      localOutline.setRoundRect(0, 0, mContentWidth, mContentHeight, paramFloat2);
      localOutline.setAlpha(1.0F);
      paramString.setOutline(localOutline);
      paramString.setClipToOutline(true);
      DisplayListCanvas localDisplayListCanvas = paramString.start(mContentWidth, mContentHeight);
      try
      {
        localDisplayListCanvas.drawColor(-16711936);
        return paramString;
      }
      finally
      {
        paramString.end(localDisplayListCanvas);
      }
    }
    
    private void doDraw()
    {
      synchronized (mLock)
      {
        if (!mSurface.isValid()) {
          return;
        }
        Object localObject2 = mBitmapRenderNode.start(mContentWidth, mContentHeight);
        try
        {
          ((DisplayListCanvas)localObject2).drawColor(-1);
          Rect localRect1 = new android/graphics/Rect;
          localRect1.<init>(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
          Rect localRect2 = new android/graphics/Rect;
          localRect2.<init>(0, 0, mContentWidth, mContentHeight);
          Paint localPaint = new android/graphics/Paint;
          localPaint.<init>();
          localPaint.setFilterBitmap(true);
          localPaint.setAlpha(242);
          ((DisplayListCanvas)localObject2).drawBitmap(mBitmap, localRect1, localRect2, localPaint);
          mBitmapRenderNode.end((DisplayListCanvas)localObject2);
          if ((!mPendingWindowPositionUpdate) && (!mFirstDraw))
          {
            localObject2 = null;
          }
          else
          {
            boolean bool1 = mFirstDraw;
            mFirstDraw = false;
            boolean bool2 = mPendingWindowPositionUpdate;
            mPendingWindowPositionUpdate = false;
            int i = mWindowPositionX;
            int j = mWindowPositionY;
            localObject2 = new android/widget/_$$Lambda$Magnifier$InternalPopupWindow$vZThyvjDQhg2J1GAeOWCNqy2iiw;
            ((_..Lambda.Magnifier.InternalPopupWindow.vZThyvjDQhg2J1GAeOWCNqy2iiw)localObject2).<init>(this, i, j, bool2, bool1);
          }
          mLastDrawContentPositionX = (mWindowPositionX + mOffsetX);
          mLastDrawContentPositionY = (mWindowPositionY + mOffsetY);
          mFrameDrawScheduled = false;
          mRenderer.draw((ThreadedRenderer.FrameDrawingCallback)localObject2);
          if (mCallback != null) {
            mCallback.onOperationComplete();
          }
          return;
        }
        finally
        {
          mBitmapRenderNode.end((DisplayListCanvas)localObject2);
        }
      }
    }
    
    private void requestUpdate()
    {
      if (mFrameDrawScheduled) {
        return;
      }
      Message localMessage = Message.obtain(mHandler, mMagnifierUpdater);
      localMessage.setAsynchronous(true);
      localMessage.sendToTarget();
      mFrameDrawScheduled = true;
    }
    
    public void destroy()
    {
      synchronized (mDestroyLock)
      {
        mSurface.destroy();
        synchronized (mLock)
        {
          mRenderer.destroy();
          mSurfaceControl.destroy();
          mSurfaceSession.kill();
          mBitmapRenderNode.destroy();
          mHandler.removeCallbacks(mMagnifierUpdater);
          if (mBitmap != null) {
            mBitmap.recycle();
          }
          return;
        }
      }
    }
    
    public void setContentPositionForNextDraw(int paramInt1, int paramInt2)
    {
      mWindowPositionX = (paramInt1 - mOffsetX);
      mWindowPositionY = (paramInt2 - mOffsetY);
      mPendingWindowPositionUpdate = true;
      requestUpdate();
    }
    
    public void updateContent(Bitmap paramBitmap)
    {
      if (mBitmap != null) {
        mBitmap.recycle();
      }
      mBitmap = paramBitmap;
      requestUpdate();
    }
  }
  
  private static class SurfaceInfo
  {
    public static final SurfaceInfo NULL = new SurfaceInfo(null, 0, 0, false);
    private int mHeight;
    private boolean mIsMainWindowSurface;
    private Surface mSurface;
    private int mWidth;
    
    SurfaceInfo(Surface paramSurface, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      mSurface = paramSurface;
      mWidth = paramInt1;
      mHeight = paramInt2;
      mIsMainWindowSurface = paramBoolean;
    }
  }
}
