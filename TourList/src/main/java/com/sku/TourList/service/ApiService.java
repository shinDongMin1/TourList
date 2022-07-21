package com.sku.TourList.service;

import com.sku.TourList.domain.Tour;

import java.util.List;

public interface ApiService {
    public List<Tour> CallRegion(String number);
    public List<Tour> CallApi(String areaCode, String sigunguCode);
    public Tour CallDetail(String contentId);
}
