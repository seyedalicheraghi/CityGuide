package org.smartcityguide.cityguide;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UserTextEditorSearch implements TextView.OnEditorActionListener {

    EditText targetEditText;
    public RequestResponse delegate = null;

    public UserTextEditorSearch(EditText targetEditText){
        this.targetEditText = targetEditText;
        onStart();
    }

    public void onStart(){
        targetEditText.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            }
            delegate.userSelection(textView.getText().toString().trim());
            return true;
        }
        return false;
    }

}
