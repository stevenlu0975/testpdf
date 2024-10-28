package org.example;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import java.util.ArrayList;
import java.util.List;

public class TextAreaRetrieveStrategy extends LocationTextExtractionStrategy {
    private static final float Y_OFFSET=10;//超過10才算換行
    private String firstRowStartText;
    private String firstRowEndText;
    private String boundaryText;
    private StringBuilder currentLine ;
    private List<RowDataDto> tempRowData;
    private float lastY = -1;
    private float lastRowWidth = -1;
    private  RectanglePoints rectanglePoints;
    TextAreaStateEnum state=TextAreaStateEnum.None;
    /**
     * 結束判斷為這行最後一個字
     * */
    public TextAreaRetrieveStrategy(String firstRowStartText) {
        this(firstRowStartText,"","");
    }
    public TextAreaRetrieveStrategy(String firstRowStartText,String firstRowEndText) {
        this(firstRowStartText,firstRowEndText,"");
    }
    public TextAreaRetrieveStrategy(String firstRowStartText,String firstRowEndText,String boundaryText) {
        this.firstRowStartText = firstRowStartText;
        this.firstRowEndText = firstRowEndText;
        this.boundaryText = boundaryText;
        rectanglePoints = new RectanglePoints();
        currentLine = new StringBuilder();
        tempRowData = new ArrayList<>();
    }
    @Override
    public void eventOccurred(IEventData data, EventType type) {
        if (type == EventType.RENDER_TEXT) {
            TextRenderInfo renderInfo = (TextRenderInfo) data;
            retrieveTextProcess(renderInfo);
        }
    }
    private void retrieveTextProcess(TextRenderInfo renderInfo) throws IndexOutOfBoundsException{
        String text = renderInfo.getText();
        if(text==null) {
            return;
        }
        float currentY = renderInfo.getBaseline().getStartPoint().get(1);
        float currentLX = renderInfo.getBaseline().getStartPoint().get(0);
        float currentRX = renderInfo.getBaseline().getEndPoint().get(0);
        float currentCell= renderInfo.getAscentLine().getEndPoint().get(1);
        float currentFloor =renderInfo.getDescentLine().getStartPoint().get(1);
        lastRowWidth = renderInfo.getAscentLine().getEndPoint().get(1)-renderInfo.getDescentLine().getStartPoint().get(1);
        RowDataDto dataDto = new RowDataDto(currentLX,currentRX,currentY,currentCell,currentFloor,text);


        if(lastY!=-1 && lastY!=currentY){
            //超過10才算換行
            if(Math.abs(currentY-lastY)<Y_OFFSET){
                currentLine.append(text);
                tempRowData.add(dataDto);
                return;
            }
            //判斷左上頂點
            if(state == TextAreaStateEnum.None && currentLine.toString().contains(firstRowStartText)){
                int index = getIndexFromRowList(firstRowStartText,tempRowData);
                rectanglePoints.setTopLeftX(tempRowData.get(index).getPointStartX());
                rectanglePoints.setTopLineY(tempRowData.get(index).getPointY());
                state = TextAreaStateEnum.TopLeftFound;
            }
            //判斷右上頂點
            if(state == TextAreaStateEnum.TopLeftFound){

                if(!firstRowEndText.isEmpty() && currentLine.toString().contains(firstRowEndText)){
                    int index = getIndexFromRowList(firstRowEndText,tempRowData,true);
                    rectanglePoints.setTopRightX(tempRowData.get(index).getPointEndX());
                    state = TextAreaStateEnum.TopRightFound;
                }else{
                    rectanglePoints.setTopRightX(tempRowData.getLast().getPointEndX());
                    state = TextAreaStateEnum.TopRightFound;
                }
            }

            //判斷結束
            if(state == TextAreaStateEnum.TopRightFound){
                //只擷取一行
                if(boundaryText.equals("")){
                    state = TextAreaStateEnum.None;
                    rectanglePoints.calculateWidthHeight();
                }
                else if(currentLine.toString().contains(boundaryText)){
                    state = TextAreaStateEnum.None;
                    rectanglePoints.calculateWidthHeight();

                }

            }
            //判斷到header才加進lsit
            if(state != TextAreaStateEnum.None){
                rectanglePoints.addRowData(tempRowData);
            }
            tempRowData = new ArrayList<>();
            currentLine.delete(0, currentLine.length());
        }
        currentLine.append(text);
        lastY = currentY;
        tempRowData.add(dataDto);
    }
    private int getIndexFromRowList(String str, List<RowDataDto> tempRowData) {
        return getIndexFromRowList(str, tempRowData, false);
    }

    private int getIndexFromRowList(String str, List<RowDataDto> tempRowData, boolean inverse) {
        int index = -1;
        if (inverse) {
            // 反向遍歷
            for (int i = tempRowData.size() - 1; i >= 0; i--) {
                RowDataDto data = tempRowData.get(i);
                if(data.getText().equals("/")||data.getText().isBlank()){
                    continue;
                }
                if (str.contains(data.getText())) {
                    index = i;
                    break;
                }
            }
        } else {
            // 正向遍歷
            for (int i = 0; i < tempRowData.size(); i++) {
                RowDataDto data = tempRowData.get(i);
                if(data.getText().equals("/")|| data.getText().isBlank() ){
                    continue;
                }
                if (str.contains(data.getText())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public RectanglePoints getRectanglePoints(){return rectanglePoints;}
}