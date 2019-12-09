package domain;

public enum DatabaseVendor {

    POSTGRESQL("postgres"),
    MYSQL("mysql");

    private String imageName;

    DatabaseVendor(final String imageName) {

        this.imageName = imageName;
    }

    public String getImageName() {

        return this.imageName;
    }
}
