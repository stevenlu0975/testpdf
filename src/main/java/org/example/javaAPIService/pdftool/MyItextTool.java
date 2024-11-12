package org.example.javaAPIService.pdftool;

import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.parser.IContentOperator;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;

import java.util.HashMap;

public class MyItextTool {
    /**
     * 得到指定範圍pdf
    * @param page 哪一頁
    * */
    public static String getSpecialContent(PdfPage page, ITextExtractionStrategy strategy){
        // todo  這個map 幹嘛用的
        HashMap<String, IContentOperator> additionalContentOperators = new HashMap<>();
        PdfCanvasProcessor parser = new PdfCanvasProcessor(strategy, additionalContentOperators);
        parser.processPageContent(page);
        return strategy.getResultantText();
    }

    // 解析表格數據的方法
    public static void parseTableFromText(String text) {
        // 這裡假設表格的每一行數據由空格或制表符分隔
        String[] rows = text.split("\n");  // 按行分割文本

        for (String row : rows) {
            // 根據空格或制表符來分割每一行中的列數據
            String[] columns = row.split("\s+");  // 使用正則表達式按多個空格分割列數據

            // 顯示提取的表格數據
            for (String column : columns) {
                System.out.print(column + "\t");
            }
            System.out.println();
        }
    }
}
