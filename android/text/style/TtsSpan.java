package android.text.style;

import android.os.Parcel;
import android.os.PersistableBundle;
import android.text.ParcelableSpan;
import java.text.NumberFormat;
import java.util.Locale;

public class TtsSpan
  implements ParcelableSpan
{
  public static final String ANIMACY_ANIMATE = "android.animate";
  public static final String ANIMACY_INANIMATE = "android.inanimate";
  public static final String ARG_ANIMACY = "android.arg.animacy";
  public static final String ARG_CASE = "android.arg.case";
  public static final String ARG_COUNTRY_CODE = "android.arg.country_code";
  public static final String ARG_CURRENCY = "android.arg.money";
  public static final String ARG_DAY = "android.arg.day";
  public static final String ARG_DENOMINATOR = "android.arg.denominator";
  public static final String ARG_DIGITS = "android.arg.digits";
  public static final String ARG_DOMAIN = "android.arg.domain";
  public static final String ARG_EXTENSION = "android.arg.extension";
  public static final String ARG_FRACTIONAL_PART = "android.arg.fractional_part";
  public static final String ARG_FRAGMENT_ID = "android.arg.fragment_id";
  public static final String ARG_GENDER = "android.arg.gender";
  public static final String ARG_HOURS = "android.arg.hours";
  public static final String ARG_INTEGER_PART = "android.arg.integer_part";
  public static final String ARG_MINUTES = "android.arg.minutes";
  public static final String ARG_MONTH = "android.arg.month";
  public static final String ARG_MULTIPLICITY = "android.arg.multiplicity";
  public static final String ARG_NUMBER = "android.arg.number";
  public static final String ARG_NUMBER_PARTS = "android.arg.number_parts";
  public static final String ARG_NUMERATOR = "android.arg.numerator";
  public static final String ARG_PASSWORD = "android.arg.password";
  public static final String ARG_PATH = "android.arg.path";
  public static final String ARG_PORT = "android.arg.port";
  public static final String ARG_PROTOCOL = "android.arg.protocol";
  public static final String ARG_QUANTITY = "android.arg.quantity";
  public static final String ARG_QUERY_STRING = "android.arg.query_string";
  public static final String ARG_TEXT = "android.arg.text";
  public static final String ARG_UNIT = "android.arg.unit";
  public static final String ARG_USERNAME = "android.arg.username";
  public static final String ARG_VERBATIM = "android.arg.verbatim";
  public static final String ARG_WEEKDAY = "android.arg.weekday";
  public static final String ARG_YEAR = "android.arg.year";
  public static final String CASE_ABLATIVE = "android.ablative";
  public static final String CASE_ACCUSATIVE = "android.accusative";
  public static final String CASE_DATIVE = "android.dative";
  public static final String CASE_GENITIVE = "android.genitive";
  public static final String CASE_INSTRUMENTAL = "android.instrumental";
  public static final String CASE_LOCATIVE = "android.locative";
  public static final String CASE_NOMINATIVE = "android.nominative";
  public static final String CASE_VOCATIVE = "android.vocative";
  public static final String GENDER_FEMALE = "android.female";
  public static final String GENDER_MALE = "android.male";
  public static final String GENDER_NEUTRAL = "android.neutral";
  public static final int MONTH_APRIL = 3;
  public static final int MONTH_AUGUST = 7;
  public static final int MONTH_DECEMBER = 11;
  public static final int MONTH_FEBRUARY = 1;
  public static final int MONTH_JANUARY = 0;
  public static final int MONTH_JULY = 6;
  public static final int MONTH_JUNE = 5;
  public static final int MONTH_MARCH = 2;
  public static final int MONTH_MAY = 4;
  public static final int MONTH_NOVEMBER = 10;
  public static final int MONTH_OCTOBER = 9;
  public static final int MONTH_SEPTEMBER = 8;
  public static final String MULTIPLICITY_DUAL = "android.dual";
  public static final String MULTIPLICITY_PLURAL = "android.plural";
  public static final String MULTIPLICITY_SINGLE = "android.single";
  public static final String TYPE_CARDINAL = "android.type.cardinal";
  public static final String TYPE_DATE = "android.type.date";
  public static final String TYPE_DECIMAL = "android.type.decimal";
  public static final String TYPE_DIGITS = "android.type.digits";
  public static final String TYPE_ELECTRONIC = "android.type.electronic";
  public static final String TYPE_FRACTION = "android.type.fraction";
  public static final String TYPE_MEASURE = "android.type.measure";
  public static final String TYPE_MONEY = "android.type.money";
  public static final String TYPE_ORDINAL = "android.type.ordinal";
  public static final String TYPE_TELEPHONE = "android.type.telephone";
  public static final String TYPE_TEXT = "android.type.text";
  public static final String TYPE_TIME = "android.type.time";
  public static final String TYPE_VERBATIM = "android.type.verbatim";
  public static final int WEEKDAY_FRIDAY = 6;
  public static final int WEEKDAY_MONDAY = 2;
  public static final int WEEKDAY_SATURDAY = 7;
  public static final int WEEKDAY_SUNDAY = 1;
  public static final int WEEKDAY_THURSDAY = 5;
  public static final int WEEKDAY_TUESDAY = 3;
  public static final int WEEKDAY_WEDNESDAY = 4;
  private final PersistableBundle mArgs;
  private final String mType;
  
  public TtsSpan(Parcel paramParcel)
  {
    mType = paramParcel.readString();
    mArgs = paramParcel.readPersistableBundle();
  }
  
  public TtsSpan(String paramString, PersistableBundle paramPersistableBundle)
  {
    mType = paramString;
    mArgs = paramPersistableBundle;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PersistableBundle getArgs()
  {
    return mArgs;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 24;
  }
  
  public String getType()
  {
    return mType;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mType);
    paramParcel.writePersistableBundle(mArgs);
  }
  
  public static class Builder<C extends Builder<?>>
  {
    private PersistableBundle mArgs = new PersistableBundle();
    private final String mType;
    
    public Builder(String paramString)
    {
      mType = paramString;
    }
    
    public TtsSpan build()
    {
      return new TtsSpan(mType, mArgs);
    }
    
    public C setIntArgument(String paramString, int paramInt)
    {
      mArgs.putInt(paramString, paramInt);
      return this;
    }
    
    public C setLongArgument(String paramString, long paramLong)
    {
      mArgs.putLong(paramString, paramLong);
      return this;
    }
    
    public C setStringArgument(String paramString1, String paramString2)
    {
      mArgs.putString(paramString1, paramString2);
      return this;
    }
  }
  
  public static class CardinalBuilder
    extends TtsSpan.SemioticClassBuilder<CardinalBuilder>
  {
    public CardinalBuilder()
    {
      super();
    }
    
    public CardinalBuilder(long paramLong)
    {
      this();
      setNumber(paramLong);
    }
    
    public CardinalBuilder(String paramString)
    {
      this();
      setNumber(paramString);
    }
    
    public CardinalBuilder setNumber(long paramLong)
    {
      return setNumber(String.valueOf(paramLong));
    }
    
    public CardinalBuilder setNumber(String paramString)
    {
      return (CardinalBuilder)setStringArgument("android.arg.number", paramString);
    }
  }
  
  public static class DateBuilder
    extends TtsSpan.SemioticClassBuilder<DateBuilder>
  {
    public DateBuilder()
    {
      super();
    }
    
    public DateBuilder(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, Integer paramInteger4)
    {
      this();
      if (paramInteger1 != null) {
        setWeekday(paramInteger1.intValue());
      }
      if (paramInteger2 != null) {
        setDay(paramInteger2.intValue());
      }
      if (paramInteger3 != null) {
        setMonth(paramInteger3.intValue());
      }
      if (paramInteger4 != null) {
        setYear(paramInteger4.intValue());
      }
    }
    
    public DateBuilder setDay(int paramInt)
    {
      return (DateBuilder)setIntArgument("android.arg.day", paramInt);
    }
    
    public DateBuilder setMonth(int paramInt)
    {
      return (DateBuilder)setIntArgument("android.arg.month", paramInt);
    }
    
    public DateBuilder setWeekday(int paramInt)
    {
      return (DateBuilder)setIntArgument("android.arg.weekday", paramInt);
    }
    
    public DateBuilder setYear(int paramInt)
    {
      return (DateBuilder)setIntArgument("android.arg.year", paramInt);
    }
  }
  
  public static class DecimalBuilder
    extends TtsSpan.SemioticClassBuilder<DecimalBuilder>
  {
    public DecimalBuilder()
    {
      super();
    }
    
    public DecimalBuilder(double paramDouble, int paramInt1, int paramInt2)
    {
      this();
      setArgumentsFromDouble(paramDouble, paramInt1, paramInt2);
    }
    
    public DecimalBuilder(String paramString1, String paramString2)
    {
      this();
      setIntegerPart(paramString1);
      setFractionalPart(paramString2);
    }
    
    public DecimalBuilder setArgumentsFromDouble(double paramDouble, int paramInt1, int paramInt2)
    {
      Object localObject = NumberFormat.getInstance(Locale.US);
      ((NumberFormat)localObject).setMinimumFractionDigits(paramInt2);
      ((NumberFormat)localObject).setMaximumFractionDigits(paramInt2);
      ((NumberFormat)localObject).setGroupingUsed(false);
      localObject = ((NumberFormat)localObject).format(paramDouble);
      paramInt1 = ((String)localObject).indexOf('.');
      if (paramInt1 >= 0)
      {
        setIntegerPart(((String)localObject).substring(0, paramInt1));
        setFractionalPart(((String)localObject).substring(paramInt1 + 1));
      }
      else
      {
        setIntegerPart((String)localObject);
      }
      return this;
    }
    
    public DecimalBuilder setFractionalPart(String paramString)
    {
      return (DecimalBuilder)setStringArgument("android.arg.fractional_part", paramString);
    }
    
    public DecimalBuilder setIntegerPart(long paramLong)
    {
      return setIntegerPart(String.valueOf(paramLong));
    }
    
    public DecimalBuilder setIntegerPart(String paramString)
    {
      return (DecimalBuilder)setStringArgument("android.arg.integer_part", paramString);
    }
  }
  
  public static class DigitsBuilder
    extends TtsSpan.SemioticClassBuilder<DigitsBuilder>
  {
    public DigitsBuilder()
    {
      super();
    }
    
    public DigitsBuilder(String paramString)
    {
      this();
      setDigits(paramString);
    }
    
    public DigitsBuilder setDigits(String paramString)
    {
      return (DigitsBuilder)setStringArgument("android.arg.digits", paramString);
    }
  }
  
  public static class ElectronicBuilder
    extends TtsSpan.SemioticClassBuilder<ElectronicBuilder>
  {
    public ElectronicBuilder()
    {
      super();
    }
    
    public ElectronicBuilder setDomain(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.domain", paramString);
    }
    
    public ElectronicBuilder setEmailArguments(String paramString1, String paramString2)
    {
      return setDomain(paramString2).setUsername(paramString1);
    }
    
    public ElectronicBuilder setFragmentId(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.fragment_id", paramString);
    }
    
    public ElectronicBuilder setPassword(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.password", paramString);
    }
    
    public ElectronicBuilder setPath(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.path", paramString);
    }
    
    public ElectronicBuilder setPort(int paramInt)
    {
      return (ElectronicBuilder)setIntArgument("android.arg.port", paramInt);
    }
    
    public ElectronicBuilder setProtocol(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.protocol", paramString);
    }
    
    public ElectronicBuilder setQueryString(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.query_string", paramString);
    }
    
    public ElectronicBuilder setUsername(String paramString)
    {
      return (ElectronicBuilder)setStringArgument("android.arg.username", paramString);
    }
  }
  
  public static class FractionBuilder
    extends TtsSpan.SemioticClassBuilder<FractionBuilder>
  {
    public FractionBuilder()
    {
      super();
    }
    
    public FractionBuilder(long paramLong1, long paramLong2, long paramLong3)
    {
      this();
      setIntegerPart(paramLong1);
      setNumerator(paramLong2);
      setDenominator(paramLong3);
    }
    
    public FractionBuilder setDenominator(long paramLong)
    {
      return setDenominator(String.valueOf(paramLong));
    }
    
    public FractionBuilder setDenominator(String paramString)
    {
      return (FractionBuilder)setStringArgument("android.arg.denominator", paramString);
    }
    
    public FractionBuilder setIntegerPart(long paramLong)
    {
      return setIntegerPart(String.valueOf(paramLong));
    }
    
    public FractionBuilder setIntegerPart(String paramString)
    {
      return (FractionBuilder)setStringArgument("android.arg.integer_part", paramString);
    }
    
    public FractionBuilder setNumerator(long paramLong)
    {
      return setNumerator(String.valueOf(paramLong));
    }
    
    public FractionBuilder setNumerator(String paramString)
    {
      return (FractionBuilder)setStringArgument("android.arg.numerator", paramString);
    }
  }
  
  public static class MeasureBuilder
    extends TtsSpan.SemioticClassBuilder<MeasureBuilder>
  {
    public MeasureBuilder()
    {
      super();
    }
    
    public MeasureBuilder setDenominator(long paramLong)
    {
      return setDenominator(String.valueOf(paramLong));
    }
    
    public MeasureBuilder setDenominator(String paramString)
    {
      return (MeasureBuilder)setStringArgument("android.arg.denominator", paramString);
    }
    
    public MeasureBuilder setFractionalPart(String paramString)
    {
      return (MeasureBuilder)setStringArgument("android.arg.fractional_part", paramString);
    }
    
    public MeasureBuilder setIntegerPart(long paramLong)
    {
      return setIntegerPart(String.valueOf(paramLong));
    }
    
    public MeasureBuilder setIntegerPart(String paramString)
    {
      return (MeasureBuilder)setStringArgument("android.arg.integer_part", paramString);
    }
    
    public MeasureBuilder setNumber(long paramLong)
    {
      return setNumber(String.valueOf(paramLong));
    }
    
    public MeasureBuilder setNumber(String paramString)
    {
      return (MeasureBuilder)setStringArgument("android.arg.number", paramString);
    }
    
    public MeasureBuilder setNumerator(long paramLong)
    {
      return setNumerator(String.valueOf(paramLong));
    }
    
    public MeasureBuilder setNumerator(String paramString)
    {
      return (MeasureBuilder)setStringArgument("android.arg.numerator", paramString);
    }
    
    public MeasureBuilder setUnit(String paramString)
    {
      return (MeasureBuilder)setStringArgument("android.arg.unit", paramString);
    }
  }
  
  public static class MoneyBuilder
    extends TtsSpan.SemioticClassBuilder<MoneyBuilder>
  {
    public MoneyBuilder()
    {
      super();
    }
    
    public MoneyBuilder setCurrency(String paramString)
    {
      return (MoneyBuilder)setStringArgument("android.arg.money", paramString);
    }
    
    public MoneyBuilder setFractionalPart(String paramString)
    {
      return (MoneyBuilder)setStringArgument("android.arg.fractional_part", paramString);
    }
    
    public MoneyBuilder setIntegerPart(long paramLong)
    {
      return setIntegerPart(String.valueOf(paramLong));
    }
    
    public MoneyBuilder setIntegerPart(String paramString)
    {
      return (MoneyBuilder)setStringArgument("android.arg.integer_part", paramString);
    }
    
    public MoneyBuilder setQuantity(String paramString)
    {
      return (MoneyBuilder)setStringArgument("android.arg.quantity", paramString);
    }
  }
  
  public static class OrdinalBuilder
    extends TtsSpan.SemioticClassBuilder<OrdinalBuilder>
  {
    public OrdinalBuilder()
    {
      super();
    }
    
    public OrdinalBuilder(long paramLong)
    {
      this();
      setNumber(paramLong);
    }
    
    public OrdinalBuilder(String paramString)
    {
      this();
      setNumber(paramString);
    }
    
    public OrdinalBuilder setNumber(long paramLong)
    {
      return setNumber(String.valueOf(paramLong));
    }
    
    public OrdinalBuilder setNumber(String paramString)
    {
      return (OrdinalBuilder)setStringArgument("android.arg.number", paramString);
    }
  }
  
  public static class SemioticClassBuilder<C extends SemioticClassBuilder<?>>
    extends TtsSpan.Builder<C>
  {
    public SemioticClassBuilder(String paramString)
    {
      super();
    }
    
    public C setAnimacy(String paramString)
    {
      return (SemioticClassBuilder)setStringArgument("android.arg.animacy", paramString);
    }
    
    public C setCase(String paramString)
    {
      return (SemioticClassBuilder)setStringArgument("android.arg.case", paramString);
    }
    
    public C setGender(String paramString)
    {
      return (SemioticClassBuilder)setStringArgument("android.arg.gender", paramString);
    }
    
    public C setMultiplicity(String paramString)
    {
      return (SemioticClassBuilder)setStringArgument("android.arg.multiplicity", paramString);
    }
  }
  
  public static class TelephoneBuilder
    extends TtsSpan.SemioticClassBuilder<TelephoneBuilder>
  {
    public TelephoneBuilder()
    {
      super();
    }
    
    public TelephoneBuilder(String paramString)
    {
      this();
      setNumberParts(paramString);
    }
    
    public TelephoneBuilder setCountryCode(String paramString)
    {
      return (TelephoneBuilder)setStringArgument("android.arg.country_code", paramString);
    }
    
    public TelephoneBuilder setExtension(String paramString)
    {
      return (TelephoneBuilder)setStringArgument("android.arg.extension", paramString);
    }
    
    public TelephoneBuilder setNumberParts(String paramString)
    {
      return (TelephoneBuilder)setStringArgument("android.arg.number_parts", paramString);
    }
  }
  
  public static class TextBuilder
    extends TtsSpan.SemioticClassBuilder<TextBuilder>
  {
    public TextBuilder()
    {
      super();
    }
    
    public TextBuilder(String paramString)
    {
      this();
      setText(paramString);
    }
    
    public TextBuilder setText(String paramString)
    {
      return (TextBuilder)setStringArgument("android.arg.text", paramString);
    }
  }
  
  public static class TimeBuilder
    extends TtsSpan.SemioticClassBuilder<TimeBuilder>
  {
    public TimeBuilder()
    {
      super();
    }
    
    public TimeBuilder(int paramInt1, int paramInt2)
    {
      this();
      setHours(paramInt1);
      setMinutes(paramInt2);
    }
    
    public TimeBuilder setHours(int paramInt)
    {
      return (TimeBuilder)setIntArgument("android.arg.hours", paramInt);
    }
    
    public TimeBuilder setMinutes(int paramInt)
    {
      return (TimeBuilder)setIntArgument("android.arg.minutes", paramInt);
    }
  }
  
  public static class VerbatimBuilder
    extends TtsSpan.SemioticClassBuilder<VerbatimBuilder>
  {
    public VerbatimBuilder()
    {
      super();
    }
    
    public VerbatimBuilder(String paramString)
    {
      this();
      setVerbatim(paramString);
    }
    
    public VerbatimBuilder setVerbatim(String paramString)
    {
      return (VerbatimBuilder)setStringArgument("android.arg.verbatim", paramString);
    }
  }
}
