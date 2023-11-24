package com.ezd.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezd.Dto.Role;
import com.ezd.Dto.Status;
import com.ezd.models.Auth;
import com.ezd.models.BecomeIdol;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.BecomeRepository;

@Service
public class BecomeService {
	   @Autowired
	    private BecomeRepository becomeRepository;

	    @Autowired
	    private AuthRepository authRepository;
	    
	    @Transactional
	    public   void saveBecomeForm(BecomeIdol becomeIdol) {
	        Auth user = becomeIdol.getUser_become(); // Lấy thông tin người dùng từ transaction

	        if (user != null) {
	            // Liên kết thông tin giao dịch với người dùng
	        	becomeIdol.setUser_become(user);
	        	becomeRepository.save(becomeIdol);
	        }
	     
	    }
	    public void adminCheckBecome(Long id, Status newStatus) {
	        BecomeIdol becomeIdol = becomeRepository.findById(id).orElse(null);

	        if (becomeIdol != null && !becomeIdol.isCheckedByAdmin()) {
	            // Kiểm tra trạng thái mới
	            if (newStatus == Status.SUCCESS || newStatus == Status.FAILED) {
	                // Cập nhật trạng thái giao dịch
	            	becomeIdol.setStatus(newStatus);
	            	becomeIdol.setCheckedByAdmin(true);
	            	becomeIdol.setAdminCheckTime(LocalDateTime.now());
	            	becomeRepository.save(becomeIdol);

	                // Nếu là "SUCCESS", cộng số tiền vào tài khoản của người dùng
	                if (newStatus == Status.SUCCESS) {
	                    Auth user = becomeIdol.getUser_become();

	                    user.setRole(Role.STAF);
	                    // Lưu thông tin người dùng sau khi cập nhật số tiền
	                    authRepository.save(user);
	                }
	            }
	        }
	    
	}

	    public List<BecomeIdol> getBecomesByStatus(Status status) {
	        return becomeRepository.findByStatus(status);
	    }


}
