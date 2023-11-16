package com.ezd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezd.models.Calendar;
import com.ezd.repository.CalendarRepository;

@Service
public class CalendarService {
	
	@Autowired
	private final CalendarRepository calendarRepository;
	
	CalendarService(CalendarRepository calendarRepository){
		this.calendarRepository = calendarRepository;
	}
	
    // Tạo một sự kiện lịch
    public Calendar createCalendarEvent(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    // Lấy tất cả sự kiện lịch
    public List<Calendar> getAllCalendarEvents() {
        return calendarRepository.findAll();
    }

    // Lấy sự kiện lịch theo ID
    public Optional<Calendar> getCalendarEventById(Long id) {
        return calendarRepository.findById(id);
    }

    // Cập nhật thông tin sự kiện lịch
    public Calendar updateCalendarEvent(Long id, Calendar updatedCalendar) {
        if (calendarRepository.existsById(id)) {
            updatedCalendar.setId(id);
            return calendarRepository.save(updatedCalendar);
        }
        return null;
    }
    
    public void deleteCalendarEvent(Long id) {
        calendarRepository.deleteById(id);
    }
}
