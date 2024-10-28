package org.example;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import lombok.Getter;

public class CropboxContentStrategy extends LocationTextExtractionStrategy{

    @Getter
    private StringBuilder stringBuilder;
    @Getter
    private StringBuilder stringBuilder2 = new StringBuilder();
    private Rectangle cropBox;
    private float lastY = -1;

    public CropboxContentStrategy(Rectangle cropBox) {
        this.stringBuilder = new StringBuilder();
        this.cropBox = cropBox;
    }

    @Override
    public void eventOccurred(IEventData data, EventType type) {
        if (data instanceof TextRenderInfo) {
            TextRenderInfo textInfo = (TextRenderInfo) data;
            Rectangle rect = textInfo.getBaseline().getBoundingRectangle();

            // 判断文本是否在裁剪框内
            if (cropBox.contains(rect)) {
                if (lastY != -1 && Math.abs(rect.getY() - lastY) > 5) {
                    stringBuilder.append("\n"); // 插入換行
                }
                stringBuilder2.append(textInfo.getText());
                stringBuilder.append(textInfo.getText());
                lastY = rect.getY(); // 更新Y座標
            }
        }
        super.eventOccurred(data, type);
    }

}
