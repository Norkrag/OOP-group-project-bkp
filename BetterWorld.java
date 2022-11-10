import java.util.Scanner;
import java.util.ArrayList;

class MenuFormatting {
    static void menuPrint(String string) {
        System.out.println("# " + string + " ".repeat(72 - 2 - string.length() - 1) + "#");
    }

    static void menuPrintWithSpacing(int spaces, String string) {
        System.out.println("#".repeat(spaces) + " " + string + " ".repeat(72 - spaces - 1 - string.length() - 1) + "#");
    }

    static void menuPrintWithSpacingEnclosed(int spaces, String string) {
        System.out.println("#".repeat(spaces) + " " + string + " " + "#".repeat(72 - spaces - 1 - string.length() - 1));
    }

    static void menuPrintFullLine() {
        System.out.println("#".repeat(72));
    }
}

class Main {
    static ContentService contentService = new ContentService();
    static UserService userService = new UserService();
    static UserOptions userOptions = new UserOptions(contentService);
    final static int backToLogin = 0;
    final static int quitApplication = 1;
    final static int backtoOptionMenu = 2;

    public static void main(String[] args) {
        // TODO - Add menu formatting to user input as well (add missing # at left
        // column)
        Scanner scanner = new Scanner(System.in);

        /*
         * initialize returnCode with 0 (backToLogin) to allow user authentication
         * during first run
         */
        int returnCode = backToLogin;
        String APPLICATION_NAME = "BetterWorld.io";
        String WELCOME_MESSAGE = "Welcome to " + APPLICATION_NAME;

        System.out.println("");
        MenuFormatting.menuPrintFullLine();
        MenuFormatting.menuPrintWithSpacingEnclosed(5, WELCOME_MESSAGE);
        MenuFormatting.menuPrintFullLine();

        /* Main loop */
        do {
            if (returnCode == backToLogin) {
                userService.authenticate(scanner);
            }
            userOptions.setPrivileges(userService.privilleges);
            userOptions.displayOptionsMenu();
            returnCode = userOptions.handleUserChoice();
        } while (returnCode != quitApplication);

        scanner.close();
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint("Quitting application...");
        MenuFormatting.menuPrintFullLine();
    }
}

class UserOptions {
    String userPrivilleges = "";
    ContentService contentService;

    UserOptions(ContentService contentService) {
        this.contentService = contentService;
    }

    public static String handleUserYesNo(Scanner scanner, String... optionalMessage) {
        if (optionalMessage[0].length() > 0) {
            MenuFormatting.menuPrint("");
            MenuFormatting.menuPrint(optionalMessage[0]);
            MenuFormatting.menuPrint("y/n");
        }

        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = "";
        /* Set to true when user does not answer with 'y' or 'n' to 'y/n' dialogue */
        boolean wrongInput = false;

        /* Keep prompting user until they give a valid answer, either 'y' or 'n' */
        do {
            if (wrongInput) {
                MenuFormatting.menuPrint("");
                MenuFormatting.menuPrint("Invalid command, please try again.");
                if (optionalMessage[0].length() > 0) {
                    MenuFormatting.menuPrint(optionalMessage[0]);
                }
                MenuFormatting.menuPrint("y/n");
            }

            System.out.print("# ");
            userPromptChoice = scanner.nextLine();
            /*
             * Convert user input to not restrict user to a case sensitive answer
             * As a result: 'y', 'Y', 'n', 'N' are all valid inputs
             */
            userPromptChoice = userPromptChoice.toLowerCase();
            /* Only matters if user does not answer with an allowed response */
            wrongInput = true;
        } while (!(userPromptChoice.equals("y") || userPromptChoice.equals("n")));

        return userPromptChoice;
    }

    void setPrivileges(String privileges) {
        userPrivilleges = privileges;
    }

    void displayOptionsMenu() {
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint("Please enter a number depending on what you want to do.");
        MenuFormatting.menuPrint("Here are your options:");

        if (userPrivilleges == "guest") {
            MenuFormatting.menuPrint("1. View Entries");
            MenuFormatting.menuPrint("2. Search for an entry");
            MenuFormatting.menuPrint("3. Log off");
            MenuFormatting.menuPrint("4. Quit");
        } else if (userPrivilleges == "admin") {
            MenuFormatting.menuPrint("1. View Entries");
            MenuFormatting.menuPrint("2. Search for an entry");
            MenuFormatting.menuPrint("3. Add Entry");
            MenuFormatting.menuPrint("4. Delete Entry");
            MenuFormatting.menuPrint("5. Log off");
            MenuFormatting.menuPrint("6. Quit");
        } else {
            System.out.println("Error! Invalid role.");
        }
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint("What do you want to do?");
        MenuFormatting.menuPrint("Option number:");
    }

