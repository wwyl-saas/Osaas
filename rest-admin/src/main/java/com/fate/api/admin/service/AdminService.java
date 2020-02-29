package com.fate.api.admin.service;

import com.fate.common.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-05 11:22
 **/
@Service
@Slf4j
public class AdminService {
    public Admin getById(long userId) {
        return new Admin();
    }
}
