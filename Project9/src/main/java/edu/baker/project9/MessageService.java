package edu.baker.project9;

/**
 * Port for sending one message to one email address. Code that needs to send
 * messages depends on this interface instead of on a specific email provider,
 * so a test can inject a simple implementation in place of a real mail service.
 */
public interface MessageService {

    /**
     * Sends one message to one email address.
     *
     * @param subject   the subject line of the message
     * @param msg       the body text of the message
     * @param emailAddr the email address to send the message to
     */
    void sendMessage(String subject, String msg, String emailAddr);
}