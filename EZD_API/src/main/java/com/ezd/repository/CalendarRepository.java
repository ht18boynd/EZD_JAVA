package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
