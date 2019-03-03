package android.os;

import android.annotation.SystemApi;

@SystemApi
public abstract interface IHwBinder
{
  @SystemApi
  public abstract boolean linkToDeath(DeathRecipient paramDeathRecipient, long paramLong);
  
  @SystemApi
  public abstract IHwInterface queryLocalInterface(String paramString);
  
  @SystemApi
  public abstract void transact(int paramInt1, HwParcel paramHwParcel1, HwParcel paramHwParcel2, int paramInt2)
    throws RemoteException;
  
  @SystemApi
  public abstract boolean unlinkToDeath(DeathRecipient paramDeathRecipient);
  
  @SystemApi
  public static abstract interface DeathRecipient
  {
    @SystemApi
    public abstract void serviceDied(long paramLong);
  }
}
