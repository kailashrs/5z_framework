package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.Parcel;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@SystemApi
public class AudioMixingRule
{
  public static final int RULE_EXCLUDE_ATTRIBUTE_CAPTURE_PRESET = 32770;
  public static final int RULE_EXCLUDE_ATTRIBUTE_USAGE = 32769;
  public static final int RULE_EXCLUDE_UID = 32772;
  private static final int RULE_EXCLUSION_MASK = 32768;
  @SystemApi
  public static final int RULE_MATCH_ATTRIBUTE_CAPTURE_PRESET = 2;
  @SystemApi
  public static final int RULE_MATCH_ATTRIBUTE_USAGE = 1;
  @SystemApi
  public static final int RULE_MATCH_UID = 4;
  private final ArrayList<AudioMixMatchCriterion> mCriteria;
  private final int mTargetMixType;
  
  private AudioMixingRule(int paramInt, ArrayList<AudioMixMatchCriterion> paramArrayList)
  {
    mCriteria = paramArrayList;
    mTargetMixType = paramInt;
  }
  
  private static boolean areCriteriaEquivalent(ArrayList<AudioMixMatchCriterion> paramArrayList1, ArrayList<AudioMixMatchCriterion> paramArrayList2)
  {
    boolean bool = false;
    if ((paramArrayList1 != null) && (paramArrayList2 != null))
    {
      if (paramArrayList1 == paramArrayList2) {
        return true;
      }
      if (paramArrayList1.size() != paramArrayList2.size()) {
        return false;
      }
      if (paramArrayList1.hashCode() == paramArrayList2.hashCode()) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  private static boolean isAudioAttributeRule(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  private static boolean isPlayerRule(int paramInt)
  {
    paramInt = 0xFFFF7FFF & paramInt;
    return (paramInt == 1) || (paramInt == 4);
  }
  
  private static boolean isValidAttributesSystemApiRule(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  private static boolean isValidRule(int paramInt)
  {
    paramInt = 0xFFFF7FFF & paramInt;
    if (paramInt != 4) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  private static boolean isValidSystemApiRule(int paramInt)
  {
    if (paramInt != 4) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (AudioMixingRule)paramObject;
      if ((mTargetMixType != mTargetMixType) || (!areCriteriaEquivalent(mCriteria, mCriteria))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  ArrayList<AudioMixMatchCriterion> getCriteria()
  {
    return mCriteria;
  }
  
  int getTargetMixType()
  {
    return mTargetMixType;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mTargetMixType), mCriteria });
  }
  
  boolean isAffectingUsage(int paramInt)
  {
    Iterator localIterator = mCriteria.iterator();
    while (localIterator.hasNext())
    {
      AudioMixMatchCriterion localAudioMixMatchCriterion = (AudioMixMatchCriterion)localIterator.next();
      if (((mRule & 0x1) != 0) && (mAttr != null) && (mAttr.getUsage() == paramInt)) {
        return true;
      }
    }
    return false;
  }
  
  static final class AudioMixMatchCriterion
  {
    final AudioAttributes mAttr;
    final int mIntProp;
    final int mRule;
    
    AudioMixMatchCriterion(AudioAttributes paramAudioAttributes, int paramInt)
    {
      mAttr = paramAudioAttributes;
      mIntProp = Integer.MIN_VALUE;
      mRule = paramInt;
    }
    
    AudioMixMatchCriterion(Integer paramInteger, int paramInt)
    {
      mAttr = null;
      mIntProp = paramInteger.intValue();
      mRule = paramInt;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mAttr, Integer.valueOf(mIntProp), Integer.valueOf(mRule) });
    }
    
    void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(mRule);
      int i = mRule & 0xFFFF7FFF;
      if (i != 4) {
        switch (i)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown match rule");
          localStringBuilder.append(i);
          localStringBuilder.append(" when writing to Parcel");
          Log.e("AudioMixMatchCriterion", localStringBuilder.toString());
          paramParcel.writeInt(-1);
          break;
        case 2: 
          paramParcel.writeInt(mAttr.getCapturePreset());
          break;
        case 1: 
          paramParcel.writeInt(mAttr.getUsage());
          break;
        }
      } else {
        paramParcel.writeInt(mIntProp);
      }
    }
  }
  
