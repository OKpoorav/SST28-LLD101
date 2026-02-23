public class SmsSender extends NotificationSender {
    public SmsSender(AuditLog audit) { 
        super(audit); 
    }

    @Override
    public SendResult send(Notification n) {
        String phone = sanitize(n.phone);
        if (phone.isEmpty()) {
            audit.add("sms failed");
            return SendResult.failure("phone number required");
        }
        
        System.out.println("SMS -> to=" + phone + " body=" + sanitize(n.body));
        audit.add("sms sent");
        return SendResult.success();
    }
}
