package Library.Project.controller;

import Library.Project.service.interfaces.IExcelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
@Tag(name = "Excel Controller")
public class ExcelController {
    private final IExcelService excelService;

    @GetMapping("/export-book")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=books.xlsx");
        try {
            excelService.exportToExcel(response.getOutputStream());
        } catch (Exception e) {
            try {
                response.reset();
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            } catch (IOException ex) {
                throw new IOException();
            }
        }
    }

}
