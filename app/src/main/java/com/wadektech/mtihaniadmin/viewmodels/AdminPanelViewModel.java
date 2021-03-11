package com.wadektech.mtihaniadmin.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.wadektech.mtihaniadmin.pojo.PDFObject;
import com.wadektech.mtihaniadmin.pojo.User;
import com.wadektech.mtihaniadmin.repository.MtihaniRepository;
import com.wadektech.mtihaniadmin.ui.MtihaniRevise;
import com.wadektech.mtihaniadmin.utils.Constants;
import com.wadektech.mtihaniadmin.utils.SingleLiveEvent;

import java.util.List;

public class AdminPanelViewModel extends ViewModel {
    private MtihaniRepository mRepository;
    private MutableLiveData<String> returningUser;

    public AdminPanelViewModel() {
        mRepository = MtihaniRepository.getInstance();
    }

    public void getAdminPassword() {
        mRepository.getAdminPassword();
    }

    public SingleLiveEvent<String> getAdminPasswordResponse() {
        return mRepository.getAdminPasswordResponse();
    }

    public void uploadPDF(Uri pdfUri, String category,String fileName) {
        mRepository.uploadPDFFile(pdfUri,category,fileName);
    }

    public void uploadMultiplePDFs(List<PDFObject> pdfObjects, String category){
        mRepository.uploadPDFs(pdfObjects,category);
    }

    public SingleLiveEvent<String> getUploadProgress(){
        return mRepository.getUploadResponse();
    }

    public LiveData<String> getReturningUser() {
        return returningUser;
    }

    public void signInReturningUser(String email){
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if (!task.getResult().isEmpty()) {
                                User user = task.getResult().toObjects(User.class).get(0);
                                if(user != null){
                                    SharedPreferences pfs = MtihaniRevise
                                            .getApp()
                                            .getApplicationContext()
                                            .getSharedPreferences(Constants.myPreferences, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pfs.edit();
                                    editor.putString(Constants.userName,user.getUsername());
                                    editor.putString(Constants.email,user.getEmail());
                                    editor.putString(Constants.userId,user.getUserId());
                                    editor.putString(Constants.imageURL,user.getImageURL());
                                    editor.commit();
                                    Log.d("ChatActivityViewModel","userId is"+user.getUserId()
                                            +" username is: "+user.getUsername()+" email is: "+user.getEmail()
                                            +" imageURL is "+user.getImageURL());
                                    returningUser.setValue("success");
                                }else{
                                    returningUser.setValue("fail");
                                }
                            }else{
                                returningUser.setValue("fail");
                            }
                        }
                    } else {
                        returningUser.setValue("error");
                        if (task.getException() != null) {
                            Log.d("ChatActivityViewModel", "error is:" + task.getException().toString());
                        }
                    }
                });
    }
}
