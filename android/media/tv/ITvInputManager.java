package android.media.tv;

import android.content.Intent;
import android.graphics.Rect;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.Surface;
import java.util.ArrayList;
import java.util.List;

public abstract interface ITvInputManager
  extends IInterface
{
  public abstract ITvInputHardware acquireTvInputHardware(int paramInt1, ITvInputHardwareCallback paramITvInputHardwareCallback, TvInputInfo paramTvInputInfo, int paramInt2)
    throws RemoteException;
  
  public abstract void addBlockedRating(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean captureFrame(String paramString, Surface paramSurface, TvStreamConfig paramTvStreamConfig, int paramInt)
    throws RemoteException;
  
  public abstract void createOverlayView(IBinder paramIBinder1, IBinder paramIBinder2, Rect paramRect, int paramInt)
    throws RemoteException;
  
  public abstract void createSession(ITvInputClient paramITvInputClient, String paramString, boolean paramBoolean, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void dispatchSurfaceChanged(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract List<TvStreamConfig> getAvailableTvStreamConfigList(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract List<String> getBlockedRatings(int paramInt)
    throws RemoteException;
  
  public abstract List<DvbDeviceInfo> getDvbDeviceList()
    throws RemoteException;
  
  public abstract List<TvInputHardwareInfo> getHardwareList()
    throws RemoteException;
  
  public abstract List<TvContentRatingSystemInfo> getTvContentRatingSystemList(int paramInt)
    throws RemoteException;
  
  public abstract TvInputInfo getTvInputInfo(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract List<TvInputInfo> getTvInputList(int paramInt)
    throws RemoteException;
  
  public abstract int getTvInputState(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isParentalControlsEnabled(int paramInt)
    throws RemoteException;
  
  public abstract boolean isRatingBlocked(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isSingleSessionActive(int paramInt)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openDvbDevice(DvbDeviceInfo paramDvbDeviceInfo, int paramInt)
    throws RemoteException;
  
  public abstract void registerCallback(ITvInputManagerCallback paramITvInputManagerCallback, int paramInt)
    throws RemoteException;
  
  public abstract void relayoutOverlayView(IBinder paramIBinder, Rect paramRect, int paramInt)
    throws RemoteException;
  
  public abstract void releaseSession(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void releaseTvInputHardware(int paramInt1, ITvInputHardware paramITvInputHardware, int paramInt2)
    throws RemoteException;
  
  public abstract void removeBlockedRating(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void removeOverlayView(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void requestChannelBrowsable(Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract void selectTrack(IBinder paramIBinder, int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void sendAppPrivateCommand(IBinder paramIBinder, String paramString, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract void sendTvInputNotifyIntent(Intent paramIntent, int paramInt)
    throws RemoteException;
  
  public abstract void setCaptionEnabled(IBinder paramIBinder, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setMainSession(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void setParentalControlsEnabled(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setSurface(IBinder paramIBinder, Surface paramSurface, int paramInt)
    throws RemoteException;
  
  public abstract void setVolume(IBinder paramIBinder, float paramFloat, int paramInt)
    throws RemoteException;
  
  public abstract void startRecording(IBinder paramIBinder, Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract void stopRecording(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void timeShiftEnablePositionTracking(IBinder paramIBinder, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void timeShiftPause(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void timeShiftPlay(IBinder paramIBinder, Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract void timeShiftResume(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void timeShiftSeekTo(IBinder paramIBinder, long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void timeShiftSetPlaybackParams(IBinder paramIBinder, PlaybackParams paramPlaybackParams, int paramInt)
    throws RemoteException;
  
  public abstract void tune(IBinder paramIBinder, Uri paramUri, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract void unblockContent(IBinder paramIBinder, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void unregisterCallback(ITvInputManagerCallback paramITvInputManagerCallback, int paramInt)
    throws RemoteException;
  
  public abstract void updateTvInputInfo(TvInputInfo paramTvInputInfo, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputManager
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputManager";
    static final int TRANSACTION_acquireTvInputHardware = 37;
    static final int TRANSACTION_addBlockedRating = 12;
    static final int TRANSACTION_captureFrame = 40;
    static final int TRANSACTION_createOverlayView = 24;
    static final int TRANSACTION_createSession = 14;
    static final int TRANSACTION_dispatchSurfaceChanged = 18;
    static final int TRANSACTION_getAvailableTvStreamConfigList = 39;
    static final int TRANSACTION_getBlockedRatings = 11;
    static final int TRANSACTION_getDvbDeviceList = 42;
    static final int TRANSACTION_getHardwareList = 36;
    static final int TRANSACTION_getTvContentRatingSystemList = 5;
    static final int TRANSACTION_getTvInputInfo = 2;
    static final int TRANSACTION_getTvInputList = 1;
    static final int TRANSACTION_getTvInputState = 4;
    static final int TRANSACTION_isParentalControlsEnabled = 8;
    static final int TRANSACTION_isRatingBlocked = 10;
    static final int TRANSACTION_isSingleSessionActive = 41;
    static final int TRANSACTION_openDvbDevice = 43;
    static final int TRANSACTION_registerCallback = 6;
    static final int TRANSACTION_relayoutOverlayView = 25;
    static final int TRANSACTION_releaseSession = 15;
    static final int TRANSACTION_releaseTvInputHardware = 38;
    static final int TRANSACTION_removeBlockedRating = 13;
    static final int TRANSACTION_removeOverlayView = 26;
    static final int TRANSACTION_requestChannelBrowsable = 45;
    static final int TRANSACTION_selectTrack = 22;
    static final int TRANSACTION_sendAppPrivateCommand = 23;
    static final int TRANSACTION_sendTvInputNotifyIntent = 44;
    static final int TRANSACTION_setCaptionEnabled = 21;
    static final int TRANSACTION_setMainSession = 16;
    static final int TRANSACTION_setParentalControlsEnabled = 9;
    static final int TRANSACTION_setSurface = 17;
    static final int TRANSACTION_setVolume = 19;
    static final int TRANSACTION_startRecording = 34;
    static final int TRANSACTION_stopRecording = 35;
    static final int TRANSACTION_timeShiftEnablePositionTracking = 33;
    static final int TRANSACTION_timeShiftPause = 29;
    static final int TRANSACTION_timeShiftPlay = 28;
    static final int TRANSACTION_timeShiftResume = 30;
    static final int TRANSACTION_timeShiftSeekTo = 31;
    static final int TRANSACTION_timeShiftSetPlaybackParams = 32;
    static final int TRANSACTION_tune = 20;
    static final int TRANSACTION_unblockContent = 27;
    static final int TRANSACTION_unregisterCallback = 7;
    static final int TRANSACTION_updateTvInputInfo = 3;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputManager");
    }
    
    public static ITvInputManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputManager");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputManager))) {
        return (ITvInputManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        String str = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 45: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject13;
          }
          requestChannelBrowsable((Uri)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject1;
          }
          sendTvInputNotifyIntent((Intent)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (DvbDeviceInfo)DvbDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject2;
          }
          paramParcel1 = openDvbDevice((DvbDeviceInfo)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramParcel1 = getDvbDeviceList();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramInt1 = isSingleSessionActive(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject4 = (TvStreamConfig)TvStreamConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject3;
          }
          paramInt1 = captureFrame(str, (Surface)localObject9, (TvStreamConfig)localObject4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramParcel1 = getAvailableTvStreamConfigList(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          releaseTvInputHardware(paramParcel1.readInt(), ITvInputHardware.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramInt1 = paramParcel1.readInt();
          localObject3 = ITvInputHardwareCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject9 = (TvInputInfo)TvInputInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = null;
          }
          localObject9 = acquireTvInputHardware(paramInt1, (ITvInputHardwareCallback)localObject3, (TvInputInfo)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject4;
          if (localObject9 != null) {
            paramParcel1 = ((ITvInputHardware)localObject9).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramParcel1 = getHardwareList();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          stopRecording(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject5;
          }
          startRecording((IBinder)localObject4, (Uri)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject9 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          timeShiftEnablePositionTracking((IBinder)localObject9, bool3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (PlaybackParams)PlaybackParams.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject6;
          }
          timeShiftSetPlaybackParams((IBinder)localObject4, (PlaybackParams)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          timeShiftSeekTo(paramParcel1.readStrongBinder(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          timeShiftResume(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          timeShiftPause(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject7;
          }
          timeShiftPlay((IBinder)localObject4, (Uri)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          unblockContent(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          removeOverlayView(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject8;
          }
          relayoutOverlayView((IBinder)localObject4, (Rect)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject3 = paramParcel1.readStrongBinder();
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          }
          createOverlayView((IBinder)localObject3, (IBinder)localObject4, (Rect)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = paramParcel1.readStrongBinder();
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject10;
          }
          sendAppPrivateCommand((IBinder)localObject4, (String)localObject3, (Bundle)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          selectTrack(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject9 = paramParcel1.readStrongBinder();
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          setCaptionEnabled((IBinder)localObject9, bool3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject4 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = str;
          }
          tune((IBinder)localObject3, (Uri)localObject9, (Bundle)localObject4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          setVolume(paramParcel1.readStrongBinder(), paramParcel1.readFloat(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          dispatchSurfaceChanged(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject11;
          }
          setSurface((IBinder)localObject4, (Surface)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          setMainSession(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          releaseSession(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          localObject4 = ITvInputClient.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          createSession((ITvInputClient)localObject4, (String)localObject9, bool3, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          removeBlockedRating(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          addBlockedRating(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramParcel1 = getBlockedRatings(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramInt1 = isRatingBlocked(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          bool3 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          setParentalControlsEnabled(bool3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramInt1 = isParentalControlsEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          unregisterCallback(ITvInputManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          registerCallback(ITvInputManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramParcel1 = getTvContentRatingSystemList(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramInt1 = getTvInputState(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (TvInputInfo)TvInputInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject12;
          }
          updateTvInputInfo((TvInputInfo)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
          paramParcel1 = getTvInputInfo(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputManager");
        paramParcel1 = getTvInputList(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputManager");
      return true;
    }
    
    private static class Proxy
      implements ITvInputManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public ITvInputHardware acquireTvInputHardware(int paramInt1, ITvInputHardwareCallback paramITvInputHardwareCallback, TvInputInfo paramTvInputInfo, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt1);
          if (paramITvInputHardwareCallback != null) {
            paramITvInputHardwareCallback = paramITvInputHardwareCallback.asBinder();
          } else {
            paramITvInputHardwareCallback = null;
          }
          localParcel1.writeStrongBinder(paramITvInputHardwareCallback);
          if (paramTvInputInfo != null)
          {
            localParcel1.writeInt(1);
            paramTvInputInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramITvInputHardwareCallback = ITvInputHardware.Stub.asInterface(localParcel2.readStrongBinder());
          return paramITvInputHardwareCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addBlockedRating(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean captureFrame(String paramString, Surface paramSurface, TvStreamConfig paramTvStreamConfig, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramSurface != null)
          {
            localParcel1.writeInt(1);
            paramSurface.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramTvStreamConfig != null)
          {
            localParcel1.writeInt(1);
            paramTvStreamConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createOverlayView(IBinder paramIBinder1, IBinder paramIBinder2, Rect paramRect, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder1);
          localParcel1.writeStrongBinder(paramIBinder2);
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createSession(ITvInputClient paramITvInputClient, String paramString, boolean paramBoolean, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramITvInputClient != null) {
            paramITvInputClient = paramITvInputClient.asBinder();
          } else {
            paramITvInputClient = null;
          }
          localParcel1.writeStrongBinder(paramITvInputClient);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dispatchSurfaceChanged(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<TvStreamConfig> getAvailableTvStreamConfigList(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(TvStreamConfig.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getBlockedRatings(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createStringArrayList();
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<DvbDeviceInfo> getDvbDeviceList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(DvbDeviceInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<TvInputHardwareInfo> getHardwareList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(TvInputHardwareInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.tv.ITvInputManager";
      }
      
      public List<TvContentRatingSystemInfo> getTvContentRatingSystemList(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(TvContentRatingSystemInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public TvInputInfo getTvInputInfo(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (TvInputInfo)TvInputInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<TvInputInfo> getTvInputList(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(TvInputInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getTvInputState(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isParentalControlsEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRatingBlocked(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSingleSessionActive(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor openDvbDevice(DvbDeviceInfo paramDvbDeviceInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramDvbDeviceInfo != null)
          {
            localParcel1.writeInt(1);
            paramDvbDeviceInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramDvbDeviceInfo = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramDvbDeviceInfo = null;
          }
          return paramDvbDeviceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerCallback(ITvInputManagerCallback paramITvInputManagerCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramITvInputManagerCallback != null) {
            paramITvInputManagerCallback = paramITvInputManagerCallback.asBinder();
          } else {
            paramITvInputManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramITvInputManagerCallback);
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void relayoutOverlayView(IBinder paramIBinder, Rect paramRect, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releaseSession(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releaseTvInputHardware(int paramInt1, ITvInputHardware paramITvInputHardware, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramInt1);
          if (paramITvInputHardware != null) {
            paramITvInputHardware = paramITvInputHardware.asBinder();
          } else {
            paramITvInputHardware = null;
          }
          localParcel1.writeStrongBinder(paramITvInputHardware);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeBlockedRating(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeOverlayView(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestChannelBrowsable(Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void selectTrack(IBinder paramIBinder, int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendAppPrivateCommand(IBinder paramIBinder, String paramString, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendTvInputNotifyIntent(Intent paramIntent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCaptionEnabled(IBinder paramIBinder, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMainSession(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setParentalControlsEnabled(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSurface(IBinder paramIBinder, Surface paramSurface, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramSurface != null)
          {
            localParcel1.writeInt(1);
            paramSurface.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVolume(IBinder paramIBinder, float paramFloat, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeFloat(paramFloat);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startRecording(IBinder paramIBinder, Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopRecording(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void timeShiftEnablePositionTracking(IBinder paramIBinder, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void timeShiftPause(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void timeShiftPlay(IBinder paramIBinder, Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void timeShiftResume(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void timeShiftSeekTo(IBinder paramIBinder, long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void timeShiftSetPlaybackParams(IBinder paramIBinder, PlaybackParams paramPlaybackParams, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramPlaybackParams != null)
          {
            localParcel1.writeInt(1);
            paramPlaybackParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void tune(IBinder paramIBinder, Uri paramUri, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unblockContent(IBinder paramIBinder, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterCallback(ITvInputManagerCallback paramITvInputManagerCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramITvInputManagerCallback != null) {
            paramITvInputManagerCallback = paramITvInputManagerCallback.asBinder();
          } else {
            paramITvInputManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramITvInputManagerCallback);
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateTvInputInfo(TvInputInfo paramTvInputInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputManager");
          if (paramTvInputInfo != null)
          {
            localParcel1.writeInt(1);
            paramTvInputInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
