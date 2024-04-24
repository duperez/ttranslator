package net.duperez.ttranslator.objects.common;

public class Language {

    String name;

    String isoName;

    public Language(String name, String isoName) {
        this.name = name;
        this.isoName = isoName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }
}
