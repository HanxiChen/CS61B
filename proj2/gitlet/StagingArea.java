package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

/** Represents a gitlet StagingArea.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HANXICHEN
 */
public class StagingArea implements Serializable {
    private HashMap<String, String> addFiles;

    private ArrayList<String> removedFiles;

    public StagingArea() {
        addFiles = new HashMap<>();
        removedFiles = new ArrayList<>();
    }

    public boolean isNewBlobs(String fileName) {
        return !addFiles.containsKey(fileName);
    }

    public boolean isEmpty() {
        return addFiles.isEmpty() && removedFiles.isEmpty();
    }

    public void addBlobs(String fileName, String id) {
        addFiles.put(fileName, id);
    }

    public void addRm(String fileName) {
        removedFiles.add(fileName);
    }

    public void delBlobs(String fileName) {
        addFiles.remove(fileName);
    }

    public void delRm(String fileName) {
        removedFiles.remove(fileName);
    }

    public void clear() {
        addFiles.clear();
        removedFiles.clear();
    }

    public void save() {
        Utils.writeObject(STAGING, this);
    }
    public Map<String, String> getAddFiles() {
        return addFiles;
    }

    public ArrayList<String> getRemovedFiles() {
        return removedFiles;
    }
}
