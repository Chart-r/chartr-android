package com.example.mac.chartr;

/*
MIT License
Copyright (c) 2016 Diego Yasuhiko Kurisaki
https://medium.com/@diegoy_kuri/masks-in-android-edit-text-fields-33a2fd47f1af
*/

/* Example:
  mEmailView.addTextChangedListener(new MaskWatcher("###-##"));
*/

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Used to monitor input fields to ensure that they are formatted in the proper way
 *
 * Specifically used to insert the proper formatting for phone numbers and birthdays
 */
public class MaskWatcher implements TextWatcher {
    private boolean isRunning = false;
    private boolean isDeleting = false;
    private final String mask;

    public MaskWatcher(String mask) {
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        isDeleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isRunning || isDeleting) {
            return;
        }
        isRunning = true;

        int editableLength = editable.length();
        if (editableLength < mask.length()) {
            if (mask.charAt(editableLength) != '#') {
                editable.append(mask.charAt(editableLength));
            } else if (mask.charAt(editableLength - 1) != '#') {
                editable.insert(editableLength - 1, mask,
                        editableLength - 1, editableLength);
            }
        }

        isRunning = false;
    }

    boolean isRunning() {
        return isRunning;
    }

    boolean isDeleting() {
        return isDeleting;
    }
}
