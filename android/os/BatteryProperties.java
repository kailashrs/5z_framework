package android.os;

public class BatteryProperties
  implements Parcelable
{
  public static final Parcelable.Creator<BatteryProperties> CREATOR = new Parcelable.Creator()
  {
    public BatteryProperties createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BatteryProperties(paramAnonymousParcel, null);
    }
    
    public BatteryProperties[] newArray(int paramAnonymousInt)
    {
      return new BatteryProperties[paramAnonymousInt];
    }
  };
  public int batteryChargeCounter;
  public int batteryFullCharge;
  public int batteryHealth;
  public int batteryLevel;
  public boolean batteryPresent;
  public int batteryStatus;
  public String batteryTechnology;
  public int batteryTemperature;
  public int batteryVoltage;
  public boolean chargerAcOnline;
  public boolean chargerUsbOnline;
  public boolean chargerWirelessOnline;
  public int maxChargingCurrent;
  public int maxChargingVoltage;
  
  public BatteryProperties() {}
  
  private BatteryProperties(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    chargerAcOnline = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    chargerUsbOnline = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    chargerWirelessOnline = bool2;
    maxChargingCurrent = paramParcel.readInt();
    maxChargingVoltage = paramParcel.readInt();
    batteryStatus = paramParcel.readInt();
    batteryHealth = paramParcel.readInt();
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    }
    batteryPresent = bool2;
    batteryLevel = paramParcel.readInt();
    batteryVoltage = paramParcel.readInt();
    batteryTemperature = paramParcel.readInt();
    batteryFullCharge = paramParcel.readInt();
    batteryChargeCounter = paramParcel.readInt();
    batteryTechnology = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void set(BatteryProperties paramBatteryProperties)
  {
    chargerAcOnline = chargerAcOnline;
    chargerUsbOnline = chargerUsbOnline;
    chargerWirelessOnline = chargerWirelessOnline;
    maxChargingCurrent = maxChargingCurrent;
    maxChargingVoltage = maxChargingVoltage;
    batteryStatus = batteryStatus;
    batteryHealth = batteryHealth;
    batteryPresent = batteryPresent;
    batteryLevel = batteryLevel;
    batteryVoltage = batteryVoltage;
    batteryTemperature = batteryTemperature;
    batteryFullCharge = batteryFullCharge;
    batteryChargeCounter = batteryChargeCounter;
    batteryTechnology = batteryTechnology;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(chargerAcOnline);
    paramParcel.writeInt(chargerUsbOnline);
    paramParcel.writeInt(chargerWirelessOnline);
    paramParcel.writeInt(maxChargingCurrent);
    paramParcel.writeInt(maxChargingVoltage);
    paramParcel.writeInt(batteryStatus);
    paramParcel.writeInt(batteryHealth);
    paramParcel.writeInt(batteryPresent);
    paramParcel.writeInt(batteryLevel);
    paramParcel.writeInt(batteryVoltage);
    paramParcel.writeInt(batteryTemperature);
    paramParcel.writeInt(batteryFullCharge);
    paramParcel.writeInt(batteryChargeCounter);
    paramParcel.writeString(batteryTechnology);
  }
}
