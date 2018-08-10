package a.team.works.u22.hal.u22teama;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;

public class NewProjectPostsScreenActivityInfomation implements Serializable {
    private String edTitle;
    private String imgUrl;
    private String imgName;
    private String edPlace;
    private String StrspinnerCate;
    private String edSConte;
    private String edInvestmentAmount;
    private byte[] byteImg;

//コンストラクト
    public  NewProjectPostsScreenActivityInfomation(){
        edTitle ="";
        edPlace ="";
        imgUrl = "";
        imgName = "";
        StrspinnerCate ="";
        edSConte ="";
        edInvestmentAmount ="";
        byteImg = new byte[10240];

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

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setByteImg(byte[] byteImg) {
        this.byteImg = byteImg;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public byte[] getByteImg() {
        return byteImg;
    }
}
