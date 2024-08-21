 
package ghops.gprint.reportview;

 
public abstract class AReport<T> {
    String text = ""; 
     
    @Override
    public String toString() {
        return text;
    }
    
}
