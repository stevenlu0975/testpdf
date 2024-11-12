package testpdf;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
//import org.example.javaAPIService.pdftool.PdfTableExtractor;
//import org.example.javaAPIService.pdftool.MyItextTool;
//import org.example.javaAPIService.pdftool.MySimpleTextExtractionStrategy;
import org.example.javaAPIService.pdftool.*;

//import com.example.itext.pdftool.MyItextTool;
//import com.example.itext.pdftool.MySimpleTextExtractionStrategy;
//import com.example.itext.pdftool.PdfTableExtractor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;



/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    /**
     * itext
     * **/
    public void testitext() throws IOException {
//        String filePath = "C:\\workspace\\pdf\\55555.pdf";
//        String filePath = "C:\\workspace\\pdf\\test.pdf";
        String filePath = "C:\\workspace\\pdf\\77777.pdf";
        File file = new File(filePath);
        // 加載PDF檔案
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(file));

        // 提取每一頁的文本
        for (int page = 2; page <= pdfDoc.getNumberOfPages(); page++) {
            String text = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(page), new MySimpleTextExtractionStrategy());
//            System.out.println("Page " + page + ": ");
//            System.out.println("=================");
//            System.out.println(text);
//            System.out.println("=================");

            // 解析文本為表格
//            parseTableFromText(text);
            PdfTableExtractor.parseTableFromText(text);
        }
        // 關閉文件
        pdfDoc.close();

    }

    public void testMyItextTool() throws IOException {
        String filePath = "C:\\workspace\\pdf\\77777.pdf";
        File file = new File(filePath);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(file));
        String specialContent = MyItextTool.getSpecialContent(pdfDoc.getPage(1),new MySimpleTextExtractionStrategy());
        MyItextTool.parseTableFromText(specialContent);

    }

}
