package com.vn.jobhunter.repository;

import com.vn.jobhunter.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    public Subscriber findByEmail(String email);
}
