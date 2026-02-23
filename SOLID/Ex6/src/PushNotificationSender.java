public class PushNotificationSender extends NotificationSender {
    public PushNotificationSender(AuditLog audit) {
        super(audit);
    }
    
    @Override
    public SendResult send(Notification n) {
        String body = sanitize(n.body);
        String subject = sanitize(n.subject);
        
        if (body.isEmpty() && subject.isEmpty()) {
            audit.add("push failed");
            return SendResult.failure("message cannot be empty");
        }
        
        String message = subject.isEmpty() ? body : subject + ": " + body;
        System.out.println("PUSH -> message=" + message);
        audit.add("push sent");
        return SendResult.success();
    }
}
