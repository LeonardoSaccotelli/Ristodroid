package model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import persistence.RistodroidDBSchema;

public class Review {
    private String id;
    private int score;
    private String text;
    private Date reviewData;
    private int dish;

    public int getDish() {
        return dish;
    }

    public void setDish(int dish) {
        this.dish = dish;
    }

    public Review(int score, String text, int dish) {
        this.id = UUID.randomUUID().toString();
        this.reviewData = new Date();
        this.score = score;
        this.text = text;
        this.dish = dish;
    }

    public Review(String id,int score, String text, Date date, int dish) {
        this.id = id;
        this.score = score;
        this.text = text;
        this.reviewData = date;
        this.dish = dish;
    }

    public String getId() {
        return id;
    }

    public Date getReviewData() {
        return reviewData;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static String averageScore (List<Review> reviews){
        String average = "0";
        if(reviews.size()!=0) {
            double sum = 0;
            for (int i = 0; i < reviews.size(); i++) {
                sum = sum + reviews.get(i).getScore();
            }
            average = Double.toString(Math.round(sum / reviews.size()));
        }
        return average;
    }

    public static void insertIntoReviewable(SQLiteDatabase db, Review newReview) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RistodroidDBSchema.ReviewTable.Cols.ID, newReview.getId());
        contentValues.put(RistodroidDBSchema.ReviewTable.Cols.SCORE, newReview.getScore());
        contentValues.put(RistodroidDBSchema.ReviewTable.Cols.TEXT, newReview.getText());
        contentValues.put(RistodroidDBSchema.ReviewTable.Cols.DATE, newReview.getReviewData().toString());
        contentValues.put(RistodroidDBSchema.ReviewTable.Cols.DISH, newReview.getDish());
        db.insertWithOnConflict(RistodroidDBSchema.ReviewTable.NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }



}
