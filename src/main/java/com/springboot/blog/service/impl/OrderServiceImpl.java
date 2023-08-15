package com.springboot.blog.service.impl;

import com.springboot.blog.entity.*;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.*;
import com.springboot.blog.repository.*;
import com.springboot.blog.service.CartService;
import com.springboot.blog.service.OrderService;
import com.springboot.blog.utils.RandomString;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final TransactionRepository transactionRepository;

    @Override
    public Order checkOut(User user, OrderRequestDto orderRequestDto) {
        var carts = cartRepository.findByIdIn(orderRequestDto.getCheckedCartId());
        var productsIdInCart = cartRepository.findProductIdByIdIn(orderRequestDto.getCheckedCartId());
        if (productsIdInCart.size() != orderRequestDto.getCheckedCartId().size()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Some products are not in the cart");
        }

        var productsInCart = productRepository.findByIds(productsIdInCart);
        var productsInCartDto = productsInCart.stream().map((product) -> mapToDTO(product)).toList();

        List<OrderDetailBookResponse> orderDetailBookRes = productsInCartDto.stream()
                .flatMap(br -> carts.stream()
                        .filter(c -> Objects.equals(br.getId(), c.getProduct().getId()))
                        .map(c -> new OrderDetailBookResponse(br, c.getQuantity())))
                .toList();

        if (orderDetailBookRes.isEmpty()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Product in cart is empty");
        }


        var order = new Order();
        order.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        order.setStatus("");
        order.setDateCreated(LocalDateTime.now());
        System.out.println(order.getDateCreated());
        order.setLastUpdated(null);
        order.setUser(user);
        order.setPayType(orderRequestDto.getPayType());
        order.setPay(false);
        order.setShipName("");
        order.setShipNote("");
        order.setShipPhone("");
        order.setDisplayAddress("");
        order.setTotalPrice(Math.round((calculateTotalPrice(orderDetailBookRes) / 1000) * 1000));
        order.setOrdersDetails(new ArrayList<>());

        for (OrderDetailBookResponse item : orderDetailBookRes) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setProduct(modelMapper.map(item.getProductDto(), Product.class));
            orderDetail.setPrice(item.getProductDto().getUnitPrice() * item.getQuantity());

            order.ordersDetails.add(orderDetail);
        }
        return order;
    }

    @Override
    public void createOrder(Order order, List<Long> checkedCartId) {
        orderRepository.save(order);
        order.ordersDetails.forEach(orderDetail -> {
            var product = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", orderDetail.getProduct().getId()));

            if (orderDetail.getQuantity() <= product.getQuantity()) {
                product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
                product.setSold(product.getSold() + orderDetail.getQuantity());
            } else {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Product's quantity is not enough");
            }
        });
        cartService.removeBatchCart(checkedCartId);
    }

    @Override
    public List<OrderResponseDto> getOrders(User user) {
        var orders = orderRepository.findByUserId(user.getId());
        var transactions = transactionRepository.findAll();

        List<OrderResponseDto> result = orders.stream()
                .map(order -> {
                    Optional<Transaction> transaction = transactions.stream()
                            .filter(t -> t.getOrder().getId().equals(order.getId()))
                            .findFirst();

                    var orderRes = new OrderResponseDto();
                    orderRes.setId(order.getId());
                    orderRes.setDateCreated(order.getDateCreated());
                    orderRes.setShipName(order.getShipName());
                    orderRes.setShipPhone(order.getShipPhone());
                    orderRes.setShipNote(order.getShipNote());
                    orderRes.setDisplayAddress(order.getDisplayAddress());
                    orderRes.setTotalPrice(order.getTotalPrice());
                    orderRes.setStatus(order.getStatus());
                    orderRes.setDateOfPayment(transaction.map(Transaction::getResponseTime).orElse(null));
                    orderRes.setPay(order.isPay());
                    orderRes.setPayType(order.getPayType());

                    return orderRes;
                })
                .toList();
        return result;

    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        var orders = orderRepository.findAll();
        var transactions = transactionRepository.findAll();

        List<OrderResponseDto> result = orders.stream()
                .map(order -> {
                    Optional<Transaction> transaction = transactions.stream()
                            .filter(t -> t.getOrder().getId().equals(order.getId()))
                            .findFirst();

                    var orderRes = new OrderResponseDto();
                    orderRes.setId(order.getId());
                    orderRes.setDateCreated(order.getDateCreated());
                    orderRes.setShipName(order.getShipName());
                    orderRes.setShipPhone(order.getShipPhone());
                    orderRes.setShipNote(order.getShipNote());
                    orderRes.setDisplayAddress(order.getDisplayAddress());
                    orderRes.setTotalPrice(order.getTotalPrice());
                    orderRes.setStatus(order.getStatus());
                    orderRes.setDateOfPayment(transaction.map(Transaction::getResponseTime).orElse(null));
                    orderRes.setPay(order.isPay());
                    orderRes.setPayType(order.getPayType());

                    return orderRes;
                })
                .toList();
        return result;
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        var order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id));

        var transaction = transactionRepository.findFirstByOrderId(id);
        var orderResponse = modelMapper.map(order, OrderResponseDto.class);

        orderResponse.setDateOfPayment(transaction != null ? transaction.getResponseTime() : null);
        orderResponse.setOrderDetails(getOrderDetails(id));

        return orderResponse;
    }

    public Order getOrderEntityById(Long id) {
        var order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id));

        var orderDetails = orderDetailRepository.getOrderDetailByOrderId(id);
        order.setOrdersDetails(orderDetails);

        for (var item : order.getOrdersDetails()) {
            var product = productRepository.findProductById(item.getProduct().getId());
            item.setProduct(product);
        }

        return order;
    }

    private List<OrderDetailResponseDto> getOrderDetails(Long orderId) {
        var orderDetails = orderDetailRepository.getOrderDetailByOrderId(orderId);
        var products = productRepository.findAll();

        return orderDetails.stream()
                .map(o -> {
                    Product book = products.stream()
                            .filter(b -> b.getId().equals(o.getProduct().getId()))
                            .findFirst()
                            .orElse(null);

                    OrderDetailResponseDto orderDetailRes = new OrderDetailResponseDto();
                    orderDetailRes.setId(o.getId());
                    orderDetailRes.setBookId(o.getProduct().getId());
                    orderDetailRes.setName(book != null ? book.getName() : null);
                    orderDetailRes.setOrderId(o.getOrder().getId());
                    orderDetailRes.setQuantity(o.getQuantity());
                    orderDetailRes.setPrice(o.getPrice());

                    return orderDetailRes;
                })
                .collect(Collectors.toList());
    }


    private ProductDto mapToDTO(Product product) {
        //using ModelMapper
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        return productDto;
    }

    private double calculateTotalPrice(List<OrderDetailBookResponse> orderDetailBookRes) {
        double totalPrice = 0;
        for (OrderDetailBookResponse item : orderDetailBookRes) {
            totalPrice += item.getProductDto().getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    public Order updateOrderStatus(OrderStatusRequestDto request) {
        var order = getOrderEntityById(request.getOrderId());
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", request.getOrderId());
        }
        order.setStatus(request.getStatus());
        orderRepository.save(order);

        return order;
    }

//    public ReturnOrderResponseDto returnOrder(ReturnOrderRequestDto request){
//        var returnOrder = modelMapper.map(request, ReturnOrder.class);
//        returnOrder.setOrder(getOrderEntityById(request.getOrderId()));
//        returnOrder.getOrder().setStatus("Status test");
//
//
//
//    }
}
