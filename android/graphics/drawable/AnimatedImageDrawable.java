package android.graphics.drawable;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.android.internal.R.styleable;
import dalvik.annotation.optimization.FastNative;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import libcore.util.NativeAllocationRegistry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedImageDrawable
  extends Drawable
  implements Animatable2
{
  private static final int FINISHED = -1;
  @Deprecated
  public static final int LOOP_INFINITE = -1;
  public static final int REPEAT_INFINITE = -1;
  private static final int REPEAT_UNDEFINED = -2;
  private ArrayList<Animatable2.AnimationCallback> mAnimationCallbacks = null;
  private ColorFilter mColorFilter;
  private Handler mHandler;
  private int mIntrinsicHeight;
  private int mIntrinsicWidth;
  private Runnable mRunnable;
  private boolean mStarting;
  private State mState;
  
  public AnimatedImageDrawable()
  {
    mState = new State(0L, null, null);
  }
  
  public AnimatedImageDrawable(long paramLong, ImageDecoder paramImageDecoder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rect paramRect, InputStream paramInputStream, AssetFileDescriptor paramAssetFileDescriptor)
    throws IOException
  {
    paramInt1 = Bitmap.scaleFromDensity(paramInt1, paramInt3, paramInt4);
    paramInt2 = Bitmap.scaleFromDensity(paramInt2, paramInt3, paramInt4);
    if (paramRect == null)
    {
      mIntrinsicWidth = paramInt1;
      mIntrinsicHeight = paramInt2;
    }
    else
    {
      paramRect.set(Bitmap.scaleFromDensity(left, paramInt3, paramInt4), Bitmap.scaleFromDensity(top, paramInt3, paramInt4), Bitmap.scaleFromDensity(right, paramInt3, paramInt4), Bitmap.scaleFromDensity(bottom, paramInt3, paramInt4));
      mIntrinsicWidth = paramRect.width();
      mIntrinsicHeight = paramRect.height();
    }
    mState = new State(nCreate(paramLong, paramImageDecoder, paramInt1, paramInt2, paramRect), paramInputStream, paramAssetFileDescriptor);
    paramLong = nNativeByteSize(mState.mNativePtr);
    new NativeAllocationRegistry(AnimatedImageDrawable.class.getClassLoader(), nGetNativeFinalizer(), paramLong).registerNativeAllocation(mState, mState.mNativePtr);
  }
  
  private Handler getHandler()
  {
    if (mHandler == null) {
      mHandler = new Handler(Looper.getMainLooper());
    }
    return mHandler;
  }
  
  private static native long nCreate(long paramLong, ImageDecoder paramImageDecoder, int paramInt1, int paramInt2, Rect paramRect)
    throws IOException;
  
  private static native long nDraw(long paramLong1, long paramLong2);
  
  @FastNative
  private static native int nGetAlpha(long paramLong);
  
  @FastNative
  private static native long nGetNativeFinalizer();
  
  @FastNative
  private static native int nGetRepeatCount(long paramLong);
  
  @FastNative
  private static native boolean nIsRunning(long paramLong);
  
  @FastNative
  private static native long nNativeByteSize(long paramLong);
  
  @FastNative
  private static native void nSetAlpha(long paramLong, int paramInt);
  
  @FastNative
  private static native void nSetColorFilter(long paramLong1, long paramLong2);
  
  @FastNative
  private static native void nSetMirrored(long paramLong, boolean paramBoolean);
  
  private static native void nSetOnAnimationEndListener(long paramLong, AnimatedImageDrawable paramAnimatedImageDrawable);
  
  @FastNative
  private static native void nSetRepeatCount(long paramLong, int paramInt);
  
  @FastNative
  private static native boolean nStart(long paramLong);
  
  @FastNative
  private static native boolean nStop(long paramLong);
  
  private void onAnimationEnd()
  {
    if (mAnimationCallbacks != null)
    {
      Iterator localIterator = mAnimationCallbacks.iterator();
      while (localIterator.hasNext()) {
        ((Animatable2.AnimationCallback)localIterator.next()).onAnimationEnd(this);
      }
    }
  }
  
  private void postOnAnimationEnd()
  {
    if (mAnimationCallbacks == null) {
      return;
    }
    getHandler().post(new _..Lambda.AnimatedImageDrawable.dGAkP_tKNvqn_qCWdrQRL806ExQ(this));
  }
  
  private void postOnAnimationStart()
  {
    if (mAnimationCallbacks == null) {
      return;
    }
    getHandler().post(new _..Lambda.AnimatedImageDrawable.6aWLU8OYhdfACSejz5_iGirYxUk(this));
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray, int paramInt)
    throws XmlPullParserException
  {
    Object localObject1 = mState;
    Object localObject2 = paramTypedArray.getResources();
    int i = paramTypedArray.getResourceId(0, 0);
    if (i != 0)
    {
      TypedValue localTypedValue = new TypedValue();
      ((Resources)localObject2).getValueForDensity(i, paramInt, localTypedValue, true);
      if ((paramInt > 0) && (density > 0) && (density != 65535)) {
        if (density == paramInt) {
          density = getDisplayMetricsdensityDpi;
        } else {
          density = (density * getDisplayMetricsdensityDpi / paramInt);
        }
      }
      paramInt = 0;
      if (density == 0) {
        paramInt = 160;
      } else if (density != 65535) {
        paramInt = density;
      }
      try
      {
        localObject2 = ImageDecoder.decodeDrawable(ImageDecoder.createSource((Resources)localObject2, ((Resources)localObject2).openRawResource(i, localTypedValue), paramInt), _..Lambda.AnimatedImageDrawable.Cgt3NliB7ZYUONyDd_eQGdYbEKc.INSTANCE);
        if ((localObject2 instanceof AnimatedImageDrawable))
        {
          paramInt = mState.mRepeatCount;
          localObject2 = (AnimatedImageDrawable)localObject2;
          mState = mState;
          mState = null;
          mIntrinsicWidth = mIntrinsicWidth;
          mIntrinsicHeight = mIntrinsicHeight;
          if (paramInt != -2) {
            setRepeatCount(paramInt);
          }
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append(paramTypedArray.getPositionDescription());
          ((StringBuilder)localObject1).append(": <animated-image> did not decode animated");
          throw new XmlPullParserException(((StringBuilder)localObject1).toString());
        }
      }
      catch (IOException localIOException)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(paramTypedArray.getPositionDescription());
        ((StringBuilder)localObject2).append(": <animated-image> requires a valid 'src' attribute");
        throw new XmlPullParserException(((StringBuilder)localObject2).toString(), null, localIOException);
      }
    }
    mState.mThemeAttrs = paramTypedArray.extractThemeAttrs();
    StringBuilder localStringBuilder;
    if ((mState.mNativePtr == 0L) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[0] == 0)))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <animated-image> requires a valid 'src' attribute");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
    mState.mAutoMirrored = paramTypedArray.getBoolean(3, mAutoMirrored);
    paramInt = paramTypedArray.getInt(1, -2);
    if (paramInt != -2) {
      setRepeatCount(paramInt);
    }
    if ((paramTypedArray.getBoolean(2, false)) && (mState.mNativePtr != 0L)) {
      start();
    }
  }
  
  public void clearAnimationCallbacks()
  {
    if (mAnimationCallbacks != null)
    {
      mAnimationCallbacks = null;
      nSetOnAnimationEndListener(mState.mNativePtr, null);
    }
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mState.mNativePtr != 0L)
    {
      if (mStarting)
      {
        mStarting = false;
        postOnAnimationStart();
      }
      long l = nDraw(mState.mNativePtr, paramCanvas.getNativeCanvasWrapper());
      if (l > 0L)
      {
        if (mRunnable == null) {
          mRunnable = new _..Lambda.AlQeVq8Y_kfuQeb_JLZ0ueV4DE8(this);
        }
        scheduleSelf(mRunnable, SystemClock.uptimeMillis() + l);
      }
      else if (l == -1L)
      {
        postOnAnimationEnd();
      }
      return;
    }
    throw new IllegalStateException("called draw on empty AnimatedImageDrawable");
  }
  
  public int getAlpha()
  {
    if (mState.mNativePtr != 0L) {
      return nGetAlpha(mState.mNativePtr);
    }
    throw new IllegalStateException("called getAlpha on empty AnimatedImageDrawable");
  }
  
  public ColorFilter getColorFilter()
  {
    return mColorFilter;
  }
  
  public int getIntrinsicHeight()
  {
    return mIntrinsicHeight;
  }
  
  public int getIntrinsicWidth()
  {
    return mIntrinsicWidth;
  }
  
  @Deprecated
  public int getLoopCount(int paramInt)
  {
    return getRepeatCount();
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public int getRepeatCount()
  {
    if (mState.mNativePtr != 0L)
    {
      if (mState.mRepeatCount == -2) {
        mState.mRepeatCount = nGetRepeatCount(mState.mNativePtr);
      }
      return mState.mRepeatCount;
    }
    throw new IllegalStateException("called getRepeatCount on empty AnimatedImageDrawable");
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedImageDrawable), mSrcDensityOverride);
  }
  
  public final boolean isAutoMirrored()
  {
    return mState.mAutoMirrored;
  }
  
  public boolean isRunning()
  {
    if (mState.mNativePtr != 0L) {
      return nIsRunning(mState.mNativePtr);
    }
    throw new IllegalStateException("called isRunning on empty AnimatedImageDrawable");
  }
  
  public boolean onLayoutDirectionChanged(int paramInt)
  {
    boolean bool1 = mState.mAutoMirrored;
    boolean bool2 = false;
    if ((bool1) && (mState.mNativePtr != 0L))
    {
      if (paramInt == 1) {
        bool2 = true;
      }
      nSetMirrored(mState.mNativePtr, bool2);
      return true;
    }
    return false;
  }
  
  public void registerAnimationCallback(Animatable2.AnimationCallback paramAnimationCallback)
  {
    if (paramAnimationCallback == null) {
      return;
    }
    if (mAnimationCallbacks == null)
    {
      mAnimationCallbacks = new ArrayList();
      nSetOnAnimationEndListener(mState.mNativePtr, this);
    }
    if (!mAnimationCallbacks.contains(paramAnimationCallback)) {
      mAnimationCallbacks.add(paramAnimationCallback);
    }
  }
  
  public void setAlpha(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 255))
    {
      if (mState.mNativePtr != 0L)
      {
        nSetAlpha(mState.mNativePtr, paramInt);
        invalidateSelf();
        return;
      }
      throw new IllegalStateException("called setAlpha on empty AnimatedImageDrawable");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Alpha must be between 0 and 255! provided ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    if (mState.mAutoMirrored != paramBoolean)
    {
      mState.mAutoMirrored = paramBoolean;
      if ((getLayoutDirection() == 1) && (mState.mNativePtr != 0L))
      {
        nSetMirrored(mState.mNativePtr, paramBoolean);
        invalidateSelf();
      }
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    long l1 = mState.mNativePtr;
    long l2 = 0L;
    if (l1 != 0L)
    {
      if (paramColorFilter != mColorFilter)
      {
        mColorFilter = paramColorFilter;
        if (paramColorFilter != null) {
          l2 = paramColorFilter.getNativeInstance();
        }
        nSetColorFilter(mState.mNativePtr, l2);
        invalidateSelf();
      }
      return;
    }
    throw new IllegalStateException("called setColorFilter on empty AnimatedImageDrawable");
  }
  
  @Deprecated
  public void setLoopCount(int paramInt)
  {
    setRepeatCount(paramInt);
  }
  
  public void setRepeatCount(int paramInt)
  {
    if (paramInt >= -1)
    {
      if (mState.mRepeatCount != paramInt)
      {
        mState.mRepeatCount = paramInt;
        if (mState.mNativePtr != 0L) {
          nSetRepeatCount(mState.mNativePtr, paramInt);
        }
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid value passed to setRepeatCount");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void start()
  {
    if (mState.mNativePtr != 0L)
    {
      if (nStart(mState.mNativePtr))
      {
        mStarting = true;
        invalidateSelf();
      }
      return;
    }
    throw new IllegalStateException("called start on empty AnimatedImageDrawable");
  }
  
  public void stop()
  {
    if (mState.mNativePtr != 0L)
    {
      if (nStop(mState.mNativePtr)) {
        postOnAnimationEnd();
      }
      return;
    }
    throw new IllegalStateException("called stop on empty AnimatedImageDrawable");
  }
  
  public boolean unregisterAnimationCallback(Animatable2.AnimationCallback paramAnimationCallback)
  {
    if ((paramAnimationCallback != null) && (mAnimationCallbacks != null) && (mAnimationCallbacks.remove(paramAnimationCallback)))
    {
      if (mAnimationCallbacks.isEmpty()) {
        clearAnimationCallbacks();
      }
      return true;
    }
    return false;
  }
  
  private class State
  {
    private final AssetFileDescriptor mAssetFd;
    boolean mAutoMirrored = false;
    private final InputStream mInputStream;
    final long mNativePtr;
    int mRepeatCount = -2;
    int[] mThemeAttrs = null;
    
    State(long paramLong, InputStream paramInputStream, AssetFileDescriptor paramAssetFileDescriptor)
    {
      mNativePtr = paramLong;
      mInputStream = paramInputStream;
      mAssetFd = paramAssetFileDescriptor;
    }
  }
}
