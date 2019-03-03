package android.hardware.camera2.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Handler;
import android.util.Range;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class CameraConstrainedHighSpeedCaptureSessionImpl
  extends CameraConstrainedHighSpeedCaptureSession
  implements CameraCaptureSessionCore
{
  private final CameraCharacteristics mCharacteristics;
  private final CameraCaptureSessionImpl mSessionImpl;
  
  CameraConstrainedHighSpeedCaptureSessionImpl(int paramInt, CameraCaptureSession.StateCallback paramStateCallback, Executor paramExecutor1, CameraDeviceImpl paramCameraDeviceImpl, Executor paramExecutor2, boolean paramBoolean, CameraCharacteristics paramCameraCharacteristics)
  {
    mCharacteristics = paramCameraCharacteristics;
    mSessionImpl = new CameraCaptureSessionImpl(paramInt, null, new WrapperCallback(paramStateCallback), paramExecutor1, paramCameraDeviceImpl, paramExecutor2, paramBoolean);
  }
  
  private boolean isConstrainedHighSpeedRequestList(List<CaptureRequest> paramList)
  {
    Preconditions.checkCollectionNotEmpty(paramList, "High speed request list");
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      if (!((CaptureRequest)paramList.next()).isPartOfCRequestList()) {
        return false;
      }
    }
    return true;
  }
  
  public void abortCaptures()
    throws CameraAccessException
  {
    mSessionImpl.abortCaptures();
  }
  
  public int capture(CaptureRequest paramCaptureRequest, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("Constrained high speed session doesn't support this method");
  }
  
  public int captureBurst(List<CaptureRequest> paramList, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    if (isConstrainedHighSpeedRequestList(paramList)) {
      return mSessionImpl.captureBurst(paramList, paramCaptureCallback, paramHandler);
    }
    throw new IllegalArgumentException("Only request lists created by createHighSpeedRequestList() can be submitted to a constrained high speed capture session");
  }
  
  public int captureBurstRequests(List<CaptureRequest> paramList, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    if (isConstrainedHighSpeedRequestList(paramList)) {
      return mSessionImpl.captureBurstRequests(paramList, paramExecutor, paramCaptureCallback);
    }
    throw new IllegalArgumentException("Only request lists created by createHighSpeedRequestList() can be submitted to a constrained high speed capture session");
  }
  
  public int captureSingleRequest(CaptureRequest paramCaptureRequest, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("Constrained high speed session doesn't support this method");
  }
  
  public void close()
  {
    mSessionImpl.close();
  }
  
  public List<CaptureRequest> createHighSpeedRequestList(CaptureRequest paramCaptureRequest)
    throws CameraAccessException
  {
    if (paramCaptureRequest != null)
    {
      Object localObject1 = paramCaptureRequest.getTargets();
      Object localObject2 = (Range)paramCaptureRequest.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
      SurfaceUtils.checkConstrainedHighSpeedSurfaces((Collection)localObject1, (Range)localObject2, (StreamConfigurationMap)mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP));
      int i = ((Integer)((Range)localObject2).getUpper()).intValue() / 30;
      ArrayList localArrayList = new ArrayList();
      CaptureRequest.Builder localBuilder1 = new CaptureRequest.Builder(new CameraMetadataNative(paramCaptureRequest.getNativeCopy()), false, -1, paramCaptureRequest.getLogicalCameraId(), null);
      localBuilder1.setTag(paramCaptureRequest.getTag());
      Iterator localIterator = ((Collection)localObject1).iterator();
      localObject2 = (Surface)localIterator.next();
      if ((((Collection)localObject1).size() == 1) && (SurfaceUtils.isSurfaceForHwVideoEncoder((Surface)localObject2))) {
        localBuilder1.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(1));
      } else {
        localBuilder1.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(3));
      }
      localBuilder1.setPartOfCHSRequestList(true);
      CaptureRequest.Builder localBuilder2 = null;
      if (((Collection)localObject1).size() == 2)
      {
        localBuilder2 = new CaptureRequest.Builder(new CameraMetadataNative(paramCaptureRequest.getNativeCopy()), false, -1, paramCaptureRequest.getLogicalCameraId(), null);
        localBuilder2.setTag(paramCaptureRequest.getTag());
        localBuilder2.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(3));
        localBuilder2.addTarget((Surface)localObject2);
        localObject1 = (Surface)localIterator.next();
        localBuilder2.addTarget((Surface)localObject1);
        localBuilder2.setPartOfCHSRequestList(true);
        paramCaptureRequest = (CaptureRequest)localObject2;
        localObject2 = paramCaptureRequest;
        if (!SurfaceUtils.isSurfaceForHwVideoEncoder(paramCaptureRequest)) {
          localObject2 = localObject1;
        }
        localBuilder1.addTarget((Surface)localObject2);
        paramCaptureRequest = localBuilder2;
      }
      else
      {
        localBuilder1.addTarget((Surface)localObject2);
        paramCaptureRequest = localBuilder2;
      }
      for (int j = 0; j < i; j++) {
        if ((j == 0) && (paramCaptureRequest != null)) {
          localArrayList.add(paramCaptureRequest.build());
        } else {
          localArrayList.add(localBuilder1.build());
        }
      }
      return Collections.unmodifiableList(localArrayList);
    }
    throw new IllegalArgumentException("Input capture request must not be null");
  }
  
  public void finalizeOutputConfigurations(List<OutputConfiguration> paramList)
    throws CameraAccessException
  {
    mSessionImpl.finalizeOutputConfigurations(paramList);
  }
  
  public CameraDevice getDevice()
  {
    return mSessionImpl.getDevice();
  }
  
  public CameraDeviceImpl.StateCallbackKK getDeviceStateCallback()
  {
    return mSessionImpl.getDeviceStateCallback();
  }
  
  public Surface getInputSurface()
  {
    return null;
  }
  
  public boolean isAborting()
  {
    return mSessionImpl.isAborting();
  }
  
  public boolean isReprocessable()
  {
    return false;
  }
  
  public void prepare(int paramInt, Surface paramSurface)
    throws CameraAccessException
  {
    mSessionImpl.prepare(paramInt, paramSurface);
  }
  
  public void prepare(Surface paramSurface)
    throws CameraAccessException
  {
    mSessionImpl.prepare(paramSurface);
  }
  
  public void replaceSessionClose()
  {
    mSessionImpl.replaceSessionClose();
  }
  
  public int setRepeatingBurst(List<CaptureRequest> paramList, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    if (isConstrainedHighSpeedRequestList(paramList)) {
      return mSessionImpl.setRepeatingBurst(paramList, paramCaptureCallback, paramHandler);
    }
    throw new IllegalArgumentException("Only request lists created by createHighSpeedRequestList() can be submitted to a constrained high speed capture session");
  }
  
  public int setRepeatingBurstRequests(List<CaptureRequest> paramList, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    if (isConstrainedHighSpeedRequestList(paramList)) {
      return mSessionImpl.setRepeatingBurstRequests(paramList, paramExecutor, paramCaptureCallback);
    }
    throw new IllegalArgumentException("Only request lists created by createHighSpeedRequestList() can be submitted to a constrained high speed capture session");
  }
  
  public int setRepeatingRequest(CaptureRequest paramCaptureRequest, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("Constrained high speed session doesn't support this method");
  }
  
  public int setSingleRepeatingRequest(CaptureRequest paramCaptureRequest, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("Constrained high speed session doesn't support this method");
  }
  
  public void stopRepeating()
    throws CameraAccessException
  {
    mSessionImpl.stopRepeating();
  }
  
  public void tearDown(Surface paramSurface)
    throws CameraAccessException
  {
    mSessionImpl.tearDown(paramSurface);
  }
  
  public void updateOutputConfiguration(OutputConfiguration paramOutputConfiguration)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("Constrained high speed session doesn't support this method");
  }
  
  private class WrapperCallback
    extends CameraCaptureSession.StateCallback
  {
    private final CameraCaptureSession.StateCallback mCallback;
    
    public WrapperCallback(CameraCaptureSession.StateCallback paramStateCallback)
    {
      mCallback = paramStateCallback;
    }
    
    public void onActive(CameraCaptureSession paramCameraCaptureSession)
    {
      mCallback.onActive(CameraConstrainedHighSpeedCaptureSessionImpl.this);
    }
    
    public void onCaptureQueueEmpty(CameraCaptureSession paramCameraCaptureSession)
    {
      mCallback.onCaptureQueueEmpty(CameraConstrainedHighSpeedCaptureSessionImpl.this);
    }
    
    public void onClosed(CameraCaptureSession paramCameraCaptureSession)
    {
      mCallback.onClosed(CameraConstrainedHighSpeedCaptureSessionImpl.this);
    }
    
    public void onConfigureFailed(CameraCaptureSession paramCameraCaptureSession)
    {
      mCallback.onConfigureFailed(CameraConstrainedHighSpeedCaptureSessionImpl.this);
    }
    
    public void onConfigured(CameraCaptureSession paramCameraCaptureSession)
    {
      mCallback.onConfigured(CameraConstrainedHighSpeedCaptureSessionImpl.this);
    }
    
    public void onReady(CameraCaptureSession paramCameraCaptureSession)
    {
      mCallback.onReady(CameraConstrainedHighSpeedCaptureSessionImpl.this);
    }
    
    public void onSurfacePrepared(CameraCaptureSession paramCameraCaptureSession, Surface paramSurface)
    {
      mCallback.onSurfacePrepared(CameraConstrainedHighSpeedCaptureSessionImpl.this, paramSurface);
    }
  }
}
