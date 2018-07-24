package a.team.works.u22.hal.u22teama;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;

public class NewProjectPostsScreenActivityInfomation implements Serializable {
    private String edTitle;
    private String edPlace;
    private String  StrspinnerCate;
    private String edSConte;
    private String edInvestmentAmount;

//コンストラクト
    public  NewProjectPostsScreenActivityInfomation(){
        edTitle ="";
        edPlace ="";
        StrspinnerCate ="";
        edSConte ="";
        edInvestmentAmount ="";

    }

// Set
    public void setEdTitel(String edTitle) {
        this.edTitle = edTitle;
    }

    public void setEtPlace(String edPlace) {
        this.edPlace = edPlace;
    }

    public void setSpinnerCate(String spinnerCate) {
        this.StrspinnerCate = spinnerCate;
    }
    public void setEdSConte(String edSConte) {
        this.edSConte = edSConte;
    }

    public void setEdInvestmentAmount(String edInvestmentAmount) {
        this.edInvestmentAmount = edInvestmentAmount;
    }

// Get
    public String getEdTitle() {
        return edTitle;
    }

    public String getEdPlace() {
        return edPlace;
    }

    public String getSpinnerCate() {
        return StrspinnerCate;
    }

    public String getEdSConte() {
        return edSConte;
    }

    public String getEdInvestmentAmount() {
        return edInvestmentAmount;
    }
}
