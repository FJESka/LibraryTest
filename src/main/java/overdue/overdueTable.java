package overdue;

public class overdueTable {

    String MemberID;
    String dueDate;
    String title;

    public overdueTable(String memberId, String dueDate, String title) {
        this.MemberID = memberId;
        this.dueDate = dueDate;
        this.title = title;
    }
    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        this.MemberID = memberID;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
