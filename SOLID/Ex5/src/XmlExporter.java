import java.nio.charset.StandardCharsets;

public class XmlExporter extends Exporter {
    @Override
    public ExportResult export(ExportRequest req) {
        String title = escapeXml(sanitize(req.title));
        String body = escapeXml(sanitize(req.body));
        String xml = "<?xml version=\"1.0\"?><export><title>" + title + "</title><body>" + body + "</body></export>";
        return new ExportResult("application/xml", xml.getBytes(StandardCharsets.UTF_8));
    }
    
    private String escapeXml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
