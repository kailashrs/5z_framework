package com.android.internal.telephony.test;

public abstract interface SimulatedRadioControl
{
  public abstract void pauseResponses();
  
  public abstract void progressConnectingCallState();
  
  public abstract void progressConnectingToActive();
  
  public abstract void resumeResponses();
  
  public abstract void setAutoProgressConnectingCall(boolean paramBoolean);
  
  public abstract void setNextCallFailCause(int paramInt);
  
  public abstract void setNextDialFailImmediately(boolean paramBoolean);
  
  public abstract void shutdown();
  
  public abstract void triggerHangupAll();
  
  public abstract void triggerHangupBackground();
  
  public abstract void triggerHangupForeground();
  
  public abstract void triggerIncomingSMS(String paramString);
  
  public abstract void triggerIncomingUssd(String paramString1, String paramString2);
  
  public abstract void triggerRing(String paramString);
  
  public abstract void triggerSsn(int paramInt1, int paramInt2);
}
