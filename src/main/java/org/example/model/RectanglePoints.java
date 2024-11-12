package org.example.model;

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
    private static final float X_OFFSET=10;// 不得插超過+-20
    private float topLeftX, topRightX,topLineY;
    private float bottomLeftX, bottomRightX,bottomLineY;
    private float height;
    @Setter(AccessLevel.NONE)
    private float width;
//    @Setter(AccessLevel.NONE)
//    private byte finishedPoints; // 處理完幾個頂點
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<List<RowDataDto>> matrix;
    public RectanglePoints(){
        this.topLeftX=-1;
        this.topRightX=-1;
        this.topLineY=-1;
        this.bottomLeftX =-1;
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
    public void calculateWidthHeight(){
//        this.width=topRightX-topLeftX;
//        this.height=topLineY-bottomLineY;
        float leftmostX=topLeftX;
        float rightmostX=topRightX;
        float topmostY=topLineY;
        float bottommostY=matrix.getLast().getFirst().getPointY();
        float topCell=matrix.getFirst().getFirst().getCeil();
        float bottomFloor=matrix.getLast().getLast().getFloor();
        for(List<RowDataDto> list : matrix){
            for(RowDataDto dto : list){
              //todo
                float lx = dto.getPointStartX();
                float rx = dto.getPointEndX();
                float y = dto.getPointY();
                float cell = dto.getCeil();
                float floor =dto.getFloor();
                //找最左邊的點
                if(lx<leftmostX ){
                    leftmostX =lx;
                }
                //找最右邊的點
                if(rx>rightmostX ){
                    rightmostX = rx;
                }

                if(y<bottommostY){
                    bottommostY=y;
                }
                //找cell
                if(cell>topCell){
                    topCell = cell;
                }
                //找floor
                if(floor<bottomFloor){
                    bottomFloor = floor;
                }
            }
        }
        this.setBottomLeftX(leftmostX);
        this.setBottomLineY(bottommostY);
        this.width=rightmostX-leftmostX;
        this.height=topCell-bottomFloor;
        System.out.println();
        System.out.println("top x"+topLeftX+" ~ "+topRightX+" top y"+topLineY);
        System.out.println("bottom x"+ bottomLeftX +" bottom y"+bottomLineY);
        System.out.println("leftMost,rightmost "+leftmostX+" , "+rightmostX);
        System.out.println("topMost,bottomMost "+topmostY+" , "+bottommostY);
        System.out.println("cell,floor"+topCell+" , "+bottomFloor);
    }
}
