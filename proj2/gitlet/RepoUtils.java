package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        GITLET_BRANCHES.mkdirs();
    }

    /**
     * 从 HEAD.txt 获取 当前的commit
     */
    static Commit getCurrentCommit(String master) {
        return Utils.readObject(join(GITLET_COMMITS, master + ".txt"), Commit.class);
    }

    /**
     * 获取指定 ID 的 commit
     */
    static Commit getCommit(String ID) {
        return readObject(join(GITLET_COMMITS, ID + ".txt"), Commit.class);
    }

    /**
     * 打印 log 中 单个commit的信息
     */
    static void printLogCommit(String fileName) {
        Commit c = readObject(join(GITLET_COMMITS, fileName + ".txt"), Commit.class);
        System.out.println("===");
        System.out.println("commit " + fileName);

        if (isMergeCommit(c.getMessage())) {
            System.out.println(mergeParents(c));
        }
        System.out.println("Date: " + c.getTime());
        System.out.println(c.getMessage());
        System.out.println();
    }
    private static boolean isMergeCommit(String message) {
        return message.contains("Merged") && message.contains(" into ");
    }
    private static String mergeParents(Commit c) {
        String parentID = c.getParents().get(0);

        String master = Utils.readContentsAsString(GITLET_HEAD);
        String masterID = Utils.readContentsAsString(join(GITLET_BRANCHES, master + ".txt"));

        return "Merge: " + parentID.substring(0,8) + masterID.substring(0,8);
    }

    /**
     * 打印 status
     */
    static void printStatus(List<String> branches, List<String> addFiles, List<String> removeFiles) {
        System.out.println("=== Branches ===");
        for (String branch : branches) {
            System.out.println(branch);
        }

        System.out.println("=== Staged Files ===");
        for (String stagedFile : addFiles) {
            System.out.println(stagedFile);
        }

        System.out.println("=== Removed Files ===");
        for (String removedFiles : removeFiles) {
            System.out.println(removedFiles);
        }

        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
    }


    public static byte[] mergeContents(String commitID, String branchID) {
        byte[] front = "<<<<<<< HEAD\n".getBytes(StandardCharsets.UTF_8);
        byte[] middle = "=======\n".getBytes(StandardCharsets.UTF_8);
        byte[] rear = ">>>>>>>\n".getBytes(StandardCharsets.UTF_8);

        byte[] commit = Utils.readContents(join(GITLET_BLOBS, commitID + ".txt"));
        byte[] branch = Utils.readContents(join(GITLET_BLOBS, branchID + ".txt"));

        byte[] content = new byte[front.length + commit.length + middle.length + branch.length + rear.length];

        System.arraycopy(front, 0, content, 0, front.length);
        System.arraycopy(commit, 0, content, front.length, commit.length);
        System.arraycopy(middle, 0, content, commit.length, middle.length);
        System.arraycopy(branch, 0, content, middle.length, branch.length);
        System.arraycopy(rear, 0, content, branch.length, rear.length);

        return content;
    }
}
