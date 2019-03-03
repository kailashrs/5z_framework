package android.hardware.camera2.impl;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.CaptureResult.Key;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.marshal.impl.MarshalQueryableArray;
import android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern;
import android.hardware.camera2.marshal.impl.MarshalQueryableBoolean;
import android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform;
import android.hardware.camera2.marshal.impl.MarshalQueryableEnum;
import android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle;
import android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger;
import android.hardware.camera2.marshal.impl.MarshalQueryablePair;
import android.hardware.camera2.marshal.impl.MarshalQueryableParcelable;
import android.hardware.camera2.marshal.impl.MarshalQueryablePrimitive;
import android.hardware.camera2.marshal.impl.MarshalQueryableRange;
import android.hardware.camera2.marshal.impl.MarshalQueryableRect;
import android.hardware.camera2.marshal.impl.MarshalQueryableReprocessFormatsMap;
import android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector;
import android.hardware.camera2.marshal.impl.MarshalQueryableSize;
import android.hardware.camera2.marshal.impl.MarshalQueryableSizeF;
import android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration;
import android.hardware.camera2.marshal.impl.MarshalQueryableString;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.LensShadingMap;
import android.hardware.camera2.params.OisSample;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.params.TonemapCurve;
import android.hardware.camera2.utils.TypeReference;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ServiceSpecificException;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

