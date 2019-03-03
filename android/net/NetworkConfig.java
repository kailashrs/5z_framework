package android.net;

import java.util.Locale;

public class NetworkConfig
{
  public boolean dependencyMet;
  public String name;
  public int priority;
  public int radio;
  public int restoreTime;
  public int type;
  
  public NetworkConfig(String paramString)
  {
    paramString = paramString.split(",");
    name = paramString[0].trim().toLowerCase(Locale.ROOT);
    type = Integer.parseInt(paramString[1]);
    radio = Integer.parseInt(paramString[2]);
    priority = Integer.parseInt(paramString[3]);
    restoreTime = Integer.parseInt(paramString[4]);
    dependencyMet = Boolean.parseBoolean(paramString[5]);
  }
  
  public boolean isDefault()
  {
    boolean bool;
    if (type == radio) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
