package a.team.works.u22.hal.u22teama;

 /*
 サーバーと連携するときのURLを取得するクラス
 fixedUrlの後にサーブレット名を追加し作成。
  */
public class GetUrl {
    private static  final String fixedUrl = "http://10.0.2.2:8080/U22Verification/";
    public static final String LoginUrl = fixedUrl + "LoginServlet";

}
