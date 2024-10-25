package org.example;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.GlyphTextEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static StringBuilder stringBuilder = new StringBuilder();
    public static void main(String[] args) throws IOException {
        // 加載PDF檔案
        PdfDocument pdfDoc = new PdfDocument(new PdfReader("C:\\Users\\2400822\\Desktop\\77777.pdf"), new PdfWriter("C:\\Users\\2400822\\Desktop\\77776.pdf"));

        PdfPage page = pdfDoc.getPage(2);

        Rectangle mediaBox = page.getMediaBox();
        System.out.println(mediaBox);
        TextLocationStrategy textLocationStrategy = new TextLocationStrategy("公司名稱/人數規模 部門/職稱/工作內容 起訖年月(西元) 離職原因 離職月薪 年薪");
        PdfCanvasProcessor parser = new PdfCanvasProcessor(textLocationStrategy);
        parser.processPageContent(page);
        // (x, y, width, height)
        Rectangle cropBox = new Rectangle(textLocationStrategy.getReX(), textLocationStrategy.getReY(), textLocationStrategy.getXDifference(), textLocationStrategy.getYDifference());
        page.setCropBox(cropBox);
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy() {
            public void eventOccurred(IEventData data, EventType type) {
                if (data instanceof TextRenderInfo) {
                    TextRenderInfo textInfo = (TextRenderInfo) data;
                    Rectangle rect = textInfo.getBaseline().getBoundingRectangle();

                    // 判断文本是否在裁剪框内
                    if (cropBox.contains(rect)) {
                        // 文本在裁剪框内，处理该文本
                        //innerText +=textInfo.getText();
                        stringBuilder.append(textInfo.getText());
                        System.out.print(textInfo.getText());
                    } else {
                        // 文本在裁剪框外
                        //System.out.print(textInfo.getText());
                    }
                }
                super.eventOccurred(data, type);
            }
        };

        //textLocationStrategy.getResultantText();
        String text = PdfTextExtractor.getTextFromPage(page, strategy);
        //System.out.println(text);
        System.out.println("裁切后的页面大小: " + cropBox);
        parseTableFromText(stringBuilder.toString());
        // 提取每一頁的文本
//        for (int page = 1; page <= pdfDoc.getNumberOfPages(); page++) {
//            String text = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(page), new SimpleTextExtractionStrategy());
//            System.out.println("Page " + page + ": ");
//            System.out.println(text);
//            // 解析文本為表格
//            //parseTableFromText(text);
//        }
        // 關閉文件
        pdfDoc.close();
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
