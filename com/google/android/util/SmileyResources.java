package com.google.android.util;

import java.util.HashMap;
import java.util.Set;

public class SmileyResources
  implements AbstractMessageParser.Resources
{
  private HashMap<String, Integer> mSmileyToRes = new HashMap();
  private final AbstractMessageParser.TrieNode smileys = new AbstractMessageParser.TrieNode();
  
  public SmileyResources(String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      AbstractMessageParser.TrieNode.addToTrie(smileys, paramArrayOfString[i], "");
      mSmileyToRes.put(paramArrayOfString[i], Integer.valueOf(paramArrayOfInt[i]));
    }
  }
  
  public AbstractMessageParser.TrieNode getAcronyms()
  {
    return null;
  }
  
  public AbstractMessageParser.TrieNode getDomainSuffixes()
  {
    return null;
  }
  
  public Set<String> getSchemes()
  {
    return null;
  }
  
  public int getSmileyRes(String paramString)
  {
    paramString = (Integer)mSmileyToRes.get(paramString);
    if (paramString == null) {
      return -1;
    }
    return paramString.intValue();
  }
  
  public AbstractMessageParser.TrieNode getSmileys()
  {
    return smileys;
  }
}