    int handleOptionFinished(Scanner scanner) {
        int returnCode = Main.backToLogin;
        String userPromptChoice = handleUserYesNo(scanner,
                "Go back to the options menu? If not, application will quit.");
        if (userPromptChoice.equals("y")) {
            returnCode = Main.backtoOptionMenu; /* handle externally - go back to option list */
        } else if (userPromptChoice.equals("n")) {
            returnCode = Main.quitApplication; /* handle externally - quit application */
        }

        return returnCode;
    }

    /*
     * return 1 if aplication can exit
     * return 0 if user logs off (to be handled externally and start over)
     */
    int handleUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int maxUserOptions = 0;

        if (userPrivilleges == "admin") {
            maxUserOptions = 6;
        } else if (userPrivilleges == "guest") {
            maxUserOptions = 4;
        }

        // TODO - Handle non int input (for all nextInt() in code)
        System.out.print("# ");
        int userMenuChoice = scanner.nextInt();
        scanner.nextLine(); /* discard newline character: '\n' */

        while (userMenuChoice < 1 || userMenuChoice > maxUserOptions) {
            MenuFormatting.menuPrint("Invalid command, please try again.");
            MenuFormatting.menuPrint("Enter a number between 1 and " + maxUserOptions);
            System.out.print("# ");
            userMenuChoice = scanner.nextInt();
            scanner.nextLine(); /* discard newline character: '\n' */
        }

        int returnCode = Main.quitApplication;
        switch (userMenuChoice) {
            case 1: /* Both user types have this option */
                displayEntries(scanner);
                returnCode = handleOptionFinished(scanner);
                break;
            case 2: /* Both user types have this option */
                searchEntries(scanner);
                returnCode = handleOptionFinished(scanner);
                break;
            case 3: /*
                     * Different action depending on user type.
                     * Each user has a separate menu with different numbers
                     */
                if (userPrivilleges == "admin") {
                    /*
                     * TODO - Fix addEntry implementation to allow user to enter
                     * multi row content that doesn't crash program when reaching left border
                     */
                    addEntry(scanner);
                    returnCode = handleOptionFinished(scanner);
                } else if (userPrivilleges == "guest") {
                    MenuFormatting.menuPrint("Logging off...");
                    returnCode = Main.backToLogin; /* handle externally - go back to authentication */
                }
                break;
            case 4: /*
                     * Different action depending on user type.
                     * Each user has a separate menu with different numbers
                     */
                if (userPrivilleges == "admin") {
                    returnCode = deleteEntry(scanner);
                    /* if returnCode was not changed by deleteEntry */
                    if (returnCode == Main.quitApplication) {
                        returnCode = handleOptionFinished(scanner);
                    }
                } else if (userPrivilleges == "guest") {
                    /* application will quit in main */
                }
                break;
            case 5: /* Only admin has options 5+ */
                MenuFormatting.menuPrint("Logging off...");
                returnCode = Main.backToLogin; /* handle externally - go back to authentication */
            case 6: /* Only admin has options 5+ */
                /* application will quit in main */
                break;
            default:
                System.out.println("Error! Code should not be reached");
        }

