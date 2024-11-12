package org.example.javaAPIService.tabula;

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
     * @description: 解析pdf表格（私有方法）
     *               使用 tabula-java 的 sdk 基本上都是这样来解析 pdf 中的表格的，所以可以将程序提取出来，直到 cell
     *               单元格为止
     * @param {*} String pdf 路径
     * @param {*} int 自定义起始行
     * @param {*} PdfCellCallback 特殊回调处理
     * @return {*}
     */
    private static JSONArray parsePdfTable(String pdfPath, int customStart, PdfCellCustomProcess callback) {
        JSONArray reJsonArr = new JSONArray(); // 存储解析后的JSON数组

        try (PDDocument document = Loader.loadPDF(new File(pdfPath))){//PDDocument.load(new File(pdfPath))) {
            PageIterator pi = new ObjectExtractor(document).extract(); // 获取页面迭代器

            // 遍历所有页面
            while (pi.hasNext()) {
                Page page = pi.next(); // 获取当前页
                List<Table> tableList = SPREADSHEEET_EXTRACTION_ALGORITHM.extract(page); // 解析页面上的所有表格

                // 遍历所有表格
                for (Table table : tableList) {
                    List<List<RectangularTextContainer>> rowList = table.getRows(); // 获取表格中的每一行

                    // 遍历所有行并获取每个单元格信息
                    for (int rowIndex = customStart; rowIndex < rowList.size(); rowIndex++) {
                        List<RectangularTextContainer> cellList = rowList.get(rowIndex); // 获取行中的每个单元格
                        callback.handler(cellList, rowIndex, reJsonArr);
                    }
                }
            }
        } catch (IOException e) {

            System.out.println("e.getCause()"+e.getCause());
            System.out.println("e.getMessage()"+e.getMessage());
            e.getStackTrace();
        } finally {
            THREAD_LOCAL.remove();
        }
        return reJsonArr; // 返回解析后的JSON数组
    }
    /**
     * @description: 解析 pdf 中简单的表格并返回 json 数组
     * @param {*} String PDF文件路径
     * @param {*} int 自定义起始行
     * @return {*}
     */
    public static JSONArray parsePdfSimpleTable(String pdfPath, int customStart) {
        return parsePdfTable(pdfPath, customStart, (cellList, rowIndex, reArr) -> {
            JSONObject jsonObj = new JSONObject();
            // 遍历单元格获取每个单元格内字段内容
            List<String> headList = (THREAD_LOCAL.get() == null) ? new ArrayList<>() : THREAD_LOCAL.get();

int col=0;
            for (int colIndex = 0; colIndex < cellList.size(); colIndex++) {
                String text = cellList.get(colIndex).getText().replace("\r", " ");
                if (rowIndex == customStart) {
//                    System.out.println("rowindex : "+rowIndex+"col : "+col+text);

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

