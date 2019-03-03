package android.preference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class RingtonePreference
  extends Preference
  implements PreferenceManager.OnActivityResultListener
{
  private static final String TAG = "RingtonePreference";
  private int mRequestCode;
  private int mRingtoneType;
  private boolean mShowDefault;
  private boolean mShowSilent;
  
  public RingtonePreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public RingtonePreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842899);
  }
  
  public RingtonePreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public RingtonePreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RingtonePreference, paramInt1, paramInt2);
    mRingtoneType = paramContext.getInt(0, 1);
    mShowDefault = paramContext.getBoolean(1, true);
    mShowSilent = paramContext.getBoolean(2, true);
    paramContext.recycle();
  }
  
  public int getRingtoneType()
  {
    return mRingtoneType;
  }
  
  public boolean getShowDefault()
  {
    return mShowDefault;
  }
  
  public boolean getShowSilent()
  {
    return mShowSilent;
  }
  
  public boolean onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == mRequestCode)
    {
      if (paramIntent != null)
      {
        Uri localUri = (Uri)paramIntent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
        if (localUri != null) {
          paramIntent = localUri.toString();
        } else {
          paramIntent = "";
        }
        if (callChangeListener(paramIntent)) {
          onSaveRingtone(localUri);
        }
      }
      return true;
    }
    return false;
  }
  
  protected void onAttachedToHierarchy(PreferenceManager paramPreferenceManager)
  {
    super.onAttachedToHierarchy(paramPreferenceManager);
    paramPreferenceManager.registerOnActivityResultListener(this);
    mRequestCode = paramPreferenceManager.getNextRequestCode();
  }
  
  protected void onClick()
  {
    Intent localIntent = new Intent("android.intent.action.RINGTONE_PICKER");
    onPrepareRingtonePickerIntent(localIntent);
    PreferenceFragment localPreferenceFragment = getPreferenceManager().getFragment();
    if (localPreferenceFragment != null) {
      localPreferenceFragment.startActivityForResult(localIntent, mRequestCode);
    } else {
      getPreferenceManager().getActivity().startActivityForResult(localIntent, mRequestCode);
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return paramTypedArray.getString(paramInt);
  }
  
  protected void onPrepareRingtonePickerIntent(Intent paramIntent)
  {
    paramIntent.putExtra("android.intent.extra.ringtone.EXISTING_URI", onRestoreRingtone());
    paramIntent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", mShowDefault);
    if (mShowDefault) {
      paramIntent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(getRingtoneType()));
    }
    paramIntent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", mShowSilent);
    paramIntent.putExtra("android.intent.extra.ringtone.TYPE", mRingtoneType);
    paramIntent.putExtra("android.intent.extra.ringtone.TITLE", getTitle());
    paramIntent.putExtra("android.intent.extra.ringtone.AUDIO_ATTRIBUTES_FLAGS", 64);
  }
  
  protected Uri onRestoreRingtone()
  {
    Uri localUri = null;
    String str = getPersistedString(null);
    if (!TextUtils.isEmpty(str)) {
      localUri = Uri.parse(str);
    }
    return localUri;
  }
  
  protected void onSaveRingtone(Uri paramUri)
  {
    if (paramUri != null) {
      paramUri = paramUri.toString();
    } else {
      paramUri = "";
    }
    persistString(paramUri);
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    paramObject = (String)paramObject;
    if (paramBoolean) {
      return;
    }
    if (!TextUtils.isEmpty(paramObject)) {
      onSaveRingtone(Uri.parse(paramObject));
    }
  }
  
  public void setRingtoneType(int paramInt)
  {
    mRingtoneType = paramInt;
  }
  
  public void setShowDefault(boolean paramBoolean)
  {
    mShowDefault = paramBoolean;
  }
  
  public void setShowSilent(boolean paramBoolean)
  {
    mShowSilent = paramBoolean;
  }
}
