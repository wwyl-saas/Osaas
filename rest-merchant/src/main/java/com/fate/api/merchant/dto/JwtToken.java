package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * token负载信息
 */
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken implements Serializable {
    private Long merchantId;
    private Long userId;
}
