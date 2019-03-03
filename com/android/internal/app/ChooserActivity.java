package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.service.chooser.ChooserTarget;
import android.service.chooser.IChooserTargetResult;
import android.service.chooser.IChooserTargetResult.Stub;
import android.service.chooser.IChooserTargetService;
import android.service.chooser.IChooserTargetService.Stub;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Space;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.widget.ResolverDrawerLayout;
import com.google.android.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class ChooserActivity
  extends ResolverActivity
{
  private static final float CALLER_TARGET_SCORE_BOOST = 900.0F;
  private static final int CHOOSER_TARGET_SERVICE_RESULT = 1;
  private static final int CHOOSER_TARGET_SERVICE_WATCHDOG_TIMEOUT = 2;
  private static final boolean DEBUG = false;
  public static final String EXTRA_PRIVATE_RETAIN_IN_ON_STOP = "com.android.internal.app.ChooserActivity.EXTRA_PRIVATE_RETAIN_IN_ON_STOP";
  private static final String PINNED_SHARED_PREFS_NAME = "chooser_pin_settings";
  private static final float PINNED_TARGET_SCORE_BOOST = 1000.0F;
  private static final int QUERY_TARGET_SERVICE_LIMIT = 5;
  private static final String TAG = "ChooserActivity";
  private static final String TARGET_DETAILS_FRAGMENT_TAG = "targetDetailsFragment";
  private static final int WATCHDOG_TIMEOUT_MILLIS = 2000;
  private ChooserTarget[] mCallerChooserTargets;
  private final Handler mChooserHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        super.handleMessage(paramAnonymousMessage);
        break;
      case 2: 
        unbindRemainingServices();
        sendVoiceChoicesIfNeeded();
        mChooserListAdapter.setShowServiceTargets(true);
        break;
      case 1: 
        if (!isDestroyed())
        {
          ChooserActivity.ServiceResultInfo localServiceResultInfo = (ChooserActivity.ServiceResultInfo)obj;
          if (!mServiceConnections.contains(connection))
          {
            paramAnonymousMessage = new StringBuilder();
            paramAnonymousMessage.append("ChooserTargetServiceConnection ");
            paramAnonymousMessage.append(connection);
            paramAnonymousMessage.append(" returned after being removed from active connections. Have you considered returning results faster?");
            Log.w("ChooserActivity", paramAnonymousMessage.toString());
          }
          else
          {
            if (resultTargets != null) {
              mChooserListAdapter.addServiceResults(originalTarget, resultTargets);
            }
            unbindService(connection);
            connection.destroy();
            mServiceConnections.remove(connection);
            if (mServiceConnections.isEmpty())
            {
              mChooserHandler.removeMessages(2);
              sendVoiceChoicesIfNeeded();
              mChooserListAdapter.setShowServiceTargets(true);
            }
          }
        }
        break;
      }
    }
  };
  private ChooserListAdapter mChooserListAdapter;
  private ChooserRowAdapter mChooserRowAdapter;
  private long mChooserShownTime;
  private IntentSender mChosenComponentSender;
  private ComponentName[] mFilteredComponentNames;
  protected boolean mIsSuccessfullySelected;
  private SharedPreferences mPinnedSharedPrefs;
  private Intent mReferrerFillInIntent;
  private IntentSender mRefinementIntentSender;
  private RefinementResultReceiver mRefinementResultReceiver;
  private Bundle mReplacementExtras;
  private final List<ChooserTargetServiceConnection> mServiceConnections = new ArrayList();
  
  public ChooserActivity() {}
  
  private String convertServiceName(String paramString1, String paramString2)
  {
    boolean bool = TextUtils.isEmpty(paramString2);
    StringBuilder localStringBuilder = null;
    if (bool) {
      return null;
    }
    if (paramString2.startsWith("."))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append(paramString2);
      paramString1 = localStringBuilder.toString();
    }
    for (;;)
    {
      break;
      if (paramString2.indexOf('.') >= 0) {
        paramString1 = paramString2;
      } else {
        paramString1 = localStringBuilder;
      }
    }
    return paramString1;
  }
  
  static SharedPreferences getPinnedSharedPrefs(Context paramContext)
  {
    return paramContext.getSharedPreferences(new File(new File(Environment.getDataUserCePackageDirectory(StorageManager.UUID_PRIVATE_INTERNAL, paramContext.getUserId(), paramContext.getPackageName()), "shared_prefs"), "chooser_pin_settings.xml"), 0);
  }
  
  private void modifyTargetIntent(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    if (("android.intent.action.SEND".equals(str)) || ("android.intent.action.SEND_MULTIPLE".equals(str))) {
      paramIntent.addFlags(134742016);
    }
  }
  
  boolean checkTargetSourceIntent(ResolverActivity.TargetInfo paramTargetInfo, Intent paramIntent)
  {
    paramTargetInfo = paramTargetInfo.getAllSourceIntents();
    int i = 0;
    int j = paramTargetInfo.size();
    while (i < j)
    {
      if (((Intent)paramTargetInfo.get(i)).filterEquals(paramIntent)) {
        return true;
      }
      i++;
    }
    return false;
  }
  
  public ResolverActivity.ResolveListAdapter createAdapter(Context paramContext, List<Intent> paramList, Intent[] paramArrayOfIntent, List<ResolveInfo> paramList1, int paramInt, boolean paramBoolean)
  {
    return new ChooserListAdapter(paramContext, paramList, paramArrayOfIntent, paramList1, paramInt, paramBoolean, createListController());
  }
  
  @VisibleForTesting
  protected ResolverListController createListController()
  {
    return new ChooserListController(this, mPm, getTargetIntent(), getReferrerPackageName(), mLaunchedFromUid);
  }
  
  void filterServiceTargets(String paramString, List<ChooserTarget> paramList)
  {
    if (paramList == null) {
      return;
    }
    PackageManager localPackageManager = getPackageManager();
    for (int i = paramList.size() - 1; i >= 0; i--)
    {
      ChooserTarget localChooserTarget = (ChooserTarget)paramList.get(i);
      Object localObject = localChooserTarget.getComponentName();
      if ((paramString == null) || (!paramString.equals(((ComponentName)localObject).getPackageName())))
      {
        int j = 0;
        try
        {
          localObject = localPackageManager.getActivityInfo((ComponentName)localObject, 0);
          if (exported)
          {
            localObject = permission;
            if (localObject == null) {
              break label105;
            }
          }
          j = 1;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          label105:
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Target ");
          localStringBuilder.append(localChooserTarget);
          localStringBuilder.append(" returned by ");
          localStringBuilder.append(paramString);
          localStringBuilder.append(" component not found");
          Log.e("ChooserActivity", localStringBuilder.toString());
          j = 1;
        }
        if (j != 0) {
          paramList.remove(i);
        }
      }
    }
  }
  
  public int getLayoutResource()
  {
    return 17367146;
  }
  
  public Intent getReplacementIntent(ActivityInfo paramActivityInfo, Intent paramIntent)
  {
    Intent localIntent1 = paramIntent;
    Intent localIntent2 = localIntent1;
    if (mReplacementExtras != null)
    {
      Bundle localBundle = mReplacementExtras.getBundle(packageName);
      localIntent2 = localIntent1;
      if (localBundle != null)
      {
        localIntent2 = new Intent(paramIntent);
        localIntent2.putExtras(localBundle);
      }
    }
    if (!name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_PARENT))
    {
      paramIntent = localIntent2;
      if (!name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE)) {}
    }
    else
    {
      paramIntent = Intent.createChooser(localIntent2, getIntent().getCharSequenceExtra("android.intent.extra.TITLE"));
      paramIntent.putExtra("android.intent.extra.AUTO_LAUNCH_SINGLE_CHOICE", false);
    }
    return paramIntent;
  }
  
  public void onActivityStarted(ResolverActivity.TargetInfo paramTargetInfo)
  {
    if (mChosenComponentSender != null)
    {
      paramTargetInfo = paramTargetInfo.getResolvedComponentName();
      if (paramTargetInfo != null)
      {
        paramTargetInfo = new Intent().putExtra("android.intent.extra.CHOSEN_COMPONENT", paramTargetInfo);
        try
        {
          mChosenComponentSender.sendIntent(this, -1, paramTargetInfo, null, null);
        }
        catch (IntentSender.SendIntentException paramTargetInfo)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unable to launch supplied IntentSender to report the chosen component: ");
          localStringBuilder.append(paramTargetInfo);
          Slog.e("ChooserActivity", localStringBuilder.toString());
        }
      }
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    long l = System.currentTimeMillis();
    mIsSuccessfullySelected = false;
    Intent localIntent = getIntent();
    Object localObject1 = localIntent.getParcelableExtra("android.intent.extra.INTENT");
    if (!(localObject1 instanceof Intent))
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Target is not an intent: ");
      paramBundle.append(localObject1);
      Log.w("ChooserActivity", paramBundle.toString());
      finish();
      super.onCreate(null);
      return;
    }
    localObject1 = (Intent)localObject1;
    if (localObject1 != null) {
      modifyTargetIntent((Intent)localObject1);
    }
    Object localObject2 = localIntent.getParcelableArrayExtra("android.intent.extra.ALTERNATE_INTENTS");
    int i;
    if (localObject2 != null)
    {
      if (localObject1 == null) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        j = localObject2.length - 1;
      } else {
        j = localObject2.length;
      }
      localObject3 = new Intent[j];
      for (j = 0; j < localObject2.length; j++)
      {
        if (!(localObject2[j] instanceof Intent))
        {
          paramBundle = new StringBuilder();
          paramBundle.append("EXTRA_ALTERNATE_INTENTS array entry #");
          paramBundle.append(j);
          paramBundle.append(" is not an Intent: ");
          paramBundle.append(localObject2[j]);
          Log.w("ChooserActivity", paramBundle.toString());
          finish();
          super.onCreate(null);
          return;
        }
        localObject4 = (Intent)localObject2[j];
        if ((j == 0) && (localObject1 == null))
        {
          localObject1 = localObject4;
          modifyTargetIntent((Intent)localObject1);
        }
        else
        {
          int k;
          if (i != 0) {
            k = j - 1;
          } else {
            k = j;
          }
          localObject3[k] = localObject4;
          modifyTargetIntent((Intent)localObject4);
        }
      }
      setAdditionalTargets((Intent[])localObject3);
    }
    mReplacementExtras = localIntent.getBundleExtra("android.intent.extra.REPLACEMENT_EXTRAS");
    CharSequence localCharSequence = localIntent.getCharSequenceExtra("android.intent.extra.TITLE");
    int j = 0;
    if (localCharSequence == null) {
      j = 17039651;
    }
    Object localObject3 = localIntent.getParcelableArrayExtra("android.intent.extra.INITIAL_INTENTS");
    Object localObject4 = null;
    if (localObject3 != null)
    {
      localObject2 = new Intent[localObject3.length];
      for (i = 0;; i++)
      {
        localObject4 = localObject2;
        if (i >= localObject3.length) {
          break;
        }
        if (!(localObject3[i] instanceof Intent))
        {
          paramBundle = new StringBuilder();
          paramBundle.append("Initial intent #");
          paramBundle.append(i);
          paramBundle.append(" not an Intent: ");
          paramBundle.append(localObject3[i]);
          Log.w("ChooserActivity", paramBundle.toString());
          finish();
          super.onCreate(null);
          return;
        }
        localObject4 = (Intent)localObject3[i];
        modifyTargetIntent((Intent)localObject4);
        localObject2[i] = localObject4;
      }
    }
    mReferrerFillInIntent = new Intent().putExtra("android.intent.extra.REFERRER", getReferrer());
    mChosenComponentSender = ((IntentSender)localIntent.getParcelableExtra("android.intent.extra.CHOSEN_COMPONENT_INTENT_SENDER"));
    mRefinementIntentSender = ((IntentSender)localIntent.getParcelableExtra("android.intent.extra.CHOOSER_REFINEMENT_INTENT_SENDER"));
    setSafeForwardingMode(true);
    Parcelable[] arrayOfParcelable = localIntent.getParcelableArrayExtra("android.intent.extra.EXCLUDE_COMPONENTS");
    if (arrayOfParcelable != null)
    {
      localObject3 = new ComponentName[arrayOfParcelable.length];
      for (i = 0;; i++)
      {
        localObject2 = localObject3;
        if (i >= arrayOfParcelable.length) {
          break;
        }
        if (!(arrayOfParcelable[i] instanceof ComponentName))
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Filtered component #");
          ((StringBuilder)localObject2).append(i);
          ((StringBuilder)localObject2).append(" not a ComponentName: ");
          ((StringBuilder)localObject2).append(arrayOfParcelable[i]);
          Log.w("ChooserActivity", ((StringBuilder)localObject2).toString());
          localObject2 = null;
          break;
        }
        localObject3[i] = ((ComponentName)arrayOfParcelable[i]);
      }
      mFilteredComponentNames = ((ComponentName[])localObject2);
    }
    arrayOfParcelable = localIntent.getParcelableArrayExtra("android.intent.extra.CHOOSER_TARGETS");
    if (arrayOfParcelable != null)
    {
      localObject3 = new ChooserTarget[arrayOfParcelable.length];
      for (i = 0;; i++)
      {
        localObject2 = localObject3;
        if (i >= arrayOfParcelable.length) {
          break;
        }
        if (!(arrayOfParcelable[i] instanceof ChooserTarget))
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Chooser target #");
          ((StringBuilder)localObject2).append(i);
          ((StringBuilder)localObject2).append(" not a ChooserTarget: ");
          ((StringBuilder)localObject2).append(arrayOfParcelable[i]);
          Log.w("ChooserActivity", ((StringBuilder)localObject2).toString());
          localObject2 = null;
          break;
        }
        localObject3[i] = ((ChooserTarget)arrayOfParcelable[i]);
      }
      mCallerChooserTargets = ((ChooserTarget[])localObject2);
    }
    mPinnedSharedPrefs = getPinnedSharedPrefs(this);
    setRetainInOnStop(localIntent.getBooleanExtra("com.android.internal.app.ChooserActivity.EXTRA_PRIVATE_RETAIN_IN_ON_STOP", false));
    super.onCreate(paramBundle, (Intent)localObject1, localCharSequence, j, (Intent[])localObject4, null, false);
    MetricsLogger.action(this, 214);
    mChooserShownTime = System.currentTimeMillis();
    MetricsLogger.histogram(null, "system_cost_for_smart_sharing", (int)(mChooserShownTime - l));
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    if (mRefinementResultReceiver != null)
    {
      mRefinementResultReceiver.destroy();
      mRefinementResultReceiver = null;
    }
    unbindRemainingServices();
    mChooserHandler.removeMessages(1);
  }
  
  public void onPrepareAdapterView(AbsListView paramAbsListView, ResolverActivity.ResolveListAdapter paramResolveListAdapter)
  {
    ListView localListView;
    if ((paramAbsListView instanceof ListView)) {
      localListView = (ListView)paramAbsListView;
    } else {
      localListView = null;
    }
    mChooserListAdapter = ((ChooserListAdapter)paramResolveListAdapter);
    if ((mCallerChooserTargets != null) && (mCallerChooserTargets.length > 0)) {
      mChooserListAdapter.addServiceResults(null, Lists.newArrayList(mCallerChooserTargets));
    }
    mChooserRowAdapter = new ChooserRowAdapter(mChooserListAdapter);
    mChooserRowAdapter.registerDataSetObserver(new OffsetDataSetObserver(paramAbsListView));
    paramAbsListView.setAdapter(mChooserRowAdapter);
    if (localListView != null) {
      localListView.setItemsCanFocus(true);
    }
  }
  
  void onRefinementCanceled()
  {
    if (mRefinementResultReceiver != null)
    {
      mRefinementResultReceiver.destroy();
      mRefinementResultReceiver = null;
    }
    finish();
  }
  
  void onRefinementResult(ResolverActivity.TargetInfo paramTargetInfo, Intent paramIntent)
  {
    if (mRefinementResultReceiver != null)
    {
      mRefinementResultReceiver.destroy();
      mRefinementResultReceiver = null;
    }
    if (paramTargetInfo == null)
    {
      Log.e("ChooserActivity", "Refinement result intent did not match any known targets; canceling");
    }
    else if (!checkTargetSourceIntent(paramTargetInfo, paramIntent))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onRefinementResult: Selected target ");
      localStringBuilder.append(paramTargetInfo);
      localStringBuilder.append(" cannot match refined source intent ");
      localStringBuilder.append(paramIntent);
      Log.e("ChooserActivity", localStringBuilder.toString());
    }
    else
    {
      paramTargetInfo = paramTargetInfo.cloneFilledIn(paramIntent, 0);
      if (super.onTargetSelected(paramTargetInfo, false))
      {
        updateModelAndChooserCounts(paramTargetInfo);
        finish();
        return;
      }
    }
    onRefinementCanceled();
  }
  
  public void onSetupVoiceInteraction() {}
  
  protected boolean onTargetSelected(ResolverActivity.TargetInfo paramTargetInfo, boolean paramBoolean)
  {
    if (mRefinementIntentSender != null)
    {
      Intent localIntent = new Intent();
      List localList = paramTargetInfo.getAllSourceIntents();
      if (!localList.isEmpty())
      {
        localIntent.putExtra("android.intent.extra.INTENT", (Parcelable)localList.get(0));
        if (localList.size() > 1)
        {
          Intent[] arrayOfIntent = new Intent[localList.size() - 1];
          int i = 1;
          int j = localList.size();
          while (i < j)
          {
            arrayOfIntent[(i - 1)] = ((Intent)localList.get(i));
            i++;
          }
          localIntent.putExtra("android.intent.extra.ALTERNATE_INTENTS", arrayOfIntent);
        }
        if (mRefinementResultReceiver != null) {
          mRefinementResultReceiver.destroy();
        }
        mRefinementResultReceiver = new RefinementResultReceiver(this, paramTargetInfo, null);
        localIntent.putExtra("android.intent.extra.RESULT_RECEIVER", mRefinementResultReceiver);
        try
        {
          mRefinementIntentSender.sendIntent(this, 0, localIntent, null, null);
          return false;
        }
        catch (IntentSender.SendIntentException localSendIntentException)
        {
          Log.e("ChooserActivity", "Refinement IntentSender failed to send", localSendIntentException);
        }
      }
    }
    updateModelAndChooserCounts(paramTargetInfo);
    return super.onTargetSelected(paramTargetInfo, paramBoolean);
  }
  
  void queryTargetServices(ChooserListAdapter paramChooserListAdapter)
  {
    PackageManager localPackageManager = getPackageManager();
    int i = 0;
    int j = 0;
    int k = paramChooserListAdapter.getDisplayResolveInfoCount();
    while (j < k)
    {
      ResolverActivity.DisplayResolveInfo localDisplayResolveInfo = paramChooserListAdapter.getDisplayResolveInfo(j);
      if (paramChooserListAdapter.getScore(localDisplayResolveInfo) != 0.0F)
      {
        Object localObject1 = getResolveInfoactivityInfo;
        Object localObject2 = metaData;
        if (localObject2 != null) {
          localObject2 = convertServiceName(packageName, ((Bundle)localObject2).getString("android.service.chooser.chooser_target_service"));
        } else {
          localObject2 = null;
        }
        int m = i;
        if (localObject2 != null)
        {
          localObject2 = new ComponentName(packageName, (String)localObject2);
          Intent localIntent = new Intent("android.service.chooser.ChooserTargetService").setComponent((ComponentName)localObject2);
          try
          {
            if (!"android.permission.BIND_CHOOSER_TARGET_SERVICE".equals(getServiceInfo0permission))
            {
              localObject1 = new java/lang/StringBuilder;
              ((StringBuilder)localObject1).<init>();
              ((StringBuilder)localObject1).append("ChooserTargetService ");
              ((StringBuilder)localObject1).append(localObject2);
              ((StringBuilder)localObject1).append(" does not require permission ");
              ((StringBuilder)localObject1).append("android.permission.BIND_CHOOSER_TARGET_SERVICE");
              ((StringBuilder)localObject1).append(" - this service will not be queried for ChooserTargets. add android:permission=\"");
              ((StringBuilder)localObject1).append("android.permission.BIND_CHOOSER_TARGET_SERVICE");
              ((StringBuilder)localObject1).append("\" to the <service> tag for ");
              ((StringBuilder)localObject1).append(localObject2);
              ((StringBuilder)localObject1).append(" in the manifest.");
              Log.w("ChooserActivity", ((StringBuilder)localObject1).toString());
              break label436;
            }
            localObject2 = new ChooserTargetServiceConnection(this, localDisplayResolveInfo);
            if ((mEnableTwinApps) && (mTwinAppsId != -1))
            {
              m = i;
              if (bindServiceAsUser(localIntent, (ServiceConnection)localObject2, 5, new UserHandle(UserHandle.getUserId(applicationInfo.uid))))
              {
                mServiceConnections.add(localObject2);
                m = i + 1;
              }
            }
            else
            {
              m = i;
              if (bindServiceAsUser(localIntent, (ServiceConnection)localObject2, 5, Process.myUserHandle()))
              {
                mServiceConnections.add(localObject2);
                m = i + 1;
              }
            }
          }
          catch (PackageManager.NameNotFoundException localNameNotFoundException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Could not look up service ");
            localStringBuilder.append(localObject2);
            localStringBuilder.append("; component name not found");
            Log.e("ChooserActivity", localStringBuilder.toString());
            break label436;
          }
        }
        i = m;
        if (m >= 5) {
          break;
        }
      }
      label436:
      j++;
    }
    if (!mServiceConnections.isEmpty()) {
      mChooserHandler.sendEmptyMessageDelayed(2, 2000L);
    } else {
      sendVoiceChoicesIfNeeded();
    }
  }
  
  public boolean shouldAutoLaunchSingleChoice(ResolverActivity.TargetInfo paramTargetInfo)
  {
    return getIntent().getBooleanExtra("android.intent.extra.AUTO_LAUNCH_SINGLE_CHOICE", super.shouldAutoLaunchSingleChoice(paramTargetInfo));
  }
  
  public boolean shouldGetActivityMetadata()
  {
    return true;
  }
  
  public void showTargetDetails(ResolveInfo paramResolveInfo)
  {
    if (paramResolveInfo == null) {
      return;
    }
    ComponentName localComponentName = activityInfo.getComponentName();
    boolean bool = mPinnedSharedPrefs.getBoolean(localComponentName.flattenToString(), false);
    new ResolverTargetActionsDialogFragment(paramResolveInfo.loadLabel(getPackageManager()), localComponentName, bool).show(getFragmentManager(), "targetDetailsFragment");
  }
  
  public void startSelected(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    long l1 = System.currentTimeMillis();
    long l2 = mChooserShownTime;
    super.startSelected(paramInt, paramBoolean1, paramBoolean2);
    if (mChooserListAdapter != null)
    {
      int i = 0;
      int j = paramInt;
      switch (mChooserListAdapter.getPositionTargetType(paramInt))
      {
      default: 
        paramInt = i;
        break;
      case 2: 
        paramInt = 217;
        j -= mChooserListAdapter.getCallerTargetCount() + mChooserListAdapter.getServiceTargetCount();
        break;
      case 1: 
        paramInt = 216;
        j -= mChooserListAdapter.getCallerTargetCount();
        break;
      case 0: 
        paramInt = 215;
      }
      if (paramInt != 0) {
        MetricsLogger.action(this, paramInt, j);
      }
      if (mIsSuccessfullySelected)
      {
        MetricsLogger.histogram(null, "user_selection_cost_for_smart_sharing", (int)(l1 - l2));
        MetricsLogger.histogram(null, "app_position_for_smart_sharing", j);
      }
    }
  }
  
  void unbindRemainingServices()
  {
    int i = 0;
    int j = mServiceConnections.size();
    while (i < j)
    {
      ChooserTargetServiceConnection localChooserTargetServiceConnection = (ChooserTargetServiceConnection)mServiceConnections.get(i);
      unbindService(localChooserTargetServiceConnection);
      localChooserTargetServiceConnection.destroy();
      i++;
    }
    mServiceConnections.clear();
    mChooserHandler.removeMessages(2);
  }
  
  void updateModelAndChooserCounts(ResolverActivity.TargetInfo paramTargetInfo)
  {
    if (paramTargetInfo != null)
    {
      ResolveInfo localResolveInfo = paramTargetInfo.getResolveInfo();
      Intent localIntent = getTargetIntent();
      if ((localResolveInfo != null) && (activityInfo != null) && (localIntent != null) && (mAdapter != null))
      {
        mAdapter.updateModel(paramTargetInfo.getResolvedComponentName());
        mAdapter.updateChooserCounts(activityInfo.packageName, getUserId(), localIntent.getAction());
      }
    }
    mIsSuccessfullySelected = true;
  }
  
  static class BaseChooserTargetComparator
    implements Comparator<ChooserTarget>
  {
    BaseChooserTargetComparator() {}
    
    public int compare(ChooserTarget paramChooserTarget1, ChooserTarget paramChooserTarget2)
    {
      return (int)Math.signum(paramChooserTarget2.getScore() - paramChooserTarget1.getScore());
    }
  }
  
  public class ChooserListAdapter
    extends ResolverActivity.ResolveListAdapter
  {
    private static final int MAX_SERVICE_TARGETS = 4;
    private static final int MAX_TARGETS_PER_SERVICE = 2;
    public static final int TARGET_BAD = -1;
    public static final int TARGET_CALLER = 0;
    public static final int TARGET_SERVICE = 1;
    public static final int TARGET_STANDARD = 2;
    private final ChooserActivity.BaseChooserTargetComparator mBaseTargetComparator = new ChooserActivity.BaseChooserTargetComparator();
    private final List<ResolverActivity.TargetInfo> mCallerTargets = new ArrayList();
    private float mLateFee = 1.0F;
    private final List<ChooserActivity.ChooserTargetInfo> mServiceTargets = new ArrayList();
    private boolean mShowServiceTargets;
    private boolean mTargetsNeedPruning = false;
    
    public ChooserListAdapter(List<Intent> paramList, Intent[] paramArrayOfIntent, List<ResolveInfo> paramList1, int paramInt, boolean paramBoolean, ResolverListController paramResolverListController)
    {
      super(paramList, paramArrayOfIntent, null, paramInt, paramBoolean, paramResolverListController, (ResolverListController)localObject1);
      if (paramList1 != null)
      {
        PackageManager localPackageManager = getPackageManager();
        UserManager localUserManager = (UserManager)getSystemService("user");
        HashSet localHashSet = new HashSet();
        IPackageManager localIPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        paramBoolean = localUserManager.getTwinAppsId();
        for (int i = 0; i < paramList1.length; i++)
        {
          Intent localIntent = paramList1[i];
          if (localIntent != null)
          {
            Object localObject2;
            for (;;)
            {
              localObject1 = null;
              paramArrayOfIntent = null;
              ResolveInfo localResolveInfo = null;
              Object localObject3 = null;
              paramInt = null;
              Object localObject4 = null;
              ComponentName localComponentName = localIntent.getComponent();
              if (localComponentName != null) {
                if ((mEnableTwinApps) && (paramBoolean != true))
                {
                  localObject1 = localResolveInfo;
                  paramArrayOfIntent = (Intent[])localObject4;
                  try
                  {
                    if (localHashSet.contains(localComponentName.flattenToString()))
                    {
                      localObject1 = localResolveInfo;
                      paramArrayOfIntent = (Intent[])localObject4;
                      paramInt = localIntent.getComponent();
                      localObject1 = localResolveInfo;
                      paramArrayOfIntent = (Intent[])localObject4;
                      int j;
                      if (paramList.getUserId() == paramBoolean) {
                        j = 0;
                      } else {
                        j = paramBoolean;
                      }
                      localObject1 = localResolveInfo;
                      paramArrayOfIntent = (Intent[])localObject4;
                      paramInt = localIPackageManager.getActivityInfo(paramInt, 0, j);
                    }
                    else
                    {
                      localObject1 = localResolveInfo;
                      paramArrayOfIntent = (Intent[])localObject4;
                      localObject4 = localPackageManager.getActivityInfo(localIntent.getComponent(), 0);
                      paramInt = (int)localObject4;
                      localObject1 = localResolveInfo;
                      paramArrayOfIntent = (Intent[])localObject4;
                      if (!shouldKeepOriginalUser(localComponentName.flattenToString()))
                      {
                        localObject1 = localResolveInfo;
                        paramArrayOfIntent = (Intent[])localObject4;
                        localHashSet.add(localComponentName.flattenToString());
                        paramInt = (int)localObject4;
                      }
                    }
                    localObject1 = localResolveInfo;
                    paramArrayOfIntent = paramInt;
                    localObject4 = new android/content/pm/ResolveInfo;
                    localObject1 = localResolveInfo;
                    paramArrayOfIntent = paramInt;
                    ((ResolveInfo)localObject4).<init>();
                    localObject1 = localObject4;
                    paramArrayOfIntent = paramInt;
                    activityInfo = paramInt;
                    paramArrayOfIntent = (Intent[])localObject4;
                  }
                  catch (PackageManager.NameNotFoundException|RemoteException paramInt)
                  {
                    paramInt = paramArrayOfIntent;
                    paramArrayOfIntent = (Intent[])localObject1;
                  }
                }
                else
                {
                  paramArrayOfIntent = (Intent[])localObject1;
                  paramInt = localObject3;
                  try
                  {
                    localObject4 = localPackageManager.getActivityInfo(localIntent.getComponent(), 0);
                    paramArrayOfIntent = (Intent[])localObject1;
                    paramInt = (int)localObject4;
                    localResolveInfo = new android/content/pm/ResolveInfo;
                    paramArrayOfIntent = (Intent[])localObject1;
                    paramInt = (int)localObject4;
                    localResolveInfo.<init>();
                    localObject1 = localResolveInfo;
                    paramArrayOfIntent = (Intent[])localObject1;
                    paramInt = (int)localObject4;
                    activityInfo = ((ActivityInfo)localObject4);
                    paramArrayOfIntent = (Intent[])localObject1;
                    paramInt = (int)localObject4;
                  }
                  catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
                }
              }
              if (paramInt == null)
              {
                localObject2 = localPackageManager.resolveActivity(localIntent, 65536);
                if (localObject2 != null) {
                  paramArrayOfIntent = activityInfo;
                } else {
                  paramArrayOfIntent = null;
                }
                paramInt = paramArrayOfIntent;
                paramArrayOfIntent = (Intent[])localObject2;
              }
              if (paramInt != null) {
                break;
              }
              paramArrayOfIntent = new StringBuilder();
              paramArrayOfIntent.append("No activity found for ");
              paramArrayOfIntent.append(localIntent);
              Log.w("ChooserActivity", paramArrayOfIntent.toString());
            }
            if ((localIntent instanceof LabeledIntent))
            {
              localObject2 = (LabeledIntent)localIntent;
              resolvePackageName = ((LabeledIntent)localObject2).getSourcePackage();
              labelRes = ((LabeledIntent)localObject2).getLabelResource();
              nonLocalizedLabel = ((LabeledIntent)localObject2).getNonLocalizedLabel();
              icon = ((LabeledIntent)localObject2).getIconResource();
              iconResourceId = icon;
            }
            if (localUserManager.isManagedProfile())
            {
              noResourceId = true;
              icon = 0;
            }
            if ((mEnableTwinApps) && (paramBoolean != true) && (UserHandle.getUserId(applicationInfo.uid) == paramBoolean))
            {
              noResourceId = true;
              icon = 0;
            }
            mCallerTargets.add(new ResolverActivity.DisplayResolveInfo(ChooserActivity.this, localIntent, paramArrayOfIntent, paramArrayOfIntent.loadLabel(localPackageManager), null, localIntent));
          }
        }
      }
    }
    
    private void insertServiceTarget(ChooserActivity.ChooserTargetInfo paramChooserTargetInfo)
    {
      float f = paramChooserTargetInfo.getModifiedScore();
      int i = 0;
      int j = mServiceTargets.size();
      while (i < j)
      {
        if (f > ((ChooserActivity.ChooserTargetInfo)mServiceTargets.get(i)).getModifiedScore())
        {
          mServiceTargets.add(i, paramChooserTargetInfo);
          return;
        }
        i++;
      }
      mServiceTargets.add(paramChooserTargetInfo);
    }
    
    private boolean shouldKeepOriginalUser(String paramString)
    {
      return "com.viber.voip/com.viber.voip.messages.ui.media.ShareSocialActivity".equals(paramString);
    }
    
    public void addServiceResults(ResolverActivity.DisplayResolveInfo paramDisplayResolveInfo, List<ChooserTarget> paramList)
    {
      if ((mTargetsNeedPruning) && (paramList.size() > 0))
      {
        mServiceTargets.clear();
        mTargetsNeedPruning = false;
      }
      float f1 = getScore(paramDisplayResolveInfo);
      Collections.sort(paramList, mBaseTargetComparator);
      float f2 = 0.0F;
      int i = 0;
      int j = Math.min(paramList.size(), 2);
      while (i < j)
      {
        ChooserTarget localChooserTarget = (ChooserTarget)paramList.get(i);
        float f3 = localChooserTarget.getScore() * f1 * mLateFee;
        float f4 = f3;
        if (i > 0)
        {
          f4 = f3;
          if (f3 >= f2) {
            f4 = f2 * 0.95F;
          }
        }
        insertServiceTarget(new ChooserActivity.ChooserTargetInfo(ChooserActivity.this, paramDisplayResolveInfo, localChooserTarget, f4));
        i++;
        f2 = f4;
      }
      mLateFee *= 0.95F;
      notifyDataSetChanged();
    }
    
    public int getCallerTargetCount()
    {
      return mCallerTargets.size();
    }
    
    public int getCount()
    {
      return super.getCount() + getServiceTargetCount() + getCallerTargetCount();
    }
    
    public ResolverActivity.TargetInfo getItem(int paramInt)
    {
      return targetInfoForPosition(paramInt, true);
    }
    
    public int getPositionTargetType(int paramInt)
    {
      int i = getCallerTargetCount();
      if (paramInt < i) {
        return 0;
      }
      i += 0;
      int j = getServiceTargetCount();
      if (paramInt - i < j) {
        return 1;
      }
      if (paramInt - (i + j) < super.getCount()) {
        return 2;
      }
      return -1;
    }
    
    public int getServiceTargetCount()
    {
      if (!mShowServiceTargets) {
        return 0;
      }
      return Math.min(mServiceTargets.size(), 4);
    }
    
    public int getStandardTargetCount()
    {
      return super.getCount();
    }
    
    public int getUnfilteredCount()
    {
      return super.getUnfilteredCount() + getServiceTargetCount() + getCallerTargetCount();
    }
    
    public boolean isComponentPinned(ComponentName paramComponentName)
    {
      return mPinnedSharedPrefs.getBoolean(paramComponentName.flattenToString(), false);
    }
    
    public View onCreateView(ViewGroup paramViewGroup)
    {
      return mInflater.inflate(17367279, paramViewGroup, false);
    }
    
    public void onListRebuilt()
    {
      if (ActivityManager.isLowRamDeviceStatic()) {
        return;
      }
      if ((mServiceTargets != null) && (getDisplayInfoCount() == 0)) {
        mTargetsNeedPruning = true;
      }
      queryTargetServices(this);
    }
    
    public void setShowServiceTargets(boolean paramBoolean)
    {
      if (paramBoolean != mShowServiceTargets)
      {
        mShowServiceTargets = paramBoolean;
        notifyDataSetChanged();
      }
    }
    
    public boolean shouldGetResolvedFilter()
    {
      return true;
    }
    
    public boolean showsExtendedInfo(ResolverActivity.TargetInfo paramTargetInfo)
    {
      return false;
    }
    
    public ResolverActivity.TargetInfo targetInfoForPosition(int paramInt, boolean paramBoolean)
    {
      int i = getCallerTargetCount();
      if (paramInt < i) {
        return (ResolverActivity.TargetInfo)mCallerTargets.get(paramInt);
      }
      i += 0;
      int j = getServiceTargetCount();
      if (paramInt - i < j) {
        return (ResolverActivity.TargetInfo)mServiceTargets.get(paramInt - i);
      }
      i += j;
      Object localObject;
      if (paramBoolean) {
        localObject = super.getItem(paramInt - i);
      } else {
        localObject = getDisplayInfoAt(paramInt - i);
      }
      return localObject;
    }
  }
  
  public class ChooserListController
    extends ResolverListController
  {
    public ChooserListController(Context paramContext, PackageManager paramPackageManager, Intent paramIntent, String paramString, int paramInt)
    {
      super(paramPackageManager, paramIntent, paramString, paramInt);
    }
    
    public float getScore(ResolverActivity.DisplayResolveInfo paramDisplayResolveInfo)
    {
      if (paramDisplayResolveInfo == null) {
        return 900.0F;
      }
      float f1 = super.getScore(paramDisplayResolveInfo);
      float f2 = f1;
      if (paramDisplayResolveInfo.isPinned()) {
        f2 = f1 + 1000.0F;
      }
      return f2;
    }
    
    boolean isComponentFiltered(ComponentName paramComponentName)
    {
      if (mFilteredComponentNames == null) {
        return false;
      }
      ComponentName[] arrayOfComponentName = mFilteredComponentNames;
      int i = arrayOfComponentName.length;
      for (int j = 0; j < i; j++) {
        if (paramComponentName.equals(arrayOfComponentName[j])) {
          return true;
        }
      }
      return false;
    }
    
    boolean isComponentPinned(ComponentName paramComponentName)
    {
      return mPinnedSharedPrefs.getBoolean(paramComponentName.flattenToString(), false);
    }
  }
  
  class ChooserRowAdapter
    extends BaseAdapter
  {
    private int mAnimationCount = 0;
    private ChooserActivity.ChooserListAdapter mChooserListAdapter;
    private final int mColumnCount = 4;
    private final LayoutInflater mLayoutInflater;
    
    public ChooserRowAdapter(ChooserActivity.ChooserListAdapter paramChooserListAdapter)
    {
      mChooserListAdapter = paramChooserListAdapter;
      mLayoutInflater = LayoutInflater.from(ChooserActivity.this);
      paramChooserListAdapter.registerDataSetObserver(new DataSetObserver()
      {
        public void onChanged()
        {
          super.onChanged();
          notifyDataSetChanged();
        }
        
        public void onInvalidated()
        {
          super.onInvalidated();
          notifyDataSetInvalidated();
        }
      });
    }
    
    private void setVertPadding(ChooserActivity.RowViewHolder paramRowViewHolder, int paramInt1, int paramInt2)
    {
      row.setPadding(row.getPaddingLeft(), paramInt1, row.getPaddingRight(), paramInt2);
    }
    
    void bindViewHolder(int paramInt, ChooserActivity.RowViewHolder paramRowViewHolder)
    {
      int i = getFirstRowPosition(paramInt);
      int j = mChooserListAdapter.getPositionTargetType(i);
      for (int k = i + 4 - 1; (mChooserListAdapter.getPositionTargetType(k) != j) && (k >= i); k--) {}
      if (j == 1)
      {
        row.setBackgroundColor(getColor(17170624));
        int m = mChooserListAdapter.getPositionTargetType(getFirstRowPosition(paramInt + 1));
        j = row.getContext().getResources().getDimensionPixelSize(17105071);
        if ((paramInt == 0) && (m != 1))
        {
          setVertPadding(paramRowViewHolder, 0, 0);
        }
        else
        {
          if (paramInt == 0) {
            paramInt = j;
          } else {
            paramInt = 0;
          }
          if (m != 1) {
            setVertPadding(paramRowViewHolder, paramInt, j);
          } else {
            setVertPadding(paramRowViewHolder, paramInt, 0);
          }
        }
      }
      else
      {
        row.setBackgroundColor(0);
        if ((mChooserListAdapter.getPositionTargetType(getFirstRowPosition(paramInt - 1)) != 1) && (paramInt != 0)) {
          setVertPadding(paramRowViewHolder, 0, 0);
        } else {
          setVertPadding(paramRowViewHolder, row.getContext().getResources().getDimensionPixelSize(17105071), 0);
        }
      }
      paramInt = row.getLayoutParams().height;
      row.getLayoutParams().height = Math.max(1, measuredRowHeight);
      if (row.getLayoutParams().height != paramInt) {
        row.requestLayout();
      }
      for (paramInt = 0; paramInt < 4; paramInt++)
      {
        View localView = cells[paramInt];
        if (i + paramInt <= k)
        {
          localView.setVisibility(0);
          itemIndices[paramInt] = (i + paramInt);
          mChooserListAdapter.bindView(itemIndices[paramInt], localView);
        }
        else
        {
          localView.setVisibility(4);
        }
      }
    }
    
    ChooserActivity.RowViewHolder createViewHolder(final ViewGroup paramViewGroup)
    {
      ViewGroup localViewGroup = (ViewGroup)mLayoutInflater.inflate(17367147, paramViewGroup, false);
      paramViewGroup = new ChooserActivity.RowViewHolder(localViewGroup, 4);
      int i = View.MeasureSpec.makeMeasureSpec(0, 0);
      for (final int j = 0; j < 4; j++)
      {
        localObject = mChooserListAdapter.createView(localViewGroup);
        ((View)localObject).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            startSelected(paramViewGroupitemIndices[j], false, true);
          }
        });
        ((View)localObject).setOnLongClickListener(new View.OnLongClickListener()
        {
          public boolean onLongClick(View paramAnonymousView)
          {
            showTargetDetails(mChooserListAdapter.resolveInfoForPosition(paramViewGroupitemIndices[j], true));
            return true;
          }
        });
        localViewGroup.addView((View)localObject);
        cells[j] = localObject;
        ViewGroup.LayoutParams localLayoutParams = ((View)localObject).getLayoutParams();
        ((View)localObject).measure(i, i);
        if (localLayoutParams == null) {
          localViewGroup.setLayoutParams(new ViewGroup.LayoutParams(-1, ((View)localObject).getMeasuredHeight()));
        } else {
          height = ((View)localObject).getMeasuredHeight();
        }
        if (j != 3) {
          localViewGroup.addView(new Space(ChooserActivity.this), new LinearLayout.LayoutParams(0, 0, 1.0F));
        }
      }
      paramViewGroup.measure();
      Object localObject = localViewGroup.getLayoutParams();
      if (localObject == null) {
        localViewGroup.setLayoutParams(new ViewGroup.LayoutParams(-1, measuredRowHeight));
      } else {
        height = measuredRowHeight;
      }
      localViewGroup.setTag(paramViewGroup);
      return paramViewGroup;
    }
    
    public int getCallerTargetRowCount()
    {
      return (int)Math.ceil(mChooserListAdapter.getCallerTargetCount() / 4.0F);
    }
    
    public int getCount()
    {
      return (int)(getCallerTargetRowCount() + getServiceTargetRowCount() + Math.ceil(mChooserListAdapter.getStandardTargetCount() / 4.0F));
    }
    
    int getFirstRowPosition(int paramInt)
    {
      int i = mChooserListAdapter.getCallerTargetCount();
      int j = (int)Math.ceil(i / 4.0F);
      if (paramInt < j) {
        return paramInt * 4;
      }
      int k = mChooserListAdapter.getServiceTargetCount();
      int m = (int)Math.ceil(k / 4.0F);
      if (paramInt < j + m) {
        return (paramInt - j) * 4 + i;
      }
      return i + k + (paramInt - j - m) * 4;
    }
    
    public Object getItem(int paramInt)
    {
      return Integer.valueOf(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public int getServiceTargetRowCount()
    {
      int i;
      if (mChooserListAdapter.getServiceTargetCount() == 0) {
        i = 0;
      } else {
        i = 1;
      }
      return i;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null) {
        paramView = createViewHolder(paramViewGroup);
      } else {
        paramView = (ChooserActivity.RowViewHolder)paramView.getTag();
      }
      bindViewHolder(paramInt, paramView);
      return row;
    }
  }
  
  final class ChooserTargetInfo
    implements ResolverActivity.TargetInfo
  {
    private final ResolveInfo mBackupResolveInfo;
    private CharSequence mBadgeContentDescription;
    private Drawable mBadgeIcon = null;
    private final ChooserTarget mChooserTarget;
    private Drawable mDisplayIcon;
    private final int mFillInFlags;
    private final Intent mFillInIntent;
    private final float mModifiedScore;
    private final ResolverActivity.DisplayResolveInfo mSourceInfo;
    
    private ChooserTargetInfo(ChooserTargetInfo paramChooserTargetInfo, Intent paramIntent, int paramInt)
    {
      mSourceInfo = mSourceInfo;
      mBackupResolveInfo = mBackupResolveInfo;
      mChooserTarget = mChooserTarget;
      mBadgeIcon = mBadgeIcon;
      mBadgeContentDescription = mBadgeContentDescription;
      mDisplayIcon = mDisplayIcon;
      mFillInIntent = paramIntent;
      mFillInFlags = paramInt;
      mModifiedScore = mModifiedScore;
    }
    
    public ChooserTargetInfo(ResolverActivity.DisplayResolveInfo paramDisplayResolveInfo, ChooserTarget paramChooserTarget, float paramFloat)
    {
      mSourceInfo = paramDisplayResolveInfo;
      mChooserTarget = paramChooserTarget;
      mModifiedScore = paramFloat;
      if (paramDisplayResolveInfo != null)
      {
        Object localObject = paramDisplayResolveInfo.getResolveInfo();
        if (localObject != null)
        {
          localObject = activityInfo;
          if ((localObject != null) && (applicationInfo != null))
          {
            PackageManager localPackageManager = getPackageManager();
            mBadgeIcon = localPackageManager.getApplicationIcon(applicationInfo);
            mBadgeContentDescription = localPackageManager.getApplicationLabel(applicationInfo);
          }
        }
      }
      paramChooserTarget = paramChooserTarget.getIcon();
      if (paramChooserTarget != null) {
        paramChooserTarget = paramChooserTarget.loadDrawable(ChooserActivity.this);
      } else {
        paramChooserTarget = null;
      }
      mDisplayIcon = paramChooserTarget;
      if (paramDisplayResolveInfo != null) {
        mBackupResolveInfo = null;
      } else {
        mBackupResolveInfo = getPackageManager().resolveActivity(getResolvedIntent(), 0);
      }
      mFillInIntent = null;
      mFillInFlags = 0;
    }
    
    private Intent getBaseIntentToSend()
    {
      Intent localIntent = getResolvedIntent();
      if (localIntent == null)
      {
        Log.e("ChooserActivity", "ChooserTargetInfo: no base intent available to send");
      }
      else
      {
        localIntent = new Intent(localIntent);
        if (mFillInIntent != null) {
          localIntent.fillIn(mFillInIntent, mFillInFlags);
        }
        localIntent.fillIn(mReferrerFillInIntent, 0);
      }
      return localIntent;
    }
    
    public ResolverActivity.TargetInfo cloneFilledIn(Intent paramIntent, int paramInt)
    {
      return new ChooserTargetInfo(ChooserActivity.this, this, paramIntent, paramInt);
    }
    
    public List<Intent> getAllSourceIntents()
    {
      ArrayList localArrayList = new ArrayList();
      if (mSourceInfo != null) {
        localArrayList.add((Intent)mSourceInfo.getAllSourceIntents().get(0));
      }
      return localArrayList;
    }
    
    public CharSequence getBadgeContentDescription()
    {
      return mBadgeContentDescription;
    }
    
    public Drawable getBadgeIcon()
    {
      return mBadgeIcon;
    }
    
    public Drawable getDisplayIcon()
    {
      return mDisplayIcon;
    }
    
    public CharSequence getDisplayLabel()
    {
      return mChooserTarget.getTitle();
    }
    
    public CharSequence getExtendedInfo()
    {
      return null;
    }
    
    public float getModifiedScore()
    {
      return mModifiedScore;
    }
    
    public ResolveInfo getResolveInfo()
    {
      ResolveInfo localResolveInfo;
      if (mSourceInfo != null) {
        localResolveInfo = mSourceInfo.getResolveInfo();
      } else {
        localResolveInfo = mBackupResolveInfo;
      }
      return localResolveInfo;
    }
    
    public ComponentName getResolvedComponentName()
    {
      if (mSourceInfo != null) {
        return mSourceInfo.getResolvedComponentName();
      }
      if (mBackupResolveInfo != null) {
        return new ComponentName(mBackupResolveInfo.activityInfo.packageName, mBackupResolveInfo.activityInfo.name);
      }
      return null;
    }
    
    public Intent getResolvedIntent()
    {
      if (mSourceInfo != null) {
        return mSourceInfo.getResolvedIntent();
      }
      Intent localIntent = new Intent(getTargetIntent());
      localIntent.setComponent(mChooserTarget.getComponentName());
      localIntent.putExtras(mChooserTarget.getIntentExtras());
      return localIntent;
    }
    
    public boolean isPinned()
    {
      boolean bool;
      if (mSourceInfo != null) {
        bool = mSourceInfo.isPinned();
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean start(Activity paramActivity, Bundle paramBundle)
    {
      throw new RuntimeException("ChooserTargets should be started as caller.");
    }
    
    public boolean startAsCaller(Activity paramActivity, Bundle paramBundle, int paramInt)
    {
      Intent localIntent = getBaseIntentToSend();
      boolean bool1 = false;
      if (localIntent == null) {
        return false;
      }
      localIntent.setComponent(mChooserTarget.getComponentName());
      localIntent.putExtras(mChooserTarget.getIntentExtras());
      boolean bool2 = bool1;
      if (mSourceInfo != null)
      {
        bool2 = bool1;
        if (mSourceInfo.getResolvedComponentName().getPackageName().equals(mChooserTarget.getComponentName().getPackageName())) {
          bool2 = true;
        }
      }
      paramActivity.startActivityAsCaller(localIntent, paramBundle, bool2, paramInt);
      return true;
    }
    
    public boolean startAsUser(Activity paramActivity, Bundle paramBundle, UserHandle paramUserHandle)
    {
      throw new RuntimeException("ChooserTargets should be started as caller.");
    }
  }
  
  static class ChooserTargetServiceConnection
    implements ServiceConnection
  {
    private ChooserActivity mChooserActivity;
    private final IChooserTargetResult mChooserTargetResult = new IChooserTargetResult.Stub()
    {
      public void sendResult(List<ChooserTarget> paramAnonymousList)
        throws RemoteException
      {
        synchronized (mLock)
        {
          if (ChooserActivity.this == null)
          {
            paramAnonymousList = new java/lang/StringBuilder;
            paramAnonymousList.<init>();
            paramAnonymousList.append("destroyed ChooserTargetServiceConnection received result from ");
            paramAnonymousList.append(mConnectedComponent);
            paramAnonymousList.append("; ignoring...");
            Log.e("ChooserActivity", paramAnonymousList.toString());
            return;
          }
          filterServiceTargets(mOriginalTarget.getResolveInfo().activityInfo.packageName, paramAnonymousList);
          Message localMessage = Message.obtain();
          what = 1;
          ChooserActivity.ServiceResultInfo localServiceResultInfo = new com/android/internal/app/ChooserActivity$ServiceResultInfo;
          localServiceResultInfo.<init>(mOriginalTarget, paramAnonymousList, ChooserActivity.ChooserTargetServiceConnection.this);
          obj = localServiceResultInfo;
          mChooserHandler.sendMessage(localMessage);
          return;
        }
      }
    };
    private ComponentName mConnectedComponent;
    private final Object mLock = new Object();
    private ResolverActivity.DisplayResolveInfo mOriginalTarget;
    
    public ChooserTargetServiceConnection(ChooserActivity paramChooserActivity, ResolverActivity.DisplayResolveInfo paramDisplayResolveInfo)
    {
      mChooserActivity = paramChooserActivity;
      mOriginalTarget = paramDisplayResolveInfo;
    }
    
    public void destroy()
    {
      synchronized (mLock)
      {
        mChooserActivity = null;
        mOriginalTarget = null;
        return;
      }
    }
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      synchronized (mLock)
      {
        if (mChooserActivity == null)
        {
          Log.e("ChooserActivity", "destroyed ChooserTargetServiceConnection got onServiceConnected");
          return;
        }
        paramIBinder = IChooserTargetService.Stub.asInterface(paramIBinder);
        try
        {
          paramIBinder.getChooserTargets(mOriginalTarget.getResolvedComponentName(), mOriginalTarget.getResolveInfo().filter, mChooserTargetResult);
        }
        catch (RemoteException localRemoteException)
        {
          paramIBinder = new java/lang/StringBuilder;
          paramIBinder.<init>();
          paramIBinder.append("Querying ChooserTargetService ");
          paramIBinder.append(paramComponentName);
          paramIBinder.append(" failed.");
          Log.e("ChooserActivity", paramIBinder.toString(), localRemoteException);
          mChooserActivity.unbindService(this);
          mChooserActivity.mServiceConnections.remove(this);
          destroy();
        }
        return;
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      synchronized (mLock)
      {
        if (mChooserActivity == null)
        {
          Log.e("ChooserActivity", "destroyed ChooserTargetServiceConnection got onServiceDisconnected");
          return;
        }
        mChooserActivity.unbindService(this);
        mChooserActivity.mServiceConnections.remove(this);
        if (mChooserActivity.mServiceConnections.isEmpty())
        {
          mChooserActivity.mChooserHandler.removeMessages(2);
          mChooserActivity.sendVoiceChoicesIfNeeded();
        }
        mConnectedComponent = null;
        destroy();
        return;
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ChooserTargetServiceConnection{service=");
      localStringBuilder.append(mConnectedComponent);
      localStringBuilder.append(", activity=");
      String str;
      if (mOriginalTarget != null) {
        str = mOriginalTarget.getResolveInfo().activityInfo.toString();
      } else {
        str = "<connection destroyed>";
      }
      localStringBuilder.append(str);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  class OffsetDataSetObserver
    extends DataSetObserver
  {
    private View mCachedView;
    private int mCachedViewType = -1;
    private final AbsListView mListView;
    
    public OffsetDataSetObserver(AbsListView paramAbsListView)
    {
      mListView = paramAbsListView;
    }
    
    public void onChanged()
    {
      if (mResolverDrawerLayout == null) {
        return;
      }
      int i = mChooserRowAdapter.getServiceTargetRowCount();
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        int m = mChooserRowAdapter.getCallerTargetRowCount() + k;
        int n = mChooserRowAdapter.getItemViewType(m);
        if (n != mCachedViewType) {
          mCachedView = null;
        }
        View localView = mChooserRowAdapter.getView(m, mCachedView, mListView);
        j += getTagmeasuredRowHeight;
        if (n >= 0)
        {
          mCachedViewType = n;
          mCachedView = localView;
        }
        else
        {
          mCachedViewType = -1;
        }
      }
      mResolverDrawerLayout.setCollapsibleHeightReserved(j);
    }
  }
  
  static class RefinementResultReceiver
    extends ResultReceiver
  {
    private ChooserActivity mChooserActivity;
    private ResolverActivity.TargetInfo mSelectedTarget;
    
    public RefinementResultReceiver(ChooserActivity paramChooserActivity, ResolverActivity.TargetInfo paramTargetInfo, Handler paramHandler)
    {
      super();
      mChooserActivity = paramChooserActivity;
      mSelectedTarget = paramTargetInfo;
    }
    
    public void destroy()
    {
      mChooserActivity = null;
      mSelectedTarget = null;
    }
    
    protected void onReceiveResult(int paramInt, Bundle paramBundle)
    {
      if (mChooserActivity == null)
      {
        Log.e("ChooserActivity", "Destroyed RefinementResultReceiver received a result");
        return;
      }
      if (paramBundle == null)
      {
        Log.e("ChooserActivity", "RefinementResultReceiver received null resultData");
        return;
      }
      switch (paramInt)
      {
      default: 
        paramBundle = new StringBuilder();
        paramBundle.append("Unknown result code ");
        paramBundle.append(paramInt);
        paramBundle.append(" sent to RefinementResultReceiver");
        Log.w("ChooserActivity", paramBundle.toString());
        break;
      case 0: 
        mChooserActivity.onRefinementCanceled();
        break;
      case -1: 
        paramBundle = paramBundle.getParcelable("android.intent.extra.INTENT");
        if ((paramBundle instanceof Intent)) {
          mChooserActivity.onRefinementResult(mSelectedTarget, (Intent)paramBundle);
        } else {
          Log.e("ChooserActivity", "RefinementResultReceiver received RESULT_OK but no Intent in resultData with key Intent.EXTRA_INTENT");
        }
        break;
      }
    }
  }
  
  static class RowViewHolder
  {
    final View[] cells;
    int[] itemIndices;
    int measuredRowHeight;
    final ViewGroup row;
    
    public RowViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      row = paramViewGroup;
      cells = new View[paramInt];
      itemIndices = new int[paramInt];
    }
    
    public void measure()
    {
      int i = View.MeasureSpec.makeMeasureSpec(0, 0);
      row.measure(i, i);
      measuredRowHeight = row.getMeasuredHeight();
    }
  }
  
  static class ServiceResultInfo
  {
    public final ChooserActivity.ChooserTargetServiceConnection connection;
    public final ResolverActivity.DisplayResolveInfo originalTarget;
    public final List<ChooserTarget> resultTargets;
    
    public ServiceResultInfo(ResolverActivity.DisplayResolveInfo paramDisplayResolveInfo, List<ChooserTarget> paramList, ChooserActivity.ChooserTargetServiceConnection paramChooserTargetServiceConnection)
    {
      originalTarget = paramDisplayResolveInfo;
      resultTargets = paramList;
      connection = paramChooserTargetServiceConnection;
    }
  }
}
