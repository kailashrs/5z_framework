package com.android.ims;

import android.os.Handler;
import android.os.Message;

public abstract interface ImsUtInterface
{
  public static final int ACTION_ACTIVATION = 1;
  public static final int ACTION_DEACTIVATION = 0;
  public static final int ACTION_ERASURE = 4;
  public static final int ACTION_INTERROGATION = 5;
  public static final int ACTION_REGISTRATION = 3;
  public static final int CB_BAIC = 1;
  public static final int CB_BAOC = 2;
  public static final int CB_BA_ALL = 7;
  public static final int CB_BA_MO = 8;
  public static final int CB_BA_MT = 9;
  public static final int CB_BIC_ACR = 6;
  public static final int CB_BIC_WR = 5;
  public static final int CB_BOIC = 3;
  public static final int CB_BOIC_EXHC = 4;
  public static final int CB_BS_MT = 10;
  public static final int CDIV_CF_ALL = 4;
  public static final int CDIV_CF_ALL_CONDITIONAL = 5;
  public static final int CDIV_CF_BUSY = 1;
  public static final int CDIV_CF_NOT_LOGGED_IN = 6;
  public static final int CDIV_CF_NOT_REACHABLE = 3;
  public static final int CDIV_CF_NO_REPLY = 2;
  public static final int CDIV_CF_UNCONDITIONAL = 0;
  public static final int INVALID = -1;
  public static final int OIR_DEFAULT = 0;
  public static final int OIR_PRESENTATION_NOT_RESTRICTED = 2;
  public static final int OIR_PRESENTATION_RESTRICTED = 1;
  
  public abstract void queryCLIP(Message paramMessage);
  
  public abstract void queryCLIR(Message paramMessage);
  
  public abstract void queryCOLP(Message paramMessage);
  
  public abstract void queryCOLR(Message paramMessage);
  
  public abstract void queryCallBarring(int paramInt, Message paramMessage);
  
  public abstract void queryCallBarring(int paramInt1, Message paramMessage, int paramInt2);
  
  public abstract void queryCallForward(int paramInt1, String paramString, int paramInt2, Message paramMessage);
  
  public abstract void queryCallForward(int paramInt, String paramString, Message paramMessage);
  
  public abstract void queryCallWaiting(Message paramMessage);
  
  public abstract void registerForSuppServiceIndication(Handler paramHandler, int paramInt, Object paramObject);
  
  public abstract void unregisterForSuppServiceIndication(Handler paramHandler);
  
  public abstract void updateCLIP(boolean paramBoolean, Message paramMessage);
  
  public abstract void updateCLIR(int paramInt, Message paramMessage);
  
  public abstract void updateCOLP(boolean paramBoolean, Message paramMessage);
  
  public abstract void updateCOLR(int paramInt, Message paramMessage);
  
  public abstract void updateCallBarring(int paramInt1, int paramInt2, Message paramMessage, String[] paramArrayOfString);
  
  public abstract void updateCallBarring(int paramInt1, int paramInt2, Message paramMessage, String[] paramArrayOfString, int paramInt3);
  
  public abstract void updateCallForward(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, Message paramMessage);
  
  public abstract void updateCallWaiting(boolean paramBoolean, int paramInt, Message paramMessage);
}
