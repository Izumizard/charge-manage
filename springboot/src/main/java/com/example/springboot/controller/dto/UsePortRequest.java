package com.example.springboot.controller.dto;

import com.example.springboot.entity.ChargingPort;
import com.example.springboot.entity.Orders;
import lombok.Data;

/**
 * 功能：
 * 日期：2024/1/14 16:09
 */
@Data
public class UsePortRequest {
    private Orders orders;
    private ChargingPort chargingPort;
}
