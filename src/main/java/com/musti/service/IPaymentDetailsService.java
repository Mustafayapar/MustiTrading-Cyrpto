package com.musti.service;

import com.musti.modal.PaymentDetails;
import com.musti.modal.Users;

public interface IPaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            Users user
                                            );
    public PaymentDetails getUsersPaymentDetails(Users user);

}
