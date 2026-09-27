package edu.baker.project9;

/**
 * Test implementation of MessageService that "sends" a message by printing one
 * line to standard output in the form "address: subject". It lets sendSpam be
 * tested without a real email provider.
 */
public class ConsoleMessageService implements MessageService {

    /**
     * Prints one line for the message in the form "address: subject".
     * The message body is not printed.
     *
     * @param subject   the subject line of the message
     * @param msg       the body text of the message (not printed)
     * @param emailAddr the email address the message is sent to
     */
    @Override
    public void sendMessage(String subject, String msg, String emailAddr) {
        System.out.println(emailAddr + ": " + subject);
    }
}