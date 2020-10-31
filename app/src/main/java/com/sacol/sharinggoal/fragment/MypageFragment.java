package com.sacol.sharinggoal.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sacol.sharinggoal.InputActivity;
import com.sacol.sharinggoal.R;
import com.sacol.sharinggoal.SettingActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button settingBtn;
    private CircleImageView profile_image;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MypageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MypageFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        setup();
    }

    private void init() {
        settingBtn = getView().findViewById(R.id.settingBtn);
        profile_image  = getView().findViewById(R.id.profile_image);
    }

    private void setup() {

        settingBtn.setOnClickListener(goSettingPage);

//        FirebaseStorage storage = FirebaseStorage.getInstance("gs://...storage 주소");
//        StorageReference storageRef = storage.getReference();
//        storageRef.child("profileImg/"+ FirebaseAuth.getInstance().getUid()+"profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                //이미지 로드 성공시
//                  profile_image.setImageURI(uri);
////                Glide.with(getApplicationContext())
//////                        .load(uri)
//////                        .into(img_test);
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                //이미지 로드 실패시
//            }
//        });
    }

    View.OnClickListener goSettingPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startSettingActivity();
        }
    };

    private void startSettingActivity() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivity(intent);
    }


}

