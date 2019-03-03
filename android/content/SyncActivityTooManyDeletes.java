package android.content;

import android.accounts.Account;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SyncActivityTooManyDeletes
  extends Activity
  implements AdapterView.OnItemClickListener
{
  private Account mAccount;
  private String mAuthority;
  private long mNumDeletes;
  private String mProvider;
  
  public SyncActivityTooManyDeletes() {}
  
  private void startSyncReallyDelete()
  {
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("deletions_override", true);
    localBundle.putBoolean("force", true);
    localBundle.putBoolean("expedited", true);
    localBundle.putBoolean("upload", true);
    ContentResolver.requestSync(mAccount, mAuthority, localBundle);
  }
  
  private void startSyncUndoDeletes()
  {
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("discard_deletions", true);
    localBundle.putBoolean("force", true);
    localBundle.putBoolean("expedited", true);
    localBundle.putBoolean("upload", true);
    ContentResolver.requestSync(mAccount, mAuthority, localBundle);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getExtras();
    if (paramBundle == null)
    {
      finish();
      return;
    }
    mNumDeletes = paramBundle.getLong("numDeletes");
    mAccount = ((Account)paramBundle.getParcelable("account"));
    mAuthority = paramBundle.getString("authority");
    mProvider = paramBundle.getString("provider");
    Object localObject = new ArrayAdapter(this, 17367043, 16908308, new CharSequence[] { getResources().getText(17041095), getResources().getText(17041098), getResources().getText(17041094) });
    paramBundle = new ListView(this);
    paramBundle.setAdapter((ListAdapter)localObject);
    paramBundle.setItemsCanFocus(true);
    paramBundle.setOnItemClickListener(this);
    localObject = new TextView(this);
    ((TextView)localObject).setText(String.format(getResources().getText(17041097).toString(), new Object[] { Long.valueOf(mNumDeletes), mProvider, mAccount.name }));
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setOrientation(1);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2, 0.0F);
    localLinearLayout.addView((View)localObject, localLayoutParams);
    localLinearLayout.addView(paramBundle, localLayoutParams);
    setContentView(localLinearLayout);
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt == 0) {
      startSyncReallyDelete();
    } else if (paramInt == 1) {
      startSyncUndoDeletes();
    }
    finish();
  }
}
