package com.hmsapp.controller;
import com.hmsapp.entity.Booking;
import com.hmsapp.service.RoomService;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private RoomService roomService;
    @GetMapping("/search/rooms")
    public ResponseEntity<?> searchRooms(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, @RequestParam String roomType, @RequestParam long propertyId,@RequestBody Booking booking) throws DocumentException, IOException, MessagingException {
        return roomService.searchRooms(fromDate,toDate,roomType,propertyId,booking);
    }
}
