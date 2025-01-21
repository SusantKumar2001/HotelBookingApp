package com.hmsapp.service;

import com.hmsapp.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Autowired
    private TwilioConfig twilioConfig;

    public String sendSms(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),                  // Recipient's number
                    new PhoneNumber(twilioConfig.getTwilioPhoneNumber()), // Twilio number
                    messageBody                                      // SMS body
            ).create();

            return "Message sent successfully, SID: " + message.getSid();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send message: " + e.getMessage();
        }
    }
}
