public abstract class NotificationSender {
    protected final AuditLog audit;
    
    protected NotificationSender(AuditLog audit) { 
        this.audit = audit; 
    }
    
    public abstract SendResult send(Notification n);
    
    protected String sanitize(String s) {
        return s == null ? "" : s;
    }
}
