package com.nishi.developer.nkvideoplayer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;

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
                        goToSettings("read gallery", 200);
                    }
                }
                break;
        }
    }


    private void goToSettings(String title, final int per) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("App Permission");
        builder.setMessage("This app needs " + title + " permission.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, per);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 200) {

            init_phone_video_grid();

        } else {

            goToSettings("read gallery", 200);

        }

    }

    private void init_phone_video_grid() {

        System.gc();

        String[] proj = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE};

        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%FolderName%"};
        String[] parameters = {MediaStore.Video.Media._ID};
        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, MediaStore.Video.Media.DATE_TAKEN + " DESC");

        count = videocursor.getCount();

        videolist = (ListView) findViewById(R.id.PhoneVideoList);

        videolist.setAdapter(new VideoAdapter(getApplicationContext()));

        videolist.setOnItemClickListener(videogridlistener);


    }


    private AdapterView.OnItemClickListener videogridlistener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView parent, View v, int position, long id) {

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

        public View getView(final int position, View convertView, ViewGroup parent) {

            System.gc();

            ViewHolder holder;

            String id = null;

            convertView = null;

            if (convertView == null) {

                convertView = LayoutInflater.from(vContext).inflate(R.layout.list_item, parent, false);

                holder = new ViewHolder();
/*
                holder.ll_video_view = (LinearLayout) convertView.findViewById(R.id.ll_video_view);

                holder.ll_video_view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

                        videocursor.moveToPosition(position);

                        String filPAth = videocursor.getString(video_column_index);


                        Uri filePathUri;

                        int column_index = videocursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                        filePathUri = Uri.parse(videocursor.getString(column_index));
                        String fileName1 = filePathUri.getLastPathSegment().toString();


                        RenameFile(fileName1, filPAth);
                        Log.e("before name " + filPAth, "after name " + fileName1);

                        return true;
                    }
                });*/

                holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

                holder.txtSize = (TextView) convertView.findViewById(R.id.txtSize);

                holder.thumbImage = (ImageView) convertView.findViewById(R.id.imgIcon);

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);

                videocursor.moveToPosition(position);

                id = videocursor.getString(video_column_index);

                holder.txtTitle.setText(id);

                video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

                videocursor.moveToPosition(position);

                long fileSizeInBytes = videocursor.getLong(video_column_index);

                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;

                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;


                DecimalFormat df2 = new DecimalFormat(".##");

                if (fileSizeInKB > 1024) {

                    //double v = df2.format(fileSizeInMB);
                    holder.txtSize.setText(" Size : " + df2.format(fileSizeInMB) + " MB "/*videocursor.getString(video_column_index)*/);

                } else {

                    holder.txtSize.setText(" Size : " + fileSizeInKB + " KB "/*videocursor.getString(video_column_index)*/);
                }
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


    private void RenameFile(String oldfileName, final String path) {

        final Dialog saveDialog = new Dialog(MainActivity.this);
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.setContentView(R.layout.dialog_file_save);
        saveDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        saveDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        saveDialog.getWindow().setGravity(Gravity.CENTER);
        saveDialog.setCanceledOnTouchOutside(false);
        saveDialog.setCancelable(false);

        final EditText edt_file_name = saveDialog.findViewById(R.id.edt_file_name);
        edt_file_name.setText(oldfileName);
        edt_file_name.setSelection(edt_file_name.getText().toString().trim().length());

        Button btn_delete = saveDialog.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File f = new File(path);

                if (f.exists()) {
                    f.delete();

                    Toast.makeText(MainActivity.this, "Delete file", Toast.LENGTH_SHORT).show();
                }

                saveDialog.dismiss();
                // finish();

            }
        });

        Button btn_ok = saveDialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(MainActivity.this);

                if (rename(edt_file_name.getText().toString().trim(), path)) {

                    try {
                        //mDatabase.addRecording(edt_file_name.getText().toString().trim(), fileName, mElapsedMillis);

                        Toast.makeText(MainActivity.this, "File save.", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.e("exception", "exception", e);
                    }

                    //  mElapsedMillis = 0;

                    saveDialog.dismiss();

                    // finish();
                }


            }
        });

        saveDialog.show();


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean rename(String name, String strOldPath) {

        File sdcard = Environment.getExternalStorageDirectory();

        File f = new File(strOldPath);
        File from = new File(f, "from.txt");
        File to = new File(sdcard, "to.txt");
        from.renameTo(to);

        return true;

    }

    static class ViewHolder {

        LinearLayout ll_video_view;

        TextView txtTitle;

        TextView txtSize;

        ImageView thumbImage;

    }


}
