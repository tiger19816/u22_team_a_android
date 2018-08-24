package a.team.works.u22.hal.u22teama;

 /*
 サーバーと連携するときのURLを取得するクラス
 fixedUrlの後にサーブレット名を追加し作成。
  */
public class GetUrl {
     private static final String fixedUrl = "http://59.106.223.97:8080/u22_team_a_web/";
//    private static final String fixedUrl = "http://10.0.2.2:8080/u22_team_a_web/";    //ローカルホスト
//    private static final String fixedUrl = "http://192.168.1.101:8080/u22_team_a_web/";    //平井モバイルルータ
    public static final String LoginUrl = fixedUrl + "UserLoginServlet";
    public static final String MyPostsUrl = fixedUrl + "JoinProjectServlet";
    public static final String projectMapUrl = fixedUrl + "DistanceServlet";
    public static final String NewProjectPostsConfirmationScreenActivity = fixedUrl + "NewProjectPostsConfirmationScreenActivityServlet";
    public static final String NewRegistration = fixedUrl + "RegistrationServlet";
    public static final String DonationCheckUrl = fixedUrl + "DonationServlet";
     public static final String DonationSetUrl = fixedUrl + "DonationSetServlet";
     public static final String MypageChangeCompleteUrl = fixedUrl + "MypageChangeCompleteServlet";
     public static final String ContactUrl = fixedUrl + "ContactServlet";
     public static final String photoUrl = fixedUrl + "temp/";
}
