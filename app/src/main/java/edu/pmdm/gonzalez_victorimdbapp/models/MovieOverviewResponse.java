package edu.pmdm.gonzalez_victorimdbapp.models;

public class MovieOverviewResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        private TitleText titleText;
        private PrimaryImage primaryImage;
        private ReleaseYear releaseYear;
        private Plot plot;

        public TitleText getTitleText() {
            return titleText;
        }

        public PrimaryImage getPrimaryImage() {
            return primaryImage;
        }

        public ReleaseYear getReleaseYear() {
            return releaseYear;
        }

        public Plot getPlot() {
            return plot;
        }
    }

    public static class TitleText {
        private String text;

        public String getText() {
            return text;
        }
    }

    public static class PrimaryImage {
        private String url;

        public String getUrl() {
            return url;
        }
    }

    public static class ReleaseYear {
        private int year;

        public int getYear() {
            return year;
        }
    }

    public static class Plot {
        private String plainText;

        public String getPlainText() {
            return plainText;
        }
    }
}
