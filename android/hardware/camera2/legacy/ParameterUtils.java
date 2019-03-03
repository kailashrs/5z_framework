package android.hardware.camera2.legacy;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera.Area;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ParamsUtils;
import android.hardware.camera2.utils.SizeAreaComparator;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ParameterUtils
{
  private static final double ASPECT_RATIO_TOLERANCE = 0.05000000074505806D;
  public static final Camera.Area CAMERA_AREA_DEFAULT = new Camera.Area(new Rect(NORMALIZED_RECTANGLE_DEFAULT), 1);
  private static final boolean DEBUG = false;
  public static final Rect NORMALIZED_RECTANGLE_DEFAULT = new Rect(64536, 64536, 1000, 1000);
  public static final int NORMALIZED_RECTANGLE_MAX = 1000;
  public static final int NORMALIZED_RECTANGLE_MIN = -1000;
  public static final Rect RECTANGLE_EMPTY = new Rect(0, 0, 0, 0);
  private static final String TAG = "ParameterUtils";
  private static final int ZOOM_RATIO_MULTIPLIER = 100;
  
  private ParameterUtils()
  {
    throw new AssertionError();
  }
  
  public static boolean containsSize(List<Camera.Size> paramList, int paramInt1, int paramInt2)
  {
    Preconditions.checkNotNull(paramList, "sizeList must not be null");
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Camera.Size localSize = (Camera.Size)paramList.next();
      if ((height == paramInt2) && (width == paramInt1)) {
        return true;
      }
    }
    return false;
  }
  
  public static WeightedRectangle convertCameraAreaToActiveArrayRectangle(Rect paramRect, ZoomData paramZoomData, Camera.Area paramArea)
  {
    return convertCameraAreaToActiveArrayRectangle(paramRect, paramZoomData, paramArea, true);
  }
  
  private static WeightedRectangle convertCameraAreaToActiveArrayRectangle(Rect paramRect, ZoomData paramZoomData, Camera.Area paramArea, boolean paramBoolean)
  {
    paramRect = previewCrop;
    paramZoomData = reportedCrop;
    float f1 = paramRect.width() * 1.0F / 2000.0F;
    float f2 = paramRect.height() * 1.0F / 2000.0F;
    Matrix localMatrix = new Matrix();
    localMatrix.setTranslate(1000.0F, 1000.0F);
    localMatrix.postScale(f1, f2);
    localMatrix.postTranslate(left, top);
    if (!paramBoolean) {
      paramRect = paramZoomData;
    }
    paramZoomData = ParamsUtils.mapRect(localMatrix, rect);
    if (!paramZoomData.intersect(paramRect)) {
      paramZoomData.set(RECTANGLE_EMPTY);
    }
    if (weight < 0)
    {
      paramRect = new StringBuilder();
      paramRect.append("convertCameraAreaToMeteringRectangle - rectangle ");
      paramRect.append(stringFromArea(paramArea));
      paramRect.append(" has too small weight, clip to 0");
      Log.w("ParameterUtils", paramRect.toString());
    }
    return new WeightedRectangle(paramZoomData, weight);
  }
  
  private static Point convertCameraPointToActiveArrayPoint(Rect paramRect, ZoomData paramZoomData, Point paramPoint, boolean paramBoolean)
  {
    paramPoint = new Camera.Area(new Rect(x, y, x, y), 1);
    paramRect = convertCameraAreaToActiveArrayRectangle(paramRect, paramZoomData, paramPoint, paramBoolean);
    return new Point(rect.left, rect.top);
  }
  
  public static Face convertFaceFromLegacy(Camera.Face paramFace, Rect paramRect, ZoomData paramZoomData)
  {
    Preconditions.checkNotNull(paramFace, "face must not be null");
    Object localObject = new Camera.Area(rect, 1);
    localObject = convertCameraAreaToActiveArrayRectangle(paramRect, paramZoomData, (Camera.Area)localObject);
    Point localPoint1 = leftEye;
    Point localPoint2 = rightEye;
    Point localPoint3 = mouth;
    if ((localPoint1 != null) && (localPoint2 != null) && (localPoint3 != null) && (x != 63536) && (y != 63536) && (x != 63536) && (y != 63536) && (x != 63536) && (y != 63536))
    {
      localPoint2 = convertCameraPointToActiveArrayPoint(paramRect, paramZoomData, localPoint1, true);
      localPoint3 = convertCameraPointToActiveArrayPoint(paramRect, paramZoomData, localPoint2, true);
      paramRect = convertCameraPointToActiveArrayPoint(paramRect, paramZoomData, localPoint2, true);
      paramFace = ((WeightedRectangle)localObject).toFace(id, localPoint2, localPoint3, paramRect);
    }
    else
    {
      paramFace = ((WeightedRectangle)localObject).toFace();
    }
    return paramFace;
  }
  
  public static MeteringData convertMeteringRectangleToLegacy(Rect paramRect, MeteringRectangle paramMeteringRectangle, ZoomData paramZoomData)
  {
    Rect localRect1 = previewCrop;
    float f1 = 2000.0F / localRect1.width();
    float f2 = 2000.0F / localRect1.height();
    Object localObject = new Matrix();
    ((Matrix)localObject).setTranslate(-left, -top);
    ((Matrix)localObject).postScale(f1, f2);
    ((Matrix)localObject).postTranslate(-1000.0F, -1000.0F);
    Rect localRect2 = ParamsUtils.mapRect((Matrix)localObject, paramMeteringRectangle.getRect());
    localObject = new Rect(localRect2);
    if (!((Rect)localObject).intersect(NORMALIZED_RECTANGLE_DEFAULT))
    {
      Log.w("ParameterUtils", "convertMeteringRectangleToLegacy - metering rectangle too small, no metering will be done");
      ((Rect)localObject).set(RECTANGLE_EMPTY);
      localObject = new Camera.Area(RECTANGLE_EMPTY, 0);
    }
    else
    {
      localObject = new Camera.Area((Rect)localObject, paramMeteringRectangle.getMeteringWeight());
    }
    Rect localRect3 = paramMeteringRectangle.getRect();
    if (!localRect3.intersect(localRect1)) {
      localRect3.set(RECTANGLE_EMPTY);
    }
    return new MeteringData((Camera.Area)localObject, localRect3, convertCameraAreaToActiveArrayRectangleCamera.AreagetMeteringWeightrect);
  }
  
  public static ZoomData convertScalerCropRegion(Rect paramRect1, Rect paramRect2, Size paramSize, Camera.Parameters paramParameters)
  {
    Rect localRect1 = new Rect(0, 0, paramRect1.width(), paramRect1.height());
    paramRect1 = paramRect2;
    if (paramRect2 == null) {
      paramRect1 = localRect1;
    }
    Rect localRect2 = new Rect();
    paramRect2 = new Rect();
    return new ZoomData(getClosestAvailableZoomCrop(paramParameters, localRect1, paramSize, paramRect1, localRect2, paramRect2), paramRect2, localRect2);
  }
  
  public static Size convertSize(Camera.Size paramSize)
  {
    Preconditions.checkNotNull(paramSize, "size must not be null");
    return new Size(width, height);
  }
  
  public static List<Size> convertSizeList(List<Camera.Size> paramList)
  {
    Preconditions.checkNotNull(paramList, "sizeList must not be null");
    ArrayList localArrayList = new ArrayList(paramList.size());
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Camera.Size localSize = (Camera.Size)paramList.next();
      localArrayList.add(new Size(width, height));
    }
    return localArrayList;
  }
  
  public static Size[] convertSizeListToArray(List<Camera.Size> paramList)
  {
    Preconditions.checkNotNull(paramList, "sizeList must not be null");
    Size[] arrayOfSize = new Size[paramList.size()];
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      paramList = (Camera.Size)localIterator.next();
      arrayOfSize[i] = new Size(width, height);
      i++;
    }
    return arrayOfSize;
  }
  
  private static List<Rect> getAvailableCropRectangles(Camera.Parameters paramParameters, Rect paramRect, Size paramSize)
  {
    Preconditions.checkNotNull(paramParameters, "params must not be null");
    Preconditions.checkNotNull(paramRect, "activeArray must not be null");
    Preconditions.checkNotNull(paramSize, "streamSize must not be null");
    Rect localRect = getPreviewCropRectangleUnzoomed(paramRect, paramSize);
    if (!paramParameters.isZoomSupported()) {
      return new ArrayList(Arrays.asList(new Rect[] { localRect }));
    }
    ArrayList localArrayList = new ArrayList(paramParameters.getMaxZoom() + 1);
    paramSize = new Matrix();
    RectF localRectF = new RectF();
    paramParameters = paramParameters.getZoomRatios().iterator();
    while (paramParameters.hasNext())
    {
      float f = 100.0F / ((Integer)paramParameters.next()).intValue();
      ParamsUtils.convertRectF(localRect, localRectF);
      paramSize.setScale(f, f, paramRect.exactCenterX(), paramRect.exactCenterY());
      paramSize.mapRect(localRectF);
      localArrayList.add(ParamsUtils.createRect(localRectF));
    }
    return localArrayList;
  }
  
  public static List<Rect> getAvailablePreviewZoomCropRectangles(Camera.Parameters paramParameters, Rect paramRect, Size paramSize)
  {
    Preconditions.checkNotNull(paramParameters, "params must not be null");
    Preconditions.checkNotNull(paramRect, "activeArray must not be null");
    Preconditions.checkNotNull(paramSize, "previewSize must not be null");
    return getAvailableCropRectangles(paramParameters, paramRect, paramSize);
  }
  
  public static List<Rect> getAvailableZoomCropRectangles(Camera.Parameters paramParameters, Rect paramRect)
  {
    Preconditions.checkNotNull(paramParameters, "params must not be null");
    Preconditions.checkNotNull(paramRect, "activeArray must not be null");
    return getAvailableCropRectangles(paramParameters, paramRect, ParamsUtils.createSize(paramRect));
  }
  
  public static int getClosestAvailableZoomCrop(Camera.Parameters paramParameters, Rect paramRect1, Size paramSize, Rect paramRect2, Rect paramRect3, Rect paramRect4)
  {
    Preconditions.checkNotNull(paramParameters, "params must not be null");
    Preconditions.checkNotNull(paramRect1, "activeArray must not be null");
    Preconditions.checkNotNull(paramSize, "streamSize must not be null");
    Preconditions.checkNotNull(paramRect3, "reportedCropRegion must not be null");
    Preconditions.checkNotNull(paramRect4, "previewCropRegion must not be null");
    paramRect2 = new Rect(paramRect2);
    if (!paramRect2.intersect(paramRect1))
    {
      Log.w("ParameterUtils", "getClosestAvailableZoomCrop - Crop region out of range; setting to active array size");
      paramRect2.set(paramRect1);
    }
    Rect localRect1 = getPreviewCropRectangleUnzoomed(paramRect1, paramSize);
    Rect localRect2 = shrinkToSameAspectRatioCentered(localRect1, paramRect2);
    int i = -1;
    List localList1 = getAvailableZoomCropRectangles(paramParameters, paramRect1);
    List localList2 = getAvailablePreviewZoomCropRectangles(paramParameters, paramRect1, paramSize);
    if (localList1.size() == localList2.size())
    {
      paramParameters = null;
      paramRect1 = null;
      int j = 0;
      paramSize = paramRect2;
      while (j < localList1.size())
      {
        localRect1 = (Rect)localList2.get(j);
        paramRect2 = (Rect)localList1.get(j);
        int k;
        if (i == -1) {
          k = 1;
        } else if ((localRect1.width() >= localRect2.width()) && (localRect1.height() >= localRect2.height())) {
          k = 1;
        } else {
          k = 0;
        }
        if (k == 0) {
          break;
        }
        paramParameters = localRect1;
        paramRect1 = paramRect2;
        i = j;
        j++;
      }
      if (i != -1)
      {
        paramRect3.set(paramRect1);
        paramRect4.set(paramParameters);
        return i;
      }
      throw new AssertionError("Should've found at least one valid zoom index");
    }
    throw new AssertionError("available reported/preview crop region size mismatch");
  }
  
  public static Size getLargestSupportedJpegSizeByArea(Camera.Parameters paramParameters)
  {
    Preconditions.checkNotNull(paramParameters, "params must not be null");
    return SizeAreaComparator.findLargestByArea(convertSizeList(paramParameters.getSupportedPictureSizes()));
  }
  
  public static float getMaxZoomRatio(Camera.Parameters paramParameters)
  {
    if (!paramParameters.isZoomSupported()) {
      return 1.0F;
    }
    paramParameters = paramParameters.getZoomRatios();
    return ((Integer)paramParameters.get(paramParameters.size() - 1)).intValue() * 1.0F / 100.0F;
  }
  
  private static Rect getPreviewCropRectangleUnzoomed(Rect paramRect, Size paramSize)
  {
    if (paramSize.getWidth() <= paramRect.width())
    {
      if (paramSize.getHeight() <= paramRect.height())
      {
        float f1 = paramRect.width() * 1.0F / paramRect.height();
        float f2 = paramSize.getWidth() * 1.0F / paramSize.getHeight();
        float f3;
        if (Math.abs(f2 - f1) < 0.05000000074505806D)
        {
          f3 = paramRect.height();
          f1 = paramRect.width();
        }
        for (;;)
        {
          break;
          if (f2 < f1)
          {
            f3 = paramRect.height();
            f1 = f3 * f2;
          }
          else
          {
            f1 = paramRect.width();
            f3 = f1 / f2;
          }
        }
        paramSize = new Matrix();
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f3);
        paramSize.setTranslate(paramRect.exactCenterX(), paramRect.exactCenterY());
        paramSize.postTranslate(-localRectF.centerX(), -localRectF.centerY());
        paramSize.mapRect(localRectF);
        return ParamsUtils.createRect(localRectF);
      }
      throw new IllegalArgumentException("previewSize must not be taller than activeArray");
    }
    throw new IllegalArgumentException("previewSize must not be wider than activeArray");
  }
  
  private static SizeF getZoomRatio(Size paramSize1, Size paramSize2)
  {
    Preconditions.checkNotNull(paramSize1, "activeArraySize must not be null");
    Preconditions.checkNotNull(paramSize2, "cropSize must not be null");
    Preconditions.checkArgumentPositive(paramSize2.getWidth(), "cropSize.width must be positive");
    Preconditions.checkArgumentPositive(paramSize2.getHeight(), "cropSize.height must be positive");
    return new SizeF(paramSize1.getWidth() * 1.0F / paramSize2.getWidth(), paramSize1.getHeight() * 1.0F / paramSize2.getHeight());
  }
  
  private static Rect shrinkToSameAspectRatioCentered(Rect paramRect1, Rect paramRect2)
  {
    float f1 = paramRect1.width() * 1.0F / paramRect1.height();
    float f2 = paramRect2.width() * 1.0F / paramRect2.height();
    float f3;
    if (f2 < f1)
    {
      f1 = paramRect1.height();
      f3 = f1 * f2;
    }
    else
    {
      f3 = paramRect1.width();
      f1 = f3 / f2;
    }
    Matrix localMatrix = new Matrix();
    RectF localRectF = new RectF(paramRect2);
    localMatrix.setScale(f3 / paramRect1.width(), f1 / paramRect1.height(), paramRect2.exactCenterX(), paramRect2.exactCenterY());
    localMatrix.mapRect(localRectF);
    return ParamsUtils.createRect(localRectF);
  }
  
  public static String stringFromArea(Camera.Area paramArea)
  {
    if (paramArea == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    Rect localRect = rect;
    localStringBuilder.setLength(0);
    localStringBuilder.append("([");
    localStringBuilder.append(left);
    localStringBuilder.append(',');
    localStringBuilder.append(top);
    localStringBuilder.append("][");
    localStringBuilder.append(right);
    localStringBuilder.append(',');
    localStringBuilder.append(bottom);
    localStringBuilder.append(']');
    localStringBuilder.append(',');
    localStringBuilder.append(weight);
    localStringBuilder.append(')');
    return localStringBuilder.toString();
  }
  
  public static String stringFromAreaList(List<Camera.Area> paramList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramList == null) {
      return null;
    }
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Camera.Area localArea = (Camera.Area)localIterator.next();
      if (localArea == null) {
        localStringBuilder.append("null");
      } else {
        localStringBuilder.append(stringFromArea(localArea));
      }
      if (i != paramList.size() - 1) {
        localStringBuilder.append(", ");
      }
      i++;
    }
    return localStringBuilder.toString();
  }
  
  public static class MeteringData
  {
    public final Camera.Area meteringArea;
    public final Rect previewMetering;
    public final Rect reportedMetering;
    
    public MeteringData(Camera.Area paramArea, Rect paramRect1, Rect paramRect2)
    {
      meteringArea = paramArea;
      previewMetering = paramRect1;
      reportedMetering = paramRect2;
    }
  }
  
  public static class WeightedRectangle
  {
    public final Rect rect;
    public final int weight;
    
    public WeightedRectangle(Rect paramRect, int paramInt)
    {
      rect = ((Rect)Preconditions.checkNotNull(paramRect, "rect must not be null"));
      weight = paramInt;
    }
    
    private static int clip(int paramInt1, int paramInt2, int paramInt3, Rect paramRect, String paramString)
    {
      StringBuilder localStringBuilder;
      if (paramInt1 < paramInt2)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("toMetering - Rectangle ");
        localStringBuilder.append(paramRect);
        localStringBuilder.append(" ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" too small, clip to ");
        localStringBuilder.append(paramInt2);
        Log.w("ParameterUtils", localStringBuilder.toString());
      }
      else
      {
        paramInt2 = paramInt1;
        if (paramInt1 > paramInt3)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("toMetering - Rectangle ");
          localStringBuilder.append(paramRect);
          localStringBuilder.append(" ");
          localStringBuilder.append(paramString);
          localStringBuilder.append(" too small, clip to ");
          localStringBuilder.append(paramInt3);
          Log.w("ParameterUtils", localStringBuilder.toString());
          paramInt2 = paramInt3;
        }
      }
      return paramInt2;
    }
    
    private static int clipLower(int paramInt1, int paramInt2, Rect paramRect, String paramString)
    {
      return clip(paramInt1, paramInt2, Integer.MAX_VALUE, paramRect, paramString);
    }
    
    public Face toFace()
    {
      int i = clip(weight, 1, 100, rect, "score");
      return new Face(rect, i);
    }
    
    public Face toFace(int paramInt, Point paramPoint1, Point paramPoint2, Point paramPoint3)
    {
      paramInt = clipLower(paramInt, 0, rect, "id");
      int i = clip(weight, 1, 100, rect, "score");
      return new Face(rect, i, paramInt, paramPoint1, paramPoint2, paramPoint3);
    }
    
    public MeteringRectangle toMetering()
    {
      int i = clip(weight, 0, 1000, rect, "weight");
      return new MeteringRectangle(clipLower(rect.left, 0, rect, "left"), clipLower(rect.top, 0, rect, "top"), clipLower(rect.width(), 0, rect, "width"), clipLower(rect.height(), 0, rect, "height"), i);
    }
  }
  
  public static class ZoomData
  {
    public final Rect previewCrop;
    public final Rect reportedCrop;
    public final int zoomIndex;
    
    public ZoomData(int paramInt, Rect paramRect1, Rect paramRect2)
    {
      zoomIndex = paramInt;
      previewCrop = paramRect1;
      reportedCrop = paramRect2;
    }
  }
}
