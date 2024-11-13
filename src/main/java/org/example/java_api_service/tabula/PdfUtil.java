package org.example.java_api_service.tabula;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PdfUtil {


    private static final SpreadsheetExtractionAlgorithm SPREADSHEEET_EXTRACTION_ALGORITHM = new SpreadsheetExtractionAlgorithm();
    private static final ThreadLocal<List<String>> THREAD_LOCAL = new ThreadLocal<>();


    /**
     * @param {*} String pdf 路径
     * @param {*} int 自定义起始行
     * @param {*} PdfCellCallback 特殊回调处理
     * @return {*}
     * @description: 解析pdf表格（私有方法）
     * 使用 tabula-java 的 sdk 基本上都是这样来解析 pdf 中的表格的，所以可以将程序提取出来，直到 cell
     * 单元格为止
     */
    private static JSONArray parsePdfTable(String pdfPath, int customStart, PdfCellCustomProcess callback) {
        JSONArray reJsonArr = new JSONArray(); // 儲存解析後的 JSON 數組

        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            PageIterator pi = new ObjectExtractor(document).extract(); // 獲取頁面迭代器

            // 遍歷所有頁面
            while (pi.hasNext()) {
                Page page = pi.next(); // 獲取當前頁面
                List<Table> tableList = SPREADSHEEET_EXTRACTION_ALGORITHM.extract(page); // 解析頁面上的所有表格

                // 遍歷所有表格
                for (Table table : tableList) {
                    List<List<RectangularTextContainer>> rowList = table.getRows(); // 獲取表格中的每一行

                    // 遍历所有行并获取每个单元格信息
                    for (int rowIndex = customStart; rowIndex < rowList.size(); rowIndex++) {
                        List<RectangularTextContainer> cellList = rowList.get(rowIndex); // 获取行中的每个单元格
                        callback.handler(cellList, rowIndex, reJsonArr);
                    }
                }
            }
        } catch (IOException e) {

            System.out.println("e.getCause()" + e.getCause());
            System.out.println("e.getMessage()" + e.getMessage());
            e.getStackTrace();
        } finally {
            THREAD_LOCAL.remove();
        }
        return reJsonArr; // 返回解析後的 JSON 數組
    }

    /**
     * @param {*} String PDF文件路径
     * @param {*} int 自定义起始行
     * @return {*}
     * @description: 解析 pdf 中简单的表格并返回 json 数组
     */
    public static JSONArray parsePdfSimpleTable(String pdfPath, int customStart) {
        return parsePdfTable(pdfPath, customStart, (cellList, rowIndex, reArr) -> {
            JSONObject jsonObj = new JSONObject();
            // 遍历单元格获取每个单元格内字段内容
            List<String> headList = (THREAD_LOCAL.get() == null) ? new ArrayList<>() : THREAD_LOCAL.get();

            int col = 0;
            for (int colIndex = 0; colIndex < cellList.size(); colIndex++) {
                String text = cellList.get(colIndex).getText().replace("\r", " ");
                if (rowIndex == customStart) {
                    headList.add(text);
                } else {
                    jsonObj.put(headList.get(colIndex), text);
                }
            }

            if (rowIndex == customStart) {
                THREAD_LOCAL.set(headList);
            }

            if (!jsonObj.isEmpty()) {
                reArr.add(jsonObj);
            }
        });
    }

}

