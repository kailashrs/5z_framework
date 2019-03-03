package android.view.autofill;

public abstract class AutofillManagerInternal
{
  public AutofillManagerInternal() {}
  
  public abstract boolean isCompatibilityModeRequested(String paramString, long paramLong, int paramInt);
  
  public abstract void onBackKeyPressed();
}
