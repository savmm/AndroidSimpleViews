package com.customview.ui.radiobuttongroup;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by mugurelli on 24.02.2017.
 */

public class SimpleRadioButton extends View {


    private int mImageWidth = 20,mImageHight=20;
    public SimpleRadioButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle();
    }
}
