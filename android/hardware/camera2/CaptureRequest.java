package android.hardware.camera2;

import android.graphics.Rect;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CameraMetadataNative.Key;
import android.hardware.camera2.impl.PublicKey;
import android.hardware.camera2.impl.SyntheticKey;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.RggbChannelVector;
import android.hardware.camera2.params.TonemapCurve;
import android.hardware.camera2.utils.HashCodeHelpers;
import android.hardware.camera2.utils.SurfaceUtils;
import android.hardware.camera2.utils.TypeReference;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public final class CaptureRequest
  extends CameraMetadata<Key<?>>
  implements Parcelable
{
  @PublicKey
  public static final Key<Boolean> BLACK_LEVEL_LOCK;
  @PublicKey
  public static final Key<Integer> COLOR_CORRECTION_ABERRATION_MODE;
  @PublicKey
  public static final Key<RggbChannelVector> COLOR_CORRECTION_GAINS;
  @PublicKey
  public static final Key<Integer> COLOR_CORRECTION_MODE;
  @PublicKey
  public static final Key<ColorSpaceTransform> COLOR_CORRECTION_TRANSFORM;
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
  public static final Key<Range<Integer>> CONTROL_AE_TARGET_FPS_RANGE;
  @PublicKey
  public static final Key<Integer> CONTROL_AF_MODE;
  @PublicKey
  public static final Key<MeteringRectangle[]> CONTROL_AF_REGIONS;
  @PublicKey
  public static final Key<Integer> CONTROL_AF_TRIGGER;
  @PublicKey
  public static final Key<Boolean> CONTROL_AWB_LOCK;
  @PublicKey
  public static final Key<Integer> CONTROL_AWB_MODE;
  @PublicKey
  public static final Key<MeteringRectangle[]> CONTROL_AWB_REGIONS;
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
  public static final Parcelable.Creator<CaptureRequest> CREATOR;
  @PublicKey
  public static final Key<Integer> DISTORTION_CORRECTION_MODE = new Key("android.distortionCorrection.mode", Integer.TYPE);
  @PublicKey
  public static final Key<Integer> EDGE_MODE;
  @PublicKey
  public static final Key<Integer> FLASH_MODE;
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
  public static final Key<Float> LENS_FILTER_DENSITY;
  @PublicKey
  public static final Key<Float> LENS_FOCAL_LENGTH;
  @PublicKey
  public static final Key<Float> LENS_FOCUS_DISTANCE;
  @PublicKey
  public static final Key<Integer> LENS_OPTICAL_STABILIZATION_MODE;
  @PublicKey
  public static final Key<Integer> NOISE_REDUCTION_MODE;
  @PublicKey
  public static final Key<Float> REPROCESS_EFFECTIVE_EXPOSURE_FACTOR;
  public static final Key<Integer> REQUEST_ID;
  @PublicKey
  public static final Key<Rect> SCALER_CROP_REGION;
  @PublicKey
  public static final Key<Long> SENSOR_EXPOSURE_TIME;
  @PublicKey
  public static final Key<Long> SENSOR_FRAME_DURATION;
  @PublicKey
  public static final Key<Integer> SENSOR_SENSITIVITY;
  @PublicKey
  public static final Key<int[]> SENSOR_TEST_PATTERN_DATA;
  @PublicKey
  public static final Key<Integer> SENSOR_TEST_PATTERN_MODE;
  @PublicKey
  public static final Key<Integer> SHADING_MODE;
  @PublicKey
  public static final Key<Integer> STATISTICS_FACE_DETECT_MODE;
  @PublicKey
  public static final Key<Boolean> STATISTICS_HOT_PIXEL_MAP_MODE;
  @PublicKey
  public static final Key<Integer> STATISTICS_LENS_SHADING_MAP_MODE;
  @PublicKey
  public static final Key<Integer> STATISTICS_OIS_DATA_MODE;
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
  private static final ArraySet<Surface> mEmptySurfaceSet = new ArraySet();
  private final String TAG = "CaptureRequest-JV";
  private boolean mIsPartOfCHSRequestList = false;
  private boolean mIsReprocess;
  private String mLogicalCameraId;
  private CameraMetadataNative mLogicalCameraSettings;
  private final HashMap<String, CameraMetadataNative> mPhysicalCameraSettings = new HashMap();
  private int mReprocessableSessionId;
  private int[] mStreamIdxArray;
  private boolean mSurfaceConverted = false;
  private int[] mSurfaceIdxArray;
  private final ArraySet<Surface> mSurfaceSet = new ArraySet();
  private final Object mSurfacesLock = new Object();
  private Object mUserTag;
  
  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public CaptureRequest createFromParcel(Parcel paramAnonymousParcel)
      {
        CaptureRequest localCaptureRequest = new CaptureRequest(null);
        localCaptureRequest.readFromParcel(paramAnonymousParcel);
        return localCaptureRequest;
      }
      
      public CaptureRequest[] newArray(int paramAnonymousInt)
      {
        return new CaptureRequest[paramAnonymousInt];
      }
    };
    COLOR_CORRECTION_MODE = new Key("android.colorCorrection.mode", Integer.TYPE);
    COLOR_CORRECTION_TRANSFORM = new Key("android.colorCorrection.transform", ColorSpaceTransform.class);
    COLOR_CORRECTION_GAINS = new Key("android.colorCorrection.gains", RggbChannelVector.class);
    COLOR_CORRECTION_ABERRATION_MODE = new Key("android.colorCorrection.aberrationMode", Integer.TYPE);
    CONTROL_AE_ANTIBANDING_MODE = new Key("android.control.aeAntibandingMode", Integer.TYPE);
    CONTROL_AE_EXPOSURE_COMPENSATION = new Key("android.control.aeExposureCompensation", Integer.TYPE);
    CONTROL_AE_LOCK = new Key("android.control.aeLock", Boolean.TYPE);
    CONTROL_AE_MODE = new Key("android.control.aeMode", Integer.TYPE);
    CONTROL_AE_REGIONS = new Key("android.control.aeRegions", [Landroid.hardware.camera2.params.MeteringRectangle.class);
    CONTROL_AE_TARGET_FPS_RANGE = new Key("android.control.aeTargetFpsRange", new TypeReference() {});
    CONTROL_AE_PRECAPTURE_TRIGGER = new Key("android.control.aePrecaptureTrigger", Integer.TYPE);
    CONTROL_AF_MODE = new Key("android.control.afMode", Integer.TYPE);
    CONTROL_AF_REGIONS = new Key("android.control.afRegions", [Landroid.hardware.camera2.params.MeteringRectangle.class);
    CONTROL_AF_TRIGGER = new Key("android.control.afTrigger", Integer.TYPE);
    CONTROL_AWB_LOCK = new Key("android.control.awbLock", Boolean.TYPE);
    CONTROL_AWB_MODE = new Key("android.control.awbMode", Integer.TYPE);
    CONTROL_AWB_REGIONS = new Key("android.control.awbRegions", [Landroid.hardware.camera2.params.MeteringRectangle.class);
    CONTROL_CAPTURE_INTENT = new Key("android.control.captureIntent", Integer.TYPE);
    CONTROL_EFFECT_MODE = new Key("android.control.effectMode", Integer.TYPE);
    CONTROL_MODE = new Key("android.control.mode", Integer.TYPE);
    CONTROL_SCENE_MODE = new Key("android.control.sceneMode", Integer.TYPE);
    CONTROL_VIDEO_STABILIZATION_MODE = new Key("android.control.videoStabilizationMode", Integer.TYPE);
    CONTROL_POST_RAW_SENSITIVITY_BOOST = new Key("android.control.postRawSensitivityBoost", Integer.TYPE);
    CONTROL_ENABLE_ZSL = new Key("android.control.enableZsl", Boolean.TYPE);
    EDGE_MODE = new Key("android.edge.mode", Integer.TYPE);
    FLASH_MODE = new Key("android.flash.mode", Integer.TYPE);
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
    LENS_OPTICAL_STABILIZATION_MODE = new Key("android.lens.opticalStabilizationMode", Integer.TYPE);
    NOISE_REDUCTION_MODE = new Key("android.noiseReduction.mode", Integer.TYPE);
    REQUEST_ID = new Key("android.request.id", Integer.TYPE);
    SCALER_CROP_REGION = new Key("android.scaler.cropRegion", Rect.class);
    SENSOR_EXPOSURE_TIME = new Key("android.sensor.exposureTime", Long.TYPE);
    SENSOR_FRAME_DURATION = new Key("android.sensor.frameDuration", Long.TYPE);
    SENSOR_SENSITIVITY = new Key("android.sensor.sensitivity", Integer.TYPE);
    SENSOR_TEST_PATTERN_DATA = new Key("android.sensor.testPatternData", [I.class);
    SENSOR_TEST_PATTERN_MODE = new Key("android.sensor.testPatternMode", Integer.TYPE);
    SHADING_MODE = new Key("android.shading.mode", Integer.TYPE);
    STATISTICS_FACE_DETECT_MODE = new Key("android.statistics.faceDetectMode", Integer.TYPE);
    STATISTICS_HOT_PIXEL_MAP_MODE = new Key("android.statistics.hotPixelMapMode", Boolean.TYPE);
    STATISTICS_LENS_SHADING_MAP_MODE = new Key("android.statistics.lensShadingMapMode", Integer.TYPE);
    STATISTICS_OIS_DATA_MODE = new Key("android.statistics.oisDataMode", Integer.TYPE);
    TONEMAP_CURVE_BLUE = new Key("android.tonemap.curveBlue", [F.class);
    TONEMAP_CURVE_GREEN = new Key("android.tonemap.curveGreen", [F.class);
    TONEMAP_CURVE_RED = new Key("android.tonemap.curveRed", [F.class);
    TONEMAP_CURVE = new Key("android.tonemap.curve", TonemapCurve.class);
    TONEMAP_MODE = new Key("android.tonemap.mode", Integer.TYPE);
    TONEMAP_GAMMA = new Key("android.tonemap.gamma", Float.TYPE);
    TONEMAP_PRESET_CURVE = new Key("android.tonemap.presetCurve", Integer.TYPE);
    LED_TRANSMIT = new Key("android.led.transmit", Boolean.TYPE);
    BLACK_LEVEL_LOCK = new Key("android.blackLevel.lock", Boolean.TYPE);
    REPROCESS_EFFECTIVE_EXPOSURE_FACTOR = new Key("android.reprocess.effectiveExposureFactor", Float.TYPE);
  }
  
  private CaptureRequest()
  {
    mIsReprocess = false;
    mReprocessableSessionId = -1;
  }
  
  private CaptureRequest(CaptureRequest paramCaptureRequest)
  {
    mLogicalCameraId = new String(mLogicalCameraId);
    Iterator localIterator = mPhysicalCameraSettings.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      mPhysicalCameraSettings.put(new String((String)localEntry.getKey()), new CameraMetadataNative((CameraMetadataNative)localEntry.getValue()));
    }
    mLogicalCameraSettings = ((CameraMetadataNative)mPhysicalCameraSettings.get(mLogicalCameraId));
    setNativeInstance(mLogicalCameraSettings);
    mSurfaceSet.addAll(mSurfaceSet);
    mIsReprocess = mIsReprocess;
    mIsPartOfCHSRequestList = mIsPartOfCHSRequestList;
    mReprocessableSessionId = mReprocessableSessionId;
    mUserTag = mUserTag;
  }
  
  private CaptureRequest(CameraMetadataNative paramCameraMetadataNative, boolean paramBoolean, int paramInt, String paramString, Set<String> paramSet)
  {
    if ((paramSet != null) && (paramBoolean)) {
      throw new IllegalArgumentException("Create a reprocess capture request with with more than one physical camera is not supported!");
    }
    mLogicalCameraId = paramString;
    mLogicalCameraSettings = CameraMetadataNative.move(paramCameraMetadataNative);
    mPhysicalCameraSettings.put(mLogicalCameraId, mLogicalCameraSettings);
    if (paramSet != null)
    {
      paramString = paramSet.iterator();
      while (paramString.hasNext())
      {
        paramCameraMetadataNative = (String)paramString.next();
        mPhysicalCameraSettings.put(paramCameraMetadataNative, new CameraMetadataNative(mLogicalCameraSettings));
      }
    }
    setNativeInstance(mLogicalCameraSettings);
    mIsReprocess = paramBoolean;
    if (paramBoolean)
    {
      if (paramInt != -1)
      {
        mReprocessableSessionId = paramInt;
      }
      else
      {
        paramCameraMetadataNative = new StringBuilder();
        paramCameraMetadataNative.append("Create a reprocess capture request with an invalid session ID: ");
        paramCameraMetadataNative.append(paramInt);
        throw new IllegalArgumentException(paramCameraMetadataNative.toString());
      }
    }
    else {
      mReprocessableSessionId = -1;
    }
  }
  
  private boolean equals(CaptureRequest paramCaptureRequest)
  {
    boolean bool;
    if ((paramCaptureRequest != null) && (Objects.equals(mUserTag, mUserTag)) && (mSurfaceSet.equals(mSurfaceSet)) && (mPhysicalCameraSettings.equals(mPhysicalCameraSettings)) && (mLogicalCameraId.equals(mLogicalCameraId)) && (mLogicalCameraSettings.equals(mLogicalCameraSettings)) && (mIsReprocess == mIsReprocess) && (mReprocessableSessionId == mReprocessableSessionId)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if (i > 0)
    {
      mLogicalCameraId = paramParcel.readString();
      mLogicalCameraSettings = new CameraMetadataNative();
      mLogicalCameraSettings.readFromParcel(paramParcel);
      setNativeInstance(mLogicalCameraSettings);
      mPhysicalCameraSettings.put(mLogicalCameraId, mLogicalCameraSettings);
      boolean bool = true;
      Object localObject1;
      for (int j = 1; j < i; j++)
      {
        localObject1 = paramParcel.readString();
        ??? = new CameraMetadataNative();
        ((CameraMetadataNative)???).readFromParcel(paramParcel);
        mPhysicalCameraSettings.put(localObject1, ???);
      }
      i = paramParcel.readInt();
      j = 0;
      if (i == 0) {
        bool = false;
      }
      mIsReprocess = bool;
      mReprocessableSessionId = -1;
      synchronized (mSurfacesLock)
      {
        mSurfaceSet.clear();
        Parcelable[] arrayOfParcelable = paramParcel.readParcelableArray(Surface.class.getClassLoader());
        if (arrayOfParcelable != null)
        {
          i = arrayOfParcelable.length;
          while (j < i)
          {
            localObject1 = (Surface)arrayOfParcelable[j];
            mSurfaceSet.add(localObject1);
            j++;
          }
        }
        if (paramParcel.readInt() == 0) {
          return;
        }
        paramParcel = new java/lang/RuntimeException;
        paramParcel.<init>("Reading cached CaptureRequest is not supported");
        throw paramParcel;
      }
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Physical camera count");
    paramParcel.append(i);
    paramParcel.append(" should always be positive");
    throw new RuntimeException(paramParcel.toString());
  }
  
  public boolean containsTarget(Surface paramSurface)
  {
    return mSurfaceSet.contains(paramSurface);
  }
  
  public void convertSurfaceToStreamId(SparseArray<OutputConfiguration> paramSparseArray)
  {
    synchronized (mSurfacesLock)
    {
      if (mSurfaceConverted)
      {
        Log.v("CaptureRequest-JV", "Cannot convert already converted surfaces!");
        return;
      }
      mStreamIdxArray = new int[mSurfaceSet.size()];
      mSurfaceIdxArray = new int[mSurfaceSet.size()];
      int i = 0;
      Iterator localIterator = mSurfaceSet.iterator();
      while (localIterator.hasNext())
      {
        Object localObject2 = (Surface)localIterator.next();
        int j = 0;
        int k = 0;
        int m = 0;
        int n = i;
        int i1;
        int i3;
        for (;;)
        {
          i = n;
          i1 = k;
          if (m >= paramSparseArray.size()) {
            break;
          }
          int i2 = paramSparseArray.keyAt(m);
          Object localObject3 = (OutputConfiguration)paramSparseArray.valueAt(m);
          i3 = 0;
          localObject3 = ((OutputConfiguration)localObject3).getSurfaces().iterator();
          for (;;)
          {
            i = n;
            i1 = k;
            if (!((Iterator)localObject3).hasNext()) {
              break;
            }
            if (localObject2 == (Surface)((Iterator)localObject3).next())
            {
              i1 = 1;
              mStreamIdxArray[n] = i2;
              mSurfaceIdxArray[n] = i3;
              i = n + 1;
              break;
            }
            i3++;
          }
          if (i1 != 0) {
            break;
          }
          m++;
          n = i;
          k = i1;
        }
        k = i;
        n = i1;
        if (i1 == 0)
        {
          long l = SurfaceUtils.getSurfaceId((Surface)localObject2);
          m = j;
          for (;;)
          {
            k = i;
            n = i1;
            if (m >= paramSparseArray.size()) {
              break;
            }
            j = paramSparseArray.keyAt(m);
            localObject2 = (OutputConfiguration)paramSparseArray.valueAt(m);
            i3 = 0;
            localObject2 = ((OutputConfiguration)localObject2).getSurfaces().iterator();
            for (;;)
            {
              k = i;
              n = i1;
              if (!((Iterator)localObject2).hasNext()) {
                break;
              }
              if (l == SurfaceUtils.getSurfaceId((Surface)((Iterator)localObject2).next()))
              {
                n = 1;
                mStreamIdxArray[i] = j;
                mSurfaceIdxArray[i] = i3;
                k = i + 1;
                break;
              }
              i3++;
            }
            if (n != 0) {
              break;
            }
            m++;
            i = k;
            i1 = n;
          }
        }
        i = k;
        if (n == 0)
        {
          mStreamIdxArray = null;
          mSurfaceIdxArray = null;
          paramSparseArray = new java/lang/IllegalArgumentException;
          paramSparseArray.<init>("CaptureRequest contains unconfigured Input/Output Surface!");
          throw paramSparseArray;
        }
      }
      mSurfaceConverted = true;
      return;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof CaptureRequest)) && (equals((CaptureRequest)paramObject))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public <T> T get(Key<T> paramKey)
  {
    return mLogicalCameraSettings.get(paramKey);
  }
  
  protected Class<Key<?>> getKeyClass()
  {
    return (Class)Key.class;
  }
  
  public List<Key<?>> getKeys()
  {
    return super.getKeys();
  }
  
  public String getLogicalCameraId()
  {
    return mLogicalCameraId;
  }
  
  public CameraMetadataNative getNativeCopy()
  {
    return new CameraMetadataNative(mLogicalCameraSettings);
  }
  
  protected <T> T getProtected(Key<?> paramKey)
  {
    return mLogicalCameraSettings.get(paramKey);
  }
  
  public int getReprocessableSessionId()
  {
    if ((mIsReprocess) && (mReprocessableSessionId != -1)) {
      return mReprocessableSessionId;
    }
    throw new IllegalStateException("Getting the reprocessable session ID for a non-reprocess capture request is illegal.");
  }
  
  public Object getTag()
  {
    return mUserTag;
  }
  
  public Collection<Surface> getTargets()
  {
    return Collections.unmodifiableCollection(mSurfaceSet);
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCodeGeneric(new Object[] { mPhysicalCameraSettings, mSurfaceSet, mUserTag });
  }
  
  public boolean isPartOfCRequestList()
  {
    return mIsPartOfCHSRequestList;
  }
  
  public boolean isReprocess()
  {
    return mIsReprocess;
  }
  
  public void recoverStreamIdToSurface()
  {
    synchronized (mSurfacesLock)
    {
      if (!mSurfaceConverted)
      {
        Log.v("CaptureRequest-JV", "Cannot convert already converted surfaces!");
        return;
      }
      mStreamIdxArray = null;
      mSurfaceIdxArray = null;
      mSurfaceConverted = false;
      return;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPhysicalCameraSettings.size());
    paramParcel.writeString(mLogicalCameraId);
    mLogicalCameraSettings.writeToParcel(paramParcel, paramInt);
    ??? = mPhysicalCameraSettings.entrySet().iterator();
    Object localObject2;
    while (((Iterator)???).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)???).next();
      if (!((String)((Map.Entry)localObject2).getKey()).equals(mLogicalCameraId))
      {
        paramParcel.writeString((String)((Map.Entry)localObject2).getKey());
        ((CameraMetadataNative)((Map.Entry)localObject2).getValue()).writeToParcel(paramParcel, paramInt);
      }
    }
    paramParcel.writeInt(mIsReprocess);
    synchronized (mSurfacesLock)
    {
      if (mSurfaceConverted) {
        localObject2 = mEmptySurfaceSet;
      } else {
        localObject2 = mSurfaceSet;
      }
      paramParcel.writeParcelableArray((Surface[])((ArraySet)localObject2).toArray(new Surface[((ArraySet)localObject2).size()]), paramInt);
      boolean bool = mSurfaceConverted;
      paramInt = 0;
      if (bool)
      {
        paramParcel.writeInt(mStreamIdxArray.length);
        while (paramInt < mStreamIdxArray.length)
        {
          paramParcel.writeInt(mStreamIdxArray[paramInt]);
          paramParcel.writeInt(mSurfaceIdxArray[paramInt]);
          paramInt++;
        }
      }
      paramParcel.writeInt(0);
      return;
    }
  }
  
  public static final class Builder
  {
    private final CaptureRequest mRequest;
    
    public Builder(CameraMetadataNative paramCameraMetadataNative, boolean paramBoolean, int paramInt, String paramString, Set<String> paramSet)
    {
      mRequest = new CaptureRequest(paramCameraMetadataNative, paramBoolean, paramInt, paramString, paramSet, null);
    }
    
    public void addTarget(Surface paramSurface)
    {
      mRequest.mSurfaceSet.add(paramSurface);
    }
    
    public CaptureRequest build()
    {
      return new CaptureRequest(mRequest, null);
    }
    
    public <T> T get(CaptureRequest.Key<T> paramKey)
    {
      return mRequest.mLogicalCameraSettings.get(paramKey);
    }
    
    public <T> T getPhysicalCameraKey(CaptureRequest.Key<T> paramKey, String paramString)
    {
      if (mRequest.mPhysicalCameraSettings.containsKey(paramString)) {
        return ((CameraMetadataNative)mRequest.mPhysicalCameraSettings.get(paramString)).get(paramKey);
      }
      paramKey = new StringBuilder();
      paramKey.append("Physical camera id: ");
      paramKey.append(paramString);
      paramKey.append(" is not valid!");
      throw new IllegalArgumentException(paramKey.toString());
    }
    
    public boolean isEmpty()
    {
      return mRequest.mLogicalCameraSettings.isEmpty();
    }
    
    public void removeTarget(Surface paramSurface)
    {
      mRequest.mSurfaceSet.remove(paramSurface);
    }
    
    public <T> void set(CaptureRequest.Key<T> paramKey, T paramT)
    {
      mRequest.mLogicalCameraSettings.set(paramKey, paramT);
    }
    
    public void setPartOfCHSRequestList(boolean paramBoolean)
    {
      CaptureRequest.access$702(mRequest, paramBoolean);
    }
    
    public <T> Builder setPhysicalCameraKey(CaptureRequest.Key<T> paramKey, T paramT, String paramString)
    {
      if (mRequest.mPhysicalCameraSettings.containsKey(paramString))
      {
        ((CameraMetadataNative)mRequest.mPhysicalCameraSettings.get(paramString)).set(paramKey, paramT);
        return this;
      }
      paramKey = new StringBuilder();
      paramKey.append("Physical camera id: ");
      paramKey.append(paramString);
      paramKey.append(" is not valid!");
      throw new IllegalArgumentException(paramKey.toString());
    }
    
    public void setTag(Object paramObject)
    {
      CaptureRequest.access$602(mRequest, paramObject);
    }
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
      return String.format("CaptureRequest.Key(%s)", new Object[] { mKey.getName() });
    }
  }
}
