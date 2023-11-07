package com.ezd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.Dto.Status;
import com.ezd.models.BecomeIdol;

public interface BecomeRepository  extends JpaRepository< BecomeIdol, Long> {
    List<BecomeIdol> findByStatus(Status status);

}
