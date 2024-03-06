package com.seclore.main.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seclore.main.domain.BookingDetails;
import com.seclore.main.domain.RoomDetails;
import com.seclore.main.domain.UserDetails;
import com.seclore.main.repository.BookingDetailsRepositoryInterface;
import com.seclore.main.repository.BookingSlotsRepositoryInterface;

@Service
public class BookingDetailsService implements BookingDetailsServiceInterface {
	@Autowired
	private BookingDetailsRepositoryInterface bookingDetailsRepository;
	
	@Autowired
	private BookingSlotsRepositoryInterface bookingSlotsRepository;
	
	@Override
	public BookingDetails addBookingDetails(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
			int userId, int roomId, String description) {
		BookingDetails bookingDetails = new BookingDetails();
		bookingDetails.setRoom(new RoomDetails());
		bookingDetails.getRoom().setRoomId(roomId);
		bookingDetails.setUser(new UserDetails());
		bookingDetails.getUser().setUserId(userId);
		bookingDetails.setDescription(description);
		bookingDetails.setStatus("BOOKED");
		
		do {
			int bookingId = bookingDetailsRepository.addBookingDetails(bookingDetails);
			bookingDetails.setBookingId(bookingId);
			bookingSlotsRepository.addNewBookingSlots(bookingDetails, startTime, endTime, startDate);
			startDate.plusDays(1);
		} while(!startDate.isAfter(endDate));
	}

	@Override
	public BookingDetails updateExistingBookingDetails(BookingDetails bookingDetails) {
		return bookingDetailsRepository.updateExistingBookingDetails(bookingDetails);
	}

	@Override
	public boolean cancelExistingBookingDetails(BookingDetails bookingDetails) {
		
		bookingDetails.setStatus("CANCELED");
		
		bookingDetails = bookingDetailsRepository.updateExistingBookingDetails(bookingDetails);
		
//		bookingSlotsRepository.deleteBookingSlots(bookingDetails.getBookingId(), startTime, endTime, startDate);
		return bookingDetails!=null;
		
	}

	@Override
	public BookingDetails approveBookingDetails(BookingDetails bookingDetails) {
		
		bookingDetails.setStatus("BOOKED");
		return bookingDetailsRepository.updateExistingBookingDetails(bookingDetails);
	}

	@Override
	public BookingDetails getExistingBookingDetails(int bookingID) {
		return bookingDetailsRepository.getExistingBookingDetailsbyBookigID(bookingID);
	}

	@Override
	public List<BookingDetails> getAllExistingBookingDetailsByUserId(int userId) {
		// TODO Auto-generated method stub
		return bookingDetailsRepository.getExistingBookingDetailsbyuserid(userId);
		
	}

	@Override
	public List<BookingDetails> getAllExistingBookingDetailsByadmin() {
		// TODO Auto-generated method stub
		return bookingDetailsRepository.getAllExistingBookingDetailsByadmin();
		
	}

	@Override
	@Transactional
	public boolean cancelPartialBooking(BookingDetails bookingDetails, LocalTime startTime, LocalTime endTime,
			LocalTime newStartTime, LocalTime newEndTime, LocalDate date, String action) {
		if(action.equals("SHRINK")) {
			bookingSlotsRepository.deleteBookingSlots(bookingDetails.getBookingId(), startTime, newStartTime, date);
			bookingSlotsRepository.deleteBookingSlots(bookingDetails.getBookingId(), newEndTime, endTime, date);
		}
		if(action.equals("SPLIT")) {
			cancelExistingBookingDetails(bookingDetails);
			
			int bookingId = bookingDetailsRepository.addBookingDetails(bookingDetails);
			bookingDetails.setBookingId(bookingId);
			bookingSlotsRepository.addNewBookingSlots(bookingDetails, startTime, newStartTime, date);
			
			bookingId = bookingDetailsRepository.addBookingDetails(bookingDetails);
			bookingDetails.setBookingId(bookingId);
			bookingSlotsRepository.addNewBookingSlots(bookingDetails, startTime, endTime, date);			
		}
		return true;
	}
}
