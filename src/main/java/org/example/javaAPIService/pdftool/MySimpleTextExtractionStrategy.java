package org.example.javaAPIService.pdftool;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MySimpleTextExtractionStrategy implements ITextExtractionStrategy {
    private Vector lastStart;
    private Vector lastEnd;
    private final StringBuilder result = new StringBuilder();

    public MySimpleTextExtractionStrategy() {
    }


    public void eventOccurred(IEventData data, EventType type) {
        if (type.equals(EventType.RENDER_TEXT)) { //表示正在渲染文本
            TextRenderInfo renderInfo = (TextRenderInfo) data; //含了有關文本的詳細信息，例如基線、字體大小、文本內容
            boolean firstRender = this.result.isEmpty();
            boolean hardReturn = false; //是否需要硬回車（換行）
            LineSegment segment = renderInfo.getBaseline(); //當前文本的基線，使用這個基線來判斷文本的起始點 start 和終點 end
            Vector start = segment.getStartPoint();
            Vector end = segment.getEndPoint();
            //不是第一次渲染文本時 ，根據上一次渲染的基線位置來判斷文本是否應該換行
            if (!firstRender) {
                Vector x1 = this.lastStart;
                Vector x2 = this.lastEnd;
                float dist = x2.subtract(x1).cross(x1.subtract(start)).lengthSquared() / x2.subtract(x1).lengthSquared();
                float sameLineThreshold = 1.0F;
                if (dist > sameLineThreshold) {
                    hardReturn = true;
                }
            }
            /*
             *如果不是第一次渲染，並且上一次渲染的末尾不是空格，當前文本的第一個字符也不是空格，
             *會檢查前後文本之間的距離。如果距離超過單個空格的一半寬度，則在兩個文本段之間添加一個空格。
             */
            if (hardReturn) {
                this.appendTextChunk("\n");
            } else if (!firstRender && this.result.charAt(this.result.length() - 1) != ' ' && renderInfo.getText().length() > 0 && renderInfo.getText().charAt(0) != ' ') {
                float spacing = this.lastEnd.subtract(start).length();
                if (spacing > renderInfo.getSingleSpaceWidth() / 2.0F) {
                    this.appendTextChunk(" ");
                }
            }
            /*
            * 最後，將當前渲染的文本添加到 result 中。
            * 更新 lastStart 和 lastEnd，以便下一次渲染時能進行對比。
            * */
            this.appendTextChunk(renderInfo.getText());
            this.lastStart = start;
            this.lastEnd = end;
        }

    }
    /*
    * 這個方法返回該策略支持的事件類型集合。在這裡，策略只對 RENDER_TEXT 事件感興趣，即文本渲染事件。
    * */
    public Set<EventType> getSupportedEvents() {
        return Collections.unmodifiableSet(new LinkedHashSet(Collections.singletonList(EventType.RENDER_TEXT)));
    }
    /*該方法返回最終提取出的文本結果，即將 result 轉換為字符串。*/
    public String getResultantText() {
        return this.result.toString();
    }
    /*用來將文本片段追加到 result 中。*/
    protected final void appendTextChunk(CharSequence text) {
        this.result.append(text);
    }
    private void contentFilter(String str){

    }
}
