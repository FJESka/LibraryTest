package adminInterface;

public class ItemCopy {

    String barcode;
    int loanPeriod;
    Integer dvdID;
    String isbn;
    String copyType;
    int status;

    //ItemCopy constructor
    public ItemCopy(String barcode, int loanPeriod, Integer dvdID, String isbn, String copyType, int status) {

        this.barcode = barcode;
        this.loanPeriod = loanPeriod;
        this.dvdID = dvdID;
        this.isbn = isbn;
        this.copyType = copyType;
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public Integer getDvdID() {
        return dvdID;
    }

    public void setDvdID(Integer dvdID) {
        this.dvdID = dvdID;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCopyType() {
        return copyType;
    }

    public void setCopyType(String copyType) {
        this.copyType = copyType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
