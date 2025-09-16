package Library.Project.service.interfaces;

import java.io.IOException;
import java.io.OutputStream;

public interface IExcelService {
    void exportToExcel(OutputStream outputStream) throws IOException;
}
