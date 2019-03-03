package android.hardware.camera2.params;

import android.graphics.Point;
import android.graphics.Rect;

public final class Face
{
  public static final int ID_UNSUPPORTED = -1;
  public static final int SCORE_MAX = 100;
  public static final int SCORE_MIN = 1;
  private final Rect mBounds;
  private final int mId;
  private final Point mLeftEye;
  private final Point mMouth;
  private final Point mRightEye;
  private final int mScore;
  
  public Face(Rect paramRect, int paramInt)
  {
    this(paramRect, paramInt, -1, null, null, null);
  }
  
  public Face(Rect paramRect, int paramInt1, int paramInt2, Point paramPoint1, Point paramPoint2, Point paramPoint3)
  {
    checkNotNull("bounds", paramRect);
    if ((paramInt1 >= 1) && (paramInt1 <= 100))
    {
      if ((paramInt2 < 0) && (paramInt2 != -1)) {
        throw new IllegalArgumentException("Id out of range");
      }
      if (paramInt2 == -1)
      {
        checkNull("leftEyePosition", paramPoint1);
        checkNull("rightEyePosition", paramPoint2);
        checkNull("mouthPosition", paramPoint3);
      }
      mBounds = paramRect;
      mScore = paramInt1;
      mId = paramInt2;
      mLeftEye = paramPoint1;
      mRightEye = paramPoint2;
      mMouth = paramPoint3;
      return;
    }
    throw new IllegalArgumentException("Confidence out of range");
  }
  
  private static void checkNotNull(String paramString, Object paramObject)
  {
    if (paramObject != null) {
      return;
    }
    paramObject = new StringBuilder();
    paramObject.append(paramString);
    paramObject.append(" was required, but it was null");
    throw new IllegalArgumentException(paramObject.toString());
  }
  
  private static void checkNull(String paramString, Object paramObject)
  {
    if (paramObject == null) {
      return;
    }
    paramObject = new StringBuilder();
    paramObject.append(paramString);
    paramObject.append(" was required to be null, but it wasn't");
    throw new IllegalArgumentException(paramObject.toString());
  }
  
  public Rect getBounds()
  {
    return mBounds;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public Point getLeftEyePosition()
  {
    return mLeftEye;
  }
  
  public Point getMouthPosition()
  {
    return mMouth;
  }
  
  public Point getRightEyePosition()
  {
    return mRightEye;
  }
  
  public int getScore()
  {
    return mScore;
  }
  
  public String toString()
  {
    return String.format("{ bounds: %s, score: %s, id: %d, leftEyePosition: %s, rightEyePosition: %s, mouthPosition: %s }", new Object[] { mBounds, Integer.valueOf(mScore), Integer.valueOf(mId), mLeftEye, mRightEye, mMouth });
  }
}
