package a.team.works.u22.hal.u22teama;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewProjectPostsScreenActivityInfomation {
    private EditText edTitle;
    private EditText edPlace;
    private String  StrspinnerCate;
    private EditText edSConte;
    private EditText edInvestmentAmount;

//コンストラクト
    public  NewProjectPostsScreenActivityInfomation(){
        edTitle.setText("");
        edPlace.setText("");
        StrspinnerCate ="";
        edSConte.setText("");
        edInvestmentAmount.setText("");

    }

// Set
    public void setEdTitel(EditText edTitle) {
        this.edTitle = edTitle;
    }

    public void setEtPlace(EditText edPlace) {
        this.edPlace = edPlace;
    }

    public void setSpinnerCate(String spinnerCate) {
        this.StrspinnerCate = spinnerCate;
    }

    public void setEdSConte(EditText edSConte) {
        this.edSConte = edSConte;
    }

    public void setEdInvestmentAmount(EditText edInvestmentAmount) {
        this.edInvestmentAmount = edInvestmentAmount;
    }

// Get
    public String getEdTitle() {
        return edTitle.toString();
    }

    public String getEdPlace() {
        return edPlace.toString();
    }

    public String getSpinnerCate() {
        return StrspinnerCate;
    }

    public String getEdSConte() {
        return edSConte.toString();
    }

    public String getEdInvestmentAmount() {
        return edInvestmentAmount.toString();
    }
}
