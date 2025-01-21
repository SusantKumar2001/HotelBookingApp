package com.hmsapp.service;

import com.hmsapp.entity.Booking;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.RoomsAvailability;
import com.hmsapp.repository.BookingRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.RoomsAvailabilityRepository;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomsAvailabilityRepository roomsRepo;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public ResponseEntity<?> searchRooms(LocalDate fromDate, LocalDate toDate, String roomType, Long propertyId, Booking booking) throws DocumentException, IOException, MessagingException {
         List<RoomsAvailability> rooms =  roomsRepo.findAvailableRooms(fromDate,toDate,roomType,propertyId);
         Property p = propertyRepository.findById(propertyId).orElseThrow(()->new RuntimeException("Could not find"));
         if(rooms.isEmpty()){
             return new ResponseEntity<>("No rooms available", HttpStatus.BAD_REQUEST);
         }
         double price = 0;
         for(RoomsAvailability r:rooms){
             if(r.getTotalRooms()==0){
                 return new ResponseEntity<>("No rooms available", HttpStatus.BAD_REQUEST);
             }
             r.setTotalRooms(r.getTotalRooms()-1);
             price = r.getNightlyPrice();

         }
         long totalNights = ChronoUnit.DAYS.between(fromDate,toDate);
         double total_price = (price*totalNights)+(price*totalNights)*18/100;
         booking.setProperty(p);
         Booking savedBooking = bookingRepository.save(booking);
         pdfService.generateBookingPdf("E:\\pdf_booking\\bookings"+"_"+savedBooking.getId()+".pdf",savedBooking,fromDate,toDate,roomType,totalNights,total_price);
         emailService.sendEmailWithAttachment(booking.getEmail(),"Booking Details","Thanks for booking😊","E:\\pdf_booking\\bookings"+"_"+savedBooking.getId()+".pdf");
         smsService.sendSms("+91"+savedBooking.getMobile(),"Your room has been booked successfully(●'◡'●)");
         return new ResponseEntity<>(rooms,HttpStatus.OK);
    }

}
