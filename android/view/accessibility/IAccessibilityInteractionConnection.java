package android.view.accessibility;

import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.MagnificationSpec;

public abstract interface IAccessibilityInteractionConnection
  extends IInterface
{
  public abstract void findAccessibilityNodeInfoByAccessibilityId(long paramLong1, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void findAccessibilityNodeInfosByText(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
    throws RemoteException;
  
  public abstract void findAccessibilityNodeInfosByViewId(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
    throws RemoteException;
  
  public abstract void findFocus(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
    throws RemoteException;
  
  public abstract void focusSearch(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
    throws RemoteException;
  
  public abstract void performAccessibilityAction(long paramLong1, int paramInt1, Bundle paramBundle, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccessibilityInteractionConnection
  {
    private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityInteractionConnection";
    static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = 1;
    static final int TRANSACTION_findAccessibilityNodeInfosByText = 3;
    static final int TRANSACTION_findAccessibilityNodeInfosByViewId = 2;
    static final int TRANSACTION_findFocus = 4;
    static final int TRANSACTION_focusSearch = 5;
    static final int TRANSACTION_performAccessibilityAction = 6;
    
    public Stub()
    {
      attachInterface(this, "android.view.accessibility.IAccessibilityInteractionConnection");
    }
    
    public static IAccessibilityInteractionConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.accessibility.IAccessibilityInteractionConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IAccessibilityInteractionConnection))) {
        return (IAccessibilityInteractionConnection)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        IAccessibilityInteractionConnectionCallback localIAccessibilityInteractionConnectionCallback = null;
        Object localObject4 = null;
        Object localObject5 = null;
        int j;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnection");
          l1 = paramParcel1.readLong();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject5) {
            break;
          }
          performAccessibilityAction(l1, paramInt1, paramParcel2, paramParcel1.readInt(), IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnection");
          l2 = paramParcel1.readLong();
          i = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt2 = paramParcel1.readInt();
          localObject3 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          j = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          l1 = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (MagnificationSpec)MagnificationSpec.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject1) {
            break;
          }
          focusSearch(l2, i, paramParcel2, paramInt2, (IAccessibilityInteractionConnectionCallback)localObject3, j, paramInt1, l1, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnection");
          l1 = paramParcel1.readLong();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt2 = paramParcel1.readInt();
          localObject3 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          j = paramParcel1.readInt();
          i = paramParcel1.readInt();
          l2 = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (MagnificationSpec)MagnificationSpec.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject2) {
            break;
          }
          findFocus(l1, paramInt1, paramParcel2, paramInt2, (IAccessibilityInteractionConnectionCallback)localObject3, j, i, l2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnection");
          l1 = paramParcel1.readLong();
          localObject4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt2 = paramParcel1.readInt();
          localIAccessibilityInteractionConnectionCallback = IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          l2 = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (MagnificationSpec)MagnificationSpec.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject3) {
            break;
          }
          findAccessibilityNodeInfosByText(l1, (String)localObject4, paramParcel2, paramInt2, localIAccessibilityInteractionConnectionCallback, i, paramInt1, l2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnection");
          l2 = paramParcel1.readLong();
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          i = paramParcel1.readInt();
          localObject4 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          l1 = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (MagnificationSpec)MagnificationSpec.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localIAccessibilityInteractionConnectionCallback) {
            break;
          }
          findAccessibilityNodeInfosByViewId(l2, (String)localObject3, paramParcel2, i, (IAccessibilityInteractionConnectionCallback)localObject4, paramInt1, paramInt2, l1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnection");
        long l1 = paramParcel1.readLong();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        paramInt2 = paramParcel1.readInt();
        localIAccessibilityInteractionConnectionCallback = IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
        int i = paramParcel1.readInt();
        paramInt1 = paramParcel1.readInt();
        long l2 = paramParcel1.readLong();
        if (paramParcel1.readInt() != 0) {
          localObject3 = (MagnificationSpec)MagnificationSpec.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject3 = null;
        }
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject4) {
          break;
        }
        findAccessibilityNodeInfoByAccessibilityId(l1, paramParcel2, paramInt2, localIAccessibilityInteractionConnectionCallback, i, paramInt1, l2, (MagnificationSpec)localObject3, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.view.accessibility.IAccessibilityInteractionConnection");
      return true;
    }
    
    private static class Proxy
      implements IAccessibilityInteractionConnection
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
      
      /* Error */
      public void findAccessibilityNodeInfoByAccessibilityId(long paramLong1, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec, Bundle paramBundle)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: aload 12
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 12
        //   14: lload_1
        //   15: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   18: aload_3
        //   19: ifnull +19 -> 38
        //   22: aload 12
        //   24: iconst_1
        //   25: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   28: aload_3
        //   29: aload 12
        //   31: iconst_0
        //   32: invokevirtual 52	android/graphics/Region:writeToParcel	(Landroid/os/Parcel;I)V
        //   35: goto +9 -> 44
        //   38: aload 12
        //   40: iconst_0
        //   41: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   44: aload 12
        //   46: iload 4
        //   48: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   51: aload 5
        //   53: ifnull +14 -> 67
        //   56: aload 5
        //   58: invokeinterface 56 1 0
        //   63: astore_3
        //   64: goto +5 -> 69
        //   67: aconst_null
        //   68: astore_3
        //   69: aload 12
        //   71: aload_3
        //   72: invokevirtual 59	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   75: aload 12
        //   77: iload 6
        //   79: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   82: aload 12
        //   84: iload 7
        //   86: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   89: aload 12
        //   91: lload 8
        //   93: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   96: aload 10
        //   98: ifnull +20 -> 118
        //   101: aload 12
        //   103: iconst_1
        //   104: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   107: aload 10
        //   109: aload 12
        //   111: iconst_0
        //   112: invokevirtual 62	android/view/MagnificationSpec:writeToParcel	(Landroid/os/Parcel;I)V
        //   115: goto +9 -> 124
        //   118: aload 12
        //   120: iconst_0
        //   121: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   124: aload 11
        //   126: ifnull +20 -> 146
        //   129: aload 12
        //   131: iconst_1
        //   132: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   135: aload 11
        //   137: aload 12
        //   139: iconst_0
        //   140: invokevirtual 65	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   143: goto +9 -> 152
        //   146: aload 12
        //   148: iconst_0
        //   149: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   152: aload_0
        //   153: getfield 19	android/view/accessibility/IAccessibilityInteractionConnection$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   156: iconst_1
        //   157: aload 12
        //   159: aconst_null
        //   160: iconst_1
        //   161: invokeinterface 71 5 0
        //   166: pop
        //   167: aload 12
        //   169: invokevirtual 74	android/os/Parcel:recycle	()V
        //   172: return
        //   173: astore_3
        //   174: goto +24 -> 198
        //   177: astore_3
        //   178: goto +20 -> 198
        //   181: astore_3
        //   182: goto +16 -> 198
        //   185: astore_3
        //   186: goto +12 -> 198
        //   189: astore_3
        //   190: goto +8 -> 198
        //   193: astore_3
        //   194: goto +4 -> 198
        //   197: astore_3
        //   198: aload 12
        //   200: invokevirtual 74	android/os/Parcel:recycle	()V
        //   203: aload_3
        //   204: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	205	0	this	Proxy
        //   0	205	1	paramLong1	long
        //   0	205	3	paramRegion	Region
        //   0	205	4	paramInt1	int
        //   0	205	5	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
        //   0	205	6	paramInt2	int
        //   0	205	7	paramInt3	int
        //   0	205	8	paramLong2	long
        //   0	205	10	paramMagnificationSpec	MagnificationSpec
        //   0	205	11	paramBundle	Bundle
        //   3	196	12	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   152	167	173	finally
        //   89	96	177	finally
        //   101	115	177	finally
        //   118	124	177	finally
        //   129	143	177	finally
        //   146	152	177	finally
        //   82	89	181	finally
        //   75	82	185	finally
        //   44	51	189	finally
        //   56	64	189	finally
        //   69	75	189	finally
        //   12	18	193	finally
        //   22	35	193	finally
        //   38	44	193	finally
        //   5	12	197	finally
      }
      
      /* Error */
      public void findAccessibilityNodeInfosByText(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: aload 12
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 12
        //   14: lload_1
        //   15: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   18: aload 12
        //   20: aload_3
        //   21: invokevirtual 80	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   24: aload 4
        //   26: ifnull +20 -> 46
        //   29: aload 12
        //   31: iconst_1
        //   32: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   35: aload 4
        //   37: aload 12
        //   39: iconst_0
        //   40: invokevirtual 52	android/graphics/Region:writeToParcel	(Landroid/os/Parcel;I)V
        //   43: goto +9 -> 52
        //   46: aload 12
        //   48: iconst_0
        //   49: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   52: aload 12
        //   54: iload 5
        //   56: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   59: aload 6
        //   61: ifnull +14 -> 75
        //   64: aload 6
        //   66: invokeinterface 56 1 0
        //   71: astore_3
        //   72: goto +5 -> 77
        //   75: aconst_null
        //   76: astore_3
        //   77: aload 12
        //   79: aload_3
        //   80: invokevirtual 59	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   83: aload 12
        //   85: iload 7
        //   87: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   90: aload 12
        //   92: iload 8
        //   94: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   97: aload 12
        //   99: lload 9
        //   101: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   104: aload 11
        //   106: ifnull +20 -> 126
        //   109: aload 12
        //   111: iconst_1
        //   112: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   115: aload 11
        //   117: aload 12
        //   119: iconst_0
        //   120: invokevirtual 62	android/view/MagnificationSpec:writeToParcel	(Landroid/os/Parcel;I)V
        //   123: goto +9 -> 132
        //   126: aload 12
        //   128: iconst_0
        //   129: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   132: aload_0
        //   133: getfield 19	android/view/accessibility/IAccessibilityInteractionConnection$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   136: iconst_3
        //   137: aload 12
        //   139: aconst_null
        //   140: iconst_1
        //   141: invokeinterface 71 5 0
        //   146: pop
        //   147: aload 12
        //   149: invokevirtual 74	android/os/Parcel:recycle	()V
        //   152: return
        //   153: astore_3
        //   154: goto +28 -> 182
        //   157: astore_3
        //   158: goto +24 -> 182
        //   161: astore_3
        //   162: goto +20 -> 182
        //   165: astore_3
        //   166: goto +16 -> 182
        //   169: astore_3
        //   170: goto +12 -> 182
        //   173: astore_3
        //   174: goto +8 -> 182
        //   177: astore_3
        //   178: goto +4 -> 182
        //   181: astore_3
        //   182: aload 12
        //   184: invokevirtual 74	android/os/Parcel:recycle	()V
        //   187: aload_3
        //   188: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	189	0	this	Proxy
        //   0	189	1	paramLong1	long
        //   0	189	3	paramString	String
        //   0	189	4	paramRegion	Region
        //   0	189	5	paramInt1	int
        //   0	189	6	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
        //   0	189	7	paramInt2	int
        //   0	189	8	paramInt3	int
        //   0	189	9	paramLong2	long
        //   0	189	11	paramMagnificationSpec	MagnificationSpec
        //   3	180	12	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   132	147	153	finally
        //   97	104	157	finally
        //   109	123	157	finally
        //   126	132	157	finally
        //   90	97	161	finally
        //   83	90	165	finally
        //   52	59	169	finally
        //   64	72	169	finally
        //   77	83	169	finally
        //   18	24	173	finally
        //   29	43	173	finally
        //   46	52	173	finally
        //   12	18	177	finally
        //   5	12	181	finally
      }
      
      /* Error */
      public void findAccessibilityNodeInfosByViewId(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: aload 12
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 12
        //   14: lload_1
        //   15: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   18: aload 12
        //   20: aload_3
        //   21: invokevirtual 80	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   24: aload 4
        //   26: ifnull +20 -> 46
        //   29: aload 12
        //   31: iconst_1
        //   32: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   35: aload 4
        //   37: aload 12
        //   39: iconst_0
        //   40: invokevirtual 52	android/graphics/Region:writeToParcel	(Landroid/os/Parcel;I)V
        //   43: goto +9 -> 52
        //   46: aload 12
        //   48: iconst_0
        //   49: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   52: aload 12
        //   54: iload 5
        //   56: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   59: aload 6
        //   61: ifnull +14 -> 75
        //   64: aload 6
        //   66: invokeinterface 56 1 0
        //   71: astore_3
        //   72: goto +5 -> 77
        //   75: aconst_null
        //   76: astore_3
        //   77: aload 12
        //   79: aload_3
        //   80: invokevirtual 59	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   83: aload 12
        //   85: iload 7
        //   87: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   90: aload 12
        //   92: iload 8
        //   94: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   97: aload 12
        //   99: lload 9
        //   101: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   104: aload 11
        //   106: ifnull +20 -> 126
        //   109: aload 12
        //   111: iconst_1
        //   112: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   115: aload 11
        //   117: aload 12
        //   119: iconst_0
        //   120: invokevirtual 62	android/view/MagnificationSpec:writeToParcel	(Landroid/os/Parcel;I)V
        //   123: goto +9 -> 132
        //   126: aload 12
        //   128: iconst_0
        //   129: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   132: aload_0
        //   133: getfield 19	android/view/accessibility/IAccessibilityInteractionConnection$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   136: iconst_2
        //   137: aload 12
        //   139: aconst_null
        //   140: iconst_1
        //   141: invokeinterface 71 5 0
        //   146: pop
        //   147: aload 12
        //   149: invokevirtual 74	android/os/Parcel:recycle	()V
        //   152: return
        //   153: astore_3
        //   154: goto +28 -> 182
        //   157: astore_3
        //   158: goto +24 -> 182
        //   161: astore_3
        //   162: goto +20 -> 182
        //   165: astore_3
        //   166: goto +16 -> 182
        //   169: astore_3
        //   170: goto +12 -> 182
        //   173: astore_3
        //   174: goto +8 -> 182
        //   177: astore_3
        //   178: goto +4 -> 182
        //   181: astore_3
        //   182: aload 12
        //   184: invokevirtual 74	android/os/Parcel:recycle	()V
        //   187: aload_3
        //   188: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	189	0	this	Proxy
        //   0	189	1	paramLong1	long
        //   0	189	3	paramString	String
        //   0	189	4	paramRegion	Region
        //   0	189	5	paramInt1	int
        //   0	189	6	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
        //   0	189	7	paramInt2	int
        //   0	189	8	paramInt3	int
        //   0	189	9	paramLong2	long
        //   0	189	11	paramMagnificationSpec	MagnificationSpec
        //   3	180	12	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   132	147	153	finally
        //   97	104	157	finally
        //   109	123	157	finally
        //   126	132	157	finally
        //   90	97	161	finally
        //   83	90	165	finally
        //   52	59	169	finally
        //   64	72	169	finally
        //   77	83	169	finally
        //   18	24	173	finally
        //   29	43	173	finally
        //   46	52	173	finally
        //   12	18	177	finally
        //   5	12	181	finally
      }
      
      /* Error */
      public void findFocus(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: aload 12
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 12
        //   14: lload_1
        //   15: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   18: aload 12
        //   20: iload_3
        //   21: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   24: aload 4
        //   26: ifnull +20 -> 46
        //   29: aload 12
        //   31: iconst_1
        //   32: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   35: aload 4
        //   37: aload 12
        //   39: iconst_0
        //   40: invokevirtual 52	android/graphics/Region:writeToParcel	(Landroid/os/Parcel;I)V
        //   43: goto +9 -> 52
        //   46: aload 12
        //   48: iconst_0
        //   49: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   52: aload 12
        //   54: iload 5
        //   56: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   59: aload 6
        //   61: ifnull +15 -> 76
        //   64: aload 6
        //   66: invokeinterface 56 1 0
        //   71: astore 4
        //   73: goto +6 -> 79
        //   76: aconst_null
        //   77: astore 4
        //   79: aload 12
        //   81: aload 4
        //   83: invokevirtual 59	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   86: aload 12
        //   88: iload 7
        //   90: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   93: aload 12
        //   95: iload 8
        //   97: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   100: aload 12
        //   102: lload 9
        //   104: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   107: aload 11
        //   109: ifnull +20 -> 129
        //   112: aload 12
        //   114: iconst_1
        //   115: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   118: aload 11
        //   120: aload 12
        //   122: iconst_0
        //   123: invokevirtual 62	android/view/MagnificationSpec:writeToParcel	(Landroid/os/Parcel;I)V
        //   126: goto +9 -> 135
        //   129: aload 12
        //   131: iconst_0
        //   132: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   135: aload_0
        //   136: getfield 19	android/view/accessibility/IAccessibilityInteractionConnection$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   139: iconst_4
        //   140: aload 12
        //   142: aconst_null
        //   143: iconst_1
        //   144: invokeinterface 71 5 0
        //   149: pop
        //   150: aload 12
        //   152: invokevirtual 74	android/os/Parcel:recycle	()V
        //   155: return
        //   156: astore 4
        //   158: goto +35 -> 193
        //   161: astore 4
        //   163: goto +30 -> 193
        //   166: astore 4
        //   168: goto +25 -> 193
        //   171: astore 4
        //   173: goto +20 -> 193
        //   176: astore 4
        //   178: goto +15 -> 193
        //   181: astore 4
        //   183: goto +10 -> 193
        //   186: astore 4
        //   188: goto +5 -> 193
        //   191: astore 4
        //   193: aload 12
        //   195: invokevirtual 74	android/os/Parcel:recycle	()V
        //   198: aload 4
        //   200: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	201	0	this	Proxy
        //   0	201	1	paramLong1	long
        //   0	201	3	paramInt1	int
        //   0	201	4	paramRegion	Region
        //   0	201	5	paramInt2	int
        //   0	201	6	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
        //   0	201	7	paramInt3	int
        //   0	201	8	paramInt4	int
        //   0	201	9	paramLong2	long
        //   0	201	11	paramMagnificationSpec	MagnificationSpec
        //   3	191	12	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   135	150	156	finally
        //   100	107	161	finally
        //   112	126	161	finally
        //   129	135	161	finally
        //   93	100	166	finally
        //   86	93	171	finally
        //   52	59	176	finally
        //   64	73	176	finally
        //   79	86	176	finally
        //   18	24	181	finally
        //   29	43	181	finally
        //   46	52	181	finally
        //   12	18	186	finally
        //   5	12	191	finally
      }
      
      /* Error */
      public void focusSearch(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: aload 12
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 12
        //   14: lload_1
        //   15: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   18: aload 12
        //   20: iload_3
        //   21: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   24: aload 4
        //   26: ifnull +20 -> 46
        //   29: aload 12
        //   31: iconst_1
        //   32: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   35: aload 4
        //   37: aload 12
        //   39: iconst_0
        //   40: invokevirtual 52	android/graphics/Region:writeToParcel	(Landroid/os/Parcel;I)V
        //   43: goto +9 -> 52
        //   46: aload 12
        //   48: iconst_0
        //   49: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   52: aload 12
        //   54: iload 5
        //   56: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   59: aload 6
        //   61: ifnull +15 -> 76
        //   64: aload 6
        //   66: invokeinterface 56 1 0
        //   71: astore 4
        //   73: goto +6 -> 79
        //   76: aconst_null
        //   77: astore 4
        //   79: aload 12
        //   81: aload 4
        //   83: invokevirtual 59	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   86: aload 12
        //   88: iload 7
        //   90: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   93: aload 12
        //   95: iload 8
        //   97: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   100: aload 12
        //   102: lload 9
        //   104: invokevirtual 42	android/os/Parcel:writeLong	(J)V
        //   107: aload 11
        //   109: ifnull +20 -> 129
        //   112: aload 12
        //   114: iconst_1
        //   115: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   118: aload 11
        //   120: aload 12
        //   122: iconst_0
        //   123: invokevirtual 62	android/view/MagnificationSpec:writeToParcel	(Landroid/os/Parcel;I)V
        //   126: goto +9 -> 135
        //   129: aload 12
        //   131: iconst_0
        //   132: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   135: aload_0
        //   136: getfield 19	android/view/accessibility/IAccessibilityInteractionConnection$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   139: iconst_5
        //   140: aload 12
        //   142: aconst_null
        //   143: iconst_1
        //   144: invokeinterface 71 5 0
        //   149: pop
        //   150: aload 12
        //   152: invokevirtual 74	android/os/Parcel:recycle	()V
        //   155: return
        //   156: astore 4
        //   158: goto +35 -> 193
        //   161: astore 4
        //   163: goto +30 -> 193
        //   166: astore 4
        //   168: goto +25 -> 193
        //   171: astore 4
        //   173: goto +20 -> 193
        //   176: astore 4
        //   178: goto +15 -> 193
        //   181: astore 4
        //   183: goto +10 -> 193
        //   186: astore 4
        //   188: goto +5 -> 193
        //   191: astore 4
        //   193: aload 12
        //   195: invokevirtual 74	android/os/Parcel:recycle	()V
        //   198: aload 4
        //   200: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	201	0	this	Proxy
        //   0	201	1	paramLong1	long
        //   0	201	3	paramInt1	int
        //   0	201	4	paramRegion	Region
        //   0	201	5	paramInt2	int
        //   0	201	6	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
        //   0	201	7	paramInt3	int
        //   0	201	8	paramInt4	int
        //   0	201	9	paramLong2	long
        //   0	201	11	paramMagnificationSpec	MagnificationSpec
        //   3	191	12	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   135	150	156	finally
        //   100	107	161	finally
        //   112	126	161	finally
        //   129	135	161	finally
        //   93	100	166	finally
        //   86	93	171	finally
        //   52	59	176	finally
        //   64	73	176	finally
        //   79	86	176	finally
        //   18	24	181	finally
        //   29	43	181	finally
        //   46	52	181	finally
        //   12	18	186	finally
        //   5	12	191	finally
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.accessibility.IAccessibilityInteractionConnection";
      }
      
      public void performAccessibilityAction(long paramLong1, int paramInt1, Bundle paramBundle, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityInteractionConnection");
          localParcel.writeLong(paramLong1);
          localParcel.writeInt(paramInt1);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt2);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramBundle = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramBundle = null;
          }
          localParcel.writeStrongBinder(paramBundle);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          localParcel.writeLong(paramLong2);
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
