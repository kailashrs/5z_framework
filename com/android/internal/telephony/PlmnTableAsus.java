package com.android.internal.telephony;

import android.telephony.Rlog;
import android.text.TextUtils;
import java.util.Locale;

public final class PlmnTableAsus
{
  static final String[] PlmnLangIndex = { "zh_CN", "zh_TW", "zh_HK" };
  static final PlmnTableNode[][] PlmnTab;
  public static final String XL = "XL";
  public static final String[] XLS = { "Axis", "XL 4G LTE", "XL Axiata", "XL" };
  
  static
  {
    PlmnTableNode[] arrayOfPlmnTableNode1 = { new PlmnTableNode("CMHK", "China Mobile HK", "中国移动香港"), new PlmnTableNode("CMCC", "CHINA MOBILE", "中国移动"), new PlmnTableNode("UNICOM", "CHN-UNICOM", "中国联通"), new PlmnTableNode("CT", "CHN-CT", "中国电信"), new PlmnTableNode("China Telecom", "China Telecom", "中国电信"), new PlmnTableNode("CT Macao", "CT Macao", "中国电信(澳门)"), new PlmnTableNode("FET", "Far EasTone", "远传电信"), new PlmnTableNode("APT", "APT", "亚太电信"), new PlmnTableNode("KGT", "KGT-Online", "和信电讯"), new PlmnTableNode("T Star", "T Star", "台湾之星"), new PlmnTableNode("VIBO", "VIBO", "台湾之星"), new PlmnTableNode("Chunghwa", "Chunghwa Telecom", "中华电信"), new PlmnTableNode("TW MOB", "TWN MOBITAI", "东信电讯"), new PlmnTableNode("TWM", "TW Mobile", "台湾大哥大"), new PlmnTableNode("TA-GSM", "TWN TransAsia Telecom GSM", "泛亚电信") };
    PlmnTableNode localPlmnTableNode1 = new PlmnTableNode("CMHK", "China Mobile HK", "中國移動香港");
    PlmnTableNode localPlmnTableNode2 = new PlmnTableNode("CMCC", "CHINA MOBILE", "中國移動");
    PlmnTableNode localPlmnTableNode3 = new PlmnTableNode("UNICOM", "CHN-UNICOM", "中國聯通");
    PlmnTableNode localPlmnTableNode4 = new PlmnTableNode("CT", "CHN-CT", "中國電信");
    PlmnTableNode localPlmnTableNode5 = new PlmnTableNode("China Telecom", "China Telecom", "中國電信");
    PlmnTableNode localPlmnTableNode6 = new PlmnTableNode("CT Macao", "CT Macao", "中國電信(澳門)");
    PlmnTableNode localPlmnTableNode7 = new PlmnTableNode("FET", "Far EasTone", "遠傳電信");
    PlmnTableNode localPlmnTableNode8 = new PlmnTableNode("APT", "APT", "亞太電信");
    PlmnTableNode localPlmnTableNode9 = new PlmnTableNode("KGT", "KGT-Online", "和信電訊");
    PlmnTableNode localPlmnTableNode10 = new PlmnTableNode("T Star", "T Star", "台灣之星");
    PlmnTableNode localPlmnTableNode11 = new PlmnTableNode("VIBO", "VIBO", "台灣之星");
    PlmnTableNode localPlmnTableNode12 = new PlmnTableNode("Chunghwa", "Chunghwa Telecom", "中華電信");
    PlmnTableNode localPlmnTableNode13 = new PlmnTableNode("TW MOB", "TWN MOBITAI", "東信電訊");
    PlmnTableNode localPlmnTableNode14 = new PlmnTableNode("TWM", "TW Mobile", "台灣大哥大");
    PlmnTableNode localPlmnTableNode15 = new PlmnTableNode("TA-GSM", "TWN TransAsia Telecom GSM", "汎亞電信");
    PlmnTableNode[] arrayOfPlmnTableNode2 = { new PlmnTableNode("CMHK", "China Mobile HK", "中國移動香港"), new PlmnTableNode("CMCC", "CHINA MOBILE", "中國移動"), new PlmnTableNode("UNICOM", "CHN-UNICOM", "中國聯通"), new PlmnTableNode("CT", "CHN-CT", "中國電信"), new PlmnTableNode("China Telecom", "China Telecom", "中國電信"), new PlmnTableNode("CT Macao", "CT Macao", "中國電信(澳門)"), new PlmnTableNode("FET", "Far EasTone", "遠傳電信"), new PlmnTableNode("APT", "APT", "亞太電信"), new PlmnTableNode("KGT", "KGT-Online", "和信電訊"), new PlmnTableNode("T Star", "T Star", "台灣之星"), new PlmnTableNode("VIBO", "VIBO", "台灣之星"), new PlmnTableNode("Chunghwa", "Chunghwa Telecom", "中華電信"), new PlmnTableNode("TW MOB", "TWN MOBITAI", "東信電訊"), new PlmnTableNode("TWM", "TW Mobile", "台灣大哥大"), new PlmnTableNode("TA-GSM", "TWN TransAsia Telecom GSM", "汎亞電信") };
    PlmnTab = new PlmnTableNode[][] { arrayOfPlmnTableNode1, { localPlmnTableNode1, localPlmnTableNode2, localPlmnTableNode3, localPlmnTableNode4, localPlmnTableNode5, localPlmnTableNode6, localPlmnTableNode7, localPlmnTableNode8, localPlmnTableNode9, localPlmnTableNode10, localPlmnTableNode11, localPlmnTableNode12, localPlmnTableNode13, localPlmnTableNode14, localPlmnTableNode15 }, arrayOfPlmnTableNode2 };
  }
  
