package org.example.java_api_service;

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
        // 指定 PDF 文件的路徑
        String pdfFilePath = "D:\\workspace\\temp\\77777.pdf";

        // 使用其中一個方法解析 PDF 文件內容
        // iText(pdfFilePath);
        // String extractedText = apachePDFBox(pdfFilePath);
        // String extractedText = spire(pdfFilePath);
        String extractedText = tika(pdfFilePath);

        // 顯示解析後的文本
        displayData(extractedText);
    }

    /**
     * 1. 使用 iText API 解析 PDF 檔案內容
     */
    public static void iText(String filePath) throws IOException {
        // 建立 PdfDocument 對象以讀取 PDF 文件
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath))) {
            for (int page = 1; page <= pdfDoc.getNumberOfPages(); page++) {
                // 提取指定頁面的文本
                String text = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(page), new SimpleTextExtractionStrategy());
                System.out.println("Page " + page + ": ");
                displayData(text);
            }
        }
    }

    /**
     * 2. 使用 ApachePDFBox API 解析 PDF 檔案內容
     */
    public static String apachePDFBox(String filePath) throws IOException {
        String text;
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            // 創建 PDFTextStripper 用來提取文字
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // 設定起始和結束頁面，這裡為頁碼 1 到 2
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(2);

            // 提取文件中的文字
            text = pdfStripper.getText(document);
        }
        return text;
    }

    /**
     * 3. 使用 Spire API 解析 PDF 檔案內容
     */
    public static String spire(String filePath) {
        // 建立 Spire 的 PdfDocument 物件並載入 PDF 文件
        com.spire.pdf.PdfDocument doc = new com.spire.pdf.PdfDocument();
        doc.loadFromFile(filePath);

        StringBuilder allText = new StringBuilder();
        for (int i = 0; i < doc.getPages().getCount(); i++) {
            // 提取每頁的文字
            PdfPageBase page = doc.getPages().get(i);
            com.spire.pdf.texts.PdfTextExtractor textExtractor = new com.spire.pdf.texts.PdfTextExtractor(page);
            PdfTextExtractOptions extractOptions = new PdfTextExtractOptions();

            // 將每頁的文字附加到 StringBuilder
            allText.append(textExtractor.extract(extractOptions)).append("\n");
        }
        return allText.toString();
    }

    /**
     * 4. 使用 Tika API 解析 PDF 檔案內容
     */
    public static String tika(String filePath) throws IOException, TikaException {
        // 創建 Tika 物件用於解析文件
        Tika tika = new Tika();
        return tika.parseToString(new File(filePath));
    }

    /**
     * 顯示解析後的 PDF 資料
     */
    public static void displayData(String text) {
        // 按行分割文本
        String[] rows = text.split("\n");

        // 逐行顯示內容
        for (String row : rows) {
            // 使用正則表達式分割每行的數據
            String[] columns = row.split("\\s+");
            System.out.println("Columns: " + List.of(columns));
            System.out.println("------------------------");
        }
    }
}
