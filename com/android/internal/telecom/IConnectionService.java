package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.CallAudioState;
import android.telecom.ConnectionRequest;
import android.telecom.Logging.Session.Info;
import android.telecom.PhoneAccountHandle;

public abstract interface IConnectionService
  extends IInterface
{
  public abstract void abort(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void addConnectionServiceAdapter(IConnectionServiceAdapter paramIConnectionServiceAdapter, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void addParticipantWithConference(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void answer(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void answerVideo(String paramString, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void conference(String paramString1, String paramString2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void connectionServiceFocusGained(Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void connectionServiceFocusLost(Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void createConnection(PhoneAccountHandle paramPhoneAccountHandle, String paramString, ConnectionRequest paramConnectionRequest, boolean paramBoolean1, boolean paramBoolean2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void createConnectionComplete(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void createConnectionFailed(PhoneAccountHandle paramPhoneAccountHandle, String paramString, ConnectionRequest paramConnectionRequest, boolean paramBoolean, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void deflect(String paramString, Uri paramUri, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void disconnect(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void handoverComplete(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void handoverFailed(String paramString, ConnectionRequest paramConnectionRequest, int paramInt, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void hold(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void mergeConference(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onCallAudioStateChanged(String paramString, CallAudioState paramCallAudioState, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onExtrasChanged(String paramString, Bundle paramBundle, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void onPostDialContinue(String paramString, boolean paramBoolean, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void playDtmfTone(String paramString, char paramChar, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void pullExternalCall(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void reject(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void rejectWithMessage(String paramString1, String paramString2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void removeConnectionServiceAdapter(IConnectionServiceAdapter paramIConnectionServiceAdapter, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void respondToRttUpgradeRequest(String paramString, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void sendCallEvent(String paramString1, String paramString2, Bundle paramBundle, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void silence(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void splitFromConference(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void startRtt(String paramString, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void stopDtmfTone(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void stopRtt(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void swapConference(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public abstract void unhold(String paramString, Session.Info paramInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConnectionService
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.IConnectionService";
    static final int TRANSACTION_abort = 6;
    static final int TRANSACTION_addConnectionServiceAdapter = 1;
    static final int TRANSACTION_addParticipantWithConference = 30;
    static final int TRANSACTION_answer = 8;
    static final int TRANSACTION_answerVideo = 7;
    static final int TRANSACTION_conference = 19;
    static final int TRANSACTION_connectionServiceFocusGained = 32;
    static final int TRANSACTION_connectionServiceFocusLost = 31;
    static final int TRANSACTION_createConnection = 3;
    static final int TRANSACTION_createConnectionComplete = 4;
    static final int TRANSACTION_createConnectionFailed = 5;
    static final int TRANSACTION_deflect = 9;
    static final int TRANSACTION_disconnect = 12;
    static final int TRANSACTION_handoverComplete = 34;
    static final int TRANSACTION_handoverFailed = 33;
    static final int TRANSACTION_hold = 14;
    static final int TRANSACTION_mergeConference = 21;
    static final int TRANSACTION_onCallAudioStateChanged = 16;
    static final int TRANSACTION_onExtrasChanged = 26;
    static final int TRANSACTION_onPostDialContinue = 23;
    static final int TRANSACTION_playDtmfTone = 17;
    static final int TRANSACTION_pullExternalCall = 24;
    static final int TRANSACTION_reject = 10;
    static final int TRANSACTION_rejectWithMessage = 11;
    static final int TRANSACTION_removeConnectionServiceAdapter = 2;
    static final int TRANSACTION_respondToRttUpgradeRequest = 29;
    static final int TRANSACTION_sendCallEvent = 25;
    static final int TRANSACTION_silence = 13;
    static final int TRANSACTION_splitFromConference = 20;
    static final int TRANSACTION_startRtt = 27;
    static final int TRANSACTION_stopDtmfTone = 18;
    static final int TRANSACTION_stopRtt = 28;
    static final int TRANSACTION_swapConference = 22;
    static final int TRANSACTION_unhold = 15;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.IConnectionService");
    }
    
    public static IConnectionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.IConnectionService");
      if ((localIInterface != null) && ((localIInterface instanceof IConnectionService))) {
        return (IConnectionService)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        String str1 = null;
        Object localObject4 = null;
        String str2 = null;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject29;
          }
          handoverComplete(paramParcel2, paramParcel1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ConnectionRequest)ConnectionRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          handoverFailed((String)localObject6, paramParcel2, paramInt1, paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          connectionServiceFocusGained(paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          connectionServiceFocusLost(paramParcel1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          addParticipantWithConference(paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject6 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          respondToRttUpgradeRequest(str2, paramParcel2, (ParcelFileDescriptor)localObject6, paramParcel1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          stopRtt(paramParcel2, paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject6 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          startRtt(str1, paramParcel2, (ParcelFileDescriptor)localObject6, paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          onExtrasChanged((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
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
          sendCallEvent(str1, str2, paramParcel2, paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          pullExternalCall(paramParcel2, paramParcel1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          onPostDialContinue(paramParcel2, bool1, paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          swapConference(paramParcel2, paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          mergeConference(paramParcel2, paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          splitFromConference(paramParcel2, paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          conference(paramParcel2, (String)localObject6, paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          stopDtmfTone(paramParcel2, paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          char c = (char)paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          playDtmfTone(paramParcel2, c, paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CallAudioState)CallAudioState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          onCallAudioStateChanged((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          unhold(paramParcel2, paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject17;
          }
          hold(paramParcel2, paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          silence(paramParcel2, paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          disconnect(paramParcel2, paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          rejectWithMessage(paramParcel2, (String)localObject6, paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          reject(paramParcel2, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject22;
          }
          deflect((String)localObject6, paramParcel2, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          answer(paramParcel2, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject24;
          }
          answerVideo(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject25;
          }
          abort(paramParcel2, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject6 = (ConnectionRequest)ConnectionRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          createConnectionFailed(paramParcel2, str1, (ConnectionRequest)localObject6, bool1, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject26;
          }
          createConnectionComplete(paramParcel2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject6 = (ConnectionRequest)ConnectionRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          boolean bool2;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          createConnection(paramParcel2, str1, (ConnectionRequest)localObject6, bool1, bool2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
          paramParcel2 = IConnectionServiceAdapter.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject27;
          }
          removeConnectionServiceAdapter(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.IConnectionService");
        paramParcel2 = IConnectionServiceAdapter.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Session.Info)Session.Info.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject28;
        }
        addConnectionServiceAdapter(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.IConnectionService");
      return true;
    }
    
    private static class Proxy
      implements IConnectionService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void abort(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void addConnectionServiceAdapter(IConnectionServiceAdapter paramIConnectionServiceAdapter, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          if (paramIConnectionServiceAdapter != null) {
            paramIConnectionServiceAdapter = paramIConnectionServiceAdapter.asBinder();
          } else {
            paramIConnectionServiceAdapter = null;
          }
          localParcel.writeStrongBinder(paramIConnectionServiceAdapter);
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
      
      public void addParticipantWithConference(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void answer(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void answerVideo(String paramString, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(7, localParcel, null, 1);
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
      
      public void conference(String paramString1, String paramString2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void connectionServiceFocusGained(Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void connectionServiceFocusLost(Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void createConnection(PhoneAccountHandle paramPhoneAccountHandle, String paramString, ConnectionRequest paramConnectionRequest, boolean paramBoolean1, boolean paramBoolean2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
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
      
      public void createConnectionComplete(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void createConnectionFailed(PhoneAccountHandle paramPhoneAccountHandle, String paramString, ConnectionRequest paramConnectionRequest, boolean paramBoolean, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deflect(String paramString, Uri paramUri, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void disconnect(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.IConnectionService";
      }
      
      public void handoverComplete(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void handoverFailed(String paramString, ConnectionRequest paramConnectionRequest, int paramInt, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(33, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void hold(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void mergeConference(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCallAudioStateChanged(String paramString, CallAudioState paramCallAudioState, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          localParcel.writeString(paramString);
          if (paramCallAudioState != null)
          {
            localParcel.writeInt(1);
            paramCallAudioState.writeToParcel(localParcel, 0);
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
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onExtrasChanged(String paramString, Bundle paramBundle, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void onPostDialContinue(String paramString, boolean paramBoolean, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playDtmfTone(String paramString, char paramChar, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void pullExternalCall(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(24, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void reject(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void rejectWithMessage(String paramString1, String paramString2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
      
      public void removeConnectionServiceAdapter(IConnectionServiceAdapter paramIConnectionServiceAdapter, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          if (paramIConnectionServiceAdapter != null) {
            paramIConnectionServiceAdapter = paramIConnectionServiceAdapter.asBinder();
          } else {
            paramIConnectionServiceAdapter = null;
          }
          localParcel.writeStrongBinder(paramIConnectionServiceAdapter);
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
      
      public void respondToRttUpgradeRequest(String paramString, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          localParcel.writeString(paramString);
          if (paramParcelFileDescriptor1 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramParcelFileDescriptor2 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor2.writeToParcel(localParcel, 0);
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
      
      public void sendCallEvent(String paramString1, String paramString2, Bundle paramBundle, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(25, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void silence(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void splitFromConference(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startRtt(String paramString, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
          localParcel.writeString(paramString);
          if (paramParcelFileDescriptor1 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramParcelFileDescriptor2 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor2.writeToParcel(localParcel, 0);
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
          mRemote.transact(27, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopDtmfTone(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopRtt(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(28, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void swapConference(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unhold(String paramString, Session.Info paramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IConnectionService");
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
          mRemote.transact(15, localParcel, null, 1);
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
