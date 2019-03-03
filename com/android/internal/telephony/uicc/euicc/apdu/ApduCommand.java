package com.android.internal.telephony.uicc.euicc.apdu;

class ApduCommand
{
  public final int channel;
  public final int cla;
  public final String cmdHex;
  public final int ins;
  public final int p1;
  public final int p2;
  public final int p3;
  
  ApduCommand(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString)
  {
    channel = paramInt1;
    cla = paramInt2;
    ins = paramInt3;
    p1 = paramInt4;
    p2 = paramInt5;
    p3 = paramInt6;
    cmdHex = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ApduCommand(channel=");
    localStringBuilder.append(channel);
    localStringBuilder.append(", cla=");
    localStringBuilder.append(cla);
    localStringBuilder.append(", ins=");
    localStringBuilder.append(ins);
    localStringBuilder.append(", p1=");
    localStringBuilder.append(p1);
    localStringBuilder.append(", p2=");
    localStringBuilder.append(p2);
    localStringBuilder.append(", p3=");
    localStringBuilder.append(p3);
    localStringBuilder.append(", cmd=");
    localStringBuilder.append(cmdHex);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
