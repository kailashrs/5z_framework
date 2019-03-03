package com.android.internal.telephony;

import java.util.BitSet;

public class HardwareConfig
{
  public static final int DEV_HARDWARE_STATE_DISABLED = 2;
  public static final int DEV_HARDWARE_STATE_ENABLED = 0;
  public static final int DEV_HARDWARE_STATE_STANDBY = 1;
  public static final int DEV_HARDWARE_TYPE_MODEM = 0;
  public static final int DEV_HARDWARE_TYPE_SIM = 1;
  public static final int DEV_MODEM_RIL_MODEL_MULTIPLE = 1;
  public static final int DEV_MODEM_RIL_MODEL_SINGLE = 0;
  static final String LOG_TAG = "HardwareConfig";
  public int maxActiveDataCall;
  public int maxActiveVoiceCall;
  public int maxStandby;
  public String modemUuid;
  public BitSet rat;
  public int rilModel;
  public int state;
  public int type;
  public String uuid;
  
  public HardwareConfig(int paramInt)
  {
    type = paramInt;
  }
  
  public HardwareConfig(String paramString)
  {
    paramString = paramString.split(",");
    type = Integer.parseInt(paramString[0]);
    switch (type)
    {
    default: 
      break;
    case 1: 
      assignSim(paramString[1].trim(), Integer.parseInt(paramString[2]), paramString[3].trim());
      break;
    case 0: 
      assignModem(paramString[1].trim(), Integer.parseInt(paramString[2]), Integer.parseInt(paramString[3]), Integer.parseInt(paramString[4]), Integer.parseInt(paramString[5]), Integer.parseInt(paramString[6]), Integer.parseInt(paramString[7]));
    }
  }
  
  public void assignModem(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (type == 0)
    {
      char[] arrayOfChar = Integer.toBinaryString(paramInt3).toCharArray();
      uuid = paramString;
      state = paramInt1;
      rilModel = paramInt2;
      rat = new BitSet(arrayOfChar.length);
      for (paramInt1 = 0; paramInt1 < arrayOfChar.length; paramInt1++)
      {
        paramString = rat;
        boolean bool;
        if (arrayOfChar[paramInt1] == '1') {
          bool = true;
        } else {
          bool = false;
        }
        paramString.set(paramInt1, bool);
      }
      maxActiveVoiceCall = paramInt4;
      maxActiveDataCall = paramInt5;
      maxStandby = paramInt6;
    }
  }
  
  public void assignSim(String paramString1, int paramInt, String paramString2)
  {
    if (type == 1)
    {
      uuid = paramString1;
      modemUuid = paramString2;
      state = paramInt;
    }
  }
  
  public int compareTo(HardwareConfig paramHardwareConfig)
  {
    return toString().compareTo(paramHardwareConfig.toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2;
    if (type == 0)
    {
      localStringBuilder1.append("Modem ");
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("{ uuid=");
      localStringBuilder2.append(uuid);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", state=");
      localStringBuilder2.append(state);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", rilModel=");
      localStringBuilder2.append(rilModel);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", rat=");
      localStringBuilder2.append(rat.toString());
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", maxActiveVoiceCall=");
      localStringBuilder2.append(maxActiveVoiceCall);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", maxActiveDataCall=");
      localStringBuilder2.append(maxActiveDataCall);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", maxStandby=");
      localStringBuilder2.append(maxStandby);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder1.append(" }");
    }
    else if (type == 1)
    {
      localStringBuilder1.append("Sim ");
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("{ uuid=");
      localStringBuilder2.append(uuid);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", modemUuid=");
      localStringBuilder2.append(modemUuid);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", state=");
      localStringBuilder2.append(state);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder1.append(" }");
    }
    else
    {
      localStringBuilder1.append("Invalid Configration");
    }
    return localStringBuilder1.toString();
  }
}
