package in.com.ralarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kumar0044q on 8/23/2016.
 */

public class Medi {


    @SerializedName("Active")
    @Expose
    private String active;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Days")
    @Expose
    private String days;
    @SerializedName("Medication")
    @Expose
    private String medication;



    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }


    @Override
    public String toString(){
        return(medication);
    }

}

