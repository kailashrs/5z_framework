package android.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R.styleable;

public final class PreferenceScreen
  extends PreferenceGroup
  implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener
{
  private Dialog mDialog;
  private Drawable mDividerDrawable;
  private boolean mDividerSpecified;
  private int mLayoutResId = 17367265;
  private ListView mListView;
  private ListAdapter mRootAdapter;
  
  public PreferenceScreen(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 16842891);
    paramContext = paramContext.obtainStyledAttributes(null, R.styleable.PreferenceScreen, 16842891, 0);
    mLayoutResId = paramContext.getResourceId(1, mLayoutResId);
    if (paramContext.hasValueOrEmpty(0))
    {
      mDividerDrawable = paramContext.getDrawable(0);
      mDividerSpecified = true;
    }
    paramContext.recycle();
  }
  
  private void showDialog(Bundle paramBundle)
  {
    Object localObject = getContext();
    if (mListView != null) {
      mListView.setAdapter(null);
    }
    View localView1 = ((LayoutInflater)((Context)localObject).getSystemService("layout_inflater")).inflate(mLayoutResId, null);
    View localView2 = localView1.findViewById(16908310);
    mListView = ((ListView)localView1.findViewById(16908298));
    if (mDividerSpecified) {
      mListView.setDivider(mDividerDrawable);
    }
    bind(mListView);
    CharSequence localCharSequence = getTitle();
    localObject = new Dialog((Context)localObject, ((Context)localObject).getThemeResId());
    mDialog = ((Dialog)localObject);
    if (TextUtils.isEmpty(localCharSequence))
    {
      if (localView2 != null) {
        localView2.setVisibility(8);
      }
      ((Dialog)localObject).getWindow().requestFeature(1);
    }
    else if ((localView2 instanceof TextView))
    {
      ((TextView)localView2).setText(localCharSequence);
      localView2.setVisibility(0);
    }
    else
    {
      ((Dialog)localObject).setTitle(localCharSequence);
    }
    ((Dialog)localObject).setContentView(localView1);
    ((Dialog)localObject).setOnDismissListener(this);
    if (paramBundle != null) {
      ((Dialog)localObject).onRestoreInstanceState(paramBundle);
    }
    getPreferenceManager().addPreferencesScreen((DialogInterface)localObject);
    ((Dialog)localObject).show();
  }
  
  public void bind(ListView paramListView)
  {
    paramListView.setOnItemClickListener(this);
    paramListView.setAdapter(getRootAdapter());
    onAttachedToActivity();
  }
  
  public Dialog getDialog()
  {
    return mDialog;
  }
  
  public ListAdapter getRootAdapter()
  {
    if (mRootAdapter == null) {
      mRootAdapter = onCreateRootAdapter();
    }
    return mRootAdapter;
  }
  
  protected boolean isOnSameScreenAsChildren()
  {
    return false;
  }
  
  protected void onClick()
  {
    if ((getIntent() == null) && (getFragment() == null) && (getPreferenceCount() != 0))
    {
      showDialog(null);
      return;
    }
  }
  
  protected ListAdapter onCreateRootAdapter()
  {
    return new PreferenceGroupAdapter(this);
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    mDialog = null;
    getPreferenceManager().removePreferencesScreen(paramDialogInterface);
  }
  
  public void onItemClick(AdapterView paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    int i = paramInt;
    if ((paramAdapterView instanceof ListView)) {
      i = paramInt - ((ListView)paramAdapterView).getHeaderViewsCount();
    }
    paramAdapterView = getRootAdapter().getItem(i);
    if (!(paramAdapterView instanceof Preference)) {
      return;
    }
    ((Preference)paramAdapterView).performClick(this);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable != null) && (paramParcelable.getClass().equals(SavedState.class)))
    {
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      if (isDialogShowing) {
        showDialog(dialogBundle);
      }
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Object localObject = super.onSaveInstanceState();
    Dialog localDialog = mDialog;
    if ((localDialog != null) && (localDialog.isShowing()))
    {
      localObject = new SavedState((Parcelable)localObject);
      isDialogShowing = true;
      dialogBundle = localDialog.onSaveInstanceState();
      return localObject;
    }
    return localObject;
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public PreferenceScreen.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PreferenceScreen.SavedState(paramAnonymousParcel);
      }
      
      public PreferenceScreen.SavedState[] newArray(int paramAnonymousInt)
      {
        return new PreferenceScreen.SavedState[paramAnonymousInt];
      }
    };
    Bundle dialogBundle;
    boolean isDialogShowing;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      isDialogShowing = bool;
      dialogBundle = paramParcel.readBundle();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(isDialogShowing);
      paramParcel.writeBundle(dialogBundle);
    }
  }
}
