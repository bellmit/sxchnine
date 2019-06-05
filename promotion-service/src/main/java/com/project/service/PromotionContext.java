package com.project.service;

import com.project.business.PromotionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PromotionContext {

    private Map<Integer, PromotionStrategy> strategiesMap = new HashMap<>();

    @Autowired
    private List<PromotionStrategy> strategies;

    @PostConstruct
    public void loadStrategies(){
        strategies.stream().forEach(s -> strategiesMap.put(s.getPromotion(), s));
    }

    public PromotionStrategy getStrategy(int promotion){
        return strategiesMap.get(promotion);
    }

}
