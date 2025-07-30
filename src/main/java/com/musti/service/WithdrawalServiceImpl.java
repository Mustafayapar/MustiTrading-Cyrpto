package com.musti.service;

import com.musti.domain.WithdrawalStatus;
import com.musti.modal.Users;
import com.musti.modal.Withdrawal;
import com.musti.repository.IWithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements IWithdrawalService {


    @Autowired
    private IWithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, Users user) {

        Withdrawal withdrawal= new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception {

        Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(withdrawalId);

        if (withdrawalOptional.isEmpty()) {
            throw new Exception("withdrawal not found");
        }

        Withdrawal withdrawal = withdrawalOptional.get();
        withdrawal.setDate(LocalDateTime.now());

        if (accept){
            withdrawal.setStatus(WithdrawalStatus.SUCCESS);
        }else {
            withdrawal.setStatus(WithdrawalStatus.PENDING);
        }


        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(Users user) {


        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
