package org.example.javaAPIService;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.texts.PdfTextExtractOptions;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExtractPDBByJavaAPI {

    public static void main(String[] args) throws IOException, TikaException {
        // Specify the path to the PDF file
        String pdfFilePath = "D:\\workspace\\temp\\77777.pdf";

        // 使用 Java API 解析 PDF 檔案內容
        //iText(pdfFilePath);
        //String extractedText = apachePDFBox(pdfFilePath);
        //String extractedText = spire(pdfFilePath);
        String extractedText = tika(pdfFilePath);

        // 印出資料
        displayData(extractedText);

    }

    /**
     * 1. 使用 iText API 解析 PDF 檔案內容
     */
    public static void iText(String filePath) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath));
        String text = "";
        // 提取每一頁的文本
        for (int page = 1; page <= pdfDoc.getNumberOfPages(); page++) {
            text = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(page), new SimpleTextExtractionStrategy());
            System.out.println("Page " + page + ": ");
            displayData(text);
        }
        // 關閉文件
        pdfDoc.close();
    }

    /**
     * 2. 使用 ApachePDFBox API 解析 PDF 檔案內容
     */
    public static String apachePDFBox(String filePath) throws IOException {
        String text = "";
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            // Create a PDFTextStripper instance for text extraction
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // Set page range if you need specific pages, e.g., pages 1 to 2
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(2);

            // Extract text from the PDF
            text = pdfStripper.getText(document);

            // Output the extracted text
            //System.out.println("Extracted Text: \n" + text);

        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        return text;
    }

    /**
     * 3. 使用 Spire API 解析 PDF 檔案內容
     */
    public static String spire(String filePath) throws IOException {
        // Create a PdfDocument object
        com.spire.pdf.PdfDocument doc = new com.spire.pdf.PdfDocument();

        // Load a PDF file
        doc.loadFromFile(filePath);

        // Create a StringBuilder to store all extracted text
        StringBuilder allText = new StringBuilder();

        // Loop through each page in the PDF
        for (int i = 0; i < doc.getPages().getCount(); i++) {
            // Get the current page
            PdfPageBase page = doc.getPages().get(i);

            // Create a PdfTextExtractor object for the current page
            com.spire.pdf.texts.PdfTextExtractor textExtractor = new com.spire.pdf.texts.PdfTextExtractor(page);

            // Create a PdfTextExtractOptions object
            PdfTextExtractOptions extractOptions = new PdfTextExtractOptions();

            // Extract text from the current page
            String text = textExtractor.extract(extractOptions);

            // Append the text to allText StringBuilder
            allText.append(text).append("\n");
        }
        return allText.toString();
    }

    /**
     * 4. 使用 tika API 解析 PDF 檔案內容
     */
    public static String tika(String filePath) throws IOException, TikaException {
        Tika tika = new Tika();
        File file = new File(filePath);

        try {
            // 提取文件中的文本
            String fileContent = tika.parseToString(file);

            // 按行分割文本
            String[] rows = fileContent.split("\n");

            for (String row : rows) {
                // 按空白字符分割每一行，将其拆分成列
                String[] columns = row.split("\\s+");

                // 显示提取的表格数据
                System.out.println("Columns: " + List.of(columns));
                System.out.println("------------------------");
            }
        } catch (IOException | TikaException e) {
            e.printStackTrace();
        }
        return tika.parseToString(file);
    }

    /**
     * 印出解析後 PDF 檔案內資料
     */
    public static void displayData(String text) {
        // 按行分割文本
        String[] rows = text.split("\n");

        for (String row : rows) {
            // 輸出每行的原始內容
            //System.out.println("Row: " + row);

            // 分割每行的列數據
            String[] columns = row.split("\\s+");  // 使用正則表達式按多個空格分割列數據

            // 顯示提取的表格數據
            System.out.println("Columns: " + List.of(columns));
            System.out.println("------------------------");
        }
    }
}
