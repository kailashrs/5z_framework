package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IGeocodeProvider
  extends IInterface
{
  public abstract String getFromLocation(double paramDouble1, double paramDouble2, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
    throws RemoteException;
  
  public abstract String getFromLocationName(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGeocodeProvider
  {
    private static final String DESCRIPTOR = "android.location.IGeocodeProvider";
    static final int TRANSACTION_getFromLocation = 1;
    static final int TRANSACTION_getFromLocationName = 2;
    
    public Stub()
    {
      attachInterface(this, "android.location.IGeocodeProvider");
    }
    
    public static IGeocodeProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IGeocodeProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IGeocodeProvider))) {
        return (IGeocodeProvider)localIInterface;
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
        String str = null;
        ArrayList localArrayList = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.location.IGeocodeProvider");
          str = paramParcel1.readString();
          double d1 = paramParcel1.readDouble();
          d2 = paramParcel1.readDouble();
          double d3 = paramParcel1.readDouble();
          d4 = paramParcel1.readDouble();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (GeocoderParams)GeocoderParams.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localArrayList) {
            break;
          }
          localArrayList = new ArrayList();
          paramParcel1 = getFromLocationName(str, d1, d2, d3, d4, paramInt1, paramParcel1, localArrayList);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          paramParcel2.writeTypedList(localArrayList);
          return true;
        }
        paramParcel1.enforceInterface("android.location.IGeocodeProvider");
        double d2 = paramParcel1.readDouble();
        double d4 = paramParcel1.readDouble();
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel1 = (GeocoderParams)GeocoderParams.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = str) {
          break;
        }
        localArrayList = new ArrayList();
        paramParcel1 = getFromLocation(d2, d4, paramInt1, paramParcel1, localArrayList);
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        paramParcel2.writeTypedList(localArrayList);
        return true;
      }
      paramParcel2.writeString("android.location.IGeocodeProvider");
      return true;
    }
    
    private static class Proxy
      implements IGeocodeProvider
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
      
      public String getFromLocation(double paramDouble1, double paramDouble2, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IGeocodeProvider");
          localParcel1.writeDouble(paramDouble1);
          localParcel1.writeDouble(paramDouble2);
          localParcel1.writeInt(paramInt);
          if (paramGeocoderParams != null)
          {
            localParcel1.writeInt(1);
            paramGeocoderParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramGeocoderParams = localParcel2.readString();
          localParcel2.readTypedList(paramList, Address.CREATOR);
          return paramGeocoderParams;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public String getFromLocationName(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 13
        //   5: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 14
        //   10: aload 13
        //   12: ldc 34
        //   14: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 13
        //   19: aload_1
        //   20: invokevirtual 86	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   23: aload 13
        //   25: dload_2
        //   26: invokevirtual 42	android/os/Parcel:writeDouble	(D)V
        //   29: aload 13
        //   31: dload 4
        //   33: invokevirtual 42	android/os/Parcel:writeDouble	(D)V
        //   36: aload 13
        //   38: dload 6
        //   40: invokevirtual 42	android/os/Parcel:writeDouble	(D)V
        //   43: aload 13
        //   45: dload 8
        //   47: invokevirtual 42	android/os/Parcel:writeDouble	(D)V
        //   50: aload 13
        //   52: iload 10
        //   54: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   57: aload 11
        //   59: ifnull +20 -> 79
        //   62: aload 13
        //   64: iconst_1
        //   65: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   68: aload 11
        //   70: aload 13
        //   72: iconst_0
        //   73: invokevirtual 52	android/location/GeocoderParams:writeToParcel	(Landroid/os/Parcel;I)V
        //   76: goto +9 -> 85
        //   79: aload 13
        //   81: iconst_0
        //   82: invokevirtual 46	android/os/Parcel:writeInt	(I)V
        //   85: aload_0
        //   86: getfield 19	android/location/IGeocodeProvider$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   89: iconst_2
        //   90: aload 13
        //   92: aload 14
        //   94: iconst_0
        //   95: invokeinterface 58 5 0
        //   100: pop
        //   101: aload 14
        //   103: invokevirtual 61	android/os/Parcel:readException	()V
        //   106: aload 14
        //   108: invokevirtual 65	android/os/Parcel:readString	()Ljava/lang/String;
        //   111: astore_1
        //   112: getstatic 71	android/location/Address:CREATOR	Landroid/os/Parcelable$Creator;
        //   115: astore 11
        //   117: aload 14
        //   119: aload 12
        //   121: aload 11
        //   123: invokevirtual 75	android/os/Parcel:readTypedList	(Ljava/util/List;Landroid/os/Parcelable$Creator;)V
        //   126: aload 14
        //   128: invokevirtual 78	android/os/Parcel:recycle	()V
        //   131: aload 13
        //   133: invokevirtual 78	android/os/Parcel:recycle	()V
        //   136: aload_1
        //   137: areturn
        //   138: astore_1
        //   139: goto +32 -> 171
        //   142: astore_1
        //   143: goto +28 -> 171
        //   146: astore_1
        //   147: goto +24 -> 171
        //   150: astore_1
        //   151: goto +20 -> 171
        //   154: astore_1
        //   155: goto +16 -> 171
        //   158: astore_1
        //   159: goto +12 -> 171
        //   162: astore_1
        //   163: goto +8 -> 171
        //   166: astore_1
        //   167: goto +4 -> 171
        //   170: astore_1
        //   171: aload 14
        //   173: invokevirtual 78	android/os/Parcel:recycle	()V
        //   176: aload 13
        //   178: invokevirtual 78	android/os/Parcel:recycle	()V
        //   181: aload_1
        //   182: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	183	0	this	Proxy
        //   0	183	1	paramString	String
        //   0	183	2	paramDouble1	double
        //   0	183	4	paramDouble2	double
        //   0	183	6	paramDouble3	double
        //   0	183	8	paramDouble4	double
        //   0	183	10	paramInt	int
        //   0	183	11	paramGeocoderParams	GeocoderParams
        //   0	183	12	paramList	List<Address>
        //   3	174	13	localParcel1	Parcel
        //   8	164	14	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   117	126	138	finally
        //   85	117	142	finally
        //   50	57	146	finally
        //   62	76	146	finally
        //   79	85	146	finally
        //   43	50	150	finally
        //   36	43	154	finally
        //   29	36	158	finally
        //   23	29	162	finally
        //   17	23	166	finally
        //   10	17	170	finally
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.location.IGeocodeProvider";
      }
    }
  }
}
