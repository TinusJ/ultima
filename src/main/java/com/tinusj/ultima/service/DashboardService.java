package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ActivityDto;
import com.tinusj.ultima.dao.dto.AnalyticsMetricsDto;
import com.tinusj.ultima.dao.dto.AudienceDto;
import com.tinusj.ultima.dao.dto.BestSellerDto;
import com.tinusj.ultima.dao.dto.BlogPostDto;
import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.DashboardBlogPostDto;
import com.tinusj.ultima.dao.dto.DashboardMetricsDto;
import com.tinusj.ultima.dao.dto.DeviceDto;
import com.tinusj.ultima.dao.dto.FileDto;
import com.tinusj.ultima.dao.dto.FolderDto;
import com.tinusj.ultima.dao.dto.MostVisitedPageDto;
import com.tinusj.ultima.dao.dto.OrderGraphDataDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.ReferralDto;
import com.tinusj.ultima.dao.dto.RevenueGraphDataDto;
import com.tinusj.ultima.dao.dto.SaaSMetricsDto;
import com.tinusj.ultima.dao.dto.SubscriptionDto;
import com.tinusj.ultima.dao.dto.TaskDto;
import com.tinusj.ultima.dao.dto.TimelineEventDto;
import com.tinusj.ultima.dao.dto.VisitorDto;
import com.tinusj.ultima.dao.dto.VisitorsGraphDataDto;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    DashboardMetricsDto getDashboardMetrics();

    SaaSMetricsDto getSaaSMetrics();

    AnalyticsMetricsDto getAnalyticsMetrics(LocalDate startDate);

    List<ContactDto> getContacts();

    List<OrderGraphDataDto> getOrderGraphData(LocalDate startDate);

    List<RevenueGraphDataDto> getRevenueGraphData(LocalDate startDate);

    List<VisitorsGraphDataDto> getVisitorsGraphData(LocalDate startDate);

    List<TimelineEventDto> getTimelineEvents();

    List<ProductDto> getProducts();

    List<ChatMessageDto> getChatMessages();

    List<ActivityDto> getActivities();

    List<BestSellerDto> getBestSellers();

    List<TaskDto> getTasks();

    List<SubscriptionDto> getSubscriptions();

    List<VisitorDto> getVisitors();

    List<MostVisitedPageDto> getMostVisitedPages();

    List<ReferralDto> getReferrals();

    List<DeviceDto> getDevices();

    List<AudienceDto> getAudience();

    List<DashboardBlogPostDto> getBlogPosts();

    List<FileDto> getFiles(Long folderId);

    List<FolderDto> getFolders(Long parentFolderId);
}
