public class WhatsAppSender extends NotificationSender {
    public WhatsAppSender(AuditLog audit) { 
        super(audit); 
    }

    @Override
    public SendResult send(Notification n) {
        String phone = sanitize(n.phone);
        if (phone.isEmpty() || !phone.startsWith("+")) {
            audit.add("WA failed");
            return SendResult.failure("phone must start with + and country code");
        }
        
        System.out.println("WA -> to=" + phone + " body=" + sanitize(n.body));
        audit.add("wa sent");
        return SendResult.success();
    }
}
