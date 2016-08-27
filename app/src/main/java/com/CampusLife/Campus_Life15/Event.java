package com.CampusLife.Campus_Life15;

/**
 * Created by rbank on 8/23/2016.
 */
public class Event {

    private String m_startDate = null;
    private String m_endDate = null;
    private String m_stampDate;
    private String m_UID;
    private String m_description = null;
    private String m_lastModified;
    private String m_location = null;
    private String m_sequence;
    private String m_status;
    private String m_summary = null;
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

            m_description = description.replaceAll("\\\\", "");
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

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(m_startDate);
        sb.append(", ");
        sb.append(m_endDate);
        sb.append(", ");
        sb.append(m_description);
        sb.append(", ");
        sb.append(m_location);
        sb.append(", ");
        sb.append(m_summary);

        return sb.toString();
    }


}
