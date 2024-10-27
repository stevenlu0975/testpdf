package org.example;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class RectanglePoints {
    private float topLeftX, topRightX,topLineY;
    private float botttomLeftX, bottomRightX,bottomLineY;
    private float width;
    @Setter(AccessLevel.NONE)
    private float length;
//    @Setter(AccessLevel.NONE)
//    private byte finishedPoints; // 處理完幾個頂點
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<List<RowDataDto>> matrix;
    public RectanglePoints(){
        this.topLeftX=-1;
        this.topRightX=-1;
        this.topLineY=-1;
        this.botttomLeftX=-1;
        this.bottomRightX=-1;
        this.bottomLineY=-1;
    }

    public void addRowData(List<RowDataDto> list){
        if(matrix==null){
            matrix = new ArrayList<>();
        }
        matrix.add(list);
    }
    /**
     * 計算長框
     * */
    public void calculateLengthWidth(){
        this.length=topRightX-topLeftX;
        this.width=topLineY-bottomLineY;
//        float leftmostX=topLeftX;
//        float rightmostX=topRightX;
//        float topmostY=topLineY;
//        float bottommostY=bottomLineY;
//        for(List<RowDataDto> list : matrix){
//            for(RowDataDto dto : list){
//              //todo
//            }
//        }
    }
}
