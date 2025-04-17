package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ActivityDto;
import com.tinusj.ultima.dao.dto.BestSellerDto;
import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.DashboardMetricsDto;
import com.tinusj.ultima.dao.dto.OrderGraphDataDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.RevenueGraphDataDto;
import com.tinusj.ultima.dao.dto.SaaSMetricsDto;
import com.tinusj.ultima.dao.dto.SubscriptionDto;
import com.tinusj.ultima.dao.dto.TaskDto;
import com.tinusj.ultima.dao.dto.TimelineEventDto;
import com.tinusj.ultima.dao.dto.VisitorDto;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    DashboardMetricsDto getDashboardMetrics();

    SaaSMetricsDto getSaaSMetrics();

    List<ContactDto> getContacts();

    List<OrderGraphDataDto> getOrderGraphData(LocalDate startDate);

    List<RevenueGraphDataDto> getRevenueGraphData(LocalDate startDate);

    List<TimelineEventDto> getTimelineEvents();

    List<ProductDto> getProducts();

    List<ChatMessageDto> getChatMessages();

    List<ActivityDto> getActivities();

    List<BestSellerDto> getBestSellers();

    List<TaskDto> getTasks();

    List<SubscriptionDto> getSubscriptions();

    List<VisitorDto> getVisitors();
}
