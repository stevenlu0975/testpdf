package org.example.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RowDataDto {
    private float pointStartX;
    private float pointEndX;
    private float pointY;
    private float ceil;
    private float floor;
    private String text;
}
