package a.team.works.u22.hal.u22teama;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.AdapterView;
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
public class TabPage2Fragment extends Fragment{

    private static final String ARG_PARAM = "page";
    private String mParam;
    private TabPage1Fragment.OnFragmentInteractionListener mListener;
    private static final String LOGIN_URL = GetUrl.MyPostsUrl;
    private String _id = "1";

    /**
     * コンストラクタ.
     */
    public TabPage2Fragment() {
    }

    public static TabPage2Fragment newInstance(int page) {
        TabPage2Fragment fragment = new TabPage2Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }


        //非同期処理を開始する。
        PostsTaskReceiver receiver = new PostsTaskReceiver();
        //ここで渡した引数はLoginTaskReceiverクラスのdoInBackground(String... params)で受け取れる。
        receiver.execute(LOGIN_URL , _id );
    }


    /**
     * 非同期通信を行うAsyncTaskクラスを継承したメンバクラス.
     */
    private class PostsTaskReceiver extends AsyncTask<String, Void, String> {

        private static final String DEBUG_TAG = "RestAccess";
        private List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();

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
            String id = params[1];

            //POSTで送りたいデータ
            String postData = "id=" + id;

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

                con.connect();

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
                    map.put("postTitle" , data.getString("postTitle"));
                    map.put("postPhoto" , data.getString("postPhoto"));
                    map.put("postStatus" , data.getString("postStatus"));
                    _list.add(map);
                }

                String[] from = {"postTitle" , "postPhoto" , "postStatus"};
                int[] to = {R.id.tvPostTitle , R.id.tvPostPhoto , R.id.tvPostStatus};
                final SimpleAdapter adapter = new SimpleAdapter(TabPage2Fragment.this , _list , R.layout.row_posts , from , to);
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
                            case R.id.tvPostPhoto:
                                TextView tvPostPhoto = (TextView) view;
                                tvPostPhoto.setText(strData);
                                return true;
                            case R.id.tvPostStatus:
                                TextView tvPostStatus = (TextView) view;
                                tvPostStatus.setText(strData);
                                return true;
                        }
                        return false;
                    }
                });
                ListView lvPostList = findViewById(R.id.lvPostList);
                lvPostList.setAdapter(adapter);
                lvPostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(String.valueOf(TabPage2Fragment.this));
//                        Map<String, String> map = (Map<String, String>) marker.getTag();
                        intent.putExtra("id", "kbzg701");
                        startActivity(intent);
                    }
                });

            }
            catch (JSONException ex) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
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
    }


//    public void onSearchButtonClick(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, view, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu_sample, menu);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_page2, container, false);

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
        if (context instanceof TabPage1Fragment.OnFragmentInteractionListener) {
            mListener = (TabPage1Fragment.OnFragmentInteractionListener) context;
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
}

