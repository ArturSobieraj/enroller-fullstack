package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ParticipantService(@Qualifier("BCryptEncoder") PasswordEncoder passwordEncoder) {
        connector = DatabaseConnector.getInstance();
        this.passwordEncoder = passwordEncoder;
    }

    public Collection<Participant> getAll() {
        return connector.getSession().createCriteria(Participant.class).list();
    }

    public Participant findByLogin(String login) {
        return (Participant) connector.getSession().get(Participant.class, login);
    }

    public Participant add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        participant.setPassword(passwordEncoder.encode(participant.getPassword()));
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        participant.setPassword(passwordEncoder.encode(participant.getPassword()));
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

}
