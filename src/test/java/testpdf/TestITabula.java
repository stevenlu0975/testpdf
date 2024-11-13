package testpdf;


import com.alibaba.fastjson.JSONArray;
import org.example.java_api_service.tabula.PdfUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

public class TestITabula extends TestCase {
    /**
     * PdfTextExtractor.getTextFromPage
     * 根据 PDF 内部的文本顺序提取
     * tubula
     */
    public TestITabula( String testName ){
        super(testName);
    }
    public void testApp() throws IOException {
        testTbubala();
    }
    public static Test suite()
    {
        return new TestSuite( TestITabula.class );
    }

    public void testTbubala(){
        String path = "D:\\workspace\\temp\\77777.pdf";
        JSONArray result = PdfUtil.parsePdfSimpleTable(path,0);
        System.out.println(result);
    }
    private static void parseTableFromText(String text) {
        // 這裡假設表格的每一行數據由空格或制表符分隔
        String[] rows = text.split("\n");  // 按行分割文本

        for (String row : rows) {
            // 根據空格或制表符來分割每一行中的列數據
            String[] columns = row.split("\\s+");  // 使用正則表達式按多個空格分割列數據

            // 顯示提取的表格數據
            for (String column : columns) {
                System.out.print(column + "\t");
            }
            System.out.println();
        }
    }
}
