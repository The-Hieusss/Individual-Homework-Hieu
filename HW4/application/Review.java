package application;

/**
 * The Review class represents a review (or comment) on an answer.
 * Each review includes an auto-generated review ID, the answer ID being reviewed,
 * the name of the reviewer (reviewFrom), and the review text.
 */
public class Review {
    private int rId;
    private int aId;
    private String reviewFrom;
    private String reviewText;

    public Review(int rId, int aId, String reviewFrom, String reviewText) {
        if (reviewFrom == null || reviewFrom.trim().isEmpty()) {
            throw new IllegalArgumentException("ReviewFrom cannot be empty.");
        }
        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new IllegalArgumentException("ReviewText cannot be empty.");
        }
        this.rId = rId;
        this.aId = aId;
        this.reviewFrom = reviewFrom;
        this.reviewText = reviewText;
    }

    public int getRId() {
        return rId;
    }

    public int getAId() {
        return aId;
    }

    public String getReviewFrom() {
        return reviewFrom;
    }

    public String getReviewText() {
        return reviewText;
    }
}
