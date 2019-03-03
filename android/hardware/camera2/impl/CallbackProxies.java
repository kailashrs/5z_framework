package android.hardware.camera2.impl;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.os.Binder;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.concurrent.Executor;

public class CallbackProxies
{
  private CallbackProxies()
  {
    throw new AssertionError();
  }
  
  public static class SessionStateCallbackProxy
    extends CameraCaptureSession.StateCallback
  {
    private final CameraCaptureSession.StateCallback mCallback;
    private final Executor mExecutor;
    
    public SessionStateCallbackProxy(Executor paramExecutor, CameraCaptureSession.StateCallback paramStateCallback)
    {
      mExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor, "executor must not be null"));
      mCallback = ((CameraCaptureSession.StateCallback)Preconditions.checkNotNull(paramStateCallback, "callback must not be null"));
    }
    
    public void onActive(CameraCaptureSession paramCameraCaptureSession)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.ISQyEhLUI1khcOCin3OIsRyTUoU localISQyEhLUI1khcOCin3OIsRyTUoU = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$ISQyEhLUI1khcOCin3OIsRyTUoU;
        localISQyEhLUI1khcOCin3OIsRyTUoU.<init>(this, paramCameraCaptureSession);
        localExecutor.execute(localISQyEhLUI1khcOCin3OIsRyTUoU);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onCaptureQueueEmpty(CameraCaptureSession paramCameraCaptureSession)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.hoQOYc189Bss2NBtrutabMRw4VU localHoQOYc189Bss2NBtrutabMRw4VU = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$hoQOYc189Bss2NBtrutabMRw4VU;
        localHoQOYc189Bss2NBtrutabMRw4VU.<init>(this, paramCameraCaptureSession);
        localExecutor.execute(localHoQOYc189Bss2NBtrutabMRw4VU);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onClosed(CameraCaptureSession paramCameraCaptureSession)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.9H0ZdANdMrdpoq2bfIL2l3DVsKk local9H0ZdANdMrdpoq2bfIL2l3DVsKk = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$9H0ZdANdMrdpoq2bfIL2l3DVsKk;
        local9H0ZdANdMrdpoq2bfIL2l3DVsKk.<init>(this, paramCameraCaptureSession);
        localExecutor.execute(local9H0ZdANdMrdpoq2bfIL2l3DVsKk);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onConfigureFailed(CameraCaptureSession paramCameraCaptureSession)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.gvbTsp9UPpKJAbdycdci_ZW5BeI localGvbTsp9UPpKJAbdycdci_ZW5BeI = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$gvbTsp9UPpKJAbdycdci_ZW5BeI;
        localGvbTsp9UPpKJAbdycdci_ZW5BeI.<init>(this, paramCameraCaptureSession);
        localExecutor.execute(localGvbTsp9UPpKJAbdycdci_ZW5BeI);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onConfigured(CameraCaptureSession paramCameraCaptureSession)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.soW0qC12Osypoky6AfL3P2_TeDw localSoW0qC12Osypoky6AfL3P2_TeDw = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$soW0qC12Osypoky6AfL3P2_TeDw;
        localSoW0qC12Osypoky6AfL3P2_TeDw.<init>(this, paramCameraCaptureSession);
        localExecutor.execute(localSoW0qC12Osypoky6AfL3P2_TeDw);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onReady(CameraCaptureSession paramCameraCaptureSession)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.Hoz_iT1tD_pl7sCGu4flyo_xB90 localHoz_iT1tD_pl7sCGu4flyo_xB90 = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$Hoz_iT1tD_pl7sCGu4flyo_xB90;
        localHoz_iT1tD_pl7sCGu4flyo_xB90.<init>(this, paramCameraCaptureSession);
        localExecutor.execute(localHoz_iT1tD_pl7sCGu4flyo_xB90);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onSurfacePrepared(CameraCaptureSession paramCameraCaptureSession, Surface paramSurface)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Executor localExecutor = mExecutor;
        _..Lambda.CallbackProxies.SessionStateCallbackProxy.tuajQwbKz3BV5CZZJdjl97HF6Tw localTuajQwbKz3BV5CZZJdjl97HF6Tw = new android/hardware/camera2/impl/_$$Lambda$CallbackProxies$SessionStateCallbackProxy$tuajQwbKz3BV5CZZJdjl97HF6Tw;
        localTuajQwbKz3BV5CZZJdjl97HF6Tw.<init>(this, paramCameraCaptureSession, paramSurface);
        localExecutor.execute(localTuajQwbKz3BV5CZZJdjl97HF6Tw);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
}
