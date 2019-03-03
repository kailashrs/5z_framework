package android.animation;

public abstract class BidirectionalTypeConverter<T, V>
  extends TypeConverter<T, V>
{
  private BidirectionalTypeConverter mInvertedConverter;
  
  public BidirectionalTypeConverter(Class<T> paramClass, Class<V> paramClass1)
  {
    super(paramClass, paramClass1);
  }
  
  public abstract T convertBack(V paramV);
  
  public BidirectionalTypeConverter<V, T> invert()
  {
    if (mInvertedConverter == null) {
      mInvertedConverter = new InvertedConverter(this);
    }
    return mInvertedConverter;
  }
  
  private static class InvertedConverter<From, To>
    extends BidirectionalTypeConverter<From, To>
  {
    private BidirectionalTypeConverter<To, From> mConverter;
    
    public InvertedConverter(BidirectionalTypeConverter<To, From> paramBidirectionalTypeConverter)
    {
      super(paramBidirectionalTypeConverter.getSourceType());
      mConverter = paramBidirectionalTypeConverter;
    }
    
    public To convert(From paramFrom)
    {
      return mConverter.convertBack(paramFrom);
    }
    
    public From convertBack(To paramTo)
    {
      return mConverter.convert(paramTo);
    }
  }
}
