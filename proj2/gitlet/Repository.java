package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static gitlet.Utils.*;
import static gitlet.RepoUtils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HANXICHEN
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    private String HEAD = "master";
    private StagingArea stagingArea;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     *
     * .gitlet
     *      | -- blobs          //所有文件 blobs
     *      | -- commits        //commit指令后保存
     *      | -- stagingArea    //暂存区
     *          |-- add             //待添加
     *          |-- del             //删除
     *      | -- branches       //装所有分支名称
     *          |-- master.txt      //指向 currentCommit的 SHA-1 id
     *          |-- HEAD            //HEAD所指分支
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    
    public static final File GITLET_BLOBS = join(GITLET_DIR, "blobs");

    public static final File GITLET_COMMITS = join(GITLET_DIR, "commits");
    
    public static final File GITLET_STAGING = join(GITLET_DIR, "stagingArea");

    public static final File STAGING_ADD = join(GITLET_STAGING, "add");

    public static final File STAGING_DEL = join(GITLET_STAGING, "del");

    public static final File GITLET_BRANCHES = join(GITLET_DIR, "branches");

    public Repository() {

    }

    /**
     * 初始化
     * 判断是否已经存在一个Gitlet文件工作目录,有则打印 A Gitlet version-control system already exists in the current directory. 后退出
     * 创建一个Gitlet文件工作目录, .gitlet /blobs /commits /stagingArea /branches
     * 提交一个 initial commit, 创建一个 commit 对象, 序列化存入 commits 文件夹中
     * 有一个分支HEAD = "master"
     */
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        RepoUtils.generateSystemDir();

        // 初始化initial commit
        Commit initialCommit = new Commit("initial commit", new HashMap<>(), new ArrayList<>());
        initialCommit.save();

        //分支master 和 HEAD
        File master = join(GITLET_BRANCHES, "master.txt");
        Utils.writeContents(master, initialCommit.getId());

        File head = join(GITLET_BRANCHES, "HEAD.txt");
        Utils.writeContents(head, HEAD);

        //初始化暂存区stagingArea
        stagingArea = new StagingArea();
        try {
            join(STAGING_ADD, "ADD.txt").createNewFile();
            join(STAGING_DEL, "DEL.txt").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将这个文件加入stagingArea,用StagingArea类中的方法
     * 如果这个文件在stagingArea已经存在,那么就覆盖之前的文件(commit命令:文件更改、添加)
     * 如果文件的当前版本和当前的commit版本相同,则不add;如果已经存在,将其从stagingArea中删除(更改会原始版本)
     * 如果文件在rm命令前已经在暂存区,从暂存区中删除即可(rm命令)
     * 如果文件不存在,打印 File does not exist.
     */
    public void add(String fileName) {
    }

    /**
     * 新建一个commit对象,其中包含日期时间还有message,还有一个独特的SHA-1 id
     * 序列化后保存进 .gitlet/commits 文件夹中,内容是暂存区中的文件
     * commit结束后,stagingArea会被清空
     * 如果没有文件被staged,打印 No changes added to the commit.
     * 如果commit没有message,打印 Please enter a commit message.
     */
    public void commit(String message) {
    }

    /**
     * 如果文件在 stagingArea中,直接将其删除
     * 如果文件在 当前commit中被追踪,不要删除删除;没有被追踪,从工作目录中删除
     * 如果文件没有在 stagingArea 和 被当前commit被追踪,打印 No reason to remove the file.
     */
    public void rm(String fileName) {
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
    }

    /**
     * 显示之前所有的commit,提交的顺序不重要
     * Utils中有一个有用的方法可以遍历一个目录中的文件
     */
    public void globalLog() {
    }

    /**
     * 用commit中的message寻找
     * 有多个将id打印在不同行,多个message需要放在引号里
     * Utils中有一个有用的方法可以遍历一个目录中的文件
     * 如果没有符合的commit
     */
    public void find(String message) {
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
    }

    /**
     * 将commit中的文件存入工作目录中,工作目录有则覆盖,新版本的文件不会被暂存
     */
    public void checkout(String s, String fileName) {
        
    }

    /**
     * 获取指定id的commit中的文件,将其存入工作的目录,如果已存在则覆盖,新版本的文件不会被暂存
     */
    public void checkout(String commitId, String s, String fileName) {
        
    }

    /**
     * 在指定branch取出当前commit的所有文件并存入当前工作目录,有则覆盖
     * 当前分支中被tracked的文件如果在指定branch不存在则被删除,除非两个分支相同,否则stagingArea会被清空
     * 当前指令最后HEAD分支变为给定的分支
     *
     */
    public void checkout(String branchName) {

    }

    /**
     * 创建一个branchName的分支,并将其指向当前的commit
     * 不会立刻切换分支到新建的分支
     *
     * 如果分支名已存在,打印 A branch with that name already exists.
     */
    public void branch(String branchName) {

    }

    /**
     * 删除指定名称的分支
     * 只删除与该分支相关的指针,不意味着删除该分支下创建的所有提交
     *
     * 如果分支名不存在,打印 A branch with that name does not exist.
     * 如果要删除当前分支,打印 Connot remove the current branch.
     */
    public void rmBranch(String branchName) {

    }

    /**
     * 检查指定id 的commit中的所有文件,删除其中没有被追踪的文件
     * 将当前分支的head移动到该commit上
     */
    public void reset(String commitId) {

    }

    public void merge(String branchName) {

    }


}
