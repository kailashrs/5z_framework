package android.hardware.camera2;

import android.graphics.Rect;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CameraMetadataNative.Key;
import android.hardware.camera2.impl.PublicKey;
import android.hardware.camera2.impl.SyntheticKey;
import android.hardware.camera2.params.BlackLevelPattern;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.ArrayUtils;
import android.hardware.camera2.utils.TypeReference;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.util.SizeF;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CameraCharacteristics
  extends CameraMetadata<Key<?>>
{
  @PublicKey
  public static final Key<int[]> COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES = new Key("android.colorCorrection.availableAberrationModes", [I.class);
  @PublicKey
  public static final Key<int[]> CONTROL_AE_AVAILABLE_ANTIBANDING_MODES = new Key("android.control.aeAvailableAntibandingModes", [I.class);
  @PublicKey
  public static final Key<int[]> CONTROL_AE_AVAILABLE_MODES = new Key("android.control.aeAvailableModes", [I.class);
  @PublicKey
  public static final Key<Range<Integer>[]> CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES = new Key("android.control.aeAvailableTargetFpsRanges", new TypeReference() {});
  @PublicKey
  public static final Key<Range<Integer>> CONTROL_AE_COMPENSATION_RANGE = new Key("android.control.aeCompensationRange", new TypeReference() {});
  @PublicKey
  public static final Key<Rational> CONTROL_AE_COMPENSATION_STEP = new Key("android.control.aeCompensationStep", Rational.class);
  @PublicKey
  public static final Key<Boolean> CONTROL_AE_LOCK_AVAILABLE;
  @PublicKey
  public static final Key<int[]> CONTROL_AF_AVAILABLE_MODES = new Key("android.control.afAvailableModes", [I.class);
  @PublicKey
  public static final Key<int[]> CONTROL_AVAILABLE_EFFECTS = new Key("android.control.availableEffects", [I.class);
  public static final Key<HighSpeedVideoConfiguration[]> CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS;
  @PublicKey
  public static final Key<int[]> CONTROL_AVAILABLE_MODES;
  @PublicKey
  public static final Key<int[]> CONTROL_AVAILABLE_SCENE_MODES = new Key("android.control.availableSceneModes", [I.class);
  @PublicKey
  public static final Key<int[]> CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES = new Key("android.control.availableVideoStabilizationModes", [I.class);
  @PublicKey
  public static final Key<int[]> CONTROL_AWB_AVAILABLE_MODES = new Key("android.control.awbAvailableModes", [I.class);
  @PublicKey
  public static final Key<Boolean> CONTROL_AWB_LOCK_AVAILABLE;
  public static final Key<int[]> CONTROL_MAX_REGIONS = new Key("android.control.maxRegions", [I.class);
  @PublicKey
  @SyntheticKey
  public static final Key<Integer> CONTROL_MAX_REGIONS_AE = new Key("android.control.maxRegionsAe", Integer.TYPE);
  @PublicKey
  @SyntheticKey
  public static final Key<Integer> CONTROL_MAX_REGIONS_AF;
  @PublicKey
  @SyntheticKey
  public static final Key<Integer> CONTROL_MAX_REGIONS_AWB = new Key("android.control.maxRegionsAwb", Integer.TYPE);
  @PublicKey
  public static final Key<Range<Integer>> CONTROL_POST_RAW_SENSITIVITY_BOOST_RANGE;
  public static final Key<StreamConfigurationDuration[]> DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS;
  public static final Key<StreamConfigurationDuration[]> DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS;
  public static final Key<StreamConfiguration[]> DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS;
  @PublicKey
  public static final Key<Boolean> DEPTH_DEPTH_IS_EXCLUSIVE;
  @PublicKey
  public static final Key<int[]> DISTORTION_CORRECTION_AVAILABLE_MODES = new Key("android.distortionCorrection.availableModes", [I.class);
  @PublicKey
  public static final Key<int[]> EDGE_AVAILABLE_EDGE_MODES;
  @PublicKey
  public static final Key<Boolean> FLASH_INFO_AVAILABLE;
  @PublicKey
  public static final Key<int[]> HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES;
  @PublicKey
  public static final Key<Integer> INFO_SUPPORTED_HARDWARE_LEVEL;
  @PublicKey
  public static final Key<String> INFO_VERSION;
  @PublicKey
  public static final Key<Size[]> JPEG_AVAILABLE_THUMBNAIL_SIZES;
  public static final Key<int[]> LED_AVAILABLE_LEDS;
  @PublicKey
  public static final Key<float[]> LENS_DISTORTION;
  @PublicKey
  public static final Key<Integer> LENS_FACING;
  @PublicKey
  public static final Key<float[]> LENS_INFO_AVAILABLE_APERTURES;
  @PublicKey
  public static final Key<float[]> LENS_INFO_AVAILABLE_FILTER_DENSITIES;
  @PublicKey
  public static final Key<float[]> LENS_INFO_AVAILABLE_FOCAL_LENGTHS;
  @PublicKey
  public static final Key<int[]> LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION;
  @PublicKey
  public static final Key<Integer> LENS_INFO_FOCUS_DISTANCE_CALIBRATION;
  @PublicKey
  public static final Key<Float> LENS_INFO_HYPERFOCAL_DISTANCE;
  @PublicKey
  public static final Key<Float> LENS_INFO_MINIMUM_FOCUS_DISTANCE;
  public static final Key<Size> LENS_INFO_SHADING_MAP_SIZE;
  @PublicKey
  public static final Key<float[]> LENS_INTRINSIC_CALIBRATION;
  @PublicKey
  public static final Key<Integer> LENS_POSE_REFERENCE;
  @PublicKey
  public static final Key<float[]> LENS_POSE_ROTATION;
  @PublicKey
  public static final Key<float[]> LENS_POSE_TRANSLATION;
  @PublicKey
  @Deprecated
  public static final Key<float[]> LENS_RADIAL_DISTORTION;
  public static final Key<byte[]> LOGICAL_MULTI_CAMERA_PHYSICAL_IDS;
  @PublicKey
  public static final Key<Integer> LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE;
  @PublicKey
  public static final Key<int[]> NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES;
  @Deprecated
  public static final Key<Byte> QUIRKS_USE_PARTIAL_RESULT;
  @PublicKey
  public static final Key<Integer> REPROCESS_MAX_CAPTURE_STALL;
  @PublicKey
  public static final Key<int[]> REQUEST_AVAILABLE_CAPABILITIES;
  public static final Key<int[]> REQUEST_AVAILABLE_CHARACTERISTICS_KEYS;
  public static final Key<int[]> REQUEST_AVAILABLE_PHYSICAL_CAMERA_REQUEST_KEYS;
  public static final Key<int[]> REQUEST_AVAILABLE_REQUEST_KEYS;
  public static final Key<int[]> REQUEST_AVAILABLE_RESULT_KEYS;
  public static final Key<int[]> REQUEST_AVAILABLE_SESSION_KEYS;
  @PublicKey
  public static final Key<Integer> REQUEST_MAX_NUM_INPUT_STREAMS;
  @PublicKey
  @SyntheticKey
  public static final Key<Integer> REQUEST_MAX_NUM_OUTPUT_PROC;
  @PublicKey
  @SyntheticKey
  public static final Key<Integer> REQUEST_MAX_NUM_OUTPUT_PROC_STALLING;
  @PublicKey
  @SyntheticKey
  public static final Key<Integer> REQUEST_MAX_NUM_OUTPUT_RAW;
  public static final Key<int[]> REQUEST_MAX_NUM_OUTPUT_STREAMS;
  @PublicKey
  public static final Key<Integer> REQUEST_PARTIAL_RESULT_COUNT;
  @PublicKey
  public static final Key<Byte> REQUEST_PIPELINE_MAX_DEPTH;
  @Deprecated
  public static final Key<int[]> SCALER_AVAILABLE_FORMATS;
  public static final Key<ReprocessFormatsMap> SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP;
  @Deprecated
  public static final Key<long[]> SCALER_AVAILABLE_JPEG_MIN_DURATIONS;
  @Deprecated
  public static final Key<Size[]> SCALER_AVAILABLE_JPEG_SIZES;
  @PublicKey
  public static final Key<Float> SCALER_AVAILABLE_MAX_DIGITAL_ZOOM;
  public static final Key<StreamConfigurationDuration[]> SCALER_AVAILABLE_MIN_FRAME_DURATIONS;
  @Deprecated
  public static final Key<long[]> SCALER_AVAILABLE_PROCESSED_MIN_DURATIONS;
  @Deprecated
  public static final Key<Size[]> SCALER_AVAILABLE_PROCESSED_SIZES;
  public static final Key<StreamConfigurationDuration[]> SCALER_AVAILABLE_STALL_DURATIONS;
  public static final Key<StreamConfiguration[]> SCALER_AVAILABLE_STREAM_CONFIGURATIONS;
  @PublicKey
  public static final Key<Integer> SCALER_CROPPING_TYPE;
  @PublicKey
  @SyntheticKey
  public static final Key<StreamConfigurationMap> SCALER_STREAM_CONFIGURATION_MAP;
  @PublicKey
  public static final Key<int[]> SENSOR_AVAILABLE_TEST_PATTERN_MODES;
  @PublicKey
  public static final Key<BlackLevelPattern> SENSOR_BLACK_LEVEL_PATTERN;
  @PublicKey
  public static final Key<ColorSpaceTransform> SENSOR_CALIBRATION_TRANSFORM1;
  @PublicKey
  public static final Key<ColorSpaceTransform> SENSOR_CALIBRATION_TRANSFORM2;
  @PublicKey
  public static final Key<ColorSpaceTransform> SENSOR_COLOR_TRANSFORM1;
  @PublicKey
  public static final Key<ColorSpaceTransform> SENSOR_COLOR_TRANSFORM2;
  @PublicKey
  public static final Key<ColorSpaceTransform> SENSOR_FORWARD_MATRIX1;
  @PublicKey
  public static final Key<ColorSpaceTransform> SENSOR_FORWARD_MATRIX2;
  @PublicKey
  public static final Key<Rect> SENSOR_INFO_ACTIVE_ARRAY_SIZE;
  @PublicKey
  public static final Key<Integer> SENSOR_INFO_COLOR_FILTER_ARRANGEMENT;
  @PublicKey
  public static final Key<Range<Long>> SENSOR_INFO_EXPOSURE_TIME_RANGE;
  @PublicKey
  public static final Key<Boolean> SENSOR_INFO_LENS_SHADING_APPLIED;
  @PublicKey
  public static final Key<Long> SENSOR_INFO_MAX_FRAME_DURATION;
  @PublicKey
  public static final Key<SizeF> SENSOR_INFO_PHYSICAL_SIZE;
  @PublicKey
  public static final Key<Size> SENSOR_INFO_PIXEL_ARRAY_SIZE;
  @PublicKey
  public static final Key<Rect> SENSOR_INFO_PRE_CORRECTION_ACTIVE_ARRAY_SIZE;
  @PublicKey
  public static final Key<Range<Integer>> SENSOR_INFO_SENSITIVITY_RANGE;
  @PublicKey
  public static final Key<Integer> SENSOR_INFO_TIMESTAMP_SOURCE;
  @PublicKey
  public static final Key<Integer> SENSOR_INFO_WHITE_LEVEL;
  @PublicKey
  public static final Key<Integer> SENSOR_MAX_ANALOG_SENSITIVITY;
  @PublicKey
  public static final Key<Rect[]> SENSOR_OPTICAL_BLACK_REGIONS;
  @PublicKey
  public static final Key<Integer> SENSOR_ORIENTATION;
  @PublicKey
  public static final Key<Integer> SENSOR_REFERENCE_ILLUMINANT1;
  @PublicKey
  public static final Key<Byte> SENSOR_REFERENCE_ILLUMINANT2;
  @PublicKey
  public static final Key<int[]> SHADING_AVAILABLE_MODES;
  @PublicKey
  public static final Key<int[]> STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES;
  @PublicKey
  public static final Key<boolean[]> STATISTICS_INFO_AVAILABLE_HOT_PIXEL_MAP_MODES;
  @PublicKey
  public static final Key<int[]> STATISTICS_INFO_AVAILABLE_LENS_SHADING_MAP_MODES;
  @PublicKey
  public static final Key<int[]> STATISTICS_INFO_AVAILABLE_OIS_DATA_MODES;
  @PublicKey
  public static final Key<Integer> STATISTICS_INFO_MAX_FACE_COUNT;
  @PublicKey
  public static final Key<Integer> SYNC_MAX_LATENCY;
  @PublicKey
  public static final Key<int[]> TONEMAP_AVAILABLE_TONE_MAP_MODES;
  @PublicKey
  public static final Key<Integer> TONEMAP_MAX_CURVE_POINTS;
  private List<CaptureRequest.Key<?>> mAvailablePhysicalRequestKeys;
  private List<CaptureRequest.Key<?>> mAvailableRequestKeys;
  private List<CaptureResult.Key<?>> mAvailableResultKeys;
  private List<CaptureRequest.Key<?>> mAvailableSessionKeys;
  private List<Key<?>> mKeys;
  private final CameraMetadataNative mProperties;
  
  static
  {
    CONTROL_MAX_REGIONS_AF = new Key("android.control.maxRegionsAf", Integer.TYPE);
    CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS = new Key("android.control.availableHighSpeedVideoConfigurations", [Landroid.hardware.camera2.params.HighSpeedVideoConfiguration.class);
    CONTROL_AE_LOCK_AVAILABLE = new Key("android.control.aeLockAvailable", Boolean.TYPE);
    CONTROL_AWB_LOCK_AVAILABLE = new Key("android.control.awbLockAvailable", Boolean.TYPE);
    CONTROL_AVAILABLE_MODES = new Key("android.control.availableModes", [I.class);
    CONTROL_POST_RAW_SENSITIVITY_BOOST_RANGE = new Key("android.control.postRawSensitivityBoostRange", new TypeReference() {});
    EDGE_AVAILABLE_EDGE_MODES = new Key("android.edge.availableEdgeModes", [I.class);
    FLASH_INFO_AVAILABLE = new Key("android.flash.info.available", Boolean.TYPE);
    HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES = new Key("android.hotPixel.availableHotPixelModes", [I.class);
    JPEG_AVAILABLE_THUMBNAIL_SIZES = new Key("android.jpeg.availableThumbnailSizes", [Landroid.util.Size.class);
    LENS_INFO_AVAILABLE_APERTURES = new Key("android.lens.info.availableApertures", [F.class);
    LENS_INFO_AVAILABLE_FILTER_DENSITIES = new Key("android.lens.info.availableFilterDensities", [F.class);
    LENS_INFO_AVAILABLE_FOCAL_LENGTHS = new Key("android.lens.info.availableFocalLengths", [F.class);
    LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION = new Key("android.lens.info.availableOpticalStabilization", [I.class);
    LENS_INFO_HYPERFOCAL_DISTANCE = new Key("android.lens.info.hyperfocalDistance", Float.TYPE);
    LENS_INFO_MINIMUM_FOCUS_DISTANCE = new Key("android.lens.info.minimumFocusDistance", Float.TYPE);
    LENS_INFO_SHADING_MAP_SIZE = new Key("android.lens.info.shadingMapSize", Size.class);
    LENS_INFO_FOCUS_DISTANCE_CALIBRATION = new Key("android.lens.info.focusDistanceCalibration", Integer.TYPE);
    LENS_FACING = new Key("android.lens.facing", Integer.TYPE);
    LENS_POSE_ROTATION = new Key("android.lens.poseRotation", [F.class);
    LENS_POSE_TRANSLATION = new Key("android.lens.poseTranslation", [F.class);
    LENS_INTRINSIC_CALIBRATION = new Key("android.lens.intrinsicCalibration", [F.class);
    LENS_RADIAL_DISTORTION = new Key("android.lens.radialDistortion", [F.class);
    LENS_POSE_REFERENCE = new Key("android.lens.poseReference", Integer.TYPE);
    LENS_DISTORTION = new Key("android.lens.distortion", [F.class);
    NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES = new Key("android.noiseReduction.availableNoiseReductionModes", [I.class);
    QUIRKS_USE_PARTIAL_RESULT = new Key("android.quirks.usePartialResult", Byte.TYPE);
    REQUEST_MAX_NUM_OUTPUT_STREAMS = new Key("android.request.maxNumOutputStreams", [I.class);
    REQUEST_MAX_NUM_OUTPUT_RAW = new Key("android.request.maxNumOutputRaw", Integer.TYPE);
    REQUEST_MAX_NUM_OUTPUT_PROC = new Key("android.request.maxNumOutputProc", Integer.TYPE);
    REQUEST_MAX_NUM_OUTPUT_PROC_STALLING = new Key("android.request.maxNumOutputProcStalling", Integer.TYPE);
    REQUEST_MAX_NUM_INPUT_STREAMS = new Key("android.request.maxNumInputStreams", Integer.TYPE);
    REQUEST_PIPELINE_MAX_DEPTH = new Key("android.request.pipelineMaxDepth", Byte.TYPE);
    REQUEST_PARTIAL_RESULT_COUNT = new Key("android.request.partialResultCount", Integer.TYPE);
    REQUEST_AVAILABLE_CAPABILITIES = new Key("android.request.availableCapabilities", [I.class);
    REQUEST_AVAILABLE_REQUEST_KEYS = new Key("android.request.availableRequestKeys", [I.class);
    REQUEST_AVAILABLE_RESULT_KEYS = new Key("android.request.availableResultKeys", [I.class);
    REQUEST_AVAILABLE_CHARACTERISTICS_KEYS = new Key("android.request.availableCharacteristicsKeys", [I.class);
    REQUEST_AVAILABLE_SESSION_KEYS = new Key("android.request.availableSessionKeys", [I.class);
    REQUEST_AVAILABLE_PHYSICAL_CAMERA_REQUEST_KEYS = new Key("android.request.availablePhysicalCameraRequestKeys", [I.class);
    SCALER_AVAILABLE_FORMATS = new Key("android.scaler.availableFormats", [I.class);
    SCALER_AVAILABLE_JPEG_MIN_DURATIONS = new Key("android.scaler.availableJpegMinDurations", [J.class);
    SCALER_AVAILABLE_JPEG_SIZES = new Key("android.scaler.availableJpegSizes", [Landroid.util.Size.class);
    SCALER_AVAILABLE_MAX_DIGITAL_ZOOM = new Key("android.scaler.availableMaxDigitalZoom", Float.TYPE);
    SCALER_AVAILABLE_PROCESSED_MIN_DURATIONS = new Key("android.scaler.availableProcessedMinDurations", [J.class);
    SCALER_AVAILABLE_PROCESSED_SIZES = new Key("android.scaler.availableProcessedSizes", [Landroid.util.Size.class);
    SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP = new Key("android.scaler.availableInputOutputFormatsMap", ReprocessFormatsMap.class);
    SCALER_AVAILABLE_STREAM_CONFIGURATIONS = new Key("android.scaler.availableStreamConfigurations", [Landroid.hardware.camera2.params.StreamConfiguration.class);
    SCALER_AVAILABLE_MIN_FRAME_DURATIONS = new Key("android.scaler.availableMinFrameDurations", [Landroid.hardware.camera2.params.StreamConfigurationDuration.class);
    SCALER_AVAILABLE_STALL_DURATIONS = new Key("android.scaler.availableStallDurations", [Landroid.hardware.camera2.params.StreamConfigurationDuration.class);
    SCALER_STREAM_CONFIGURATION_MAP = new Key("android.scaler.streamConfigurationMap", StreamConfigurationMap.class);
    SCALER_CROPPING_TYPE = new Key("android.scaler.croppingType", Integer.TYPE);
    SENSOR_INFO_ACTIVE_ARRAY_SIZE = new Key("android.sensor.info.activeArraySize", Rect.class);
    SENSOR_INFO_SENSITIVITY_RANGE = new Key("android.sensor.info.sensitivityRange", new TypeReference() {});
    SENSOR_INFO_COLOR_FILTER_ARRANGEMENT = new Key("android.sensor.info.colorFilterArrangement", Integer.TYPE);
    SENSOR_INFO_EXPOSURE_TIME_RANGE = new Key("android.sensor.info.exposureTimeRange", new TypeReference() {});
    SENSOR_INFO_MAX_FRAME_DURATION = new Key("android.sensor.info.maxFrameDuration", Long.TYPE);
    SENSOR_INFO_PHYSICAL_SIZE = new Key("android.sensor.info.physicalSize", SizeF.class);
    SENSOR_INFO_PIXEL_ARRAY_SIZE = new Key("android.sensor.info.pixelArraySize", Size.class);
    SENSOR_INFO_WHITE_LEVEL = new Key("android.sensor.info.whiteLevel", Integer.TYPE);
    SENSOR_INFO_TIMESTAMP_SOURCE = new Key("android.sensor.info.timestampSource", Integer.TYPE);
    SENSOR_INFO_LENS_SHADING_APPLIED = new Key("android.sensor.info.lensShadingApplied", Boolean.TYPE);
    SENSOR_INFO_PRE_CORRECTION_ACTIVE_ARRAY_SIZE = new Key("android.sensor.info.preCorrectionActiveArraySize", Rect.class);
    SENSOR_REFERENCE_ILLUMINANT1 = new Key("android.sensor.referenceIlluminant1", Integer.TYPE);
    SENSOR_REFERENCE_ILLUMINANT2 = new Key("android.sensor.referenceIlluminant2", Byte.TYPE);
    SENSOR_CALIBRATION_TRANSFORM1 = new Key("android.sensor.calibrationTransform1", ColorSpaceTransform.class);
    SENSOR_CALIBRATION_TRANSFORM2 = new Key("android.sensor.calibrationTransform2", ColorSpaceTransform.class);
    SENSOR_COLOR_TRANSFORM1 = new Key("android.sensor.colorTransform1", ColorSpaceTransform.class);
    SENSOR_COLOR_TRANSFORM2 = new Key("android.sensor.colorTransform2", ColorSpaceTransform.class);
    SENSOR_FORWARD_MATRIX1 = new Key("android.sensor.forwardMatrix1", ColorSpaceTransform.class);
    SENSOR_FORWARD_MATRIX2 = new Key("android.sensor.forwardMatrix2", ColorSpaceTransform.class);
    SENSOR_BLACK_LEVEL_PATTERN = new Key("android.sensor.blackLevelPattern", BlackLevelPattern.class);
    SENSOR_MAX_ANALOG_SENSITIVITY = new Key("android.sensor.maxAnalogSensitivity", Integer.TYPE);
    SENSOR_ORIENTATION = new Key("android.sensor.orientation", Integer.TYPE);
    SENSOR_AVAILABLE_TEST_PATTERN_MODES = new Key("android.sensor.availableTestPatternModes", [I.class);
    SENSOR_OPTICAL_BLACK_REGIONS = new Key("android.sensor.opticalBlackRegions", [Landroid.graphics.Rect.class);
    SHADING_AVAILABLE_MODES = new Key("android.shading.availableModes", [I.class);
    STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES = new Key("android.statistics.info.availableFaceDetectModes", [I.class);
    STATISTICS_INFO_MAX_FACE_COUNT = new Key("android.statistics.info.maxFaceCount", Integer.TYPE);
    STATISTICS_INFO_AVAILABLE_HOT_PIXEL_MAP_MODES = new Key("android.statistics.info.availableHotPixelMapModes", [Z.class);
    STATISTICS_INFO_AVAILABLE_LENS_SHADING_MAP_MODES = new Key("android.statistics.info.availableLensShadingMapModes", [I.class);
    STATISTICS_INFO_AVAILABLE_OIS_DATA_MODES = new Key("android.statistics.info.availableOisDataModes", [I.class);
    TONEMAP_MAX_CURVE_POINTS = new Key("android.tonemap.maxCurvePoints", Integer.TYPE);
    TONEMAP_AVAILABLE_TONE_MAP_MODES = new Key("android.tonemap.availableToneMapModes", [I.class);
    LED_AVAILABLE_LEDS = new Key("android.led.availableLeds", [I.class);
    INFO_SUPPORTED_HARDWARE_LEVEL = new Key("android.info.supportedHardwareLevel", Integer.TYPE);
    INFO_VERSION = new Key("android.info.version", String.class);
    SYNC_MAX_LATENCY = new Key("android.sync.maxLatency", Integer.TYPE);
    REPROCESS_MAX_CAPTURE_STALL = new Key("android.reprocess.maxCaptureStall", Integer.TYPE);
    DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS = new Key("android.depth.availableDepthStreamConfigurations", [Landroid.hardware.camera2.params.StreamConfiguration.class);
    DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS = new Key("android.depth.availableDepthMinFrameDurations", [Landroid.hardware.camera2.params.StreamConfigurationDuration.class);
    DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS = new Key("android.depth.availableDepthStallDurations", [Landroid.hardware.camera2.params.StreamConfigurationDuration.class);
    DEPTH_DEPTH_IS_EXCLUSIVE = new Key("android.depth.depthIsExclusive", Boolean.TYPE);
    LOGICAL_MULTI_CAMERA_PHYSICAL_IDS = new Key("android.logicalMultiCamera.physicalIds", [B.class);
    LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE = new Key("android.logicalMultiCamera.sensorSyncType", Integer.TYPE);
  }
  
  public CameraCharacteristics(CameraMetadataNative paramCameraMetadataNative)
  {
    mProperties = CameraMetadataNative.move(paramCameraMetadataNative);
    setNativeInstance(mProperties);
  }
  
  private <TKey> List<TKey> getAvailableKeyList(Class<?> paramClass, Class<TKey> paramClass1, int[] paramArrayOfInt)
  {
    if (!paramClass.equals(CameraMetadata.class))
    {
      if (CameraMetadata.class.isAssignableFrom(paramClass)) {
        return Collections.unmodifiableList(getKeys(paramClass, paramClass1, null, paramArrayOfInt));
      }
      throw new AssertionError("metadataClass must be a subclass of CameraMetadata");
    }
    throw new AssertionError("metadataClass must be a strict subclass of CameraMetadata");
  }
  
  public <T> T get(Key<T> paramKey)
  {
    return mProperties.get(paramKey);
  }
  
  public List<CaptureRequest.Key<?>> getAvailableCaptureRequestKeys()
  {
    if (mAvailableRequestKeys == null)
    {
      Class localClass = (Class)CaptureRequest.Key.class;
      int[] arrayOfInt = (int[])get(REQUEST_AVAILABLE_REQUEST_KEYS);
      if (arrayOfInt != null) {
        mAvailableRequestKeys = getAvailableKeyList(CaptureRequest.class, localClass, arrayOfInt);
      } else {
        throw new AssertionError("android.request.availableRequestKeys must be non-null in the characteristics");
      }
    }
    return mAvailableRequestKeys;
  }
  
  public List<CaptureResult.Key<?>> getAvailableCaptureResultKeys()
  {
    if (mAvailableResultKeys == null)
    {
      Class localClass = (Class)CaptureResult.Key.class;
      int[] arrayOfInt = (int[])get(REQUEST_AVAILABLE_RESULT_KEYS);
      if (arrayOfInt != null) {
        mAvailableResultKeys = getAvailableKeyList(CaptureResult.class, localClass, arrayOfInt);
      } else {
        throw new AssertionError("android.request.availableResultKeys must be non-null in the characteristics");
      }
    }
    return mAvailableResultKeys;
  }
  
  public List<CaptureRequest.Key<?>> getAvailablePhysicalCameraRequestKeys()
  {
    if (mAvailablePhysicalRequestKeys == null)
    {
      Class localClass = (Class)CaptureRequest.Key.class;
      int[] arrayOfInt = (int[])get(REQUEST_AVAILABLE_PHYSICAL_CAMERA_REQUEST_KEYS);
      if (arrayOfInt == null) {
        return null;
      }
      mAvailablePhysicalRequestKeys = getAvailableKeyList(CaptureRequest.class, localClass, arrayOfInt);
    }
    return mAvailablePhysicalRequestKeys;
  }
  
  public List<CaptureRequest.Key<?>> getAvailableSessionKeys()
  {
    if (mAvailableSessionKeys == null)
    {
      Class localClass = (Class)CaptureRequest.Key.class;
      int[] arrayOfInt = (int[])get(REQUEST_AVAILABLE_SESSION_KEYS);
      if (arrayOfInt == null) {
        return null;
      }
      mAvailableSessionKeys = getAvailableKeyList(CaptureRequest.class, localClass, arrayOfInt);
    }
    return mAvailableSessionKeys;
  }
  
  protected Class<Key<?>> getKeyClass()
  {
    return (Class)Key.class;
  }
  
  public List<Key<?>> getKeys()
  {
    if (mKeys != null) {
      return mKeys;
    }
    int[] arrayOfInt = (int[])get(REQUEST_AVAILABLE_CHARACTERISTICS_KEYS);
    if (arrayOfInt != null)
    {
      mKeys = Collections.unmodifiableList(getKeys(getClass(), getKeyClass(), this, arrayOfInt));
      return mKeys;
    }
    throw new AssertionError("android.request.availableCharacteristicsKeys must be non-null in the characteristics");
  }
  
  public CameraMetadataNative getNativeCopy()
  {
    return new CameraMetadataNative(mProperties);
  }
  
  public Set<String> getPhysicalCameraIds()
  {
    Object localObject = (int[])get(REQUEST_AVAILABLE_CAPABILITIES);
    if (localObject != null)
    {
      if (!ArrayUtils.contains((int[])localObject, 11)) {
        return Collections.emptySet();
      }
      localObject = (byte[])get(LOGICAL_MULTI_CAMERA_PHYSICAL_IDS);
      try
      {
        localObject = new String((byte[])localObject, "UTF-8");
        return Collections.unmodifiableSet(new HashSet(Arrays.asList(((String)localObject).split("\000"))));
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new AssertionError("android.logicalCam.physicalIds must be UTF-8 string");
      }
    }
    throw new AssertionError("android.request.availableCapabilities must be non-null in the characteristics");
  }
  
  protected <T> T getProtected(Key<?> paramKey)
  {
    return mProperties.get(paramKey);
  }
  
  public static final class Key<T>
  {
    private final CameraMetadataNative.Key<T> mKey;
    
    private Key(CameraMetadataNative.Key<?> paramKey)
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
      return String.format("CameraCharacteristics.Key(%s)", new Object[] { mKey.getName() });
    }
  }
}
