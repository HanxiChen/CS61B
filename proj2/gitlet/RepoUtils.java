package gitlet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    static Commit getCommit(String commitId) {
        return readObject(join(GITLET_COMMITS, commitId + ".txt"), Commit.class);
    }

    /**
     * 获取指定　ID　的　parentCommit
     */
    static Commit getParentCommit(Commit commit) {
        String parentCommitId = commit.getParents().get(0);
        return getCommit(parentCommitId);
    }
    /**
     * 获取 当前工作目录 List
     */
    static List<String> getWorkFile() {
        List<String> workFile = Utils.plainFilenamesIn(CWD);
        List<String> fileList = new ArrayList<>();
        for (String file: workFile) {
            if (file.contains(".txt")) {
                fileList.add(file);
            }
        }
        return fileList;
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
        System.out.println("Date: " + c.getTimeStamp());
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

        return "Merge: " + parentID.substring(0, 7) + " " + masterID.substring(0, 7);
    }

    /**
     * 打印 status
     */
    static void printStatus(List<String> branches, List<String> addFiles, List<String> removeFiles,
                            List<String> modifiedFiles, List<String> untrackedFiles) {

        System.out.println("=== Branches ===");

        System.out.println(branches.get(0));
        for (int i = 1; i < branches.size(); i++) {
            String branch = branches.get(i);
            System.out.println(branch.substring(0, branch.length() - 4));
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String stagedFile : addFiles) {
            System.out.println(stagedFile);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String removedFile : removeFiles) {
            System.out.println(removedFile);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String modifiedFile : modifiedFiles) {
            System.out.println(modifiedFile);
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String untrackedFile : untrackedFiles) {
            System.out.println(untrackedFile);
        }
        System.out.println();
    }

    /**
     * merge failure case
     */

    /**
     * 寻找 branch 和 当前分支 的分割点
     */
    static Commit getSpiltPoint(Commit branchCommit, Commit currentCommit) {
        List<String> pBranchCommit = branchCommit.getParents();
        List<String> pCurrentCommit = currentCommit.getParents();
        for (String commitID: pBranchCommit) {
            if (pCurrentCommit.contains(commitID)) {
                return Utils.readObject(
                        join(GITLET_COMMITS, commitID + ".txt"), Commit.class);
            }
        }
        return null;
    }

    /**
     * conflict 存在时 文件内容
     * @return
     */
    static String mergeContents(String commitID, String branchID) {
        String front = "<<<<<<< HEAD\n";
        String middle = "\n=======\n";
        String rear = "\n>>>>>>>\n";

        byte[] commitContent = readObject(join(GITLET_BLOBS, commitID + ".txt"), Blob.class).getContent();
        String commit = new String(commitContent);

        byte[] branchContent = readObject(join(GITLET_BLOBS, branchID + ".txt"), Blob.class).getContent();
        String branch = new String(branchContent);

        return front + commit + middle + branch + rear;
    }
}
