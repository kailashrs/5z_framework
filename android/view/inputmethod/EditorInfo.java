package android.view.inputmethod;

import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Printer;
import java.util.Arrays;

public class EditorInfo
  implements InputType, Parcelable
{
  public static final Parcelable.Creator<EditorInfo> CREATOR = new Parcelable.Creator()
  {
    public EditorInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      EditorInfo localEditorInfo = new EditorInfo();
      inputType = paramAnonymousParcel.readInt();
      imeOptions = paramAnonymousParcel.readInt();
      privateImeOptions = paramAnonymousParcel.readString();
      actionLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel));
      actionId = paramAnonymousParcel.readInt();
      initialSelStart = paramAnonymousParcel.readInt();
      initialSelEnd = paramAnonymousParcel.readInt();
      initialCapsMode = paramAnonymousParcel.readInt();
      hintText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel));
      label = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel));
      packageName = paramAnonymousParcel.readString();
      fieldId = paramAnonymousParcel.readInt();
      fieldName = paramAnonymousParcel.readString();
      extras = paramAnonymousParcel.readBundle();
      LocaleList localLocaleList = (LocaleList)LocaleList.CREATOR.createFromParcel(paramAnonymousParcel);
      if (localLocaleList.isEmpty()) {
        localLocaleList = null;
      }
      hintLocales = localLocaleList;
      contentMimeTypes = paramAnonymousParcel.readStringArray();
      return localEditorInfo;
    }
    
    public EditorInfo[] newArray(int paramAnonymousInt)
    {
      return new EditorInfo[paramAnonymousInt];
    }
  };
  public static final int IME_ACTION_DONE = 6;
  public static final int IME_ACTION_GO = 2;
  public static final int IME_ACTION_NEXT = 5;
  public static final int IME_ACTION_NONE = 1;
  public static final int IME_ACTION_PREVIOUS = 7;
  public static final int IME_ACTION_SEARCH = 3;
  public static final int IME_ACTION_SEND = 4;
  public static final int IME_ACTION_UNSPECIFIED = 0;
  public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
  public static final int IME_FLAG_NAVIGATE_NEXT = 134217728;
  public static final int IME_FLAG_NAVIGATE_PREVIOUS = 67108864;
  public static final int IME_FLAG_NO_ACCESSORY_ACTION = 536870912;
  public static final int IME_FLAG_NO_ENTER_ACTION = 1073741824;
  public static final int IME_FLAG_NO_EXTRACT_UI = 268435456;
  public static final int IME_FLAG_NO_FULLSCREEN = 33554432;
  public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
  public static final int IME_MASK_ACTION = 255;
  public static final int IME_NULL = 0;
  public int actionId = 0;
  public CharSequence actionLabel = null;
  public String[] contentMimeTypes = null;
  public Bundle extras;
  public int fieldId;
  public String fieldName;
  public LocaleList hintLocales = null;
  public CharSequence hintText;
  public int imeOptions = 0;
  public int initialCapsMode = 0;
  public int initialSelEnd = -1;
  public int initialSelStart = -1;
  public int inputType = 0;
  public CharSequence label;
  public String packageName;
  public String privateImeOptions = null;
  
  public EditorInfo() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("inputType=0x");
    localStringBuilder.append(Integer.toHexString(inputType));
    localStringBuilder.append(" imeOptions=0x");
    localStringBuilder.append(Integer.toHexString(imeOptions));
    localStringBuilder.append(" privateImeOptions=");
    localStringBuilder.append(privateImeOptions);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("actionLabel=");
    localStringBuilder.append(actionLabel);
    localStringBuilder.append(" actionId=");
    localStringBuilder.append(actionId);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("initialSelStart=");
    localStringBuilder.append(initialSelStart);
    localStringBuilder.append(" initialSelEnd=");
    localStringBuilder.append(initialSelEnd);
    localStringBuilder.append(" initialCapsMode=0x");
    localStringBuilder.append(Integer.toHexString(initialCapsMode));
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("hintText=");
    localStringBuilder.append(hintText);
    localStringBuilder.append(" label=");
    localStringBuilder.append(label);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("packageName=");
    localStringBuilder.append(packageName);
    localStringBuilder.append(" fieldId=");
    localStringBuilder.append(fieldId);
    localStringBuilder.append(" fieldName=");
    localStringBuilder.append(fieldName);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("extras=");
    localStringBuilder.append(extras);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("hintLocales=");
    localStringBuilder.append(hintLocales);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("contentMimeTypes=");
    localStringBuilder.append(Arrays.toString(contentMimeTypes));
    paramPrinter.println(localStringBuilder.toString());
  }
  
  public final void makeCompatible(int paramInt)
  {
    if (paramInt < 11)
    {
      paramInt = inputType & 0xFFF;
      if ((paramInt != 2) && (paramInt != 18))
      {
        if (paramInt != 209)
        {
          if (paramInt == 225) {
            inputType = (0x81 | inputType & 0xFFF000);
          }
        }
        else {
          inputType = (0x21 | inputType & 0xFFF000);
        }
      }
      else {
        inputType = (inputType & 0xFFF000 | 0x2);
      }
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(inputType);
    paramParcel.writeInt(imeOptions);
    paramParcel.writeString(privateImeOptions);
    TextUtils.writeToParcel(actionLabel, paramParcel, paramInt);
    paramParcel.writeInt(actionId);
    paramParcel.writeInt(initialSelStart);
    paramParcel.writeInt(initialSelEnd);
    paramParcel.writeInt(initialCapsMode);
    TextUtils.writeToParcel(hintText, paramParcel, paramInt);
    TextUtils.writeToParcel(label, paramParcel, paramInt);
    paramParcel.writeString(packageName);
    paramParcel.writeInt(fieldId);
    paramParcel.writeString(fieldName);
    paramParcel.writeBundle(extras);
    if (hintLocales != null) {
      hintLocales.writeToParcel(paramParcel, paramInt);
    } else {
      LocaleList.getEmptyLocaleList().writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeStringArray(contentMimeTypes);
  }
}
