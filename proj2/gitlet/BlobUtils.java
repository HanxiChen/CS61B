package gitlet;

import static gitlet.Repository.GITLET_BLOBS;
import static gitlet.Utils.join;
import static gitlet.Utils.readObject;

public class BlobUtils {
    /**
     * @param id
     * @return blob
     */
    static Blob getBlob(String id) {
        return readObject(join(GITLET_BLOBS, id), Blob.class);
    }

    /**
     * @param blob
     * @return filename
     */
    static String getFileName(Blob blob) {
        return blob.getSourceFile().toString();
    }


}
