package android.hardware.camera2.params;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.utils.HashCodeHelpers;
import android.hardware.camera2.utils.SurfaceUtils;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.renderscript.Allocation;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public final class StreamConfigurationMap
{
  private static final long DURATION_20FPS_NS = 50000000L;
  private static final int DURATION_MIN_FRAME = 0;
  private static final int DURATION_STALL = 1;
  private static final int HAL_DATASPACE_DEPTH = 4096;
  private static final int HAL_DATASPACE_RANGE_SHIFT = 27;
  private static final int HAL_DATASPACE_STANDARD_SHIFT = 16;
  private static final int HAL_DATASPACE_TRANSFER_SHIFT = 22;
  private static final int HAL_DATASPACE_UNKNOWN = 0;
  private static final int HAL_DATASPACE_V0_JFIF = 146931712;
  private static final int HAL_PIXEL_FORMAT_BLOB = 33;
  private static final int HAL_PIXEL_FORMAT_IMPLEMENTATION_DEFINED = 34;
  private static final int HAL_PIXEL_FORMAT_RAW10 = 37;
  private static final int HAL_PIXEL_FORMAT_RAW12 = 38;
  private static final int HAL_PIXEL_FORMAT_RAW16 = 32;
  private static final int HAL_PIXEL_FORMAT_RAW_OPAQUE = 36;
  private static final int HAL_PIXEL_FORMAT_Y16 = 540422489;
  private static final int HAL_PIXEL_FORMAT_YCbCr_420_888 = 35;
  private static final String TAG = "StreamConfigurationMap";
  private final SparseIntArray mAllOutputFormats = new SparseIntArray();
  private final StreamConfiguration[] mConfigurations;
  private final StreamConfiguration[] mDepthConfigurations;
  private final StreamConfigurationDuration[] mDepthMinFrameDurations;
  private final SparseIntArray mDepthOutputFormats = new SparseIntArray();
  private final StreamConfigurationDuration[] mDepthStallDurations;
  private final SparseIntArray mHighResOutputFormats = new SparseIntArray();
  private final HighSpeedVideoConfiguration[] mHighSpeedVideoConfigurations;
  private final HashMap<Range<Integer>, Integer> mHighSpeedVideoFpsRangeMap = new HashMap();
  private final HashMap<Size, Integer> mHighSpeedVideoSizeMap = new HashMap();
  private final SparseIntArray mInputFormats = new SparseIntArray();
  private final ReprocessFormatsMap mInputOutputFormatsMap;
  private final boolean mListHighResolution;
  private final StreamConfigurationDuration[] mMinFrameDurations;
  private final SparseIntArray mOutputFormats = new SparseIntArray();
  private final StreamConfigurationDuration[] mStallDurations;
  
  public StreamConfigurationMap(StreamConfiguration[] paramArrayOfStreamConfiguration1, StreamConfigurationDuration[] paramArrayOfStreamConfigurationDuration1, StreamConfigurationDuration[] paramArrayOfStreamConfigurationDuration2, StreamConfiguration[] paramArrayOfStreamConfiguration2, StreamConfigurationDuration[] paramArrayOfStreamConfigurationDuration3, StreamConfigurationDuration[] paramArrayOfStreamConfigurationDuration4, HighSpeedVideoConfiguration[] paramArrayOfHighSpeedVideoConfiguration, ReprocessFormatsMap paramReprocessFormatsMap, boolean paramBoolean)
  {
    if (paramArrayOfStreamConfiguration1 == null)
    {
      Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfiguration2, "depthConfigurations");
      mConfigurations = new StreamConfiguration[0];
      mMinFrameDurations = new StreamConfigurationDuration[0];
      mStallDurations = new StreamConfigurationDuration[0];
    }
    else
    {
      mConfigurations = ((StreamConfiguration[])Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfiguration1, "configurations"));
      mMinFrameDurations = ((StreamConfigurationDuration[])Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfigurationDuration1, "minFrameDurations"));
      mStallDurations = ((StreamConfigurationDuration[])Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfigurationDuration2, "stallDurations"));
    }
    mListHighResolution = paramBoolean;
    if (paramArrayOfStreamConfiguration2 == null)
    {
      mDepthConfigurations = new StreamConfiguration[0];
      mDepthMinFrameDurations = new StreamConfigurationDuration[0];
      mDepthStallDurations = new StreamConfigurationDuration[0];
    }
    else
    {
      mDepthConfigurations = ((StreamConfiguration[])Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfiguration2, "depthConfigurations"));
      mDepthMinFrameDurations = ((StreamConfigurationDuration[])Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfigurationDuration3, "depthMinFrameDurations"));
      mDepthStallDurations = ((StreamConfigurationDuration[])Preconditions.checkArrayElementsNotNull(paramArrayOfStreamConfigurationDuration4, "depthStallDurations"));
    }
    if (paramArrayOfHighSpeedVideoConfiguration == null) {
      mHighSpeedVideoConfigurations = new HighSpeedVideoConfiguration[0];
    } else {
      mHighSpeedVideoConfigurations = ((HighSpeedVideoConfiguration[])Preconditions.checkArrayElementsNotNull(paramArrayOfHighSpeedVideoConfiguration, "highSpeedVideoConfigurations"));
    }
    int m;
    for (paramArrayOfStreamConfiguration2 : mConfigurations)
    {
      int k = paramArrayOfStreamConfiguration2.getFormat();
      if (paramArrayOfStreamConfiguration2.isOutput())
      {
        mAllOutputFormats.put(k, mAllOutputFormats.get(k) + 1);
        long l1 = 0L;
        long l2 = l1;
        if (mListHighResolution)
        {
          paramArrayOfStreamConfigurationDuration1 = mMinFrameDurations;
          m = paramArrayOfStreamConfigurationDuration1.length;
          for (int n = 0;; n++)
          {
            l2 = l1;
            if (n >= m) {
              break;
            }
            paramArrayOfStreamConfigurationDuration3 = paramArrayOfStreamConfigurationDuration1[n];
            if ((paramArrayOfStreamConfigurationDuration3.getFormat() == k) && (paramArrayOfStreamConfigurationDuration3.getWidth() == paramArrayOfStreamConfiguration2.getSize().getWidth()) && (paramArrayOfStreamConfigurationDuration3.getHeight() == paramArrayOfStreamConfiguration2.getSize().getHeight()))
            {
              l2 = paramArrayOfStreamConfigurationDuration3.getDuration();
              break;
            }
          }
        }
        if (l2 <= 50000000L) {
          paramArrayOfStreamConfigurationDuration1 = mOutputFormats;
        } else {
          paramArrayOfStreamConfigurationDuration1 = mHighResOutputFormats;
        }
      }
      else
      {
        paramArrayOfStreamConfigurationDuration1 = mInputFormats;
      }
      paramArrayOfStreamConfigurationDuration1.put(k, paramArrayOfStreamConfigurationDuration1.get(k) + 1);
    }
    for (paramArrayOfStreamConfigurationDuration2 : mDepthConfigurations) {
      if (paramArrayOfStreamConfigurationDuration2.isOutput()) {
        mDepthOutputFormats.put(paramArrayOfStreamConfigurationDuration2.getFormat(), mDepthOutputFormats.get(paramArrayOfStreamConfigurationDuration2.getFormat()) + 1);
      }
    }
    if ((paramArrayOfStreamConfiguration1 != null) && (mOutputFormats.indexOfKey(34) < 0)) {
      throw new AssertionError("At least one stream configuration for IMPLEMENTATION_DEFINED must exist");
    }
    for (paramArrayOfStreamConfiguration1 : mHighSpeedVideoConfigurations)
    {
      paramArrayOfStreamConfigurationDuration3 = paramArrayOfStreamConfiguration1.getSize();
      paramArrayOfStreamConfiguration2 = paramArrayOfStreamConfiguration1.getFpsRange();
      paramArrayOfStreamConfigurationDuration1 = (Integer)mHighSpeedVideoSizeMap.get(paramArrayOfStreamConfigurationDuration3);
      paramArrayOfStreamConfiguration1 = paramArrayOfStreamConfigurationDuration1;
      if (paramArrayOfStreamConfigurationDuration1 == null) {
        paramArrayOfStreamConfiguration1 = Integer.valueOf(0);
      }
      mHighSpeedVideoSizeMap.put(paramArrayOfStreamConfigurationDuration3, Integer.valueOf(paramArrayOfStreamConfiguration1.intValue() + 1));
      paramArrayOfStreamConfiguration1 = (Integer)mHighSpeedVideoFpsRangeMap.get(paramArrayOfStreamConfiguration2);
      if (paramArrayOfStreamConfiguration1 == null) {
        paramArrayOfStreamConfiguration1 = Integer.valueOf(0);
      }
      mHighSpeedVideoFpsRangeMap.put(paramArrayOfStreamConfiguration2, Integer.valueOf(paramArrayOfStreamConfiguration1.intValue() + 1));
    }
    mInputOutputFormatsMap = paramReprocessFormatsMap;
  }
  
  private void appendHighResOutputsString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("HighResolutionOutputs(");
    for (int k : getOutputFormats())
    {
      Size[] arrayOfSize = getHighResolutionOutputSizes(k);
      if (arrayOfSize != null)
      {
        int m = arrayOfSize.length;
        for (int n = 0; n < m; n++)
        {
          Size localSize = arrayOfSize[n];
          long l1 = getOutputMinFrameDuration(k, localSize);
          long l2 = getOutputStallDuration(k, localSize);
          paramStringBuilder.append(String.format("[w:%d, h:%d, format:%s(%d), min_duration:%d, stall:%d], ", new Object[] { Integer.valueOf(localSize.getWidth()), Integer.valueOf(localSize.getHeight()), formatToString(k), Integer.valueOf(k), Long.valueOf(l1), Long.valueOf(l2) }));
        }
      }
    }
    if (paramStringBuilder.charAt(paramStringBuilder.length() - 1) == ' ') {
      paramStringBuilder.delete(paramStringBuilder.length() - 2, paramStringBuilder.length());
    }
    paramStringBuilder.append(")");
  }
  
  private void appendHighSpeedVideoConfigurationsString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("HighSpeedVideoConfigurations(");
    for (Size localSize : getHighSpeedVideoSizes()) {
      for (Range localRange : getHighSpeedVideoFpsRangesFor(localSize)) {
        paramStringBuilder.append(String.format("[w:%d, h:%d, min_fps:%d, max_fps:%d], ", new Object[] { Integer.valueOf(localSize.getWidth()), Integer.valueOf(localSize.getHeight()), localRange.getLower(), localRange.getUpper() }));
      }
    }
    if (paramStringBuilder.charAt(paramStringBuilder.length() - 1) == ' ') {
      paramStringBuilder.delete(paramStringBuilder.length() - 2, paramStringBuilder.length());
    }
    paramStringBuilder.append(")");
  }
  
  private void appendInputsString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("Inputs(");
    for (int k : getInputFormats()) {
      for (Size localSize : getInputSizes(k)) {
        paramStringBuilder.append(String.format("[w:%d, h:%d, format:%s(%d)], ", new Object[] { Integer.valueOf(localSize.getWidth()), Integer.valueOf(localSize.getHeight()), formatToString(k), Integer.valueOf(k) }));
      }
    }
    if (paramStringBuilder.charAt(paramStringBuilder.length() - 1) == ' ') {
      paramStringBuilder.delete(paramStringBuilder.length() - 2, paramStringBuilder.length());
    }
    paramStringBuilder.append(")");
  }
  
  private void appendOutputsString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("Outputs(");
    for (int k : getOutputFormats()) {
      for (Size localSize : getOutputSizes(k))
      {
        long l1 = getOutputMinFrameDuration(k, localSize);
        long l2 = getOutputStallDuration(k, localSize);
        paramStringBuilder.append(String.format("[w:%d, h:%d, format:%s(%d), min_duration:%d, stall:%d], ", new Object[] { Integer.valueOf(localSize.getWidth()), Integer.valueOf(localSize.getHeight()), formatToString(k), Integer.valueOf(k), Long.valueOf(l1), Long.valueOf(l2) }));
      }
    }
    if (paramStringBuilder.charAt(paramStringBuilder.length() - 1) == ' ') {
      paramStringBuilder.delete(paramStringBuilder.length() - 2, paramStringBuilder.length());
    }
    paramStringBuilder.append(")");
  }
  
  private void appendValidOutputFormatsForInputString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("ValidOutputFormatsForInput(");
    for (int k : getInputFormats())
    {
      paramStringBuilder.append(String.format("[in:%s(%d), out:", new Object[] { formatToString(k), Integer.valueOf(k) }));
      int[] arrayOfInt2 = getValidOutputFormatsForInput(k);
      for (k = 0; k < arrayOfInt2.length; k++)
      {
        paramStringBuilder.append(String.format("%s(%d)", new Object[] { formatToString(arrayOfInt2[k]), Integer.valueOf(arrayOfInt2[k]) }));
        if (k < arrayOfInt2.length - 1) {
          paramStringBuilder.append(", ");
        }
      }
      paramStringBuilder.append("], ");
    }
    if (paramStringBuilder.charAt(paramStringBuilder.length() - 1) == ' ') {
      paramStringBuilder.delete(paramStringBuilder.length() - 2, paramStringBuilder.length());
    }
    paramStringBuilder.append(")");
  }
  
  private static <T> boolean arrayContains(T[] paramArrayOfT, T paramT)
  {
    if (paramArrayOfT == null) {
      return false;
    }
    int i = paramArrayOfT.length;
    for (int j = 0; j < i; j++) {
      if (Objects.equals(paramArrayOfT[j], paramT)) {
        return true;
      }
    }
    return false;
  }
  
  static int checkArgumentFormat(int paramInt)
  {
    if ((!ImageFormat.isPublicFormat(paramInt)) && (!PixelFormat.isPublicFormat(paramInt))) {
      throw new IllegalArgumentException(String.format("format 0x%x was not defined in either ImageFormat or PixelFormat", new Object[] { Integer.valueOf(paramInt) }));
    }
    return paramInt;
  }
  
  static int checkArgumentFormatInternal(int paramInt)
  {
    if (paramInt != 36) {
      if (paramInt != 256)
      {
        if (paramInt == 540422489) {}
      }
      else {
        switch (paramInt)
        {
        default: 
          return checkArgumentFormat(paramInt);
          throw new IllegalArgumentException("ImageFormat.JPEG is an unknown internal format");
        }
      }
    }
    return paramInt;
  }
  
  private int checkArgumentFormatSupported(int paramInt, boolean paramBoolean)
  {
    checkArgumentFormat(paramInt);
    int i = imageFormatToInternal(paramInt);
    int j = imageFormatToDataspace(paramInt);
    if (paramBoolean)
    {
      if (j == 4096)
      {
        if (mDepthOutputFormats.indexOfKey(i) >= 0) {
          return paramInt;
        }
      }
      else if (mAllOutputFormats.indexOfKey(i) >= 0) {
        return paramInt;
      }
    }
    else if (mInputFormats.indexOfKey(i) >= 0) {
      return paramInt;
    }
    throw new IllegalArgumentException(String.format("format %x is not supported by this stream configuration map", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  static int depthFormatToPublic(int paramInt)
  {
    if (paramInt != 256)
    {
      if (paramInt != 540422489)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown DATASPACE_DEPTH format ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 34: 
          throw new IllegalArgumentException("IMPLEMENTATION_DEFINED must not leak to public API");
        case 33: 
          return 257;
        }
        return 4098;
      }
      return 1144402265;
    }
    throw new IllegalArgumentException("ImageFormat.JPEG is an unknown internal format");
  }
  
  private String formatToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 1144402265: 
      return "DEPTH16";
    case 842094169: 
      return "YV12";
    case 540422489: 
      return "Y16";
    case 538982489: 
      return "Y8";
    case 4098: 
      return "RAW_DEPTH";
    case 257: 
      return "DEPTH_POINT_CLOUD";
    case 256: 
      return "JPEG";
    case 37: 
      return "RAW10";
    case 36: 
      return "RAW_PRIVATE";
    case 35: 
      return "YUV_420_888";
    case 34: 
      return "PRIVATE";
    case 32: 
      return "RAW_SENSOR";
    case 20: 
      return "YUY2";
    case 17: 
      return "NV21";
    case 16: 
      return "NV16";
    case 4: 
      return "RGB_565";
    case 3: 
      return "RGB_888";
    case 2: 
      return "RGBX_8888";
    }
    return "RGBA_8888";
  }
  
  private StreamConfigurationDuration[] getDurations(int paramInt1, int paramInt2)
  {
    StreamConfigurationDuration[] arrayOfStreamConfigurationDuration;
    switch (paramInt1)
    {
    default: 
      throw new IllegalArgumentException("duration was invalid");
    case 1: 
      if (paramInt2 == 4096) {
        arrayOfStreamConfigurationDuration = mDepthStallDurations;
      } else {
        arrayOfStreamConfigurationDuration = mStallDurations;
      }
      return arrayOfStreamConfigurationDuration;
    }
    if (paramInt2 == 4096) {
      arrayOfStreamConfigurationDuration = mDepthMinFrameDurations;
    } else {
      arrayOfStreamConfigurationDuration = mMinFrameDurations;
    }
    return arrayOfStreamConfigurationDuration;
  }
  
  private SparseIntArray getFormatsMap(boolean paramBoolean)
  {
    SparseIntArray localSparseIntArray;
    if (paramBoolean) {
      localSparseIntArray = mAllOutputFormats;
    } else {
      localSparseIntArray = mInputFormats;
    }
    return localSparseIntArray;
  }
  
  private long getInternalFormatDuration(int paramInt1, int paramInt2, Size paramSize, int paramInt3)
  {
    if (isSupportedInternalConfiguration(paramInt1, paramInt2, paramSize))
    {
      for (StreamConfigurationDuration localStreamConfigurationDuration : getDurations(paramInt3, paramInt2)) {
        if ((localStreamConfigurationDuration.getFormat() == paramInt1) && (localStreamConfigurationDuration.getWidth() == paramSize.getWidth()) && (localStreamConfigurationDuration.getHeight() == paramSize.getHeight())) {
          return localStreamConfigurationDuration.getDuration();
        }
      }
      return 0L;
    }
    throw new IllegalArgumentException("size was not supported");
  }
  
  private Size[] getInternalFormatSizes(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    Object localObject1 = this;
    int i = paramInt1;
    boolean bool = paramBoolean1;
    int j = 4096;
    if ((paramInt2 == 4096) && (paramBoolean2)) {
      return new Size[0];
    }
    Object localObject2;
    if (!bool) {
      localObject2 = mInputFormats;
    } else if (paramInt2 == 4096) {
      localObject2 = mDepthOutputFormats;
    } else if (paramBoolean2) {
      localObject2 = mHighResOutputFormats;
    } else {
      localObject2 = mOutputFormats;
    }
    int k = ((SparseIntArray)localObject2).get(i);
    if (((bool) && (paramInt2 != 4096)) || ((k != 0) && ((!bool) || (paramInt2 == 4096) || (mAllOutputFormats.get(i) != 0))))
    {
      Size[] arrayOfSize = new Size[k];
      if (paramInt2 == 4096) {
        localObject2 = mDepthConfigurations;
      } else {
        localObject2 = mConfigurations;
      }
      if (paramInt2 == 4096) {
        localObject1 = mDepthMinFrameDurations;
      } else {
        localObject1 = mMinFrameDurations;
      }
      int m = localObject2.length;
      int n = 0;
      for (i = 0;; i++)
      {
        bool = paramBoolean1;
        if (i >= m) {
          break;
        }
        Object localObject3 = localObject2[i];
        int i1 = localObject3.getFormat();
        if (i1 == paramInt1) {
          if (localObject3.isOutput() == bool)
          {
            if ((bool) && (mListHighResolution))
            {
              long l1 = 0L;
              long l2;
              for (j = 0;; j++)
              {
                l2 = l1;
                if (j >= localObject1.length) {
                  break;
                }
                Object localObject4 = localObject1[j];
                if ((localObject4.getFormat() == i1) && (localObject4.getWidth() == localObject3.getSize().getWidth()) && (localObject4.getHeight() == localObject3.getSize().getHeight()))
                {
                  l2 = localObject4.getDuration();
                  break;
                }
              }
              j = 4096;
              if (paramInt2 != 4096)
              {
                if (l2 > 50000000L) {
                  bool = true;
                } else {
                  bool = false;
                }
                if (paramBoolean2 != bool) {
                  continue;
                }
              }
            }
            j = 4096;
            arrayOfSize[n] = localObject3.getSize();
            n++;
          }
          else
          {
            j = 4096;
          }
        }
      }
      if (n == k) {
        return arrayOfSize;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Too few sizes (expected ");
      ((StringBuilder)localObject2).append(k);
      ((StringBuilder)localObject2).append(", actual ");
      ((StringBuilder)localObject2).append(n);
      ((StringBuilder)localObject2).append(")");
      throw new AssertionError(((StringBuilder)localObject2).toString());
    }
    throw new IllegalArgumentException("format not available");
  }
  
  private int getPublicFormatCount(boolean paramBoolean)
  {
    int i = getFormatsMap(paramBoolean).size();
    int j = i;
    if (paramBoolean) {
      j = i + mDepthOutputFormats.size();
    }
    return j;
  }
  
  private Size[] getPublicFormatSizes(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      checkArgumentFormatSupported(paramInt, paramBoolean1);
      return getInternalFormatSizes(imageFormatToInternal(paramInt), imageFormatToDataspace(paramInt), paramBoolean1, paramBoolean2);
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return null;
  }
  
  private int[] getPublicFormats(boolean paramBoolean)
  {
    int[] arrayOfInt = new int[getPublicFormatCount(paramBoolean)];
    Object localObject = getFormatsMap(paramBoolean);
    int i = 0;
    int j = 0;
    int k = 0;
    while (k < ((SparseIntArray)localObject).size())
    {
      arrayOfInt[j] = imageFormatToPublic(((SparseIntArray)localObject).keyAt(k));
      k++;
      j++;
    }
    k = j;
    if (paramBoolean) {
      for (;;)
      {
        k = j;
        if (i >= mDepthOutputFormats.size()) {
          break;
        }
        arrayOfInt[j] = depthFormatToPublic(mDepthOutputFormats.keyAt(i));
        j++;
        i++;
      }
    }
    if (arrayOfInt.length == k) {
      return arrayOfInt;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Too few formats ");
    ((StringBuilder)localObject).append(k);
    ((StringBuilder)localObject).append(", expected ");
    ((StringBuilder)localObject).append(arrayOfInt.length);
    throw new AssertionError(((StringBuilder)localObject).toString());
  }
  
  static int imageFormatToDataspace(int paramInt)
  {
    if ((paramInt != 4098) && (paramInt != 1144402265)) {
      switch (paramInt)
      {
      default: 
        return 0;
      case 256: 
        return 146931712;
      }
    }
    return 4096;
  }
  
  static int imageFormatToInternal(int paramInt)
  {
    if (paramInt != 4098)
    {
      if (paramInt != 1144402265)
      {
        switch (paramInt)
        {
        default: 
          return paramInt;
        }
        return 33;
      }
      return 540422489;
    }
    return 32;
  }
  
  public static int[] imageFormatToInternal(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return null;
    }
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      paramArrayOfInt[i] = imageFormatToInternal(paramArrayOfInt[i]);
    }
    return paramArrayOfInt;
  }
  
  static int imageFormatToPublic(int paramInt)
  {
    if (paramInt != 33)
    {
      if (paramInt != 256) {
        return paramInt;
      }
      throw new IllegalArgumentException("ImageFormat.JPEG is an unknown internal format");
    }
    return 256;
  }
  
  static int[] imageFormatToPublic(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return null;
    }
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      paramArrayOfInt[i] = imageFormatToPublic(paramArrayOfInt[i]);
    }
    return paramArrayOfInt;
  }
  
  public static <T> boolean isOutputSupportedFor(Class<T> paramClass)
  {
    Preconditions.checkNotNull(paramClass, "klass must not be null");
    if (paramClass == ImageReader.class) {
      return true;
    }
    if (paramClass == MediaRecorder.class) {
      return true;
    }
    if (paramClass == MediaCodec.class) {
      return true;
    }
    if (paramClass == Allocation.class) {
      return true;
    }
    if (paramClass == SurfaceHolder.class) {
      return true;
    }
    return paramClass == SurfaceTexture.class;
  }
  
  private boolean isSupportedInternalConfiguration(int paramInt1, int paramInt2, Size paramSize)
  {
    StreamConfiguration[] arrayOfStreamConfiguration;
    if (paramInt2 == 4096) {
      arrayOfStreamConfiguration = mDepthConfigurations;
    } else {
      arrayOfStreamConfiguration = mConfigurations;
    }
    for (paramInt2 = 0; paramInt2 < arrayOfStreamConfiguration.length; paramInt2++) {
      if ((arrayOfStreamConfiguration[paramInt2].getFormat() == paramInt1) && (arrayOfStreamConfiguration[paramInt2].getSize().equals(paramSize))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof StreamConfigurationMap))
    {
      paramObject = (StreamConfigurationMap)paramObject;
      if ((Arrays.equals(mConfigurations, mConfigurations)) && (Arrays.equals(mMinFrameDurations, mMinFrameDurations)) && (Arrays.equals(mStallDurations, mStallDurations)) && (Arrays.equals(mDepthConfigurations, mDepthConfigurations)) && (Arrays.equals(mHighSpeedVideoConfigurations, mHighSpeedVideoConfigurations))) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public Size[] getHighResolutionOutputSizes(int paramInt)
  {
    if (!mListHighResolution) {
      return null;
    }
    return getPublicFormatSizes(paramInt, true, true);
  }
  
  public Range<Integer>[] getHighSpeedVideoFpsRanges()
  {
    Set localSet = mHighSpeedVideoFpsRangeMap.keySet();
    return (Range[])localSet.toArray(new Range[localSet.size()]);
  }
  
  public Range<Integer>[] getHighSpeedVideoFpsRangesFor(Size paramSize)
  {
    Object localObject1 = (Integer)mHighSpeedVideoSizeMap.get(paramSize);
    int i = 0;
    if ((localObject1 != null) && (((Integer)localObject1).intValue() != 0))
    {
      Range[] arrayOfRange = new Range[((Integer)localObject1).intValue()];
      int j = 0;
      localObject1 = mHighSpeedVideoConfigurations;
      int k = localObject1.length;
      while (i < k)
      {
        Object localObject2 = localObject1[i];
        int m = j;
        if (paramSize.equals(localObject2.getSize()))
        {
          arrayOfRange[j] = localObject2.getFpsRange();
          m = j + 1;
        }
        i++;
        j = m;
      }
      return arrayOfRange;
    }
    throw new IllegalArgumentException(String.format("Size %s does not support high speed video recording", new Object[] { paramSize }));
  }
  
  public Size[] getHighSpeedVideoSizes()
  {
    Set localSet = mHighSpeedVideoSizeMap.keySet();
    return (Size[])localSet.toArray(new Size[localSet.size()]);
  }
  
  public Size[] getHighSpeedVideoSizesFor(Range<Integer> paramRange)
  {
    Object localObject1 = (Integer)mHighSpeedVideoFpsRangeMap.get(paramRange);
    int i = 0;
    if ((localObject1 != null) && (((Integer)localObject1).intValue() != 0))
    {
      Size[] arrayOfSize = new Size[((Integer)localObject1).intValue()];
      int j = 0;
      localObject1 = mHighSpeedVideoConfigurations;
      int k = localObject1.length;
      while (i < k)
      {
        Object localObject2 = localObject1[i];
        int m = j;
        if (paramRange.equals(localObject2.getFpsRange()))
        {
          arrayOfSize[j] = localObject2.getSize();
          m = j + 1;
        }
        i++;
        j = m;
      }
      return arrayOfSize;
    }
    throw new IllegalArgumentException(String.format("FpsRange %s does not support high speed video recording", new Object[] { paramRange }));
  }
  
  public final int[] getInputFormats()
  {
    return getPublicFormats(false);
  }
  
  public Size[] getInputSizes(int paramInt)
  {
    return getPublicFormatSizes(paramInt, false, false);
  }
  
  public final int[] getOutputFormats()
  {
    return getPublicFormats(true);
  }
  
  public long getOutputMinFrameDuration(int paramInt, Size paramSize)
  {
    Preconditions.checkNotNull(paramSize, "size must not be null");
    checkArgumentFormatSupported(paramInt, true);
    return getInternalFormatDuration(imageFormatToInternal(paramInt), imageFormatToDataspace(paramInt), paramSize, 0);
  }
  
  public <T> long getOutputMinFrameDuration(Class<T> paramClass, Size paramSize)
  {
    if (isOutputSupportedFor(paramClass)) {
      return getInternalFormatDuration(34, 0, paramSize, 0);
    }
    throw new IllegalArgumentException("klass was not supported");
  }
  
  public Size[] getOutputSizes(int paramInt)
  {
    return getPublicFormatSizes(paramInt, true, false);
  }
  
  public <T> Size[] getOutputSizes(Class<T> paramClass)
  {
    if (!isOutputSupportedFor(paramClass)) {
      return null;
    }
    return getInternalFormatSizes(34, 0, true, false);
  }
  
  public long getOutputStallDuration(int paramInt, Size paramSize)
  {
    checkArgumentFormatSupported(paramInt, true);
    return getInternalFormatDuration(imageFormatToInternal(paramInt), imageFormatToDataspace(paramInt), paramSize, 1);
  }
  
  public <T> long getOutputStallDuration(Class<T> paramClass, Size paramSize)
  {
    if (isOutputSupportedFor(paramClass)) {
      return getInternalFormatDuration(34, 0, paramSize, 1);
    }
    throw new IllegalArgumentException("klass was not supported");
  }
  
  public final int[] getValidOutputFormatsForInput(int paramInt)
  {
    if (mInputOutputFormatsMap == null) {
      return new int[0];
    }
    return mInputOutputFormatsMap.getOutputs(paramInt);
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCodeGeneric(new Object[][] { mConfigurations, mMinFrameDurations, mStallDurations, mDepthConfigurations, mHighSpeedVideoConfigurations });
  }
  
  public boolean isOutputSupportedFor(int paramInt)
  {
    checkArgumentFormat(paramInt);
    int i = imageFormatToInternal(paramInt);
    paramInt = imageFormatToDataspace(paramInt);
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramInt == 4096)
    {
      if (mDepthOutputFormats.indexOfKey(i) >= 0) {
        bool2 = true;
      }
      return bool2;
    }
    bool2 = bool1;
    if (getFormatsMap(true).indexOfKey(i) >= 0) {
      bool2 = true;
    }
    return bool2;
  }
  
  public boolean isOutputSupportedFor(Surface paramSurface)
  {
    Preconditions.checkNotNull(paramSurface, "surface must not be null");
    Size localSize = SurfaceUtils.getSurfaceSize(paramSurface);
    int i = SurfaceUtils.getSurfaceFormat(paramSurface);
    int j = SurfaceUtils.getSurfaceDataspace(paramSurface);
    boolean bool = SurfaceUtils.isFlexibleConsumer(paramSurface);
    if (j != 4096) {
      paramSurface = mConfigurations;
    }
    for (Object localObject : mDepthConfigurations) {
      if ((localObject.getFormat() == i) && (localObject.isOutput()))
      {
        if (localObject.getSize().equals(localSize)) {
          return true;
        }
        if ((bool) && (localObject.getSize().getWidth() <= 1920)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("StreamConfiguration(");
    appendOutputsString(localStringBuilder);
    localStringBuilder.append(", ");
    appendHighResOutputsString(localStringBuilder);
    localStringBuilder.append(", ");
    appendInputsString(localStringBuilder);
    localStringBuilder.append(", ");
    appendValidOutputFormatsForInputString(localStringBuilder);
    localStringBuilder.append(", ");
    appendHighSpeedVideoConfigurationsString(localStringBuilder);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
