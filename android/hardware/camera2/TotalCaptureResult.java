package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TotalCaptureResult
  extends CaptureResult
{
  private final List<CaptureResult> mPartialResults;
  private final HashMap<String, CaptureResult> mPhysicalCaptureResults;
  private final int mSessionId;
  
  public TotalCaptureResult(CameraMetadataNative paramCameraMetadataNative, int paramInt)
  {
    super(paramCameraMetadataNative, paramInt);
    mPartialResults = new ArrayList();
    mSessionId = -1;
    mPhysicalCaptureResults = new HashMap();
  }
  
  public TotalCaptureResult(CameraMetadataNative paramCameraMetadataNative, CaptureRequest paramCaptureRequest, CaptureResultExtras paramCaptureResultExtras, List<CaptureResult> paramList, int paramInt, PhysicalCaptureResultInfo[] paramArrayOfPhysicalCaptureResultInfo)
  {
    super(paramCameraMetadataNative, paramCaptureRequest, paramCaptureResultExtras);
    if (paramList == null) {
      mPartialResults = new ArrayList();
    } else {
      mPartialResults = paramList;
    }
    mSessionId = paramInt;
    mPhysicalCaptureResults = new HashMap();
    int i = paramArrayOfPhysicalCaptureResultInfo.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      paramCameraMetadataNative = paramArrayOfPhysicalCaptureResultInfo[paramInt];
      paramList = new CaptureResult(paramCameraMetadataNative.getCameraMetadata(), paramCaptureRequest, paramCaptureResultExtras);
      mPhysicalCaptureResults.put(paramCameraMetadataNative.getCameraId(), paramList);
    }
  }
  
  public List<CaptureResult> getPartialResults()
  {
    return Collections.unmodifiableList(mPartialResults);
  }
  
  public Map<String, CaptureResult> getPhysicalCameraResults()
  {
    return Collections.unmodifiableMap(mPhysicalCaptureResults);
  }
  
  public int getSessionId()
  {
    return mSessionId;
  }
}
