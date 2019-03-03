package android.hardware.camera2.utils;

import android.app.ActivityThread;
import android.hardware.camera2.legacy.LegacyCameraDevice;
import android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.SystemProperties;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SurfaceUtils
{
  public SurfaceUtils() {}
  
  public static void checkConstrainedHighSpeedSurfaces(Collection<Surface> paramCollection, Range<Integer> paramRange, StreamConfigurationMap paramStreamConfigurationMap)
  {
    if ((paramCollection != null) && (paramCollection.size() != 0) && (paramCollection.size() <= 2))
    {
      if (isPrivilegedApp()) {
        return;
      }
      if (paramRange == null)
      {
        paramRange = Arrays.asList(paramStreamConfigurationMap.getHighSpeedVideoSizes());
      }
      else
      {
        localObject = paramStreamConfigurationMap.getHighSpeedVideoFpsRanges();
        if (!Arrays.asList((Object[])localObject).contains(paramRange)) {
          break label300;
        }
        paramRange = Arrays.asList(paramStreamConfigurationMap.getHighSpeedVideoSizesFor(paramRange));
      }
      Object localObject = paramCollection.iterator();
      while (((Iterator)localObject).hasNext())
      {
        Surface localSurface = (Surface)((Iterator)localObject).next();
        checkHighSpeedSurfaceFormat(localSurface);
        paramStreamConfigurationMap = getSurfaceSize(localSurface);
        if (paramRange.contains(paramStreamConfigurationMap))
        {
          if ((!isSurfaceForPreview(localSurface)) && (!isSurfaceForHwVideoEncoder(localSurface))) {
            throw new IllegalArgumentException("This output surface is neither preview nor hardware video encoding surface");
          }
          if ((isSurfaceForPreview(localSurface)) && (isSurfaceForHwVideoEncoder(localSurface))) {
            throw new IllegalArgumentException("This output surface can not be both preview and hardware video encoding surface");
          }
        }
        else
        {
          paramCollection = new StringBuilder();
          paramCollection.append("Surface size ");
          paramCollection.append(paramStreamConfigurationMap.toString());
          paramCollection.append(" is not part of the high speed supported size list ");
          paramCollection.append(Arrays.toString(paramRange.toArray()));
          throw new IllegalArgumentException(paramCollection.toString());
        }
      }
      if (paramCollection.size() == 2)
      {
        paramCollection = paramCollection.iterator();
        boolean bool = isSurfaceForPreview((Surface)paramCollection.next());
        if (bool == isSurfaceForPreview((Surface)paramCollection.next())) {
          throw new IllegalArgumentException("The 2 output surfaces must have different type");
        }
      }
      return;
      label300:
      paramCollection = new StringBuilder();
      paramCollection.append("Fps range ");
      paramCollection.append(paramRange.toString());
      paramCollection.append(" in the request is not a supported high speed fps range ");
      paramCollection.append(Arrays.toString((Object[])localObject));
      throw new IllegalArgumentException(paramCollection.toString());
    }
    throw new IllegalArgumentException("Output target surface list must not be null and the size must be 1 or 2");
  }
  
  private static void checkHighSpeedSurfaceFormat(Surface paramSurface)
  {
    int i = getSurfaceFormat(paramSurface);
    if (i == 34) {
      return;
    }
    paramSurface = new StringBuilder();
    paramSurface.append("Surface format(");
    paramSurface.append(i);
    paramSurface.append(") is not for preview or hardware video encoding!");
    throw new IllegalArgumentException(paramSurface.toString());
  }
  
  public static int getSurfaceDataspace(Surface paramSurface)
  {
    try
    {
      int i = LegacyCameraDevice.detectSurfaceDataspace(paramSurface);
      return i;
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface)
    {
      throw new IllegalArgumentException("Surface was abandoned", paramSurface);
    }
  }
  
  public static int getSurfaceFormat(Surface paramSurface)
  {
    try
    {
      int i = LegacyCameraDevice.detectSurfaceType(paramSurface);
      return i;
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface)
    {
      throw new IllegalArgumentException("Surface was abandoned", paramSurface);
    }
  }
  
  public static long getSurfaceId(Surface paramSurface)
  {
    try
    {
      long l = LegacyCameraDevice.getSurfaceId(paramSurface);
      return l;
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface) {}
    return 0L;
  }
  
  public static Size getSurfaceSize(Surface paramSurface)
  {
    try
    {
      paramSurface = LegacyCameraDevice.getSurfaceSize(paramSurface);
      return paramSurface;
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface)
    {
      throw new IllegalArgumentException("Surface was abandoned", paramSurface);
    }
  }
  
  public static boolean isFlexibleConsumer(Surface paramSurface)
  {
    return LegacyCameraDevice.isFlexibleConsumer(paramSurface);
  }
  
  private static boolean isPrivilegedApp()
  {
    String str = ActivityThread.currentOpPackageName();
    Object localObject = SystemProperties.get("persist.camera.privapp.list");
    if (((String)localObject).length() > 0)
    {
      TextUtils.SimpleStringSplitter localSimpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
      localSimpleStringSplitter.setString((String)localObject);
      localObject = localSimpleStringSplitter.iterator();
      while (((Iterator)localObject).hasNext()) {
        if (str.equals((String)((Iterator)localObject).next())) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean isSurfaceForHwVideoEncoder(Surface paramSurface)
  {
    return LegacyCameraDevice.isVideoEncoderConsumer(paramSurface);
  }
  
  public static boolean isSurfaceForPreview(Surface paramSurface)
  {
    return LegacyCameraDevice.isPreviewConsumer(paramSurface);
  }
}
