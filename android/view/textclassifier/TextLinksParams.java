package android.view.textclassifier;

import android.text.Spannable;
import android.text.style.ClickableSpan;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public final class TextLinksParams
{
  private static final Function<TextLinks.TextLink, TextLinks.TextLinkSpan> DEFAULT_SPAN_FACTORY = _..Lambda.TextLinksParams.km8pN8nazHT6NQiHykIrRALWbkE.INSTANCE;
  private final int mApplyStrategy;
  private final TextClassifier.EntityConfig mEntityConfig;
  private final Function<TextLinks.TextLink, TextLinks.TextLinkSpan> mSpanFactory;
  
  private TextLinksParams(int paramInt, Function<TextLinks.TextLink, TextLinks.TextLinkSpan> paramFunction)
  {
    mApplyStrategy = paramInt;
    mSpanFactory = paramFunction;
    mEntityConfig = TextClassifier.EntityConfig.createWithHints(null);
  }
  
  private static int checkApplyStrategy(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 1)) {
      throw new IllegalArgumentException("Invalid apply strategy. See TextLinksParams.ApplyStrategy for options.");
    }
    return paramInt;
  }
  
  public static TextLinksParams fromLinkMask(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramInt & 0x1) != 0) {
      localArrayList.add("url");
    }
    if ((paramInt & 0x2) != 0) {
      localArrayList.add("email");
    }
    if ((paramInt & 0x4) != 0) {
      localArrayList.add("phone");
    }
    if ((paramInt & 0x8) != 0) {
      localArrayList.add("address");
    }
    return new Builder().setEntityConfig(TextClassifier.EntityConfig.createWithExplicitEntityList(localArrayList)).build();
  }
  
  public int apply(Spannable paramSpannable, TextLinks paramTextLinks)
  {
    Preconditions.checkNotNull(paramSpannable);
    Preconditions.checkNotNull(paramTextLinks);
    if (!paramSpannable.toString().startsWith(paramTextLinks.getText())) {
      return 3;
    }
    if (paramTextLinks.getLinks().isEmpty()) {
      return 1;
    }
    int i = 0;
    Iterator localIterator = paramTextLinks.getLinks().iterator();
    for (;;)
    {
      boolean bool = localIterator.hasNext();
      int j = 0;
      if (!bool) {
        break;
      }
      TextLinks.TextLink localTextLink = (TextLinks.TextLink)localIterator.next();
      paramTextLinks = (TextLinks.TextLinkSpan)mSpanFactory.apply(localTextLink);
      int k = i;
      if (paramTextLinks != null)
      {
        ClickableSpan[] arrayOfClickableSpan = (ClickableSpan[])paramSpannable.getSpans(localTextLink.getStart(), localTextLink.getEnd(), ClickableSpan.class);
        if (arrayOfClickableSpan.length > 0)
        {
          k = i;
          if (mApplyStrategy == 1)
          {
            int m = arrayOfClickableSpan.length;
            for (k = j; k < m; k++) {
              paramSpannable.removeSpan(arrayOfClickableSpan[k]);
            }
            paramSpannable.setSpan(paramTextLinks, localTextLink.getStart(), localTextLink.getEnd(), 33);
            k = i + 1;
          }
        }
        else
        {
          paramSpannable.setSpan(paramTextLinks, localTextLink.getStart(), localTextLink.getEnd(), 33);
          k = i + 1;
        }
      }
      i = k;
    }
    if (i == 0) {
      return 2;
    }
    return 0;
  }
  
  public TextClassifier.EntityConfig getEntityConfig()
  {
    return mEntityConfig;
  }
  
  public static final class Builder
  {
    private int mApplyStrategy = 0;
    private Function<TextLinks.TextLink, TextLinks.TextLinkSpan> mSpanFactory = TextLinksParams.DEFAULT_SPAN_FACTORY;
    
    public Builder() {}
    
    public TextLinksParams build()
    {
      return new TextLinksParams(mApplyStrategy, mSpanFactory, null);
    }
    
    public Builder setApplyStrategy(int paramInt)
    {
      mApplyStrategy = TextLinksParams.checkApplyStrategy(paramInt);
      return this;
    }
    
    public Builder setEntityConfig(TextClassifier.EntityConfig paramEntityConfig)
    {
      return this;
    }
    
    public Builder setSpanFactory(Function<TextLinks.TextLink, TextLinks.TextLinkSpan> paramFunction)
    {
      if (paramFunction == null) {
        paramFunction = TextLinksParams.DEFAULT_SPAN_FACTORY;
      }
      mSpanFactory = paramFunction;
      return this;
    }
  }
}
