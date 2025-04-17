package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ActivityDto;
import com.tinusj.ultima.dao.dto.BestSellerDto;
import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.DashboardMetricsDto;
import com.tinusj.ultima.dao.dto.OrderGraphDataDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.TimelineEventDto;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    DashboardMetricsDto getDashboardMetrics();

    List<ContactDto> getContacts();

    List<OrderGraphDataDto> getOrderGraphData(LocalDate startDate);

    List<TimelineEventDto> getTimelineEvents();

    List<ProductDto> getProducts();

    List<ChatMessageDto> getChatMessages();

    List<ActivityDto> getActivities();

    List<BestSellerDto> getBestSellers();
}
