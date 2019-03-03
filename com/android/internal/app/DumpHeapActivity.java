package com.android.internal.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DebugUtils;
import android.util.Slog;

public class DumpHeapActivity
  extends Activity
{
  public static final String ACTION_DELETE_DUMPHEAP = "com.android.server.am.DELETE_DUMPHEAP";
  public static final String EXTRA_DELAY_DELETE = "delay_delete";
  public static final Uri JAVA_URI = Uri.parse("content://com.android.server.heapdump/java");
  public static final String KEY_DIRECT_LAUNCH = "direct_launch";
  public static final String KEY_PROCESS = "process";
  public static final String KEY_SIZE = "size";
  AlertDialog mDialog;
  boolean mHandled = false;
  String mProcess;
  long mSize;
  
  public DumpHeapActivity() {}
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mProcess = getIntent().getStringExtra("process");
    mSize = getIntent().getLongExtra("size", 0L);
    paramBundle = getIntent().getStringExtra("direct_launch");
    if (paramBundle != null)
    {
      Intent localIntent = new Intent("android.app.action.REPORT_HEAP_LIMIT");
      localIntent.setPackage(paramBundle);
      Object localObject = ClipData.newUri(getContentResolver(), "Heap Dump", JAVA_URI);
      localIntent.setClipData((ClipData)localObject);
      localIntent.addFlags(1);
      localIntent.setType(((ClipData)localObject).getDescription().getMimeType(0));
      localIntent.putExtra("android.intent.extra.STREAM", JAVA_URI);
      try
      {
        startActivity(localIntent);
        scheduleDelete();
        mHandled = true;
        finish();
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unable to direct launch to ");
        ((StringBuilder)localObject).append(paramBundle);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(localActivityNotFoundException.getMessage());
        Slog.i("DumpHeapActivity", ((StringBuilder)localObject).toString());
      }
    }
    paramBundle = new AlertDialog.Builder(this, 16974394);
    paramBundle.setTitle(17039906);
    paramBundle.setMessage(getString(17039905, new Object[] { mProcess, DebugUtils.sizeValueToString(mSize, null) }));
    paramBundle.setNegativeButton(17039360, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        mHandled = true;
        sendBroadcast(new Intent("com.android.server.am.DELETE_DUMPHEAP"));
        finish();
      }
    });
    paramBundle.setPositiveButton(17039370, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        mHandled = true;
        scheduleDelete();
        paramAnonymousDialogInterface = new Intent("android.intent.action.SEND");
        ClipData localClipData = ClipData.newUri(getContentResolver(), "Heap Dump", DumpHeapActivity.JAVA_URI);
        paramAnonymousDialogInterface.setClipData(localClipData);
        paramAnonymousDialogInterface.addFlags(1);
        paramAnonymousDialogInterface.setType(localClipData.getDescription().getMimeType(0));
        paramAnonymousDialogInterface.putExtra("android.intent.extra.STREAM", DumpHeapActivity.JAVA_URI);
        startActivity(Intent.createChooser(paramAnonymousDialogInterface, getText(17039906)));
        finish();
      }
    });
    mDialog = paramBundle.show();
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    mDialog.dismiss();
  }
  
  protected void onStop()
  {
    super.onStop();
    if ((!isChangingConfigurations()) && (!mHandled)) {
      sendBroadcast(new Intent("com.android.server.am.DELETE_DUMPHEAP"));
    }
  }
  
  void scheduleDelete()
  {
    Intent localIntent = new Intent("com.android.server.am.DELETE_DUMPHEAP");
    localIntent.putExtra("delay_delete", true);
    sendBroadcast(localIntent);
  }
}
