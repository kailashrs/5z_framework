package android.hardware.radio;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class TunerCallbackAdapter
  extends ITunerCallback.Stub
{
  private static final String TAG = "BroadcastRadio.TunerCallbackAdapter";
  private final RadioTuner.Callback mCallback;
  RadioManager.ProgramInfo mCurrentProgramInfo;
  private boolean mDelayedCompleteCallback = false;
  private final Handler mHandler;
  boolean mIsAntennaConnected = true;
  List<RadioManager.ProgramInfo> mLastCompleteList;
  private final Object mLock = new Object();
  ProgramList mProgramList;
  
  TunerCallbackAdapter(RadioTuner.Callback paramCallback, Handler paramHandler)
  {
    mCallback = paramCallback;
    if (paramHandler == null) {
      mHandler = new Handler(Looper.getMainLooper());
    } else {
      mHandler = paramHandler;
    }
  }
  
  private void sendBackgroundScanCompleteLocked()
  {
    mDelayedCompleteCallback = false;
    mHandler.post(new _..Lambda.TunerCallbackAdapter.xIUT1Qu5TkA83V8ttYy1zv_JuFo(this));
  }
  
  void clearLastCompleteList()
  {
    synchronized (mLock)
    {
      mLastCompleteList = null;
      return;
    }
  }
  
  void close()
  {
    synchronized (mLock)
    {
      if (mProgramList != null) {
        mProgramList.close();
      }
      return;
    }
  }
  
  RadioManager.ProgramInfo getCurrentProgramInformation()
  {
    synchronized (mLock)
    {
      RadioManager.ProgramInfo localProgramInfo = mCurrentProgramInfo;
      return localProgramInfo;
    }
  }
  
  List<RadioManager.ProgramInfo> getLastCompleteList()
  {
    synchronized (mLock)
    {
      List localList = mLastCompleteList;
      return localList;
    }
  }
  
  boolean isAntennaConnected()
  {
    return mIsAntennaConnected;
  }
  
  public void onAntennaState(boolean paramBoolean)
  {
    mIsAntennaConnected = paramBoolean;
    mHandler.post(new _..Lambda.TunerCallbackAdapter.dR_VQmFrL_tBD2wpNvborTd8W08(this, paramBoolean));
  }
  
  public void onBackgroundScanAvailabilityChange(boolean paramBoolean)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.4zf9n0sz_rU8z6a9GJmRInWrYkQ(this, paramBoolean));
  }
  
  public void onBackgroundScanComplete()
  {
    synchronized (mLock)
    {
      if (mLastCompleteList == null)
      {
        Log.i("BroadcastRadio.TunerCallbackAdapter", "Got onBackgroundScanComplete callback, but the program list didn't get through yet. Delaying it...");
        mDelayedCompleteCallback = true;
        return;
      }
      sendBackgroundScanCompleteLocked();
      return;
    }
  }
  
  public void onConfigurationChanged(RadioManager.BandConfig paramBandConfig)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.B4BuskgdSatf_Xt5wzgLniEltQk(this, paramBandConfig));
  }
  
  public void onCurrentProgramInfoChanged(RadioManager.ProgramInfo paramProgramInfo)
  {
    if (paramProgramInfo == null)
    {
      Log.e("BroadcastRadio.TunerCallbackAdapter", "ProgramInfo must not be null");
      return;
    }
    synchronized (mLock)
    {
      mCurrentProgramInfo = paramProgramInfo;
      mHandler.post(new _..Lambda.TunerCallbackAdapter.RSNrzX5_O3nayC2_jg0kAR6KkKY(this, paramProgramInfo));
      return;
    }
  }
  
  public void onEmergencyAnnouncement(boolean paramBoolean)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.ZwPm3xxjeLvbP12KweyzqFJVnj4(this, paramBoolean));
  }
  
  public void onError(int paramInt)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.jl29exheqPoYrltfLs9fLsjsI1A(this, paramInt));
  }
  
  public void onParametersUpdated(Map paramMap)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.Yz_4KCDu1MOynGdkDf_oMxqhjeY(this, paramMap));
  }
  
  public void onProgramListChanged()
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.UsmGhKordXy4lhCylRP0mm2NcYc(this));
  }
  
  public void onProgramListUpdated(ProgramList.Chunk paramChunk)
  {
    synchronized (mLock)
    {
      if (mProgramList == null) {
        return;
      }
      mProgramList.apply((ProgramList.Chunk)Objects.requireNonNull(paramChunk));
      return;
    }
  }
  
  public void onTrafficAnnouncement(boolean paramBoolean)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.tiaoLZrR2K56rYeqHvSRh5lRdBI(this, paramBoolean));
  }
  
  public void onTuneFailed(int paramInt, ProgramSelector paramProgramSelector)
  {
    mHandler.post(new _..Lambda.TunerCallbackAdapter.Hj_P___HTEx_8p7qvYVPXmhwu7w(this, paramInt, paramProgramSelector));
    if ((paramInt != Integer.MIN_VALUE) && (paramInt != -38)) {
      if (paramInt != -32)
      {
        if ((paramInt != -22) && (paramInt != -19)) {
          if (paramInt != -1) {
            break label99;
          }
        }
      }
      else
      {
        paramInt = 1;
        break label101;
      }
    }
    paramProgramSelector = new StringBuilder();
    paramProgramSelector.append("Got an error with no mapping to the legacy API (");
    paramProgramSelector.append(paramInt);
    paramProgramSelector.append("), doing a best-effort conversion to ERROR_SCAN_TIMEOUT");
    Log.i("BroadcastRadio.TunerCallbackAdapter", paramProgramSelector.toString());
    label99:
    paramInt = 3;
    label101:
    mHandler.post(new _..Lambda.TunerCallbackAdapter.HcS5_voI1xju970_jCP6Iz0LgPE(this, paramInt));
  }
  
  void setProgramListObserver(ProgramList paramProgramList, ProgramList.OnCloseListener paramOnCloseListener)
  {
    Objects.requireNonNull(paramOnCloseListener);
    synchronized (mLock)
    {
      if (mProgramList != null)
      {
        Log.w("BroadcastRadio.TunerCallbackAdapter", "Previous program list observer wasn't properly closed, closing it...");
        mProgramList.close();
      }
      mProgramList = paramProgramList;
      if (paramProgramList == null) {
        return;
      }
      _..Lambda.TunerCallbackAdapter.Hl80_0ppQ17uTjZuGamwBQMrO6Y localHl80_0ppQ17uTjZuGamwBQMrO6Y = new android/hardware/radio/_$$Lambda$TunerCallbackAdapter$Hl80_0ppQ17uTjZuGamwBQMrO6Y;
      localHl80_0ppQ17uTjZuGamwBQMrO6Y.<init>(this, paramProgramList, paramOnCloseListener);
      paramProgramList.setOnCloseListener(localHl80_0ppQ17uTjZuGamwBQMrO6Y);
      paramOnCloseListener = new android/hardware/radio/_$$Lambda$TunerCallbackAdapter$V_mJUy8dIlOVjsZ1ckkgn490jFI;
      paramOnCloseListener.<init>(this, paramProgramList);
      paramProgramList.addOnCompleteListener(paramOnCloseListener);
      return;
    }
  }
}
