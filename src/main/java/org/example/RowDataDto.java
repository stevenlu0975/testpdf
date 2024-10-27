package org.example;

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
    private String text;
}
