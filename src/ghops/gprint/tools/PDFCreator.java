package ghops.gprint.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

public class PDFCreator {

    private float xStart = 10;
    private float yStart = 590;

    private float xPosition = xStart;
    private float yPosition = yStart;

    private float rowHeight = 20;

    private int fontSize = 8;

    private PDType0Font font;
    private PDType0Font fontBold;
    private PDDocument doc;

    private boolean showHeader = false;
    private boolean showAllHeader = false;
    private String header = "";

    private TreeMap<String, String> headers;
    private List<TreeMap<String, String>> data;
    private TreeMap<String, Float> size;

    private int currentRow = 0;

    private String fileName = "report";

    public PDFCreator(TreeMap<String, String> headers, TreeMap<String, Float> size, List<TreeMap<String, String>> data) {
        this.headers = headers;
        this.size = size;
        this.data = data;
    }

    public void showHeader(boolean header) {
        this.showHeader = header;
    }

    public void write(boolean showHeader) {
        this.showHeader = showHeader;
        this.write();
    }

    public void write(boolean showHeader, boolean allPage) {
        this.showHeader = showHeader;
        this.showAllHeader = allPage;
        this.write();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void write() {
        this.currentRow = 0;
        try {

            this.doc = new PDDocument();

            this.font = PDType0Font.load(doc, new File("JetBrainsMono-Regular.ttf"));
            this.fontBold = PDType0Font.load(doc, new File("JetBrainsMono-Bold.ttf"));

            PDPage page = new PDPage(PDRectangle.A4);
            page.setRotation(90);
            doc.addPage(page);

            this.yStart = page.getMediaBox().getWidth() - 20;
            this.yPosition = this.yStart;

            if (this.showHeader) {
                this.writeHeader(page);
            }

            while (currentRow < this.data.size()) {
                if (this.yPosition < 10) {
                    this.yPosition = this.yStart;
                    page = new PDPage(PDRectangle.A4);
                    page.setRotation(90);
                    doc.addPage(page);
                    if (this.showAllHeader) {
                        this.writeHeader(page);
                    }
                }
                this.writeRow(page, currentRow);
                currentRow++;
            }

            doc.save(this.fileName + ".pdf");
            doc.close();
        } catch (IOException ex) {
            Logger.getLogger(ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeHeader(PDPage page) {
        try (PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true)) {
            cs.transform(new Matrix(0, 1, -1, 0, page.getMediaBox().getWidth(), 0));
            this.xPosition = this.xStart;

            int colIndex = 1;

            for (String key : this.headers.keySet()) {
                cs.setFont(this.fontBold, this.fontSize);
                cs.beginText();
                cs.newLineAtOffset(xPosition, yPosition);
                cs.showText(this.headers.get(key));
                cs.endText();
                //float percent = 20; //(float) (tc.getWidth() / (this.table.getWidth()));
                xPosition += this.size.get("column" + colIndex);
                //System.out.println(page.getMediaBox().getHeight() * percent);

                // xPosition += page.getMediaBox().getHeight() * percent;
                colIndex++;
            }
            cs.close();
            this.yPosition -= this.rowHeight;
        } catch (IOException ex) {
            Logger.getLogger(ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeRow(PDPage page, int rowIndex) {

        try (PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true)) {
            cs.transform(new Matrix(0, 1, -1, 0, page.getMediaBox().getWidth(), 0));
            this.xPosition = this.xStart;

            int colIndex = 1;
            TreeMap<String, String> tm = this.data.get(rowIndex);
            for (String key : tm.keySet()) {
                cs.setFont(this.font, this.fontSize);
                cs.beginText();
                cs.newLineAtOffset(xPosition, yPosition);
                cs.showText(tm.get(key));
                cs.endText();
                //float percent = 20; //(float) (tc.getWidth() / (this.table.getWidth()));
                xPosition += this.size.get("column" + colIndex);
                //System.out.println(page.getMediaBox().getHeight() * percent);

                // xPosition += page.getMediaBox().getHeight() * percent;
                colIndex++;
            }

            cs.close();
            this.yPosition -= this.rowHeight;
        } catch (IOException ex) {
            Logger.getLogger(ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
