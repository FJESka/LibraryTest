package users;

public abstract class User {

    private int memberID;
    private int socialSecurityNumber;
    private String telNo;
    private String fName;
    private String lName;
    private String email;
    private String memberType;
    private String username;
    private String password;

    public User(int memberID, int socialSecurityNumber, String telNo, String fName, String lName, String email, String memberType, String username, String password) {
        this.memberID = memberID;
        this.socialSecurityNumber = socialSecurityNumber;
        this.telNo = telNo;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.memberType = memberType;
        this.username = username;
        this.password = password;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(int socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
