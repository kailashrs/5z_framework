package android.view;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.IResultReceiver.Stub;

public abstract interface IWindow
  extends IInterface
{
  public abstract void closeSystemDialogs(String paramString)
    throws RemoteException;
  
  public abstract void dispatchAppVisibility(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void dispatchDragEvent(DragEvent paramDragEvent)
    throws RemoteException;
  
  public abstract void dispatchGetNewSurface()
    throws RemoteException;
  
  public abstract void dispatchPointerCaptureChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void dispatchSystemUiVisibilityChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void dispatchWallpaperOffsets(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void dispatchWindowShown()
    throws RemoteException;
  
  public abstract void executeCommand(String paramString1, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract void moved(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void requestAppKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
    throws RemoteException;
  
  public abstract void resized(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, boolean paramBoolean1, MergedConfiguration paramMergedConfiguration, Rect paramRect7, boolean paramBoolean2, boolean paramBoolean3, int paramInt, DisplayCutout.ParcelableWrapper paramParcelableWrapper)
    throws RemoteException;
  
  public abstract void updatePointerIcon(float paramFloat1, float paramFloat2)
    throws RemoteException;
  
  public abstract void windowFocusChanged(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWindow
  {
    private static final String DESCRIPTOR = "android.view.IWindow";
    static final int TRANSACTION_closeSystemDialogs = 7;
    static final int TRANSACTION_dispatchAppVisibility = 4;
    static final int TRANSACTION_dispatchDragEvent = 10;
    static final int TRANSACTION_dispatchGetNewSurface = 5;
    static final int TRANSACTION_dispatchPointerCaptureChanged = 15;
    static final int TRANSACTION_dispatchSystemUiVisibilityChanged = 12;
    static final int TRANSACTION_dispatchWallpaperCommand = 9;
    static final int TRANSACTION_dispatchWallpaperOffsets = 8;
    static final int TRANSACTION_dispatchWindowShown = 13;
    static final int TRANSACTION_executeCommand = 1;
    static final int TRANSACTION_moved = 3;
    static final int TRANSACTION_requestAppKeyboardShortcuts = 14;
    static final int TRANSACTION_resized = 2;
    static final int TRANSACTION_updatePointerIcon = 11;
    static final int TRANSACTION_windowFocusChanged = 6;
    
    public Stub()
    {
      attachInterface(this, "android.view.IWindow");
    }
    
    public static IWindow asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IWindow");
      if ((localIInterface != null) && ((localIInterface instanceof IWindow))) {
        return (IWindow)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 15: 
          paramParcel1.enforceInterface("android.view.IWindow");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          dispatchPointerCaptureChanged(bool3);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.view.IWindow");
          requestAppKeyboardShortcuts(IResultReceiver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.view.IWindow");
          dispatchWindowShown();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.view.IWindow");
          dispatchSystemUiVisibilityChanged(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.view.IWindow");
          updatePointerIcon(paramParcel1.readFloat(), paramParcel1.readFloat());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.view.IWindow");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DragEvent)DragEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          dispatchDragEvent(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.view.IWindow");
          localObject1 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          int i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          dispatchWallpaperCommand((String)localObject1, paramInt2, i, paramInt1, paramParcel2, bool3);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.view.IWindow");
          float f1 = paramParcel1.readFloat();
          float f2 = paramParcel1.readFloat();
          float f3 = paramParcel1.readFloat();
          float f4 = paramParcel1.readFloat();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          dispatchWallpaperOffsets(f1, f2, f3, f4, bool3);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.view.IWindow");
          closeSystemDialogs(paramParcel1.readString());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.view.IWindow");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          windowFocusChanged(bool3, bool1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.IWindow");
          dispatchGetNewSurface();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.IWindow");
          bool3 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          dispatchAppVisibility(bool3);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.IWindow");
          moved(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IWindow");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          Rect localRect1;
          if (paramParcel1.readInt() != 0) {
            localRect1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect1 = null;
          }
          Rect localRect2;
          if (paramParcel1.readInt() != 0) {
            localRect2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect2 = null;
          }
          Rect localRect3;
          if (paramParcel1.readInt() != 0) {
            localRect3 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          MergedConfiguration localMergedConfiguration;
          if (paramParcel1.readInt() != 0) {
            localMergedConfiguration = (MergedConfiguration)MergedConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localMergedConfiguration = null;
          }
          Rect localRect4;
          if (paramParcel1.readInt() != 0) {
            localRect4 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DisplayCutout.ParcelableWrapper)DisplayCutout.ParcelableWrapper.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          resized(paramParcel2, (Rect)localObject1, (Rect)localObject2, localRect1, localRect2, localRect3, bool3, localMergedConfiguration, localRect4, bool1, bool2, paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.view.IWindow");
        paramParcel2 = paramParcel1.readString();
        localObject2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = (Parcel)localObject1;
        }
        executeCommand(paramParcel2, (String)localObject2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.view.IWindow");
      return true;
    }
    
    private static class Proxy
      implements IWindow
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void closeSystemDialogs(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeString(paramString);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchAppVisibility(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchDragEvent(DragEvent paramDragEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          if (paramDragEvent != null)
          {
            localParcel.writeInt(1);
            paramDragEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchGetNewSurface()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchPointerCaptureChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchSystemUiVisibilityChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchWallpaperOffsets(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeFloat(paramFloat1);
          localParcel.writeFloat(paramFloat2);
          localParcel.writeFloat(paramFloat3);
          localParcel.writeFloat(paramFloat4);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchWindowShown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void executeCommand(String paramString1, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.IWindow";
      }
      
      public void moved(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestAppKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          if (paramIResultReceiver != null) {
            paramIResultReceiver = paramIResultReceiver.asBinder();
          } else {
            paramIResultReceiver = null;
          }
          localParcel.writeStrongBinder(paramIResultReceiver);
          localParcel.writeInt(paramInt);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void resized(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, boolean paramBoolean1, MergedConfiguration paramMergedConfiguration, Rect paramRect7, boolean paramBoolean2, boolean paramBoolean3, int paramInt, DisplayCutout.ParcelableWrapper paramParcelableWrapper)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 14
        //   5: aload 14
        //   7: ldc 34
        //   9: invokevirtual 37	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload_1
        //   13: ifnull +19 -> 32
        //   16: aload 14
        //   18: iconst_1
        //   19: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   22: aload_1
        //   23: aload 14
        //   25: iconst_0
        //   26: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   29: goto +9 -> 38
        //   32: aload 14
        //   34: iconst_0
        //   35: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   38: aload_2
        //   39: ifnull +19 -> 58
        //   42: aload 14
        //   44: iconst_1
        //   45: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   48: aload_2
        //   49: aload 14
        //   51: iconst_0
        //   52: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   55: goto +9 -> 64
        //   58: aload 14
        //   60: iconst_0
        //   61: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   64: aload_3
        //   65: ifnull +19 -> 84
        //   68: aload 14
        //   70: iconst_1
        //   71: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   74: aload_3
        //   75: aload 14
        //   77: iconst_0
        //   78: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   81: goto +9 -> 90
        //   84: aload 14
        //   86: iconst_0
        //   87: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   90: aload 4
        //   92: ifnull +20 -> 112
        //   95: aload 14
        //   97: iconst_1
        //   98: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   101: aload 4
        //   103: aload 14
        //   105: iconst_0
        //   106: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   109: goto +9 -> 118
        //   112: aload 14
        //   114: iconst_0
        //   115: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   118: aload 5
        //   120: ifnull +20 -> 140
        //   123: aload 14
        //   125: iconst_1
        //   126: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   129: aload 5
        //   131: aload 14
        //   133: iconst_0
        //   134: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   137: goto +9 -> 146
        //   140: aload 14
        //   142: iconst_0
        //   143: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   146: aload 6
        //   148: ifnull +20 -> 168
        //   151: aload 14
        //   153: iconst_1
        //   154: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   157: aload 6
        //   159: aload 14
        //   161: iconst_0
        //   162: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   165: goto +9 -> 174
        //   168: aload 14
        //   170: iconst_0
        //   171: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   174: aload 14
        //   176: iload 7
        //   178: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   181: aload 8
        //   183: ifnull +20 -> 203
        //   186: aload 14
        //   188: iconst_1
        //   189: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   192: aload 8
        //   194: aload 14
        //   196: iconst_0
        //   197: invokevirtual 106	android/util/MergedConfiguration:writeToParcel	(Landroid/os/Parcel;I)V
        //   200: goto +9 -> 209
        //   203: aload 14
        //   205: iconst_0
        //   206: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   209: aload 9
        //   211: ifnull +20 -> 231
        //   214: aload 14
        //   216: iconst_1
        //   217: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   220: aload 9
        //   222: aload 14
        //   224: iconst_0
        //   225: invokevirtual 103	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   228: goto +9 -> 237
        //   231: aload 14
        //   233: iconst_0
        //   234: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   237: aload 14
        //   239: iload 10
        //   241: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   244: aload 14
        //   246: iload 11
        //   248: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   251: aload 14
        //   253: iload 12
        //   255: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   258: aload 13
        //   260: ifnull +20 -> 280
        //   263: aload 14
        //   265: iconst_1
        //   266: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   269: aload 13
        //   271: aload 14
        //   273: iconst_0
        //   274: invokevirtual 109	android/view/DisplayCutout$ParcelableWrapper:writeToParcel	(Landroid/os/Parcel;I)V
        //   277: goto +9 -> 286
        //   280: aload 14
        //   282: iconst_0
        //   283: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   286: aload_0
        //   287: getfield 19	android/view/IWindow$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   290: iconst_2
        //   291: aload 14
        //   293: aconst_null
        //   294: iconst_1
        //   295: invokeinterface 46 5 0
        //   300: pop
        //   301: aload 14
        //   303: invokevirtual 49	android/os/Parcel:recycle	()V
        //   306: return
        //   307: astore_1
        //   308: goto +20 -> 328
        //   311: astore_1
        //   312: goto +16 -> 328
        //   315: astore_1
        //   316: goto +12 -> 328
        //   319: astore_1
        //   320: goto +8 -> 328
        //   323: astore_1
        //   324: goto +4 -> 328
        //   327: astore_1
        //   328: aload 14
        //   330: invokevirtual 49	android/os/Parcel:recycle	()V
        //   333: aload_1
        //   334: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	335	0	this	Proxy
        //   0	335	1	paramRect1	Rect
        //   0	335	2	paramRect2	Rect
        //   0	335	3	paramRect3	Rect
        //   0	335	4	paramRect4	Rect
        //   0	335	5	paramRect5	Rect
        //   0	335	6	paramRect6	Rect
        //   0	335	7	paramBoolean1	boolean
        //   0	335	8	paramMergedConfiguration	MergedConfiguration
        //   0	335	9	paramRect7	Rect
        //   0	335	10	paramBoolean2	boolean
        //   0	335	11	paramBoolean3	boolean
        //   0	335	12	paramInt	int
        //   0	335	13	paramParcelableWrapper	DisplayCutout.ParcelableWrapper
        //   3	326	14	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   286	301	307	finally
        //   251	258	311	finally
        //   263	277	311	finally
        //   280	286	311	finally
        //   244	251	315	finally
        //   237	244	319	finally
        //   174	181	323	finally
        //   186	200	323	finally
        //   203	209	323	finally
        //   214	228	323	finally
        //   231	237	323	finally
        //   5	12	327	finally
        //   16	29	327	finally
        //   32	38	327	finally
        //   42	55	327	finally
        //   58	64	327	finally
        //   68	81	327	finally
        //   84	90	327	finally
        //   95	109	327	finally
        //   112	118	327	finally
        //   123	137	327	finally
        //   140	146	327	finally
        //   151	165	327	finally
        //   168	174	327	finally
      }
      
      public void updatePointerIcon(float paramFloat1, float paramFloat2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeFloat(paramFloat1);
          localParcel.writeFloat(paramFloat2);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void windowFocusChanged(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindow");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
