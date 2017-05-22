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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.adapter.Image_9Adapter;
import com.longer.school.modle.bean.Good;
import com.longer.school.modle.bean.User;
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

/**
 * Created by Axu on 2016/9/23.
 */
public class Add_goodsActivity extends AppCompatActivity {
    @Bind(R.id.edt_addgoods_title)
    EditText edtAddgoodsTitle;
    @Bind(R.id.edt_addgoods_price)
    EditText edtAddgoodsPrice;
    @Bind(R.id.edt_addgoods_linktel)
    EditText edtAddgoodsLinktel;
    @Bind(R.id.edt_addgoods_linkqq)
    EditText edtAddgoodsLinkqq;
    @Bind(R.id.edt_addgoods_infor)
    EditText edtAddgoodsInfor;
    @Bind(R.id.tv_addgoods_addpic)
    TextView tvAddgoodsAddpic;
    @Bind(R.id.recy_addlost)
    RecyclerView recyAddlost;
    @Bind(R.id.tv_goods_jd)
    TextView tvGoodsJd;
    @Bind(R.id.progress_addgoods_progressBar)
    ProgressBar progressAddgoodsProgressBar;
    @Bind(R.id.btn_addgoods_up)
    Button btnAddgoodsUp;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.switch_addgoods_knife)
    Switch switchAddgoodsKnife;
    @Bind(R.id.eadio_good_cs)
    RadioButton eadioGoodCs;
    @Bind(R.id.eadio_good_qg)
    RadioButton eadioGoodQg;
    @Bind(R.id.edt_addgoods_name)
    EditText edtAddgoodsName;
    @Bind(R.id.sp_goods_kinds)
    Spinner spGoodsKinds;

    private Context context;
    private Image_9Adapter imagecrtnewsAdapter;
    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<String> uppath = new ArrayList<>();
    private ProgressDialog pg;
    //    private static String pic_path = null;//允许用户选择一张图片，所以只保存一个图片地址
    private int num;
    public static final int REQUEST_CODE = 1000;
    private boolean upload = false;//判断是否正在上传中 true是正在上传

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_add_goods);
        ButterKnife.bind(this);
        context = Add_goodsActivity.this;
        switchAddgoodsKnife.setChecked(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发布");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtAddgoodsName.setText(FileTools.getshareString("name") + "童学");
        String[] kings = new String[]{"其它", "学习相关", "生活用品"};
        ArrayAdapter<String> adapter_kinds = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, kings);
        adapter_kinds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoodsKinds.setAdapter(adapter_kinds);


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
        pg = new ProgressDialog(context);
        pg.setMessage("自动登录中...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        pg.show();
        BmobUser.loginByAccount(FileTools.getshareString("username"), FileTools.getshareString("password"), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    User userInfo = BmobUser.getCurrentUser(User.class);
                    Application.setuser(userInfo);
                    pg.dismiss();
                } else {
                    Toast.show("用户登陆失败");
                    pg.dismiss();
                    finish();
                }
            }
        });
    }

    @OnClick({R.id.tv_addgoods_addpic, R.id.btn_addgoods_up, R.id.eadio_good_cs, R.id.eadio_good_qg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addgoods_addpic:
                // 先判断是否有权限。
                if (AndPermission.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 有权限，直接do anything.
                    checkpic();
                } else {
                    //Log.d("tip","没有权限");
                    // 申请权限。
                    AndPermission.with(Add_goodsActivity.this)
                            .requestCode(100)
                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .send();
                }
                break;
            case R.id.btn_addgoods_up:
                upgoods();
                break;
            case R.id.eadio_good_cs:
                if (eadioGoodQg.isChecked()) {
                    eadioGoodQg.setChecked(false);
                    eadioGoodCs.setChecked(true);
                }
                break;
            case R.id.eadio_good_qg:
                if (eadioGoodCs.isChecked()) {
                    eadioGoodCs.setChecked(false);
                    eadioGoodQg.setChecked(true);
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

        ImageConfig imageConfig = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(Color.parseColor(FileTools.getshareString("refreshcolor")))
                .titleBgColor(Color.parseColor(FileTools.getshareString("refreshcolor")))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                .crop()
                .mutiSelectMaxSize(3)
                .pathList(path)
                .filePath("/ImageSelector/Pictures")
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
            imagecrtnewsAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 上传跳蚤市场信息
     */
    public void upgoods() {
        File file = new File(Environment.getExternalStorageDirectory() + "/School");//判断文件夹是否存在，用于存储压缩后的图片
        if (!file.exists()) {
            file.mkdir();
        }
        catchpic(path);//压缩图片
        if (TextUtils.isEmpty(edtAddgoodsTitle.getText()) || (TextUtils.isEmpty(edtAddgoodsLinkqq.getText()) && TextUtils.isEmpty(edtAddgoodsLinktel.getText())) || TextUtils.isEmpty(edtAddgoodsInfor.getText()) || TextUtils.isEmpty(edtAddgoodsPrice.getText()) || TextUtils.isEmpty(edtAddgoodsName.getText())) {
            Toast.show("不能有空哦");
            return;
        }
        upload = true;
        btnAddgoodsUp.setEnabled(false);
        btnAddgoodsUp.setText("发布中...");

        //先上传动态，和图片，如果都成功了，给他们之间添加关系
        num = path.size();
        if (num == 0) {//无图情况
            uploadcontent(null);
            return;
        }
        progressAddgoodsProgressBar.setVisibility(View.VISIBLE);
        final String[] paths = (String[]) uppath.toArray(new String[num]);
        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                    //成功上传图片后，上传动态，以及批量添加数据到image_pyq
                    tvGoodsJd.setText("上传完成");
                    uploadcontent(list);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                tvGoodsJd.setText("正在上传第" + i + "张  " + i1 + "%");
                progressAddgoodsProgressBar.setProgress(i1);
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("上传图片失败" + s);
                upload = false;
                btnAddgoodsUp.setEnabled(true);
                btnAddgoodsUp.setText("发布");
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
            File path = new File(Environment.getExternalStorageDirectory() + "/School/" + "pic_good" + Integer.toString(x) + ".jpg");
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
        } else {
            Log.d("登录帐号", "不为空");
        }

        String title = edtAddgoodsTitle.getText().toString().trim();
        String infor = edtAddgoodsInfor.getText().toString().trim();
        String price = edtAddgoodsPrice.getText().toString().trim();
        String linkqq = edtAddgoodsLinkqq.getText().toString().trim();
        String linktel = edtAddgoodsLinktel.getText().toString().trim();
        String name = edtAddgoodsName.getText().toString().trim();
        String kinds = spGoodsKinds.getSelectedItem().toString();

        final Good good = new Good();
        good.setTitle(title);
        good.setName(name);
        good.setInfor(infor);
        good.setPrice(price);
        good.setLink_qq(linkqq);
        good.setLink_tel(linktel);
        good.setSeller(Application.my);
        good.setKind(kinds);
        good.setComplete(false);
        boolean knife = switchAddgoodsKnife.isChecked() ? true : false;
        good.setKnife(knife);
        boolean type = eadioGoodCs.isChecked() ? true : false;
        good.setType(type);
//        Log.d("cc", "my:" + Application.my.getName());

        if (pic != null) {
            if (pic.size() >= 1) {
                good.setPic1(pic.get(0));
            }
            if (pic.size() >= 2) {
                good.setPic2(pic.get(1));
            }
            if (pic.size() == 3) {
                good.setPic3(pic.get(2));
            }
        }
        good.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                upload = false;
                btnAddgoodsUp.setEnabled(true);
                btnAddgoodsUp.setText("发布");
                if (e == null) {
                    Toast.show("创建数据成功：" + s);
                    finish();
                } else {
                    Toast.show("发布信息失败：" + e.toString());
                }
            }
        });
    }
}
