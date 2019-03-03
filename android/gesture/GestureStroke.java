package android.gesture;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GestureStroke
{
  static final float TOUCH_TOLERANCE = 3.0F;
  public final RectF boundingBox;
  public final float length;
  private Path mCachedPath;
  public final float[] points;
  private final long[] timestamps;
  
  private GestureStroke(RectF paramRectF, float paramFloat, float[] paramArrayOfFloat, long[] paramArrayOfLong)
  {
    boundingBox = new RectF(left, top, right, bottom);
    length = paramFloat;
    points = ((float[])paramArrayOfFloat.clone());
    timestamps = ((long[])paramArrayOfLong.clone());
  }
  
  public GestureStroke(ArrayList<GesturePoint> paramArrayList)
  {
    int i = paramArrayList.size();
    float[] arrayOfFloat = new float[i * 2];
    long[] arrayOfLong = new long[i];
    RectF localRectF = null;
    float f = 0.0F;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      GesturePoint localGesturePoint = (GesturePoint)paramArrayList.get(k);
      arrayOfFloat[(k * 2)] = x;
      arrayOfFloat[(k * 2 + 1)] = y;
      arrayOfLong[j] = timestamp;
      if (localRectF == null)
      {
        localRectF = new RectF();
        top = y;
        left = x;
        right = x;
        bottom = y;
        f = 0.0F;
      }
      else
      {
        f = (float)(f + Math.hypot(x - arrayOfFloat[((k - 1) * 2)], y - arrayOfFloat[((k - 1) * 2 + 1)]));
        localRectF.union(x, y);
      }
      j++;
    }
    timestamps = arrayOfLong;
    points = arrayOfFloat;
    boundingBox = localRectF;
    length = f;
  }
  
  static GestureStroke deserialize(DataInputStream paramDataInputStream)
    throws IOException
  {
    int i = paramDataInputStream.readInt();
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      localArrayList.add(GesturePoint.deserialize(paramDataInputStream));
    }
    return new GestureStroke(localArrayList);
  }
  
  private void makePath()
  {
    float[] arrayOfFloat = points;
    int i = arrayOfFloat.length;
    Object localObject1 = null;
    float f1 = 0.0F;
    float f2 = 0.0F;
    int j = 0;
    while (j < i)
    {
      float f3 = arrayOfFloat[j];
      float f4 = arrayOfFloat[(j + 1)];
      Object localObject2;
      float f5;
      float f6;
      if (localObject1 == null)
      {
        localObject2 = new Path();
        ((Path)localObject2).moveTo(f3, f4);
        f5 = f3;
        f6 = f4;
      }
      else
      {
        f5 = Math.abs(f3 - f1);
        float f7 = Math.abs(f4 - f2);
        if (f5 < 3.0F)
        {
          localObject2 = localObject1;
          f5 = f1;
          f6 = f2;
          if (f7 < 3.0F) {}
        }
        else
        {
          localObject1.quadTo(f1, f2, (f3 + f1) / 2.0F, (f4 + f2) / 2.0F);
          f5 = f3;
          f6 = f4;
          localObject2 = localObject1;
        }
      }
      j += 2;
      localObject1 = localObject2;
      f1 = f5;
      f2 = f6;
    }
    mCachedPath = localObject1;
  }
  
  public void clearPath()
  {
    if (mCachedPath != null) {
      mCachedPath.rewind();
    }
  }
  
  public Object clone()
  {
    return new GestureStroke(boundingBox, length, points, timestamps);
  }
  
  public OrientedBoundingBox computeOrientedBoundingBox()
  {
    return GestureUtils.computeOrientedBoundingBox(points);
  }
  
  void draw(Canvas paramCanvas, Paint paramPaint)
  {
    if (mCachedPath == null) {
      makePath();
    }
    paramCanvas.drawPath(mCachedPath, paramPaint);
  }
  
  public Path getPath()
  {
    if (mCachedPath == null) {
      makePath();
    }
    return mCachedPath;
  }
  
  void serialize(DataOutputStream paramDataOutputStream)
    throws IOException
  {
    float[] arrayOfFloat = points;
    long[] arrayOfLong = timestamps;
    int i = points.length;
    paramDataOutputStream.writeInt(i / 2);
    for (int j = 0; j < i; j += 2)
    {
      paramDataOutputStream.writeFloat(arrayOfFloat[j]);
      paramDataOutputStream.writeFloat(arrayOfFloat[(j + 1)]);
      paramDataOutputStream.writeLong(arrayOfLong[(j / 2)]);
    }
  }
  
  public Path toPath(float paramFloat1, float paramFloat2, int paramInt)
  {
    float[] arrayOfFloat = GestureUtils.temporalSampling(this, paramInt);
    Object localObject1 = boundingBox;
    GestureUtils.translate(arrayOfFloat, -left, -top);
    paramFloat1 /= ((RectF)localObject1).width();
    paramFloat2 /= ((RectF)localObject1).height();
    if (paramFloat1 > paramFloat2) {
      paramFloat1 = paramFloat2;
    }
    GestureUtils.scale(arrayOfFloat, paramFloat1, paramFloat1);
    float f1 = 0.0F;
    float f2 = 0.0F;
    Object localObject2 = null;
    int i = arrayOfFloat.length;
    paramInt = 0;
    while (paramInt < i)
    {
      float f3 = arrayOfFloat[paramInt];
      float f4 = arrayOfFloat[(paramInt + 1)];
      if (localObject2 == null)
      {
        localObject1 = new Path();
        ((Path)localObject1).moveTo(f3, f4);
        paramFloat1 = f3;
        paramFloat2 = f4;
      }
      else
      {
        paramFloat1 = Math.abs(f3 - f1);
        float f5 = Math.abs(f4 - f2);
        if (paramFloat1 < 3.0F)
        {
          paramFloat1 = f1;
          paramFloat2 = f2;
          localObject1 = localObject2;
          if (f5 < 3.0F) {}
        }
        else
        {
          localObject2.quadTo(f1, f2, (f3 + f1) / 2.0F, (f4 + f2) / 2.0F);
          paramFloat1 = f3;
          paramFloat2 = f4;
          localObject1 = localObject2;
        }
      }
      paramInt += 2;
      f1 = paramFloat1;
      f2 = paramFloat2;
      localObject2 = localObject1;
    }
    return localObject2;
  }
}
