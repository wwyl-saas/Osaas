package com.fate.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * @program: parent
 * @description: 只传Id的消息体
 * @author: chenyixin
 * @create: 2019-09-13 13:28
 **/
@Data
@AllArgsConstructor
public class MyMessage implements Serializable {
    private Long id;
}
