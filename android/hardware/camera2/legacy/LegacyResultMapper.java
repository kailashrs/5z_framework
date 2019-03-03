package android.hardware.camera2.legacy;

import android.graphics.Rect;
import android.hardware.Camera.Area;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ParamsUtils;
import android.location.Location;
import android.util.Log;
import android.util.Size;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LegacyResultMapper
{
  private static final boolean DEBUG = false;
  private static final String TAG = "LegacyResultMapper";
  private LegacyRequest mCachedRequest = null;
  private CameraMetadataNative mCachedResult = null;
  
  public LegacyResultMapper() {}
  
  private static int convertLegacyAfMode(String paramString)
  {
    if (paramString == null)
    {
      Log.w("LegacyResultMapper", "convertLegacyAfMode - no AF mode, default to OFF");
      return 0;
    }
    int i = -1;
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 910005312: 
      if (paramString.equals("continuous-picture")) {
        i = 1;
      }
      break;
    case 173173288: 
      if (paramString.equals("infinity")) {
        i = 6;
      }
      break;
    case 103652300: 
      if (paramString.equals("macro")) {
        i = 4;
      }
      break;
    case 97445748: 
      if (paramString.equals("fixed")) {
        i = 5;
      }
      break;
    case 3108534: 
      if (paramString.equals("edof")) {
        i = 3;
      }
      break;
    case 3005871: 
      if (paramString.equals("auto")) {
        i = 0;
      }
      break;
    case -194628547: 
      if (paramString.equals("continuous-video")) {
        i = 2;
      }
      break;
    }
    switch (i)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("convertLegacyAfMode - unknown mode ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" , ignoring");
      Log.w("LegacyResultMapper", localStringBuilder.toString());
      return 0;
    case 6: 
      return 0;
    case 5: 
      return 0;
    case 4: 
      return 2;
    case 3: 
      return 5;
    case 2: 
      return 3;
    case 1: 
      return 4;
    }
    return 1;
  }
  
  private static int convertLegacyAwbMode(String paramString)
  {
    if (paramString == null) {
      return 1;
    }
    int i = -1;
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 1942983418: 
      if (paramString.equals("daylight")) {
        i = 4;
      }
      break;
    case 1902580840: 
      if (paramString.equals("fluorescent")) {
        i = 2;
      }
      break;
    case 1650323088: 
      if (paramString.equals("twilight")) {
        i = 6;
      }
      break;
    case 474934723: 
      if (paramString.equals("cloudy-daylight")) {
        i = 5;
      }
      break;
    case 109399597: 
      if (paramString.equals("shade")) {
        i = 7;
      }
      break;
    case 3005871: 
      if (paramString.equals("auto")) {
        i = 0;
      }
      break;
    case -719316704: 
      if (paramString.equals("warm-fluorescent")) {
        i = 3;
      }
      break;
    case -939299377: 
      if (paramString.equals("incandescent")) {
        i = 1;
      }
      break;
    }
    switch (i)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("convertAwbMode - unrecognized WB mode ");
      localStringBuilder.append(paramString);
      Log.w("LegacyResultMapper", localStringBuilder.toString());
      return 1;
    case 7: 
      return 8;
    case 6: 
      return 7;
    case 5: 
      return 6;
    case 4: 
      return 5;
    case 3: 
      return 4;
    case 2: 
      return 3;
    case 1: 
      return 2;
    }
    return 1;
  }
  
  private static CameraMetadataNative convertResultMetadata(LegacyRequest paramLegacyRequest)
  {
    Object localObject1 = characteristics;
    CaptureRequest localCaptureRequest = captureRequest;
    Object localObject2 = previewSize;
    Camera.Parameters localParameters = parameters;
    paramLegacyRequest = new CameraMetadataNative();
    Object localObject3 = (Rect)((CameraCharacteristics)localObject1).get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
    localObject2 = ParameterUtils.convertScalerCropRegion((Rect)localObject3, (Rect)localCaptureRequest.get(CaptureRequest.SCALER_CROP_REGION), (Size)localObject2, localParameters);
    paramLegacyRequest.set(CaptureResult.COLOR_CORRECTION_ABERRATION_MODE, (Integer)localCaptureRequest.get(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE));
    mapAe(paramLegacyRequest, (CameraCharacteristics)localObject1, localCaptureRequest, (Rect)localObject3, (ParameterUtils.ZoomData)localObject2, localParameters);
    mapAf(paramLegacyRequest, (Rect)localObject3, (ParameterUtils.ZoomData)localObject2, localParameters);
    mapAwb(paramLegacyRequest, localParameters);
    localObject3 = CaptureRequest.CONTROL_CAPTURE_INTENT;
    int i = 1;
    int j = LegacyRequestMapper.filterSupportedCaptureIntent(((Integer)ParamsUtils.getOrDefault(localCaptureRequest, (CaptureRequest.Key)localObject3, Integer.valueOf(1))).intValue());
    paramLegacyRequest.set(CaptureResult.CONTROL_CAPTURE_INTENT, Integer.valueOf(j));
    if (((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_MODE, Integer.valueOf(1))).intValue() == 2) {
      paramLegacyRequest.set(CaptureResult.CONTROL_MODE, Integer.valueOf(2));
    } else {
      paramLegacyRequest.set(CaptureResult.CONTROL_MODE, Integer.valueOf(1));
    }
    Object localObject4 = localParameters.getSceneMode();
    j = LegacyMetadataMapper.convertSceneModeFromLegacy((String)localObject4);
    if (j != -1)
    {
      paramLegacyRequest.set(CaptureResult.CONTROL_SCENE_MODE, Integer.valueOf(j));
    }
    else
    {
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("Unknown scene mode ");
      ((StringBuilder)localObject3).append((String)localObject4);
      ((StringBuilder)localObject3).append(" returned by camera HAL, setting to disabled.");
      Log.w("LegacyResultMapper", ((StringBuilder)localObject3).toString());
      paramLegacyRequest.set(CaptureResult.CONTROL_SCENE_MODE, Integer.valueOf(0));
    }
    localObject3 = localParameters.getColorEffect();
    j = LegacyMetadataMapper.convertEffectModeFromLegacy((String)localObject3);
    if (j != -1)
    {
      paramLegacyRequest.set(CaptureResult.CONTROL_EFFECT_MODE, Integer.valueOf(j));
    }
    else
    {
      localObject4 = new StringBuilder();
      ((StringBuilder)localObject4).append("Unknown effect mode ");
      ((StringBuilder)localObject4).append((String)localObject3);
      ((StringBuilder)localObject4).append(" returned by camera HAL, setting to off.");
      Log.w("LegacyResultMapper", ((StringBuilder)localObject4).toString());
      paramLegacyRequest.set(CaptureResult.CONTROL_EFFECT_MODE, Integer.valueOf(0));
    }
    if ((!localParameters.isVideoStabilizationSupported()) || (!localParameters.getVideoStabilization())) {
      i = 0;
    }
    paramLegacyRequest.set(CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE, Integer.valueOf(i));
    if ("infinity".equals(localParameters.getFocusMode())) {
      paramLegacyRequest.set(CaptureResult.LENS_FOCUS_DISTANCE, Float.valueOf(0.0F));
    }
    paramLegacyRequest.set(CaptureResult.LENS_FOCAL_LENGTH, Float.valueOf(localParameters.getFocalLength()));
    paramLegacyRequest.set(CaptureResult.REQUEST_PIPELINE_DEPTH, (Byte)((CameraCharacteristics)localObject1).get(CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH));
    mapScaler(paramLegacyRequest, (ParameterUtils.ZoomData)localObject2, localParameters);
    paramLegacyRequest.set(CaptureResult.SENSOR_TEST_PATTERN_MODE, Integer.valueOf(0));
    paramLegacyRequest.set(CaptureResult.JPEG_GPS_LOCATION, (Location)localCaptureRequest.get(CaptureRequest.JPEG_GPS_LOCATION));
    paramLegacyRequest.set(CaptureResult.JPEG_ORIENTATION, (Integer)localCaptureRequest.get(CaptureRequest.JPEG_ORIENTATION));
    paramLegacyRequest.set(CaptureResult.JPEG_QUALITY, Byte.valueOf((byte)localParameters.getJpegQuality()));
    paramLegacyRequest.set(CaptureResult.JPEG_THUMBNAIL_QUALITY, Byte.valueOf((byte)localParameters.getJpegThumbnailQuality()));
    localObject1 = localParameters.getJpegThumbnailSize();
    if (localObject1 != null) {
      paramLegacyRequest.set(CaptureResult.JPEG_THUMBNAIL_SIZE, ParameterUtils.convertSize((Camera.Size)localObject1));
    } else {
      Log.w("LegacyResultMapper", "Null thumbnail size received from parameters.");
    }
    paramLegacyRequest.set(CaptureResult.NOISE_REDUCTION_MODE, (Integer)localCaptureRequest.get(CaptureRequest.NOISE_REDUCTION_MODE));
    return paramLegacyRequest;
  }
  
  private static MeteringRectangle[] getMeteringRectangles(Rect paramRect, ParameterUtils.ZoomData paramZoomData, List<Camera.Area> paramList, String paramString)
  {
    paramString = new ArrayList();
    if (paramList != null)
    {
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        Camera.Area localArea = (Camera.Area)paramList.next();
        paramString.add(ParameterUtils.convertCameraAreaToActiveArrayRectangle(paramRect, paramZoomData, localArea).toMetering());
      }
    }
    return (MeteringRectangle[])paramString.toArray(new MeteringRectangle[0]);
  }
  
  private static void mapAe(CameraMetadataNative paramCameraMetadataNative, CameraCharacteristics paramCameraCharacteristics, CaptureRequest paramCaptureRequest, Rect paramRect, ParameterUtils.ZoomData paramZoomData, Camera.Parameters paramParameters)
  {
    int i = LegacyMetadataMapper.convertAntiBandingModeOrDefault(paramParameters.getAntibanding());
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AE_ANTIBANDING_MODE, Integer.valueOf(i));
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(paramParameters.getExposureCompensation()));
    boolean bool;
    if (paramParameters.isAutoExposureLockSupported()) {
      bool = paramParameters.getAutoExposureLock();
    } else {
      bool = false;
    }
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AE_LOCK, Boolean.valueOf(bool));
    Boolean localBoolean = (Boolean)paramCaptureRequest.get(CaptureRequest.CONTROL_AE_LOCK);
    if ((localBoolean != null) && (localBoolean.booleanValue() != bool))
    {
      paramCaptureRequest = new StringBuilder();
      paramCaptureRequest.append("mapAe - android.control.aeLock was requested to ");
      paramCaptureRequest.append(localBoolean);
      paramCaptureRequest.append(" but resulted in ");
      paramCaptureRequest.append(bool);
      Log.w("LegacyResultMapper", paramCaptureRequest.toString());
    }
    mapAeAndFlashMode(paramCameraMetadataNative, paramCameraCharacteristics, paramParameters);
    if (paramParameters.getMaxNumMeteringAreas() > 0)
    {
      paramCameraCharacteristics = getMeteringRectangles(paramRect, paramZoomData, paramParameters.getMeteringAreas(), "AE");
      paramCameraMetadataNative.set(CaptureResult.CONTROL_AE_REGIONS, paramCameraCharacteristics);
    }
  }
  
  private static void mapAeAndFlashMode(CameraMetadataNative paramCameraMetadataNative, CameraCharacteristics paramCameraCharacteristics, Camera.Parameters paramParameters)
  {
    int i = 0;
    boolean bool = ((Boolean)paramCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)).booleanValue();
    int j = 0;
    if (bool) {
      paramCameraCharacteristics = null;
    } else {
      paramCameraCharacteristics = Integer.valueOf(0);
    }
    int k = 1;
    String str = paramParameters.getFlashMode();
    int m = i;
    Object localObject = paramCameraCharacteristics;
    int n = k;
    if (str != null)
    {
      n = str.hashCode();
      if (n != 3551)
      {
        if (n != 109935)
        {
          if (n != 3005871)
          {
            if (n != 110547964)
            {
              if ((n == 1081542389) && (str.equals("red-eye")))
              {
                n = 3;
                break label197;
              }
            }
            else if (str.equals("torch"))
            {
              n = 4;
              break label197;
            }
          }
          else if (str.equals("auto"))
          {
            n = 1;
            break label197;
          }
        }
        else if (str.equals("off"))
        {
          n = j;
          break label197;
        }
      }
      else if (str.equals("on"))
      {
        n = 2;
        break label197;
      }
      n = -1;
      switch (n)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("mapAeAndFlashMode - Ignoring unknown flash mode ");
        ((StringBuilder)localObject).append(paramParameters.getFlashMode());
        Log.w("LegacyResultMapper", ((StringBuilder)localObject).toString());
        m = i;
        localObject = paramCameraCharacteristics;
        n = k;
        break;
      case 4: 
        m = 2;
        localObject = Integer.valueOf(3);
        n = k;
        break;
      case 3: 
        n = 4;
        m = i;
        localObject = paramCameraCharacteristics;
        break;
      case 2: 
        m = 1;
        n = 3;
        localObject = Integer.valueOf(3);
        break;
      case 1: 
        n = 2;
        m = i;
        localObject = paramCameraCharacteristics;
        break;
      case 0: 
        label197:
        n = k;
        localObject = paramCameraCharacteristics;
        m = i;
      }
    }
    paramCameraMetadataNative.set(CaptureResult.FLASH_STATE, localObject);
    paramCameraMetadataNative.set(CaptureResult.FLASH_MODE, Integer.valueOf(m));
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AE_MODE, Integer.valueOf(n));
  }
  
  private static void mapAf(CameraMetadataNative paramCameraMetadataNative, Rect paramRect, ParameterUtils.ZoomData paramZoomData, Camera.Parameters paramParameters)
  {
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AF_MODE, Integer.valueOf(convertLegacyAfMode(paramParameters.getFocusMode())));
    if (paramParameters.getMaxNumFocusAreas() > 0)
    {
      paramRect = getMeteringRectangles(paramRect, paramZoomData, paramParameters.getFocusAreas(), "AF");
      paramCameraMetadataNative.set(CaptureResult.CONTROL_AF_REGIONS, paramRect);
    }
  }
  
  private static void mapAwb(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    boolean bool;
    if (paramParameters.isAutoWhiteBalanceLockSupported()) {
      bool = paramParameters.getAutoWhiteBalanceLock();
    } else {
      bool = false;
    }
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AWB_LOCK, Boolean.valueOf(bool));
    int i = convertLegacyAwbMode(paramParameters.getWhiteBalance());
    paramCameraMetadataNative.set(CaptureResult.CONTROL_AWB_MODE, Integer.valueOf(i));
  }
  
  private static void mapScaler(CameraMetadataNative paramCameraMetadataNative, ParameterUtils.ZoomData paramZoomData, Camera.Parameters paramParameters)
  {
    paramCameraMetadataNative.set(CaptureResult.SCALER_CROP_REGION, reportedCrop);
  }
  
  public CameraMetadataNative cachedConvertResultMetadata(LegacyRequest paramLegacyRequest, long paramLong)
  {
    if ((mCachedRequest != null) && (parameters.same(mCachedRequest.parameters)) && (captureRequest.equals(mCachedRequest.captureRequest)))
    {
      paramLegacyRequest = new CameraMetadataNative(mCachedResult);
    }
    else
    {
      CameraMetadataNative localCameraMetadataNative = convertResultMetadata(paramLegacyRequest);
      mCachedRequest = paramLegacyRequest;
      mCachedResult = new CameraMetadataNative(localCameraMetadataNative);
      paramLegacyRequest = localCameraMetadataNative;
    }
    paramLegacyRequest.set(CaptureResult.SENSOR_TIMESTAMP, Long.valueOf(paramLong));
    return paramLegacyRequest;
  }
}
