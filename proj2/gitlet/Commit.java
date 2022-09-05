package gitlet;

// TODO: any imports you need here
import static gitlet.Utils.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HANXICHEN
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String id;

    private HashMap<String, String> blobs;  //<fileName, ID>

    private List<String> parents;

    private Date time;

    private String timeStamp;

    private String message;

    private File commitFile;

    public Commit(String message, HashMap<String, String> blobs, List<String> parents) {
        time = new Date();
        timeStamp = generateTimeStamp();
        id = generateId();
        this.blobs = blobs;
        this.parents = parents;
        this.message = message;
        commitFile = generateFile();
    }

    private String generateId() {
        return Utils.sha1((Object) serialize(this));
    }

    private String generateTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(time);
    }

    private File generateFile() {
        return Utils.join(GITLET_COMMITS, id + ".txt");
    }

    public void save() {
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeObject(commitFile, this);
    }

    public String getId() {
        return id;
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public List<String> getParents() {
        return parents;
    }

    public Date getTime() {
        return time;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public File getCommitFile() {
        return commitFile;
    }
}
