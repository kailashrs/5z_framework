package com.google.android.util;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import java.util.ArrayList;

public class SmileyParser
  extends AbstractMessageParser
{
  private SmileyResources mRes;
  
  public SmileyParser(String paramString, SmileyResources paramSmileyResources)
  {
    super(paramString, true, false, false, false, false, false);
    mRes = paramSmileyResources;
  }
  
  protected AbstractMessageParser.Resources getResources()
  {
    return mRes;
  }
  
  public CharSequence getSpannableString(Context paramContext)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    if (getPartCount() == 0) {
      return "";
    }
    int i = 0;
    ArrayList localArrayList = getPart(0).getTokens();
    int j = localArrayList.size();
    while (i < j)
    {
      AbstractMessageParser.Token localToken = (AbstractMessageParser.Token)localArrayList.get(i);
      int k = localSpannableStringBuilder.length();
      localSpannableStringBuilder.append(localToken.getRawText());
      if (localToken.getType() == AbstractMessageParser.Token.Type.SMILEY)
      {
        int m = mRes.getSmileyRes(localToken.getRawText());
        if (m != -1) {
          localSpannableStringBuilder.setSpan(new ImageSpan(paramContext, m), k, localSpannableStringBuilder.length(), 33);
        }
      }
      i++;
    }
    return localSpannableStringBuilder;
  }
}
