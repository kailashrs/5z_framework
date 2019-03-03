package android.app;

import android.app.servertransaction.ClientTransaction;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentReceiver.Stub;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractor.Stub;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract interface IApplicationThread
  extends IInterface
{
  public abstract void attachAgent(String paramString)
    throws RemoteException;
  
  public abstract void bindApplication(String paramString1, ApplicationInfo paramApplicationInfo, List<ProviderInfo> paramList, ComponentName paramComponentName, ProfilerInfo paramProfilerInfo, Bundle paramBundle1, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo, Map paramMap, Bundle paramBundle2, String paramString2, boolean paramBoolean5)
    throws RemoteException;
  
  public abstract void clearDnsCache()
    throws RemoteException;
  
  public abstract void dispatchPackageBroadcast(int paramInt, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpActivity(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String paramString, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpDbInfo(ParcelFileDescriptor paramParcelFileDescriptor, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpGfxInfo(ParcelFileDescriptor paramParcelFileDescriptor, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpHeap(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract void dumpMemInfo(ParcelFileDescriptor paramParcelFileDescriptor, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpMemInfoProto(ParcelFileDescriptor paramParcelFileDescriptor, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpProvider(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void dumpService(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void handleTrustStorageUpdate()
    throws RemoteException;
  
  public abstract void notifyCleartextNetwork(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void processInBackground()
    throws RemoteException;
  
  public abstract void profilerControl(boolean paramBoolean, ProfilerInfo paramProfilerInfo, int paramInt)
    throws RemoteException;
  
  public abstract void requestAssistContextExtras(IBinder paramIBinder1, IBinder paramIBinder2, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void runIsolatedEntryPoint(String paramString, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void scheduleApplicationInfoChanged(ApplicationInfo paramApplicationInfo)
    throws RemoteException;
  
  public abstract void scheduleBindService(IBinder paramIBinder, Intent paramIntent, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void scheduleCrash(String paramString)
    throws RemoteException;
  
  public abstract void scheduleCreateBackupAgent(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
    throws RemoteException;
  
  public abstract void scheduleCreateService(IBinder paramIBinder, ServiceInfo paramServiceInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
    throws RemoteException;
  
  public abstract void scheduleDestroyBackupAgent(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo)
    throws RemoteException;
  
  public abstract void scheduleEnterAnimationComplete(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void scheduleExit()
    throws RemoteException;
  
  public abstract void scheduleInstallProvider(ProviderInfo paramProviderInfo)
    throws RemoteException;
  
  public abstract void scheduleLocalVoiceInteractionStarted(IBinder paramIBinder, IVoiceInteractor paramIVoiceInteractor)
    throws RemoteException;
  
  public abstract void scheduleLowMemory()
    throws RemoteException;
  
  public abstract void scheduleOnNewActivityOptions(IBinder paramIBinder, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void scheduleReceiver(Intent paramIntent, ActivityInfo paramActivityInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void scheduleRegisteredReceiver(IIntentReceiver paramIIntentReceiver, Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void scheduleServiceArgs(IBinder paramIBinder, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void scheduleSleeping(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void scheduleStopService(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void scheduleSuicide()
    throws RemoteException;
  
  public abstract void scheduleTransaction(ClientTransaction paramClientTransaction)
    throws RemoteException;
  
  public abstract void scheduleTranslucentConversionComplete(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void scheduleTrimMemory(int paramInt)
    throws RemoteException;
  
  public abstract void scheduleUnbindService(IBinder paramIBinder, Intent paramIntent)
    throws RemoteException;
  
  public abstract void setCoreSettings(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void setHttpProxy(String paramString1, String paramString2, String paramString3, Uri paramUri)
    throws RemoteException;
  
  public abstract void setNetworkBlockSeq(long paramLong)
    throws RemoteException;
  
  public abstract void setProcessState(int paramInt)
    throws RemoteException;
  
  public abstract void setSchedulingGroup(int paramInt)
    throws RemoteException;
  
  public abstract void startBinderTracking()
    throws RemoteException;
  
  public abstract void stopBinderTrackingAndDump(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract void unstableProviderDied(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void updatePackageCompatibilityInfo(String paramString, CompatibilityInfo paramCompatibilityInfo)
    throws RemoteException;
  
  public abstract void updateTimePrefs(int paramInt)
    throws RemoteException;
  
  public abstract void updateTimeZone()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IApplicationThread
  {
    private static final String DESCRIPTOR = "android.app.IApplicationThread";
    static final int TRANSACTION_attachAgent = 48;
    static final int TRANSACTION_bindApplication = 4;
    static final int TRANSACTION_clearDnsCache = 26;
    static final int TRANSACTION_dispatchPackageBroadcast = 22;
    static final int TRANSACTION_dumpActivity = 25;
    static final int TRANSACTION_dumpDbInfo = 35;
    static final int TRANSACTION_dumpGfxInfo = 33;
    static final int TRANSACTION_dumpHeap = 24;
    static final int TRANSACTION_dumpMemInfo = 31;
    static final int TRANSACTION_dumpMemInfoProto = 32;
    static final int TRANSACTION_dumpProvider = 34;
    static final int TRANSACTION_dumpService = 12;
    static final int TRANSACTION_handleTrustStorageUpdate = 47;
    static final int TRANSACTION_notifyCleartextNetwork = 43;
    static final int TRANSACTION_processInBackground = 9;
    static final int TRANSACTION_profilerControl = 16;
    static final int TRANSACTION_requestAssistContextExtras = 37;
    static final int TRANSACTION_runIsolatedEntryPoint = 5;
    static final int TRANSACTION_scheduleApplicationInfoChanged = 49;
    static final int TRANSACTION_scheduleBindService = 10;
    static final int TRANSACTION_scheduleCrash = 23;
    static final int TRANSACTION_scheduleCreateBackupAgent = 18;
    static final int TRANSACTION_scheduleCreateService = 2;
    static final int TRANSACTION_scheduleDestroyBackupAgent = 19;
    static final int TRANSACTION_scheduleEnterAnimationComplete = 42;
    static final int TRANSACTION_scheduleExit = 6;
    static final int TRANSACTION_scheduleInstallProvider = 40;
    static final int TRANSACTION_scheduleLocalVoiceInteractionStarted = 46;
    static final int TRANSACTION_scheduleLowMemory = 14;
    static final int TRANSACTION_scheduleOnNewActivityOptions = 20;
    static final int TRANSACTION_scheduleReceiver = 1;
    static final int TRANSACTION_scheduleRegisteredReceiver = 13;
    static final int TRANSACTION_scheduleServiceArgs = 7;
    static final int TRANSACTION_scheduleSleeping = 15;
    static final int TRANSACTION_scheduleStopService = 3;
    static final int TRANSACTION_scheduleSuicide = 21;
    static final int TRANSACTION_scheduleTransaction = 51;
    static final int TRANSACTION_scheduleTranslucentConversionComplete = 38;
    static final int TRANSACTION_scheduleTrimMemory = 30;
    static final int TRANSACTION_scheduleUnbindService = 11;
    static final int TRANSACTION_setCoreSettings = 28;
    static final int TRANSACTION_setHttpProxy = 27;
    static final int TRANSACTION_setNetworkBlockSeq = 50;
    static final int TRANSACTION_setProcessState = 39;
    static final int TRANSACTION_setSchedulingGroup = 17;
    static final int TRANSACTION_startBinderTracking = 44;
    static final int TRANSACTION_stopBinderTrackingAndDump = 45;
    static final int TRANSACTION_unstableProviderDied = 36;
    static final int TRANSACTION_updatePackageCompatibilityInfo = 29;
    static final int TRANSACTION_updateTimePrefs = 41;
    static final int TRANSACTION_updateTimeZone = 8;
    
    public Stub()
    {
      attachInterface(this, "android.app.IApplicationThread");
    }
    
    public static IApplicationThread asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IApplicationThread");
      if ((localIInterface != null) && ((localIInterface instanceof IApplicationThread))) {
        return (IApplicationThread)localIInterface;
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
        String str1 = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        CompatibilityInfo localCompatibilityInfo = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        String str2 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        ArrayList localArrayList = null;
        IInstrumentationWatcher localIInstrumentationWatcher = null;
        IUiAutomationConnection localIUiAutomationConnection = null;
        Bundle localBundle = null;
        Object localObject13 = null;
        HashMap localHashMap = null;
        boolean bool5;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 51: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ClientTransaction)ClientTransaction.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localHashMap;
          }
          scheduleTransaction(paramParcel1);
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          setNetworkBlockSeq(paramParcel1.readLong());
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          scheduleApplicationInfoChanged(paramParcel1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          attachAgent(paramParcel1.readString());
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          handleTrustStorageUpdate();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleLocalVoiceInteractionStarted(paramParcel1.readStrongBinder(), IVoiceInteractor.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          stopBinderTrackingAndDump(paramParcel1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          startBinderTracking();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          notifyCleartextNetwork(paramParcel1.createByteArray());
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleEnterAnimationComplete(paramParcel1.readStrongBinder());
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          updateTimePrefs(paramParcel1.readInt());
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          scheduleInstallProvider(paramParcel1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          setProcessState(paramParcel1.readInt());
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          scheduleTranslucentConversionComplete(paramParcel2, bool4);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          requestAssistContextExtras(paramParcel1.readStrongBinder(), paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          unstableProviderDied(paramParcel1.readStrongBinder());
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject3;
          }
          dumpDbInfo(paramParcel2, paramParcel1.createStringArray());
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject4;
          }
          dumpProvider(paramParcel2, paramParcel1.readStrongBinder(), paramParcel1.createStringArray());
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject5;
          }
          dumpGfxInfo(paramParcel2, paramParcel1.createStringArray());
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject13 = (Debug.MemoryInfo)Debug.MemoryInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject13 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
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
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          dumpMemInfoProto(paramParcel2, (Debug.MemoryInfo)localObject13, bool4, bool1, bool2, bool3, paramParcel1.createStringArray());
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject13 = (Debug.MemoryInfo)Debug.MemoryInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject13 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
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
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          dumpMemInfo(paramParcel2, (Debug.MemoryInfo)localObject13, bool4, bool1, bool2, bool3, bool5, paramParcel1.createStringArray());
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleTrimMemory(paramParcel1.readInt());
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CompatibilityInfo)CompatibilityInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localCompatibilityInfo;
          }
          updatePackageCompatibilityInfo(paramParcel2, paramParcel1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          setCoreSettings(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readString();
          localObject13 = paramParcel1.readString();
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject7;
          }
          setHttpProxy(paramParcel2, (String)localObject13, (String)localObject9, paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          clearDnsCache();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject8;
          }
          dumpActivity(paramParcel2, paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.createStringArray());
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
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
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          dumpHeap(bool4, bool1, bool2, paramParcel2, paramParcel1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleCrash(paramParcel1.readString());
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          dispatchPackageBroadcast(paramParcel1.readInt(), paramParcel1.createStringArray());
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleSuicide();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          scheduleOnNewActivityOptions(paramParcel2, paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CompatibilityInfo)CompatibilityInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject9;
          }
          scheduleDestroyBackupAgent(paramParcel2, paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject13 = (CompatibilityInfo)CompatibilityInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject13 = localObject10;
          }
          scheduleCreateBackupAgent(paramParcel2, (CompatibilityInfo)localObject13, paramParcel1.readInt());
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          setSchedulingGroup(paramParcel1.readInt());
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject11;
          }
          profilerControl(bool4, paramParcel2, paramParcel1.readInt());
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readStrongBinder();
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          scheduleSleeping(paramParcel2, bool4);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleLowMemory();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          localObject7 = IIntentReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject13 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject13 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          scheduleRegisteredReceiver((IIntentReceiver)localObject7, paramParcel2, paramInt1, (String)localObject9, (Bundle)localObject13, bool4, bool1, paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject12;
          }
          dumpService(paramParcel2, paramParcel1.readStrongBinder(), paramParcel1.createStringArray());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localArrayList;
          }
          scheduleUnbindService(paramParcel2, paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          localObject13 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localIInstrumentationWatcher;
          }
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          scheduleBindService((IBinder)localObject13, paramParcel2, bool4, paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          processInBackground();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          updateTimeZone();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          paramParcel2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIUiAutomationConnection;
          }
          scheduleServiceArgs(paramParcel2, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleExit();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          runIsolatedEntryPoint(paramParcel1.readString(), paramParcel1.createStringArray());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          localArrayList = paramParcel1.createTypedArrayList(ProviderInfo.CREATOR);
          if (paramParcel1.readInt() != 0) {
            localObject13 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject13 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject7 = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = null;
          }
          localIInstrumentationWatcher = IInstrumentationWatcher.Stub.asInterface(paramParcel1.readStrongBinder());
          localIUiAutomationConnection = IUiAutomationConnection.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
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
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            localObject10 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localCompatibilityInfo = (CompatibilityInfo)CompatibilityInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localCompatibilityInfo = null;
          }
          localHashMap = paramParcel1.readHashMap(getClass().getClassLoader());
          if (paramParcel1.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          bindApplication(str2, paramParcel2, localArrayList, (ComponentName)localObject13, (ProfilerInfo)localObject7, (Bundle)localObject9, localIInstrumentationWatcher, localIUiAutomationConnection, paramInt1, bool4, bool1, bool2, bool3, (Configuration)localObject10, localCompatibilityInfo, localHashMap, localBundle, str1, bool5);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          scheduleStopService(paramParcel1.readStrongBinder());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IApplicationThread");
          localObject7 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ServiceInfo)ServiceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject13 = (CompatibilityInfo)CompatibilityInfo.CREATOR.createFromParcel(paramParcel1);
          }
          scheduleCreateService((IBinder)localObject7, paramParcel2, (CompatibilityInfo)localObject13, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.IApplicationThread");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localObject13 = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject13 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localObject7 = (CompatibilityInfo)CompatibilityInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject7 = null;
        }
        paramInt1 = paramParcel1.readInt();
        localObject10 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject9 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject9 = null;
        }
        if (paramParcel1.readInt() != 0) {
          bool4 = true;
        } else {
          bool4 = false;
        }
        scheduleReceiver(paramParcel2, (ActivityInfo)localObject13, (CompatibilityInfo)localObject7, paramInt1, (String)localObject10, (Bundle)localObject9, bool4, paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.app.IApplicationThread");
      return true;
    }
    
    private static class Proxy
      implements IApplicationThread
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
      
      public void attachAgent(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeString(paramString);
          mRemote.transact(48, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void bindApplication(String paramString1, ApplicationInfo paramApplicationInfo, List<ProviderInfo> paramList, ComponentName paramComponentName, ProfilerInfo paramProfilerInfo, Bundle paramBundle1, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo, Map paramMap, Bundle paramBundle2, String paramString2, boolean paramBoolean5)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 20
        //   5: aload 20
        //   7: ldc 34
        //   9: invokevirtual 37	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 20
        //   14: aload_1
        //   15: invokevirtual 40	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   18: aload_2
        //   19: ifnull +19 -> 38
        //   22: aload 20
        //   24: iconst_1
        //   25: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   28: aload_2
        //   29: aload 20
        //   31: iconst_0
        //   32: invokevirtual 62	android/content/pm/ApplicationInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   35: goto +9 -> 44
        //   38: aload 20
        //   40: iconst_0
        //   41: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   44: aload 20
        //   46: aload_3
        //   47: invokevirtual 66	android/os/Parcel:writeTypedList	(Ljava/util/List;)V
        //   50: aload 4
        //   52: ifnull +20 -> 72
        //   55: aload 20
        //   57: iconst_1
        //   58: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   61: aload 4
        //   63: aload 20
        //   65: iconst_0
        //   66: invokevirtual 69	android/content/ComponentName:writeToParcel	(Landroid/os/Parcel;I)V
        //   69: goto +9 -> 78
        //   72: aload 20
        //   74: iconst_0
        //   75: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   78: aload 5
        //   80: ifnull +20 -> 100
        //   83: aload 20
        //   85: iconst_1
        //   86: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   89: aload 5
        //   91: aload 20
        //   93: iconst_0
        //   94: invokevirtual 72	android/app/ProfilerInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   97: goto +9 -> 106
        //   100: aload 20
        //   102: iconst_0
        //   103: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   106: aload 6
        //   108: ifnull +20 -> 128
        //   111: aload 20
        //   113: iconst_1
        //   114: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   117: aload 6
        //   119: aload 20
        //   121: iconst_0
        //   122: invokevirtual 75	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   125: goto +9 -> 134
        //   128: aload 20
        //   130: iconst_0
        //   131: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   134: aload 7
        //   136: ifnull +14 -> 150
        //   139: aload 7
        //   141: invokeinterface 79 1 0
        //   146: astore_1
        //   147: goto +5 -> 152
        //   150: aconst_null
        //   151: astore_1
        //   152: aload 20
        //   154: aload_1
        //   155: invokevirtual 82	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   158: aload 8
        //   160: ifnull +14 -> 174
        //   163: aload 8
        //   165: invokeinterface 85 1 0
        //   170: astore_1
        //   171: goto +5 -> 176
        //   174: aconst_null
        //   175: astore_1
        //   176: aload 20
        //   178: aload_1
        //   179: invokevirtual 82	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   182: aload 20
        //   184: iload 9
        //   186: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   189: aload 20
        //   191: iload 10
        //   193: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   196: aload 20
        //   198: iload 11
        //   200: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   203: aload 20
        //   205: iload 12
        //   207: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   210: aload 20
        //   212: iload 13
        //   214: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   217: aload 14
        //   219: ifnull +20 -> 239
        //   222: aload 20
        //   224: iconst_1
        //   225: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   228: aload 14
        //   230: aload 20
        //   232: iconst_0
        //   233: invokevirtual 88	android/content/res/Configuration:writeToParcel	(Landroid/os/Parcel;I)V
        //   236: goto +9 -> 245
        //   239: aload 20
        //   241: iconst_0
        //   242: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   245: aload 15
        //   247: ifnull +20 -> 267
        //   250: aload 20
        //   252: iconst_1
        //   253: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   256: aload 15
        //   258: aload 20
        //   260: iconst_0
        //   261: invokevirtual 91	android/content/res/CompatibilityInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   264: goto +9 -> 273
        //   267: aload 20
        //   269: iconst_0
        //   270: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   273: aload 20
        //   275: aload 16
        //   277: invokevirtual 95	android/os/Parcel:writeMap	(Ljava/util/Map;)V
        //   280: aload 17
        //   282: ifnull +20 -> 302
        //   285: aload 20
        //   287: iconst_1
        //   288: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   291: aload 17
        //   293: aload 20
        //   295: iconst_0
        //   296: invokevirtual 75	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   299: goto +9 -> 308
        //   302: aload 20
        //   304: iconst_0
        //   305: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   308: aload 20
        //   310: aload 18
        //   312: invokevirtual 40	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   315: aload 20
        //   317: iload 19
        //   319: invokevirtual 56	android/os/Parcel:writeInt	(I)V
        //   322: aload_0
        //   323: getfield 19	android/app/IApplicationThread$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   326: iconst_4
        //   327: aload 20
        //   329: aconst_null
        //   330: iconst_1
        //   331: invokeinterface 46 5 0
        //   336: pop
        //   337: aload 20
        //   339: invokevirtual 49	android/os/Parcel:recycle	()V
        //   342: return
        //   343: astore_1
        //   344: goto +20 -> 364
        //   347: astore_1
        //   348: goto +16 -> 364
        //   351: astore_1
        //   352: goto +12 -> 364
        //   355: astore_1
        //   356: goto +8 -> 364
        //   359: astore_1
        //   360: goto +4 -> 364
        //   363: astore_1
        //   364: aload 20
        //   366: invokevirtual 49	android/os/Parcel:recycle	()V
        //   369: aload_1
        //   370: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	371	0	this	Proxy
        //   0	371	1	paramString1	String
        //   0	371	2	paramApplicationInfo	ApplicationInfo
        //   0	371	3	paramList	List<ProviderInfo>
        //   0	371	4	paramComponentName	ComponentName
        //   0	371	5	paramProfilerInfo	ProfilerInfo
        //   0	371	6	paramBundle1	Bundle
        //   0	371	7	paramIInstrumentationWatcher	IInstrumentationWatcher
        //   0	371	8	paramIUiAutomationConnection	IUiAutomationConnection
        //   0	371	9	paramInt	int
        //   0	371	10	paramBoolean1	boolean
        //   0	371	11	paramBoolean2	boolean
        //   0	371	12	paramBoolean3	boolean
        //   0	371	13	paramBoolean4	boolean
        //   0	371	14	paramConfiguration	Configuration
        //   0	371	15	paramCompatibilityInfo	CompatibilityInfo
        //   0	371	16	paramMap	Map
        //   0	371	17	paramBundle2	Bundle
        //   0	371	18	paramString2	String
        //   0	371	19	paramBoolean5	boolean
        //   3	362	20	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   196	217	343	finally
        //   222	236	343	finally
        //   239	245	343	finally
        //   250	264	343	finally
        //   267	273	343	finally
        //   273	280	343	finally
        //   285	299	343	finally
        //   302	308	343	finally
        //   308	337	343	finally
        //   189	196	347	finally
        //   182	189	351	finally
        //   44	50	355	finally
        //   55	69	355	finally
        //   72	78	355	finally
        //   83	97	355	finally
        //   100	106	355	finally
        //   111	125	355	finally
        //   128	134	355	finally
        //   139	147	355	finally
        //   152	158	355	finally
        //   163	171	355	finally
        //   176	182	355	finally
        //   12	18	359	finally
        //   22	35	359	finally
        //   38	44	359	finally
        //   5	12	363	finally
      }
      
      public void clearDnsCache()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(26, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchPackageBroadcast(int paramInt, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramInt);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpActivity(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeString(paramString);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(25, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpDbInfo(ParcelFileDescriptor paramParcelFileDescriptor, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(35, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpGfxInfo(ParcelFileDescriptor paramParcelFileDescriptor, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(33, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpHeap(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramBoolean3);
          localParcel.writeString(paramString);
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
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
      
      public void dumpMemInfo(ParcelFileDescriptor paramParcelFileDescriptor, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramMemoryInfo != null)
          {
            localParcel.writeInt(1);
            paramMemoryInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramBoolean3);
          localParcel.writeInt(paramBoolean4);
          localParcel.writeInt(paramBoolean5);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(31, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpMemInfoProto(ParcelFileDescriptor paramParcelFileDescriptor, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramMemoryInfo != null)
          {
            localParcel.writeInt(1);
            paramMemoryInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramBoolean3);
          localParcel.writeInt(paramBoolean4);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(32, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpProvider(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dumpService(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeStringArray(paramArrayOfString);
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
        return "android.app.IApplicationThread";
      }
      
      public void handleTrustStorageUpdate()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(47, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyCleartextNetwork(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(43, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void processInBackground()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void profilerControl(boolean paramBoolean, ProfilerInfo paramProfilerInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramBoolean);
          if (paramProfilerInfo != null)
          {
            localParcel.writeInt(1);
            paramProfilerInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestAssistContextExtras(IBinder paramIBinder1, IBinder paramIBinder2, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder1);
          localParcel.writeStrongBinder(paramIBinder2);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(37, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void runIsolatedEntryPoint(String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeString(paramString);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleApplicationInfoChanged(ApplicationInfo paramApplicationInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramApplicationInfo != null)
          {
            localParcel.writeInt(1);
            paramApplicationInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(49, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleBindService(IBinder paramIBinder, Intent paramIntent, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleCrash(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeString(paramString);
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleCreateBackupAgent(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramApplicationInfo != null)
          {
            localParcel.writeInt(1);
            paramApplicationInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCompatibilityInfo != null)
          {
            localParcel.writeInt(1);
            paramCompatibilityInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleCreateService(IBinder paramIBinder, ServiceInfo paramServiceInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramServiceInfo != null)
          {
            localParcel.writeInt(1);
            paramServiceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCompatibilityInfo != null)
          {
            localParcel.writeInt(1);
            paramCompatibilityInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleDestroyBackupAgent(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramApplicationInfo != null)
          {
            localParcel.writeInt(1);
            paramApplicationInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCompatibilityInfo != null)
          {
            localParcel.writeInt(1);
            paramCompatibilityInfo.writeToParcel(localParcel, 0);
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
      
      public void scheduleEnterAnimationComplete(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(42, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleExit()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleInstallProvider(ProviderInfo paramProviderInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramProviderInfo != null)
          {
            localParcel.writeInt(1);
            paramProviderInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(40, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleLocalVoiceInteractionStarted(IBinder paramIBinder, IVoiceInteractor paramIVoiceInteractor)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramIVoiceInteractor != null) {
            paramIBinder = paramIVoiceInteractor.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(46, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleLowMemory()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleOnNewActivityOptions(IBinder paramIBinder, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void scheduleReceiver(Intent paramIntent, ActivityInfo paramActivityInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramActivityInfo != null)
          {
            localParcel.writeInt(1);
            paramActivityInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCompatibilityInfo != null)
          {
            localParcel.writeInt(1);
            paramCompatibilityInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
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
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleRegisteredReceiver(IIntentReceiver paramIIntentReceiver, Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramIIntentReceiver != null) {
            paramIIntentReceiver = paramIIntentReceiver.asBinder();
          } else {
            paramIIntentReceiver = null;
          }
          localParcel.writeStrongBinder(paramIIntentReceiver);
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
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
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleServiceArgs(IBinder paramIBinder, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
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
      
      public void scheduleSleeping(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleStopService(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleSuicide()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleTransaction(ClientTransaction paramClientTransaction)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramClientTransaction != null)
          {
            localParcel.writeInt(1);
            paramClientTransaction.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(51, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleTranslucentConversionComplete(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(38, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleTrimMemory(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramInt);
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void scheduleUnbindService(IBinder paramIBinder, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
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
      
      public void setCoreSettings(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void setHttpProxy(String paramString1, String paramString2, String paramString3, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
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
      
      public void setNetworkBlockSeq(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeLong(paramLong);
          mRemote.transact(50, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setProcessState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramInt);
          mRemote.transact(39, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSchedulingGroup(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramInt);
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startBinderTracking()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(44, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopBinderTrackingAndDump(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(45, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unstableProviderDied(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(36, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updatePackageCompatibilityInfo(String paramString, CompatibilityInfo paramCompatibilityInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeString(paramString);
          if (paramCompatibilityInfo != null)
          {
            localParcel.writeInt(1);
            paramCompatibilityInfo.writeToParcel(localParcel, 0);
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
      
      public void updateTimePrefs(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          localParcel.writeInt(paramInt);
          mRemote.transact(41, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateTimeZone()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IApplicationThread");
          mRemote.transact(8, localParcel, null, 1);
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
