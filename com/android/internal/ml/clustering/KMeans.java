package com.android.internal.ml.clustering;

import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans
{
  private static final boolean DEBUG = false;
  private static final String TAG = "KMeans";
  private final int mMaxIterations;
  private final Random mRandomState;
  private float mSqConvergenceEpsilon;
  
  public KMeans()
  {
    this(new Random());
  }
  
  public KMeans(Random paramRandom)
  {
    this(paramRandom, 30, 0.005F);
  }
  
  public KMeans(Random paramRandom, int paramInt, float paramFloat)
  {
    mRandomState = paramRandom;
    mMaxIterations = paramInt;
    mSqConvergenceEpsilon = (paramFloat * paramFloat);
  }
  
  @VisibleForTesting
  public static Mean nearestMean(float[] paramArrayOfFloat, List<Mean> paramList)
  {
    Object localObject = null;
    float f1 = Float.MAX_VALUE;
    int i = paramList.size();
    int j = 0;
    while (j < i)
    {
      Mean localMean = (Mean)paramList.get(j);
      float f2 = sqDistance(paramArrayOfFloat, mCentroid);
      float f3 = f1;
      if (f2 < f1)
      {
        localObject = localMean;
        f3 = f2;
      }
      j++;
      f1 = f3;
    }
    return localObject;
  }
  
  public static double score(List<Mean> paramList)
  {
    int i = paramList.size();
    double d = 0.0D;
    for (int j = 0; j < i; j++)
    {
      Mean localMean1 = (Mean)paramList.get(j);
      for (int k = 0; k < i; k++)
      {
        Mean localMean2 = (Mean)paramList.get(k);
        if (localMean1 != localMean2) {
          d += Math.sqrt(sqDistance(mCentroid, mCentroid));
        }
      }
    }
    return d;
  }
  
  @VisibleForTesting
  public static float sqDistance(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f = 0.0F;
    int i = paramArrayOfFloat1.length;
    for (int j = 0; j < i; j++) {
      f += (paramArrayOfFloat1[j] - paramArrayOfFloat2[j]) * (paramArrayOfFloat1[j] - paramArrayOfFloat2[j]);
    }
    return f;
  }
  
  private boolean step(ArrayList<Mean> paramArrayList, float[][] paramArrayOfFloat)
  {
    for (int i = paramArrayList.size() - 1; i >= 0; i--) {
      getmClosestItems.clear();
    }
    Object localObject;
    for (i = paramArrayOfFloat.length - 1; i >= 0; i--)
    {
      localObject = paramArrayOfFloat[i];
      nearestMeanmClosestItems.add(localObject);
    }
    boolean bool = true;
    for (i = paramArrayList.size() - 1; i >= 0; i--)
    {
      localObject = (Mean)paramArrayList.get(i);
      if (mClosestItems.size() != 0)
      {
        paramArrayOfFloat = mCentroid;
        mCentroid = new float[paramArrayOfFloat.length];
        int j = 0;
        float[] arrayOfFloat;
        for (int k = 0; k < mClosestItems.size(); k++) {
          for (int m = 0; m < mCentroid.length; m++)
          {
            arrayOfFloat = mCentroid;
            arrayOfFloat[m] += ((float[])mClosestItems.get(k))[m];
          }
        }
        for (k = j; k < mCentroid.length; k++)
        {
          arrayOfFloat = mCentroid;
          arrayOfFloat[k] /= mClosestItems.size();
        }
        if (sqDistance(paramArrayOfFloat, mCentroid) > mSqConvergenceEpsilon) {
          bool = false;
        }
      }
    }
    return bool;
  }
  
  @VisibleForTesting
  public void checkDataSetSanity(float[][] paramArrayOfFloat)
  {
    if (paramArrayOfFloat != null)
    {
      if (paramArrayOfFloat.length != 0)
      {
        if (paramArrayOfFloat[0] != null)
        {
          int i = paramArrayOfFloat[0].length;
          int j = paramArrayOfFloat.length;
          int k = 1;
          while (k < j) {
            if ((paramArrayOfFloat[k] != null) && (paramArrayOfFloat[k].length == i)) {
              k++;
            } else {
              throw new IllegalArgumentException("Bad data set format.");
            }
          }
          return;
        }
        throw new IllegalArgumentException("Bad data set format.");
      }
      throw new IllegalArgumentException("Data set is empty.");
    }
    throw new IllegalArgumentException("Data set is null.");
  }
  
  public List<Mean> predict(int paramInt, float[][] paramArrayOfFloat)
  {
    checkDataSetSanity(paramArrayOfFloat);
    int i = 0;
    int j = paramArrayOfFloat[0].length;
    ArrayList localArrayList = new ArrayList();
    for (int k = 0; k < paramInt; k++)
    {
      Mean localMean = new Mean(j);
      for (int m = 0; m < j; m++) {
        mCentroid[m] = mRandomState.nextFloat();
      }
      localArrayList.add(localMean);
    }
    for (paramInt = i; (paramInt < mMaxIterations) && (!step(localArrayList, paramArrayOfFloat)); paramInt++) {}
    return localArrayList;
  }
  
  public static class Mean
  {
    float[] mCentroid;
    final ArrayList<float[]> mClosestItems = new ArrayList();
    
    public Mean(int paramInt)
    {
      mCentroid = new float[paramInt];
    }
    
    public Mean(float... paramVarArgs)
    {
      mCentroid = paramVarArgs;
    }
    
    public float[] getCentroid()
    {
      return mCentroid;
    }
    
    public List<float[]> getItems()
    {
      return mClosestItems;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Mean(centroid: ");
      localStringBuilder.append(Arrays.toString(mCentroid));
      localStringBuilder.append(", size: ");
      localStringBuilder.append(mClosestItems.size());
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    }
  }
}
