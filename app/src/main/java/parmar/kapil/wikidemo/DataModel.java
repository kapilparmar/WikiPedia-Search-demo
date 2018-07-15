package parmar.kapil.wikidemo;

/**
 * Created by kapil on 7/15/2018.
 */

public class DataModel {

    private String  name, Description, image;

    public DataModel(String name, String description, String image) {
        this.name = name;
        Description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
