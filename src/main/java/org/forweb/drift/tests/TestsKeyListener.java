package org.forweb.drift.tests;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static org.forweb.drift.tests.TestsKeyListener.KEY_TYPED.PRESSED;
import static org.forweb.drift.tests.TestsKeyListener.KEY_TYPED.RELEASED;
import static org.forweb.drift.tests.TestsKeyListener.KEY_TYPED.TYPED;

public class TestsKeyListener implements KeyListener{

    public enum KEY_TYPED {
        PRESSED, RELEASED, TYPED
    }

    List<Listener> keyListners = new ArrayList<>();

    public void addListener(Listener listner) {
        keyListners.add(listner);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        trigger(keyEvent.getKeyCode(), TYPED);
    }

    private void trigger(int keyCode, KEY_TYPED type) {
        for (Listener listener : keyListners) {
            listener.listen(keyCode, type);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        trigger(keyEvent.getKeyCode(), PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        trigger(keyEvent.getKeyCode(), RELEASED);
    }
}
