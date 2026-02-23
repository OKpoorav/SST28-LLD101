public class ExportResult {
    public final String contentType;
    public final byte[] bytes;
    public final boolean success;
    public final String errorMessage;

    public ExportResult(String contentType, byte[] bytes) {
        this.contentType = contentType;
        this.bytes = bytes;
        this.success = true;
        this.errorMessage = null;
    }
    
    public static ExportResult error(String message) {
        ExportResult result = new ExportResult("", new byte[0]);
        return new ExportResult(message);
    }
    
    private ExportResult(String errorMessage) {
        this.contentType = "";
        this.bytes = new byte[0];
        this.success = false;
        this.errorMessage = errorMessage;
    }
}
