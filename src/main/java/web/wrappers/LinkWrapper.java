package web.wrappers;

public class LinkWrapper {

    private String url;

    public LinkWrapper(String url) {
        this.url = url;
    }

    public LinkWrapper() {
    }

    @Override
    public String toString() {
        return "LinkWrapper{" +
                "url='" + url + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }
}
