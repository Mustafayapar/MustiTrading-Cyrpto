package com.musti.service;

import com.musti.modal.Users;
import com.musti.modal.Withdrawal;

import java.util.List;

public interface IWithdrawalService {

    Withdrawal requestWithdrawal(Long amount, Users user);

    Withdrawal procedWithdrawal(Long withdrawalId, boolean accept ) throws Exception;

    List<Withdrawal> getUsersWithdrawalHistory(Users user);

    List<Withdrawal> getWithdrawalRequest();

}
