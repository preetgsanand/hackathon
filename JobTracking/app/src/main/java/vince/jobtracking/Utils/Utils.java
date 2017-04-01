package vince.jobtracking.Utils;

import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vince.jobtracking.AdminActivity;
import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.R;

/**
 * Created by vince on 3/17/17.
 */
public class Utils {

    public static int activity;
    public static int MainAdminActivity;

    public static long DateToLong(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        try {
            date = date.split("T")[0];
            Date d = (Date) formatter.parse(date);
            long mills = d.getTime();
            return mills;

        }
        catch (Exception e) {
            Log.e("Date - Utils",e.toString());
        }
        return 0;
    }

    public static String LongToDate(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String LongToDjangoDate(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static JSONObject JobToUserSubmitJson(Job job) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", job.getWebid());
            jsonObject.put("name",job.getName());
            jsonObject.put("user", User.findById(User.class,1).getWebid());
            jsonObject.put("description",job.getDescription());
            jsonObject.put("deadline",Utils.LongToDjangoDate(job.getDeadline())+"T00:00:00Z");
            jsonObject.put("added",Utils.LongToDjangoDate(job.getAdded())+"T00:00:00Z");
            jsonObject.put("status",job.getStatus());
            jsonObject.put("submitrequest",1);
        }
        catch (Exception e) {
            Log.e("JobToJson ",e.toString());
        }
        return jsonObject;
    }

    public static JSONObject JobToAdminAcceptJson(Job job) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", job.getWebid());
            jsonObject.put("name",job.getName());
            jsonObject.put("user",User.findById(User.class,1).getWebid());
            jsonObject.put("description",job.getDescription());
            jsonObject.put("deadline",Utils.LongToDjangoDate(job.getDeadline())+"T00:00:00Z");
            jsonObject.put("added",Utils.LongToDjangoDate(job.getAdded())+"T00:00:00Z");
            jsonObject.put("status",2);
            jsonObject.put("submitrequest",2);
        }
        catch (Exception e) {
            Log.e("JobToJson ",e.toString());
        }
        return jsonObject;
    }

    public static List<Job> JSONArrayToJob(JSONArray response) {
        List<Job> jobs = new ArrayList<>();
        try {
            for(int i = 0 ; i < response.length() ; i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                Job job = new Job();
                job.setWebid(jsonObject.getLong("id"));
                job.setName(jsonObject.getString("name"));
                job.setAdded(Utils.DateToLong(jsonObject.getString("added")));
                job.setDeadline(Utils.DateToLong(jsonObject.getString("deadline")));
                job.setDescription(jsonObject.getString("description"));
                JSONArray user = jsonObject.getJSONArray("user");
                String userId = "";
                for(int j = 0 ; j < user.length() ; j++) {
                    int userInt = user.getInt(j);
                    if(j == user.length()-1) {
                        userId = userId+userInt;
                    }
                    else {
                        userId = userId+userInt+" ";
                    }
                }
                job.setUserId(userId);
                Log.e("userid",job.getUserId());
                job.setStatus(jsonObject.getInt("status"));
                job.setSubmitRequest(jsonObject.getInt("submitrequest"));
                jobs.add(job);

            }
            return jobs;
        }
        catch (Exception e) {
            Log.e("Json Parsing Job Utils",e.toString());
            return null;
        }
    }

    public static JSONObject JobToJson(Job job) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",job.getName());
            JSONArray user = jsonObject.getJSONArray("user");
            String userId = "";
            for(int j = 0 ; j < user.length() ; j++) {
                int userInt = user.getInt(j);
                if(j == user.length()-1) {
                    userId = userId+userInt;
                }
                else {
                    userId = userId+userInt+" ";
                }
            }
            job.setUserId(userId);
            jsonObject.put("description",job.getDescription());
            jsonObject.put("deadline",Utils.LongToDjangoDate(job.getDeadline())+"T00:00:00Z");
            jsonObject.put("added",Utils.LongToDjangoDate(job.getAdded())+"T00:00:00Z");
            jsonObject.put("status",job.getStatus());
            jsonObject.put("submitrequest",job.getSubmitRequest());
        }
        catch (Exception e) {
            Log.e("JobToJson ",e.toString());
        }
        return jsonObject;
    }

    public static Job JsonToJob(JSONObject jsonObject) {
        try {
                Job job = new Job();
                job.setWebid(jsonObject.getLong("id"));
                job.setName(jsonObject.getString("name"));
                job.setAdded(Utils.DateToLong(jsonObject.getString("added")));
                job.setDeadline(Utils.DateToLong(jsonObject.getString("deadline")));
                job.setDescription(jsonObject.getString("description"));
                JSONArray user = jsonObject.getJSONArray("user");
                String userId = "";
                for(int j = 0 ; j < user.length() ; j++) {
                    userId.concat(user.getInt(j)+"");
                }
                job.setUserId(userId);
                Log.e("userid",job.getUserId()+"");
                job.setStatus(jsonObject.getInt("status"));
                job.setSubmitRequest(jsonObject.getInt("submitrequest"));
            return job;
        }
        catch (Exception e) {
            Log.e("Json Parsing Job Utils",e.toString());
            return null;
        }
    }

    public static User JsonToUser(JSONObject userObject) {
        try {
            User user = new User();
            user.setWebid(userObject.getLong("id"));
            user.setName(userObject.getString("name"));
            user.setEmail(userObject.getString("email"));
            user.setPhone(userObject.getString("phone"));
            user.setDob(Utils.DateToLong(userObject.getString("dob")));
            user.setDepartment(userObject.getLong("department"));
            user.setRole(userObject.getLong("role"));
            return user;
        }
        catch (Exception e) {
            Log.e("Json Parsing Utils User",e.toString());
            return null;
        }
    }

    public static List<User> JSONArrayTOUser(JSONArray response) {
        List<User> users = new ArrayList<>();
        try {
            for(int i = 0 ; i < response.length() ; i++) {
                JSONObject userObject = response.getJSONObject(i);
                User user = new User();
                user.setWebid(userObject.getLong("id"));
                user.setName(userObject.getString("name"));
                user.setEmail(userObject.getString("email"));
                user.setPhone(userObject.getString("phone"));
                user.setDob(Utils.DateToLong(userObject.getString("dob")));
                user.setDepartment(userObject.getLong("department"));
                user.setRole(userObject.getLong("role"));
                users.add(user);
            }

            return users;
        }
        catch (Exception e) {
            Log.e("Json Parsing Utils User",e.toString());
            return null;
        }
    }

    public static JSONObject UserToJson(User user) {
        JSONObject userObject = new JSONObject();
        try {
            userObject.put("name", user.getName());
            userObject.put("phone",user.getPhone());
            userObject.put("email",user.getEmail());
            userObject.put("dob",Utils.LongToDjangoDate(user.getDob())+"T00:00:00Z");
            userObject.put("department",user.getDepartment());
            return userObject;
        }
        catch (Exception e){
            Log.e("JSON Construction ",e.toString());
            return null;
        }
    }


}
