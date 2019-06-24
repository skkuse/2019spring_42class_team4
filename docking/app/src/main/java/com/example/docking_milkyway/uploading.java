package com.example.docking_milkyway;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.docking_milkyway.util.ImageResizeUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class uploading extends AppCompatActivity {

    private Button getcamera, getalbum, uploadok, uplodacancel;
    private EditText textinsert, locationinsert;
    private TagGroup tagGroup;

    private Uri savingUri;
    private String currentPhotoPath; //실제 사진 파일 저장 경로
    String mImageCaptureName; //이미지 이름

    private File tempFile;

    private String[] tags;

    String user_id;
    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public static final int PICK_FROM_ALBUM = 1;
    public static final int PICK_FROM_CAMERA = 2;
    private Boolean isCamera = false;

    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            } //권한 요청 성공

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            } //권한 요청 실패

        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode !=Activity.RESULT_OK) {
            Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if(tempFile.exists()) {
                    if(tempFile.delete()) {
                        Log.d("은하", tempFile.getAbsolutePath() + "삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }
        switch(requestCode) {
            case PICK_FROM_ALBUM: {
                Log.d("은하", "album사진 선택");
                Uri photoUri = data.getData();
                cropImage(photoUri);
                break;
            }
            case PICK_FROM_CAMERA: {
                Log.d("은하", "카메라 사진 선택");
                Uri photoUri = Uri.fromFile(tempFile);
                cropImage(photoUri);
                break;
            }
            case Crop.REQUEST_CROP: {
                setImage();
            }
        }
    }

    private void setImage() {
        ImageView imageView = findViewById(R.id.albumimage);

        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d("은하", "setImage : " + tempFile.getAbsolutePath());
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(originalBm);
    }

    private void goToAlbum() {
        isCamera = false;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Log.d("은하", "앨범 접근 성공");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_ALBUM);
        Log.d("은하", "어디?");
    }

    private void takePhoto() {
        isCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try{
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Uri photoUri = FileProvider.getUriForFile
                        (this, "com.example.docking_milkyway.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            } else {
                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    private void cropImage(Uri photoUri) {
        Log.d("은하", "tempFile : " + tempFile);
        if(tempFile == null) {
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }
        savingUri = Uri.fromFile(tempFile);
        Crop.of(photoUri, savingUri).asSquare().start(this);
    }

    private File createImageFile() throws IOException {
        //the name of Image file
        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
        String imageFileName = "Docking_" + timeStamp;

        //이미지가 저장될 폴더 이름 (docking)
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/docking/");
        if (!storageDir.exists()) storageDir.mkdirs();

        //빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploading);
        Log.d("은하", "in uploading");

        tedPermission();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        getcamera = findViewById(R.id.getcamera);
        getalbum = findViewById(R.id.getalbum);
        uploadok = findViewById(R.id.uploadok);
        uplodacancel = findViewById(R.id.uploadcancel);

        textinsert = findViewById(R.id.textinsert);
        tagGroup = findViewById(R.id.tag_group);
        locationinsert = findViewById(R.id.locationinsert);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");

        getalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //앨범 접근
                goToAlbum();
            }
        });

        getcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //카메라 접근
                takePhoto();
            }
        });

        uploadok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SaveSharedPreference login_history = new SaveSharedPreference();
                //String userid = (String) login_history.getUserName(v.getContext());
                //String userid = "1002galaxy@gmail.com"; //테스트용

                Log.d("은하", "uploadbtn click");
                Log.d("은하", "현재 "+userid+" 업로딩중");
                String text = textinsert.getText().toString();
                String newlocation = locationinsert.getText().toString();
                Boolean tag=false;

                tags = tagGroup.getTags();


                if(TextUtils.isEmpty(text) || Uri.EMPTY.equals(savingUri)){
                    Toast.makeText(v.getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), savingUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    Uri file = savingUri;
                    StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());

                    UploadTask uploadTask = riversRef.putBytes(data);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri = task.getResult();

                                ContentDB temp_content = new ContentDB(downloadUri.toString(), text, false, userid);
                                if (!newlocation.isEmpty()) {
                                    temp_content.location = newlocation;
                                }

                                firebaseFirestore.collection("Contents")
                                        .add(temp_content)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                String SSN = documentReference.getId();
                                                Log.d("은하", "DocumentSnapshot written with ID : " + SSN);
                                                firebaseFirestore.collection("Contents").document(SSN)
                                                        .update("SSN", SSN)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("은하", "DocumentSnapshot successfully updated!");
                                                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("은하", "Error updating document", e);
                                                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("은하", "Error adding document", e);
                                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });

                            } else {
                                //handle fail
                                Log.d("은하", "image upload fail");
                            }
                        }
                    });

                }

            }
        });

        uplodacancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }

}
