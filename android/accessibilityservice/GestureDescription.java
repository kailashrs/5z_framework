package android.accessibilityservice;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.List;

public final class GestureDescription
{
  private static final long MAX_GESTURE_DURATION_MS = 60000L;
  private static final int MAX_STROKE_COUNT = 10;
  private final List<StrokeDescription> mStrokes = new ArrayList();
  private final float[] mTempPos = new float[2];
  
  private GestureDescription() {}
  
  private GestureDescription(List<StrokeDescription> paramList)
  {
    mStrokes.addAll(paramList);
  }
  
  public static long getMaxGestureDuration()
  {
    return 60000L;
  }
  
  public static int getMaxStrokeCount()
  {
    return 10;
  }
  
  private long getNextKeyPointAtLeast(long paramLong)
  {
    long l1 = Long.MAX_VALUE;
    for (int i = 0; i < mStrokes.size(); i++)
    {
      long l2 = mStrokes.get(i)).mStartTime;
      long l3 = l1;
      if (l2 < l1)
      {
        l3 = l1;
        if (l2 >= paramLong) {
          l3 = l2;
        }
      }
      l2 = mStrokes.get(i)).mEndTime;
      l1 = l3;
      if (l2 < l3)
      {
        l1 = l3;
        if (l2 >= paramLong) {
          l1 = l2;
        }
      }
    }
    if (l1 == Long.MAX_VALUE) {
      paramLong = -1L;
    } else {
      paramLong = l1;
    }
    return paramLong;
  }
  
  private int getPointsForTime(long paramLong, TouchPoint[] paramArrayOfTouchPoint)
  {
    int i = 0;
    int j = 0;
    while (j < mStrokes.size())
    {
      StrokeDescription localStrokeDescription = (StrokeDescription)mStrokes.get(j);
      int k = i;
      if (localStrokeDescription.hasPointForTime(paramLong))
      {
        mStrokeId = localStrokeDescription.getId();
        mContinuedStrokeId = localStrokeDescription.getContinuedStrokeId();
        TouchPoint localTouchPoint = paramArrayOfTouchPoint[i];
        boolean bool;
        if ((localStrokeDescription.getContinuedStrokeId() < 0) && (paramLong == mStartTime)) {
          bool = true;
        } else {
          bool = false;
        }
        mIsStartOfPath = bool;
        localTouchPoint = paramArrayOfTouchPoint[i];
        if ((!localStrokeDescription.willContinue()) && (paramLong == mEndTime)) {
          bool = true;
        } else {
          bool = false;
        }
        mIsEndOfPath = bool;
        localStrokeDescription.getPosForTime(paramLong, mTempPos);
        mX = Math.round(mTempPos[0]);
        mY = Math.round(mTempPos[1]);
        k = i + 1;
      }
      j++;
      i = k;
    }
    return i;
  }
  
  private static long getTotalDuration(List<StrokeDescription> paramList)
  {
    long l = Long.MIN_VALUE;
    for (int i = 0; i < paramList.size(); i++) {
      l = Math.max(l, getmEndTime);
    }
    return Math.max(l, 0L);
  }
  
  public StrokeDescription getStroke(int paramInt)
  {
    return (StrokeDescription)mStrokes.get(paramInt);
  }
  
  public int getStrokeCount()
  {
    return mStrokes.size();
  }
  
  public static class Builder
  {
    private final List<GestureDescription.StrokeDescription> mStrokes = new ArrayList();
    
    public Builder() {}
    
    public Builder addStroke(GestureDescription.StrokeDescription paramStrokeDescription)
    {
      if (mStrokes.size() < 10)
      {
        mStrokes.add(paramStrokeDescription);
        if (GestureDescription.getTotalDuration(mStrokes) <= 60000L) {
          return this;
        }
        mStrokes.remove(paramStrokeDescription);
        throw new IllegalStateException("Gesture would exceed maximum duration with new stroke");
      }
      throw new IllegalStateException("Attempting to add too many strokes to a gesture");
    }
    
    public GestureDescription build()
    {
      if (mStrokes.size() != 0) {
        return new GestureDescription(mStrokes, null);
      }
      throw new IllegalStateException("Gestures must have at least one stroke");
    }
  }
  
  public static class GestureStep
    implements Parcelable
  {
    public static final Parcelable.Creator<GestureStep> CREATOR = new Parcelable.Creator()
    {
      public GestureDescription.GestureStep createFromParcel(Parcel paramAnonymousParcel)
      {
        return new GestureDescription.GestureStep(paramAnonymousParcel);
      }
      
      public GestureDescription.GestureStep[] newArray(int paramAnonymousInt)
      {
        return new GestureDescription.GestureStep[paramAnonymousInt];
      }
    };
    public int numTouchPoints;
    public long timeSinceGestureStart;
    public GestureDescription.TouchPoint[] touchPoints;
    
    public GestureStep(long paramLong, int paramInt, GestureDescription.TouchPoint[] paramArrayOfTouchPoint)
    {
      timeSinceGestureStart = paramLong;
      numTouchPoints = paramInt;
      touchPoints = new GestureDescription.TouchPoint[paramInt];
      for (int i = 0; i < paramInt; i++) {
        touchPoints[i] = new GestureDescription.TouchPoint(paramArrayOfTouchPoint[i]);
      }
    }
    
    public GestureStep(Parcel paramParcel)
    {
      timeSinceGestureStart = paramParcel.readLong();
      paramParcel = paramParcel.readParcelableArray(GestureDescription.TouchPoint.class.getClassLoader());
      int i = 0;
      if (paramParcel == null) {
        j = 0;
      } else {
        j = paramParcel.length;
      }
      numTouchPoints = j;
      touchPoints = new GestureDescription.TouchPoint[numTouchPoints];
      for (int j = i; j < numTouchPoints; j++) {
        touchPoints[j] = ((GestureDescription.TouchPoint)paramParcel[j]);
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(timeSinceGestureStart);
      paramParcel.writeParcelableArray(touchPoints, paramInt);
    }
  }
  
  public static class MotionEventGenerator
  {
    private static GestureDescription.TouchPoint[] sCurrentTouchPoints;
    
    public MotionEventGenerator() {}
    
    private static GestureDescription.TouchPoint[] getCurrentTouchPoints(int paramInt)
    {
      if ((sCurrentTouchPoints == null) || (sCurrentTouchPoints.length < paramInt))
      {
        sCurrentTouchPoints = new GestureDescription.TouchPoint[paramInt];
        for (int i = 0; i < paramInt; i++) {
          sCurrentTouchPoints[i] = new GestureDescription.TouchPoint();
        }
      }
      return sCurrentTouchPoints;
    }
    
    public static List<GestureDescription.GestureStep> getGestureStepsFromGestureDescription(GestureDescription paramGestureDescription, int paramInt)
    {
      ArrayList localArrayList = new ArrayList();
      GestureDescription.TouchPoint[] arrayOfTouchPoint = getCurrentTouchPoints(paramGestureDescription.getStrokeCount());
      int i = 0;
      long l1 = 0L;
      long l3;
      for (long l2 = paramGestureDescription.getNextKeyPointAtLeast(0L); l2 >= 0L; l2 = l3)
      {
        if (i != 0) {
          l2 = Math.min(l2, paramInt + l1);
        }
        i = paramGestureDescription.getPointsForTime(l2, arrayOfTouchPoint);
        localArrayList.add(new GestureDescription.GestureStep(l2, i, arrayOfTouchPoint));
        l3 = paramGestureDescription.getNextKeyPointAtLeast(1L + l2);
        l1 = l2;
      }
      return localArrayList;
    }
  }
  
  public static class StrokeDescription
  {
    private static final int INVALID_STROKE_ID = -1;
    static int sIdCounter;
    boolean mContinued;
    int mContinuedStrokeId = -1;
    long mEndTime;
    int mId;
    Path mPath;
    private PathMeasure mPathMeasure;
    long mStartTime;
    float[] mTapLocation;
    private float mTimeToLengthConversion;
    
    public StrokeDescription(Path paramPath, long paramLong1, long paramLong2)
    {
      this(paramPath, paramLong1, paramLong2, false);
    }
    
    public StrokeDescription(Path paramPath, long paramLong1, long paramLong2, boolean paramBoolean)
    {
      mContinued = paramBoolean;
      boolean bool = true;
      if (paramLong2 > 0L) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      Preconditions.checkArgument(paramBoolean, "Duration must be positive");
      if (paramLong1 >= 0L) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      Preconditions.checkArgument(paramBoolean, "Start time must not be negative");
      Preconditions.checkArgument(paramPath.isEmpty() ^ true, "Path is empty");
      RectF localRectF = new RectF();
      paramPath.computeBounds(localRectF, false);
      if ((bottom >= 0.0F) && (top >= 0.0F) && (right >= 0.0F) && (left >= 0.0F)) {
        paramBoolean = bool;
      } else {
        paramBoolean = false;
      }
      Preconditions.checkArgument(paramBoolean, "Path bounds must not be negative");
      mPath = new Path(paramPath);
      mPathMeasure = new PathMeasure(paramPath, false);
      if (mPathMeasure.getLength() == 0.0F)
      {
        paramPath = new Path(paramPath);
        paramPath.lineTo(-1.0F, -1.0F);
        mTapLocation = new float[2];
        new PathMeasure(paramPath, false).getPosTan(0.0F, mTapLocation, null);
      }
      if (!mPathMeasure.nextContour())
      {
        mPathMeasure.setPath(mPath, false);
        mStartTime = paramLong1;
        mEndTime = (paramLong1 + paramLong2);
        mTimeToLengthConversion = (getLength() / (float)paramLong2);
        int i = sIdCounter;
        sIdCounter = i + 1;
        mId = i;
        return;
      }
      throw new IllegalArgumentException("Path has more than one contour");
    }
    
    public StrokeDescription continueStroke(Path paramPath, long paramLong1, long paramLong2, boolean paramBoolean)
    {
      if (mContinued)
      {
        paramPath = new StrokeDescription(paramPath, paramLong1, paramLong2, paramBoolean);
        mContinuedStrokeId = mId;
        return paramPath;
      }
      throw new IllegalStateException("Only strokes marked willContinue can be continued");
    }
    
    public int getContinuedStrokeId()
    {
      return mContinuedStrokeId;
    }
    
    public long getDuration()
    {
      return mEndTime - mStartTime;
    }
    
    public int getId()
    {
      return mId;
    }
    
    float getLength()
    {
      return mPathMeasure.getLength();
    }
    
    public Path getPath()
    {
      return new Path(mPath);
    }
    
    boolean getPosForTime(long paramLong, float[] paramArrayOfFloat)
    {
      if (mTapLocation != null)
      {
        paramArrayOfFloat[0] = mTapLocation[0];
        paramArrayOfFloat[1] = mTapLocation[1];
        return true;
      }
      if (paramLong == mEndTime) {
        return mPathMeasure.getPosTan(getLength(), paramArrayOfFloat, null);
      }
      float f1 = mTimeToLengthConversion;
      float f2 = (float)(paramLong - mStartTime);
      return mPathMeasure.getPosTan(f1 * f2, paramArrayOfFloat, null);
    }
    
    public long getStartTime()
    {
      return mStartTime;
    }
    
    boolean hasPointForTime(long paramLong)
    {
      boolean bool;
      if ((paramLong >= mStartTime) && (paramLong <= mEndTime)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean willContinue()
    {
      return mContinued;
    }
  }
  
  public static class TouchPoint
    implements Parcelable
  {
    public static final Parcelable.Creator<TouchPoint> CREATOR = new Parcelable.Creator()
    {
      public GestureDescription.TouchPoint createFromParcel(Parcel paramAnonymousParcel)
      {
        return new GestureDescription.TouchPoint(paramAnonymousParcel);
      }
      
      public GestureDescription.TouchPoint[] newArray(int paramAnonymousInt)
      {
        return new GestureDescription.TouchPoint[paramAnonymousInt];
      }
    };
    private static final int FLAG_IS_END_OF_PATH = 2;
    private static final int FLAG_IS_START_OF_PATH = 1;
    public int mContinuedStrokeId;
    public boolean mIsEndOfPath;
    public boolean mIsStartOfPath;
    public int mStrokeId;
    public float mX;
    public float mY;
    
    public TouchPoint() {}
    
    public TouchPoint(TouchPoint paramTouchPoint)
    {
      copyFrom(paramTouchPoint);
    }
    
    public TouchPoint(Parcel paramParcel)
    {
      mStrokeId = paramParcel.readInt();
      mContinuedStrokeId = paramParcel.readInt();
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if ((i & 0x1) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mIsStartOfPath = bool2;
      boolean bool2 = bool1;
      if ((i & 0x2) != 0) {
        bool2 = true;
      }
      mIsEndOfPath = bool2;
      mX = paramParcel.readFloat();
      mY = paramParcel.readFloat();
    }
    
    public void copyFrom(TouchPoint paramTouchPoint)
    {
      mStrokeId = mStrokeId;
      mContinuedStrokeId = mContinuedStrokeId;
      mIsStartOfPath = mIsStartOfPath;
      mIsEndOfPath = mIsEndOfPath;
      mX = mX;
      mY = mY;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("TouchPoint{mStrokeId=");
      localStringBuilder.append(mStrokeId);
      localStringBuilder.append(", mContinuedStrokeId=");
      localStringBuilder.append(mContinuedStrokeId);
      localStringBuilder.append(", mIsStartOfPath=");
      localStringBuilder.append(mIsStartOfPath);
      localStringBuilder.append(", mIsEndOfPath=");
      localStringBuilder.append(mIsEndOfPath);
      localStringBuilder.append(", mX=");
      localStringBuilder.append(mX);
      localStringBuilder.append(", mY=");
      localStringBuilder.append(mY);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mStrokeId);
      paramParcel.writeInt(mContinuedStrokeId);
      int i = mIsStartOfPath;
      if (mIsEndOfPath) {
        paramInt = 2;
      } else {
        paramInt = 0;
      }
      paramParcel.writeInt(i | paramInt);
      paramParcel.writeFloat(mX);
      paramParcel.writeFloat(mY);
    }
  }
}
