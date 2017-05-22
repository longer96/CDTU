package com.longer.school.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.longer.school.Application;
import com.longer.school.modle.bean.Lost;
import com.longer.school.modle.bean.User;
import com.longer.school.R;
import com.longer.school.adapter.Image_9Adapter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageLoader;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class Add_lostActivity extends AppCompatActivity {


    @Bind(R.id.eadio_xw)
    RadioButton eadioXw;
    @Bind(R.id.eadio_zl)
    RadioButton eadioZl;
    @Bind(R.id.edt_addlost_title)
    EditText edtAddlostTitle;
    @Bind(R.id.edt_addlost_infor)
    EditText edtAddlostInfor;
    @Bind(R.id.edt_addlost_name)
    EditText edtAddlostName;
    @Bind(R.id.edt_addlost_linkqq)
    EditText edtAddlostLinkqq;
    @Bind(R.id.edt_addlost_linktel)
    EditText edtAddlostLinktel;
    @Bind(R.id.progress_addlost_progressBar)
    ProgressBar pg;
    @Bind(R.id.tv_lost_jd)
    TextView tvLostJd;
    public Context context;
    @Bind(R.id.btn_addlost_up)
    Button btnAddlostUp;
    @Bind(R.id.edt_addlost_thing)
    EditText edtAddlostThing;
    @Bind(R.id.edt_addlost_where)
    EditText edtAddlostWhere;
    @Bind(R.id.tv_addlost_addpic)
    TextView tvAddlostAddpic;
    @Bind(R.id.recy_addlost)
    RecyclerView recyAddlost;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    private Image_9Adapter imagecrtnewsAdapter;
    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<String> uppath = new ArrayList<>();
    //    private static String pic_path = null;//允许用户选择一张图片，所以只保存一个图片地址
    private int num;
    public static final int REQUEST_CODE = 1000;
    private boolean upload = false;//判断是否正在上传中 true是正在上传
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_add_lost);
        ButterKnife.bind(this);
        context = Add_lostActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发布失物招领");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        edtAddlostName.setText(FileTools.getshareString("name") + "童学");

        //判断是不是为空，不然用户填完所有资料才告诉他不好
        if (Application.my == null) {
            //先自动登录
            autologin();
        }
    }

    /**
     * 防止没有登录的用户，我们自动先帮他登录
     */
    private void autologin() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("自动登录中...");
        progressDialog.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        progressDialog.show();
        BmobUser.loginByAccount(FileTools.getshareString("username"), FileTools.getshareString("password"), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    User userInfo = BmobUser.getCurrentUser(User.class);
                    Application.setuser(userInfo);
                    progressDialog.dismiss();
                } else {
                    Toast.show("用户登陆失败");
                    progressDialog.dismiss();
                    finish();
                }
            }
        });
    }

    @OnClick({R.id.tv_addlost_addpic, R.id.btn_addlost_up, R.id.eadio_xw, R.id.eadio_zl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addlost_addpic:
                // 先判断是否有权限。
                if (AndPermission.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 有权限，直接do anything.
                    checkpic();
                } else {
                    //Log.d("tip","没有权限");
                    // 申请权限。
                    AndPermission.with(Add_lostActivity.this)
                            .requestCode(100)
                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .send();
                }
                break;
            case R.id.btn_addlost_up:
                if (!upload) {
                    uplost();
                }
                break;
            case R.id.eadio_xw:
                if (eadioZl.isChecked()) {
                    eadioZl.setChecked(false);
                    eadioXw.setChecked(true);
                }
                break;
            case R.id.eadio_zl:
                if (eadioXw.isChecked()) {
                    eadioZl.setChecked(true);
                    eadioXw.setChecked(false);
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
                checkpic();
            } else if (requestCode == 101) {
                Log.d("tip", "100");
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            Toast.show("额，没有权限将无法选择图片！");
        }
    };

    /**
     * 选择照片
     */
    public void checkpic() {
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recy_addlost);

        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(Color.parseColor(FileTools.getshareString("refreshcolor")))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(Color.parseColor(FileTools.getshareString("refreshcolor")))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(3)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recycler.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter = new Image_9Adapter(context, path);
        recycler.setAdapter(imagecrtnewsAdapter);
    }


    public class GlideLoader implements ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            path.clear();
            path.addAll(pathList);
