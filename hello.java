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
    /*
     * TODO
     * Start | Remove debugging code
     */
    static void debugPrint(String content) {
        if (debug) {
            System.out.println(content);
        }
    }

    /* Set to false if you don't want debug messages in console */
    static boolean debug = false;
    /* End | Remove debugging code */

    static ContentService contentService = new ContentService();
    static UserService userService = new UserService();
    static UserOptions userOptions = new UserOptions(contentService);

    public static void main(String[] args) {
        // TODO - Add menu formatting to user input as well
        Scanner scanner = new Scanner(System.in);
        /* initialize returnCode with 0 to allow user authentication during first run */
        int returnCode = 0;
        String APPLICATION_NAME = "APPLICATION_NAME";
        String WELCOME_MESSAGE = "Welcome to " + APPLICATION_NAME;

        System.out.println("");
        MenuFormatting.menuPrintFullLine();
        MenuFormatting.menuPrintWithSpacingEnclosed(5, WELCOME_MESSAGE);
        MenuFormatting.menuPrintFullLine();

        do {
            Main.debugPrint("returnCode = " + returnCode);

            if (returnCode == 0) {
                userService.authenticate(scanner);
            }
            userOptions.setPrivileges(userService.privilleges);
            userOptions.displayOptionsMenu();
            returnCode = userOptions.handleUserChoice();
        } while (returnCode != 1);

        scanner.close();
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint("Quitting application...");
        MenuFormatting.menuPrintFullLine();
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
}

// TODO - Add Log off option
class UserOptions {
    String userPrivilleges = "";
    ContentService contentService;

    UserOptions(ContentService contentService) {
        this.contentService = contentService;
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
        int returnCode = 0;
        String userPromptChoice = Main.handleUserYesNo(scanner,
                "Go back to the options menu? If not, application will quit.");
        if (userPromptChoice.equals("y")) {
            returnCode = 2; /* handle externally - go back to option list */
        } else if (userPromptChoice.equals("n")) {
            returnCode = 1; /* handle externally - quit application */
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

        int returnCode = 1;
        switch (userMenuChoice) {
            case 1: /* Both user types have this option */
                displayEntries(scanner);
                returnCode = handleOptionFinished(scanner);
                break;
            case 2: /* Both user types have this option */
                searchEntries(scanner);
                returnCode = handleOptionFinished(scanner);
                break;
            // TODO - Placeholder, need to implement
            case 3: /*
                     * Different action depending on user type.
                     * Each user has a separate menu with different numbers
                     */
                if (userPrivilleges == "admin") {
                    addEntry(scanner);
                    returnCode = handleOptionFinished(scanner);
                } else if (userPrivilleges == "guest") {
                    MenuFormatting.menuPrint("Logging off...");
                    returnCode = 0; /* handle externally - go back to authentication */
                }
                break;
            case 4: /*
                     * Different action depending on user type.
                     * Each user has a separate menu with different numbers
                     */
                if (userPrivilleges == "admin") {
                    returnCode = deleteEntry(scanner);
                    /* if returnCode was not changed by deleteEntry */
                    if (returnCode == 1) {
                        returnCode = handleOptionFinished(scanner);
                    }
                } else if (userPrivilleges == "guest") {
                    /* application will quit in main */
                }
                break;
            // TODO - Placeholder, need to implement
            case 5: /* Only admin has options 5+ */
                MenuFormatting.menuPrint("Logging off...");
                returnCode = 0; /* handle externally - go back to authentication */
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
        String userPromptChoice = Main.handleUserYesNo(scanner, "Are you sure you want to delete an entry?");
        if (userPromptChoice.equals("y")) {
            /* Nothing, continue with program */
            MenuFormatting.menuPrint("Which entry do you want to delete?");
            displayEntriesToDelete(scanner);
            return 1;
        }
        if (userPromptChoice.equals("n")) {
            return 2;
        }

        return 1;
    }

    void displayEntries(Scanner scanner) {
        int userChosenEntry = 0;
        optionSelectedText(1);

        contentService.displayContentEntries();

        // TODO - Add option to open all entries as once as well
        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = Main.handleUserYesNo(scanner, "Do you want to view a specific entry?");

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

            /*
             * TODO - Ask user if they want to do anything after displaying entry
             * Or go back to main menu.
             */
            contentService.viewContentById(userChosenEntry);
        } else if (userPromptChoice.equals("n")) {
            /* handled externally in handleUserChoice() */
        }
    }

    void displayEntriesToDelete(Scanner scanner) {
        int userChosenEntry = 0;
        optionSelectedText(4);

        contentService.displayContentEntries();

        // TODO - Add option to open all entries as once as well
        /* Keeps track if user selects 'y', 'n' or another option */
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
    }

    void searchEntries(Scanner scanner) {
        optionSelectedText(2);

        System.out.print("# ");
        String userSearchCriteria = scanner.nextLine();
        MenuFormatting.menuPrint("");

        MenuFormatting.menuPrint("Here are the results for your search:");
        contentService.displayContentEntriesMatchingCriteria(userSearchCriteria);
        // TODO - Go Back to main menu
    }
}

class ContentService {
    /* Start id at 1 for user accesibility */
    int id = 1;
    ArrayList<ContentEntry> contentArray = new ArrayList<ContentEntry>();

    static class ContentEntry {
        public int id;
        public String title;
        public String content;
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
        this.createContent("Default Content Title 1", "Default Content Content 1");
        this.createContent("Default Content Title 2", "Default Content Content 2");
        this.createContent("Default Content Title 3", "Default Content Content 3");
    }

    public void createContent(String title, String content) {
        ContentEntry contentEntry = new ContentEntry();
        Main.debugPrint("Created contentEntry with id: " + id);

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

    // TODO - Add ability to search by Title / id
    public void viewContentById(int id) {
        /* the array id starts from 0, not 1 */
        final int arrayId = id - 1;

        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrintFullLine();
        MenuFormatting.menuPrintWithSpacingEnclosed(5, contentArray.get(arrayId).title);
        MenuFormatting.menuPrintFullLine();
        MenuFormatting.menuPrint("");
        MenuFormatting.menuPrint(contentArray.get(arrayId).content);
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
        String userPromptChoice = Main.handleUserYesNo(scanner, "Do you want to authenticate as administrator?");

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
            /* Nothing, user already has guest privilleges by default */
        }

        if (privilleges == "admin") {
            MenuFormatting.menuPrint("Welcome, administrator!");
        } else {
            MenuFormatting.menuPrint(" ");
            MenuFormatting.menuPrint("Welcome, guest!");
        }
    }
}
