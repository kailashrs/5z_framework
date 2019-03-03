package android.text;

public abstract interface TextWatcher
  extends NoCopySpan
{
  public abstract void afterTextChanged(Editable paramEditable);
  
  public abstract void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3);
}
