package base;


public class BookingRequest {

    private int roomid;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String endPoint;


    public BookingRequest() {}

    public int getRoomid() { return roomid; }
    public void setRoomid(int roomid) { this.roomid = roomid; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public void setEndPoint(String endpoint) {
        endPoint = endpoint;
    }



}