//            pic_path = pathList.get(pathList.size() - 1);
//            path.add(pic_path);
            imagecrtnewsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 上传失物招领信息
     */
    public void uplost() {
        File file = new File(Environment.getExternalStorageDirectory() + "/School");//判断文件夹是否存在，用于存储压缩后的图片
        if (!file.exists()) {
            file.mkdir();
        }
        catchpic(path);//压缩图片
        if (TextUtils.isEmpty(edtAddlostInfor.getText()) || (TextUtils.isEmpty(edtAddlostLinkqq.getText()) && TextUtils.isEmpty(edtAddlostLinktel.getText())) || TextUtils.isEmpty(edtAddlostName.getText()) || TextUtils.isEmpty(edtAddlostTitle.getText()) || TextUtils.isEmpty(edtAddlostThing.getText()) || TextUtils.isEmpty(edtAddlostWhere.getText())) {
            Toast.show("不能为空哦");
            return;
        }
        upload = true;
        btnAddlostUp.setEnabled(false);
        btnAddlostUp.setText("发布中...");

        //先上传动态，和图片，如果都成功了，给他们之间添加关系
        num = path.size();
        if (num == 0) {//无图情况
            uploadcontent(null);
            return;
        }
        pg.setVisibility(View.VISIBLE);
        final String[] paths = (String[]) uppath.toArray(new String[num]);
        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                    //成功上传图片后，上传动态，以及批量添加数据到image_pyq
                    tvLostJd.setText("上传完成");
                    uploadcontent(list);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                tvLostJd.setText("正在上传第" + i + "张  " + i1 + "%");
                pg.setProgress(i1);
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("上传图片失败" + s);
                upload = false;
                btnAddlostUp.setEnabled(true);
                btnAddlostUp.setText("发布");
            }
        });
    }

    /**
     * 转化图片
     */
    public void catchpic(ArrayList<String> paths) {
        int x = 0;
        uppath.clear();
        for (String p : paths) {
            Bitmap bit = getimage(p);
            File path = new File(Environment.getExternalStorageDirectory() + "/School/" + "pic_lost" + Integer.toString(x) + ".jpg");
            compressBiamp(bit, path.toString(), 50);
            uppath.add(path.toString());
            x++;
        }
    }

    /**
     * 压缩图片到指定位置(默认JPG格式)
     *
     * @param bitmap       需要压缩的图片
     * @param compressPath 生成文件路径
     * @param quality      图片质量，0~100
     * @return if true,保存成功
     */
    public static boolean compressBiamp(Bitmap bitmap, String compressPath, int quality) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(compressPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);// (0-100)压缩文件
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 根据路径压缩尺寸
     *
     * @param srcPath
     * @return
     */
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();//开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1080f;//这里设置高度
        float ww = 720f;//这里设置宽度
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 添加动态，并得到动态的id
     */
    void uploadcontent(final List<BmobFile> pic) {
        if (Application.my == null) {
            Log.d("登录帐号", "null");
            Toast.show("亲~不好意思，登录用户才能发布信息哦~");
            return;
        }
        String title = edtAddlostTitle.getText().toString().trim();
        String infor = edtAddlostInfor.getText().toString().trim();
        String name = edtAddlostName.getText().toString().trim();
        String linkqq = edtAddlostLinkqq.getText().toString().trim();
        String linktel = edtAddlostLinktel.getText().toString().trim();
        String thing = edtAddlostThing.getText().toString().trim();
        String where = edtAddlostWhere.getText().toString().trim();

        final Lost lost = new Lost();
        lost.setName(name);
        lost.setTitle(title);
        lost.setInfor(infor);
        lost.setThing(thing);
        lost.setWhere(where);
        lost.setLink_qq(linkqq);
        lost.setLink_tel(linktel);
        lost.setComplete(false);
        lost.setUser(Application.my);
//        Log.d("cc", "my:" + Application.my.getName());
        boolean type = eadioZl.isChecked() ? true : false;
        lost.setType(type);
        if (pic != null) {
            if (pic.size() >= 1) {
                lost.setPic1(pic.get(0));
            }
            if (pic.size() >= 2) {
                lost.setPic2(pic.get(1));
            }
            if (pic.size() == 3) {
                lost.setPic3(pic.get(2));
            }
        }
        lost.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                upload = false;
                btnAddlostUp.setEnabled(true);
                btnAddlostUp.setText("发布");
                if (e == null) {
                    Toast.show("创建数据成功：" + s);
                    finish();
                } else {
                    Toast.show("发布动态失败：" + e.toString());
                }
            }
        });
    }
}

