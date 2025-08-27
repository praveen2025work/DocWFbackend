package com.docwf.service.impl;

import com.docwf.dto.WorkflowCalendarDto;
import com.docwf.dto.WorkflowCalendarDayDto;
import com.docwf.dto.CreateCalendarWithDaysDto;
import com.docwf.entity.WorkflowCalendar;
import com.docwf.entity.WorkflowCalendarDay;
import com.docwf.exception.WorkflowException;
import com.docwf.repository.WorkflowCalendarRepository;
import com.docwf.repository.WorkflowCalendarDayRepository;
import com.docwf.service.WorkflowCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkflowCalendarServiceImpl implements WorkflowCalendarService {
    
    @Autowired
    private WorkflowCalendarRepository calendarRepository;
    
    @Autowired
    private WorkflowCalendarDayRepository calendarDayRepository;
    
    @Override
    public WorkflowCalendarDto createCalendar(WorkflowCalendarDto calendarDto) {
        WorkflowCalendar calendar = new WorkflowCalendar(
            calendarDto.getCalendarName(),
            calendarDto.getDescription(),
            calendarDto.getStartDate(),
            calendarDto.getEndDate(),
            calendarDto.getRecurrence(),
            calendarDto.getCreatedBy()
        );
        
        WorkflowCalendar savedCalendar = calendarRepository.save(calendar);
        return convertToDto(savedCalendar);
    }
    
    @Override
    public WorkflowCalendarDto createCalendarWithDays(CreateCalendarWithDaysDto calendarWithDaysDto) {
        // Create the calendar first
        WorkflowCalendar calendar = new WorkflowCalendar(
            calendarWithDaysDto.getCalendarName(),
            calendarWithDaysDto.getDescription(),
            calendarWithDaysDto.getStartDate(),
            calendarWithDaysDto.getEndDate(),
            calendarWithDaysDto.getRecurrence(),
            calendarWithDaysDto.getCreatedBy()
        );
        
        // Save the calendar to get the ID
        WorkflowCalendar savedCalendar = calendarRepository.save(calendar);
        
        // Create and save all calendar days
        List<WorkflowCalendarDay> calendarDays = calendarWithDaysDto.getCalendarDays().stream()
            .map(dayInput -> new WorkflowCalendarDay(
                savedCalendar,
                dayInput.getDayDate(),
                dayInput.getDayType(),
                dayInput.getNote()
            ))
            .collect(Collectors.toList());
        
        // Save all calendar days
        List<WorkflowCalendarDay> savedDays = calendarDayRepository.saveAll(calendarDays);
        
        // Add the days to the calendar entity
        savedCalendar.setCalendarDays(savedDays);
        
        return convertToDto(savedCalendar);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowCalendarDto> getCalendarById(Long calendarId) {
        return calendarRepository.findById(calendarId).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowCalendarDto> getCalendarByName(String calendarName) {
        return calendarRepository.findByCalendarName(calendarName).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDto> getAllCalendars() {
        return calendarRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowCalendarDto> getAllCalendars(String recurrence, Pageable pageable) {
        Page<WorkflowCalendar> calendars;
        if (recurrence != null && !recurrence.isEmpty()) {
            calendars = calendarRepository.findByRecurrence(recurrence, pageable);
        } else {
            calendars = calendarRepository.findAll(pageable);
        }
        return calendars.map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowCalendarDto> searchCalendars(String calendarName, String description, String recurrence, 
                                                    String createdBy, String startDate, String endDate, 
                                                    String createdAfter, String createdBefore, Pageable pageable) {
        // Implement proper search logic with dynamic query building
        if (calendarName == null && description == null && recurrence == null && createdBy == null && 
            startDate == null && endDate == null && createdAfter == null && createdBefore == null) {
            // No search criteria, return all calendars with pagination
            return getAllCalendars(recurrence, pageable);
        }
        
        // Build dynamic search criteria
        List<WorkflowCalendar> filteredCalendars = calendarRepository.findAll().stream()
                .filter(calendar -> calendarName == null || (calendar.getCalendarName() != null && 
                        calendar.getCalendarName().toLowerCase().contains(calendarName.toLowerCase())))
                .filter(calendar -> description == null || (calendar.getDescription() != null && 
                        calendar.getDescription().toLowerCase().contains(description.toLowerCase())))
                .filter(calendar -> recurrence == null || (calendar.getRecurrence() != null && 
                        calendar.getRecurrence().equals(recurrence)))
                .filter(calendar -> createdBy == null || (calendar.getCreatedBy() != null && 
                        calendar.getCreatedBy().toLowerCase().contains(createdBy.toLowerCase())))
                .filter(calendar -> startDate == null || (calendar.getStartDate() != null && 
                        calendar.getStartDate().isAfter(LocalDate.parse(startDate))))
                .filter(calendar -> endDate == null || (calendar.getEndDate() != null && 
                        calendar.getEndDate().isBefore(LocalDate.parse(endDate))))
                .filter(calendar -> createdAfter == null || (calendar.getCreatedAt() != null && 
                        calendar.getCreatedAt().isAfter(LocalDateTime.parse(createdAfter))))
                .filter(calendar -> createdBefore == null || (calendar.getCreatedAt() != null && 
                        calendar.getCreatedAt().isBefore(LocalDateTime.parse(createdBefore))))
                .collect(Collectors.toList());
        
        // Apply pagination manually since we're filtering in memory
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredCalendars.size());
        
        if (start > filteredCalendars.size()) {
            return Page.empty(pageable);
        }
        
        List<WorkflowCalendarDto> pageContent = filteredCalendars.subList(start, end)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, filteredCalendars.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDto> getCalendarsByRecurrence(String recurrence) {
        return calendarRepository.findByRecurrence(recurrence).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDto> getCalendarsByDateRange(LocalDate startDate, LocalDate endDate) {
        return calendarRepository.findCalendarsInDateRange(startDate, endDate).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowCalendarDto updateCalendar(Long calendarId, WorkflowCalendarDto calendarDto) {
        WorkflowCalendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new WorkflowException("Calendar not found with ID: " + calendarId));
        
        calendar.setCalendarName(calendarDto.getCalendarName());
        calendar.setDescription(calendarDto.getDescription());
        calendar.setStartDate(calendarDto.getStartDate());
        calendar.setEndDate(calendarDto.getEndDate());
        calendar.setRecurrence(calendarDto.getRecurrence());
        calendar.setUpdatedBy(calendarDto.getUpdatedBy());
        
        WorkflowCalendar updatedCalendar = calendarRepository.save(calendar);
        return convertToDto(updatedCalendar);
    }
    
    @Override
    public void deleteCalendar(Long calendarId) {
        if (!calendarRepository.existsById(calendarId)) {
            throw new WorkflowException("Calendar not found with ID: " + calendarId);
        }
        calendarRepository.deleteById(calendarId);
    }
    
    @Override
    public WorkflowCalendarDayDto addCalendarDay(Long calendarId, WorkflowCalendarDayDto dayDto) {
        WorkflowCalendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new WorkflowException("Calendar not found with ID: " + calendarId));
        
        WorkflowCalendarDay calendarDay = new WorkflowCalendarDay(
            calendar,
            dayDto.getDayDate(),
            dayDto.getDayType(),
            dayDto.getNote()
        );
        
        WorkflowCalendarDay savedDay = calendarDayRepository.save(calendarDay);
        return convertToDayDto(savedDay);
    }
    
    @Override
    public List<WorkflowCalendarDayDto> addCalendarDays(Long calendarId, List<WorkflowCalendarDayDto> daysDto) {
        WorkflowCalendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new WorkflowException("Calendar not found with ID: " + calendarId));
        
        List<WorkflowCalendarDay> calendarDays = daysDto.stream()
            .map(dayDto -> new WorkflowCalendarDay(
                calendar,
                dayDto.getDayDate(),
                dayDto.getDayType(),
                dayDto.getNote()
            ))
            .collect(Collectors.toList());
        
        List<WorkflowCalendarDay> savedDays = calendarDayRepository.saveAll(calendarDays);
        return savedDays.stream()
            .map(this::convertToDayDto)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDayDto> getCalendarDays(Long calendarId) {
        return calendarDayRepository.findByCalendarCalendarId(calendarId).stream()
            .map(this::convertToDayDto)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDayDto> getCalendarDaysByType(Long calendarId, String dayType) {
        return calendarDayRepository.findByCalendarCalendarIdAndDayType(calendarId, dayType).stream()
            .map(this::convertToDayDto)
            .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowCalendarDayDto updateCalendarDay(Long dayId, WorkflowCalendarDayDto dayDto) {
        WorkflowCalendarDay calendarDay = calendarDayRepository.findById(dayId)
            .orElseThrow(() -> new WorkflowException("Calendar day not found with ID: " + dayId));
        
        calendarDay.setDayDate(dayDto.getDayDate());
        calendarDay.setDayType(dayDto.getDayType());
        calendarDay.setNote(dayDto.getNote());
        
        WorkflowCalendarDay updatedDay = calendarDayRepository.save(calendarDay);
        return convertToDayDto(updatedDay);
    }
    
    @Override
    public void deleteCalendarDay(Long dayId) {
        if (!calendarDayRepository.existsById(dayId)) {
            throw new WorkflowException("Calendar day not found with ID: " + dayId);
        }
        calendarDayRepository.deleteById(dayId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isDateValid(Long calendarId, LocalDate date) {
        Optional<WorkflowCalendar> calendarOpt = calendarRepository.findById(calendarId);
        if (calendarOpt.isEmpty()) {
            return false;
        }
        
        WorkflowCalendar calendar = calendarOpt.get();
        return calendar.isDateValid(date);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getValidDates(Long calendarId, LocalDate startDate, LocalDate endDate) {
        Optional<WorkflowCalendar> calendarOpt = calendarRepository.findById(calendarId);
        if (calendarOpt.isEmpty()) {
            return List.of();
        }
        
        WorkflowCalendar calendar = calendarOpt.get();
        return startDate.datesUntil(endDate.plusDays(1))
            .filter(calendar::isDateValid)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getHolidays(Long calendarId, LocalDate startDate, LocalDate endDate) {
        return calendarDayRepository.findByCalendarAndDateRange(calendarId, startDate, endDate).stream()
            .filter(day -> "HOLIDAY".equals(day.getDayType()))
            .map(WorkflowCalendarDay::getDayDate)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getRunDays(Long calendarId, LocalDate startDate, LocalDate endDate) {
        return calendarDayRepository.findByCalendarAndDateRange(calendarId, startDate, endDate).stream()
            .filter(day -> "RUNDAY".equals(day.getDayType()))
            .map(WorkflowCalendarDay::getDayDate)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canExecuteWorkflow(Long calendarId, LocalDate date) {
        Optional<WorkflowCalendar> calendarOpt = calendarRepository.findById(calendarId);
        if (calendarOpt.isEmpty()) {
            return false;
        }
        
        WorkflowCalendar calendar = calendarOpt.get();
        return calendar.canExecuteWorkflow(date);
    }
    
    @Override
    @Transactional(readOnly = true)
    public LocalDate getNextValidDate(Long calendarId, LocalDate fromDate) {
        Optional<WorkflowCalendar> calendarOpt = calendarRepository.findById(calendarId);
        if (calendarOpt.isEmpty()) {
            return null;
        }
        
        WorkflowCalendar calendar = calendarOpt.get();
        LocalDate currentDate = fromDate.plusDays(1);
        
        while (currentDate.isBefore(calendar.getEndDate().plusDays(1))) {
            if (calendar.isDateValid(currentDate)) {
                return currentDate;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public LocalDate getPreviousValidDate(Long calendarId, LocalDate fromDate) {
        Optional<WorkflowCalendar> calendarOpt = calendarRepository.findById(calendarId);
        if (calendarOpt.isEmpty()) {
            return null;
        }
        
        WorkflowCalendar calendar = calendarOpt.get();
        LocalDate currentDate = fromDate.minusDays(1);
        
        while (currentDate.isAfter(calendar.getStartDate().minusDays(1))) {
            if (calendar.isDateValid(currentDate)) {
                return currentDate;
            }
            currentDate = currentDate.minusDays(1);
        }
        
        return null;
    }
    
    @Override
    public List<WorkflowCalendarDayDto> addCalendarDaysBatch(Long calendarId, List<WorkflowCalendarDayDto> daysDto) {
        return daysDto.stream()
            .map(dayDto -> addCalendarDays(calendarId, List.of(dayDto)))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getValidDatesInRange(Long calendarId, LocalDate startDate, LocalDate endDate) {
        return getValidDates(calendarId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDayDto> getHolidaysInRange(Long calendarId, LocalDate startDate, LocalDate endDate) {
        return calendarDayRepository.findByCalendarAndDateRange(calendarId, startDate, endDate).stream()
            .filter(day -> "HOLIDAY".equals(day.getDayType()))
            .map(this::convertToDayDto)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowCalendarDayDto> getRunDaysInRange(Long calendarId, LocalDate startDate, LocalDate endDate) {
        return calendarDayRepository.findByCalendarAndDateRange(calendarId, startDate, endDate).stream()
            .filter(day -> "RUNDAY".equals(day.getDayType()))
            .map(this::convertToDayDto)
            .collect(Collectors.toList());
    }
    
    // Helper methods for conversion
    private WorkflowCalendarDto convertToDto(WorkflowCalendar calendar) {
        WorkflowCalendarDto dto = new WorkflowCalendarDto();
        dto.setCalendarId(calendar.getCalendarId());
        dto.setCalendarName(calendar.getCalendarName());
        dto.setDescription(calendar.getDescription());
        dto.setStartDate(calendar.getStartDate());
        dto.setEndDate(calendar.getEndDate());
        dto.setRecurrence(calendar.getRecurrence());
        dto.setCreatedBy(calendar.getCreatedBy());
        dto.setCreatedAt(calendar.getCreatedAt());
        dto.setUpdatedBy(calendar.getUpdatedBy());
        dto.setUpdatedAt(calendar.getUpdatedAt());
        
        // Convert calendar days
        List<WorkflowCalendarDayDto> dayDtos = calendar.getCalendarDays().stream()
            .map(this::convertToDayDto)
            .collect(Collectors.toList());
        dto.setCalendarDays(dayDtos);
        
        return dto;
    }
    
    private WorkflowCalendarDayDto convertToDayDto(WorkflowCalendarDay calendarDay) {
        WorkflowCalendarDayDto dto = new WorkflowCalendarDayDto();
        dto.setCalendarDayId(calendarDay.getCalendarDayId());
        dto.setCalendarId(calendarDay.getCalendar().getCalendarId());
        dto.setDayDate(calendarDay.getDayDate());
        dto.setDayType(calendarDay.getDayType());
        dto.setNote(calendarDay.getNote());
        return dto;
    }
}
