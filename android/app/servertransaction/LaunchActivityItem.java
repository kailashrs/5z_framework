package android.app.servertransaction;

import android.app.ActivityThread.ActivityClientRecord;
import android.app.ClientTransactionHandler;
import android.app.ProfilerInfo;
import android.app.ResultInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.Trace;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractor.Stub;
import com.android.internal.content.ReferrerIntent;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LaunchActivityItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<LaunchActivityItem> CREATOR = new Parcelable.Creator()
  {
    public LaunchActivityItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LaunchActivityItem(paramAnonymousParcel, null);
    }
    
    public LaunchActivityItem[] newArray(int paramAnonymousInt)
    {
      return new LaunchActivityItem[paramAnonymousInt];
    }
  };
  private CompatibilityInfo mCompatInfo;
  private Configuration mCurConfig;
  private int mIdent;
  private ActivityInfo mInfo;
  private Intent mIntent;
  private boolean mIsForward;
  private Configuration mOverrideConfig;
  private List<ReferrerIntent> mPendingNewIntents;
  private List<ResultInfo> mPendingResults;
  private PersistableBundle mPersistentState;
  private int mProcState;
  private ProfilerInfo mProfilerInfo;
  private String mReferrer;
  private Bundle mState;
  private IVoiceInteractor mVoiceInteractor;
  
  private LaunchActivityItem() {}
  
  private LaunchActivityItem(Parcel paramParcel)
  {
    setValues(this, (Intent)paramParcel.readTypedObject(Intent.CREATOR), paramParcel.readInt(), (ActivityInfo)paramParcel.readTypedObject(ActivityInfo.CREATOR), (Configuration)paramParcel.readTypedObject(Configuration.CREATOR), (Configuration)paramParcel.readTypedObject(Configuration.CREATOR), (CompatibilityInfo)paramParcel.readTypedObject(CompatibilityInfo.CREATOR), paramParcel.readString(), IVoiceInteractor.Stub.asInterface(paramParcel.readStrongBinder()), paramParcel.readInt(), paramParcel.readBundle(getClass().getClassLoader()), paramParcel.readPersistableBundle(getClass().getClassLoader()), paramParcel.createTypedArrayList(ResultInfo.CREATOR), paramParcel.createTypedArrayList(ReferrerIntent.CREATOR), paramParcel.readBoolean(), (ProfilerInfo)paramParcel.readTypedObject(ProfilerInfo.CREATOR));
  }
  
  private boolean activityInfoEqual(ActivityInfo paramActivityInfo)
  {
    ActivityInfo localActivityInfo = mInfo;
    boolean bool1 = false;
    boolean bool2 = false;
    if (localActivityInfo == null)
    {
      if (paramActivityInfo == null) {
        bool2 = true;
      }
      return bool2;
    }
    if ((paramActivityInfo != null) && (mInfo.flags == flags) && (mInfo.maxAspectRatio == maxAspectRatio) && (Objects.equals(mInfo.launchToken, launchToken)) && (Objects.equals(mInfo.getComponentName(), paramActivityInfo.getComponentName()))) {
      bool2 = true;
    } else {
      bool2 = bool1;
    }
    return bool2;
  }
  
  private static boolean areBundlesEqual(BaseBundle paramBaseBundle1, BaseBundle paramBaseBundle2)
  {
    boolean bool = true;
    if ((paramBaseBundle1 != null) && (paramBaseBundle2 != null))
    {
      if (paramBaseBundle1.size() != paramBaseBundle2.size()) {
        return false;
      }
      Iterator localIterator = paramBaseBundle1.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((str != null) && (!Objects.equals(paramBaseBundle1.get(str), paramBaseBundle2.get(str)))) {
          return false;
        }
      }
      return true;
    }
    if (paramBaseBundle1 != paramBaseBundle2) {
      bool = false;
    }
    return bool;
  }
  
  public static LaunchActivityItem obtain(Intent paramIntent, int paramInt1, ActivityInfo paramActivityInfo, Configuration paramConfiguration1, Configuration paramConfiguration2, CompatibilityInfo paramCompatibilityInfo, String paramString, IVoiceInteractor paramIVoiceInteractor, int paramInt2, Bundle paramBundle, PersistableBundle paramPersistableBundle, List<ResultInfo> paramList, List<ReferrerIntent> paramList1, boolean paramBoolean, ProfilerInfo paramProfilerInfo)
  {
    LaunchActivityItem localLaunchActivityItem1 = (LaunchActivityItem)ObjectPool.obtain(LaunchActivityItem.class);
    LaunchActivityItem localLaunchActivityItem2 = localLaunchActivityItem1;
    if (localLaunchActivityItem1 == null) {
      localLaunchActivityItem2 = new LaunchActivityItem();
    }
    setValues(localLaunchActivityItem2, paramIntent, paramInt1, paramActivityInfo, paramConfiguration1, paramConfiguration2, paramCompatibilityInfo, paramString, paramIVoiceInteractor, paramInt2, paramBundle, paramPersistableBundle, paramList, paramList1, paramBoolean, paramProfilerInfo);
    return localLaunchActivityItem2;
  }
  
  private static void setValues(LaunchActivityItem paramLaunchActivityItem, Intent paramIntent, int paramInt1, ActivityInfo paramActivityInfo, Configuration paramConfiguration1, Configuration paramConfiguration2, CompatibilityInfo paramCompatibilityInfo, String paramString, IVoiceInteractor paramIVoiceInteractor, int paramInt2, Bundle paramBundle, PersistableBundle paramPersistableBundle, List<ResultInfo> paramList, List<ReferrerIntent> paramList1, boolean paramBoolean, ProfilerInfo paramProfilerInfo)
  {
    mIntent = paramIntent;
    mIdent = paramInt1;
    mInfo = paramActivityInfo;
    mCurConfig = paramConfiguration1;
    mOverrideConfig = paramConfiguration2;
    mCompatInfo = paramCompatibilityInfo;
    mReferrer = paramString;
    mVoiceInteractor = paramIVoiceInteractor;
    mProcState = paramInt2;
    mState = paramBundle;
    mPersistentState = paramPersistableBundle;
    mPendingResults = paramList;
    mPendingNewIntents = paramList1;
    mIsForward = paramBoolean;
    mProfilerInfo = paramProfilerInfo;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (LaunchActivityItem)paramObject;
      int i;
      if (((mIntent == null) && (mIntent == null)) || ((mIntent != null) && (mIntent.filterEquals(mIntent)))) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i == 0) || (mIdent != mIdent) || (!activityInfoEqual(mInfo)) || (!Objects.equals(mCurConfig, mCurConfig)) || (!Objects.equals(mOverrideConfig, mOverrideConfig)) || (!Objects.equals(mCompatInfo, mCompatInfo)) || (!Objects.equals(mReferrer, mReferrer)) || (mProcState != mProcState) || (!areBundlesEqual(mState, mState)) || (!areBundlesEqual(mPersistentState, mPersistentState)) || (!Objects.equals(mPendingResults, mPendingResults)) || (!Objects.equals(mPendingNewIntents, mPendingNewIntents)) || (mIsForward != mIsForward) || (!Objects.equals(mProfilerInfo, mProfilerInfo))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityStart");
    paramClientTransactionHandler.handleLaunchActivity(new ActivityThread.ActivityClientRecord(paramIBinder, mIntent, mIdent, mInfo, mOverrideConfig, mCompatInfo, mReferrer, mVoiceInteractor, mState, mPersistentState, mPendingResults, mPendingNewIntents, mIsForward, mProfilerInfo, paramClientTransactionHandler), paramPendingTransactionActions, null);
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    int i = mIntent.filterHashCode();
    int j = mIdent;
    int k = Objects.hashCode(mCurConfig);
    int m = Objects.hashCode(mOverrideConfig);
    int n = Objects.hashCode(mCompatInfo);
    int i1 = Objects.hashCode(mReferrer);
    int i2 = Objects.hashCode(Integer.valueOf(mProcState));
    Bundle localBundle = mState;
    int i3 = 0;
    int i4;
    if (localBundle != null) {
      i4 = mState.size();
    } else {
      i4 = 0;
    }
    if (mPersistentState != null) {
      i3 = mPersistentState.size();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 17 + i) + j) + k) + m) + n) + i1) + i2) + i4) + i3) + Objects.hashCode(mPendingResults)) + Objects.hashCode(mPendingNewIntents)) + mIsForward) + Objects.hashCode(mProfilerInfo);
  }
  
  public void preExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder)
  {
    paramClientTransactionHandler.updateProcessState(mProcState, false);
    paramClientTransactionHandler.updatePendingConfiguration(mCurConfig);
  }
  
  public void recycle()
  {
    setValues(this, null, 0, null, null, null, null, null, null, 0, null, null, null, null, false, null);
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("LaunchActivityItem{intent=");
    localStringBuilder.append(mIntent);
    localStringBuilder.append(",ident=");
    localStringBuilder.append(mIdent);
    localStringBuilder.append(",info=");
    localStringBuilder.append(mInfo);
    localStringBuilder.append(",curConfig=");
    localStringBuilder.append(mCurConfig);
    localStringBuilder.append(",overrideConfig=");
    localStringBuilder.append(mOverrideConfig);
    localStringBuilder.append(",referrer=");
    localStringBuilder.append(mReferrer);
    localStringBuilder.append(",procState=");
    localStringBuilder.append(mProcState);
    localStringBuilder.append(",state=");
    localStringBuilder.append(mState);
    localStringBuilder.append(",persistentState=");
    localStringBuilder.append(mPersistentState);
    localStringBuilder.append(",pendingResults=");
    localStringBuilder.append(mPendingResults);
    localStringBuilder.append(",pendingNewIntents=");
    localStringBuilder.append(mPendingNewIntents);
    localStringBuilder.append(",profilerInfo=");
    localStringBuilder.append(mProfilerInfo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(mIntent, paramInt);
    paramParcel.writeInt(mIdent);
    paramParcel.writeTypedObject(mInfo, paramInt);
    paramParcel.writeTypedObject(mCurConfig, paramInt);
    paramParcel.writeTypedObject(mOverrideConfig, paramInt);
    paramParcel.writeTypedObject(mCompatInfo, paramInt);
    paramParcel.writeString(mReferrer);
    paramParcel.writeStrongInterface(mVoiceInteractor);
    paramParcel.writeInt(mProcState);
    paramParcel.writeBundle(mState);
    paramParcel.writePersistableBundle(mPersistentState);
    paramParcel.writeTypedList(mPendingResults, paramInt);
    paramParcel.writeTypedList(mPendingNewIntents, paramInt);
    paramParcel.writeBoolean(mIsForward);
    paramParcel.writeTypedObject(mProfilerInfo, paramInt);
  }
}
