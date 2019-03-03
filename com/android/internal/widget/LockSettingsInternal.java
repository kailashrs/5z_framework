package com.android.internal.widget;

public abstract class LockSettingsInternal
{
  public LockSettingsInternal() {}
  
  public abstract long addEscrowToken(byte[] paramArrayOfByte, int paramInt);
  
  public abstract boolean isEscrowTokenActive(long paramLong, int paramInt);
  
  public abstract boolean removeEscrowToken(long paramLong, int paramInt);
  
  public abstract boolean setLockCredentialWithToken(String paramString, int paramInt1, long paramLong, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
  
  public abstract boolean unlockUserWithToken(long paramLong, byte[] paramArrayOfByte, int paramInt);
}
