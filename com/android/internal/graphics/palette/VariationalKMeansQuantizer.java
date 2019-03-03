package com.android.internal.graphics.palette;

import com.android.internal.graphics.ColorUtils;
import com.android.internal.ml.clustering.KMeans;
import com.android.internal.ml.clustering.KMeans.Mean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class VariationalKMeansQuantizer
  implements Quantizer
{
  private static final boolean DEBUG = false;
  private static final String TAG = "KMeansQuantizer";
  private final int mInitializations;
  private final KMeans mKMeans = new KMeans(new Random(0L), 30, 0.0F);
  private final float mMinClusterSqDistance;
  private List<Palette.Swatch> mQuantizedColors;
  
  public VariationalKMeansQuantizer()
  {
    this(0.25F);
  }
  
  public VariationalKMeansQuantizer(float paramFloat)
  {
    this(paramFloat, 1);
  }
  
  public VariationalKMeansQuantizer(float paramFloat, int paramInt)
  {
    mMinClusterSqDistance = (paramFloat * paramFloat);
    mInitializations = paramInt;
  }
  
  private List<KMeans.Mean> getOptimalKMeans(int paramInt, float[][] paramArrayOfFloat)
  {
    Object localObject = null;
    double d1 = -1.7976931348623157E308D;
    int i = mInitializations;
    while (i > 0)
    {
      List localList = mKMeans.predict(paramInt, paramArrayOfFloat);
      double d2 = KMeans.score(localList);
      double d3;
      if (localObject != null)
      {
        d3 = d1;
        if (d2 <= d1) {}
      }
      else
      {
        d3 = d2;
        localObject = localList;
      }
      i--;
      d1 = d3;
    }
    return localObject;
  }
  
  public List<Palette.Swatch> getQuantizedColors()
  {
    return mQuantizedColors;
  }
  
  public void quantize(int[] paramArrayOfInt, int paramInt, Palette.Filter[] paramArrayOfFilter)
  {
    paramArrayOfFilter = new float[3];
    Palette.Filter[] tmp5_4 = paramArrayOfFilter;
    tmp5_4[0] = 0.0F;
    Palette.Filter[] tmp9_5 = tmp5_4;
    tmp9_5[1] = 0.0F;
    Palette.Filter[] tmp13_9 = tmp9_5;
    tmp13_9[2] = 0.0F;
    tmp13_9;
    Object localObject1 = new float[paramArrayOfInt.length][3];
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      ColorUtils.colorToHSL(paramArrayOfInt[i], paramArrayOfFilter);
      localObject1[i][0] = (paramArrayOfFilter[0] / 360.0F);
      localObject1[i][1] = paramArrayOfFilter[1];
      localObject1[i][2] = paramArrayOfFilter[2];
    }
    localObject1 = getOptimalKMeans(paramInt, (float[][])localObject1);
    paramInt = 0;
    paramArrayOfInt = paramArrayOfFilter;
    while (paramInt < ((List)localObject1).size())
    {
      paramArrayOfFilter = (KMeans.Mean)((List)localObject1).get(paramInt);
      localObject2 = paramArrayOfFilter.getCentroid();
      for (i = paramInt + 1; i < ((List)localObject1).size(); i++)
      {
        KMeans.Mean localMean = (KMeans.Mean)((List)localObject1).get(i);
        float[] arrayOfFloat = localMean.getCentroid();
        if (KMeans.sqDistance((float[])localObject2, arrayOfFloat) < mMinClusterSqDistance)
        {
          ((List)localObject1).remove(localMean);
          paramArrayOfFilter.getItems().addAll(localMean.getItems());
          for (int j = 0; j < localObject2.length; j++) {
            localObject2[j] = ((float)(localObject2[j] + (arrayOfFloat[j] - localObject2[j]) / 2.0D));
          }
          i--;
        }
      }
      paramInt++;
    }
    mQuantizedColors = new ArrayList();
    Object localObject2 = ((List)localObject1).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (KMeans.Mean)((Iterator)localObject2).next();
      if (((KMeans.Mean)localObject1).getItems().size() != 0)
      {
        paramArrayOfInt = ((KMeans.Mean)localObject1).getCentroid();
        paramArrayOfFilter = mQuantizedColors;
        float f1 = paramArrayOfInt[0];
        float f2 = paramArrayOfInt[1];
        float f3 = paramArrayOfInt[2];
        paramInt = ((KMeans.Mean)localObject1).getItems().size();
        paramArrayOfFilter.add(new Palette.Swatch(new float[] { f1 * 360.0F, f2, f3 }, paramInt));
      }
    }
  }
}
