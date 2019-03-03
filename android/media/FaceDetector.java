package android.media;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

public class FaceDetector
{
  private static boolean sInitialized = false;
  private byte[] mBWBuffer;
  private long mDCR;
  private long mFD;
  private int mHeight;
  private int mMaxFaces;
  private long mSDK;
  private int mWidth;
  
  static
  {
    try
    {
      System.loadLibrary("FFTEm");
      nativeClassInit();
      sInitialized = true;
    }
    catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
    {
      Log.d("FFTEm", "face detection library not found!");
    }
  }
  
  public FaceDetector(int paramInt1, int paramInt2, int paramInt3)
  {
    if (!sInitialized) {
      return;
    }
    fft_initialize(paramInt1, paramInt2, paramInt3);
    mWidth = paramInt1;
    mHeight = paramInt2;
    mMaxFaces = paramInt3;
    mBWBuffer = new byte[paramInt1 * paramInt2];
  }
  
  private native void fft_destroy();
  
  private native int fft_detect(Bitmap paramBitmap);
  
  private native void fft_get_face(Face paramFace, int paramInt);
  
  private native int fft_initialize(int paramInt1, int paramInt2, int paramInt3);
  
  private static native void nativeClassInit();
  
  protected void finalize()
    throws Throwable
  {
    fft_destroy();
  }
  
  public int findFaces(Bitmap paramBitmap, Face[] paramArrayOfFace)
  {
    boolean bool = sInitialized;
    int i = 0;
    if (!bool) {
      return 0;
    }
    if ((paramBitmap.getWidth() == mWidth) && (paramBitmap.getHeight() == mHeight))
    {
      if (paramArrayOfFace.length >= mMaxFaces)
      {
        int j = fft_detect(paramBitmap);
        int k = j;
        if (j >= mMaxFaces) {
          k = mMaxFaces;
        }
        while (i < k)
        {
          if (paramArrayOfFace[i] == null) {
            paramArrayOfFace[i] = new Face(null);
          }
          fft_get_face(paramArrayOfFace[i], i);
          i++;
        }
        return k;
      }
      throw new IllegalArgumentException("faces[] smaller than maxFaces");
    }
    throw new IllegalArgumentException("bitmap size doesn't match initialization");
  }
  
  public class Face
  {
    public static final float CONFIDENCE_THRESHOLD = 0.4F;
    public static final int EULER_X = 0;
    public static final int EULER_Y = 1;
    public static final int EULER_Z = 2;
    private float mConfidence;
    private float mEyesDist;
    private float mMidPointX;
    private float mMidPointY;
    private float mPoseEulerX;
    private float mPoseEulerY;
    private float mPoseEulerZ;
    
    private Face() {}
    
    public float confidence()
    {
      return mConfidence;
    }
    
    public float eyesDistance()
    {
      return mEyesDist;
    }
    
    public void getMidPoint(PointF paramPointF)
    {
      paramPointF.set(mMidPointX, mMidPointY);
    }
    
    public float pose(int paramInt)
    {
      if (paramInt == 0) {
        return mPoseEulerX;
      }
      if (paramInt == 1) {
        return mPoseEulerY;
      }
      if (paramInt == 2) {
        return mPoseEulerZ;
      }
      throw new IllegalArgumentException();
    }
  }
}
