package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Repository.*;

/** Represents a gitlet StagingArea.
 *  @author HANXICHEN
 */
public class StagingArea implements Serializable {
    private HashMap<String, String> addFiles;

    private HashMap<String, String> removedFiles;

    public StagingArea() {
        addFiles = new HashMap<>();
        removedFiles = new HashMap<>();
    }

    public boolean isNewBlobs(String fileName) {
        return !addFiles.containsKey(fileName);
    }

    public boolean isEmpty() {
        return addFiles.isEmpty() && removedFiles.isEmpty();
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

    public Map<String, String> getRemovedFiles() {
        return removedFiles;
    }
}
