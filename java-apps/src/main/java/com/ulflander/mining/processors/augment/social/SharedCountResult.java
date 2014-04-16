package com.ulflander.mining.processors.augment.social;

import com.google.gson.annotations.SerializedName;

/**
 * POJO for sharedcount.com JSON results.
 *
 * Created by Ulflander on 4/12/14.
 */
public class SharedCountResult {

    /**
     * StumbleUpon.
     */
    @SerializedName("StumbleUpon")
    private int stumbleUpon;

    /**
     * Reddit.
     */
    @SerializedName("Reddit")
    private int reddit;

    /**
     * Delicious.
     */
    @SerializedName("Delicious")
    private int delicious;

    /**
     * GooglePlusOne.
     */
    @SerializedName("GooglePlusOne")
    private int googlePlusOne;

    /**
     * Twitter.
     */
    @SerializedName("Twitter")
    private int twitter;

    /**
     * Pinterest.
     */
    @SerializedName("Pinterest")
    private int pinterest;

    /**
     * LinkedIn.
     */
    @SerializedName("LinkedIn")
    private int linkedIn;

    /**
     * Facebook.
     */
    @SerializedName("Facebook")
    private Facebook facebook;


    /**
     * Facebook results.
     */
    public static class Facebook {

        /**
         * Comments box count.
         */
        @SerializedName("commentsbox_count")
        private int commentsBoxCount;

        /**
         * Click count.
         */
        @SerializedName("click_count")
        private int clickCount;

        /**
         * Total.
         */
        @SerializedName("total_count")
        private int totalCount;

        /**
         * Comment count.
         */
        @SerializedName("comment_count")
        private int commentCount;

        /**
         * Like count.
         */
        @SerializedName("like_count")
        private int likeCount;

        /**
         * Share count.
         */
        @SerializedName("share_count")
        private int shareCount;

        /**
         * Get comments box count.
         *
         * @return Comments box count
         */
        public final int getCommentsBoxCount() {
            return commentsBoxCount;
        }

        /**
         * Set comments box count.
         *
         * @param o Comments box count
         */
        public final void setCommentsBoxCount(final int o) {
            this.commentsBoxCount = o;
        }

        /**
         * Get click count.
         *
         * @return Click count
         */
        public final int getClickCount() {
            return clickCount;
        }

        /**
         * Set click count.
         *
         * @param o Click count
         */
        public final void setClickCount(final int o) {
            this.clickCount = o;
        }

        /**
         * Get total count.
         *
         * @return Total count
         */
        public final int getTotalCount() {
            return totalCount;
        }

        /**
         * Set total count.
         *
         * @param o Total count
         */
        public final void setTotalCount(final int o) {
            this.totalCount = o;
        }

        /**
         * Get comment count.
         *
         * @return Comment count
         */
        public final int getCommentCount() {
            return commentCount;
        }

        /**
         * Set comment count.
         *
         * @param o Comment count
         */
        public final void setCommentCount(final int o) {
            this.commentCount = o;
        }

        /**
         * Get like count.
         *
         * @return Like count
         */
        public final int getLikeCount() {
            return likeCount;
        }

        /**
         * Set like count.
         *
         * @param o Like count
         */
        public final void setLikeCount(final int o) {
            this.likeCount = o;
        }

        /**
         * Get share count.
         *
         * @return Share count
         */
        public final int getShareCount() {
            return shareCount;
        }

        /**
         * Set share count.
         *
         * @param o Share count
         */
        public final void setShareCount(final int o) {
            this.shareCount = o;
        }


    }



    /**
     * Get stumble upon.
     *
     * @return Stumble upon
     */
    public final int getStumbleUpon() {
        return stumbleUpon;
    }

    /**
     * Set stumble upon.
     *
     * @param o Stumble upon
     */
    public final void setStumbleUpon(final int o) {
        this.stumbleUpon = o;
    }

    /**
     * Get reddit.
     *
     * @return Reddit
     */
    public final int getReddit() {
        return reddit;
    }

    /**
     * Set reddit.
     *
     * @param o Reddit
     */
    public final void setReddit(final int o) {
        this.reddit = o;
    }

    /**
     * Get delicious.
     *
     * @return Delicious
     */
    public final int getDelicious() {
        return delicious;
    }

    /**
     * Set delicious.
     *
     * @param o Delicious
     */
    public final void setDelicious(final int o) {
        this.delicious = o;
    }

    /**
     * Get google plus one.
     *
     * @return Google plus one
     */
    public final int getGooglePlusOne() {
        return googlePlusOne;
    }

    /**
     * Set google plus one.
     *
     * @param o Google plus one
     */
    public final void setGooglePlusOne(final int o) {
        this.googlePlusOne = o;
    }

    /**
     * Get twitter.
     *
     * @return Twitter
     */
    public final int getTwitter() {
        return twitter;
    }

    /**
     * Set twitter.
     *
     * @param o Twitter
     */
    public final void setTwitter(final int o) {
        this.twitter = o;
    }

    /**
     * Get pinterest.
     *
     * @return Pinterest
     */
    public final int getPinterest() {
        return pinterest;
    }

    /**
     * Set pinterest.
     *
     * @param o Pinterest
     */
    public final void setPinterest(final int o) {
        this.pinterest = o;
    }

    /**
     * Get linked in.
     *
     * @return Linked in
     */
    public final int getLinkedIn() {
        return linkedIn;
    }

    /**
     * Set linked in.
     *
     * @param o Linked in
     */
    public final void setLinkedIn(final int o) {
        this.linkedIn = o;
    }

    /**
     * Get facebook.
     *
     * @return Facebook
     */
    public final Facebook getFacebook() {
        return facebook;
    }

    /**
     * Set facebook.
     *
     * @param o Facebook
     */
    public final void setFacebook(final Facebook o) {
        this.facebook = o;
    }


}
