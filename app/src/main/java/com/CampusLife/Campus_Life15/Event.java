package com.CampusLife.Campus_Life15;

/**
 * Created by rbank on 8/23/2016.
 */
public class Event {

    private String m_startDate;
    private String m_endDate;
    private String m_stampDate;
    private String m_UID;
    private String m_description;
    private String m_lastModified;
    private String m_location;
    private String m_sequence;
    private String m_status;
    private String m_summary;
    private String m_transparent;
    public Event(){

    }
    public String getStartDate() {
        return m_startDate;
    }

    public void setStartDate(String startDate) {
        m_startDate = startDate;
    }

    public String getEndDate() {
        return m_endDate;
    }

    public void setEndDate(String endDate) {
        m_endDate = endDate;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public String getLocation() {
        return m_location;
    }

    public void setLocation(String location) {
        m_location = location;
    }

    public String getSummary() {
        return m_summary;
    }

    public void setSummary(String summary) {
        m_summary = summary;
    }


}
