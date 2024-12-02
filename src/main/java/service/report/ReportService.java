package service.report;

import model.User;


public interface ReportService {

    boolean generateEmployeeSalesReport(User user, String filePath);

    boolean generateAllEmployeesSalesReport(String filePath);
}
