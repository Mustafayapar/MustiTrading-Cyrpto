package com.musti.service;

import com.musti.domain.PaymentMethod;
import com.musti.domain.PaymentOrderStatus;
import com.musti.modal.PaymentOrder;
import com.musti.modal.Users;
import com.musti.repository.IPaymentOrderRepository;
import com.musti.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IPaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createPaymentOrder(Users user, Long amount, PaymentMethod paymentMethod) {

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setUser(user);


        return  paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(()->new Exception("Payment order not found"));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){

                RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);

                Integer amount =payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }


        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLing(Users user, Long amount) throws RazorpayException {

        Long Amount = amount*100;
        try{
            //Instantiate a Razorpay client with your key Id and secret key
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecretKey);

            //Create JSON object with the paymeent link request parameters
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", Amount);
            paymentLinkRequest.put("currency", "USD");

            //Create a JSON object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            //Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);


            //Set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);

            // Set the callback URL and method
            paymentLinkRequest.put("callback_url", "https://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method", "get");

            //create the payment limk using the paymentlink.create() method,
            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("url");

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPayment_url(paymentLinkUrl);

            return paymentResponse;

        }catch (RazorpayException e){

            System.out.println("Error creating payment link"+e.getMessage());
            throw new RazorpayException(e.getMessage());
        }

    }

    @Override
    public PaymentResponse createStripePaymentLing(Users user, Long amount,Long orderId) throws StripeException {

        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5173/payment/cansel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount(amount*100)
                .setProductData(SessionCreateParams.LineItem
                        .PriceData.ProductData.builder()
                        .setName("Top up wallet")
                        .build()
                ).build()
                ).build()
                ).build();

        Session session =Session.create(params);
        System.out.println("Session created"+ session);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment_url(session.getUrl());



        return paymentResponse;
    }
}
