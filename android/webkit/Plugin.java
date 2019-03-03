package android.webkit;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

@Deprecated
public class Plugin
{
  private String mDescription;
  private String mFileName;
  private PreferencesClickHandler mHandler;
  private String mName;
  private String mPath;
  
  @Deprecated
  public Plugin(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    mName = paramString1;
    mPath = paramString2;
    mFileName = paramString3;
    mDescription = paramString4;
    mHandler = new DefaultClickHandler(null);
  }
  
  @Deprecated
  public void dispatchClickEvent(Context paramContext)
  {
    if (mHandler != null) {
      mHandler.handleClickEvent(paramContext);
    }
  }
  
  @Deprecated
  public String getDescription()
  {
    return mDescription;
  }
  
  @Deprecated
  public String getFileName()
  {
    return mFileName;
  }
  
  @Deprecated
  public String getName()
  {
    return mName;
  }
  
  @Deprecated
  public String getPath()
  {
    return mPath;
  }
  
  @Deprecated
  public void setClickHandler(PreferencesClickHandler paramPreferencesClickHandler)
  {
    mHandler = paramPreferencesClickHandler;
  }
  
  @Deprecated
  public void setDescription(String paramString)
  {
    mDescription = paramString;
  }
  
  @Deprecated
  public void setFileName(String paramString)
  {
    mFileName = paramString;
  }
  
  @Deprecated
  public void setName(String paramString)
  {
    mName = paramString;
  }
  
  @Deprecated
  public void setPath(String paramString)
  {
    mPath = paramString;
  }
  
  @Deprecated
  public String toString()
  {
    return mName;
  }
  
  @Deprecated
  private class DefaultClickHandler
    implements Plugin.PreferencesClickHandler, DialogInterface.OnClickListener
  {
    private AlertDialog mDialog;
    
    private DefaultClickHandler() {}
    
    @Deprecated
    public void handleClickEvent(Context paramContext)
    {
      if (mDialog == null) {
        mDialog = new AlertDialog.Builder(paramContext).setTitle(mName).setMessage(mDescription).setPositiveButton(17039370, this).setCancelable(false).show();
      }
    }
    
    @Deprecated
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      mDialog.dismiss();
      mDialog = null;
    }
  }
  
  public static abstract interface PreferencesClickHandler
  {
    public abstract void handleClickEvent(Context paramContext);
  }
}
