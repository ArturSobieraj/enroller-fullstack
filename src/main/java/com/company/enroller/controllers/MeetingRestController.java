package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveNewMeeting(@RequestBody Meeting meeting) {
        meetingService.addMeeting(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable String id) {
        meetingService.deleteMeeting(Long.parseLong(id));
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping("/add/{login}/{meetingId}")
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable String login, @PathVariable String meetingId) {
        Participant participant = participantService.findByLogin(login);
        Meeting meeting = meetingService.getMeetingById(Long.parseLong(meetingId));
        meeting.addParticipant(participant);
        meetingService.updateMeeting(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/remove/{login}/{meetingId}")
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable String login, @PathVariable String meetingId) {
        Participant participant = participantService.findByLogin(login);
        Meeting meeting = meetingService.getMeetingById(Long.parseLong(meetingId));
        meeting.removeParticipant(participant);
        meetingService.updateMeeting(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
