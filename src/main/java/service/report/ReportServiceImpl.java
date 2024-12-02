package service.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.Constants;
import model.Book;
import model.User;
import repository.order.OrderRepository;
import repository.user.UserRepository;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ReportServiceImpl implements ReportService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ReportServiceImpl(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean generateEmployeeSalesReport(User user, String filePath) {
        Map<Book, Integer> booksSoldByEmployee = orderRepository.getBooksSoldByEmployee(user);

        Document document = new Document();
        try{
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(createDocumentTitle("Books sold by Employee " + user.getUsername()));
            document.add(createEmployeeSalesTableHeader(user));
            document.add(createEmployeeSalesTable(user, booksSoldByEmployee));
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            document.close();
        }

        return true;
    }

    @Override
    public boolean generateAllEmployeesSalesReport(String filePath) {
        List<User> users = userRepository.findAll();

        Document document = new Document();
        try{
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(createDocumentTitle("Books sold by Employees"));
            for(User user : users){
                if(user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE))) {
                    Map<Book, Integer> booksSoldByEmployee = orderRepository.getBooksSoldByEmployee(user);
                    document.add(createEmployeeSalesTableHeader(user));
                    document.add(createEmployeeSalesTable(user, booksSoldByEmployee));
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            document.close();
        }
        return true;
    }

    private Paragraph createDocumentTitle(String titleString){
        Font titleFont = new Font(Font.FontFamily.COURIER, 18, Font.BOLD);
        Paragraph title = new Paragraph(titleString);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);

        return title;
    }

    private Paragraph createEmployeeSalesTableHeader(User user){
        Font sectionFont = new Font(Font.FontFamily.COURIER, 14, Font.BOLD);
        Paragraph employeeHeader = new Paragraph("Employee: " + user.getUsername() + "(ID: " + user.getId() + ")", sectionFont);
        employeeHeader.setSpacingBefore(10);
        employeeHeader.setSpacingAfter(5);

        return employeeHeader;
    }

    private PdfPTable createEmployeeSalesTable(User user, Map<Book, Integer> booksSold) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(10);
        addTableHeader(table, "Title", "Author", "Published Date", "Price", "Stock", "Quantity sold");

        for(Map.Entry<Book, Integer> bookEntry : booksSold.entrySet()){
            Book book = bookEntry.getKey();
            Integer quantity = bookEntry.getValue();
            table.addCell(book.getTitle());
            table.addCell(book.getAuthor());
            table.addCell(book.getPublishedDate().toString());
            table.addCell(book.getPrice().toString());
            table.addCell(book.getStock().toString());
            table.addCell(quantity.toString());
        }

        return table;
    }

    private void addTableHeader(PdfPTable table, String... headers){
        Font headerFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

        for(String header : headers){
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
}
