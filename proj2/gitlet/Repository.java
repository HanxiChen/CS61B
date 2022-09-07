package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static gitlet.Utils.*;
import static gitlet.RepoUtils.*;

/** Represents a gitlet repository.
 *  @author HANXICHEN
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    private String HEAD = "master";
    private String MASTER = "";
    private StagingArea stagingArea = null;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     *
     * .gitlet
     *      | -- blobs          //所有文件 blobs
     *      | -- commits        //commit指令后保存
     *      | -- stagingArea    //暂存区
     *      | -- branches       //装所有分支名称
     *          |-- master.txt      //指向 currentCommit的 SHA-1 id
     *          |-- HEAD.txt        //HEAD所指分支
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    
    public static final File GITLET_BLOBS = join(GITLET_DIR, "blobs");

    public static final File GITLET_COMMITS = join(GITLET_DIR, "commits");
    
    public static final File GITLET_STAGING = join(GITLET_DIR, "stagingArea");

    public static final File GITLET_BRANCHES = join(GITLET_DIR, "branches");

    public static final File GITLET_HEAD = join(GITLET_BRANCHES, "HEAD.txt");

    public static final File STAGING = join(GITLET_STAGING, "staging.txt");

    public Repository() {
        if (GITLET_DIR.exists()) {
            HEAD = Utils.readContentsAsString(GITLET_HEAD);
            MASTER = Utils.readContentsAsString(join(GITLET_BRANCHES, HEAD + ".txt"));
            stagingArea = Utils.readObject(STAGING, StagingArea.class);
        }
    }

    /**
     * 初始化
     * 判断是否已经存在一个Gitlet文件工作目录,
     *      有则打印 A Gitlet version-control system already exists in the current directory. 后退出
     * 创建一个Gitlet文件工作目录, .gitlet /blobs /commits /stagingArea /branches
     * 提交一个 initial commit, 创建一个 commit 对象, 序列化存入 commits 文件夹中
     * 有一个分支HEAD = "master"
     */
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        }

        RepoUtils.generateSystemDir();

        // 初始化initial commit
        Commit initialCommit = new Commit("initial commit", new HashMap<>(), new ArrayList<>());
        initialCommit.save();

        //分支master 和 HEAD
        MASTER = initialCommit.getId();
        Utils.writeContents(join(GITLET_BRANCHES, "master.txt"), MASTER);

        Utils.writeContents(GITLET_HEAD, HEAD);

        //初始化暂存区stagingArea
        stagingArea = new StagingArea();
        try {
            STAGING.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stagingArea.save();
    }

    /**
     * 将这个文件加入stagingArea,用StagingArea类中的方法
     * 如果文件的当前版本和当前的commit版本相同,则不add;如果已经存在,将其从stagingArea中删除(更改回原始版本)
     * 如果文件在rm命令前已经在暂存区,从暂存区中删除即可(rm命令)
     * 如果文件不存在,打印 File does not exist.
     */
    public void add(String fileName) {
        File newFile = join(CWD, fileName);

        if (newFile.exists()) {
            Blob newBlob = new Blob(newFile);
            //如果文件的当前版本和当前的commit版本中的文件相同,则不add;如果已经存在,将其从stagingArea中删除(更改回原始版本)
            if (!RepoUtils.getCurrentCommit(MASTER).getBlobs().containsValue(newBlob.getId())) {
                stagingArea.getAddFiles().put(fileName, newBlob.getId());
            }

            //如果文件在rm命令前已经在暂存区,从暂存区中删除即可(rm命令)
            stagingArea.getRemovedFiles().remove(fileName);

            stagingArea.save();

        } else {
            System.out.print("File does not exist.");
        }
    }

    /**
     * 获取HEAD所指的commit,得到其中的blobs和stagingArea中的addFile和removedFile
     * 将staging中的add和remove全部在blobs中处理
     * 新建一个commit对象,其中包含日期时间还有message,还有一个独特的SHA-1 id
     * 序列化后保存进 .gitlet/commits 文件夹中,内容是暂存区中的文件
     * commit结束后,stagingArea会被清空
     * 如果没有文件被staged,打印 No changes added to the commit.
     * 如果commit没有message,打印 Please enter a commit message.
     */
    public void commit(String message) {
        //判断暂存区中文件暂存情况以及message非不非空
        if (stagingArea == null || stagingArea.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        } else if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        Commit curCommit = getCurrentCommit(MASTER);
        HashMap<String, String> blobs = curCommit.getBlobs();
        ArrayList<String> getAdd = new ArrayList<>(stagingArea.getAddFiles().keySet());
        ArrayList<String> getRemoved = new ArrayList<>(stagingArea.getRemovedFiles().keySet());

        //对当前commit中的 blobs 进行 put和remove处理
        for (String fileName : getAdd) {
            blobs.put(fileName, stagingArea.getAddFiles().get(fileName));
            Blob blob = new Blob(new File(CWD, fileName));
            Utils.writeObject(join(GITLET_BLOBS, blob.getId() + ".txt"), blob);
        }
        for (String fileName : getRemoved) {
            blobs.remove(fileName);
        }

        //处理parent
        List<String> parents = curCommit.getParents();
        parents.add(0, curCommit.getId());

        //创建新的commit,并将HEAD指向它
        Commit commit = new Commit(message, blobs, parents);
        commit.save();

        MASTER = commit.getId();
        join(GITLET_BRANCHES, HEAD + ".txt").delete();
        Utils.writeContents(join(GITLET_BRANCHES, HEAD + ".txt"), MASTER);
        Utils.writeObject(join(GITLET_COMMITS, MASTER + ".txt"), commit);

        stagingArea.clear();
        stagingArea.save();
    }

    /**
     * 如果文件在 stagingArea 中,直接将其删除
     * 如果文件在 当前commit 中被追踪,删除; 没有被追踪,从工作目录中删除
     * 如果文件没有在 stagingArea 和 被当前commit被追踪,打印 No reason to remove the file.
     */
    public void rm(String fileName) {
        Commit curCommit = RepoUtils.getCurrentCommit(MASTER);
        ArrayList<String> blobsFileName = new ArrayList<>(curCommit.getBlobs().keySet());

        // 判断是否在 stagingArea中
        boolean isInStagingArea = stagingArea.getAddFiles().containsKey(fileName);
        if (isInStagingArea) {
            stagingArea.getAddFiles().remove(fileName);
        }

        // 判断是否被追踪(是否在当前commit的blobs中)
        boolean isTracked = false;
        for (String file : blobsFileName) {
            if (fileName.equals(file)) {
                isTracked = true;
                break;
            }
        }

        if (isTracked) {
            String id = new Blob(new File(CWD, fileName)).getId();
            stagingArea.getRemovedFiles().put(fileName, id);
            restrictedDelete(fileName);
        }

        if (!isTracked && !isInStagingArea) {
            System.out.println("No reason to remove the file.");
        }

        stagingArea.save();
    }

    /**
     * 从当前的commit开始,沿着提交树显示每次提交的信息
     * ===
     * commit SHA-1 id
     * Merge: id1 id2
     * Date: 时间戳
     * Message
     * 合并提交Merge, id的前七位位数字组成,按顺序排列,第一个合并时所在的分支,第二个被合并的分支
     */
    public void log() {
        Commit commit = RepoUtils.getCurrentCommit(MASTER);
        ArrayList<String> parents = new ArrayList<>(commit.getParents());

        printLogCommit(commit.getId());
        for (String id : parents) {
            printLogCommit(id + ".txt");
        }

    }

    /**
     * 显示之前所有的commit,提交的顺序不重要
     * Utils中有一个有用的方法可以遍历一个目录中的文件
     */
    public void globalLog() {
        List<String> fileNameList = Utils.plainFilenamesIn(GITLET_COMMITS);

        if (fileNameList == null) {
            return;
        }
        for (String fileName : fileNameList) {
            printLogCommit(fileName);
        }
    }

    /**
     * 用commit中的message寻找
     * 有多个将id打印在不同行,多个message需要放在引号里
     * Utils中有一个有用的方法可以遍历一个目录中的文件
     * 如果没有符合的commit,打印 Found no commit with that message.
     */
    public void find(String message) {
        List<String> fileNameList = Utils.plainFilenamesIn(GITLET_COMMITS);
        List<String> idList = new ArrayList<>();

        if (fileNameList == null) {
            return;
        }
        for (String fileName : fileNameList) {
            Commit c = readObject(join(GITLET_COMMITS, fileName), Commit.class);

            if (c.getMessage().contains(message)) {
                idList.add(c.getId());
            }
        }

        if (!idList.isEmpty()) {
            for (String id : idList) {
                System.out.println(id);
            }
        } else {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * 显示当前的分支  staged files  删除的文件  未提交的文件
     *  === Branches ===
     * *master
     * other-branch
     *
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     *
     * === Removed Files ===
     * goodbye.txt
     *
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     *
     * === Untracked Files ===
     * random.stuff
     */
    public void status() {
        if (!join(GITLET_DIR).exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Commit currentCommit = RepoUtils.getCurrentCommit(MASTER);
        List<String> workFile = Utils.plainFilenamesIn(CWD);
        HashMap<String, String> blobs = currentCommit.getBlobs();
        List<String> blobsFile = new ArrayList<>(currentCommit.getBlobs().keySet());

        // 得到branches
        ArrayList<String> branches = new ArrayList<>(Utils.plainFilenamesIn(GITLET_BRANCHES));
        branches.remove("HEAD.txt");
        branches.remove(HEAD + ".txt");
        branches.add("*" + HEAD);
        Collections.sort(branches);

        //得到staginArea中的 addFiles 和 removeFiles
        List<String> addFiles = new ArrayList<>(stagingArea.getAddFiles().keySet());
        Collections.sort(addFiles);

        List<String> removeFiles = new ArrayList<>(stagingArea.getRemovedFiles().keySet());
        Collections.sort(removeFiles);

        // Modifications Not Staged For Commit
        List<String> modifiedFiles = new ArrayList<>();
        for (String fileName : workFile) {
            if (blobs.size() != 0 && (blobs.containsKey(fileName)
                    || addFiles.contains(fileName))) {                      // 在当前提交中被追踪
                Blob blob = new Blob(join(CWD, fileName));
                if (!blobs.get(fileName).equals(blob.getId())) {
                    modifiedFiles.add(fileName + " (modified)");
                }
            }
        }

        for (String fileName : addFiles) {
            if (!workFile.contains(fileName)) {
                modifiedFiles.add(fileName + " (deleted)");
            }
        }

        for (String fileName : blobsFile) {
            if (!addFiles.contains(fileName) && !workFile.contains(fileName)) {
                modifiedFiles.add(fileName + " (deleted)");
            }
        }
        Collections.sort(modifiedFiles);

        // Untracked Files
        List<String> untrackedFiles = new ArrayList<>();
        for (String file : workFile) {
            if (file.contains(".txt") && !blobs.containsKey(file)
                    && !stagingArea.getAddFiles().containsKey(file)) {
                untrackedFiles.add(file);
            }
        }
        Collections.sort(untrackedFiles);

        // 按格式打印
        printStatus(branches, addFiles, removeFiles, modifiedFiles, untrackedFiles);
    }

    /**
     * 将commit中的文件存入工作目录中,工作目录有则覆盖,新版本的文件不会被暂存
     */
    public void checkout(String s, String fileName) {
        Commit currentCommit = RepoUtils.getCurrentCommit(MASTER);
        HashMap<String, String> blobs = currentCommit.getBlobs();

        //获取currentCommit
        if (!blobs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        checkout(currentCommit, fileName);
    }

    /**
     * 获取指定id的commit中的文件,将其存入工作的目录,如果已存在则覆盖,新版本的文件不会被暂存
     */
    public void checkout(String commitId, String s, String fileName) {
        List<String> commitList = Utils.plainFilenamesIn(GITLET_COMMITS);

        for (String id: commitList) {
            if (id.contains(commitId)) {
                commitId = id.substring(0, id.length() - 4);
                break;
            }
        }

        //获取指定 commit
        Commit commit = RepoUtils.getCommit(commitId);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
        } else if (!commit.getBlobs().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
        } else {
            checkout(commit, fileName);
        }
    }

    /**
     * 在指定branch取出当前commit的所有文件并存入当前工作目录,有则覆盖
     * 当前分支中被tracked的文件如果在指定branch不存在则被删除,除非两个分支相同,否则stagingArea会被清空
     * 当前指令最后HEAD分支变为给定的分支
     */
    public void checkout(String branchName) {
        List<String> branchList = Utils.plainFilenamesIn(GITLET_BRANCHES);
        if (branchList.contains(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (HEAD.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        //获取指定分支中的commit
        String id = Utils.readContentsAsString(join(GITLET_BRANCHES, branchName + ".txt"));
        Commit commit = RepoUtils.getCommit(id);
        checkout(branchName, commit);
    }

    /**
     * 判断CWD文中是否tracked等等操作
     */
    private void checkout(String branch, Commit commit) {
        Commit currentCommit = RepoUtils.getCurrentCommit(MASTER);

        //获取工作目录中的文件
        List<String> workFile = Utils.plainFilenamesIn(CWD);
        List<String> fileList = new ArrayList<>();
        for (String file: workFile) {
            if (file.contains(".txt")) {
                fileList.add(file);
            }
        }

        //判断CWD中文件是否untracked
        for (String file: fileList) {
            if (!currentCommit.getBlobs().containsKey(file)
                    && commit.getBlobs().containsKey(file)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }

        //如果CWD文件中存在被跟踪但没有存在branch指的commit中,删除
        for (String file: fileList) {
            if (currentCommit.getBlobs().containsKey(file)
                    && !commit.getBlobs().containsKey(file)) {
                Utils.restrictedDelete(file);
            }
        }

        //将branch指的commit放入工作目录中
        List<String> fileNames = new ArrayList<>(commit.getBlobs().keySet());
        for (String fileName: fileNames) {
            checkout(commit, fileName);
        }

        //HEAD指向 指定分支branch;   stagingArea清空
        stagingArea.clear();
        stagingArea.save();

        HEAD = branch;  MASTER = commit.getId();
        Utils.writeContents(GITLET_HEAD, HEAD);
        Utils.writeContents(join(GITLET_BRANCHES, HEAD + ".txt"), MASTER);
        Utils.writeObject(join(GITLET_COMMITS, MASTER + ".txt"), commit);
    }

    /**
     * 检查CWD中是否存在fileName文件,有则删除后添加(覆盖),没有直接添加
     */
    private void checkout(Commit commit, String fileName) {
        //如果工作目录存在fileName的文件,删除
        File file = join(CWD, fileName);
        if (file.exists()) {
            Utils.restrictedDelete(file);
        }

        //添加fileName文件
        Blob blob = Utils.readObject(join(GITLET_BLOBS,
                commit.getBlobs().get(fileName) + ".txt"), Blob.class);
        byte[] fileContent = blob.getContent();
        File newFile = join(CWD, fileName);
        Utils.writeContents(newFile, fileContent);
    }

    /**
     * 创建一个branchName的分支,并将其指向当前的commit
     * 不会立刻切换分支到新建的分支
     *
     * 如果分支名已存在,打印 A branch with that name already exists.
     */
    public void branch(String branchName) {
        File newBranch = join(GITLET_BRANCHES, branchName + ".txt");

        if (newBranch.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }

        //往新建的branch文件写入HEAD所指的当前commit
        Utils.writeContents(newBranch, MASTER);
    }

    /**
     * 删除指定名称的分支
     * 只删除与该分支相关的指针,不意味着删除该分支下创建的所有提交
     *
     * 如果分支名不存在,打印 A branch with that name does not exist.
     * 如果要删除当前分支,打印 Connot remove the current branch.
     */
    public void rmBranch(String branchName) {
        File branch = join(GITLET_BRANCHES, branchName + ".txt");

        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        if (HEAD.equals(branchName)) {
            System.out.println("Connot remove the current branch.");
        }

        branch.delete();
    }

    /**
     * 检查指定id 的commit中的所有文件,删除其中没有被追踪的文件
     * 将当前分支的head移动到该commit上
     */
    public void reset(String commitId) {
        Commit commit = RepoUtils.getCommit(commitId);

        if (commit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        checkout(HEAD, commit);
    }

    /**
     * 找到 branchName 与 HEAD 的 最新的共同祖先(分割点)
     * 分割点 与 branchName分支的commit-id相同,什么都不做
     *      打印 "Given branch is an ancestor of the current branch."
     * 分割点 与 HEAD分支的commit-id相同,checkout(branchName),打印 "Current branch fast-forwarded."
     *
     * 1. 在branchName有文件被修改,但在HEAD中没有被修改,checkout(branchName)
     * 2. 在HEAD分支修改过的文件,但在分割点后branchName分支没有被修改过,保持原样
     * 3. HEAD 和 branchName有相同文件被修改,merge中不变;
     *      如果一个文件在两个分支都删除了,工作目录还有,merge操作不track不staged
     * 4. 在分割点不存在的,只存在HEAD的文件保持原样
     * 5. 在分割点不存在的,只存在branchName的文件,应checkout和staged
     * 6. 分割点存在,HEAD中没修改,在branchName分支中不存在的,删除和untracked
     * 7. 分割点存在,branchName中没修改,在HEAD分支中不存在的,保持原样
     *
     * 8. 在当前分支和branch以不同方式修改的文件是不允许的,需要将冲突文件内容替换
     *      分割点不是当前分支和branch,merge自动commit,
     *      message Merged [given branch name] into [current branch name]
     *
     * merge出现问题,打印 Encountered a merge conflict
     *
     * merge commits 与其他commit不同,父分支包含两个(原父分支和 branchName分支的父分支)
     *
     * 如果stagingArea中还有东西,打印 You have uncommitted changes.
     * 如果branchName不存在,打印 A branch with that name does not exist.
     * 如果一个分支要和自己合并,打印 Connot merge a branch with itself.
     * 如果 untracked 文件在当前commit 将被合并覆盖或者删除,
     *      打印There is an untracked file in the way; delete it, or add and commit it first.
     *      退出
     * 在执行其他操作时先执行这个检查
     */
    public void merge(String branchName) {
        // Failure cases
        if (!stagingArea.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        } else if (!join(GITLET_BRANCHES, branchName + ".txt").exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (branchName.equals(HEAD)) {
            System.out.println("Connot merge a branch with itself.");
            return;
        }

        // Failure cases: untracked 文件
        List<String> workFile = Utils.plainFilenamesIn(CWD);
        List<String> fileList = new ArrayList<>();
        for (String file: workFile) {
            if (file.contains(".txt")) {
                fileList.add(file);
            }
        }

        String branchID = Utils.readContentsAsString(
                join(GITLET_BRANCHES, branchName + ".txt"));
        Commit branchCommit = RepoUtils.getCommit(branchID);
        Commit currentCommit = RepoUtils.getCurrentCommit(MASTER);

        for (String file: fileList) {
            if (!currentCommit.getBlobs().containsKey(file)
                    && branchCommit.getBlobs().containsKey(file)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }

        // 寻找 split point 分割点
        List<String> pBranchCommit = branchCommit.getParents();
        List<String> pCurrentCommit = currentCommit.getParents();

        Commit spiltPoint = null;

        for (String commitID: pBranchCommit) {
            if (pCurrentCommit.contains(commitID)) {
                spiltPoint = Utils.readObject(
                        join(GITLET_COMMITS, commitID + ".txt"), Commit.class);
                break;
            }
        }

        // 对分割点进行判断和操作
        if (spiltPoint.getId().equals(branchCommit.getId())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        } else if (spiltPoint.getId().equals(currentCommit.getId())) {
            checkout(branchName, branchCommit);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        boolean conflict = false;
        for (String fileName: currentCommit.getBlobs().keySet()) {
            String currentFileID = currentCommit.getBlobs().get(fileName);
            String branchFileID = branchCommit.getBlobs().get(fileName);
            String spID = spiltPoint.getBlobs().get(fileName);

            if (spiltPoint.getBlobs().containsKey(fileName) && branchCommit.getBlobs().containsKey(fileName)) {
                // 1(2) 文件在 branch 中修改但是没有在 HEAD 中修改, checkout 和 staged
                if (!spID.equals(branchID) && spID.equals(currentFileID)) {
                    checkout(branchID, "--", fileName);
                    stagingArea.getAddFiles().put(fileName, branchFileID);
                    stagingArea.save();
                } else if (spID.equals(branchID) && spID.equals(currentFileID)) {
                    File mergeFile = join(CWD, fileName + ".txt");
                    byte[] contents = RepoUtils.mergeContents(currentFileID, branchFileID);
                    Utils.writeContents(mergeFile, (Object) contents);
                    conflict = true;
                }
            }
        }

        List<String> spFile = new ArrayList<>(spiltPoint.getBlobs().keySet());
        List<String> commitFile = new ArrayList<>(currentCommit.getBlobs().keySet());
        List<String> branchFile = new ArrayList<>(branchCommit.getBlobs().keySet());

        // 5(4) 文件在分割点不存在的,只存在branchName, checkout和staged
        for (String file: branchFile) {
            if (!spFile.contains(file) && !commitFile.contains(file)) {
                checkout(branchID, "--", file);
                stagingArea.getAddFiles().put(file, branchCommit.getBlobs().get(file));
                stagingArea.save();
            }
        }
        // 6\7 文件在分割点存在,HEAD中没修改(ID不变),在branchName分支中不存在的,删除和untracked
        for (String file: spFile) {
            if (commitFile.contains(file) && !branchFile.contains(file)
                    && spiltPoint.getBlobs().get(file).equals(currentCommit.getBlobs().get(file))) {
                rm(file);
            } else if (branchFile.contains(file) && !commitFile.contains(file)
                    && spiltPoint.getBlobs().get(file).equals(branchCommit.getBlobs().get(file))) {
                rm(file);
            }
        }

        if (!conflict) {
            String mergeCommitMessage = "Merged " + branchName + " into " + HEAD + ".";
            commit(mergeCommitMessage);
        } else {
            System.out.println("Encountered a merge conflict.");
            return;
        }
    }
}
