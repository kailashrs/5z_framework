package android.hardware;

import [F;
import java.util.GregorianCalendar;

public class GeomagneticField
{
  private static final long BASE_TIME = new GregorianCalendar(2015, 1, 1).getTimeInMillis();
  private static final float[][] DELTA_G;
  private static final float[][] DELTA_H;
  private static final float EARTH_REFERENCE_RADIUS_KM = 6371.2F;
  private static final float EARTH_SEMI_MAJOR_AXIS_KM = 6378.137F;
  private static final float EARTH_SEMI_MINOR_AXIS_KM = 6356.7524F;
  private static final float[][] G_COEFF;
  private static final float[][] H_COEFF;
  private static final float[][] SCHMIDT_QUASI_NORM_FACTORS = computeSchmidtQuasiNormFactors(G_COEFF.length);
  private float mGcLatitudeRad;
  private float mGcLongitudeRad;
  private float mGcRadiusKm;
  private float mX;
  private float mY;
  private float mZ;
  
  static
  {
    float[] arrayOfFloat1 = { -2445.3F, 3012.5F, 1676.6F };
    float[] arrayOfFloat2 = { 69.5F, 67.4F, 72.8F, -129.8F, -29.0F, 13.2F, -70.9F };
    float[] arrayOfFloat3 = { 81.6F, -76.1F, -6.8F, 51.9F, 15.0F, 9.3F, -2.8F, 6.7F };
    float[] arrayOfFloat4 = { 24.0F, 8.6F, -16.9F, -3.2F, -20.6F, 13.3F, 11.7F, -16.0F, -2.0F };
    float[] arrayOfFloat5 = { 5.4F, 8.8F, 3.1F, -3.1F, 0.6F, -13.3F, -0.1F, 8.7F, -9.1F, -10.5F };
    float[] arrayOfFloat6 = { -2.0F, -0.3F, 0.4F, 1.3F, -0.9F, 0.9F, 0.1F, 0.5F, -0.4F, -0.4F, 0.2F, -0.9F, 0.0F };
    G_COEFF = new float[][] { { 0.0F }, { -29438.5F, -1501.1F }, arrayOfFloat1, { 1351.1F, -2352.3F, 1225.6F, 581.9F }, { 907.2F, 813.7F, 120.3F, -335.0F, 70.3F }, { -232.6F, 360.1F, 192.4F, -141.0F, -157.4F, 4.3F }, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, arrayOfFloat5, { -1.9F, -6.5F, 0.2F, 0.6F, -0.6F, 1.7F, -0.7F, 2.1F, 2.3F, -1.8F, -3.6F }, { 3.1F, -1.5F, -2.3F, 2.1F, -0.9F, 0.6F, -0.7F, 0.2F, 1.7F, -0.2F, 0.4F, 3.5F }, arrayOfFloat6 };
    arrayOfFloat1 = new float[] { 0.0F };
    arrayOfFloat2 = new float[] { 0.0F, 4796.2F };
    arrayOfFloat3 = new float[] { 0.0F, -115.3F, 245.0F, -538.3F };
    arrayOfFloat4 = new float[] { 0.0F, 283.4F, -188.6F, 180.9F, -329.5F };
    arrayOfFloat5 = new float[] { 0.0F, 47.4F, 196.9F, -119.4F, 16.1F, 100.1F };
    arrayOfFloat6 = new float[] { 0.0F, -20.7F, 33.2F, 58.8F, -66.5F, 7.3F, 62.5F };
    float[] arrayOfFloat7 = { 0.0F, 10.2F, -18.1F, 13.2F, -14.6F, 16.2F, 5.7F, -9.1F, 2.2F };
    float[] arrayOfFloat8 = { 0.0F, -0.1F, 2.1F, -0.7F, -1.1F, 0.7F, -0.2F, -2.1F, -1.5F, -2.5F, -2.0F, -2.3F };
    float[] arrayOfFloat9 = { 0.0F, -1.0F, 0.5F, 1.8F, -2.2F, 0.3F, 0.7F, -0.1F, 0.3F, 0.2F, -0.9F, -0.2F, 0.7F };
    H_COEFF = new float[][] { arrayOfFloat1, arrayOfFloat2, { 0.0F, -2845.6F, -642.0F }, arrayOfFloat3, arrayOfFloat4, arrayOfFloat5, arrayOfFloat6, { 0.0F, -54.1F, -19.4F, 5.6F, 24.4F, 3.3F, -27.5F, -2.3F }, arrayOfFloat7, { 0.0F, -21.6F, 10.8F, 11.7F, -6.8F, -6.9F, 7.8F, 1.0F, -3.9F, 8.5F }, { 0.0F, 3.3F, -0.3F, 4.6F, 4.4F, -7.9F, -0.6F, -4.1F, -2.8F, -1.1F, -8.7F }, arrayOfFloat8, arrayOfFloat9 };
    arrayOfFloat1 = new float[] { -8.6F, -3.3F, 2.4F };
    arrayOfFloat2 = new float[] { -0.2F, 0.1F, -1.4F, 0.0F, 1.3F, 3.8F };
    arrayOfFloat3 = new float[] { -0.5F, -0.2F, -0.6F, 2.4F, -1.1F, 0.3F, 1.5F };
    arrayOfFloat4 = new float[] { 0.2F, -0.2F, -0.4F, 1.3F, 0.2F, -0.4F, -0.9F, 0.3F };
    arrayOfFloat5 = new float[] { 0.0F, -0.1F, -0.1F, 0.4F, -0.5F, -0.2F, 0.1F, 0.0F, -0.2F, -0.1F };
    arrayOfFloat6 = new float[] { 0.0F, 0.0F, -0.1F, 0.3F, -0.1F, -0.1F, -0.1F, 0.0F, -0.2F, -0.1F, -0.2F };
    arrayOfFloat7 = new float[] { 0.0F, 0.0F, -0.1F, 0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1F, -0.1F };
    arrayOfFloat8 = new float[] { 0.1F, 0.0F, 0.0F, 0.1F, -0.1F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    DELTA_G = new float[][] { { 0.0F }, { 10.7F, 17.9F }, arrayOfFloat1, { 3.1F, -6.2F, -0.4F, -10.4F }, { -0.4F, 0.8F, -9.2F, 4.0F, -4.2F }, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, { 0.0F, 0.1F, -0.5F, 0.5F, -0.2F, 0.4F, 0.2F, -0.4F, 0.3F }, arrayOfFloat5, arrayOfFloat6, arrayOfFloat7, arrayOfFloat8 };
    arrayOfFloat1 = new float[] { 0.0F, -26.8F };
    arrayOfFloat2 = new float[] { 0.0F, 8.4F, -0.4F, 2.3F };
    arrayOfFloat3 = new float[] { 0.0F, 0.0F, -2.2F, -0.7F, 0.1F, 1.0F, 1.3F };
    arrayOfFloat4 = new float[] { 0.0F, -0.3F, 0.3F, 0.3F, 0.6F, -0.1F, -0.2F, 0.3F, 0.0F };
    arrayOfFloat5 = new float[] { 0.0F, 0.0F, 0.1F, 0.0F, 0.1F, 0.0F, 0.0F, 0.1F, 0.0F, -0.1F, 0.0F, -0.1F };
    DELTA_H = new float[][] { { 0.0F }, arrayOfFloat1, { 0.0F, -27.1F, -13.3F }, arrayOfFloat2, { 0.0F, -0.6F, 5.3F, 3.0F, -5.3F }, { 0.0F, 0.4F, 1.6F, -1.1F, 3.3F, 0.1F }, arrayOfFloat3, { 0.0F, 0.7F, 0.5F, -0.2F, -0.1F, -0.7F, 0.1F, 0.1F }, arrayOfFloat4, { 0.0F, -0.2F, -0.1F, -0.2F, 0.1F, 0.1F, 0.0F, -0.2F, 0.4F, 0.3F }, { 0.0F, 0.1F, -0.1F, 0.0F, 0.0F, -0.2F, 0.1F, -0.1F, -0.2F, 0.1F, -0.1F }, arrayOfFloat5, { 0.0F, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F } };
  }
  
  public GeomagneticField(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong)
  {
    int i = G_COEFF.length;
    float f1 = Math.min(89.99999F, Math.max(-89.99999F, paramFloat1));
    computeGeocentricCoordinates(f1, paramFloat2, paramFloat3);
    LegendreTable localLegendreTable = new LegendreTable(i - 1, (float)(1.5707963267948966D - mGcLatitudeRad));
    float[] arrayOfFloat1 = new float[i + 2];
    arrayOfFloat1[0] = 1.0F;
    paramFloat1 = 6371.2F / mGcRadiusKm;
    int j = 1;
    arrayOfFloat1[1] = paramFloat1;
    int k = 2;
    for (int m = 2; m < arrayOfFloat1.length; m++) {
      arrayOfFloat1[m] = (arrayOfFloat1[(m - 1)] * arrayOfFloat1[1]);
    }
    float[] arrayOfFloat2 = new float[i];
    float[] arrayOfFloat3 = new float[i];
    arrayOfFloat2[0] = 0.0F;
    arrayOfFloat3[0] = 1.0F;
    arrayOfFloat2[1] = ((float)Math.sin(mGcLongitudeRad));
    arrayOfFloat3[1] = ((float)Math.cos(mGcLongitudeRad));
    for (m = k; m < i; m++)
    {
      k = m >> 1;
      arrayOfFloat2[m] = (arrayOfFloat2[(m - k)] * arrayOfFloat3[k] + arrayOfFloat3[(m - k)] * arrayOfFloat2[k]);
      arrayOfFloat3[m] = (arrayOfFloat3[(m - k)] * arrayOfFloat3[k] - arrayOfFloat2[(m - k)] * arrayOfFloat2[k]);
    }
    float f2 = 1.0F / (float)Math.cos(mGcLatitudeRad);
    float f3 = (float)(paramLong - BASE_TIME) / 3.1536001E10F;
    paramFloat3 = 0.0F;
    paramFloat1 = 0.0F;
    paramFloat2 = 0.0F;
    m = j;
    while (m < i)
    {
      float f4 = paramFloat2;
      paramFloat2 = paramFloat1;
      j = 0;
      paramFloat1 = f4;
      while (j <= m)
      {
        f4 = G_COEFF[m][j] + DELTA_G[m][j] * f3;
        float f5 = H_COEFF[m][j] + DELTA_H[m][j] * f3;
        paramFloat3 += arrayOfFloat1[(m + 2)] * (arrayOfFloat3[j] * f4 + arrayOfFloat2[j] * f5) * mPDeriv[m][j] * SCHMIDT_QUASI_NORM_FACTORS[m][j];
        paramFloat2 += arrayOfFloat1[(m + 2)] * j * (arrayOfFloat2[j] * f4 - arrayOfFloat3[j] * f5) * mP[m][j] * SCHMIDT_QUASI_NORM_FACTORS[m][j] * f2;
        paramFloat1 -= (m + 1) * arrayOfFloat1[(m + 2)] * (arrayOfFloat3[j] * f4 + arrayOfFloat2[j] * f5) * mP[m][j] * SCHMIDT_QUASI_NORM_FACTORS[m][j];
        j++;
      }
      m++;
      f4 = paramFloat2;
      paramFloat2 = paramFloat1;
      paramFloat1 = f4;
    }
    double d = Math.toRadians(f1) - mGcLatitudeRad;
    mX = ((float)(paramFloat3 * Math.cos(d) + paramFloat2 * Math.sin(d)));
    mY = paramFloat1;
    mZ = ((float)(-paramFloat3 * Math.sin(d) + paramFloat2 * Math.cos(d)));
  }
  
  private void computeGeocentricCoordinates(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    paramFloat3 /= 1000.0F;
    double d = Math.toRadians(paramFloat1);
    paramFloat1 = (float)Math.cos(d);
    float f1 = (float)Math.sin(d);
    float f2 = f1 / paramFloat1;
    float f3 = (float)Math.sqrt(4.0680636E7F * paramFloat1 * paramFloat1 + 4.04083E7F * f1 * f1);
    mGcLatitudeRad = ((float)Math.atan((f3 * paramFloat3 + 4.04083E7F) * f2 / (f3 * paramFloat3 + 4.0680636E7F)));
    mGcLongitudeRad = ((float)Math.toRadians(paramFloat2));
    mGcRadiusKm = ((float)Math.sqrt(paramFloat3 * paramFloat3 + 2.0F * paramFloat3 * (float)Math.sqrt(4.0680636E7F * paramFloat1 * paramFloat1 + 4.04083E7F * f1 * f1) + (4.0680636E7F * 4.0680636E7F * paramFloat1 * paramFloat1 + 4.04083E7F * 4.04083E7F * f1 * f1) / (4.0680636E7F * paramFloat1 * paramFloat1 + 4.04083E7F * f1 * f1)));
  }
  
  private static float[][] computeSchmidtQuasiNormFactors(int paramInt)
  {
    float[][] arrayOfFloat = new float[paramInt + 1][];
    arrayOfFloat[0] = { 1.0F };
    for (int i = 1; i <= paramInt; i++)
    {
      arrayOfFloat[i] = new float[i + 1];
      arrayOfFloat[i][0] = (arrayOfFloat[(i - 1)][0] * (2 * i - 1) / i);
      for (int j = 1; j <= i; j++)
      {
        [F local[F = arrayOfFloat[i];
        float f = arrayOfFloat[i][(j - 1)];
        int k;
        if (j == 1) {
          k = 2;
        } else {
          k = 1;
        }
        local[F[j] = (f * (float)Math.sqrt((i - j + 1) * k / (i + j)));
      }
    }
    return arrayOfFloat;
  }
  
  public float getDeclination()
  {
    return (float)Math.toDegrees(Math.atan2(mY, mX));
  }
  
  public float getFieldStrength()
  {
    return (float)Math.sqrt(mX * mX + mY * mY + mZ * mZ);
  }
  
  public float getHorizontalStrength()
  {
    return (float)Math.hypot(mX, mY);
  }
  
  public float getInclination()
  {
    return (float)Math.toDegrees(Math.atan2(mZ, getHorizontalStrength()));
  }
  
  public float getX()
  {
    return mX;
  }
  
  public float getY()
  {
    return mY;
  }
  
  public float getZ()
  {
    return mZ;
  }
  
  private static class LegendreTable
  {
    public final float[][] mP;
    public final float[][] mPDeriv;
    
    public LegendreTable(int paramInt, float paramFloat)
    {
      float f1 = (float)Math.cos(paramFloat);
      paramFloat = (float)Math.sin(paramFloat);
      mP = new float[paramInt + 1][];
      mPDeriv = new float[paramInt + 1][];
      mP[0] = { 1.0F };
      mPDeriv[0] = { 0.0F };
      for (int i = 1; i <= paramInt; i++)
      {
        mP[i] = new float[i + 1];
        mPDeriv[i] = new float[i + 1];
        for (int j = 0; j <= i; j++) {
          if (i == j)
          {
            mP[i][j] = (mP[(i - 1)][(j - 1)] * paramFloat);
            mPDeriv[i][j] = (mP[(i - 1)][(j - 1)] * f1 + mPDeriv[(i - 1)][(j - 1)] * paramFloat);
          }
          else if ((i != 1) && (j != i - 1))
          {
            float f2 = ((i - 1) * (i - 1) - j * j) / ((2 * i - 1) * (2 * i - 3));
            mP[i][j] = (mP[(i - 1)][j] * f1 - mP[(i - 2)][j] * f2);
            mPDeriv[i][j] = (-paramFloat * mP[(i - 1)][j] + mPDeriv[(i - 1)][j] * f1 - mPDeriv[(i - 2)][j] * f2);
          }
          else
          {
            mP[i][j] = (mP[(i - 1)][j] * f1);
            mPDeriv[i][j] = (-paramFloat * mP[(i - 1)][j] + mPDeriv[(i - 1)][j] * f1);
          }
        }
      }
    }
  }
}
