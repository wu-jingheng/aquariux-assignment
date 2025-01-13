package com.wjh.aquariux_assignment_wjh.dto;

import com.wjh.aquariux_assignment_wjh.model.AggregatedPrice;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Generated
@AllArgsConstructor
public class PriceRetrievalResponse {
    private String message;
    private Map<String, AggregatedPrice> aggregatedPrices;
}
