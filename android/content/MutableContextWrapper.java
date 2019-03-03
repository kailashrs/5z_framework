package android.content;

public class MutableContextWrapper
  extends ContextWrapper
{
  public MutableContextWrapper(Context paramContext)
  {
    super(paramContext);
  }
  
  public void setBaseContext(Context paramContext)
  {
    mBase = paramContext;
  }
}