        return returnCode; /* handle externally - quit application */
    }

    void optionSelectedText(int optionNumber) {
        MenuFormatting.menuPrint("");
        // TODO - Add Edit Entry option
        switch (optionNumber) {
            case 1:
                MenuFormatting.menuPrint("Option selected: View Entries");
                MenuFormatting.menuPrint("Showing entries:");
                break;
            case 2:
                MenuFormatting.menuPrint("Option selected: Search for an entry");
                MenuFormatting.menuPrint("Enter your search term to search for a specific title:");
                break;
            case 3:
                MenuFormatting.menuPrint("Option selected: Add Entry");
                MenuFormatting.menuPrint("You will need to enter a title and the content of the entry.");
                MenuFormatting.menuPrint("");
                break;
            case 4:
                MenuFormatting.menuPrint("Option selected: Delete Entry");
                MenuFormatting.menuPrint("Showing entries:");
                break;
            default:
                System.out.println("Error! Code should not be reached");
        }
    }

    void addEntry(Scanner scanner) {
        optionSelectedText(3);

        String newContentEntryTitle = "";
        String newContentEntryContent = "";
        MenuFormatting.menuPrint("Enter title");
        System.out.print("# ");
        newContentEntryTitle = scanner.nextLine();

        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint("Enter content");
        System.out.print("# ");
        newContentEntryContent = scanner.nextLine();

        contentService.createContent(newContentEntryTitle, newContentEntryContent);
    }

    int deleteEntry(Scanner scanner) {
        String userPromptChoice = handleUserYesNo(scanner, "Are you sure you want to delete an entry?");
        int userChosenEntry = 0;

        if (userPromptChoice.equals("y")) {
            /* Nothing, continue with program */
            optionSelectedText(4);
    
            contentService.displayContentEntries();
            MenuFormatting.menuPrint("Which entry do you want to delete?");
            System.out.print("# ");
            userChosenEntry = scanner.nextInt();
            scanner.nextLine(); /* discard newline character: '\n' */
    
            while (userChosenEntry < 1 || userChosenEntry > contentService.contentArray.size()) {
                MenuFormatting.menuPrint("Invalid command, please try again.");
                MenuFormatting.menuPrint("Enter a number between 1 and " + contentService.contentArray.size());
                System.out.print("# ");
                userChosenEntry = scanner.nextInt();
                scanner.nextLine(); /* discard newline character: '\n' */
            }
    
            contentService.deleteContent(userChosenEntry);
            return Main.quitApplication;
        }
        if (userPromptChoice.equals("n")) {
            return Main.backtoOptionMenu;
        }

        return Main.quitApplication;
    }

    void displayEntries(Scanner scanner) {
        int userChosenEntry = 0;
        optionSelectedText(1);

        contentService.displayContentEntries();

        // TODO - Add option to open all entries as once as well
        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = handleUserYesNo(scanner, "Do you want to view a specific entry?");

        if (userPromptChoice.equals("y")) {
            MenuFormatting.menuPrint("");
            MenuFormatting.menuPrint("Which entry?");
            MenuFormatting.menuPrint("Enter a number between 1 and " + contentService.contentArray.size());
            System.out.print("# ");
            userChosenEntry = scanner.nextInt();
            scanner.nextLine(); /* discard newline character: '\n' */

            while (userChosenEntry < 1 || userChosenEntry > contentService.contentArray.size()) {
                MenuFormatting.menuPrint("Invalid command, please try again.");
                MenuFormatting.menuPrint("Enter a number between 1 and " + contentService.contentArray.size());
                System.out.print("# ");
                userChosenEntry = scanner.nextInt();
                scanner.nextLine(); /* discard newline character: '\n' */
            }

            contentService.viewContentById(userChosenEntry);
        } else if (userPromptChoice.equals("n")) {
            /* handled externally in handleUserChoice() */
        }
    }

    void searchEntries(Scanner scanner) {
        optionSelectedText(2);
        System.out.print("# ");
        String userSearchCriteria = scanner.nextLine();
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint("Here are the results for your search:");
        contentService.displayContentEntriesMatchingCriteria(userSearchCriteria);
    }
}

class ContentService {
    /* Start id at 1 for user accesibility */
    int id = 1;
    ArrayList<ContentEntry> contentArray = new ArrayList<ContentEntry>();

    static class ContentEntry {
        public int id;
        public String title;
        public String[] content;
    }

    /* Initialize class on constructor */
    public ContentService() {
        /*
         * When a new ContentService object is created add some initial data.
         * This is to simulate an existing database
         */
        initializeContent();
    }

    private void initializeContent() {
        // TODO - Handle 0 content entry bugs (Can't view specific entry for ex)
        this.createContent("An Introduction",
                "The global average temperature has increased by more than one degree",
                "Celsius since the pre-industrial era, and the trend is worrying.",
                "The 2011-20 decade is the warmest on record, with the warmest seven",
                "years all occurring since 2014, The atmospheric concentrations of",
                "major greenhouse gases (GHGs) have locked in this warming trend for",
                "generations to come. The increase of ocean temperature over the past",
                "decade has been higher than the long-term average and in 2020,",
                "the North Atlantic hurricane season had the most significant number",
                "of named storms on record,");
    }

    public void createContent(String title, String... content) {
        ContentEntry contentEntry = new ContentEntry();

        contentEntry.id = id; /* assign the first available id to entry */
        /* assign a title & content based on function parameters */
        contentEntry.title = title;
        contentEntry.content = content;

        /* Add the newly created ContentEntry to the contentArray */
        contentArray.add(contentEntry);
        /*
         * Increment the id since the current id was used for an entry.
         * As ids should be unique, this id should not be available anymore
         */
        id++;

        return;
    }

