package com.tinusj.ultima.service.impl;


import com.tinusj.ultima.dao.dto.ActivityDto;
import com.tinusj.ultima.dao.dto.BestSellerDto;
import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.DashboardMetricsDto;
import com.tinusj.ultima.dao.dto.OrderGraphDataDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.TimelineEventDto;
import com.tinusj.ultima.repository.ActivityRepository;
import com.tinusj.ultima.repository.ChatMessageRepository;
import com.tinusj.ultima.repository.CommentRepository;
import com.tinusj.ultima.repository.ContactRepository;
import com.tinusj.ultima.repository.CustomerRepository;
import com.tinusj.ultima.repository.OrderRepository;
import com.tinusj.ultima.repository.ProductRepository;
import com.tinusj.ultima.repository.TimelineEventRepository;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final ContactRepository contactRepository;
    private final TimelineEventRepository timelineEventRepository;
    private final ProductRepository productRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ActivityRepository activityRepository;

    @Override
    public DashboardMetricsDto getDashboardMetrics() {
        long orderCount = orderRepository.count();
        double revenue = orderRepository.sumTotalPrice() != null ? orderRepository.sumTotalPrice() : 0.0;
        long customerCount = customerRepository.count();
        long commentCount = commentRepository.count();
        return new DashboardMetricsDto(orderCount, revenue, customerCount, commentCount);
    }

    @Override
    public List<ContactDto> getContacts() {
        return contactRepository.findAll().stream()
                .map(c -> new ContactDto(c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderGraphDataDto> getOrderGraphData(LocalDate startDate) {
        return orderRepository.findOrderCountsByDate(startDate).stream()
                .map(obj -> new OrderGraphDataDto((LocalDate) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TimelineEventDto> getTimelineEvents() {
        return timelineEventRepository.findAll().stream()
                .map(e -> new TimelineEventDto(e.getId(), e.getTitle(), e.getDescription(), e.getEventDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageDto> getChatMessages() {
        return chatMessageRepository.findAll().stream()
                .map(m -> new ChatMessageDto(m.getId(), m.getSender(), m.getContent(), m.getSentAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivityDto> getActivities() {
        return activityRepository.findAll().stream()
                .map(a -> new ActivityDto(a.getId(), a.getDescription(), a.getActivityDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BestSellerDto> getBestSellers() {
        return productRepository.findBestSellers().stream()
                .map(obj -> new BestSellerDto(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        ((Number) obj[2]).longValue(),
                        new BigDecimal(obj[3].toString())))
                .collect(Collectors.toList());
    }
}