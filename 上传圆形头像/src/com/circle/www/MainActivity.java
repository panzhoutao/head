package com.circle.www;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.circle.www.utils.CacheUtils;
import com.circle.www.utils.FileUtils;
import com.circle.www.utils.ImageResizer;
import com.circle.www.utils.TypeConverter;
import com.circle.www.view.CircleImageView;

public class MainActivity extends Activity {

	private com.circle.www.view.CircleImageView circleImageView;
	Context context;
	private PopupWindow popWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		String newPath = CacheUtils.getImagePath(this, "sendImage/" + "crop"
				+ ".jpg");

		outputUri = Uri.fromFile(new File(newPath));

		initView();
		
	}

	// 初始化圆形头像控件；设置点击事件；
	private void initView() {

		circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
		circleImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				showPopWindow();

			}
		});

	}

	protected void dismissPopwindow() {
		if (popWindow != null && popWindow.isShowing()) {

			popWindow.dismiss();
			popWindow = null;
		}

	}

	// 弹出popWindow
	protected void showPopWindow() {
		View view = View.inflate(context, R.layout.popwindow, null);
		popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		popWindow.showAtLocation(MainActivity.this.findViewById(R.id.main_rl),
				Gravity.BOTTOM, 0, 0);

		TextView tvCamera = (TextView) view
				.findViewById(R.id.item_popupwindows_camera);
		TextView tvPhone = (TextView) view
				.findViewById(R.id.item_popupwindows_phone);
		TextView tvCancel = (TextView) view
				.findViewById(R.id.item_popupwindows_cancel);

		tvCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击相机
				dismissPopwindow();
				showCamera();

			}
		});

		tvPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 从相册中获取
				dismissPopwindow();
				showPhone();

			}

		});

		tvCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取消
				dismissPopwindow();
			}
		});

	}

	int REQUESTCODE_CAMERA = 0;
	int REQUESTCODE_GALLERY = 1;
	int REQUESTCODE_CROP = 2;
	Bitmap changedImage;
	String newPath = null;
	Uri outputUri = null;

	// 启用系统相机
	protected void showCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUESTCODE_CAMERA);
	}

	// 启用系统
	protected void showPhone() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, REQUESTCODE_GALLERY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		// 相机回调
		if (requestCode == REQUESTCODE_CAMERA) {

			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				changedImage = (Bitmap) bundle.get("data");
				circleImageView.setImageBitmap(changedImage);
			}
		}// 相册回调
		else if (requestCode == REQUESTCODE_GALLERY) {

			if (resultCode == RESULT_OK) {

				String path = FileUtils.getPictureSelectedPath(data, this);
				newPath = CacheUtils.getImagePath(this, "sendImage/"
						+ TypeConverter.getUUID() + ".jpg");
				try {
					Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(
							path, 400, 400);
					FileUtils.compressAndWriteFile(bitmap, this, newPath);

					startPhotoZoom(this, Uri.fromFile(new File(newPath)));

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		// 裁剪回调
		else if (requestCode == REQUESTCODE_CROP) {
			if (resultCode == RESULT_OK) {
				try {
					changedImage = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									outputUri));
					circleImageView.setImageBitmap(changedImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void startPhotoZoom(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		activity.startActivityForResult(intent, REQUESTCODE_CROP);
	}

}
