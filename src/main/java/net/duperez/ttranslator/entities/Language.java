package net.duperez.ttranslator.entities;

public class Language {

    String name;

    String smName;

    public Language(String name, String smName) {
        this.name = name;
        this.smName = smName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmName() {
        return smName;
    }

    public void setSmName(String smName) {
        this.smName = smName;
    }
}
