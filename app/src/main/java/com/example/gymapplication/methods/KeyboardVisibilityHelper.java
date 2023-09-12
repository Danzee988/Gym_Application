package com.example.gymapplication.methods;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardVisibilityHelper {
    private View rootView;
    private OnKeyboardVisibilityListener onKeyboardVisibilityListener;

    public KeyboardVisibilityHelper(Activity activity, View rootView) {
        this.rootView = rootView;
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect r = new Rect();
            private boolean wasOpened = false;

            @Override
            public void onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(r);

                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;

                // If the keypadHeight is less than a threshold, consider the keyboard as closed
                boolean isOpen = keypadHeight > screenHeight * 0.15;

                if (isOpen == wasOpened) {
                    // Keyboard state has not changed, do nothing
                    return;
                }

                wasOpened = isOpen;

                if (isOpen) {
                    // Keyboard is open, you can choose to hide views here
                    if (onKeyboardVisibilityListener != null) {
                        onKeyboardVisibilityListener.onKeyboardVisibilityChanged(true);
                    }
                } else {
                    // Keyboard is closed, show views again
                    if (onKeyboardVisibilityListener != null) {
                        onKeyboardVisibilityListener.onKeyboardVisibilityChanged(false);
                    }
                }
            }
        });
    }

    public void setOnKeyboardVisibilityListener(OnKeyboardVisibilityListener listener) {
        this.onKeyboardVisibilityListener = listener;
    }

    public interface OnKeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean isVisible);
    }
}
