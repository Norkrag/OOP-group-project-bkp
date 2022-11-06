import java.util.Scanner;

class Main {
    static Content content = new Content();
    static User user = new User();

    public static void main(String[] args) {
        // System.out.println(content.content);
        user.authenticate();
    }

}

class Content {
    // String content = "This is some environmental content stuff";
}

class User {
    public void authenticate() {
        /* Initialize privileges as guest */
        String privilleges = "guest";
        String password = "password";
        /* Create a new scanner object to be able to parse user input */
        Scanner scanner = new Scanner(System.in);
        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = "";
        /* Set to true when user does not answer with 'y' or 'n' to 'y/n' dialogue */
        boolean wrongInput = false;
        String userPasswordInput = "";
        Integer userDialogueChoice = 0;

        System.out.println("Do you want to authenticate as administrator?");
        System.out.println("y/n");

        /* Keep prompting user until they give a valid answer, either 'y' or 'n' */
        do {
            if (wrongInput) {
                System.out.println("Invalid command, please try again.");
                System.out.println("Do you want to authenticate as administrator?");
                System.out.println("y/n");
            }
            userPromptChoice = scanner.nextLine();
            /*
             * Convert user input to not restrict user to a case sensitive answer
             * As a result: 'y', 'Y', 'n', 'N' are all valid inputs
             */
            userPromptChoice = userPromptChoice.toLowerCase();
            /* Only matters if user does not answer with an allowed response */
            wrongInput = true;
        } while (!(userPromptChoice.equals("y") || userPromptChoice.equals("n")));

        if (userPromptChoice.equals("y")) {
            /*
             * While userChoice is either 0 (initial value)
             * or 1 (user chose to keep trying to authenticate)
             * and the password entered by the user is wrong
             */
            while ((userDialogueChoice.equals(0) || userDialogueChoice.equals(1))
                    && !userPasswordInput.equals(password)) {
                System.out.println("Please input password:");
                userPasswordInput = scanner.nextLine();
                /* Total password entry attempts */
                int attempts = 1;

                while (!userPasswordInput.equals(password)) {
                    System.out.println("Wrong password! Try again:");
                    userPasswordInput = scanner.nextLine();
                    attempts++;

                    /*
                     * Give the user 3 tries to authenticate as admin
                     * After that continue with follow-up dialogue
                     */
                    if (attempts == 3) {
                        break;
                    }
                }

                /* If user successfully logs in as admin */
                if (userPasswordInput.equals(password)) {
                    privilleges = "admin";
                } else {
                    System.out.println("Wrong password!");
                    System.out.println("You have the following options: ");
                    System.out.println("1. Keep trying to authenticate as administrator.");
                    System.out.println("2. Continue as guest.");

                    userDialogueChoice = scanner.nextInt();
                    /*
                     * discard newline character: '\n'
                     * (nextInt does not read the newline character from user input)
                     */
                    scanner.nextLine();
                    while (!(userDialogueChoice.equals(1) || userDialogueChoice.equals(2))) {
                        System.out.println("Invalid command, please try again.");
                        userDialogueChoice = scanner.nextInt();
                        scanner.nextLine(); /* discard newline character: '\n' */
                    }
                }
            }
        } else {
            /* Nothing, user already has guest privilleges by default */
        }

        /* We no longer need to prompt the user for input, so scanner can be closed*/
        scanner.close();

        if (privilleges == "admin") {
            System.out.println("Welcome, administrator!");
        } else {
            System.out.println("Welcome, guest!");
        }
    }
}