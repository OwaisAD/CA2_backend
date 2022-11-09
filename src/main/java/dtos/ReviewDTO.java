package dtos;

// from ny

public class ReviewDTO {
    private String summary_short;
    private String suggested_link_text;
    private String url;

    private String review_reference;


    public ReviewDTO(String summary_short, String suggested_link_text, String url) {
        this.summary_short = summary_short;
        this.suggested_link_text = suggested_link_text;
        this.url = url;
    }

    public String getReviewReference() {
        return review_reference;
    }

    public void setReviewReference(String reviewReference) {
        this.review_reference = reviewReference;
    }
}
