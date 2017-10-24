package flashbox.tracck.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class CustomNumPicker extends NumberPicker {
    public CustomNumPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        if(child instanceof EditText) {
            ((EditText) child).setTextSize(1);
        }
    }
}
