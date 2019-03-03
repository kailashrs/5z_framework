package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.ConnectionRequest;
import android.telecom.DisconnectCause;
import android.telecom.Logging.Session.Info;
import android.telecom.ParcelableConference;
import android.telecom.ParcelableConnection;
import android.telecom.PhoneAccountHandle;
import android.telecom.StatusHints;
import java.util.List;

public abstract interface IConnectionServiceAdapter
  extends IInterface
{
  public abstract void addConferenceCall(String paramString, ParcelableConference paramParcelableConference, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void addExistingConnection(String paramString, ParcelableConnection paramParcelableConnection, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void handleCreateConnectionComplete(String paramString, ConnectionRequest paramConnectionRequest, ParcelableConnection paramParcelableConnection, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onConnectionEvent(String paramString1, String paramString2, Bundle paramBundle, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onConnectionServiceFocusReleased(Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onPhoneAccountChanged(String paramString, PhoneAccountHandle paramPhoneAccountHandle, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onPostDialChar(String paramString, char paramChar, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onPostDialWait(String paramString1, String paramString2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onRemoteRttRequest(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onRttInitiationFailure(String paramString, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onRttInitiationSuccess(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onRttSessionRemotelyTerminated(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void putExtras(String paramString, Bundle paramBundle, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void queryRemoteConnectionServices(RemoteServiceCallback paramRemoteServiceCallback, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void removeCall(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void removeExtras(String paramString, List<String> paramList, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void resetCdmaConnectionTime(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setActive(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setAddress(String paramString, Uri paramUri, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setAudioRoute(String paramString1, int paramInt, String paramString2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setCallerDisplayName(String paramString1, String paramString2, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setConferenceMergeFailed(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setConferenceableConnections(String paramString, List<String> paramList, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setConnectionCapabilities(String paramString, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setConnectionProperties(String paramString, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setDialing(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setDisconnected(String paramString, DisconnectCause paramDisconnectCause, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setIsConferenced(String paramString1, String paramString2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setIsVoipAudioMode(String paramString, boolean paramBoolean, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setOnHold(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setPulling(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setRingbackRequested(String paramString, boolean paramBoolean, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setRinging(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setStatusHints(String paramString, StatusHints paramStatusHints, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setVideoProvider(String paramString, IVideoProvider paramIVideoProvider, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void setVideoState(String paramString, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConnectionServiceAdapter
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.IConnectionServiceAdapter";
    static final int TRANSACTION_addConferenceCall = 13;
    static final int TRANSACTION_addExistingConnection = 25;
    static final int TRANSACTION_handleCreateConnectionComplete = 1;
    static final int TRANSACTION_onConnectionEvent = 29;
    static final int TRANSACTION_onConnectionServiceFocusReleased = 35;
    static final int TRANSACTION_onPhoneAccountChanged = 34;
    static final int TRANSACTION_onPostDialChar = 16;
    static final int TRANSACTION_onPostDialWait = 15;
    static final int TRANSACTION_onRemoteRttRequest = 33;
    static final int TRANSACTION_onRttInitiationFailure = 31;
    static final int TRANSACTION_onRttInitiationSuccess = 30;
    static final int TRANSACTION_onRttSessionRemotelyTerminated = 32;
    static final int TRANSACTION_putExtras = 26;
    static final int TRANSACTION_queryRemoteConnectionServices = 17;
    static final int TRANSACTION_removeCall = 14;
    static final int TRANSACTION_removeExtras = 27;
    static final int TRANSACTION_resetCdmaConnectionTime = 36;
    static final int TRANSACTION_setActive = 2;
    static final int TRANSACTION_setAddress = 22;
    static final int TRANSACTION_setAudioRoute = 28;
    static final int TRANSACTION_setCallerDisplayName = 23;
    static final int TRANSACTION_setConferenceMergeFailed = 12;
    static final int TRANSACTION_setConferenceableConnections = 24;
    static final int TRANSACTION_setConnectionCapabilities = 9;
    static final int TRANSACTION_setConnectionProperties = 10;
    static final int TRANSACTION_setDialing = 4;
    static final int TRANSACTION_setDisconnected = 6;
    static final int TRANSACTION_setIsConferenced = 11;
    static final int TRANSACTION_setIsVoipAudioMode = 20;
    static final int TRANSACTION_setOnHold = 7;
    static final int TRANSACTION_setPulling = 5;
    static final int TRANSACTION_setRingbackRequested = 8;
    static final int TRANSACTION_setRinging = 3;
    static final int TRANSACTION_setStatusHints = 21;
    static final int TRANSACTION_setVideoProvider = 18;
    static final int TRANSACTION_setVideoState = 19;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.IConnectionServiceAdapter");
    }
    
    public static IConnectionServiceAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.IConnectionServiceAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof IConnectionServiceAdapter))) {
        return (IConnectionServiceAdapter)localIInterface;
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
        Object localObject1 = null;
        String str1 = null;
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
        Object localObject26 = null;
        Object localObject27 = null;
        Object localObject28 = null;
        Object localObject29 = null;
        Object localObject30 = null;
        Object localObject31 = null;
        Object localObject32 = null;
        Object localObject33 = null;
        String str2 = null;
        Object localObject34 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject34;
          }
          resetCdmaConnectionTime(paramParcel2, paramParcel1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onConnectionServiceFocusReleased(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          onPhoneAccountChanged((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onRemoteRttRequest(paramParcel2, paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onRttSessionRemotelyTerminated(paramParcel2, paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onRttInitiationFailure(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          onRttInitiationSuccess(paramParcel2, paramParcel1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          str1 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject6;
          }
          onConnectionEvent(str1, str2, paramParcel2, paramParcel1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          setAudioRoute((String)localObject6, paramInt1, paramParcel2, paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          localObject6 = paramParcel1.createStringArrayList();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          removeExtras(paramParcel2, (List)localObject6, paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          putExtras((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelableConnection)ParcelableConnection.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          addExistingConnection((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          paramParcel2 = paramParcel1.createStringArrayList();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          setConferenceableConnections((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          setCallerDisplayName((String)localObject6, paramParcel2, paramInt1, paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          setAddress((String)localObject6, paramParcel2, paramInt1, paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (StatusHints)StatusHints.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          setStatusHints((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          setIsVoipAudioMode(paramParcel2, bool2, paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          setVideoState(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          localObject6 = IVideoProvider.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject17;
          }
          setVideoProvider(paramParcel2, (IVideoProvider)localObject6, paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = RemoteServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          queryRemoteConnectionServices(paramParcel2, paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          char c = (char)paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          onPostDialChar(paramParcel2, c, paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          onPostDialWait((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          removeCall(paramParcel2, paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelableConference)ParcelableConference.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject22;
          }
          addConferenceCall((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          setConferenceMergeFailed(paramParcel2, paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject24;
          }
          setIsConferenced(paramParcel2, (String)localObject6, paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject25;
          }
          setConnectionProperties(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject26;
          }
          setConnectionCapabilities(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject27;
          }
          setRingbackRequested(paramParcel2, bool2, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject28;
          }
          setOnHold(paramParcel2, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DisconnectCause)DisconnectCause.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject29;
          }
          setDisconnected((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject30;
          }
          setPulling(paramParcel2, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject31;
          }
          setDialing(paramParcel2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject32;
          }
          setRinging(paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject33;
          }
          setActive(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionServiceAdapter");
        str1 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (ConnectionRequest)ConnectionRequest.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localObject6 = (ParcelableConnection)ParcelableConnection.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject6 = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = str2;
        }
        handleCreateConnectionComplete(str1, paramParcel2, (ParcelableConnection)localObject6, paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.IConnectionServiceAdapter");
      return true;
    }
    
    private static class Proxy
      implements IConnectionServiceAdapter
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addConferenceCall(String paramString, ParcelableConference paramParcelableConference, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramParcelableConference != null)
          {
            localParcel.writeInt(1);
            paramParcelableConference.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void addExistingConnection(String paramString, ParcelableConnection paramParcelableConnection, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramParcelableConnection != null)
          {
            localParcel.writeInt(1);
            paramParcelableConnection.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(25, localParcel, null, 1);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.IConnectionServiceAdapter";
      }
      
      public void handleCreateConnectionComplete(String paramString, ConnectionRequest paramConnectionRequest, ParcelableConnection paramParcelableConnection, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramConnectionRequest != null)
          {
            localParcel.writeInt(1);
            paramConnectionRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramParcelableConnection != null)
          {
            localParcel.writeInt(1);
            paramParcelableConnection.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void onConnectionEvent(String paramString1, String paramString2, Bundle paramBundle, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(29, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConnectionServiceFocusReleased(Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(35, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPhoneAccountChanged(String paramString, PhoneAccountHandle paramPhoneAccountHandle, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramPhoneAccountHandle != null)
          {
            localParcel.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPostDialChar(String paramString, char paramChar, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramChar);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void onPostDialWait(String paramString1, String paramString2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void onRemoteRttRequest(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void onRttInitiationFailure(String paramString, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(31, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRttInitiationSuccess(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRttSessionRemotelyTerminated(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void putExtras(String paramString, Bundle paramBundle, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(26, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void queryRemoteConnectionServices(RemoteServiceCallback paramRemoteServiceCallback, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          if (paramRemoteServiceCallback != null) {
            paramRemoteServiceCallback = paramRemoteServiceCallback.asBinder();
          } else {
            paramRemoteServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramRemoteServiceCallback);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void removeCall(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void removeExtras(String paramString, List<String> paramList, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeStringList(paramList);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void resetCdmaConnectionTime(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(36, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setActive(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setAddress(String paramString, Uri paramUri, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setAudioRoute(String paramString1, int paramInt, String paramString2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString2);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setCallerDisplayName(String paramString1, String paramString2, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setConferenceMergeFailed(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setConferenceableConnections(String paramString, List<String> paramList, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeStringList(paramList);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setConnectionCapabilities(String paramString, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setConnectionProperties(String paramString, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setDialing(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setDisconnected(String paramString, DisconnectCause paramDisconnectCause, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramDisconnectCause != null)
          {
            localParcel.writeInt(1);
            paramDisconnectCause.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setIsConferenced(String paramString1, String paramString2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setIsVoipAudioMode(String paramString, boolean paramBoolean, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setOnHold(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setPulling(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setRingbackRequested(String paramString, boolean paramBoolean, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setRinging(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setStatusHints(String paramString, StatusHints paramStatusHints, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramStatusHints != null)
          {
            localParcel.writeInt(1);
            paramStatusHints.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setVideoProvider(String paramString, IVideoProvider paramIVideoProvider, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          if (paramIVideoProvider != null) {
            paramString = paramIVideoProvider.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
      
      public void setVideoState(String paramString, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionServiceAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          if (paramInfo != null)
          {
            localParcel.writeInt(1);
            paramInfo.writeToParcel(localParcel, 0);
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
    }
  }
}
