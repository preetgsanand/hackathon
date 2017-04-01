package vince.jobtracking.Database;

import com.orm.SugarRecord;

/**
 * Created by vince on 4/1/17.
 */
public class Part extends SugarRecord {

    public Part() {

    }

    private String name;
    private long webid;
    private long job;
    private String added;
    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWebid() {
        return webid;
    }

    public void setWebid(long webid) {
        this.webid = webid;
    }

    public long getJob() {
        return job;
    }

    public void setJob(long job) {
        this.job = job;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
