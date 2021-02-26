package domain.database;

public enum DatabaseVendor {

    POSTGRESQL("postgres"),
    MYSQL("mysql");

    private final String imageName;

    DatabaseVendor(final String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return this.imageName;
    }
}