public class CameraMetadataNative
  implements Parcelable
{
  private static final String CELLID_PROCESS = "CELLID";
  public static final Parcelable.Creator<CameraMetadataNative> CREATOR = new Parcelable.Creator()
  {
    public CameraMetadataNative createFromParcel(Parcel paramAnonymousParcel)
    {
      CameraMetadataNative localCameraMetadataNative = new CameraMetadataNative();
      localCameraMetadataNative.readFromParcel(paramAnonymousParcel);
      return localCameraMetadataNative;
    }
    
    public CameraMetadataNative[] newArray(int paramAnonymousInt)
    {
      return new CameraMetadataNative[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  private static final int FACE_LANDMARK_SIZE = 6;
  private static final String GPS_PROCESS = "GPS";
  public static final int NATIVE_JPEG_FORMAT = 33;
  public static final int NUM_TYPES = 6;
  private static final String TAG = "CameraMetadataJV";
  public static final int TYPE_BYTE = 0;
  public static final int TYPE_DOUBLE = 4;
  public static final int TYPE_FLOAT = 2;
  public static final int TYPE_INT32 = 1;
  public static final int TYPE_INT64 = 3;
  public static final int TYPE_RATIONAL = 5;
  private static final HashMap<Key<?>, GetCommand> sGetCommandMap = new HashMap();
  private static final HashMap<Key<?>, SetCommand> sSetCommandMap;
  private long mMetadataPtr;
  
  static
  {
    sGetCommandMap.put(CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getAvailableFormats();
      }
    });
    sGetCommandMap.put(CaptureResult.STATISTICS_FACES.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getFaces();
      }
    });
    sGetCommandMap.put(CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getFaceRectangles();
      }
    });
    sGetCommandMap.put(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getStreamConfigurationMap();
      }
    });
    sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AE.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getMaxRegions(paramAnonymousKey);
      }
    });
    sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getMaxRegions(paramAnonymousKey);
      }
    });
    sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AF.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getMaxRegions(paramAnonymousKey);
      }
    });
    sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getMaxNumOutputs(paramAnonymousKey);
      }
    });
    sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getMaxNumOutputs(paramAnonymousKey);
      }
    });
    sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getMaxNumOutputs(paramAnonymousKey);
      }
    });
    sGetCommandMap.put(CaptureRequest.TONEMAP_CURVE.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getTonemapCurve();
      }
    });
    sGetCommandMap.put(CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getGpsLocation();
      }
    });
    sGetCommandMap.put(CaptureResult.STATISTICS_LENS_SHADING_CORRECTION_MAP.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getLensShadingMap();
      }
    });
    sGetCommandMap.put(CaptureResult.STATISTICS_OIS_SAMPLES.getNativeKey(), new GetCommand()
    {
      public <T> T getValue(CameraMetadataNative paramAnonymousCameraMetadataNative, CameraMetadataNative.Key<T> paramAnonymousKey)
      {
        return paramAnonymousCameraMetadataNative.getOisSamples();
      }
    });
    sSetCommandMap = new HashMap();
    sSetCommandMap.put(CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new SetCommand()
    {
      public <T> void setValue(CameraMetadataNative paramAnonymousCameraMetadataNative, T paramAnonymousT)
      {
        paramAnonymousCameraMetadataNative.setAvailableFormats((int[])paramAnonymousT);
      }
    });
    sSetCommandMap.put(CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new SetCommand()
    {
      public <T> void setValue(CameraMetadataNative paramAnonymousCameraMetadataNative, T paramAnonymousT)
      {
        paramAnonymousCameraMetadataNative.setFaceRectangles((Rect[])paramAnonymousT);
      }
    });
    sSetCommandMap.put(CaptureResult.STATISTICS_FACES.getNativeKey(), new SetCommand()
    {
      public <T> void setValue(CameraMetadataNative paramAnonymousCameraMetadataNative, T paramAnonymousT)
      {
        paramAnonymousCameraMetadataNative.setFaces((Face[])paramAnonymousT);
      }
    });
    sSetCommandMap.put(CaptureRequest.TONEMAP_CURVE.getNativeKey(), new SetCommand()
    {
      public <T> void setValue(CameraMetadataNative paramAnonymousCameraMetadataNative, T paramAnonymousT)
      {
        paramAnonymousCameraMetadataNative.setTonemapCurve((TonemapCurve)paramAnonymousT);
      }
    });
    sSetCommandMap.put(CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new SetCommand()
    {
      public <T> void setValue(CameraMetadataNative paramAnonymousCameraMetadataNative, T paramAnonymousT)
      {
        paramAnonymousCameraMetadataNative.setGpsLocation((Location)paramAnonymousT);
      }
    });
    registerAllMarshalers();
  }
  
  public CameraMetadataNative()
  {
    mMetadataPtr = nativeAllocate();
    if (mMetadataPtr != 0L) {
      return;
    }
    throw new OutOfMemoryError("Failed to allocate native CameraMetadata");
  }
  
  public CameraMetadataNative(CameraMetadataNative paramCameraMetadataNative)
  {
    mMetadataPtr = nativeAllocateCopy(paramCameraMetadataNative);
    if (mMetadataPtr != 0L) {
      return;
    }
    throw new OutOfMemoryError("Failed to allocate native CameraMetadata");
  }
  
  private static boolean areValuesAllNull(Object... paramVarArgs)
  {
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      if (paramVarArgs[j] != null) {
        return false;
      }
    }
    return true;
  }
  
  private void close()
  {
    nativeClose();
    mMetadataPtr = 0L;
  }
  
  private int[] getAvailableFormats()
  {
    int[] arrayOfInt = (int[])getBase(CameraCharacteristics.SCALER_AVAILABLE_FORMATS);
    if (arrayOfInt != null) {
      for (int i = 0; i < arrayOfInt.length; i++) {
        if (arrayOfInt[i] == 33) {
          arrayOfInt[i] = 256;
        }
      }
    }
    return arrayOfInt;
  }
  
  private <T> T getBase(CameraCharacteristics.Key<T> paramKey)
  {
    return getBase(paramKey.getNativeKey());
  }
  
  private <T> T getBase(CaptureRequest.Key<T> paramKey)
  {
    return getBase(paramKey.getNativeKey());
  }
  
  private <T> T getBase(CaptureResult.Key<T> paramKey)
  {
    return getBase(paramKey.getNativeKey());
  }
  
  private <T> T getBase(Key<T> paramKey)
  {
    int i = nativeGetTagFromKeyLocal(paramKey.getName());
    byte[] arrayOfByte1 = readValues(i);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1 == null)
    {
      if (mFallbackName == null) {
        return null;
      }
      i = nativeGetTagFromKeyLocal(mFallbackName);
      arrayOfByte1 = readValues(i);
      arrayOfByte2 = arrayOfByte1;
      if (arrayOfByte1 == null) {
        return null;
      }
    }
    return getMarshalerForKey(paramKey, nativeGetTypeFromTagLocal(i)).unmarshal(ByteBuffer.wrap(arrayOfByte2).order(ByteOrder.nativeOrder()));
  }
  
  private Rect[] getFaceRectangles()
  {
    Rect[] arrayOfRect1 = (Rect[])getBase(CaptureResult.STATISTICS_FACE_RECTANGLES);
    if (arrayOfRect1 == null) {
      return null;
    }
    Rect[] arrayOfRect2 = new Rect[arrayOfRect1.length];
    for (int i = 0; i < arrayOfRect1.length; i++) {
      arrayOfRect2[i] = new Rect(left, top, right - left, bottom - top);
    }
    return arrayOfRect2;
  }
  
  private Face[] getFaces()
  {
    Object localObject1 = (Integer)get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
    byte[] arrayOfByte = (byte[])get(CaptureResult.STATISTICS_FACE_SCORES);
    Rect[] arrayOfRect = (Rect[])get(CaptureResult.STATISTICS_FACE_RECTANGLES);
    int[] arrayOfInt1 = (int[])get(CaptureResult.STATISTICS_FACE_IDS);
    int[] arrayOfInt2 = (int[])get(CaptureResult.STATISTICS_FACE_LANDMARKS);
    int i = 0;
    int j = 0;
    if (areValuesAllNull(new Object[] { localObject1, arrayOfByte, arrayOfRect, arrayOfInt1, arrayOfInt2 })) {
      return null;
    }
    Object localObject2;
    if (localObject1 == null)
    {
      Log.w("CameraMetadataJV", "Face detect mode metadata is null, assuming the mode is SIMPLE");
      localObject2 = Integer.valueOf(1);
    }
    else
    {
      if (((Integer)localObject1).intValue() == 0) {
        return new Face[0];
      }
      localObject2 = localObject1;
      if (((Integer)localObject1).intValue() != 1)
      {
        localObject2 = localObject1;
        if (((Integer)localObject1).intValue() != 2)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Unknown face detect mode: ");
          ((StringBuilder)localObject2).append(localObject1);
          Log.w("CameraMetadataJV", ((StringBuilder)localObject2).toString());
          return new Face[0];
        }
      }
    }
    if ((arrayOfByte != null) && (arrayOfRect != null))
    {
      if (arrayOfByte.length != arrayOfRect.length) {
        Log.w("CameraMetadataJV", String.format("Face score size(%d) doesn match face rectangle size(%d)!", new Object[] { Integer.valueOf(arrayOfByte.length), Integer.valueOf(arrayOfRect.length) }));
      }
      int k = Math.min(arrayOfByte.length, arrayOfRect.length);
      localObject1 = localObject2;
      int m = k;
      if (((Integer)localObject2).intValue() == 2) {
        if ((arrayOfInt1 != null) && (arrayOfInt2 != null))
        {
          if ((arrayOfInt1.length != k) || (arrayOfInt2.length != k * 6)) {
            Log.w("CameraMetadataJV", String.format("Face id size(%d), or face landmark size(%d) don'tmatch face number(%d)!", new Object[] { Integer.valueOf(arrayOfInt1.length), Integer.valueOf(arrayOfInt2.length * 6), Integer.valueOf(k) }));
          }
          m = Math.min(Math.min(k, arrayOfInt1.length), arrayOfInt2.length / 6);
          localObject1 = localObject2;
        }
        else
        {
          Log.w("CameraMetadataJV", "Expect face ids and landmarks to be non-null for FULL mode,fallback to SIMPLE mode");
          localObject1 = Integer.valueOf(1);
          m = k;
        }
      }
      localObject2 = new ArrayList();
      if (((Integer)localObject1).intValue() == 1) {
        while (j < m)
        {
          if ((arrayOfByte[j] <= 100) && (arrayOfByte[j] >= 1)) {
            ((ArrayList)localObject2).add(new Face(arrayOfRect[j], arrayOfByte[j]));
          }
          j++;
        }
      }
      for (j = i; j < m; j++) {
        if ((arrayOfByte[j] <= 100) && (arrayOfByte[j] >= 1) && (arrayOfInt1[j] >= 0))
        {
          Point localPoint1 = new Point(arrayOfInt2[(j * 6)], arrayOfInt2[(j * 6 + 1)]);
          Point localPoint2 = new Point(arrayOfInt2[(j * 6 + 2)], arrayOfInt2[(j * 6 + 3)]);
          localObject1 = new Point(arrayOfInt2[(j * 6 + 4)], arrayOfInt2[(j * 6 + 5)]);
          ((ArrayList)localObject2).add(new Face(arrayOfRect[j], arrayOfByte[j], arrayOfInt1[j], localPoint1, localPoint2, (Point)localObject1));
        }
      }
      localObject1 = new Face[((ArrayList)localObject2).size()];
      ((ArrayList)localObject2).toArray((Object[])localObject1);
      return localObject1;
    }
    Log.w("CameraMetadataJV", "Expect face scores and rectangles to be non-null");
    return new Face[0];
  }
  
  private Location getGpsLocation()
  {
    Object localObject = (String)get(CaptureResult.JPEG_GPS_PROCESSING_METHOD);
    double[] arrayOfDouble = (double[])get(CaptureResult.JPEG_GPS_COORDINATES);
    Long localLong = (Long)get(CaptureResult.JPEG_GPS_TIMESTAMP);
    if (areValuesAllNull(new Object[] { localObject, arrayOfDouble, localLong })) {
      return null;
    }
    localObject = new Location(translateProcessToLocationProvider((String)localObject));
    if (localLong != null) {
      ((Location)localObject).setTime(localLong.longValue() * 1000L);
    } else {
      Log.w("CameraMetadataJV", "getGpsLocation - No timestamp for GPS location.");
    }
    if (arrayOfDouble != null)
    {
      ((Location)localObject).setLatitude(arrayOfDouble[0]);
      ((Location)localObject).setLongitude(arrayOfDouble[1]);
      ((Location)localObject).setAltitude(arrayOfDouble[2]);
    }
    else
    {
      Log.w("CameraMetadataJV", "getGpsLocation - No coordinates for GPS location");
    }
    return localObject;
  }
  
  private LensShadingMap getLensShadingMap()
  {
    float[] arrayOfFloat = (float[])getBase(CaptureResult.STATISTICS_LENS_SHADING_MAP);
    Size localSize = (Size)get(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE);
    if (arrayOfFloat == null) {
      return null;
    }
    if (localSize == null)
    {
      Log.w("CameraMetadataJV", "getLensShadingMap - Lens shading map size was null.");
      return null;
    }
    return new LensShadingMap(arrayOfFloat, localSize.getHeight(), localSize.getWidth());
  }
  
  private static <T> Marshaler<T> getMarshalerForKey(Key<T> paramKey, int paramInt)
  {
    return MarshalRegistry.getMarshaler(paramKey.getTypeReference(), paramInt);
  }
  
  private <T> Integer getMaxNumOutputs(Key<T> paramKey)
  {
    Object localObject = (int[])getBase(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS);
    if (localObject == null) {
      return null;
    }
    if (paramKey.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW)) {
      return Integer.valueOf(localObject[0]);
    }
    if (paramKey.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC)) {
      return Integer.valueOf(localObject[1]);
    }
    if (paramKey.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING)) {
      return Integer.valueOf(localObject[2]);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid key ");
    ((StringBuilder)localObject).append(paramKey);
    throw new AssertionError(((StringBuilder)localObject).toString());
  }
  
  private <T> Integer getMaxRegions(Key<T> paramKey)
  {
    Object localObject = (int[])getBase(CameraCharacteristics.CONTROL_MAX_REGIONS);
    if (localObject == null) {
      return null;
    }
    if (paramKey.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AE)) {
      return Integer.valueOf(localObject[0]);
    }
    if (paramKey.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB)) {
      return Integer.valueOf(localObject[1]);
    }
    if (paramKey.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)) {
      return Integer.valueOf(localObject[2]);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid key ");
    ((StringBuilder)localObject).append(paramKey);
    throw new AssertionError(((StringBuilder)localObject).toString());
  }
  
  public static int getNativeType(int paramInt, long paramLong)
  {
    return nativeGetTypeFromTag(paramInt, paramLong);
  }
  
  private OisSample[] getOisSamples()
  {
    long[] arrayOfLong = (long[])getBase(CaptureResult.STATISTICS_OIS_TIMESTAMPS);
    float[] arrayOfFloat1 = (float[])getBase(CaptureResult.STATISTICS_OIS_X_SHIFTS);
    float[] arrayOfFloat2 = (float[])getBase(CaptureResult.STATISTICS_OIS_Y_SHIFTS);
    if (arrayOfLong == null)
    {
      if (arrayOfFloat1 == null)
      {
        if (arrayOfFloat2 == null) {
          return null;
        }
        throw new AssertionError("timestamps is null but yShifts is not");
      }
      throw new AssertionError("timestamps is null but xShifts is not");
    }
    if (arrayOfFloat1 != null)
    {
      if (arrayOfFloat2 != null)
      {
        int i = arrayOfFloat1.length;
        int j = arrayOfLong.length;
        int k = 0;
        if (i == j)
        {
          if (arrayOfFloat2.length == arrayOfLong.length)
          {
            OisSample[] arrayOfOisSample = new OisSample[arrayOfLong.length];
            while (k < arrayOfLong.length)
            {
              arrayOfOisSample[k] = new OisSample(arrayOfLong[k], arrayOfFloat1[k], arrayOfFloat2[k]);
              k++;
            }
            return arrayOfOisSample;
          }
          throw new AssertionError(String.format("timestamps has %d entries but yShifts has %d", new Object[] { Integer.valueOf(arrayOfLong.length), Integer.valueOf(arrayOfFloat2.length) }));
        }
        throw new AssertionError(String.format("timestamps has %d entries but xShifts has %d", new Object[] { Integer.valueOf(arrayOfLong.length), Integer.valueOf(arrayOfFloat1.length) }));
      }
      throw new AssertionError("timestamps is not null but yShifts is");
    }
    throw new AssertionError("timestamps is not null but xShifts is");
  }
  
  private StreamConfigurationMap getStreamConfigurationMap()
  {
    StreamConfiguration[] arrayOfStreamConfiguration1 = (StreamConfiguration[])getBase(CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS);
    StreamConfigurationDuration[] arrayOfStreamConfigurationDuration1 = (StreamConfigurationDuration[])getBase(CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS);
    StreamConfigurationDuration[] arrayOfStreamConfigurationDuration2 = (StreamConfigurationDuration[])getBase(CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS);
    StreamConfiguration[] arrayOfStreamConfiguration2 = (StreamConfiguration[])getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS);
    StreamConfigurationDuration[] arrayOfStreamConfigurationDuration3 = (StreamConfigurationDuration[])getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS);
    StreamConfigurationDuration[] arrayOfStreamConfigurationDuration4 = (StreamConfigurationDuration[])getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS);
    HighSpeedVideoConfiguration[] arrayOfHighSpeedVideoConfiguration = (HighSpeedVideoConfiguration[])getBase(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS);
    ReprocessFormatsMap localReprocessFormatsMap = (ReprocessFormatsMap)getBase(CameraCharacteristics.SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP);
    int[] arrayOfInt = (int[])getBase(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
    boolean bool1 = false;
    int i = arrayOfInt.length;
    boolean bool2;
    for (int j = 0;; j++)
    {
      bool2 = bool1;
      if (j >= i) {
        break;
      }
      if (arrayOfInt[j] == 6)
      {
        bool2 = true;
        break;
      }
    }
    return new StreamConfigurationMap(arrayOfStreamConfiguration1, arrayOfStreamConfigurationDuration1, arrayOfStreamConfigurationDuration2, arrayOfStreamConfiguration2, arrayOfStreamConfigurationDuration3, arrayOfStreamConfigurationDuration4, arrayOfHighSpeedVideoConfiguration, localReprocessFormatsMap, bool2);
  }
  
  public static int getTag(String paramString)
  {
    return nativeGetTagFromKey(paramString, Long.MAX_VALUE);
  }
  
  public static int getTag(String paramString, long paramLong)
  {
    return nativeGetTagFromKey(paramString, paramLong);
  }
  
  private <T> TonemapCurve getTonemapCurve()
  {
    float[] arrayOfFloat1 = (float[])getBase(CaptureRequest.TONEMAP_CURVE_RED);
    float[] arrayOfFloat2 = (float[])getBase(CaptureRequest.TONEMAP_CURVE_GREEN);
    float[] arrayOfFloat3 = (float[])getBase(CaptureRequest.TONEMAP_CURVE_BLUE);
    if (areValuesAllNull(new Object[] { arrayOfFloat1, arrayOfFloat2, arrayOfFloat3 })) {
      return null;
    }
    if ((arrayOfFloat1 != null) && (arrayOfFloat2 != null) && (arrayOfFloat3 != null)) {
      return new TonemapCurve(arrayOfFloat1, arrayOfFloat2, arrayOfFloat3);
    }
    Log.w("CameraMetadataJV", "getTonemapCurve - missing tone curve components");
    return null;
  }
  
  public static CameraMetadataNative move(CameraMetadataNative paramCameraMetadataNative)
  {
    CameraMetadataNative localCameraMetadataNative = new CameraMetadataNative();
    localCameraMetadataNative.swap(paramCameraMetadataNative);
    return localCameraMetadataNative;
  }
  
  private native long nativeAllocate();
  
  private native long nativeAllocateCopy(CameraMetadataNative paramCameraMetadataNative)
    throws NullPointerException;
  
  private synchronized native void nativeClose();
  
  private synchronized native void nativeDump()
    throws IOException;
  
  private synchronized native ArrayList nativeGetAllVendorKeys(Class paramClass);
  
  private synchronized native int nativeGetEntryCount();
  
  private static native int nativeGetTagFromKey(String paramString, long paramLong)
    throws IllegalArgumentException;
  
  private synchronized native int nativeGetTagFromKeyLocal(String paramString)
    throws IllegalArgumentException;
  
  private static native int nativeGetTypeFromTag(int paramInt, long paramLong)
    throws IllegalArgumentException;
  
  private synchronized native int nativeGetTypeFromTagLocal(int paramInt)
    throws IllegalArgumentException;
  
  private synchronized native boolean nativeIsEmpty();
  
  private synchronized native void nativeReadFromParcel(Parcel paramParcel);
  
  private synchronized native byte[] nativeReadValues(int paramInt);
  
  private static native int nativeSetupGlobalVendorTagDescriptor();
  
  private synchronized native void nativeSwap(CameraMetadataNative paramCameraMetadataNative)
    throws NullPointerException;
  
  private synchronized native void nativeWriteToParcel(Parcel paramParcel);
  
  private synchronized native void nativeWriteValues(int paramInt, byte[] paramArrayOfByte);
  
  private static void registerAllMarshalers()
  {
    MarshalQueryable[] arrayOfMarshalQueryable = new MarshalQueryable[20];
    MarshalQueryablePrimitive localMarshalQueryablePrimitive = new MarshalQueryablePrimitive();
    int i = 0;
    arrayOfMarshalQueryable[0] = localMarshalQueryablePrimitive;
    arrayOfMarshalQueryable[1] = new MarshalQueryableEnum();
    arrayOfMarshalQueryable[2] = new MarshalQueryableArray();
    arrayOfMarshalQueryable[3] = new MarshalQueryableBoolean();
    arrayOfMarshalQueryable[4] = new MarshalQueryableNativeByteToInteger();
    arrayOfMarshalQueryable[5] = new MarshalQueryableRect();
    arrayOfMarshalQueryable[6] = new MarshalQueryableSize();
    arrayOfMarshalQueryable[7] = new MarshalQueryableSizeF();
    arrayOfMarshalQueryable[8] = new MarshalQueryableString();
    arrayOfMarshalQueryable[9] = new MarshalQueryableReprocessFormatsMap();
    arrayOfMarshalQueryable[10] = new MarshalQueryableRange();
    arrayOfMarshalQueryable[11] = new MarshalQueryablePair();
    arrayOfMarshalQueryable[12] = new MarshalQueryableMeteringRectangle();
    arrayOfMarshalQueryable[13] = new MarshalQueryableColorSpaceTransform();
    arrayOfMarshalQueryable[14] = new MarshalQueryableStreamConfiguration();
    arrayOfMarshalQueryable[15] = new MarshalQueryableStreamConfigurationDuration();
    arrayOfMarshalQueryable[16] = new MarshalQueryableRggbChannelVector();
    arrayOfMarshalQueryable[17] = new MarshalQueryableBlackLevelPattern();
    arrayOfMarshalQueryable[18] = new MarshalQueryableHighSpeedVideoConfiguration();
    arrayOfMarshalQueryable[19] = new MarshalQueryableParcelable();
    int j = arrayOfMarshalQueryable.length;
    while (i < j)
    {
      MarshalRegistry.registerMarshalQueryable(arrayOfMarshalQueryable[i]);
      i++;
    }
  }
  
  private boolean setAvailableFormats(int[] paramArrayOfInt)
  {
    int i = 0;
    if (paramArrayOfInt == null) {
      return false;
    }
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    while (i < paramArrayOfInt.length)
    {
      arrayOfInt[i] = paramArrayOfInt[i];
      if (paramArrayOfInt[i] == 256) {
        arrayOfInt[i] = 33;
      }
      i++;
    }
    setBase(CameraCharacteristics.SCALER_AVAILABLE_FORMATS, arrayOfInt);
    return true;
  }
  
  private <T> void setBase(CameraCharacteristics.Key<T> paramKey, T paramT)
  {
    setBase(paramKey.getNativeKey(), paramT);
  }
  
  private <T> void setBase(CaptureRequest.Key<T> paramKey, T paramT)
  {
    setBase(paramKey.getNativeKey(), paramT);
  }
  
  private <T> void setBase(CaptureResult.Key<T> paramKey, T paramT)
  {
    setBase(paramKey.getNativeKey(), paramT);
  }
  
  private <T> void setBase(Key<T> paramKey, T paramT)
  {
    int i = nativeGetTagFromKeyLocal(paramKey.getName());
    if (paramT == null)
    {
      writeValues(i, null);
      return;
    }
    paramKey = getMarshalerForKey(paramKey, nativeGetTypeFromTagLocal(i));
    byte[] arrayOfByte = new byte[paramKey.calculateMarshalSize(paramT)];
    paramKey.marshal(paramT, ByteBuffer.wrap(arrayOfByte).order(ByteOrder.nativeOrder()));
    writeValues(i, arrayOfByte);
  }
  
  private boolean setFaceRectangles(Rect[] paramArrayOfRect)
  {
    int i = 0;
    if (paramArrayOfRect == null) {
      return false;
    }
    Rect[] arrayOfRect = new Rect[paramArrayOfRect.length];
    while (i < arrayOfRect.length)
    {
      arrayOfRect[i] = new Rect(left, top, right + left, bottom + top);
      i++;
    }
    setBase(CaptureResult.STATISTICS_FACE_RECTANGLES, arrayOfRect);
    return true;
  }
  
  private boolean setFaces(Face[] paramArrayOfFace)
  {
    int i = 0;
    if (paramArrayOfFace == null) {
      return false;
    }
    int j = paramArrayOfFace.length;
    int k = paramArrayOfFace.length;
    int m = 1;
    int n = 0;
    while (n < k)
    {
      localObject = paramArrayOfFace[n];
      if (localObject == null)
      {
        i1 = j - 1;
        Log.w("CameraMetadataJV", "setFaces - null face detected, skipping");
      }
      else
      {
        i1 = j;
        if (((Face)localObject).getId() == -1)
        {
          m = 0;
          i1 = j;
        }
      }
      n++;
      j = i1;
    }
    Rect[] arrayOfRect = new Rect[j];
    byte[] arrayOfByte = new byte[j];
    Object localObject = null;
    int[] arrayOfInt = null;
    if (m != 0)
    {
      localObject = new int[j];
      arrayOfInt = new int[j * 6];
    }
    n = 0;
    int i1 = paramArrayOfFace.length;
    for (j = i; j < i1; j++)
    {
      Face localFace = paramArrayOfFace[j];
      if (localFace != null)
      {
        arrayOfRect[n] = localFace.getBounds();
        arrayOfByte[n] = ((byte)(byte)localFace.getScore());
        if (m != 0)
        {
          localObject[n] = localFace.getId();
          i = 0 + 1;
          arrayOfInt[(n * 6 + 0)] = getLeftEyePositionx;
          k = i + 1;
          arrayOfInt[(n * 6 + i)] = getLeftEyePositiony;
          i = k + 1;
          arrayOfInt[(n * 6 + k)] = getRightEyePositionx;
          k = i + 1;
          arrayOfInt[(n * 6 + i)] = getRightEyePositiony;
          i = k + 1;
          arrayOfInt[(n * 6 + k)] = getMouthPositionx;
          arrayOfInt[(n * 6 + i)] = getMouthPositiony;
        }
        n++;
      }
    }
    set(CaptureResult.STATISTICS_FACE_RECTANGLES, arrayOfRect);
    set(CaptureResult.STATISTICS_FACE_IDS, localObject);
    set(CaptureResult.STATISTICS_FACE_LANDMARKS, arrayOfInt);
    set(CaptureResult.STATISTICS_FACE_SCORES, arrayOfByte);
    return true;
  }
  
  private boolean setGpsLocation(Location paramLocation)
  {
    if (paramLocation == null) {
      return false;
    }
    double d1 = paramLocation.getLatitude();
    double d2 = paramLocation.getLongitude();
    double d3 = paramLocation.getAltitude();
    String str = translateLocationProviderToProcess(paramLocation.getProvider());
    long l = paramLocation.getTime() / 1000L;
    set(CaptureRequest.JPEG_GPS_TIMESTAMP, Long.valueOf(l));
    set(CaptureRequest.JPEG_GPS_COORDINATES, new double[] { d1, d2, d3 });
    if (str == null) {
      Log.w("CameraMetadataJV", "setGpsLocation - No process method, Location is not from a GPS or NETWORKprovider");
    } else {
      setBase(CaptureRequest.JPEG_GPS_PROCESSING_METHOD, str);
    }
    return true;
  }
  
  private <T> boolean setTonemapCurve(TonemapCurve paramTonemapCurve)
  {
    if (paramTonemapCurve == null) {
      return false;
    }
    float[][] arrayOfFloat = new float[3][];
    for (int i = 0; i <= 2; i++)
    {
      arrayOfFloat[i] = new float[paramTonemapCurve.getPointCount(i) * 2];
      paramTonemapCurve.copyColorCurve(i, arrayOfFloat[i], 0);
    }
    setBase(CaptureRequest.TONEMAP_CURVE_RED, arrayOfFloat[0]);
    setBase(CaptureRequest.TONEMAP_CURVE_GREEN, arrayOfFloat[1]);
    setBase(CaptureRequest.TONEMAP_CURVE_BLUE, arrayOfFloat[2]);
    return true;
  }
  
  public static void setupGlobalVendorTagDescriptor()
    throws ServiceSpecificException
  {
    int i = nativeSetupGlobalVendorTagDescriptor();
    if (i == 0) {
      return;
    }
    throw new ServiceSpecificException(i, "Failure to set up global vendor tags");
  }
  
  private static String translateLocationProviderToProcess(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = -1;
    int j = paramString.hashCode();
    if (j != 102570)
    {
      if ((j == 1843485230) && (paramString.equals("network"))) {
        i = 1;
      }
    }
    else if (paramString.equals("gps")) {
      i = 0;
    }
    switch (i)
    {
    default: 
      return null;
    case 1: 
      return "CELLID";
    }
    return "GPS";
  }
  
  private static String translateProcessToLocationProvider(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = -1;
    int j = paramString.hashCode();
    if (j != 70794)
    {
      if ((j == 1984215549) && (paramString.equals("CELLID"))) {
        i = 1;
      }
    }
    else if (paramString.equals("GPS")) {
      i = 0;
    }
    switch (i)
    {
    default: 
      return null;
    case 1: 
      return "network";
    }
    return "gps";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dumpToLog()
  {
    try
    {
      nativeDump();
    }
    catch (IOException localIOException)
    {
      Log.wtf("CameraMetadataJV", "Dump logging failed", localIOException);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public <T> T get(CameraCharacteristics.Key<T> paramKey)
  {
    return get(paramKey.getNativeKey());
  }
  
  public <T> T get(CaptureRequest.Key<T> paramKey)
  {
    return get(paramKey.getNativeKey());
  }
  
  public <T> T get(CaptureResult.Key<T> paramKey)
  {
    return get(paramKey.getNativeKey());
  }
  
  public <T> T get(Key<T> paramKey)
  {
    Preconditions.checkNotNull(paramKey, "key must not be null");
    GetCommand localGetCommand = (GetCommand)sGetCommandMap.get(paramKey);
    if (localGetCommand != null) {
      return localGetCommand.getValue(this, paramKey);
    }
    return getBase(paramKey);
  }
  
  public <K> ArrayList<K> getAllVendorKeys(Class<K> paramClass)
  {
    if (paramClass != null) {
      return nativeGetAllVendorKeys(paramClass);
    }
    throw new NullPointerException();
  }
  
  public int getEntryCount()
  {
    return nativeGetEntryCount();
  }
  
  public boolean isEmpty()
  {
    return nativeIsEmpty();
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    nativeReadFromParcel(paramParcel);
  }
  
  public byte[] readValues(int paramInt)
  {
    return nativeReadValues(paramInt);
  }
  
  public <T> void set(CameraCharacteristics.Key<T> paramKey, T paramT)
  {
    set(paramKey.getNativeKey(), paramT);
  }
  
  public <T> void set(CaptureRequest.Key<T> paramKey, T paramT)
  {
    set(paramKey.getNativeKey(), paramT);
  }
  
  public <T> void set(CaptureResult.Key<T> paramKey, T paramT)
  {
    set(paramKey.getNativeKey(), paramT);
  }
  
  public <T> void set(Key<T> paramKey, T paramT)
  {
    SetCommand localSetCommand = (SetCommand)sSetCommandMap.get(paramKey);
    if (localSetCommand != null)
    {
      localSetCommand.setValue(this, paramT);
      return;
    }
    setBase(paramKey, paramT);
  }
  
  public void swap(CameraMetadataNative paramCameraMetadataNative)
  {
    nativeSwap(paramCameraMetadataNative);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    nativeWriteToParcel(paramParcel);
  }
  
  public void writeValues(int paramInt, byte[] paramArrayOfByte)
  {
    nativeWriteValues(paramInt, paramArrayOfByte);
  }
  
  public static class Key<T>
  {
    private final String mFallbackName;
    private boolean mHasTag;
    private final int mHash;
    private final String mName;
    private int mTag;
    private final Class<T> mType;
    private final TypeReference<T> mTypeReference;
    private long mVendorId = Long.MAX_VALUE;
    
    public Key(String paramString, TypeReference<T> paramTypeReference)
    {
      if (paramString != null)
      {
        if (paramTypeReference != null)
        {
          mName = paramString;
          mFallbackName = null;
          mType = paramTypeReference.getRawType();
          mTypeReference = paramTypeReference;
          mHash = (mName.hashCode() ^ mTypeReference.hashCode());
          return;
        }
        throw new NullPointerException("TypeReference needs to be non-null");
      }
      throw new NullPointerException("Key needs a valid name");
    }
    
    public Key(String paramString, Class<T> paramClass)
    {
      if (paramString != null)
      {
        if (paramClass != null)
        {
          mName = paramString;
          mFallbackName = null;
          mType = paramClass;
          mTypeReference = TypeReference.createSpecializedTypeReference(paramClass);
          mHash = (mName.hashCode() ^ mTypeReference.hashCode());
          return;
        }
        throw new NullPointerException("Type needs to be non-null");
      }
      throw new NullPointerException("Key needs a valid name");
    }
    
    public Key(String paramString, Class<T> paramClass, long paramLong)
    {
      if (paramString != null)
      {
        if (paramClass != null)
        {
          mName = paramString;
          mFallbackName = null;
          mType = paramClass;
          mVendorId = paramLong;
          mTypeReference = TypeReference.createSpecializedTypeReference(paramClass);
          mHash = (mName.hashCode() ^ mTypeReference.hashCode());
          return;
        }
        throw new NullPointerException("Type needs to be non-null");
      }
      throw new NullPointerException("Key needs a valid name");
    }
    
    public Key(String paramString1, String paramString2, Class<T> paramClass)
    {
      if (paramString1 != null)
      {
        if (paramClass != null)
        {
          mName = paramString1;
          mFallbackName = paramString2;
          mType = paramClass;
          mTypeReference = TypeReference.createSpecializedTypeReference(paramClass);
          mHash = (mName.hashCode() ^ mTypeReference.hashCode());
          return;
        }
        throw new NullPointerException("Type needs to be non-null");
      }
      throw new NullPointerException("Key needs a valid name");
    }
    
    public final boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (hashCode() == paramObject.hashCode()))
      {
        if ((paramObject instanceof CaptureResult.Key)) {
          paramObject = ((CaptureResult.Key)paramObject).getNativeKey();
        }
        for (;;)
        {
          break;
          if ((paramObject instanceof CaptureRequest.Key))
          {
            paramObject = ((CaptureRequest.Key)paramObject).getNativeKey();
          }
          else if ((paramObject instanceof CameraCharacteristics.Key))
          {
            paramObject = ((CameraCharacteristics.Key)paramObject).getNativeKey();
          }
          else
          {
            if (!(paramObject instanceof Key)) {
              break label131;
            }
            paramObject = (Key)paramObject;
          }
        }
        if ((!mName.equals(mName)) || (!mTypeReference.equals(mTypeReference))) {
          bool = false;
        }
        return bool;
        label131:
        return false;
      }
      return false;
    }
    
    public final String getName()
    {
      return mName;
    }
    
    public final int getTag()
    {
      if (!mHasTag)
      {
        mTag = CameraMetadataNative.getTag(mName, mVendorId);
        mHasTag = true;
      }
      return mTag;
    }
    
    public final Class<T> getType()
    {
      return mType;
    }
    
    public final TypeReference<T> getTypeReference()
    {
      return mTypeReference;
    }
    
    public final long getVendorId()
    {
      return mVendorId;
    }
    
    public final int hashCode()
    {
      return mHash;
    }
  }
}
