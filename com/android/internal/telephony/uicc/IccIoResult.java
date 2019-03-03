package com.android.internal.telephony.uicc;

import android.os.Build;

public class IccIoResult
{
  private static final String UNKNOWN_ERROR = "unknown";
  public byte[] payload;
  public int sw1;
  public int sw2;
  
  public IccIoResult(int paramInt1, int paramInt2, String paramString)
  {
    this(paramInt1, paramInt2, IccUtils.hexStringToBytes(paramString));
  }
  
  public IccIoResult(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    sw1 = paramInt1;
    sw2 = paramInt2;
    payload = paramArrayOfByte;
  }
  
  private String getErrorString()
  {
    int i = sw1;
    if (i != 152)
    {
      switch (i)
      {
      default: 
        switch (i)
        {
        default: 
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              switch (i)
              {
              default: 
                break;
              case 159: 
                return null;
              case 158: 
                return null;
              }
              break;
            case 148: 
              i = sw2;
              if (i != 0)
              {
                if (i != 2)
                {
                  if (i != 4)
                  {
                    if (i != 8) {
                      break;
                    }
                    return "file is inconsistent with the command";
                  }
                  return "file ID not found/pattern not found";
                }
                return "out f range (invalid address)";
              }
              return "no EF selected";
            case 147: 
              if (sw2 != 0) {
                break;
              }
              return "SIM Application Toolkit is busy. Command cannot be executed at present, further normal commands are allowed.";
            case 146: 
              if (sw2 >> 4 == 0) {
                return "command successful but after using an internal update retry routine";
              }
              if (sw2 != 64) {
                break;
              }
              return "memory problem";
            case 145: 
              return null;
            case 144: 
              return null;
            }
            break;
          case 111: 
            if (sw2 != 0) {
              return "The interpretation of this status word is command dependent";
            }
            return "technical problem with no diagnostic given";
          case 110: 
            return "wrong instruction class given in the command";
          case 109: 
            return "unknown instruction code given in the command";
          }
          break;
        case 107: 
          return "incorrect parameter P1 or P2";
        case 106: 
          switch (sw2)
          {
          case 133: 
          default: 
            break;
          case 136: 
            return "Referenced data not found";
          case 135: 
            return "Lc inconsistent with P1 to P2";
          case 134: 
            return "Incorrect parameters P1 to P2";
          case 132: 
            return "Not enough memory space";
          case 131: 
            return "Record not found";
          case 130: 
            return "File not found";
          case 129: 
            return "Function not supported";
          case 128: 
            return "Incorrect parameters in the data field";
          }
          break;
        case 105: 
          i = sw2;
          if (i != 0)
          {
            if (i != 137) {
              switch (i)
              {
              default: 
                break;
              case 134: 
                return "Command not allowed (no EF selected)";
              case 133: 
                return "Conditions of use not satisfied";
              case 132: 
                return "Referenced data invalidated";
              case 131: 
                return "Authentication/PIN method blocked";
              case 130: 
                return "Security status not satisfied";
              case 129: 
                return "Command incompatible with file structure";
              }
            }
            return "Command not allowed - secure channel - security not satisfied";
          }
          return "No information given";
        case 104: 
          i = sw2;
          if (i != 0) {
            switch (i)
            {
            default: 
              break;
            case 130: 
              return "Secure messaging not supported";
            case 129: 
              return "Logical channel not supported";
            }
          }
          return "No information given";
        case 103: 
          if (sw2 != 0) {
            return "The interpretation of this status word is command dependent";
          }
          return "incorrect parameter P3";
        }
        break;
      case 101: 
        i = sw2;
        if (i != 0)
        {
          if (i != 129) {
            break;
          }
          return "Memory problem";
        }
        return "No information given, state of non-volatile memory changed";
      case 100: 
        if (sw2 != 0) {
          break;
        }
        return "No information given, state of non-volatile memory unchanged";
      case 99: 
        if (sw2 >> 4 == 12) {
          return "Command successful but after using an internalupdate retry routine but Verification failed";
        }
        switch (sw2)
        {
        default: 
          break;
        case 242: 
          return "More data expected and proactive command pending";
        case 241: 
          return "More data expected";
        }
        break;
      case 98: 
        i = sw2;
        if (i != 0) {
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              break;
            case 243: 
              return "Response data available";
            case 242: 
              return "More data available and proactive command pending";
            case 241: 
              return "More data available";
            }
            break;
          case 132: 
            return "Selected file in termination state";
          case 131: 
            return "Selected file invalidated";
          case 130: 
            return "End of file/record reached before reading Le bytes";
          case 129: 
            return "Part of returned data may be corrupted";
          }
        }
        return "No information given, state of non volatile memory unchanged";
      }
    }
    else
    {
      i = sw2;
      if (i == 2) {
        break label797;
      }
      if (i == 4) {
        break label794;
      }
      if (i == 8) {
        break label791;
      }
      if (i == 16) {
        break label788;
      }
      if (i == 64) {
        break label785;
      }
      if (i == 80) {
        break label782;
      }
      if (i == 98) {
        break label779;
      }
    }
    switch (i)
    {
    default: 
      return "unknown";
    case 103: 
      return "authentication error, no memory space available in EF_MUK";
    case 102: 
      return "authentication error, no memory space available";
    case 101: 
      return "key freshness failure";
    }
    return "authentication error, security context not supported";
    label779:
    return "authentication error, application specific";
    label782:
    return "increase cannot be performed, Max value reached";
    label785:
    return "unsuccessful CHV verification, no attempt left/unsuccessful UNBLOCK CHV verification, no attempt left/CHV blockedUNBLOCK CHV blocked";
    label788:
    return "in contradiction with invalidation status";
    label791:
    return "in contradiction with CHV status";
    label794:
    return "access condition not fulfilled/unsuccessful CHV verification, at least one attempt left/unsuccessful UNBLOCK CHV verification, at least one attempt left/authentication failed";
    label797:
    return "no CHV initialized";
  }
  
  public IccException getException()
  {
    if (success()) {
      return null;
    }
    if (sw1 != 148)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("sw1:");
      localStringBuilder.append(sw1);
      localStringBuilder.append(" sw2:");
      localStringBuilder.append(sw2);
      return new IccException(localStringBuilder.toString());
    }
    if (sw2 == 8) {
      return new IccFileTypeMismatch();
    }
    return new IccFileNotFound();
  }
  
  public boolean success()
  {
    boolean bool;
    if ((sw1 != 144) && (sw1 != 145) && (sw1 != 158) && (sw1 != 159)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IccIoResult sw1:0x");
    localStringBuilder.append(Integer.toHexString(sw1));
    localStringBuilder.append(" sw2:0x");
    localStringBuilder.append(Integer.toHexString(sw2));
    localStringBuilder.append(" Payload: ");
    Object localObject;
    if ((Build.IS_DEBUGGABLE) && (Build.IS_ENG)) {
      localObject = payload;
    } else {
      localObject = "*******";
    }
    localStringBuilder.append(localObject);
    if (!success())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" Error: ");
      ((StringBuilder)localObject).append(getErrorString());
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    return localStringBuilder.toString();
  }
}
