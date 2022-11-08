import java.util.Scanner;

class Main {
    static Content content = new Content();
    static User user = new User();

    public static void main(String[] args) {

        user.authenticate();

        System.out.println(content.content1);
        System.out.println(content.content2);
        System.out.println(content.content3);
        System.out.println(content.content4);
        System.out.println(content.content5);
        System.out.println(content.content6);
        System.out.println(content.content7);
        System.out.println(content.content8);
        System.out.println(content.content9);
        System.out.println(content.content10);

    }

}

class Content {

    String content1 = " ";
    String content2 = ("\033[3m // An Introduction: \033[0m");
    String content3 = " ";
    String content4 = "Global average temperature has increased by more than one degree Celsius since the pre-industrial "
            +
            "era, and the trend is worrying. " +
            "The 2011-20 decade is the warmest on record, with the warmest seven years all occurring since 2014,"
            + "The atmospheric concentrations of major greenhouse gases (GHGs) have locked-in this warming trend for generations to come. "
            + "The increase of ocean temperature over the past decade has been higher than the long-term average and in 2020, "
            + "the North Atlantic hurricane season had the largest number of named storms on record,";

    String content5 = "\nClimate variability and change are altering and intensifying risk patterns with significant impacts on people, society and the environment."
            + "Changes in temperature and precipitation are leading to both extreme weather events and slow onset changes,"
            + " including \033[3mprolonged droughts, more frequent and extreme storms, wildfires, floods and rising sea levels, \033[0m"
            + "The cumulative impacts of climate change could also cause parts of the Earth system to change irreversibly,"
            + "such as the loss of the Amazon rainforest or the West Antarctic ice sheet."
            + "Climate change also causes impacts that are more difficult to monetise, such as loss of life, health,"
            + "health, territory, human mobility and cultural heritage. Both economic and non-economic impacts must be averted, minimised and addressed to strengthen climate resilience.\n";

    String content6 = ("\033[3m // Objectives: \033[0m\n");
    String content7 = "This Guidance aims to support efforts to strengthen the resilience of developing countries to the adverse impacts of climate change and variability. "
            +
            "The intended audience includes both government officials at national and sub-national levels, "
            + "and providers of development co-operation. It may also provide insights to other development partners, "
            +
            "including civil society organisations (CSOs) and private-sector actors. The Guidance presents the following:\n";
    String content8 = ("\033[0;1m-	Three high-level aspirations"
            + " recognised in practice, research and recent international agreements, including on climate change and disaster risk reduction "
            + "as key considerations for ensuring that action" + "  "
            + "on climate resilience also supports broader sustainable development objectives.\n");
    String content9 = ("\033[0;1m-	Four mechanisms"
            + " that can facilitate a focus on climate resilience in national and sub-national policy processes. The mechanisms also aim to guide support provided by development co-operation in light of partner countries’ own domestic priorities on climate change and development.\n");

    String content10 = ("\033[0;1m-	•	Three enablers"
            + "for action for strengthening climate resilience, and identifying opportunities for government officials and development co-operation to enhance these enablers.\n");

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
                System.out.println("y/n\n");
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

        /* We no longer need to prompt the user for input, so scanner can be closed */
        scanner.close();

        if (privilleges == "admin") {
            System.out.println("Welcome, administrator!");
        } else {
            System.out.println(" ");
            System.out.println("Welcome, guest!");
        }
    }
}