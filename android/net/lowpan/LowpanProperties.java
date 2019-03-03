package android.net.lowpan;

public final class LowpanProperties
{
  public static final LowpanProperty<int[]> KEY_CHANNEL_MASK = new LowpanStandardProperty("android.net.lowpan.property.CHANNEL_MASK", [I.class);
  public static final LowpanProperty<Integer> KEY_MAX_TX_POWER = new LowpanStandardProperty("android.net.lowpan.property.MAX_TX_POWER", Integer.class);
  
  private LowpanProperties() {}
  
  static final class LowpanStandardProperty<T>
    extends LowpanProperty<T>
  {
    private final String mName;
    private final Class<T> mType;
    
    LowpanStandardProperty(String paramString, Class<T> paramClass)
    {
      mName = paramString;
      mType = paramClass;
    }
    
    public String getName()
    {
      return mName;
    }
    
    public Class<T> getType()
    {
      return mType;
    }
    
    public String toString()
    {
      return getName();
    }
  }
}