  public PlmnTableAsus() {}
  
  public static String GetCustomPlmn(String paramString1, String paramString2)
  {
    if ((!TextUtils.isEmpty(paramString2)) && (paramString1 != null) && (paramString1.length() >= 5))
    {
      if (containInXLS(paramString2) >= 0)
      {
        paramString1 = new StringBuilder();
        paramString1.append("[APLMN] operatorShort change from ");
        paramString1.append(paramString2);
        paramString1.append(" to ");
        paramString1.append("XL");
        Rlog.i("PLMN", paramString1.toString());
        return "XL";
      }
      try
      {
        int i = Integer.valueOf(paramString1.substring(0, 3), 10).intValue();
        if ((i != 460) && (i != 454) && (i != 466)) {
          return null;
        }
        int j = PlmnLangIndex.length;
        paramString1 = Locale.getDefault();
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString1.getLanguage());
        ((StringBuilder)localObject).append("_");
        ((StringBuilder)localObject).append(paramString1.getCountry());
        localObject = ((StringBuilder)localObject).toString();
        for (i = 0; i < j; i++) {
          if (((String)localObject).equals(PlmnLangIndex[i]))
          {
            PlmnTableNode[] arrayOfPlmnTableNode = PlmnTab[i];
            int k = arrayOfPlmnTableNode.length;
            int m = 0;
            while (m < k)
            {
              paramString1 = arrayOfPlmnTableNode[m];
              if ((!paramString2.equalsIgnoreCase(mShort)) && (!paramString2.equalsIgnoreCase(mLong)))
              {
                m++;
              }
              else
              {
                paramString2 = new StringBuilder();
                paramString2.append("short=");
                paramString2.append(mShort);
                paramString2.append(" Long=");
                paramString2.append(mLong);
                paramString2.append(" ChangeTo=");
                paramString2.append(mName);
                Rlog.i("PLMN", paramString2.toString());
                return mName;
              }
            }
          }
        }
        return null;
      }
      catch (NumberFormatException paramString1)
      {
        return null;
      }
    }
    return null;
  }
  
  public static int containInXLS(String paramString)
  {
    int i = -1;
    int j = i;
    if (paramString != null) {
      for (int k = 0;; k++)
      {
        j = i;
        if (k >= XLS.length) {
          break;
        }
        if (XLS[k].equalsIgnoreCase(paramString.trim()))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("[APLMN] contain in XLS: ");
          localStringBuilder.append(paramString);
          Rlog.i("PLMN", localStringBuilder.toString());
          i = k;
        }
      }
    }
    return j;
  }
  
  static class PlmnTableNode
  {
    public String mLong;
    public String mName;
    public String mShort;
    
    PlmnTableNode(String paramString1, String paramString2, String paramString3)
    {
      mShort = paramString1;
      mLong = paramString2;
      mName = paramString3;
    }
  }
}
