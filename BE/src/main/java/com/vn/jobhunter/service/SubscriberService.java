package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Skill;
import com.vn.jobhunter.domain.Subscriber;
import com.vn.jobhunter.repository.SkillRepository;
import com.vn.jobhunter.repository.SubscriberRepository;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }

    public Subscriber handleCreate(Subscriber subscriber) throws InvalidException {
        //check already existed
        if (this.findByEmail(subscriber.getEmail()) != null)
            throw new InvalidException("Email already subscribed!");

        List<Skill> skills = subscriber.getSkills().stream()
                .filter(skill -> this.skillRepository.findById(skill.getId()).isPresent())
                .collect(Collectors.toList());
        subscriber.setSkills(skills);

        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdate(Subscriber subscriber) throws InvalidException {

        if (this.subscriberRepository.findById(subscriber.getId()).isEmpty())
            throw new InvalidException("Subscriber does not exist!");

        List<Skill> skills = subscriber.getSkills().stream()
                .filter(skill -> this.skillRepository.findById(skill.getId()).isPresent())
                .collect(Collectors.toList());

        Subscriber subscriberInDB = this.subscriberRepository.findById(subscriber.getId()).get();

        subscriberInDB.setSkills(skills);

        return this.subscriberRepository.save(subscriberInDB);

    }
}
