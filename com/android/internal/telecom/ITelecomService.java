package com.android.internal.telecom;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomAnalytics;
import java.util.ArrayList;
import java.util.List;

public abstract interface ITelecomService
  extends IInterface
{
  public abstract void acceptHandover(Uri paramUri, int paramInt, PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract void acceptRingingCall(String paramString)
    throws RemoteException;
  
  public abstract void acceptRingingCallWithVideoState(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void addNewIncomingCall(PhoneAccountHandle paramPhoneAccountHandle, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void addNewUnknownCall(PhoneAccountHandle paramPhoneAccountHandle, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void cancelMissedCallsNotification(String paramString)
    throws RemoteException;
  
  public abstract void clearAccounts(String paramString)
    throws RemoteException;
  
  public abstract Intent createManageBlockedNumbersIntent()
    throws RemoteException;
  
  public abstract TelecomAnalytics dumpCallAnalytics()
    throws RemoteException;
  
  public abstract boolean enablePhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean endCall(String paramString)
    throws RemoteException;
  
  public abstract Uri getAdnUriForPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
    throws RemoteException;
  
  public abstract List<PhoneAccountHandle> getAllPhoneAccountHandles()
    throws RemoteException;
  
  public abstract List<PhoneAccount> getAllPhoneAccounts()
    throws RemoteException;
  
  public abstract int getAllPhoneAccountsCount()
    throws RemoteException;
  
  public abstract List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract int getCallState()
    throws RemoteException;
  
  public abstract int getCurrentTtyMode(String paramString)
    throws RemoteException;
  
  public abstract String getDefaultDialerPackage()
    throws RemoteException;
  
  public abstract PhoneAccountHandle getDefaultOutgoingPhoneAccount(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract ComponentName getDefaultPhoneApp()
    throws RemoteException;
  
  public abstract String getLine1Number(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
    throws RemoteException;
  
  public abstract PhoneAccount getPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract List<PhoneAccountHandle> getPhoneAccountsForPackage(String paramString)
    throws RemoteException;
  
  public abstract List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String paramString)
    throws RemoteException;
  
  public abstract PhoneAccountHandle getSimCallManager()
    throws RemoteException;
  
  public abstract PhoneAccountHandle getSimCallManagerForUser(int paramInt)
    throws RemoteException;
  
  public abstract String getSystemDialerPackage()
    throws RemoteException;
  
  public abstract PhoneAccountHandle getUserSelectedOutgoingPhoneAccount()
    throws RemoteException;
  
  public abstract String getVoiceMailNumber(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
    throws RemoteException;
  
  public abstract boolean handlePinMmi(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean handlePinMmiForPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean isInCall(String paramString)
    throws RemoteException;
  
  public abstract boolean isInManagedCall(String paramString)
    throws RemoteException;
  
  public abstract boolean isIncomingCallPermitted(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract boolean isOutgoingCallPermitted(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract boolean isRinging(String paramString)
    throws RemoteException;
  
  public abstract boolean isTtySupported(String paramString)
    throws RemoteException;
  
  public abstract boolean isVoiceMailNumber(PhoneAccountHandle paramPhoneAccountHandle, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void placeCall(Uri paramUri, Bundle paramBundle, String paramString)
    throws RemoteException;
  
  public abstract void registerPhoneAccount(PhoneAccount paramPhoneAccount)
    throws RemoteException;
  
  public abstract boolean setDefaultDialer(String paramString)
    throws RemoteException;
  
  public abstract void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract void showInCallScreen(boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void silenceRinger(String paramString)
    throws RemoteException;
  
  public abstract void unregisterPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract void waitOnHandlers()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITelecomService
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.ITelecomService";
    static final int TRANSACTION_acceptHandover = 48;
    static final int TRANSACTION_acceptRingingCall = 31;
    static final int TRANSACTION_acceptRingingCallWithVideoState = 32;
    static final int TRANSACTION_addNewIncomingCall = 39;
    static final int TRANSACTION_addNewUnknownCall = 40;
    static final int TRANSACTION_cancelMissedCallsNotification = 33;
    static final int TRANSACTION_clearAccounts = 17;
    static final int TRANSACTION_createManageBlockedNumbersIntent = 44;
    static final int TRANSACTION_dumpCallAnalytics = 24;
    static final int TRANSACTION_enablePhoneAccount = 42;
    static final int TRANSACTION_endCall = 30;
    static final int TRANSACTION_getAdnUriForPhoneAccount = 36;
    static final int TRANSACTION_getAllPhoneAccountHandles = 12;
    static final int TRANSACTION_getAllPhoneAccounts = 11;
    static final int TRANSACTION_getAllPhoneAccountsCount = 10;
    static final int TRANSACTION_getCallCapablePhoneAccounts = 5;
    static final int TRANSACTION_getCallState = 29;
    static final int TRANSACTION_getCurrentTtyMode = 38;
    static final int TRANSACTION_getDefaultDialerPackage = 22;
    static final int TRANSACTION_getDefaultOutgoingPhoneAccount = 2;
    static final int TRANSACTION_getDefaultPhoneApp = 21;
    static final int TRANSACTION_getLine1Number = 20;
    static final int TRANSACTION_getPhoneAccount = 9;
    static final int TRANSACTION_getPhoneAccountsForPackage = 8;
    static final int TRANSACTION_getPhoneAccountsSupportingScheme = 7;
    static final int TRANSACTION_getSelfManagedPhoneAccounts = 6;
    static final int TRANSACTION_getSimCallManager = 13;
    static final int TRANSACTION_getSimCallManagerForUser = 14;
    static final int TRANSACTION_getSystemDialerPackage = 23;
    static final int TRANSACTION_getUserSelectedOutgoingPhoneAccount = 3;
    static final int TRANSACTION_getVoiceMailNumber = 19;
    static final int TRANSACTION_handlePinMmi = 34;
    static final int TRANSACTION_handlePinMmiForPhoneAccount = 35;
    static final int TRANSACTION_isInCall = 26;
    static final int TRANSACTION_isInManagedCall = 27;
    static final int TRANSACTION_isIncomingCallPermitted = 45;
    static final int TRANSACTION_isOutgoingCallPermitted = 46;
    static final int TRANSACTION_isRinging = 28;
    static final int TRANSACTION_isTtySupported = 37;
    static final int TRANSACTION_isVoiceMailNumber = 18;
    static final int TRANSACTION_placeCall = 41;
    static final int TRANSACTION_registerPhoneAccount = 15;
    static final int TRANSACTION_setDefaultDialer = 43;
    static final int TRANSACTION_setUserSelectedOutgoingPhoneAccount = 4;
    static final int TRANSACTION_showInCallScreen = 1;
    static final int TRANSACTION_silenceRinger = 25;
    static final int TRANSACTION_unregisterPhoneAccount = 16;
    static final int TRANSACTION_waitOnHandlers = 47;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.ITelecomService");
    }
    
    public static ITelecomService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.ITelecomService");
      if ((localIInterface != null) && ((localIInterface instanceof ITelecomService))) {
        return (ITelecomService)localIInterface;
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
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 48: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject16;
          }
          acceptHandover((Uri)localObject1, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          waitOnHandlers();
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          paramInt1 = isOutgoingCallPermitted(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = isIncomingCallPermitted(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = createManageBlockedNumbersIntent();
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
        case 43: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = setDefaultDialer(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 42: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject3;
          }
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          paramInt1 = enablePhoneAccount((PhoneAccountHandle)localObject1, bool3);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject16 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject16 = localObject4;
          }
          placeCall((Uri)localObject1, (Bundle)localObject16, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          addNewUnknownCall((PhoneAccountHandle)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          addNewIncomingCall((PhoneAccountHandle)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = getCurrentTtyMode(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = isTtySupported(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject7;
          }
          paramParcel1 = getAdnUriForPhoneAccount((PhoneAccountHandle)localObject1, paramParcel1.readString());
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
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject8;
          }
          paramInt1 = handlePinMmiForPhoneAccount((PhoneAccountHandle)localObject1, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = handlePinMmi(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          cancelMissedCallsNotification(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          acceptRingingCallWithVideoState(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          acceptRingingCall(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = endCall(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = getCallState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = isRinging(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = isInManagedCall(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = isInCall(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          silenceRinger(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = dumpCallAnalytics();
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
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getSystemDialerPackage();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getDefaultDialerPackage();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getDefaultPhoneApp();
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
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject9;
          }
          paramParcel1 = getLine1Number((PhoneAccountHandle)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject10;
          }
          paramParcel1 = getVoiceMailNumber((PhoneAccountHandle)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject11;
          }
          paramInt1 = isVoiceMailNumber((PhoneAccountHandle)localObject1, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          clearAccounts(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          unregisterPhoneAccount(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccount)PhoneAccount.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          registerPhoneAccount(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getSimCallManagerForUser(paramParcel1.readInt());
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
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getSimCallManager();
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
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getAllPhoneAccountHandles();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getAllPhoneAccounts();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramInt1 = getAllPhoneAccountsCount();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          paramParcel1 = getPhoneAccount(paramParcel1);
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
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getPhoneAccountsForPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getPhoneAccountsSupportingScheme(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getSelfManagedPhoneAccounts(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          paramParcel1 = getCallCapablePhoneAccounts(bool3, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          setUserSelectedOutgoingPhoneAccount(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getUserSelectedOutgoingPhoneAccount();
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
          paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
          paramParcel1 = getDefaultOutgoingPhoneAccount(paramParcel1.readString(), paramParcel1.readString());
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
        paramParcel1.enforceInterface("com.android.internal.telecom.ITelecomService");
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        showInCallScreen(bool3, paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.ITelecomService");
      return true;
    }
    
    private static class Proxy
      implements ITelecomService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acceptHandover(Uri paramUri, int paramInt, PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
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
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void acceptRingingCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
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
      
      public void acceptRingingCallWithVideoState(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
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
      
      public void addNewIncomingCall(PhoneAccountHandle paramPhoneAccountHandle, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
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
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addNewUnknownCall(PhoneAccountHandle paramPhoneAccountHandle, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
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
          mRemote.transact(40, localParcel1, localParcel2, 0);
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
      
      public void cancelMissedCallsNotification(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
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
      
      public void clearAccounts(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
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
      
      public Intent createManageBlockedNumbersIntent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Intent localIntent;
          if (localParcel2.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            localIntent = null;
          }
          return localIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public TelecomAnalytics dumpCallAnalytics()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          TelecomAnalytics localTelecomAnalytics;
          if (localParcel2.readInt() != 0) {
            localTelecomAnalytics = (TelecomAnalytics)TelecomAnalytics.CREATOR.createFromParcel(localParcel2);
          } else {
            localTelecomAnalytics = null;
          }
          return localTelecomAnalytics;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean enablePhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          boolean bool = true;
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
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
      
      public boolean endCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(30, localParcel1, localParcel2, 0);
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
      
      public Uri getAdnUriForPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPhoneAccountHandle = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPhoneAccountHandle = null;
          }
          return paramPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PhoneAccountHandle> getAllPhoneAccountHandles()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(PhoneAccountHandle.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PhoneAccount> getAllPhoneAccounts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(PhoneAccount.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getAllPhoneAccountsCount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
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
      
      public List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(PhoneAccountHandle.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCallState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(29, localParcel1, localParcel2, 0);
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
      
      public int getCurrentTtyMode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          mRemote.transact(38, localParcel1, localParcel2, 0);
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
      
      public String getDefaultDialerPackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public PhoneAccountHandle getDefaultOutgoingPhoneAccount(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getDefaultPhoneApp()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.ITelecomService";
      }
      
      public String getLine1Number(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramPhoneAccountHandle = localParcel2.readString();
          return paramPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PhoneAccount getPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPhoneAccountHandle = (PhoneAccount)PhoneAccount.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPhoneAccountHandle = null;
          }
          return paramPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PhoneAccountHandle> getPhoneAccountsForPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(PhoneAccountHandle.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createTypedArrayList(PhoneAccountHandle.CREATOR);
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(PhoneAccountHandle.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PhoneAccountHandle getSimCallManager()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PhoneAccountHandle localPhoneAccountHandle;
          if (localParcel2.readInt() != 0) {
            localPhoneAccountHandle = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPhoneAccountHandle = null;
          }
          return localPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PhoneAccountHandle getSimCallManagerForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PhoneAccountHandle localPhoneAccountHandle;
          if (localParcel2.readInt() != 0) {
            localPhoneAccountHandle = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPhoneAccountHandle = null;
          }
          return localPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSystemDialerPackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public PhoneAccountHandle getUserSelectedOutgoingPhoneAccount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PhoneAccountHandle localPhoneAccountHandle;
          if (localParcel2.readInt() != 0) {
            localPhoneAccountHandle = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPhoneAccountHandle = null;
          }
          return localPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getVoiceMailNumber(PhoneAccountHandle paramPhoneAccountHandle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramPhoneAccountHandle = localParcel2.readString();
          return paramPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean handlePinMmi(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(34, localParcel1, localParcel2, 0);
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
      
      public boolean handlePinMmiForPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          boolean bool = true;
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public boolean isInCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(26, localParcel1, localParcel2, 0);
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
      
      public boolean isInManagedCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(27, localParcel1, localParcel2, 0);
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
      
      public boolean isIncomingCallPermitted(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          boolean bool = true;
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public boolean isOutgoingCallPermitted(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          boolean bool = true;
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public boolean isRinging(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(28, localParcel1, localParcel2, 0);
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
      
      public boolean isTtySupported(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(37, localParcel1, localParcel2, 0);
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
      
      public boolean isVoiceMailNumber(PhoneAccountHandle paramPhoneAccountHandle, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          boolean bool = true;
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public void placeCall(Uri paramUri, Bundle paramBundle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
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
          localParcel1.writeString(paramString);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerPhoneAccount(PhoneAccount paramPhoneAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccount != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public boolean setDefaultDialer(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(43, localParcel1, localParcel2, 0);
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
      
      public void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showInCallScreen(boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
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
      
      public void silenceRinger(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          localParcel1.writeString(paramString);
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
      
      public void unregisterPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void waitOnHandlers()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telecom.ITelecomService");
          mRemote.transact(47, localParcel1, localParcel2, 0);
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
