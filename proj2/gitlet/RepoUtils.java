package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class RepoUtils {
    /**
     * 初始化时在 .gitlet中各类创建文件夹
     */
    static void generateSystemDir() {
        GITLET_DIR.mkdirs();

        GITLET_BLOBS.mkdirs();

        GITLET_COMMITS.mkdirs();

        GITLET_STAGING.mkdirs();

        STAGING_ADD.mkdirs();

        STAGING_DEL.mkdirs();

        GITLET_BRANCHES.mkdirs();
    }




}
