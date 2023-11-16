package com.ezd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.models.Calendar;
import com.ezd.service.CalendarService;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {
	
	@Autowired
	private final CalendarService calendarService;
	
    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    
    // Tạo sự kiện lịch
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Calendar> createCalendarEvent(@ModelAttribute Calendar calendar) {
        Calendar createdEvent = calendarService.createCalendarEvent(calendar);
        return ResponseEntity.ok(createdEvent);
    }

    // Lấy tất cả sự kiện lịch
    @GetMapping("/")
    public ResponseEntity<List<Calendar>> getAllCalendarEvents() {
        List<Calendar> events = calendarService.getAllCalendarEvents();
        return ResponseEntity.ok(events);
    }

    // Lấy sự kiện lịch theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getCalendarEventById(@PathVariable Long id) {
        return calendarService.getCalendarEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin sự kiện lịch
    @PutMapping("/edit/{id}")
    public ResponseEntity<Calendar> updateCalendarEvent(@PathVariable Long id, @ModelAttribute Calendar updatedCalendar) {
        Calendar updatedEvent = calendarService.updateCalendarEvent(id, updatedCalendar);
        if (updatedEvent != null) {
            return ResponseEntity.ok(updatedEvent);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa sự kiện lịch
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCalendarEvent(@PathVariable Long id) {
        calendarService.deleteCalendarEvent(id);
        return ResponseEntity.noContent().build();
    }
}
