package android.view;

import android.content.ClipData;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.MergedConfiguration;

public abstract interface IWindowSession
  extends IInterface
{
  public abstract int add(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, Rect paramRect1, Rect paramRect2, InputChannel paramInputChannel)
    throws RemoteException;
  
  public abstract int addToDisplay(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, DisplayCutout.ParcelableWrapper paramParcelableWrapper, InputChannel paramInputChannel)
    throws RemoteException;
  
  public abstract int addToDisplayWithoutInputChannel(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2)
    throws RemoteException;
  
  public abstract int addWithoutInputChannel(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, Rect paramRect1, Rect paramRect2)
    throws RemoteException;
  
  public abstract void cancelDragAndDrop(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void dragRecipientEntered(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract void dragRecipientExited(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract void finishDrawing(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract void getDisplayFrame(IWindow paramIWindow, Rect paramRect)
    throws RemoteException;
  
  public abstract boolean getInTouchMode()
    throws RemoteException;
  
  public abstract IWindowId getWindowId(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onRectangleOnScreenRequested(IBinder paramIBinder, Rect paramRect)
    throws RemoteException;
  
  public abstract boolean outOfMemory(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract IBinder performDrag(IWindow paramIWindow, int paramInt1, SurfaceControl paramSurfaceControl, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ClipData paramClipData)
    throws RemoteException;
  
  public abstract boolean performHapticFeedback(IWindow paramIWindow, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void pokeDrawLock(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void prepareToReplaceWindows(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int relayout(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong, Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, Rect paramRect7, DisplayCutout.ParcelableWrapper paramParcelableWrapper, MergedConfiguration paramMergedConfiguration, Surface paramSurface)
    throws RemoteException;
  
  public abstract void remove(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract void reportDropResult(IWindow paramIWindow, boolean paramBoolean)
    throws RemoteException;
  
  public abstract Bundle sendWallpaperCommand(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setInTouchMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setInsets(IWindow paramIWindow, int paramInt, Rect paramRect1, Rect paramRect2, Region paramRegion)
    throws RemoteException;
  
  public abstract void setTransparentRegion(IWindow paramIWindow, Region paramRegion)
    throws RemoteException;
  
  public abstract void setWallpaperDisplayOffset(IBinder paramIBinder, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setWallpaperPosition(IBinder paramIBinder, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    throws RemoteException;
  
  public abstract boolean startMovingTask(IWindow paramIWindow, float paramFloat1, float paramFloat2)
    throws RemoteException;
  
  public abstract void updatePointerIcon(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract void updateTapExcludeRegion(IWindow paramIWindow, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract void wallpaperCommandComplete(IBinder paramIBinder, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void wallpaperOffsetsComplete(IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWindowSession
  {
    private static final String DESCRIPTOR = "android.view.IWindowSession";
    static final int TRANSACTION_add = 1;
    static final int TRANSACTION_addToDisplay = 2;
    static final int TRANSACTION_addToDisplayWithoutInputChannel = 4;
    static final int TRANSACTION_addWithoutInputChannel = 3;
    static final int TRANSACTION_cancelDragAndDrop = 18;
    static final int TRANSACTION_dragRecipientEntered = 19;
    static final int TRANSACTION_dragRecipientExited = 20;
    static final int TRANSACTION_finishDrawing = 12;
    static final int TRANSACTION_getDisplayFrame = 11;
    static final int TRANSACTION_getInTouchMode = 14;
    static final int TRANSACTION_getWindowId = 27;
    static final int TRANSACTION_onRectangleOnScreenRequested = 26;
    static final int TRANSACTION_outOfMemory = 8;
    static final int TRANSACTION_performDrag = 16;
    static final int TRANSACTION_performHapticFeedback = 15;
    static final int TRANSACTION_pokeDrawLock = 28;
    static final int TRANSACTION_prepareToReplaceWindows = 7;
    static final int TRANSACTION_relayout = 6;
    static final int TRANSACTION_remove = 5;
    static final int TRANSACTION_reportDropResult = 17;
    static final int TRANSACTION_sendWallpaperCommand = 24;
    static final int TRANSACTION_setInTouchMode = 13;
    static final int TRANSACTION_setInsets = 10;
    static final int TRANSACTION_setTransparentRegion = 9;
    static final int TRANSACTION_setWallpaperDisplayOffset = 23;
    static final int TRANSACTION_setWallpaperPosition = 21;
    static final int TRANSACTION_startMovingTask = 29;
    static final int TRANSACTION_updatePointerIcon = 30;
    static final int TRANSACTION_updateTapExcludeRegion = 31;
    static final int TRANSACTION_wallpaperCommandComplete = 25;
    static final int TRANSACTION_wallpaperOffsetsComplete = 22;
    
    public Stub()
    {
      attachInterface(this, "android.view.IWindowSession");
    }
    
    public static IWindowSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IWindowSession");
      if ((localIInterface != null) && ((localIInterface instanceof IWindowSession))) {
        return (IWindowSession)localIInterface;
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
        boolean bool4 = false;
        Rect localRect1 = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Rect localRect2 = null;
        Rect localRect3 = null;
        MergedConfiguration localMergedConfiguration = null;
        Surface localSurface = null;
        DisplayCutout.ParcelableWrapper localParcelableWrapper = null;
        Object localObject5 = null;
        Object localObject6 = null;
        int i;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 31: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          updateTapExcludeRegion(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          updatePointerIcon(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          paramInt1 = startMovingTask(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readFloat(), paramParcel1.readFloat());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          pokeDrawLock(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = getWindowId(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject6;
          if (localObject2 != null) {
            paramParcel1 = ((IWindowId)localObject2).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localRect1;
          }
          onRectangleOnScreenRequested((IBinder)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          wallpaperCommandComplete((IBinder)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject3 = paramParcel1.readStrongBinder();
          localObject4 = paramParcel1.readString();
          i = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          paramParcel1 = sendWallpaperCommand((IBinder)localObject3, (String)localObject4, i, paramInt2, paramInt1, (Bundle)localObject2, bool4);
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
        case 23: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          setWallpaperDisplayOffset(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          wallpaperOffsetsComplete(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          setWallpaperPosition(paramParcel1.readStrongBinder(), paramParcel1.readFloat(), paramParcel1.readFloat(), paramParcel1.readFloat(), paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          dragRecipientExited(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          dragRecipientEntered(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          cancelDragAndDrop(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          reportDropResult((IWindow)localObject2, bool4);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject4 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (SurfaceControl)SurfaceControl.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          float f1 = paramParcel1.readFloat();
          float f2 = paramParcel1.readFloat();
          float f3 = paramParcel1.readFloat();
          float f4 = paramParcel1.readFloat();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (ClipData)ClipData.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject3) {
            break;
          }
          paramParcel1 = performDrag((IWindow)localObject4, paramInt2, (SurfaceControl)localObject2, paramInt1, f1, f2, f3, f4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          paramInt1 = performHapticFeedback((IWindow)localObject2, paramInt1, bool4);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          paramInt1 = getInTouchMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setInTouchMode(bool4);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          finishDrawing(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          paramParcel1 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject2 = new Rect();
          getDisplayFrame(paramParcel1, (Rect)localObject2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          ((Rect)localObject2).writeToParcel(paramParcel2, 1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject6 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Region)Region.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject4) {
            break;
          }
          setInsets((IWindow)localObject6, paramInt1, (Rect)localObject2, (Rect)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localRect2;
          }
          setTransparentRegion((IWindow)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          paramInt1 = outOfMemory(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject2 = paramParcel1.readStrongBinder();
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          prepareToReplaceWindows((IBinder)localObject2, bool4);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject3 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          int j = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (WindowManager.LayoutParams)WindowManager.LayoutParams.CREATOR.createFromParcel(paramParcel1);; localObject2 = localRect3) {
            break;
          }
          i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          int k = paramParcel1.readInt();
          long l = paramParcel1.readLong();
          localRect1 = new Rect();
          paramParcel1 = new Rect();
          localObject1 = new Rect();
          localObject6 = new Rect();
          localRect3 = new Rect();
          localObject4 = new Rect();
          localRect2 = new Rect();
          localParcelableWrapper = new DisplayCutout.ParcelableWrapper();
          localMergedConfiguration = new MergedConfiguration();
          localSurface = new Surface();
          paramInt1 = relayout((IWindow)localObject3, j, (WindowManager.LayoutParams)localObject2, i, paramInt1, paramInt2, k, l, localRect1, paramParcel1, (Rect)localObject1, (Rect)localObject6, localRect3, (Rect)localObject4, localRect2, localParcelableWrapper, localMergedConfiguration, localSurface);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          localRect1.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          ((Rect)localObject1).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          ((Rect)localObject6).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localRect3.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          ((Rect)localObject4).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localRect2.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localParcelableWrapper.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localMergedConfiguration.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localSurface.writeToParcel(paramParcel2, 1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          remove(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject3 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (WindowManager.LayoutParams)WindowManager.LayoutParams.CREATOR.createFromParcel(paramParcel1);; localObject2 = localMergedConfiguration) {
            break;
          }
          i = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject4 = new Rect();
          paramParcel1 = new Rect();
          paramInt1 = addToDisplayWithoutInputChannel((IWindow)localObject3, paramInt1, (WindowManager.LayoutParams)localObject2, i, paramInt2, (Rect)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          ((Rect)localObject4).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject3 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (WindowManager.LayoutParams)WindowManager.LayoutParams.CREATOR.createFromParcel(paramParcel1);; localObject2 = localSurface) {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          localObject4 = new Rect();
          paramParcel1 = new Rect();
          paramInt1 = addWithoutInputChannel((IWindow)localObject3, paramInt2, (WindowManager.LayoutParams)localObject2, paramInt1, (Rect)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          ((Rect)localObject4).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IWindowSession");
          localObject3 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
          i = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (WindowManager.LayoutParams)WindowManager.LayoutParams.CREATOR.createFromParcel(paramParcel1);; localObject2 = localParcelableWrapper) {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject4 = new Rect();
          localRect1 = new Rect();
          localRect2 = new Rect();
          localObject6 = new Rect();
          localObject1 = new DisplayCutout.ParcelableWrapper();
          paramParcel1 = new InputChannel();
          paramInt1 = addToDisplay((IWindow)localObject3, i, (WindowManager.LayoutParams)localObject2, paramInt1, paramInt2, (Rect)localObject4, localRect1, localRect2, (Rect)localObject6, (DisplayCutout.ParcelableWrapper)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          ((Rect)localObject4).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localRect1.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localRect2.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          ((Rect)localObject6).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          ((DisplayCutout.ParcelableWrapper)localObject1).writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel1.enforceInterface("android.view.IWindowSession");
        localObject3 = IWindow.Stub.asInterface(paramParcel1.readStrongBinder());
        paramInt2 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (localObject2 = (WindowManager.LayoutParams)WindowManager.LayoutParams.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject5) {
          break;
        }
        paramInt1 = paramParcel1.readInt();
        localObject6 = new Rect();
        paramParcel1 = new Rect();
        localObject4 = new InputChannel();
        paramInt1 = add((IWindow)localObject3, paramInt2, (WindowManager.LayoutParams)localObject2, paramInt1, (Rect)localObject6, paramParcel1, (InputChannel)localObject4);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        paramParcel2.writeInt(1);
        ((Rect)localObject6).writeToParcel(paramParcel2, 1);
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
        paramParcel2.writeInt(1);
        ((InputChannel)localObject4).writeToParcel(paramParcel2, 1);
        return true;
      }
      paramParcel2.writeString("android.view.IWindowSession");
      return true;
    }
    
    private static class Proxy
      implements IWindowSession
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int add(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, Rect paramRect1, Rect paramRect2, InputChannel paramInputChannel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt1);
          if (paramLayoutParams != null)
          {
            localParcel1.writeInt(1);
            paramLayoutParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramRect1.readFromParcel(localParcel2);
          }
          if (localParcel2.readInt() != 0) {
            paramRect2.readFromParcel(localParcel2);
          }
          if (localParcel2.readInt() != 0) {
            paramInputChannel.readFromParcel(localParcel2);
          }
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public int addToDisplay(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, DisplayCutout.ParcelableWrapper paramParcelableWrapper, InputChannel paramInputChannel)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +13 -> 31
        //   21: aload_1
        //   22: invokeinterface 42 1 0
        //   27: astore_1
        //   28: goto +5 -> 33
        //   31: aconst_null
        //   32: astore_1
        //   33: aload 12
        //   35: aload_1
        //   36: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   39: aload 12
        //   41: iload_2
        //   42: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   45: aload_3
        //   46: ifnull +19 -> 65
        //   49: aload 12
        //   51: iconst_1
        //   52: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   55: aload_3
        //   56: aload 12
        //   58: iconst_0
        //   59: invokevirtual 55	android/view/WindowManager$LayoutParams:writeToParcel	(Landroid/os/Parcel;I)V
        //   62: goto +9 -> 71
        //   65: aload 12
        //   67: iconst_0
        //   68: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   71: aload 12
        //   73: iload 4
        //   75: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   78: aload 12
        //   80: iload 5
        //   82: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   85: aload_0
        //   86: getfield 19	android/view/IWindowSession$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   89: iconst_2
        //   90: aload 12
        //   92: aload 13
        //   94: iconst_0
        //   95: invokeinterface 61 5 0
        //   100: pop
        //   101: aload 13
        //   103: invokevirtual 64	android/os/Parcel:readException	()V
        //   106: aload 13
        //   108: invokevirtual 68	android/os/Parcel:readInt	()I
        //   111: istore_2
        //   112: aload 13
        //   114: invokevirtual 68	android/os/Parcel:readInt	()I
        //   117: istore 4
        //   119: iload 4
        //   121: ifeq +17 -> 138
        //   124: aload 6
        //   126: aload 13
        //   128: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   131: goto +7 -> 138
        //   134: astore_1
        //   135: goto +162 -> 297
        //   138: aload 13
        //   140: invokevirtual 68	android/os/Parcel:readInt	()I
        //   143: istore 4
        //   145: iload 4
        //   147: ifeq +17 -> 164
        //   150: aload 7
        //   152: aload 13
        //   154: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   157: goto +7 -> 164
        //   160: astore_1
        //   161: goto +136 -> 297
        //   164: aload 13
        //   166: invokevirtual 68	android/os/Parcel:readInt	()I
        //   169: istore 4
        //   171: iload 4
        //   173: ifeq +17 -> 190
        //   176: aload 8
        //   178: aload 13
        //   180: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   183: goto +7 -> 190
        //   186: astore_1
        //   187: goto +110 -> 297
        //   190: aload 13
        //   192: invokevirtual 68	android/os/Parcel:readInt	()I
        //   195: istore 4
        //   197: iload 4
        //   199: ifeq +17 -> 216
        //   202: aload 9
        //   204: aload 13
        //   206: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   209: goto +7 -> 216
        //   212: astore_1
        //   213: goto +84 -> 297
        //   216: aload 13
        //   218: invokevirtual 68	android/os/Parcel:readInt	()I
        //   221: istore 4
        //   223: iload 4
        //   225: ifeq +17 -> 242
        //   228: aload 10
        //   230: aload 13
        //   232: invokevirtual 86	android/view/DisplayCutout$ParcelableWrapper:readFromParcel	(Landroid/os/Parcel;)V
        //   235: goto +7 -> 242
        //   238: astore_1
        //   239: goto +58 -> 297
        //   242: aload 13
        //   244: invokevirtual 68	android/os/Parcel:readInt	()I
        //   247: istore 4
        //   249: iload 4
        //   251: ifeq +17 -> 268
        //   254: aload 11
        //   256: aload 13
        //   258: invokevirtual 77	android/view/InputChannel:readFromParcel	(Landroid/os/Parcel;)V
        //   261: goto +7 -> 268
        //   264: astore_1
        //   265: goto +32 -> 297
        //   268: aload 13
        //   270: invokevirtual 80	android/os/Parcel:recycle	()V
        //   273: aload 12
        //   275: invokevirtual 80	android/os/Parcel:recycle	()V
        //   278: iload_2
        //   279: ireturn
        //   280: astore_1
        //   281: goto +16 -> 297
        //   284: astore_1
        //   285: goto +12 -> 297
        //   288: astore_1
        //   289: goto +8 -> 297
        //   292: astore_1
        //   293: goto +4 -> 297
        //   296: astore_1
        //   297: aload 13
        //   299: invokevirtual 80	android/os/Parcel:recycle	()V
        //   302: aload 12
        //   304: invokevirtual 80	android/os/Parcel:recycle	()V
        //   307: aload_1
        //   308: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	309	0	this	Proxy
        //   0	309	1	paramIWindow	IWindow
        //   0	309	2	paramInt1	int
        //   0	309	3	paramLayoutParams	WindowManager.LayoutParams
        //   0	309	4	paramInt2	int
        //   0	309	5	paramInt3	int
        //   0	309	6	paramRect1	Rect
        //   0	309	7	paramRect2	Rect
        //   0	309	8	paramRect3	Rect
        //   0	309	9	paramRect4	Rect
        //   0	309	10	paramParcelableWrapper	DisplayCutout.ParcelableWrapper
        //   0	309	11	paramInputChannel	InputChannel
        //   3	300	12	localParcel1	Parcel
        //   8	290	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   124	131	134	finally
        //   138	145	134	finally
        //   150	157	160	finally
        //   164	171	160	finally
        //   176	183	186	finally
        //   190	197	186	finally
        //   202	209	212	finally
        //   216	223	212	finally
        //   228	235	238	finally
        //   242	249	238	finally
        //   254	261	264	finally
        //   85	119	280	finally
        //   78	85	284	finally
        //   71	78	288	finally
        //   39	45	292	finally
        //   49	62	292	finally
        //   65	71	292	finally
        //   10	17	296	finally
        //   21	28	296	finally
        //   33	39	296	finally
      }
      
      public int addToDisplayWithoutInputChannel(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt1);
          if (paramLayoutParams != null)
          {
            localParcel1.writeInt(1);
            paramLayoutParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramRect1.readFromParcel(localParcel2);
          }
          if (localParcel2.readInt() != 0) {
            paramRect2.readFromParcel(localParcel2);
          }
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int addWithoutInputChannel(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, Rect paramRect1, Rect paramRect2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt1);
          if (paramLayoutParams != null)
          {
            localParcel1.writeInt(1);
            paramLayoutParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramRect1.readFromParcel(localParcel2);
          }
          if (localParcel2.readInt() != 0) {
            paramRect2.readFromParcel(localParcel2);
          }
          return paramInt1;
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
      
      public void cancelDragAndDrop(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void dragRecipientEntered(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
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
      
      public void dragRecipientExited(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
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
      
      public void finishDrawing(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
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
      
      public void getDisplayFrame(IWindow paramIWindow, Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramRect.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getInTouchMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
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
      
      public String getInterfaceDescriptor()
      {
        return "android.view.IWindowSession";
      }
      
      public IWindowId getWindowId(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = IWindowId.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onRectangleOnScreenRequested(IBinder paramIBinder, Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
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
      
      public boolean outOfMemory(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          paramIWindow = mRemote;
          boolean bool = false;
          paramIWindow.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
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
      
      public IBinder performDrag(IWindow paramIWindow, int paramInt1, SurfaceControl paramSurfaceControl, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ClipData paramClipData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt1);
          if (paramSurfaceControl != null)
          {
            localParcel1.writeInt(1);
            paramSurfaceControl.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeFloat(paramFloat1);
          localParcel1.writeFloat(paramFloat2);
          localParcel1.writeFloat(paramFloat3);
          localParcel1.writeFloat(paramFloat4);
          if (paramClipData != null)
          {
            localParcel1.writeInt(1);
            paramClipData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIWindow = localParcel2.readStrongBinder();
          return paramIWindow;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean performHapticFeedback(IWindow paramIWindow, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          paramIWindow = mRemote;
          boolean bool = false;
          paramIWindow.transact(15, localParcel1, localParcel2, 0);
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
      
      public void pokeDrawLock(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void prepareToReplaceWindows(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
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
      
      /* Error */
      public int relayout(IWindow paramIWindow, int paramInt1, WindowManager.LayoutParams paramLayoutParams, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong, Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, Rect paramRect7, DisplayCutout.ParcelableWrapper paramParcelableWrapper, MergedConfiguration paramMergedConfiguration, Surface paramSurface)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 20
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 21
        //   10: aload 20
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +13 -> 31
        //   21: aload_1
        //   22: invokeinterface 42 1 0
        //   27: astore_1
        //   28: goto +5 -> 33
        //   31: aconst_null
        //   32: astore_1
        //   33: aload 20
        //   35: aload_1
        //   36: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   39: aload 20
        //   41: iload_2
        //   42: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   45: aload_3
        //   46: ifnull +19 -> 65
        //   49: aload 20
        //   51: iconst_1
        //   52: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   55: aload_3
        //   56: aload 20
        //   58: iconst_0
        //   59: invokevirtual 55	android/view/WindowManager$LayoutParams:writeToParcel	(Landroid/os/Parcel;I)V
        //   62: goto +9 -> 71
        //   65: aload 20
        //   67: iconst_0
        //   68: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   71: aload 20
        //   73: iload 4
        //   75: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   78: aload 20
        //   80: iload 5
        //   82: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   85: aload 20
        //   87: iload 6
        //   89: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   92: aload 20
        //   94: iload 7
        //   96: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   99: aload 20
        //   101: lload 8
        //   103: invokevirtual 139	android/os/Parcel:writeLong	(J)V
        //   106: aload_0
        //   107: getfield 19	android/view/IWindowSession$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   110: bipush 6
        //   112: aload 20
        //   114: aload 21
        //   116: iconst_0
        //   117: invokeinterface 61 5 0
        //   122: pop
        //   123: aload 21
        //   125: invokevirtual 64	android/os/Parcel:readException	()V
        //   128: aload 21
        //   130: invokevirtual 68	android/os/Parcel:readInt	()I
        //   133: istore_2
        //   134: aload 21
        //   136: invokevirtual 68	android/os/Parcel:readInt	()I
        //   139: istore 4
        //   141: iload 4
        //   143: ifeq +17 -> 160
        //   146: aload 10
        //   148: aload 21
        //   150: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   153: goto +7 -> 160
        //   156: astore_1
        //   157: goto +230 -> 387
        //   160: aload 21
        //   162: invokevirtual 68	android/os/Parcel:readInt	()I
        //   165: istore 4
        //   167: iload 4
        //   169: ifeq +17 -> 186
        //   172: aload 11
        //   174: aload 21
        //   176: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   179: goto +7 -> 186
        //   182: astore_1
        //   183: goto +204 -> 387
        //   186: aload 21
        //   188: invokevirtual 68	android/os/Parcel:readInt	()I
        //   191: istore 4
        //   193: iload 4
        //   195: ifeq +17 -> 212
        //   198: aload 12
        //   200: aload 21
        //   202: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   205: goto +7 -> 212
        //   208: astore_1
        //   209: goto +178 -> 387
        //   212: aload 21
        //   214: invokevirtual 68	android/os/Parcel:readInt	()I
        //   217: ifeq +13 -> 230
        //   220: aload 13
        //   222: aload 21
        //   224: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   227: goto +3 -> 230
        //   230: aload 21
        //   232: invokevirtual 68	android/os/Parcel:readInt	()I
        //   235: ifeq +13 -> 248
        //   238: aload 14
        //   240: aload 21
        //   242: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   245: goto +3 -> 248
        //   248: aload 21
        //   250: invokevirtual 68	android/os/Parcel:readInt	()I
        //   253: ifeq +13 -> 266
        //   256: aload 15
        //   258: aload 21
        //   260: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   263: goto +3 -> 266
        //   266: aload 21
        //   268: invokevirtual 68	android/os/Parcel:readInt	()I
        //   271: ifeq +13 -> 284
        //   274: aload 16
        //   276: aload 21
        //   278: invokevirtual 74	android/graphics/Rect:readFromParcel	(Landroid/os/Parcel;)V
        //   281: goto +3 -> 284
        //   284: aload 21
        //   286: invokevirtual 68	android/os/Parcel:readInt	()I
        //   289: ifeq +13 -> 302
        //   292: aload 17
        //   294: aload 21
        //   296: invokevirtual 86	android/view/DisplayCutout$ParcelableWrapper:readFromParcel	(Landroid/os/Parcel;)V
        //   299: goto +3 -> 302
        //   302: aload 21
        //   304: invokevirtual 68	android/os/Parcel:readInt	()I
        //   307: ifeq +13 -> 320
        //   310: aload 18
        //   312: aload 21
        //   314: invokevirtual 142	android/util/MergedConfiguration:readFromParcel	(Landroid/os/Parcel;)V
        //   317: goto +3 -> 320
        //   320: aload 21
        //   322: invokevirtual 68	android/os/Parcel:readInt	()I
        //   325: istore 4
        //   327: iload 4
        //   329: ifeq +17 -> 346
        //   332: aload 19
        //   334: aload 21
        //   336: invokevirtual 145	android/view/Surface:readFromParcel	(Landroid/os/Parcel;)V
        //   339: goto +7 -> 346
        //   342: astore_1
        //   343: goto +44 -> 387
        //   346: aload 21
        //   348: invokevirtual 80	android/os/Parcel:recycle	()V
        //   351: aload 20
        //   353: invokevirtual 80	android/os/Parcel:recycle	()V
        //   356: iload_2
        //   357: ireturn
        //   358: astore_1
        //   359: goto +28 -> 387
        //   362: astore_1
        //   363: goto +24 -> 387
        //   366: astore_1
        //   367: goto +20 -> 387
        //   370: astore_1
        //   371: goto +16 -> 387
        //   374: astore_1
        //   375: goto +12 -> 387
        //   378: astore_1
        //   379: goto +8 -> 387
        //   382: astore_1
        //   383: goto +4 -> 387
        //   386: astore_1
        //   387: aload 21
        //   389: invokevirtual 80	android/os/Parcel:recycle	()V
        //   392: aload 20
        //   394: invokevirtual 80	android/os/Parcel:recycle	()V
        //   397: aload_1
        //   398: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	399	0	this	Proxy
        //   0	399	1	paramIWindow	IWindow
        //   0	399	2	paramInt1	int
        //   0	399	3	paramLayoutParams	WindowManager.LayoutParams
        //   0	399	4	paramInt2	int
        //   0	399	5	paramInt3	int
        //   0	399	6	paramInt4	int
        //   0	399	7	paramInt5	int
        //   0	399	8	paramLong	long
        //   0	399	10	paramRect1	Rect
        //   0	399	11	paramRect2	Rect
        //   0	399	12	paramRect3	Rect
        //   0	399	13	paramRect4	Rect
        //   0	399	14	paramRect5	Rect
        //   0	399	15	paramRect6	Rect
        //   0	399	16	paramRect7	Rect
        //   0	399	17	paramParcelableWrapper	DisplayCutout.ParcelableWrapper
        //   0	399	18	paramMergedConfiguration	MergedConfiguration
        //   0	399	19	paramSurface	Surface
        //   3	390	20	localParcel1	Parcel
        //   8	380	21	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   146	153	156	finally
        //   160	167	156	finally
        //   172	179	182	finally
        //   186	193	182	finally
        //   198	205	208	finally
        //   212	227	208	finally
        //   230	245	208	finally
        //   248	263	208	finally
        //   266	281	208	finally
        //   284	299	208	finally
        //   302	317	208	finally
        //   320	327	208	finally
        //   332	339	342	finally
        //   106	141	358	finally
        //   99	106	362	finally
        //   92	99	366	finally
        //   85	92	370	finally
        //   78	85	374	finally
        //   71	78	378	finally
        //   39	45	382	finally
        //   49	62	382	finally
        //   65	71	382	finally
        //   10	17	386	finally
        //   21	28	386	finally
        //   33	39	386	finally
      }
      
      public void remove(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportDropResult(IWindow paramIWindow, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramBoolean);
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
      
      public Bundle sendWallpaperCommand(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInTouchMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setInsets(IWindow paramIWindow, int paramInt, Rect paramRect1, Rect paramRect2, Region paramRegion)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt);
          if (paramRect1 != null)
          {
            localParcel1.writeInt(1);
            paramRect1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect2 != null)
          {
            localParcel1.writeInt(1);
            paramRect2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRegion != null)
          {
            localParcel1.writeInt(1);
            paramRegion.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTransparentRegion(IWindow paramIWindow, Region paramRegion)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          if (paramRegion != null)
          {
            localParcel1.writeInt(1);
            paramRegion.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setWallpaperDisplayOffset(IBinder paramIBinder, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void setWallpaperPosition(IBinder paramIBinder, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeFloat(paramFloat1);
          localParcel1.writeFloat(paramFloat2);
          localParcel1.writeFloat(paramFloat3);
          localParcel1.writeFloat(paramFloat4);
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
      
      public boolean startMovingTask(IWindow paramIWindow, float paramFloat1, float paramFloat2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeFloat(paramFloat1);
          localParcel1.writeFloat(paramFloat2);
          paramIWindow = mRemote;
          boolean bool = false;
          paramIWindow.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
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
      
      public void updatePointerIcon(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
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
      
      public void updateTapExcludeRegion(IWindow paramIWindow, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
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
      
      public void wallpaperCommandComplete(IBinder paramIBinder, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void wallpaperOffsetsComplete(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowSession");
          localParcel1.writeStrongBinder(paramIBinder);
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
    }
  }
}
