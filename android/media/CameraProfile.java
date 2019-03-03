package android.media;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import java.util.Arrays;
import java.util.HashMap;

public class CameraProfile
{
  public static final int QUALITY_HIGH = 2;
  public static final int QUALITY_LOW = 0;
  public static final int QUALITY_MEDIUM = 1;
  private static final HashMap<Integer, int[]> sCache = new HashMap();
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public CameraProfile() {}
  
  private static int[] getImageEncodingQualityLevels(int paramInt)
  {
    int i = native_get_num_image_encoding_quality_levels(paramInt);
    if (i == 3)
    {
      localObject = new int[i];
      for (int j = 0; j < i; j++) {
        localObject[j] = native_get_image_encoding_quality_level(paramInt, j);
      }
      Arrays.sort((int[])localObject);
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unexpected Jpeg encoding quality levels ");
    ((StringBuilder)localObject).append(i);
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  public static int getJpegEncodingQualityParameter(int paramInt)
  {
    int i = Camera.getNumberOfCameras();
    Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
    for (int j = 0; j < i; j++)
    {
      Camera.getCameraInfo(j, localCameraInfo);
      if (facing == 0) {
        return getJpegEncodingQualityParameter(j, paramInt);
      }
    }
    return 0;
  }
  
  public static int getJpegEncodingQualityParameter(int paramInt1, int paramInt2)
  {
    if ((paramInt2 >= 0) && (paramInt2 <= 2)) {
      synchronized (sCache)
      {
        int[] arrayOfInt1 = (int[])sCache.get(Integer.valueOf(paramInt1));
        int[] arrayOfInt2 = arrayOfInt1;
        if (arrayOfInt1 == null)
        {
          arrayOfInt2 = getImageEncodingQualityLevels(paramInt1);
          sCache.put(Integer.valueOf(paramInt1), arrayOfInt2);
        }
        paramInt1 = arrayOfInt2[paramInt2];
        return paramInt1;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported quality level: ");
    localStringBuilder.append(paramInt2);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static final native int native_get_image_encoding_quality_level(int paramInt1, int paramInt2);
  
  private static final native int native_get_num_image_encoding_quality_levels(int paramInt);
  
  private static final native void native_init();
}
