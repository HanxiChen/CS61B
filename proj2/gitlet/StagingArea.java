package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static gitlet.BlobUtils.*;
import static gitlet.Utils.*;
import static gitlet.Repository.*;

/** Represents a gitlet StagingArea.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HANXICHEN
 */
public class StagingArea implements Serializable {
    private Map<String, String> blobs;

    private ArrayList<String> removedFiles;

    public StagingArea() {
        blobs = new HashMap<>();
        removedFiles = new ArrayList<>();
    }

    public void addBlobs(String fileName, String id) {
        blobs.put(fileName, id);
    }

    public void addRmFile(String fileName) {
        removedFiles.add(fileName);
    }

    public void delBlobs(Blob blob) {
        blobs.remove(getFileName(blob));
    }

    public void delRmFile(Blob blob) {
        removedFiles.remove(getFileName(blob));
    }

    public void saveAdd() {
        writeObject(STAGING_ADD, this);
    }

    public void saveRm() {
        writeObject(STAGING_DEL, this);
    }

    public void clear() {
        blobs.clear();
        removedFiles.clear();
    }
}
