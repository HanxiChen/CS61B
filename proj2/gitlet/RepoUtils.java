package gitlet;

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
     * 获取指定 commit 的 List<String> parents
     */
    static List<String> getParentsList(Commit commit) {
        Commit c = commit;

        List<String> parentsList = new ArrayList<>();
        while (c.getParents().size() > 0) {
            parentsList.add(c.getParents().get(0));
            c = readObject(join(GITLET_COMMITS, c.getParents().get(0) + ".txt"), Commit.class);
        }

        return parentsList;
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
    static void printLogCommit(Commit c) {
        System.out.println("===");
        System.out.println("commit " + c.getId());

        if (isMergeCommit(c)) {
            System.out.println(mergeParents(c));
        }
        System.out.println("Date: " + c.getTimeStamp());
        System.out.println(c.getMessage());
        System.out.println();
    }
    static void printLogCommit(String fileName) {
        printLogCommit(readObject(join(GITLET_COMMITS, fileName), Commit.class));
    }
    private static String mergeParents(Commit c) {
        String parent1 = c.getParents().get(0);

        String parent2 = c.getParents().get(1);

        return "Merge: " + parent1.substring(0, 7) + " " + parent2.substring(0, 7);
    }

    /**
     * 判断 给定commit 是否是 mergeCommit
     */
    static boolean isMergeCommit(Commit c) {
        return c.getParents().size() == 2;
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
     * 寻找 branch 和 当前分支 的分割点
     */
    static Commit getSpiltPoint(Commit branchCommit, Commit currentCommit) {
        List<String> pBranchCommit = getParentsList(branchCommit);
        List<String> pCurrentCommit = getParentsList(currentCommit);

        if (isMergeCommit(branchCommit)) {
            pBranchCommit.add(1, branchCommit.getParents().get(1));
        }
        if (isMergeCommit(currentCommit)) {
            pCurrentCommit.add(1, currentCommit.getParents().get(1));
        }

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
     */
    static String mergeContents(String commitID, String branchID) {
        String front = "<<<<<<< HEAD\n";
        String middle = "=======\n";
        String rear = ">>>>>>>\n";

        byte[] commitC = readObject(join(GITLET_BLOBS, commitID + ".txt"), Blob.class).getContent();
        String commit = new String(commitC);

        String branch = "";
        if (branchID != null) {
            byte[] branchC = readObject(
                    join(GITLET_BLOBS, branchID + ".txt"), Blob.class).getContent();
            branch = new String(branchC);
        }

        return front + commit + middle + branch + rear;
    }

    /**
     * 处理 merge操作后 commit 的 parent
     */
    static void modifyMergeParent(String master, String branchID) {
        Commit mergeCommit = RepoUtils.getCurrentCommit(master);

        mergeCommit.getParents().add(1, branchID);

        Utils.writeObject(join(GITLET_COMMITS, mergeCommit.getId() + ".txt"), mergeCommit);
    }
}
