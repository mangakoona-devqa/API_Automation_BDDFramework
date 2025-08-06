package base;

public class BookingRequest {

    private int roomid;
    private String firstname;
    private String lastname;
    private boolean depositpaid;
    private BookingDates bookingDates;
    private String email;
    private String phone;
    private String token;
    private String endPoint;
    private int bookingId;

    public BookingRequest() {}

    public int getRoomid() { return roomid; }
    public void setRoomid(int roomid) { this.roomid = roomid; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public boolean isDepositpaid() { return depositpaid; }
    public void setDepositpaid(boolean depositpaid) { this.depositpaid = depositpaid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BookingDates getBookingdates() {
        return bookingDates;
    }

    public void setBookingdates(BookingDates bookingdates) {
        this.bookingDates = bookingdates;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public void setEndPoint(String endpoint) {
        endPoint = endpoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setToken(String tokenvalue) {
        token = tokenvalue;
    }

    public String getToken() {
        return token;
    }

    public void setBookingId(int bookingid) {
        bookingId = bookingid;
    }

    public int getBookingId() {
        return bookingId;
    }


}
