package com.vn.jobhunter.controller;

import com.vn.jobhunter.domain.Subscriber;
import com.vn.jobhunter.service.SubscriberService;
import com.vn.jobhunter.util.SecurityUtil;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers/skills")
    public ResponseEntity<Subscriber> getAllSkillSubscriber() {
        String email = SecurityUtil.getCurrentUserLogin();
        return ResponseEntity.ok(this.subscriberService.findByEmail(email));
    }

    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> createSubscriber(@RequestBody @Valid Subscriber subscriber) throws InvalidException {
        Subscriber createdSubcriber = this.subscriberService.handleCreate(subscriber);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcriber);
    }

    @PutMapping("/subscribers")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody @Valid Subscriber subscriber) throws InvalidException {
        Subscriber updatedSubcriber = this.subscriberService.handleUpdate(subscriber);

        return ResponseEntity.ok(updatedSubcriber);
    }
}
