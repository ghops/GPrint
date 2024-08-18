package ghops.gprint.tools;

import ghops.gprint.models.ReportColumn;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

public class TableGenerator {

    private static PDType0Font font;
    private static int pageRows = 20;
    private static int fontSize = 9;

    public static void generateTable(List<List<ReportColumn>> data, List<ReportColumn> columns, String outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            float xPosition = 10;
            float yPosition = 600;
            float xStart = 10;
            float rowHeight = 20;

            int totalRows = data.size();

            int totalPages = (int) Math.ceil((double) totalRows / pageRows);

            int currentPage = 0;
            int currentRow = 0;

            int current = 0;

            while (currentPage < totalPages) {
                
                PDPage page = new PDPage();
                page.setRotation(90);

                document.addPage(page);

                yPosition = page.getMediaBox().getWidth() - rowHeight;

                pageRows = (int) (page.getMediaBox().getWidth() / rowHeight) -1;
                
                totalPages = (int) Math.ceil((double) totalRows / pageRows);

                font = PDType0Font.load(document, new File("Exo2-Regular.ttf"));

                current = currentPage * pageRows;
                System.out.println(current);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.transform(new Matrix(0, 1, -1, 0, page.getMediaBox().getWidth(), 0));

                    for (ReportColumn rc : columns) {
                        contentStream.setFont(font, fontSize);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition, yPosition);
                        contentStream.showText(rc.getText());
                        contentStream.endText();
                        xPosition += rc.getWidth();
                    }
                    xPosition = xStart;
                    yPosition -= rowHeight;
                     
                     
                    while (current < current + pageRows) {
                        if (current >= totalRows) {
                            break;
                        }

                        List<ReportColumn> row = data.get(current);

                        for (ReportColumn rc : row) {
                                                       
                            contentStream.setFont(font, fontSize); 
                            contentStream.beginText();
                            contentStream.newLineAtOffset(xPosition, yPosition);
                            contentStream.showText(rc.getText());
                            contentStream.endText(); 
                            xPosition += rc.getWidth();
                        }
                        yPosition -= rowHeight;

                        xPosition = xStart;
                        current++;
                    }

                    /*
                    for (int i = 0; i < pageRows; i++) {

                        List<ReportColumn> row = data.get(currentRow);

                        for (ReportColumn rc : row) {
                            contentStream.setFont(font, fontSize);
                            contentStream.beginText();
                            contentStream.newLineAtOffset(xPosition, yPosition);
                            contentStream.showText(rc.getText());
                            contentStream.endText();
                            xPosition += rc.getWidth();
                        }

                        yPosition -= rowHeight;

                        xPosition = xStart;
                        currentRow++;
                    }
                    */
                    yPosition = page.getMediaBox().getWidth() - rowHeight;
                    xPosition = xStart;
                    currentPage++;
                }
            }

            document.save(outputFile);
        }
    }

    public static void generateTableX(List<List<ReportColumn>> data, List<String> columns, int pageSize, String outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            int totalRows = data.size();
            int totalPages = (int) Math.ceil((double) totalRows / pageSize);

            int currentPage = 0;
            int currentRow = 0;

            while (currentPage < totalPages) {
                PDPage page = new PDPage();
                page.setRotation(90);

                document.addPage(page);

                PDType0Font font = PDType0Font.load(document, new File("Exo2-Regular.ttf"));

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.transform(new Matrix(0, 1, -1, 0, page.getMediaBox().getWidth(), 0));
                    contentStream.setFont(font, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(10, 600);
                    contentStream.showText("Page " + (currentPage + 1));
                    contentStream.endText();

                    int rowsOnPage = Math.min(pageSize, totalRows - currentRow);
                    int rowsToPrint = Math.min(rowsOnPage, data.size() - currentRow);

                    // Draw table headers
                    contentStream.setFont(font, 10);
                    contentStream.setLeading(14.5f);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 650);
                    for (String column : columns) {
                        contentStream.showText(column);
                        contentStream.newLineAtOffset(200, 0);
                    }
                    contentStream.endText();

                    // Draw table rows
                    contentStream.setFont(font, 10);
                    contentStream.setLeading(14.5f);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 630);
                    for (int i = 0; i < rowsToPrint; i++) {
                        List<ReportColumn> rowData = data.get(currentRow + i);
                        for (ReportColumn cellData : rowData) {
                            contentStream.showText(cellData.getText());
                            contentStream.newLineAtOffset(100, 0);
                        }
                        contentStream.newLineAtOffset(-columns.size() * 100, -14.5f);
                    }
                    contentStream.endText();

                    currentRow += rowsToPrint;
                    currentPage++;
                }
            }

            document.save(outputFile);
        }
    }
}
