package vince.jobtracking.Database;

import com.orm.SugarRecord;

/**
 * Created by vince on 3/7/17.
 */
public class User extends SugarRecord{
    private long webid;
    private String name;
    private String phone;
    private String email;
    private long dob;
    private long department;

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    private long role;

    public User() {

    }


    public long getWebid() {
        return webid;
    }

    public void setWebid(long webid) {
        this.webid = webid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public long getDepartment() {
        return department;
    }

    public void setDepartment(long department) {
        this.department = department;
    }




}
