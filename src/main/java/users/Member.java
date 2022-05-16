package users;

public class Member extends User {
    private int maxLoanLimit;
    private int allowedToBorrow;

    public Member(int memberID, int socialSecurityNumber, String telNo, String fName, String lName, String email,String memberType, String username, String password, int maxLoanLimit, int allowedToBorrow) {
        super(memberID, socialSecurityNumber, telNo, fName, lName, email, memberType, username, password);
        this.maxLoanLimit = maxLoanLimit;
        this.allowedToBorrow = allowedToBorrow;
    }


    public int getMaxLoanLimit() {
        return maxLoanLimit;
    }

    public void setMaxLoanLimit(int maxLoanLimit) {
        this.maxLoanLimit = maxLoanLimit;
    }

    public int getAllowedToBorrow() {
        return allowedToBorrow;
    }

    public void setAllowedToBorrow(int allowedToBorrow) {
        this.allowedToBorrow = allowedToBorrow;
    }
}
