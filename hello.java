import java.util.Scanner;
import java.util.ArrayList;

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
    static boolean debug = true;
    /* End | Remove debugging code */

    static ContentService contentService = new ContentService();
    static UserService userService = new UserService();
    static UserOptions userOptions = new UserOptions(contentService);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /* initialize returnCode with 0 to allow user authentication during first run */
        int returnCode = 0;
        /*
         * Test - create some content
         * This should be possible to do from the application when implemented
         */
        contentService.createContent("Title 1", "Content1");
        contentService.createContent("Title 2", "Content2");
        contentService.createContent("Title 3", "Content3");

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
        System.out.println("");
        System.out.println("Quitting application...");
    }

    public static String handleUserYesNo(Scanner scanner, String... optionalMessage) {
        if (optionalMessage[0].length() > 0) {
            System.out.println("");
            System.out.println(optionalMessage[0]);
            System.out.println("y/n");
        }

        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = "";
        /* Set to true when user does not answer with 'y' or 'n' to 'y/n' dialogue */
        boolean wrongInput = false;

        /* Keep prompting user until they give a valid answer, either 'y' or 'n' */
        do {
            if (wrongInput) {
                System.out.println("");
                System.out.println("Invalid command, please try again.");
                if (optionalMessage[0].length() > 0) {
                    System.out.println(optionalMessage[0]);
                }
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
        System.out.println("");
        System.out.println("Please enter a number depending on what you want to do.");
        System.out.println("Here are your options:");

        if (userPrivilleges == "guest") {
            System.out.println("1. View Entries");
            System.out.println("2. Search for an entry");
            System.out.println("3. Log off");
            System.out.println("4. Quit");
        } else if (userPrivilleges == "admin") {
            System.out.println("1. View Entries");
            System.out.println("2. Search for an entry");
            System.out.println("3. Add Entry");
            System.out.println("4. Delete Entry");
            System.out.println("5. Edit Entry");
            System.out.println("6. Log off");
            System.out.println("7. Quit");
        } else {
            System.out.println("Invalid role");
        }
        System.out.println("");
        System.out.println("What do you want to do?");
        System.out.println("Option number:");
    }

    /*
     * return 1 if aplication can exit
     * return 0 if user logs off (to be handled externally and start over)
     */
    int handleUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int maxUserOptions = 0;

        if (userPrivilleges == "admin") {
            maxUserOptions = 7;
        } else if (userPrivilleges == "guest") {
            maxUserOptions = 4;
        }

        // TODO - Handle non int input (for all nextInt() in code)
        int userMenuChoice = scanner.nextInt();
        scanner.nextLine(); /* discard newline character: '\n' */

        while (userMenuChoice < 1 || userMenuChoice > maxUserOptions) {
            System.out.println("Invalid command, please try again.");
            System.out.println("Enter a number between 1 and " + maxUserOptions);
            userMenuChoice = scanner.nextInt();
            scanner.nextLine(); /* discard newline character: '\n' */
        }

        int returnCode = 1;
        switch (userMenuChoice) {
            case 1: /* Both user types have this option */
                displayEntries(scanner);
                String userPromptChoice = Main.handleUserYesNo(scanner,
                        "Do you want to go back to the options menu? If not, application will quit.");
                if (userPromptChoice.equals("y")) {
                    returnCode = 2; /* handle externally - go back to option list */
                } else if (userPromptChoice.equals("n")) {
                    returnCode = 1; /* handle externally - quit application */
                }
                break;
            case 2: /* Both user types have this option */
                searchEntries(scanner);
                break;
            // TODO - Placeholder, need to implement
            case 3: /*
                     * Different action depending on user type.
                     * Each user has a separate menu with different numbers
                     */
                if (userPrivilleges == "admin") {
                    System.out.println("ToDo - Adding entry...");
                } else if (userPrivilleges == "guest") {
                    System.out.println("Logging off...");
                    returnCode = 0; /* handle externally - go back to authentication */
                }
                break;
            // TODO - Placeholder, need to implement
            case 4: /*
                     * Different action depending on user type.
                     * Each user has a separate menu with different numbers
                     */
                if (userPrivilleges == "admin") {
                    System.out.println("ToDo - Deleting entry...");
                } else if (userPrivilleges == "guest") {
                    /* application will quit in main */
                }
                break;
            // TODO - Placeholder, need to implement
            case 5: /* Only admin has options 5+ */
                System.out.println("ToDo - Editing entry...");
                break;
            // TODO - Placeholder, need to implement
            case 6: /* Only admin has options 5+ */
                System.out.println("Logging off...");
                returnCode = 0; /* handle externally - go back to authentication */
            case 7: /* Only admin has options 5+ */
                /* application will quit in main */
                break;
            default:
                System.out.println("Error! Code should not be reached");
        }

        return returnCode; /* handle externally - quit application */
    }

    void optionSelectedText(int optionNumber) {
        System.out.println("");
        switch (optionNumber) {
            case 1:
                System.out.println("Option selected: View Entries");
                System.out.println("Showing entries:");
                break;
            case 2:
                System.out.println("Option selected: Search for an entry");
                System.out.println("Enter your search term to search for a specific title:");
                break;
            default:
                System.out.println("Error! Code should not be reached");
        }
    }

    void displayEntries(Scanner scanner) {
        int userChosenEntry = 0;
        optionSelectedText(1);

        contentService.displayContentEntries();

        // TODO - Add option to open all entries as once as well
        /* Keeps track if user selects 'y', 'n' or another option */
        String userPromptChoice = Main.handleUserYesNo(scanner, "Do you want to view a specific entry?");

        if (userPromptChoice.equals("y")) {
            System.out.println("");
            System.out.println("Which entry?");
            System.out.println("Enter a number between 1 and " + contentService.getNumberOfContentEntries());
            userChosenEntry = scanner.nextInt();
            scanner.nextLine(); /* discard newline character: '\n' */

            while (userChosenEntry < 1 || userChosenEntry > contentService.getNumberOfContentEntries()) {
                System.out.println("Invalid command, please try again.");
                System.out.println("Enter a number between 1 and " + contentService.getNumberOfContentEntries());
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

    void searchEntries(Scanner scanner) {
        optionSelectedText(2);

        String userSearchCriteria = scanner.nextLine();
        System.out.println("");

        System.out.println("Here are the results for your search:");
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

    private String returnStringOfHashes(int numberOfHashes) {
        String hashes = "";

        for(int i = 0; i < numberOfHashes; i++) {
            hashes += "#";
        }
        
        return hashes;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    // TODO - Add ability to search by Title / id
    public void viewContentById(int id) {
        /* the array id starts from 0, not 1 */
        final int arrayId = id - 1;

        System.out.println("");
        System.out.println(returnStringOfHashes(72));
        System.out.println("##### " + contentArray.get(arrayId).title + " " + returnStringOfHashes(72 - 6 - contentArray.get(arrayId).title.length() - 1));
        System.out.println(returnStringOfHashes(72));
        System.out.println("");
        System.out.println(contentArray.get(arrayId).content);
        System.out.println("");
        System.out.println(returnStringOfHashes(72));
    }

    private String returnContentTitleForId(int id) {
        /* the array id starts from 0, not 1 */
        final int arrayId = id - 1;

        return contentArray.get(arrayId).title;
    }

    public int getNumberOfContentEntries() {
        /*
         * return current id value -1
         * (since id was incremented but latest value is not yet used)
         */
        return id - 1;
    }

    public void displayContentEntries() {
        /*
         * Go through every content entry / post in the 'database'
         * Display the title of each entry
         */
        for (int i = 1; i <= getNumberOfContentEntries(); i++) {
            /*
             * id starts at 0;
             * we add 1 to the id so the user sees entries starting from 1, not 0.
             * Display all ContentEntry(es) available by title and a number preceding each
             * title.
             */
            System.out.println(i + ". " + returnContentTitleForId(i));
        }
    }

    public void displayContentEntriesMatchingCriteria(String searchTerm) {
        /*
         * Go through every content entry / post in the 'database'
         * Display the title of each entry
         */
        for (int i = 1; i <= getNumberOfContentEntries(); i++) {
            /*
             * id starts at 0;
             * we add 1 to the id so the user sees entries starting from 1, not 0.
             * Display all ContentEntry(es) available by title and a number preceding each
             * title.
             */
            /* Cast both strings to lowercase to allow user to search without case sensitivity */
            if (returnContentTitleForId(i).toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println(i + ". " + returnContentTitleForId(i));
            }
        }
    }

    // String content1 = " ";
    // String content2 = ("\033[3m // An Introduction: \033[0m");
    // String content3 = " ";
    // String content4 = "Global average temperature has increased by more than one
    // degree Celsius since the pre-industrial "
    // +
    // "era, and the trend is worrying. " +
    // "The 2011-20 decade is the warmest on record, with the warmest seven years
    // all occurring since 2014,"
    // + "The atmospheric concentrations of major greenhouse gases (GHGs) have
    // locked-in this warming trend for generations to come. "
    // + "The increase of ocean temperature over the past decade has been higher
    // than the long-term average and in 2020, "
    // + "the North Atlantic hurricane season had the largest number of named storms
    // on record,";

    // String content5 = "\nClimate variability and change are altering and
    // intensifying risk patterns with significant impacts on people, society and
    // the environment."
    // + "Changes in temperature and precipitation are leading to both extreme
    // weather events and slow onset changes,"
    // + " including \033[3mprolonged droughts, more frequent and extreme storms,
    // wildfires, floods and rising sea levels, \033[0m"
    // + "The cumulative impacts of climate change could also cause parts of the
    // Earth system to change irreversibly,"
    // + "such as the loss of the Amazon rainforest or the West Antarctic ice
    // sheet."
    // + "Climate change also causes impacts that are more difficult to monetise,
    // such as loss of life, health,"
    // + "health, territory, human mobility and cultural heritage. Both economic and
    // non-economic impacts must be averted, minimised and addressed to strengthen
    // climate resilience.\n";

    // String content6 = ("\033[3m // Objectives: \033[0m\n");
    // String content7 = "This Guidance aims to support efforts to strengthen the
    // resilience of developing countries to the adverse impacts of climate change
    // and variability. "
    // +
    // "The intended audience includes both government officials at national and
    // sub-national levels, "
    // + "and providers of development co-operation. It may also provide insights to
    // other development partners, "
    // +
    // "including civil society organisations (CSOs) and private-sector actors. The
    // Guidance presents the following:\n";
    // String content8 = ("\033[0;1m- Three high-level aspirations"
    // + " recognised in practice, research and recent international agreements,
    // including on climate change and disaster risk reduction "
    // + "as key considerations for ensuring that action" + " "
    // + "on climate resilience also supports broader sustainable development
    // objectives.\n");
    // String content9 = ("\033[0;1m- Four mechanisms"
    // + " that can facilitate a focus on climate resilience in national and
    // sub-national policy processes. The mechanisms also aim to guide support
    // provided by development co-operation in light of partner countries’ own
    // domestic priorities on climate change and development.\n");

    // String content10 = ("\033[0;1m- Three enablers"
    // + " for action for strengthening climate resilience, and identifying
    // opportunities for government officials and development co-operation to
    // enhance these enablers.\n");

    // String content11 = "Countries are the drivers of domestic efforts to
    // strengthen climate resilience.\nThe mechanisms and enablers therefore take
    // the country perspective "
    // + ("\033[3m(including national, sub-national and community levels)\033[0m\n")
    // + "as the starting point. This is complemented with a focus on the potential
    // role of development co-operation or other actors supporting policy processes
    // in partner countries.";
    // String content12 = "This Guidance is not a planning tool. Instead, it
    // highlights potential actions by different stakeholders across levels of
    // governance through the proposed mechanisms and enablers to strengthen climate
    // resilience. In each of the sections on the mechanisms and enablers, a list of
    // tools and available guidance complements the rationale for the proposed set
    // of actions. The aspirations are treated as cross-cutting considerations, but
    // recommendations for action are not systematically put forward.\n";

    // String content13 = ("\033[3m // Individual tasks to strive
    // towards:\n\033[0m");
    // String content14 = "Here are a some of the latest objectives that you could
    // get going on and start making a difference towards a more resilient & safe
    // future for earth straight away!\n";
    // String content15 = "1. Spread the Word\n"
    // + "\n"
    // + "Encourage your friends, family and co-workers to reduce their carbon
    // pollution. Join a global movement like Count Us In, which aims to inspire 1
    // billion people to take practical steps and challenge their leaders to act
    // more boldly on climate. Organizers of the platform say that if 1 billion
    // people took action, they could reduce as much as 20 per cent of global carbon
    // emissions. Or you could sign up to the UN’s #ActNow campaign on climate
    // change and sustainability and add your voice to this critical global
    // debate.\n"
    // + "\n"
    // + "2. Keep up the political pressure\n" +
    // "\n"
    // + "Lobby local politicians and businesses to support efforts to cut emissions
    // and reduce carbon pollution. #ActNow Speak Up has sections on political
    // pressure and corporate action - and Count Us In also has some handy tips for
    // how to do this. Pick an environmental issue you care about, decide on a
    // specific request for change and then try to arrange a meeting with your local
    // representative. It might seem intimidating but your voice deserves to be
    // heard. If humanity is to succeed in tackling the climate emergency,
    // politicians must be part of the solution. Its up to all of us to keep up with
    // the pressure.\n"
    // + "\n"
    // + "4. Rein in your power use\n"
    // + "\n"
    // + "If you can, switch to a zero-carbon or renewable energy provider. Install
    // solar panels on your roof. Be more efficient: turn your heating down a degree
    // or two, if possible. Switch off appliances and lights when you are not using
    // them and better yet buy the most efficient products in the first place (hint:
    // this will save you money!). Insulate your loft or roof: you will be warmer in
    // the winter, cooler in the summer and save some money too.\n"
    // + "\n"
    // + "5. Tweak your diet\n"
    // + "\n"
    // + "Eat more plant-based meals, your body and the planet will thank you.
    // Today, around 60 per cent of the worlds agricultural land is used for
    // livestock grazing and people in many countries are consuming more
    // animal-sourced food than is healthy. Plant-rich diets can help reduce chronic
    // illnesses, such as heart disease, stroke, diabetes and cancer.\n"
    // + "\n"
    // + "6. Shop local and buy sustainable\n"
    // + "\n"
    // + "To reduce your foods carbon footprint, buy local and seasonal foods. You
    // will be helping small businesses and farms in your area and reducing fossil
    // fuel emissions associated with transport and cold chain storage. Sustainable
    // agriculture uses up to 56 per cent less energy, creates 64 per cent fewer
    // emissions and allows for greater levels of biodiversity than conventional
    // farming. Go one step further and try growing your own fruit, vegetables and
    // herbs. You can plant them in a garden, on a balcony or even on a window sill.
    // Set up a community garden in your neighbourhood to get others involved.\n"
    // + "\n"
    // + "7. Dont waste food\n"
    // + "\n"
    // + "One-third of all food produced is either lost or wasted. According to
    // UNEPs Food Waste Index Report 2021, people globally waste 1 billion tonnes of
    // food each year, which accounts for around 8-10 per cent of global greenhouse
    // gas emissions. Avoid waste by only buying what you need. Take advantage of
    // every edible part of the foods you purchase. Measure portion sizes of rice
    // and other staples before cooking them, store food correctly (use your freezer
    // if you have one), be creative with leftovers, share extras with your friends
    // and neighbours and contribute to a local food-sharing scheme. Make compost
    // out of inedible remnants and use it to fertilize your garden. Composting is
    // one of the best options for managing organic waste while also reducing
    // environmental impacts.\n"
    // + "\n"
    // + "8. Dress (climate) smart\n"
    // + "\n"
    // + "The fashion industry accounts for 8-10 per cent of global carbon
    // emissions, more than all international flights and maritime shipping
    // combined, and fast fashion has created a throwaway culture that sees clothes
    // quickly end up in landfills. But we can change this. Buy fewer new clothes
    // and wear them longer. Seek out sustainable labels and use rental services for
    // special occasions rather than buying new items that will only be worn once.
    // Recycle pre-loved clothes and repair when necessary.\n"
    // + "\n"
    // + "9. Plant trees\n"
    // + "\n"
    // + "Every year approximately 12 million hectares of forest are destroyed and
    // this deforestation, together with agriculture and other land use changes, is
    // responsible for roughly 25 per cent of global greenhouse gas emissions. We
    // can all play a part in reversing this trend by planting trees, either
    // individually or as part of a collective. For example, the
    // Plant-for-the-Planet initiative allows people to sponsor tree-planting around
    // the world."
    // + "Check out this UNEP guide to see what else you can do as part of the UN
    // Decade on Ecosystem Restoration, a global drive to halt the degradation of
    // land and oceans, protect biodiversity, and rebuild ecosystems.\n"
    // + "\n"
    // + "10. Focus on planet-friendly investments\n"
    // + "\n"
    // + "Individuals can also spur change through their savings and investments by
    // choosing financial institutions that do not invest in carbon-polluting
    // industries. #ActNow Speak Up has a section on money and so does Count Us In.
    // This sends a clear signal to the market and already many financial
    // institutions are offering more ethical investments, allowing you to use your
    // money to support causes you believe in and avoid those you don’t. You can ask
    // your financial institution about their responsible banking policies and find
    // out how they rank in independent research.\n";

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

        if (privilleges == "admin") {
            System.out.println("Welcome, administrator!");
        } else {
            System.out.println(" ");
            System.out.println("Welcome, guest!");
        }
    }
}
