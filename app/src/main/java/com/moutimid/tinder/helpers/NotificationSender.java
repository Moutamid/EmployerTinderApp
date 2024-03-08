package com.moutimid.tinder.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationSender {
    public static void sendNotificationToUser(Context context,String receiver, String title, String message) {
        sendFCMPush(context, receiver, title, message);
    }

    public static void sendFCMPush(Context context, String receiverToken, String name, String message) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", name);
            notifcationBody.put("message", message);
            notification.put("to", receiverToken);
            notification.put("data", notifcationBody);
            Log.e("DATAAAAAA", notification.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, Config.NOTIFICATIONAPIURL, notification,
                response -> {
                    Log.e("True", response + "");
//                    Toast.makeText(BookingActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Responce", response.toString());
                },
                error -> {
                    Log.e("False", error + "");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=" + Config.ServerKey);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

}
