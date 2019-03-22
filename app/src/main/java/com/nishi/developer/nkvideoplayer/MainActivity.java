package com.nishi.developer.nkvideoplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*  private RecyclerView rv_video_folder;
      private ArrayList<String> videoFolderLIST;
      private Video_folder_adapter videoFolderAdapter;
      private TextView tv_no_videofolder;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);


          init();

      }

      private void init() {

          rv_video_folder = (RecyclerView) findViewById(R.id.rv_video_folder);
          rv_video_folder.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
          rv_video_folder.setHasFixedSize(true);

          tv_no_videofolder = (TextView) findViewById(R.id.tv_no_videofolder);

          videoFolderLIST = new ArrayList<String>();
          File directory = new File(Environment.getExternalStorageDirectory().getPath());
          findVideos(directory, videoFolderLIST);
      }

      public void findVideos(File dir, ArrayList<String> list) {
          list.clear();
          File[] files = dir.listFiles();

          Log.e("siez", String.valueOf(files.length));
          for (File file : dir.listFiles()) {
              if (file.isDirectory()) findVideos(file, list);
              else if (file.getAbsolutePath().contains(".mp4")) list.add(file.getAbsolutePath());
          }
      }


      public class Video_folder_adapter extends RecyclerView.Adapter<Video_folder_adapter.ViewHolder> {

          private ArrayList<String> videoFolderArrayList;

          private Context context;


          public Video_folder_adapter(Context context, ArrayList<String> videoFolderArrayList) {

              this.context = context;

              this.videoFolderArrayList = videoFolderArrayList;

          }

          @Override
          public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

              View view = LayoutInflater.from(context).inflate(R.layout.video_folder_layout, parent, false);

              return new ViewHolder(view);
          }

          @Override
          public int getItemCount() {
              return videoFolderArrayList.size();
          }


          @Override
          public void onBindViewHolder(final ViewHolder holder, final int position) {

              //      recording = recordingArrayList.get(position);

              // holder.manageSeekBar(holder);

          }


          public class ViewHolder extends RecyclerView.ViewHolder {

              private ImageView imageViewPlay;
              //
  //            private SeekBar seekBar;
  //
              private TextView textViewName, tv_time, tv_created_date, tv_totaltime;
  //
  //
  //            private LinearLayout rr_item_recorder;


              public ViewHolder(View itemView) {
                  super(itemView);
  //
  //                rr_item_recorder = (LinearLayout) itemView.findViewById(R.id.rr_item_recorder);
  //
                  imageViewPlay = (ImageView) itemView.findViewById(R.id.imageViewPlay);
  //
  //                seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
  //
                  textViewName = (TextView) itemView.findViewById(R.id.textViewRecordingname);
  //
  //                tv_time = (TextView) itemView.findViewById(R.id.textViewRecordingTime);
  //
  //                tv_created_date = (TextView) itemView.findViewById(R.id.tv_created_date);
  //
  //                tv_totaltime = (TextView) itemView.findViewById(R.id.tv_totaltime);

              }


          }


      }*/
    private Cursor videocursor;

    private int video_column_index;

    private ListView videolist;

    private int count;

    private String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};

    private String[] permission_gallery;

    private Context mContext;

    private final int MY_PERMISSIONS_REQUEST_GALLERY = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;


        permission_gallery = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);

            switch (permissionCheck) {

                case PackageManager.PERMISSION_GRANTED:

                    init_phone_video_grid();

                    break;

                case PackageManager.PERMISSION_DENIED:

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        requestPermissions(permission_gallery, MY_PERMISSIONS_REQUEST_GALLERY);

                    } else {

                        requestPermissions(permission_gallery, MY_PERMISSIONS_REQUEST_GALLERY);
                    }

                    break;
            }

        } else {

            init_phone_video_grid();

        }

    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_GALLERY:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    init_phone_video_grid();

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(this, permission_gallery, MY_PERMISSIONS_REQUEST_GALLERY);

                    } else {
                        // goToSettings("read gallery", 200);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            init_phone_video_grid();

        } else {

        }

    }

    @SuppressWarnings("deprecation")
    private void init_phone_video_grid() {

        System.gc();

        String[] proj = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE};

        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        count = videocursor.getCount();

        videolist = (ListView) findViewById(R.id.PhoneVideoList);

        videolist.setAdapter(new VideoAdapter(getApplicationContext()));

        videolist.setOnItemClickListener(videogridlistener);

    }

    private AdapterView.OnItemClickListener videogridlistener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position,
                                long id) {

            System.gc();

            video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            videocursor.moveToPosition(position);

            String filename = videocursor.getString(video_column_index);

            Intent intent = new Intent(MainActivity.this, VideoViewDemo.class);

            intent.putExtra("videofilename", filename);

            startActivity(intent);
        }
    };

    public class VideoAdapter extends BaseAdapter {

        private Context vContext;

        public VideoAdapter(Context c) {
            vContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            System.gc();

            ViewHolder holder;

            String id = null;

            convertView = null;

            if (convertView == null) {

                convertView = LayoutInflater.from(vContext).inflate(R.layout.list_item, parent, false);

                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

                holder.txtSize = (TextView) convertView.findViewById(R.id.txtSize);

                holder.thumbImage = (ImageView) convertView.findViewById(R.id.imgIcon);

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);

                videocursor.moveToPosition(position);

                id = videocursor.getString(video_column_index);

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

                videocursor.moveToPosition(position);

                // id += " Size(KB):" +
                // videocursor.getString(video_column_index);


                int i = video_column_index / 1024;


                holder.txtTitle.setText(id);

                holder.txtSize.setText(" Size(KB):" + videocursor.getString(video_column_index));

                String[] proj = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA};

                try {

                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj, MediaStore.Video.Media.DISPLAY_NAME + "=?", new String[]{id}, null);

                    cursor.moveToFirst();

                    long ids = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));

                    ContentResolver crThumb = getContentResolver();

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inSampleSize = 1;

                    Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, ids, MediaStore.Video.Thumbnails.MICRO_KIND, options);

                    holder.thumbImage.setImageBitmap(curThumb);

                    curThumb = null;
                } catch (Exception e) {

                }

            } /*
             * else holder = (ViewHolder) convertView.getTag();
             */
            return convertView;
        }
    }

    static class ViewHolder {

        TextView txtTitle;

        TextView txtSize;

        ImageView thumbImage;

    }


}
