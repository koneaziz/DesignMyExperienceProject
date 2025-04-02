package com.aa.designmyexperience.Models;

import java.time.LocalDate;
import java.util.Date;

public class Event {

    /* Event Fields */
    private int eventId;
    private int eventOwner;
    private String eventTitle;
    private String eventDescription;
    private LocalDate eventDate;
    private LocalDate eventEndDate;
    private String eventLocation;
    private double eventPrice;
    private String eventCategory;
    private int eventMaxParticipants;
    private int eventRegisteredParticipants;
    private double eventDiscount;
    private String eventImage;

    /*  Constructors */
    public Event() {

    }
    public Event (int eventOwner, String eventTitle, String eventDescription, LocalDate eventDate, LocalDate eventEndDate, String eventLocation, double eventPrice, String eventCategory, int eventMaxParticipants, int eventRegisteredParticipants, double eventDiscount, String eventImage) {
        this.eventOwner = eventOwner;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventEndDate = eventEndDate;
        this.eventLocation = eventLocation;
        this.eventPrice = eventPrice;
        this.eventCategory = eventCategory;
        this.eventMaxParticipants = eventMaxParticipants;
        this.eventRegisteredParticipants = eventRegisteredParticipants;
        this.eventDiscount = eventDiscount;
        this.eventImage = eventImage;
    }
    public Event (int eventOwner, String eventTitle, String eventDescription, LocalDate eventDate, LocalDate eventEndDate, String eventLocation, double eventPrice, String eventCategory, int eventMaxParticipants, int eventRegisteredParticipants, double eventDiscount) {
        this.eventOwner = eventOwner;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventEndDate = eventEndDate;
        this.eventLocation = eventLocation;
        this.eventPrice = eventPrice;
        this.eventCategory = eventCategory;
        this.eventMaxParticipants = eventMaxParticipants;
        this.eventRegisteredParticipants = eventRegisteredParticipants;
        this.eventDiscount = eventDiscount;
    }

    public Event (int eventId, int eventOwner, String eventTitle, String eventDescription, LocalDate eventDate, LocalDate eventEndDate, String eventLocation, double eventPrice, String eventCategory, int eventMaxParticipants, int eventRegisteredParticipants, double eventDiscount, String eventImage) {
        this.eventId = eventId;
        this.eventOwner = eventOwner;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventEndDate = eventEndDate;
        this.eventLocation = eventLocation;
        this.eventPrice = eventPrice;
        this.eventCategory = eventCategory;
        this.eventMaxParticipants = eventMaxParticipants;
        this.eventRegisteredParticipants = eventRegisteredParticipants;
        this.eventDiscount = eventDiscount;
        this.eventImage = eventImage;
    }

    /* Getters & Setters */
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(int eventOwner) {
        this.eventOwner = eventOwner;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public LocalDate getEventEndDate() {
        return eventEndDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventEndDate(LocalDate eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public int getEventMaxParticipants() {
        return eventMaxParticipants;
    }

    public void setEventMaxParticipants(int eventMaxParticipants) {
        this.eventMaxParticipants = eventMaxParticipants;
    }

    public int getEventRegisteredParticipants() {
        return eventRegisteredParticipants;
    }

    public void setEventRegisteredParticipants(int eventRegisteredParticipants) {
        this.eventRegisteredParticipants = eventRegisteredParticipants;
    }

    public double getEventDiscount() {
        return eventDiscount;
    }

    public void setEventDiscount(double eventDiscount) {
        this.eventDiscount = eventDiscount;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }





}
