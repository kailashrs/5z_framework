package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.utils.ParamsUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public class LegacyFocusStateMapper
{
  private static final boolean DEBUG = false;
  private static String TAG = "LegacyFocusStateMapper";
  private String mAfModePrevious = null;
  private int mAfRun = 0;
  private int mAfState = 0;
  private int mAfStatePrevious = 0;
  private final Camera mCamera;
  private final Object mLock = new Object();
  
  public LegacyFocusStateMapper(Camera paramCamera)
  {
    mCamera = ((Camera)Preconditions.checkNotNull(paramCamera, "camera must not be null"));
  }
  
  private static String afStateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UNKNOWN(");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    case 6: 
      return "PASSIVE_UNFOCUSED";
    case 5: 
      return "NOT_FOCUSED_LOCKED";
    case 4: 
      return "FOCUSED_LOCKED";
    case 3: 
      return "ACTIVE_SCAN";
    case 2: 
      return "PASSIVE_FOCUSED";
    case 1: 
      return "PASSIVE_SCAN";
    }
    return "INACTIVE";
  }
  
  public void mapResultTriggers(CameraMetadataNative paramCameraMetadataNative)
  {
    Preconditions.checkNotNull(paramCameraMetadataNative, "result must not be null");
    synchronized (mLock)
    {
      int i = mAfState;
      paramCameraMetadataNative.set(CaptureResult.CONTROL_AF_STATE, Integer.valueOf(i));
      mAfStatePrevious = i;
      return;
    }
  }
  
  public void processRequestTriggers(CaptureRequest arg1, Camera.Parameters arg2)
  {
    Preconditions.checkNotNull(???, "captureRequest must not be null");
    ??? = CaptureRequest.CONTROL_AF_TRIGGER;
    final int i = 0;
    int j = ((Integer)ParamsUtils.getOrDefault(???, (CaptureRequest.Key)???, Integer.valueOf(0))).intValue();
    ??? = ???.getFocusMode();
    if (!Objects.equals(mAfModePrevious, ???)) {
      synchronized (mLock)
      {
        mAfRun += 1;
        mAfState = 0;
        mCamera.cancelAutoFocus();
      }
    }
    mAfModePrevious = ???;
    synchronized (mLock)
    {
      final int k = mAfRun;
      ??? = new Camera.AutoFocusMoveCallback()
      {
        public void onAutoFocusMoving(boolean paramAnonymousBoolean, Camera arg2)
        {
          synchronized (mLock)
          {
            int i = mAfRun;
            if (k != i)
            {
              localObject1 = LegacyFocusStateMapper.TAG;
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("onAutoFocusMoving - ignoring move callbacks from old af run");
              ((StringBuilder)localObject2).append(k);
              Log.d((String)localObject1, ((StringBuilder)localObject2).toString());
              return;
            }
            i = 1;
            int j;
            if (paramAnonymousBoolean) {
              j = 1;
            } else {
              j = 2;
            }
            Object localObject2 = paramCaptureRequest;
            int k = ((String)localObject2).hashCode();
            if (k != -194628547)
            {
              if ((k == 910005312) && (((String)localObject2).equals("continuous-picture")))
              {
                i = 0;
                break label148;
              }
            }
            else {
              if (((String)localObject2).equals("continuous-video")) {
                break label148;
              }
            }
            i = -1;
            switch (i)
            {
            default: 
              localObject2 = LegacyFocusStateMapper.TAG;
              break;
            case 0: 
            case 1: 
              label148:
              break;
            }
            Object localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("onAutoFocus - got unexpected onAutoFocus in mode ");
            ((StringBuilder)localObject1).append(paramCaptureRequest);
            Log.w((String)localObject2, ((StringBuilder)localObject1).toString());
            LegacyFocusStateMapper.access$302(LegacyFocusStateMapper.this, j);
            return;
          }
        }
      };
      k = ???.hashCode();
      int m = -1;
      if (k != -194628547)
      {
        if (k != 3005871)
        {
          if (k != 103652300)
          {
            if ((k == 910005312) && (???.equals("continuous-picture")))
            {
              k = 2;
              break label221;
            }
          }
          else if (???.equals("macro"))
          {
            k = 1;
            break label221;
          }
        }
        else if (???.equals("auto"))
        {
          k = 0;
          break label221;
        }
      }
      else if (???.equals("continuous-video"))
      {
        k = 3;
        break label221;
      }
      k = -1;
      switch (k)
      {
      default: 
        break;
      case 0: 
      case 1: 
      case 2: 
      case 3: 
        label221:
        mCamera.setAutoFocusMoveCallback(???);
      }
      switch (j)
      {
      default: 
        ??? = TAG;
        ??? = new StringBuilder();
        ???.append("processRequestTriggers - ignoring unknown control.afTrigger = ");
        ???.append(j);
        Log.w(???, ???.toString());
        break;
      case 2: 
        synchronized (mLock)
        {
          synchronized (mLock)
          {
            mAfRun += 1;
            mAfState = 0;
            mCamera.cancelAutoFocus();
          }
        }
      case 1: 
        k = ???.hashCode();
        if (k != -194628547)
        {
          if (k != 3005871)
          {
            if (k != 103652300)
            {
              if (k != 910005312)
              {
                k = m;
              }
              else
              {
                k = m;
                if (???.equals("continuous-picture")) {
                  k = 2;
                }
              }
            }
            else
            {
              k = m;
              if (???.equals("macro")) {
                k = 1;
              }
            }
          }
          else
          {
            k = m;
            if (???.equals("auto")) {
              k = 0;
            }
          }
        }
        else
        {
          k = m;
          if (???.equals("continuous-video")) {
            k = 3;
          }
        }
        switch (k)
        {
        default: 
          k = i;
          break;
        case 2: 
        case 3: 
          k = 1;
          break;
        case 0: 
        case 1: 
          k = 3;
        }
        synchronized (mLock)
        {
          i = mAfRun + 1;
          mAfRun = i;
          mAfState = k;
          if (k != 0) {
            mCamera.autoFocus(new Camera.AutoFocusCallback()
            {
              public void onAutoFocus(boolean paramAnonymousBoolean, Camera arg2)
              {
                synchronized (mLock)
                {
                  int i = mAfRun;
                  int j = i;
                  int k = 1;
                  if (i != j)
                  {
                    Log.d(LegacyFocusStateMapper.TAG, String.format("onAutoFocus - ignoring AF callback (old run %d, new run %d)", new Object[] { Integer.valueOf(i), Integer.valueOf(i) }));
                    return;
                  }
                  if (paramAnonymousBoolean) {
                    i = 4;
                  } else {
                    i = 5;
                  }
                  String str = paramCaptureRequest;
                  j = str.hashCode();
                  if (j != -194628547)
                  {
                    if (j != 3005871)
                    {
                      if (j != 103652300)
                      {
                        if ((j == 910005312) && (str.equals("continuous-picture"))) {
                          break label188;
                        }
                      }
                      else if (str.equals("macro"))
                      {
                        k = 3;
                        break label188;
                      }
                    }
                    else if (str.equals("auto"))
                    {
                      k = 0;
                      break label188;
                    }
                  }
                  else if (str.equals("continuous-video"))
                  {
                    k = 2;
                    break label188;
                  }
                  k = -1;
                  switch (k)
                  {
                  default: 
                    str = LegacyFocusStateMapper.TAG;
                    break;
                  case 0: 
                  case 1: 
                  case 2: 
                  case 3: 
                    label188:
                    break;
                  }
                  StringBuilder localStringBuilder = new java/lang/StringBuilder;
                  localStringBuilder.<init>();
                  localStringBuilder.append("onAutoFocus - got unexpected onAutoFocus in mode ");
                  localStringBuilder.append(paramCaptureRequest);
                  Log.w(str, localStringBuilder.toString());
                  LegacyFocusStateMapper.access$302(LegacyFocusStateMapper.this, i);
                  return;
                }
              }
            });
          }
        }
      }
      return;
    }
  }
}