  @SystemApi
  public static class Builder
  {
    private ArrayList<AudioMixingRule.AudioMixMatchCriterion> mCriteria = new ArrayList();
    private int mTargetMixType = -1;
    
    @SystemApi
    public Builder() {}
    
    private Builder addRuleInternal(AudioAttributes paramAudioAttributes, Integer paramInteger, int paramInt)
      throws IllegalArgumentException
    {
      if (mTargetMixType == -1)
      {
        if (AudioMixingRule.isPlayerRule(paramInt)) {
          mTargetMixType = 0;
        } else {
          mTargetMixType = 1;
        }
      }
      else if (((mTargetMixType == 0) && (!AudioMixingRule.isPlayerRule(paramInt))) || ((mTargetMixType == 1) && (AudioMixingRule.isPlayerRule(paramInt)))) {
        throw new IllegalArgumentException("Incompatible rule for mix");
      }
      synchronized (mCriteria)
      {
        Object localObject = mCriteria.iterator();
        int i = 0xFFFF7FFF & paramInt;
        while (((Iterator)localObject).hasNext())
        {
          AudioMixingRule.AudioMixMatchCriterion localAudioMixMatchCriterion = (AudioMixingRule.AudioMixMatchCriterion)((Iterator)localObject).next();
          if (i != 4)
          {
            switch (i)
            {
            default: 
              break;
            case 2: 
              if (mAttr.getCapturePreset() != paramAudioAttributes.getCapturePreset()) {
                break;
              }
              if (mRule == paramInt) {
                return this;
              }
              localObject = new java/lang/IllegalArgumentException;
              paramInteger = new java/lang/StringBuilder;
              paramInteger.<init>();
              paramInteger.append("Contradictory rule exists for ");
              paramInteger.append(paramAudioAttributes);
              ((IllegalArgumentException)localObject).<init>(paramInteger.toString());
              throw ((Throwable)localObject);
            case 1: 
              if (mAttr.getUsage() != paramAudioAttributes.getUsage()) {
                break;
              }
              if (mRule == paramInt) {
                return this;
              }
              localObject = new java/lang/IllegalArgumentException;
              paramInteger = new java/lang/StringBuilder;
              paramInteger.<init>();
              paramInteger.append("Contradictory rule exists for ");
              paramInteger.append(paramAudioAttributes);
              ((IllegalArgumentException)localObject).<init>(paramInteger.toString());
              throw ((Throwable)localObject);
            }
          }
          else if (mIntProp == paramInteger.intValue())
          {
            if (mRule == paramInt) {
              return this;
            }
            localObject = new java/lang/IllegalArgumentException;
            paramAudioAttributes = new java/lang/StringBuilder;
            paramAudioAttributes.<init>();
            paramAudioAttributes.append("Contradictory rule exists for UID ");
            paramAudioAttributes.append(paramInteger);
            ((IllegalArgumentException)localObject).<init>(paramAudioAttributes.toString());
            throw ((Throwable)localObject);
          }
        }
        if (i != 4)
        {
          switch (i)
          {
          default: 
            paramAudioAttributes = new java/lang/IllegalStateException;
            paramAudioAttributes.<init>("Unreachable code in addRuleInternal()");
            throw paramAudioAttributes;
          }
          paramInteger = mCriteria;
          localObject = new android/media/audiopolicy/AudioMixingRule$AudioMixMatchCriterion;
          ((AudioMixingRule.AudioMixMatchCriterion)localObject).<init>(paramAudioAttributes, paramInt);
          paramInteger.add(localObject);
        }
        else
        {
          localObject = mCriteria;
          paramAudioAttributes = new android/media/audiopolicy/AudioMixingRule$AudioMixMatchCriterion;
          paramAudioAttributes.<init>(paramInteger, paramInt);
          ((ArrayList)localObject).add(paramAudioAttributes);
        }
        return this;
      }
    }
    
