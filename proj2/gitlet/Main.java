package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  特定错误的提示:
 *  没有输入参数 Please enter a command.
 *  输入了不存在的命令 No command with that name exists.
 *  输入了错误数量或操作数格式的命令 Incorrect operands.
 *  输入的命令需要位于Gitlet工作目录,但不在这样的目录 Not in an initialized Gitlet directory.
 *
 *  @author HANXICHEN
 */
public class Main {
    /**
     * To check the length of the args match the command needed,
     */
    private static boolean check(String[] args, int len) {
        if (args.length == len) {
            return true;
        }
        System.out.println("Incorrect Operands");
        return false;
    }
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        Repository repo = new Repository();
        if (args.length == 0) {
            System.out.println("Please enter a command:");
        } else {
            String firstArg = args[0];
            switch(firstArg) {
                case "init":                        // 初始化
                    if (check(args, 1)) {
                        repo.init();
                    }
                    break;
                case "add":                         // 添加
                    if (check(args, 2)) {
                        repo.add(args[1]);
                    }
                    break;
                case "commit":                      // 提交
                    if (check(args, 2)) {
                        repo.commit(args[1]);
                    }
                    break;
                case "rm":                          // 删除
                    if (check(args, 2)) {
                        repo.rm(args[1]);
                    }
                    break;
                case "log":                         // 日志
                    if (check(args, 1)) {
                        repo.log();
                    }
                    break;
                case "global-log":                  // 全局日志
                    if (check(args, 1)) {
                        repo.globalLog();
                    }
                    break;
                case "find":                        // 寻找
                    if (check(args, 2)) {
                        repo.find(args[1]);
                    }
                    break;
                case "status":                      // 状态
                    if (check(args, 1)) {
                        repo.status();
                    }
                    break;
                case "checkout":
                    if (check(args, 3) && args[1].equals("--")) {           // fileName
                        repo.checkout("--", args[1]);
                    } else if (check(args, 4) && args[2].equals("--")) {    // Commit-id  fileName
                        repo.checkout(args[1], args[2], args[3]);
                    } else if (check(args, 2)) {                            // branch
                        repo.checkout(args[1]);
                    } else {
                        System.out.println("Incorrect Operands.");
                    }
                    break;
                case "branch":                      // 分支
                    if (check(args, 2)) {
                        repo.branch(args[1]);
                    }
                    break;
                case "rm-branch":                   // 删除分支
                    if (check(args, 2)) {
                        repo.rmBranch(args[1]);
                    }
                    break;
                case "reset":                       // 充值
                    if (check(args, 2)) {
                        repo.reset(args[1]);
                    }
                    break;
                case "merge":                       // 合并
                    if (check(args, 2)) {
                        repo.merge(args[1]);
                    }
                    break;
                default:
                    System.out.println("No command with that name exists.");
            }
        }
        System.exit(0);
    }
}
