public class EmailSender extends NotificationSender {
    private static final int MAX_BODY_LENGTH = 40;
    
    public EmailSender(AuditLog audit) { 
        super(audit); 
    }

    @Override
    public SendResult send(Notification n) {
        String email = sanitize(n.email);
        if (email.isEmpty() || !email.contains("@")) {
            audit.add("email failed");
            return SendResult.failure("invalid email address");
        }
        
        String body = sanitize(n.body);
        if (body.length() > MAX_BODY_LENGTH) {
            body = body.substring(0, MAX_BODY_LENGTH);
        }
        
        System.out.println("EMAIL -> to=" + email + " subject=" + sanitize(n.subject) + " body=" + body);
        audit.add("email sent");
        return SendResult.success();
    }
}
