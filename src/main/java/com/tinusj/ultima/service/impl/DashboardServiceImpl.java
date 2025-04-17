package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.ActivityDto;
import com.tinusj.ultima.dao.dto.AnalyticsMetricsDto;
import com.tinusj.ultima.dao.dto.AudienceDto;
import com.tinusj.ultima.dao.dto.BestSellerDto;
import com.tinusj.ultima.dao.dto.BlogPostDto;
import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.DashboardMetricsDto;
import com.tinusj.ultima.dao.dto.DeviceDto;
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
import com.tinusj.ultima.repository.ActivityRepository;
import com.tinusj.ultima.repository.BlogPostRepository;
import com.tinusj.ultima.repository.ChatMessageRepository;
import com.tinusj.ultima.repository.CommentRepository;
import com.tinusj.ultima.repository.ContactRepository;
import com.tinusj.ultima.repository.CustomerRepository;
import com.tinusj.ultima.repository.OrderRepository;
import com.tinusj.ultima.repository.ProductRepository;
import com.tinusj.ultima.repository.SubscriptionRepository;
import com.tinusj.ultima.repository.TaskRepository;
import com.tinusj.ultima.repository.TimelineEventRepository;
import com.tinusj.ultima.repository.VisitorRepository;
import com.tinusj.ultima.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final ContactRepository contactRepository;
    private final TimelineEventRepository timelineEventRepository;
    private final ProductRepository productRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final VisitorRepository visitorRepository;
    private final BlogPostRepository blogPostRepository;

    public DashboardServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository,
                                CommentRepository commentRepository, ContactRepository contactRepository,
                                TimelineEventRepository timelineEventRepository, ProductRepository productRepository,
                                ChatMessageRepository chatMessageRepository, ActivityRepository activityRepository,
                                TaskRepository taskRepository, SubscriptionRepository subscriptionRepository,
                                VisitorRepository visitorRepository, BlogPostRepository blogPostRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.commentRepository = commentRepository;
        this.contactRepository = contactRepository;
        this.timelineEventRepository = timelineEventRepository;
        this.productRepository = productRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.activityRepository = activityRepository;
        this.taskRepository = taskRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.visitorRepository = visitorRepository;
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public DashboardMetricsDto getDashboardMetrics() {
        long orderCount = orderRepository.count();
        double revenue = orderRepository.sumTotalPrice() != null ? orderRepository.sumTotalPrice() : 0.0;
        long customerCount = customerRepository.count();
        long commentCount = commentRepository.count();
        return new DashboardMetricsDto(orderCount, revenue, customerCount, commentCount);
    }

    @Override
    public SaaSMetricsDto getSaaSMetrics() {
        long userCount = customerRepository.count();
        long subscriptionCount = subscriptionRepository.count();
        double revenue = orderRepository.sumTotalPrice() != null ? orderRepository.sumTotalPrice() : 0.0;
        long visitorCount = visitorRepository.count();
        return new SaaSMetricsDto(userCount, subscriptionCount, revenue, visitorCount);
    }

    @Override
    public AnalyticsMetricsDto getAnalyticsMetrics(LocalDate startDate) {
        double revenue = orderRepository.sumTotalPrice() != null ? orderRepository.sumTotalPrice() : 0.0;
        long potentialReach = customerRepository.count() * 10; // Simplified: 10x customers
        long pageviews = visitorRepository.count();
        long totalVisits = visitorRepository.count();
        double engagementRate = totalVisits > 0 ? orderRepository.calculateEngagementRate(startDate, totalVisits) : 0.0;
        return new AnalyticsMetricsDto(revenue, potentialReach, pageviews, engagementRate);
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
    public List<RevenueGraphDataDto> getRevenueGraphData(LocalDate startDate) {
        return orderRepository.findRevenueByDate(startDate).stream()
                .map(obj -> new RevenueGraphDataDto((LocalDate) obj[0], ((Number) obj[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorsGraphDataDto> getVisitorsGraphData(LocalDate startDate) {
        return visitorRepository.findVisitorCountsByDate(startDate).stream()
                .map(obj -> new VisitorsGraphDataDto((LocalDate) obj[0], ((Number) obj[1]).longValue()))
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

    @Override
    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(t -> new TaskDto(t.getId(), t.getTitle(), t.getDescription(), t.getStatus().name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionDto> getSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(s -> new SubscriptionDto(s.getId(), s.getName(), s.getPrice(), s.getMaxUsers(), s.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorDto> getVisitors() {
        return visitorRepository.findAll().stream()
                .map(v -> new VisitorDto(v.getId(), v.getSource(), v.getVisitCount(), v.getVisitDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MostVisitedPageDto> getMostVisitedPages() {
        return visitorRepository.findMostVisitedPages().stream()
                .map(obj -> new MostVisitedPageDto((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReferralDto> getReferrals() {
        return visitorRepository.findReferrals().stream()
                .map(obj -> new ReferralDto((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDto> getDevices() {
        return visitorRepository.findDeviceDistribution().stream()
                .map(obj -> new DeviceDto((String) obj[0], ((Number) obj[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AudienceDto> getAudience() {
        // Simplified: Assume customers represent audience, with mock age/gender
        return customerRepository.findAll().stream()
                .map(c -> new AudienceDto(c.getId(), c.getName(), "18-34", "Unknown"))
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDto> getBlogPosts() {
        return blogPostRepository.findAll().stream()
                .map(b -> new BlogPostDto(b.getId(), b.getTitle(), b.getViewCount()))
                .collect(Collectors.toList());
    }
}