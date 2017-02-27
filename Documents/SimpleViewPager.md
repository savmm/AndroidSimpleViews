#Simple View Pager

Simple View Pager Allows to scroll through the container on the same layout by sliding the screen

###Features of Simple View Pager

  - Any ViewGroup based View can be use as Container
  - Swipe Right and Left to swtich pages
  - Programmatically switch pages
  - Continuous Pages
  - Auto Created PageDots
  - Customize style For Active PageDot and Deactive PageDosts
  - Customize PageDots location

#Usages

##Methods and Settings
```groovy
      mSimpleViewPager.setDotsSeparate(10 /*in px*/);
      mSimpleViewPager.setPageDotsAlignment(PageDotsAlignment.BOTTOM);
      mSimpleViewPager.setShowPageDots(true);
      mSimpleViewPager.setDotRadius(14   /*in px*/);
      mSimpleViewPager.setActiveDotRadius(18   /*in px*/);
      mSimpleViewPager.setDotColor( getResources().getColor( android.R.color.holo_purple));
      mSimpleViewPager.setActiveDotColor(getResources().getColor(android.R.color.holo_orange_dark));
 ```  
        
