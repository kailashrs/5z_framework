package android.hardware.camera2;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CameraMetadataNative.Key;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PublicKey;
import android.hardware.camera2.impl.SyntheticKey;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.LensShadingMap;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.OisSample;
import android.hardware.camera2.params.RggbChannelVector;
import android.hardware.camera2.params.TonemapCurve;
import android.hardware.camera2.utils.TypeReference;
import android.location.Location;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import java.util.List;

public class CaptureResult
  extends CameraMetadata<Key<?>>
{
  @PublicKey
  public static final Key<Boolean> BLACK_LEVEL_LOCK;
  @PublicKey
  public static final Key<Integer> COLOR_CORRECTION_ABERRATION_MODE;
  @PublicKey
  public static final Key<RggbChannelVector> COLOR_CORRECTION_GAINS;
  @PublicKey
  public static final Key<Integer> COLOR_CORRECTION_MODE = new Key("android.colorCorrection.mode", Integer.TYPE);
  @PublicKey
  public static final Key<ColorSpaceTransform> COLOR_CORRECTION_TRANSFORM = new Key("android.colorCorrection.transform", ColorSpaceTransform.class);
  @PublicKey
  public static final Key<Integer> CONTROL_AE_ANTIBANDING_MODE;
  @PublicKey
  public static final Key<Integer> CONTROL_AE_EXPOSURE_COMPENSATION;
  @PublicKey
  public static final Key<Boolean> CONTROL_AE_LOCK;
  @PublicKey
  public static final Key<Integer> CONTROL_AE_MODE;
  @PublicKey
  public static final Key<Integer> CONTROL_AE_PRECAPTURE_TRIGGER;
  @PublicKey
  public static final Key<MeteringRectangle[]> CONTROL_AE_REGIONS;
  @PublicKey
  public static final Key<Integer> CONTROL_AE_STATE;
  @PublicKey
  public static final Key<Range<Integer>> CONTROL_AE_TARGET_FPS_RANGE;
  @PublicKey
  public static final Key<Integer> CONTROL_AF_MODE;
  @PublicKey
  public static final Key<MeteringRectangle[]> CONTROL_AF_REGIONS;
  @PublicKey
  public static final Key<Integer> CONTROL_AF_SCENE_CHANGE;
  @PublicKey
  public static final Key<Integer> CONTROL_AF_STATE;
  @PublicKey
  public static final Key<Integer> CONTROL_AF_TRIGGER;
  @PublicKey
  public static final Key<Boolean> CONTROL_AWB_LOCK;
  @PublicKey
  public static final Key<Integer> CONTROL_AWB_MODE;
  @PublicKey
  public static final Key<MeteringRectangle[]> CONTROL_AWB_REGIONS;
  @PublicKey
  public static final Key<Integer> CONTROL_AWB_STATE;
  @PublicKey
  public static final Key<Integer> CONTROL_CAPTURE_INTENT;
  @PublicKey
  public static final Key<Integer> CONTROL_EFFECT_MODE;
  @PublicKey
  public static final Key<Boolean> CONTROL_ENABLE_ZSL;
  @PublicKey
  public static final Key<Integer> CONTROL_MODE;
  @PublicKey
  public static final Key<Integer> CONTROL_POST_RAW_SENSITIVITY_BOOST;
  @PublicKey
  public static final Key<Integer> CONTROL_SCENE_MODE;
  @PublicKey
  public static final Key<Integer> CONTROL_VIDEO_STABILIZATION_MODE;
  @PublicKey
  public static final Key<Integer> DISTORTION_CORRECTION_MODE = new Key("android.distortionCorrection.mode", Integer.TYPE);
  @PublicKey
  public static final Key<Integer> EDGE_MODE;
  @PublicKey
  public static final Key<Integer> FLASH_MODE;
  @PublicKey
  public static final Key<Integer> FLASH_STATE;
  @PublicKey
  public static final Key<Integer> HOT_PIXEL_MODE;
  public static final Key<double[]> JPEG_GPS_COORDINATES;
  @PublicKey
  @SyntheticKey
  public static final Key<Location> JPEG_GPS_LOCATION;
  public static final Key<String> JPEG_GPS_PROCESSING_METHOD;
  public static final Key<Long> JPEG_GPS_TIMESTAMP;
  @PublicKey
  public static final Key<Integer> JPEG_ORIENTATION;
  @PublicKey
  public static final Key<Byte> JPEG_QUALITY;
  @PublicKey
  public static final Key<Byte> JPEG_THUMBNAIL_QUALITY;
  @PublicKey
  public static final Key<Size> JPEG_THUMBNAIL_SIZE;
  public static final Key<Boolean> LED_TRANSMIT;
  @PublicKey
  public static final Key<Float> LENS_APERTURE;
  @PublicKey
  public static final Key<float[]> LENS_DISTORTION;
  @PublicKey
  public static final Key<Float> LENS_FILTER_DENSITY;
  @PublicKey
  public static final Key<Float> LENS_FOCAL_LENGTH;
  @PublicKey
  public static final Key<Float> LENS_FOCUS_DISTANCE;
  @PublicKey
  public static final Key<Pair<Float, Float>> LENS_FOCUS_RANGE;
  @PublicKey
  public static final Key<float[]> LENS_INTRINSIC_CALIBRATION;
  @PublicKey
  public static final Key<Integer> LENS_OPTICAL_STABILIZATION_MODE;
  @PublicKey
  public static final Key<float[]> LENS_POSE_ROTATION;
  @PublicKey
  public static final Key<float[]> LENS_POSE_TRANSLATION;
  @PublicKey
  @Deprecated
  public static final Key<float[]> LENS_RADIAL_DISTORTION;
  @PublicKey
  public static final Key<Integer> LENS_STATE;
  @PublicKey
  public static final Key<Integer> NOISE_REDUCTION_MODE;
  @Deprecated
  public static final Key<Boolean> QUIRKS_PARTIAL_RESULT;
  @PublicKey
  public static final Key<Float> REPROCESS_EFFECTIVE_EXPOSURE_FACTOR;
  @Deprecated
  public static final Key<Integer> REQUEST_FRAME_COUNT;
  public static final Key<Integer> REQUEST_ID;
  @PublicKey
  public static final Key<Byte> REQUEST_PIPELINE_DEPTH;
  @PublicKey
  public static final Key<Rect> SCALER_CROP_REGION;
  @PublicKey
  public static final Key<float[]> SENSOR_DYNAMIC_BLACK_LEVEL;
  @PublicKey
  public static final Key<Integer> SENSOR_DYNAMIC_WHITE_LEVEL;
  @PublicKey
  public static final Key<Long> SENSOR_EXPOSURE_TIME;
  @PublicKey
  public static final Key<Long> SENSOR_FRAME_DURATION;
  @PublicKey
  public static final Key<Float> SENSOR_GREEN_SPLIT;
  @PublicKey
  public static final Key<Rational[]> SENSOR_NEUTRAL_COLOR_POINT;
  @PublicKey
  public static final Key<Pair<Double, Double>[]> SENSOR_NOISE_PROFILE;
  @PublicKey
  public static final Key<Long> SENSOR_ROLLING_SHUTTER_SKEW;
  @PublicKey
  public static final Key<Integer> SENSOR_SENSITIVITY;
  @PublicKey
  public static final Key<int[]> SENSOR_TEST_PATTERN_DATA;
  @PublicKey
  public static final Key<Integer> SENSOR_TEST_PATTERN_MODE;
  @PublicKey
  public static final Key<Long> SENSOR_TIMESTAMP;
  @PublicKey
  public static final Key<Integer> SHADING_MODE;
  @PublicKey
  @SyntheticKey
  public static final Key<Face[]> STATISTICS_FACES;
  @PublicKey
  public static final Key<Integer> STATISTICS_FACE_DETECT_MODE;
  public static final Key<int[]> STATISTICS_FACE_IDS;
  public static final Key<int[]> STATISTICS_FACE_LANDMARKS;
  public static final Key<Rect[]> STATISTICS_FACE_RECTANGLES;
  public static final Key<byte[]> STATISTICS_FACE_SCORES;
  @PublicKey
  public static final Key<Point[]> STATISTICS_HOT_PIXEL_MAP;
  @PublicKey
  public static final Key<Boolean> STATISTICS_HOT_PIXEL_MAP_MODE;
  @PublicKey
  public static final Key<LensShadingMap> STATISTICS_LENS_SHADING_CORRECTION_MAP;
  public static final Key<float[]> STATISTICS_LENS_SHADING_MAP;
  @PublicKey
  public static final Key<Integer> STATISTICS_LENS_SHADING_MAP_MODE;
  @PublicKey
  public static final Key<Integer> STATISTICS_OIS_DATA_MODE;
  @PublicKey
  @SyntheticKey
  public static final Key<OisSample[]> STATISTICS_OIS_SAMPLES;
  public static final Key<long[]> STATISTICS_OIS_TIMESTAMPS;
  public static final Key<float[]> STATISTICS_OIS_X_SHIFTS;
  public static final Key<float[]> STATISTICS_OIS_Y_SHIFTS;
  @Deprecated
  public static final Key<float[]> STATISTICS_PREDICTED_COLOR_GAINS;
  @Deprecated
  public static final Key<Rational[]> STATISTICS_PREDICTED_COLOR_TRANSFORM;
  @PublicKey
  public static final Key<Integer> STATISTICS_SCENE_FLICKER;
  public static final Key<Long> SYNC_FRAME_NUMBER;
  private static final String TAG = "CaptureResult";
  @PublicKey
  @SyntheticKey
  public static final Key<TonemapCurve> TONEMAP_CURVE;
  public static final Key<float[]> TONEMAP_CURVE_BLUE;
  public static final Key<float[]> TONEMAP_CURVE_GREEN;
  public static final Key<float[]> TONEMAP_CURVE_RED;
  @PublicKey
  public static final Key<Float> TONEMAP_GAMMA;
  @PublicKey
  public static final Key<Integer> TONEMAP_MODE;
  @PublicKey
  public static final Key<Integer> TONEMAP_PRESET_CURVE;
  private static final boolean VERBOSE = false;
  private final long mFrameNumber;
  private final CaptureRequest mRequest;
  private final CameraMetadataNative mResults;
  private final int mSequenceId;
  
  static
  {
    COLOR_CORRECTION_GAINS = new Key("android.colorCorrection.gains", RggbChannelVector.class);
    COLOR_CORRECTION_ABERRATION_MODE = new Key("android.colorCorrection.aberrationMode", Integer.TYPE);
    CONTROL_AE_ANTIBANDING_MODE = new Key("android.control.aeAntibandingMode", Integer.TYPE);
    CONTROL_AE_EXPOSURE_COMPENSATION = new Key("android.control.aeExposureCompensation", Integer.TYPE);
    CONTROL_AE_LOCK = new Key("android.control.aeLock", Boolean.TYPE);
    CONTROL_AE_MODE = new Key("android.control.aeMode", Integer.TYPE);
    CONTROL_AE_REGIONS = new Key("android.control.aeRegions", [Landroid.hardware.camera2.params.MeteringRectangle.class);
    CONTROL_AE_TARGET_FPS_RANGE = new Key("android.control.aeTargetFpsRange", new TypeReference() {});
    CONTROL_AE_PRECAPTURE_TRIGGER = new Key("android.control.aePrecaptureTrigger", Integer.TYPE);
    CONTROL_AE_STATE = new Key("android.control.aeState", Integer.TYPE);
    CONTROL_AF_MODE = new Key("android.control.afMode", Integer.TYPE);
    CONTROL_AF_REGIONS = new Key("android.control.afRegions", [Landroid.hardware.camera2.params.MeteringRectangle.class);
    CONTROL_AF_TRIGGER = new Key("android.control.afTrigger", Integer.TYPE);
    CONTROL_AF_STATE = new Key("android.control.afState", Integer.TYPE);
    CONTROL_AWB_LOCK = new Key("android.control.awbLock", Boolean.TYPE);
    CONTROL_AWB_MODE = new Key("android.control.awbMode", Integer.TYPE);
    CONTROL_AWB_REGIONS = new Key("android.control.awbRegions", [Landroid.hardware.camera2.params.MeteringRectangle.class);
    CONTROL_CAPTURE_INTENT = new Key("android.control.captureIntent", Integer.TYPE);
    CONTROL_AWB_STATE = new Key("android.control.awbState", Integer.TYPE);
    CONTROL_EFFECT_MODE = new Key("android.control.effectMode", Integer.TYPE);
    CONTROL_MODE = new Key("android.control.mode", Integer.TYPE);
    CONTROL_SCENE_MODE = new Key("android.control.sceneMode", Integer.TYPE);
    CONTROL_VIDEO_STABILIZATION_MODE = new Key("android.control.videoStabilizationMode", Integer.TYPE);
    CONTROL_POST_RAW_SENSITIVITY_BOOST = new Key("android.control.postRawSensitivityBoost", Integer.TYPE);
    CONTROL_ENABLE_ZSL = new Key("android.control.enableZsl", Boolean.TYPE);
    CONTROL_AF_SCENE_CHANGE = new Key("android.control.afSceneChange", Integer.TYPE);
    EDGE_MODE = new Key("android.edge.mode", Integer.TYPE);
    FLASH_MODE = new Key("android.flash.mode", Integer.TYPE);
    FLASH_STATE = new Key("android.flash.state", Integer.TYPE);
    HOT_PIXEL_MODE = new Key("android.hotPixel.mode", Integer.TYPE);
    JPEG_GPS_LOCATION = new Key("android.jpeg.gpsLocation", Location.class);
    JPEG_GPS_COORDINATES = new Key("android.jpeg.gpsCoordinates", [D.class);
    JPEG_GPS_PROCESSING_METHOD = new Key("android.jpeg.gpsProcessingMethod", String.class);
    JPEG_GPS_TIMESTAMP = new Key("android.jpeg.gpsTimestamp", Long.TYPE);
    JPEG_ORIENTATION = new Key("android.jpeg.orientation", Integer.TYPE);
    JPEG_QUALITY = new Key("android.jpeg.quality", Byte.TYPE);
    JPEG_THUMBNAIL_QUALITY = new Key("android.jpeg.thumbnailQuality", Byte.TYPE);
    JPEG_THUMBNAIL_SIZE = new Key("android.jpeg.thumbnailSize", Size.class);
    LENS_APERTURE = new Key("android.lens.aperture", Float.TYPE);
    LENS_FILTER_DENSITY = new Key("android.lens.filterDensity", Float.TYPE);
    LENS_FOCAL_LENGTH = new Key("android.lens.focalLength", Float.TYPE);
    LENS_FOCUS_DISTANCE = new Key("android.lens.focusDistance", Float.TYPE);
    LENS_FOCUS_RANGE = new Key("android.lens.focusRange", new TypeReference() {});
    LENS_OPTICAL_STABILIZATION_MODE = new Key("android.lens.opticalStabilizationMode", Integer.TYPE);
    LENS_STATE = new Key("android.lens.state", Integer.TYPE);
    LENS_POSE_ROTATION = new Key("android.lens.poseRotation", [F.class);
    LENS_POSE_TRANSLATION = new Key("android.lens.poseTranslation", [F.class);
    LENS_INTRINSIC_CALIBRATION = new Key("android.lens.intrinsicCalibration", [F.class);
    LENS_RADIAL_DISTORTION = new Key("android.lens.radialDistortion", [F.class);
    LENS_DISTORTION = new Key("android.lens.distortion", [F.class);
    NOISE_REDUCTION_MODE = new Key("android.noiseReduction.mode", Integer.TYPE);
    QUIRKS_PARTIAL_RESULT = new Key("android.quirks.partialResult", Boolean.TYPE);
    REQUEST_FRAME_COUNT = new Key("android.request.frameCount", Integer.TYPE);
    REQUEST_ID = new Key("android.request.id", Integer.TYPE);
    REQUEST_PIPELINE_DEPTH = new Key("android.request.pipelineDepth", Byte.TYPE);
    SCALER_CROP_REGION = new Key("android.scaler.cropRegion", Rect.class);
    SENSOR_EXPOSURE_TIME = new Key("android.sensor.exposureTime", Long.TYPE);
    SENSOR_FRAME_DURATION = new Key("android.sensor.frameDuration", Long.TYPE);
    SENSOR_SENSITIVITY = new Key("android.sensor.sensitivity", Integer.TYPE);
    SENSOR_TIMESTAMP = new Key("android.sensor.timestamp", Long.TYPE);
    SENSOR_NEUTRAL_COLOR_POINT = new Key("android.sensor.neutralColorPoint", [Landroid.util.Rational.class);
    SENSOR_NOISE_PROFILE = new Key("android.sensor.noiseProfile", new TypeReference() {});
    SENSOR_GREEN_SPLIT = new Key("android.sensor.greenSplit", Float.TYPE);
    SENSOR_TEST_PATTERN_DATA = new Key("android.sensor.testPatternData", [I.class);
    SENSOR_TEST_PATTERN_MODE = new Key("android.sensor.testPatternMode", Integer.TYPE);
    SENSOR_ROLLING_SHUTTER_SKEW = new Key("android.sensor.rollingShutterSkew", Long.TYPE);
    SENSOR_DYNAMIC_BLACK_LEVEL = new Key("android.sensor.dynamicBlackLevel", [F.class);
    SENSOR_DYNAMIC_WHITE_LEVEL = new Key("android.sensor.dynamicWhiteLevel", Integer.TYPE);
    SHADING_MODE = new Key("android.shading.mode", Integer.TYPE);
    STATISTICS_FACE_DETECT_MODE = new Key("android.statistics.faceDetectMode", Integer.TYPE);
    STATISTICS_FACE_IDS = new Key("android.statistics.faceIds", [I.class);
    STATISTICS_FACE_LANDMARKS = new Key("android.statistics.faceLandmarks", [I.class);
    STATISTICS_FACE_RECTANGLES = new Key("android.statistics.faceRectangles", [Landroid.graphics.Rect.class);
    STATISTICS_FACE_SCORES = new Key("android.statistics.faceScores", [B.class);
    STATISTICS_FACES = new Key("android.statistics.faces", [Landroid.hardware.camera2.params.Face.class);
    STATISTICS_LENS_SHADING_CORRECTION_MAP = new Key("android.statistics.lensShadingCorrectionMap", LensShadingMap.class);
    STATISTICS_LENS_SHADING_MAP = new Key("android.statistics.lensShadingMap", [F.class);
    STATISTICS_PREDICTED_COLOR_GAINS = new Key("android.statistics.predictedColorGains", [F.class);
    STATISTICS_PREDICTED_COLOR_TRANSFORM = new Key("android.statistics.predictedColorTransform", [Landroid.util.Rational.class);
    STATISTICS_SCENE_FLICKER = new Key("android.statistics.sceneFlicker", Integer.TYPE);
    STATISTICS_HOT_PIXEL_MAP_MODE = new Key("android.statistics.hotPixelMapMode", Boolean.TYPE);
    STATISTICS_HOT_PIXEL_MAP = new Key("android.statistics.hotPixelMap", [Landroid.graphics.Point.class);
    STATISTICS_LENS_SHADING_MAP_MODE = new Key("android.statistics.lensShadingMapMode", Integer.TYPE);
    STATISTICS_OIS_DATA_MODE = new Key("android.statistics.oisDataMode", Integer.TYPE);
    STATISTICS_OIS_TIMESTAMPS = new Key("android.statistics.oisTimestamps", [J.class);
    STATISTICS_OIS_X_SHIFTS = new Key("android.statistics.oisXShifts", [F.class);
    STATISTICS_OIS_Y_SHIFTS = new Key("android.statistics.oisYShifts", [F.class);
    STATISTICS_OIS_SAMPLES = new Key("android.statistics.oisSamples", [Landroid.hardware.camera2.params.OisSample.class);
    TONEMAP_CURVE_BLUE = new Key("android.tonemap.curveBlue", [F.class);
    TONEMAP_CURVE_GREEN = new Key("android.tonemap.curveGreen", [F.class);
    TONEMAP_CURVE_RED = new Key("android.tonemap.curveRed", [F.class);
    TONEMAP_CURVE = new Key("android.tonemap.curve", TonemapCurve.class);
    TONEMAP_MODE = new Key("android.tonemap.mode", Integer.TYPE);
    TONEMAP_GAMMA = new Key("android.tonemap.gamma", Float.TYPE);
    TONEMAP_PRESET_CURVE = new Key("android.tonemap.presetCurve", Integer.TYPE);
    LED_TRANSMIT = new Key("android.led.transmit", Boolean.TYPE);
    BLACK_LEVEL_LOCK = new Key("android.blackLevel.lock", Boolean.TYPE);
    SYNC_FRAME_NUMBER = new Key("android.sync.frameNumber", Long.TYPE);
    REPROCESS_EFFECTIVE_EXPOSURE_FACTOR = new Key("android.reprocess.effectiveExposureFactor", Float.TYPE);
  }
  
  public CaptureResult(CameraMetadataNative paramCameraMetadataNative, int paramInt)
  {
    if (paramCameraMetadataNative != null)
    {
      mResults = CameraMetadataNative.move(paramCameraMetadataNative);
      if (!mResults.isEmpty())
      {
        setNativeInstance(mResults);
        mRequest = null;
        mSequenceId = paramInt;
        mFrameNumber = -1L;
        return;
      }
      throw new AssertionError("Results must not be empty");
    }
    throw new IllegalArgumentException("results was null");
  }
  
  public CaptureResult(CameraMetadataNative paramCameraMetadataNative, CaptureRequest paramCaptureRequest, CaptureResultExtras paramCaptureResultExtras)
  {
    if (paramCameraMetadataNative != null)
    {
      if (paramCaptureRequest != null)
      {
        if (paramCaptureResultExtras != null)
        {
          mResults = CameraMetadataNative.move(paramCameraMetadataNative);
          if (!mResults.isEmpty())
          {
            setNativeInstance(mResults);
            mRequest = paramCaptureRequest;
            mSequenceId = paramCaptureResultExtras.getRequestId();
            mFrameNumber = paramCaptureResultExtras.getFrameNumber();
            return;
          }
          throw new AssertionError("Results must not be empty");
        }
        throw new IllegalArgumentException("extras was null");
      }
      throw new IllegalArgumentException("parent was null");
    }
    throw new IllegalArgumentException("results was null");
  }
  
  public void dumpToLog()
  {
    mResults.dumpToLog();
  }
  
  public <T> T get(Key<T> paramKey)
  {
    return mResults.get(paramKey);
  }
  
  public long getFrameNumber()
  {
    return mFrameNumber;
  }
  
  protected Class<Key<?>> getKeyClass()
  {
    return (Class)Key.class;
  }
  
  public List<Key<?>> getKeys()
  {
    return super.getKeys();
  }
  
  public CameraMetadataNative getNativeCopy()
  {
    return new CameraMetadataNative(mResults);
  }
  
  protected <T> T getProtected(Key<?> paramKey)
  {
    return mResults.get(paramKey);
  }
  
  public CaptureRequest getRequest()
  {
    return mRequest;
  }
  
  public int getSequenceId()
  {
    return mSequenceId;
  }
  
  public static final class Key<T>
  {
    private final CameraMetadataNative.Key<T> mKey;
    
    Key(CameraMetadataNative.Key<?> paramKey)
    {
      mKey = paramKey;
    }
    
    public Key(String paramString, TypeReference<T> paramTypeReference)
    {
      mKey = new CameraMetadataNative.Key(paramString, paramTypeReference);
    }
    
    public Key(String paramString, Class<T> paramClass)
    {
      mKey = new CameraMetadataNative.Key(paramString, paramClass);
    }
    
    public Key(String paramString, Class<T> paramClass, long paramLong)
    {
      mKey = new CameraMetadataNative.Key(paramString, paramClass, paramLong);
    }
    
    public Key(String paramString1, String paramString2, Class<T> paramClass)
    {
      mKey = new CameraMetadataNative.Key(paramString1, paramString2, paramClass);
    }
    
    public final boolean equals(Object paramObject)
    {
      boolean bool;
      if (((paramObject instanceof Key)) && (mKey.equals(mKey))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String getName()
    {
      return mKey.getName();
    }
    
    public CameraMetadataNative.Key<T> getNativeKey()
    {
      return mKey;
    }
    
    public long getVendorId()
    {
      return mKey.getVendorId();
    }
    
    public final int hashCode()
    {
      return mKey.hashCode();
    }
    
    public String toString()
    {
      return String.format("CaptureResult.Key(%s)", new Object[] { mKey.getName() });
    }
  }
}
