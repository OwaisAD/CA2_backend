package dtos;

// from ny

public class ReviewDTO {
    private String summary_short;
    private String suggested_link_text;
    private String url;

    private String reviewReference;


    public ReviewDTO(String summary_short, String suggested_link_text, String url) {
        this.summary_short = summary_short;
        this.suggested_link_text = suggested_link_text;
        this.url = url;
    }

    public String getSummary_short() {
        return summary_short;
    }

    public void setSummary_short(String summary_short) {
        this.summary_short = summary_short;
    }

    public String getSuggested_link_text() {
        return suggested_link_text;
    }

    public void setSuggested_link_text(String suggested_link_text) {
        this.suggested_link_text = suggested_link_text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReviewReference() {
        return reviewReference;
    }

    public void setReviewReference(String reviewReference) {
        this.reviewReference = reviewReference;
    }
}
