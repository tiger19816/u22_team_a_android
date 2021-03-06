package a.team.works.u22.hal.u22teama;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TabLayoutのFragmentクラス2.
 * Fragmentはレイアウトxmlとセットになっている。
 * Fragmentを設定したTab内で行われる処理は、このクラス内に記述する。
 *
 * @author Taiga Hirai
 */
public class TabPagePostFragment extends Fragment{

    private static final String ARG_PARAM = "page";
    private String mParam;
    private OnFragmentInteractionListener mListener;
    private static final String LOGIN_URL = GetUrl.MyPostsUrl;
    private String _flag = "1";
    private String _id = "0";

    public ProgressDialog _pDialog;

    /**
     * コンストラクタ.
     */
    public TabPagePostFragment() {
    }

    public static TabPagePostFragment newInstance(int page) {
        TabPagePostFragment fragment = new TabPagePostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getActivity().getSharedPreferences("prefUserId",0);
        int loginInfo = pref.getInt("id", 0);
        _id = String.valueOf(loginInfo);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }

        //非同期処理を開始する。
        PostsTaskReceiver receiver = new PostsTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , _flag , _id);
    }


    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class PostsTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";
        private List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();


        /**
         * 通信開始前に実行されるメソッド。
         *
         * ここで、プログレスダイアログを生成しましょう。
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _pDialog = new ProgressDialog(getActivity());
            _pDialog.setMessage(getString(R.string.progress_message));  // メッセージを設定。

            // プログレスダイアログの表示。
            _pDialog.show();

        }

        /**
         * 非同期に処理したい内容を記述するメソッド.
         * このメソッドは必ず実装する必要がある。
         *
         * @param params String型の配列。（可変長）
         * @return String型の結果JSONデータ。
         */
        @Override
        public String doInBackground(String... params) {
            String urlStr = params[0];
            String flag = params[1];
            String id = params[2];

            //POSTで送りたいデータ
            String postData = "flag=" + flag + "&id=" + id ;

            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();

                //GET通信かPOST通信かを指定する。
                con.setRequestMethod("POST");

                //自動リダイレクトを許可するかどうか。
                con.setInstanceFollowRedirects(false);

                //時間制限。（ミリ秒単位）
                con.setReadTimeout(10000);
                con.setConnectTimeout(20000);

                con.setDoOutput(true);

                //POSTデータ送信処理。InputStream処理よりも先に記述する。
                OutputStream os = null;
                try {
                    os = con.getOutputStream();

                    //送信する値をByteデータに変換する（UTF-8）
                    os.write(postData.getBytes("UTF-8"));
                    os.flush();
                }
                catch (IOException ex) {
                    Log.e(DEBUG_TAG, "POST送信エラー", ex);
                }
                finally {
                    if(os != null) {
                        try {
                            os.close();
                        }
                        catch (IOException ex) {
                            Log.e(DEBUG_TAG, "OutputStream解放失敗", ex);
                        }
                    }
                }

                is = con.getInputStream();
                result = is2String(is);
            }
            catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            }
            catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            }
            finally {
                if(con != null) {
                    con.disconnect();
                }
                if(is != null) {
                    try {
                        is.close();
                    }
                    catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }

            return result;
        }

        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject rootJSON = new JSONObject(result);

                JSONArray datas = rootJSON.getJSONArray("postsList");
                //投稿情報
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    Map map = new HashMap<String , Object>();
                    map.put("postNo" , data.getString("postNo"));
                    map.put("postTitle" , data.getString("postTitle"));
                    map.put("postPhoto" , data.getString("postPhoto"));
                    map.put("postMoney" , data.getString("postMoney"));
                    map.put("postDate" , data.getString("postDate"));
                    map.put("postStatus" , data.getString("postStatus"));
                    _list.add(map);
                }

                String[] from = {"postTitle" , "postPhoto" , "postMoney" , "postDate" , "postStatus"};
                int[] to = {R.id.tvPostTitle , R.id.wvPostsImage , R.id.tvPostMoney , R.id.tvPostDate , R.id.tvPostStatus};
                final SimpleAdapter adapter = new SimpleAdapter(getActivity() , _list , R.layout.row_posts , from , to);
                adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        int id = view.getId();
                        String strData = (String) data;
                        switch (id) {
                            case R.id.tvPostTitle:
                                TextView tvPostTitle = (TextView) view;
                                tvPostTitle.setText(strData);
                                return true;
                            case R.id.wvPostsImage:
                                WebView myWebView = (WebView) view;
                                myWebView.setWebViewClient(new WebViewClient());
                                myWebView.getSettings().setUseWideViewPort(true);
                                myWebView.getSettings().setLoadWithOverviewMode(true);
                                myWebView.loadUrl(GetUrl.photoUrl + "?img=temp/" + strData);
                                return true;
                            case R.id.tvPostMoney:
                                TextView tvPostMoney = (TextView) view;
                                tvPostMoney.setText("投稿金：" + Tools.StrNumToStringCom(strData) + "円");
                                return true;
                            case R.id.tvPostDate:
                                TextView tvPostDate = (TextView) view;
                                tvPostDate.setText("投稿日：" + DataConversion.getDataConversion02(strData));
                                return true;
                            case R.id.tvPostStatus:
                                TextView tvPostStatus = (TextView) view;
                                tvPostStatus.setText("状態：" + strData);
                                return true;
                        }
                        return false;
                    }
                });
                ListView lvPostList = getActivity().findViewById(R.id.lvPostList);
                lvPostList.setAdapter(adapter);
                lvPostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                        Map<String, String> map = (Map<String, String>) adapter.getItem(position);
                        intent.putExtra("projectId", map.get("postNo"));
                        startActivity(intent);
                    }
                });

            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
            // ロード画面を消す。
            if (_pDialog != null && _pDialog.isShowing()) {
                _pDialog.dismiss();
            }
        }


    }
    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while (0 <= (line = reader.read(b))) {
            sb.append(b, 0, line);
        }
        return sb.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_page_post, container, false);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public interface AsyncTaskCallBack {
        public void taskComp(Bitmap result);
    }
}


