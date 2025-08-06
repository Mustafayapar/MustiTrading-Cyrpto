package com.musti.service;

import com.musti.domain.PaymentMethod;
import com.musti.modal.PaymentOrder;
import com.musti.modal.Users;
import com.musti.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface IPaymentService {

    PaymentOrder createPaymentOrder(Users user, Long amount,
                                    PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLing(Users user, Long amount,Long orderId) throws RazorpayException;

    PaymentResponse createStripePaymentLing(Users user, Long amount,Long orderId) throws RazorpayException, StripeException;




}
