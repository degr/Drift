package org.forweb.drift.tests.impact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.List;

public class ContactListener implements org.jbox2d.callbacks.ContactListener {

    private final List<Impact> contacts;

    public ContactListener(List<Impact> contacts) {
        this.contacts = contacts;
    }


    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        contacts.add(new Impact(contact, impulse));
    }
}
