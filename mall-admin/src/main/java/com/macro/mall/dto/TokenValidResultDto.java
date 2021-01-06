package com.macro.mall.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuanxx
 * @description :
 * @create 2017-10-31
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidResultDto {
    private Integer userId;
    private String mobile;
    private String userName;
    private boolean valid;
}
