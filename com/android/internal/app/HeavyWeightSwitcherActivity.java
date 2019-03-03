package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HeavyWeightSwitcherActivity
  extends Activity
{
  public static final String KEY_CUR_APP = "cur_app";
  public static final String KEY_CUR_TASK = "cur_task";
  public static final String KEY_HAS_RESULT = "has_result";
  public static final String KEY_INTENT = "intent";
  public static final String KEY_NEW_APP = "new_app";
  String mCurApp;
  int mCurTask;
  boolean mHasResult;
  String mNewApp;
  IntentSender mStartIntent;
  private View.OnClickListener mSwitchNewListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      try
      {
        ActivityManager.getService().finishHeavyWeightApp();
      }
      catch (RemoteException paramAnonymousView) {}
      try
      {
        if (mHasResult) {
          startIntentSenderForResult(mStartIntent, -1, null, 33554432, 33554432, 0);
        } else {
          startIntentSenderForResult(mStartIntent, -1, null, 0, 0, 0);
        }
      }
      catch (IntentSender.SendIntentException paramAnonymousView)
      {
        Log.w("HeavyWeightSwitcherActivity", "Failure starting", paramAnonymousView);
      }
      finish();
    }
  };
  private View.OnClickListener mSwitchOldListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      try
      {
        ActivityManager.getService().moveTaskToFront(mCurTask, 0, null);
      }
      catch (RemoteException paramAnonymousView) {}
      finish();
    }
  };
  
  public HeavyWeightSwitcherActivity() {}
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    mStartIntent = ((IntentSender)getIntent().getParcelableExtra("intent"));
    mHasResult = getIntent().getBooleanExtra("has_result", false);
    mCurApp = getIntent().getStringExtra("cur_app");
    mCurTask = getIntent().getIntExtra("cur_task", 0);
    mNewApp = getIntent().getStringExtra("new_app");
    setContentView(17367182);
    setIconAndText(16909196, 16909195, 0, mCurApp, mNewApp, 17040526, 0);
    setIconAndText(16909157, 16909155, 16909156, mNewApp, mCurApp, 17040468, 17040469);
    findViewById(16909427).setOnClickListener(mSwitchOldListener);
    findViewById(16909426).setOnClickListener(mSwitchNewListener);
  }
  
  void setDrawable(int paramInt, Drawable paramDrawable)
  {
    if (paramDrawable != null) {
      ((ImageView)findViewById(paramInt)).setImageDrawable(paramDrawable);
    }
  }
  
  void setIconAndText(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2, int paramInt4, int paramInt5)
  {
    String str = paramString1;
    Object localObject1 = null;
    Object localObject2 = str;
    Object localObject3 = localObject1;
    if (paramString1 != null)
    {
      localObject2 = str;
      try
      {
        localObject3 = getPackageManager().getApplicationInfo(paramString1, 0);
        localObject2 = str;
        paramString1 = ((ApplicationInfo)localObject3).loadLabel(getPackageManager());
        localObject2 = paramString1;
        localObject3 = ((ApplicationInfo)localObject3).loadIcon(getPackageManager());
        localObject2 = paramString1;
      }
      catch (PackageManager.NameNotFoundException paramString1)
      {
        localObject3 = localObject1;
      }
    }
    setDrawable(paramInt1, (Drawable)localObject3);
    setText(paramInt2, getString(paramInt4, new Object[] { localObject2 }));
    if (paramInt3 != 0)
    {
      paramString1 = paramString2;
      localObject2 = paramString1;
      if (paramString2 != null) {
        try
        {
          localObject2 = getPackageManager().getApplicationInfo(paramString2, 0).loadLabel(getPackageManager());
        }
        catch (PackageManager.NameNotFoundException paramString2)
        {
          localObject2 = paramString1;
        }
      }
      setText(paramInt3, getString(paramInt5, new Object[] { localObject2 }));
    }
  }
  
  void setText(int paramInt, CharSequence paramCharSequence)
  {
    ((TextView)findViewById(paramInt)).setText(paramCharSequence);
  }
}
