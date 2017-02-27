#Simple View Pager

Simple View Pager Allows to scroll through the container on the same layout by sliding the screen

##Features of Simple View Pager

  - Any ViewGroup based View can be use as Container
  - Swipe Right and Left to swtich pages
  - Programmatically switch pages
  - Continuous Pages
  - Auto Created PageDots
  - Customize style For Active PageDot and Deactive PageDosts
  - Customize PageDots location

##Usages

###Methods and Settings

```groovy
      mSimpleViewPager.setDotsSeparate(10 /*in px*/);
      mSimpleViewPager.setPageDotsAlignment(PageDotsAlignment.BOTTOM);
      mSimpleViewPager.setShowPageDots(true);
      mSimpleViewPager.setDotRadius(14   /*in px*/);
      mSimpleViewPager.setActiveDotRadius(18   /*in px*/);
      mSimpleViewPager.setDotColor( getResources().getColor( android.R.color.holo_purple));
      mSimpleViewPager.setActiveDotColor(getResources().getColor(android.R.color.holo_orange_dark));
 ```  
        
### Events
  
  ```groovy
       mSimpleViewPager.setOnPageChangingListener(new OnPageChangingListener() {
            @Override
            public boolean onPageChanging(int currentViewIndex, int newViewIndex) {
                // "true" means that the operation will be canceled
                return false;
            }
        });

        mSimpleViewPager.setOnPageChangedListener(new OnPageChangedListener() {
            @Override
            public void onPageChanged(int oldViewIndex, int currentViewIndex) {

            }
        });
 ```  
 
### Add a container programmatically
   ```groovy
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,      ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundColor(Color.YELLOW);
        mSimpleViewPager.addContainer(relativeLayout);
```  
         
### Programmatically Next and Previous
 ```groovy
        mImageButton_Previous = (ImageButton) findViewById(R.id.imageButton_Previous);
        mImageButton_Next = (ImageButton) findViewById(R.id.imageButton_Next);

        mImageButton_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleViewPager.next();
            }
        });

        mImageButton_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleViewPager.previous();
            }
        });
 ```
