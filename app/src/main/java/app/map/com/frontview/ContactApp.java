package app.map.com.frontview;

/**
 * Created by arul on 21/5/17.
 */

public class ContactApp {

    private String name;
    private String ph;
    private String status;

    public void putName(String name)
    {
        this.name=name;
    }

    public void putPh(String ph)
    {
        this.ph=ph;
    }

    public void putStatus(String status)
    {
        this.status=status;

    }

    public String getName()
    {
         return name;

    }

    public String getPh()
    {
      return ph;

    }

    public String getStatus()
    {
       return status;

    }



}
