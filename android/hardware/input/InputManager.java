package android.hardware.input;

import android.app.IInputForwarder;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.VibrationEffect;
import android.os.VibrationEffect.OneShot;
import android.os.VibrationEffect.Waveform;
import android.os.Vibrator;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.util.SparseArray;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.PointerIcon;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public final class InputManager
{
  public static final String ACTION_QUERY_KEYBOARD_LAYOUTS = "android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS";
  private static final boolean DEBUG = false;
  public static final int DEFAULT_POINTER_SPEED = 0;
  public static final int INJECT_INPUT_EVENT_MODE_ASYNC = 0;
  public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2;
  public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1;
  public static final int MAX_POINTER_SPEED = 7;
  public static final String META_DATA_KEYBOARD_LAYOUTS = "android.hardware.input.metadata.KEYBOARD_LAYOUTS";
  public static final int MIN_POINTER_SPEED = -7;
  private static final int MSG_DEVICE_ADDED = 1;
  private static final int MSG_DEVICE_CHANGED = 3;
  private static final int MSG_DEVICE_REMOVED = 2;
  public static final int SWITCH_STATE_OFF = 0;
  public static final int SWITCH_STATE_ON = 1;
  public static final int SWITCH_STATE_UNKNOWN = -1;
  private static final String TAG = "InputManager";
  private static InputManager sInstance;
  private final IInputManager mIm;
  private final ArrayList<InputDeviceListenerDelegate> mInputDeviceListeners = new ArrayList();
  private SparseArray<InputDevice> mInputDevices;
  private InputDevicesChangedListener mInputDevicesChangedListener;
  private final Object mInputDevicesLock = new Object();
  private List<OnTabletModeChangedListenerDelegate> mOnTabletModeChangedListeners;
  private TabletModeChangedListener mTabletModeChangedListener;
  private final Object mTabletModeLock = new Object();
  
  private InputManager(IInputManager paramIInputManager)
  {
    mIm = paramIInputManager;
  }
  
  private static boolean containsDeviceId(int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i += 2) {
      if (paramArrayOfInt[i] == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  private int findInputDeviceListenerLocked(InputDeviceListener paramInputDeviceListener)
  {
    int i = mInputDeviceListeners.size();
    for (int j = 0; j < i; j++) {
      if (mInputDeviceListeners.get(j)).mListener == paramInputDeviceListener) {
        return j;
      }
    }
    return -1;
  }
  
  private int findOnTabletModeChangedListenerLocked(OnTabletModeChangedListener paramOnTabletModeChangedListener)
  {
    int i = mOnTabletModeChangedListeners.size();
    for (int j = 0; j < i; j++) {
      if (mOnTabletModeChangedListeners.get(j)).mListener == paramOnTabletModeChangedListener) {
        return j;
      }
    }
    return -1;
  }
  
  public static InputManager getInstance()
  {
    try
    {
      Object localObject1 = sInstance;
      if (localObject1 == null) {
        try
        {
          localObject1 = new android/hardware/input/InputManager;
          ((InputManager)localObject1).<init>(IInputManager.Stub.asInterface(ServiceManager.getServiceOrThrow("input")));
          sInstance = (InputManager)localObject1;
        }
        catch (ServiceManager.ServiceNotFoundException localServiceNotFoundException)
        {
          localObject1 = new java/lang/IllegalStateException;
          ((IllegalStateException)localObject1).<init>(localServiceNotFoundException);
          throw ((Throwable)localObject1);
        }
      }
      localObject1 = sInstance;
      return localObject1;
    }
    finally {}
  }
  
  private void initializeTabletModeListenerLocked()
  {
    TabletModeChangedListener localTabletModeChangedListener = new TabletModeChangedListener(null);
    try
    {
      mIm.registerTabletModeChangedListener(localTabletModeChangedListener);
      mTabletModeChangedListener = localTabletModeChangedListener;
      mOnTabletModeChangedListeners = new ArrayList();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private void onInputDevicesChanged(int[] paramArrayOfInt)
  {
    synchronized (mInputDevicesLock)
    {
      int i = mInputDevices.size();
      int j;
      for (;;)
      {
        i--;
        if (i <= 0) {
          break;
        }
        j = mInputDevices.keyAt(i);
        if (!containsDeviceId(paramArrayOfInt, j))
        {
          mInputDevices.removeAt(i);
          sendMessageToInputDeviceListenersLocked(2, j);
        }
      }
      for (i = 0; i < paramArrayOfInt.length; i += 2)
      {
        int k = paramArrayOfInt[i];
        j = mInputDevices.indexOfKey(k);
        if (j >= 0)
        {
          InputDevice localInputDevice = (InputDevice)mInputDevices.valueAt(j);
          if (localInputDevice != null)
          {
            int m = paramArrayOfInt[(i + 1)];
            if (localInputDevice.getGeneration() != m)
            {
              mInputDevices.setValueAt(j, null);
              sendMessageToInputDeviceListenersLocked(3, k);
            }
          }
        }
        else
        {
          mInputDevices.put(k, null);
          sendMessageToInputDeviceListenersLocked(1, k);
        }
      }
      return;
    }
  }
  
  private void onTabletModeChanged(long paramLong, boolean paramBoolean)
  {
    synchronized (mTabletModeLock)
    {
      int i = mOnTabletModeChangedListeners.size();
      for (int j = 0; j < i; j++) {
        ((OnTabletModeChangedListenerDelegate)mOnTabletModeChangedListeners.get(j)).sendTabletModeChanged(paramLong, paramBoolean);
      }
      return;
    }
  }
  
  private void populateInputDevicesLocked()
  {
    if (mInputDevicesChangedListener == null)
    {
      InputDevicesChangedListener localInputDevicesChangedListener = new InputDevicesChangedListener(null);
      try
      {
        mIm.registerInputDevicesChangedListener(localInputDevicesChangedListener);
        mInputDevicesChangedListener = localInputDevicesChangedListener;
      }
      catch (RemoteException localRemoteException1)
      {
        throw localRemoteException1.rethrowFromSystemServer();
      }
    }
    if (mInputDevices == null) {
      try
      {
        int[] arrayOfInt = mIm.getInputDeviceIds();
        mInputDevices = new SparseArray();
        for (int i = 0; i < arrayOfInt.length; i++) {
          mInputDevices.put(arrayOfInt[i], null);
        }
        return;
      }
      catch (RemoteException localRemoteException2)
      {
        throw localRemoteException2.rethrowFromSystemServer();
      }
    }
  }
  
  private void sendMessageToInputDeviceListenersLocked(int paramInt1, int paramInt2)
  {
    int i = mInputDeviceListeners.size();
    for (int j = 0; j < i; j++)
    {
      InputDeviceListenerDelegate localInputDeviceListenerDelegate = (InputDeviceListenerDelegate)mInputDeviceListeners.get(j);
      localInputDeviceListenerDelegate.sendMessage(localInputDeviceListenerDelegate.obtainMessage(paramInt1, paramInt2, 0));
    }
  }
  
  public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
  {
    if (paramInputDeviceIdentifier != null)
    {
      if (paramString != null) {
        try
        {
          mIm.addKeyboardLayoutForInputDevice(paramInputDeviceIdentifier, paramString);
          return;
        }
        catch (RemoteException paramInputDeviceIdentifier)
        {
          throw paramInputDeviceIdentifier.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
    }
    throw new IllegalArgumentException("inputDeviceDescriptor must not be null");
  }
  
  public IInputForwarder createInputForwarder(int paramInt)
  {
    try
    {
      IInputForwarder localIInputForwarder = mIm.createInputForwarder(paramInt);
      return localIInputForwarder;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean[] deviceHasKeys(int paramInt, int[] paramArrayOfInt)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfInt.length];
    try
    {
      mIm.hasKeys(paramInt, 65280, paramArrayOfInt, arrayOfBoolean);
      return arrayOfBoolean;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public boolean[] deviceHasKeys(int[] paramArrayOfInt)
  {
    return deviceHasKeys(-1, paramArrayOfInt);
  }
  
  public void disableInputDevice(int paramInt)
  {
    try
    {
      mIm.disableInputDevice(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Could not disable input device with id = ");
      localStringBuilder.append(paramInt);
      Log.w("InputManager", localStringBuilder.toString());
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void enableInputDevice(int paramInt)
  {
    try
    {
      mIm.enableInputDevice(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Could not enable input device with id = ");
      localStringBuilder.append(paramInt);
      Log.w("InputManager", localStringBuilder.toString());
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
  {
    try
    {
      paramInputDeviceIdentifier = mIm.getCurrentKeyboardLayoutForInputDevice(paramInputDeviceIdentifier);
      return paramInputDeviceIdentifier;
    }
    catch (RemoteException paramInputDeviceIdentifier)
    {
      throw paramInputDeviceIdentifier.rethrowFromSystemServer();
    }
  }
  
  public String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
  {
    if (paramInputDeviceIdentifier != null) {
      try
      {
        paramInputDeviceIdentifier = mIm.getEnabledKeyboardLayoutsForInputDevice(paramInputDeviceIdentifier);
        return paramInputDeviceIdentifier;
      }
      catch (RemoteException paramInputDeviceIdentifier)
      {
        throw paramInputDeviceIdentifier.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("inputDeviceDescriptor must not be null");
  }
  
  public InputDevice getInputDevice(int paramInt)
  {
    synchronized (mInputDevicesLock)
    {
      populateInputDevicesLocked();
      int i = mInputDevices.indexOfKey(paramInt);
      if (i < 0) {
        return null;
      }
      InputDevice localInputDevice1 = (InputDevice)mInputDevices.valueAt(i);
      InputDevice localInputDevice2 = localInputDevice1;
      if (localInputDevice1 == null) {
        try
        {
          localInputDevice1 = mIm.getInputDevice(paramInt);
          localInputDevice2 = localInputDevice1;
          if (localInputDevice1 != null)
          {
            mInputDevices.setValueAt(i, localInputDevice1);
            localInputDevice2 = localInputDevice1;
          }
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      return localRemoteException;
    }
  }
  
  public InputDevice getInputDeviceByDescriptor(String paramString)
  {
    if (paramString != null) {
      synchronized (mInputDevicesLock)
      {
        populateInputDevicesLocked();
        int i = mInputDevices.size();
        for (int j = 0; j < i; j++)
        {
          InputDevice localInputDevice1 = (InputDevice)mInputDevices.valueAt(j);
          InputDevice localInputDevice2 = localInputDevice1;
          if (localInputDevice1 == null)
          {
            int k = mInputDevices.keyAt(j);
            try
            {
              localInputDevice2 = mIm.getInputDevice(k);
              if (localInputDevice2 == null) {
                continue;
              }
              mInputDevices.setValueAt(j, localInputDevice2);
            }
            catch (RemoteException paramString)
            {
              throw paramString.rethrowFromSystemServer();
            }
          }
          if (paramString.equals(localInputDevice2.getDescriptor())) {
            return localInputDevice2;
          }
        }
        return null;
      }
    }
    throw new IllegalArgumentException("descriptor must not be null.");
  }
  
  public int[] getInputDeviceIds()
  {
    synchronized (mInputDevicesLock)
    {
      populateInputDevicesLocked();
      int i = mInputDevices.size();
      int[] arrayOfInt = new int[i];
      for (int j = 0; j < i; j++) {
        arrayOfInt[j] = mInputDevices.keyAt(j);
      }
      return arrayOfInt;
    }
  }
  
  public Vibrator getInputDeviceVibrator(int paramInt)
  {
    return new InputDeviceVibrator(paramInt);
  }
  
  public KeyboardLayout getKeyboardLayout(String paramString)
  {
    if (paramString != null) {
      try
      {
        paramString = mIm.getKeyboardLayout(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
  }
  
  public KeyboardLayout[] getKeyboardLayouts()
  {
    try
    {
      KeyboardLayout[] arrayOfKeyboardLayout = mIm.getKeyboardLayouts();
      return arrayOfKeyboardLayout;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public KeyboardLayout[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
  {
    try
    {
      paramInputDeviceIdentifier = mIm.getKeyboardLayoutsForInputDevice(paramInputDeviceIdentifier);
      return paramInputDeviceIdentifier;
    }
    catch (RemoteException paramInputDeviceIdentifier)
    {
      throw paramInputDeviceIdentifier.rethrowFromSystemServer();
    }
  }
  
  public int getPointerSpeed(Context paramContext)
  {
    int i = 0;
    int j;
    try
    {
      j = Settings.System.getInt(paramContext.getContentResolver(), "pointer_speed");
    }
    catch (Settings.SettingNotFoundException paramContext)
    {
      j = i;
    }
    return j;
  }
  
  public TouchCalibration getTouchCalibration(String paramString, int paramInt)
  {
    try
    {
      paramString = mIm.getTouchCalibrationForInputDevice(paramString, paramInt);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean injectInputEvent(InputEvent paramInputEvent, int paramInt)
  {
    if (paramInputEvent != null)
    {
      if ((paramInt != 0) && (paramInt != 2) && (paramInt != 1)) {
        throw new IllegalArgumentException("mode is invalid");
      }
      try
      {
        boolean bool = mIm.injectInputEvent(paramInputEvent, paramInt);
        return bool;
      }
      catch (RemoteException paramInputEvent)
      {
        throw paramInputEvent.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("event must not be null");
  }
  
  public int isInTabletMode()
  {
    try
    {
      int i = mIm.isInTabletMode();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isInputDeviceEnabled(int paramInt)
  {
    try
    {
      boolean bool = mIm.isInputDeviceEnabled(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Could not check enabled status of input device with id = ");
      localStringBuilder.append(paramInt);
      Log.w("InputManager", localStringBuilder.toString());
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void registerInputDeviceListener(InputDeviceListener paramInputDeviceListener, Handler paramHandler)
  {
    if (paramInputDeviceListener != null) {
      synchronized (mInputDevicesLock)
      {
        populateInputDevicesLocked();
        if (findInputDeviceListenerLocked(paramInputDeviceListener) < 0)
        {
          ArrayList localArrayList = mInputDeviceListeners;
          InputDeviceListenerDelegate localInputDeviceListenerDelegate = new android/hardware/input/InputManager$InputDeviceListenerDelegate;
          localInputDeviceListenerDelegate.<init>(paramInputDeviceListener, paramHandler);
          localArrayList.add(localInputDeviceListenerDelegate);
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public void registerOnTabletModeChangedListener(OnTabletModeChangedListener paramOnTabletModeChangedListener, Handler paramHandler)
  {
    if (paramOnTabletModeChangedListener != null) {
      synchronized (mTabletModeLock)
      {
        if (mOnTabletModeChangedListeners == null) {
          initializeTabletModeListenerLocked();
        }
        if (findOnTabletModeChangedListenerLocked(paramOnTabletModeChangedListener) < 0)
        {
          OnTabletModeChangedListenerDelegate localOnTabletModeChangedListenerDelegate = new android/hardware/input/InputManager$OnTabletModeChangedListenerDelegate;
          localOnTabletModeChangedListenerDelegate.<init>(paramOnTabletModeChangedListener, paramHandler);
          mOnTabletModeChangedListeners.add(localOnTabletModeChangedListenerDelegate);
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
  {
    if (paramInputDeviceIdentifier != null)
    {
      if (paramString != null) {
        try
        {
          mIm.removeKeyboardLayoutForInputDevice(paramInputDeviceIdentifier, paramString);
          return;
        }
        catch (RemoteException paramInputDeviceIdentifier)
        {
          throw paramInputDeviceIdentifier.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
    }
    throw new IllegalArgumentException("inputDeviceDescriptor must not be null");
  }
  
  public void requestPointerCapture(IBinder paramIBinder, boolean paramBoolean)
  {
    try
    {
      mIm.requestPointerCapture(paramIBinder, paramBoolean);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
  {
    if (paramInputDeviceIdentifier != null)
    {
      if (paramString != null) {
        try
        {
          mIm.setCurrentKeyboardLayoutForInputDevice(paramInputDeviceIdentifier, paramString);
          return;
        }
        catch (RemoteException paramInputDeviceIdentifier)
        {
          throw paramInputDeviceIdentifier.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
    }
    throw new IllegalArgumentException("identifier must not be null");
  }
  
  public void setCustomPointerIcon(PointerIcon paramPointerIcon)
  {
    try
    {
      mIm.setCustomPointerIcon(paramPointerIcon);
      return;
    }
    catch (RemoteException paramPointerIcon)
    {
      throw paramPointerIcon.rethrowFromSystemServer();
    }
  }
  
  public void setPointerIconType(int paramInt)
  {
    try
    {
      mIm.setPointerIconType(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setPointerSpeed(Context paramContext, int paramInt)
  {
    if ((paramInt >= -7) && (paramInt <= 7))
    {
      Settings.System.putInt(paramContext.getContentResolver(), "pointer_speed", paramInt);
      return;
    }
    throw new IllegalArgumentException("speed out of range");
  }
  
  public void setTouchCalibration(String paramString, int paramInt, TouchCalibration paramTouchCalibration)
  {
    try
    {
      mIm.setTouchCalibrationForInputDevice(paramString, paramInt, paramTouchCalibration);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void tryPointerSpeed(int paramInt)
  {
    if ((paramInt >= -7) && (paramInt <= 7)) {
      try
      {
        mIm.tryPointerSpeed(paramInt);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("speed out of range");
  }
  
  public void unregisterInputDeviceListener(InputDeviceListener paramInputDeviceListener)
  {
    if (paramInputDeviceListener != null) {
      synchronized (mInputDevicesLock)
      {
        int i = findInputDeviceListenerLocked(paramInputDeviceListener);
        if (i >= 0)
        {
          ((InputDeviceListenerDelegate)mInputDeviceListeners.get(i)).removeCallbacksAndMessages(null);
          mInputDeviceListeners.remove(i);
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public void unregisterOnTabletModeChangedListener(OnTabletModeChangedListener paramOnTabletModeChangedListener)
  {
    if (paramOnTabletModeChangedListener != null) {
      synchronized (mTabletModeLock)
      {
        int i = findOnTabletModeChangedListenerLocked(paramOnTabletModeChangedListener);
        if (i >= 0) {
          ((OnTabletModeChangedListenerDelegate)mOnTabletModeChangedListeners.remove(i)).removeCallbacksAndMessages(null);
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public static abstract interface InputDeviceListener
  {
    public abstract void onInputDeviceAdded(int paramInt);
    
    public abstract void onInputDeviceChanged(int paramInt);
    
    public abstract void onInputDeviceRemoved(int paramInt);
  }
  
  private static final class InputDeviceListenerDelegate
    extends Handler
  {
    public final InputManager.InputDeviceListener mListener;
    
    public InputDeviceListenerDelegate(InputManager.InputDeviceListener paramInputDeviceListener, Handler paramHandler)
    {
      super();
      mListener = paramInputDeviceListener;
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 3: 
        mListener.onInputDeviceChanged(arg1);
        break;
      case 2: 
        mListener.onInputDeviceRemoved(arg1);
        break;
      case 1: 
        mListener.onInputDeviceAdded(arg1);
      }
    }
  }
  
  private final class InputDeviceVibrator
    extends Vibrator
  {
    private final int mDeviceId;
    private final Binder mToken;
    
    public InputDeviceVibrator(int paramInt)
    {
      mDeviceId = paramInt;
      mToken = new Binder();
    }
    
    public void cancel()
    {
      try
      {
        mIm.cancelVibrate(mDeviceId, mToken);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public boolean hasAmplitudeControl()
    {
      return false;
    }
    
    public boolean hasVibrator()
    {
      return true;
    }
    
    public void vibrate(int paramInt, String paramString, VibrationEffect paramVibrationEffect, AudioAttributes paramAudioAttributes)
    {
      if ((paramVibrationEffect instanceof VibrationEffect.OneShot))
      {
        paramVibrationEffect = (VibrationEffect.OneShot)paramVibrationEffect;
        paramString = new long[2];
        paramString[0] = 0L;
        paramString[1] = paramVibrationEffect.getDuration();
        paramInt = -1;
      }
      else
      {
        if (!(paramVibrationEffect instanceof VibrationEffect.Waveform)) {
          break label83;
        }
        paramVibrationEffect = (VibrationEffect.Waveform)paramVibrationEffect;
        paramString = paramVibrationEffect.getTimings();
        paramInt = paramVibrationEffect.getRepeatIndex();
      }
      try
      {
        mIm.vibrate(mDeviceId, paramString, paramInt, mToken);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      label83:
      Log.w("InputManager", "Pre-baked effects aren't supported on input devices");
    }
  }
  
  private final class InputDevicesChangedListener
    extends IInputDevicesChangedListener.Stub
  {
    private InputDevicesChangedListener() {}
    
    public void onInputDevicesChanged(int[] paramArrayOfInt)
      throws RemoteException
    {
      InputManager.this.onInputDevicesChanged(paramArrayOfInt);
    }
  }
  
  public static abstract interface OnTabletModeChangedListener
  {
    public abstract void onTabletModeChanged(long paramLong, boolean paramBoolean);
  }
  
  private static final class OnTabletModeChangedListenerDelegate
    extends Handler
  {
    private static final int MSG_TABLET_MODE_CHANGED = 0;
    public final InputManager.OnTabletModeChangedListener mListener;
    
    public OnTabletModeChangedListenerDelegate(InputManager.OnTabletModeChangedListener paramOnTabletModeChangedListener, Handler paramHandler)
    {
      super();
      mListener = paramOnTabletModeChangedListener;
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (what == 0)
      {
        paramMessage = (SomeArgs)obj;
        long l1 = argi1;
        long l2 = argi2;
        boolean bool = ((Boolean)arg1).booleanValue();
        mListener.onTabletModeChanged(l1 & 0xFFFFFFFF | l2 << 32, bool);
      }
    }
    
    public void sendTabletModeChanged(long paramLong, boolean paramBoolean)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      argi1 = ((int)(0xFFFFFFFFFFFFFFFF & paramLong));
      argi2 = ((int)(paramLong >> 32));
      arg1 = Boolean.valueOf(paramBoolean);
      obtainMessage(0, localSomeArgs).sendToTarget();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SwitchState {}
  
  private final class TabletModeChangedListener
    extends ITabletModeChangedListener.Stub
  {
    private TabletModeChangedListener() {}
    
    public void onTabletModeChanged(long paramLong, boolean paramBoolean)
    {
      InputManager.this.onTabletModeChanged(paramLong, paramBoolean);
    }
  }
}