    private Builder checkAddRuleObjInternal(int paramInt, Object paramObject)
      throws IllegalArgumentException
    {
      if (paramObject != null)
      {
        if (AudioMixingRule.isValidRule(paramInt))
        {
          if (AudioMixingRule.isAudioAttributeRule(0xFFFF7FFF & paramInt))
          {
            if ((paramObject instanceof AudioAttributes)) {
              return addRuleInternal((AudioAttributes)paramObject, null, paramInt);
            }
            throw new IllegalArgumentException("Invalid AudioAttributes argument");
          }
          if ((paramObject instanceof Integer)) {
            return addRuleInternal(null, (Integer)paramObject, paramInt);
          }
          throw new IllegalArgumentException("Invalid Integer argument");
        }
        paramObject = new StringBuilder();
        paramObject.append("Illegal rule value ");
        paramObject.append(paramInt);
        throw new IllegalArgumentException(paramObject.toString());
      }
      throw new IllegalArgumentException("Illegal null argument for mixing rule");
    }
    
    @SystemApi
    public Builder addMixRule(int paramInt, Object paramObject)
      throws IllegalArgumentException
    {
      if (AudioMixingRule.isValidSystemApiRule(paramInt)) {
        return checkAddRuleObjInternal(paramInt, paramObject);
      }
      paramObject = new StringBuilder();
      paramObject.append("Illegal rule value ");
      paramObject.append(paramInt);
      throw new IllegalArgumentException(paramObject.toString());
    }
    
    @SystemApi
    public Builder addRule(AudioAttributes paramAudioAttributes, int paramInt)
      throws IllegalArgumentException
    {
      if (AudioMixingRule.isValidAttributesSystemApiRule(paramInt)) {
        return checkAddRuleObjInternal(paramInt, paramAudioAttributes);
      }
      paramAudioAttributes = new StringBuilder();
      paramAudioAttributes.append("Illegal rule value ");
      paramAudioAttributes.append(paramInt);
      throw new IllegalArgumentException(paramAudioAttributes.toString());
    }
    
    Builder addRuleFromParcel(Parcel paramParcel)
      throws IllegalArgumentException
    {
      int i = paramParcel.readInt();
      int j = 0xFFFF7FFF & i;
      Object localObject = null;
      Integer localInteger = null;
      if (j != 4)
      {
        switch (j)
        {
        default: 
          paramParcel.readInt();
          paramParcel = new StringBuilder();
          paramParcel.append("Illegal rule value ");
          paramParcel.append(i);
          paramParcel.append(" in parcel");
          throw new IllegalArgumentException(paramParcel.toString());
        case 2: 
          j = paramParcel.readInt();
          paramParcel = new AudioAttributes.Builder().setInternalCapturePreset(j).build();
          break;
        case 1: 
          j = paramParcel.readInt();
          paramParcel = new AudioAttributes.Builder().setUsage(j).build();
          break;
        }
      }
      else
      {
        localInteger = new Integer(paramParcel.readInt());
        paramParcel = localObject;
      }
      return addRuleInternal(paramParcel, localInteger, i);
    }
    
    public AudioMixingRule build()
    {
      return new AudioMixingRule(mTargetMixType, mCriteria, null);
    }
    
    @SystemApi
    public Builder excludeMixRule(int paramInt, Object paramObject)
      throws IllegalArgumentException
    {
      if (AudioMixingRule.isValidSystemApiRule(paramInt)) {
        return checkAddRuleObjInternal(0x8000 | paramInt, paramObject);
      }
      paramObject = new StringBuilder();
      paramObject.append("Illegal rule value ");
      paramObject.append(paramInt);
      throw new IllegalArgumentException(paramObject.toString());
    }
    
    @SystemApi
    public Builder excludeRule(AudioAttributes paramAudioAttributes, int paramInt)
      throws IllegalArgumentException
    {
      if (AudioMixingRule.isValidAttributesSystemApiRule(paramInt)) {
        return checkAddRuleObjInternal(0x8000 | paramInt, paramAudioAttributes);
      }
      paramAudioAttributes = new StringBuilder();
      paramAudioAttributes.append("Illegal rule value ");
      paramAudioAttributes.append(paramInt);
      throw new IllegalArgumentException(paramAudioAttributes.toString());
    }
  }
}
