package com.android.internal.telephony;

import android.content.res.Resources;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.SparseIntArray;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GsmAlphabet
{
  public static final byte GSM_EXTENDED_ESCAPE = 27;
  private static final String TAG = "GSM";
  public static final int UDH_SEPTET_COST_CONCATENATED_MESSAGE = 6;
  public static final int UDH_SEPTET_COST_LENGTH = 1;
  public static final int UDH_SEPTET_COST_ONE_SHIFT_TABLE = 4;
  public static final int UDH_SEPTET_COST_TWO_SHIFT_TABLES = 7;
  private static final SparseIntArray[] sCharsToGsmTables;
  private static final SparseIntArray[] sCharsToShiftTables;
  private static boolean sDisableCountryEncodingCheck = false;
  private static int[] sEnabledLockingShiftTables;
  private static int[] sEnabledSingleShiftTables;
  private static int sHighestEnabledSingleShiftCode;
  private static final String[] sLanguageShiftTables;
  private static final String[] sLanguageTables = { "@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞ￿ÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà", "@£$¥€éùıòÇ\nĞğ\rÅåΔ_ΦΓΛΩΠΨΣΘΞ￿ŞşßÉ !\"#¤%&'()*+,-./0123456789:;<=>?İABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§çabcdefghijklmnopqrstuvwxyzäöñüà", "", "@£$¥êéúíóç\nÔô\rÁáΔ_ªÇÀ∞^\\€Ó|￿ÂâÊÉ !\"#º%&'()*+,-./0123456789:;<=>?ÍABCDEFGHIJKLMNOPQRSTUVWXYZÃÕÚÜ§~abcdefghijklmnopqrstuvwxyzãõ`üà", "ঁংঃঅআইঈউঊঋ\nঌ \r এঐ  ওঔকখগঘঙচ￿ছজঝঞ !টঠডঢণত)(থদ,ধ.ন0123456789:; পফ?বভমযর ল   শষসহ়ঽািীুূৃৄ  েৈ  োৌ্ৎabcdefghijklmnopqrstuvwxyzৗড়ঢ়ৰৱ", "ઁંઃઅઆઇઈઉઊઋ\nઌઍ\r એઐઑ ઓઔકખગઘઙચ￿છજઝઞ !ટઠડઢણત)(થદ,ધ.ન0123456789:; પફ?બભમયર લળ વશષસહ઼ઽાિીુૂૃૄૅ ેૈૉ ોૌ્ૐabcdefghijklmnopqrstuvwxyzૠૡૢૣ૱", "ँंःअआइईउऊऋ\nऌऍ\rऎएऐऑऒओऔकखगघङच￿छजझञ !टठडढणत)(थद,ध.न0123456789:;ऩपफ?बभमयरऱलळऴवशषसह़ऽािीुूृॄॅॆेैॉॊोौ्ॐabcdefghijklmnopqrstuvwxyzॲॻॼॾॿ", " ಂಃಅಆಇಈಉಊಋ\nಌ \rಎಏಐ ಒಓಔಕಖಗಘಙಚ￿ಛಜಝಞ !ಟಠಡಢಣತ)(ಥದ,ಧ.ನ0123456789:; ಪಫ?ಬಭಮಯರಱಲಳ ವಶಷಸಹ಼ಽಾಿೀುೂೃೄ ೆೇೈ ೊೋೌ್ೕabcdefghijklmnopqrstuvwxyzೖೠೡೢೣ", " ംഃഅആഇഈഉഊഋ\nഌ \rഎഏഐ ഒഓഔകഖഗഘങച￿ഛജഝഞ !ടഠഡഢണത)(ഥദ,ധ.ന0123456789:; പഫ?ബഭമയരറലളഴവശഷസഹ ഽാിീുൂൃൄ െേൈ ൊോൌ്ൗabcdefghijklmnopqrstuvwxyzൠൡൢൣ൹", "ଁଂଃଅଆଇଈଉଊଋ\nଌ \r ଏଐ  ଓଔକଖଗଘଙଚ￿ଛଜଝଞ !ଟଠଡଢଣତ)(ଥଦ,ଧ.ନ0123456789:; ପଫ?ବଭମଯର ଲଳ ଵଶଷସହ଼ଽାିୀୁୂୃୄ  େୈ  ୋୌ୍ୖabcdefghijklmnopqrstuvwxyzୗୠୡୢୣ", "ਁਂਃਅਆਇਈਉਊ \n  \r ਏਐ  ਓਔਕਖਗਘਙਚ￿ਛਜਝਞ !ਟਠਡਢਣਤ)(ਥਦ,ਧ.ਨ0123456789:; ਪਫ?ਬਭਮਯਰ ਲਲ਼ ਵਸ਼ ਸਹ਼ ਾਿੀੁੂ    ੇੈ  ੋੌ੍ੑabcdefghijklmnopqrstuvwxyzੰੱੲੳੴ", " ஂஃஅஆஇஈஉஊ \n  \rஎஏஐ ஒஓஔக   ஙச￿ ஜ ஞ !ட   ணத)(  , .ந0123456789:;னப ?  மயரறலளழவஶஷஸஹ  ாிீுூ   ெேை ொோௌ்ௐabcdefghijklmnopqrstuvwxyzௗ௰௱௲௹", "ఁంఃఅఆఇఈఉఊఋ\nఌ \rఎఏఐ ఒఓఔకఖగఘఙచ￿ఛజఝఞ !టఠడఢణత)(థద,ధ.న0123456789:; పఫ?బభమయరఱలళ వశషసహ ఽాిీుూృౄ ెేై ొోౌ్ౕabcdefghijklmnopqrstuvwxyzౖౠౡౢౣ", "اآبٻڀپڦتۂٿ\nٹٽ\rٺټثجځڄڃڅچڇحخد￿ڌڈډڊ !ڏڍذرڑړ)(ڙز,ږ.ژ0123456789:;ښسش?صضطظعفقکڪګگڳڱلمنںڻڼوۄەہھءیېےٍُِٗٔabcdefghijklmnopqrstuvwxyzّٰٕٖٓ" };
  
  static
  {
    sLanguageShiftTables = new String[] { "          \f         ^                   {}     \\            [~] |                                    €                          ", "          \f         ^                   {}     \\            [~] |      Ğ İ         Ş               ç € ğ ı         ş            ", "         ç\f         ^                   {}     \\            [~] |Á       Í     Ó     Ú           á   €   í     ó     ú          ", "     ê   ç\fÔô Áá  ΦΓ^ΩΠΨΣΘ     Ê        {}     \\            [~] |À       Í     Ó     Ú     ÃÕ    Â   €   í     ó     ú     ãõ  â", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*০১ ২৩৪৫৬৭৮৯য়ৠৡৢ{}ৣ৲৳৴৵\\৶৷৸৹৺       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ૦૧૨૩૪૫૬૭૮૯  {}     \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ०१२३४५६७८९॒॑{}॓॔क़ख़ग़\\ज़ड़ढ़फ़य़ॠॡॢॣ॰ॱ [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ೦೧೨೩೪೫೬೭೮೯ೞೱ{}ೲ    \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ൦൧൨൩൪൫൬൭൮൯൰൱{}൲൳൴൵ൺ\\ൻർൽൾൿ       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ୦୧୨୩୪୫୬୭୮୯ଡ଼ଢ଼{}ୟ୰ୱ  \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ੦੧੨੩੪੫੬੭੮੯ਖ਼ਗ਼{}ਜ਼ੜਫ਼ੵ \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ௦௧௨௩௪௫௬௭௮௯௳௴{}௵௶௷௸௺\\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*   ౦౧౨౩౪౫౬౭౮౯ౘౙ{}౸౹౺౻౼\\౽౾౿         [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*؀؁ ۰۱۲۳۴۵۶۷۸۹،؍{}؎؏ؐؑؒ\\ؓؔ؛؟ـْ٘٫٬ٲٳۍ[~]۔|ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          " };
    enableCountrySpecificEncodings();
    int i = sLanguageTables.length;
    int j = sLanguageShiftTables.length;
    Object localObject1;
    if (i != j)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Error: language tables array length ");
      ((StringBuilder)localObject1).append(i);
      ((StringBuilder)localObject1).append(" != shift tables array length ");
      ((StringBuilder)localObject1).append(j);
      Rlog.e("GSM", ((StringBuilder)localObject1).toString());
    }
    sCharsToGsmTables = new SparseIntArray[i];
    int m;
    Object localObject2;
    int n;
    for (int k = 0; k < i; k++)
    {
      localObject1 = sLanguageTables[k];
      m = ((String)localObject1).length();
      if ((m != 0) && (m != 128))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Error: language tables index ");
        ((StringBuilder)localObject2).append(k);
        ((StringBuilder)localObject2).append(" length ");
        ((StringBuilder)localObject2).append(m);
        ((StringBuilder)localObject2).append(" (expected 128 or 0)");
        Rlog.e("GSM", ((StringBuilder)localObject2).toString());
      }
      localObject2 = new SparseIntArray(m);
      sCharsToGsmTables[k] = localObject2;
      for (n = 0; n < m; n++) {
        ((SparseIntArray)localObject2).put(((String)localObject1).charAt(n), n);
      }
    }
    sCharsToShiftTables = new SparseIntArray[i];
    for (k = 0; k < j; k++)
    {
      localObject1 = sLanguageShiftTables[k];
      i = ((String)localObject1).length();
      if ((i != 0) && (i != 128))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Error: language shift tables index ");
        ((StringBuilder)localObject2).append(k);
        ((StringBuilder)localObject2).append(" length ");
        ((StringBuilder)localObject2).append(i);
        ((StringBuilder)localObject2).append(" (expected 128 or 0)");
        Rlog.e("GSM", ((StringBuilder)localObject2).toString());
      }
      localObject2 = new SparseIntArray(i);
      sCharsToShiftTables[k] = localObject2;
      for (n = 0; n < i; n++)
      {
        m = ((String)localObject1).charAt(n);
        if (m != 32) {
          ((SparseIntArray)localObject2).put(m, n);
        }
      }
    }
  }
  
  private GsmAlphabet() {}
  
  public static int charToGsm(char paramChar)
  {
    try
    {
      int i = charToGsm(paramChar, false);
      return i;
    }
    catch (EncodeException localEncodeException) {}
    return sCharsToGsmTables[0].get(32, 32);
  }
  
  public static int charToGsm(char paramChar, boolean paramBoolean)
    throws EncodeException
  {
    int i = sCharsToGsmTables[0].get(paramChar, -1);
    if (i == -1)
    {
      if (sCharsToShiftTables[0].get(paramChar, -1) == -1)
      {
        if (!paramBoolean) {
          return sCharsToGsmTables[0].get(32, 32);
        }
        throw new EncodeException(paramChar);
      }
      return 27;
    }
    return i;
  }
  
  public static int charToGsmExtended(char paramChar)
  {
    paramChar = sCharsToShiftTables[0].get(paramChar, -1);
    if (paramChar == '￿') {
      return sCharsToGsmTables[0].get(32, 32);
    }
    return paramChar;
  }
  
  public static int countGsmSeptets(char paramChar)
  {
    try
    {
      int i = countGsmSeptets(paramChar, false);
      return i;
    }
    catch (EncodeException localEncodeException) {}
    return 0;
  }
  
  public static int countGsmSeptets(char paramChar, boolean paramBoolean)
    throws EncodeException
  {
    if (sCharsToGsmTables[0].get(paramChar, -1) != -1) {
      return 1;
    }
    if (sCharsToShiftTables[0].get(paramChar, -1) != -1) {
      return 2;
    }
    if (!paramBoolean) {
      return 1;
    }
    throw new EncodeException(paramChar);
  }
  
  public static TextEncodingDetails countGsmSeptets(CharSequence paramCharSequence, boolean paramBoolean)
  {
    if (!sDisableCountryEncodingCheck) {
      enableCountrySpecificEncodings();
    }
    if (sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length == 0)
    {
      localObject1 = new TextEncodingDetails();
      i = countGsmSeptetsUsingTables(paramCharSequence, paramBoolean, 0, 0);
      if (i == -1) {
        return null;
      }
      codeUnitSize = 1;
      codeUnitCount = i;
      if (i > 160)
      {
        msgCount = ((i + 152) / 153);
        codeUnitsRemaining = (msgCount * 153 - i);
      }
      else
      {
        msgCount = 1;
        codeUnitsRemaining = (160 - i);
      }
      codeUnitSize = 1;
      return localObject1;
    }
    int j = sHighestEnabledSingleShiftCode;
    Object localObject1 = new ArrayList(sEnabledLockingShiftTables.length + 1);
    ((List)localObject1).add(new LanguagePairCount(0));
    for (m : sEnabledLockingShiftTables) {
      if ((m != 0) && (!sLanguageTables[m].isEmpty())) {
        ((List)localObject1).add(new LanguagePairCount(m));
      }
    }
    int m = paramCharSequence.length();
    int n;
    for (int i = 0; (i < m) && (!((List)localObject1).isEmpty()); i++)
    {
      n = paramCharSequence.charAt(i);
      if (n == 27)
      {
        Rlog.w("GSM", "countGsmSeptets() string contains Escape character, ignoring!");
      }
      else
      {
        ??? = ((List)localObject1).iterator();
        while (((Iterator)???).hasNext())
        {
          LanguagePairCount localLanguagePairCount = (LanguagePairCount)((Iterator)???).next();
          int[] arrayOfInt;
          if (sCharsToGsmTables[languageCode].get(n, -1) == -1) {
            for (??? = 0; ??? <= j; ???++) {
              if (septetCounts[???] != -1) {
                if (sCharsToShiftTables[???].get(n, -1) == -1)
                {
                  if (paramBoolean)
                  {
                    arrayOfInt = septetCounts;
                    arrayOfInt[???] += 1;
                    arrayOfInt = unencodableCounts;
                    arrayOfInt[???] += 1;
                  }
                  else
                  {
                    septetCounts[???] = -1;
                  }
                }
                else
                {
                  arrayOfInt = septetCounts;
                  arrayOfInt[???] += 2;
                }
              }
            }
          }
          for (??? = 0; ??? <= j; ???++) {
            if (septetCounts[???] != -1)
            {
              arrayOfInt = septetCounts;
              arrayOfInt[???] += 1;
            }
          }
        }
      }
    }
    paramCharSequence = new TextEncodingDetails();
    msgCount = Integer.MAX_VALUE;
    codeUnitSize = 1;
    i = Integer.MAX_VALUE;
    ??? = ((List)localObject1).iterator();
    while (((Iterator)???).hasNext())
    {
      localObject1 = (LanguagePairCount)((Iterator)???).next();
      m = 0;
      while (m <= j)
      {
        int i1 = septetCounts[m];
        int i2;
        if (i1 == -1)
        {
          i2 = i;
        }
        else
        {
          if ((languageCode != 0) && (m != 0)) {}
          for (??? = 8;; ??? = 5)
          {
            break;
            if ((languageCode == 0) && (m == 0))
            {
              ??? = 0;
              break;
            }
          }
          if (i1 + ??? > 160)
          {
            n = ???;
            if (??? == 0) {
              n = 1;
            }
            n = 160 - (n + 6);
            ??? = (i1 + n - 1) / n;
            n = ??? * n - i1;
          }
          else
          {
            i2 = 1;
            n = 160 - ??? - i1;
            ??? = i2;
          }
          int i3 = unencodableCounts[m];
          if ((paramBoolean) && (i3 > i))
          {
            i2 = i;
          }
          else
          {
            if ((paramBoolean) && (i3 < i)) {
              break label770;
            }
            if (??? >= msgCount)
            {
              i2 = i;
              if (??? != msgCount) {
                break label806;
              }
              i2 = i;
              if (n <= codeUnitsRemaining) {
                break label806;
              }
            }
            label770:
            i2 = i3;
            msgCount = ???;
            codeUnitCount = i1;
            codeUnitsRemaining = n;
            languageTable = languageCode;
            languageShiftTable = m;
          }
        }
        label806:
        m++;
        i = i2;
      }
    }
    if (msgCount == Integer.MAX_VALUE) {
      return null;
    }
    return paramCharSequence;
  }
  
  public static int countGsmSeptetsUsingTables(CharSequence paramCharSequence, boolean paramBoolean, int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = paramCharSequence.length();
    SparseIntArray localSparseIntArray1 = sCharsToGsmTables[paramInt1];
    SparseIntArray localSparseIntArray2 = sCharsToShiftTables[paramInt2];
    paramInt2 = 0;
    paramInt1 = i;
    while (paramInt2 < j)
    {
      i = paramCharSequence.charAt(paramInt2);
      if (i == 27)
      {
        Rlog.w("GSM", "countGsmSeptets() string contains Escape character, skipping.");
      }
      else if (localSparseIntArray1.get(i, -1) != -1)
      {
        paramInt1++;
      }
      else if (localSparseIntArray2.get(i, -1) != -1)
      {
        paramInt1 += 2;
      }
      else
      {
        if (!paramBoolean) {
          break label113;
        }
        paramInt1++;
      }
      paramInt2++;
      continue;
      label113:
      return -1;
    }
    return paramInt1;
  }
  
  private static void enableCountrySpecificEncodings()
  {
    Resources localResources = Resources.getSystem();
    sEnabledSingleShiftTables = localResources.getIntArray(17236040);
    sEnabledLockingShiftTables = localResources.getIntArray(17236039);
    if (sEnabledSingleShiftTables.length > 0) {
      sHighestEnabledSingleShiftCode = sEnabledSingleShiftTables[(sEnabledSingleShiftTables.length - 1)];
    } else {
      sHighestEnabledSingleShiftCode = 0;
    }
  }
  
  public static int findGsmSeptetLimitIndex(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramString.length();
    SparseIntArray localSparseIntArray1 = sCharsToGsmTables[paramInt3];
    SparseIntArray localSparseIntArray2 = sCharsToShiftTables[paramInt4];
    paramInt4 = 0;
    paramInt3 = paramInt1;
    while (paramInt3 < i)
    {
      if (localSparseIntArray1.get(paramString.charAt(paramInt3), -1) == -1)
      {
        if (localSparseIntArray2.get(paramString.charAt(paramInt3), -1) == -1) {
          paramInt1 = paramInt4 + 1;
        } else {
          paramInt1 = paramInt4 + 2;
        }
      }
      else {
        paramInt1 = paramInt4 + 1;
      }
      if (paramInt1 > paramInt2) {
        return paramInt3;
      }
      paramInt3++;
      paramInt4 = paramInt1;
    }
    return i;
  }
  
  public static int[] getEnabledLockingShiftTables()
  {
    try
    {
      int[] arrayOfInt = sEnabledLockingShiftTables;
      return arrayOfInt;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public static int[] getEnabledSingleShiftTables()
  {
    try
    {
      int[] arrayOfInt = sEnabledSingleShiftTables;
      return arrayOfInt;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public static String gsm7BitPackedToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return gsm7BitPackedToString(paramArrayOfByte, paramInt1, paramInt2, 0, 0, 0);
  }
  
  public static String gsm7BitPackedToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = paramInt4;
    StringBuilder localStringBuilder = new StringBuilder(paramInt2);
    if (i >= 0)
    {
      paramInt4 = i;
      if (i > sLanguageTables.length) {}
    }
    Object localObject1;
    for (;;)
    {
      break;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("unknown language table ");
      ((StringBuilder)localObject1).append(i);
      ((StringBuilder)localObject1).append(", using default");
      Rlog.w("GSM", ((StringBuilder)localObject1).toString());
      paramInt4 = 0;
    }
    if (paramInt5 >= 0)
    {
      i = paramInt5;
      if (paramInt5 <= sLanguageShiftTables.length) {}
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("unknown single shift table ");
      ((StringBuilder)localObject1).append(paramInt5);
      ((StringBuilder)localObject1).append(", using default");
      Rlog.w("GSM", ((StringBuilder)localObject1).toString());
      i = 0;
    }
    int j = 0;
    try
    {
      localObject1 = sLanguageTables[paramInt4];
      String str = sLanguageShiftTables[i];
      boolean bool = ((String)localObject1).isEmpty();
      paramInt5 = 0;
      if (bool)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("no language table for code ");
        ((StringBuilder)localObject1).append(paramInt4);
        ((StringBuilder)localObject1).append(", using default");
        Rlog.w("GSM", ((StringBuilder)localObject1).toString());
        localObject1 = sLanguageTables[0];
      }
      Object localObject2 = str;
      if (str.isEmpty())
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("no single shift table for code ");
        ((StringBuilder)localObject2).append(i);
        ((StringBuilder)localObject2).append(", using default");
        Rlog.w("GSM", ((StringBuilder)localObject2).toString());
        localObject2 = sLanguageShiftTables[0];
      }
      paramInt4 = j;
      while (paramInt5 < paramInt2)
      {
        i = 7 * paramInt5 + paramInt3;
        int k = i / 8;
        int m = i % 8;
        j = paramArrayOfByte[(paramInt1 + k)] >> m & 0x7F;
        i = j;
        if (m > 1) {
          i = j & 127 >> m - 1 | paramArrayOfByte[(paramInt1 + k + 1)] << 8 - m & 0x7F;
        }
        if (paramInt4 != 0)
        {
          if (i == 27)
          {
            localStringBuilder.append(' ');
          }
          else
          {
            char c = ((String)localObject2).charAt(i);
            if (c == ' ') {
              localStringBuilder.append(((String)localObject1).charAt(i));
            } else {
              localStringBuilder.append(c);
            }
          }
          paramInt4 = 0;
        }
        else
        {
          if (i != 27) {
            break label480;
          }
          paramInt4 = 1;
        }
        break label493;
        label480:
        localStringBuilder.append(((String)localObject1).charAt(i));
        label493:
        paramInt5++;
      }
      return localStringBuilder.toString();
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.e("GSM", "Error GSM 7 bit packed: ", paramArrayOfByte);
    }
    return null;
  }
  
  public static String gsm8BitUnpackedToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return gsm8BitUnpackedToString(paramArrayOfByte, paramInt1, paramInt2, "");
  }
  
  public static String gsm8BitUnpackedToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
  {
    int i = 0;
    String str = null;
    StringBuilder localStringBuilder = null;
    boolean bool = TextUtils.isEmpty(paramString);
    int j = 2;
    int k;
    Object localObject1;
    Object localObject2;
    if (!bool)
    {
      k = i;
      localObject1 = str;
      localObject2 = localStringBuilder;
      if (!paramString.equalsIgnoreCase("us-ascii"))
      {
        k = i;
        localObject1 = str;
        localObject2 = localStringBuilder;
        if (Charset.isSupported(paramString))
        {
          k = 1;
          localObject1 = Charset.forName(paramString);
          localObject2 = ByteBuffer.allocate(2);
        }
      }
    }
    else
    {
      localObject2 = localStringBuilder;
      localObject1 = str;
      k = i;
    }
    paramString = sLanguageTables[0];
    str = sLanguageShiftTables[0];
    localStringBuilder = new StringBuilder(paramInt2);
    int m = 0;
    int n;
    for (i = paramInt1; i < paramInt1 + paramInt2; i = n)
    {
      n = paramArrayOfByte[i] & 0xFF;
      if (n == 255) {
        break;
      }
      if (n == 27)
      {
        if (m != 0) {
          localStringBuilder.append(' ');
        }
        for (m = 0;; m = 1)
        {
          n = j;
          j = i;
          i = n;
          break;
        }
      }
      if (m != 0)
      {
        int i1;
        if (n < str.length())
        {
          j = str.charAt(n);
          i1 = j;
        }
        else
        {
          j = 32;
          i1 = j;
        }
        if (i1 == 32)
        {
          if (n < paramString.length()) {
            localStringBuilder.append(paramString.charAt(n));
          } else {
            localStringBuilder.append(' ');
          }
        }
        else {
          localStringBuilder.append(i1);
        }
        j = i;
      }
      else if ((k != 0) && (n >= 128) && (i + 1 < paramInt1 + paramInt2))
      {
        ((ByteBuffer)localObject2).clear();
        ((ByteBuffer)localObject2).put(paramArrayOfByte, i, 2);
        ((ByteBuffer)localObject2).flip();
        localStringBuilder.append(((Charset)localObject1).decode((ByteBuffer)localObject2).toString());
        j = i + 1;
      }
      else if (n < paramString.length())
      {
        localStringBuilder.append(paramString.charAt(n));
        j = i;
      }
      else
      {
        localStringBuilder.append(' ');
        j = i;
      }
      i = 2;
      m = 0;
      n = j + 1;
      j = i;
    }
    return localStringBuilder.toString();
  }
  
  public static char gsmExtendedToChar(int paramInt)
  {
    if (paramInt == 27) {
      return ' ';
    }
    if ((paramInt >= 0) && (paramInt < 128))
    {
      char c = sLanguageShiftTables[0].charAt(paramInt);
      if (c == ' ') {
        return sLanguageTables[0].charAt(paramInt);
      }
      return c;
    }
    return ' ';
  }
  
  public static char gsmToChar(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < 128)) {
      return sLanguageTables[0].charAt(paramInt);
    }
    return ' ';
  }
  
  public static boolean isGsmSeptets(char paramChar)
  {
    if (sCharsToGsmTables[0].get(paramChar, -1) != -1) {
      return true;
    }
    return sCharsToShiftTables[0].get(paramChar, -1) != -1;
  }
  
  private static void packSmsChar(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 / 8;
    paramInt1 %= 8;
    i++;
    paramArrayOfByte[i] = ((byte)(byte)(paramArrayOfByte[i] | paramInt2 << paramInt1));
    if (paramInt1 > 1) {
      paramArrayOfByte[(i + 1)] = ((byte)(byte)(paramInt2 >> 8 - paramInt1));
    }
  }
  
  public static void setEnabledLockingShiftTables(int[] paramArrayOfInt)
  {
    try
    {
      sEnabledLockingShiftTables = paramArrayOfInt;
      sDisableCountryEncodingCheck = true;
      return;
    }
    finally
    {
      paramArrayOfInt = finally;
      throw paramArrayOfInt;
    }
  }
  
  public static void setEnabledSingleShiftTables(int[] paramArrayOfInt)
  {
    try
    {
      sEnabledSingleShiftTables = paramArrayOfInt;
      sDisableCountryEncodingCheck = true;
      if (paramArrayOfInt.length > 0) {
        sHighestEnabledSingleShiftCode = paramArrayOfInt[(paramArrayOfInt.length - 1)];
      } else {
        sHighestEnabledSingleShiftCode = 0;
      }
      return;
    }
    finally {}
  }
  
  public static byte[] stringToGsm7BitPacked(String paramString)
    throws EncodeException
  {
    return stringToGsm7BitPacked(paramString, 0, true, 0, 0);
  }
  
  public static byte[] stringToGsm7BitPacked(String paramString, int paramInt1, int paramInt2)
    throws EncodeException
  {
    return stringToGsm7BitPacked(paramString, 0, true, paramInt1, paramInt2);
  }
  
  public static byte[] stringToGsm7BitPacked(String paramString, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3)
    throws EncodeException
  {
    Object localObject = paramString;
    int i = ((String)localObject).length();
    int j = countGsmSeptetsUsingTables((CharSequence)localObject, paramBoolean ^ true, paramInt2, paramInt3);
    if (j != -1)
    {
      int k = j + paramInt1;
      if (k <= 255)
      {
        byte[] arrayOfByte = new byte[(k * 7 + 7) / 8 + 1];
        SparseIntArray localSparseIntArray = sCharsToGsmTables[paramInt2];
        localObject = sCharsToShiftTables[paramInt3];
        j = 0;
        paramInt3 = paramInt1;
        paramInt2 = paramInt1 * 7;
        paramInt1 = paramInt3;
        while ((j < i) && (paramInt1 < k))
        {
          int m = paramString.charAt(j);
          int n = localSparseIntArray.get(m, -1);
          paramInt3 = n;
          int i1 = paramInt1;
          int i2 = paramInt2;
          if (n == -1)
          {
            paramInt3 = ((SparseIntArray)localObject).get(m, -1);
            if (paramInt3 == -1)
            {
              if (!paramBoolean)
              {
                paramInt3 = localSparseIntArray.get(32, 32);
                i1 = paramInt1;
                i2 = paramInt2;
              }
              else
              {
                throw new EncodeException("stringToGsm7BitPacked(): unencodable char");
              }
            }
            else
            {
              packSmsChar(arrayOfByte, paramInt2, 27);
              i2 = paramInt2 + 7;
              i1 = paramInt1 + 1;
            }
          }
          packSmsChar(arrayOfByte, i2, paramInt3);
          paramInt1 = i1 + 1;
          j++;
          paramInt2 = i2 + 7;
        }
        arrayOfByte[0] = ((byte)(byte)k);
        return arrayOfByte;
      }
      throw new EncodeException("Payload cannot exceed 255 septets");
    }
    throw new EncodeException("countGsmSeptetsUsingTables(): unencodable char");
  }
  
  public static byte[] stringToGsm7BitPackedWithHeader(String paramString, byte[] paramArrayOfByte)
    throws EncodeException
  {
    return stringToGsm7BitPackedWithHeader(paramString, paramArrayOfByte, 0, 0);
  }
  
  public static byte[] stringToGsm7BitPackedWithHeader(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws EncodeException
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
    {
      paramString = stringToGsm7BitPacked(paramString, ((paramArrayOfByte.length + 1) * 8 + 6) / 7, true, paramInt1, paramInt2);
      paramString[1] = ((byte)(byte)paramArrayOfByte.length);
      System.arraycopy(paramArrayOfByte, 0, paramString, 2, paramArrayOfByte.length);
      return paramString;
    }
    return stringToGsm7BitPacked(paramString, paramInt1, paramInt2);
  }
  
  public static byte[] stringToGsm8BitPacked(String paramString)
  {
    byte[] arrayOfByte = new byte[countGsmSeptetsUsingTables(paramString, true, 0, 0)];
    stringToGsm8BitUnpackedField(paramString, arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }
  
  public static void stringToGsm8BitUnpackedField(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    SparseIntArray localSparseIntArray1 = sCharsToGsmTables[0];
    SparseIntArray localSparseIntArray2 = sCharsToShiftTables[0];
    int j = 0;
    int k = paramString.length();
    int m;
    for (;;)
    {
      m = i;
      if (j >= k) {
        break;
      }
      m = i;
      if (i - paramInt1 >= paramInt2) {
        break;
      }
      int n = paramString.charAt(j);
      int i1 = localSparseIntArray1.get(n, -1);
      int i2 = i;
      m = i1;
      if (i1 == -1)
      {
        m = localSparseIntArray2.get(n, -1);
        if (m == -1)
        {
          m = localSparseIntArray1.get(32, 32);
          i2 = i;
        }
        else
        {
          if (i + 1 - paramInt1 >= paramInt2)
          {
            m = i;
            break;
          }
          paramArrayOfByte[i] = ((byte)27);
          i2 = i + 1;
        }
      }
      paramArrayOfByte[i2] = ((byte)(byte)m);
      j++;
      i = i2 + 1;
    }
    while (m - paramInt1 < paramInt2)
    {
      paramArrayOfByte[m] = ((byte)-1);
      m++;
    }
  }
  
  private static class LanguagePairCount
  {
    final int languageCode;
    final int[] septetCounts;
    final int[] unencodableCounts;
    
    LanguagePairCount(int paramInt)
    {
      languageCode = paramInt;
      int i = GsmAlphabet.sHighestEnabledSingleShiftCode;
      septetCounts = new int[i + 1];
      unencodableCounts = new int[i + 1];
      int j = 1;
      int k = 0;
      while (j <= i)
      {
        if (GsmAlphabet.sEnabledSingleShiftTables[k] == j) {
          k++;
        } else {
          septetCounts[j] = -1;
        }
        j++;
      }
      if ((paramInt == 1) && (i >= 1)) {
        septetCounts[1] = -1;
      } else if ((paramInt == 3) && (i >= 2)) {
        septetCounts[2] = -1;
      }
    }
  }
  
  public static class TextEncodingDetails
  {
    public int codeUnitCount;
    public int codeUnitSize;
    public int codeUnitsRemaining;
    public int languageShiftTable;
    public int languageTable;
    public int msgCount;
    
    public TextEncodingDetails() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("TextEncodingDetails { msgCount=");
      localStringBuilder.append(msgCount);
      localStringBuilder.append(", codeUnitCount=");
      localStringBuilder.append(codeUnitCount);
      localStringBuilder.append(", codeUnitsRemaining=");
      localStringBuilder.append(codeUnitsRemaining);
      localStringBuilder.append(", codeUnitSize=");
      localStringBuilder.append(codeUnitSize);
      localStringBuilder.append(", languageTable=");
      localStringBuilder.append(languageTable);
      localStringBuilder.append(", languageShiftTable=");
      localStringBuilder.append(languageShiftTable);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
}
