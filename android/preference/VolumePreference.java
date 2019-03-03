package android.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.SeekBar;
import com.android.internal.R.styleable;

public class VolumePreference
  extends SeekBarDialogPreference
  implements PreferenceManager.OnActivityStopListener, View.OnKeyListener, SeekBarVolumizer.Callback
{
  private SeekBarVolumizer mSeekBarVolumizer;
  private int mStreamType;
  
  public VolumePreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public VolumePreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 17891522);
  }
  
  public VolumePreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public VolumePreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.VolumePreference, paramInt1, paramInt2);
    mStreamType = paramContext.getInt(0, 0);
    paramContext.recycle();
  }
  
  private void cleanup()
  {
    getPreferenceManager().unregisterOnActivityStopListener(this);
    if (mSeekBarVolumizer != null)
    {
      Object localObject = getDialog();
      if ((localObject != null) && (((Dialog)localObject).isShowing()))
      {
        localObject = ((Dialog)localObject).getWindow().getDecorView().findViewById(16909333);
        if (localObject != null) {
          ((View)localObject).setOnKeyListener(null);
        }
        mSeekBarVolumizer.revertVolume();
      }
      mSeekBarVolumizer.stop();
      mSeekBarVolumizer = null;
    }
  }
  
  public void onActivityStop()
  {
    if (mSeekBarVolumizer != null) {
      mSeekBarVolumizer.stopSample();
    }
  }
  
  protected void onBindDialogView(View paramView)
  {
    super.onBindDialogView(paramView);
    SeekBar localSeekBar = (SeekBar)paramView.findViewById(16909333);
    mSeekBarVolumizer = new SeekBarVolumizer(getContext(), mStreamType, null, this);
    mSeekBarVolumizer.start();
    mSeekBarVolumizer.setSeekBar(localSeekBar);
    getPreferenceManager().registerOnActivityStopListener(this);
    paramView.setOnKeyListener(this);
    paramView.setFocusableInTouchMode(true);
    paramView.requestFocus();
  }
  
  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if ((!paramBoolean) && (mSeekBarVolumizer != null)) {
      mSeekBarVolumizer.revertVolume();
    }
    cleanup();
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    if (mSeekBarVolumizer == null) {
      return true;
    }
    int i;
    if (paramKeyEvent.getAction() == 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (paramInt != 164)
    {
      switch (paramInt)
      {
      default: 
        return false;
      case 25: 
        if (i != 0) {
          mSeekBarVolumizer.changeVolumeBy(-1);
        }
        return true;
      }
      if (i != 0) {
        mSeekBarVolumizer.changeVolumeBy(1);
      }
      return true;
    }
    if (i != 0) {
      mSeekBarVolumizer.muteVolume();
    }
    return true;
  }
  
  public void onMuted(boolean paramBoolean1, boolean paramBoolean2) {}
  
  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean) {}
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable != null) && (paramParcelable.getClass().equals(SavedState.class)))
    {
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      if (mSeekBarVolumizer != null) {
        mSeekBarVolumizer.onRestoreInstanceState(paramParcelable.getVolumeStore());
      }
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }
  
  public void onSampleStarting(SeekBarVolumizer paramSeekBarVolumizer)
  {
    if ((mSeekBarVolumizer != null) && (paramSeekBarVolumizer != mSeekBarVolumizer)) {
      mSeekBarVolumizer.stopSample();
    }
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Object localObject = super.onSaveInstanceState();
    if (isPersistent()) {
      return localObject;
    }
    localObject = new SavedState((Parcelable)localObject);
    if (mSeekBarVolumizer != null) {
      mSeekBarVolumizer.onSaveInstanceState(((SavedState)localObject).getVolumeStore());
    }
    return localObject;
  }
  
  public void setStreamType(int paramInt)
  {
    mStreamType = paramInt;
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public VolumePreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new VolumePreference.SavedState(paramAnonymousParcel);
      }
      
      public VolumePreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new VolumePreference.SavedState[paramAnonymousInt];
      }
    };
    VolumePreference.VolumeStore mVolumeStore = new VolumePreference.VolumeStore();
    
    public SavedState(Parcel paramParcel)
    {
      super();
      mVolumeStore.volume = paramParcel.readInt();
      mVolumeStore.originalVolume = paramParcel.readInt();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    VolumePreference.VolumeStore getVolumeStore()
    {
      return mVolumeStore;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(mVolumeStore.volume);
      paramParcel.writeInt(mVolumeStore.originalVolume);
    }
  }
  
  public static class VolumeStore
  {
    public int originalVolume = -1;
    public int volume = -1;
    
    public VolumeStore() {}
  }
}
