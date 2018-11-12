package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.commands.group.GroupEmailCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.email.Body;
import seedu.address.model.email.EmailDraft;
import seedu.address.model.email.Subject;

/**
 * Parses input arguments and creates a new EmailCommand object
 */

public class EmailCommandParser implements Parser<EmailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmailCommand
     * and returns an EmailCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT, PREFIX_BODY, PREFIX_GROUP);

        if (!arePrefixesPresent(argMultimap, PREFIX_SUBJECT, PREFIX_BODY)
                || (argMultimap.getPreamble().isEmpty() && !arePrefixesPresent(argMultimap, PREFIX_GROUP))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
        }

        Subject subject = ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get());
        Body body = ParserUtil.parseBody(argMultimap.getValue(PREFIX_BODY).get());

        if (arePrefixesPresent(argMultimap, PREFIX_GROUP)) {
            String groupName = argMultimap.getValue(PREFIX_GROUP).get();
            return new GroupEmailCommand(groupName, subject.toString(), body.toString());
        }
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        EmailDraft emailDraft = new EmailDraft(index, subject, body);

        return new EmailCommand(emailDraft);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}
     */
    private boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
