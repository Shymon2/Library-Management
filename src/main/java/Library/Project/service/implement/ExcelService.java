package Library.Project.service.implement;

import Library.Project.entity.Book;
import Library.Project.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final BookRepository bookRepository;

    public void exportToExcel(OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Books");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            Cell idCell = headerRow.createCell(0);
            idCell.setCellValue("ID");
            idCell.setCellStyle(headerStyle);

            Cell titleCell = headerRow.createCell(1);
            titleCell.setCellValue("Title");
            titleCell.setCellStyle(headerStyle);

            Cell authorCell = headerRow.createCell(2);
            authorCell.setCellValue("Author");
            authorCell.setCellStyle(headerStyle);

            // Lấy dữ liệu books từ repository
            List<Book> books = bookRepository.findAll();

            int rowNum = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getTitle());
                row.createCell(2).setCellValue(book.getAuthor());
            }

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
    }
}
