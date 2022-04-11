package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HANXICHEN
 */
public class Repository {

    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    private String HEAD = "master";


    /**
     * Description:
     * 初始化一个新的 Gitlet 版本控制系统
     * 存在一个初始的commit， 内容initial commit
     * 系统将有一个分支 master，指向这个 initial commit
     * initial commit 提交的时间采用当前日期的 timestamp ec.00:00:00 UTC, Thursday, 1 January 1970
     * Gitlet 创建的所有存储库中的提交将具有完全相同的内容，因此所有存储库将自动共享此提交（它们都将具有相同的 UID），并且所有存储库中的所有提交都将追溯到它。
     *
     * Failure cases:
     *  当前系统已存在 Gitlet版本控制系统，中止
     *  打印 "A Gitlet version-control system already exists in the current directory."
     */
    public void init() {

    }

    /**
     * Description:
     * 将当前存在的文本添加到 暂存区
     * 文件会覆盖暂存区中的已存在文件
     * 如果暂存区文件和 .gitlet文件中版本相同，不添加
     *
     * Failure cases：
     *  如果文件不存在，打印 "File does not exist." 并退出
     * */
    public void add(String arg) {
    }

    /**
     * Description:
     * 提交后将清除暂存区域。
     *
     * commit 命令从不添加、更改或删除工作目录中的文件（目录中的.gitlet文件除外）。该 rm命令将删除此类文件，并暂存它们以进行删除，以便在commit.
     *
     * 该命令忽略在暂存后对文件所做的任何更改以添加或删除，该 commit命令仅修改.gitlet 目录的内容。例如，如果您使用 Unix rm命令（而不是 Gitlet 的同名命令）删除跟踪文件，它对下一次提交没有影响，它仍将包含（现在已删除的）文件版本。
     *
     * 在提交命令之后，新的提交被添加为提交树中的一个新节点。
     *
     * 刚刚进行的提交成为“当前提交”，并且头指针现在指向它。先前的头部提交是此提交的父提交。
     *
     * 每个提交都应该包含它的日期和时间。
     *
     * 每个提交都有与之关联的日志消息，描述提交中文件的更改。这是由用户指定的。整个消息应该只占用args传递给的数组中的一个条目main。要包含多字消息，您必须用引号将它们括起来。
     *
     * 每个提交都由其 SHA-1 id 标识，该 id 必须包括其文件的文件 (blob) 引用、父引用、日志消息和提交时间。
     *
     * Failure cases：
     *  文件没有暂存，打印 "No changes added to the commit."
     *  提交信息空白，打印 "Please enter a commit message."
     * */
    public void commit(String arg) {
    }

    /**
     * Description:
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for removal and remove the file from
     *  the working directory if the user has not already done so (do not remove it unless it is tracked in the current commit).
     *
     * Failure cases:
     *  If the file is neither staged nor tracked by the head commit,
     *  print the error message No reason to remove the file.
     * */
    public void rm(String arg) {
    }

    /**
     * Description:
     * Starting at the current head commit, display information about each commit backwards along the commit tree until
     *  the initial commit, following the first parent commit links, ignoring any second parents found in merge commits.
     * This set of commit nodes is called the commit’s history.
     * For every node in this history, the information it should display is the commit id, the time the
     *  commit was made, and the commit message.
     * */
    public void log() {
    }

    /**
     * Description:
     * Like log, except displays information about all commits ever made.
     * The order of the commits does not matter.
     * Hint: there is a useful method in gitlet.
     * Utils that will help you iterate over files within a directory.
     * */
    public void global_log() {
    }

    /**
     * Description: Prints out the ids of all commits that have the given commit message, one per line.
     * If there are multiple such commits, it prints the ids out on separate lines.
     * The commit message is a single operand; to indicate a multiword message,
     *  put the operand in quotation marks, as for the commit command below.
     * Hint: the hint for this command is the same as the one for global-log.
     *
     * Failure cases:
     * If no such commit exists, prints the error message Found no commit with that message.
     * */
    public void find(String arg) {
    }

    /**
     * Description:
     * Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal.
     */
    public void status() {
    }

    /**
     * Descriptions:
     *
     * Takes the version of the file as it exists in the head commit and puts it in the working directory,
     *  overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.
     *
     * Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory,
     *  overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.
     *
     * Takes all files in the commit at the head of the given branch, and puts them in the working directory,
     *  overwriting the versions of the files that are already there if they exist. Also, at the end of this command,
     *  the given branch will now be considered the current branch (HEAD). Any files that are tracked in the current branch
     *  but are not present in the checked-out branch are deleted. The staging area is cleared, unless the checked-out branch is the current branch.
     *
     * Failure cases:
     *
     * If the file does not exist in the previous commit, abort, printing the error message File does not exist in that commit.
     * Do not change the CWD.
     *
     * If no commit with the given id exists, print No commit with that id exists.
     * Otherwise, if the file does not exist in the given commit, print the same message as for failure case 1. Do not change the CWD.
     *
     * If no branch with that name exists, print No such branch exists. If that branch is the current branch,
     * print No need to checkout the current branch. If a working file is untracked in the current branch and would be overwritten by the checkout,
     * print There is an untracked file in the way; delete it, or add and commit it first. and exit; perform this check before doing anything else. Do not change the CWD.
     * */
    public void checkout(String[] args) {
    }

    /**
     * Description:
     * Creates a new branch with the given name, and points it at the current head commit.
     * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to the newly created branch (just as in real Git).
     * Before you ever call branch, your code should be running with a default branch called “master”.
     *
     * Failure cases:
     * If a branch with the given name already exists,
     * print the error message A branch with that name already exists.
     * */
    public void branch(String arg) {
    }

    /**
     * Description:
     * Deletes the branch with the given name.
     * This only means to delete the pointer associated with the branch;
     * it does not mean to delete all commits that were created under the branch, or anything like that.
     *
     * Failure cases:
     * If a branch with the given name does not exist, aborts.
     * Print the error message A branch with that name does not exist.
     * If you try to remove the branch you’re currently on, aborts, printing the error message Cannot remove the current branch.
     * */
    public void rm_branch(String arg) {
    }

    /**
     * Description:
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     * See the intro for an example of what happens to the head pointer after using reset.
     * The [commit id] may be abbreviated as for checkout. The staging area is cleared.
     * The command is essentially checkout of an arbitrary commit that also changes the current branch head.
     *
     * Failure case:
     * If no commit with the given id exists, print No commit with that id exists.
     * If a working file is untracked in the current branch and would be overwritten by the reset,
     * print `There is an untracked file in the way; delete it, or add and commit it first.`
     * and exit; perform this check before doing anything else.
     */
    public void reset(String arg) {
    }

    /**
     * Description:
     * Merges files from the given branch into the current branch.
     *
     * Failure cases:
     * If there are staged additions or removals present, print the error message You have uncommitted changes. and exit.
     * If a branch with the given name does not exist, print the error message A branch with that name does not exist.
     * If attempting to merge a branch with itself, print the error message Cannot merge a branch with itself.
     * If merge would generate an error because the commit that it does has no changes in it, just let the normal commit error message for this go through.
     * If an untracked file in the current commit would be overwritten or deleted by the merge, print There is an untracked file in the way; delete it, or add and commit it first. and exit;
     * perform this check before doing anything else.
     * */
    public void merge(String arg) {
    }
}
