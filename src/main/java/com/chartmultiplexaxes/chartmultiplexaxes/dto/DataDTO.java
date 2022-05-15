package com.chartmultiplexaxes.chartmultiplexaxes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {
    Map<LocalTime,Float>values;
    Map<LocalTime, Float> values2;
    Map<Integer,Float> intMap1;
    Map<Integer, Float> intMap2;
}
