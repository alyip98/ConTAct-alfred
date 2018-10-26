package seedu.address.model.email;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.index.Index;

/**
 * Represents an email message with a subject and a body in addressbook.
 */
public class EmailDraft {

    // Identity fields
    private final Index index;
    private final Subject subject;
    private final Body body;

    public EmailDraft(Index index, Subject subject, Body body) {
        requireAllNonNull(index, subject, body);
        this.index = index;
        this.subject = subject;
        this.body = body;
    }

    public Index getIndex() {
        return index;
    }

    public Body getBody() {
        return body;
    }

    public Subject getSubject() {
        return subject;
    }
}
