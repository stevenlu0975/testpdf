package org.example;

import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import java.util.ArrayList;
import java.util.List;

public class TextLocationStrategy extends LocationTextExtractionStrategy {

    private String searchText;
    private StringBuilder currentLine = new StringBuilder();
    private StringBuilder fullText = new StringBuilder();
    private List<TextRenderInfo> textRenderInfos = new ArrayList<>();
    float startX = -1;
    private float lastY = -1;
    private float lastX = -1;
    private boolean samerow = false;
    private float xDifference = 0;
    private float yDifference = 0;
    private float reY = -1;
    private float reX = -1;

    public TextLocationStrategy(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void eventOccurred(IEventData data, EventType type) {
        if (type == EventType.RENDER_TEXT) {

            TextRenderInfo renderInfo = (TextRenderInfo) data;

            String text = renderInfo.getText();
            if (text != null) {

                float currentY = renderInfo.getBaseline().getStartPoint().get(1);

                // 獲取文字的起始位置（左下角）
                // 如果 Y 座標變化超過一定範圍，認定為新行
                if (lastY != -1 && (currentY != lastY)) {//換行了
                    if(currentLine.toString().contains(searchText)){

                        System.out.println("currentline : " + currentLine);
                        System.out.println("文字 \"" + searchText + "\" 位於：X=" + startX + ", Y=" + lastY);
                        System.out.println("文字 \"" + searchText + "\" 結束於：X=" + lastX + ", Y=" + lastY);

                        reX = startX;
                        reY = lastY;
                        xDifference = lastX - startX;
                        yDifference = renderInfo.getAscentLine().getEndPoint().get(1) - renderInfo.getDescentLine().getStartPoint().get(1);
                        System.out.println("yDifference "+yDifference);
                    }
                    samerow = false;
                    currentLine = new StringBuilder(); // 開啟新行
                }
                if(!samerow) {
                    startX = renderInfo.getBaseline().getStartPoint().get(0);
                    samerow = true;
                }
                // 拼接文字
                currentLine.append(text);
                lastY = currentY;
                lastX = renderInfo.getBaseline().getEndPoint().get(0);
            }

        }
    }
//    @Override
//    public String getResultantText() {
//        // 將拼接的文字轉換為字符串，並檢查是否包含要查找的文本
//        String allText = fullText.toString();
//        int startIndex = allText.indexOf(searchText);
//
//        if (startIndex != -1) {
//            // 文字匹配成功，計算起始和結束位置
//            int endIndex = startIndex + searchText.length();
//
//            System.out.println("找到文字: " + searchText);
//
//            // 遍歷文字塊，找到起始位置和結束位置
//            for (int i = 0; i < textRenderInfos.size(); i++) {
//                TextRenderInfo renderInfo = textRenderInfos.get(i);
//                String currentText = renderInfo.getText();
//
//                if (startIndex < currentText.length()) {
//                    // 找到起始點
//                    Vector startPoint = renderInfo.getBaseline().getStartPoint();
//                    System.out.println("起始位置：X=" + startPoint.get(0) + ", Y=" + startPoint.get(1));
//                    break;
//                }
//                startIndex -= currentText.length();
//            }
//
//            // 遍歷文字塊，找到結束位置
//            for (int i = 0; i < textRenderInfos.size(); i++) {
//                TextRenderInfo renderInfo = textRenderInfos.get(i);
//                String currentText = renderInfo.getText();
//
//                if (endIndex <= currentText.length()) {
//                    // 找到結束點
//                    Vector endPoint = renderInfo.getAscentLine().getEndPoint();
//                    System.out.println("結束位置：X=" + endPoint.get(0) + ", Y=" + endPoint.get(1));
//                    break;
//                }
//                endIndex -= currentText.length();
//            }
//        } else {
//            System.out.println("未找到文字：" + searchText);
//        }
//
//        return allText;
//    }

    public float getReX() {
        System.out.println("reX : " + reX);
        return reX;
    }
    public float getReY() {
        System.out.println("lastY : " + reY);
        return reY;
    }
    public float getXDifference() {
        System.out.println("xDifferent : " + xDifference);
        return xDifference;
    }
    public float getYDifference() {
        System.out.println("yDifferent : " + yDifference);
        return yDifference;
    }
}
