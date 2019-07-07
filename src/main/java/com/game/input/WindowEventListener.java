package com.game.input;

import com.game.input.events.KeyEvent;
import com.game.input.events.MouseEvent;
import com.game.input.events.ScrollEvent;
import com.game.input.events.WindowResizeEvent;

public interface WindowEventListener {
    public default void processKeyEvent(KeyEvent keyEvent) {
    }

    public default void processMouseEvent(MouseEvent mouseEvent) {
    }

    public default void processScrollEvent(ScrollEvent scrollEvent) {
    }

    public default void processWindowResizeEvent(WindowResizeEvent windowResizeEvent) {
    }
}
