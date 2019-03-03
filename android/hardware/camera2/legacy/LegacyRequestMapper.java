package android.hardware.camera2.legacy;

import android.graphics.Rect;
import android.hardware.Camera.Area;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ListUtils;
import android.hardware.camera2.utils.ParamsUtils;
import android.location.Location;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class LegacyRequestMapper
{
  private static final boolean DEBUG = false;
  private static final byte DEFAULT_JPEG_QUALITY = 85;
  private static final String TAG = "LegacyRequestMapper";
  
  public LegacyRequestMapper() {}
  
  private static boolean checkForCompleteGpsData(Location paramLocation)
  {
    boolean bool;
    if ((paramLocation != null) && (paramLocation.getProvider() != null) && (paramLocation.getTime() != 0L)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static String convertAeAntiBandingModeToLegacy(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 3: 
      return "auto";
    case 2: 
      return "60hz";
    case 1: 
      return "50hz";
    }
    return "off";
  }
  
  private static int[] convertAeFpsRangeToLegacy(Range<Integer> paramRange)
  {
    return new int[] { ((Integer)paramRange.getLower()).intValue() * 1000, ((Integer)paramRange.getUpper()).intValue() * 1000 };
  }
  
  private static String convertAwbModeToLegacy(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("convertAwbModeToLegacy - unrecognized control.awbMode");
      localStringBuilder.append(paramInt);
      Log.w("LegacyRequestMapper", localStringBuilder.toString());
      return "auto";
    case 8: 
      return "shade";
    case 7: 
      return "twilight";
    case 6: 
      return "cloudy-daylight";
    case 5: 
      return "daylight";
    case 4: 
      return "warm-fluorescent";
    case 3: 
      return "fluorescent";
    case 2: 
      return "incandescent";
    }
    return "auto";
  }
  
  private static List<Camera.Area> convertMeteringRegionsToLegacy(Rect paramRect, ParameterUtils.ZoomData paramZoomData, MeteringRectangle[] paramArrayOfMeteringRectangle, int paramInt, String paramString)
  {
    int i = 0;
    if ((paramArrayOfMeteringRectangle != null) && (paramInt > 0))
    {
      ArrayList localArrayList = new ArrayList();
      int j = paramArrayOfMeteringRectangle.length;
      MeteringRectangle localMeteringRectangle;
      for (int k = 0; k < j; k++)
      {
        localMeteringRectangle = paramArrayOfMeteringRectangle[k];
        if (localMeteringRectangle.getMeteringWeight() != 0) {
          localArrayList.add(localMeteringRectangle);
        }
      }
      if (localArrayList.size() == 0)
      {
        Log.w("LegacyRequestMapper", "Only received metering rectangles with weight 0.");
        return Arrays.asList(new Camera.Area[] { ParameterUtils.CAMERA_AREA_DEFAULT });
      }
      j = Math.min(paramInt, localArrayList.size());
      paramArrayOfMeteringRectangle = new ArrayList(j);
      for (k = i; k < j; k++)
      {
        localMeteringRectangle = (MeteringRectangle)localArrayList.get(k);
        paramArrayOfMeteringRectangle.add(convertMeteringRectangleToLegacymeteringArea);
      }
      if (paramInt < localArrayList.size())
      {
        paramRect = new StringBuilder();
        paramRect.append("convertMeteringRegionsToLegacy - Too many requested ");
        paramRect.append(paramString);
        paramRect.append(" regions, ignoring all beyond the first ");
        paramRect.append(paramInt);
        Log.w("LegacyRequestMapper", paramRect.toString());
      }
      return paramArrayOfMeteringRectangle;
    }
    if (paramInt > 0) {
      return Arrays.asList(new Camera.Area[] { ParameterUtils.CAMERA_AREA_DEFAULT });
    }
    return null;
  }
  
  public static void convertRequestMetadata(LegacyRequest paramLegacyRequest)
  {
    CameraCharacteristics localCameraCharacteristics = characteristics;
    CaptureRequest localCaptureRequest = captureRequest;
    Object localObject1 = previewSize;
    Camera.Parameters localParameters = parameters;
    Object localObject2 = (Rect)localCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
    localObject1 = ParameterUtils.convertScalerCropRegion((Rect)localObject2, (Rect)localCaptureRequest.get(CaptureRequest.SCALER_CROP_REGION), (Size)localObject1, localParameters);
    if (localParameters.isZoomSupported()) {
      localParameters.setZoom(zoomIndex);
    }
    int i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, Integer.valueOf(1))).intValue();
    if ((i != 1) && (i != 2))
    {
      paramLegacyRequest = new StringBuilder();
      paramLegacyRequest.append("convertRequestToMetadata - Ignoring unsupported colorCorrection.aberrationMode = ");
      paramLegacyRequest.append(i);
      Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
    }
    paramLegacyRequest = (Integer)localCaptureRequest.get(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE);
    if (paramLegacyRequest != null) {
      paramLegacyRequest = convertAeAntiBandingModeToLegacy(paramLegacyRequest.intValue());
    } else {
      paramLegacyRequest = (String)ListUtils.listSelectFirstFrom(localParameters.getSupportedAntibanding(), new String[] { "auto", "off", "50hz", "60hz" });
    }
    if (paramLegacyRequest != null) {
      localParameters.setAntibanding(paramLegacyRequest);
    }
    paramLegacyRequest = (MeteringRectangle[])localCaptureRequest.get(CaptureRequest.CONTROL_AE_REGIONS);
    if (localCaptureRequest.get(CaptureRequest.CONTROL_AWB_REGIONS) != null) {
      Log.w("LegacyRequestMapper", "convertRequestMetadata - control.awbRegions setting is not supported, ignoring value");
    }
    i = localParameters.getMaxNumMeteringAreas();
    paramLegacyRequest = convertMeteringRegionsToLegacy((Rect)localObject2, (ParameterUtils.ZoomData)localObject1, paramLegacyRequest, i, "AE");
    if (i > 0) {
      localParameters.setMeteringAreas(paramLegacyRequest);
    }
    paramLegacyRequest = (MeteringRectangle[])localCaptureRequest.get(CaptureRequest.CONTROL_AF_REGIONS);
    i = localParameters.getMaxNumFocusAreas();
    paramLegacyRequest = convertMeteringRegionsToLegacy((Rect)localObject2, (ParameterUtils.ZoomData)localObject1, paramLegacyRequest, i, "AF");
    if (i > 0) {
      localParameters.setFocusAreas(paramLegacyRequest);
    }
    paramLegacyRequest = (Range)localCaptureRequest.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
    if (paramLegacyRequest != null)
    {
      localObject1 = convertAeFpsRangeToLegacy(paramLegacyRequest);
      localObject2 = null;
      Iterator localIterator = localParameters.getSupportedPreviewFpsRange().iterator();
      for (;;)
      {
        paramLegacyRequest = (LegacyRequest)localObject2;
        if (!localIterator.hasNext()) {
          break;
        }
        paramLegacyRequest = (int[])localIterator.next();
        j = (int)Math.floor(paramLegacyRequest[0] / 1000.0D);
        i = (int)Math.ceil(paramLegacyRequest[1] / 1000.0D);
        if ((localObject1[0] == j * 1000) && (localObject1[1] == i * 1000)) {
          break;
        }
      }
      if (paramLegacyRequest != null)
      {
        localParameters.setPreviewFpsRange(paramLegacyRequest[0], paramLegacyRequest[1]);
      }
      else
      {
        paramLegacyRequest = new StringBuilder();
        paramLegacyRequest.append("Unsupported FPS range set [");
        paramLegacyRequest.append(localObject1[0]);
        paramLegacyRequest.append(",");
        paramLegacyRequest.append(localObject1[1]);
        paramLegacyRequest.append("]");
        Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
      }
    }
    paramLegacyRequest = (Range)localCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
    int j = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(0))).intValue();
    i = j;
    if (!paramLegacyRequest.contains(Integer.valueOf(j)))
    {
      Log.w("LegacyRequestMapper", "convertRequestMetadata - control.aeExposureCompensation is out of range, ignoring value");
      i = 0;
    }
    localParameters.setExposureCompensation(i);
    paramLegacyRequest = (Boolean)getIfSupported(localCaptureRequest, CaptureRequest.CONTROL_AE_LOCK, Boolean.valueOf(false), localParameters.isAutoExposureLockSupported(), Boolean.valueOf(false));
    if (paramLegacyRequest != null) {
      localParameters.setAutoExposureLock(paramLegacyRequest.booleanValue());
    }
    mapAeAndFlashMode(localCaptureRequest, localParameters);
    i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(0))).intValue();
    paramLegacyRequest = LegacyMetadataMapper.convertAfModeToLegacy(i, localParameters.getSupportedFocusModes());
    if (paramLegacyRequest != null) {
      localParameters.setFocusMode(paramLegacyRequest);
    }
    paramLegacyRequest = CaptureRequest.CONTROL_AWB_MODE;
    if (localParameters.getSupportedWhiteBalance() != null) {
      bool = true;
    } else {
      bool = false;
    }
    paramLegacyRequest = (Integer)getIfSupported(localCaptureRequest, paramLegacyRequest, Integer.valueOf(1), bool, Integer.valueOf(1));
    if (paramLegacyRequest != null) {
      localParameters.setWhiteBalance(convertAwbModeToLegacy(paramLegacyRequest.intValue()));
    }
    paramLegacyRequest = (Boolean)getIfSupported(localCaptureRequest, CaptureRequest.CONTROL_AWB_LOCK, Boolean.valueOf(false), localParameters.isAutoWhiteBalanceLockSupported(), Boolean.valueOf(false));
    if (paramLegacyRequest != null) {
      localParameters.setAutoWhiteBalanceLock(paramLegacyRequest.booleanValue());
    }
    i = filterSupportedCaptureIntent(((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(1))).intValue());
    if ((i != 3) && (i != 4)) {
      bool = false;
    } else {
      bool = true;
    }
    localParameters.setRecordingHint(bool);
    paramLegacyRequest = (Integer)getIfSupported(localCaptureRequest, CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, Integer.valueOf(0), localParameters.isVideoStabilizationSupported(), Integer.valueOf(0));
    if (paramLegacyRequest != null)
    {
      if (paramLegacyRequest.intValue() == 1) {
        bool = true;
      } else {
        bool = false;
      }
      localParameters.setVideoStabilization(bool);
    }
    boolean bool = ListUtils.listContains(localParameters.getSupportedFocusModes(), "infinity");
    paramLegacyRequest = (Float)getIfSupported(localCaptureRequest, CaptureRequest.LENS_FOCUS_DISTANCE, Float.valueOf(0.0F), bool, Float.valueOf(0.0F));
    if ((paramLegacyRequest == null) || (paramLegacyRequest.floatValue() != 0.0F))
    {
      paramLegacyRequest = new StringBuilder();
      paramLegacyRequest.append("convertRequestToMetadata - Ignoring android.lens.focusDistance ");
      paramLegacyRequest.append(bool);
      paramLegacyRequest.append(", only 0.0f is supported");
      Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
    }
    if (localParameters.getSupportedSceneModes() != null)
    {
      i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_MODE, Integer.valueOf(1))).intValue();
      switch (i)
      {
      default: 
        paramLegacyRequest = new StringBuilder();
        paramLegacyRequest.append("Control mode ");
        paramLegacyRequest.append(i);
        paramLegacyRequest.append(" is unsupported, defaulting to AUTO");
        Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
        paramLegacyRequest = "auto";
        break;
      case 2: 
        i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(0))).intValue();
        paramLegacyRequest = LegacyMetadataMapper.convertSceneModeToLegacy(i);
        if (paramLegacyRequest == null)
        {
          paramLegacyRequest = "auto";
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Skipping unknown requested scene mode: ");
          ((StringBuilder)localObject2).append(i);
          Log.w("LegacyRequestMapper", ((StringBuilder)localObject2).toString());
        }
        break;
      case 1: 
        paramLegacyRequest = "auto";
      }
      localParameters.setSceneMode(paramLegacyRequest);
    }
    if (localParameters.getSupportedColorEffects() != null)
    {
      i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.CONTROL_EFFECT_MODE, Integer.valueOf(0))).intValue();
      paramLegacyRequest = LegacyMetadataMapper.convertEffectModeToLegacy(i);
      if (paramLegacyRequest != null)
      {
        localParameters.setColorEffect(paramLegacyRequest);
      }
      else
      {
        localParameters.setColorEffect("none");
        paramLegacyRequest = new StringBuilder();
        paramLegacyRequest.append("Skipping unknown requested effect mode: ");
        paramLegacyRequest.append(i);
        Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
      }
    }
    i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.SENSOR_TEST_PATTERN_MODE, Integer.valueOf(0))).intValue();
    if (i != 0)
    {
      paramLegacyRequest = new StringBuilder();
      paramLegacyRequest.append("convertRequestToMetadata - ignoring sensor.testPatternMode ");
      paramLegacyRequest.append(i);
      paramLegacyRequest.append("; only OFF is supported");
      Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
    }
    paramLegacyRequest = (Location)localCaptureRequest.get(CaptureRequest.JPEG_GPS_LOCATION);
    if (paramLegacyRequest != null)
    {
      if (checkForCompleteGpsData(paramLegacyRequest))
      {
        localParameters.setGpsAltitude(paramLegacyRequest.getAltitude());
        localParameters.setGpsLatitude(paramLegacyRequest.getLatitude());
        localParameters.setGpsLongitude(paramLegacyRequest.getLongitude());
        localParameters.setGpsProcessingMethod(paramLegacyRequest.getProvider().toUpperCase());
        localParameters.setGpsTimestamp(paramLegacyRequest.getTime());
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Incomplete GPS parameters provided in location ");
        ((StringBuilder)localObject2).append(paramLegacyRequest);
        Log.w("LegacyRequestMapper", ((StringBuilder)localObject2).toString());
      }
    }
    else {
      localParameters.removeGpsData();
    }
    localObject2 = (Integer)localCaptureRequest.get(CaptureRequest.JPEG_ORIENTATION);
    paramLegacyRequest = CaptureRequest.JPEG_ORIENTATION;
    if (localObject2 == null) {
      i = 0;
    } else {
      i = ((Integer)localObject2).intValue();
    }
    localParameters.setRotation(((Integer)ParamsUtils.getOrDefault(localCaptureRequest, paramLegacyRequest, Integer.valueOf(i))).intValue());
    localParameters.setJpegQuality(((Byte)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.JPEG_QUALITY, Byte.valueOf((byte)85))).byteValue() & 0xFF);
    localParameters.setJpegThumbnailQuality(((Byte)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.JPEG_THUMBNAIL_QUALITY, Byte.valueOf((byte)85))).byteValue() & 0xFF);
    localObject2 = localParameters.getSupportedJpegThumbnailSizes();
    if ((localObject2 != null) && (((List)localObject2).size() > 0))
    {
      paramLegacyRequest = (Size)localCaptureRequest.get(CaptureRequest.JPEG_THUMBNAIL_SIZE);
      if (paramLegacyRequest == null) {}
      while (ParameterUtils.containsSize((List)localObject2, paramLegacyRequest.getWidth(), paramLegacyRequest.getHeight()))
      {
        i = 0;
        break;
      }
      i = 1;
      if (i != 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Invalid JPEG thumbnail size set ");
        ((StringBuilder)localObject2).append(paramLegacyRequest);
        ((StringBuilder)localObject2).append(", skipping thumbnail...");
        Log.w("LegacyRequestMapper", ((StringBuilder)localObject2).toString());
      }
      if ((paramLegacyRequest != null) && (i == 0)) {
        localParameters.setJpegThumbnailSize(paramLegacyRequest.getWidth(), paramLegacyRequest.getHeight());
      } else {
        localParameters.setJpegThumbnailSize(0, 0);
      }
    }
    i = ((Integer)ParamsUtils.getOrDefault(localCaptureRequest, CaptureRequest.NOISE_REDUCTION_MODE, Integer.valueOf(1))).intValue();
    if ((i != 1) && (i != 2))
    {
      paramLegacyRequest = new StringBuilder();
      paramLegacyRequest.append("convertRequestToMetadata - Ignoring unsupported noiseReduction.mode = ");
      paramLegacyRequest.append(i);
      Log.w("LegacyRequestMapper", paramLegacyRequest.toString());
    }
  }
  
  static int filterSupportedCaptureIntent(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 5: 
    case 6: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported control.captureIntent value ");
      localStringBuilder.append(1);
      localStringBuilder.append("; default to PREVIEW");
      Log.w("LegacyRequestMapper", localStringBuilder.toString());
      break;
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    case 4: 
      break;
    }
    paramInt = 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown control.captureIntent value ");
    localStringBuilder.append(1);
    localStringBuilder.append("; default to PREVIEW");
    Log.w("LegacyRequestMapper", localStringBuilder.toString());
    return paramInt;
  }
  
  private static <T> T getIfSupported(CaptureRequest paramCaptureRequest, CaptureRequest.Key<T> paramKey, T paramT1, boolean paramBoolean, T paramT2)
  {
    paramCaptureRequest = ParamsUtils.getOrDefault(paramCaptureRequest, paramKey, paramT1);
    if (!paramBoolean)
    {
      if (!Objects.equals(paramCaptureRequest, paramT2))
      {
        paramT1 = new StringBuilder();
        paramT1.append(paramKey.getName());
        paramT1.append(" is not supported; ignoring requested value ");
        paramT1.append(paramCaptureRequest);
        Log.w("LegacyRequestMapper", paramT1.toString());
      }
      return null;
    }
    return paramCaptureRequest;
  }
  
  private static void mapAeAndFlashMode(CaptureRequest paramCaptureRequest, Camera.Parameters paramParameters)
  {
    int i = ((Integer)ParamsUtils.getOrDefault(paramCaptureRequest, CaptureRequest.FLASH_MODE, Integer.valueOf(0))).intValue();
    int j = ((Integer)ParamsUtils.getOrDefault(paramCaptureRequest, CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1))).intValue();
    List localList = paramParameters.getSupportedFlashModes();
    String str = null;
    if (ListUtils.listContains(localList, "off")) {
      str = "off";
    }
    if (j == 1)
    {
      if (i == 2)
      {
        if (ListUtils.listContains(localList, "torch"))
        {
          paramCaptureRequest = "torch";
        }
        else
        {
          Log.w("LegacyRequestMapper", "mapAeAndFlashMode - Ignore flash.mode == TORCH;camera does not support it");
          paramCaptureRequest = str;
        }
      }
      else
      {
        paramCaptureRequest = str;
        if (i == 1) {
          if (ListUtils.listContains(localList, "on"))
          {
            paramCaptureRequest = "on";
          }
          else
          {
            Log.w("LegacyRequestMapper", "mapAeAndFlashMode - Ignore flash.mode == SINGLE;camera does not support it");
            paramCaptureRequest = str;
          }
        }
      }
    }
    else if (j == 3)
    {
      if (ListUtils.listContains(localList, "on"))
      {
        paramCaptureRequest = "on";
      }
      else
      {
        Log.w("LegacyRequestMapper", "mapAeAndFlashMode - Ignore control.aeMode == ON_ALWAYS_FLASH;camera does not support it");
        paramCaptureRequest = str;
      }
    }
    else if (j == 2)
    {
      if (ListUtils.listContains(localList, "auto"))
      {
        paramCaptureRequest = "auto";
      }
      else
      {
        Log.w("LegacyRequestMapper", "mapAeAndFlashMode - Ignore control.aeMode == ON_AUTO_FLASH;camera does not support it");
        paramCaptureRequest = str;
      }
    }
    else
    {
      paramCaptureRequest = str;
      if (j == 4) {
        if (ListUtils.listContains(localList, "red-eye"))
        {
          paramCaptureRequest = "red-eye";
        }
        else
        {
          Log.w("LegacyRequestMapper", "mapAeAndFlashMode - Ignore control.aeMode == ON_AUTO_FLASH_REDEYE;camera does not support it");
          paramCaptureRequest = str;
        }
      }
    }
    if (paramCaptureRequest != null) {
      paramParameters.setFlashMode(paramCaptureRequest);
    }
  }
}
