package seedu.address.logic.commands.group;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.OutlookRequest;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.email.Body;
import seedu.address.model.email.EmailDraft;
import seedu.address.model.email.Subject;
import seedu.address.model.student.Email;
import seedu.address.model.student.Student;

public class GroupEmailCommand extends EmailCommand {

    private String groupName;
    private String subject;
    private String body;

    public GroupEmailCommand(String groupName, String subject, String body) {
        super(new EmailDraft(Index.fromZeroBased(0), new Subject(subject), new Body(body)));
        this.groupName = groupName;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<String> recipients = model.getGroup(groupName).getSet()
                .stream().map(Student::getEmail).map(Email::toString).collect(Collectors.toList());
        try {
            OutlookRequest.sendMail(
                    new OutlookRequest(recipients, subject, body));
        } catch (Exception e) {
            return new CommandResult(String.format(MESSAGE_SENT_FAILURE, e.getMessage()));
        }
        return new CommandResult(String.format(MESSAGE_SENT_SUCCESS, String.join(", ", recipients)));
    }
}
