public abstract class Exporter {
    public abstract ExportResult export(ExportRequest req);
    
    protected String sanitize(String s) {
        return s == null ? "" : s;
    }
}
