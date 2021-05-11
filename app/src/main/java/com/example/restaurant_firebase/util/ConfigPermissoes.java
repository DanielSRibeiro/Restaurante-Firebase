package com.example.restaurant_firebase.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class ConfigPermissoes {

    public static void validarPermissoes(Activity activity, String[] permissoes, int requestCode){
        if(Build.VERSION.SDK_INT > 23){
            ArrayList<String> listPermissoes = new ArrayList<>();
            for (String permissao: permissoes) {
                boolean temPermissao = ActivityCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao){
                    listPermissoes.add(permissao);
                }
            }

            if (!listPermissoes.isEmpty()){
                ActivityCompat.requestPermissions(activity, listPermissoes.toArray(new String[listPermissoes.size()]), requestCode);
            }
        }
    }
}
