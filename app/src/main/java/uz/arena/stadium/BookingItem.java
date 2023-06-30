package uz.arena.stadium;

public class BookingItem {
    String name, lost, phone, date, time, time2, st_name, location, dateGone, st_uid, status,
            rasim1, rasim2, rasim3, rasim4, latitude, longitude, dimensions, choosing, check_1,
            check_2, summa, radio;

    public BookingItem() {
    }


    public BookingItem(String name, String lost, String phone, String date, String time, String time2,
                       String st_name, String location, String dateGone, String st_uid, String status,
                       String rasim1, String rasim2, String rasim3, String rasim4, String latitude,
                       String longitude, String dimensions, String choosing, String check_1, String check_2
            , String summa, String radio) {

        this.name = name;
        this.lost = lost;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.time2 = time2;
        this.st_name = st_name;
        this.location = location;
        this.dateGone = dateGone;
        this.st_uid = st_uid;
        this.status = status;
        this.rasim1 = rasim1;
        this.rasim2 = rasim2;
        this.rasim3 = rasim3;
        this.rasim4 = rasim4;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dimensions = dimensions;
        this.choosing = choosing;
        this.check_1 = check_1;
        this.check_2 = check_2;
        this.summa = summa;
        this.radio = radio;
    }

    public String getName() {
        return name;
    }

    public String getLost() {
        return lost;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTime2() {
        return time2;
    }

    public String getSt_name() {
        return st_name;
    }

    public String getLocation() {
        return location;
    }

    public String getDateGone() {
        return dateGone;
    }

    public String getSt_uid() {
        return st_uid;
    }

    public String getStatus() {
        return status;
    }

    public String getRasim1() {
        return rasim1;
    }

    public String getRasim2() {
        return rasim2;
    }

    public String getRasim3() {
        return rasim3;
    }

    public String getRasim4() {
        return rasim4;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getChoosing() {
        return choosing;
    }

    public String getCheck_1() {
        return check_1;
    }

    public String getCheck_2() {
        return check_2;
    }

    public String getSumma() {
        return summa;
    }

    public String getRadio() {
        return summa;
    }
}
