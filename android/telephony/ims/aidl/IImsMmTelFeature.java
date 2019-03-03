package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.feature.CapabilityChangeRequest;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSession.Stub;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsEcbm.Stub;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsMultiEndpoint.Stub;
import com.android.ims.internal.IImsUt;
import com.android.ims.internal.IImsUt.Stub;

public abstract interface IImsMmTelFeature
  extends IInterface
{
  public abstract void acknowledgeSms(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void acknowledgeSmsReport(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void addCapabilityCallback(IImsCapabilityCallback paramIImsCapabilityCallback)
    throws RemoteException;
  
  public abstract void changeCapabilitiesConfiguration(CapabilityChangeRequest paramCapabilityChangeRequest, IImsCapabilityCallback paramIImsCapabilityCallback)
    throws RemoteException;
  
  public abstract ImsCallProfile createCallProfile(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract IImsCallSession createCallSession(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract IImsEcbm getEcbmInterface()
    throws RemoteException;
  
  public abstract int getFeatureState()
    throws RemoteException;
  
  public abstract IImsMultiEndpoint getMultiEndpointInterface()
    throws RemoteException;
  
  public abstract String getSmsFormat()
    throws RemoteException;
  
  public abstract IImsUt getUtInterface()
    throws RemoteException;
  
  public abstract void onSmsReady()
    throws RemoteException;
  
  public abstract void queryCapabilityConfiguration(int paramInt1, int paramInt2, IImsCapabilityCallback paramIImsCapabilityCallback)
    throws RemoteException;
  
  public abstract int queryCapabilityStatus()
    throws RemoteException;
  
  public abstract void removeCapabilityCallback(IImsCapabilityCallback paramIImsCapabilityCallback)
    throws RemoteException;
  
  public abstract void sendSms(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setListener(IImsMmTelListener paramIImsMmTelListener)
    throws RemoteException;
  
  public abstract void setSmsListener(IImsSmsListener paramIImsSmsListener)
    throws RemoteException;
  
  public abstract void setUiTtyMode(int paramInt, Message paramMessage)
    throws RemoteException;
  
  public abstract int shouldProcessCall(String[] paramArrayOfString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsMmTelFeature
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsMmTelFeature";
    static final int TRANSACTION_acknowledgeSms = 17;
    static final int TRANSACTION_acknowledgeSmsReport = 18;
    static final int TRANSACTION_addCapabilityCallback = 11;
    static final int TRANSACTION_changeCapabilitiesConfiguration = 13;
    static final int TRANSACTION_createCallProfile = 3;
    static final int TRANSACTION_createCallSession = 4;
    static final int TRANSACTION_getEcbmInterface = 7;
    static final int TRANSACTION_getFeatureState = 2;
    static final int TRANSACTION_getMultiEndpointInterface = 9;
    static final int TRANSACTION_getSmsFormat = 19;
    static final int TRANSACTION_getUtInterface = 6;
    static final int TRANSACTION_onSmsReady = 20;
    static final int TRANSACTION_queryCapabilityConfiguration = 14;
    static final int TRANSACTION_queryCapabilityStatus = 10;
    static final int TRANSACTION_removeCapabilityCallback = 12;
    static final int TRANSACTION_sendSms = 16;
    static final int TRANSACTION_setListener = 1;
    static final int TRANSACTION_setSmsListener = 15;
    static final int TRANSACTION_setUiTtyMode = 8;
    static final int TRANSACTION_shouldProcessCall = 5;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsMmTelFeature");
    }
    
    public static IImsMmTelFeature asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsMmTelFeature");
      if ((localIInterface != null) && ((localIInterface instanceof IImsMmTelFeature))) {
        return (IImsMmTelFeature)localIInterface;
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
        IImsCallSession localIImsCallSession = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 20: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          onSmsReady();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramParcel1 = getSmsFormat();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          acknowledgeSmsReport(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          acknowledgeSms(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject4 = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          sendSms(paramInt1, paramInt2, (String)localObject4, paramParcel2, bool, paramParcel1.createByteArray());
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          setSmsListener(IImsSmsListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          queryCapabilityConfiguration(paramParcel1.readInt(), paramParcel1.readInt(), IImsCapabilityCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CapabilityChangeRequest)CapabilityChangeRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject5;
          }
          changeCapabilitiesConfiguration(paramParcel2, IImsCapabilityCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          removeCapabilityCallback(IImsCapabilityCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          addCapabilityCallback(IImsCapabilityCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramInt1 = queryCapabilityStatus();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          localObject4 = getMultiEndpointInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localIImsCallSession;
          if (localObject4 != null) {
            paramParcel1 = ((IImsMultiEndpoint)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Message)Message.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          setUiTtyMode(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          localObject4 = getEcbmInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject2;
          if (localObject4 != null) {
            paramParcel1 = ((IImsEcbm)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          localObject4 = getUtInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject3;
          if (localObject4 != null) {
            paramParcel1 = ((IImsUt)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramInt1 = shouldProcessCall(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localIImsCallSession = createCallSession(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject4;
          if (localIImsCallSession != null) {
            paramParcel1 = localIImsCallSession.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramParcel1 = createCallProfile(paramParcel1.readInt(), paramParcel1.readInt());
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
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
          paramInt1 = getFeatureState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelFeature");
        setListener(IImsMmTelListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsMmTelFeature");
      return true;
    }
    
    private static class Proxy
      implements IImsMmTelFeature
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acknowledgeSms(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void acknowledgeSmsReport(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void addCapabilityCallback(IImsCapabilityCallback paramIImsCapabilityCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramIImsCapabilityCallback != null) {
            paramIImsCapabilityCallback = paramIImsCapabilityCallback.asBinder();
          } else {
            paramIImsCapabilityCallback = null;
          }
          localParcel.writeStrongBinder(paramIImsCapabilityCallback);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void changeCapabilitiesConfiguration(CapabilityChangeRequest paramCapabilityChangeRequest, IImsCapabilityCallback paramIImsCapabilityCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramCapabilityChangeRequest != null)
          {
            localParcel.writeInt(1);
            paramCapabilityChangeRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIImsCapabilityCallback != null) {
            paramCapabilityChangeRequest = paramIImsCapabilityCallback.asBinder();
          } else {
            paramCapabilityChangeRequest = null;
          }
          localParcel.writeStrongBinder(paramCapabilityChangeRequest);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public ImsCallProfile createCallProfile(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ImsCallProfile localImsCallProfile;
          if (localParcel2.readInt() != 0) {
            localImsCallProfile = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(localParcel2);
          } else {
            localImsCallProfile = null;
          }
          return localImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsCallSession createCallSession(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramImsCallProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramImsCallProfile = IImsCallSession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsEcbm getEcbmInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsEcbm localIImsEcbm = IImsEcbm.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsEcbm;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getFeatureState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.ims.aidl.IImsMmTelFeature";
      }
      
      public IImsMultiEndpoint getMultiEndpointInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsMultiEndpoint localIImsMultiEndpoint = IImsMultiEndpoint.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsMultiEndpoint;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSmsFormat()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsUt getUtInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsUt localIImsUt = IImsUt.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsUt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onSmsReady()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void queryCapabilityConfiguration(int paramInt1, int paramInt2, IImsCapabilityCallback paramIImsCapabilityCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIImsCapabilityCallback != null) {
            paramIImsCapabilityCallback = paramIImsCapabilityCallback.asBinder();
          } else {
            paramIImsCapabilityCallback = null;
          }
          localParcel.writeStrongBinder(paramIImsCapabilityCallback);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int queryCapabilityStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeCapabilityCallback(IImsCapabilityCallback paramIImsCapabilityCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramIImsCapabilityCallback != null) {
            paramIImsCapabilityCallback = paramIImsCapabilityCallback.asBinder();
          } else {
            paramIImsCapabilityCallback = null;
          }
          localParcel.writeStrongBinder(paramIImsCapabilityCallback);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendSms(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramBoolean);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setListener(IImsMmTelListener paramIImsMmTelListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramIImsMmTelListener != null) {
            paramIImsMmTelListener = paramIImsMmTelListener.asBinder();
          } else {
            paramIImsMmTelListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsMmTelListener);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSmsListener(IImsSmsListener paramIImsSmsListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          if (paramIImsSmsListener != null) {
            paramIImsSmsListener = paramIImsSmsListener.asBinder();
          } else {
            paramIImsSmsListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsSmsListener);
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
      
      public void setUiTtyMode(int paramInt, Message paramMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel1.writeInt(paramInt);
          if (paramMessage != null)
          {
            localParcel1.writeInt(1);
            paramMessage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int shouldProcessCall(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelFeature");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
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
