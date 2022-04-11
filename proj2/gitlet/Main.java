package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author HANXICHEN
 */
public class Main {

    /**
     * To check whether the entered args length meets the requirement of the command
    */
    public static boolean inputCheck(int len, String... args) {
        if (len == args.length) {
            return true;
        }
        System.out.println("Incorrect operands");
        return false;
    }

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Please enter a command.");
        } else {
            Repository repo = new Repository();
            switch(args[0]) {
                case "init":
                    //
                    if (inputCheck(1, args)) {
                        repo.init();
                    }
                    break;
                case "add":
                    //
                    if (inputCheck(2, args)) {
                        repo.add(args[1]);
                    }
                    break;
                case "commit":
                    //
                    if (inputCheck(2, args)) {
                        repo.commit(args[1]);
                    }
                    break;
                case "rm":
                    //
                    if (inputCheck(2, args)) {
                        repo.rm(args[1]);
                    }
                    break;
                case "log":
                    //
                    if (inputCheck(1, args)) {
                        repo.log();
                    }
                    break;
                case "global-log":
                    //
                    if (inputCheck(1, args)) {
                        repo.global_log();
                    }
                    break;
                case "find":
                    //
                    if (inputCheck(2, args)) {
                        repo.find(args[1]);
                    }
                    break;
                case "status":
                    //
                    if (inputCheck(1, args)) {
                        repo.status();
                    }
                    break;
                case "checkout":
                    //
                    if (args.length != 2 && args.length != 3 && args.length != 4) {
                        System.out.println("Incorrect operands");
                    } else if ((args.length == 3 && args[1].equals("--")) || (args.length == 4 && args[2].equals("--"))) {
                        System.out.println("Incorrect operands");
                    } else {
                        repo.checkout(args);
                    }
                    break;
                case "branch" :
                    if (inputCheck(2, args)) {
                        repo.branch(args[1]);
                    }
                case "rm-branch":
                    //
                    if (inputCheck(2, args)) {
                        repo.rm_branch(args[1]);
                    }
                    break;
                case "reset":
                    //
                    if (inputCheck(2, args)) {
                        repo.reset(args[1]);
                    }
                    break;
                case "merge":
                    //
                    if (inputCheck(2, args)) {
                        repo.merge(args[1]);
                    }
                    break;
                default:
                    System.out.println("No command with that name exists");
                    break;
            }
        }
    }
}
