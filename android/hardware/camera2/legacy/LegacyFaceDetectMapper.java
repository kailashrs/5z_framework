package android.hardware.camera2.legacy;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.utils.ParamsUtils;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.List;

public class LegacyFaceDetectMapper
{
  private static final boolean DEBUG = false;
  private static String TAG = "LegacyFaceDetectMapper";
  private final Camera mCamera;
  private boolean mFaceDetectEnabled = false;
  private boolean mFaceDetectReporting = false;
  private boolean mFaceDetectScenePriority = false;
  private final boolean mFaceDetectSupported;
  private Camera.Face[] mFaces;
  private Camera.Face[] mFacesPrev;
  private final Object mLock = new Object();
  
  public LegacyFaceDetectMapper(Camera paramCamera, CameraCharacteristics paramCameraCharacteristics)
  {
    mCamera = ((Camera)Preconditions.checkNotNull(paramCamera, "camera must not be null"));
    Preconditions.checkNotNull(paramCameraCharacteristics, "characteristics must not be null");
    mFaceDetectSupported = ArrayUtils.contains((int[])paramCameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES), 1);
    if (!mFaceDetectSupported) {
      return;
    }
    mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener()
    {
      public void onFaceDetection(Camera.Face[] paramAnonymousArrayOfFace, Camera arg2)
      {
        int i;
        if (paramAnonymousArrayOfFace == null) {
          i = 0;
        } else {
          i = paramAnonymousArrayOfFace.length;
        }
        synchronized (mLock)
        {
          if (mFaceDetectEnabled) {
            LegacyFaceDetectMapper.access$202(LegacyFaceDetectMapper.this, paramAnonymousArrayOfFace);
          } else if (i > 0) {
            Log.d(LegacyFaceDetectMapper.TAG, "onFaceDetection - Ignored some incoming faces sinceface detection was disabled");
          }
          return;
        }
      }
    });
  }
  
  public void mapResultFaces(CameraMetadataNative paramCameraMetadataNative, LegacyRequest paramLegacyRequest)
  {
    Preconditions.checkNotNull(paramCameraMetadataNative, "result must not be null");
    Preconditions.checkNotNull(paramLegacyRequest, "legacyRequest must not be null");
    synchronized (mLock)
    {
      int i;
      if (mFaceDetectReporting) {
        i = 1;
      } else {
        i = 0;
      }
      Camera.Face[] arrayOfFace;
      if (mFaceDetectReporting) {
        arrayOfFace = mFaces;
      } else {
        arrayOfFace = null;
      }
      boolean bool = mFaceDetectScenePriority;
      Object localObject2 = mFacesPrev;
      mFacesPrev = arrayOfFace;
      Object localObject3 = characteristics;
      ??? = captureRequest;
      localObject2 = previewSize;
      Camera.Parameters localParameters = parameters;
      paramLegacyRequest = (Rect)((CameraCharacteristics)localObject3).get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
      localObject2 = ParameterUtils.convertScalerCropRegion(paramLegacyRequest, (Rect)((CaptureRequest)???).get(CaptureRequest.SCALER_CROP_REGION), (Size)localObject2, localParameters);
      ??? = new ArrayList();
      if (arrayOfFace != null)
      {
        int j = arrayOfFace.length;
        for (int k = 0; k < j; k++)
        {
          localObject3 = arrayOfFace[k];
          if (localObject3 != null) {
            ((List)???).add(ParameterUtils.convertFaceFromLegacy((Camera.Face)localObject3, paramLegacyRequest, (ParameterUtils.ZoomData)localObject2));
          } else {
            Log.w(TAG, "mapResultFaces - read NULL face from camera1 device");
          }
        }
      }
      paramCameraMetadataNative.set(CaptureResult.STATISTICS_FACES, (Face[])((List)???).toArray(new Face[0]));
      paramCameraMetadataNative.set(CaptureResult.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(i));
      if (bool) {
        paramCameraMetadataNative.set(CaptureResult.CONTROL_SCENE_MODE, Integer.valueOf(1));
      }
      return;
    }
  }
  
  public void processFaceDetectMode(CaptureRequest arg1, Camera.Parameters paramParameters)
  {
    Preconditions.checkNotNull(???, "captureRequest must not be null");
    paramParameters = CaptureRequest.STATISTICS_FACE_DETECT_MODE;
    boolean bool1 = false;
    int i = ((Integer)ParamsUtils.getOrDefault(???, paramParameters, Integer.valueOf(0))).intValue();
    if ((i != 0) && (!mFaceDetectSupported))
    {
      Log.w(TAG, "processFaceDetectMode - Ignoring statistics.faceDetectMode; face detection is not available");
      return;
    }
    int j = ((Integer)ParamsUtils.getOrDefault(???, CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(0))).intValue();
    if ((j == 1) && (!mFaceDetectSupported))
    {
      Log.w(TAG, "processFaceDetectMode - ignoring control.sceneMode == FACE_PRIORITY; face detection is not available");
      return;
    }
    switch (i)
    {
    default: 
      paramParameters = TAG;
      ??? = new StringBuilder();
      ???.append("processFaceDetectMode - ignoring unknown statistics.faceDetectMode = ");
      ???.append(i);
      Log.w(paramParameters, ???.toString());
      return;
    case 2: 
      Log.w(TAG, "processFaceDetectMode - statistics.faceDetectMode == FULL unsupported, downgrading to SIMPLE");
      break;
    }
    boolean bool2;
    if ((i == 0) && (j != 1)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    synchronized (mLock)
    {
      if (bool2 != mFaceDetectEnabled)
      {
        if (bool2)
        {
          mCamera.startFaceDetection();
        }
        else
        {
          mCamera.stopFaceDetection();
          mFaces = null;
        }
        mFaceDetectEnabled = bool2;
        if (j == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mFaceDetectScenePriority = bool2;
        bool2 = bool1;
        if (i != 0) {
          bool2 = true;
        }
        mFaceDetectReporting = bool2;
      }
      return;
    }
  }
}
