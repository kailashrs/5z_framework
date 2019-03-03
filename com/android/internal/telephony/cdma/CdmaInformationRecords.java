package com.android.internal.telephony.cdma;

import android.os.Parcel;

public final class CdmaInformationRecords
{
  public static final int RIL_CDMA_CALLED_PARTY_NUMBER_INFO_REC = 1;
  public static final int RIL_CDMA_CALLING_PARTY_NUMBER_INFO_REC = 2;
  public static final int RIL_CDMA_CONNECTED_NUMBER_INFO_REC = 3;
  public static final int RIL_CDMA_DISPLAY_INFO_REC = 0;
  public static final int RIL_CDMA_EXTENDED_DISPLAY_INFO_REC = 7;
  public static final int RIL_CDMA_LINE_CONTROL_INFO_REC = 6;
  public static final int RIL_CDMA_REDIRECTING_NUMBER_INFO_REC = 5;
  public static final int RIL_CDMA_SIGNAL_INFO_REC = 4;
  public static final int RIL_CDMA_T53_AUDIO_CONTROL_INFO_REC = 10;
  public static final int RIL_CDMA_T53_CLIR_INFO_REC = 8;
  public static final int RIL_CDMA_T53_RELEASE_INFO_REC = 9;
  public Object record;
  
  public CdmaInformationRecords(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    switch (i)
    {
    case 9: 
    default: 
      paramParcel = new StringBuilder();
      paramParcel.append("RIL_UNSOL_CDMA_INFO_REC: unsupported record. Got ");
      paramParcel.append(idToString(i));
      paramParcel.append(" ");
      throw new RuntimeException(paramParcel.toString());
    case 10: 
      record = new CdmaT53AudioControlInfoRec(paramParcel.readInt(), paramParcel.readInt());
      break;
    case 8: 
      record = new CdmaT53ClirInfoRec(paramParcel.readInt());
      break;
    case 6: 
      record = new CdmaLineControlInfoRec(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
      break;
    case 5: 
      record = new CdmaRedirectingNumberInfoRec(paramParcel.readString(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
      break;
    case 4: 
      record = new CdmaSignalInfoRec(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
      break;
    case 1: 
    case 2: 
    case 3: 
      record = new CdmaNumberInfoRec(i, paramParcel.readString(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
      break;
    case 0: 
    case 7: 
      record = new CdmaDisplayInfoRec(i, paramParcel.readString());
    }
  }
  
  public CdmaInformationRecords(CdmaDisplayInfoRec paramCdmaDisplayInfoRec)
  {
    record = paramCdmaDisplayInfoRec;
  }
  
  public CdmaInformationRecords(CdmaLineControlInfoRec paramCdmaLineControlInfoRec)
  {
    record = paramCdmaLineControlInfoRec;
  }
  
  public CdmaInformationRecords(CdmaNumberInfoRec paramCdmaNumberInfoRec)
  {
    record = paramCdmaNumberInfoRec;
  }
  
  public CdmaInformationRecords(CdmaRedirectingNumberInfoRec paramCdmaRedirectingNumberInfoRec)
  {
    record = paramCdmaRedirectingNumberInfoRec;
  }
  
  public CdmaInformationRecords(CdmaSignalInfoRec paramCdmaSignalInfoRec)
  {
    record = paramCdmaSignalInfoRec;
  }
  
  public CdmaInformationRecords(CdmaT53AudioControlInfoRec paramCdmaT53AudioControlInfoRec)
  {
    record = paramCdmaT53AudioControlInfoRec;
  }
  
  public CdmaInformationRecords(CdmaT53ClirInfoRec paramCdmaT53ClirInfoRec)
  {
    record = paramCdmaT53ClirInfoRec;
  }
  
  public static String idToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "<unknown record>";
    case 10: 
      return "RIL_CDMA_T53_AUDIO_CONTROL_INFO_REC";
    case 9: 
      return "RIL_CDMA_T53_RELEASE_INFO_REC";
    case 8: 
      return "RIL_CDMA_T53_CLIR_INFO_REC";
    case 7: 
      return "RIL_CDMA_EXTENDED_DISPLAY_INFO_REC";
    case 6: 
      return "RIL_CDMA_LINE_CONTROL_INFO_REC";
    case 5: 
      return "RIL_CDMA_REDIRECTING_NUMBER_INFO_REC";
    case 4: 
      return "RIL_CDMA_SIGNAL_INFO_REC";
    case 3: 
      return "RIL_CDMA_CONNECTED_NUMBER_INFO_REC";
    case 2: 
      return "RIL_CDMA_CALLING_PARTY_NUMBER_INFO_REC";
    case 1: 
      return "RIL_CDMA_CALLED_PARTY_NUMBER_INFO_REC";
    }
    return "RIL_CDMA_DISPLAY_INFO_REC";
  }
  
  public static class CdmaDisplayInfoRec
  {
    public String alpha;
    public int id;
    
    public CdmaDisplayInfoRec(int paramInt, String paramString)
    {
      id = paramInt;
      alpha = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaDisplayInfoRec: { id: ");
      localStringBuilder.append(CdmaInformationRecords.idToString(id));
      localStringBuilder.append(", alpha: ");
      localStringBuilder.append(alpha);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class CdmaLineControlInfoRec
  {
    public byte lineCtrlPolarityIncluded;
    public byte lineCtrlPowerDenial;
    public byte lineCtrlReverse;
    public byte lineCtrlToggle;
    
    public CdmaLineControlInfoRec(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      lineCtrlPolarityIncluded = ((byte)(byte)paramInt1);
      lineCtrlToggle = ((byte)(byte)paramInt2);
      lineCtrlReverse = ((byte)(byte)paramInt3);
      lineCtrlPowerDenial = ((byte)(byte)paramInt4);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaLineControlInfoRec: { lineCtrlPolarityIncluded: ");
      localStringBuilder.append(lineCtrlPolarityIncluded);
      localStringBuilder.append(" lineCtrlToggle: ");
      localStringBuilder.append(lineCtrlToggle);
      localStringBuilder.append(" lineCtrlReverse: ");
      localStringBuilder.append(lineCtrlReverse);
      localStringBuilder.append(" lineCtrlPowerDenial: ");
      localStringBuilder.append(lineCtrlPowerDenial);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class CdmaNumberInfoRec
  {
    public int id;
    public String number;
    public byte numberPlan;
    public byte numberType;
    public byte pi;
    public byte si;
    
    public CdmaNumberInfoRec(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      number = paramString;
      numberType = ((byte)(byte)paramInt2);
      numberPlan = ((byte)(byte)paramInt3);
      pi = ((byte)(byte)paramInt4);
      si = ((byte)(byte)paramInt5);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaNumberInfoRec: { id: ");
      localStringBuilder.append(CdmaInformationRecords.idToString(id));
      localStringBuilder.append(", number: <MASKED>, numberType: ");
      localStringBuilder.append(numberType);
      localStringBuilder.append(", numberPlan: ");
      localStringBuilder.append(numberPlan);
      localStringBuilder.append(", pi: ");
      localStringBuilder.append(pi);
      localStringBuilder.append(", si: ");
      localStringBuilder.append(si);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class CdmaRedirectingNumberInfoRec
  {
    public static final int REASON_CALLED_DTE_OUT_OF_ORDER = 9;
    public static final int REASON_CALL_FORWARDING_BUSY = 1;
    public static final int REASON_CALL_FORWARDING_BY_THE_CALLED_DTE = 10;
    public static final int REASON_CALL_FORWARDING_NO_REPLY = 2;
    public static final int REASON_CALL_FORWARDING_UNCONDITIONAL = 15;
    public static final int REASON_UNKNOWN = 0;
    public CdmaInformationRecords.CdmaNumberInfoRec numberInfoRec;
    public int redirectingReason;
    
    public CdmaRedirectingNumberInfoRec(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      numberInfoRec = new CdmaInformationRecords.CdmaNumberInfoRec(5, paramString, paramInt1, paramInt2, paramInt3, paramInt4);
      redirectingReason = paramInt5;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaNumberInfoRec: { numberInfoRec: ");
      localStringBuilder.append(numberInfoRec);
      localStringBuilder.append(", redirectingReason: ");
      localStringBuilder.append(redirectingReason);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class CdmaSignalInfoRec
  {
    public int alertPitch;
    public boolean isPresent;
    public int signal;
    public int signalType;
    
    public CdmaSignalInfoRec() {}
    
    public CdmaSignalInfoRec(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      boolean bool;
      if (paramInt1 != 0) {
        bool = true;
      } else {
        bool = false;
      }
      isPresent = bool;
      signalType = paramInt2;
      alertPitch = paramInt3;
      signal = paramInt4;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaSignalInfo: { isPresent: ");
      localStringBuilder.append(isPresent);
      localStringBuilder.append(", signalType: ");
      localStringBuilder.append(signalType);
      localStringBuilder.append(", alertPitch: ");
      localStringBuilder.append(alertPitch);
      localStringBuilder.append(", signal: ");
      localStringBuilder.append(signal);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class CdmaT53AudioControlInfoRec
  {
    public byte downlink;
    public byte uplink;
    
    public CdmaT53AudioControlInfoRec(int paramInt1, int paramInt2)
    {
      uplink = ((byte)(byte)paramInt1);
      downlink = ((byte)(byte)paramInt2);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaT53AudioControlInfoRec: { uplink: ");
      localStringBuilder.append(uplink);
      localStringBuilder.append(" downlink: ");
      localStringBuilder.append(downlink);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class CdmaT53ClirInfoRec
  {
    public byte cause;
    
    public CdmaT53ClirInfoRec(int paramInt)
    {
      cause = ((byte)(byte)paramInt);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CdmaT53ClirInfoRec: { cause: ");
      localStringBuilder.append(cause);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
}
