package android.content.pm;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigUpdaterService
  extends Service
{
  static final String ACTION_UPDATE_APP2SD_BLIST = "asus.intent.action.APP2SD_BLACKLIST_UPDATED";
  static final String ACTION_UPDATE_APP_ASPECT_RATIO_LIST = "asus.intent.action.ASPECT_RATIO_FILE_UPDATED";
  static final String ACTION_UPDATE_APP_NOTCH_LIST = "asus.intent.action.NOTCH_APP_FILE_UPDATED";
  static final String ACTION_UPDATE_FUNINSTALL_LIST = "asus.intent.action.FORCEUNINSTALL_LIST_UPDATED";
  static final int MSG_UPDATE_APP_ASPECT_RATIO = 2;
  static final int MSG_UPDATE_APP_NOTCH = 3;
  static final int MSG_UPDATE_FORCE_UNINSTALL_LIST = 1;
  static final int MSG__UPDATE_APP2SD_BLACKLIST = 0;
  static final String TAG = "ConfigUpdaterService";
  static final String TARGET_FILE_APP2SD = "blacklist.xml";
  static final String TARGET_FILE_FUUNINSTALL = "list.xml";
  static final String TARGET_FOLDER_APP2SD = "app2sd/";
  static final String TARGET_FOLDER_FUNINSTALL = "force_uninstall/";
  static final String TARGET_PATH_ROOT = "/data/system/";
  final boolean DEBUG = false;
  private UpdateConfigHandler mHandler = new UpdateConfigHandler(null);
  
  public ConfigUpdaterService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }
  
  public void onDestroy()
  {
    super.onDestroy();
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    int i = -1;
    if (paramIntent != null)
    {
      if ("asus.intent.action.APP2SD_BLACKLIST_UPDATED".equals(paramIntent.getAction())) {
        i = 0;
      } else if ("asus.intent.action.FORCEUNINSTALL_LIST_UPDATED".equals(paramIntent.getAction())) {
        i = 1;
      } else if ("asus.intent.action.ASPECT_RATIO_FILE_UPDATED".equals(paramIntent.getAction())) {
        i = 2;
      } else if ("asus.intent.action.NOTCH_APP_FILE_UPDATED".equals(paramIntent.getAction())) {
        i = 3;
      }
      if (i > -1)
      {
        mHandler.removeMessages(i);
        Message localMessage = mHandler.obtainMessage();
        what = i;
        arg1 = paramInt2;
        obj = paramIntent.getData();
        mHandler.sendMessage(localMessage);
      }
    }
    return super.onStartCommand(paramIntent, paramInt1, paramInt2);
  }
  
  private class UpdateConfigHandler
    extends Handler
  {
    private UpdateConfigHandler() {}
    
    private void copyFile(Uri paramUri, File paramFile)
    {
      try
      {
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("remote uri: ");
        ((StringBuilder)localObject).append(paramUri);
        Log.i("ConfigUpdaterService", ((StringBuilder)localObject).toString());
        paramUri = getContentResolver().openFileDescriptor(paramUri, "r");
        if (paramFile.exists())
        {
          File localFile = new java/io/File;
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append(paramFile.getParent());
          ((StringBuilder)localObject).append(File.separator);
          ((StringBuilder)localObject).append(paramFile.getName());
          ((StringBuilder)localObject).append(".bak");
          localFile.<init>(((StringBuilder)localObject).toString());
          paramFile.renameTo(localFile);
        }
        paramFile.createNewFile();
        localObject = new java/io/FileInputStream;
        ((FileInputStream)localObject).<init>(paramUri.getFileDescriptor());
        paramUri = new java/io/FileOutputStream;
        paramUri.<init>(paramFile);
        for (int i = ((FileInputStream)localObject).read(); i != -1; i = ((FileInputStream)localObject).read()) {
          paramUri.write(i);
        }
        ((FileInputStream)localObject).close();
        paramUri.close();
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("file '");
        paramUri.append(paramFile.getPath());
        paramUri.append("' copy complete!");
        Log.i("ConfigUpdaterService", paramUri.toString());
      }
      catch (IOException paramUri)
      {
        paramUri.printStackTrace();
      }
      catch (FileNotFoundException paramUri)
      {
        paramUri.printStackTrace();
      }
    }
    
    private void listUpdate(Uri paramUri, int paramInt)
    {
      File localFile;
      StringBuilder localStringBuilder;
      switch (paramInt)
      {
      default: 
        paramUri = new StringBuilder();
        paramUri.append("Unknown MSG:");
        paramUri.append(paramInt);
        Log.w("ConfigUpdaterService", paramUri.toString());
        return;
      case 3: 
        localFile = new File("/data/system/appscaling/");
        if (!localFile.exists())
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("create folder: ");
          localStringBuilder.append(localFile.getPath());
          localStringBuilder.append(": ");
          localStringBuilder.append(localFile.mkdir());
          Log.i("ConfigUpdaterService", localStringBuilder.toString());
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(localFile.getPath());
        localStringBuilder.append(File.separator);
        localStringBuilder.append("app_notch_config.xml");
        copyFile(paramUri, new File(localStringBuilder.toString()));
        getPackageManager().refreshAppSupportNotchList();
        break;
      case 2: 
        localFile = new File("/data/system/appscaling/");
        if (!localFile.exists())
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("create folder: ");
          localStringBuilder.append(localFile.getPath());
          localStringBuilder.append(": ");
          localStringBuilder.append(localFile.mkdir());
          Log.i("ConfigUpdaterService", localStringBuilder.toString());
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(localFile.getPath());
        localStringBuilder.append(File.separator);
        localStringBuilder.append("app_aspect_ratio_config.xml");
        copyFile(paramUri, new File(localStringBuilder.toString()));
        getPackageManager().refreshAppAspectRatio();
        break;
      case 1: 
        localFile = new File("/data/system/force_uninstall/");
        if (!localFile.exists())
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("create folder: ");
          localStringBuilder.append(localFile.getPath());
          localStringBuilder.append(": ");
          localStringBuilder.append(localFile.mkdir());
          Log.i("ConfigUpdaterService", localStringBuilder.toString());
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(localFile.getPath());
        localStringBuilder.append(File.separator);
        localStringBuilder.append("list.xml");
        copyFile(paramUri, new File(localStringBuilder.toString()));
        getPackageManager().executeForceUninstall();
        break;
      case 0: 
        localFile = new File("/data/system/app2sd/");
        if (!localFile.exists())
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("create folder: ");
          localStringBuilder.append(localFile.getPath());
          localStringBuilder.append(": ");
          localStringBuilder.append(localFile.mkdir());
          Log.i("ConfigUpdaterService", localStringBuilder.toString());
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(localFile.getPath());
        localStringBuilder.append(File.separator);
        localStringBuilder.append("blacklist.xml");
        copyFile(paramUri, new File(localStringBuilder.toString()));
        getPackageManager().refreshApp2sdBlacklist();
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      Uri localUri = (Uri)obj;
      if (localUri != null)
      {
        listUpdate(localUri, what);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("remote file uri is ");
        localStringBuilder.append(localUri);
        Log.w("ConfigUpdaterService", localStringBuilder.toString());
      }
      stopSelf(arg1);
    }
  }
}
