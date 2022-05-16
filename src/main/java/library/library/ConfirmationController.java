package library.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConfirmationController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button printBtn;

    @FXML
    private ListView<String> receiptList;

    @FXML
    void changeToSearch(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.setScene(new Scene(root, 600, 400));
    }

    @FXML
    void printReciept(ActionEvent event) {

    }

    private ArrayList<String> loanInfoList = new ArrayList<>();

    void populateReceiptList(){

        ObservableList<String> confirmList = FXCollections.observableArrayList(loanInfoList);
        receiptList.setItems(confirmList);
    }

    @FXML
     void displayLoan(ActionEvent event) {

        LoanController loanController = new LoanController();
        try {

        String selectLoanQuery = "SELECT * FROM loan WHERE loan.barcode = '3'";
        System.out.println(selectLoanQuery);
        PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(selectLoanQuery);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            System.out.println("TEST");
            if (!receiptList.getItems().contains("barcode")) {

                loanInfoList.add(rs.getString("loanID") + " " + rs.getString("barcode") + " " + rs.getString("memberID") + " " + rs.getString("dateOfLoan"));

                populateReceiptList();
            }
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
