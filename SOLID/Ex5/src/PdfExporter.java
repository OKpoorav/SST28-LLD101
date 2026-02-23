import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {
    private static final int MAX_LENGTH = 20;
    
    @Override
    public ExportResult export(ExportRequest req) {
        String body = sanitize(req.body);
        if (body.length() > MAX_LENGTH) {
            return ExportResult.error("PDF cannot handle content > 20 chars");
        }
        String fakePdf = "PDF(" + sanitize(req.title) + "):" + body;
        return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
    }
}
