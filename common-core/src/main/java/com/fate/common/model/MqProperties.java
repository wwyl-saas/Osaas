package com.fate.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: parent
 * @description: mq属性
 * @author: chenyixin
 * @create: 2019-09-23 15:43
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqProperties implements Serializable {
    private Long merchantId;
    private String messageTag;
    private Integer delay;
}
