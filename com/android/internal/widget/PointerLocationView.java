package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.VelocityTracker;
import android.view.VelocityTracker.Estimator;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManagerPolicyConstants.PointerEventListener;
import java.util.ArrayList;

public class PointerLocationView
  extends View
  implements InputManager.InputDeviceListener, WindowManagerPolicyConstants.PointerEventListener
{
  private static final String ALT_STRATEGY_PROPERY_KEY = "debug.velocitytracker.alt";
  private static final String TAG = "Pointer";
  private final int ESTIMATE_FUTURE_POINTS = 2;
  private final float ESTIMATE_INTERVAL = 0.02F;
  private final int ESTIMATE_PAST_POINTS = 4;
  private int mActivePointerId;
  private final VelocityTracker mAltVelocity;
  private boolean mCurDown;
  private int mCurNumPointers;
  private final Paint mCurrentPointPaint;
  private int mHeaderBottom;
  private final InputManager mIm;
  private int mMaxNumPointers;
  private final Paint mPaint;
  private final Paint mPathPaint;
  private final ArrayList<PointerState> mPointers = new ArrayList();
  private boolean mPrintCoords = true;
  private RectF mReusableOvalRect = new RectF();
  private final Paint mTargetPaint;
  private final MotionEvent.PointerCoords mTempCoords = new MotionEvent.PointerCoords();
  private final FasterStringBuilder mText = new FasterStringBuilder();
  private final Paint mTextBackgroundPaint;
  private final Paint mTextLevelPaint;
  private final Paint.FontMetricsInt mTextMetrics = new Paint.FontMetricsInt();
  private final Paint mTextPaint;
  private final ViewConfiguration mVC;
  private final VelocityTracker mVelocity;
  
  public PointerLocationView(Context paramContext)
  {
    super(paramContext);
    setFocusableInTouchMode(true);
    mIm = ((InputManager)paramContext.getSystemService(InputManager.class));
    mVC = ViewConfiguration.get(paramContext);
    mTextPaint = new Paint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setTextSize(10.0F * getResourcesgetDisplayMetricsdensity);
    mTextPaint.setARGB(255, 0, 0, 0);
    mTextBackgroundPaint = new Paint();
    mTextBackgroundPaint.setAntiAlias(false);
    mTextBackgroundPaint.setARGB(128, 255, 255, 255);
    mTextLevelPaint = new Paint();
    mTextLevelPaint.setAntiAlias(false);
    mTextLevelPaint.setARGB(192, 255, 0, 0);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setARGB(255, 255, 255, 255);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(2.0F);
    mCurrentPointPaint = new Paint();
    mCurrentPointPaint.setAntiAlias(true);
    mCurrentPointPaint.setARGB(255, 255, 0, 0);
    mCurrentPointPaint.setStyle(Paint.Style.STROKE);
    mCurrentPointPaint.setStrokeWidth(2.0F);
    mTargetPaint = new Paint();
    mTargetPaint.setAntiAlias(false);
    mTargetPaint.setARGB(255, 0, 0, 192);
    mPathPaint = new Paint();
    mPathPaint.setAntiAlias(false);
    mPathPaint.setARGB(255, 0, 96, 255);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(1.0F);
    paramContext = new PointerState();
    mPointers.add(paramContext);
    mActivePointerId = 0;
    mVelocity = VelocityTracker.obtain();
    paramContext = SystemProperties.get("debug.velocitytracker.alt");
    if (paramContext.length() != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Comparing default velocity tracker strategy with ");
      localStringBuilder.append(paramContext);
      Log.d("Pointer", localStringBuilder.toString());
      mAltVelocity = VelocityTracker.obtain(paramContext);
    }
    else
    {
      mAltVelocity = null;
    }
  }
  
  private void drawOval(Canvas paramCanvas, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, Paint paramPaint)
  {
    paramCanvas.save(1);
    paramCanvas.rotate((float)(180.0F * paramFloat5 / 3.141592653589793D), paramFloat1, paramFloat2);
    mReusableOvalRect.left = (paramFloat1 - paramFloat4 / 2.0F);
    mReusableOvalRect.right = (paramFloat4 / 2.0F + paramFloat1);
    mReusableOvalRect.top = (paramFloat2 - paramFloat3 / 2.0F);
    mReusableOvalRect.bottom = (paramFloat3 / 2.0F + paramFloat2);
    paramCanvas.drawOval(mReusableOvalRect, paramPaint);
    paramCanvas.restore();
  }
  
  private void logCoords(String paramString, int paramInt1, int paramInt2, MotionEvent.PointerCoords paramPointerCoords, int paramInt3, MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getToolType(paramInt2);
    int j = paramMotionEvent.getButtonState();
    switch (paramInt1 & 0xFF)
    {
    default: 
      str = Integer.toString(paramInt1);
      break;
    case 10: 
      str = "HOVER EXIT";
      break;
    case 9: 
      str = "HOVER ENTER";
      break;
    case 8: 
      str = "SCROLL";
      break;
    case 7: 
      str = "HOVER MOVE";
      break;
    case 6: 
      if (paramInt2 == (paramInt1 & 0xFF00) >> 8) {
        str = "UP";
      } else {
        str = "MOVE";
      }
      break;
    case 5: 
      if (paramInt2 == (paramInt1 & 0xFF00) >> 8) {
        str = "DOWN";
      } else {
        str = "MOVE";
      }
      break;
    case 4: 
      str = "OUTSIDE";
      break;
    case 3: 
      str = "CANCEL";
      break;
    case 2: 
      str = "MOVE";
      break;
    case 1: 
      str = "UP";
      break;
    }
    String str = "DOWN";
    Log.i("Pointer", mText.clear().append(paramString).append(" id ").append(paramInt3 + 1).append(": ").append(str).append(" (").append(x, 3).append(", ").append(y, 3).append(") Pressure=").append(pressure, 3).append(" Size=").append(size, 3).append(" TouchMajor=").append(touchMajor, 3).append(" TouchMinor=").append(touchMinor, 3).append(" ToolMajor=").append(toolMajor, 3).append(" ToolMinor=").append(toolMinor, 3).append(" Orientation=").append((float)(orientation * 180.0F / 3.141592653589793D), 1).append("deg").append(" Tilt=").append((float)(paramPointerCoords.getAxisValue(25) * 180.0F / 3.141592653589793D), 1).append("deg").append(" Distance=").append(paramPointerCoords.getAxisValue(24), 1).append(" VScroll=").append(paramPointerCoords.getAxisValue(9), 1).append(" HScroll=").append(paramPointerCoords.getAxisValue(10), 1).append(" BoundingBox=[(").append(paramMotionEvent.getAxisValue(32), 3).append(", ").append(paramMotionEvent.getAxisValue(33), 3).append(")").append(", (").append(paramMotionEvent.getAxisValue(34), 3).append(", ").append(paramMotionEvent.getAxisValue(35), 3).append(")]").append(" ToolType=").append(MotionEvent.toolTypeToString(i)).append(" ButtonState=").append(MotionEvent.buttonStateToString(j)).toString());
  }
  
  private void logInputDeviceState(int paramInt, String paramString)
  {
    InputDevice localInputDevice = mIm.getInputDevice(paramInt);
    StringBuilder localStringBuilder;
    if (localInputDevice != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(": ");
      localStringBuilder.append(localInputDevice);
      Log.i("Pointer", localStringBuilder.toString());
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(": ");
      localStringBuilder.append(paramInt);
      Log.i("Pointer", localStringBuilder.toString());
    }
  }
  
  private void logInputDevices()
  {
    int[] arrayOfInt = InputDevice.getDeviceIds();
    for (int i = 0; i < arrayOfInt.length; i++) {
      logInputDeviceState(arrayOfInt[i], "Device Enumerated");
    }
  }
  
  private void logMotionEvent(String paramString, MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    int j = paramMotionEvent.getHistorySize();
    int k = paramMotionEvent.getPointerCount();
    int m = 0;
    int i1;
    for (int n = 0; n < j; n++) {
      for (i1 = 0; i1 < k; i1++)
      {
        int i2 = paramMotionEvent.getPointerId(i1);
        paramMotionEvent.getHistoricalPointerCoords(i1, n, mTempCoords);
        logCoords(paramString, i, i1, mTempCoords, i2, paramMotionEvent);
      }
    }
    for (n = m; n < k; n++)
    {
      i1 = paramMotionEvent.getPointerId(n);
      paramMotionEvent.getPointerCoords(n, mTempCoords);
      logCoords(paramString, i, n, mTempCoords, i1, paramMotionEvent);
    }
  }
  
  private static boolean shouldLogKey(int paramInt)
  {
    boolean bool1 = true;
    switch (paramInt)
    {
    default: 
      bool2 = bool1;
      if (KeyEvent.isGamepadButton(paramInt)) {
        return bool2;
      }
      if (KeyEvent.isModifierKey(paramInt)) {
        bool2 = bool1;
      }
      break;
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
      return true;
    }
    boolean bool2 = false;
    return bool2;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mIm.registerInputDeviceListener(this, getHandler());
    logInputDevices();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    mIm.unregisterInputDeviceListener(this);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    int i = getWidth();
    int j = i / 7;
    int k = -mTextMetrics.ascent + 1 + 100;
    int m = mHeaderBottom + 100;
    int n = mPointers.size();
    PointerState localPointerState;
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;
    Object localObject;
    if (mActivePointerId >= 0)
    {
      localPointerState = (PointerState)mPointers.get(mActivePointerId);
      paramCanvas.drawRect(0.0F, 100.0F, j - 1, m, mTextBackgroundPaint);
      paramCanvas.drawText(mText.clear().append("P: ").append(mCurNumPointers).append(" / ").append(mMaxNumPointers).toString(), 1.0F, k, mTextPaint);
      i1 = mTraceCount;
      if (((mCurDown) && (mCurDown)) || (i1 == 0))
      {
        paramCanvas.drawRect(j, 100.0F, j * 2 - 1, m, mTextBackgroundPaint);
        paramCanvas.drawText(mText.clear().append("X: ").append(mCoords.x, 1).toString(), 1 + j, k, mTextPaint);
        paramCanvas.drawRect(j * 2, 100.0F, j * 3 - 1, m, mTextBackgroundPaint);
        paramCanvas.drawText(mText.clear().append("Y: ").append(mCoords.y, 1).toString(), j * 2 + 1, k, mTextPaint);
      }
      else
      {
        f1 = mTraceX[(i1 - 1)] - mTraceX[0];
        f2 = mTraceY[(i1 - 1)] - mTraceY[0];
        f3 = j;
        f4 = j * 2 - 1;
        f5 = m;
        if (Math.abs(f1) < mVC.getScaledTouchSlop()) {
          localObject = mTextBackgroundPaint;
        } else {
          localObject = mTextLevelPaint;
        }
        paramCanvas.drawRect(f3, 100.0F, f4, f5, (Paint)localObject);
        paramCanvas.drawText(mText.clear().append("dX: ").append(f1, 1).toString(), 1 + j, k, mTextPaint);
        f4 = j * 2;
        f1 = j * 3 - 1;
        f5 = m;
        if (Math.abs(f2) < mVC.getScaledTouchSlop()) {}
        for (localObject = mTextBackgroundPaint;; localObject = mTextLevelPaint) {
          break;
        }
        paramCanvas.drawRect(f4, 100.0F, f1, f5, (Paint)localObject);
        paramCanvas.drawText(mText.clear().append("dY: ").append(f2, 1).toString(), j * 2 + 1, k, mTextPaint);
      }
      paramCanvas.drawRect(j * 3, 100.0F, j * 4 - 1, m, mTextBackgroundPaint);
      paramCanvas.drawText(mText.clear().append("Xv: ").append(mXVelocity, 3).toString(), j * 3 + 1, k, mTextPaint);
      paramCanvas.drawRect(j * 4, 100.0F, j * 5 - 1, m, mTextBackgroundPaint);
      paramCanvas.drawText(mText.clear().append("Yv: ").append(mYVelocity, 3).toString(), 1 + j * 4, k, mTextPaint);
      paramCanvas.drawRect(j * 5, 100.0F, j * 6 - 1, m, mTextBackgroundPaint);
      paramCanvas.drawRect(j * 5, 100.0F, j * 5 + mCoords.pressure * j - 1.0F, m, mTextLevelPaint);
      paramCanvas.drawText(mText.clear().append("Prs: ").append(mCoords.pressure, 2).toString(), 1 + j * 5, k, mTextPaint);
      paramCanvas.drawRect(j * 6, 100.0F, i, m, mTextBackgroundPaint);
      paramCanvas.drawRect(j * 6, 100.0F, j * 6 + mCoords.size * j - 1.0F, m, mTextLevelPaint);
      paramCanvas.drawText(mText.clear().append("Size: ").append(mCoords.size, 2).toString(), 1 + j * 6, k, mTextPaint);
    }
    for (int i1 = 0; i1 < n; i1 = m + 1)
    {
      localPointerState = (PointerState)mPointers.get(i1);
      int i2 = mTraceCount;
      mPaint.setARGB(255, 128, 255, 255);
      f2 = 0.0F;
      m = 0;
      int i3 = 0;
      int i4 = 0;
      f5 = 0.0F;
      while (i4 < i2)
      {
        f1 = mTraceX[i4];
        f4 = mTraceY[i4];
        if (Float.isNaN(f1))
        {
          m = 0;
        }
        else
        {
          if (m != 0)
          {
            paramCanvas.drawLine(f2, f5, f1, f4, mPathPaint);
            if (mTraceCurrent[i4] != 0) {
              localObject = mCurrentPointPaint;
            } else {
              localObject = mPaint;
            }
            paramCanvas.drawPoint(f2, f5, (Paint)localObject);
            i3 = 1;
          }
          f2 = f1;
          f5 = f4;
          m = 1;
        }
        i4++;
      }
      if (i3 != 0)
      {
        mPaint.setARGB(128, 128, 0, 128);
        localObject = mEstimator;
        f4 = -0.08F;
        f1 = ((VelocityTracker.Estimator)localObject).estimateX(-0.08F);
        f3 = mEstimator.estimateY(-0.08F);
        i3 = -3;
        float f7;
        for (m = -3; m <= 2; m++)
        {
          float f6 = mEstimator.estimateX(m * 0.02F);
          f7 = mEstimator.estimateY(m * 0.02F);
          paramCanvas.drawLine(f1, f3, f6, f7, mPaint);
          f1 = f6;
          f3 = f7;
        }
        m = i1;
        mPaint.setARGB(255, 255, 64, 128);
        paramCanvas.drawLine(f2, f5, f2 + mXVelocity * 16.0F, f5 + mYVelocity * 16.0F, mPaint);
        if (mAltVelocity != null)
        {
          localObject = mPaint;
          i1 = 0;
          ((Paint)localObject).setARGB(128, 0, 128, 128);
          f1 = mAltEstimator.estimateX(f4);
          f4 = mAltEstimator.estimateY(f4);
          while (i3 <= 2)
          {
            f7 = mAltEstimator.estimateX(i3 * 0.02F);
            f3 = mAltEstimator.estimateY(i3 * 0.02F);
            paramCanvas.drawLine(f1, f4, f7, f3, mPaint);
            f1 = f7;
            f4 = f3;
            i3++;
          }
          mPaint.setARGB(255, 64, 255, 128);
          paramCanvas.drawLine(f2, f5, f2 + mAltXVelocity * 16.0F, f5 + mAltYVelocity * 16.0F, mPaint);
        }
        else
        {
          i1 = 0;
        }
      }
      else
      {
        m = i1;
        i1 = 0;
      }
      if ((mCurDown) && (mCurDown))
      {
        paramCanvas.drawLine(0.0F, mCoords.y, getWidth(), mCoords.y, mTargetPaint);
        paramCanvas.drawLine(mCoords.x, 0.0F, mCoords.x, getHeight(), mTargetPaint);
        i3 = (int)(mCoords.pressure * 255.0F);
        mPaint.setARGB(255, i3, 255, 255 - i3);
        paramCanvas.drawPoint(mCoords.x, mCoords.y, mPaint);
        mPaint.setARGB(255, i3, 255 - i3, 128);
        drawOval(paramCanvas, mCoords.x, mCoords.y, mCoords.touchMajor, mCoords.touchMinor, mCoords.orientation, mPaint);
        mPaint.setARGB(255, i3, 128, 255 - i3);
        drawOval(paramCanvas, mCoords.x, mCoords.y, mCoords.toolMajor, mCoords.toolMinor, mCoords.orientation, mPaint);
        f5 = mCoords.toolMajor * 0.7F;
        f2 = f5;
        if (f5 < 20.0F) {
          f2 = 20.0F;
        }
        mPaint.setARGB(255, i3, 255, i1);
        f5 = (float)(Math.sin(mCoords.orientation) * f2);
        f4 = (float)(-Math.cos(mCoords.orientation) * f2);
        if ((mToolType != 2) && (mToolType != 4)) {
          paramCanvas.drawLine(mCoords.x - f5, mCoords.y - f4, mCoords.x + f5, mCoords.y + f4, mPaint);
        } else {
          paramCanvas.drawLine(mCoords.x, mCoords.y, mCoords.x + f5, mCoords.y + f4, mPaint);
        }
        f2 = (float)Math.sin(mCoords.getAxisValue(25));
        paramCanvas.drawCircle(mCoords.x + f5 * f2, mCoords.y + f4 * f2, 3.0F, mPaint);
        if (mHasBoundingBox) {
          paramCanvas.drawRect(mBoundingLeft, mBoundingTop, mBoundingRight, mBoundingBottom, mPaint);
        }
      }
    }
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getSource();
    if ((i & 0x2) != 0) {
      onPointerEvent(paramMotionEvent);
    } else if ((i & 0x10) != 0) {
      logMotionEvent("Joystick", paramMotionEvent);
    } else if ((i & 0x8) != 0) {
      logMotionEvent("Position", paramMotionEvent);
    } else {
      logMotionEvent("Generic", paramMotionEvent);
    }
    return true;
  }
  
  public void onInputDeviceAdded(int paramInt)
  {
    logInputDeviceState(paramInt, "Device Added");
  }
  
  public void onInputDeviceChanged(int paramInt)
  {
    logInputDeviceState(paramInt, "Device Changed");
  }
  
  public void onInputDeviceRemoved(int paramInt)
  {
    logInputDeviceState(paramInt, "Device Removed");
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (shouldLogKey(paramInt))
    {
      paramInt = paramKeyEvent.getRepeatCount();
      StringBuilder localStringBuilder;
      if (paramInt == 0)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Key Down: ");
        localStringBuilder.append(paramKeyEvent);
        Log.i("Pointer", localStringBuilder.toString());
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Key Repeat #");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(": ");
        localStringBuilder.append(paramKeyEvent);
        Log.i("Pointer", localStringBuilder.toString());
      }
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (shouldLogKey(paramInt))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Key Up: ");
      localStringBuilder.append(paramKeyEvent);
      Log.i("Pointer", localStringBuilder.toString());
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    mTextPaint.getFontMetricsInt(mTextMetrics);
    mHeaderBottom = (-mTextMetrics.ascent + mTextMetrics.descent + 2);
  }
  
  public void onPointerEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    int j = mPointers.size();
    int k = 1;
    int m;
    if (i != 0)
    {
      m = j;
      if ((i & 0xFF) == 5) {}
    }
    Object localObject1;
    Object localObject2;
    for (;;)
    {
      break;
      if (i == 0)
      {
        for (m = 0; m < j; m++)
        {
          localObject1 = (PointerState)mPointers.get(m);
          ((PointerState)localObject1).clearTrace();
          PointerState.access$102((PointerState)localObject1, false);
        }
        mCurDown = true;
        mCurNumPointers = 0;
        mMaxNumPointers = 0;
        mVelocity.clear();
        if (mAltVelocity != null) {
          mAltVelocity.clear();
        }
      }
      mCurNumPointers += 1;
      if (mMaxNumPointers < mCurNumPointers) {
        mMaxNumPointers = mCurNumPointers;
      }
      m = paramMotionEvent.getPointerId((i & 0xFF00) >> 8);
      while (j <= m)
      {
        localObject1 = new PointerState();
        mPointers.add(localObject1);
        j++;
      }
      if ((mActivePointerId < 0) || (!mPointers.get(mActivePointerId)).mCurDown)) {
        mActivePointerId = m;
      }
      localObject2 = (PointerState)mPointers.get(m);
      PointerState.access$102((PointerState)localObject2, true);
      localObject1 = InputDevice.getDevice(paramMotionEvent.getDeviceId());
      boolean bool;
      if ((localObject1 != null) && (((InputDevice)localObject1).getMotionRange(32) != null)) {
        bool = true;
      } else {
        bool = false;
      }
      PointerState.access$1302((PointerState)localObject2, bool);
      m = j;
    }
    int n = paramMotionEvent.getPointerCount();
    mVelocity.addMovement(paramMotionEvent);
    mVelocity.computeCurrentVelocity(1);
    if (mAltVelocity != null)
    {
      mAltVelocity.addMovement(paramMotionEvent);
      mAltVelocity.computeCurrentVelocity(1);
    }
    int i1 = paramMotionEvent.getHistorySize();
    for (j = 0; j < i1; j++) {
      for (int i2 = 0; i2 < n; i2++)
      {
        int i3 = paramMotionEvent.getPointerId(i2);
        if (mCurDown) {
          localObject1 = (PointerState)mPointers.get(i3);
        } else {
          localObject1 = null;
        }
        if (localObject1 != null) {
          localObject2 = mCoords;
        } else {
          localObject2 = mTempCoords;
        }
        paramMotionEvent.getHistoricalPointerCoords(i2, j, (MotionEvent.PointerCoords)localObject2);
        if (mPrintCoords) {
          logCoords("Pointer", i, i2, (MotionEvent.PointerCoords)localObject2, i3, paramMotionEvent);
        }
        if (localObject1 != null) {
          ((PointerState)localObject1).addTrace(x, y, false);
        }
      }
    }
    for (j = 0; j < n; j++)
    {
      i1 = paramMotionEvent.getPointerId(j);
      if (mCurDown) {
        localObject1 = (PointerState)mPointers.get(i1);
      } else {
        localObject1 = null;
      }
      if (localObject1 != null) {
        localObject2 = mCoords;
      } else {
        localObject2 = mTempCoords;
      }
      paramMotionEvent.getPointerCoords(j, (MotionEvent.PointerCoords)localObject2);
      if (mPrintCoords) {
        logCoords("Pointer", i, j, (MotionEvent.PointerCoords)localObject2, i1, paramMotionEvent);
      }
      if (localObject1 != null)
      {
        ((PointerState)localObject1).addTrace(x, y, true);
        PointerState.access$502((PointerState)localObject1, mVelocity.getXVelocity(i1));
        PointerState.access$602((PointerState)localObject1, mVelocity.getYVelocity(i1));
        mVelocity.getEstimator(i1, mEstimator);
        if (mAltVelocity != null)
        {
          PointerState.access$1002((PointerState)localObject1, mAltVelocity.getXVelocity(i1));
          PointerState.access$1102((PointerState)localObject1, mAltVelocity.getYVelocity(i1));
          mAltVelocity.getEstimator(i1, mAltEstimator);
        }
        PointerState.access$1202((PointerState)localObject1, paramMotionEvent.getToolType(j));
        if (mHasBoundingBox)
        {
          PointerState.access$1402((PointerState)localObject1, paramMotionEvent.getAxisValue(32, j));
          PointerState.access$1502((PointerState)localObject1, paramMotionEvent.getAxisValue(33, j));
          PointerState.access$1602((PointerState)localObject1, paramMotionEvent.getAxisValue(34, j));
          PointerState.access$1702((PointerState)localObject1, paramMotionEvent.getAxisValue(35, j));
        }
      }
    }
    if ((i == 1) || (i == 3) || ((i & 0xFF) == 6))
    {
      j = (0xFF00 & i) >> 8;
      i1 = paramMotionEvent.getPointerId(j);
      if (i1 >= m)
      {
        paramMotionEvent = new StringBuilder();
        paramMotionEvent.append("Got pointer ID out of bounds: id=");
        paramMotionEvent.append(i1);
        paramMotionEvent.append(" arraysize=");
        paramMotionEvent.append(m);
        paramMotionEvent.append(" pointerindex=");
        paramMotionEvent.append(j);
        paramMotionEvent.append(" action=0x");
        paramMotionEvent.append(Integer.toHexString(i));
        Slog.wtf("Pointer", paramMotionEvent.toString());
        return;
      }
      localObject1 = (PointerState)mPointers.get(i1);
      PointerState.access$102((PointerState)localObject1, false);
      if ((i != 1) && (i != 3))
      {
        mCurNumPointers -= 1;
        if (mActivePointerId == i1)
        {
          if (j == 0) {
            j = k;
          } else {
            j = 0;
          }
          mActivePointerId = paramMotionEvent.getPointerId(j);
        }
        ((PointerState)localObject1).addTrace(NaN.0F, NaN.0F, false);
      }
      else
      {
        mCurDown = false;
        mCurNumPointers = 0;
      }
    }
    invalidate();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    onPointerEvent(paramMotionEvent);
    if ((paramMotionEvent.getAction() == 0) && (!isFocused())) {
      requestFocus();
    }
    return true;
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    logMotionEvent("Trackball", paramMotionEvent);
    return true;
  }
  
  public void setPrintCoords(boolean paramBoolean)
  {
    mPrintCoords = paramBoolean;
  }
  
  private static final class FasterStringBuilder
  {
    private char[] mChars = new char[64];
    private int mLength;
    
    public FasterStringBuilder() {}
    
    private int reserve(int paramInt)
    {
      int i = mLength;
      int j = mLength;
      char[] arrayOfChar1 = mChars;
      int k = arrayOfChar1.length;
      if (j + paramInt > k)
      {
        char[] arrayOfChar2 = new char[k * 2];
        System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, i);
        mChars = arrayOfChar2;
      }
      return i;
    }
    
    public FasterStringBuilder append(float paramFloat, int paramInt)
    {
      int i = 1;
      for (int j = 0; j < paramInt; j++) {
        i *= 10;
      }
      paramFloat = (float)(Math.rint(i * paramFloat) / i);
      append((int)paramFloat);
      if (paramInt != 0)
      {
        append(".");
        paramFloat = Math.abs(paramFloat);
        paramFloat = (float)(paramFloat - Math.floor(paramFloat));
        append((int)(i * paramFloat), paramInt);
      }
      return this;
    }
    
    public FasterStringBuilder append(int paramInt)
    {
      return append(paramInt, 0);
    }
    
    public FasterStringBuilder append(int paramInt1, int paramInt2)
    {
      if (paramInt1 < 0) {
        i = 1;
      } else {
        i = 0;
      }
      int j = paramInt1;
      if (i != 0)
      {
        paramInt1 = -paramInt1;
        j = paramInt1;
        if (paramInt1 < 0)
        {
          append("-2147483648");
          return this;
        }
      }
      paramInt1 = reserve(11);
      char[] arrayOfChar = mChars;
      if (j == 0)
      {
        arrayOfChar[paramInt1] = ((char)48);
        mLength += 1;
        return this;
      }
      if (i != 0)
      {
        i = paramInt1 + 1;
        arrayOfChar[paramInt1] = ((char)45);
        paramInt1 = i;
      }
      int k = 1000000000;
      int m = 10;
      int i = paramInt1;
      paramInt1 = k;
      int n;
      int i1;
      for (;;)
      {
        n = paramInt1;
        k = i;
        i1 = j;
        if (j >= paramInt1) {
          break;
        }
        k = paramInt1 / 10;
        n = m - 1;
        m = n;
        paramInt1 = k;
        if (n < paramInt2)
        {
          arrayOfChar[i] = ((char)48);
          i++;
          m = n;
          paramInt1 = k;
        }
      }
      for (;;)
      {
        paramInt2 = i1 / n;
        i1 -= paramInt2 * n;
        n /= 10;
        paramInt1 = k + 1;
        arrayOfChar[k] = ((char)(char)(paramInt2 + 48));
        if (n == 0)
        {
          mLength = paramInt1;
          return this;
        }
        k = paramInt1;
      }
    }
    
    public FasterStringBuilder append(String paramString)
    {
      int i = paramString.length();
      int j = reserve(i);
      paramString.getChars(0, i, mChars, j);
      mLength += i;
      return this;
    }
    
    public FasterStringBuilder clear()
    {
      mLength = 0;
      return this;
    }
    
    public String toString()
    {
      return new String(mChars, 0, mLength);
    }
  }
  
  public static class PointerState
  {
    private VelocityTracker.Estimator mAltEstimator = new VelocityTracker.Estimator();
    private float mAltXVelocity;
    private float mAltYVelocity;
    private float mBoundingBottom;
    private float mBoundingLeft;
    private float mBoundingRight;
    private float mBoundingTop;
    private MotionEvent.PointerCoords mCoords = new MotionEvent.PointerCoords();
    private boolean mCurDown;
    private VelocityTracker.Estimator mEstimator = new VelocityTracker.Estimator();
    private boolean mHasBoundingBox;
    private int mToolType;
    private int mTraceCount;
    private boolean[] mTraceCurrent = new boolean[32];
    private float[] mTraceX = new float[32];
    private float[] mTraceY = new float[32];
    private float mXVelocity;
    private float mYVelocity;
    
    public PointerState() {}
    
    public void addTrace(float paramFloat1, float paramFloat2, boolean paramBoolean)
    {
      int i = mTraceX.length;
      if (mTraceCount == i)
      {
        i *= 2;
        Object localObject = new float[i];
        System.arraycopy(mTraceX, 0, localObject, 0, mTraceCount);
        mTraceX = ((float[])localObject);
        localObject = new float[i];
        System.arraycopy(mTraceY, 0, localObject, 0, mTraceCount);
        mTraceY = ((float[])localObject);
        localObject = new boolean[i];
        System.arraycopy(mTraceCurrent, 0, localObject, 0, mTraceCount);
        mTraceCurrent = ((boolean[])localObject);
      }
      mTraceX[mTraceCount] = paramFloat1;
      mTraceY[mTraceCount] = paramFloat2;
      mTraceCurrent[mTraceCount] = paramBoolean;
      mTraceCount += 1;
    }
    
    public void clearTrace()
    {
      mTraceCount = 0;
    }
  }
}
