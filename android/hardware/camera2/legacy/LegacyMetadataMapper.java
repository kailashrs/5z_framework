package android.hardware.camera2.legacy;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.CameraInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.CaptureResult.Key;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CameraMetadataNative.Key;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.utils.ArrayUtils;
import android.hardware.camera2.utils.ListUtils;
import android.hardware.camera2.utils.ParamsUtils;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SizeF;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LegacyMetadataMapper
{
  private static final long APPROXIMATE_CAPTURE_DELAY_MS = 200L;
  private static final long APPROXIMATE_JPEG_ENCODE_TIME_MS = 600L;
  private static final long APPROXIMATE_SENSOR_AREA_PX = 8388608L;
  private static final boolean DEBUG = false;
  public static final int HAL_PIXEL_FORMAT_BGRA_8888 = 5;
  public static final int HAL_PIXEL_FORMAT_BLOB = 33;
  public static final int HAL_PIXEL_FORMAT_IMPLEMENTATION_DEFINED = 34;
  public static final int HAL_PIXEL_FORMAT_RGBA_8888 = 1;
  private static final float LENS_INFO_MINIMUM_FOCUS_DISTANCE_FIXED_FOCUS = 0.0F;
  static final boolean LIE_ABOUT_AE_MAX_REGIONS = false;
  static final boolean LIE_ABOUT_AE_STATE = false;
  static final boolean LIE_ABOUT_AF = false;
  static final boolean LIE_ABOUT_AF_MAX_REGIONS = false;
  static final boolean LIE_ABOUT_AWB = false;
  static final boolean LIE_ABOUT_AWB_STATE = false;
  private static final long NS_PER_MS = 1000000L;
  private static final float PREVIEW_ASPECT_RATIO_TOLERANCE = 0.01F;
  private static final int REQUEST_MAX_NUM_INPUT_STREAMS_COUNT = 0;
  private static final int REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_PROC = 3;
  private static final int REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_PROC_STALL = 1;
  private static final int REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_RAW = 0;
  private static final int REQUEST_PIPELINE_MAX_DEPTH_HAL1 = 3;
  private static final int REQUEST_PIPELINE_MAX_DEPTH_OURS = 3;
  private static final String TAG = "LegacyMetadataMapper";
  static final int UNKNOWN_MODE = -1;
  private static final int[] sAllowedTemplates = { 1, 2, 3 };
  private static final int[] sEffectModes;
  private static final String[] sLegacyEffectMode;
  private static final String[] sLegacySceneModes = { "auto", "action", "portrait", "landscape", "night", "night-portrait", "theatre", "beach", "snow", "sunset", "steadyphoto", "fireworks", "sports", "party", "candlelight", "barcode", "hdr" };
  private static final int[] sSceneModes = { 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18 };
  
  static
  {
    sLegacyEffectMode = new String[] { "none", "mono", "negative", "solarize", "sepia", "posterize", "whiteboard", "blackboard", "aqua" };
    sEffectModes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
  }
  
  public LegacyMetadataMapper() {}
  
  private static void appendStreamConfig(ArrayList<StreamConfiguration> paramArrayList, int paramInt, List<Camera.Size> paramList)
  {
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Camera.Size localSize = (Camera.Size)paramList.next();
      paramArrayList.add(new StreamConfiguration(paramInt, width, height, false));
    }
  }
  
  private static long calculateJpegStallDuration(Camera.Size paramSize)
  {
    return width * height * 71L + 200000000L;
  }
  
  private static int[] convertAeFpsRangeToLegacy(Range<Integer> paramRange)
  {
    return new int[] { ((Integer)paramRange.getLower()).intValue(), ((Integer)paramRange.getUpper()).intValue() };
  }
  
  static String convertAfModeToLegacy(int paramInt, List<String> paramList)
  {
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      String str1 = null;
      switch (paramInt)
      {
      default: 
        break;
      case 5: 
        str1 = "edof";
        break;
      case 4: 
        str1 = "continuous-picture";
        break;
      case 3: 
        str1 = "continuous-video";
        break;
      case 2: 
        str1 = "macro";
        break;
      case 1: 
        str1 = "auto";
        break;
      case 0: 
        if (paramList.contains("fixed")) {
          str1 = "fixed";
        } else {
          str1 = "infinity";
        }
        break;
      }
      String str2 = str1;
      if (!paramList.contains(str1))
      {
        str2 = (String)paramList.get(0);
        Log.w("LegacyMetadataMapper", String.format("convertAfModeToLegacy - ignoring unsupported mode %d, defaulting to %s", new Object[] { Integer.valueOf(paramInt), str2 }));
      }
      return str2;
    }
    Log.w("LegacyMetadataMapper", "No focus modes supported; API1 bug");
    return null;
  }
  
  private static int convertAntiBandingMode(String paramString)
  {
    if (paramString == null) {
      return -1;
    }
    int i = paramString.hashCode();
    if (i != 109935)
    {
      if (i != 1628397)
      {
        if (i != 1658188)
        {
          if ((i == 3005871) && (paramString.equals("auto")))
          {
            i = 3;
            break label96;
          }
        }
        else if (paramString.equals("60hz"))
        {
          i = 2;
          break label96;
        }
      }
      else if (paramString.equals("50hz"))
      {
        i = 1;
        break label96;
      }
    }
    else if (paramString.equals("off"))
    {
      i = 0;
      break label96;
    }
    i = -1;
    switch (i)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("convertAntiBandingMode - Unknown antibanding mode ");
      localStringBuilder.append(paramString);
      Log.w("LegacyMetadataMapper", localStringBuilder.toString());
      return -1;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      label96:
      return 1;
    }
    return 0;
  }
  
  static int convertAntiBandingModeOrDefault(String paramString)
  {
    int i = convertAntiBandingMode(paramString);
    if (i == -1) {
      return 0;
    }
    return i;
  }
  
  static int convertEffectModeFromLegacy(String paramString)
  {
    if (paramString == null) {
      return 0;
    }
    int i = ArrayUtils.getArrayIndex(sLegacyEffectMode, paramString);
    if (i < 0) {
      return -1;
    }
    return sEffectModes[i];
  }
  
  static String convertEffectModeToLegacy(int paramInt)
  {
    paramInt = ArrayUtils.getArrayIndex(sEffectModes, paramInt);
    if (paramInt < 0) {
      return null;
    }
    return sLegacyEffectMode[paramInt];
  }
  
  public static void convertRequestMetadata(LegacyRequest paramLegacyRequest)
  {
    LegacyRequestMapper.convertRequestMetadata(paramLegacyRequest);
  }
  
  static int convertSceneModeFromLegacy(String paramString)
  {
    if (paramString == null) {
      return 0;
    }
    int i = ArrayUtils.getArrayIndex(sLegacySceneModes, paramString);
    if (i < 0) {
      return -1;
    }
    return sSceneModes[i];
  }
  
  static String convertSceneModeToLegacy(int paramInt)
  {
    if (paramInt == 1) {
      return "auto";
    }
    paramInt = ArrayUtils.getArrayIndex(sSceneModes, paramInt);
    if (paramInt < 0) {
      return null;
    }
    return sLegacySceneModes[paramInt];
  }
  
  public static CameraCharacteristics createCharacteristics(Camera.Parameters paramParameters, Camera.CameraInfo paramCameraInfo)
  {
    Preconditions.checkNotNull(paramParameters, "parameters must not be null");
    Preconditions.checkNotNull(paramCameraInfo, "info must not be null");
    paramParameters = paramParameters.flatten();
    CameraInfo localCameraInfo = new CameraInfo();
    info = paramCameraInfo;
    return createCharacteristics(paramParameters, localCameraInfo);
  }
  
  public static CameraCharacteristics createCharacteristics(String paramString, CameraInfo paramCameraInfo)
  {
    Preconditions.checkNotNull(paramString, "parameters must not be null");
    Preconditions.checkNotNull(paramCameraInfo, "info must not be null");
    Preconditions.checkNotNull(info, "info.info must not be null");
    CameraMetadataNative localCameraMetadataNative = new CameraMetadataNative();
    mapCharacteristicsFromInfo(localCameraMetadataNative, info);
    paramCameraInfo = Camera.getEmptyParameters();
    paramCameraInfo.unflatten(paramString);
    mapCharacteristicsFromParameters(localCameraMetadataNative, paramCameraInfo);
    return new CameraCharacteristics(localCameraMetadataNative);
  }
  
  public static CameraMetadataNative createRequestTemplate(CameraCharacteristics paramCameraCharacteristics, int paramInt)
  {
    if (ArrayUtils.contains(sAllowedTemplates, paramInt))
    {
      CameraMetadataNative localCameraMetadataNative = new CameraMetadataNative();
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(1));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, Integer.valueOf(3));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(0));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_LOCK, Boolean.valueOf(false));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, Integer.valueOf(0));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(1));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AWB_LOCK, Boolean.valueOf(false));
      Object localObject1 = (Rect)paramCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
      Object localObject2 = new MeteringRectangle[1];
      localObject2[0] = new MeteringRectangle(0, 0, ((Rect)localObject1).width() - 1, ((Rect)localObject1).height() - 1, 0);
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_REGIONS, localObject2);
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AWB_REGIONS, localObject2);
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AF_REGIONS, localObject2);
      switch (paramInt)
      {
      default: 
        throw new AssertionError("Impossible; keep in sync with sAllowedTemplates");
      case 3: 
        i = 3;
        break;
      case 2: 
        i = 2;
        break;
      case 1: 
        i = 1;
      }
      localCameraMetadataNative.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(i));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
      localCameraMetadataNative.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
      localObject2 = (Float)paramCameraCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
      if ((localObject2 != null) && (((Float)localObject2).floatValue() == 0.0F))
      {
        i = 0;
      }
      else
      {
        if ((paramInt != 3) && (paramInt != 4))
        {
          if (((paramInt != 1) && (paramInt != 2)) || (!ArrayUtils.contains((int[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES), 4))) {
            break label366;
          }
          i = 4;
        }
        else
        {
          if (!ArrayUtils.contains((int[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES), 3)) {
            break label366;
          }
          i = 3;
        }
        break label369;
        label366:
        i = 1;
      }
      label369:
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(i));
      Range[] arrayOfRange = (Range[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
      localObject1 = arrayOfRange[0];
      int j = arrayOfRange.length;
      int i = 0;
      while (i < j)
      {
        Range localRange = arrayOfRange[i];
        if (((Integer)((Range)localObject1).getUpper()).intValue() < ((Integer)localRange.getUpper()).intValue())
        {
          localObject2 = localRange;
        }
        else
        {
          localObject2 = localObject1;
          if (((Range)localObject1).getUpper() == localRange.getUpper())
          {
            localObject2 = localObject1;
            if (((Integer)((Range)localObject1).getLower()).intValue() < ((Integer)localRange.getLower()).intValue()) {
              localObject2 = localRange;
            }
          }
        }
        i++;
        localObject1 = localObject2;
      }
      localCameraMetadataNative.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, localObject1);
      localCameraMetadataNative.set(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(0));
      localCameraMetadataNative.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(0));
      localCameraMetadataNative.set(CaptureRequest.FLASH_MODE, Integer.valueOf(0));
      if (paramInt == 2) {
        localCameraMetadataNative.set(CaptureRequest.NOISE_REDUCTION_MODE, Integer.valueOf(2));
      } else {
        localCameraMetadataNative.set(CaptureRequest.NOISE_REDUCTION_MODE, Integer.valueOf(1));
      }
      if (paramInt == 2) {
        localCameraMetadataNative.set(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, Integer.valueOf(2));
      } else {
        localCameraMetadataNative.set(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, Integer.valueOf(1));
      }
      localCameraMetadataNative.set(CaptureRequest.LENS_FOCAL_LENGTH, Float.valueOf(((float[])paramCameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS))[0]));
      paramCameraCharacteristics = (Size[])paramCameraCharacteristics.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES);
      localObject2 = CaptureRequest.JPEG_THUMBNAIL_SIZE;
      if (paramCameraCharacteristics.length > 1) {
        paramCameraCharacteristics = paramCameraCharacteristics[1];
      } else {
        paramCameraCharacteristics = paramCameraCharacteristics[0];
      }
      localCameraMetadataNative.set((CaptureRequest.Key)localObject2, paramCameraCharacteristics);
      return localCameraMetadataNative;
    }
    throw new IllegalArgumentException("templateId out of range");
  }
  
  private static int[] getTagsForKeys(CameraCharacteristics.Key<?>[] paramArrayOfKey)
  {
    int[] arrayOfInt = new int[paramArrayOfKey.length];
    for (int i = 0; i < paramArrayOfKey.length; i++) {
      arrayOfInt[i] = paramArrayOfKey[i].getNativeKey().getTag();
    }
    return arrayOfInt;
  }
  
  private static int[] getTagsForKeys(CaptureRequest.Key<?>[] paramArrayOfKey)
  {
    int[] arrayOfInt = new int[paramArrayOfKey.length];
    for (int i = 0; i < paramArrayOfKey.length; i++) {
      arrayOfInt[i] = paramArrayOfKey[i].getNativeKey().getTag();
    }
    return arrayOfInt;
  }
  
  private static int[] getTagsForKeys(CaptureResult.Key<?>[] paramArrayOfKey)
  {
    int[] arrayOfInt = new int[paramArrayOfKey.length];
    for (int i = 0; i < paramArrayOfKey.length; i++) {
      arrayOfInt[i] = paramArrayOfKey[i].getNativeKey().getTag();
    }
    return arrayOfInt;
  }
  
  private static void mapCharacteristicsFromInfo(CameraMetadataNative paramCameraMetadataNative, Camera.CameraInfo paramCameraInfo)
  {
    CameraCharacteristics.Key localKey = CameraCharacteristics.LENS_FACING;
    int i;
    if (facing == 0) {
      i = 1;
    } else {
      i = 0;
    }
    paramCameraMetadataNative.set(localKey, Integer.valueOf(i));
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_ORIENTATION, Integer.valueOf(orientation));
  }
  
  private static void mapCharacteristicsFromParameters(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    paramCameraMetadataNative.set(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES, new int[] { 1, 2 });
    mapControlAe(paramCameraMetadataNative, paramParameters);
    mapControlAf(paramCameraMetadataNative, paramParameters);
    mapControlAwb(paramCameraMetadataNative, paramParameters);
    mapControlOther(paramCameraMetadataNative, paramParameters);
    mapLens(paramCameraMetadataNative, paramParameters);
    mapFlash(paramCameraMetadataNative, paramParameters);
    mapJpeg(paramCameraMetadataNative, paramParameters);
    paramCameraMetadataNative.set(CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES, new int[] { 1, 2 });
    mapScaler(paramCameraMetadataNative, paramParameters);
    mapSensor(paramCameraMetadataNative, paramParameters);
    mapStatistics(paramCameraMetadataNative, paramParameters);
    mapSync(paramCameraMetadataNative, paramParameters);
    paramCameraMetadataNative.set(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, Integer.valueOf(2));
    mapScalerStreamConfigs(paramCameraMetadataNative, paramParameters);
    mapRequest(paramCameraMetadataNative, paramParameters);
  }
  
  private static void mapControlAe(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    Object localObject1 = paramParameters.getSupportedAntibanding();
    int i;
    if ((localObject1 != null) && (((List)localObject1).size() > 0))
    {
      localObject2 = new int[((List)localObject1).size()];
      i = 0;
      localObject1 = ((List)localObject1).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2[i] = convertAntiBandingMode((String)((Iterator)localObject1).next());
        i++;
      }
      paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES, Arrays.copyOf((int[])localObject2, i));
    }
    else
    {
      paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES, new int[0]);
    }
    Object localObject2 = paramParameters.getSupportedPreviewFpsRange();
    if (localObject2 != null)
    {
      i = ((List)localObject2).size();
      if (i > 0)
      {
        localObject1 = new Range[i];
        i = 0;
        Iterator localIterator = ((List)localObject2).iterator();
        while (localIterator.hasNext())
        {
          int[] arrayOfInt = (int[])localIterator.next();
          localObject1[i] = Range.create(Integer.valueOf((int)Math.floor(arrayOfInt[0] / 1000.0D)), Integer.valueOf((int)Math.ceil(arrayOfInt[1] / 1000.0D)));
          i++;
        }
        paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES, localObject1);
        localObject1 = ArrayUtils.convertStringListToIntArray(paramParameters.getSupportedFlashModes(), new String[] { "off", "auto", "on", "red-eye", "torch" }, new int[] { 1, 2, 3, 4 });
        if (localObject1 != null)
        {
          localObject2 = localObject1;
          if (localObject1.length != 0) {}
        }
        else
        {
          localObject2 = new int[] { 1 };
        }
        paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES, localObject2);
        int j = paramParameters.getMinExposureCompensation();
        i = paramParameters.getMaxExposureCompensation();
        paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE, Range.create(Integer.valueOf(j), Integer.valueOf(i)));
        float f = paramParameters.getExposureCompensationStep();
        paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP, ParamsUtils.createRational(f));
        boolean bool = paramParameters.isAutoExposureLockSupported();
        paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE, Boolean.valueOf(bool));
        return;
      }
      throw new AssertionError("At least one FPS range must be supported.");
    }
    throw new AssertionError("Supported FPS ranges cannot be null.");
  }
  
  private static void mapControlAf(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    List localList = ArrayUtils.convertStringListToIntList(paramParameters.getSupportedFocusModes(), new String[] { "auto", "continuous-picture", "continuous-video", "edof", "infinity", "macro", "fixed" }, new int[] { 1, 4, 3, 5, 0, 2, 0 });
    if (localList != null)
    {
      paramParameters = localList;
      if (localList.size() != 0) {}
    }
    else
    {
      Log.w("LegacyMetadataMapper", "No AF modes supported (HAL bug); defaulting to AF_MODE_OFF only");
      paramParameters = new ArrayList(1);
      paramParameters.add(Integer.valueOf(0));
    }
    paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES, ArrayUtils.toIntArray(paramParameters));
  }
  
  private static void mapControlAwb(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    List localList = ArrayUtils.convertStringListToIntList(paramParameters.getSupportedWhiteBalance(), new String[] { "auto", "incandescent", "fluorescent", "warm-fluorescent", "daylight", "cloudy-daylight", "twilight", "shade" }, new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
    Object localObject;
    if (localList != null)
    {
      localObject = localList;
      if (localList.size() != 0) {}
    }
    else
    {
      Log.w("LegacyMetadataMapper", "No AWB modes supported (HAL bug); defaulting to AWB_MODE_AUTO only");
      localObject = new ArrayList(1);
      ((List)localObject).add(Integer.valueOf(1));
    }
    paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES, ArrayUtils.toIntArray((List)localObject));
    boolean bool = paramParameters.isAutoWhiteBalanceLockSupported();
    paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE, Boolean.valueOf(bool));
  }
  
  private static void mapControlOther(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    if (paramParameters.isVideoStabilizationSupported())
    {
      localObject = new int[2];
      Object tmp12_11 = localObject;
      tmp12_11[0] = 0;
      Object tmp16_12 = tmp12_11;
      tmp16_12[1] = 1;
      tmp16_12;
    }
    else
    {
      localObject = new int[1];
      localObject[0] = 0;
    }
    paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES, localObject);
    int i = paramParameters.getMaxNumMeteringAreas();
    int j = paramParameters.getMaxNumFocusAreas();
    paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_MAX_REGIONS, new int[] { i, 0, j });
    Object localObject = paramParameters.getSupportedColorEffects();
    if (localObject == null) {
      localObject = new int[0];
    } else {
      localObject = ArrayUtils.convertStringListToIntArray((List)localObject, sLegacyEffectMode, sEffectModes);
    }
    paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS, localObject);
    int k = paramParameters.getMaxNumDetectedFaces();
    List localList = paramParameters.getSupportedSceneModes();
    localObject = ArrayUtils.convertStringListToIntList(localList, sLegacySceneModes, sSceneModes);
    paramParameters = (Camera.Parameters)localObject;
    if (localList != null)
    {
      paramParameters = (Camera.Parameters)localObject;
      if (localList.size() == 1)
      {
        paramParameters = (Camera.Parameters)localObject;
        if (((String)localList.get(0)).equals("auto")) {
          paramParameters = null;
        }
      }
    }
    j = 1;
    i = j;
    if (paramParameters == null)
    {
      i = j;
      if (k == 0) {
        i = 0;
      }
    }
    if (i != 0)
    {
      localObject = paramParameters;
      if (paramParameters == null) {
        localObject = new ArrayList();
      }
      if (k > 0) {
        ((List)localObject).add(Integer.valueOf(1));
      }
      while ((((List)localObject).contains(Integer.valueOf(0))) && (((List)localObject).remove(new Integer(0)))) {}
      paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES, ArrayUtils.toIntArray((List)localObject));
    }
    else
    {
      paramCameraMetadataNative.set(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES, new int[] { 0 });
    }
    localObject = CameraCharacteristics.CONTROL_AVAILABLE_MODES;
    if (i != 0)
    {
      paramParameters = new int[2];
      Camera.Parameters tmp304_303 = paramParameters;
      tmp304_303[0] = 1;
      Camera.Parameters tmp308_304 = tmp304_303;
      tmp308_304[1] = 2;
      tmp308_304;
    }
    else
    {
      paramParameters = new int[1];
      paramParameters[0] = 1;
    }
    paramCameraMetadataNative.set((CameraCharacteristics.Key)localObject, paramParameters);
  }
  
  private static void mapFlash(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    boolean bool = false;
    paramParameters = paramParameters.getSupportedFlashModes();
    if (paramParameters != null) {
      bool = ListUtils.listElementsEqualTo(paramParameters, "off") ^ true;
    }
    paramCameraMetadataNative.set(CameraCharacteristics.FLASH_INFO_AVAILABLE, Boolean.valueOf(bool));
  }
  
  private static void mapJpeg(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    paramParameters = paramParameters.getSupportedJpegThumbnailSizes();
    if (paramParameters != null)
    {
      paramParameters = ParameterUtils.convertSizeListToArray(paramParameters);
      Arrays.sort(paramParameters, new android.hardware.camera2.utils.SizeAreaComparator());
      paramCameraMetadataNative.set(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES, paramParameters);
    }
  }
  
  private static void mapLens(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    if ("fixed".equals(paramParameters.getFocusMode())) {
      paramCameraMetadataNative.set(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE, Float.valueOf(0.0F));
    }
    float f = paramParameters.getFocalLength();
    paramCameraMetadataNative.set(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS, new float[] { f });
  }
  
  private static void mapRequest(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES, new int[] { 0 });
    Object localObject = new ArrayList(Arrays.asList(new CameraCharacteristics.Key[] { CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES, CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES, CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES, CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES, CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE, CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP, CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE, CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES, CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS, CameraCharacteristics.CONTROL_AVAILABLE_MODES, CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES, CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES, CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES, CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE, CameraCharacteristics.CONTROL_MAX_REGIONS, CameraCharacteristics.FLASH_INFO_AVAILABLE, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES, CameraCharacteristics.LENS_FACING, CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS, CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES, CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES, CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS, CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT, CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH, CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM, CameraCharacteristics.SCALER_CROPPING_TYPE, CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES, CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE, CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE, CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE, CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE, CameraCharacteristics.SENSOR_ORIENTATION, CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES, CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT, CameraCharacteristics.SYNC_MAX_LATENCY }));
    if (paramCameraMetadataNative.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE) != null) {
      ((List)localObject).add(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
    }
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_AVAILABLE_CHARACTERISTICS_KEYS, getTagsForKeys((CameraCharacteristics.Key[])((List)localObject).toArray(new CameraCharacteristics.Key[0])));
    ArrayList localArrayList = new ArrayList(Arrays.asList(new CaptureRequest.Key[] { CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, CaptureRequest.CONTROL_AE_LOCK, CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AWB_LOCK, CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_CAPTURE_INTENT, CaptureRequest.CONTROL_EFFECT_MODE, CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_SCENE_MODE, CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, CaptureRequest.FLASH_MODE, CaptureRequest.JPEG_GPS_COORDINATES, CaptureRequest.JPEG_GPS_PROCESSING_METHOD, CaptureRequest.JPEG_GPS_TIMESTAMP, CaptureRequest.JPEG_ORIENTATION, CaptureRequest.JPEG_QUALITY, CaptureRequest.JPEG_THUMBNAIL_QUALITY, CaptureRequest.JPEG_THUMBNAIL_SIZE, CaptureRequest.LENS_FOCAL_LENGTH, CaptureRequest.NOISE_REDUCTION_MODE, CaptureRequest.SCALER_CROP_REGION, CaptureRequest.STATISTICS_FACE_DETECT_MODE }));
    if (paramParameters.getMaxNumMeteringAreas() > 0) {
      localArrayList.add(CaptureRequest.CONTROL_AE_REGIONS);
    }
    if (paramParameters.getMaxNumFocusAreas() > 0) {
      localArrayList.add(CaptureRequest.CONTROL_AF_REGIONS);
    }
    localObject = new CaptureRequest.Key[localArrayList.size()];
    localArrayList.toArray((Object[])localObject);
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_AVAILABLE_REQUEST_KEYS, getTagsForKeys((CaptureRequest.Key[])localObject));
    localObject = new ArrayList(Arrays.asList(new CaptureResult.Key[] { CaptureResult.COLOR_CORRECTION_ABERRATION_MODE, CaptureResult.CONTROL_AE_ANTIBANDING_MODE, CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION, CaptureResult.CONTROL_AE_LOCK, CaptureResult.CONTROL_AE_MODE, CaptureResult.CONTROL_AF_MODE, CaptureResult.CONTROL_AF_STATE, CaptureResult.CONTROL_AWB_MODE, CaptureResult.CONTROL_AWB_LOCK, CaptureResult.CONTROL_MODE, CaptureResult.FLASH_MODE, CaptureResult.JPEG_GPS_COORDINATES, CaptureResult.JPEG_GPS_PROCESSING_METHOD, CaptureResult.JPEG_GPS_TIMESTAMP, CaptureResult.JPEG_ORIENTATION, CaptureResult.JPEG_QUALITY, CaptureResult.JPEG_THUMBNAIL_QUALITY, CaptureResult.LENS_FOCAL_LENGTH, CaptureResult.NOISE_REDUCTION_MODE, CaptureResult.REQUEST_PIPELINE_DEPTH, CaptureResult.SCALER_CROP_REGION, CaptureResult.SENSOR_TIMESTAMP, CaptureResult.STATISTICS_FACE_DETECT_MODE }));
    if (paramParameters.getMaxNumMeteringAreas() > 0) {
      ((List)localObject).add(CaptureResult.CONTROL_AE_REGIONS);
    }
    if (paramParameters.getMaxNumFocusAreas() > 0) {
      ((List)localObject).add(CaptureResult.CONTROL_AF_REGIONS);
    }
    paramParameters = new CaptureResult.Key[((List)localObject).size()];
    ((List)localObject).toArray(paramParameters);
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_AVAILABLE_RESULT_KEYS, getTagsForKeys(paramParameters));
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS, new int[] { 0, 3, 1 });
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_MAX_NUM_INPUT_STREAMS, Integer.valueOf(0));
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT, Integer.valueOf(1));
    paramCameraMetadataNative.set(CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH, Byte.valueOf((byte)6));
  }
  
  private static void mapScaler(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    paramCameraMetadataNative.set(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM, Float.valueOf(ParameterUtils.getMaxZoomRatio(paramParameters)));
    paramCameraMetadataNative.set(CameraCharacteristics.SCALER_CROPPING_TYPE, Integer.valueOf(0));
  }
  
  private static void mapScalerStreamConfigs(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = paramParameters.getSupportedPreviewSizes();
    List localList = paramParameters.getSupportedPictureSizes();
    SizeAreaComparator localSizeAreaComparator = new SizeAreaComparator();
    Collections.sort((List)localObject1, localSizeAreaComparator);
    Object localObject2 = SizeAreaComparator.findLargestByArea(localList);
    float f = width * 1.0F / height;
    while (!((List)localObject1).isEmpty())
    {
      i = ((List)localObject1).size() - 1;
      localObject2 = (Camera.Size)((List)localObject1).get(i);
      if (Math.abs(f - width * 1.0F / height) < 0.01F) {
        break;
      }
      ((List)localObject1).remove(i);
    }
    localObject2 = localObject1;
    if (((List)localObject1).isEmpty())
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("mapScalerStreamConfigs - failed to find any preview size matching JPEG aspect ratio ");
      ((StringBuilder)localObject2).append(f);
      Log.w("LegacyMetadataMapper", ((StringBuilder)localObject2).toString());
      localObject2 = paramParameters.getSupportedPreviewSizes();
    }
    Collections.sort((List)localObject2, Collections.reverseOrder(localSizeAreaComparator));
    appendStreamConfig(localArrayList, 34, (List)localObject2);
    appendStreamConfig(localArrayList, 35, (List)localObject2);
    localObject1 = paramParameters.getSupportedPreviewFormats().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      i = ((Integer)((Iterator)localObject1).next()).intValue();
      if ((ImageFormat.isPublicFormat(i)) && (i != 17)) {
        appendStreamConfig(localArrayList, i, (List)localObject2);
      }
    }
    appendStreamConfig(localArrayList, 33, paramParameters.getSupportedPictureSizes());
    paramCameraMetadataNative.set(CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS, (StreamConfiguration[])localArrayList.toArray(new StreamConfiguration[0]));
    paramCameraMetadataNative.set(CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS, new StreamConfigurationDuration[0]);
    paramParameters = new StreamConfigurationDuration[localList.size()];
    int i = 0;
    long l1 = -1L;
    localObject2 = localList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (Camera.Size)((Iterator)localObject2).next();
      long l2 = calculateJpegStallDuration((Camera.Size)localObject1);
      paramParameters[i] = new StreamConfigurationDuration(33, width, height, l2);
      long l3 = l1;
      if (l1 < l2) {
        l3 = l2;
      }
      i++;
      l1 = l3;
    }
    paramCameraMetadataNative.set(CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS, paramParameters);
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION, Long.valueOf(l1));
  }
  
  private static void mapSensor(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    Size localSize = ParameterUtils.getLargestSupportedJpegSizeByArea(paramParameters);
    Rect localRect = ParamsUtils.createRect(localSize);
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE, localRect);
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES, new int[] { 0 });
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE, localSize);
    float f1 = paramParameters.getFocalLength();
    double d1 = paramParameters.getHorizontalViewAngle() * 3.141592653589793D / 180.0D;
    double d2 = paramParameters.getVerticalViewAngle() * 3.141592653589793D / 180.0D;
    float f2 = (float)Math.abs(2.0F * f1 * Math.tan(d2 / 2.0D));
    f1 = (float)Math.abs(2.0F * f1 * Math.tan(d1 / 2.0D));
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE, new SizeF(f1, f2));
    paramCameraMetadataNative.set(CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE, Integer.valueOf(0));
  }
  
  private static void mapStatistics(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    int[] arrayOfInt;
    if (paramParameters.getMaxNumDetectedFaces() > 0)
    {
      arrayOfInt = new int[2];
      int[] tmp12_11 = arrayOfInt;
      tmp12_11[0] = 0;
      int[] tmp16_12 = tmp12_11;
      tmp16_12[1] = 1;
      tmp16_12;
    }
    else
    {
      arrayOfInt = new int[1];
      arrayOfInt[0] = 0;
    }
    paramCameraMetadataNative.set(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES, arrayOfInt);
    paramCameraMetadataNative.set(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT, Integer.valueOf(paramParameters.getMaxNumDetectedFaces()));
  }
  
  private static void mapSync(CameraMetadataNative paramCameraMetadataNative, Camera.Parameters paramParameters)
  {
    paramCameraMetadataNative.set(CameraCharacteristics.SYNC_MAX_LATENCY, Integer.valueOf(-1));
  }
}
