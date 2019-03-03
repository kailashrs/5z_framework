package android.telecom;

public abstract interface Response<IN, OUT>
{
  public abstract void onError(IN paramIN, int paramInt, String paramString);
  
  public abstract void onResult(IN paramIN, OUT... paramVarArgs);
}
