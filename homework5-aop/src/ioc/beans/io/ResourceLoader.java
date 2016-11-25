package ioc.beans.io;


public class ResourceLoader {

    public Resource getResource(String location){
        return new FileResource(location);
    }
}
