package com.maq.ecom.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.LoadingDialog;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.views.activities.ChangeProfileImgActivity;
import com.maq.ecom.views.activities.ChangePwdActivity;
import com.maq.ecom.views.activities.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;

public class ProfileFragment extends Fragment {

    SessionManager sessionManager;
    MultipartBody.Part imgFileSlip = null;
    String imgFileName;
    LoadingDialog loadingDialog;

    @BindView(R.id.profileAct_civ_photo)
    CircleImageView iv_userImg;

    @BindView(R.id.profileAct_tv_name)
    TextView tv_name;
    @BindView(R.id.profileAct_tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.profileAct_tv_email)
    TextView tv_email;

    @OnClick(R.id.profileAct_rl_changePhoto)
    void changeImg() {
        Utils.navigateTo(getActivity(), ChangeProfileImgActivity.class);
    }

    @OnClick(R.id.profileAct_ll_changePwd)
    void changePwd() {
        Utils.navigateTo(getActivity(), ChangePwdActivity.class);
    }

    @OnClick(R.id.profileAct_btn_logout)
    void logout() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", ((dialogInterface, i) -> {
                    sessionManager.clearSharedPref(); //del pref data
                    Utils.navigateClearTo(getActivity(), LoginActivity.class);
                }))
                .setNegativeButton("No", null)
                .show();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    private void init() {
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());

        Utils.loadProfileImage(getActivity(), sessionManager.getProfileImg(), iv_userImg);
        tv_name.setText(sessionManager.getUserName());
        tv_mobile.setText(sessionManager.getUserMobile());
        tv_email.setText(sessionManager.getEmailId());
    }
}