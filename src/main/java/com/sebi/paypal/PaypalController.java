package com.sebi.paypal;

import com.google.gson.JsonObject;
import com.paypal.base.rest.APIContext;
import com.sebi.exception.OrderException;
import com.sebi.model.Order;
import com.sebi.repository.OrderRepository;
import com.sebi.response.ApiResponse;
import com.sebi.service.OrderService;
import com.sebi.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@RestController
@RequestMapping("/api")
public class PaypalController {

    @Autowired
    PaypalService service;
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;
    private String paymentId;
    private Boolean ok=true;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
                                                                 @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order =orderService.findOrderById(orderId);


        try {
            String currency="USD";
            String method="paypal";
            String intent="sale";
            String desc="desc";
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",order.getTotalPrice()*100);
            paymentLinkRequest.put("currency","USD");

            JSONObject customer = new JSONObject();
            customer.put("name",order.getUser().getFirstName());
//            customer.put("email","sb-dffs627313504@personal.example.com");
            customer.put("email","sb-dffs627313504@personal.example.com");
            paymentLinkRequest.put("customer",customer);

            JSONObject notify = new JSONObject();
            notify.put("sms",true);
            notify.put("email",true);

            paymentLinkRequest.put("notify",notify);
            paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId);
            paymentLinkRequest.put("callback_method","get");

            Payment payment = service.createPayment(order.getTotalPrice(), currency, method,
                   intent, desc, "http://localhost:3000",
                    "http://localhost:3000/payment/"+orderId);
            this.paymentId=payment.getId();
            String id= payment.getId();
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    String url=link.getHref();
                    PaymentLinkResponse res = new PaymentLinkResponse();
                    res.setPayment_link_id(id);
                    res.setPayment_link_url(url);

                    return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.CREATED);
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }

        return null;

    }
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect (@RequestParam(name="paymentId") String paymentId,@RequestParam (name="orderId") Long orderId) throws OrderException {
        Order order = orderService.findOrderById(orderId);
        if(this.ok){
            order.getPaymentDetails().setPaymentId(this.paymentId);
            order.getPaymentDetails().setStatus("COMPLETED");
            order.setOrderStatus("PLACED");
            orderRepository.save(order);
        }
        ApiResponse response = new ApiResponse();
        response.setMessage("your order get placed");
        response.setStatus(true);

        return new ResponseEntity<ApiResponse>(response,HttpStatus.ACCEPTED);

    }



}
