package com.android.internal.policy;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PipSnapAlgorithm
{
  private static final float CORNER_MAGNET_THRESHOLD = 0.3F;
  private static final int SNAP_MODE_CORNERS_AND_SIDES = 1;
  private static final int SNAP_MODE_CORNERS_ONLY = 0;
  private static final int SNAP_MODE_EDGE = 2;
  private static final int SNAP_MODE_EDGE_MAGNET_CORNERS = 3;
  private static final int SNAP_MODE_LONG_EDGE_MAGNET_CORNERS = 4;
  private final Context mContext;
  private final float mDefaultSizePercent;
  private final int mDefaultSnapMode = 3;
  private final int mFlingDeceleration;
  private boolean mIsMinimized;
  private final float mMaxAspectRatioForMinSize;
  private final float mMinAspectRatioForMinSize;
  private final int mMinimizedVisibleSize;
  private int mOrientation = 0;
  private final ArrayList<Integer> mSnapGravities = new ArrayList();
  private int mSnapMode = 3;
  
  public PipSnapAlgorithm(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    mContext = paramContext;
    mMinimizedVisibleSize = localResources.getDimensionPixelSize(17105372);
    mDefaultSizePercent = localResources.getFloat(17105085);
    mMaxAspectRatioForMinSize = localResources.getFloat(17105083);
    mMinAspectRatioForMinSize = (1.0F / mMaxAspectRatioForMinSize);
    mFlingDeceleration = mContext.getResources().getDimensionPixelSize(17105371);
    onConfigurationChanged();
  }
  
  private void calculateSnapTargets()
  {
    mSnapGravities.clear();
    switch (mSnapMode)
    {
    case 2: 
    default: 
      break;
    case 1: 
      if (mOrientation == 2)
      {
        mSnapGravities.add(Integer.valueOf(49));
        mSnapGravities.add(Integer.valueOf(81));
      }
      else
      {
        mSnapGravities.add(Integer.valueOf(19));
        mSnapGravities.add(Integer.valueOf(21));
      }
      break;
    }
    mSnapGravities.add(Integer.valueOf(51));
    mSnapGravities.add(Integer.valueOf(53));
    mSnapGravities.add(Integer.valueOf(83));
    mSnapGravities.add(Integer.valueOf(85));
  }
  
  private float distanceToPoint(Point paramPoint, int paramInt1, int paramInt2)
  {
    return PointF.length(x - paramInt1, y - paramInt2);
  }
  
  private Point findClosestPoint(int paramInt1, int paramInt2, Point[] paramArrayOfPoint)
  {
    Object localObject = null;
    float f1 = Float.MAX_VALUE;
    int i = paramArrayOfPoint.length;
    int j = 0;
    while (j < i)
    {
      Point localPoint = paramArrayOfPoint[j];
      float f2 = distanceToPoint(localPoint, paramInt1, paramInt2);
      float f3 = f1;
      if (f2 < f1)
      {
        localObject = localPoint;
        f3 = f2;
      }
      j++;
      f1 = f3;
    }
    return localObject;
  }
  
  private int findX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (int)((paramFloat3 - paramFloat2) / paramFloat1);
  }
  
  private int findY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (int)(paramFloat1 * paramFloat3 + paramFloat2);
  }
  
  private void snapRectToClosestEdge(Rect paramRect1, Rect paramRect2, Rect paramRect3)
  {
    int i = Math.max(left, Math.min(right, left));
    int j = Math.max(top, Math.min(bottom, top));
    paramRect3.set(paramRect1);
    if (mIsMinimized)
    {
      paramRect3.offsetTo(i, j);
      return;
    }
    int k = Math.abs(left - left);
    int m = Math.abs(top - top);
    int n = Math.abs(right - left);
    int i1 = Math.abs(bottom - top);
    if (mSnapMode == 4)
    {
      if (mOrientation == 2) {
        i1 = Math.min(m, i1);
      } else {
        i1 = Math.min(k, n);
      }
    }
    else {
      i1 = Math.min(Math.min(k, n), Math.min(m, i1));
    }
    if (i1 == k) {
      paramRect3.offsetTo(left, j);
    } else if (i1 == m) {
      paramRect3.offsetTo(i, top);
    } else if (i1 == n) {
      paramRect3.offsetTo(right, j);
    } else {
      paramRect3.offsetTo(i, bottom);
    }
  }
  
  public void applyMinimizedOffset(Rect paramRect1, Rect paramRect2, Point paramPoint, Rect paramRect3)
  {
    if (left <= paramRect2.centerX()) {
      paramRect1.offsetTo(left + mMinimizedVisibleSize - paramRect1.width(), top);
    } else {
      paramRect1.offsetTo(x - right - mMinimizedVisibleSize, top);
    }
  }
  
  public void applySnapFraction(Rect paramRect1, Rect paramRect2, float paramFloat)
  {
    if (paramFloat < 1.0F)
    {
      paramRect1.offsetTo(left + (int)(paramRect2.width() * paramFloat), top);
    }
    else
    {
      int i;
      int j;
      if (paramFloat < 2.0F)
      {
        i = top;
        j = (int)(paramRect2.height() * (paramFloat - 1.0F));
        paramRect1.offsetTo(right, i + j);
      }
      else if (paramFloat < 3.0F)
      {
        paramRect1.offsetTo(left + (int)((1.0F - (paramFloat - 2.0F)) * paramRect2.width()), bottom);
      }
      else
      {
        j = top;
        i = (int)((1.0F - (paramFloat - 3.0F)) * paramRect2.height());
        paramRect1.offsetTo(left, j + i);
      }
    }
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("  ");
    localObject = ((StringBuilder)localObject).toString();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(PipSnapAlgorithm.class.getSimpleName());
    paramPrintWriter.println(localStringBuilder.toString());
    paramString = new StringBuilder();
    paramString.append((String)localObject);
    paramString.append("mSnapMode=");
    paramString.append(mSnapMode);
    paramPrintWriter.println(paramString.toString());
    paramString = new StringBuilder();
    paramString.append((String)localObject);
    paramString.append("mOrientation=");
    paramString.append(mOrientation);
    paramPrintWriter.println(paramString.toString());
    paramString = new StringBuilder();
    paramString.append((String)localObject);
    paramString.append("mMinimizedVisibleSize=");
    paramString.append(mMinimizedVisibleSize);
    paramPrintWriter.println(paramString.toString());
    paramString = new StringBuilder();
    paramString.append((String)localObject);
    paramString.append("mIsMinimized=");
    paramString.append(mIsMinimized);
    paramPrintWriter.println(paramString.toString());
  }
  
  public Rect findClosestSnapBounds(Rect paramRect1, Rect paramRect2)
  {
    Object localObject1 = new Rect(left, top, right + paramRect2.width(), bottom + paramRect2.height());
    Rect localRect = new Rect(paramRect2);
    int i = mSnapMode;
    int j = 0;
    int k = 0;
    Object localObject2;
    if ((i != 4) && (mSnapMode != 3))
    {
      if (mSnapMode == 2)
      {
        snapRectToClosestEdge(paramRect2, paramRect1, localRect);
      }
      else
      {
        paramRect1 = new Rect();
        localObject2 = new Point[mSnapGravities.size()];
        while (k < mSnapGravities.size())
        {
          Gravity.apply(((Integer)mSnapGravities.get(k)).intValue(), paramRect2.width(), paramRect2.height(), (Rect)localObject1, 0, 0, paramRect1);
          localObject2[k] = new Point(left, top);
          k++;
        }
        paramRect1 = findClosestPoint(left, top, (Point[])localObject2);
        localRect.offsetTo(x, y);
      }
    }
    else
    {
      localObject2 = new Rect();
      Point[] arrayOfPoint = new Point[mSnapGravities.size()];
      for (k = j; k < mSnapGravities.size(); k++)
      {
        Gravity.apply(((Integer)mSnapGravities.get(k)).intValue(), paramRect2.width(), paramRect2.height(), (Rect)localObject1, 0, 0, (Rect)localObject2);
        arrayOfPoint[k] = new Point(left, top);
      }
      localObject1 = findClosestPoint(left, top, arrayOfPoint);
      if (distanceToPoint((Point)localObject1, left, top) < Math.max(paramRect2.width(), paramRect2.height()) * 0.3F) {
        localRect.offsetTo(x, y);
      } else {
        snapRectToClosestEdge(paramRect2, paramRect1, localRect);
      }
    }
    return localRect;
  }
  
  public Rect findClosestSnapBounds(Rect paramRect1, Rect paramRect2, float paramFloat1, float paramFloat2, Point paramPoint)
  {
    Rect localRect = new Rect(paramRect2);
    paramRect2 = getEdgeIntersect(paramRect2, paramRect1, paramFloat1, paramFloat2, paramPoint);
    localRect.offsetTo(x, y);
    return findClosestSnapBounds(paramRect1, localRect);
  }
  
  public Point getEdgeIntersect(Rect paramRect1, Rect paramRect2, float paramFloat1, float paramFloat2, Point paramPoint)
  {
    int i;
    if (mOrientation == 2) {
      i = 1;
    } else {
      i = 0;
    }
    int j = left;
    int k = top;
    float f1 = paramFloat2 / paramFloat1;
    float f2 = k - j * f1;
    Point localPoint1 = new Point();
    Point localPoint2 = new Point();
    int m;
    if (paramFloat1 > 0.0F) {
      m = right;
    } else {
      m = left;
    }
    x = m;
    y = findY(f1, f2, x);
    if (paramFloat2 > 0.0F) {
      m = bottom;
    } else {
      m = top;
    }
    y = m;
    x = findX(f1, f2, y);
    if (i != 0)
    {
      if (paramFloat1 > 0.0F) {
        m = right - left;
      } else {
        m = left - left;
      }
    }
    else if (paramFloat2 > 0.0F) {
      m = bottom - top;
    } else {
      m = top - top;
    }
    if (m > 0)
    {
      int n;
      if (i != 0) {
        n = y;
      } else {
        n = x;
      }
      int i1;
      if (i != 0) {
        i1 = y;
      } else {
        i1 = x;
      }
      int i2 = paramRect2.centerX();
      if (((n < i2) && (i1 < i2)) || ((n > i2) && (i1 > i2)))
      {
        if (i != 0) {
          d1 = paramFloat1;
        } else {
          d1 = paramFloat2;
        }
        m = Math.min((int)(0.0D - Math.pow(d1, 2.0D)) / (mFlingDeceleration * 2), m);
        if (i != 0)
        {
          i = left;
          if (paramFloat1 <= 0.0F) {
            m = -m;
          }
          x = (i + m);
        }
        else
        {
          i = top;
          if (paramFloat2 <= 0.0F) {
            m = -m;
          }
          y = (i + m);
        }
        return localPoint2;
      }
    }
    double d2 = Math.hypot(x - j, y - k);
    double d1 = Math.hypot(x - j, y - k);
    if (d2 == 0.0D) {
      return localPoint2;
    }
    if (d1 == 0.0D) {
      return localPoint1;
    }
    if (Math.abs(d2) > Math.abs(d1)) {
      paramRect1 = localPoint2;
    } else {
      paramRect1 = localPoint1;
    }
    return paramRect1;
  }
  
  public void getMovementBounds(Rect paramRect1, Rect paramRect2, Rect paramRect3, int paramInt)
  {
    paramRect3.set(paramRect2);
    right = Math.max(left, right - paramRect1.width());
    bottom = Math.max(top, bottom - paramRect1.height());
    bottom -= paramInt;
  }
  
  public Size getSizeForAspectRatio(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    paramInt1 = (int)Math.max(paramFloat2, Math.min(paramInt1, paramInt2) * mDefaultSizePercent);
    if ((paramFloat1 > mMinAspectRatioForMinSize) && (paramFloat1 <= mMaxAspectRatioForMinSize))
    {
      paramFloat2 = PointF.length(mMaxAspectRatioForMinSize * paramInt1, paramInt1);
      paramInt2 = (int)Math.round(Math.sqrt(paramFloat2 * paramFloat2 / (paramFloat1 * paramFloat1 + 1.0F)));
      paramInt1 = Math.round(paramInt2 * paramFloat1);
    }
    else if (paramFloat1 <= 1.0F)
    {
      paramInt2 = Math.round(paramInt1 / paramFloat1);
    }
    else
    {
      paramInt2 = paramInt1;
      paramInt1 = Math.round(paramInt2 * paramFloat1);
    }
    return new Size(paramInt1, paramInt2);
  }
  
  public float getSnapFraction(Rect paramRect1, Rect paramRect2)
  {
    Rect localRect = new Rect();
    snapRectToClosestEdge(paramRect1, paramRect2, localRect);
    float f1 = (left - left) / paramRect2.width();
    float f2 = (top - top) / paramRect2.height();
    if (top == top) {
      return f1;
    }
    if (left == right) {
      return 1.0F + f2;
    }
    if (top == bottom) {
      return 2.0F + (1.0F - f1);
    }
    return 3.0F + (1.0F - f2);
  }
  
  public void onConfigurationChanged()
  {
    Resources localResources = mContext.getResources();
    mOrientation = getConfigurationorientation;
    mSnapMode = localResources.getInteger(17694856);
    calculateSnapTargets();
  }
  
  public void setMinimized(boolean paramBoolean)
  {
    mIsMinimized = paramBoolean;
  }
}
