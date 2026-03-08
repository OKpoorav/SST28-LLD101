package com.example.reports;

/**
 * TODO (student):
 * Implement Proxy responsibilities here:
 * - access check
 * - lazy loading
 * - caching of RealReport within the same proxy
 */
public class ReportProxy implements Report {
    private RealReport realReport;   

    private final String reportId;
    private final String title;
    private final String classification;
    private final AccessControl accessControl = new AccessControl();

    public ReportProxy(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    @Override
    public void display(User user) {
        // Starter placeholder: intentionally incorrect.
        // Students should remove direct real loading on every call.
        boolean access = accessControl.canAccess(user,classification);
        if(!access){
            System.out.println("Access Denied");
            return;
        }
        if(realReport == null){
        realReport = new RealReport(reportId, title, classification);
        }
        realReport.display(user);
    }
}
