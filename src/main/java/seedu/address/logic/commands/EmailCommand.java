package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.OutlookRequest;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.email.EmailDraft;
import seedu.address.model.student.Email;
import seedu.address.model.student.Student;

/**
 * Extracts email address from student identified using it's displayed index from the list of students.
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends email to student specified. "
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + PREFIX_SUBJECT + "SUBJECT "
            + PREFIX_BODY + "BODY "
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_SUBJECT + "Tutorial time changed "
            + PREFIX_BODY + "Tutorial this week is on tuesday. ";

    public static final String MESSAGE_SENT_SUCCESS = "Email sent to: %1$s";
    public static final String MESSAGE_SENT_FAILURE = "Email not delivered, received response: %s";

    protected final EmailDraft emailDraft;

    /**
     * Creates an EmailCommand to send the specified {@code EmailDraft}
     */
    public EmailCommand(EmailDraft emailDraft) {
        this.emailDraft = emailDraft;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();
        Index targetIndex = emailDraft.getIndex();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEmail = lastShownList.get(targetIndex.getZeroBased());
        Email addressToEmail = studentToEmail.getEmail();
        List<String> emailAdd = List.of(addressToEmail.toString());
        String subject = emailDraft.getSubject().toString();

        String body = emailDraft.getBody().toString();
        OutlookRequest outlookRequest = new OutlookRequest(emailAdd, subject, body);
        try {
            OutlookRequest.sendMail(outlookRequest);
        } catch (Exception e) {
            return new CommandResult(String.format(MESSAGE_SENT_FAILURE, e.getMessage()));
        }

        return new CommandResult(String.format(MESSAGE_SENT_SUCCESS, addressToEmail));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && emailDraft.equals(((EmailCommand) other).emailDraft)); // state check
    }

}