    public void deleteContent(int id) {
        contentArray.remove(id - 1);
        id--;
    }

    public void viewContentById(int id) {
        /* the array id starts from 0, not 1 */
        final int arrayId = id - 1;

        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrintFullLine();
        MenuFormatting.menuPrintWithSpacingEnclosed(5, contentArray.get(arrayId).title);
        MenuFormatting.menuPrintFullLine();
        MenuFormatting.menuPrint("");
        for (int i = 0; i < contentArray.get(arrayId).content.length; i++) {
            MenuFormatting.menuPrint(contentArray.get(arrayId).content[i]);
        }
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrintFullLine();
    }

    private String returnContentTitleForId(int id) {
        /* the array id starts from 0, not 1 */
        final int arrayId = id - 1;

        return contentArray.get(arrayId).title;
    }

    public void displayContentEntries() {
        /*
         * Go through every content entry / post in the 'database'
         * Display the title of each entry
         */
        for (int i = 1; i <= contentArray.size(); i++) {
            /*
             * id starts at 0;
             * we add 1 to the id so the user sees entries starting from 1, not 0.
             * Display all ContentEntry(es) available by title and a number preceding each
             * title.
             */
            MenuFormatting.menuPrint(i + ". " + returnContentTitleForId(i));
        }
    }

    public void displayContentEntriesMatchingCriteria(String searchTerm) {
        /*
         * Go through every content entry / post in the 'database'
         * Display the title of each entry
         */
        for (int i = 1; i <= contentArray.size(); i++) {
            /*
             * id starts at 0;
             * we add 1 to the id so the user sees entries starting from 1, not 0.
             * Display all ContentEntry(es) available by title and a number preceding each
             * title.
             */
            /*
             * Cast both strings to lowercase to allow user to search without case
             * sensitivity
             */
            if (returnContentTitleForId(i).toLowerCase().contains(searchTerm.toLowerCase())) {
                MenuFormatting.menuPrint(i + ". " + returnContentTitleForId(i));
            }
        }
    }
}

class UserService {
    /* Initialize privileges as guest */
    public String privilleges = "guest";

    public void authenticate(Scanner scanner) {
        String password = "password";
        /* Create a new scanner object to be able to parse user input */

        String userPasswordInput = "";
        Integer userDialogueChoice = 0;

        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = UserOptions.handleUserYesNo(scanner, "Do you want to authenticate as administrator?");

        if (userPromptChoice.equals("y")) {
            /*
             * While userChoice is either 0 (initial value)
             * or 1 (user chose to keep trying to authenticate)
             * and the password entered by the user is wrong
             */
            while ((userDialogueChoice.equals(0) || userDialogueChoice.equals(1))
                    && !userPasswordInput.equals(password)) {
                MenuFormatting.menuPrint("Please input password:");
                System.out.print("# ");
                userPasswordInput = scanner.nextLine();
                /* Total password entry attempts */
                int attempts = 1;

                while (!userPasswordInput.equals(password)) {
                    MenuFormatting.menuPrint("Wrong password! Try again:");
                    System.out.print("# ");
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
                    MenuFormatting.menuPrint("Wrong password!");
                    MenuFormatting.menuPrint("You have the following options: ");
                    MenuFormatting.menuPrint("1. Keep trying to authenticate as administrator.");
                    MenuFormatting.menuPrint("2. Continue as guest.");

                    System.out.print("# ");
                    userDialogueChoice = scanner.nextInt();
                    /*
                     * discard newline character: '\n'
                     * (nextInt does not read the newline character from user input)
                     */
                    scanner.nextLine();
                    while (!(userDialogueChoice.equals(1) || userDialogueChoice.equals(2))) {
                        MenuFormatting.menuPrint("Invalid command, please try again.");
                        System.out.print("# ");
                        userDialogueChoice = scanner.nextInt();
                        scanner.nextLine(); /* discard newline character: '\n' */
                    }
                }
            }
        } else {
            privilleges = "guest";
        }

        if (privilleges == "admin") {
            MenuFormatting.menuPrint("Welcome, administrator!");
        } else {
            MenuFormatting.menuPrint(" ");
            MenuFormatting.menuPrint("Welcome, guest!");
        }
    }
}
