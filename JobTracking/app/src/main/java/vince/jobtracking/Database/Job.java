package vince.jobtracking.Database;

import com.orm.SugarRecord;


/**
 * Created by vince on 3/11/17.
 */
public class Job extends SugarRecord {

    private long webid;
    private String userId;
    private String name;
    private long deadline;
    private String description;
    private long added;
    private int submitRequest;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public Job() {

    }

    public int getSubmitRequest() {
        return submitRequest;
    }

    public void setSubmitRequest(int submitRequest) {
        this.submitRequest = submitRequest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }
}
