package com.android.internal.widget;

import android.app.Notification.MessagingStyle.Message;
import android.app.Person;
import android.app.Person.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.util.NotificationColorUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RemoteViews.RemoteView
public class MessagingLayout
  extends FrameLayout
{
  private static final float COLOR_SHIFT_AMOUNT = 60.0F;
  public static final Interpolator FAST_OUT_LINEAR_IN = new PathInterpolator(0.4F, 0.0F, 1.0F, 1.0F);
  public static final Interpolator FAST_OUT_SLOW_IN = new PathInterpolator(0.4F, 0.0F, 0.2F, 1.0F);
  public static final Interpolator LINEAR_OUT_SLOW_IN;
  public static final View.OnLayoutChangeListener MESSAGING_PROPERTY_ANIMATOR = new MessagingPropertyAnimator();
  private static final Consumer<MessagingMessage> REMOVE_MESSAGE;
  private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
  private ArrayList<MessagingGroup> mAddedGroups = new ArrayList();
  private Icon mAvatarReplacement;
  private int mAvatarSize;
  private CharSequence mConversationTitle;
  private boolean mDisplayImagesAtEnd;
  private ArrayList<MessagingGroup> mGroups = new ArrayList();
  private List<MessagingMessage> mHistoricMessages = new ArrayList();
  private boolean mIsOneToOne;
  private int mLayoutColor;
  private int mMessageTextColor;
  private List<MessagingMessage> mMessages = new ArrayList();
  private MessagingLinearLayout mMessagingLinearLayout;
  private CharSequence mNameReplacement;
  private Paint mPaint = new Paint(1);
  private int mSenderTextColor;
  private boolean mShowHistoricMessages;
  private Paint mTextPaint = new Paint();
  private TextView mTitleView;
  private Person mUser;
  
  static
  {
    REMOVE_MESSAGE = _..Lambda.DKD2sNhLnyRFoBkFvfwKyxoEx10.INSTANCE;
    LINEAR_OUT_SLOW_IN = new PathInterpolator(0.0F, 0.0F, 0.2F, 1.0F);
  }
  
  public MessagingLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public MessagingLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public MessagingLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public MessagingLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void addMessagesToGroups(List<MessagingMessage> paramList1, List<MessagingMessage> paramList2, boolean paramBoolean)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    findGroups(paramList1, paramList2, localArrayList1, localArrayList2);
    createGroupViews(localArrayList1, localArrayList2, paramBoolean);
  }
  
  private void addRemoteInputHistoryToMessages(List<Notification.MessagingStyle.Message> paramList, CharSequence[] paramArrayOfCharSequence)
  {
    if ((paramArrayOfCharSequence != null) && (paramArrayOfCharSequence.length != 0))
    {
      for (int i = paramArrayOfCharSequence.length - 1; i >= 0; i--) {
        paramList.add(new Notification.MessagingStyle.Message(paramArrayOfCharSequence[i], 0L, (Person)null, true));
      }
      return;
    }
  }
  
  private void bind(List<Notification.MessagingStyle.Message> paramList1, List<Notification.MessagingStyle.Message> paramList2, boolean paramBoolean)
  {
    paramList2 = createMessages(paramList2, true);
    paramList1 = createMessages(paramList1, false);
    ArrayList localArrayList = new ArrayList(mGroups);
    addMessagesToGroups(paramList2, paramList1, paramBoolean);
    removeGroups(localArrayList);
    mMessages.forEach(REMOVE_MESSAGE);
    mHistoricMessages.forEach(REMOVE_MESSAGE);
    mMessages = paramList1;
    mHistoricMessages = paramList2;
    updateHistoricMessageVisibility();
    updateTitleAndNamesDisplay();
  }
  
  private void createGroupViews(List<List<MessagingMessage>> paramList, List<Person> paramList1, boolean paramBoolean)
  {
    mGroups.clear();
    for (int i = 0; i < paramList.size(); i++)
    {
      List localList = (List)paramList.get(i);
      Object localObject1 = null;
      int j = localList.size();
      boolean bool = true;
      j--;
      while (j >= 0)
      {
        localObject1 = ((MessagingMessage)localList.get(j)).getGroup();
        if (localObject1 != null) {
          break;
        }
        j--;
      }
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = MessagingGroup.createGroup(mMessagingLinearLayout);
        mAddedGroups.add(localObject2);
      }
      ((MessagingGroup)localObject2).setDisplayImagesAtEnd(mDisplayImagesAtEnd);
      ((MessagingGroup)localObject2).setLayoutColor(mLayoutColor);
      ((MessagingGroup)localObject2).setTextColors(mSenderTextColor, mMessageTextColor);
      Person localPerson = (Person)paramList1.get(i);
      Object localObject3 = null;
      localObject1 = localObject3;
      if (localPerson != mUser)
      {
        localObject1 = localObject3;
        if (mNameReplacement != null) {
          localObject1 = mNameReplacement;
        }
      }
      ((MessagingGroup)localObject2).setSender(localPerson, (CharSequence)localObject1);
      if ((i != paramList.size() - 1) || (!paramBoolean)) {
        bool = false;
      }
      ((MessagingGroup)localObject2).setSending(bool);
      mGroups.add(localObject2);
      if (mMessagingLinearLayout.indexOfChild((View)localObject2) != i)
      {
        mMessagingLinearLayout.removeView((View)localObject2);
        mMessagingLinearLayout.addView((View)localObject2, i);
      }
      ((MessagingGroup)localObject2).setMessages(localList);
    }
  }
  
  private List<MessagingMessage> createMessages(List<Notification.MessagingStyle.Message> paramList, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramList.size(); i++)
    {
      Notification.MessagingStyle.Message localMessage = (Notification.MessagingStyle.Message)paramList.get(i);
      MessagingMessage localMessagingMessage1 = findAndRemoveMatchingMessage(localMessage);
      MessagingMessage localMessagingMessage2 = localMessagingMessage1;
      if (localMessagingMessage1 == null) {
        localMessagingMessage2 = MessagingMessage.createMessage(this, localMessage);
      }
      localMessagingMessage2.setIsHistoric(paramBoolean);
      localArrayList.add(localMessagingMessage2);
    }
    return localArrayList;
  }
  
  private MessagingMessage findAndRemoveMatchingMessage(Notification.MessagingStyle.Message paramMessage)
  {
    int i = 0;
    MessagingMessage localMessagingMessage;
    for (int j = 0; j < mMessages.size(); j++)
    {
      localMessagingMessage = (MessagingMessage)mMessages.get(j);
      if (localMessagingMessage.sameAs(paramMessage))
      {
        mMessages.remove(j);
        return localMessagingMessage;
      }
    }
    for (j = i; j < mHistoricMessages.size(); j++)
    {
      localMessagingMessage = (MessagingMessage)mHistoricMessages.get(j);
      if (localMessagingMessage.sameAs(paramMessage))
      {
        mHistoricMessages.remove(j);
        return localMessagingMessage;
      }
    }
    return null;
  }
  
  private int findColor(CharSequence paramCharSequence, int paramInt)
  {
    double d = NotificationColorUtil.calculateLuminance(paramInt);
    return NotificationColorUtil.getShiftedColor(paramInt, (int)(60.0F * (float)((float)(Math.abs(paramCharSequence.hashCode()) % 5 / 4.0F - 0.5F + Math.max(0.30000001192092896D - d, 0.0D)) - Math.max(0.30000001192092896D - (1.0D - d), 0.0D))));
  }
  
  private void findGroups(List<MessagingMessage> paramList1, List<MessagingMessage> paramList2, List<List<MessagingMessage>> paramList, List<Person> paramList3)
  {
    int i = paramList1.size();
    Object localObject1 = null;
    Object localObject2 = null;
    int j = 0;
    while (j < paramList2.size() + i)
    {
      MessagingMessage localMessagingMessage;
      if (j < i) {
        localMessagingMessage = (MessagingMessage)paramList1.get(j);
      } else {
        localMessagingMessage = (MessagingMessage)paramList2.get(j - i);
      }
      int k;
      if (localObject1 == null) {
        k = 1;
      } else {
        k = 0;
      }
      Person localPerson = localMessagingMessage.getMessage().getSenderPerson();
      Object localObject3;
      if (localPerson == null) {
        localObject3 = null;
      } else if (localPerson.getKey() == null) {
        localObject3 = localPerson.getName();
      } else {
        localObject3 = localPerson.getKey();
      }
      Object localObject4 = localObject2;
      if ((true ^ TextUtils.equals((CharSequence)localObject3, (CharSequence)localObject2) | k))
      {
        localObject2 = new ArrayList();
        paramList.add(localObject2);
        localObject1 = localPerson;
        if (localPerson == null) {
          localObject1 = mUser;
        }
        paramList3.add(localObject1);
        localObject1 = localObject2;
        localObject4 = localObject3;
      }
      ((List)localObject1).add(localMessagingMessage);
      j++;
      localObject2 = localObject4;
    }
  }
  
  private String findNameSplit(String paramString)
  {
    String[] arrayOfString = paramString.split(" ");
    if (arrayOfString.length > 1)
    {
      paramString = new StringBuilder();
      paramString.append(Character.toString(arrayOfString[0].charAt(0)));
      paramString.append(Character.toString(arrayOfString[1].charAt(0)));
      return paramString.toString();
    }
    return paramString.substring(0, 1);
  }
  
  private void removeGroups(ArrayList<MessagingGroup> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      MessagingGroup localMessagingGroup = (MessagingGroup)paramArrayList.get(j);
      if (!mGroups.contains(localMessagingGroup))
      {
        List localList = localMessagingGroup.getMessages();
        _..Lambda.MessagingLayout.AR_BLYGwVbm8HbmaOhECHwnOBBg localAR_BLYGwVbm8HbmaOhECHwnOBBg = new _..Lambda.MessagingLayout.AR_BLYGwVbm8HbmaOhECHwnOBBg(this, localMessagingGroup);
        boolean bool = localMessagingGroup.isShown();
        mMessagingLinearLayout.removeView(localMessagingGroup);
        if ((bool) && (!MessagingLinearLayout.isGone(localMessagingGroup)))
        {
          mMessagingLinearLayout.addTransientView(localMessagingGroup, 0);
          localMessagingGroup.removeGroupAnimated(localAR_BLYGwVbm8HbmaOhECHwnOBBg);
        }
        else
        {
          localAR_BLYGwVbm8HbmaOhECHwnOBBg.run();
        }
        mMessages.removeAll(localList);
        mHistoricMessages.removeAll(localList);
      }
    }
  }
  
  private void updateHistoricMessageVisibility()
  {
    int i = mHistoricMessages.size();
    int k;
    Object localObject;
    for (int j = 0;; j++)
    {
      k = 8;
      if (j >= i) {
        break;
      }
      localObject = (MessagingMessage)mHistoricMessages.get(j);
      if (mShowHistoricMessages) {
        k = 0;
      }
      ((MessagingMessage)localObject).setVisibility(k);
    }
    int m = mGroups.size();
    for (j = 0; j < m; j++)
    {
      MessagingGroup localMessagingGroup = (MessagingGroup)mGroups.get(j);
      localObject = localMessagingGroup.getMessages();
      int n = ((List)localObject).size();
      k = 0;
      int i1 = 0;
      while (i1 < n)
      {
        i = k;
        if (((MessagingMessage)((List)localObject).get(i1)).getVisibility() != 8) {
          i = k + 1;
        }
        i1++;
        k = i;
      }
      if ((k > 0) && (localMessagingGroup.getVisibility() == 8)) {
        localMessagingGroup.setVisibility(0);
      } else if ((k == 0) && (localMessagingGroup.getVisibility() != 8)) {
        localMessagingGroup.setVisibility(8);
      }
    }
  }
  
  private void updateTitleAndNamesDisplay()
  {
    ArrayMap localArrayMap = new ArrayMap();
    Object localObject1 = new ArrayMap();
    int i = 0;
    Object localObject3;
    for (int j = 0; j < mGroups.size(); j++)
    {
      localObject2 = (MessagingGroup)mGroups.get(j);
      localObject3 = ((MessagingGroup)localObject2).getSenderName();
      if ((((MessagingGroup)localObject2).needsGeneratedAvatar()) && (!TextUtils.isEmpty((CharSequence)localObject3)) && (!localArrayMap.containsKey(localObject3)))
      {
        char c = ((CharSequence)localObject3).charAt(0);
        if (((ArrayMap)localObject1).containsKey(Character.valueOf(c)))
        {
          localObject2 = (CharSequence)((ArrayMap)localObject1).get(Character.valueOf(c));
          if (localObject2 != null)
          {
            localArrayMap.put(localObject2, findNameSplit((String)localObject2));
            ((ArrayMap)localObject1).put(Character.valueOf(c), null);
          }
          localArrayMap.put(localObject3, findNameSplit((String)localObject3));
        }
        else
        {
          localArrayMap.put(localObject3, Character.toString(c));
          ((ArrayMap)localObject1).put(Character.valueOf(c), localObject3);
        }
      }
    }
    Object localObject2 = new ArrayMap();
    for (j = 0; j < mGroups.size(); j++)
    {
      localObject1 = (MessagingGroup)mGroups.get(j);
      int k;
      if (((MessagingGroup)localObject1).getSender() == mUser) {
        k = 1;
      } else {
        k = 0;
      }
      localObject3 = ((MessagingGroup)localObject1).getSenderName();
      if ((((MessagingGroup)localObject1).needsGeneratedAvatar()) && (!TextUtils.isEmpty((CharSequence)localObject3)) && ((!mIsOneToOne) || (mAvatarReplacement == null) || (k != 0)))
      {
        localObject1 = ((MessagingGroup)localObject1).getAvatarSymbolIfMatching((CharSequence)localObject3, (String)localArrayMap.get(localObject3), mLayoutColor);
        if (localObject1 != null) {
          ((ArrayMap)localObject2).put(localObject3, localObject1);
        }
      }
    }
    for (j = i; j < mGroups.size(); j++)
    {
      MessagingGroup localMessagingGroup = (MessagingGroup)mGroups.get(j);
      CharSequence localCharSequence = localMessagingGroup.getSenderName();
      if ((localMessagingGroup.needsGeneratedAvatar()) && (!TextUtils.isEmpty(localCharSequence))) {
        if ((mIsOneToOne) && (mAvatarReplacement != null) && (localMessagingGroup.getSender() != mUser))
        {
          localMessagingGroup.setAvatar(mAvatarReplacement);
        }
        else
        {
          localObject1 = (Icon)((ArrayMap)localObject2).get(localCharSequence);
          localObject3 = localObject1;
          if (localObject1 == null)
          {
            localObject3 = createAvatarSymbol(localCharSequence, (String)localArrayMap.get(localCharSequence), mLayoutColor);
            ((ArrayMap)localObject2).put(localCharSequence, localObject3);
          }
          localMessagingGroup.setCreatedAvatar((Icon)localObject3, localCharSequence, (String)localArrayMap.get(localCharSequence), mLayoutColor);
        }
      }
    }
  }
  
  public Icon createAvatarSymbol(CharSequence paramCharSequence, String paramString, int paramInt)
  {
    if ((!paramString.isEmpty()) && (!TextUtils.isDigitsOnly(paramString)) && (!SPECIAL_CHAR_PATTERN.matcher(paramString).find()))
    {
      Bitmap localBitmap = Bitmap.createBitmap(mAvatarSize, mAvatarSize, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      float f1 = mAvatarSize / 2.0F;
      paramInt = findColor(paramCharSequence, paramInt);
      mPaint.setColor(paramInt);
      localCanvas.drawCircle(f1, f1, f1, mPaint);
      if (ColorUtils.calculateLuminance(paramInt) > 0.5D) {
        paramInt = 1;
      } else {
        paramInt = 0;
      }
      paramCharSequence = mTextPaint;
      if (paramInt != 0) {
        paramInt = -16777216;
      } else {
        paramInt = -1;
      }
      paramCharSequence.setColor(paramInt);
      paramCharSequence = mTextPaint;
      float f2;
      if (paramString.length() == 1) {
        f2 = mAvatarSize;
      }
      for (float f3 = 0.5F;; f3 = 0.3F)
      {
        break;
        f2 = mAvatarSize;
      }
      paramCharSequence.setTextSize(f2 * f3);
      localCanvas.drawText(paramString, f1, (int)(f1 - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0F), mTextPaint);
      return Icon.createWithBitmap(localBitmap);
    }
    paramString = Icon.createWithResource(getContext(), 17303230);
    paramString.setTint(findColor(paramCharSequence, paramInt));
    return paramString;
  }
  
  public ArrayList<MessagingGroup> getMessagingGroups()
  {
    return mGroups;
  }
  
  public MessagingLinearLayout getMessagingLinearLayout()
  {
    return mMessagingLinearLayout;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mMessagingLinearLayout = ((MessagingLinearLayout)findViewById(16909185));
    mMessagingLinearLayout.setMessagingLayout(this);
    Object localObject = getResources().getDisplayMetrics();
    int i = Math.max(widthPixels, heightPixels);
    localObject = new Rect(0, 0, i, i);
    mMessagingLinearLayout.setClipBounds((Rect)localObject);
    mTitleView = ((TextView)findViewById(16908310));
    mAvatarSize = getResources().getDimensionPixelSize(17105291);
    mTextPaint.setTextAlign(Paint.Align.CENTER);
    mTextPaint.setAntiAlias(true);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!mAddedGroups.isEmpty()) {
      getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
      {
        public boolean onPreDraw()
        {
          Iterator localIterator = mAddedGroups.iterator();
          while (localIterator.hasNext())
          {
            MessagingGroup localMessagingGroup = (MessagingGroup)localIterator.next();
            if (localMessagingGroup.isShown())
            {
              MessagingPropertyAnimator.fadeIn(localMessagingGroup.getAvatar());
              MessagingPropertyAnimator.fadeIn(localMessagingGroup.getSenderView());
              MessagingPropertyAnimator.startLocalTranslationFrom(localMessagingGroup, localMessagingGroup.getHeight(), MessagingLayout.LINEAR_OUT_SLOW_IN);
            }
          }
          mAddedGroups.clear();
          getViewTreeObserver().removeOnPreDrawListener(this);
          return true;
        }
      });
    }
  }
  
  @RemotableViewMethod
  public void setAvatarReplacement(Icon paramIcon)
  {
    mAvatarReplacement = paramIcon;
  }
  
  @RemotableViewMethod
  public void setData(Bundle paramBundle)
  {
    Object localObject1 = paramBundle.getParcelableArray("android.messages");
    localObject1 = Notification.MessagingStyle.Message.getMessagesFromBundleArray((Parcelable[])localObject1);
    Object localObject2 = paramBundle.getParcelableArray("android.messages.historic");
    localObject2 = Notification.MessagingStyle.Message.getMessagesFromBundleArray((Parcelable[])localObject2);
    setUser((Person)paramBundle.getParcelable("android.messagingUser"));
    mConversationTitle = null;
    TextView localTextView = (TextView)findViewById(16908998);
    if (localTextView != null) {
      mConversationTitle = localTextView.getText();
    }
    addRemoteInputHistoryToMessages((List)localObject1, paramBundle.getCharSequenceArray("android.remoteInputHistory"));
    bind((List)localObject1, (List)localObject2, paramBundle.getBoolean("android.remoteInputSpinner", false));
  }
  
  @RemotableViewMethod
  public void setDisplayImagesAtEnd(boolean paramBoolean)
  {
    mDisplayImagesAtEnd = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setIsOneToOne(boolean paramBoolean)
  {
    mIsOneToOne = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setLayoutColor(int paramInt)
  {
    mLayoutColor = paramInt;
  }
  
  @RemotableViewMethod
  public void setMessageTextColor(int paramInt)
  {
    mMessageTextColor = paramInt;
  }
  
  @RemotableViewMethod
  public void setNameReplacement(CharSequence paramCharSequence)
  {
    mNameReplacement = paramCharSequence;
  }
  
  @RemotableViewMethod
  public void setSenderTextColor(int paramInt)
  {
    mSenderTextColor = paramInt;
  }
  
  public void setUser(Person paramPerson)
  {
    mUser = paramPerson;
    if (mUser.getIcon() == null)
    {
      paramPerson = Icon.createWithResource(getContext(), 17303230);
      paramPerson.setTint(mLayoutColor);
      mUser = mUser.toBuilder().setIcon(paramPerson).build();
    }
  }
  
  public void showHistoricMessages(boolean paramBoolean)
  {
    mShowHistoricMessages = paramBoolean;
    updateHistoricMessageVisibility();
  }
}
