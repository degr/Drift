package org.forweb.drift.tests.impact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.dynamics.contacts.Contact;

public class Impact {
    private final Contact contact;

    private final ContactImpulse impulse;

    public Impact(Contact contact, ContactImpulse impulse) {
        this.contact = contact;
        this.impulse = impulse;
    }
    public Contact getContact() {
        return contact;
    }

    public ContactImpulse getImpulse() {
        return impulse;
    }
}
