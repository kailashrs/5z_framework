package com.android.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

class CommandDetails
  extends ValueObject
  implements Parcelable
{
  public static final Parcelable.Creator<CommandDetails> CREATOR = new Parcelable.Creator()
  {
    public CommandDetails createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CommandDetails(paramAnonymousParcel);
    }
    
    public CommandDetails[] newArray(int paramAnonymousInt)
    {
      return new CommandDetails[paramAnonymousInt];
    }
  };
  public int commandNumber;
  public int commandQualifier;
  public boolean compRequired;
  public int typeOfCommand;
  
  CommandDetails() {}
  
  public CommandDetails(Parcel paramParcel)
  {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    compRequired = bool;
    commandNumber = paramParcel.readInt();
    typeOfCommand = paramParcel.readInt();
    commandQualifier = paramParcel.readInt();
  }
  
  public boolean compareTo(CommandDetails paramCommandDetails)
  {
    boolean bool;
    if ((compRequired == compRequired) && (commandNumber == commandNumber) && (commandQualifier == commandQualifier) && (typeOfCommand == typeOfCommand)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ComprehensionTlvTag getTag()
  {
    return ComprehensionTlvTag.COMMAND_DETAILS;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CmdDetails: compRequired=");
    localStringBuilder.append(compRequired);
    localStringBuilder.append(" commandNumber=");
    localStringBuilder.append(commandNumber);
    localStringBuilder.append(" typeOfCommand=");
    localStringBuilder.append(typeOfCommand);
    localStringBuilder.append(" commandQualifier=");
    localStringBuilder.append(commandQualifier);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(compRequired);
    paramParcel.writeInt(commandNumber);
    paramParcel.writeInt(typeOfCommand);
    paramParcel.writeInt(commandQualifier);
  }
}
