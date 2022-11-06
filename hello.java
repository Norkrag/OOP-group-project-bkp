class HelloWorld {
    static Content content = new Content();

    public static void main(String[] args) {
        System.out.println(content.content);
    }

}

class Content {
    String content = "This is some environmental content stuff";
}
