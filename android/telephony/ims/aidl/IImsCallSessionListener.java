package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSession.Stub;

public abstract interface IImsCallSessionListener
  extends IInterface
{
  public abstract void callSessionConferenceExtendFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionConferenceExtendReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionConferenceExtended(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionConferenceMaxnumUserCountUpdated(int paramInt)
    throws RemoteException;
  
  public abstract void callSessionConferenceStateUpdated(ImsConferenceState paramImsConferenceState)
    throws RemoteException;
  
  public abstract void callSessionHandover(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionHandoverFailed(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionHeld(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionHoldFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionHoldReceived(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionInitiated(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionInitiatedFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionInviteParticipantsRequestDelivered()
    throws RemoteException;
  
  public abstract void callSessionInviteParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionMayHandover(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void callSessionMergeComplete(IImsCallSession paramIImsCallSession)
    throws RemoteException;
  
  public abstract void callSessionMergeFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionMergeStarted(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionMultipartyStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void callSessionProgressing(ImsStreamMediaProfile paramImsStreamMediaProfile)
    throws RemoteException;
  
  public abstract void callSessionPropertyChanged(int paramInt)
    throws RemoteException;
  
  public abstract void callSessionRemoveParticipantsRequestDelivered()
    throws RemoteException;
  
  public abstract void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionResumeFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionResumeReceived(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionResumed(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionRttMessageReceived(String paramString)
    throws RemoteException;
  
  public abstract void callSessionRttModifyRequestReceived(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionRttModifyResponseReceived(int paramInt)
    throws RemoteException;
  
  public abstract void callSessionSuppServiceReceived(ImsSuppServiceNotification paramImsSuppServiceNotification)
    throws RemoteException;
  
  public abstract void callSessionTerminated(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionTtyModeReceived(int paramInt)
    throws RemoteException;
  
  public abstract void callSessionUpdateFailed(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void callSessionUpdateReceived(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionUpdated(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void callSessionUssdMessageReceived(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsCallSessionListener
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsCallSessionListener";
    static final int TRANSACTION_callSessionConferenceExtendFailed = 18;
    static final int TRANSACTION_callSessionConferenceExtendReceived = 19;
    static final int TRANSACTION_callSessionConferenceExtended = 17;
    static final int TRANSACTION_callSessionConferenceMaxnumUserCountUpdated = 25;
    static final int TRANSACTION_callSessionConferenceStateUpdated = 24;
    static final int TRANSACTION_callSessionHandover = 27;
    static final int TRANSACTION_callSessionHandoverFailed = 28;
    static final int TRANSACTION_callSessionHeld = 5;
    static final int TRANSACTION_callSessionHoldFailed = 6;
    static final int TRANSACTION_callSessionHoldReceived = 7;
    static final int TRANSACTION_callSessionInitiated = 2;
    static final int TRANSACTION_callSessionInitiatedFailed = 3;
    static final int TRANSACTION_callSessionInviteParticipantsRequestDelivered = 20;
    static final int TRANSACTION_callSessionInviteParticipantsRequestFailed = 21;
    static final int TRANSACTION_callSessionMayHandover = 29;
    static final int TRANSACTION_callSessionMergeComplete = 12;
    static final int TRANSACTION_callSessionMergeFailed = 13;
    static final int TRANSACTION_callSessionMergeStarted = 11;
    static final int TRANSACTION_callSessionMultipartyStateChanged = 31;
    static final int TRANSACTION_callSessionProgressing = 1;
    static final int TRANSACTION_callSessionPropertyChanged = 36;
    static final int TRANSACTION_callSessionRemoveParticipantsRequestDelivered = 22;
    static final int TRANSACTION_callSessionRemoveParticipantsRequestFailed = 23;
    static final int TRANSACTION_callSessionResumeFailed = 9;
    static final int TRANSACTION_callSessionResumeReceived = 10;
    static final int TRANSACTION_callSessionResumed = 8;
    static final int TRANSACTION_callSessionRttMessageReceived = 35;
    static final int TRANSACTION_callSessionRttModifyRequestReceived = 33;
    static final int TRANSACTION_callSessionRttModifyResponseReceived = 34;
    static final int TRANSACTION_callSessionSuppServiceReceived = 32;
    static final int TRANSACTION_callSessionTerminated = 4;
    static final int TRANSACTION_callSessionTtyModeReceived = 30;
    static final int TRANSACTION_callSessionUpdateFailed = 15;
    static final int TRANSACTION_callSessionUpdateReceived = 16;
    static final int TRANSACTION_callSessionUpdated = 14;
    static final int TRANSACTION_callSessionUssdMessageReceived = 26;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsCallSessionListener");
    }
    
    public static IImsCallSessionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsCallSessionListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsCallSessionListener))) {
        return (IImsCallSessionListener)localIInterface;
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
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        Object localObject24 = null;
        Object localObject25 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 36: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionPropertyChanged(paramParcel1.readInt());
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionRttMessageReceived(paramParcel1.readString());
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionRttModifyResponseReceived(paramParcel1.readInt());
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject25;
          }
          callSessionRttModifyRequestReceived(paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsSuppServiceNotification)ImsSuppServiceNotification.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          callSessionSuppServiceReceived(paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          callSessionMultipartyStateChanged(bool);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionTtyModeReceived(paramParcel1.readInt());
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionMayHandover(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          callSessionHandoverFailed(paramInt2, paramInt1, paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          callSessionHandover(paramInt2, paramInt1, paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionUssdMessageReceived(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionConferenceMaxnumUserCountUpdated(paramParcel1.readInt());
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsConferenceState)ImsConferenceState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          callSessionConferenceStateUpdated(paramParcel1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          callSessionRemoveParticipantsRequestFailed(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionRemoveParticipantsRequestDelivered();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          callSessionInviteParticipantsRequestFailed(paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionInviteParticipantsRequestDelivered();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          paramParcel2 = IImsCallSession.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          callSessionConferenceExtendReceived(paramParcel2, paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          callSessionConferenceExtendFailed(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          paramParcel2 = IImsCallSession.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          callSessionConferenceExtended(paramParcel2, paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          callSessionUpdateReceived(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          callSessionUpdateFailed(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          callSessionUpdated(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          callSessionMergeFailed(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          callSessionMergeComplete(IImsCallSession.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          paramParcel2 = IImsCallSession.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          callSessionMergeStarted(paramParcel2, paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          callSessionResumeReceived(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          callSessionResumeFailed(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject17;
          }
          callSessionResumed(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          callSessionHoldReceived(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          callSessionHoldFailed(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          callSessionHeld(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          callSessionTerminated(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject22;
          }
          callSessionInitiatedFailed(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          callSessionInitiated(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCallSessionListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ImsStreamMediaProfile)ImsStreamMediaProfile.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject24;
        }
        callSessionProgressing(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsCallSessionListener");
      return true;
    }
    
    private static class Proxy
      implements IImsCallSessionListener
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
      
      public void callSessionConferenceExtendFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionConferenceExtendReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramIImsCallSession != null) {
            paramIImsCallSession = paramIImsCallSession.asBinder();
          } else {
            paramIImsCallSession = null;
          }
          localParcel.writeStrongBinder(paramIImsCallSession);
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionConferenceExtended(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramIImsCallSession != null) {
            paramIImsCallSession = paramIImsCallSession.asBinder();
          } else {
            paramIImsCallSession = null;
          }
          localParcel.writeStrongBinder(paramIImsCallSession);
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionConferenceMaxnumUserCountUpdated(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(25, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionConferenceStateUpdated(ImsConferenceState paramImsConferenceState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsConferenceState != null)
          {
            localParcel.writeInt(1);
            paramImsConferenceState.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(24, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionHandover(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(27, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionHandoverFailed(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(28, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionHeld(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionHoldFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionHoldReceived(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionInitiated(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionInitiatedFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionInviteParticipantsRequestDelivered()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionInviteParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionMayHandover(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(29, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionMergeComplete(IImsCallSession paramIImsCallSession)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramIImsCallSession != null) {
            paramIImsCallSession = paramIImsCallSession.asBinder();
          } else {
            paramIImsCallSession = null;
          }
          localParcel.writeStrongBinder(paramIImsCallSession);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionMergeFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionMergeStarted(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramIImsCallSession != null) {
            paramIImsCallSession = paramIImsCallSession.asBinder();
          } else {
            paramIImsCallSession = null;
          }
          localParcel.writeStrongBinder(paramIImsCallSession);
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionMultipartyStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(31, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionProgressing(ImsStreamMediaProfile paramImsStreamMediaProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsStreamMediaProfile != null)
          {
            localParcel.writeInt(1);
            paramImsStreamMediaProfile.writeToParcel(localParcel, 0);
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
      
      public void callSessionPropertyChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(36, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionRemoveParticipantsRequestDelivered()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionResumeFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionResumeReceived(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
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
      
      public void callSessionResumed(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionRttMessageReceived(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeString(paramString);
          mRemote.transact(35, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionRttModifyRequestReceived(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(33, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionRttModifyResponseReceived(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionSuppServiceReceived(ImsSuppServiceNotification paramImsSuppServiceNotification)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsSuppServiceNotification != null)
          {
            localParcel.writeInt(1);
            paramImsSuppServiceNotification.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(32, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionTerminated(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionTtyModeReceived(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionUpdateFailed(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionUpdateReceived(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionUpdated(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void callSessionUssdMessageReceived(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCallSessionListener");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(26, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.ims.aidl.IImsCallSessionListener";
      }
    }
  }
}
