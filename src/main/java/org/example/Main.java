package org.example;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.example.model.RectanglePoints;

import java.io.*;
import java.util.List;


public class Main {
    //private static StringBuilder stringBuilder = new StringBuilder();
    public static void main(String[] args) throws IOException {
        // 加載PDF檔案
        long currentTimeMillis = System.currentTimeMillis();
        String path="C:\\workspace\\pdf\\";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(path+"77777.pdf"), new PdfWriter(path+"77776.pdf"));

        PdfPage page = pdfDoc.getPage(1);

        Rectangle mediaBox = page.getMediaBox();
        System.out.println(mediaBox);
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy  = new TextAreaRetrieveStrategy("公司名稱/人數規模","年薪","是否曾任職於IBM");
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("語文 聽","撰寫","【教育程度】(請填寫高中");
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("稱謂 ","職業 ","【教育程度】(請填寫高中");
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("教育程度 學校名稱 ","","【電腦技能】較專精之電腦軟硬體、應用系統及程式語言");
        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("電腦軟硬體、應用系統、程式語言 瞭解程","","【專業資格考試或認證】");
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("專業證照、政府/考試名","","【任用諮詢】請");
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("姓名 公司 職稱","","請翻面，背面尚有資料待填");
//        TextAreaRetrieveStrategy textAreaRetrieveStrategy = new TextAreaRetrieveStrategy("是否曾任職於IBM_台灣國際商業機器(股)公司：","","【信用資料】");
        PdfCanvasProcessor parser = new PdfCanvasProcessor(textAreaRetrieveStrategy);
        parser.processPageContent(page);
        RectanglePoints rectanglePoints = textAreaRetrieveStrategy.getRectanglePointsInfo();

        // (x, y, width, height)
        Rectangle cropBox = new Rectangle(rectanglePoints.getBottomLeftX(), rectanglePoints.getBottomLineY(), rectanglePoints.getWidth(), rectanglePoints.getHeight());
        //因此會把 PDF 內不在此範圍內的內容視為「被裁剪掉」，從而實現分割 PDF 的效果。
        page.setCropBox(cropBox);


        CropboxContentStrategy strategy = new CropboxContentStrategy(cropBox);
        //textLocationStrategy.getResultantText();
        String text = PdfTextExtractor.getTextFromPage(page, strategy);
        //System.out.println(text);
        System.out.println("\n裁切後的頁面大小: " + cropBox);
        //parseTableFromText(stringBuilder.toString());
        parseTableFromText(strategy.getStringBuilder().toString());

        // 關閉文件
        pdfDoc.close();
        long endTime = System.currentTimeMillis();
        System.out.println("start: "+currentTimeMillis);
        System.out.println("end: "+endTime);
        System.out.println("total: "+(endTime-currentTimeMillis));
    }

    // 解析表格數據的方法
    private static void parseTableFromText(String text) {
        // 這裡假設表格的每一行數據由空格或制表符分隔
        String[] rows = text.split("\n");  // 按行分割文本

        for (String row : rows) {
            // 根據空格或制表符來分割每一行中的列數據
            String[] columns = row.split("\\s+");  // 使用正則表達式按多個空格分割列數據

            // 顯示提取的表格數據
//            for (String column : columns) {
//                System.out.print(column + "\t");
//            }
            System.out.println(List.of(columns));

            System.out.println();
        }
    }
}
