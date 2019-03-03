package android.text.method;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class CharacterPickerDialog
  extends Dialog
  implements AdapterView.OnItemClickListener, View.OnClickListener
{
  private Button mCancelButton;
  private LayoutInflater mInflater;
  private boolean mInsert;
  private String mOptions;
  private Editable mText;
  private View mView;
  
  public CharacterPickerDialog(Context paramContext, View paramView, Editable paramEditable, String paramString, boolean paramBoolean)
  {
    super(paramContext, 16973913);
    mView = paramView;
    mText = paramEditable;
    mOptions = paramString;
    mInsert = paramBoolean;
    mInflater = LayoutInflater.from(paramContext);
  }
  
  private void replaceCharacterAndClose(CharSequence paramCharSequence)
  {
    int i = Selection.getSelectionEnd(mText);
    if ((!mInsert) && (i != 0)) {
      mText.replace(i - 1, i, paramCharSequence);
    } else {
      mText.insert(i, paramCharSequence);
    }
    dismiss();
  }
  
  public void onClick(View paramView)
  {
    if (paramView == mCancelButton) {
      dismiss();
    } else if ((paramView instanceof Button)) {
      replaceCharacterAndClose(((Button)paramView).getText());
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getWindow().getAttributes();
    token = mView.getApplicationWindowToken();
    type = 1003;
    flags |= 0x1;
    setContentView(17367140);
    paramBundle = (GridView)findViewById(16908849);
    paramBundle.setAdapter(new OptionsAdapter(getContext()));
    paramBundle.setOnItemClickListener(this);
    mCancelButton = ((Button)findViewById(16908840));
    mCancelButton.setOnClickListener(this);
  }
  
  public void onItemClick(AdapterView paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    replaceCharacterAndClose(String.valueOf(mOptions.charAt(paramInt)));
  }
  
  private class OptionsAdapter
    extends BaseAdapter
  {
    public OptionsAdapter(Context paramContext) {}
    
    public final int getCount()
    {
      return mOptions.length();
    }
    
    public final Object getItem(int paramInt)
    {
      return String.valueOf(mOptions.charAt(paramInt));
    }
    
    public final long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (Button)mInflater.inflate(17367141, null);
      paramView.setText(String.valueOf(mOptions.charAt(paramInt)));
      paramView.setOnClickListener(CharacterPickerDialog.this);
      return paramView;
    }
  }
}
