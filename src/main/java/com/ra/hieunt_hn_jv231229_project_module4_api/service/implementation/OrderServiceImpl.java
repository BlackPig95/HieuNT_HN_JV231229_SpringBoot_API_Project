package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderDetailWithProductInfoResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderWithDetailResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.RevenueTimeResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Order;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.OrderDetail;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IOrderRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IOrderService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService
{
    private final IOrderRepo orderRepo;
    private final IProductRepo productRepo;

    @Override
    public List<Order> findAllOrders()
    {
        return orderRepo.findAll();
    }

    @Override
    public Order updateOrderStatus(Long orderId, String newOrderStatus)
    {
        Order updateOrder = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("No order with id " + orderId + " found"));
        for (OrderStatus orderStatus : OrderStatus.values())
        {
            if (newOrderStatus.equalsIgnoreCase(orderStatus.name()))
            {
                updateOrder.setStatus(orderStatus);
                return orderRepo.save(updateOrder);
            }
        }
        throw new RuntimeException("Status is not valid. " +
                "Please choose one of the status in this list: 'WAITING','CONFIRM','DELIVERY','SUCCESS','CANCEL','DENIED'");
    }

    @Override//Find the order with detailed information about order details and
    //product details that are attached to this order via foreign key
    public OrderWithDetailResponse findOrderAndDetails(Long orderId)
    {
        //Result set from orderRepo include 2 different entities: Order and OrderDetail
        List<Object[]> orderAndDetail = orderRepo.getOrderAndDetail(orderId);
        //Create an order entity to hold the reference to the entity returned from orderRepo
        Order orderInfo = new Order();
        //Create a list of OrderDetail entity to hold the reference from orderRepo
        //because each Order can have multiple OrderDetail attached to it via foreign key
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Object[] row : orderAndDetail)
        {
            orderInfo = (Order) row[0];//Ordinal number of entities returned from orderRepo is based on the @Query statement
            OrderDetail orderDetail = (OrderDetail) row[1];
            orderDetailList.add(orderDetail);//Add each OrderDetail entity to the list
        }
        //Response entity created to support formatting the info to return to the API request
        List<OrderDetailWithProductInfoResponse> orderAndProductDetailList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList)
        {   //Get the product info by using the orderDetail foreign key
            Product product = productRepo.findById(orderDetail.getCompositeKey().getProduct().getProductId()).orElseThrow(() -> new RuntimeException("Can't find info about the product on this order detail"));
            //Set up info in the response entity to return to the API request
            OrderDetailWithProductInfoResponse orderAndProductDetail
                    = OrderDetailWithProductInfoResponse.builder()
                    //To display which order this orderDetail belongs to
                    .belongToOrderId(orderDetail.getCompositeKey().getOrder().getOrderId())
                    //To display which product this orderDetail is referring to
                    .withProductId(orderDetail.getCompositeKey().getProduct().getProductId())
                    //Price per unit in order detail
                    .unitPrice(orderDetail.getUnitPrice())
                    //Quantity of the products purchased in each order detail
                    .orderQuantity(orderDetail.getOrderQuantity())
                    //Name of the product purchased
                    .productName(product.getProductName())
                    //Category name of the product purchased
                    .productCategory(product.getCategory().getCategoryName())
                    //Initial price of the product (Might differ with unitPrice in orderDetail
                    //due to promotion/ sale off/ etc...
                    .productPrice(product.getUnitPrice())
                    .build();
            //Total value of each orderDetail is the quantity of products purchased * price per unit
            orderAndProductDetail.setTotalValuePerOrderDetail(orderAndProductDetail.getUnitPrice() * orderAndProductDetail.getOrderQuantity());
            //Add each entity to the list of detailed information on each order detail that
            //is attached to the Order found by orderId
            orderAndProductDetailList.add(orderAndProductDetail);
        }
        //Could not use map because the Json in API body will always show String value
        //of the hashcode of orderInfo object (key-value pair display)
//        Map<Order, List<OrderDetailWithProductInfoResponse>> orderAndDetailMap = new HashMap<>();
//        orderAndDetailMap.put(orderInfo, orderAndProductDetailList);
        return OrderWithDetailResponse.builder()
                .orderId(orderInfo.getOrderId())
                .serialNumber(orderInfo.getSerialNumber())
                .totalPrice(orderInfo.getTotalPrice())
                .status(orderInfo.getStatus().name())
                .note(orderInfo.getNote())
                .receiveName(orderInfo.getReceiveName())
                .receivePhone(orderInfo.getReceivePhone())
                .createdAt(orderInfo.getCreatedAt())
                .receivedAt(orderInfo.getReceivedAt())
                .customerName(orderInfo.getUser().getFullname())
                .orderDetailList(orderAndProductDetailList)
                .build();

    }

    @Override
    public List<Order> findOrdersByStatus(String status)
    {
        for (OrderStatus orderStatus : OrderStatus.values())
        {
            if (status.equalsIgnoreCase(orderStatus.name()))
                return orderRepo.findOrdersByStatus(orderStatus);
        }
        throw new RuntimeException("Can't find orders because the status chosen is not valid. " +
                "Please choose one of the status in this list: 'WAITING','CONFIRM','DELIVERY','SUCCESS','CANCEL','DENIED'");
    }

    @Override//Support method to get list of OrderId attached to the currently signed in user
    public List<Long> findOrdersIdByUserId(Long userId)
    {
        return orderRepo.findOrdersIdByUserId(userId);
    }

    @Override
    public RevenueTimeResponse findRevenueInTime(Date from, Date to)
    {
        Date tempDate = new Date();
        //Make sure the date can be passed in regardless of order in calendar and still
        //provide the same result
        if (from.after(to))
        {
            tempDate = from;
            from = to;
            to = tempDate;
        }
        List<Order> orderInTimeList = orderRepo.findOrderInTime(from, to);
        Double totalRevenue = orderInTimeList.stream().mapToDouble(Order::getTotalPrice).sum();
        return RevenueTimeResponse.builder()
                .from(from)
                .to(to)
                .revenue(totalRevenue)
                .build();
    }

    @Override
    public Integer findNumberOfInvoicesInTimePeriod(Date from, Date to)
    {
        Date tempDate = new Date();
        //Make sure the date can be passed in regardless of order in calendar and still
        //provide the same result
        if (from.after(to))
        {
            tempDate = from;
            from = to;
            to = tempDate;
        }
        return orderRepo.findOrderInTime(from, to).size();
    }
}
