package android.hardware.radio;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class TunerAdapter
  extends RadioTuner
{
  private static final String TAG = "BroadcastRadio.TunerAdapter";
  private int mBand;
  private final TunerCallbackAdapter mCallback;
  private boolean mIsClosed = false;
  private Map<String, String> mLegacyListFilter;
  private ProgramList mLegacyListProxy;
  private final ITuner mTuner;
  
  TunerAdapter(ITuner paramITuner, TunerCallbackAdapter paramTunerCallbackAdapter, int paramInt)
  {
    mTuner = ((ITuner)Objects.requireNonNull(paramITuner));
    mCallback = ((TunerCallbackAdapter)Objects.requireNonNull(paramTunerCallbackAdapter));
    mBand = paramInt;
  }
  
  public int cancel()
  {
    try
    {
      mTuner.cancel();
      return 0;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", localRemoteException);
      return -32;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't cancel", localIllegalStateException);
    }
    return -38;
  }
  
  public void cancelAnnouncement()
  {
    try
    {
      mTuner.cancelAnnouncement();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("service died", localRemoteException);
    }
  }
  
  public void close()
  {
    synchronized (mTuner)
    {
      if (mIsClosed)
      {
        Log.v("BroadcastRadio.TunerAdapter", "Tuner is already closed");
        return;
      }
      mIsClosed = true;
      if (mLegacyListProxy != null)
      {
        mLegacyListProxy.close();
        mLegacyListProxy = null;
      }
      mCallback.close();
      try
      {
        mTuner.close();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BroadcastRadio.TunerAdapter", "Exception trying to close tuner", localRemoteException);
      }
      return;
    }
  }
  
  public int getConfiguration(RadioManager.BandConfig[] paramArrayOfBandConfig)
  {
    if ((paramArrayOfBandConfig != null) && (paramArrayOfBandConfig.length == 1)) {
      try
      {
        paramArrayOfBandConfig[0] = mTuner.getConfiguration();
        return 0;
      }
      catch (RemoteException paramArrayOfBandConfig)
      {
        Log.e("BroadcastRadio.TunerAdapter", "service died", paramArrayOfBandConfig);
        return -32;
      }
    }
    throw new IllegalArgumentException("The argument must be an array of length 1");
  }
  
  public ProgramList getDynamicProgramList(ProgramList.Filter paramFilter)
  {
    synchronized (mTuner)
    {
      if (mLegacyListProxy != null)
      {
        mLegacyListProxy.close();
        mLegacyListProxy = null;
      }
      mLegacyListFilter = null;
      Object localObject = new android/hardware/radio/ProgramList;
      ((ProgramList)localObject).<init>();
      TunerCallbackAdapter localTunerCallbackAdapter = mCallback;
      _..Lambda.TunerAdapter.ytmKJEaNVVp6n7nE6SVU6pZ9g7c localYtmKJEaNVVp6n7nE6SVU6pZ9g7c = new android/hardware/radio/_$$Lambda$TunerAdapter$ytmKJEaNVVp6n7nE6SVU6pZ9g7c;
      localYtmKJEaNVVp6n7nE6SVU6pZ9g7c.<init>(this);
      localTunerCallbackAdapter.setProgramListObserver((ProgramList)localObject, localYtmKJEaNVVp6n7nE6SVU6pZ9g7c);
      try
      {
        mTuner.startProgramListUpdates(paramFilter);
        return localObject;
      }
      catch (RemoteException paramFilter)
      {
        mCallback.setProgramListObserver(null, _..Lambda.TunerAdapter.St9hluCzvLWs9wyE7kDX24NpwJQ.INSTANCE);
        localObject = new java/lang/RuntimeException;
        ((RuntimeException)localObject).<init>("service died", paramFilter);
        throw ((Throwable)localObject);
      }
      catch (UnsupportedOperationException paramFilter)
      {
        Log.i("BroadcastRadio.TunerAdapter", "Program list is not supported with this hardware");
        return null;
      }
    }
  }
  
  public Bitmap getMetadataImage(int paramInt)
  {
    try
    {
      Bitmap localBitmap = mTuner.getImage(paramInt);
      return localBitmap;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("service died", localRemoteException);
    }
  }
  
  public boolean getMute()
  {
    try
    {
      boolean bool = mTuner.isMuted();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", localRemoteException);
    }
    return true;
  }
  
  public Map<String, String> getParameters(List<String> paramList)
  {
    try
    {
      paramList = mTuner.getParameters((List)Objects.requireNonNull(paramList));
      return paramList;
    }
    catch (RemoteException paramList)
    {
      throw new RuntimeException("service died", paramList);
    }
  }
  
  public int getProgramInformation(RadioManager.ProgramInfo[] paramArrayOfProgramInfo)
  {
    if ((paramArrayOfProgramInfo != null) && (paramArrayOfProgramInfo.length == 1))
    {
      RadioManager.ProgramInfo localProgramInfo = mCallback.getCurrentProgramInformation();
      if (localProgramInfo == null)
      {
        Log.w("BroadcastRadio.TunerAdapter", "Didn't get program info yet");
        return -38;
      }
      paramArrayOfProgramInfo[0] = localProgramInfo;
      return 0;
    }
    Log.e("BroadcastRadio.TunerAdapter", "The argument must be an array of length 1");
    return -22;
  }
  
  public List<RadioManager.ProgramInfo> getProgramList(Map<String, String> paramMap)
  {
    synchronized (mTuner)
    {
      Object localObject;
      if ((mLegacyListProxy == null) || (!Objects.equals(mLegacyListFilter, paramMap)))
      {
        Log.i("BroadcastRadio.TunerAdapter", "Program list filter has changed, requesting new list");
        localObject = new android/hardware/radio/ProgramList;
        ((ProgramList)localObject).<init>();
        mLegacyListProxy = ((ProgramList)localObject);
        mLegacyListFilter = paramMap;
        mCallback.clearLastCompleteList();
        mCallback.setProgramListObserver(mLegacyListProxy, _..Lambda.TunerAdapter.xm27iP_3PUgByOaDoK2KJcP5fnA.INSTANCE);
      }
      try
      {
        ITuner localITuner2 = mTuner;
        localObject = new android/hardware/radio/ProgramList$Filter;
        ((ProgramList.Filter)localObject).<init>(paramMap);
        localITuner2.startProgramListUpdates((ProgramList.Filter)localObject);
        paramMap = mCallback.getLastCompleteList();
        if (paramMap != null) {
          return paramMap;
        }
        paramMap = new java/lang/IllegalStateException;
        paramMap.<init>("Program list is not ready yet");
        throw paramMap;
      }
      catch (RemoteException localRemoteException)
      {
        paramMap = new java/lang/RuntimeException;
        paramMap.<init>("service died", localRemoteException);
        throw paramMap;
      }
    }
  }
  
  public boolean hasControl()
  {
    try
    {
      boolean bool = mTuner.isClosed();
      return bool ^ true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isAnalogForced()
  {
    try
    {
      boolean bool = isConfigFlagSet(2);
      return bool;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      throw new IllegalStateException(localUnsupportedOperationException);
    }
  }
  
  public boolean isAntennaConnected()
  {
    return mCallback.isAntennaConnected();
  }
  
  public boolean isConfigFlagSet(int paramInt)
  {
    try
    {
      boolean bool = mTuner.isConfigFlagSet(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("service died", localRemoteException);
    }
  }
  
  public boolean isConfigFlagSupported(int paramInt)
  {
    try
    {
      boolean bool = mTuner.isConfigFlagSupported(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("service died", localRemoteException);
    }
  }
  
  public int scan(int paramInt, boolean paramBoolean)
  {
    try
    {
      ITuner localITuner = mTuner;
      boolean bool = true;
      if (paramInt != 1) {
        bool = false;
      }
      localITuner.scan(bool, paramBoolean);
      return 0;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", localRemoteException);
      return -32;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't scan", localIllegalStateException);
    }
    return -38;
  }
  
  public void setAnalogForced(boolean paramBoolean)
  {
    try
    {
      setConfigFlag(2, paramBoolean);
      return;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      throw new IllegalStateException(localUnsupportedOperationException);
    }
  }
  
  public void setConfigFlag(int paramInt, boolean paramBoolean)
  {
    try
    {
      mTuner.setConfigFlag(paramInt, paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("service died", localRemoteException);
    }
  }
  
  public int setConfiguration(RadioManager.BandConfig paramBandConfig)
  {
    if (paramBandConfig == null) {
      return -22;
    }
    try
    {
      mTuner.setConfiguration(paramBandConfig);
      mBand = paramBandConfig.getType();
      return 0;
    }
    catch (RemoteException paramBandConfig)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", paramBandConfig);
      return -32;
    }
    catch (IllegalArgumentException paramBandConfig)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't set configuration", paramBandConfig);
    }
    return -22;
  }
  
  public int setMute(boolean paramBoolean)
  {
    try
    {
      mTuner.setMuted(paramBoolean);
      return 0;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", localRemoteException);
      return -32;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't set muted", localIllegalStateException);
    }
    return Integer.MIN_VALUE;
  }
  
  public Map<String, String> setParameters(Map<String, String> paramMap)
  {
    try
    {
      paramMap = mTuner.setParameters((Map)Objects.requireNonNull(paramMap));
      return paramMap;
    }
    catch (RemoteException paramMap)
    {
      throw new RuntimeException("service died", paramMap);
    }
  }
  
  public boolean startBackgroundScan()
  {
    try
    {
      boolean bool = mTuner.startBackgroundScan();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("service died", localRemoteException);
    }
  }
  
  public int step(int paramInt, boolean paramBoolean)
  {
    try
    {
      ITuner localITuner = mTuner;
      boolean bool = true;
      if (paramInt != 1) {
        bool = false;
      }
      localITuner.step(bool, paramBoolean);
      return 0;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", localRemoteException);
      return -32;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't step", localIllegalStateException);
    }
    return -38;
  }
  
  public int tune(int paramInt1, int paramInt2)
  {
    try
    {
      mTuner.tune(ProgramSelector.createAmFmSelector(mBand, paramInt1, paramInt2));
      return 0;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "service died", localRemoteException);
      return -32;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't tune", localIllegalArgumentException);
      return -22;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Log.e("BroadcastRadio.TunerAdapter", "Can't tune", localIllegalStateException);
    }
    return -38;
  }
  
  public void tune(ProgramSelector paramProgramSelector)
  {
    try
    {
      mTuner.tune(paramProgramSelector);
      return;
    }
    catch (RemoteException paramProgramSelector)
    {
      throw new RuntimeException("service died", paramProgramSelector);
    }
  }
}
