package org.example.java_api_service.tabula;

import com.alibaba.fastjson.JSONArray;
import technology.tabula.RectangularTextContainer;

import java.util.List;

@FunctionalInterface
public interface PdfCellCustomProcess {

    /**
     * @description: 自定义单元格回调处理
     * @return {*}
     */
    void handler(List<RectangularTextContainer> cellList, int rowIndex, JSONArray reJsonArr);
}
