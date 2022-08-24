package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

/** Represents a gitlet Blob.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HANXICHEN
 */
public class Blob implements Serializable {
    private File sourceFile;        // 源文件

    private byte[] content;         // 源文件中的内容

    private String id;              // 独特的SHA-1 ID

    private File blobFile;          // writeObject()保存的文件

    public Blob(File sourceFile) {
        this.sourceFile = sourceFile;
        content = readContents(sourceFile);
        id = sha1(content, sourceFile.getPath());
        blobFile = join(Repository.GITLET_BLOBS, id);
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public byte[] getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public File getBlobFile() {
        return blobFile;
    }

    public void save() {
        File parentFile = blobFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        writeObject(blobFile, this);
    }
}

