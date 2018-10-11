package tk.ouyangv5.wecatbalance;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import de.robv.android.xposed.XposedBridge;
/*
    这个类用于监听TextView的值变化,当监听到包含¥的字符时候，将其设置成自己的值
 */
public class TextViewWatcher implements TextWatcher {
    public static final String LAST_TEXT="¥9999999.99";
    private TextView textView;

    public TextViewWatcher(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        XposedBridge.log(text);
        if (text.contains("¥")) {
            textView.removeTextChangedListener(this);
            textView.setText(LAST_TEXT);
            textView.addTextChangedListener(this);
        }
    }
}
