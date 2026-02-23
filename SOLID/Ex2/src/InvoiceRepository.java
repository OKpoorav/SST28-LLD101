
public interface InvoiceRepository {
    void save(String invoiceId, String content);
    int countLines(String invoiceId);
}
